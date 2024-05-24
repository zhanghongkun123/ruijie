package com.ruijie.rcos.rcdc.rco.module.impl.thirdparty.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.ThirdUserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.callback.SyncThirdPartyUserCallBack;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyAuthPlatformConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartySyncScheduleDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserSyncConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.ThirdPartyUserSyncTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.thirdparty.service.RcoThirdPartyUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.handler.CronExpressionConvertHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;

/**
 * 第三方用户相关操作类实现
 *
 * Description: 第三方用户相关操作类实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/15 15:59
 *
 * @author zjy
 */
@Service
public class RcoThirdPartyUserServiceImpl implements RcoThirdPartyUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoThirdPartyUserServiceImpl.class);

    @Autowired
    private IacThirdPartyUserAPI thirdPartyUserAPI;

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Override
    public BatchTaskSubmitResult syncThirdPartyUser(SyncThirdPartyUserCallBack syncUserCallBack) throws BusinessException {
        Assert.notNull(syncUserCallBack, "syncAdUserCallBack not be null");

        return thirdPartyUserAPI.syncUser(syncUserCallBack);
    }

    @Override
    public void createOrUpdateSyncSchedule(BaseThirdPartyAuthPlatformConfigDTO cbbThirdPartyAuthPlatformConfigDTO) throws BusinessException {
        Assert.notNull(cbbThirdPartyAuthPlatformConfigDTO, "cbbThirdPartyAuthPlatformConfigDTO must not be null");

        LOGGER.info("开始创建或更新第三方用户同步任务");
        // 查找任务
        BaseThirdPartySyncScheduleDTO scheduleTask = thirdPartyUserAPI.getScheduleTask();
        if (!cbbThirdPartyAuthPlatformConfigDTO.getThirdPartyEnable() ||
                cbbThirdPartyAuthPlatformConfigDTO.getUserSyncConfig() == null ||
                !cbbThirdPartyAuthPlatformConfigDTO.getUserSyncConfig().getUserSyncEnable()) {
            // 停止任务
            if (scheduleTask == null || QuartzTaskState.STOP == scheduleTask.getQuartzTaskState()) {
                LOGGER.info("当前不存在同步第三方用户任务或任务已经为关闭状态，直接跳过");
                return;
            }
            scheduleTask.setQuartzTaskState(QuartzTaskState.STOP);
            thirdPartyUserAPI.editScheduleTask(scheduleTask);
        } else {
            // 创建或更新任务
            BaseThirdPartyUserSyncConfigDTO userSyncConfig = cbbThirdPartyAuthPlatformConfigDTO.getUserSyncConfig();
            // 表达式转换
            String cornExpression = "";
            if (ThirdPartyUserSyncTypeEnum.TIMER == userSyncConfig.getUserSyncType()) {
                RcoScheduleTaskRequest rcoScheduleTaskRequest = new RcoScheduleTaskRequest();
                rcoScheduleTaskRequest.setScheduleTypeCode(UserConstants.THIRD_PARTY_USER_SYNC_CODE);
                rcoScheduleTaskRequest.setTaskName(LocaleI18nResolver.resolve(
                        ThirdUserBusinessKey.RCDC_USER_THIRD_PARTY_USER_SYNC_TASK_NAME));
                rcoScheduleTaskRequest.setTaskCycle(TaskCycleEnum.DAY);
                rcoScheduleTaskRequest.setScheduleTime(userSyncConfig.getUserSyncTime());
                cornExpression = CronExpressionConvertHandler.generateExpression(rcoScheduleTaskRequest);
            } else {
                // 按小时的同步任务
                cornExpression = "0 0 0/" + userSyncConfig.getUserSyncPeriod() + " * * ? *";
            }
            LOGGER.info("第三方用户定时任务表达式为 ： [{}]", cornExpression);
            if (scheduleTask == null) {
                BaseThirdPartySyncScheduleDTO cbbThirdPartySyncScheduleDTO = new BaseThirdPartySyncScheduleDTO();
                cbbThirdPartySyncScheduleDTO.setQuartzTaskState(QuartzTaskState.START);
                cbbThirdPartySyncScheduleDTO.setCbbThirdPartyUserSyncConfigDTO(userSyncConfig);
                cbbThirdPartySyncScheduleDTO.setCronExpression(cornExpression);
                thirdPartyUserAPI.createScheduleTask(cbbThirdPartySyncScheduleDTO);
            } else {
                scheduleTask.setCronExpression(cornExpression);
                scheduleTask.setCbbThirdPartyUserSyncConfigDTO(userSyncConfig);
                scheduleTask.setQuartzTaskState(QuartzTaskState.START);
                thirdPartyUserAPI.editScheduleTask(scheduleTask);
            }
        }
    }
}
