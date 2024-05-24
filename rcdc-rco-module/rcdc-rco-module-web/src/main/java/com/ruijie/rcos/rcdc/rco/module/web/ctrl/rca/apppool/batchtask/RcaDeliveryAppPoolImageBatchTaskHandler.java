package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.DeliveryHostImageBatchTaskService;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.validation.AppPoolImageTemplateValidation;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Description: 镜像版本交付给应用池
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月27日
 *
 * @author liuwc
 */
public class RcaDeliveryAppPoolImageBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaUpdateAppPoolNetworkBatchTaskHandler.class);

    private DeliveryHostImageBatchTaskService startChangeHostImageBatchTaskService;

    private AppPoolImageTemplateValidation appPoolImageTemplateValidation;

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaHostAPI rcaHostAPI;

    private BatchTaskBuilderFactory batchTaskBuilderFactory;

    private UUID imageTemplateId;

    private String imageVersionName;

    private String identityId;

    public RcaDeliveryAppPoolImageBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");
        String poolName = "";
        try {
            UUID poolId = taskItem.getItemID();
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(poolId);
            poolName = appPoolBaseDTO.getName();
            appPoolImageTemplateValidation.validate(appPoolBaseDTO, imageTemplateId);
            // 变更池的镜像并刷新应用
            rcaAppPoolAPI.updateImageTemplateId(appPoolBaseDTO.getId(), imageTemplateId);
            List<RcaHostDTO> hostDTOList = rcaHostAPI.findAllByPoolIdIn(Lists.newArrayList(poolId));
            if (CollectionUtils.isEmpty(hostDTOList)) {
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(RcaBusinessKey.RCDC_RCO_DELIVERY_IMAGE_VERSION_TO_POOL_ITEM_SUCCESS)
                        .msgArgs(imageVersionName, appPoolBaseDTO.getName()).build();
            }

            // 更新应用池状态为编辑中
            rcaAppPoolAPI.updateAppPoolState(appPoolBaseDTO.getId(), RcaEnum.PoolState.UPDATING);
            // 创建交付主机的批任务
            // 创建DefaultBatchTaskBuilder
            InternalBatchTaskBuilder builder = (InternalBatchTaskBuilder) batchTaskBuilderFactory.create();
            builder.setIdentityId(identityId);
            DefaultWebResponse defaultWebResponse
                    = startChangeHostImageBatchTaskService.startChangeHostImage(builder, appPoolBaseDTO, hostDTOList, imageTemplateId);

            if (defaultWebResponse.getStatus() == WebResponse.Status.SUCCESS) {

                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_DELIVERY_IMAGE_VERSION_TO_POOL_ITEM_SUCCESS,
                        imageVersionName, poolName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(RcaBusinessKey.RCDC_RCO_DELIVERY_IMAGE_VERSION_TO_POOL_ITEM_SUCCESS)
                        .msgArgs(imageVersionName, poolName).build();
            }

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_DELIVERY_IMAGE_VERSION_TO_POOL_ITEM_FAIL,
                    imageVersionName, poolName, defaultWebResponse.getMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCO_DELIVERY_IMAGE_VERSION_TO_POOL_ITEM_FAIL)
                    .msgArgs(imageVersionName, poolName, defaultWebResponse.getMessage()).build();

        } catch (BusinessException e) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_DELIVERY_IMAGE_VERSION_TO_POOL_ITEM_FAIL,
                    imageVersionName, poolName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCO_DELIVERY_IMAGE_VERSION_TO_POOL_ITEM_FAIL)
                    .msgArgs(imageVersionName, poolName, e.getI18nMessage()).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCO_DELIVERY_IMAGE_VERSION_TO_POOL_RESULT);
    }

    public DeliveryHostImageBatchTaskService getStartChangeHostImageBatchTaskService() {
        return startChangeHostImageBatchTaskService;
    }

    public void setStartChangeHostImageBatchTaskService(DeliveryHostImageBatchTaskService startChangeHostImageBatchTaskService) {
        this.startChangeHostImageBatchTaskService = startChangeHostImageBatchTaskService;
    }

    public AppPoolImageTemplateValidation getAppPoolImageTemplateValidation() {
        return appPoolImageTemplateValidation;
    }

    public void setAppPoolImageTemplateValidation(AppPoolImageTemplateValidation appPoolImageTemplateValidation) {
        this.appPoolImageTemplateValidation = appPoolImageTemplateValidation;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public RcaAppPoolAPI getRcaAppPoolAPI() {
        return rcaAppPoolAPI;
    }

    public void setRcaAppPoolAPI(RcaAppPoolAPI rcaAppPoolAPI) {
        this.rcaAppPoolAPI = rcaAppPoolAPI;
    }

    public RcaHostAPI getRcaHostAPI() {
        return rcaHostAPI;
    }

    public void setRcaHostAPI(RcaHostAPI rcaHostAPI) {
        this.rcaHostAPI = rcaHostAPI;
    }

    public BatchTaskBuilderFactory getBatchTaskBuilderFactory() {
        return batchTaskBuilderFactory;
    }

    public void setBatchTaskBuilderFactory(BatchTaskBuilderFactory batchTaskBuilderFactory) {
        this.batchTaskBuilderFactory = batchTaskBuilderFactory;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getImageVersionName() {
        return imageVersionName;
    }

    public void setImageVersionName(String imageVersionName) {
        this.imageVersionName = imageVersionName;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }
}
