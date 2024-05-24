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
 * Create Time: 2020/03/23 16:08
 *
 * @author ljm
 */
public class RelieveFaultComputerBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelieveFaultComputerBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private ComputerBusinessAPI computerBusinessAPI;

    private Map<UUID, String> idMap;

    private boolean isBatch = true;

    private String pcName;

    public RelieveFaultComputerBatchTaskHandler(Map<UUID, String> idMap,
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
     *
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

            UUID uuid = UUID.fromString(computerId);
            ComputerIdRequest request = new ComputerIdRequest(uuid);
            ComputerInfoResponse response = computerBusinessAPI.getComputerInfoByComputerId(request);
            computerIdentification = response.getMac();
            if (response.getType() == ComputerTypeEnum.THIRD) {
                throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_NOT_SUPPORT_THIRD_PARTY_OP, computerIdentification);
            }
            ComputerIdRequest idRequest = new ComputerIdRequest();
            idRequest.setComputerId(uuid);
            computerBusinessAPI.relieveFault(idRequest);
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_SUCCESS, computerIdentification);

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_SUCCESS)
                .msgArgs(new String[]{computerIdentification}).build();

        } catch (BusinessException e) {
            LOGGER.error("取消PC报障异常：" + computerIdentification, e);
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_FAIL,
                computerIdentification, e.getI18nMessage());
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_FAIL, e, computerIdentification, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量取消PC报障
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount,
                ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_RESULT);
        }

        // 取消单条PC报障
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder()
                .msgKey(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_SUCCESS_RESULT)
                .msgArgs(new String[]{pcName})
                .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder()
                .msgKey(ComputerBusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_FAIL_RESULT).msgArgs(new String[]{pcName})
                .batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }
}
