package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopImageUpdateDTO;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAppAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 变更应用池镜像模板批任务处理器
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月04日
 *
 * @author zhengjingyong
 */
public class RcaUpdateAppPoolImageBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaUpdateAppPoolNetworkBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private UUID templateImageId;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private RcaAppPoolBaseDTO appPoolBaseDTO;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private RcaHostAppAPI rcaHostAppAPI;

    public RcaUpdateAppPoolImageBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(taskItem.getItemID());
        String tmpDesktopName = cbbDeskDTO.getName();

        try {
            if (!cbbImageTemplateMgmtAPI.checkImageTemplateExist(templateImageId)) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_NOT_EXIST,
                        appPoolBaseDTO.getName(), tmpDesktopName);
            }

            LOGGER.info("准备变更云桌面[id:{}, name:{}]镜像", taskItem.getItemID(), tmpDesktopName);
            CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO = new CbbDesktopImageUpdateDTO();
            cbbDesktopImageUpdateDTO.setDesktopId(taskItem.getItemID());
            cbbDesktopImageUpdateDTO.setImageId(templateImageId);
            cbbDesktopImageUpdateDTO.setBatchTaskItem(taskItem);
            userDesktopMgmtAPI.updateDesktopImage(cbbDesktopImageUpdateDTO);

            // 当镜像变更成功，需要从镜像模板的主机和池应用中更新应用
            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(taskItem.getItemID());
            if (cbbDeskDTO.getWillApplyImageId() == null) {
                rcaHostAppAPI.updateAppFromImageHostWithPoolApp(templateImageId, taskItem.getItemID(), appPoolBaseDTO.getId());
            }

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_ITEM_SUCCESS,
                    appPoolBaseDTO.getName(), tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_ITEM_SUCCESS)
                    .msgArgs(appPoolBaseDTO.getName(), tmpDesktopName).batchTaskItemStatus(BatchTaskItemStatus.SUCCESS).build();
        } catch (BusinessException e) {
            LOGGER.error(String.format("变更应用池[%s]云桌面[%s]镜像异常", appPoolBaseDTO.getName(), taskItem.getItemID()), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_ITEM_FAIL, appPoolBaseDTO.getName(),
                    tmpDesktopName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_ITEM_FAIL)
                    .msgArgs(appPoolBaseDTO.getName(), tmpDesktopName, e.getI18nMessage())
                    .batchTaskItemStatus(BatchTaskItemStatus.FAILURE).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        try {
            rcaAppPoolAPI.updateAppPoolState(appPoolBaseDTO.getId(), RcaEnum.PoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新应用池[%s]信息失败", appPoolBaseDTO.getId()), e);
        }

        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_SUCCESS, appPoolBaseDTO.getName());

        return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_RESULT);
    }


    public void setRcaAppPoolAPI(RcaAppPoolAPI rcaAppPoolAPI) {
        this.rcaAppPoolAPI = rcaAppPoolAPI;
    }

    public void setTemplateImageId(UUID templateImageId) {
        this.templateImageId = templateImageId;
    }

    public void setAppPoolBaseDTO(RcaAppPoolBaseDTO appPoolBaseDTO) {
        this.appPoolBaseDTO = appPoolBaseDTO;
    }


    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbDeskMgmtAPI(CbbDeskMgmtAPI cbbDeskMgmtAPI) {
        this.cbbDeskMgmtAPI = cbbDeskMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
    }

    public void setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public RcaHostAppAPI getRcaHostAppAPI() {
        return rcaHostAppAPI;
    }

    public void setRcaHostAppAPI(RcaHostAppAPI rcaHostAppAPI) {
        this.rcaHostAppAPI = rcaHostAppAPI;
    }
}
