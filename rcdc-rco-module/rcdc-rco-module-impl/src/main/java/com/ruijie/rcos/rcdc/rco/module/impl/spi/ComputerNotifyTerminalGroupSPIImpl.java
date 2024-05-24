package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalGroupOperNotifySPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbTerminalGroupOperNotifyRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description: PC纳管通知的SPI实现
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月7日
 *
 * @author wjp
 */
public class ComputerNotifyTerminalGroupSPIImpl implements CbbTerminalGroupOperNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerNotifyTerminalGroupSPIImpl.class);

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Override
    public DefaultResponse notifyTerminalGroupChange(CbbTerminalGroupOperNotifyRequest terminalGroupOperNotifyRequest) {
        Assert.notNull(terminalGroupOperNotifyRequest, "terminalGroupOperNotifyRequest is null");
        try {
            List<ComputerEntity> computerEntityList =
                    computerBusinessDAO.findByTerminalGroupIdList(Lists.newArrayList(terminalGroupOperNotifyRequest.getDeleteIdSet()));
            for (ComputerEntity computerEntity : computerEntityList) {
                computerEntity.setTerminalGroupId(terminalGroupOperNotifyRequest.getMoveGroupId());
                computerBusinessService.update(computerEntity, 0);
                computerBusinessService.moveGroupNotify(computerEntity);
            }
            return new DefaultResponse();
        } catch (Exception e) {
            LOGGER.error("删除终端分组同步移动PC终端失败", e);
            return new DefaultResponse();
        }
    }
}
