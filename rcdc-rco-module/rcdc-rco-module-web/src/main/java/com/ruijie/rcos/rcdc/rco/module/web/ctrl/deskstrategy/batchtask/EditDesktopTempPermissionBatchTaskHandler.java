package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask;

import java.util.List;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.DesktopTempPermissionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 编辑临时权限批处理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/15
 *
 * @author linke
 */
public class EditDesktopTempPermissionBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditDesktopTempPermissionBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private DesktopTempPermissionAPI desktopTempPermissionAPI;

    private DesktopTempPermissionUpdateDTO updateDTO;

    public EditDesktopTempPermissionBatchTaskHandler(List<DefaultBatchTaskItem> itemList, DesktopTempPermissionUpdateDTO updateDTO) {
        super(itemList);
        auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        desktopTempPermissionAPI = SpringBeanHelper.getBean(DesktopTempPermissionAPI.class);
        this.updateDTO = updateDTO;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem is not null");
        Assert.notNull(batchTaskItem.getItemID(), "itemId is not null");

        try {
            desktopTempPermissionAPI.updateDesktopTempPermission(updateDTO);

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_UPDATE_SUCCESS)
                    .msgArgs(new String[] {updateDTO.getName()}).build();
        } catch (Exception e) {
            LOGGER.error("编辑云桌面临时权限[{}]失败", updateDTO.getName(), e);
            String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_UPDATE_ERROR, updateDTO.getName(), msg);

            throw new BusinessException(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_UPDATE_ERROR, e, updateDTO.getName(), msg);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (sucCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_UPDATE_SUCCESS)
                    .msgArgs(new String[] {updateDTO.getName()}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        }
        return DefaultBatchTaskFinishResult.builder().msgKey(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_BATCH_UPDATE_TASK_FAIL)
                .msgArgs(new String[] {updateDTO.getName()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
    }

}
