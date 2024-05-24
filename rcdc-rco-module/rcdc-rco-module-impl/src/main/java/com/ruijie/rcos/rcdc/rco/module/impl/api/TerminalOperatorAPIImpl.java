package com.ruijie.rcos.rcdc.rco.module.impl.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbNetworkDataPacketDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalOperatorAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.WakeTerminalUtils;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 对终端进行操作的API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年03月24日
 *
 * @author xgx
 */
public class TerminalOperatorAPIImpl implements TerminalOperatorAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOperatorAPIImpl.class);

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private WakeTerminalUtils wakeTerminalUtils;

    @Autowired
    private TerminalService terminalService;


    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;



    @Override
    public CbbNetworkDataPacketDTO buildNetworkDataPacketDTO(String terminalId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId can not be blank");
        return wakeTerminalUtils.buildWakeTerminalNetData(terminalId);
    }

}
