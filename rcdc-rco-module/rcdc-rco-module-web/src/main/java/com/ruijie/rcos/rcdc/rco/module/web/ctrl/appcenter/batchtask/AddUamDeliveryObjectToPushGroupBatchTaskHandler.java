package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbAddDeliveryObjectDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AsyncNoticeTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskImageRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryGroupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.util.Collections;
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
public class AddUamDeliveryObjectToPushGroupBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddUamDeliveryObjectToPushGroupBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private String cloudDesktopName;

    private UUID deliveryGroupId;

    private boolean batchFlag = true;

    public AddUamDeliveryObjectToPushGroupBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
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

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID cloudDesktopId = batchTaskItem.getItemID();
        // 判定交付组是否存在
        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);

        String deliveryGroupName = cbbUamDeliveryGroupDetail.getDeliveryGroupName();
        String desktopName = null;
        try {

            List<CloudDesktopDTO> desktopDTOList = userDesktopMgmtAPI.listDesktopByDesktopIds(Collections.singletonList(cloudDesktopId));
            // 判定云桌面是否存在，不存在则抛异常
            if (CollectionUtils.isEmpty(desktopDTOList)) {
                throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, cloudDesktopId.toString());
            }

            CloudDesktopDTO cloudDesktopDTO = desktopDTOList.get(0);
            desktopName = cloudDesktopDTO.getDesktopName();
            // 判定是否为个性（第三方桌面无云桌面类型，排除第三方）
            if ((CbbCloudDeskPattern.RECOVERABLE.name().equals(cloudDesktopDTO.getDesktopType()) ||
                    CbbCloudDeskPattern.APP_LAYER.name().equals(cloudDesktopDTO.getDesktopType())) &&
                    !CbbCloudDeskType.THIRD.name().equals(cloudDesktopDTO.getStrategyType())) {
                LOGGER.error("云桌面[{}]为非个性桌面，不允许添加到交付组[{}]", JSON.toJSONString(desktopName), deliveryGroupId);
                throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_PERSONAL, desktopName, deliveryGroupName);
            }

            OsPlatform osPlatform = cbbUamDeliveryGroupDetail.getOsPlatform();
            int lastIndexOf = cloudDesktopDTO.getOsType().lastIndexOf("_");
            String osType = cloudDesktopDTO.getOsType().substring(0, lastIndexOf);
            String osBit = cloudDesktopDTO.getOsType().substring(lastIndexOf + 1);
            if (osPlatform == OsPlatform.WINDOWS && !CbbOsType.WINDOWS_OS_TYPE_LIST.contains(CbbOsType.getOsType(osType, Integer.parseInt(osBit)))) {
                LOGGER.error("云桌面[{}]为非windows桌面，不允许添加到windows交付组[{}]", desktopName, deliveryGroupName);
                throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_WINDOWS, desktopName, deliveryGroupName);
            }
            if (osPlatform == OsPlatform.LINUX &&
                    !CbbOsType.APP_PUSH_PACKAGE_LINUX_OS_TYPE_LIST.contains(CbbOsType.getOsType(osType, Integer.parseInt(osBit)))) {
                LOGGER.error("云桌面[{}]为非linux桌面，不允许添加到linux交付组[{}]", desktopName, deliveryGroupName);
                throw new BusinessException("RCDC_UAM_DELIVERY_OBJECT_NOT_LINUX", desktopName, deliveryGroupName);
            }

            // 判定云桌面是否已经被使用
            checkDesktopHasExisted(cloudDesktopId, desktopName, cbbUamDeliveryGroupDetail);

            CbbAddDeliveryObjectDTO cbbAddDeliveryObjectDTO = new CbbAddDeliveryObjectDTO();
            cbbAddDeliveryObjectDTO.setDeliveryGroupId(cbbUamDeliveryGroupDetail.getId());
            cbbAddDeliveryObjectDTO.setCloudDesktopId(cloudDesktopId);
            cbbAddDeliveryObjectDTO.setDeliveryStatus(DeliveryStatusEnum.DELIVERING);
            cbbAddDeliveryObjectDTO.setAsyncNoticeType(AsyncNoticeTypeEnum.ADD_DELIVERY_OBJECT);
            cbbAddDeliveryObjectDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());

            cbbAppDeliveryMgmtAPI.addUamDeliveryObject(cbbAddDeliveryObjectDTO);
            LOGGER.info("交付组[{}]添加交付对象[{}]成功", deliveryGroupName, desktopName);

            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_SUCCESS_LOG, deliveryGroupName, desktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_SUCCESS_LOG).msgArgs(deliveryGroupName, desktopName).build();
        } catch (BusinessException e) {
            LOGGER.error("添加交付对象[{}]失败，失败原因：", desktopName, e);

            if (BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID.equals(e.getKey())) {
                auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_EXISTS_ADD_FAIL_LOG, e, deliveryGroupName,
                        e.getI18nMessage());
                throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_EXISTS_ADD_FAIL_LOG, e, deliveryGroupName,
                        e.getI18nMessage());
            }

            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_FAIL_LOG, e, deliveryGroupName, desktopName,
                    e.getI18nMessage());
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_FAIL_LOG, e, deliveryGroupName, desktopName,
                    e.getI18nMessage());
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
        Boolean existUsed =
                cbbAppDeliveryMgmtAPI.existsUsedDeliveryObject(cbbUamDeliveryGroupDetail.getId(), Collections.singletonList(cloudDesktopId));
        if (Boolean.TRUE.equals(existUsed)) {
            LOGGER.error("已经被使用的云桌面[{}]，不允许再添加到该交付组！云桌面id：{}", cloudDesktopId);
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_CLOUD_DESKTOP_EXISTS_USED, desktopName,
                    cbbUamDeliveryGroupDetail.getDeliveryGroupName());
        }
    }

}
