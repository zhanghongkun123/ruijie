package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.spi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import com.ruijie.rcos.rcdc.rco.module.def.api.StatisticsTerminalAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.BusinessAction;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalEventNoticeSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbNoticeRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 设备上线记录。此处的设备包含IDV，VDI，APP和PC
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author zhiweiHong
 */
@DispatcherImplemetion(BusinessAction.ONLINE)
public class TerminalOnlineSituationRecordSPIImpl implements CbbTerminalEventNoticeSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOnlineSituationRecordSPIImpl.class);

    @Autowired
    private StatisticsTerminalAPI statisticsTerminalAPI;

    @Override
    public void notify(CbbNoticeRequest request) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getTerminalBasicInfo(), "terminalBaseInfo can not be null");
        String terminalId = request.getTerminalBasicInfo().getTerminalId();
        Assert.hasText(terminalId, "terminal id can not be null");
        CbbTerminalPlatformEnums platform = request.getTerminalBasicInfo().getPlatform();
        Assert.notNull(platform, "platform id can not be null");
        LOGGER.debug("terminalId={},platform={}", terminalId, platform);
        statisticsTerminalAPI.recordTerminalOnlineSituation(terminalId, platform);
    }
}
