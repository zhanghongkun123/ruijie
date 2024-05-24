package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;




import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ComputerIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.ComputerBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.PublicBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 16:08
 *
 * @author ketb
 */
public class CloseComputerBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseComputerBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private ComputerBusinessAPI computerBusinessAPI;

    private CbbTerminalOperatorAPI terminalOperatorAPI;

    private Map<UUID, String> idMap;

    public CloseComputerBatchTaskHandler(Map<UUID, String> idMap,
                                         Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.idMap = idMap;
    }

    /**
     * set
     * @param computerBusinessAPI 对象
     */
    public void setComputerBusinessAPI(ComputerBusinessAPI computerBusinessAPI) {
        Assert.notNull(computerBusinessAPI, "computerBusinessAPI must not be null");
        this.computerBusinessAPI = computerBusinessAPI;
    }

    /**
     * set
     * @param terminalOperatorAPI 对象
     */
    public void setTerminalOperatorAPI(CbbTerminalOperatorAPI terminalOperatorAPI) {
        Assert.notNull(terminalOperatorAPI, "terminalOperatorAPI must not be null");
        this.terminalOperatorAPI = terminalOperatorAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "BatchTaskItem不能为null");
        String computerId = idMap.get(taskItem.getItemID());
        String computerIdentification = computerId;
        try {
            ComputerIdRequest request = new ComputerIdRequest(UUID.fromString(computerId));
            ComputerInfoResponse response = computerBusinessAPI.getComputerInfoByComputerId(request);
            computerIdentification = response.getMac();
            if (response.getType() == ComputerTypeEnum.THIRD) {
                throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_NOT_SUPPORT_THIRD_PARTY_OP, computerIdentification);
            }
            terminalOperatorAPI.shutdownComputer(com.ruijie.rcos.rcdc.terminal.module.def.constants.Constants.PC_FLAG + response.getMac());
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_SUCCESS_LOG, computerIdentification);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_SUCCESS_LOG).msgArgs(new String[] {computerIdentification}).build();
        } catch (Exception e) {
            LOGGER.error("关闭终端：" + computerIdentification, e);
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                String i18nMessage = ex.getI18nMessage();
                if (PublicBusinessKey.RCDC_TERMINAL_OFFLINE_CANNOT_SHUTDOWN.equals(ex.getKey())) {
                    i18nMessage = LocaleI18nResolver.resolve(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_OFFLINE);
                }
                auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_FAIL_LOG, computerIdentification, i18nMessage);
                throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_FAIL_LOG, e, computerIdentification, i18nMessage);
            } else {
                throw new IllegalStateException("发送关闭PC命令异常，PC为[" + computerIdentification + "]", e);
            }
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, ComputerBusinessKey.RCDC_RCO_COMPUTER_CLOSE_RESULT);
    }
}
