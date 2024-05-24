package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBAdvancedSettingMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbSpecialDeviceDriverConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * 批量删除特定设备驱动配置handler
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月23日
 * 
 * @author zhuangchenwu
 */
public class DeleteSpecialDeviceDriverConfigBatchHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSpecialDeviceDriverConfigBatchHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbUSBAdvancedSettingMgmtAPI cbbUSBAdvancedSettingMgmtAPI;

    public DeleteSpecialDeviceDriverConfigBatchHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
            CbbUSBAdvancedSettingMgmtAPI cbbUSBAdvancedSettingMgmtAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.cbbUSBAdvancedSettingMgmtAPI = cbbUSBAdvancedSettingMgmtAPI;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount,
                CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_BATCH_DELETE_RESULT);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "the item must not be null");
        CbbSpecialDeviceDriverConfigDTO configDTO = getSpecialDeviceDriverConfig(item.getItemID());
        try {
            cbbUSBAdvancedSettingMgmtAPI.deleteSpecialDeviceDriverConfig(item.getItemID());

            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_DELETE_SUCCESS,
                    Objects.requireNonNull(configDTO).getFirmFlag(), configDTO.getProductFlag());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_DELETE_SUCCESS)
                    .msgArgs(new String[] {configDTO.getFirmFlag(), configDTO.getProductFlag()}).build();
        } catch (BusinessException e) {
            LOGGER.error("删除USB特定设备驱动配置信息出现异常，id为：" + item.getItemID(), e);
            return processDeleteFail(item, configDTO, e);
        }
    }

    private BatchTaskItemResult processDeleteFail(BatchTaskItem item, CbbSpecialDeviceDriverConfigDTO configDTO, BusinessException e)
            throws BusinessException {
        if (configDTO == null) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_NOT_EXIST_DELETE_FAIL, e,
                    item.getItemID().toString(), e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_NOT_EXIST_DELETE_FAIL, e,
                    item.getItemID().toString(), e.getI18nMessage());
        } else {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_DELETE_FAIL, e, configDTO.getFirmFlag(),
                    configDTO.getProductFlag(), e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_DELETE_FAIL, e,
                    configDTO.getFirmFlag(), configDTO.getProductFlag(), e.getI18nMessage());
        }
    }

    private CbbSpecialDeviceDriverConfigDTO getSpecialDeviceDriverConfig(UUID id) {
        CbbSpecialDeviceDriverConfigDTO configDTO = null;
        try {
            configDTO = cbbUSBAdvancedSettingMgmtAPI.getSpecialDeviceDriverConfig(id);
        } catch (Exception e) {
            LOGGER.error("查询USB特定设备驱动配置信息出现异常，id为：" + id, e);
        }
        return configDTO;
    }

}
