package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.alibaba.fastjson.JSON;
import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.Severity;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseOperateLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.operatelog.BaseGetOperateLogPageRequest;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseGetSystemLogPageRequest;
import com.ruijie.rcos.gss.log.module.def.dto.BaseOperateLogDTO;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.DeskopOpLogPageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDesktopOpLogDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.SyslogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SyslogScheduleConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.SyslogSendCycleEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.SyslogInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.syslog.SyslogClient;
import com.ruijie.rcos.sk.base.syslog.SyslogClientFactory;
import com.ruijie.rcos.sk.base.syslog.SyslogConfigInfo;
import com.ruijie.rcos.sk.base.syslog.SyslogConfigManager;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description: 发送syslog定时任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 9:52
 *
 * @author yxq
 */
@Service
@EnableScheduling
public class SendSyslogQuartzTask implements SchedulingConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendSyslogQuartzTask.class);

    private static final ThreadExecutor THREAD_EXECUTOR = ThreadExecutors.newBuilder("sendSyslog").maxThreadNum(5).queueSize(100).build();

    /**
     * 发送失败时，重试发送的次数
     */
    private static final int TRY_RESEND_COUNT = 2;

    @Autowired
    private SyslogConfigManager syslogConfigManager;

    @Autowired
    private SyslogClientFactory syslogClientFactory;

    @Autowired
    private BaseOperateLogMgmtAPI baseOperateLogMgmtAPI;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private CbbDesktopOpLogMgmtAPI cbbDesktopOpLogMgmtAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private SyslogAPI syslogAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    private final static String SEND_SYSLOG_CRON = "0 0 3 * * ?";

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        Assert.notNull(taskRegistrar, "taskRegistrar must not null");
        taskRegistrar.addTriggerTask(() -> {
            try {
                execute();
            } catch (BusinessException e) {
                LOGGER.error("定时发送审计日志失败，失败原因：", e);
            }
        }, triggerContext -> {
                // 任务触发，可修改任务的执行周期
                SyslogScheduleConfigDTO syslogSchedule = syslogAPI.getSyslogScheduleConfig();
                if (SyslogSendCycleEnum.MINUTE == syslogSchedule.getSendCycleType() && syslogSchedule.getIntervalMinute() != null) {
                    LOGGER.info("数据库查询下次定时任务执行间隔分钟为：[{}]", syslogSchedule.getIntervalMinute());
                    PeriodicTrigger periodicTrigger = new PeriodicTrigger(Long.valueOf(syslogSchedule.getIntervalMinute()), TimeUnit.MINUTES);
                    return periodicTrigger.nextExecutionTime(triggerContext);
                }

                String cron = StringUtils.isBlank(syslogSchedule.getScheduleCron()) ? SEND_SYSLOG_CRON : syslogSchedule.getScheduleCron();
                CronTrigger cronTrigger = new CronTrigger(cron);
                LOGGER.info("数据库查询下次定时任务执行cron表达式为：[{}]", cron);
                return cronTrigger.nextExecutionTime(triggerContext);
            });
    }

    private void execute() throws BusinessException {
        LOGGER.info("执行定时发送syslog定时任务");

        SyslogConfigInfo syslogConfigInfo = syslogConfigManager.getSyslogConfigInfo();
        LOGGER.info("获取syslog配置信息为：[{}]", JSON.toJSONString(syslogConfigInfo));
        // 设置查询日志的起始时间和结束时间
        Date thisDate = new Date();
        Date lastDate = getLastDate();

        if (!syslogConfigInfo.isOpen()) {
            LOGGER.info("未开启syslog功能，无需发送日志");
            return;
        }
        // 更新最后一次运行rsyslog发送日志的时间
        setLastDate(thisDate);

        String clusterVirtualIp = globalParameterAPI.findParameter(Constants.VIP_PARAM_KEY);
        LOGGER.info("服务器VIP为：[{}}", clusterVirtualIp);

        THREAD_EXECUTOR.execute(() -> {
            try {
                sendOperateLog(clusterVirtualIp, thisDate, lastDate);
            } catch (BusinessException e) {
                LOGGER.error("发送系统日志失败，失败原因：", e);
            }
        });

        THREAD_EXECUTOR.execute(() -> {
            try {
                sendSystemLog(clusterVirtualIp, thisDate, lastDate);
            } catch (BusinessException e) {
                LOGGER.error("发送系统日志失败，失败原因：", e);
            }
        });

        THREAD_EXECUTOR.execute(() -> {
            try {
                sendCloudDeskLog(clusterVirtualIp, thisDate, lastDate);
            } catch (BusinessException e) {
                LOGGER.error("发送系统日志失败，失败原因：", e);
            }
        });

    }

    private Date getLastDate() {
        String lastDateStr = globalParameterService.findParameter(Constants.RCDC_RCO_NEED_SEND_SYSLOG);
        if (lastDateStr.equals("true")) {
            Calendar todayTimeStart = new GregorianCalendar();
            todayTimeStart.set(Calendar.HOUR_OF_DAY, 0);
            todayTimeStart.set(Calendar.MINUTE, 0);
            todayTimeStart.set(Calendar.SECOND, 0);
            todayTimeStart.set(Calendar.MILLISECOND, 0);
            todayTimeStart.add(Calendar.DAY_OF_MONTH, 0);

            return todayTimeStart.getTime();
        }
        if (lastDateStr.equals("false")) {
            return DateUtil.getYesterdayStart();
        }
        long lastTimeLong = Long.parseLong(lastDateStr);
        return new Date(lastTimeLong + 1);
    }


    private void setLastDate(Date thisDate) {
        LOGGER.info("执行更新syslog定时任务执行时间");

        globalParameterService.updateParameter(Constants.RCDC_RCO_NEED_SEND_SYSLOG, Long.toString(thisDate.getTime()));

        LOGGER.info("执行更新执行时间syslog定时任务成功");
    }

    /**
     * 发送操作日志
     * 
     * @throws BusinessException 业务异常
     * @param clusterVirtualIp VIP
     */
    private void sendOperateLog(String clusterVirtualIp, Date thisDate, Date lastDate) throws BusinessException {
        LOGGER.info("发送操作日志");

        SyslogClient syslogClient =
                syslogClientFactory.createSyslogClient(Constants.RCDC_RCO_SYSLOG_APP_NAME, clusterVirtualIp);
        int page = 0;
        // 每次查询1000条记录
        int limit = 1000;
        BaseGetOperateLogPageRequest request = new BaseGetOperateLogPageRequest();
        // 只查询昨天的日志
        request.setStartTime(lastDate);
        request.setEndTime(thisDate);
        while (true) {
            request.setPage(page++);
            request.setLimit(limit);
            DefaultPageResponse<BaseOperateLogDTO> operateLogResponse = baseOperateLogMgmtAPI.getOperateLogPage(request);
            BaseOperateLogDTO[] itemArr = operateLogResponse.getItemArr();
            if (itemArr == null || itemArr.length == 0) {
                break;
            }
            List<SyslogInfoDTO> syslogInfoDTOList = new SyslogInfoDTO().buildSyslogInfoDTOList(itemArr);
            boolean isAllSuccess = sendLog(syslogInfoDTOList, Severity.INFORMATIONAL, Facility.USER, syslogClient);
            // 如果有日志发送失败，则需要写审计日志
            if (!isAllSuccess) {
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_SEND_OPERATE_LOG_EXISTS_FAIL);
            }
        }

        try {
            syslogClient.close();
        } catch (IOException e) {
            LOGGER.error("关闭syslog客户端失败，失败原因：", e);
        }
    }

    /**
     * 发送系统日志
     *
     * @throws BusinessException 业务异常
     * @param clusterVirtualIp VIP
     */
    private void sendSystemLog(String clusterVirtualIp, Date thisDate, Date lastDate) throws BusinessException {
        LOGGER.info("发送系统日志");

        SyslogClient syslogClient =
                syslogClientFactory.createSyslogClient(Constants.RCDC_RCO_SYSLOG_APP_NAME, clusterVirtualIp);
        int page = 0;
        // 每次查询1000条记录
        int limit = 1000;
        BaseGetSystemLogPageRequest request = new BaseGetSystemLogPageRequest();
        // 只查询昨天的日志
        request.setStartTime(lastDate);
        request.setEndTime(thisDate);
        while (true) {
            request.setPage(page++);
            request.setLimit(limit);
            DefaultPageResponse<BaseSystemLogDTO> systemLogResponse = baseSystemLogMgmtAPI.getSystemLogPage(request);
            BaseSystemLogDTO[] itemArr = systemLogResponse.getItemArr();
            if (itemArr == null || itemArr.length == 0) {
                break;
            }
            List<SyslogInfoDTO> syslogInfoDTOList = new SyslogInfoDTO().buildSyslogInfoDTOList(itemArr);
            boolean isAllSuccess = sendLog(syslogInfoDTOList, Severity.INFORMATIONAL, Facility.USER, syslogClient);
            // 如果有日志发送失败，则需要写审计日志
            if (!isAllSuccess) {
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_SEND_SYSTEM_LOG_EXISTS_FAIL);
            }
        }

        try {
            syslogClient.close();
        } catch (IOException e) {
            LOGGER.error("关闭syslog客户端失败，失败原因：", e);
        }
    }

    /**
     * 发送云桌面日志
     *
     * @throws BusinessException 业务异常
     * @param clusterVirtualIp VIP
     */
    private void sendCloudDeskLog(String clusterVirtualIp, Date thisDate, Date lastDate) throws BusinessException {
        LOGGER.info("发送云桌面日志");

        SyslogClient syslogClient =
                syslogClientFactory.createSyslogClient(Constants.RCDC_RCO_SYSLOG_APP_NAME, clusterVirtualIp);
        int page = 0;
        // 每次查询1000条记录
        int limit = 1000;
        DeskopOpLogPageRequest request = new DeskopOpLogPageRequest();
        // 只查询昨天的日志
        request.setStartTime(lastDate);
        request.setEndTime(thisDate);
        while (true) {
            request.setPage(page++);
            request.setLimit(limit);
            DefaultPageResponse<CbbDesktopOpLogDetailDTO> desktopOptLogResponse = cbbDesktopOpLogMgmtAPI.pageQuery(request);
            CbbDesktopOpLogDetailDTO[] itemArr = desktopOptLogResponse.getItemArr();
            if (itemArr == null || itemArr.length == 0) {
                break;
            }
            List<SyslogInfoDTO> syslogInfoDTOList = new SyslogInfoDTO().buildSyslogInfoDTOList(itemArr);
            boolean isAllSuccess = sendLog(syslogInfoDTOList, Severity.INFORMATIONAL, Facility.USER, syslogClient);
            // 如果有日志发送失败，则需要写审计日志
            if (!isAllSuccess) {
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_SEND_CLOUD_DESK_LOG_EXISTS_FAIL);
            }
        }

        try {
            syslogClient.close();
        } catch (IOException e) {
            LOGGER.error("关闭syslog客户端失败，失败原因：", e);
        }
    }

    private boolean sendLog(List<SyslogInfoDTO> syslogInfoDTOList, Severity severity, Facility facility, SyslogClient syslogClient) {
        AtomicBoolean isSuccess = new AtomicBoolean(true);
        syslogInfoDTOList.forEach((dto) -> {
            String message = JSON.toJSONString(dto);
            try {
                syslogClient.sendMessage(message, severity, facility);
            } catch (BusinessException e) {
                LOGGER.error(String.format("发送日志[%s]失败，失败原因：", message), e);
                boolean isSendSuccess = tryResendMessage(message, severity, facility, syslogClient);
                isSuccess.set(isSuccess.get() && isSendSuccess);
            }
        });
        return isSuccess.get();
    }


    private boolean tryResendMessage(String message, Severity severity, Facility facility, SyslogClient syslogClient) {
        int count = 0;
        while (count++ < TRY_RESEND_COUNT) {
            try {
                syslogClient.sendMessage(message, severity, facility);
                return true;
            } catch (BusinessException e) {
                LOGGER.error(String.format("发送日志[%s]失败，进行第[%s]次重试", message, count), e);
            }
        }
        LOGGER.error(String.format("日志[%s]已经[%s]次发送失败，不再进行重试", message, TRY_RESEND_COUNT));
        return false;
    }
}
