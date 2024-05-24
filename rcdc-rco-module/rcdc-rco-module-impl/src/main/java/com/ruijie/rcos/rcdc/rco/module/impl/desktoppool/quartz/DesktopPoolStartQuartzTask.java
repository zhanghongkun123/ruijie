package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolAPIHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.StartupTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.quartz.AbstractCloudDeskQuartzTask;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description: 池桌面定时启动任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/29
 *
 * @author linke
 */
@Service
@Quartz(taskGroup = TaskGroup.ADMIN_CONFIG, scheduleName = DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESKTOP_POOL_START,
        scheduleTypeCode = ScheduleTypeCodeConstants.DESKTOP_POOL_START_TYPR_CODE, blockInMaintenanceMode = true)
public class DesktopPoolStartQuartzTask extends AbstractCloudDeskQuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolStartQuartzTask.class);

    private static final int MAX_WORKING_NUM = 20;

    private static final long TASK_SLEEP_TIME = TimeUnit.SECONDS.toMillis(3);

    /**
     * 分配50个线程数处理池桌面开机
     */
    private static final ExecutorService START_THREAD_POOL = ThreadExecutors.newBuilder("desktopPoolStartQuartzTask")
            .maxThreadNum(20).queueSize(1000).build();

    private static final List<CbbCloudDeskState> DESKTOP_READY_START_LIST = Lists.newArrayList(CbbCloudDeskState.SLEEP, CbbCloudDeskState.CLOSE);

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolAPIHelper desktopPoolAPIHelper;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "DesktopPoolStartQuartzTask quartzTaskContext can not be null");
        CloudDeskQuartzData cloudDeskQuartzData = quartzTaskContext.getByType(CloudDeskQuartzData.class);

        // 查询动态池处于开机状态的数量
        if (ArrayUtils.isEmpty(cloudDeskQuartzData.getDesktopPoolArr())) {
            LOGGER.warn("桌面池定时启动任务执行结束，未配置任何桌面池，cloudDeskQuartzData:[{}]", JSON.toJSONString(cloudDeskQuartzData));
            return;
        }

        List<UUID> desktopPoolIdList = Arrays.asList(cloudDeskQuartzData.getDesktopPoolArr());
        List<CbbDesktopPoolDTO> desktopPoolList = cbbDesktopPoolMgmtAPI.listDesktopPoolByIdList(desktopPoolIdList);
        desktopPoolIdList = desktopPoolList.stream().filter(pool -> !pool.getIsOpenMaintenance()).map(CbbDesktopPoolDTO::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(desktopPoolIdList)) {
            LOGGER.warn("桌面池定时启动任务执行结束，无状态正常的可预启动的桌面池，cloudDeskQuartzData:[{}]", JSON.toJSONString(cloudDeskQuartzData));
            return;
        }
        List<PoolDesktopInfoDTO> desktopInfoList = desktopPoolService.listDesktopByDesktopPoolIds(desktopPoolIdList);
        if (CollectionUtils.isEmpty(desktopInfoList)) {
            LOGGER.warn("桌面池定时启动任务执行结束，无桌面，cloudDeskQuartzData:[{}]", JSON.toJSONString(cloudDeskQuartzData));
            return;
        }

        Queue<UUID> needStartDesktopIdQueue = buildNeedStartDesktopIdQueue(cloudDeskQuartzData.getAmount(), desktopInfoList);

        List<StartupTaskInfoDTO> workingList = new ArrayList<>();
        Map<UUID, CloudDesktopDetailDTO> desktopMap = new HashMap<>();
        while (true) {
            // 遍历检查执行中的任务
            checkWorkingList(workingList, desktopMap);

            // 添加启动桌面子任务
            while (workingList.size() < MAX_WORKING_NUM && !needStartDesktopIdQueue.isEmpty()) {
                UUID deskId = needStartDesktopIdQueue.poll();
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

        LOGGER.info("云桌面操作定时任务执行结束,cloudDeskQuartzData:[{}]", JSON.toJSONString(cloudDeskQuartzData));
    }

    private Queue<UUID> buildNeedStartDesktopIdQueue(int amount, List<PoolDesktopInfoDTO> desktopInfoList) {
        Map<UUID, List<PoolDesktopInfoDTO>> poolDesktopMap = desktopInfoList.stream().collect(
                Collectors.groupingBy(PoolDesktopInfoDTO::getDesktopPoolId));
        Queue<UUID> needStartDesktopIdQueue = new LinkedList<>();
        if (poolDesktopMap.isEmpty()) {
            return needStartDesktopIdQueue;
        }
        List<PoolDesktopInfoDTO> poolDesktopInfoList;
        for (Map.Entry<UUID, List<PoolDesktopInfoDTO>> entry : poolDesktopMap.entrySet()) {
            UUID poolId = entry.getKey();
            poolDesktopInfoList = entry.getValue();
            // 过滤出未绑定用户且策略一致且未绑定磁盘的桌面
            try {
                poolDesktopInfoList = desktopPoolAPIHelper.filterPoolDeskNotUserAndAccordWithList(poolId, poolDesktopInfoList);
            } catch (BusinessException e) {
                LOGGER.error("桌面池定时启动任务,池桌面[{}],过滤出未绑定用户且策略一致且未绑定磁盘的桌面,出现异常:{}", poolId, e);
                continue;
            }
            if (CollectionUtils.isEmpty(poolDesktopInfoList)) {
                LOGGER.warn("桌面池定时启动任务,池桌面[{}]下无未绑定用户且策略一致且未绑定磁盘的桌面，设置预启动数量为[{}]", poolId, amount);
                continue;
            }
            long runningNum = poolDesktopInfoList.stream().filter(desktop -> desktop.getDeskState() == CbbCloudDeskState.RUNNING).count();
            int diff = (int) (amount - runningNum);
            if (diff <= 0) {
                // 运行中数量已达标
                continue;
            }

            // 优先启动休眠的桌面
            for (CbbCloudDeskState deskState : DESKTOP_READY_START_LIST) {
                if (diff <= 0) {
                    break;
                }
                List<UUID> idList = poolDesktopInfoList.stream().filter(desktop -> desktop.getDeskState() == deskState)
                        .map(PoolDesktopInfoDTO::getDeskId).collect(Collectors.toList());
                if (diff > idList.size()) {
                    needStartDesktopIdQueue.addAll(idList);
                } else {
                    // 打乱顺序
                    Collections.shuffle(idList);
                    needStartDesktopIdQueue.addAll(idList.subList(0, diff));
                }
                diff -= idList.size();
                LOGGER.info("桌面池定时启动任务,启动数量:[{}],查询桌面状态:[{}]的数量为:[{}],剩余启动数量:[{}]", amount, deskState.name(), idList.size(), diff);
            }
        }
        return needStartDesktopIdQueue;
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
            LOGGER.error("桌面池定时启动任务,查询状态机无任务taskInfo[{}]", JSON.toJSONString(taskInfo), e);
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                cloudDesktopMap.remove(taskInfo.getDeskId());
            }
            return;
        } catch (Exception e) {
            LOGGER.error("桌面池定时启动任务,查看启动桌面[{}]状态机[{}]任务异常", taskInfo.getDeskId(), taskInfo.getTaskId(), e);
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_STARTUP_ERROR_LOG, new String[] {cloudDesktop.getDesktopName(),
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
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_STARTUP_ERROR_LOG, new String[] {cloudDesktop.getDesktopName(),
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
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_STARTUP_SUCCESS_LOG, new String[] {cloudDesktop.getDesktopName()});
                cloudDesktopMap.remove(taskInfo.getDeskId());
            }
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
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_STARTUP_ERROR_LOG, new String[] {cloudDesktopDetailDTO.getDesktopName(),
                        e.getI18nMessage()});
            } catch (Exception e) {
                LOGGER.error("执行启动池桌面失败。deskId:[{}]", deskId, e);
                // 添加审计日志
                addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESK_STARTUP_ERROR_LOG, new String[] {cloudDesktopDetailDTO.getDesktopName(),
                        e.getMessage()});
            }
        });
        workingList.add(new StartupTaskInfoDTO(deskId, taskId));
        cloudDesktopMap.put(deskId, cloudDesktopDetailDTO);
    }

    private CloudDesktopDetailDTO getCloudDesktopDetail(UUID deskId) {
        try {
            return userDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESKTOP_POOL_START_FAIL_SYSTEM_LOG,
                    new String[] {deskId.toString(), e.getI18nMessage()});
            LOGGER.error(String .format("执行启动池桌面失败。deskId:[%s]", deskId), e);
            // 云桌面信息为空
            return null;
        } catch (Exception e) {
            String message = getUnknownErrorMsg();
            addSystemLog(DesktopPoolBusinessKey.RCDC_RCO_QUARTZ_DESKTOP_POOL_START_FAIL_SYSTEM_LOG, new String[] {deskId.toString(), message});
            LOGGER.error(String .format("执行启动池桌面失败。deskId:[%s]", deskId), e);
            // 云桌面信息为空
            return null;
        }
    }

    private String getUnknownErrorMsg() {
        return LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESK_STARTUP_UNKNOWN_ERROR, new String[] {});
    }

    @Override
    protected void executeOperator(UUID deskId, Set<UUID> userGroupChildrenSet, List<UUID> userIdList, List<UUID> deskIdList) {
        // 无需实现
    }

    @Override
    protected ThreadExecutor getThreadExecutor() {
        // 未使用
        return null;
    }

    /**
     * 查询所有状态的云桌面,所以返回空数组
     * 
     * @return 数组
     */
    @Override
    protected String[] getNeedHandleState() {
        return new String[] {};
    }

}
