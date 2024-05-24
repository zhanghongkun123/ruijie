package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.BusinessAction;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.terminal.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalEventNoticeSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbNoticeRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/12 18:23
 *
 * @author ketb
 */
@DispatcherImplemetion(BusinessAction.OFFLINE)
public class ComputerOfflineSPIImpl implements CbbTerminalEventNoticeSPI {
    public static final Logger LOGGER = LoggerFactory.getLogger(ComputerOfflineSPIImpl.class);

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Override
    public void notify(CbbNoticeRequest request) {
        Assert.notNull(request, "request can not be null.");

        // PC纳管业务中，terminalId即为mac，通信报文中的terminalId不是指表记录id
        String terminalId = request.getTerminalBasicInfo().getTerminalId();
        Assert.hasText(terminalId, "terminalId can not be blank.");
        String pcMac = terminalId.substring(Constants.PC_FLAG.length());
        ComputerEntity computerEntity = computerBusinessDAO.findComputerEntityByMac(pcMac);
        if (computerEntity == null) {
            LOGGER.error("can not find computer.terminalId is {}", terminalId);
            return;
        }
        LOGGER.info("notify computer offline.terminalId is {}", terminalId);
        computerEntity.setState(ComputerStateEnum.OFFLINE);
        computerBusinessService.update(computerEntity, 0);
    }
}
