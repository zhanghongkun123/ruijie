package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 纳管三方应用主机批量任务Handler
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月23日
 *
 * @author liuwc
 */
public class ShutdownThirdPartRcaHostBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownThirdPartRcaHostBatchTaskHandler.class);

    private RcaHostAPI rcaHostAPI;

    private BaseAuditLogAPI auditLogAPI;

    public ShutdownThirdPartRcaHostBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
    }

    public void setRcaHostAPI(RcaHostAPI rcaHostAPI) {
        this.rcaHostAPI = rcaHostAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        UUID hostId = taskItem.getItemID();
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(hostId);
        String hostName = rcaHostDTO.getName();
        if (!ObjectUtils.isEmpty(rcaHostDTO) && rcaHostDTO.getStatus() == RcaEnum.HostStatus.OFFLINE) {
            LOGGER.error("应用主机[{}]关机失败，应用主机离线", hostId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_OFFLINE, rcaHostDTO.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_OFFLINE).msgArgs(new String[]{hostName}).build();
        }

        try {
            rcaHostAPI.shutdownThirdPartyHost(hostId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_SUCCESS, hostName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_SUCCESS).msgArgs(new String[]{hostName}).build();
        } catch (BusinessException e) {
            LOGGER.error("通知应用主机应用主机关机,主机id={}, 名称={}", hostId, hostName);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_FAIL, hostName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_FAIL)
                    .msgArgs(new String[]{hostName, e.getI18nMessage()})
                    .build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_BATCH_RESULT);
    }

}
