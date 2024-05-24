package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.DashboardStatisticsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dao.TerminalOnlineSituationHourRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dto.TerminalOnlineSituationHourRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity.TerminalOnlineSituationHourRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;

/**
 * Description: 首页每小时收集终端在线情况定时任务
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
@Service
@Quartz(cron = "0 0 * * * ?", scheduleName = DashboardStatisticsBusinessKey.RCDC_RCO_TERMINAL_ONLINE_SITUATION_COLLECT_EVERY_HOUR,
        scheduleTypeCode = ScheduleTypeCodeConstants.TERMINAL_ONLINE_SITUATION_COLLECT_EVERY_HOUR, blockInMaintenanceMode = true)
public class TerminalOnlineSituationHourCollectQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOnlineSituationHourCollectQuartzTask.class);

    @Autowired
    private ViewTerminalDAO viewTerminalDAO;

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    @Autowired
    private TerminalOnlineSituationHourRecordDAO terminalOnlineSituationHourRecordDAO;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");

        LOGGER.info("首页每小时收集终端在线情况定时任务开始");

        List<TerminalOnlineSituationHourRecordDTO> terminalOnlineHourRecordDTOList = generateTerminalList();
        terminalOnlineHourRecordDTOList.forEach(dto -> {
            TerminalOnlineSituationHourRecordEntity terminalOnlineHourRecordEntity =
                    new TerminalOnlineSituationHourRecordEntity(dto.getTerminalId(), dto.getPlatform());
            String hourKey = terminalOnlineHourRecordEntity.getHourKey();
            LOGGER.debug("首页每小时收集终端在线情况定时任务<{}>", hourKey);
            TerminalOnlineSituationHourRecordEntity oldTerminalOnlineHourRecordEntity =
                    terminalOnlineSituationHourRecordDAO.findByTerminalIdAndHourKey(dto.getTerminalId(), hourKey);
            if (Objects.isNull(oldTerminalOnlineHourRecordEntity)) {
                try {
                    LOGGER.info("终端<{}>类型<{}>", terminalOnlineHourRecordEntity.getTerminalId(), terminalOnlineHourRecordEntity.getPlatform());
                    terminalOnlineSituationHourRecordDAO.save(terminalOnlineHourRecordEntity);
                } catch (Exception e) {
                    LOGGER.error("插入记录失败，原因是：{}", e);
                }
            }
        });
    }

    private List<TerminalOnlineSituationHourRecordDTO> generateTerminalList() {
        List<TerminalOnlineSituationHourRecordDTO> terminalOnlineSituationHourRecordDTOList = new ArrayList<>();
        List<ViewTerminalEntity> terminalEntityList = viewTerminalDAO.findByTerminalState(CbbTerminalStateEnums.ONLINE);
        if (!CollectionUtils.isEmpty(terminalEntityList)) {
            List<TerminalOnlineSituationHourRecordDTO> terminalDTOList = terminalEntityList.stream()
                    .filter(entity -> StringUtils.isNotBlank(entity.getTerminalId()) && Objects.nonNull(entity.getPlatform())
                            && entity.getPlatform() != CbbTerminalPlatformEnums.PC)
                    .map(entity -> new TerminalOnlineSituationHourRecordDTO(entity.getTerminalId(), entity.getPlatform()))
                    .collect(Collectors.toList());
            terminalOnlineSituationHourRecordDTOList.addAll(terminalDTOList);
        }

        List<ComputerEntity> computerEntityList = computerBusinessDAO.findByState(ComputerStateEnum.ONLINE);
        if (!CollectionUtils.isEmpty(computerEntityList)) {
            List<TerminalOnlineSituationHourRecordDTO> computerDTOList =
                    computerEntityList.stream().filter(entity -> StringUtils.isNotBlank(entity.getMac()))
                            .map(entity -> new TerminalOnlineSituationHourRecordDTO(entity.getMac(), CbbTerminalPlatformEnums.PC))
                            .collect(Collectors.toList());
            terminalOnlineSituationHourRecordDTOList.addAll(computerDTOList);
        }
        return terminalOnlineSituationHourRecordDTOList;
    }
}
