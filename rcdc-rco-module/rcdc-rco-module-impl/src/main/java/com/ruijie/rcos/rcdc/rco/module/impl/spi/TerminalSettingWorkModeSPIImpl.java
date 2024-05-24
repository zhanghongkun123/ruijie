package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageDownloadStateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.message.MessageUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageDownloadStateService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TerminalSettingWorkModeRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 更新终端工作模式
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/24
 *
 * @author ypp
 */
@DispatcherImplemetion(ShineAction.SETTING_WORK_MODE)
public class TerminalSettingWorkModeSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalSettingWorkModeSPIImpl.class);

    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private ImageDownloadStateService imageDownloadStateService;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "request must not be null");
        Assert.notNull(cbbDispatcherRequest.getData(), "cbbDispatcherRequest.get must not be null");

        LOGGER.info("收到终端[{}]部署信息[{}]", cbbDispatcherRequest.getTerminalId(), cbbDispatcherRequest.getData());

        try {
            TerminalSettingWorkModeRequest terminalSettingWorkModeRequest =
                    JSON.parseObject(cbbDispatcherRequest.getData(), TerminalSettingWorkModeRequest.class);

            if (terminalSettingWorkModeRequest.getTerminalPlatform() != CbbTerminalPlatformEnums.VDI) {
                return;
            }

            // VDI的要回收授权
            cbbTerminalLicenseMgmtAPI.cancelTerminalAuth(cbbDispatcherRequest.getTerminalId());
            cbbTerminalOperatorAPI.updateTerminalPlatform(cbbDispatcherRequest.getTerminalId(), CbbTerminalPlatformEnums.VDI);

            // 清楚VDI终端的历史数据，IDV切换到VDI模式
            deleteTerminalDownloadImageInfo(cbbDispatcherRequest.getTerminalId());

            CbbResponseShineMessage cbbShineMessageRequest = MessageUtils.buildResponseMessage(cbbDispatcherRequest, new Object());
            LOGGER.info("响应终端[{}]部署信息设置成功", cbbDispatcherRequest.getTerminalId());
            messageHandlerAPI.response(cbbShineMessageRequest);

        } catch (Exception e) {
            LOGGER.error("终端[{}]部署信息设置失败", cbbDispatcherRequest.getTerminalId(), e);

            CbbResponseShineMessage cbbShineMessageRequest = MessageUtils.buildErrorResponseMessage(cbbDispatcherRequest);
            messageHandlerAPI.response(cbbShineMessageRequest);
        }


    }


    /**
     * 清楚VDI终端的历史数据，IDV切换到VDI模式
     *
     * @param terminalId 终端id
     */
    private void deleteTerminalDownloadImageInfo(String terminalId) {
        // 有可能是从idv终端刷机成VDI使用的
        ImageDownloadStateDTO imageDownloadStateDTO = imageDownloadStateService.getByTerminalId(terminalId);
        if (imageDownloadStateDTO != null) {
            imageDownloadStateService.deleteByTerminalId(terminalId);
        }
    }

}
