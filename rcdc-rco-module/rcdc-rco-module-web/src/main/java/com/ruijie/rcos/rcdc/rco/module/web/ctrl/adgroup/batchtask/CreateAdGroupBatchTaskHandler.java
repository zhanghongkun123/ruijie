package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.batchtask;

import java.util.Iterator;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupEntityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacCreateAdGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdGroupAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.AdGroupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.dto.CreateAdGroupWebDTO;
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
 * Description: 删除AD域组handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-09-22
 *
 * @author zqj
 */
public class CreateAdGroupBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAdGroupBatchTaskHandler.class);

    private IacAdGroupAPI cbbAdGroupAPI;


    private BaseAuditLogAPI auditLogAPI;

    private IacAdMgmtAPI cbbAdMgmtAPI;

    private RccmManageAPI rccmManageAPI;

    private String adGroupName;


    public CreateAdGroupBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, IacAdGroupAPI cbbAdGroupAPI,
                                         BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItemIterator);
        this.cbbAdGroupAPI = cbbAdGroupAPI;
        this.auditLogAPI = auditLogAPI;
    }

    public void setAdGroupMgmtAPI(IacAdGroupAPI cbbAdGroupAPI) {
        this.cbbAdGroupAPI = cbbAdGroupAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setAdGroupName(String adGroupName) {
        this.adGroupName = adGroupName;
    }

    public void setCbbAdMgmtAPI(IacAdMgmtAPI cbbAdMgmtAPI) {
        this.cbbAdMgmtAPI = cbbAdMgmtAPI;
    }

    public void setRccmManageAPI(RccmManageAPI rccmManageAPI) {
        this.rccmManageAPI = rccmManageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        CreateAdGroupBatchTaskItem createAdGroupBatchTaskItem = (CreateAdGroupBatchTaskItem) batchTaskItem;
        CreateAdGroupWebDTO createAdGroupWebDTO = createAdGroupBatchTaskItem.getCreateAdGroupDTO();
        try {
            IacAdGroupEntityDTO cbbAdGroupEntityDTO = cbbAdGroupAPI.getAdGroupByObjectGuid(createAdGroupWebDTO.getObjectGuid());
            if (cbbAdGroupEntityDTO != null) {
                throw new BusinessException(AdGroupBusinessKey.RCDC_RCO_AD_GROUP_NAME_EXIST, createAdGroupWebDTO.getName());
            }

            IacAdGroupDTO adGroupByObjectGuid =  cbbAdMgmtAPI.getAdGroupByObjectGuid(createAdGroupWebDTO.getObjectGuid());
            if (adGroupByObjectGuid == null) {
                throw new BusinessException(AdGroupBusinessKey.RCDC_RCO_AD_SERVER_GROUP_NOT_EXIST, createAdGroupWebDTO.getName());
            }

            IacCreateAdGroupDTO iacCreateAdGroupDTO = new IacCreateAdGroupDTO();
            BeanUtils.copyProperties(createAdGroupWebDTO, iacCreateAdGroupDTO);
            cbbAdGroupAPI.createAdGroup(iacCreateAdGroupDTO);

            // 推送AD域安全组数据到rccm
            rccmManageAPI.pushAdGroupToRccm();

            // 记录审计日志
            auditLogAPI.recordLog(AdGroupBusinessKey.RCDC_RCO_AD_GROUP_CREATE_SUCCESS_LOG, iacCreateAdGroupDTO.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(AdGroupBusinessKey.RCDC_RCO_AD_GROUP_CREATE_ITEM_SUCCESS_DESC).msgArgs(createAdGroupWebDTO.getName())
                    .build();
        } catch (Exception e) {
            LOGGER.error("创建AD域组[{}]发生异常，异常原因：", createAdGroupWebDTO.getName(), e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(AdGroupBusinessKey.RCDC_RCO_AD_GROUP_CREATE_FAIL_LOG, createAdGroupWebDTO.getName(), message);
            throw new BusinessException(AdGroupBusinessKey.RCDC_RCO_AD_GROUP_CREATE_ITEM_FAIL_DESC, e, createAdGroupWebDTO.getName(), message);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (adGroupName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(AdGroupBusinessKey.RCDC_RCO_AD_GROUP_CREATE_ITEM_SUCCESS_DESC)
                        .msgArgs(new String[]{adGroupName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(AdGroupBusinessKey.RCDC_RCO_AD_GROUP_CREATE_SINGLE_FAIL)
                        .msgArgs(new String[]{adGroupName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount, AdGroupBusinessKey.RCDC_RCO_BATCH_CREATE_AD_GROUP_TASK_RESULT);
        }
    }


}
