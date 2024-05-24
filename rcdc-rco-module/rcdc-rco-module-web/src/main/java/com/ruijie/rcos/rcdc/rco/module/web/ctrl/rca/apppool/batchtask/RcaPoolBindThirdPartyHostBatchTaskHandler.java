package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
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
 * Description: 变更应用池第三方主机批任务处理器
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月21日
 *
 * @author zhengjingyong
 */
public class RcaPoolBindThirdPartyHostBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaEditAppPoolBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaHostAPI rcaHostAPI;

    private UUID appPoolId;

    private String appPoolName;

    public RcaPoolBindThirdPartyHostBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
                                                     RcaAppPoolAPI rcaAppPoolAPI, RcaHostAPI rcaHostAPI, UUID appPoolId) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.rcaHostAPI = rcaHostAPI;
        this.appPoolId = appPoolId;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        String hostName = "";
        try {
            UUID hostId = batchTaskItem.getItemID();
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(appPoolId);
            appPoolName = appPoolBaseDTO.getName();

            RcaHostDTO hostDTO = rcaHostAPI.getById(hostId);
            hostName = hostDTO.getName();

            rcaAppPoolAPI.bindThirdPartyHost(appPoolId, Lists.newArrayList(hostId));

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_BIND_HOST_TASK_ITEM_SUCCESS, appPoolName, hostName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_BIND_HOST_TASK_ITEM_SUCCESS)
                    .msgArgs(new String[] {appPoolName, hostName}).build();
        } catch (Exception e) {
            LOGGER.error("应用池[{}]绑定第三方主机[{}]发生异常，ex :", appPoolName, hostName, e);
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_BIND_HOST_TASK_ITEM_FAIL, appPoolName, hostName, errorMsg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_BIND_HOST_TASK_ITEM_FAIL)
                    .msgArgs(new String[]{appPoolName, hostName, errorMsg}).build();
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount > 0) {
            try {
                rcaAppPoolAPI.mergeAppFromHost(appPoolId);
                rcaAppPoolAPI.notifyTerminalAppChange(appPoolId);
            } catch (Exception ex) {
                LOGGER.error("刷新应用并通知终端发生异常，应用池id:[{}], ex: ", appPoolId, ex);
            }
        }

        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BIND_HOST_SUCCESS)
                    .msgArgs(new String[]{appPoolName})
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BIND_HOST_FAIL)
                    .msgArgs(new String[]{appPoolName})
                    .build();
        }
    }
}
