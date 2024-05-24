package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalSystemUpgradePackageAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalSystemUpgradePackageInfoDTO;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * 
 * Description: 删除单条终端升级包处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月08日
 * 
 * @author nt
 */
public class DeleteSingleUpgradePackageBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSingleUpgradePackageBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalSystemUpgradePackageAPI terminalUpgradePackageAPI;

    private String packageName;

    public DeleteSingleUpgradePackageBatchTaskHandler(CbbTerminalSystemUpgradePackageAPI terminalUpgradePackageAPI, BatchTaskItem batchTaskItem,
                                                      BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItem);
        Assert.notNull(terminalUpgradePackageAPI, "terminalUpgradePackageAPI can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");

        this.auditLogAPI = auditLogAPI;
        this.terminalUpgradePackageAPI = terminalUpgradePackageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem不能为null");

        String localPackageName = taskItem.getItemID().toString();
        try {
            localPackageName = getPackageName(taskItem.getItemID());
            final String upgradePackageName = terminalUpgradePackageAPI.deleteUpgradePackage(taskItem.getItemID());
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_SUCCESS_LOG, upgradePackageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_RESULT_SUCCESS).msgArgs(new String[] {upgradePackageName})
                    .build();
        } catch (BusinessException ex) {
            LOGGER.error("delete terminal system package fail", ex);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_FAIL_LOG, localPackageName, ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_RESULT_FAIL, ex, ex.getI18nMessage());
        } finally {
            packageName = localPackageName;
        }
    }

    private String getPackageName(UUID packageId) throws BusinessException {

        String localPackageName = packageId.toString();
        CbbTerminalSystemUpgradePackageInfoDTO packageInfoDTO = terminalUpgradePackageAPI.findById(packageId);
        if (packageInfoDTO != null) {
            localPackageName = packageInfoDTO.getName();
        }
        return localPackageName;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_TASK_SUCCESS) //
                    .msgArgs(new String[] {packageName}) //
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_TASK_FAIL) //
                    .msgArgs(new String[] {packageName}) //
                    .build();
        }
    }
}
