package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.maintenance.module.def.api.BusinessMaintenanceAPI;
import com.ruijie.rcos.rcdc.maintenance.module.def.dto.enums.BusinessMaintenanceStatusEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolAPIHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.StartupTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description: 池桌面维持预启动数定时任务,每分钟执行
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/28
 *
 * @author WuShengQiang
 */
@Service
@Quartz(scheduleTypeCode = "desktop_of_pool_pre_start", scheduleName = DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESKTOP_OF_POOL_PRE_START,
        cron = "0 0/1 * * * ?")
public class DesktopPoolPreStartQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolPreStartQuartzTask.class);

    private static final List<CbbCloudDeskState> DESKTOP_READY_START_LIST = Lists.newArrayList(CbbCloudDeskState.SLEEP, CbbCloudDeskState.CLOSE);

    private static final int MAX_WORKING_NUM = 20;

    private static final long TASK_SLEEP_TIME = TimeUnit.SECONDS.toMillis(3);

    /**
     * 分配20个线程数处理池桌面开机
     */
    private static final ExecutorService START_THREAD_POOL = ThreadExecutors.newBuilder("desktopPoolPreStartQuartzTask")
            .maxThreadNum(20).queueSize(1000).build();

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private DesktopPoolAPIHelper desktopPoolAPIHelper;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Autowired
    private BusinessMaintenanceAPI businessMaintenanceAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {

        SystemMaintenanceState maintenanceMode = maintenanceModeMgmtAPI.getMaintenanceMode();
        BusinessMaintenanceStatusEnums businessMaintenanceMode = businessMaintenanceAPI.getBusinessStatus();
        if (maintenanceMode != SystemMaintenanceState.NORMAL || businessMaintenanceMode != BusinessMaintenanceStatusEnums.NORMAL) {
            LOGGER.info("维持预启动数定时任务不执行,当前系统维护模式:{},业务维护模式:{}", maintenanceMode, businessMaintenanceMode);
            return;
        }

        // 1.获取符合状态的池列表
        List<DesktopPoolBasicDTO> desktopPoolList = desktopPoolService.listDesktopPoolByPoolModel(Lists.newArrayList(CbbDesktopPoolModel.DYNAMIC));
        desktopPoolList = desktopPoolList.stream().filter(pool -> pool.getPoolState() == CbbDesktopPoolState.AVAILABLE
                && !pool.getIsOpenMaintenance() && pool.getPreStartDesktopNum() > 0).collect(Collectors.toList());
        LOGGER.info("维持预启动数定时任务,获取可用状态且未开启维护模式且预启动数大于0的动态池列表,总数:{}", desktopPoolList.size());
        if (CollectionUtils.isEmpty(desktopPoolList)) {
            return;
        }

        Queue<UUID> desktopIdQueue = new LinkedList<>();
        for (DesktopPoolBasicDTO poolBasicDTO : desktopPoolList) {
            UUID poolId = poolBasicDTO.getId();
            List<PoolDesktopInfoDTO> desktopInfoList = desktopPoolService.listNormalDeskInfoByDesktopPoolId(poolId);
            // 2.过滤出未绑定用户且策略一致且未绑定磁盘的桌面
            try {
                desktopInfoList = desktopPoolAPIHelper.filterPoolDeskNotUserAndAccordWithList(poolId, desktopInfoList);
            } catch (Exception e) {
                LOGGER.error("维持预启动数定时任务,池桌面[{}],过滤出未绑定用户且策略一致的桌面,出现异常:{}", poolId, e);
                continue;
            }
            // 3.处理需要预启动的桌面ID队列
            handleNeedPreStartDesktopId(poolBasicDTO, desktopInfoList, desktopIdQueue);
        }

        int queueSize = desktopIdQueue.size();
        List<StartupTaskInfoDTO> workingList = new ArrayList<>();
        Map<UUID, CloudDesktopDetailDTO> desktopMap = new HashMap<>();
        while (true) {
            // 遍历检查执行中的任务
            checkWorkingList(workingList, desktopMap);

            // 添加启动桌面子任务
            while (workingList.size() < MAX_WORKING_NUM && !desktopIdQueue.isEmpty()) {
                UUID deskId = desktopIdQueue.poll();
                CloudDesktopDetailDTO cloudDesktopDetailDTO = getCloudDesktopDetail(deskId);
                if (cloudDesktopDetailDTO == null) {
                    continue;
                }
                addStartupTask(cloudDesktopDetailDTO, workingList, desktopMap);
            }
            if (workingList.isEmpty()) {
                break;
            }
            Thread.sleep(TASK_SLEEP_TIME);
        }

        LOGGER.info("维持预启动数定时任务执行结束,预启动桌面队列大小:[{}]", queueSize);
    }

    private void handleNeedPreStartDesktopId(DesktopPoolBasicDTO poolBasicDTO, List<PoolDesktopInfoDTO> desktopInfoList,
                                             Queue<UUID> desktopIdQueue) {
        Integer preStartNum = poolBasicDTO.getPreStartDesktopNum();
        if (CollectionUtils.isEmpty(desktopInfoList)) {
            LOGGER.warn("维持预启动数定时任务,池桌面[{}]下无未绑定用户且策略一致且未绑定磁盘的桌面，设置预启动数量为[{}]", poolBasicDTO.getId(), preStartNum);
            return;
        }
        // 运行中,启动中,唤醒中 都属于已启动数量
        long runningNum = desktopInfoList.stream().filter(desktop -> desktop.getDeskState() == CbbCloudDeskState.RUNNING
                || desktop.getDeskState() == CbbCloudDeskState.START_UP || desktop.getDeskState() == CbbCloudDeskState.WAKING).count();
        int diff = (int) (preStartNum - runningNum);
        if (diff <= 0) {
            // 运行中数量已达标
            return;
        }

        // 优先启动休眠的桌面
        for (CbbCloudDeskState deskState : DESKTOP_READY_START_LIST) {
            if (diff <= 0) {
                break;
            }
            List<UUID> idList = desktopInfoList.stream().filter(desktop -> desktop.getDeskState() == deskState)
                    .map(PoolDesktopInfoDTO::getDeskId).collect(Collectors.toList());
            if (diff > idList.size()) {
                desktopIdQueue.addAll(idList);
            } else {
                // 打乱顺序
                Collections.shuffle(idList);
                desktopIdQueue.addAll(idList.subList(0, diff));
            }
            diff -= idList.size();
            LOGGER.info("维持预启动数定时任务,设置[{}],已启动[{}],查询桌面状态[{}]数量为[{}],剩余数量:[{}]",
                    preStartNum, runningNum, deskState.name(), idList.size(), diff);
        }
    }

    private void checkWorkingList(List<StartupTaskInfoDTO> workingList, Map<UUID, CloudDesktopDetailDTO> cloudDesktopMap) {
        if (workingList.isEmpty()) {
            return;
        }
        List<StartupTaskInfoDTO> removeList = new ArrayList<>();
        for (StartupTaskInfoDTO taskInfo : workingList) {
            checkTaskResult(taskInfo, removeList, cloudDesktopMap);
        }

        workingList.removeAll(removeList);
    }

    private void checkTaskResult(StartupTaskInfoDTO taskInfo, List<StartupTaskInfoDTO> removeList, Map<UUID, CloudDesktopDetailDTO> cloudDesktopMap) {
        // 查询状态机
        StateMachineMgmtAgent stateMachineMgmtAgent;
        try {
            stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskInfo.getTaskId());
        } catch (NoSuchElementException e) {
            LOGGER.error("维持预启动数定时任务,查询状态机无任务taskInfo[{}]", JSON.toJSONString(taskInfo), e);
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                cloudDesktopMap.remove(taskInfo.getDeskId());
            }
            return;
        } catch (Exception e) {
            LOGGER.error("维持预启动数定时任务,查看启动桌面[{}]状态机[{}]任务异常", taskInfo.getDeskId(), taskInfo.getTaskId(), e);
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_PRE_STARTUP_ERROR_LOG, new String[] {cloudDesktop.getDesktopName(),
                        getUnknownErrorMsg()});
                cloudDesktopMap.remove(taskInfo.getDeskId());
            }
            return;
        }

        StateMachineMgmtAgent.SmRunningState runningState = stateMachineMgmtAgent.getRunningState();
        if (runningState != StateMachineMgmtAgent.SmRunningState.FINISHED) {
            // 未结束
            return;
        }
        if (StateMachineMgmtAgent.SmExecutionStage.UNDO == stateMachineMgmtAgent.getExecutionStage()) {
            // 任务失败
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                StateMachineMgmtAgent.ExceptionInfo errorInfo = stateMachineMgmtAgent.getExceptionInfo();
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_PRE_STARTUP_ERROR_LOG, new String[] {cloudDesktop.getDesktopName(),
                        Objects.isNull(errorInfo) ? getUnknownErrorMsg() : errorInfo.getExceptionMessage()});
                cloudDesktopMap.remove(taskInfo.getDeskId());
            }
            return;
        }
        if (StateMachineMgmtAgent.SmExecutionStage.DO == stateMachineMgmtAgent.getExecutionStage()) {
            // 任务成功
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_PRE_STARTUP_SUCCESS_LOG, new String[] {cloudDesktop.getDesktopName()});
                cloudDesktopMap.remove(taskInfo.getDeskId());
            }
        }
    }

    private CloudDesktopDetailDTO getCloudDesktopDetail(UUID deskId) {
        try {
            return userDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_PRE_STARTUP_ERROR_LOG,
                    new String[] {deskId.toString(), e.getI18nMessage()});
            LOGGER.error(String.format("执行启动池桌面失败。deskId:[%s]", deskId), e);
            // 云桌面信息为空
            return null;
        } catch (Exception e) {
            String message = getUnknownErrorMsg();
            addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_PRE_STARTUP_ERROR_LOG, new String[] {deskId.toString(), message});
            LOGGER.error(String.format("执行启动池桌面失败。deskId:[%s]", deskId), e);
            // 云桌面信息为空
            return null;
        }
    }

    private void addStartupTask(CloudDesktopDetailDTO cloudDesktopDetailDTO, List<StartupTaskInfoDTO> workingList,
                                Map<UUID, CloudDesktopDetailDTO> cloudDesktopMap) {
        UUID deskId = cloudDesktopDetailDTO.getId();
        UUID taskId = UUID.randomUUID();

        // 启动桌面
        START_THREAD_POOL.execute(() -> {
            try {
                userDesktopOperateAPI.start(new CloudDesktopStartRequest(deskId, taskId));
            } catch (BusinessException e) {
                LOGGER.error("执行启动池桌面失败。deskId:[{}]", deskId, e);
                // 添加审计日志
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_PRE_STARTUP_ERROR_LOG, new String[] {cloudDesktopDetailDTO.getDesktopName(),
                        e.getI18nMessage()});
            } catch (Exception e) {
                LOGGER.error("执行启动池桌面失败。deskId:[{}]", deskId, e);
                // 添加审计日志
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_PRE_STARTUP_ERROR_LOG, new String[] {cloudDesktopDetailDTO.getDesktopName(),
                        e.getMessage()});
            }
        });

        workingList.add(new StartupTaskInfoDTO(deskId, taskId));
        cloudDesktopMap.put(deskId, cloudDesktopDetailDTO);
    }

    private void addSystemLog(String businessKey, String[] args) {
        BaseCreateSystemLogRequest createRequest = new BaseCreateSystemLogRequest(businessKey, args);
        baseSystemLogMgmtAPI.createSystemLog(createRequest);
    }

    private String getUnknownErrorMsg() {
        return LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESK_STARTUP_UNKNOWN_ERROR);
    }
}
