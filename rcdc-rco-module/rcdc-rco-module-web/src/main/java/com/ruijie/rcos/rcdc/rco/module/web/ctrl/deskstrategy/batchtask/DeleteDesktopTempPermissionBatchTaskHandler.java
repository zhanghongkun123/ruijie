package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.DesktopTempPermissionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 删除临时权限批处理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/8
 *
 * @author linke
 */
public class DeleteDesktopTempPermissionBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDesktopTempPermissionBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbDesktopTempPermissionAPI cbbDesktopTempPermissionAPI;

    private DesktopTempPermissionAPI desktopTempPermissionAPI;

    private String singleName;

    public DeleteDesktopTempPermissionBatchTaskHandler(List<DefaultBatchTaskItem> itemList, String singleName) {
        this(itemList);
        this.singleName = singleName;
    }

    public DeleteDesktopTempPermissionBatchTaskHandler(List<DefaultBatchTaskItem> itemList) {
        super(itemList);
        auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        cbbDesktopTempPermissionAPI = SpringBeanHelper.getBean(CbbDesktopTempPermissionAPI.class);
        desktopTempPermissionAPI = SpringBeanHelper.getBean(DesktopTempPermissionAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem is not null");
        Assert.notNull(batchTaskItem.getItemID(), "itemId is not null");
        UUID id = batchTaskItem.getItemID();

        CbbDesktopTempPermissionDTO permissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(id);
        try {
            // 删除绑定关系记录
            desktopTempPermissionAPI.deleteById(id, true);

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_DELETE_ITEM_SUC_DESC)
                    .msgArgs(new String[] {permissionDTO.getName()}).build();
        } catch (Exception e) {
            LOGGER.error("删除云桌面临时权限[{}]失败", permissionDTO.getName(), e);
            String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_DELETE_ERROR, permissionDTO.getName(), msg);
            throw new BusinessException(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_DELETE_ITEM_FAIL_DESC, e, permissionDTO.getName()
                    , msg);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (StringUtils.isNotBlank(singleName)) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_DELETE_ITEM_SUC_DESC)
                        .msgArgs(new String[] {singleName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_DELETE_SINGLE_RESULT_FAIL)
                    .msgArgs(new String[] {singleName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
        return buildDefaultFinishResult(sucCount, failCount, DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_DELETE_BATCH_RESULT);
    }


}
