package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask;

import java.util.*;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupEntityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdGroupAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import org.springframework.util.Assert;


import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 修改应用分组分配安全组数据批处理handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/02/25
 *
 * @author zhengjingyong
 */
public class RcaUnbindAppGroupAdGroupBatchHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaUnbindAppGroupAdGroupBatchHandler.class);

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaAppGroupAPI rcaAppGroupAPI;

    private RcaGroupMemberAPI rcaGroupMemberAPI;

    private BaseAuditLogAPI auditLogAPI;

    private IacAdGroupAPI cbbAdGroupAPI;

    private UUID appGroupId;

    private String adGroupName = "";

    public RcaUnbindAppGroupAdGroupBatchHandler(Iterator<? extends BatchTaskItem> iterator, RcaAppPoolAPI rcaAppPoolAPI,
                                                RcaAppGroupAPI rcaAppGroupAPI, RcaGroupMemberAPI rcaGroupMemberAPI, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.rcaAppGroupAPI = rcaAppGroupAPI;
        this.rcaGroupMemberAPI = rcaGroupMemberAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        UUID adGroupId = taskItem.getItemID();
        try {
            RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(appGroupId);
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());
            if (RcaEnum.PoolState.AVAILABLE != appPoolBaseDTO.getPoolState()) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_APP_POOL_UNAVAILABLE_UPDATE_BIND_FORBID,
                        groupDTO.getName(), appPoolBaseDTO.getName());
            }

            IacAdGroupEntityDTO adGroupDetail = cbbAdGroupAPI.getAdGroupDetail(adGroupId);
            adGroupName = adGroupDetail.getName();
            rcaGroupMemberAPI.unbindAdGroup(appGroupId, adGroupId);

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_AD_GROUP_TASK_ITEM_SUCCESS, adGroupName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_AD_GROUP_TASK_ITEM_SUCCESS)
                    .msgArgs(new String[]{adGroupName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_AD_GROUP_TASK_ITEM_FAIL,
                    adGroupName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_AD_GROUP_TASK_ITEM_FAIL)
                    .msgArgs(new String[]{adGroupName, e.getI18nMessage()}).build();
        }
    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount,
                RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_AD_GROUP_TASK_RESULT);
    }

    public UUID getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(UUID appGroupId) {
        this.appGroupId = appGroupId;
    }

    public IacAdGroupAPI getCbbAdGroupAPI() {
        return cbbAdGroupAPI;
    }

    public void setCbbAdGroupAPI(IacAdGroupAPI cbbAdGroupAPI) {
        this.cbbAdGroupAPI = cbbAdGroupAPI;
    }
}
