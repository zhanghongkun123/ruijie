package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacSyncDomainUserDTO;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacCreateDomainUserRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.SyncAdUserArrRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.SyncAdUserRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 异步线程处理创建云桌面请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-03-16
 *
 * @author zqj
 */
public class AsyncBatchSyncAdUserThread extends AbstractAsyncUserMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBatchSyncAdUserThread.class);

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

    private IacUserMgmtAPI cbbUserAPI;

    private SyncAdUserArrRequest syncAdUserArrRequest;

    private UserDesktopConfigAPI userDesktopConfigAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private CbbDeskSpecAPI cbbDeskSpecAPI;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(Constant.BATCH_SYNC_AD_USER_THREAD).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    public AsyncBatchSyncAdUserThread(AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                      SyncAdUserArrRequest syncAdUserArrRequest) throws BusinessException {
        super(UUID.randomUUID(), action, openApiTaskInfoAPI);
        setSyncAdUserArrRequest(syncAdUserArrRequest);
    }

    @Override
    public void run() {
        batchTask();
    }

    private void batchTask() {
        List<TaskItemRequest> taskItemRequestList = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(0);
        List<String> errIdentityList = new ArrayList<>();
        List<SyncAdUserRequest> syncAdUserList = Arrays.stream(syncAdUserArrRequest.getUserInfoArr())
                .filter(distinctByKey(SyncAdUserRequest::getUserName)).collect(Collectors.toList());
        //并发（当前队列任务大小+任务数量之和）超出设置最大值后，任务设置为失败，并给出对应提示："超出批量任务并发的最大值"
        if (TASK_RUN_COUNT.get() + syncAdUserList.size() > QUEUE_SIZE) {
            String taskNumOverTimeTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_OPENAPI_ASYNC_TASK_NUM_OVERTIME);
            saveBatchTaskResult(OpenApiTaskState.ERROR, taskItemRequestList, taskNumOverTimeTip, taskNumOverTimeTip);
            return;
        }
        TASK_RUN_COUNT.addAndGet(syncAdUserList.size());
        CountDownLatch countDownLatch = new CountDownLatch(syncAdUserList.size());
        //批量创建AD域用户
        batchCreateDomainUser(taskItemRequestList, successCount, errIdentityList, syncAdUserList, countDownLatch);
        try {
            countDownLatch.await();
            saveTaskResult(taskItemRequestList, successCount, errIdentityList, syncAdUserList);
        } catch (InterruptedException e) {
            LOGGER.error(String.format("等待中断异常，批量同步AD域用户超时,任务Id：%s", this.customTaskId), e);
            saveTaskResult(taskItemRequestList, successCount, errIdentityList, syncAdUserList);
            Thread.currentThread().interrupt();
        }
    }

    private void batchCreateDomainUser(List<TaskItemRequest> taskItemRequestList, AtomicInteger successCount,
                                       List<String> errIdentityList, List<SyncAdUserRequest> syncAdUserList, CountDownLatch countDownLatch) {
        for (SyncAdUserRequest syncAdUserRequest : syncAdUserList) {
            TaskItemRequest taskItemRequest = new TaskItemRequest();
            taskItemRequest.setId(syncAdUserRequest.getId());
            taskItemRequestList.add(taskItemRequest);
            THREAD_EXECUTOR.execute(() -> {
                try {
                    //创建Ad 域用户
                    IacCreateDomainUserRequest iacCreateDomainUserRequest = new IacCreateDomainUserRequest();
                    iacCreateDomainUserRequest.setUserType(IacUserTypeEnum.AD);
                    iacCreateDomainUserRequest.setUserName(syncAdUserRequest.getUserName());
                    IacSyncDomainUserDTO domainUserDTO = cbbUserAPI.createDomainUser(iacCreateDomainUserRequest);
                    //构建任务结果
                    buildTaskResult(syncAdUserRequest, taskItemRequest, domainUserDTO);
                    //同步成功，创建云桌面
                    createDesktop(domainUserDTO);
                    successCount.incrementAndGet();
                } catch (BusinessException e) {
                    dealErrorIdentity(errIdentityList, syncAdUserRequest, taskItemRequest);
                    dealBusinessException(taskItemRequest, e.getI18nMessage());
                } catch (Exception e) {
                    LOGGER.error(String.format("同步AD域用户异常,用户名:%s", syncAdUserRequest.getUserName()), e);
                    dealErrorIdentity(errIdentityList, syncAdUserRequest, taskItemRequest);
                    //用户同步成功，存在云桌面创建失败，需要合并云桌面失败结果
                    String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_SYNC_AD_USER_SYSTEM_ERROR);
                    dealBusinessException(taskItemRequest, systemErrorTip);
                } finally {
                    TASK_RUN_COUNT.decrementAndGet();
                    countDownLatch.countDown();
                }
            });
        }
    }

    private void buildTaskResult(SyncAdUserRequest syncAdUserRequest, TaskItemRequest taskItemRequest, IacSyncDomainUserDTO domainUserDTO) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.USER_ID, domainUserDTO.getUserId());
        jsonObject.put(Constant.USER_NAME, syncAdUserRequest.getUserName());
        taskItemRequest.setItemBizData(jsonObject);
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.SUCCESS);
        taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_SYNC_AD_USER_SUCCESS));
    }

    private void dealBusinessException(TaskItemRequest taskItemRequest, String i18nMessage) {
        //用户同步成功，存在云桌面创建失败，需要合并云桌面失败结果
        if (StringUtils.hasText(taskItemRequest.getItemResult())) {
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_CREATE_FAIL, i18nMessage);
            taskItemRequest.setItemResult(taskItemRequest.getItemResult() + "," + describe);
        } else {
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_SYNC_AD_USER_FAIL, i18nMessage);
            taskItemRequest.setItemResult(describe);
        }
    }

    private void dealErrorIdentity(List<String> errIdentityList, SyncAdUserRequest syncAdUserRequest, TaskItemRequest taskItemRequest) {
        errIdentityList.add(syncAdUserRequest.getUserName());
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
    }

    private void createDesktop(IacSyncDomainUserDTO domainUserDTO) throws BusinessException {
        if (Boolean.TRUE.equals(domainUserDTO.getHasCreate())) {
            // 查用户组有没配置vdi桌面，有配置则创建桌面
            UserGroupDesktopConfigDTO desktopConfigDTO = userDesktopConfigAPI.getUserGroupDesktopConfig
                    (domainUserDTO.getGroupId(), UserCloudDeskTypeEnum.VDI);
            if (desktopConfigDTO != null) {
                //存在vdi桌面配置-创建桌面
                CreateCloudDesktopRequest createDesktopRequest = new CreateCloudDesktopRequest();
                createDesktopRequest.setDesktopImageId(desktopConfigDTO.getImageTemplateId());
                createDesktopRequest.setNetworkId(desktopConfigDTO.getNetworkId());
                createDesktopRequest.setStrategyId(desktopConfigDTO.getStrategyId());
                createDesktopRequest.setUserId(domainUserDTO.getUserId());
                createDesktopRequest.setClusterId(desktopConfigDTO.getClusterId());
                createDesktopRequest.setDeskSpec(cbbDeskSpecAPI.getById(desktopConfigDTO.getDeskSpecId()));
                createDesktopRequest.setPlatformId(desktopConfigDTO.getPlatformId());
                userDesktopMgmtAPI.create(createDesktopRequest);
            }
        }
    }

    private void saveTaskResult(List<TaskItemRequest> taskItemRequestList, AtomicInteger successCount,
                                List<String> errIdentityList, List<SyncAdUserRequest> syncAdUserList) {
        String errorMsg = getErrorMsg(errIdentityList);
        String taskResult = getTaskResult(successCount, syncAdUserList.size());
        LOGGER.info("保存任务结果：{}", taskResult);
        saveBatchTaskResult(syncAdUserList.size() == successCount.get()
                ? OpenApiTaskState.FINISHED : OpenApiTaskState.ERROR, taskItemRequestList, taskResult, errorMsg);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
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
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_SYNC_AD_USER_TASK_FAIL_RESULT, String.valueOf(errorMsg));
            } else {
                for (int i = 0; i < segNum; i++) {
                    errorMsg.append(errIdentityList.get(i)).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_SYNC_AD_USER_TASK_FAILS_RESULT,
                        String.valueOf(errorMsg), errIdentityList.size() + "");
            }
        }
        return rs;
    }

    private String getTaskResult(AtomicInteger successCount, int total) {
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_SYNC_AD_USER_TASK_RESULT,
                successCount.get() + "", total - successCount.get() + "");
    }

    public void setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
    }

    public void setSyncAdUserArrRequest(SyncAdUserArrRequest syncAdUserArrRequest) {
        this.syncAdUserArrRequest = syncAdUserArrRequest;
    }

    public void setUserDesktopConfigAPI(UserDesktopConfigAPI userDesktopConfigAPI) {
        this.userDesktopConfigAPI = userDesktopConfigAPI;
    }

    public void setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public void setCbbDeskSpecAPI(CbbDeskSpecAPI cbbDeskSpecAPI) {
        this.cbbDeskSpecAPI = cbbDeskSpecAPI;
    }
}
