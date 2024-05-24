package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbDeleteDeliveryAppDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UamDeliveryAppDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryAppBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.beans.BeanUtils;
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
public class DeleteUamDeliveryAppBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUamDeliveryAppBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    private GeneralPermissionHelper generalPermissionHelper;

    private SessionContext sessionContext;

    private String appName;

    private String groupName;

    private boolean batchFlag = true;

    public DeleteUamDeliveryAppBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbAppDeliveryMgmtAPI(CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI) {
        this.cbbAppDeliveryMgmtAPI = cbbAppDeliveryMgmtAPI;
    }

    public void setAppDeliveryMgmtAPI(AppDeliveryMgmtAPI appDeliveryMgmtAPI) {
        this.appDeliveryMgmtAPI = appDeliveryMgmtAPI;
    }

    public void setAppCenterPermissionHelper(GeneralPermissionHelper generalPermissionHelper) {
        this.generalPermissionHelper = generalPermissionHelper;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setBatchFlag(boolean batchFlag) {
        this.batchFlag = batchFlag;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID id = batchTaskItem.getItemID();
        String uamAppName = null;
        String deliveryGroupName = null;
        try {
            UamDeliveryAppDTO uamDeliveryApp = appDeliveryMgmtAPI.findDeliveryAppById(id);

            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail =
                    cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(uamDeliveryApp.getDeliveryGroupId());
            deliveryGroupName = cbbUamDeliveryGroupDetail.getDeliveryGroupName();
            uamAppName = uamDeliveryApp.getAppName();

            // 校验是否有该记录权限
            generalPermissionHelper.checkPermission(sessionContext, uamDeliveryApp.getDeliveryGroupId(), AdminDataPermissionType.DELIVERY_GROUP);

            CbbDeleteDeliveryAppDTO cbbDeleteDeliveryAppDTO = new CbbDeleteDeliveryAppDTO();
            BeanUtils.copyProperties(uamDeliveryApp, cbbDeleteDeliveryAppDTO);
            cbbAppDeliveryMgmtAPI.deleteUamDeliveryApp(cbbDeleteDeliveryAppDTO);
            LOGGER.info("删除交付应用[{}]成功", id);
            auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_DELETE_SUCCESS_LOG, deliveryGroupName, uamAppName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_DELETE_SUCCESS_LOG)
                    .msgArgs(new String[]{deliveryGroupName, uamAppName}).build();
        } catch (BusinessException e) {
            LOGGER.error("删除交付应用[{}]失败，失败原因：", id, e);

            if (CbbAppCenterBusinessKey.RCDC_UAM_DELIVERY_APP_NOT_EXIST.equals(e.getKey())) {
                auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_NOT_EXISTS_DELETE_FAIL_LOG, e.getI18nMessage());
                throw new BusinessException(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_NOT_EXISTS_DELETE_FAIL_LOG, e, e.getI18nMessage());
            }

            auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_DELETE_FAIL_LOG,
                    deliveryGroupName, uamAppName, e.getI18nMessage());
            throw new BusinessException(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_DELETE_FAIL_LOG, e, deliveryGroupName, uamAppName,
                    e.getI18nMessage());
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量删除返回结果
        if (batchFlag) {
            return buildDefaultFinishResult(successCount, failCount, UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_DELETE_SUCCESS_RESULT);
        }

        // 删除单个应用
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_DELETE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[]{groupName, appName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_DELETE_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[]{groupName, appName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }


}
