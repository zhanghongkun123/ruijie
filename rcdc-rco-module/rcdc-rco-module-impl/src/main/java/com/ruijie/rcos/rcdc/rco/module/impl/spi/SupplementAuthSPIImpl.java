package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageDownloadStateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageDownloadStateService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.enums.TerminalSupportVdiAndIdvEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.TerminalWorkModeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.message.MessageUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.SupplementAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.response.SupplementAuthResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBizConfigDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.TerminalAuthResultEnums;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 7800终端补充授权
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月15日
 *
 * @author ypp
 */

@DispatcherImplemetion(ShineAction.SUPPLEMENT_AUTH)
public class SupplementAuthSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplementAuthSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private ImageDownloadStateService imageDownloadStateService;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getData(), "cbbDispatcherRequest.get must not be null");

        LOGGER.info("收到终端[{}]补充授权信息[{}]", request.getTerminalId(), request.getData());

        try {
            SupplementAuthDTO supplementAuthDTO = JSON.parseObject(request.getData(), SupplementAuthDTO.class);


            // 未选择工作模式，直接跳过
            if (Objects.isNull(supplementAuthDTO.getWorkMode())) {
                LOGGER.info("终端[{}]补充授权信息：未部署或者部署模式为VDI，无需补充授权", request.getTerminalId());
                responseToShine(request, Boolean.TRUE);
                return;
            }

            if (supplementAuthDTO.getWorkMode() == CbbTerminalPlatformEnums.VDI) {
                cleanIdvTerminalInfo(request);

                responseToShine(request, Boolean.TRUE);
                return;
            }

            // 终端在白名单中，跳过授权
            if (checkTerminalIsInWhiteList(request, supplementAuthDTO)) {
                LOGGER.info("终端[{}]在免授权白名单中，无需补充授权", request.getTerminalId());
                responseToShine(request, Boolean.TRUE);
                return;
            }

            TerminalWorkModeConfigDTO terminalWorkModeConfigDTO = new TerminalWorkModeConfigDTO();
            terminalWorkModeConfigDTO.setTerminalId(request.getTerminalId());
            terminalWorkModeConfigDTO.setProductType(supplementAuthDTO.getProductType());
            terminalWorkModeConfigDTO.setPlatform(supplementAuthDTO.getPlatform());
            terminalWorkModeConfigDTO.setTerminalWorkSupportModeArr(supplementAuthDTO.getTerminalWorkSupportModeArr());
            CbbTerminalBizConfigDTO cbbTerminalBizConfigDTO = terminalService.getTerminalSupportMode(terminalWorkModeConfigDTO);

            LOGGER.info("终端[{}]进行补充授权", terminalWorkModeConfigDTO.getTerminalId());

            boolean hasSuc = cbbTerminalLicenseMgmtAPI.checkEnableAuthTerminal(request.getTerminalId(), cbbTerminalBizConfigDTO.getAuthMode());
            responseToShine(request, hasSuc);

        } catch (Exception e) {
            // 终端记录不存在，跳过
            if (e instanceof BusinessException &&
                    BusinessKey.RCDC_USER_TERMINAL_TERMINAL_NOT_EXIST.equals(((BusinessException) e).getKey())) {
                LOGGER.error("终端[{}]无需补充授权, 终端记录不存在", request.getTerminalId());
                responseToShine(request, Boolean.TRUE);
                return;
            }
            LOGGER.error("终端[{}]补充授权信息异常", request.getTerminalId(), e);
            responseToShine(request, Boolean.FALSE);
        }
    }

    private boolean checkTerminalIsInWhiteList(CbbDispatcherRequest request, SupplementAuthDTO supplementAuthDTO) throws BusinessException {
        ViewTerminalEntity viewTerminalEntity = terminalService.getViewByTerminalId(request.getTerminalId());
        // 白名单终端跳过授权
        CbbShineTerminalBasicInfo basicInfo = new CbbShineTerminalBasicInfo();
        basicInfo.setProductType(supplementAuthDTO.getProductType());
        basicInfo.setTerminalId(request.getTerminalId());
        basicInfo.setAllDiskInfo(viewTerminalEntity.getAllDiskInfo());
        basicInfo.setPlatform(viewTerminalEntity.getPlatform());
        basicInfo.setSerialNumber(viewTerminalEntity.getSerialNumber());

        if (!Objects.isNull(supplementAuthDTO.getProductType())) {
            return cbbTerminalConfigAPI.isTerminalInWhiteList(supplementAuthDTO.getProductType(),basicInfo);
        }
        return false;
    }

    private void cleanIdvTerminalInfo(CbbDispatcherRequest request) {
        // 1.清除占用idv授权，2. 删除idv桌面信息
        try {
            // 删除授权
            cbbTerminalLicenseMgmtAPI.cancelTerminalAuth(request.getTerminalId());

            // 删除桌面记录
            UserTerminalEntity terminalEntity = userTerminalDAO.findFirstByTerminalId(request.getTerminalId());
            if (terminalEntity != null && terminalEntity.getBindDeskId() != null) {
                LOGGER.info("终端[{}]清除残留的桌面数据及授权信息，无需补充授权", request.getTerminalId());
                terminalService.initialTerminalCleanData(terminalEntity);
            }

            // 删除残留下载记录
            ImageDownloadStateDTO imageDownloadStateDTO = imageDownloadStateService.getByTerminalId(request.getTerminalId());
            if (imageDownloadStateDTO != null) {
                imageDownloadStateService.deleteByTerminalId(request.getTerminalId());
            }
        } catch (Exception e) {
            LOGGER.error("处理vdi残留idv桌面及授权数据发生异常，terminalId:{}", request.getTerminalId(), e);
        }
    }

    private void responseToShine(CbbDispatcherRequest request, Boolean hasAuth) {
        SupplementAuthResponse supplementAuthResponse = new SupplementAuthResponse();
        supplementAuthResponse.setTerminalAuthState(TerminalAuthResultEnums.FAIL);
        if (Boolean.TRUE.equals(hasAuth)) {
            supplementAuthResponse.setTerminalAuthState(TerminalAuthResultEnums.SUCCESS);
        }
        CbbResponseShineMessage cbbShineMessageRequest = MessageUtils.buildResponseMessage(request, supplementAuthResponse);
        messageHandlerAPI.response(cbbShineMessageRequest);
        LOGGER.info("响应终端[{}][{}]请求补充授权结果[{}]", request.getTerminalId(), request.getData(), JSON.toJSONString(supplementAuthResponse));
    }
}
