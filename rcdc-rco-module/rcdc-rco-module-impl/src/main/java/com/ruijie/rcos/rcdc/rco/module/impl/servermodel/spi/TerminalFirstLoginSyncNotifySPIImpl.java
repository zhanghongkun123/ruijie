package com.ruijie.rcos.rcdc.rco.module.impl.servermodel.spi;

import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.TerminalWorkModeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.constants.ServerModelConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBizConfigDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalConnectHandlerSPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: TerminalFirstLoginSyncNotifySPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 *
 * @author wjp
 */
@DispatcherImplemetion(ServerModelConstants.TERMINAL_FIRST_LOGIN_SYNC_NOTIFY)
public class TerminalFirstLoginSyncNotifySPIImpl implements CbbTerminalConnectHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalFirstLoginSyncNotifySPIImpl.class);

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    @Autowired
    private TerminalService terminalService;


    @Override
    public boolean isAllowConnect(CbbShineTerminalBasicInfo terminalBasicInfo) {
        Assert.notNull(terminalBasicInfo, "terminalBasicInfo is null");
        try {
            LOGGER.info("终端首报文SPI收到操作信息<{}>", JSONObject.toJSONString(terminalBasicInfo));
            if (serverModelAPI.isVdiModel()) {
                return true;
            }

            CbbTerminalPlatformEnums platform = terminalBasicInfo.getPlatform();
            if (terminalBasicInfo.getTerminalWorkSupportModeArr() != null) {

                TerminalWorkModeConfigDTO terminalWorkModeConfigDTO = new TerminalWorkModeConfigDTO();
                terminalWorkModeConfigDTO.setTerminalId(terminalBasicInfo.getTerminalId());
                terminalWorkModeConfigDTO.setTerminalWorkSupportModeArr(terminalBasicInfo.getTerminalWorkSupportModeArr());
                terminalWorkModeConfigDTO.setProductType(terminalBasicInfo.getProductType());
                terminalWorkModeConfigDTO.setPlatform(terminalBasicInfo.getPlatform());
                platform = terminalService.getTerminalSupportMode(terminalWorkModeConfigDTO).getTerminalPlatform();

            }

            if (isVDITerminal(platform) && !cbbTerminalConfigAPI.isRjTerminal(terminalBasicInfo.getProductType())) {
                LOGGER.warn("VDI终端误接入，不处理此终端记录");
                return false;
            }

        } catch (Exception e) {
            LOGGER.error("终端首报文SPI收到操作请求记录失败。content = [" + JSONObject.toJSONString(terminalBasicInfo) + "]", e);
            return false;
        }
        return true;
    }

    private boolean isVDITerminal(CbbTerminalPlatformEnums platform) {
        if (platform == CbbTerminalPlatformEnums.VDI || platform == CbbTerminalPlatformEnums.APP) {
            LOGGER.warn("VDI终端误接入，不处理此终端记录");
            return true;
        }
        return false;
    }

    /**
     * 5.3项目办公终端能力策略方式如下：(服务器上的映射规则)
     * 终端能力 终端最终类型
     * IDV/VDI/VOI ---- IDV
     * IDV/VDI ---- IDV
     * IDV/VOI ---- IDV
     * VDI/VOI ---- VDI
     * IDV ---- IDV
     * VDI ---- VDI
     * 3120 ---- VDI(3120表示终端型号)
     */
    @Override
    public CbbTerminalBizConfigDTO notifyTerminalSupport(CbbShineTerminalBasicInfo terminalBasicInfo) {
        Assert.notNull(terminalBasicInfo, "terminalBasicInfo is null");
        TerminalWorkModeConfigDTO terminalWorkModeConfigDTO = new TerminalWorkModeConfigDTO();
        terminalWorkModeConfigDTO.setTerminalId(terminalBasicInfo.getTerminalId());
        terminalWorkModeConfigDTO.setTerminalWorkSupportModeArr(terminalBasicInfo.getTerminalWorkSupportModeArr());
        terminalWorkModeConfigDTO.setProductType(terminalBasicInfo.getProductType());
        terminalWorkModeConfigDTO.setPlatform(terminalBasicInfo.getPlatform());
        return terminalService.getTerminalSupportMode(terminalWorkModeConfigDTO);
    }
}

