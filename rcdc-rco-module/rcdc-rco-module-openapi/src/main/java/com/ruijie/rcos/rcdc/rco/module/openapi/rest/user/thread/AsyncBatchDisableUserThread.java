package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType.IDV;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType.VDI;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.OPEN_API_REST_USER_NOT_EXISTS;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.OPEN_API_USER_CLOSE_DSEK_FAIL;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.BatchDisableUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.DisableUserRequest;
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
 * Description: 批量禁用用户线程
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 16:41
 *
 * @author zdc
 */
public class AsyncBatchDisableUserThread extends AbstractAsyncUserMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBatchDisableUserThread.class);

    private BatchDisableUserRequest batchDisableUserRequest;

    private IacUserMgmtAPI cbbUserAPI;

    private UserMgmtAPI userMgmtAPI;

    private UserInfoAPI userInfoAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 5000;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(Constant.BATCH_DISABLE_USER_THREAD).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    /**
     * 当前运行中任务数量
     */
    private static final AtomicInteger TASK_RUN_COUNT = new AtomicInteger(0);

    /**
     * 用于记录失败任务id
     */
    private static final Map<UUID, UUID> FAIL_TASK_ID = Maps.newConcurrentMap();

    public AsyncBatchDisableUserThread(AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                       BatchDisableUserRequest batchDisableUserRequest) throws BusinessException {
        super(UUID.randomUUID(), action, openApiTaskInfoAPI);
        this.batchDisableUserRequest = batchDisableUserRequest;
    }

    @Override
    public void run() {
        batchDisableUser();
    }

    private void batchDisableUser() {
        String[] userArr = batchDisableUserRequest.getUserArr();
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
        //批量禁用用户
        batchDisableUser(countDownLatch, taskItemRequestList, successCount, errIdentityList);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(String.format("批量禁用用户超时,任务Id：[%s]", this.customTaskId), e);
            Thread.currentThread().interrupt();
        } finally {
            //任务Id释放掉
            FAIL_TASK_ID.remove(customTaskId);
        }
        TASK_RUN_COUNT.addAndGet(-userArr.length);
        saveTaskResult(userArr, taskItemRequestList, successCount, errIdentityList);
    }

    private void batchDisableUser(CountDownLatch countDownLatch, List<TaskItemRequest> taskItemRequestList,
                                  AtomicInteger successCount, List<String> errIdentityList) {
        for (TaskItemRequest taskItemRequest : taskItemRequestList) {
            THREAD_EXECUTOR.execute(() -> {
                DisableUserRequest disableUserRequest = taskItemRequest.getRequestBody().toJavaObject(DisableUserRequest.class);
                try {
                    LockableExecutor.executeWithTryLock(customTaskId.toString(), () -> {
                        try {
                            String userName = disableUserRequest.getUserName();
                            UserUtils.checkUserName(userName);
                            IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(userName);
                            if (Objects.isNull(cbbUserDetailDTO)) {
                                throw new BusinessException(OPEN_API_REST_USER_NOT_EXISTS, userName);
                            }
                            // 禁用用户
                            UUID userId = cbbUserDetailDTO.getId();
                            if (cbbUserDetailDTO.getUserState() == IacUserStateEnum.DISABLE) {
                                saveItemSuccess(disableUserRequest, taskItemRequest, successCount, taskItemRequestList);
                                return;
                            }
                            cbbUserDetailDTO.setUserState(IacUserStateEnum.DISABLE);
                            final IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
                            BeanUtils.copyProperties(cbbUserDetailDTO, cbbUpdateUserDTO);
                            cbbUserAPI.updateUser(cbbUpdateUserDTO);
                            notifyTerminal(userId, cbbUserDetailDTO, userName);
                            LOGGER.info("禁用用户关闭桌面，用户为[{}]", userName);
                            boolean isSuccess = this.closeVm(cbbUserDetailDTO);
                            if (!isSuccess) {
                                throw new BusinessException(OPEN_API_USER_CLOSE_DSEK_FAIL, userName);
                            }
                            saveItemSuccess(disableUserRequest, taskItemRequest, successCount, taskItemRequestList);
                        } catch (Exception e) {
                            errIdentityList.add(disableUserRequest.getUserName());
                            LOGGER.error(String.format("禁用用户异常,用户名:[%s]", disableUserRequest.getUserName()), e);
                            dealException(disableUserRequest, taskItemRequest, e, taskItemRequestList);
                        } finally {
                            countDownLatch.countDown();
                        }
                    },180);
                } catch (BusinessException e) {
                    LOGGER.error(String.format("锁异常,任务ID:[%s]用户名:[%s]", customTaskId.toString(), disableUserRequest.getUserName()), e);
                    dealException(disableUserRequest, taskItemRequest, e, taskItemRequestList);
                }
            });
        }
    }

    private void saveItemSuccess(DisableUserRequest disableUserRequest, TaskItemRequest taskItemRequest,
                                 AtomicInteger successCount, List<TaskItemRequest> taskItemRequestList) {
        String userName = disableUserRequest.getUserName();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.USER_NAME, userName);
        taskItemRequest.setItemBizData(jsonObject);
        successCount.incrementAndGet();
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.SUCCESS);
        taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_DISABLE_SUCCESS));
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
    }

    private void dealException(DisableUserRequest disableUserRequest,
                               TaskItemRequest taskItemRequest, Exception e, List<TaskItemRequest> taskItemRequestList) {
        FAIL_TASK_ID.put(customTaskId, customTaskId);
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            LOGGER.error("AsyncEnableVDIDesktop error!", e);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_DISABLE_FAIL, businessException.getI18nMessage());
            taskItemRequest.setItemResult(describe);
        } else {
            String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_DISABLE_SYSTEM_ERROR);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_DISABLE_FAIL, systemErrorTip);
            taskItemRequest.setItemResult(describe);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.USER_NAME, disableUserRequest.getUserName());
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
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_BATCH_DISABLE_TASK_RESULT,
                successCount.get() + "", batchDisableUserRequest.getUserArr().length - successCount.get() + "");
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
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_BATCH_DISABLE_TASK_FAIL_RESULT,
                        String.valueOf(errorMsg));
            } else {
                for (int i = 0; i < segNum; i++) {
                    errorMsg.append(errIdentityList.get(i)).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_BATCH_DISABLE_TASK_FAILS_RESULT,
                        String.valueOf(errorMsg), errIdentityList.size() + "");
            }
        }
        return rs;
    }

    private void notifyTerminal(UUID userId, IacUserDetailDTO cbbUserDetailDTO, String userName) {

        LOGGER.info("禁用用户主动推送用户状态，用户为[{}]", userName);
        userMgmtAPI.syncUserInfoToTerminal(cbbUserDetailDTO);
        LOGGER.info("禁用用户并退出终端会话，用户为[{}]", userName);
        userInfoAPI.userLogout(userId);
        //通知web端用户退出
        userInfoAPI.notifyWebUserLogout(userId);
    }

    private boolean closeVm(IacUserDetailDTO cbbUserDetailDTO) {
        Assert.notNull(cbbUserDetailDTO, "cbbUserDetailDTO is null");
        UUID userId = cbbUserDetailDTO.getId();
        String userName = cbbUserDetailDTO.getUserName();
        // 关闭用户关联的桌面
        String logFlag = null;
        boolean isSuccess = true;
        final List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllDesktopByUserId(userId);
        for (CloudDesktopDTO desktop : desktopList) {
            try {
                logFlag = isBlank(desktop.getDesktopName()) ? desktop.getId().toString() : desktop.getDesktopName();
                if (!Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.RUNNING.toString())
                        && !Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.SLEEP.toString())) {
                    continue;
                }
                CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(desktop.getDesktopCategory());
                LOGGER.info("准备下发关闭{}类型云桌面[id={}]命令", deskType.name(), desktop.getId());
                if (deskType == IDV) {
                    CbbShutdownDeskIDVDTO shutdownDeskIDVDTO = new CbbShutdownDeskIDVDTO();
                    shutdownDeskIDVDTO.setId(desktop.getId());
                    shutdownDeskIDVDTO.setIsForce(Boolean.FALSE);
                    shutdownDeskIDVDTO.setTimeout(TimeUnit.MINUTES.toMillis(5));
                    cbbIDVDeskOperateAPI.shutdownDeskIDV(shutdownDeskIDVDTO);
                } else if (deskType == VDI) {
                    if (Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.SLEEP.toString())) {
                        cloudDesktopOperateAPI.start(new CloudDesktopStartRequest(desktop.getCbbId()));
                    }
                    cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(desktop.getId(), Boolean.FALSE));
                } else {
                    LOGGER.error("用户[{}]不支持对云桌面类型：[{}]，进行关闭", logFlag, deskType.name());
                }
            } catch (Exception e) {
                LOGGER.error(String.format("用户[%s]禁用成功，关闭桌面[%s]失败", userName, logFlag), e);
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    /**
     *
     * @param cbbUserAPI cbbUserAPI
     * @return this
     */
    public AsyncBatchDisableUserThread setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
        return this;
    }

    /**
     *
     * @param userMgmtAPI userMgmtAPI
     * @return this
     */
    public AsyncBatchDisableUserThread setUserMgmtAPI(UserMgmtAPI userMgmtAPI) {
        this.userMgmtAPI = userMgmtAPI;
        return this;
    }

    /**
     *
     * @param userInfoAPI userInfoAPI
     * @return this
     */
    public AsyncBatchDisableUserThread setUserInfoAPI(UserInfoAPI userInfoAPI) {
        this.userInfoAPI = userInfoAPI;
        return this;
    }

    /**
     *
     * @param userDesktopMgmtAPI userDesktopMgmtAPI
     * @return this
     */
    public AsyncBatchDisableUserThread setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
        return this;
    }

    /**
     *
     * @param cbbIDVDeskOperateAPI cbbIDVDeskOperateAPI
     * @return this
     */
    public AsyncBatchDisableUserThread setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        this.cbbIDVDeskOperateAPI = cbbIDVDeskOperateAPI;
        return this;
    }

    /**
     *
     * @param cloudDesktopOperateAPI cloudDesktopOperateAPI
     * @return this
     */
    public AsyncBatchDisableUserThread setCloudDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }
}