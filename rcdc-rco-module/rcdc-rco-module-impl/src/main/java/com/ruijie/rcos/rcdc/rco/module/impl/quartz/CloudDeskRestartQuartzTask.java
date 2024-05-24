package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskOperateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: 定时重启云桌面任务
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28 14:47
 *
 * @author tangxu
 */

@Service
@Quartz(taskGroup = TaskGroup.ADMIN_CONFIG, scheduleName = BusinessKey.RCDC_RCO_QUARTZ_CLOUD_DESK_RESTART,
        scheduleTypeCode = ScheduleTypeCodeConstants.CLOUD_DESK_RESTART_SCHEDULE_TYPE_CODE, blockInMaintenanceMode = true)
public class CloudDeskRestartQuartzTask extends AbstractCloudDeskQuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDeskRestartQuartzTask.class);

    private static final ThreadExecutor THREAD_EXECUTOR = ThreadExecutors.newBuilder("restart-cloud-desk") //
            .maxThreadNum(10) //
            .queueSize(10000) //
            .build();

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private CbbVDIDeskOperateAPI cbbVDIDeskOperateAPI;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Autowired
    private CbbThirdPartyDeskOperateMgmtAPI cbbThirdPartyDeskOperateMgmtAPI;

    @Override
    protected void executeOperator(UUID deskId, Set<UUID> userGroupChildrenSet, List<UUID> userIdList, List<UUID> deskList) {
        LOGGER.debug("开始执行云桌面[{}]重启操作", deskId);
        Stopwatch stopwatch = Stopwatch.createStarted();
        String desktopName = "";
        try {
            // 基于桌面ID查询桌面是否仍然属于某个用户组下，或者是否是开启状态
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);

            desktopName = cloudDesktopDetailDTO.getDesktopName();
            if (!Arrays.asList(getNeedHandleState()).contains(cloudDesktopDetailDTO.getDesktopState())) {
                LOGGER.debug("桌面[{}{}]状态[{}]无需重启", deskId, desktopName, cloudDesktopDetailDTO.getDesktopState());
                return;
            }

            UUID curUserGroupId = cloudDesktopDetailDTO.getUserGroupId();
            // 1.确认原来只通过用户组勾选的方式加入的用户是否发生切换组：
            UUID userId = cloudDesktopDetailDTO.getUserId();
            if (!deskList.contains(deskId) && !userIdList.contains(userId) && !userGroupChildrenSet.contains(curUserGroupId)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("桌面[桌面id：{},用户id：{},用户组id：{}]不在用户组[{}],用户[{}],桌面[{}]中不执行重启操作", deskId, userId, curUserGroupId,
                            JSON.toJSONString(userGroupChildrenSet), JSON.toJSONString(userIdList), JSON.toJSONString(deskList));
                }
                return;
            }

            // 根据桌面类型重启云桌面
            restartDeskByType(cloudDesktopDetailDTO);

            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_RESTART_CLOUD_DESK_SUCCESS_SYSTEM_LOG, new String[] {desktopName, timeMillis});
            LOGGER.info("执行重启云桌面[{}]成功；桌面id：{}", desktopName, deskId);
        } catch (BusinessException e) {
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_RESTART_CLOUD_DESK_FAIL_SYSTEM_LOG,
                    new String[] {desktopName, e.getI18nMessage(), timeMillis});
            LOGGER.error("执行重启云桌面:{}桌面id:{}任务失败;原因:{}", desktopName, deskId, e.getI18nMessage(), e);
        } catch (Exception e) {
            LOGGER.error("执行重启云桌面[{}]任务失败", deskId, e);
        }
        LOGGER.debug("执行云桌面[{}][{}]重启操作结束", deskId, desktopName);

    }

    private void restartDeskByType(CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(cloudDesktopDetailDTO.getDesktopCategory());
        UUID deskId = cloudDesktopDetailDTO.getId();
        switch (deskType) {
            case IDV:
                cbbIDVDeskOperateAPI.rebootDeskIDV(deskId);
                break;
            case VDI:
                cbbVDIDeskOperateAPI.rebootDeskVDI(deskId);
                break;
            case THIRD:
                cbbThirdPartyDeskOperateMgmtAPI.rebootDeskThird(deskId);
                break;
            default:
                throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_RESTART_FAIL_NOT_SUPPORT_DESKTOP_TYPE,
                        cloudDesktopDetailDTO.getDesktopName(), deskType.name());
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
        return new String[] {CbbCloudDeskState.RUNNING.name(), CbbCloudDeskState.SLEEP.name()};
    }
}
