package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MoveDesktopToRecycleBinRequest;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.BatchSoftDeleteArrRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.SoftDeleteInfoRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Description: 异步线程处理软删除云桌面请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-03-16
 *
 * @author zqj
 */
public class AsyncBatchSoftDeleteVDIDesktopThread extends AbstractAsyncDesktopMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBatchSoftDeleteVDIDesktopThread.class);


    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 10000;

    /**
     * 当前运行中任务数量
     */
    private static final AtomicInteger TASK_RUN_COUNT = new AtomicInteger(0);


    private BatchSoftDeleteArrRequest batchSoftDeleteArrRequest;


    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(Constant.BATCH_SOFT_DELETE_VDI_DESKTOP_THREAD).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();


    public AsyncBatchSoftDeleteVDIDesktopThread(AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                                UserDesktopMgmtAPI userDesktopMgmtAPI, BatchSoftDeleteArrRequest request) throws BusinessException {
        super(UUID.randomUUID(), action, openApiTaskInfoAPI, userDesktopMgmtAPI);
        setBatchSoftDeleteArrRequest(request);
    }

    @Override
    public void run() {
        batchTask();
    }

    private void batchTask() {
        SoftDeleteInfoRequest[] deskInfoArr = batchSoftDeleteArrRequest.getDeskInfoArr();
        CountDownLatch countDownLatch = new CountDownLatch(deskInfoArr.length);
        List<TaskItemRequest> taskItemRequestList = new ArrayList<>();
        //并发（当前队列任务大小+任务数量之和）超出设置最大值后，任务设置为失败，并给出对应提示："超出批量任务并发的最大值"
        if (TASK_RUN_COUNT.get() + deskInfoArr.length > QUEUE_SIZE) {
            String taskNumOverTimeTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_NUM_OVERTIME);
            saveBatchTaskResult(OpenApiTaskState.ERROR, taskItemRequestList, taskNumOverTimeTip, taskNumOverTimeTip);
            return;
        }
        TASK_RUN_COUNT.addAndGet(deskInfoArr.length);
        AtomicInteger successCount = new AtomicInteger(0);
        List<String> errIdentityList = new ArrayList<>();
        taskItemRequestList = Arrays.stream(deskInfoArr).map(s -> {
            TaskItemRequest taskItemRequest = new TaskItemRequest();
            taskItemRequest.setId(s.getId());
            taskItemRequest.setMainTaskId(customTaskId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.DESK_ID, s.getDeskId());
            taskItemRequest.setItemBizData(jsonObject);
            taskItemRequest.setRequestBody((JSONObject) JSON.toJSON(s));
            return taskItemRequest;
        }).collect(Collectors.toList());
        //批量软删除云桌面
        batchMoveDesktopToRecycleBin(countDownLatch, taskItemRequestList, successCount, errIdentityList);
        try {
            countDownLatch.await();
            saveTaskResult(taskItemRequestList, successCount, errIdentityList);
        } catch (InterruptedException e) {
            saveTaskResult(taskItemRequestList, successCount, errIdentityList);
            LOGGER.error(String.format("批量软删除云桌面超时,任务Id：[%s]", this.customTaskId), e);
            Thread.currentThread().interrupt();
        }
    }

    private void batchMoveDesktopToRecycleBin(CountDownLatch countDownLatch, List<TaskItemRequest> taskItemRequestList,
                                              AtomicInteger successCount, List<String> errIdentityList) {
        for (TaskItemRequest taskItemRequest : taskItemRequestList) {
            THREAD_EXECUTOR.execute(() -> {
                SoftDeleteInfoRequest softDeleteInfoRequest = taskItemRequest.getRequestBody().toJavaObject(SoftDeleteInfoRequest.class);
                try {
                    LockableExecutor.executeWithTryLock(customTaskId.toString(), () -> {
                        try {
                            MoveDesktopToRecycleBinRequest moveDesktopToRecycleBinRequest = buildParam(softDeleteInfoRequest);
                            taskItemRequest.setItemBizStatus(BatchTaskItemStatus.PROCESSING);
                            saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
                            //移动回收站
                            userDesktopMgmtAPI.moveDesktopToRecycleBin(moveDesktopToRecycleBinRequest);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(Constant.TASK_ID, moveDesktopToRecycleBinRequest.getCustomTaskId());
                            taskItemRequest.setItemBizData(jsonObject);
                            successCount.incrementAndGet();
                            taskItemRequest.setItemBizStatus(BatchTaskItemStatus.SUCCESS);
                            taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_SOFT_DELETE_SUCCESS));
                            saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
                        } catch (Exception e) {
                            //异常处理
                            dealException(errIdentityList, softDeleteInfoRequest, taskItemRequest, e, taskItemRequestList);
                        } finally {
                            TASK_RUN_COUNT.decrementAndGet();
                            countDownLatch.countDown();
                        }
                    }, 120);
                } catch (BusinessException e) {
                    LOGGER.error(String.format("锁异常,任务ID:[%s],桌面Id:[%s]", customTaskId.toString(), softDeleteInfoRequest.getDeskId()), e);
                    dealException(errIdentityList, softDeleteInfoRequest, taskItemRequest, e, taskItemRequestList);
                }

            });
        }
    }

    private MoveDesktopToRecycleBinRequest buildParam(SoftDeleteInfoRequest softDeleteInfoRequest) {
        MoveDesktopToRecycleBinRequest moveDesktopToRecycleBinRequest = new MoveDesktopToRecycleBinRequest();
        moveDesktopToRecycleBinRequest.setDesktopId(softDeleteInfoRequest.getDeskId());
        moveDesktopToRecycleBinRequest.setCustomTaskId(UUID.randomUUID());
        return moveDesktopToRecycleBinRequest;
    }

    private void dealException(List<String> errIdentityList, SoftDeleteInfoRequest softDeleteInfoRequest,
                               TaskItemRequest taskItemRequest, Exception e, List<TaskItemRequest> taskItemRequestList) {
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
        errIdentityList.add(softDeleteInfoRequest.getDeskId().toString());
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            LOGGER.error("AsyncCreateVDIDesktop error!", e);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_SOFT_DELETE_FAIL,
                    businessException.getI18nMessage());
            taskItemRequest.setItemResult(describe);
        } else {
            LOGGER.error(String.format("软删除云桌面异常,桌面Id:[%s]", softDeleteInfoRequest.getDeskId()), e);
            String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_SOFT_DELETE_SYSTEM_ERROR);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_SOFT_DELETE_FAIL, systemErrorTip);
            taskItemRequest.setItemResult(describe);
        }
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
    }

    private void saveTaskResult(List<TaskItemRequest> taskItemRequestList, AtomicInteger successCount, List<String> errIdentityList) {
        String errorMsg = getErrorMsg(errIdentityList);
        String taskResult = getTaskResult(successCount);
        saveBatchTaskResult(batchSoftDeleteArrRequest.getDeskInfoArr().length == successCount.get()
                ? OpenApiTaskState.FINISHED : OpenApiTaskState.ERROR, taskItemRequestList, taskResult, errorMsg);
    }

    private String getErrorMsg(List<String> errIdentityList) {
        StringBuilder errorMsg;
        String rs = "";
        if (!errIdentityList.isEmpty()) {
            errorMsg = new StringBuilder();
            int segNum = 3;
            if (errIdentityList.size() <= segNum) {
                for (String userName : errIdentityList) {
                    errorMsg.append(userName).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_SOFT_DELETE_TASK_FAIL_RESULT, String.valueOf(errorMsg));
            } else {
                for (int i = 0; i < segNum; i++) {
                    errorMsg.append(errIdentityList.get(i)).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_SOFT_DELETE_TASK_FAILS_RESULT,
                        String.valueOf(errorMsg), errIdentityList.size() + "");
            }
        }
        return rs;
    }

    private String getTaskResult(AtomicInteger successCount) {
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_SOFT_DELETE_TASK_RESULT,
                successCount.get() + "", batchSoftDeleteArrRequest.getDeskInfoArr().length - successCount.get() + "");
    }


    public void setBatchSoftDeleteArrRequest(BatchSoftDeleteArrRequest batchSoftDeleteArrRequest) {
        this.batchSoftDeleteArrRequest = batchSoftDeleteArrRequest;
    }
}
