package com.ruijie.rcos.rcdc.rco.module.impl.desksnapshot.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskSnapshotAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotUserType;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSnapshotAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desksnapshot.DeskSnapshotBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.quartz.AbstractCloudDeskQuartzTask;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description: 处理定时任务创建云桌面快照
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月10日
 *
 * @author luojianmo
 */
@Service
@Quartz(taskGroup = TaskGroup.ADMIN_CONFIG, scheduleName = DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_SNAPSHOT,
        scheduleTypeCode = ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_SNAPSHOT_TYPR_CODE, blockInMaintenanceMode = true)
public class CloudDeskCreateSnapshotQuartzTask extends AbstractCloudDeskQuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDeskCreateSnapshotQuartzTask.class);

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 10000;

    /**
     * 线程池名称
     */
    private static final String THREAD_POOL_NAME = "create-desk-snapshot";

    /**
     * 超级管理员
     */
    private static final String CREATE_SNAPSHOT_ADMIN = "admin";

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI;

    @Autowired
    private DeskSnapshotAPI deskSnapshotAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Override
    protected void executeOperator(UUID deskId, Set<UUID> userGroupChildrenSet, List<UUID> userIdList, List<UUID> deskIdList) {
        LOGGER.info("开始执行云桌面[{}]创建快照操作", deskId);
        Stopwatch stopwatch = Stopwatch.createStarted();
        CloudDesktopDetailDTO cloudDesktopDetailDTO;
        String deskName = deskId.toString();
        try {
            cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            deskName = cloudDesktopDetailDTO.getDesktopName();
            if (Objects.nonNull(cloudDesktopDetailDTO.getDesktopPoolId())) {
                desktopPoolMgmtAPI.checkDesktopPoolMaintenanceReady(cloudDesktopDetailDTO.getDesktopPoolId());
            }
        } catch (BusinessException e) {
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_SNAPSHOT_FAIL_SYSTEM_LOG,
                    new String[]{deskName, e.getI18nMessage(), timeMillis});
            LOGGER.error("执行创建云桌面快照失败。deskId = " + deskId, e);
            return;
        } catch (Exception e) {
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            String message = LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_RCO_DESK_SNAPSHOT_NUKNOWN_ERROR, new String[]{});
            addSystemLog(DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_SNAPSHOT_FAIL_SYSTEM_LOG,
                    new String[]{deskName, message, timeMillis});
            LOGGER.error("执行创建云桌面快照失败。deskId = " + deskId, e);
            return;
        }

        // 1、确认原来只是通过用户组勾选的方式加入的用户是否发生切换组：
        UUID curUserGroupId = cloudDesktopDetailDTO.getUserGroupId();
        UUID userId = cloudDesktopDetailDTO.getUserId();
        if (!deskIdList.contains(deskId) && !userIdList.contains(userId) && !userGroupChildrenSet.contains(curUserGroupId)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("桌面[桌面id：{},用户id：{},用户组id：{}]不在用户组[{}],用户[{}],桌面[{}]中不执行创建云桌面操作", deskId, userId, curUserGroupId,
                        JSON.toJSONString(userGroupChildrenSet), JSON.toJSONString(userIdList), JSON.toJSONString(deskIdList));
            }
            return;
        }

        // 2、过滤IDV云桌面不支持创建快照
        if (CbbCloudDeskType.IDV.name().equals(cloudDesktopDetailDTO.getDeskType()) ||
                CbbCloudDeskType.THIRD.name().equals(cloudDesktopDetailDTO.getDeskType())) {
            LOGGER.info("IDV和THIRD_PARTY云桌面不支持创建快照。桌面id：{}，桌面名称：{}", deskId, deskName);
            return;
        }

        // 3、只能对处于休眠、关机、运行中状态的云桌面执行创建快照操作
        Set<String> expectState = Arrays.stream(getNeedHandleState()).collect(Collectors.toSet());
        if (!expectState.contains(cloudDesktopDetailDTO.getDesktopState())) {
            LOGGER.info("桌面[{}]状态[{}],不支持创建桌面快照", deskName, cloudDesktopDetailDTO.getDesktopState());
            String desktopState =
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_CLOUDDESKTOP_STATE_PRE + cloudDesktopDetailDTO.getDesktopState().toLowerCase());
            String message = LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_SNAPSHOT_BY_DESKTOP_STATE_UNSUPPORT,
                    desktopState);
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_SNAPSHOT_FAIL_SYSTEM_LOG,
                    new String[]{deskName, message, timeMillis});
            return;
        }

        // 4、过滤掉最后在线时间早于最后备份时间的云桌面，不做备份
        Date latestLoginTime = cloudDesktopDetailDTO.getLatestLoginTime();
        if (!CbbCloudDeskState.RUNNING.name().equals(cloudDesktopDetailDTO.getDesktopState())
                && !cbbVDIDeskSnapshotAPI.needDoSnapshot(deskId, latestLoginTime)) {
            LOGGER.info("桌面[{}]无需定时备份", deskId);
            // 添加审计日志：桌面[{}]未新增数据无需备份
            addSystemLog(DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_NO_DATA_TO_DO_SNAPSHOT, new String[]{deskName});
            return;
        }

        try {
            CbbDeskSnapshotUserType userType = CbbDeskSnapshotUserType.SYSTEM;
            UUID userAdminId = adminMgmtAPI.getAdminByUserName(CREATE_SNAPSHOT_ADMIN).getId();

            String name = deskSnapshotAPI.generateSnapshotNameByDesktopName(deskName);
            UUID taskId = UUID.randomUUID();
            UUID deskSnapshotId = UUID.randomUUID();
            CbbCreateDeskSnapshotDTO cbbCreateDeskSnapshotDTO = new CbbCreateDeskSnapshotDTO();
            cbbCreateDeskSnapshotDTO.setDeskId(deskId);
            cbbCreateDeskSnapshotDTO.setName(name);
            cbbCreateDeskSnapshotDTO.setTaskId(taskId);
            cbbCreateDeskSnapshotDTO.setDeskSnapshotId(deskSnapshotId);
            cbbCreateDeskSnapshotDTO.setDeskLoginTime(cloudDesktopDetailDTO.getLatestLoginTime());

            //快照来源分为：系统，用户，管理员，系统占用管理员数量，定时任务创建时来源为系统，为默认用户
            cbbCreateDeskSnapshotDTO.setUserId(userAdminId);
            cbbCreateDeskSnapshotDTO.setUserType(userType);
            cbbVDIDeskSnapshotAPI.createDeskSnapshot(cbbCreateDeskSnapshotDTO);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();

            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_SNAPSHOT_SUCCESS_SYSTEM_LOG, new String[]{deskName, timeMillis});
            LOGGER.info("执行创建云桌面[{}]快照成功；桌面id：{}", deskName, deskId);
        } catch (BusinessException e) {
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_SNAPSHOT_FAIL_SYSTEM_LOG,
                    new String[]{deskName, e.getI18nMessage(), timeMillis});
            LOGGER.error("执行创建云桌面[{}]快照失败；桌面id：{}", deskName, deskId);
            LOGGER.error("失败原因：", e);
        } catch (Exception e) {
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            String message = LocaleI18nResolver.resolve(DeskSnapshotBusinessKey.RCDC_RCO_DESK_SNAPSHOT_NUKNOWN_ERROR, new String[]{});
            addSystemLog(DeskSnapshotBusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_CREATE_SNAPSHOT_FAIL_SYSTEM_LOG,
                    new String[]{deskName, message, timeMillis});
            LOGGER.error("执行创建云桌面[{}]快照失败；桌面id：{}", deskName, deskId);
            LOGGER.error("失败原因：", e);
        }
    }

    @Override
    protected ThreadExecutor getThreadExecutor() {
        return THREAD_EXECUTOR;
    }

    @Override
    protected String[] getNeedHandleState() {
        return new String[]{CbbCloudDeskState.RUNNING.name(), CbbCloudDeskState.CLOSE.name(), CbbCloudDeskState.SLEEP.name()};
    }

    @Override
    protected boolean checkStaticPoolIsSupport() {
        return true;
    }
}
