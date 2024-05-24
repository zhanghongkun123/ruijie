package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.QuartzTaskDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月17日
 *
 * @author xgx
 */
@Service
@Quartz(taskGroup = TaskGroup.ADMIN_CONFIG, scheduleName = BusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_SHUT_DOWN,
        scheduleTypeCode = ScheduleTypeCodeConstants.CLOUD_DESK_SHUT_DOWN_SCHEDULE_TYPE_CODE, blockInMaintenanceMode = true)
public class CloudDeskShutDownQuartzTask extends AbstractCloudDeskQuartzTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDeskShutDownQuartzTask.class);

    private static final ThreadExecutor THREAD_EXECUTOR = ThreadExecutors.newBuilder("shut-down-cloud-desk") //
            .maxThreadNum(10) //
            .queueSize(10000) //
            .build();

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Override
    protected void executeOperator(UUID deskId, Set<UUID> userGroupChildrenSet, List<UUID> userIdList, List<UUID> deskIdList) {
        //内部自己实现
    }

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        CloudDeskQuartzData cloudDeskQuartzData = quartzTaskContext.getByType(CloudDeskQuartzData.class);
        List<UUID> deskIdList = getDeskIdArr(cloudDeskQuartzData, checkStaticPoolIsSupport());

        UUID[] userIdArr = Optional.ofNullable(cloudDeskQuartzData.getUserArr()).orElse(new UUID[0]);
        List<UUID> userIdList = Arrays.stream(userIdArr).collect(Collectors.toList());

        // 获取所有用户组
        UUID[] userGroupIdArr = Optional.ofNullable(cloudDeskQuartzData.getUserGroupArr()).orElse(new UUID[0]);
        Set<UUID> userGroupChildrenSet = Stream.of(userGroupIdArr).collect(Collectors.toSet());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("云桌面定时任务配置涉及所有用户组id[{}]", JSON.toJSONString(userGroupChildrenSet));
        }
        // 查询所有桌面id
        Set<UUID> deskIdSet = queryAll();

        deskIdSet.forEach(deskId -> getThreadExecutor().execute(() -> operator(
                new QuartzTaskDTO(deskId, userGroupChildrenSet, userIdList, deskIdList, cloudDeskQuartzData.getAllowCancel()))));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("云桌面操作定时任务执行结束,参数：{}",JSON.toJSONString(cloudDeskQuartzData));
        }
    }


    private void operator(QuartzTaskDTO quartzTaskDTO) {
        LOGGER.debug("开始执行云桌面[{}]关闭操作", quartzTaskDTO.getDeskId());
        Stopwatch stopwatch = Stopwatch.createStarted();
        String desktopName = "";
        try {
            // 基于桌面ID查询桌面是否仍然属于某个用户组下，或者是否是开启状态
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(quartzTaskDTO.getDeskId());

            desktopName = cloudDesktopDetailDTO.getDesktopName();
            if (!Arrays.asList(getNeedHandleState()).contains(cloudDesktopDetailDTO.getDesktopState())) {
                LOGGER.debug("桌面[{}{}]状态[{}]无需关闭", quartzTaskDTO.getDeskId(), desktopName);
                return;
            }

            UUID curUserGroupId = cloudDesktopDetailDTO.getUserGroupId();
            // 1.确认原来只通过用户组勾选的方式加入的用户是否发生切换组：
            UUID userId = cloudDesktopDetailDTO.getUserId();
            if (!quartzTaskDTO.getDeskList().contains(quartzTaskDTO.getDeskId()) &&
                    !quartzTaskDTO.getUserIdList().contains(userId) &&
                    !quartzTaskDTO.getUserGroupChildrenSet().contains(curUserGroupId)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("桌面[桌面id：{},用户id：{},用户组id：{}]不在用户组[{}]用户[{}],桌面[{}]中不执行关闭操作",
                            quartzTaskDTO.getDeskId(), userId, curUserGroupId,
                            JSON.toJSONString(quartzTaskDTO.getUserGroupChildrenSet()),
                            JSON.toJSONString(quartzTaskDTO.getUserIdList()), JSON.toJSONString(quartzTaskDTO.getDeskList()));
                }
                return;
            }

            // 根据桌面类型关闭云桌面
            shutdownDeskByType(cloudDesktopDetailDTO, quartzTaskDTO.getAllowCancel());

            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_SUCCESS_SYSTEM_LOG, new String[]{desktopName, timeMillis});
            LOGGER.info("执行关闭云桌面[{}]成功；桌面id：{}", desktopName, quartzTaskDTO.getDeskId());
        } catch (BusinessException e) {
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_FAIL_SYSTEM_LOG,
                    new String[]{desktopName, e.getI18nMessage(), timeMillis});
            LOGGER.error("执行关闭云桌面[{}桌面id:{}]任务失败;原因：{}", desktopName, quartzTaskDTO.getDeskId(), e.getI18nMessage(), e);
        } catch (Exception e) {
            LOGGER.error("执行关闭云桌面[{}]任务失败：", quartzTaskDTO.getDeskId(), e);
        }
        LOGGER.debug("执行云桌面[{}][{}]关闭操作结束", quartzTaskDTO.getDeskId(), desktopName);

    }

    private void shutdownDeskByType(CloudDesktopDetailDTO cloudDesktopDetailDTO, Boolean allowCancel) throws BusinessException {
        CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(cloudDesktopDetailDTO.getDesktopCategory());
        LOGGER.info("准备下发关闭{}类型云桌面[id={}]命令", deskType.name(), cloudDesktopDetailDTO.getId());
        switch (deskType) {
            case IDV:
                CbbShutdownDeskIDVDTO shutdownDeskIDVDTO = new CbbShutdownDeskIDVDTO();
                shutdownDeskIDVDTO.setId(cloudDesktopDetailDTO.getId());
                shutdownDeskIDVDTO.setIsForce(Boolean.FALSE);
                shutdownDeskIDVDTO.setTimeout(TimeUnit.MINUTES.toMillis(5));
                shutdownDeskIDVDTO.setAllowCancel(allowCancel);
                cbbIDVDeskOperateAPI.shutdownDeskIDV(shutdownDeskIDVDTO);
                break;
            case VDI:
            case THIRD:
                userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(cloudDesktopDetailDTO.getId(), Boolean.FALSE, allowCancel));
                break;
            default:
                throw new BusinessException("不支持的云桌面类型:{0}", deskType.name());
        }
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
        return new String[]{CbbCloudDeskState.RUNNING.name()};
    }

}
