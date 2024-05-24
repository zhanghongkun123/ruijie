package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppStoreMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbPushInstallPackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamAppBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
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
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29 11:53
 *
 * @author coderLee23
 */
public class DeleteUamAppBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUamAppBatchTaskHandler.class);

    private boolean batchFlag = true;

    private BaseAuditLogAPI auditLogAPI;

    private CbbAppStoreMgmtAPI cbbbAppStoreMgmtAPI;


    private CbbUamAppTestAPI cbbUamAppTestAPI;

    private CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI;

    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    private SessionContext sessionContext;

    private GeneralPermissionHelper generalPermissionHelper;

    private String appName;

    private Boolean shouldOnlyDeleteDataFromDb;

    public void setBatchFlag(boolean batchFlag) {
        this.batchFlag = batchFlag;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbbAppStoreMgmtAPI(CbbAppStoreMgmtAPI cbbbAppStoreMgmtAPI) {
        this.cbbbAppStoreMgmtAPI = cbbbAppStoreMgmtAPI;
    }

    public void setCbbUamAppTestAPI(CbbUamAppTestAPI cbbUamAppTestAPI) {
        this.cbbUamAppTestAPI = cbbUamAppTestAPI;
    }

    public void setCbbPushInstallPackageMgmtAPI(CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI) {
        this.cbbPushInstallPackageMgmtAPI = cbbPushInstallPackageMgmtAPI;
    }

    public void setAppSoftwarePackageMgmtAPI(CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI) {
        this.cbbAppSoftwarePackageMgmtAPI = cbbAppSoftwarePackageMgmtAPI;
    }

    public void setCbbAppDeliveryMgmtAPI(CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI) {
        this.cbbAppDeliveryMgmtAPI = cbbAppDeliveryMgmtAPI;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setAppCenterPermissionHelper(GeneralPermissionHelper generalPermissionHelper) {
        this.generalPermissionHelper = generalPermissionHelper;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setShouldOnlyDeleteDataFromDb(Boolean shouldOnlyDeleteDataFromDb) {
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
    }

    public DeleteUamAppBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");

        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);

        UUID appId = batchTaskItem.getItemID();

        String uamAppName = null;
        try {
            // 判定是否存在应用
            CbbUamAppDTO cbbUamAppDTO = cbbbAppStoreMgmtAPI.getUamApp(appId);
            uamAppName = cbbUamAppDTO.getAppName();

            // 权限校验
            generalPermissionHelper.checkPermission(sessionContext, appId, AdminDataPermissionType.UAM_APP);
            // 判定应用是否被使用
            Boolean existsUsed = cbbAppDeliveryMgmtAPI.existsUsedDeliveryApp(appId);
            if (Boolean.TRUE.equals(existsUsed)) {
                LOGGER.error("应用{}正在被交付组使用，不允许删除", uamAppName);
                throw new BusinessException(UamAppBusinessKey.RCDC_UAM_APP_EXISTS_DELIVERY_GROUP_USED, uamAppName);
            }

            // 判定是否被测试组使用
            Boolean existsTestUsed = cbbUamAppTestAPI.existsUsedApplication(appId);
            if (Boolean.TRUE.equals(existsTestUsed)) {
                LOGGER.error("应用{}正在被测试组使用，不允许删除", uamAppName);
                throw new BusinessException(UamAppBusinessKey.RCDC_UAM_APP_EXISTS_DELIVERY_TEST_USED, uamAppName);
            }

            // 推送安装包
            if (cbbUamAppDTO.getAppType() == AppTypeEnum.PUSH_INSTALL_PACKAGE) {
                cbbPushInstallPackageMgmtAPI.deletePushInstallPackageByAppId(appId);
            } else {
                if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
                    cbbAppSoftwarePackageMgmtAPI.deleteAppSoftwarePackageFromDb(appId);
                } else {
                    cbbAppSoftwarePackageMgmtAPI.deleteAppSoftwarePackage(appId);
                }

            }

            // 删除所有相关权限数据
            generalPermissionHelper.deletePermission(appId, AdminDataPermissionType.UAM_APP);

            LOGGER.info("删除应用[{}]成功", appId);
            auditLogAPI.recordLog(UamAppBusinessKey.RCDC_UAM_APP_DELETE_SUCCESS_LOG, uamAppName, prefix);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UamAppBusinessKey.RCDC_UAM_APP_DELETE_SUCCESS_LOG).msgArgs(uamAppName, prefix).build();
        } catch (BusinessException e) {
            LOGGER.error("删除应用[{}]失败，失败原因：", appId, e);

            if (CbbAppCenterBusinessKey.RCDC_UAM_APP_NOT_EXIST.equals(e.getKey())) {
                auditLogAPI.recordLog(UamAppBusinessKey.RCDC_UAM_APP_NOT_EXISTS_DELETE_FAIL_LOG, e.getI18nMessage(), prefix);
                throw new BusinessException(UamAppBusinessKey.RCDC_UAM_APP_NOT_EXISTS_DELETE_FAIL_LOG, e, e.getI18nMessage(), prefix);
            }

            auditLogAPI.recordLog(UamAppBusinessKey.RCDC_UAM_APP_DELETE_FAIL_LOG, uamAppName, e.getI18nMessage(), prefix);
            throw new BusinessException(UamAppBusinessKey.RCDC_UAM_APP_DELETE_FAIL_LOG, e, uamAppName, e.getI18nMessage(), prefix);
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);

        // 批量删除返回结果
        if (batchFlag) {
            return WebBatchTaskUtils.buildDefaultFinishResult(successCount, failCount, UamAppBusinessKey.RCDC_UAM_APP_DELETE_SUCCESS_RESULT, prefix);
        }

        // 删除单个应用
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamAppBusinessKey.RCDC_UAM_APP_DELETE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {appName, prefix}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamAppBusinessKey.RCDC_UAM_APP_DELETE_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[] {appName, prefix}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

}
