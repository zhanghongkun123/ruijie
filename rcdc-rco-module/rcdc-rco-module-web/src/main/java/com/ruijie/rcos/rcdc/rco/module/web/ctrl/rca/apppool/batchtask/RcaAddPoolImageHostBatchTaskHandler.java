package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.RcaCreateCloudDesktopRequest;
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
 * Description: 追加应用池派生主机
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年1月5日
 *
 * @author zhengjingyong
 */
public class RcaAddPoolImageHostBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RcaAddPoolImageHostBatchTaskHandler.class);

    private static final String NAME_FORMAT = "%s_%s";

    private BaseAuditLogAPI auditLogAPI;

    private AtomicInteger startIndex;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaAppPoolBaseDTO rcaAppPoolBaseDTO;

    private CreateRcaHostDesktopAPI createRcaHostDesktopAPI;


    public RcaAddPoolImageHostBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator,
                                               CbbDeskMgmtAPI cbbDeskMgmtAPI, RcaAppPoolAPI rcaAppPoolAPI,
                                               Integer startIndex) {
        super(batchTaskItemIterator);
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.cbbDeskMgmtAPI = cbbDeskMgmtAPI;
        this.startIndex = new AtomicInteger(startIndex);
    }

    public void setRcaAppPoolBaseDTO(RcaAppPoolBaseDTO rcaAppPoolBaseDTO) {
        this.rcaAppPoolBaseDTO = rcaAppPoolBaseDTO;
    }

    public void setCreateRcaHostDesktopAPI(CreateRcaHostDesktopAPI createRcaHostDesktopAPI) {
        this.createRcaHostDesktopAPI = createRcaHostDesktopAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");

        UUID desktopId = UUID.randomUUID();
        LOGGER.info("开始创建应用池[{}]中的派生云主机桌面, deskId:{}", rcaAppPoolBaseDTO.getName(), desktopId);

        String desktopName = buildDesktopName(rcaAppPoolBaseDTO.getName());
        LOGGER.info("开始创建派生云主机桌面, 云桌面名称{}", desktopName);

        CreateAppPoolHostBatchTaskItem taskItem = (CreateAppPoolHostBatchTaskItem) batchTaskItem;
        RcaCreateCloudDesktopRequest createCloudDesktopRequest = taskItem.getRequest();
        createCloudDesktopRequest.setPoolId(rcaAppPoolBaseDTO.getId());
        createCloudDesktopRequest.setPoolType(rcaAppPoolBaseDTO.getPoolType());
        createCloudDesktopRequest.setDesktopName(desktopName);
        createCloudDesktopRequest.setSessionType(rcaAppPoolBaseDTO.getSessionType());
        createCloudDesktopRequest.setDesktopId(desktopId);
        createCloudDesktopRequest.setClusterId(rcaAppPoolBaseDTO.getClusterId());
        createCloudDesktopRequest.setMaxHostSessionNum(rcaAppPoolBaseDTO.getMaxHostSessionNum());
        createCloudDesktopRequest.setBatchTaskItem(taskItem);

        createRcaHostDesktopAPI.create(desktopId, createCloudDesktopRequest);

        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_ITEM_SUCCESS,
                createCloudDesktopRequest.getDesktopName());

        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_ITEM_SUCCESS).msgArgs(createCloudDesktopRequest.getDesktopName())
                .build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 派生主机创建完成后，不需要刷新应用池的应用列表，仅需要更新池的主机数量
        try {
            rcaAppPoolAPI.updateHostNum(rcaAppPoolBaseDTO.getId(), successCount);
        } catch (BusinessException e) {
            LOGGER.error("更新应用主机总数发生异常，应用池：{}， ex: ", rcaAppPoolBaseDTO.getId(), e);
        }

        return buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_ADD_HOST_TASK_RESULT);
    }


    private synchronized String buildDesktopName(String deskNamePrefix) throws BusinessException {
        String name = String.format(NAME_FORMAT, deskNamePrefix, startIndex.incrementAndGet());
        while (cbbDeskMgmtAPI.checkDesktopNameExist(name)) {
            LOGGER.warn("已存在云桌面名称[{}]，重新生成", name);
            name = String.format(NAME_FORMAT, deskNamePrefix, startIndex.incrementAndGet());
        }
        return name;
    }

}

