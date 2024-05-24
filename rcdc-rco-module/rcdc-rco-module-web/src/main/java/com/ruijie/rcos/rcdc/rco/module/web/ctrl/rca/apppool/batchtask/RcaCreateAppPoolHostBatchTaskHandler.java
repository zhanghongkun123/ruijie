package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.RcaCreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.CreateRcaHostDesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.CreateAppPoolHostBatchTaskItem;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 创建应用池主机批处理器
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月27日
 *
 * @author zhengjingyong
 */
public class RcaCreateAppPoolHostBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaCreateAppPoolHostBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaAppPoolBaseDTO rcaAppPoolBaseDTO;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private CreateRcaHostDesktopAPI createRcaHostDesktopAPI;

    private String desktopName;

    private static final String NAME_FORMAT = "%s_%s";

    private AtomicInteger startIndex;


    public RcaCreateAppPoolHostBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
                                                RcaAppPoolAPI rcaAppPoolAPI, Integer startIndex) {
        super(batchTaskItemIterator);
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.auditLogAPI = auditLogAPI;
        this.startIndex = new AtomicInteger(startIndex);
    }

    public RcaAppPoolBaseDTO getRcaAppPoolBaseDTO() {
        return rcaAppPoolBaseDTO;
    }

    public void setRcaAppPoolBaseDTO(RcaAppPoolBaseDTO rcaAppPoolBaseDTO) {
        this.rcaAppPoolBaseDTO = rcaAppPoolBaseDTO;
    }

    public void setCbbDeskMgmtAPI(CbbDeskMgmtAPI cbbDeskMgmtAPI) {
        this.cbbDeskMgmtAPI = cbbDeskMgmtAPI;
    }

    public void setCreateRcaHostDesktopAPI(CreateRcaHostDesktopAPI createRcaHostDesktopAPI) {
        this.createRcaHostDesktopAPI = createRcaHostDesktopAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");

        try {
            UUID desktopId = UUID.randomUUID();
            LOGGER.info("开始创建应用池[{}]中的派生云主机桌面, deskId:{}", rcaAppPoolBaseDTO.getName(), desktopId);

            desktopName = buildDesktopName(rcaAppPoolBaseDTO.getName());
            LOGGER.info("开始创建派生云主机桌面, 云桌面名称{}", desktopName);

            CreateAppPoolHostBatchTaskItem taskItem = (CreateAppPoolHostBatchTaskItem) batchTaskItem;
            RcaCreateCloudDesktopRequest createCloudDesktopRequest = taskItem.getRequest();
            createCloudDesktopRequest.setPoolId(rcaAppPoolBaseDTO.getId());
            createCloudDesktopRequest.setPoolType(rcaAppPoolBaseDTO.getPoolType());
            createCloudDesktopRequest.setDesktopName(desktopName);
            createCloudDesktopRequest.setSessionType(rcaAppPoolBaseDTO.getSessionType());
            createCloudDesktopRequest.setDesktopId(desktopId);
            createCloudDesktopRequest.setMaxHostSessionNum(rcaAppPoolBaseDTO.getMaxHostSessionNum());
            createCloudDesktopRequest.setBatchTaskItem(taskItem);

            createRcaHostDesktopAPI.create(desktopId, createCloudDesktopRequest);

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_ITEM_SUCCESS,
                    createCloudDesktopRequest.getDesktopName());

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_ITEM_SUCCESS)
                    .msgArgs(createCloudDesktopRequest.getDesktopName()).build();
        } catch (Exception e) {
            LOGGER.error("创建应用池派生主机发生异常，主机名称[{}],ex :", desktopName, e);
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_ITEM_FAIL, desktopName, errorMsg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_ITEM_FAIL)
                    .msgArgs(new String[]{desktopName, errorMsg}).build();
        }

    }

    private synchronized String buildDesktopName(String deskNamePrefix) throws BusinessException {
        String name = String.format(NAME_FORMAT, deskNamePrefix, startIndex.incrementAndGet());
        while (cbbDeskMgmtAPI.checkDesktopNameExist(name)) {
            LOGGER.warn("已存在云桌面名称[{}]，重新生成", name);
            name = String.format(NAME_FORMAT, deskNamePrefix, startIndex.incrementAndGet());
        }
        return name;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        String[] strArr = new String[] {String.valueOf(successCount), String.valueOf(failCount), rcaAppPoolBaseDTO.getName()};
        // 将池置为可用
        try {
            rcaAppPoolAPI.updateAppPoolState(rcaAppPoolBaseDTO.getId(), RcaEnum.PoolState.AVAILABLE);
        } catch (BusinessException e) {
            LOGGER.error(String.format("更新应用池[%s]信息失败", rcaAppPoolBaseDTO.getId()), e);
        }

        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_TASK_RESULT).msgArgs(strArr)
                    .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            BatchTaskStatus batchTaskStatus;
            if (successCount == 0) {
                batchTaskStatus = BatchTaskStatus.FAILURE;
            } else {
                batchTaskStatus = BatchTaskStatus.PARTIAL_SUCCESS;
            }

            return DefaultBatchTaskFinishResult.builder().msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_TASK_RESULT).msgArgs(strArr)
                    .batchTaskStatus(batchTaskStatus).build();
        }

    }
}

