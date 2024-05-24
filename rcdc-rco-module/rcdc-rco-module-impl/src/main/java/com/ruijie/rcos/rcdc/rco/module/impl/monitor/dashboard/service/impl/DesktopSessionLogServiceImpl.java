package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.impl;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopSessionLogState;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopSessionLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopSessionLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/7
 *
 * @author linke
 */
@Service
public class DesktopSessionLogServiceImpl implements DesktopSessionLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopSessionLogServiceImpl.class);

    private static final String HANDLE_DESK_SESSION_LOG_LOCK = "HANDLE_DESK_SESSION_LOG_LOCK_";

    private static final Map<String, List<UUID>> TERMINAL_ID_DESKS_MAP = new ConcurrentHashMap<>();

    private static final Map<String, UUID> TERMINAL_ID_USER_MAP = new ConcurrentHashMap<>();

    private static final String THREAD_POOL_NAME = "handle-desk-session-log";

    private static final int MAX_THREAD_NUM = 30;

    private static final int QUEUE_SIZE = 1000;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    @Autowired
    private DesktopSessionLogDAO desktopSessionLogDAO;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private UserService userService;

    @Override
    public void handleDeskSessionLog(UUID userId, String terminalId, List<UserSessionDTO> sessionInfoList) {
        Assert.notNull(userId, "userId must not null");
        Assert.hasText(terminalId, "terminalId must has text");
        Assert.notNull(sessionInfoList, "sessionInfoList must not null");

        THREAD_EXECUTOR.execute(() -> {
            try {
                executeHandleDeskSessionLog(userId, terminalId, sessionInfoList);
            } catch (Exception e) {
                LOGGER.error("处理终端[{}]用户[{}]的deskSessionLog异常", e);
            }
        });
    }

    @Override
    public void handleDeskSessionLogTerminalOffline(String terminalId) {
        Assert.hasText(terminalId, "terminalId must has text");
        THREAD_EXECUTOR.execute(() -> {
            try {
                LockableExecutor.executeWithTryLock(HANDLE_DESK_SESSION_LOG_LOCK + terminalId, () -> {
                    List<DesktopSessionLogEntity> connectList = desktopSessionLogDAO.findAllByTerminalIdAndState(terminalId,
                            DesktopSessionLogState.CONNECTING);
                    connectList = connectList.stream().filter(item -> Objects.isNull(item.getConnectionId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(connectList)) {
                        return;
                    }
                    Date currentTime = new Date();
                    connectList.forEach(item -> {
                        item.setLogoutTime(currentTime);
                        item.setState(DesktopSessionLogState.FINISHED);
                    });
                    desktopSessionLogDAO.saveAll(connectList);
                }, 3);
            } catch (Exception e) {
                LOGGER.error("处理终端[{}]用户[{}]的deskSessionLog异常", e);
            }
        });
    }

    private void executeHandleDeskSessionLog(UUID userId, String terminalId, List<UserSessionDTO> sessionInfoList) throws BusinessException {
        LockableExecutor.executeWithTryLock(HANDLE_DESK_SESSION_LOG_LOCK + terminalId, () -> {

            // 如果不存在这个终端对应的用户缓存，需要检查终端之前的非该用户的记录要全部setLogoutTime
            dealOtherUserFinishSessionLog(userId, terminalId);
            TERMINAL_ID_USER_MAP.put(terminalId, userId);

            List<UUID> cacheDeskIdList;
            if (!TERMINAL_ID_DESKS_MAP.containsKey(terminalId)) {
                List<DesktopSessionLogEntity> terminalConnectingList = desktopSessionLogDAO.findAllByTerminalIdAndState(terminalId,
                        DesktopSessionLogState.CONNECTING);
                cacheDeskIdList = terminalConnectingList.stream().map(DesktopSessionLogEntity::getDesktopId).distinct().collect(Collectors.toList());
                TERMINAL_ID_DESKS_MAP.put(terminalId, cacheDeskIdList);
            } else  {
                cacheDeskIdList = TERMINAL_ID_DESKS_MAP.get(terminalId);
            }

            // 只处理桌面
            List<UserSessionDTO> deskUserSessionList = sessionInfoList.stream().filter(item -> item.getResourceType() == ResourceTypeEnum.DESK)
                    .collect(Collectors.toList());
            List<UUID> reportDeskIdList = deskUserSessionList.stream().map(UserSessionDTO::getResourceId).filter(Objects::nonNull).distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(cacheDeskIdList) && CollectionUtils.isEmpty(reportDeskIdList)) {
                return;
            }
            // 处理要结束掉的记录
            List<DesktopSessionLogEntity> entityList = dealNeedFinishSessionLog(userId, terminalId, reportDeskIdList, cacheDeskIdList);
            // 处理新增的记录
            entityList.addAll(dealNeedAddSessionLog(userId, terminalId, reportDeskIdList, cacheDeskIdList));

            if (!CollectionUtils.isEmpty(entityList)) {
                desktopSessionLogDAO.saveAll(entityList);
            }
            TERMINAL_ID_DESKS_MAP.put(terminalId, reportDeskIdList);
        }, 3);
    }

    private void dealOtherUserFinishSessionLog(UUID userId, String terminalId) {
        if (TERMINAL_ID_USER_MAP.containsKey(terminalId) && Objects.equals(userId, TERMINAL_ID_USER_MAP.get(terminalId))) {
            return;
        }

        // 如果不存在这个终端对应的用户缓存，需要检查终端之前的非该用户的记录要全部setLogoutTime
        List<DesktopSessionLogEntity> connectList = desktopSessionLogDAO.findAllByTerminalIdAndState(terminalId, DesktopSessionLogState.CONNECTING);
        List<DesktopSessionLogEntity> otherUserLogList = connectList.stream().filter(item -> !Objects.equals(userId, item.getUserId()))
                .collect(Collectors.toList());
        otherUserLogList = otherUserLogList.stream().filter(item -> Objects.isNull(item.getConnectionId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(otherUserLogList)) {
            return;
        }
        Date currentTime = new Date();
        otherUserLogList.forEach(item -> {
            item.setLogoutTime(currentTime);
            item.setState(DesktopSessionLogState.FINISHED);
        });
        desktopSessionLogDAO.saveAll(otherUserLogList);
    }

    private List<DesktopSessionLogEntity> dealNeedFinishSessionLog(UUID userId, String terminalId, List<UUID> reportDeskIdList,
                                                                   List<UUID> cacheDeskIdList) {
        List<UUID> needFinishDeskIdList;
        // 判断哪些桌面需要断连
        if (CollectionUtils.isEmpty(reportDeskIdList)) {
            needFinishDeskIdList = Lists.newArrayList(cacheDeskIdList);
        } else {
            needFinishDeskIdList = cacheDeskIdList.stream().filter(item -> !reportDeskIdList.contains(item)).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(needFinishDeskIdList)) {
            return new ArrayList<>();
        }
        // 找到记录setLogoutTime
        List<DesktopSessionLogEntity> sessionLogEntityList = desktopSessionLogDAO.findByUserIdAndTerminalIdAndAndStateAndDesktopIdIn(userId,
                terminalId, DesktopSessionLogState.CONNECTING, needFinishDeskIdList);
        // 多会话或者第三方桌面记录的无connectionId
        sessionLogEntityList = sessionLogEntityList.stream().filter(item -> Objects.isNull(item.getConnectionId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sessionLogEntityList)) {
            return new ArrayList<>();
        }
        Date currentTime = new Date();
        sessionLogEntityList.forEach(item -> {
            item.setLogoutTime(currentTime);
            item.setState(DesktopSessionLogState.FINISHED);
        });
        return sessionLogEntityList;
    }

    private List<DesktopSessionLogEntity> dealNeedAddSessionLog(UUID userId, String terminalId, List<UUID> reportDeskIdList,
                                                                List<UUID> cacheDeskIdList) {
        // 判断哪些桌面需要新增记录
        List<UUID> needAddDeskIdList;
        if (CollectionUtils.isEmpty(cacheDeskIdList)) {
            needAddDeskIdList = Lists.newArrayList(reportDeskIdList);
        } else {
            needAddDeskIdList = reportDeskIdList.stream().filter(item -> !cacheDeskIdList.contains(item)).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(needAddDeskIdList)) {
            return new ArrayList<>();
        }
        CloudDesktopDetailDTO desktopDetailDTO;
        RcoViewUserEntity rcoViewUserEntity = userService.getUserInfoById(userId);
        List<DesktopSessionLogEntity> addList = new ArrayList<>();
        DesktopSessionLogEntity entity;
        Date currentTime = new Date();
        for (UUID deskId : needAddDeskIdList) {
            try {
                desktopDetailDTO = queryCloudDesktopService.queryDeskDetail(deskId);
            } catch (BusinessException e) {
                LOGGER.error("获取桌面[{}]详情异常", deskId, e);
                continue;
            }
            if (Objects.isNull(desktopDetailDTO.getDesktopPoolId())) {
                LOGGER.debug("非池桌面无需记录会话记录，桌面[{}]", desktopDetailDTO.getId());
                continue;
            }
            if (CbbEstProtocolType.EST == desktopDetailDTO.getEstProtocolType()) {
                LOGGER.debug("带外的会话通过虚拟化事件进行记录，桌面[{}]", desktopDetailDTO.getId());
                continue;
            }
            // 插入新的会话记录
            entity = new DesktopSessionLogEntity();
            entity.setCreateTime(currentTime);
            entity.setDesktopId(deskId);
            entity.setLoginTime(currentTime);
            entity.setDesktopPoolType(DesktopPoolType.valueOf(desktopDetailDTO.getDesktopPoolType()));
            entity.setDesktopName(desktopDetailDTO.getDesktopName());
            entity.setUserGroupName(rcoViewUserEntity.getGroupName());
            entity.setUserGroupId(rcoViewUserEntity.getGroupId());
            entity.setUserId(userId);
            entity.setUserName(rcoViewUserEntity.getUserName());
            entity.setRelatedId(desktopDetailDTO.getDesktopPoolId());
            entity.setDesktopPoolName(desktopDetailDTO.getDesktopPoolName());
            entity.setCbbDesktopPoolType(CbbDesktopPoolType.valueOf(desktopDetailDTO.getDesktopCategory()));
            entity.setDesktopSessionType(desktopDetailDTO.getSessionType());
            entity.setTerminalId(terminalId);
            entity.setState(DesktopSessionLogState.CONNECTING);
            addList.add(entity);
        }
        return addList;
    }
}
