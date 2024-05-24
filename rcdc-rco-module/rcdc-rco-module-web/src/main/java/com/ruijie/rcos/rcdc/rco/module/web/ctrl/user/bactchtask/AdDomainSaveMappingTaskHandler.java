package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.callback.DomainSaveMappingCallBack;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
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
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月22日
 *
 * @author zhanghongkun
 */


public class AdDomainSaveMappingTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdDomainSaveMappingTaskHandler.class);

    private IacAdMgmtAPI cbbAdMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private Boolean enableSyncUser;

    private DomainSaveMappingCallBack domainSaveMappingCallBack;

    private String syncHintKey;

    public AdDomainSaveMappingTaskHandler(IacAdMgmtAPI cbbAdMgmtAPI, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                          DomainSaveMappingCallBack domainSaveMappingCallBack) {
        super(batchTaskItem);
        Assert.notNull(cbbAdMgmtAPI, "the cbbBackupDetailAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");

        this.cbbAdMgmtAPI = cbbAdMgmtAPI;
        this.auditLogAPI = auditLogAPI;
        this.domainSaveMappingCallBack = domainSaveMappingCallBack;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        DomainSaveMappingBatchTaskItem adItem = (DomainSaveMappingBatchTaskItem) batchTaskItem;
        LOGGER.info("adItem解析为:{}", JSONObject.toJSONString(adItem));
        try {
            cbbAdMgmtAPI.saveAdLocalMappingConfig(adItem.getCbbSaveDomainMappingConfigDTO());
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_AD_SAVE_MAPPING_SUCCESS, new String[] {syncHintKey});
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_AD_SAVE_MAPPING_SUCCESS).msgArgs(new String[] {syncHintKey}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_AD_SAVE_MAPPING_FAIL, e, syncHintKey, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_AD_SAVE_MAPPING_FAIL, e, syncHintKey, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount == 1) {
            if (BooleanUtils.isTrue(enableSyncUser)) {
                LOGGER.info("enableSyncUser为:[{0}]开始同步AD域用户", enableSyncUser);
                try {
                    domainSaveMappingCallBack.callBackSyncUser();
                } catch (BusinessException e) {
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_AD_SYNC_USER_FAIL_BY_EXCEPTION, e, e.getI18nMessage());
                    return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_AD_SYNC_USER_FAIL_BY_EXCEPTION)
                            .msgArgs(new String[] {e.getI18nMessage()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
                }
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_AD_SAVE_MAPPING_SUCCESS).msgArgs(new String[] {syncHintKey})
                    .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_AD_SAVE_MAPPING_FAIL_RESULT)
                    .msgArgs(new String[] {syncHintKey, String.valueOf(successCount), String.valueOf(failCount)})
                    .batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    public void setEnableSyncUser(Boolean enableSyncUser) {
        this.enableSyncUser = enableSyncUser;
    }

    public void setSyncHintKey(String syncHintKey) {
        this.syncHintKey = syncHintKey;
    }
}
