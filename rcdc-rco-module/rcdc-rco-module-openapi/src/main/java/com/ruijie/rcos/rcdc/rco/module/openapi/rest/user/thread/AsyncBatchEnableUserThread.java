package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread;

import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.OPEN_API_REST_USER_NOT_EXISTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.BatchEnableUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.EnableUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.util.UserUtils;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 批量启用用户线程
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 16:41
 *
 * @author zdc
 */
public class AsyncBatchEnableUserThread extends AbstractAsyncUserMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBatchEnableUserThread.class);

    private BatchEnableUserRequest batchEnableUserRequest;

    private IacUserMgmtAPI cbbUserAPI;

    private UserMgmtAPI userMgmtAPI;

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 5000;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(Constant.BATCH_ENABLE_USER_THREAD).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    /**
     * 当前运行中任务数量
     */
    private static final AtomicInteger TASK_RUN_COUNT = new AtomicInteger(0);

    /**
     * 用于记录失败任务id
     */
    private static final Map<UUID, UUID> FAIL_TASK_ID = Maps.newConcurrentMap();

    public AsyncBatchEnableUserThread(AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                      BatchEnableUserRequest batchEnableUserRequest) throws BusinessException {
        super(UUID.randomUUID(), action, openApiTaskInfoAPI);
        this.batchEnableUserRequest = batchEnableUserRequest;
    }

    @Override
    public void run() {
        batchEnableUser();
    }

    private void batchEnableUser() {
        String[] userArr = batchEnableUserRequest.getUserArr();
        List<TaskItemRequest> taskItemRequestList = new ArrayList<>();
        List<String> errIdentityList = new ArrayList<>();
        //并发（当前队列任务大小+任务数量之和）超出设置最大值后，任务设置为失败，并给出对应提示："超出批量任务并发的最大值"
        if (TASK_RUN_COUNT.get() + userArr.length > QUEUE_SIZE) {
            String taskNumOverTimeTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_NUM_OVERTIME);
            saveBatchTaskResult(OpenApiTaskState.ERROR, taskItemRequestList, taskNumOverTimeTip, taskNumOverTimeTip);
            return;
        }
        TASK_RUN_COUNT.addAndGet(userArr.length);
        CountDownLatch countDownLatch = new CountDownLatch(userArr.length);
        AtomicInteger successCount = new AtomicInteger(0);
        taskItemRequestList = Arrays.stream(userArr).map(s -> {
            TaskItemRequest taskItemRequest = new TaskItemRequest();
            taskItemRequest.setId(s);
            taskItemRequest.setMainTaskId(customTaskId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.USER_NAME, s);
            taskItemRequest.setItemBizData(jsonObject);
            taskItemRequest.setRequestBody((JSONObject) JSON.toJSON(jsonObject));
            return taskItemRequest;
        }).collect(Collectors.toList());
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
        //批量启用用户
        batchEnableUser(countDownLatch, taskItemRequestList, successCount, errIdentityList);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(String.format("批量启用用户超时,任务Id：[%s]", this.customTaskId), e);
            Thread.currentThread().interrupt();
        } finally {
            //任务Id释放掉
            FAIL_TASK_ID.remove(customTaskId);
        }
        TASK_RUN_COUNT.addAndGet(-userArr.length);
        saveTaskResult(userArr, taskItemRequestList, successCount, errIdentityList);
    }

    private void batchEnableUser(CountDownLatch countDownLatch, List<TaskItemRequest> taskItemRequestList,
                                 AtomicInteger successCount, List<String> errIdentityList) {
        for (TaskItemRequest taskItemRequest : taskItemRequestList) {
            THREAD_EXECUTOR.execute(() -> {
                EnableUserRequest enableUserRequest = taskItemRequest.getRequestBody().toJavaObject(EnableUserRequest.class);
                try {
                    LockableExecutor.executeWithTryLock(customTaskId.toString(), () -> {
                        try {
                            String userName = enableUserRequest.getUserName();
                            UserUtils.checkUserName(userName);
                            IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(userName);
                            if (Objects.isNull(cbbUserDetailDTO)) {
                                throw new BusinessException(OPEN_API_REST_USER_NOT_EXISTS, userName);
                            }
                            // 启用用户
                            if (cbbUserDetailDTO.getUserState() != IacUserStateEnum.ENABLE) {
                                final IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
                                BeanUtils.copyProperties(cbbUserDetailDTO, cbbUpdateUserDTO);
                                cbbUpdateUserDTO.setUserState(IacUserStateEnum.ENABLE);
                                cbbUserAPI.updateUser(cbbUpdateUserDTO);
                                //同步用户信息给IDV/TCI终端
                                cbbUserDetailDTO.setUserState(IacUserStateEnum.ENABLE);
                                userMgmtAPI.syncUserInfoToTerminal(cbbUserDetailDTO);
                            }
                            // 保存子任务
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(Constant.USER_NAME, userName);
                            taskItemRequest.setItemBizData(jsonObject);
                            successCount.incrementAndGet();
                            taskItemRequest.setItemBizStatus(BatchTaskItemStatus.SUCCESS);
                            taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ENABLE_SUCCESS));
                            saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
                        } catch (Exception e) {
                            errIdentityList.add(enableUserRequest.getUserName());
                            LOGGER.error(String.format("启用用户异常,用户名:[%s]", enableUserRequest.getUserName()), e);
                            dealException(enableUserRequest, taskItemRequest, e, taskItemRequestList);
                        } finally {
                            countDownLatch.countDown();
                        }
                    },180);
                } catch (BusinessException e) {
                    LOGGER.error(String.format("锁异常,任务ID:[%s]用户名:[%s]", customTaskId.toString(), enableUserRequest.getUserName()), e);
                    dealException(enableUserRequest, taskItemRequest, e, taskItemRequestList);
                }
            });
        }
    }

    private void dealException(EnableUserRequest enableUserRequest,
                               TaskItemRequest taskItemRequest, Exception e, List<TaskItemRequest> taskItemRequestList) {
        FAIL_TASK_ID.put(customTaskId, customTaskId);
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            LOGGER.error("AsyncEnableVDIDesktop error!", e);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ENABLE_FAIL, businessException.getI18nMessage());
            taskItemRequest.setItemResult(describe);
        } else {
            String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ENABLE_SYSTEM_ERROR);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ENABLE_FAIL, systemErrorTip);
            taskItemRequest.setItemResult(describe);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.USER_NAME, enableUserRequest.getUserName());
        taskItemRequest.setItemBizData(jsonObject);
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
    }

    private void saveTaskResult(String[] userArr, List<TaskItemRequest> taskItemRequestList,
                                AtomicInteger successCount, List<String> errIdentityList) {
        String taskResult = getTaskResult(successCount);
        String errorMsg = getErrorMsg(errIdentityList);
        saveBatchTaskResult(userArr.length == successCount.get()
                ? OpenApiTaskState.FINISHED : OpenApiTaskState.ERROR, taskItemRequestList, taskResult, errorMsg);
    }

    private String getTaskResult(AtomicInteger successCount) {
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_BATCH_ENABLE_TASK_RESULT,
                successCount.get() + "", batchEnableUserRequest.getUserArr().length - successCount.get() + "");
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
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_BATCH_ENABLE_TASK_FAIL_RESULT,
                        String.valueOf(errorMsg));
            } else {
                for (int i = 0; i < segNum; i++) {
                    errorMsg.append(errIdentityList.get(i)).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_BATCH_ENABLE_TASK_FAILS_RESULT,
                        String.valueOf(errorMsg), errIdentityList.size() + "");
            }
        }
        return rs;
    }

    /**
     *
     * @param cbbUserAPI cbbUserAPI
     * @return this
     */
    public AsyncBatchEnableUserThread setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
        return this;
    }

    /**
     *
     * @param userMgmtAPI userMgmtAPI
     * @return this
     */
    public AsyncBatchEnableUserThread setUserMgmtAPI(UserMgmtAPI userMgmtAPI) {
        this.userMgmtAPI = userMgmtAPI;
        return this;
    }
}