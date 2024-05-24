package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacLdapMgmtAPI;
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


public class LdapDomainSaveMappingTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapDomainSaveMappingTaskHandler.class);

    private IacLdapMgmtAPI cbbLdapMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private Boolean enableSyncUser;

    private DomainSaveMappingCallBack domainSaveMappingCallBack;

    private String syncHintKey;

    public LdapDomainSaveMappingTaskHandler(IacLdapMgmtAPI cbbLdapMgmtAPI, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                            DomainSaveMappingCallBack domainSaveMappingCallBack) {
        super(batchTaskItem);
        Assert.notNull(cbbLdapMgmtAPI, "the cbbBackupDetailAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");

        this.cbbLdapMgmtAPI = cbbLdapMgmtAPI;
        this.auditLogAPI = auditLogAPI;
        this.domainSaveMappingCallBack = domainSaveMappingCallBack;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        DomainSaveMappingBatchTaskItem ldapItem = (DomainSaveMappingBatchTaskItem) batchTaskItem;
        LOGGER.info("ldapItem解析为:{}", JSONObject.toJSONString(ldapItem));
        try {
            cbbLdapMgmtAPI.saveLdapLocalMappingConfig(ldapItem.getCbbSaveDomainMappingConfigDTO());
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_LDAP_SAVE_MAPPING_SUCCESS, new String[] {syncHintKey});
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_LDAP_SAVE_MAPPING_SUCCESS).msgArgs(new String[] {syncHintKey}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_LDAP_SAVE_MAPPING_FAIL, e, syncHintKey, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_LDAP_SAVE_MAPPING_FAIL, e, syncHintKey, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount == 1) {
            if (BooleanUtils.isTrue(enableSyncUser)) {
                LOGGER.info("enableSyncUser为:[{0}]开始同步LDAP域用户", enableSyncUser);
                try {
                    domainSaveMappingCallBack.callBackSyncUser();
                } catch (BusinessException e) {
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_LDAP_SYNC_USER_FAIL_BY_EXCEPTION, e.getI18nMessage());
                    return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_LDAP_SYNC_USER_FAIL_BY_EXCEPTION)
                            .msgArgs(new String[] {e.getI18nMessage()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
                }
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_LDAP_SAVE_MAPPING_SUCCESS)
                    .msgArgs(new String[] {syncHintKey}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_LDAP_SAVE_MAPPING_FAIL_RESULT)
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
