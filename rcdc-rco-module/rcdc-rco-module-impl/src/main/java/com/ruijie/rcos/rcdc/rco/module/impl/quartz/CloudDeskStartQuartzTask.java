package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月17日
 *
 * @author xgx
 */
@Service
@Quartz(taskGroup = TaskGroup.ADMIN_CONFIG, scheduleName = BusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_START,
        scheduleTypeCode = ScheduleTypeCodeConstants.CLOUD_DESK_START_SCHEDULE_TYPE_CODE, blockInMaintenanceMode = true)
public class CloudDeskStartQuartzTask extends AbstractCloudDeskQuartzTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDeskStartQuartzTask.class);

    private static final ThreadExecutor THREAD_EXECUTOR = ThreadExecutors.newBuilder("start-cloud-desk") //
            .maxThreadNum(10) //
            .queueSize(10000) //
            .build();

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Override
    protected void executeOperator(UUID deskId, Set<UUID> userGroupChildrenSet, List<UUID> userIdList, List<UUID> deskIdList) {
        LOGGER.debug("开始执行云桌面[{}]启动操作", deskId);

        Stopwatch stopwatch = Stopwatch.createStarted();
        String deskName = "";
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            deskName = cloudDesktopDetailDTO.getDesktopName();
            if (Objects.nonNull(cloudDesktopDetailDTO.getDesktopPoolId())) {
                DesktopPoolDetailDTO desktopPoolDetailDTO = desktopPoolMgmtAPI.getDesktopPoolDetail(cloudDesktopDetailDTO.getDesktopPoolId());
                if (Boolean.TRUE.equals(desktopPoolDetailDTO.getIsOpenMaintenance())) {
                    LOGGER.error("桌面池[{}]处于维护模式", desktopPoolDetailDTO.getName());
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CHECK_UNDER_MAINTENANCE_ERROR,
                            desktopPoolDetailDTO.getName());
                }
            }

            Set<String> expectState;
            if (CbbCloudDeskType.THIRD.name().equals(cloudDesktopDetailDTO.getDeskType())) {
                // 如果是第三方桌面池则如果是离线状态就执行启动操作
                expectState = Arrays.stream(new String[]{CbbCloudDeskState.OFF_LINE.name()}).collect(Collectors.toSet());
            } else {
                // (VDI、IDV、非第三方桌面池)只能对处于休眠和关闭状态的云桌面执行启动操作
                expectState = Arrays.stream(new String[] {CbbCloudDeskState.CLOSE.name(), CbbCloudDeskState.SLEEP.name()})
                        .collect(Collectors.toSet());
            }
            if (!expectState.contains(cloudDesktopDetailDTO.getDesktopState())) {
                LOGGER.debug("桌面[{}{}]状态[{}]无需启动", deskId, deskName);
                return;
            }

            // 1.确认原来只是通过用户组勾选的方式加入的用户是否发生切换组：
            UUID curUserGroupId = cloudDesktopDetailDTO.getUserGroupId();
            UUID userId = cloudDesktopDetailDTO.getUserId();
            if (!deskIdList.contains(deskId) && !userIdList.contains(userId) && !userGroupChildrenSet.contains(curUserGroupId)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("桌面[桌面id：{},用户id：{},用户组id：{}]不在用户组[{}],用户[{}],桌面[{}]中不执行关闭操作", deskId, userId, curUserGroupId,
                            JSON.toJSONString(userGroupChildrenSet), JSON.toJSONString(userIdList), JSON.toJSONString(deskIdList));
                }
                return;
            }
            userDesktopOperateAPI.start(new CloudDesktopStartRequest(deskId));

            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_START_CLOUD_DESK_SUCCESS_SYSTEM_LOG, new String[] {deskName, timeMillis});
            LOGGER.info("执行启动云桌面[{}]成功；桌面id：{}", deskName, deskId);
        } catch (BusinessException e) {
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_START_CLOUD_DESK_FAIL_SYSTEM_LOG, new String[] {deskName, e.getI18nMessage(), timeMillis});
            LOGGER.error("执行启动云桌面[" + deskName + "桌面id:" + deskId + "]任务失败;原因：" + e.getI18nMessage(), e);
        } catch (Exception e) {
            LOGGER.error("执行启动云桌面[" + deskName + "桌面id:" + deskId + "]任务失败", e);
        }
        LOGGER.debug("执行云桌面[{}][{}]启动操作结束", deskId, deskName);
    }

    @Override
    protected boolean checkStaticPoolIsSupport() {
        return true;
    }

    @Override
    protected ThreadExecutor getThreadExecutor() {
        return THREAD_EXECUTOR;
    }

    @Override
    protected String[] getNeedHandleState() {
        return new String[] {CbbCloudDeskState.CLOSE.name(), CbbCloudDeskState.SLEEP.name(), CbbCloudDeskState.OFF_LINE.name()};
    }

}
