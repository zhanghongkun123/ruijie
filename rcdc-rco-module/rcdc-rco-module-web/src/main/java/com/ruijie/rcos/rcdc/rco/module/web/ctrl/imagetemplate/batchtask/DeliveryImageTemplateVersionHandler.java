package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.EffectiveStrategy;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.DeliveryImageTemplateVersionWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.util.LockUtils;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年07月04日
 *
 * @author xgx
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DeliveryImageTemplateVersionHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryImageTemplateVersionHandler.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private UUID imageVersionId;

    private String imageVersionName;

    private EffectiveStrategy effectiveStrategy;

    private UUID rootImageId;

    private UUID[] deskPoolIdArr;


    public DeliveryImageTemplateVersionHandler(Collection<? extends BatchTaskItem> batchTaskItemCollection,
            DeliveryImageTemplateVersionWebRequest request, String imageVersionName, UUID rootImageId, UUID[] deskPoolIdArr) {
        super(batchTaskItemCollection);
        this.imageVersionName = imageVersionName;
        this.imageVersionId = request.getImageVersionId();
        this.effectiveStrategy = request.getEffectiveStrategy();
        this.deskPoolIdArr = deskPoolIdArr;
        this.rootImageId = rootImageId;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        String deskName = null;
        try {
            UUID deskId = batchTaskItem.getItemID();
            CbbDeskInfoDTO deskInfoDTO = cbbVDIDeskMgmtAPI.getByDeskId(deskId);
            deskName = deskInfoDTO.getName();

            if (deskInfoDTO.getDeskState() == CbbCloudDeskState.SLEEP && effectiveStrategy == EffectiveStrategy.RIGHT_NOW) {
                CloudDesktopStartRequest request = new CloudDesktopStartRequest(deskId, Boolean.FALSE);
                request.setSupportCrossCpuVendor(Boolean.TRUE);
                userDesktopOperateAPI.start(request);
            }

            CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO = new CbbDesktopImageUpdateDTO();
            cbbDesktopImageUpdateDTO.setDesktopId(batchTaskItem.getItemID());
            cbbDesktopImageUpdateDTO.setImageId(imageVersionId);
            cbbDesktopImageUpdateDTO.setEnableVdiForceApply(effectiveStrategy.getEnableForceApply());
            cbbDesktopImageUpdateDTO.setBatchTaskItem(batchTaskItem);

            userDesktopMgmtAPI.updateDesktopImage(cbbDesktopImageUpdateDTO);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_ITEM_SUCCESS, imageVersionName, deskName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_ITEM_SUCCESS).msgArgs(imageVersionName, deskName).build();
        } catch (BusinessException e) {
            deskName = Optional.ofNullable(deskName).orElse(String.valueOf(batchTaskItem.getItemID()));
            LOGGER.error("交付镜像模板版本[{}]给云桌面[{}]失败", imageVersionName, deskName, e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_ITEM_FAIL, imageVersionName, deskName,
                    e.getI18nMessage());
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_ITEM_FAIL, e, imageVersionName, deskName,
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        Assert.notNull(successCount, "successCount can not be null");
        Assert.notNull(failCount, "failCount can not be null");
        try {
            LockUtils.unLock(String.valueOf(rootImageId));
            if (!ObjectUtils.isEmpty(deskPoolIdArr)) {
                // 状态由更新中变成可用
                cbbDesktopPoolMgmtAPI.finishChangeDesktopPoolImageTemplateIdAndState(deskPoolIdArr);
            }
        } catch (Throwable e) {
            LOGGER.error("更新桌面池[{}]状态失败", JSON.toJSONString(deskPoolIdArr), e);
        }

        return buildDefaultFinishResult(successCount, failCount, BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_RESULT);
    }

}
