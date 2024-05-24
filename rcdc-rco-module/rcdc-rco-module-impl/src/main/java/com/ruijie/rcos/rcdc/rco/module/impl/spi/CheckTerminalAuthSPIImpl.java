package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageDownloadStateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageDownloadStateService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.enums.TerminalWhiteTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.message.MessageUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TerminalCheckAuthDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.TerminalAuthResultEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 检查CT3XXX系列的终端授权
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月09日
 *
 * @author xwx
 */
@DispatcherImplemetion(ShineAction.CHECK_AUTH)
public class CheckTerminalAuthSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckTerminalAuthSPIImpl.class);

    private static final String AUTH_RESULT = "authResult";


    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private ImageDownloadStateService imageDownloadStateService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cant be null");

        LOGGER.info("终端[{}]开始进行补偿授权校验", request.getTerminalId());
        TerminalCheckAuthDTO terminalCheckAuthDTO = JSONObject.parseObject(request.getData(), TerminalCheckAuthDTO.class);
        if (terminalCheckAuthDTO == null) {
            responseError(request, null);
            return;
        }
        switch (terminalCheckAuthDTO.getAuthMode()) {
            case VDI:
                responseVDIAuthResult(request);
                break;
            case VOI:
                responseVOIAuthResult(request);
                break;
            default:
                responseError(request, terminalCheckAuthDTO.getAuthMode());
        }
    }

    private void responseError(CbbDispatcherRequest request, CbbTerminalPlatformEnums authMode) {
        LOGGER.error("终端[{}]上报的工作模式为[{}]", request.getTerminalId(), authMode);

        responseToShine(request, TerminalAuthResultEnums.ERROR);
    }

    private void responseVOIAuthResult(CbbDispatcherRequest request) {

        Boolean isAuthSuccess = true;
        try {
            //CT3X中免授权的应该过滤不做授权
            CbbTerminalBasicInfoDTO terminalInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            if (!TerminalWhiteTypeEnum.isInTerminalWhiteList(terminalInfo.getProductType())) {
                isAuthSuccess = cbbTerminalLicenseMgmtAPI.checkEnableAuthTerminal(request.getTerminalId(), CbbTerminalPlatformEnums.VOI);
            } else {
                cbbTerminalOperatorAPI.updateAuthState(request.getTerminalId(), Boolean.TRUE);
            }
            //TCI需要将授权模式更改为VOI，方便终端列表查询以及授权统计
            cbbTerminalOperatorAPI.updateTerminalPlatform(request.getTerminalId(), CbbTerminalPlatformEnums.VOI);
        } catch (BusinessException e) {
            LOGGER.error("终端[{}]授权失败", request.getTerminalId(), e);
            isAuthSuccess = false;
        }
        responseToShine(request, isAuthSuccess ? TerminalAuthResultEnums.SUCCESS : TerminalAuthResultEnums.FAIL);
    }

    private void responseVDIAuthResult(CbbDispatcherRequest request) {
        //VDI的要回收授权
        try {
            cbbTerminalLicenseMgmtAPI.cancelTerminalAuth(request.getTerminalId());
            cbbTerminalOperatorAPI.updateTerminalPlatform(request.getTerminalId(), CbbTerminalPlatformEnums.VDI);
            //清除旧的云桌面绑定记录
            handleTCIBindInfo(request.getTerminalId());
            //清楚VDI终端的历史数据，TCI切换到VDI模式
            handleVDITerminalHistoryData(request.getTerminalId());
        } catch (Exception e) {
            LOGGER.error("终端[{}]回收授权失败", request.getTerminalId(), e);
        }
        responseToShine(request, TerminalAuthResultEnums.SUCCESS);
    }

    private void handleTCIBindInfo(String terminalId) {
        UserTerminalEntity userTerminalEntity = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (userTerminalEntity == null) {
            LOGGER.error("终端不存在,terminalId = [{}]，无需解除绑定信息", terminalId);
            return;
        }
        //有绑定过桌面，需要清除
        if (userTerminalEntity.getBindDeskId() != null) {
            terminalService.initialTerminalCleanData(userTerminalEntity);
        }
    }

    private void responseToShine(CbbDispatcherRequest request, TerminalAuthResultEnums authResult) {
        JSONObject result = new JSONObject();
        result.put(AUTH_RESULT, authResult);
        CbbResponseShineMessage cbbResponseShineMessage = MessageUtils.buildResponseMessage(request, result);
        LOGGER.info("响应终端[{}]授权结果[{}]", request.getTerminalId(), JSON.toJSONString(request));
        messageHandlerAPI.response(cbbResponseShineMessage);
    }

    /**
     * 清楚VDI终端的历史数据，TCI切换到VDI模式
     *
     * @param terminalId
     */
    private void handleVDITerminalHistoryData(String terminalId) {
        //有可能是从TCI终端刷机成VDI使用的
        ImageDownloadStateDTO imageDownloadStateDTO = imageDownloadStateService.getByTerminalId(terminalId);
        if (imageDownloadStateDTO != null) {
            imageDownloadStateService.deleteByTerminalId(terminalId);
        }
    }
}
