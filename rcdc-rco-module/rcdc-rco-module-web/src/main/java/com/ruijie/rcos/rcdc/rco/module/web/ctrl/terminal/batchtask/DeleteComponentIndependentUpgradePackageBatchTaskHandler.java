package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalComponentIndependentUpgradePackageAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalComponentIndependentUpgradePackageInfoDTO;
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
 * Description: 批量删除终端组件独立升级包处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/17
 *
 * @author lyb
 */
public class DeleteComponentIndependentUpgradePackageBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteComponentIndependentUpgradePackageBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalComponentIndependentUpgradePackageAPI componentIndependentUpgradePackageAPI;

    public DeleteComponentIndependentUpgradePackageBatchTaskHandler(
            CbbTerminalComponentIndependentUpgradePackageAPI componentIndependentUpgradePackageAPI, Iterator<? extends BatchTaskItem> iterator,
            BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.componentIndependentUpgradePackageAPI = componentIndependentUpgradePackageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem不能为null");

        try {
            String packageName = componentIndependentUpgradePackageAPI.deleteUpgradePackage(taskItem.getItemID());
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_SUCCESS_LOG, packageName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_RESULT_SUCCESS)
                    .msgArgs(new String[] {packageName}).build();
        } catch (BusinessException ex) {
            LOGGER.error("删除组件独立升级包失败，packageId[{}]", taskItem.getItemID(), ex);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_FAIL_LOG,
                    getPackageName(taskItem.getItemID()), ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_RESULT_FAIL, ex,
                    ex.getI18nMessage());
        }
    }

    private String getPackageName(UUID packageId) {
        try {
            CbbTerminalComponentIndependentUpgradePackageInfoDTO upgradePackageInfoDTO =
                    componentIndependentUpgradePackageAPI.findPackageInfoByPackageId(packageId);
            return upgradePackageInfoDTO.getName();
        } catch (BusinessException e) {
            LOGGER.info("获取升级包名称异常", e);
            return packageId.toString();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount,
                TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_RESULT);
    }
}
