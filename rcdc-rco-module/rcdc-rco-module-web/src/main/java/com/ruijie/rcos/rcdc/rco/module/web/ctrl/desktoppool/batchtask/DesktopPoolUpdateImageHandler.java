package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
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

/**
 * Description: 桌面池编辑镜像批量处理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-06
 *
 * @author linke
 */
public class DesktopPoolUpdateImageHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolUpdateImageHandler.class);

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private UUID imageId;

    private String poolName;

    private CbbDesktopPoolDTO cbbDesktopPoolDTO;

    // 只用单个任务时日志记录使用
    private String desktopName;

    /** 处理的记录数累计值 */
    protected AtomicInteger processItemCount = new AtomicInteger(0);

    public DesktopPoolUpdateImageHandler(Iterator<? extends BatchTaskItem> iterator, UUID imageId, CbbDesktopPoolDTO cbbDesktopPoolDTO) {
        super(iterator);
        this.imageId = imageId;
        this.cbbDesktopPoolDTO = cbbDesktopPoolDTO;
        this.poolName = cbbDesktopPoolDTO.getName();
        this.cbbDeskMgmtAPI = SpringBeanHelper.getBean(CbbDeskMgmtAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.userDesktopMgmtAPI = SpringBeanHelper.getBean(UserDesktopMgmtAPI.class);
        this.cbbImageTemplateMgmtAPI = SpringBeanHelper.getBean(CbbImageTemplateMgmtAPI.class);
        this.cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "Param [batchTaskItem] must not be null");

        UUID desktopId = taskItem.getItemID();
        Assert.notNull(desktopId, "Param [desktopId] must not be null");
        String tempDesktopName = desktopId.toString();
        String  tempImageName = "";
        try {
            CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(taskItem.getItemID());
            tempDesktopName = cbbDeskDTO.getName();
            if (!cbbImageTemplateMgmtAPI.checkImageTemplateExist(imageId)) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_IMAGE_NOT_EXIST, poolName, tempDesktopName);
            }
            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
            tempImageName = imageTemplateDetail.getImageName();

            LOGGER.info("准备变更云桌面[id:{}, name:{}]镜像", desktopId, tempDesktopName);
            CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO = new CbbDesktopImageUpdateDTO();
            cbbDesktopImageUpdateDTO.setDesktopId(desktopId);
            cbbDesktopImageUpdateDTO.setImageId(imageId);
            cbbDesktopImageUpdateDTO.setBatchTaskItem(taskItem);
            userDesktopMgmtAPI.updateDesktopImage(cbbDesktopImageUpdateDTO);

            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_TASK_LOG_SUC, poolName, tempDesktopName, tempImageName);
            return DefaultBatchTaskItemResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_TASK_RESULT_SUC)
                    .msgArgs(poolName, tempDesktopName).batchTaskItemStatus(BatchTaskItemStatus.SUCCESS).build();
        } catch (BusinessException e) {
            LOGGER.error("变更桌面池[%s]云桌面[%s]镜像异常", poolName, desktopId, e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_TASK_LOG_FAIL, poolName,
                    tempDesktopName, tempImageName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_TASK_ITEM_FAIL)
                    .msgArgs(poolName, tempDesktopName, e.getI18nMessage()).batchTaskItemStatus(BatchTaskItemStatus.FAILURE).build();
        } finally {
            processItemCount.incrementAndGet();
            desktopName = tempDesktopName;
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        try {
            cbbDesktopPoolMgmtAPI.updateState(cbbDesktopPoolDTO.getId(), CbbDesktopPoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error("更新桌面池[{}]信息失败", cbbDesktopPoolDTO.getId(), e);
        }
        if (processItemCount.get() == 1) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_TASK_RESULT_SUC)
                        .msgArgs(new String[]{poolName, desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_TASK_RESULT_FAIL)
                    .msgArgs(new String[]{poolName, desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
        return buildDefaultFinishResult(successCount, failCount, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_BATCH_RESULT);
    }
}
