package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.BatchEditVDIDeskRemarkRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.EditVDIDeskRemarkRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_EDIT_REMARK_DYNAMIC_POOL_NOT_SUPPORT;

/**
 * Description: 批量编辑云桌面标签线程
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 16:41
 *
 * @author zdc
 */
public class AsyncBatchEditVDIDeskRemarkThread extends AbstractAsyncTaskThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBatchEditVDIDeskRemarkThread.class);

    private BatchEditVDIDeskRemarkRequest request;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 5000;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(Constant.BATCH_EDIT_VDI_DESK_REMARK_THREAD).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    /**
     * 当前运行中任务数量
     */
    private static final AtomicInteger TASK_RUN_COUNT = new AtomicInteger(0);

    /**
     * 用于记录失败任务id
     */
    private static final Map<UUID, UUID> FAIL_TASK_ID = Maps.newConcurrentMap();

    public AsyncBatchEditVDIDeskRemarkThread(AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                             BatchEditVDIDeskRemarkRequest request) throws BusinessException {
        super(UUID.randomUUID(), action, openApiTaskInfoAPI);
        this.request = request;
    }

    @Override
    public void run() {
        batchEditRemark();
    }

    private void batchEditRemark() {
        EditVDIDeskRemarkRequest[] deskInfoDTOArr = request.getDeskInfoArr();

        List<TaskItemRequest> taskItemRequestList = new ArrayList<>();
        List<String> errIdentityList = new ArrayList<>();
        //并发（当前队列任务大小+任务数量之和）超出设置最大值后，任务设置为失败，并给出对应提示："超出批量任务并发的最大值"
        if (TASK_RUN_COUNT.get() + deskInfoDTOArr.length > QUEUE_SIZE) {
            String taskNumOverTimeTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_NUM_OVERTIME);
            saveBatchTaskResult(OpenApiTaskState.ERROR, taskItemRequestList, taskNumOverTimeTip, taskNumOverTimeTip);
            return;
        }
        TASK_RUN_COUNT.addAndGet(deskInfoDTOArr.length);
        CountDownLatch countDownLatch = new CountDownLatch(deskInfoDTOArr.length);
        AtomicInteger successCount = new AtomicInteger(0);
        taskItemRequestList = Arrays.stream(deskInfoDTOArr).map(s -> {
            TaskItemRequest taskItemRequest = new TaskItemRequest();
            taskItemRequest.setId(s.getDeskId().toString());
            taskItemRequest.setMainTaskId(customTaskId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.DESK_ID, s.getDeskId());
            taskItemRequest.setItemBizData(jsonObject);
            taskItemRequest.setRequestBody((JSONObject) JSON.toJSON(s));
            return taskItemRequest;
        }).collect(Collectors.toList());

        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
        batchEditVDIDeskRemark(countDownLatch, taskItemRequestList, successCount, errIdentityList);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(String.format("批量编辑云桌面标签超时,任务Id：[%s]", this.customTaskId), e);
            Thread.currentThread().interrupt();
        } finally {
            //任务Id释放掉
            FAIL_TASK_ID.remove(customTaskId);
        }
        TASK_RUN_COUNT.addAndGet(-deskInfoDTOArr.length);
        saveTaskResult(deskInfoDTOArr, taskItemRequestList, successCount, errIdentityList);
    }

    private void batchEditVDIDeskRemark(CountDownLatch countDownLatch, List<TaskItemRequest> taskItemRequestList,
                                        AtomicInteger successCount, List<String> errIdentityList) {
        for (TaskItemRequest taskItemRequest : taskItemRequestList) {
            THREAD_EXECUTOR.execute(() -> {
                EditVDIDeskRemarkRequest request = taskItemRequest.getRequestBody().toJavaObject(EditVDIDeskRemarkRequest.class);
                try {
                    LockableExecutor.executeWithTryLock(customTaskId.toString(), () -> {
                        try {
                            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(request.getDeskId());
                            checkBeforeEditDeskRemark(cloudDesktopDetailDTO);
                            // 更新云桌面标签
                            cbbVDIDeskMgmtAPI.updateVDIDeskRemark(request.getDeskId(), request.getRemark());
                            // 保存子任务
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(Constant.DESK_ID, request.getDeskId());
                            taskItemRequest.setItemBizData(jsonObject);
                            successCount.incrementAndGet();
                            taskItemRequest.setItemBizStatus(BatchTaskItemStatus.SUCCESS);
                            taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_REMARK_SUCCESS));
                            saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
                        } catch (Exception e) {
                            errIdentityList.add(request.getDeskId().toString());
                            LOGGER.error(String.format("编辑云桌面标签异常,桌面id:[%s]", request.getDeskId().toString()), e);
                            dealException(request, taskItemRequest, e, taskItemRequestList);
                        } finally {
                            countDownLatch.countDown();
                        }
                    },180);
                } catch (BusinessException e) {
                    LOGGER.error(String.format("锁异常,任务ID:[%s]桌面id:[%s]", customTaskId.toString(), request.getDeskId().toString()), e);
                    dealException(request, taskItemRequest, e, taskItemRequestList);
                }
            });
        }
    }

    private void checkBeforeEditDeskRemark(CloudDesktopDetailDTO desktopDTO) throws BusinessException {
        if (com.ruijie.rcos.sk.base.util.StringUtils.equals(desktopDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
            throw new BusinessException(RCDC_OPEN_API_REST_DESKTOP_EDIT_REMARK_DYNAMIC_POOL_NOT_SUPPORT, desktopDTO.getDesktopName());
        }
    }

    private void dealException(EditVDIDeskRemarkRequest request,
                               TaskItemRequest taskItemRequest, Exception e, List<TaskItemRequest> taskItemRequestList) {
        FAIL_TASK_ID.put(customTaskId, customTaskId);
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            LOGGER.error("AsyncEnableVDIDesktop error!", e);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_REMARK_FAIL,
                        businessException.getI18nMessage());
            taskItemRequest.setItemResult(describe);
        } else {
            String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_REMARK_SYSTEM_ERROR);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_REMARK_FAIL, systemErrorTip);
            taskItemRequest.setItemResult(describe);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.DESK_ID, request.getDeskId());
        taskItemRequest.setItemBizData(jsonObject);
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
    }

    private void saveTaskResult(EditVDIDeskRemarkRequest[] deskInfoDTO, List<TaskItemRequest> taskItemRequestList,
                                AtomicInteger successCount, List<String> errIdentityList) {
        String taskResult = getTaskResult(successCount);
        String errorMsg = getErrorMsg(errIdentityList);
        saveBatchTaskResult(deskInfoDTO.length == successCount.get()
                ? OpenApiTaskState.FINISHED : OpenApiTaskState.ERROR, taskItemRequestList, taskResult, errorMsg);
    }

    private String getTaskResult(AtomicInteger successCount) {
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_REMARK_TASK_RESULT,
                successCount.get() + "", request.getDeskInfoArr().length - successCount.get() + "");
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
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_REMARK_FAIL_RESULT,
                        String.valueOf(errorMsg));
            } else {
                for (int i = 0; i < segNum; i++) {
                    errorMsg.append(errIdentityList.get(i)).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_REMARK_FAILS_RESULT,
                        String.valueOf(errorMsg), errIdentityList.size() + "");
            }
        }
        return rs;
    }

    /**
     *
     * @param userDesktopMgmtAPI userDesktopMgmtAPI
     * @return this
     */
    public AsyncBatchEditVDIDeskRemarkThread setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
        return this;
    }

    /**
     *
     * @param cbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI
     * @return this
     */
    public AsyncBatchEditVDIDeskRemarkThread setCbbVDIDeskMgmtAPI(CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI) {
        this.cbbVDIDeskMgmtAPI = cbbVDIDeskMgmtAPI;
        return this;
    }
}