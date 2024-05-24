package com.ruijie.rcos.rcdc.rco.module.impl.spi.mtool.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.SyncUpgradeCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineRequestKeyInformationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.mtool.dto.MtoolAction;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.12
 *
 * @author linhj
 */
@DispatcherImplemetion(MtoolAction.GET_KEY_INFORMATION)
public class ShineRequestKeyInformationSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShineRequestKeyInformationSPIImpl.class);

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private PrinterManageServiceAPI printerManageServiceAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {

        Assert.notNull(request, "request must not null");
        Assert.notNull(request.getTerminalId(), "request.terminalId must not null");

        try {
            // 业务校验
            CbbTerminalBasicInfoDTO basicInfoDTO;
            try {
                basicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            } catch (BusinessException ex) {
                responseMessage(request, SyncUpgradeCode.TERMINAL_NOT_FOUND);
                return;
            }

            UserTerminalEntity userTerminal = userTerminalDAO.findFirstByTerminalId(request.getTerminalId());
            if (userTerminal == null) {
                responseMessage(request, SyncUpgradeCode.TERMINAL_NOT_FOUND);
                return;
            }

            if (userTerminal.getTerminalMode() == null || userTerminal.getTerminalMode() == IdvTerminalModeEnums.UNKNOWN) {
                responseMessage(request, SyncUpgradeCode.TERMINAL_MODE_ERROR);
                return;
            }

            if (userTerminal.getBindDeskId() == null) {
                responseMessage(request, SyncUpgradeCode.TERMINAL_DESKTOP_NOT_FOUND);
                return;
            }

            CbbDeskDTO deskIDVDTO;
            try {
                deskIDVDTO = cbbIDVDeskMgmtAPI.getDeskIDV(userTerminal.getBindDeskId());
            } catch (BusinessException ex) {
                responseMessage(request, SyncUpgradeCode.TERMINAL_DESKTOP_NOT_FOUND);
                return;
            }

            // 返回参数封装
            doPackageKeyInfomation(request, basicInfoDTO, userTerminal, deskIDVDTO);

        } catch (Exception ex) {
            LOGGER.error("查询信息处理异常", ex);
            responseMessage(request, SyncUpgradeCode.OTHER_ERROR);
        }
    }

    // 返回参数封装
    private void doPackageKeyInfomation(CbbDispatcherRequest request, CbbTerminalBasicInfoDTO basicInfoDTO, UserTerminalEntity userTerminal,
                                        CbbDeskDTO cbbDeskDTO) {

        ShineRequestKeyInformationDTO keyInformationDTO = new ShineRequestKeyInformationDTO();

        // 打印机开关配置
        keyInformationDTO.setHasOpenPrinter(Boolean.valueOf(printerManageServiceAPI.getPrinterConfigStatus()));

        // 用户配置
        keyInformationDTO.getUserConfig().setIdvTerminalMode(userTerminal.getTerminalMode());
        keyInformationDTO.getUserConfig().setTerminalGroupId(basicInfoDTO.getGroupId());
        keyInformationDTO.getUserConfig().setUserName(Optional.of(userTerminal).map(UserTerminalEntity::getBindUserName).orElse(""));
        keyInformationDTO.setImageId(cbbDeskDTO.getImageTemplateId());
        keyInformationDTO.setDeskId(userTerminal.getBindDeskId());

        // 通知终端
        responseMessage(request, SyncUpgradeCode.SUCCESS, keyInformationDTO);
    }

    private void responseMessage(CbbDispatcherRequest request, int code) {
        responseMessage(request, code, StringUtils.EMPTY);
    }

    private void responseMessage(CbbDispatcherRequest request, int code, Object content) {
        try {
            shineMessageHandler.responseContent(request, code, content);
            LOGGER.info("应答报文给终端 [{}] 成功，应答状态码：{},应答内容：{}", request.getTerminalId(), code, JSON.toJSONString(content));
        } catch (Exception ex) {
            LOGGER.error("应答报文给终端 [" + request.getTerminalId() + "] 失败，应答状态码：" + code + "，应答内容：" + content, ex);
        }
    }
}
