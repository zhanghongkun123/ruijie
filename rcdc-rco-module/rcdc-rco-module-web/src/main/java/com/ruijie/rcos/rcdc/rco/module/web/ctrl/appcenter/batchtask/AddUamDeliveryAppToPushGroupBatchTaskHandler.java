package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppStoreMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbPushInstallPackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbAddDeliveryAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbPushInstallPackageDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryAppBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29 11:53
 *
 * @author coderLee23
 */
public class AddUamDeliveryAppToPushGroupBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddUamDeliveryAppToPushGroupBatchTaskHandler.class);

    private static final List<AppStatusEnum> ALLOW_ADD_APP_STATUS_LIST = Arrays.asList(AppStatusEnum.PUBLISHED, AppStatusEnum.PRE_PUBLISH);

    private BaseAuditLogAPI auditLogAPI;

    private CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI;

    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    private CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI;

    private GeneralPermissionHelper generalPermissionHelper;

    private SessionContext sessionContext;

    private String appName;

    private UUID deliveryGroupId;

    private boolean batchFlag = true;

    public AddUamDeliveryAppToPushGroupBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbAppStoreMgmtAPI(CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI) {
        this.cbbAppStoreMgmtAPI = cbbAppStoreMgmtAPI;
    }

    public void setCbbAppDeliveryMgmtAPI(CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI) {
        this.cbbAppDeliveryMgmtAPI = cbbAppDeliveryMgmtAPI;
    }

    public void setCbbPushInstallPackageMgmtAPI(CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI) {
        this.cbbPushInstallPackageMgmtAPI = cbbPushInstallPackageMgmtAPI;
    }

    public void setAppCenterPermissionHelper(GeneralPermissionHelper generalPermissionHelper) {
        this.generalPermissionHelper = generalPermissionHelper;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public void setBatchFlag(boolean batchFlag) {
        this.batchFlag = batchFlag;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID appId = batchTaskItem.getItemID();
        // 判定交付组是否存在,需要每次获取，添加时候有可能更新
        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
        List<CbbPushInstallPackageDetailDTO> pushInstallPackageDTOList =
                cbbPushInstallPackageMgmtAPI.listPushInstallPackageByUamAppIds(Arrays.asList(appId));
        String deliveryGroupName = cbbUamDeliveryGroupDetail.getDeliveryGroupName();
        String uamAppName = null;
        try {
            CbbUamAppDTO cbbUamAppDTO = cbbAppStoreMgmtAPI.getUamApp(appId);
            uamAppName = cbbUamAppDTO.getAppName();

            // 数据权限校验
            generalPermissionHelper.checkPermission(sessionContext, appId, AdminDataPermissionType.UAM_APP);

            if (!ALLOW_ADD_APP_STATUS_LIST.contains(cbbUamAppDTO.getAppStatus())) {
                LOGGER.error("未发布的应用[{}]，不允许添加到交付组[{}]", JSON.toJSONString(cbbUamAppDTO), deliveryGroupName);
                throw new BusinessException(UamDeliveryAppBusinessKey.RCDC_PUBLISH_DELIVERY_APP_NOT_ALLOWED_ADD, uamAppName, deliveryGroupName);
            }

            if (pushInstallPackageDTOList.size() < 1) {
                LOGGER.error("应用[{}]对应推送包已不存在",JSON.toJSONString(cbbUamAppDTO));
                throw new BusinessException("RCDC_PUBLISH_DELIVERY_APP_PUSH_PACKAGE_NOT_EXIST",uamAppName);
            }
            CbbPushInstallPackageDetailDTO pushInstallPackageDetailDTO = pushInstallPackageDTOList.get(0);
            OsPlatform osPlatform = cbbUamDeliveryGroupDetail.getOsPlatform();
            if (pushInstallPackageDetailDTO.getOsPlatform() != osPlatform) {
                LOGGER.error("应用[{}]与交付组[{}]操作系统类型不匹配，不允许添加到交付组", JSON.toJSONString(cbbUamAppDTO), deliveryGroupName);
                throw new BusinessException(UamDeliveryAppBusinessKey.RCDC_PUBLISH_DELIVERY_APP_NOT_MATCH_DELIVERY_GROUP_OS_TYPE,
                        uamAppName, deliveryGroupName);
            }

            // 已添加则忽略再次添加，避免重复添加问题
            Boolean existAdd = cbbAppDeliveryMgmtAPI.existsDeliveryApp(deliveryGroupId, appId);
            if (Boolean.FALSE.equals(existAdd)) {
                CbbAddDeliveryAppDTO cbbAddDeliveryAppDTO = new CbbAddDeliveryAppDTO();
                cbbAddDeliveryAppDTO.setDeliveryGroupId(deliveryGroupId);
                cbbAddDeliveryAppDTO.setAppId(appId);
                cbbAddDeliveryAppDTO.setAppType(cbbUamAppDTO.getAppType());
                cbbAddDeliveryAppDTO.setDeliveryStatus(DeliveryStatusEnum.DELIVERING);
                cbbAddDeliveryAppDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());
                cbbAppDeliveryMgmtAPI.addUamDeliveryApp(cbbAddDeliveryAppDTO);
            }

            LOGGER.info("交付组[{}]添加交付应用[{}]成功", deliveryGroupName, uamAppName);
            auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_ADD_SUCCESS_LOG, deliveryGroupName, uamAppName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_ADD_SUCCESS_LOG).msgArgs(deliveryGroupName, uamAppName).build();
        } catch (BusinessException e) {
            LOGGER.error("交付组[{}]添加交付应用[{}]失败，失败原因：", deliveryGroupName, appId, e);

            if (CbbAppCenterBusinessKey.RCDC_UAM_APP_NOT_EXIST.equals(e.getKey())) {
                auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_NOT_EXISTS_ADD_FAIL_LOG, deliveryGroupName, e.getI18nMessage());
                throw new BusinessException(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_NOT_EXISTS_ADD_FAIL_LOG, e, deliveryGroupName,
                        e.getI18nMessage());
            }

            auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_ADD_FAIL_LOG, deliveryGroupName, uamAppName, e.getI18nMessage());
            throw new BusinessException(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_ADD_FAIL_LOG, e, deliveryGroupName, uamAppName,
                    e.getI18nMessage());
        }

    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 添加应用返回结果
        if (batchFlag) {
            return buildDefaultFinishResult(successCount, failCount, UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_ADD_SUCCESS_RESULT);
        }

        // 添加单个应用
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_ADD_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {appName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_ADD_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[] {appName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }


}
