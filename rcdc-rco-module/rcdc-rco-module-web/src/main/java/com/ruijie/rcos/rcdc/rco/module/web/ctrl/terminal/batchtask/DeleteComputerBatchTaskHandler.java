package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ComputerIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.ComputerBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
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
public class DeleteComputerBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteComputerBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private ComputerBusinessAPI computerBusinessAPI;

    private Map<UUID, String> idMap;

    private boolean isBatch = true;

    private String pcName;

    public DeleteComputerBatchTaskHandler(Map<UUID, String> idMap,
                                          Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.idMap = idMap;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    /**
     * set
     * @param computerBusinessAPI 对象
     */
    public void setComputerBusinessAPI(ComputerBusinessAPI computerBusinessAPI) {
        Assert.notNull(computerBusinessAPI, "computerBusinessAPI must not be null");
        this.computerBusinessAPI = computerBusinessAPI;
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
            if (response.getType() == ComputerTypeEnum.PC) {
                computerBusinessAPI.deleteComputer(request);
            } else {
                computerBusinessAPI.deleteThirdPartyComputer(request);
            }
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_COMPUTER_SUCCESS_LOG, computerIdentification);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_COMPUTER_SUCCESS_LOG).msgArgs(new String[] {computerIdentification}).build();
        } catch (Exception e) {
            LOGGER.error("删除PC异常：" + computerIdentification, e);
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_COMPUTER_FAIL_LOG,
                        computerIdentification, ex.getI18nMessage());
                throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_COMPUTER_FAIL_LOG, e, computerIdentification,
                        ex.getI18nMessage());
            } else {
                throw new IllegalStateException("删除PC异常，PC为[" + computerIdentification + "]", e);
            }
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
         // 批量删除用户
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_RESULT);
        }

         // 删除单条PC
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder()
                    .msgKey(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_SINGLE_SUCCESS_RESULT).msgArgs(new String[] {pcName})
                    .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder()
                    .msgKey(ComputerBusinessKey.RCDC_RCO_COMPUTER_DELETE_SINGLE_FAIL_RESULT).msgArgs(new String[] {pcName})
                    .batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }
}
