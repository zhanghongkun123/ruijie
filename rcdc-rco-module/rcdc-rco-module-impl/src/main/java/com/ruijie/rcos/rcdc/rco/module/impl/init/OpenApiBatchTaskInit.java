package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MoveDesktopToRecycleBinRequest;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.openapi.entity.OpenApiTaskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.openapi.service.OpenApiTaskInfoService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: openapi-批量任务-运行中任务修复
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-05-07
 *
 * @author zqj
 */
@Service
public class OpenApiBatchTaskInit implements SafetySingletonInitializer {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuInitImpl.class);

    private static final String TASK_DESK_CREATE_FAIL = "TASK_DESK_CREATE_FAIL";

    private static final String TASK_DESK_DELETE_FAIL = "TASK_DESK_DELETE_FAIL";

    public static final String CROSS_BAR = "-";

    /**
     * 分隔符
     */
    public static final String NAME_SEPARATOR = "、";

    public static final String ROLLBACK = "rollback";

    /**
     * 批量创建云桌面
     */
    private static final String BATCH_CREATE_VDI = "BATCH_CREATE_VDI";

    /**
     * 批量软删除云桌面
     */
    private static final String BATCH_SOFT_DELETE_VDI = "BATCH_SOFT_DELETE_VDI";

    /**
     * 用户名
     */
    private static final String USER_NAME = "user_name";

    /**
     * 云桌面id常量
     */
    private static final String DESK_ID = "desk_id";

    private static final String BATCH_CREATE_VDI_DESKTOP_INIT_THREAD_MAIN = "batch_create_vdi_desktop_init_thread_main";

    private static final String BATCH_DELETE_VDI_DESKTOP_INIT_THREAD_MAIN = "batch_delete_vdi_desktop_init_thread_main";

    private static final String OPENAPI_ASYNC_TASK_RUNNING_THREAD_MAIN = "openapi_async_task_running_thread_main";

    @Autowired
    private OpenApiTaskInfoService openApiTaskInfoService;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Autowired
    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Override
    public void safeInit() {
        // openapi-批量任务-运行中任务修复
        try {
            ThreadExecutors.execute(BATCH_CREATE_VDI_DESKTOP_INIT_THREAD_MAIN, () -> {
                //处理批量创建云桌面运行中的任务
                LOGGER.info("进行批量创建云桌面的运行中任务修复");
                dealBatchCreateDesktop();
                LOGGER.info("完成批量创建云桌面的运行中任务修复");

            });
            ThreadExecutors.execute(BATCH_DELETE_VDI_DESKTOP_INIT_THREAD_MAIN, () -> {
                //处理批量软删除云桌面运行中的任务
                LOGGER.info("进行批量软删除云桌面的运行中任务修复");
                dealBatchSoftDeleteDesktop();
                LOGGER.info("完成批量软删除云桌面的运行中任务修复");
            });

            //处理openapi异步任务，RCDC重启运行中的任务，修改为任务失败
            ThreadExecutors.execute(OPENAPI_ASYNC_TASK_RUNNING_THREAD_MAIN, this::dealOpenapiAsyncTaskRunning);
        } catch (Exception e) {
            LOGGER.error(" OPENAPI异步任务运行中任务修复异常", e);
        }

    }

    /**
     * 处理RCDC重启导致openapi异步任务卡在运行中。任务状态全部标记为失败
     */
    private void dealOpenapiAsyncTaskRunning() {
        List<OpenApiTaskInfoEntity> taskList = openApiTaskInfoService.findByTaskState(OpenApiTaskState.RUNNING.name());
        for (OpenApiTaskInfoEntity openApiTaskInfoEntity : taskList) {
            //批量任务跳过
            if (BATCH_SOFT_DELETE_VDI.equals(openApiTaskInfoEntity.getAction()) || BATCH_CREATE_VDI.equals(openApiTaskInfoEntity.getAction())) {
                continue;
            }
            saveOpenApiTaskInfo(openApiTaskInfoEntity, null, "");
        }
    }

    private void dealBatchSoftDeleteDesktop() {
        List<OpenApiTaskInfoEntity> taskList = openApiTaskInfoService.findByActionAndTaskState(
                BATCH_SOFT_DELETE_VDI, OpenApiTaskState.RUNNING.name());
        for (OpenApiTaskInfoEntity openApiTaskInfoEntity : taskList) {
            //任务未开始情况
            if (dealNoStartTask(openApiTaskInfoEntity)) {
                continue;
            }
            List<TaskItemRequest> taskItemRequestList = JSON.parseArray(openApiTaskInfoEntity.getTaskItemList(), TaskItemRequest.class);
            long failCount = taskItemRequestList.stream().filter(s -> s.getItemBizStatus() == BatchTaskItemStatus.FAILURE).count();
            long successCount = taskItemRequestList.stream().filter(s -> s.getItemBizStatus() == BatchTaskItemStatus.SUCCESS).count();
            //任务全部做完
            if (dealAllFinishTask(openApiTaskInfoEntity, taskItemRequestList, failCount,
                    successCount, BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_SOFT_DELETE_TASK_RESULT)) {
                continue;
            }
            List<String> failIdentityList = new ArrayList<>();
            for (TaskItemRequest taskItemRequest : taskItemRequestList) {
                failCount = dealTaskItem(openApiTaskInfoEntity, failCount, failIdentityList, taskItemRequest, BATCH_SOFT_DELETE_VDI);
            }
            //由于运行中，不知道状态机运行结果，加入警告处理.状态改为失败
            saveAlarm(openApiTaskInfoEntity, failIdentityList, BATCH_SOFT_DELETE_VDI);
            String taskResult = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_SOFT_DELETE_TASK_RESULT,
                    taskItemRequestList.size() - failCount + "", failCount + "");
            saveOpenApiTaskInfo(openApiTaskInfoEntity, taskItemRequestList, taskResult);
        }
    }

    private void dealBatchCreateDesktop() {
        List<OpenApiTaskInfoEntity> taskList = openApiTaskInfoService.findByActionAndTaskState(BATCH_CREATE_VDI, OpenApiTaskState.RUNNING.name());
        for (OpenApiTaskInfoEntity openApiTaskInfoEntity : taskList) {
            //任务未开始情况
            if (dealNoStartTask(openApiTaskInfoEntity)) {
                continue;
            }
            List<TaskItemRequest> taskItemRequestList = JSON.parseArray(openApiTaskInfoEntity.getTaskItemList(), TaskItemRequest.class);
            long failCount = taskItemRequestList.stream().filter(s -> s.getItemBizStatus() == BatchTaskItemStatus.FAILURE).count();
            long successCount = taskItemRequestList.stream().filter(s -> s.getItemBizStatus() == BatchTaskItemStatus.SUCCESS).count();
            //任务全部做完
            if (dealAllFinishTask(openApiTaskInfoEntity, taskItemRequestList, failCount,
                    successCount, BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_CREATE_TASK_RESULT)) {
                continue;
            }
            List<String> failIdentityList = new ArrayList<>();
            for (TaskItemRequest taskItemRequest : taskItemRequestList) {
                failCount = dealTaskItem(openApiTaskInfoEntity, failCount, failIdentityList, taskItemRequest, BATCH_CREATE_VDI);
            }
            //由于运行中，不知道状态机运行结果，加入警告处理.状态改为失败
            saveAlarm(openApiTaskInfoEntity, failIdentityList, BATCH_CREATE_VDI);
            String taskResult = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_CREATE_TASK_RESULT,
                    taskItemRequestList.size() - failCount + "", failCount + "");
            saveOpenApiTaskInfo(openApiTaskInfoEntity, taskItemRequestList, taskResult);
        }
    }

    private boolean dealNoStartTask(OpenApiTaskInfoEntity openApiTaskInfoEntity) {
        if (openApiTaskInfoEntity.getTaskItemList() == null) {
            saveOpenApiTaskInfo(openApiTaskInfoEntity,null,LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_SHUTDOWN));
            return true;
        }
        return false;
    }

    private void saveOpenApiTaskInfo(OpenApiTaskInfoEntity openApiTaskInfoEntity, List<TaskItemRequest> taskItemRequestList, String taskResult) {
        openApiTaskInfoEntity.setTaskResult(taskResult);
        openApiTaskInfoEntity.setTaskState(OpenApiTaskState.ERROR.name());
        openApiTaskInfoEntity.setExceptionMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_SHUTDOWN));
        if (taskItemRequestList != null) {
            openApiTaskInfoEntity.setTaskItemList(JSON.toJSONString(taskItemRequestList));
        }
        openApiTaskInfoService.save(openApiTaskInfoEntity);
    }

    private long dealTaskItem(OpenApiTaskInfoEntity openApiTaskInfoEntity, long failCount,
                              List<String> failIdentityList, TaskItemRequest taskItemRequest, String action) {
        JSONObject itemBizData = taskItemRequest.getItemBizData();
        //未开始任务
        if (taskItemRequest.getItemBizStatus() == null) {
            taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
            taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_NO_START));
            failCount++;

        } else if (taskItemRequest.getItemBizStatus() == BatchTaskItemStatus.PROCESSING) {
            //运行中的任务，不知道状态机运行结果，加入警告处理.状态改为失败
            failCount++;
            taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
            taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_RESULT_UNKNOWN));
            if (action.equals(BATCH_CREATE_VDI)) {
                //itemBizData 必存在值
                failIdentityList.add(itemBizData.getString(USER_NAME));
            } else if (action.equals(BATCH_SOFT_DELETE_VDI)) {
                //itemBizData 必存在值
                UUID deskId = itemBizData.getObject(DESK_ID, UUID.class);
                try {
                    UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(deskId);
                    failIdentityList.add(userDesktopEntity.getDesktopName());
                } catch (BusinessException e) {
                    LOGGER.error("获取云桌面异常，桌面Id[{}]", deskId);
                }

            }
        } else if (taskItemRequest.getItemBizStatus() == BatchTaskItemStatus.SUCCESS &&
                action.equals(BATCH_CREATE_VDI) && dealDesktopRollback(openApiTaskInfoEntity, taskItemRequest, itemBizData)) {
            //批量创建云桌面-成功状态，需要回滚操作，则删除云桌面，状态改为失败
            failCount++;
        }
        return failCount;
    }

    private boolean dealAllFinishTask(OpenApiTaskInfoEntity openApiTaskInfoEntity,
                                      List<TaskItemRequest> taskItemRequestList, long failCount, long successCount, String key) {
        if (taskItemRequestList.size() == successCount + failCount) {
            String taskResult = LocaleI18nResolver.resolve(key, successCount + "", failCount + "");
            openApiTaskInfoEntity.setTaskResult(taskResult);
            //全部成功更改任务为成功
            if (failCount == 0) {
                openApiTaskInfoEntity.setTaskState(OpenApiTaskState.FINISHED.name());
            } else {
                //部分成功更改任务为失败
                openApiTaskInfoEntity.setTaskState(OpenApiTaskState.ERROR.name());
                openApiTaskInfoEntity.setExceptionMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_SHUTDOWN));
            }
            openApiTaskInfoService.save(openApiTaskInfoEntity);
            return true;
        }
        return false;
    }

    private void saveAlarm(OpenApiTaskInfoEntity openApiTaskInfoEntity, List<String> failIdentityList, String action) {
        if (!failIdentityList.isEmpty()) {
            SaveAlarmRequest request = new SaveAlarmRequest();
            String identityStr = String.join(NAME_SEPARATOR, failIdentityList);
            if (action.equals(BATCH_SOFT_DELETE_VDI)) {
                request.setAlarmCode(TASK_DESK_DELETE_FAIL + CROSS_BAR + openApiTaskInfoEntity.getTaskId());
                request.setAlarmName(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_TASK_DELETE_FAIL_ALARM_NAME));
                request.setAlarmContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_TASK_DELETE_UNKNOWN,
                        openApiTaskInfoEntity.getTaskId().toString(), identityStr));
            } else if (action.equals(BATCH_CREATE_VDI)) {
                request.setAlarmCode(TASK_DESK_CREATE_FAIL + CROSS_BAR + openApiTaskInfoEntity.getTaskId());
                request.setAlarmName(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_TASK_CREATE_FAIL_ALARM_NAME));
                request.setAlarmContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_TASK_CREATE_UNKNOWN,
                        openApiTaskInfoEntity.getTaskId().toString(), identityStr));
            }
            request.setAlarmType(AlarmConstants.ALARM_TYPE_DESKTOP);
            request.setEnableSendMail(true);
            request.setAlarmLevel(AlarmLevel.TIPS);
            request.setAlarmTime(new Date());
            request.setBusinessId(openApiTaskInfoEntity.getTaskId().toString());
            baseAlarmAPI.saveAlarm(request);
        }

    }

    private boolean dealDesktopRollback(OpenApiTaskInfoEntity openApiTaskInfoEntity, TaskItemRequest taskItemRequest, JSONObject itemBizData) {
        //成功状态，需要回滚操作，则删除云桌面，状态改为失败
        if (Boolean.TRUE.equals(taskItemRequest.getFailRollback()) && itemBizData != null &&
                (itemBizData.getBoolean(ROLLBACK) == null || Boolean.FALSE.equals(itemBizData.getBoolean(ROLLBACK)))) {
            try {
                taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
                UUID deskId = itemBizData.getObject(DESK_ID, UUID.class);
                MoveDesktopToRecycleBinRequest moveDesktopToRecycleBinRequest = new MoveDesktopToRecycleBinRequest();
                moveDesktopToRecycleBinRequest.setDesktopId(deskId);
                //移动回收站
                userDesktopMgmtAPI.moveDesktopToRecycleBin(moveDesktopToRecycleBinRequest);
                //删除云桌面
                recycleBinMgmtAPI.delete(new IdRequest(deskId));
                itemBizData.put(ROLLBACK, true);
                String rollbackTxt = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_ROLLBACK_SUCCESS);
                taskItemRequest.setItemResult(taskItemRequest.getItemResult() + "，" + rollbackTxt);
            } catch (Exception e) {
                if (e instanceof BusinessException) {
                    BusinessException businessException = (BusinessException) e;
                    String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_ROLLBACK_FAIL,
                            businessException.getI18nMessage());
                    taskItemRequest.setItemResult(describe);
                } else {
                    LOGGER.error(String.format("回滚删除云桌面失败,任务Id：[%s]", openApiTaskInfoEntity.getTaskId()), e);
                    String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_DELETE_SYSTEM_ERROR);
                    String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_ROLLBACK_FAIL, systemErrorTip);
                    taskItemRequest.setItemResult(describe);
                }
                itemBizData.put(ROLLBACK, false);
            }
            return true;
        }
        return false;
    }
}
