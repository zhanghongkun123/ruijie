package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryGroupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29 11:53
 *
 * @author coderLee23
 */
public class DeleteUamDeliveryGroupBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUamDeliveryGroupBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    private GeneralPermissionHelper generalPermissionHelper;

    private SessionContext sessionContext;

    private String deliveryGroupName;

    private boolean batchFlag = true;

    public DeleteUamDeliveryGroupBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbAppDeliveryMgmtAPI(CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI) {
        this.cbbAppDeliveryMgmtAPI = cbbAppDeliveryMgmtAPI;
    }

    public void setAppCenterPermissionHelper(GeneralPermissionHelper generalPermissionHelper) {
        this.generalPermissionHelper = generalPermissionHelper;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setBatchFlag(boolean batchFlag) {
        this.batchFlag = batchFlag;
    }

    public void setDeliveryGroupName(String deliveryGroupName) {
        this.deliveryGroupName = deliveryGroupName;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID deliveryGroupId = batchTaskItem.getItemID();
        String groupName = null;
        try {
            // 判定是否存在交付组
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
            groupName = cbbUamDeliveryGroupDetail.getDeliveryGroupName();

            // 校验是否有该记录权限
            generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

            // 删除交付组
            cbbAppDeliveryMgmtAPI.deleteUamDeliveryGroup(cbbUamDeliveryGroupDetail);
            // 删除对应的权限
            generalPermissionHelper.deletePermission(deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

            LOGGER.info("删除交付组[{} {}]成功", deliveryGroupId, groupName);
            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_DELETE_SUCCESS_LOG, groupName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_DELETE_SUCCESS_LOG).msgArgs(groupName).build();
        } catch (BusinessException e) {
            LOGGER.error("删除交付应用[{}]失败，失败原因：", deliveryGroupId, e);

            if (CbbAppCenterBusinessKey.RCDC_UAM_DELIVERY_GROUP_NOT_EXIST.equals(e.getKey())) {
                auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_NOT_EXIST_DELETE_FAIL_LOG, e.getI18nMessage());
                throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_NOT_EXIST_DELETE_FAIL_LOG, e, e.getI18nMessage());
            }

            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_DELETE_FAIL_LOG, groupName, e.getI18nMessage());
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_DELETE_FAIL_LOG, e, groupName, e.getI18nMessage());
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量删除返回结果
        if (batchFlag) {
            return buildDefaultFinishResult(successCount, failCount, UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_DELETE_SUCCESS_RESULT);
        }

        // 删除单个应用
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_DELETE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {deliveryGroupName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_DELETE_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[] {deliveryGroupName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }


}
