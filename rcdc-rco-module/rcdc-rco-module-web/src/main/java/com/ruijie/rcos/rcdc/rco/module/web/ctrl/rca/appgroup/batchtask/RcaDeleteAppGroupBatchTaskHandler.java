package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupDTO;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 批量删除融合应用池分组
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年1月9日
 *
 * @author zhengjingyong
 */
public class RcaDeleteAppGroupBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaDeleteAppGroupBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppGroupAPI rcaAppGroupAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private String poolName = "";
    
    private String groupName = "";

    public RcaDeleteAppGroupBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
                                             RcaAppPoolAPI rcaAppPoolAPI, RcaAppGroupAPI rcaAppGroupAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.rcaAppGroupAPI = rcaAppGroupAPI;
        this.rcaAppPoolAPI = rcaAppPoolAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        UUID groupId = batchTaskItem.getItemID();
        try {
            RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(groupId);
            groupName = groupDTO.getName();

            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());
            poolName = appPoolBaseDTO.getName();

            rcaAppGroupAPI.deleteAppGroup(groupId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_ITEM_SUCCESS, poolName, groupName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_ITEM_SUCCESS)
                    .msgArgs(new String[]{poolName, groupName}).build();
        } catch (Exception e) {
            LOGGER.error("删除应用池[{}]分组[{}]发生异常，ex :", poolName, groupName, e);
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_ITEM_FAIL,
                    poolName, groupName, errorMsg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_ITEM_FAIL)
                    .msgArgs(new String[]{poolName, groupName, errorMsg}).build();

        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_RESULT);
    }
}
