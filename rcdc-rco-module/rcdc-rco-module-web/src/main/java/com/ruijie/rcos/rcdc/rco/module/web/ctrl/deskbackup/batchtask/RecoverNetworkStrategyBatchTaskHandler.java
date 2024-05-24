package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbCloudPlatformRecoverAPI;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbRecoverNetworkStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.NetworkStrategyRecoverRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey.*;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/01/25
 *
 * @author lanzf
 */
public class RecoverNetworkStrategyBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverNetworkStrategyBatchTaskHandler.class);

    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    private CbbCloudPlatformRecoverAPI cbbCloudPlatformRecoverAPI;

    private BaseAuditLogAPI auditLogAPI;

    private NetworkStrategyRecoverRequest webReq;

    public RecoverNetworkStrategyBatchTaskHandler(Iterator<DefaultBatchTaskItem> batchTaskItem, //
                                                  BaseAuditLogAPI auditLogAPI, //
                                                  CbbNetworkMgmtAPI cbbNetworkMgmtAPI, //
                                                  CbbCloudPlatformRecoverAPI cbbCloudPlatformRecoverAPI, //
                                                  NetworkStrategyRecoverRequest webReq) {
        super(batchTaskItem);

        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        Assert.notNull(webReq, "webReq can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");
        this.webReq = webReq;
        this.auditLogAPI = auditLogAPI;
        this.cbbCloudPlatformRecoverAPI = cbbCloudPlatformRecoverAPI;
        this.cbbNetworkMgmtAPI =  cbbNetworkMgmtAPI;

    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        final String networkName = getNetworkStrategyNameElseId(batchTaskItem.getItemID());

        try {
            final CbbDeskNetworkDetailDTO deskNetwork = cbbNetworkMgmtAPI.getDeskNetwork(batchTaskItem.getItemID());
            final CbbRecoverNetworkStrategyDTO apiReq = new CbbRecoverNetworkStrategyDTO();
            apiReq.setNetworkStrategyId(batchTaskItem.getItemID());
            apiReq.setVswitchId(webReq.getVswitchId());
            apiReq.setPlatformId(deskNetwork.getPlatformId());
            LOGGER.info("开始恢复网络策略：[{}]，新关联虚拟交换机信息：[{}]", apiReq.getNetworkStrategyId(), apiReq.getVswitchId());
            cbbCloudPlatformRecoverAPI.recoverNetworkStrategy(apiReq);

            auditLogAPI.recordLog(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_SUC_LOG, networkName);
            return DefaultBatchTaskItemResult.builder() //
                    .batchTaskItemStatus(BatchTaskItemStatus.SUCCESS) //
                    .msgKey(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_ITEM_RESULT_SUC) //
                    .msgArgs(networkName) //
                    .build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_FAIL_LOG, networkName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.fail(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_ITEM_RESULT_FAIL, networkName, e.getI18nMessage());
        }
    }

    private String getNetworkStrategyNameElseId(UUID networkStrategyId) {
        try {
            final CbbDeskNetworkDetailDTO deskNetwork = cbbNetworkMgmtAPI.getDeskNetwork(networkStrategyId);
            return deskNetwork.getDeskNetworkName();
        } catch (Exception e) {
            return String.valueOf(networkStrategyId);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder()
                    .batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_RESULT_SUC)
                    .build();
        }

        return DefaultBatchTaskFinishResult.builder()
                .batchTaskStatus(BatchTaskStatus.FAILURE)
                .msgKey(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_RESULT_FAIL)
                .build();
    }

}
