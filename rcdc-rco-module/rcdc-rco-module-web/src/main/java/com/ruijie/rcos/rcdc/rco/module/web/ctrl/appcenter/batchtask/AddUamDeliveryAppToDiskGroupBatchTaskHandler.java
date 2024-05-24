package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppStoreMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbAddDeliveryAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryAppBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29 11:53
 *
 * @author coderLee23
 */
public class AddUamDeliveryAppToDiskGroupBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddUamDeliveryAppToDiskGroupBatchTaskHandler.class);

    private static final List<AppStatusEnum> ALLOW_ADD_APP_STATUS_LIST = Arrays.asList(AppStatusEnum.PUBLISHED, AppStatusEnum.PRE_PUBLISH);

    private static final String TCI = "TCI";


    private BaseAuditLogAPI auditLogAPI;

    private CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI;

    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    private GeneralPermissionHelper generalPermissionHelper;

    private SessionContext sessionContext;

    private String appName;

    private UUID deliveryGroupId;

    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;



    public AddUamDeliveryAppToDiskGroupBatchTaskHandler(BatchTaskItem batchTaskItem) {
        super(batchTaskItem);
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

    public void setCbbAppSoftwarePackageMgmtAPI(CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI) {
        this.cbbAppSoftwarePackageMgmtAPI = cbbAppSoftwarePackageMgmtAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID appId = batchTaskItem.getItemID();
        // 判定交付组是否存在,需要每次获取，添加时候有可能更新
        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
        String deliveryGroupName = cbbUamDeliveryGroupDetail.getDeliveryGroupName();
        String uamAppName = null;
        try {
            // 数据权限校验
            CbbUamAppDTO cbbUamAppDTO = cbbAppStoreMgmtAPI.getUamApp(appId);
            uamAppName = cbbUamAppDTO.getAppName();
            generalPermissionHelper.checkPermission(sessionContext, appId, AdminDataPermissionType.UAM_APP);

            if (!ALLOW_ADD_APP_STATUS_LIST.contains(cbbUamAppDTO.getAppStatus())) {
                LOGGER.error("未发布的应用[{}]，不允许添加到交付组[{}]", JSON.toJSONString(cbbUamAppDTO), deliveryGroupName);
                throw new BusinessException(UamDeliveryAppBusinessKey.RCDC_PUBLISH_DELIVERY_APP_NOT_ALLOWED_ADD, uamAppName, deliveryGroupName);
            }

            checkSoftPackageIsAllowAdd(deliveryGroupName, cbbUamAppDTO, cbbUamDeliveryGroupDetail);

            CbbAddDeliveryAppDTO cbbAddDeliveryAppDTO = new CbbAddDeliveryAppDTO();
            cbbAddDeliveryAppDTO.setDeliveryGroupId(deliveryGroupId);
            cbbAddDeliveryAppDTO.setAppId(appId);
            cbbAddDeliveryAppDTO.setAppType(cbbUamAppDTO.getAppType());
            cbbAddDeliveryAppDTO.setDeliveryStatus(DeliveryStatusEnum.DELIVERING);
            cbbAddDeliveryAppDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());
            cbbAppDeliveryMgmtAPI.addUamDeliveryApp(cbbAddDeliveryAppDTO);

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

    private void checkSoftPackageIsAllowAdd(String deliveryGroupName, CbbUamAppDTO cbbUamAppDTO,
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetailDTO) throws BusinessException {

        UUID appId = cbbUamAppDTO.getId();
        String uamAppName = cbbUamAppDTO.getAppName();
        // 应用软件包只允许添加一个
        Boolean exist = cbbAppDeliveryMgmtAPI.existsDeliveryApp(deliveryGroupId, AppTypeEnum.APP_SOFTWARE_PACKAGE);
        if (Boolean.TRUE.equals(exist)) {
            LOGGER.error("应用软件包只允许添加一个,新增应用：{} ", JSON.toJSONString(cbbUamAppDTO));
            throw new BusinessException(UamDeliveryAppBusinessKey.RCDC_UAM_APP_SOFTWARE_PACKAGE_ALLOW_ADD_ONE);
        }

        // 获取应用软件包
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(appId);
        CbbOsType osType = appSoftwarePackageDTO.getOsType();
        CbbImageType cbbImageType = appSoftwarePackageDTO.getAppSoftwarePackageType();
        String cbbImageTypeName = cbbImageType == CbbImageType.VOI ? TCI : cbbImageType.name();

        // 当前交付组存在 虚机类型和云桌面类型
        CbbOsType detailCbbOsType = cbbUamDeliveryGroupDetailDTO.getOsType();
        CbbImageType detailCbbImageType = cbbUamDeliveryGroupDetailDTO.getCbbImageType();
        // 交付组已有规格限制，则不允许添加不同规格的app，避免与已添加的云桌面规格不匹配
        String detailCbbImageTypeName = detailCbbImageType == CbbImageType.VOI ? TCI : detailCbbImageType.name();

        if (!Objects.equals(osType, detailCbbOsType) || !Objects.equals(cbbImageType, detailCbbImageType)) {
            LOGGER.error("应用[{}]的虚机类型或者云桌面类型不匹配，不允许添加", JSON.toJSONString(cbbUamAppDTO));
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_APP_MISMATCH, uamAppName, osType.getOsName(), cbbImageTypeName,
                    deliveryGroupName, detailCbbOsType.name(), detailCbbImageTypeName);
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
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
