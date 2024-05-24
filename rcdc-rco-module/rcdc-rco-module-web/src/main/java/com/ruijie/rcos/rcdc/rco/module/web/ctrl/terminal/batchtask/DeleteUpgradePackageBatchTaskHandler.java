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

import java.util.Iterator;
import java.util.UUID;

/**
 * 
 * Description: 批量重启终端任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月18日
 * 
 * @author nt
 */
public class DeleteUpgradePackageBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUpgradePackageBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalSystemUpgradePackageAPI terminalUpgradePackageAPI;

    public DeleteUpgradePackageBatchTaskHandler(CbbTerminalSystemUpgradePackageAPI terminalUpgradePackageAPI,
            Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.terminalUpgradePackageAPI = terminalUpgradePackageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem不能为null");

        try {
            String packageName =
                    terminalUpgradePackageAPI.deleteUpgradePackage(taskItem.getItemID());
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_SUCCESS_LOG,
                    packageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_RESULT_SUCCESS)
                    .msgArgs(new String[] {packageName}).build();
        } catch (BusinessException ex) {
            LOGGER.error("delete terminal system package fail", ex);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_FAIL_LOG,
                    getPackageName(taskItem.getItemID()), ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_RESULT_FAIL, ex, ex.getI18nMessage());
        }
    }

    private String getPackageName(UUID packageId) {
        try {
            CbbTerminalSystemUpgradePackageInfoDTO upgradePackageInfoDTO = terminalUpgradePackageAPI.findById(packageId);
            return upgradePackageInfoDTO.getName();
        } catch (BusinessException e) {
            LOGGER.info("获取升级包名称异常", e);
            return packageId.toString();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount,
                TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_RESULT);
    }
}
