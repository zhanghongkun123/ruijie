package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateIDVDeskStrategyIdRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.EditDesktopStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktop.common.DesktopStrategyCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.BatchEditStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.EditStrategyRequest;
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

import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.*;

/**
 * Description: 批量变更云桌面的云桌面标签线程
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 16:41
 *
 * @author zdc
 */
public class AsyncBatchEditStrategyThread extends AbstractAsyncTaskThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBatchEditStrategyThread.class);

    private BatchEditStrategyRequest request;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private DeskStrategyAPI deskStrategyAPI;

    private UamAppTestAPI uamAppTestAPI;

    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    private DesktopStrategyCommonHelper desktopStrategyCommonHelper;

    private DeskSpecAPI deskSpecAPI;

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 5000;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(Constant.BATCH_EDIT_DESKTOP_STRATEGY_THREAD).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    /**
     * 当前运行中任务数量
     */
    private static final AtomicInteger TASK_RUN_COUNT = new AtomicInteger(0);

    /**
     * 用于记录失败任务id
     */
    private static final Map<UUID, UUID> FAIL_TASK_ID = Maps.newConcurrentMap();

    public AsyncBatchEditStrategyThread(AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI, BatchEditStrategyRequest request)
            throws BusinessException {
        super(UUID.randomUUID(), action, openApiTaskInfoAPI);
        this.request = request;
    }

    @Override
    public void run() {
        batchEditDesktopStrategy();
    }

    private void batchEditDesktopStrategy() {
        EditStrategyRequest[] deskInfoDTOArr = request.getDeskInfoArr();
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
        batchEditDesktopStrategy(countDownLatch, taskItemRequestList, successCount, errIdentityList);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(String.format("批量编辑云桌面策略超时,任务Id：[%s]", this.customTaskId), e);
            Thread.currentThread().interrupt();
        } finally {
            //任务Id释放掉
            FAIL_TASK_ID.remove(customTaskId);
        }
        TASK_RUN_COUNT.addAndGet(-deskInfoDTOArr.length);
        saveTaskResult(deskInfoDTOArr, taskItemRequestList, successCount, errIdentityList);
    }

    private void batchEditDesktopStrategy(CountDownLatch countDownLatch, List<TaskItemRequest> taskItemRequestList,
                                          AtomicInteger successCount, List<String> errIdentityList) {
        for (TaskItemRequest taskItemRequest : taskItemRequestList) {
            THREAD_EXECUTOR.execute(() -> {
                EditStrategyRequest request = taskItemRequest.getRequestBody().toJavaObject(EditStrategyRequest.class);
                try {
                    LockableExecutor.executeWithTryLock(customTaskId.toString(), () -> {
                        try {
                            CbbCloudDeskPattern targetPattern = deskStrategyAPI.findPatternById(request.getStrategyId());
                            if (Objects.isNull(targetPattern)) {
                                throw new BusinessException(RCDC_RCO_STRATEGY_NOT_FOUND, request.getStrategyId().toString());
                            }
                            desktopStrategyCommonHelper.checkStrategy(request.getDeskId(), request.getStrategyId());
                            editDesktopStrategy(request);
                            // 保存子任务
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(Constant.DESK_ID, request.getDeskId());
                            taskItemRequest.setItemBizData(jsonObject);
                            successCount.incrementAndGet();
                            taskItemRequest.setItemBizStatus(BatchTaskItemStatus.SUCCESS);
                            taskItemRequest.setItemResult(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_STRATEGY_SUCCESS));
                            saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
                        } catch (Exception e) {
                            errIdentityList.add(request.getDeskId().toString());
                            LOGGER.error(String.format("编辑云桌面标签异常,桌面id:[%s]", request.getDeskId()), e);
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

    private void editDesktopStrategy(EditStrategyRequest request) throws BusinessException {
        UUID deskId = request.getDeskId();
        UUID strategyId = request.getStrategyId();
        CloudDesktopDetailDTO desktopDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        if (desktopDTO.getUserProfileStrategyId() != null && CbbCloudDeskPattern.RECOVERABLE != deskStrategyAPI.findPatternById(strategyId)) {
            throw new BusinessException(RCDC_OPEN_API_REST_DESKTOP_EDIT_STRATEGY_CONFLICT_WITH_UPM, desktopDTO.getDesktopName());
        }
        CbbCloudDeskPattern targetPattern = deskStrategyAPI.findPatternById(strategyId);

        Boolean isTargetStrategyNotEqPersonal =
                CbbCloudDeskPattern.PERSONAL.name().equals(desktopDTO.getDesktopType()) && targetPattern != CbbCloudDeskPattern.PERSONAL;
        // 个性切还原/应用分层需要校验桌面测试信息
        if (isTargetStrategyNotEqPersonal) {
            uamAppTestAPI.checkTestingDesk(deskId);
        }

        CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(desktopDTO.getDesktopCategory());
        LOGGER.info("准备编辑修改云桌面[id:{}]策略", desktopDTO.getId());
        switch (deskType) {
            case IDV:
                cbbIDVDeskMgmtAPI.updateDeskStrategyIDV(new CbbUpdateIDVDeskStrategyIdRequest(deskId, strategyId));
                // TCI公共终端策略变更下发：
                if (deskStrategyTciNotifyAPI != null) {
                    deskStrategyTciNotifyAPI.notifyDeskFetchStartParams(deskId);
                }
                // 发送水印配置
                desktopDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
                deskStrategyAPI.sendDesktopStrategyWatermark(desktopDTO);

                if (Boolean.TRUE.equals(isTargetStrategyNotEqPersonal)) {
                    appDeliveryMgmtAPI.deleteDeliveryObjectWhenStrategyModify(deskId);
                    try {
                        uamAppTestAPI.deleteCompletedTestDeskWhenStrategyModify(deskId);
                    } catch (BusinessException e) {
                        LOGGER.error("删除桌面[{}]测试组异常", deskId, e);
                    }
                }
                break;
            case VDI:
                checkBeforeEditStrategy(desktopDTO, strategyId, null);
                userDesktopMgmtAPI.configStrategy(new EditDesktopStrategyRequest(deskId, strategyId));
                break;
            default:
                throw new BusinessException("不支持的云桌面类型:{0}", deskType.name());
        }
    }

    private void dealException(EditStrategyRequest request,
                               TaskItemRequest taskItemRequest, Exception e, List<TaskItemRequest> taskItemRequestList) {
        FAIL_TASK_ID.put(customTaskId, customTaskId);
        taskItemRequest.setItemBizStatus(BatchTaskItemStatus.FAILURE);
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            LOGGER.error("AsyncEnableVDIDesktop error!", e);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_STRATEGY_FAIL,
                        businessException.getI18nMessage());
            taskItemRequest.setItemResult(describe);
        } else {
            String systemErrorTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_STRATEGY_SYSTEM_ERROR);
            String describe = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_STRATEGY_FAIL, systemErrorTip);
            taskItemRequest.setItemResult(describe);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.DESK_ID, request.getDeskId());
        taskItemRequest.setItemBizData(jsonObject);
        saveBatchTaskResult(OpenApiTaskState.RUNNING, taskItemRequestList, "", "");
    }

    private void saveTaskResult(EditStrategyRequest[] deskInfoDTO, List<TaskItemRequest> taskItemRequestList,
                                AtomicInteger successCount, List<String> errIdentityList) {
        String taskResult = getTaskResult(successCount);
        String errorMsg = getErrorMsg(errIdentityList);
        saveBatchTaskResult(deskInfoDTO.length == successCount.get()
                ? OpenApiTaskState.FINISHED : OpenApiTaskState.ERROR, taskItemRequestList, taskResult, errorMsg);
    }

    private String getTaskResult(AtomicInteger successCount) {
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_STRATEGY_TASK_RESULT,
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
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_STRATEGY_FAIL_RESULT,
                        String.valueOf(errorMsg));
            } else {
                for (int i = 0; i < segNum; i++) {
                    errorMsg.append(errIdentityList.get(i)).append(",");
                }
                errorMsg = new StringBuilder(errorMsg.substring(0, errorMsg.length() - 1));
                rs = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CLOUDDESKTOP_DESK_EDIT_STRATEGY_FAILS_RESULT,
                        String.valueOf(errorMsg), errIdentityList.size() + "");
            }
        }
        return rs;
    }

    private void checkBeforeEditStrategy(CloudDesktopDetailDTO desktopDTO, UUID strategyId, UUID desktopPoolId) throws BusinessException {
        if (Objects.isNull(desktopPoolId) && Objects.equals(desktopDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
            // 没传池ID是针对单个桌面的操作，同时桌面因为是动态池桌面不支持单独修改
            throw new BusinessException(RCDC_RCO_DESKTOP_EDIT_STRATEGY_DYNAMIC_POOL_NOT_SUPPORT, desktopDTO.getDesktopName());
        }
    }

    /**
     *
     * @param userDesktopMgmtAPI userDesktopMgmtAPI
     * @return this
     */
    public AsyncBatchEditStrategyThread setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
        return this;
    }

    /**
     *
     * @param deskStrategyAPI deskStrategyAPI
     * @return this
     */
    public AsyncBatchEditStrategyThread setDesktopSpecAPI(DeskStrategyAPI deskStrategyAPI) {
        this.deskStrategyAPI = deskStrategyAPI;
        return this;
    }

    /**
     *
     * @param uamAppTestAPI uamAppTestAPI
     * @return this
     */
    public AsyncBatchEditStrategyThread setUamAppTestAPI(UamAppTestAPI uamAppTestAPI) {
        this.uamAppTestAPI = uamAppTestAPI;
        return this;
    }

    /**
     *
     * @param cbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI
     * @return this
     */
    public AsyncBatchEditStrategyThread setCbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        this.cbbIDVDeskMgmtAPI = cbbIDVDeskMgmtAPI;
        return this;
    }

    /**
     *
     * @param deskStrategyTciNotifyAPI deskStrategyTciNotifyAPI
     * @return this
     */
    public AsyncBatchEditStrategyThread setDeskStrategyTciNotifyAPI(DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI) {
        this.deskStrategyTciNotifyAPI = deskStrategyTciNotifyAPI;
        return this;
    }

    /**
     *
     * @param appDeliveryMgmtAPI appDeliveryMgmtAPI
     * @return this
     */
    public AsyncBatchEditStrategyThread setAppDeliveryMgmtAPI(AppDeliveryMgmtAPI appDeliveryMgmtAPI) {
        this.appDeliveryMgmtAPI = appDeliveryMgmtAPI;
        return this;
    }

    /**
     *
     * @param desktopStrategyCommonHelper desktopStrategyCommonHelper
     * @return this
     */
    public AsyncBatchEditStrategyThread setDesktopStrategyCommonHelper(DesktopStrategyCommonHelper desktopStrategyCommonHelper) {
        this.desktopStrategyCommonHelper = desktopStrategyCommonHelper;
        return this;
    }

    /**
     *
     * @param deskSpecAPI desktopSpecAPI
     * @return this
     */
    public AsyncBatchEditStrategyThread setDesktopSpecAPI(DeskSpecAPI deskSpecAPI) {
        this.deskSpecAPI = deskSpecAPI;
        return this;
    }
}