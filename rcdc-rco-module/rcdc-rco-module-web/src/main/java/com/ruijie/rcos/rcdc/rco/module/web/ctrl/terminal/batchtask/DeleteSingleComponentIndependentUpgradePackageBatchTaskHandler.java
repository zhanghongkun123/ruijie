package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalComponentIndependentUpgradePackageAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalComponentIndependentUpgradePackageInfoDTO;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
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
 * Description: 删除单条终端组件独立升级包处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/17
 *
 * @author lyb
 */
public class DeleteSingleComponentIndependentUpgradePackageBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSingleComponentIndependentUpgradePackageBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalComponentIndependentUpgradePackageAPI componentIndependentUpgradePackageAPI;

    private String packageName;

    public DeleteSingleComponentIndependentUpgradePackageBatchTaskHandler(
            CbbTerminalComponentIndependentUpgradePackageAPI componentIndependentUpgradePackageAPI, BatchTaskItem batchTaskItem,
            BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItem);
        Assert.notNull(componentIndependentUpgradePackageAPI, "componentIndependentUpgradePackageAPI can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");

        this.auditLogAPI = auditLogAPI;
        this.componentIndependentUpgradePackageAPI = componentIndependentUpgradePackageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem不能为null");

        String localPackageName = taskItem.getItemID().toString();
        try {
            localPackageName = getPackageName(taskItem.getItemID());
            final String upgradePackageName = componentIndependentUpgradePackageAPI.deleteUpgradePackage(taskItem.getItemID());
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_SUCCESS_LOG, upgradePackageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_RESULT_SUCCESS)
                    .msgArgs(new String[] {upgradePackageName}).build();
        } catch (BusinessException ex) {
            LOGGER.error("删除终端组件独立升级包[{}]失败", localPackageName, ex);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_FAIL_LOG, localPackageName,
                    ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_RESULT_FAIL, ex,
                    ex.getI18nMessage());
        } finally {
            packageName = localPackageName;
        }
    }

    private String getPackageName(UUID packageId) throws BusinessException {

        String localPackageName = packageId.toString();
        CbbTerminalComponentIndependentUpgradePackageInfoDTO packageInfoDTO =
                componentIndependentUpgradePackageAPI.findPackageInfoByPackageId(packageId);
        if (packageInfoDTO != null) {
            localPackageName = packageInfoDTO.getName();
        }
        return localPackageName;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_TASK_SUCCESS) //
                    .msgArgs(new String[] {packageName}) //
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_TASK_FAIL) //
                    .msgArgs(new String[] {packageName}) //
                    .build();
        }
    }
}
