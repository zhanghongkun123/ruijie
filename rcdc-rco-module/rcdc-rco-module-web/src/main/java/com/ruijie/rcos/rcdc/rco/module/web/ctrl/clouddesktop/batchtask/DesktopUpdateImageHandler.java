package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
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
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-04-06
 *
 * @author chen zj
 */
public class DesktopUpdateImageHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopUpdateImageHandler.class);

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CloudDesktopWebService cloudDesktopWebService;

    private ClusterAPI clusterAPI;

    private DeskSpecAPI deskSpecAPI;

    private UUID imageId;

    private UUID willApplyImageId;

    private String desktopName;


    public DesktopUpdateImageHandler(Iterator<? extends BatchTaskItem> iterator, UUID imageId, String desktopName) {
        super(iterator);
        this.imageId = imageId;
        this.desktopName = desktopName;
        this.clusterAPI = SpringBeanHelper.getBean(ClusterAPI.class);
        this.cloudDesktopWebService = SpringBeanHelper.getBean(CloudDesktopWebService.class);
        this.deskSpecAPI = SpringBeanHelper.getBean(DeskSpecAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "Param [batchTaskItem] must not be null");

        UUID  desktopId = taskItem.getItemID();
        Assert.notNull(desktopId, "Param [desktopId] must not be null");
        willApplyImageId = null;
        try {
            cloudDesktopWebService.checkThirdPartyDesktop(taskItem.getItemID(), UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_IMAGE_TEMPLATE_THIRD_PARTY);
            CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(taskItem.getItemID());

            checkBeforeUpdate(cbbDeskDTO);

            LOGGER.info("准备变更云桌面[id:{}, name:{}]镜像", desktopId, cbbDeskDTO.getName());
            CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO = new CbbDesktopImageUpdateDTO();
            cbbDesktopImageUpdateDTO.setDesktopId(desktopId);
            cbbDesktopImageUpdateDTO.setImageId(imageId);
            cbbDesktopImageUpdateDTO.setBatchTaskItem(taskItem);
            userDesktopMgmtAPI.updateDesktopImage(cbbDesktopImageUpdateDTO);

            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopId);
            String msgKey = CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_LOG_SUC;
            if (cbbDeskDTO.getWillApplyImageId() != null) {
                willApplyImageId = cbbDeskDTO.getWillApplyImageId();
                msgKey = CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_WAIT_LOG_SUC;
            }
            auditLogAPI.recordLog(msgKey, desktopName);
            return DefaultBatchTaskItemResult.builder().msgKey(msgKey)
                    .msgArgs(new String[] {desktopName}).batchTaskItemStatus(BatchTaskItemStatus.SUCCESS).build();
        } catch (BusinessException e) {
            LOGGER.error("变更云桌面[{" + desktopId + "}]镜像异常：", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_LOG_FAIL, e, desktopName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().msgKey(CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_LOG_FAIL)
                    .msgArgs(new String[] {desktopName, e.getI18nMessage()}).batchTaskItemStatus(BatchTaskItemStatus.FAILURE).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount == 1) {
            String msgKey = CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_RESULT_SUC;
            if (willApplyImageId != null) {
                msgKey = CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_WAIT_RESULT_SUC;
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(msgKey)
                    .msgArgs(new String[]{desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_RESULT_FAIL)
                    .msgArgs(new String[]{desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    private void checkBeforeUpdate(CbbDeskDTO cbbDeskDTO) throws BusinessException {
        if (Objects.equals(cbbDeskDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC)) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_EDIT_IMAGE_DYNAMIC_POOL_NOT_SUPPORT, cbbDeskDTO.getName());
        }

        boolean isImageTemplateExist = cbbImageTemplateMgmtAPI.checkImageTemplateExist(imageId);
        if (!isImageTemplateExist) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_EDIT_IMAGE_NOT_EXIST, cbbDeskDTO.getName());
        }

        // 校验VDI桌面所属的计算集群和新的镜像模板CPU架构是否一致
        if (CbbCloudDeskType.VDI == cbbDeskDTO.getDeskType()) {
            clusterAPI.validateVDIImageTemplateFramework(cbbDeskDTO.getClusterId(), imageId);
            // 检查新镜像模板是否支持显卡配置
            deskSpecAPI.checkGpuSupportByNewImage(cbbDeskDTO.getDeskId(), imageId, null);
        }
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public void setCbbDeskMgmtAPI(CbbDeskMgmtAPI cbbDeskMgmtAPI) {
        this.cbbDeskMgmtAPI = cbbDeskMgmtAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
    }

    public void setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        this.cloudDesktopWebService = cloudDesktopWebService;
    }
}
