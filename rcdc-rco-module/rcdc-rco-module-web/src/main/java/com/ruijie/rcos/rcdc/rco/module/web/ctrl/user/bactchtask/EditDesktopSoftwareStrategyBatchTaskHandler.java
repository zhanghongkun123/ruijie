package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.SoftwareControlBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * *编辑云桌面软件管控策略异步批处理类
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年09月06日
 * 
 * @author lihengjing
 */
public class EditDesktopSoftwareStrategyBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditDesktopSoftwareStrategyBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private String deskName;

    private UUID softwareStrategyId;

    private String softwareStrategyName;

    public EditDesktopSoftwareStrategyBatchTaskHandler(UserDesktopMgmtAPI cloudDesktopMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                                       BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
    }

    public EditDesktopSoftwareStrategyBatchTaskHandler(UserDesktopMgmtAPI cloudDesktopMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                                       BaseAuditLogAPI auditLogAPI, String deskName) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        this.deskName = deskName;
    }

    public void setSoftwareStrategyId(@Nullable UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }


    public void setSoftwareStrategyName(@Nullable String softwareStrategyName) {
        this.softwareStrategyName = softwareStrategyName;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (deskName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder()
                        .msgKey(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_SINGLE_RESULT_SUC)
                        .msgArgs(new String[] {deskName, softwareStrategyName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder()
                        .msgKey(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_SINGLE_RESULT_FAIL)
                        .msgArgs(new String[] {deskName, softwareStrategyName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount, SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_BATCH_RESULT);
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        String logName = deskName;

        CloudDesktopDetailDTO cloudDesktopDetailDTO = obtainDesktopById(taskItem.getItemID());
        if (Objects.nonNull(cloudDesktopDetailDTO)) {
            checkBeforeUpdate(cloudDesktopDetailDTO);
            logName = cloudDesktopDetailDTO.getDesktopName();
        }

        try {
            cloudDesktopMgmtAPI.updateDesktopSoftwareStrategy(taskItem.getItemID(), softwareStrategyId);
            auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_SUC_LOG, logName, softwareStrategyName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_ITEM_SUC_DESC)
                    .msgArgs(new String[] {logName, softwareStrategyName}).build();
        } catch (BusinessException e) {
            LOGGER.error("变更云桌面软件管控策略失败，云桌面id=" + taskItem.getItemID().toString(), e);
            auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_FAIL_LOG, logName, softwareStrategyName,
                    e.getI18nMessage());
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_ITEM_FAIL_DESC, e, logName,
                    softwareStrategyName, e.getI18nMessage());
        }
    }

    private CloudDesktopDetailDTO obtainDesktopById(UUID deskId) {
        try {
            return cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面数据失败，桌面id[{}]", deskId, e);
        }
        // 获取云桌面数据失败，返回null
        return null;
    }

    private void checkBeforeUpdate(CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        if (Objects.equals(cloudDesktopDetailDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_DYNAMIC_POOL_NOT_SUPPORT,
                    cloudDesktopDetailDTO.getDesktopName());
        }
    }

}
