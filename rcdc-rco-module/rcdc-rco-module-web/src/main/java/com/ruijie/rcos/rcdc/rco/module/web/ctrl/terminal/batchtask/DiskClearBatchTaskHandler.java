package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
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
 * Description: D盘清空批量任务处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020-09-08
 *
 * @author zhiweiHong
 */
public class DiskClearBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiskClearBatchTaskHandler.class);
    /**
     * key - 随机UUID
     * value - 终端MAC地址
     */

    private final Map<UUID, String> idMap;

    private final CbbTerminalOperatorAPI terminalOperatorAPI;

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    public DiskClearBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, CbbTerminalOperatorAPI terminalOperatorAPI,
                                     BaseAuditLogAPI auditLogAPI, Map<UUID, String> idMap, CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        super(batchTaskItemIterator);
        Assert.notNull(idMap, "idMap can not be null");
        Assert.notNull(terminalOperatorAPI, "terminalOperatorAPI can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");

        this.terminalOperatorAPI = terminalOperatorAPI;
        this.auditLogAPI = auditLogAPI;
        this.idMap = idMap;
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not null!");
        String terminalId = idMap.get(batchTaskItem.getItemID());

        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        try {
            terminalOperatorAPI.clearIdvTerminalDataDisk(terminalId);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_SUCCESS_LOG, terminalAddr);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_SUCCESS_LOG).msgArgs(new String[] {terminalAddr}).build();
        } catch (BusinessException e) {
            LOGGER.error("发送数据盘清空命令给终端[{}]失败，失败原因：[{}]", terminalId, e.getI18nMessage());
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_FAIL_LOG, terminalAddr, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_FAIL_LOG, e, terminalAddr, e.getI18nMessage());
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_RESULT);
    }
}
