package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MoveDesktopToRecycleBinRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.BatchCreateVDIDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.CreateVDIDesktopRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Description: 异步线程处理创建云桌面请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-03-16
 *
 * @author zqj
 */
public class AsyncBatchCreateVDIDesktopThread extends AbstractAsyncDesktopMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBatchCreateVDIDesktopThread.class);

    private UserInfoAPI userInfoAPI;

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 5000;


    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    private BatchCreateVDIDesktopRequest batchCreateVDIDesktopRequest;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(Constant.BATCH_CREATE_VDI_DESKTOP_THREAD).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    /**
     * 当前运行中任务数量
     */
    private static final AtomicInteger TASK_RUN_COUNT = new AtomicInteger(0);

    /**
     * 用于记录失败任务id
     */
    private static final Map<UUID, UUID> FAIL_TASK_ID = Maps.newConcurrentMap();


    public AsyncBatchCreateVDIDesktopThread(AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                            UserDesktopMgmtAPI userDesktopMgmtAPI, BatchCreateVDIDesktopRequest request) throws BusinessException {
        super(UUID.randomUUID(), action, openApiTaskInfoAPI, userDesktopMgmtAPI);
        setBatchCreateVDIDesktopRequest(request);
    }

    @Override
    public void run() {
        batchCreateDesk();
    }

    private void batchCreateDesk() {
        CreateVDIDesktopRequest[] createVDIDesktopArr = batchCreateVDIDesktopRequest.getDeskInfoArr();
        List<TaskItemRequest> taskItemRequestList = new ArrayList<>();
        List<String> errIdentityList = new ArrayList<>();
        //并发（当前队列任务大小+任务数量之和）超出设置最大值后，任务设置为失败，并给出对应提示："超出批量任务并发的最大值"
        if (TASK_RUN_COUNT.get() + createVDIDesktopArr.length > QUEUE_SIZE) {
            String taskNumOverTimeTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_NUM_OVERTIME);
            saveBatchTaskResult(OpenApiTaskState.ERROR, taskItemRequestList, taskNumOverTimeTip, taskNumOverTimeTip);
            return;
        }
        TASK_RUN_COUNT.addAndGet(createVDIDesktopArr.length);
        CountDownLatch countDownLatch = new CountDownLatch(createVDIDesktopArr.length);
        AtomicInteger successCount = new AtomicInteger(0);
        taskItemRequestList = Arrays.stream(createVDIDesktopArr).map(s -> {
            TaskItemRequest taskItemRequest = new TaskItemRequest();
            taskItemRequest.setId(s.getId());
            taskItemRequest.setMainTaskId(customTaskId);
            taskItemRequest.setFailRollback(batchCreateVDIDesktopRequest.getFailRollback());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.USER_NAME, s.getUserName());
            taskItemRequest.setItemBizData(jsonObject);
            taskItemRequest.setRequestBody((JSONObject) JSON.toJSON(s));
            return taskItemRequest;
        }).collect(Collectors.toList());
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
        //批量创建云桌面.
        batchCreateDesktop(countDownLatch, taskItemRequestList, successCount, errIdentityList);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(String.format("批量创建云桌面超时,任务Id：[%s]", this.customTaskId), e);
            Thread.currentThread().interrupt();
        } finally {
            //任务Id释放掉
            FAIL_TASK_ID.remove(customTaskId);
        }
        //存在回滚，则删除云桌面
        delDesktop(createVDIDesktopArr, taskItemRequestList, successCount, errIdentityList);
        TASK_RUN_COUNT.addAndGet(-createVDIDesktopArr.length);
        saveTaskResult(createVDIDesktopArr, taskItemRequestList, successCount, errIdentityList);
    }

    private void delDesktop(CreateVDIDesktopRequest[] createVDIDesktopArr, List<TaskItemRequest> taskItemRequestList,
                            AtomicInteger successCount, List<String> errIdentityList) {
        OpenApiTaskState openApiTaskState = createVDIDesktopArr.length == successCount.get()
                ? OpenApiTaskState.FINISHED : OpenApiTaskState.ERROR;
        //存在失败 如果需要回滚 ，执行回滚原先创建成功的云桌面
        if (Boolean.TRUE.equals(batchCreateVDIDesktopRequest.getFailRollback())
                && openApiTaskState == OpenApiTaskState.ERROR) {
            CountDownLatch delCountDownLatch = new CountDownLatch(taskItemRequestList.size());
            //批量删除云桌面
            batchDelDesktop(taskItemRequestList, delCountDownLatch);
            try {
                delCountDownLatch.await();
            } catch (InterruptedException e) {
                LOGGER.error(String.format("回滚删除云桌面超时,任务Id：[%s]", this.customTaskId), e);
                saveTaskResult(createVDIDesktopArr, taskItemRequestList, successCount, errIdentityList);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void batchCreateDesktop(CountDownLatch countDownLatch,
                                    List<TaskItemRequest> taskItemRequestList, AtomicInteger successCount, List<String> errIdentityList) {
        for (TaskItemRequest taskItemRequest : taskItemRequestList) {
            THREAD_EXECUTOR.execute(() -> {
                CreateVDIDesktopRequest createVdiDesktopRequest = taskItemRequest.getRequestBody().toJavaObject(CreateVDIDesktopRequest.class);
                try {
                    LockableExecutor.executeWithTryLock(customTaskId.toString(), () -> {
                        try {
                            //存在失败，需要回滚，之后的任务设为失败，否则任务继续执行
                            if (Boolean.TRUE.equals(batchCreateVDIDesktopRequest.getFailRollback()) && FAIL_TASK_ID.size() > 0
                                    && FAIL_TASK_ID.get(taskItemRequest.getMainTaskId()) != null) {
                                taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
                                return;
                            }
                            taskItemRequest.setItemBizStatus(BatchTaskItemStatus.PROCESSING);
                            saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
                            createDesktop(successCount, createVdiDesktopRequest, taskItemRequest, taskItemRequestList);
                        } catch (Exception e) {
                            errIdentityList.add(createVdiDesktopRequest.getUserName());
                            LOGGER.error(String.format("创建云桌面异常,用户名:[%s]", createVdiDesktopRequest.getUserName()), e);
                            dealException(createVdiDesktopRequest, taskItemRequest, e, taskItemRequestList);
                        } finally {
                            countDownLatch.countDown();
                        }
                    }, 180);
                } catch (BusinessException e) {
                    LOGGER.error(String.format("锁异常,任务ID:[%s]用户名:[%s]", customTaskId.toString(), createVdiDesktopRequest.getUserName()), e);
                    dealException(createVdiDesktopRequest, taskItemRequest, e, taskItemRequestList);
                }
            });
        }
    }

    private void saveTaskResult(CreateVDIDesktopRequest[] createVDIDesktopArr, List<TaskItemRequest> taskItemRequestList,
                                AtomicInteger successCount, List<String> errIdentityList) {
        String taskResult = getTaskResult(successCount);
        String errorMsg = getErrorMsg(errIdentityList);
        saveBatchTaskResult(createVDIDesktopArr.length == successCount.get()
                ? OpenApiTaskState.FINISHED : OpenApiTaskState.ERROR, taskItemRequestList, taskResult, errorMsg);
    }

    private void createDesktop(AtomicInteger successCount, CreateVDIDesktopRequest createVdiDesktopRequest,
                               TaskItemRequest taskItemRequest, List<TaskItemRequest> taskItemRequestList) throws BusinessException {
        UUID userId = userInfoAPI.getUserIdByUserName(createVdiDesktopRequest.getUserName());
        CreateCloudDesktopRequest createCloudDesktopRequest = new CreateCloudDesktopRequest();
        createCloudDesktopRequest.setUserId(userId);
        createCloudDesktopRequest.setDesktopImageId(createVdiDesktopRequest.getImageId());
        createCloudDesktopRequest.setNetworkId(createVdiDesktopRequest.getNetworkId());
        createCloudDesktopRequest.setStrategyId(createVdiDesktopRequest.getDeskStrategyId());
        createCloudDesktopRequest.setCustomTaskId(UUID.randomUUID());
        createCloudDesktopRequest.setClusterId(createVdiDesktopRequest.getClusterId());
        createCloudDesktopRequest.setDeskSpec(buildDeskSpec(createVdiDesktopRequest));
        createCloudDesktopRequest.setPlatformId(createVdiDesktopRequest.getPlatformId());

        CreateDesktopResponse response = userDesktopMgmtAPI.create(createCloudDesktopRequest);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.DESK_ID, response.getId());
        jsonObject.put(Constant.TASK_ID, createCloudDesktopRequest.getCustomTaskId());
        jsonObject.put(Constant.USER_NAME, createVdiDesktopRequest.getUserName());
        jsonObject.put(Constant.DESK_NAME, response.getDesktopName());
        taskItemRequest.setItemBizData(jsonObject);
        successCount.incrementAndGet();
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.SUCCESS);
        taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_CREATE_SUCCESS));
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
    }

    private void dealException(CreateVDIDesktopRequest createVdiDesktopRequest,
                               TaskItemRequest taskItemRequest, Exception e, List<TaskItemRequest> taskItemRequestList) {
        FAIL_TASK_ID.put(customTaskId, customTaskId);
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            if (businessException.getKey().equals(BusinessKey.RCDC_USER_USER_MESSAGE_NOT_EXIST)) {
                LOGGER.error("user[{}] is not exist!", createVdiDesktopRequest.getUserName());
                String rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_USER_NAME_NOT_EXIST, createVdiDesktopRequest.getUserName());
                String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_CREATE_FAIL, rs);
                taskItemRequest.setItemResult(describe);
            } else {
                LOGGER.error("AsyncCreateVDIDesktop error!", e);
                String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_CREATE_FAIL, businessException.getI18nMessage());
                taskItemRequest.setItemResult(describe);
            }
        } else {
            String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_CREATE_SYSTEM_ERROR);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_CREATE_FAIL, systemErrorTip);
            taskItemRequest.setItemResult(describe);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.USER_NAME, createVdiDesktopRequest.getUserName());
        taskItemRequest.setItemBizData(jsonObject);
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
    }

    private void batchDelDesktop(List<TaskItemRequest> taskItemRequestList, CountDownLatch countDownLatch) {
        String batchSoftDeleteVdiDesktopThreadMain = "create_back_batch_delete_vdi_desktop_thread";
        for (TaskItemRequest taskItemRequest : taskItemRequestList) {
            if (taskItemRequest.getItemBizStatus() == BatchTaskItemStatus.FAILURE) {
                countDownLatch.countDown();
                continue;
            }
            if (taskItemRequest.getItemBizData() != null) {
                taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
                ThreadExecutors.execute(batchSoftDeleteVdiDesktopThreadMain, () -> {
                    try {
                        LockableExecutor.executeWithTryLock(customTaskId.toString(), () -> {
                            delDesktop(countDownLatch, taskItemRequest, taskItemRequestList);
                        }, 60);
                    } catch (BusinessException e) {
                        LOGGER.error(String.format("锁异常,任务Id:[%s]", customTaskId), e);
                    }

                });
            } else {
                LOGGER.warn("业务数据异常,任务Id:[{}]", taskItemRequest.getMainTaskId());
                countDownLatch.countDown();
            }
        }
    }

    private void delDesktop(CountDownLatch countDownLatch, TaskItemRequest taskItemRequest, List<TaskItemRequest> taskItemRequestList) {
        MoveDesktopToRecycleBinRequest moveDesktopToRecycleBinRequest = new MoveDesktopToRecycleBinRequest();
        JSONObject itemBizData = taskItemRequest.getItemBizData();
        UUID deskId = itemBizData.getObject(Constant.DESK_ID, UUID.class);
        moveDesktopToRecycleBinRequest.setDesktopId(deskId);
        try {
            //移动回收站
            userDesktopMgmtAPI.moveDesktopToRecycleBin(moveDesktopToRecycleBinRequest);
            //删除云桌面
            recycleBinMgmtAPI.delete(new IdRequest(deskId));
            itemBizData.put(Constant.ROLLBACK, true);
            String rollbackTxt = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_ROLLBACK_SUCCESS);
            taskItemRequest.setItemResult(taskItemRequest.getItemResult() + "，" + rollbackTxt);
            saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_ROLLBACK_FAIL,
                        businessException.getI18nMessage());
                taskItemRequest.setItemResult(describe);
            } else {
                LOGGER.error(String.format("回滚删除云桌面失败,任务Id：[%s]", this.customTaskId), e);
                String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_DELETE_SYSTEM_ERROR);
                String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_ROLLBACK_FAIL, systemErrorTip);
                taskItemRequest.setItemResult(describe);
            }
            itemBizData.put(Constant.ROLLBACK, false);
            saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
        } finally {
            countDownLatch.countDown();
        }
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
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_CREATE_TASK_FAIL_RESULT,
                        String.valueOf(errorMsg));
            } else {
                for (int i = 0; i < segNum; i++) {
                    errorMsg.append(errIdentityList.get(i)).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_CREATE_TASK_FAILS_RESULT,
                        String.valueOf(errorMsg), errIdentityList.size() + "");
            }
        }
        return rs;
    }

    private String getTaskResult(AtomicInteger successCount) {
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_BATCH_CREATE_TASK_RESULT,
                successCount.get() + "", batchCreateVDIDesktopRequest.getDeskInfoArr().length - successCount.get() + "");
    }


    public void setUserInfoAPI(UserInfoAPI userInfoAPI) {
        this.userInfoAPI = userInfoAPI;
    }

    public void setBatchCreateVDIDesktopRequest(BatchCreateVDIDesktopRequest batchCreateVDIDesktopRequest) {
        this.batchCreateVDIDesktopRequest = batchCreateVDIDesktopRequest;
    }

    public void setRecycleBinMgmtAPI(UserRecycleBinMgmtAPI recycleBinMgmtAPI) {
        this.recycleBinMgmtAPI = recycleBinMgmtAPI;
    }
}
