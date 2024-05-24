package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbAddDeliveryObjectDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AsyncNoticeTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskImageRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryGroupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.util.*;


/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29 11:53
 *
 * @author coderLee23
 */
public class AddUamDeliveryObjectToDiskGroupBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddUamDeliveryObjectToDiskGroupBatchTaskHandler.class);

    private static final String TCI = "TCI";

    private BaseAuditLogAPI auditLogAPI;

    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    private CloudDesktopWebService cloudDesktopWebService;

    private String cloudDesktopName;

    private UUID deliveryGroupId;

    private boolean batchFlag = true;

    public AddUamDeliveryObjectToDiskGroupBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbAppDeliveryMgmtAPI(CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI) {
        this.cbbAppDeliveryMgmtAPI = cbbAppDeliveryMgmtAPI;
    }

    public void setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public void setBatchFlag(boolean batchFlag) {
        this.batchFlag = batchFlag;
    }

    public void setCloudDesktopName(String cloudDesktopName) {
        this.cloudDesktopName = cloudDesktopName;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public void setAppDeliveryMgmtAPI(AppDeliveryMgmtAPI appDeliveryMgmtAPI) {
        this.appDeliveryMgmtAPI = appDeliveryMgmtAPI;
    }

    public void setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        this.cloudDesktopWebService = cloudDesktopWebService;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID cloudDesktopId = batchTaskItem.getItemID();
        // 判定交付组是否存在
        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);

        String deliveryGroupName = cbbUamDeliveryGroupDetail.getDeliveryGroupName();
        String desktopName = null;
        try {
            // 判断是否为第三方桌面
            cloudDesktopWebService.checkThirdPartyDesktop(cloudDesktopId,
                    UamDeliveryObjectBusinessKey.RCDC_UAM_CREATE_THIRD_PARTY_DESKTOP_DELIVERY_GROUP_FAIL);
            List<DeskImageRelatedDTO> deskImageRelatedDTOList = userDesktopMgmtAPI.findByDeskIdIn(Collections.singletonList(cloudDesktopId));
            // 判定云桌面是否存在，不存在则抛异常
            if (CollectionUtils.isEmpty(deskImageRelatedDTOList)) {
                throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, cloudDesktopId.toString());
            }

            DeskImageRelatedDTO cloudDesktopDetailDTO = deskImageRelatedDTOList.get(0);
            desktopName = cloudDesktopDetailDTO.getDesktopName();
            // 判定是否为个性
            if (!CbbCloudDeskPattern.PERSONAL.name().equals(cloudDesktopDetailDTO.getDesktopType())) {
                LOGGER.error("云桌面[{}]为非个性桌面，不允许添加到交付组[{}]", JSON.toJSONString(cloudDesktopDetailDTO), deliveryGroupId);
                throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_PERSONAL, desktopName, deliveryGroupName);
            }

            if (!CbbOsType.APP_SOFTWARE_PACKAGE_WINDOWS_OS_TYPE_LIST.contains(cloudDesktopDetailDTO.getDesktopImageType())) {
                LOGGER.error("云桌面[{}]为非windows7或windows10的桌面，不允许添加到交付组[{}]", JSON.toJSONString(cloudDesktopDetailDTO));
                throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_WINDOWS_7_OR_10, desktopName,
                        deliveryGroupName);
            }

            // 判定云桌面是否已经被使用
            checkDesktopHasExisted(cloudDesktopId, desktopName, cbbUamDeliveryGroupDetail);
            // 检测类型是否匹配
            checkTypeMatch(cbbUamDeliveryGroupDetail, desktopName, cloudDesktopDetailDTO);
            // 添加到桌面
            addUamDeliveryObject(cloudDesktopId, cbbUamDeliveryGroupDetail);

            LOGGER.info("交付组[{}]添加交付对象[{}]成功", deliveryGroupName, desktopName);

            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_SUCCESS_LOG, deliveryGroupName, desktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_SUCCESS_LOG).msgArgs(deliveryGroupName, desktopName).build();
        } catch (BusinessException e) {
            LOGGER.error("添加交付对象[{}]失败，失败原因：", desktopName, e);

            if (BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID.equals(e.getKey())) {
                auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_EXISTS_ADD_FAIL_LOG, deliveryGroupName,
                        e.getI18nMessage());
                throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_EXISTS_ADD_FAIL_LOG, e, deliveryGroupName,
                        e.getI18nMessage());
            }

            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_FAIL_LOG, deliveryGroupName, desktopName,
                    e.getI18nMessage());
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_FAIL_LOG, e, deliveryGroupName, desktopName,
                    e.getI18nMessage());
        }

    }


    private void addUamDeliveryObject(UUID cloudDesktopId, CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail) throws BusinessException {
        CbbAddDeliveryObjectDTO cbbAddDeliveryObjectDTO = new CbbAddDeliveryObjectDTO();
        cbbAddDeliveryObjectDTO.setDeliveryGroupId(cbbUamDeliveryGroupDetail.getId());
        cbbAddDeliveryObjectDTO.setCloudDesktopId(cloudDesktopId);
        cbbAddDeliveryObjectDTO.setDeliveryStatus(DeliveryStatusEnum.DELIVERING);
        cbbAddDeliveryObjectDTO.setAsyncNoticeType(AsyncNoticeTypeEnum.ADD_DELIVERY_OBJECT);
        cbbAddDeliveryObjectDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());
        cbbAppDeliveryMgmtAPI.addUamDeliveryObject(cbbAddDeliveryObjectDTO);
    }

    private static void checkTypeMatch(CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail, String desktopName,
            DeskImageRelatedDTO cloudDesktopDetailDTO) throws BusinessException {
        UUID imageTemplateId = cbbUamDeliveryGroupDetail.getImageTemplateId();
        UUID desktopImageTemplateId = cloudDesktopDetailDTO.getImageTemplateId();
        String deliveryGroupName = cbbUamDeliveryGroupDetail.getDeliveryGroupName();
        if (!Objects.equals(imageTemplateId, desktopImageTemplateId)) {
            LOGGER.error("云桌面[{}]镜像模板[{}]与交付组[{}]镜像模板[{}]不匹配", desktopName, desktopImageTemplateId, deliveryGroupName, imageTemplateId);
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_IMAGE_TEMPLATE_MISMATCH, desktopName, deliveryGroupName);
        }

        String osVersion = cbbUamDeliveryGroupDetail.getOsVersion();
        String desktopOsVersion = cloudDesktopDetailDTO.getOsVersion();
        if (!Objects.equals(osVersion, desktopOsVersion)) {
            LOGGER.error("云桌面[{}]操作系统版本号[{}]与交付组[{}]操作系统版本号[{}]不匹配", desktopName, desktopOsVersion, deliveryGroupName, osVersion);
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OS_VERSION_MISMATCH, desktopName, desktopOsVersion,
                    deliveryGroupName, osVersion);
        }

        CbbOsType cbbOsType = cloudDesktopDetailDTO.getDesktopImageType();
        String cbbImageType = cloudDesktopDetailDTO.getCbbImageType();
        // 虚机类型
        CbbOsType detailCbbOsType = cbbUamDeliveryGroupDetail.getOsType();
        // 云桌面类型
        CbbImageType detailCbbImageType = cbbUamDeliveryGroupDetail.getCbbImageType();

        // osType 和 DesktopImageType 不存在不需要限制添加桌面类型
        if (!Objects.equals(cbbOsType, detailCbbOsType) || !Objects.equals(cbbImageType, detailCbbImageType.name())) {
            String cbbImageTypeName = cbbImageType.equals(CbbImageType.VOI.name()) ? TCI : cbbImageType;
            String detailCbbImageTypeName = detailCbbImageType == CbbImageType.VOI ? TCI : detailCbbImageType.name();

            LOGGER.error("云桌面[{}]的虚机类型或者云桌面类型不匹配，不允许添加到交付组[{}]", JSON.toJSONString(cloudDesktopDetailDTO));
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_MISMATCH, desktopName, cbbOsType.getOsName(),
                    cbbImageTypeName, cbbUamDeliveryGroupDetail.getDeliveryGroupName(), detailCbbOsType.getOsName(), detailCbbImageTypeName);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量添加返回结果
        if (batchFlag) {
            return buildDefaultFinishResult(successCount, failCount, UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_SUCCESS_RESULT);
        }

        // 添加单个应用
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {cloudDesktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[] {cloudDesktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    /**
     * 验证是否被交付组添加过，推送安装包支持桌面被不同的交付组使用
     *
     * @param cloudDesktopId 桌面id
     * @param desktopName 桌面名称
     * @param cbbUamDeliveryGroupDetail 交付组DTO
     * @return
     */
    private void checkDesktopHasExisted(UUID cloudDesktopId, String desktopName, CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail)
            throws BusinessException {
        Boolean existUsed = appDeliveryMgmtAPI.existsByAppDeliveryTypeAndCloudDesktopIdIn(AppDeliveryTypeEnum.APP_DISK,
                Collections.singletonList(cloudDesktopId));
        if (Boolean.TRUE.equals(existUsed)) {
            LOGGER.error("已经被使用的云桌面[{}]，不允许再添加到该交付组！云桌面id：{}", cloudDesktopId);
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_CLOUD_DESKTOP_EXISTS_USED, desktopName,
                    cbbUamDeliveryGroupDetail.getDeliveryGroupName());
        }
    }

}
