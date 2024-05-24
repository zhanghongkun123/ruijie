package com.ruijie.rcos.rcdc.rco.module.impl.deskbackup.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskBackupAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbCreateDeskBackupDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskBackupAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.deskbackup.DeskBackupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.quartz.AbstractCloudDeskQuartzTask;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/29
 *
 * @author wuShengQiang
 */
@Service
@Quartz(taskGroup = TaskGroup.ADMIN_CONFIG, scheduleName = DeskBackupBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_BACKUP,
    scheduleTypeCode = ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_BACKUP_TYPR_CODE, blockInMaintenanceMode = true)
public class CloudDeskCreateBackupQuartzTask extends AbstractCloudDeskQuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDeskCreateBackupQuartzTask.class);

    private static final int MAX_WORKING_NUM = 10;

    private static final int TASK_TIME_UNLIMITED = 0;

    private static final long TASK_SLEEP_TIME = TimeUnit.SECONDS.toMillis(5);

    private static final long ONE_HOUR_MILLIS = 3600000L;

    private static final Set<String> DESK_EXPECT_STATE =
        Sets.newHashSet(CbbCloudDeskState.RUNNING.name(), CbbCloudDeskState.CLOSE.name(), CbbCloudDeskState.SLEEP.name());

    @Autowired
    private DeskBackupAPI deskBackupAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;
    
    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "CloudDeskCreateBackupQuartzTask quartzTaskContext can not be null");
        CloudDeskQuartzData cloudDeskQuartzData = quartzTaskContext.getByType(CloudDeskQuartzData.class);
        List<UUID> deskIdList = getDeskIdArr(cloudDeskQuartzData, checkStaticPoolIsSupport());

        UUID[] deskPoolIdArr = Optional.ofNullable(cloudDeskQuartzData.getDesktopPoolArr()).orElse(new UUID[0]);
        List<UUID> deskPoolIdList = Stream.of(deskPoolIdArr).collect(Collectors.toList());

        UUID[] userIdArr = Optional.ofNullable(cloudDeskQuartzData.getUserArr()).orElse(new UUID[0]);
        List<UUID> userIdList = Arrays.stream(userIdArr).collect(Collectors.toList());

        // 获取所有用户组
        UUID[] userGroupIdArr = Optional.ofNullable(cloudDeskQuartzData.getUserGroupArr()).orElse(new UUID[0]);
        List<UUID> userGroupList = Stream.of(userGroupIdArr).collect(Collectors.toList());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("云桌面定时任务配置涉及所有用户组id[{}]", JSON.toJSONString(userGroupList));
        }
        // 查询所有桌面id
        // 云桌面备份适配云平台，升级场景：platformId为空，则取默认云平台
        UUID platformId = cloudDeskQuartzData.getPlatformId();
        platformId = Objects.isNull(platformId) ? cloudPlatformManageAPI.getDefaultCloudPlatform().getId() : platformId;
        List<CloudDesktopDTO> cloudDesktopDTOList =
                userDesktopMgmtAPI.listByDeskIdsOrPoolIdsOrUserIdsOrUserGroupIdsAndPlatformId(deskIdList, 
                        deskPoolIdList, userIdList, userGroupList, platformId);
        Queue<CloudDesktopDTO> backupTaskInfoQueue = new LinkedList<>(cloudDesktopDTOList);
        List<BackupTaskInfo> workingList = new ArrayList<>();
        Map<UUID, CloudDesktopDTO> cloudDesktopMap = new HashMap<>();

        Integer backupDuration = cloudDeskQuartzData.getBackupDuration();
        long endTime = System.currentTimeMillis() + backupDuration * ONE_HOUR_MILLIS;

        while (true) {
            // 遍历检查执行中的任务
            checkBackupTaskResult(workingList, cloudDesktopMap);
            // 添加备份子任务
            while (!isTaskTimeOut(backupDuration, endTime) && workingList.size() < MAX_WORKING_NUM && !backupTaskInfoQueue.isEmpty()) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                CloudDesktopDTO desktopDTO = backupTaskInfoQueue.poll();
                if (desktopDTO == null) {
                    break;
                }
                if (validateDesk(stopwatch, desktopDTO)) {
                    addBackupTask(stopwatch, cloudDeskQuartzData.getExtStorageId(), desktopDTO, workingList, cloudDesktopMap);
                }
            }
            Thread.sleep(TASK_SLEEP_TIME);
            if (workingList.isEmpty()) {
                break;
            }
        }

        // 已达到备份最长持续时间,日志记录未执行的备份
        backupTaskInfoQueue.forEach(cloudDesktopDTO -> {
            Stopwatch stopwatch = Stopwatch.createStarted();
            if (validateDesk(stopwatch, cloudDesktopDTO)) {
                addSystemLog(DeskBackupBusinessKey.RCDC_RCO_QUARTZ_DESK_BACKUP_TIME_OUT_ERROR_LOG,
                    new String[]{cloudDesktopDTO.getDesktopName()});
            }
        });


        LOGGER.info("云桌面操作定时任务执行结束,cloudDeskQuartzData:[{}]", JSON.toJSONString(cloudDeskQuartzData));
    }

    private void checkBackupTaskResult(List<BackupTaskInfo> workingList, Map<UUID, CloudDesktopDTO> cloudDesktopMap) {
        if (workingList.isEmpty()) {
            return;
        }
        List<BackupTaskInfo> removeList = new ArrayList<>();
        for (BackupTaskInfo taskInfo : workingList) {
            Stopwatch stopwatch = taskInfo.getStopwatch();
            StateMachineMgmtAgent stateMachineMgmtAgent = null;
            try {
                stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskInfo.getTaskId());
            } catch (Exception e) {
                LOGGER.error("查看备份状态机任务异常", e);
            }

            StateMachineMgmtAgent.SmRunningState runningState = stateMachineMgmtAgent == null ? null : stateMachineMgmtAgent.getRunningState();
            if (runningState == StateMachineMgmtAgent.SmRunningState.RUNNING) {
                continue;
            }
            removeList.add(taskInfo);
            CloudDesktopDTO cloudDesktop = cloudDesktopMap.remove(taskInfo.getDeskId());
            if (cloudDesktop == null) {
                continue;
            }
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            String businessKey = DeskBackupBusinessKey.RCDC_RCO_QUARTZ_DESK_BACKUP_CREATE_SUCCESS_LOG;
            String[] argArr = new String[]{cloudDesktop.getDesktopName(), timeMillis};
            if (runningState == null) {
                businessKey = DeskBackupBusinessKey.RCDC_RCO_QUARTZ_DESK_BACKUP_CREATE_ERROR_LOG;
                argArr = new String[]{cloudDesktop.getDesktopName(),
                    LocaleI18nResolver.resolve(DeskBackupBusinessKey.RCDC_RCO_DESK_BACKUP_NUKNOWN_ERROR, new String[]{}),
                    timeMillis};
            } else {
                if (StateMachineMgmtAgent.SmExecutionStage.UNDO == stateMachineMgmtAgent.getExecutionStage()) {
                    // 任务失败
                    businessKey = DeskBackupBusinessKey.RCDC_RCO_QUARTZ_DESK_BACKUP_CREATE_ERROR_LOG;
                    StateMachineMgmtAgent.ExceptionInfo exceptionInfo = stateMachineMgmtAgent.getExceptionInfo();
                    String exceptionInfoArg = exceptionInfo == null ?
                        LocaleI18nResolver.resolve(DeskBackupBusinessKey.RCDC_RCO_DESK_BACKUP_NUKNOWN_ERROR, new String[]{})
                        : exceptionInfo.getExceptionMessage();
                    argArr = new String[]{cloudDesktop.getDesktopName(), exceptionInfoArg, timeMillis};
                }
            }
            addSystemLog(businessKey, argArr);
        }
        workingList.removeAll(removeList);
    }

    private boolean validateDesk(Stopwatch stopwatch, CloudDesktopDTO cloudDesktopDTO) {
        UUID deskId = cloudDesktopDTO.getId();
        String deskName = cloudDesktopDTO.getDesktopName();
        // 2、过滤IDV云桌面不支持创建备份
        if (CbbCloudDeskType.IDV.name().equals(cloudDesktopDTO.getDesktopType())) {
            LOGGER.info("IDV云桌面不支持创建备份。桌面id：{}，桌面名称：{}", deskId, deskName);
            return false;
        }

        // 3、只能对处于休眠、关机、运行中状态的云桌面执行创建备份操作
        if (!DESK_EXPECT_STATE.contains(cloudDesktopDTO.getDesktopState())) {
            LOGGER.info("桌面[{}]状态[{}],不支持创建桌面备份", deskName, cloudDesktopDTO.getDesktopState());
            String desktopState =
                LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_CLOUDDESKTOP_STATE_PRE + cloudDesktopDTO.getDesktopState().toLowerCase());
            String message = LocaleI18nResolver.resolve(DeskBackupBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_BACKUP_BY_DESKTOP_STATE_UNSUPPORT,
                desktopState);
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(DeskBackupBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_BACKUP_FAIL_SYSTEM_LOG,
                new String[]{deskName, message, timeMillis});
            return false;
        }
        //检查桌面池状态
        try {
            if (Objects.nonNull(cloudDesktopDTO.getDesktopPoolId())) {
                desktopPoolMgmtAPI.checkDesktopPoolMaintenanceReady(cloudDesktopDTO.getDesktopPoolId());
            }
        } catch (BusinessException e) {
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(DeskBackupBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_BACKUP_FAIL_SYSTEM_LOG,
                    new String[]{cloudDesktopDTO.getDesktopName(), e.getI18nMessage(), timeMillis});
            LOGGER.error("执行创建云桌面[{}]备份失败,deskId:[{}]", cloudDesktopDTO.getDesktopName(), cloudDesktopDTO.getId(), e);
            return false;
        }

        //4.最近登录时间如果>t_cbb_desk_backup表中记录的上次登录时间才进行备份
        return cbbVDIDeskBackupAPI.needDoBackup(cloudDesktopDTO.getId(), cloudDesktopDTO.getLatestLoginTime());
    }

    private void addBackupTask(Stopwatch stopwatch, UUID storageId, CloudDesktopDTO cloudDesktopDTO, List<BackupTaskInfo> workingList,
                               Map<UUID, CloudDesktopDTO> cloudDesktopMap) throws BusinessException {
        UUID deskId = cloudDesktopDTO.getId();

        String backupName = deskBackupAPI.generateBackupNameByDesktopName(cloudDesktopDTO.getDesktopName());

        CbbCreateDeskBackupDTO cbbCreateDeskBackupDTO = new CbbCreateDeskBackupDTO();
        UUID taskId = UUID.randomUUID();
        cbbCreateDeskBackupDTO.setTaskId(taskId);
        cbbCreateDeskBackupDTO.setDeskBackupId(UUID.randomUUID());
        cbbCreateDeskBackupDTO.setDeskId(deskId);
        cbbCreateDeskBackupDTO.setName(backupName);
        cbbCreateDeskBackupDTO.setExtStorageId(storageId);
        if (!CbbCloudDeskState.RUNNING.name().equals(cloudDesktopDTO.getDesktopState())) {
            Date latestLoginTime = cloudDesktopDTO.getLatestLoginTime() == null ? new Date() : cloudDesktopDTO.getLatestLoginTime();
            cbbCreateDeskBackupDTO.setLatestLoginTime(latestLoginTime);
        }
        try {
            deskBackupAPI.createDeskBackup(cbbCreateDeskBackupDTO);
            workingList.add(new BackupTaskInfo(deskId, taskId, stopwatch));
            cloudDesktopMap.put(deskId, cloudDesktopDTO);
        } catch (BusinessException e) {
            LOGGER.error("定时备份任务添加桌面[{}]备份失败", cloudDesktopDTO.getDesktopName(), e);
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(DeskBackupBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_BACKUP_FAIL_SYSTEM_LOG,
                    new String[]{cloudDesktopDTO.getDesktopName(), e.getI18nMessage(), timeMillis});
        }

    }

    private boolean isTaskTimeOut(Integer backupDuration, long endTime) {
        if (TASK_TIME_UNLIMITED == backupDuration) {
            return false;
        }
        return System.currentTimeMillis() >= endTime;
    }

    @Override
    protected void executeOperator(UUID deskId, Set<UUID> userGroupChildrenSet, List<UUID> userIdList, List<UUID> deskIdList) {

    }

    @Override
    protected boolean checkStaticPoolIsSupport() {
        return true;
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
        return new String[]{};
    }

    /**
     * Description: 备份任务临时信息
     * Copyright: Copyright (c) 2021
     * Company: Ruijie Co., Ltd.
     * Create Time: 2021/5/16
     *
     * @author linke
     */
    static class BackupTaskInfo {
        private UUID deskId;

        private UUID taskId;

        private Stopwatch stopwatch;

        BackupTaskInfo(UUID deskId, UUID taskId, Stopwatch stopwatch) {
            this.deskId = deskId;
            this.taskId = taskId;
            this.stopwatch = stopwatch;
        }

        @Override
        public boolean equals(Object o) {
            Assert.notNull(o, "Object can not be null");
            if (this == o) {
                return true;
            }
            if (getClass() != o.getClass()) {
                return false;
            }
            BackupTaskInfo that = (BackupTaskInfo) o;
            return Objects.equals(deskId, that.deskId) && Objects.equals(taskId, that.taskId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(deskId, taskId);
        }

        public UUID getDeskId() {
            return deskId;
        }

        public void setDeskId(UUID deskId) {
            this.deskId = deskId;
        }

        public UUID getTaskId() {
            return taskId;
        }

        public void setTaskId(UUID taskId) {
            this.taskId = taskId;
        }

        public Stopwatch getStopwatch() {
            return stopwatch;
        }

        public void setStopwatch(Stopwatch stopwatch) {
            this.stopwatch = stopwatch;
        }
    }
}
