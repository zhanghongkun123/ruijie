package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import java.util.*;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.SaveVmsMappingRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 处理创建桌面消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月8日
 *
 * @author yinfeng
 */
@Service
public class CreateDeskHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDeskHandler.class);

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private CbbVmsMappingAPI cbbVmsMappingAPI;

    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private OneAgentUpgradeMgmtAPI oneAgentUpgradeMgmtAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.CREATE_DESK == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) throws Exception {
        if (!request.getIsSuccess()) {
            LOGGER.error("收到创建桌面[{}] 失败消息[{}]，删除 user desktop 数据", request.getDeskId(),
                    request.getErrorMsg());
            // 删除 UserDesktopEntity 数据
            userDesktopDAO.deleteByCbbDesktopId(request.getDeskId());
            return;
        }
        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(request.getDeskId());
        if (deskDTO.getDesktopPoolType() != DesktopPoolType.COMMON) {
            List<CbbDeskInfoDTO> deskInfoDTOList = new ArrayList<>();
            CbbDeskInfoDTO cbbDeskInfoDTO = new CbbDeskInfoDTO();
            BeanUtils.copyProperties(deskDTO, cbbDeskInfoDTO);
            deskInfoDTOList.add(cbbDeskInfoDTO);
            CbbDesktopPoolDTO desktopPoolDetail = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(deskDTO.getDesktopPoolId());
            List<UUID> deskIdList = deskInfoDTOList.stream().map(CbbDeskInfoDTO::getDeskId).collect(Collectors.toList());
            if (deskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                cbbDesktopPoolMgmtAPI.notifyOaUpdateMaxSession(desktopPoolDetail, deskIdList);
            }
            cbbDesktopPoolMgmtAPI.pushSessionIdleTimeConfig(desktopPoolDetail, deskIdList);
        }
        // 更新工作模式
        computerBusinessService.updateWorkModel(request.getDeskId(), ComputerWorkModelEnum.CLOUD_DESK);

        // 根据pc终端状态更新桌面状态
        ComputerEntity computerEntity = computerBusinessService.getComputerById(request.getDeskId());
        if (computerEntity != null) {
            CbbCloudDeskState cbbCloudDeskState = computerEntity.getState() == ComputerStateEnum.ONLINE ?
                    CbbCloudDeskState.RUNNING : CbbCloudDeskState.OFF_LINE;
            cbbIDVDeskMgmtAPI.updateIDVDeskStateByDeskId(computerEntity.getId(), cbbCloudDeskState);

            //第三方桌面通知升级oa
            oneAgentUpgradeMgmtAPI.notifyDeskTopStartUpgrade(Arrays.asList(computerEntity.getId()));
        }
        // 保存vms信息
        saveVmsMapping(request.getDeskId());

        // 通知UWS桌面新增
        uwsDockingAPI.notifyDesktopAdd(request.getDeskId());

        //存在rccm纳管，需要处理用户集群关系缓存
        pushUser(request);
        LOGGER.info("收到创建桌面消息, 桌面id[{}]", request.getDeskId());
    }

    private void pushUser(CbbDeskOperateNotifyRequest request) {
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(request.getDeskId());
        if (userDesktopEntity != null && userDesktopEntity.getUserId() != null) {
            rccmManageService.pushUserByUserIdList(Collections.singletonList(userDesktopEntity.getUserId()));
        }
    }

    private void saveVmsMapping(UUID deskId) {
        SaveVmsMappingRequest saveRequest = new SaveVmsMappingRequest();
        saveRequest.setBusinessId(deskId);
        saveRequest.setBusinessType(ProductTypeEnum.RCO);
        cbbVmsMappingAPI.saveVmsMappingInfo(saveRequest);
    }
}
