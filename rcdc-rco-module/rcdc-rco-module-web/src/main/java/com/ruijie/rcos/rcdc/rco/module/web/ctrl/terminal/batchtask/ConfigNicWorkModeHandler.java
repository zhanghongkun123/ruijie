package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNicWorkModeEnums;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/20 10:38
 *
 * @author yxq
 */
public class ConfigNicWorkModeHandler  extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigNicWorkModeHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbNicWorkModeEnums nicWorkMode;

    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    /**
     * 记录操作个数
     */
    private int processItemCount;

    private Map<UUID, String> idMap;

    public ConfigNicWorkModeHandler(Map<UUID, String> idMap, Iterator<DefaultBatchTaskItem> iterator) {
        super(iterator);
        this.idMap = idMap;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setNicWorkMode(CbbNicWorkModeEnums nicWorkMode) {
        this.nicWorkMode = nicWorkMode;
    }

    public void setUserTerminalMgmtAPI(UserTerminalMgmtAPI userTerminalMgmtAPI) {
        this.userTerminalMgmtAPI = userTerminalMgmtAPI;
    }

    public void setCbbTerminalOperatorAPI(CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item must not be null");

        String terminalId = idMap.get(item.getItemID());

        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        try {
            // 修改网卡工作模式
            userTerminalMgmtAPI.configTerminalNicWorkMode(terminalId, nicWorkMode);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SUCCESS_LOG, terminalAddr);

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SEND_SUCCESS_WITH_TERMINAL).msgArgs(terminalAddr).build();
        } catch (BusinessException e) {
            LOGGER.error(String.format("修改终端[%s]网卡工作模式失败，失败原因：", terminalId), e);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_FAIL_LOG, terminalAddr, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SEND_FAIL_WITH_REASON, e, terminalAddr,
                    e.getI18nMessage());
        } finally {
            processItemCount++;
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        LOGGER.info("批量修改网卡工作模式处理个数为：[{}]", processItemCount);
        if (processItemCount == 1) {
            // 只处理一个，并且成功
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(TerminalBusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SEND_SUCCESS)
                        .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            }

            // 只处理一个，并且失败
            return DefaultBatchTaskFinishResult.builder().msgKey(TerminalBusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SEND_FAIL)
                    .batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }

        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_RESULT);
    }
}
