package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread.AbstractAsyncTaskThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.BatchUpdateHardwareCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.UpdateHardwareCertificationRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月23日
 *
 * @author zdc
 */
public class AsyncBatchUpdateHardwareCertificationThread extends AbstractAsyncTaskThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBatchUpdateHardwareCertificationThread.class);

    private BatchUpdateHardwareCertificationRequest batchRequest;

    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 5000;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(Constant.BATCH_UPDATE_USER_MAC_BINDING_THREAD).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    /**
     * 当前运行中任务数量
     */
    private static final AtomicInteger TASK_RUN_COUNT = new AtomicInteger(0);

    public AsyncBatchUpdateHardwareCertificationThread(UUID businessId, AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI)
            throws BusinessException {
        super(businessId, action, openApiTaskInfoAPI);
    }

    @Override
    public void run() {
        batchTask();
    }

    private void batchTask() {
        UpdateHardwareCertificationRequest[] requestArr = batchRequest.getUpdateBindingArr();
        List<TaskItemRequest> taskItemRequestList = new ArrayList<>();
        List<UpdateHardwareCertificationRequest> errIdentityList = new ArrayList<>();
        //并发（当前队列任务大小+任务数量之和）超出设置最大值后，任务设置为失败，并给出对应提示："超出批量任务并发的最大值"
        if (TASK_RUN_COUNT.get() + requestArr.length > QUEUE_SIZE) {
            String taskNumOverTimeTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_NUM_OVERTIME);
            saveBatchTaskResult(OpenApiTaskState.ERROR, taskItemRequestList, taskNumOverTimeTip, taskNumOverTimeTip);
            return;
        }
        TASK_RUN_COUNT.addAndGet(requestArr.length);
        CountDownLatch countDownLatch = new CountDownLatch(requestArr.length);
        AtomicInteger successCount = new AtomicInteger(0);
        taskItemRequestList = Arrays.stream(requestArr).map(s -> {
            TaskItemRequest taskItemRequest = new TaskItemRequest();
            taskItemRequest.setId(s.getId());
            taskItemRequest.setMainTaskId(customTaskId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.BINDING_ID, s.getBindingId());
            jsonObject.put(Constant.BINDING_STATE, s.getState());
            taskItemRequest.setItemBizData(jsonObject);
            taskItemRequest.setRequestBody((JSONObject) JSON.toJSON(jsonObject));
            return taskItemRequest;
        }).collect(Collectors.toList());
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");

        batchDeleteUserMacBINDING(countDownLatch, taskItemRequestList, successCount, errIdentityList);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(String.format("批量更新用户和终端MAC绑定关系超时,任务Id：[%s]", this.customTaskId), e);
            Thread.currentThread().interrupt();
        }
        TASK_RUN_COUNT.addAndGet(-requestArr.length);
        saveTaskResult(requestArr, taskItemRequestList, successCount, errIdentityList);
    }

    private void batchDeleteUserMacBINDING(CountDownLatch countDownLatch, List<TaskItemRequest> taskItemRequestList,
                                 AtomicInteger successCount, List<UpdateHardwareCertificationRequest> errIdentityList) {
        for (TaskItemRequest taskItemRequest : taskItemRequestList) {
            THREAD_EXECUTOR.execute(() -> {
                UpdateHardwareCertificationRequest request = taskItemRequest.getRequestBody()
                        .toJavaObject(UpdateHardwareCertificationRequest.class);
                try {
                    LockableExecutor.executeWithTryLock(customTaskId.toString(), () -> {
                        try {
                            userHardwareCertificationAPI.checkNumAndUpdateState(request.getBindingId(), request.getState());
                            // 保存子任务
                            successCount.incrementAndGet();
                            taskItemRequest.setItemBizStatus(BatchTaskItemStatus.SUCCESS);
                            taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UPDATE_USER_MAC_BINDING_SUCCESS));
                            saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
                        } catch (Exception e) {
                            errIdentityList.add(request);
                            LOGGER.error(String.format("更新用户和终端MAC绑定关系异常,绑定关系id:[%s],待更新状态:[%s]", request.getBindingId(), request.getState()), e);
                            dealException(taskItemRequest, e, taskItemRequestList);
                        } finally {
                            countDownLatch.countDown();
                        }
                    },180);
                } catch (BusinessException e) {
                    LOGGER.error(String.format("锁异常,任务ID:[%s]绑定关系id:[%s],待更新状态:[%s]",
                            customTaskId.toString(), request.getBindingId(), request.getState()), e);
                    dealException(taskItemRequest, e, taskItemRequestList);
                }
            });
        }
    }

    private void dealException(TaskItemRequest taskItemRequest, Exception e, List<TaskItemRequest> taskItemRequestList) {
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            LOGGER.error("AsyncUpdateUerMacBinding error!", e);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UPDATE_USER_MAC_BINDING_FAIL,
                    UserTipUtil.resolveBusizExceptionMsg(businessException));
            taskItemRequest.setItemResult(describe);
        } else {
            String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UPDATE_USER_MAC_BINDING_SYSTEM_ERROR);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UPDATE_USER_MAC_BINDING_FAIL, systemErrorTip);
            taskItemRequest.setItemResult(describe);
        }
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
    }

    private void saveTaskResult(UpdateHardwareCertificationRequest[] requestArr, List<TaskItemRequest> taskItemRequestList,
                                AtomicInteger successCount, List<UpdateHardwareCertificationRequest> errIdentityList) {
        String taskResult = getTaskResult(successCount);
        String errorMsg = getErrorMsg(errIdentityList);
        saveBatchTaskResult(requestArr.length == successCount.get()
                ? OpenApiTaskState.FINISHED : OpenApiTaskState.ERROR, taskItemRequestList, taskResult, errorMsg);
    }

    private String getTaskResult(AtomicInteger successCount) {
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UPDATE_USER_MAC_BINDING_TASK_RESULT,
                successCount.get() + "", batchRequest.getUpdateBindingArr().length - successCount.get() + "");
    }

    private String getErrorMsg(List<UpdateHardwareCertificationRequest> errIdentityList) {
        StringBuilder errorMsg;
        String rs = "";
        List<String> errList = errIdentityList.stream().map(UpdateHardwareCertificationRequest::getBindingId).map(UUID::toString)
                .distinct().collect(Collectors.toList());
        if (!errList.isEmpty()) {
            errorMsg = new StringBuilder();
            int segNum = 3;
            if (errList.size() <= segNum) {
                for (String userName : errList) {
                    errorMsg.append(userName).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UPDATE_USER_MAC_BINDING_TASK_FAIL_RESULT,
                        String.valueOf(errorMsg));
            } else {
                for (int i = 0; i < segNum; i++) {
                    errorMsg.append(errList.get(i)).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UPDATE_USER_MAC_BINDING_TASK_FAILS_RESULT,
                        String.valueOf(errorMsg), errList.size() + "");
            }
        }
        return rs;
    }

    /**
     *
     * @param batchRequest batchRequest
     * @return this
     */
    public AsyncBatchUpdateHardwareCertificationThread setBatchRequest(BatchUpdateHardwareCertificationRequest batchRequest) {
        this.batchRequest = batchRequest;
        return this;
    }

    /**
     *
     * @param userHardwareCertificationAPI userHardwareCertificationAPI
     * @return this
     */
    public AsyncBatchUpdateHardwareCertificationThread setUserHardwareCertificationAPI(UserHardwareCertificationAPI userHardwareCertificationAPI) {
        this.userHardwareCertificationAPI = userHardwareCertificationAPI;
        return this;
    }
}
