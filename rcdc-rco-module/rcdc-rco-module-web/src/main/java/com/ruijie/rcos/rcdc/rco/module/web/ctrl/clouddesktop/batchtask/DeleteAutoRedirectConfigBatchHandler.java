package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBAdvancedSettingMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbAutoRedirectConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
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
 * 批量删除自动重定向配置handler
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月23日
 * 
 * @author zhuangchenwu
 */
public class DeleteAutoRedirectConfigBatchHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAutoRedirectConfigBatchHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbUSBAdvancedSettingMgmtAPI cbbUSBAdvancedSettingMgmtAPI;

    public DeleteAutoRedirectConfigBatchHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
            CbbUSBAdvancedSettingMgmtAPI cbbUSBAdvancedSettingMgmtAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.cbbUSBAdvancedSettingMgmtAPI = cbbUSBAdvancedSettingMgmtAPI;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount,
                CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_BATCH_DELETE_RESULT);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "the item must not be null");
        CbbAutoRedirectConfigDTO configDTO = getAutoRedirectConfig(item.getItemID());
        try {
            cbbUSBAdvancedSettingMgmtAPI.deleteAutoRedirectConfig(item.getItemID());

            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_DELETE_SUCCESS,
                    Objects.requireNonNull(configDTO).getFirmFlag(), configDTO.getProductFlag());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_DELETE_SUCCESS)
                    .msgArgs(new String[] {configDTO.getFirmFlag(), configDTO.getProductFlag()}).build();
        } catch (BusinessException e) {
            LOGGER.error("删除USB自动重定向配置信息出现异常，id为：" + item.getItemID(), e);
            return processDeleteFail(item, configDTO, e);
        }
    }

    private BatchTaskItemResult processDeleteFail(BatchTaskItem item, CbbAutoRedirectConfigDTO configDTO, BusinessException e)
            throws BusinessException {
        if (configDTO == null) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_NOT_EXIST_DELETE_FAIL, e,
                    item.getItemID().toString(), e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_NOT_EXIST_DELETE_FAIL, e,
                    item.getItemID().toString(), e.getI18nMessage());
        } else {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_DELETE_FAIL, e, configDTO.getFirmFlag(),
                    configDTO.getProductFlag(), e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_DELETE_FAIL, e, configDTO.getFirmFlag(),
                    configDTO.getProductFlag(), e.getI18nMessage());

        }
    }

    private CbbAutoRedirectConfigDTO getAutoRedirectConfig(UUID id) {
        CbbAutoRedirectConfigDTO configDTO = null;
        try {
            configDTO = cbbUSBAdvancedSettingMgmtAPI.getAutoRedirectConfig(id);
        } catch (Exception e) {
            LOGGER.error("查询USB自动重定向配置信息出现异常，id为：" + id, e);
        }
        return configDTO;
    }

}
