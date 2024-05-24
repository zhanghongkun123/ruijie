package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_IS_EMPTY;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalComponentIndependentUpgradeAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbUpgradeTerminalDTO;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 
 * Description: 批量升级终端任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月28日
 * 
 * @author nt
 */
public class AddComponentIndependentUpgradeTerminalBatchTaskHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private Map<UUID, String> idMap;

    private CbbTerminalComponentIndependentUpgradeAPI cbbTerminalUpgradeAPI;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    public AddComponentIndependentUpgradeTerminalBatchTaskHandler(CbbTerminalComponentIndependentUpgradeAPI cbbTerminalUpgradeAPI,
                                                                  CbbTerminalOperatorAPI cbbTerminalOperatorAPI,
                                                                  Map<UUID, String> idMap, Iterator<? extends BatchTaskItem> iterator,
                                                                  BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.idMap = idMap;
        this.cbbTerminalUpgradeAPI = cbbTerminalUpgradeAPI;
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "BatchTaskItem不能为null");

        TerminalUpgradeBatchTaskItem upgradeItem = (TerminalUpgradeBatchTaskItem) taskItem;
        String terminalId = idMap.get(upgradeItem.getItemID());
        UUID upgradeTaskId = upgradeItem.getUpgradeTaskId();

        return addUpgradeTaskAddOptLog(terminalId, upgradeTaskId);
    }

    private BatchTaskItemResult addUpgradeTaskAddOptLog(String terminalId, UUID upgradeTaskId) throws BusinessException {
        if (StringUtils.isBlank(terminalId)) {
            throw new BusinessException(RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_IS_EMPTY, "terminalId");
        }
        CbbTerminalBasicInfoDTO basicInfoDTO;
        try {
            basicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_FAIL_LOG, terminalId.toUpperCase(),
                    e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_RESULT_FAIL, e, "", e.getI18nMessage());
        }
        CbbUpgradeTerminalDTO addRequest = new CbbUpgradeTerminalDTO();
        addRequest.setTerminalId(terminalId);
        addRequest.setUpgradeTaskId(upgradeTaskId);
        try {
            cbbTerminalUpgradeAPI.addComponentIndependentUpgradeTerminal(addRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_SUCCESS_LOG,
                    basicInfoDTO.getMacAddr().toUpperCase());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_RESULT_SUCCESS)
                    .msgArgs(new String[] {basicInfoDTO.getMacAddr().toUpperCase()}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_FAIL_LOG,
                    basicInfoDTO.getMacAddr().toUpperCase(),
                    e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_RESULT_FAIL, e,
                    basicInfoDTO.getMacAddr().toUpperCase(), e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_RESULT);
    }
}
