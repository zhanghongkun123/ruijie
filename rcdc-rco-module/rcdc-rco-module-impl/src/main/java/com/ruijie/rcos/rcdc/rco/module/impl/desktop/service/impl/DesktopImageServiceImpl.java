package com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.common.utils.StateMachineUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopImageService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-04-10
 *
 * @author chen zj
 */
@Service
public class DesktopImageServiceImpl implements DesktopImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopImageServiceImpl.class);

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private StateMachineUtils stateMachineUtils;

    @Autowired
    private CbbVDIDeskOperateAPI cbbVDIDeskOperateAPI;

    @Override
    public void updateDesktopImage(CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO) throws BusinessException {
        Assert.notNull(cbbDesktopImageUpdateDTO, "cbbDesktopImageUpdateDTO must not be null");

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(cbbDesktopImageUpdateDTO.getDesktopId());
        switch (cbbDeskDTO.getDeskType()) {
            case IDV:
                // IDV桌面修改数据看后通知Shine
                handleUpdateIDVDesktopImage(cbbDesktopImageUpdateDTO.getDesktopId(), cbbDesktopImageUpdateDTO.getImageId());
                break;
            case VDI:
                if (cbbDesktopImageUpdateDTO.getImageId().equals(cbbDeskDTO.getImageTemplateId())) {
                    LOGGER.info("云桌面[{}]关联镜像[{}]未发生变更，无需更新", cbbDesktopImageUpdateDTO.getDesktopId(), cbbDeskDTO.getImageTemplateId());
                    // 变回原来镜像，清空willApplyImageId
                    if (cbbDeskDTO.getWillApplyImageId() != null) {
                        cbbDeskMgmtAPI.clearWillApplyImageId(cbbDesktopImageUpdateDTO.getDesktopId());
                    }
                    break;
                }
                // 目前支持其他中间态，保存待替换镜像模板，等关机后或者中间态结束后执行以下的方法
                if (!Boolean.TRUE.equals(cbbDesktopImageUpdateDTO.getEnableVdiForceApply())) {
                    if (Boolean.TRUE.equals(
                            cbbDeskMgmtAPI.markWillApplyImageInfo(cbbDesktopImageUpdateDTO.getDesktopId(), cbbDesktopImageUpdateDTO.getImageId()))) {
                        LOGGER.info("保存更新云桌面镜像版本信息：[{}]", JSON.toJSONString(cbbDesktopImageUpdateDTO));
                        break;
                    }
                }

                // VDI桌面变更镜像模板
                UUID desktopId = cbbDesktopImageUpdateDTO.getDesktopId();
                stateMachineUtils.forceExecuteStateMachine(desktopId.toString(),
                    () -> cbbVDIDeskMgmtAPI.updateDeskVDIImageTemplate(cbbDesktopImageUpdateDTO));
                break;
            default:
                break;
        }
    }

    private void handleUpdateIDVDesktopImage(UUID desktopId, UUID imageId) throws BusinessException {
        try {
            LOGGER.info("更新IDV云桌面[{}]关联镜像[{}]", desktopId, imageId);

            validUpdateIdvImage(desktopId, imageId);

            cbbDeskMgmtAPI.updateImageTemplateId(desktopId, imageId);
            // 终端
            sendMessage(desktopId);
        } catch (BusinessException e) {
            LOGGER.error("变更IDV云桌面[{" + desktopId + "}]镜像出现异常：", e);
            throw e;
        }
    }

    private void validUpdateIdvImage(UUID desktopId, UUID newImageId) throws BusinessException {
        CbbDeskDTO deskInfo = cbbDeskMgmtAPI.getDeskById(desktopId);
        UUID oldImageId = deskInfo.getImageTemplateId();
        List<CbbImageDiskInfoDTO> oldImageDiskInfoList = cbbImageTemplateMgmtAPI.getPublishedImageDiskInfoList(oldImageId);
        List<CbbImageDiskInfoDTO> newImageDiskInfoList = cbbImageTemplateMgmtAPI.getPublishedImageDiskInfoList(newImageId);
        boolean existOldDataDisk = oldImageDiskInfoList.stream().anyMatch(disk -> disk.getImageDiskType() == CbbImageDiskType.DATA);
        boolean existNewDataDisk = newImageDiskInfoList.stream().anyMatch(disk -> disk.getImageDiskType() == CbbImageDiskType.DATA);
        // 原先不存在镜像D盘，但是新的镜像有镜像D盘，此时可能会造成盘符冲突。因此，不允许更换带有镜像D盘的镜像：
        if (!existOldDataDisk && existNewDataDisk) {
            throw new BusinessException(BusinessKey.RCDC_RCO_NOT_ALLOW_UPDATE_DATA_DISK_WITH_OLD_NOT_EXIST);
        }
    }

    private void sendMessage(UUID desktopId) throws BusinessException {
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(desktopId);
        if (userDesktopEntity == null) {
            LOGGER.error("未找到云桌面[{}]对应用户信息，无法通知终端变更云桌面镜像成功", desktopId);
            return;
        }

        String terminalId = userDesktopEntity.getTerminalId();
        if (StringUtils.isBlank(terminalId)) {
            LOGGER.error("未找到云桌面[{}]对应的终端信息，无法通知终端变更云桌面镜像成功", desktopId);
            return;
        }

        CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        if (CbbTerminalStateEnums.ONLINE != cbbTerminalBasicInfoDTO.getState()) {
            LOGGER.info("当前终端[{}]不在线, 无需通知", terminalId);
            return;
        }

        CbbShineMessageRequest request = CbbShineMessageRequest.create(ShineAction.NOTIFY_IMAGE_CHANGED, terminalId);
        request.setContent(new JSONObject());
        LOGGER.info("通知终端[{}]IDV云桌面更新镜像成功", terminalId);
        cbbTranspondMessageHandlerAPI.asyncRequest(request, new CbbTerminalCallback() {
            @Override
            public void success(String terminalId, CbbShineMessageResponse msg) {
                Assert.notNull(terminalId, "terminalId cannot be null!");
                Assert.notNull(msg, "msg cannot be null!");

                LOGGER.info("通知终端变更云桌面镜像成功，terminalId[{}]，信息[{}]", terminalId, msg.toString());
            }

            @Override
            public void timeout(String terminalId) {
                Assert.notNull(terminalId, "terminalId cannot be null!");

                LOGGER.error("通知终端变更云桌面镜像超时，terminalId[{}], 终端重启后会再次检测桌面关联镜像", terminalId);
            }
        });
    }

}
