package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.ReleaseByBusinessIdRequest;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmStatus;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.EditAppPoolRequest;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 编辑融合应用池
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年1月1日
 *
 * @author zhengjingyong
 */
public class RcaEditAppPoolBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaEditAppPoolBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaHostAPI rcaHostAPI;

    private BaseAlarmAPI baseAlarmAPI;

    private String appPoolName;

    private EditAppPoolRequest editAppPoolRequest;

    private String errorMessage = "";


    private Boolean isHostChanged = false;

    public RcaEditAppPoolBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
                                          RcaAppPoolAPI rcaAppPoolAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.rcaAppPoolAPI = rcaAppPoolAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        try {
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(editAppPoolRequest.getId());
            appPoolName = appPoolBaseDTO.getName();

            List<RcaHostDTO> hostDTOList = rcaHostAPI.findAllByPoolIdIn(Collections.singletonList(editAppPoolRequest.getId()));

            // 保存更新信息
            rcaAppPoolAPI.editAppPool(editAppPoolRequest);

            // 处理告警信息
            handleAppPoolAlarm();

            // 更新第三方池信息
            thirdPartyHostChange(hostDTOList, editAppPoolRequest, appPoolBaseDTO);

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_EDIT_TASK_ITEM_SUCCESS, appPoolName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_EDIT_TASK_ITEM_SUCCESS).msgArgs(new String[]{appPoolName}).build();
        } catch (Exception e) {
            LOGGER.error("编辑应用池[{}]发生异常，ex :", appPoolName, e);
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            errorMessage = errorMsg;
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_EDIT_TASK_ITEM_FAIL, appPoolName, errorMsg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_EDIT_TASK_ITEM_FAIL)
                    .msgArgs(new String[]{appPoolName, errorMsg}).build();
        }


    }

    private void handleAppPoolAlarm() {
        if (Objects.nonNull(editAppPoolRequest.getPreStartHostNum())
                && editAppPoolRequest.getPreStartHostNum() == 0) { // 编辑应用池将预启动数量设置为0
            releaseIfExistAlarm(editAppPoolRequest.getId());
        }
    }

    private void releaseIfExistAlarm(UUID poolId) {
        if (Objects.isNull(poolId)) {
            return;
        }

        try { //防止告警问题影响定时任务
            String businessId = poolId.toString();
            ListAlarmRequest listRequest = new ListAlarmRequest();
            listRequest.setBusinessIdArr(new String[] { businessId });
            listRequest.setEnableQueryHistory(false);
            DefaultPageResponse<AlarmDTO> pageResponse =  baseAlarmAPI.listAlarmList(listRequest);
            if (pageResponse.getTotal() > 0) {
                ReleaseByBusinessIdRequest releaseByBusinessIdRequest = new ReleaseByBusinessIdRequest();
                releaseByBusinessIdRequest.setBusinessId(businessId);
                releaseByBusinessIdRequest.setAlarmType(AlarmConstants.ALARM_TYPE_RCDC_SERVICE);
                releaseByBusinessIdRequest.setAlarmStatus(AlarmStatus.AUTO_RELEASED);
                baseAlarmAPI.releaseByBusinessId(releaseByBusinessIdRequest);
                LOGGER.info("应用池[{}]将预启动数量设置为0，自动解除告警成功", poolId);
            }
        } catch (BusinessException e) {
            LOGGER.error("预启动数量设置为0，自动解除应用池{}预启动主机任务告警失败", poolId, e);
        }
    }

    private void thirdPartyHostChange(List<RcaHostDTO> hostDTOList, EditAppPoolRequest editAppPoolRequest,
                                      RcaAppPoolBaseDTO appPoolBaseDTO) throws BusinessException {
        if (RcaEnum.HostSourceType.THIRD_PARTY != appPoolBaseDTO.getHostSourceType()) {
            LOGGER.info("非第三方应用池，无需变更第三方主机");
            return;
        }

        List<UUID> editHostIdList = new ArrayList<>();
        if (editAppPoolRequest.getHostIdArr() != null) {
            editHostIdList = Arrays.stream(editAppPoolRequest.getHostIdArr()).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(editHostIdList) && CollectionUtils.isEmpty(hostDTOList)) {
            LOGGER.info("应用池没有主机，不需要更新主机信息，poolId :{}", appPoolBaseDTO.getId());
            return;
        }

        List<UUID> deleteHostIdList = hostDTOList.stream().map(RcaHostDTO::getId).collect(Collectors.toList());
        // 新增主机列表，绑定池和主机的关系
        List<UUID> addHostIdList = new ArrayList<>(editHostIdList);
        addHostIdList.removeAll(deleteHostIdList);
        if (!CollectionUtils.isEmpty(addHostIdList)) {
            isHostChanged = true;
            rcaAppPoolAPI.bindThirdPartyHost(appPoolBaseDTO.getId(), addHostIdList);
        }

        // 删除主机列表，解除池和主机的关系
        deleteHostIdList.removeAll(editHostIdList);
        if (!CollectionUtils.isEmpty(deleteHostIdList)) {
            isHostChanged = true;
            rcaAppPoolAPI.unbindThirdPartyHost(appPoolBaseDTO.getId(), deleteHostIdList);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            if (isHostChanged) {
                try {
                    rcaAppPoolAPI.mergeAppFromHost(editAppPoolRequest.getId());
                    rcaAppPoolAPI.notifyTerminalAppChange(editAppPoolRequest.getId());
                } catch (Exception ex) {
                    LOGGER.error("刷新应用并通知终端发生异常，应用池id:[{}], ex: ", editAppPoolRequest.getId(), ex);
                }
            }
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_SUCCESS)
                    .msgArgs(new String[]{appPoolName})
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_FAIL)
                    .msgArgs(new String[]{appPoolName, errorMessage})
                    .build();
        }
    }

    public void setEditAppPoolRequest(EditAppPoolRequest editAppPoolRequest) {
        this.editAppPoolRequest = editAppPoolRequest;
    }

    public void setRcaHostAPI(RcaHostAPI rcaHostAPI) {
        this.rcaHostAPI = rcaHostAPI;
    }

    public void setBaseAlarmAPI(BaseAlarmAPI baseAlarmAPI) {
        this.baseAlarmAPI = baseAlarmAPI;
    }
}
