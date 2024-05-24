package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.quartz;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaTrusteeshipHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaTrusteeshipHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.HostStatusEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.utils.UserLicenseHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopSessionLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.UserLicenseBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.cache.TempSessionCache;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.enums.ClearSessionReasonTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.service.UserLicenseService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.license.module.def.api.CbbLicenseCenterAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.dao.UserSessionDAO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.util.CollectionUitls;

/**
 * Description: 定时清理用户并发授权（每30s检查一次）
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/6
 *
 * @author lihengjing
 */
@Service
@Quartz(cron = "0/30 * * * * ?", scheduleTypeCode = "user_license_clean",
        scheduleName = UserLicenseBusinessKey.RCDC_USER_LICENSE_QUARTZ_CLEAN_LICENSE)
public class UserLicenseCleanQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLicenseCleanQuartzTask.class);

    @Autowired
    private CbbLicenseCenterAPI cbbLicenseCenterAPI;

    @Autowired
    private UserLicenseService userLicenseService;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private RcaTrusteeshipHostAPI rcaTrusteeshipHostAPI;

    @Autowired
    private UserSessionDAO userSessionDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private TempSessionCache tempSessionCache;

    @Autowired
    private DesktopSessionLogService desktopSessionLogService;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        if (cbbLicenseCenterAPI.isActiveUserLicenseMode()) {
            LOGGER.info("用户并发授权会话回收定时任务执行开始");

            // 回收会话数量初始化0
            int cleanCount = 0;
            // 获取当前时间点
            Instant start = Instant.now();
            // 清理未及时上报的预生成会话记录
            cleanCount += cleanTempSession();
            // 根据终端离线情况进行会话、授权释放
            cleanCount += cleanOfflineTerminalSession();
            // 根据资源离线情况进行会话、授权释放
            cleanCount += cleanOfflineResourceSession();

            // 获取方法执行后的时间点
            Instant end = Instant.now();
            // 计算持续时间
            Duration duration = Duration.between(start, end);

            LOGGER.info("用户并发授权会话回收定时任务执行结束，清理[{}]个会话记录，时长（毫秒）：{}", cleanCount, duration.toMillis());
        }
    }

    private int cleanTempSession() {
        int cleanCount = 0;
        List<UUID> timeoutSessionIdList = tempSessionCache.getTimeoutSessionIdList();
        LOGGER.info("超时未上报预占用会话总计[{}]个会话记录", timeoutSessionIdList.size());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("超时未上报预占用会话总计[{}]个会话记录，详情：{}", timeoutSessionIdList.size(), JSONObject.toJSONString(timeoutSessionIdList));
        }
        for (UUID sessionId : timeoutSessionIdList) {
            cleanCount += userLicenseService.clearTimeoutReportUserSession(sessionId);
        }
        LOGGER.info("超时未上报预占用会话，进行会话授权释放，总计清理[{}]个会话记录", cleanCount);
        return cleanCount;
    }

    private int cleanOfflineTerminalSession() {
        int cleanCount = 0;

        List<UserSessionDTO> terminalSessionList = userSessionDAO.getDistinctTerminalInfoList();
        if (!CollectionUitls.isEmpty(terminalSessionList)) {

            // 在线的 Shine、One Client终端ID列表
            List<String> terminalList = cbbTerminalOperatorAPI.getOnlineTerminalIdList();

            for (UserSessionDTO userSessionDTO : terminalSessionList) {
                String terminalId = userSessionDTO.getTerminalId();
                TerminalTypeEnum terminalType = userSessionDTO.getTerminalType();
                try {
                    if (terminalType == TerminalTypeEnum.WEB_CLIENT) {
                        cleanCount = cleanOfflineWebClient(cleanCount, terminalId, terminalType);
                    } else {
                        cleanCount = cleanOfflineShineAndOC(cleanCount, terminalList, terminalId, terminalType);
                    }
                } catch (BusinessException e) {
                    LOGGER.warn("客户端类型[{}]ID[{}]已经离线，进行会话授权释放出现异常，本次跳过", terminalType.name(), terminalId, e);
                }
            }
            LOGGER.info("离线客户端，进行会话授权释放，总计清理[{}]个会话记录", cleanCount);
        }
        return cleanCount;
    }

    private int cleanOfflineShineAndOC(int cleanCount, List<String> terminalList, String terminalId, TerminalTypeEnum terminalType)
            throws BusinessException {
        // Shine、One Client检查终端是否离线
        if (!terminalList.contains(terminalId)) {
            try {
                TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
                // 确认终端是否在线
                if (Objects.nonNull(terminalDTO) && terminalDTO.getTerminalState() != CbbTerminalStateEnums.ONLINE) {
                    int count = userLicenseService.clearUserSessionAndLicenseByTerminal(ClearSessionReasonTypeEnum.TERMINAL_OFFLINE, terminalType,
                            terminalId);
                    LOGGER.info("客户端类型[{}]ID[{}]已经离线，进行会话授权释放，清理[{}]个会话记录", terminalType.name(), terminalId, count);
                    cleanCount += count;

                    // 多会话和第三方桌面连接日志要在这里处理
                    desktopSessionLogService.handleDeskSessionLogTerminalOffline(terminalId);
                }
            } catch (BusinessException e) {
                LOGGER.error("获取终端信息发生异常，终端id: {}，直接删除相关会话，异常详情：{}", terminalId, e);
                int count = userLicenseService.clearUserSessionAndLicenseByTerminal(ClearSessionReasonTypeEnum.TERMINAL_OFFLINE, terminalType,
                        terminalId);
                LOGGER.info("客户端类型[{}]ID[{}]已经离线，进行会话授权释放，清理[{}]个会话记录", terminalType.name(), terminalId, count);
                cleanCount += count;
            }
        }
        return cleanCount;
    }

    private int cleanOfflineWebClient(int cleanCount, String terminalId, TerminalTypeEnum terminalType) throws BusinessException {
        // 网页版客户端超过3分钟无更新，视为离线
        if (UserLicenseHelper.isTerminalReportTimeout(terminalType, terminalId)) {
            int count =
                    userLicenseService.clearUserSessionAndLicenseByTerminal(ClearSessionReasonTypeEnum.TERMINAL_OFFLINE, terminalType, terminalId);
            LOGGER.info("客户端类型[{}]ID[{}]已经超过3分钟无上报会话信息，进行会话授权释放，清理[{}]个会话记录", terminalType.name(), terminalId, count);
            cleanCount += count;
        }
        return cleanCount;
    }

    private int cleanOfflineResourceSession() {
        int cleanCount = 0;

        List<UserSessionDTO> resourceSessionList = userSessionDAO.getDistinctResourceInfoList();
        if (!CollectionUitls.isEmpty(resourceSessionList)) {

            List<UUID> allRunningDeskIdList = cbbVDIDeskMgmtAPI.listAllDeskInfo().stream()
                    .filter(dto -> dto.getDeskState() == CbbCloudDeskState.RUNNING).map(CbbDeskInfoDTO::getDeskId).collect(Collectors.toList());

            for (UserSessionDTO userSessionDTO : resourceSessionList) {
                UUID resourceId = userSessionDTO.getResourceId();
                ResourceTypeEnum resourceType = userSessionDTO.getResourceType();
                try {
                    if (resourceType == ResourceTypeEnum.DESK) {
                        cleanCount = cleanOfflineDesk(cleanCount, allRunningDeskIdList, resourceId, resourceType);
                    } else if (resourceType == ResourceTypeEnum.APP) {
                        cleanCount = cleanOfflineAppHost(cleanCount, resourceId, resourceType);
                    }
                } catch (BusinessException e) {
                    LOGGER.warn("资源类型[{}]ID[{}]已经非运行状态，进行会话授权释放出现异常，本次跳过，异常详情：{}", resourceType, resourceId, e);
                }
            }

            LOGGER.info("非运行中资源，进行会话授权释放，总计清理[{}]个会话记录", cleanCount);
        }

        return cleanCount;
    }

    private int cleanOfflineAppHost(int cleanCount, UUID resourceId, ResourceTypeEnum resourceType) throws BusinessException {
        // 检查云应用主机离线情况
        RcaTrusteeshipHostDTO vmInfo = rcaTrusteeshipHostAPI.getVmInfo(resourceId);
        if (Objects.nonNull(vmInfo) && vmInfo.getStatus() != HostStatusEnums.ON_LINE) {
            int count =
                    userLicenseService.clearUserSessionAndLicenseByResource(ClearSessionReasonTypeEnum.RESOURCE_OFFLINE, resourceType, resourceId);
            LOGGER.info("资源类型[{}]ID[{}]已经非运行状态，进行会话授权释放，清理[{}]个会话记录", resourceType, resourceId, count);
            cleanCount += count;
        }
        return cleanCount;
    }

    private int cleanOfflineDesk(int cleanCount, List<UUID> allRunningDeskIdList, UUID resourceId, ResourceTypeEnum resourceType)
            throws BusinessException {
        // 检查云桌面离线情况
        if (!allRunningDeskIdList.contains(resourceId)) {
            try {
                CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(resourceId);
                if (Objects.nonNull(cloudDesktopDetailDTO) && !cloudDesktopDetailDTO.getDesktopState().equals(CbbCloudDeskState.RUNNING.name())) {
                    int count = userLicenseService.clearUserSessionAndLicenseByResource(ClearSessionReasonTypeEnum.RESOURCE_OFFLINE, resourceType,
                            resourceId);
                    LOGGER.info("资源类型[{}]ID[{}]已经非运行状态，进行会话授权释放，清理[{}]个会话记录", resourceType, resourceId, count);
                    cleanCount += count;
                }
            } catch (BusinessException e) {
                LOGGER.error("获取桌面信息发生异常，桌面id: {}，直接删除相关会话，异常详情：{}", resourceId, e);
                int count = userLicenseService.clearUserSessionAndLicenseByResource(ClearSessionReasonTypeEnum.RESOURCE_OFFLINE, resourceType,
                        resourceId);
                LOGGER.info("资源类型[{}]ID[{}]已经非运行状态，进行会话授权释放，清理[{}]个会话记录", resourceType, resourceId, count);
                cleanCount += count;
            }
        }
        return cleanCount;
    }

}
