package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktopsession.DesktopSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.DesktopSessionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolAPIHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Description: 桌面池回收定时任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/8
 *
 * @author wuShengQiang
 */
@Service
@Quartz(scheduleTypeCode = "desktop_of_pool_recover", scheduleName = DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESKTOP_OF_POOL_RECOVER,
        cron = "0 0/1 * * * ?")
public class DesktopOfPoolRecoverQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopOfPoolRecoverQuartzTask.class);

    private static final int SHUTDOWN_RETRY_NUM = 3;

    private static final int RECYCLING_ADD_TIME = 5;

    private static final Map<UUID, DesktopRecoverInfo> NEED_RECOVER_CLOSE_DESKTOP_MAP = new ConcurrentHashMap<>();

    private static final Set<CbbCloudDeskState> NEED_CHECK_STRATEGY_STATE = Sets.newHashSet(CbbCloudDeskState.RUNNING, CbbCloudDeskState.SLEEP,
            CbbCloudDeskState.CLOSE);

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private DesktopPoolAPIHelper desktopPoolAPIHelper;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private DesktopUpdateService desktopUpdateService;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private DesktopSessionServiceAPI desktopSessionServiceAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    /**
     * 分配100个线程数处理池桌面回收
     */
    private static final ExecutorService RECOVER_THREAD_POOL =
            ThreadExecutors.newBuilder("desktopOfPoolRecover").maxThreadNum(100).queueSize(1000).build();

    /**
     * 分配100个线程数处理，gt关机，池桌面策略不一致问题
     */
    private static final ExecutorService OTHER_THREAD_POOL = ThreadExecutors.newBuilder("desktopOfPoolRecoverOther").maxThreadNum(100)
            .queueSize(1000).build();

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {

        // 查询池及关联桌面列表
        // 获取所有池列表
        List<CbbDesktopPoolModel> poolModelList = Lists.newArrayList(CbbDesktopPoolModel.DYNAMIC, CbbDesktopPoolModel.STATIC);
        List<DesktopPoolBasicDTO> desktopPoolList = desktopPoolService.listDesktopPoolByPoolModel(poolModelList);
        LOGGER.info("获取所有池列表,总数:{}", desktopPoolList.size());

        desktopPoolList = desktopPoolList.stream().filter(pool -> pool.getPoolState() != CbbDesktopPoolState.DELETING
                && pool.getPoolState() != CbbDesktopPoolState.CREATING).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(desktopPoolList)) {
            LOGGER.info("过滤掉删除中和创建中桌面池后无桌面池需要检查桌面回收，任务结束");
            return;
        }
        List<UUID> allDeskIdList = new ArrayList<>();
        // 打乱一下
        Collections.shuffle(desktopPoolList);
        for (DesktopPoolBasicDTO desktopPoolBasic : desktopPoolList) {
            checkPoolDeskAndRecoverDesk(desktopPoolBasic, allDeskIdList);
        }
        cleanNotExistDesktop(allDeskIdList);
    }

    private void checkPoolDeskAndRecoverDesk(DesktopPoolBasicDTO desktopPoolBasic, List<UUID> allDeskIdList) {
        List<PoolDesktopInfoDTO> desktopInfoList = desktopPoolService.listNormalDeskInfoByDesktopPoolId(desktopPoolBasic.getId());
        if (CollectionUtils.isEmpty(desktopInfoList)) {
            LOGGER.info("桌面池[{}]，无符合状态的桌面需要检查", desktopPoolBasic.getId());
            return;
        }
        allDeskIdList.addAll(desktopInfoList.stream().map(PoolDesktopInfoDTO::getDeskId).collect(Collectors.toList()));
        // 处理无人使用且各种策略与桌面池不一致的桌面
        handleNoUserStrategyNotMatchDesk(desktopInfoList, desktopPoolBasic);

        // 过滤其中满足回收条件的数据
        List<PoolDesktopInfoDTO> needRecoverDesktopList = desktopInfoList.stream()
                .filter(desktopDTO -> filterNeedRecoverDesktop(desktopDTO, desktopPoolBasic)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(needRecoverDesktopList)) {
            LOGGER.info("桌面池[{}]无需要回收的桌面", desktopPoolBasic.getId());
            return;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("桌面池[{}]需要回收的桌面集合，needRecoverDesktopList=[{}]", desktopPoolBasic.getId(), JSON.toJSONString(needRecoverDesktopList));
        }
        // 执行回收
        for (PoolDesktopInfoDTO desktopDTO : needRecoverDesktopList) {
            RECOVER_THREAD_POOL.execute(() -> doRecoverDesktop(desktopDTO));
        }
    }

    private void handleNoUserStrategyNotMatchDesk(List<PoolDesktopInfoDTO> desktopInfoList, DesktopPoolBasicDTO desktopPoolBasicDTO) {
        // 静态池/第三方/多会话不需要处理
        if (CbbDesktopPoolModel.STATIC == desktopPoolBasicDTO.getPoolModel() || CbbDesktopPoolType.THIRD == desktopPoolBasicDTO.getPoolType()
                || desktopPoolBasicDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            return;
        }

        for (PoolDesktopInfoDTO desktopDTO : desktopInfoList) {
            if (Objects.nonNull(desktopDTO.getUserId())) {
                continue;
            }
            // 休眠，开机且无用户的需要判断策略是否一致，不一致需要唤醒或者关机
            if (!NEED_CHECK_STRATEGY_STATE.contains(desktopDTO.getDeskState())) {
                // 非RUNNING,非SLEEP,非CLOSE状态的桌面直接跳过
                continue;
            }
            try {
                if (desktopPoolAPIHelper.checkDeskConfigEqualsPool(desktopDTO, desktopPoolBasicDTO)) {
                    // 所有策略一致直接跳过
                    continue;
                }
            } catch (BusinessException e) {
                LOGGER.error("比对桌面[{}]和其桌面池配置是否一致发生异常", desktopDTO.getDeskId(), e);
                continue;
            }
            try {
                if (desktopAPI.isAnyConnectedChannel(desktopDTO.getDeskId())) {
                    // 存在用户连接桌面使用中跳过
                    continue;
                }
            } catch (BusinessException e) {
                LOGGER.error("检查桌面[{}]是否还有会话连接中发生异常", desktopDTO.getDeskId(), e);
                continue;
            }

            switch (desktopDTO.getDeskState()) {
                case RUNNING:
                    OTHER_THREAD_POOL.execute(() -> shutdownDesktop(desktopDTO, true));
                    break;
                case SLEEP:
                    OTHER_THREAD_POOL.execute(() -> startUpSleepDesktop(desktopDTO));
                    break;
                case CLOSE:
                    desktopUpdateService.updateVDIConfigAsync(desktopDTO.getDeskId());
                    break;
                default:
                    LOGGER.info("桌面[{}]无需操作", desktopDTO.getDeskId());
            }
        }
    }

    private boolean filterNeedRecoverDesktop(PoolDesktopInfoDTO desktopDTO, DesktopPoolBasicDTO desktopPool) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("过滤需要回收的云桌面，desktopDTO：{}", JSON.toJSONString(desktopDTO));
        }
        CbbDesktopPoolModel poolModel = desktopPool.getPoolModel();
        if (CbbDesktopPoolType.VDI == desktopPool.getPoolType()) {
            if (CbbDesktopPoolModel.DYNAMIC == poolModel) {
                return handleVdiDynamic(desktopDTO, desktopPool);
            } else {
                return handleVdiStatic(desktopDTO, desktopPool);
            }
        } else {
            // 第三方,无需后续关机
            if (CbbDesktopPoolModel.DYNAMIC == poolModel) {
                return handleThirdDynamic(desktopDTO, desktopPool);
            } else {
                return handleThirdStatic(desktopDTO, desktopPool);
            }
        }
    }

    private boolean handleVdiDynamic(PoolDesktopInfoDTO desktopDTO, DesktopPoolBasicDTO desktopPool) {
        if (CbbDesktopSessionType.SINGLE == desktopPool.getSessionType()) {
            return handleVdiSingleCheckAssignment(desktopDTO) || checkDynamicDesktopRecover(desktopPool, desktopDTO);
        } else {
            // 动态多会话,无需后续关机
            handleMultipleRecover(desktopDTO, desktopPool);
        }
        return false;
    }

    private boolean handleVdiStatic(PoolDesktopInfoDTO desktopDTO, DesktopPoolBasicDTO desktopPool) {
        if (CbbDesktopSessionType.SINGLE == desktopPool.getSessionType()) {
            return handleVdiSingleCheckAssignment(desktopDTO);
        } else {
            // 静态多会话,无需后续关机
            handleMultipleRecoverWhenUserNotInPool(CbbCloudDeskState.CLOSE, desktopDTO);
        }
        return false;
    }

    private boolean handleVdiSingleCheckAssignment(PoolDesktopInfoDTO desktopDTO) {
        // 无用户，不需要回收
        if (Objects.isNull(desktopDTO.getUserId())) {
            // 桌面未绑定用户，不回收
            NEED_RECOVER_CLOSE_DESKTOP_MAP.remove(desktopDTO.getDeskId());
            return false;
        }
        // 判断用户是否分配了该池的池桌面
        return checkRecoverWhenUserNotInPool(CbbCloudDeskState.CLOSE, desktopDTO, desktopDTO.getUserId(),
                Objects.nonNull(desktopDTO.getConnectClosedTime()));
    }

    private boolean handleThirdDynamic(PoolDesktopInfoDTO desktopDTO, DesktopPoolBasicDTO desktopPool) {
        UUID deskId = desktopDTO.getDeskId();
        if (desktopPool.getSessionType() == CbbDesktopSessionType.SINGLE) {
            // 动态单会话,UserDesktopEntity表记录userId置为null
            List<DesktopSessionDTO> sessionList = desktopSessionServiceAPI.findByDeskId(deskId);
            if (!CollectionUtils.isEmpty(sessionList)) {
                return false;
            }
            // 无会话连接时,则根据分配时间判断超过池设置的断开时间(分配时间计算结果会大于OA上报的注销时间)
            if (meetRecyclingTime(desktopDTO.getAssignmentTime(), desktopPool.getIdleDesktopRecover(), RECYCLING_ADD_TIME)) {
                userDesktopService.unbindUserAndDesktopRelation(deskId);
                sessionDeleteLog(deskId, desktopDTO.getUserId(), desktopDTO.getDesktopName());
            }
        } else {
            handleMultipleRecover(desktopDTO, desktopPool);
        }
        return false;
    }

    private boolean handleThirdStatic(PoolDesktopInfoDTO desktopDTO, DesktopPoolBasicDTO desktopPool) {
        // 判断用户是否分配了该池桌面
        UUID deskId = desktopDTO.getDeskId();
        if (desktopPool.getSessionType() == CbbDesktopSessionType.SINGLE) {
            if (Objects.isNull(desktopDTO.getUserId())) {
                return false;
            }
            // 用户不在池里时，回收离线和已断连的桌面
            DesktopSessionDTO sessionDTO = desktopSessionServiceAPI.findByUserIdAndDeskId(desktopDTO.getUserId(), deskId);
            if (checkRecoverWhenUserNotInPool(CbbCloudDeskState.OFF_LINE, desktopDTO, desktopDTO.getUserId(), Objects.isNull(sessionDTO))) {
                userDesktopService.unbindUserAndDesktopRelation(deskId);
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SCHEDULE_RECOVER, desktopDTO.getPoolName(),
                        desktopDTO.getDesktopName());
            }
        } else {
            // 多会话,需要删除HostUserEntity表记录
            handleMultipleRecoverWhenUserNotInPool(CbbCloudDeskState.OFF_LINE, desktopDTO);
        }
        return false;
    }

    private void handleMultipleRecover(PoolDesktopInfoDTO desktopDTO, DesktopPoolBasicDTO desktopPool) {
        // 多会话,需要删除HostUserEntity表记录
        UUID deskId = desktopDTO.getDeskId();
        List<HostUserEntity> hostUserList = hostUserService.findByDeskId(deskId);
        List<DesktopSessionDTO> sessionList = desktopSessionServiceAPI.findByDeskId(deskId);
        List<UUID> userIdList = sessionList.stream().map(DesktopSessionDTO::getUserId).collect(Collectors.toList());
        for (HostUserEntity hostUserEntity : hostUserList) {
            UUID userId = hostUserEntity.getUserId();
            if (userIdList.contains(userId)) {
                continue;
            }
            // 根据分配时间判断超过池设置的断开时间(分配时间计算结果会大于OA上报的注销时间)
            if (meetRecyclingTime(hostUserEntity.getCreateTime(), desktopPool.getIdleDesktopRecover(), RECYCLING_ADD_TIME)) {
                hostUserService.removeByUserIdAndDeskId(userId, deskId);
                sessionDeleteLog(deskId, userId, desktopDTO.getDesktopName());
                // 多会话解绑后,不需要关机等后续操作
            }
        }
    }

    private void handleMultipleRecoverWhenUserNotInPool(CbbCloudDeskState state, PoolDesktopInfoDTO desktopDTO) {
        // 多会话,需要删除HostUserEntity表记录
        UUID deskId = desktopDTO.getDeskId();
        List<HostUserEntity> hostUserList = hostUserService.findByDeskId(deskId);
        for (HostUserEntity hostUserEntity : hostUserList) {
            DesktopSessionDTO sessionDTO = desktopSessionServiceAPI.findByUserIdAndDeskId(hostUserEntity.getUserId(), deskId);
            // 用户不在池里时，回收离线和无会话的桌面
            if (checkRecoverWhenUserNotInPool(state, desktopDTO, hostUserEntity.getUserId(), Objects.isNull(sessionDTO))) {
                hostUserService.removeByUserIdAndDeskId(hostUserEntity.getUserId(), deskId);
                sessionDeleteLog(deskId, hostUserEntity.getUserId(), desktopDTO.getDesktopName());
            }
        }
    }

    private void sessionDeleteLog(UUID deskId, UUID userId, String desktopName) {
        try {
            String userName = userInfoAPI.getUserNameById(userId);
            auditLogAPI.recordLog(DesktopSessionBusinessKey.RCDC_RCO_DESKTOP_SESSION_DELETE_SUCCESS, userName, desktopName);
        } catch (Exception e) {
            LOGGER.error("桌面:{} 回收时查询用户:{} 信息异常:", deskId, userId, e);
        }
    }

    /**
     * 用户未分配到桌面池时，判断是否需要回收
     *
     * @param state         桌面状态
     * @param desktopDTO    桌面信息
     * @param userId        用户ID
     * @param connectClosed 连接关闭
     * @return true需要回收，false不需要回收
     */
    private boolean checkRecoverWhenUserNotInPool(CbbCloudDeskState state, PoolDesktopInfoDTO desktopDTO, UUID userId, boolean connectClosed) {
        if (desktopPoolUserMgmtAPI.checkUserInDesktopPool(desktopDTO.getDesktopPoolId(), userId)) {
            return false;
        }
        if (Objects.equals(state, desktopDTO.getDeskState())) {
            // 用户不在池中且已关机就回收
            return true;
        }
        // 已断连，回收
        return connectClosed;
    }

    /**
     * 动态池判断是否需要回收
     *
     * @param poolBasicDTO poolBasicDTO
     * @param desktopDTO   PoolDesktopInfoDTO
     * @return true需要回收，false不需要回收
     */
    private boolean checkDynamicDesktopRecover(DesktopPoolBasicDTO poolBasicDTO, PoolDesktopInfoDTO desktopDTO) {
        // 非关机状态就删除掉NEED_RECOVER_CLOSE_DESKTOP_MAP缓存
        UUID deskId = desktopDTO.getDeskId();
        if (desktopDTO.getDeskState() != CbbCloudDeskState.CLOSE) {
            removeDesktopRecoverInfoCache(deskId, CbbCloudDeskState.CLOSE);
        }

        Date connectClosedTime = desktopDTO.getConnectClosedTime();

        // 补充休眠且没有断连时间的桌面
        if (Objects.isNull(connectClosedTime) && desktopDTO.getDeskState() == CbbCloudDeskState.SLEEP) {
            userDesktopService.setConnectClosedTime(desktopDTO.getDeskId(), new Date());
            return false;
        }

        // 已关机且有用户的需要立即回收
        if (Objects.equals(desktopDTO.getDeskState(), CbbCloudDeskState.CLOSE) && Objects.nonNull(desktopDTO.getUserId())) {
            return true;
        }

        if (Objects.isNull(poolBasicDTO) || Objects.isNull(poolBasicDTO.getIdleDesktopRecover())) {
            return false;
        }

        if (CbbEstProtocolType.EST == desktopDTO.getEstProtocolType()) {
            if (Objects.isNull(connectClosedTime)) {
                // 带外异常场景:断连时间为空,且无会话连接时,则根据分配时间判断超过池设置的断开时间
                return !isAnyConnected(deskId) && meetRecyclingTime(desktopDTO.getAssignmentTime(),
                        poolBasicDTO.getIdleDesktopRecover(),
                        RECYCLING_ADD_TIME);
            }
            return meetRecyclingTime(connectClosedTime, poolBasicDTO.getIdleDesktopRecover(), 0);
        } else {
            // 带内:先判断分配时间是否满足回收时间,再判断无会话连接时,可以直接回收
            return meetRecyclingTime(desktopDTO.getAssignmentTime(), poolBasicDTO.getIdleDesktopRecover(), RECYCLING_ADD_TIME)
                    && !isAnyConnected(deskId);
        }
    }

    private boolean isAnyConnected(UUID deskId) {
        try {
            return desktopAPI.isAnyConnectedChannel(deskId);
        } catch (BusinessException e) {
            LOGGER.error("动态池桌面:{} 检查会话连接信息时异常:", deskId, e);
        }
        return true;
    }

    private void doRecoverDesktop(PoolDesktopInfoDTO desktopDTO) {
        UUID desktopId = desktopDTO.getDeskId();
        try {
            switch (desktopDTO.getDeskState()) {
                case SLEEP:
                    LOGGER.info("休眠桌面[{}]执行唤醒，本次不执行回收", desktopId);
                    startUpSleepDesktop(desktopDTO);
                    break;
                case RUNNING:
                    recoverRunningDesktop(desktopDTO);
                    break;
                case CLOSE:
                    recoverCloseDesktop(desktopDTO);
                    break;
                default:
                    LOGGER.info("桌面[{}]状态为[{}]无法执行回收任务", desktopId, desktopDTO.getDeskState());
            }
        } catch (Exception e) {
            LOGGER.error("关闭池桌面发生异常，desktopDTO={}，e={}", JSON.toJSONString(desktopDTO), e);
        }
    }

    private boolean meetRecyclingTime(Date startTime, Integer interval, int addTime) {
        if (Objects.isNull(startTime) || Objects.isNull(interval)) {
            return false;
        }
        // 判断当前时间是否已满足，当前时间 > 指定时间(连接关闭时间/分配时间 + 空闲时间)
        Calendar end = Calendar.getInstance();
        end.setTime(startTime);
        end.add(Calendar.MINUTE, interval > RECYCLING_ADD_TIME ? interval : interval + addTime);
        Calendar current = Calendar.getInstance();
        return current.after(end);
    }

    private void startUpSleepDesktop(PoolDesktopInfoDTO desktopDTO) {
        CloudDesktopStartRequest startRequest = new CloudDesktopStartRequest();
        startRequest.setId(desktopDTO.getDeskId());
        try {
            cloudDesktopOperateAPI.start(startRequest);
        } catch (Exception e) {
            LOGGER.error("休眠桌面[{}]执行唤醒发生错误", desktopDTO.getDeskId(), e);
            String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            baseSystemLogMgmtAPI.createSystemLog(new Date(), DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_SLEEP_DESKTOP_RECOVER_ERROR_LOG,
                    desktopDTO.getPoolName(), desktopDTO.getDesktopName(), msg);
        }
    }

    private void recoverCloseDesktop(PoolDesktopInfoDTO desktopDTO) throws BusinessException {
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopDTO.getDeskId());
        if (cbbDeskDTO.getDeskState() != CbbCloudDeskState.CLOSE) {
            LOGGER.warn("桌面[{}]非关机状态，不执行回收recoverCloseDesktop回收任务， state={}", desktopDTO.getDeskId(),
                    desktopDTO.getDeskState());
            return;
        }
        // 校验是否上一个任务是否有这个桌面的记录，有才回收，没有添加记录，PS: 这样是防止刚分配就被回收的卡点问题
        DesktopRecoverInfo recoverInfo = getDesktopRecoverInfoCache(desktopDTO.getDeskId(), CbbCloudDeskState.CLOSE);
        if (Objects.isNull(recoverInfo)) {
            // 没有记录就添加，下一轮再检查回收
            NEED_RECOVER_CLOSE_DESKTOP_MAP.put(desktopDTO.getDeskId(), new DesktopRecoverInfo(desktopDTO, CbbCloudDeskState.CLOSE));
            return;
        }
        // 判断是否是同一个用户，同一个用户的才删除绑定关系
        if (Objects.equals(recoverInfo.getUserId(), desktopDTO.getUserId())) {
            clearUserBind(desktopDTO);
            removeDesktopRecoverInfoCache(desktopDTO.getDeskId(), CbbCloudDeskState.CLOSE);
            return;
        }
        // 不是同一个用户重新put一下
        NEED_RECOVER_CLOSE_DESKTOP_MAP.put(desktopDTO.getDeskId(), new DesktopRecoverInfo(desktopDTO, CbbCloudDeskState.CLOSE));
    }

    private void recoverRunningDesktop(PoolDesktopInfoDTO desktopDTO) throws BusinessException {
        UUID desktopId = desktopDTO.getDeskId();
        LOGGER.info("开始执行关机并回收桌面[{}]", desktopId);

        // 再次查询确认是否还有连接使用桌面
        if (desktopAPI.isAnyConnectedChannel(desktopId)) {
            LOGGER.info("执行关机并回收桌面时，查询桌面仍然存在连接使用桌面[{}]，取消关机回收任务", desktopId);
            userDesktopService.clearConnectClosedTime(desktopId);
            removeDesktopRecoverInfoCache(desktopDTO.getDeskId(), CbbCloudDeskState.RUNNING);
            return;
        }

        // 先发送关机命令给GT进行关机，后面强制关机
        DesktopRecoverInfo recoverInfo = getDesktopRecoverInfoCache(desktopDTO.getDeskId(), CbbCloudDeskState.RUNNING);
        if (Objects.isNull(recoverInfo) || !Objects.equals(recoverInfo.getUserId(), desktopDTO.getUserId())) {
            // 没有关机的记录，或者用户不一样了重置关机次数
            recoverInfo = new DesktopRecoverInfo(desktopDTO, CbbCloudDeskState.RUNNING);
            NEED_RECOVER_CLOSE_DESKTOP_MAP.put(desktopDTO.getDeskId(), recoverInfo);
        }

        // 先gt关机，尝试失败后再强制关机
        if (recoverInfo.getRetryNum() < SHUTDOWN_RETRY_NUM) {
            // 先尝试下发关机命令
            OTHER_THREAD_POOL.execute(() -> shutdownDesktopAndClearUserBind(desktopDTO, false));
            recoverInfo.addRetryNum();
            LOGGER.info("桌面池[{}]回收任务，桌面[{}]尝试第{}次关机", desktopDTO.getDesktopPoolId(), desktopDTO.getDeskId(),
                    recoverInfo.getRetryNum());
            return;
        }

        // 强制关机
        shutdownDesktopAndClearUserBind(desktopDTO, true);
    }

    /**
     * 关机并删除用户和桌面的关联关系
     *
     * @param desktopDTO desktopDTO
     * @param force 是否强制
     */
    private void shutdownDesktopAndClearUserBind(PoolDesktopInfoDTO desktopDTO, boolean force) {
        if (shutdownDesktop(desktopDTO, force)) {
            clearUserBind(desktopDTO);
            removeDesktopRecoverInfoCache(desktopDTO.getDeskId(), CbbCloudDeskState.RUNNING);
        }
    }

    /**
     * 关机
     *
     * @param desktopDTO desktopDTO
     * @param force 是否强制
     */
    private boolean shutdownDesktop(PoolDesktopInfoDTO desktopDTO, boolean force) {
        try {
            desktopOperateRequestCache.removeCache(desktopDTO.getDeskId());
            CloudDesktopShutdownRequest shutdownRequest = new CloudDesktopShutdownRequest();
            shutdownRequest.setId(desktopDTO.getDeskId());
            shutdownRequest.setForce(force);
            cloudDesktopOperateAPI.shutdown(shutdownRequest);
            if (force) {
                // 强制关机记录日志
                baseSystemLogMgmtAPI.createSystemLog(new Date(), DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SCHEDULE_RECOVER_POWER_OFF_LOG,
                        desktopDTO.getPoolName(), desktopDTO.getDesktopName());
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("回收桌面[{}]时，尝试关机失败, force:{}", desktopDTO.getDeskId(), force, e);
            String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            baseSystemLogMgmtAPI.createSystemLog(new Date(), DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SCHEDULE_RECOVER_SHUTDOWN_FAIL,
                    desktopDTO.getPoolName(), desktopDTO.getDesktopName(), msg);
            return false;
        }
    }

    /**
     * 删除用户和桌面的关联关系
     *
     * @param desktopDTO desktopDTO
     */
    private void clearUserBind(PoolDesktopInfoDTO desktopDTO) {
        UUID desktopId = desktopDTO.getDeskId();
        userDesktopService.unbindUserAndDesktopRelation(desktopId);
        if (DesktopPoolType.STATIC == desktopDTO.getDesktopPoolType()) {
            // 静态池回收后，桌面要进入维护模式
            cbbDeskMgmtAPI.changeDeskMaintenanceModel(Lists.newArrayList(desktopId), true);
        }
        LOGGER.info("定时任务进行空闲池桌面[{}][{}]回收成功", desktopDTO.getDesktopName(), desktopId);
        baseSystemLogMgmtAPI.createSystemLog(new Date(), DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SCHEDULE_RECOVER, desktopDTO.getPoolName(),
                desktopDTO.getDesktopName());
    }

    private void removeDesktopRecoverInfoCache(UUID desktopId, CbbCloudDeskState deskState) {
        DesktopRecoverInfo recoverInfo = getDesktopRecoverInfoCache(desktopId, deskState);
        if (Objects.nonNull(recoverInfo)) {
            NEED_RECOVER_CLOSE_DESKTOP_MAP.remove(desktopId);
        }
    }

    private DesktopRecoverInfo getDesktopRecoverInfoCache(UUID desktopId, CbbCloudDeskState deskState) {
        DesktopRecoverInfo recoverInfo = NEED_RECOVER_CLOSE_DESKTOP_MAP.get(desktopId);
        if (Objects.nonNull(recoverInfo) && deskState == recoverInfo.getDesktopState()) {
            return recoverInfo;
        }
        // 返回空
        return null;
    }

    /**
     * 清理已经不存在的云桌面ID的记录
     *
     * @param desktopIdList desktopIdList
     */
    private void cleanNotExistDesktop(List<UUID> desktopIdList) {
        Set<UUID> desktopIdSet = Sets.newHashSet(desktopIdList);
        Set<UUID> cacheDesktopIdSet = NEED_RECOVER_CLOSE_DESKTOP_MAP.keySet();
        List<UUID> notExistIdList = cacheDesktopIdSet.stream().filter(id -> !desktopIdSet.contains(id)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(notExistIdList)) {
            return;
        }
        for (UUID id : notExistIdList) {
            NEED_RECOVER_CLOSE_DESKTOP_MAP.remove(id);
        }
    }

    /**
     * Description: 回收桌面信息
     * Copyright: Copyright (c) 2022
     * Company: Ruijie Co., Ltd.
     * Create Time: 2022/9/12
     *
     * @author linke
     */
    static class DesktopRecoverInfo {

        private UUID desktopId;

        private UUID userId;

        private CbbCloudDeskState desktopState;

        private int retryNum;

        DesktopRecoverInfo(PoolDesktopInfoDTO desktopDTO, CbbCloudDeskState desktopState) {
            this.desktopId = desktopDTO.getDeskId();
            this.userId = desktopDTO.getUserId();
            this.desktopState = desktopState;
            this.retryNum = 0;
        }

        /**
         * 增加次数
         */
        public void addRetryNum() {
            this.retryNum++;
        }

        public UUID getDesktopId() {
            return desktopId;
        }

        public void setDesktopId(UUID desktopId) {
            this.desktopId = desktopId;
        }

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

        public CbbCloudDeskState getDesktopState() {
            return desktopState;
        }

        public void setDesktopState(CbbCloudDeskState desktopState) {
            this.desktopState = desktopState;
        }

        public int getRetryNum() {
            return retryNum;
        }

        public void setRetryNum(int retryNum) {
            this.retryNum = retryNum;
        }
    }
}
