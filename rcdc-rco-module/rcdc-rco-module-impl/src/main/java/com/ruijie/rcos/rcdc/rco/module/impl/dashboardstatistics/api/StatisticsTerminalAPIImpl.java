package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.api;

import java.util.Date;
import java.util.Objects;

import com.ruijie.rcos.rcdc.terminal.module.def.constants.Constants;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import com.ruijie.rcos.rcdc.rco.module.def.api.StatisticsTerminalAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.TerminalOnlineSituationHourRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.TerminalOnlineSituationHourRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: StatisticsTerminalAPIImpl
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/1
 *
 * @author wjp
 */
public class StatisticsTerminalAPIImpl implements StatisticsTerminalAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsTerminalAPIImpl.class);

    /**
     * 获取锁的等待时间
     */
    private static final int WAIT_TIME = 3;

    @Autowired
    private TerminalOnlineSituationHourRecordDAO terminalOnlineSituationHourRecordDAO;

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    @Override
    public void recordTerminalOnlineSituation(String terminalId, CbbTerminalPlatformEnums platform) {
        Assert.hasText(terminalId, "terminal id can not be null");
        Assert.notNull(platform, "platform id can not be null");

        LOGGER.info("{}终端接入，类型是{}", terminalId, platform);
        if (platform == CbbTerminalPlatformEnums.PC) {
            String pcMac = terminalId.substring(Constants.PC_FLAG.length());
            ComputerEntity computerEntity = computerBusinessDAO.findComputerEntityByMac(pcMac);
            if (computerEntity == null) {
                LOGGER.error("can not find computer.terminalId is {}", terminalId);
                return;
            }
        }
        String currentHour = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH");
        try {
            // 终端ID加上时间应该为唯一标识，作为锁的标识
            LockableExecutor.executeWithTryLock(terminalId + currentHour, () -> {
                TerminalOnlineSituationHourRecordEntity terminalOnlineHourRecordEntity =
                        terminalOnlineSituationHourRecordDAO.findByTerminalIdAndHourKey(terminalId, currentHour);
                if (Objects.isNull(terminalOnlineHourRecordEntity)) {
                    terminalOnlineHourRecordEntity = new TerminalOnlineSituationHourRecordEntity(terminalId, platform);
                    try {
                        terminalOnlineSituationHourRecordDAO.save(terminalOnlineHourRecordEntity);
                    } catch (Exception e) {
                        LOGGER.error("插入数据失败，原因是：", e);
                    }
                }
                LOGGER.info("更新设备[{}]上线信息成功", terminalId);
            }, WAIT_TIME);
        } catch (BusinessException e) {
            LOGGER.error(String.format("更新设备[%s]上线信息失败，失败原因：", terminalId), e);
        }
    }
}
