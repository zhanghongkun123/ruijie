package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.handler;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.base.upgrade.module.def.dto.AppPacketConfigDTO;
import com.ruijie.rcos.base.upgrade.module.def.dto.ApplicationPacketDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.CommonUpgradeBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.dto.RcoAppPacketConfigDTO;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/19 10:45
 *
 * @author chenl
 */
public class PacketConfigTaskHandler extends AbstractSingleTaskHandler {


    private BaseAuditLogAPI auditLogAPI;

    private BaseApplicationPacketAPI baseApplicationPacketAPI;

    private RcoAppPacketConfigDTO webRequest;

    private ApplicationPacketDTO applicationPacketDTO;


    public PacketConfigTaskHandler(RcoAppPacketConfigDTO webRequest, ApplicationPacketDTO applicationPacketDTO,
                                   BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                   BaseApplicationPacketAPI baseApplicationPacketAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(baseApplicationPacketAPI, "baseApplicationPacketAPI is not null");
        Assert.notNull(webRequest, "webRequest is not null");

        this.webRequest = webRequest;
        this.auditLogAPI = auditLogAPI;
        this.baseApplicationPacketAPI = baseApplicationPacketAPI;
        this.applicationPacketDTO = applicationPacketDTO;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        applicationPacketDTO = baseApplicationPacketAPI.detailPacket(webRequest.getId());
        try {
            AppPacketConfigDTO apiRequest = new AppPacketConfigDTO();
            apiRequest.setId(webRequest.getId());
            apiRequest.setUpgradeMode(webRequest.getUpgradeMode());
            apiRequest.setUpgradeRange(webRequest.getUpgradeRange());
            apiRequest.setUpgradeTargetArr(webRequest.toUpgradeTargets());
            apiRequest.setStartUpgradeTime(webRequest.getUpgradeBeginTime());
            apiRequest.setEndUpgradeTime(webRequest.getUpgradeEndTime());
            baseApplicationPacketAPI.updatePacketConfig(apiRequest);
            auditLogAPI.recordLog(CommonUpgradeBusinessKey.RCDC_PACKET_CONFIG_SUCCESS_LOG, applicationPacketDTO.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CommonUpgradeBusinessKey.RCDC_PACKET_CONFIG_SUCCESS_LOG).msgArgs(applicationPacketDTO.getName()).build();
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(CommonUpgradeBusinessKey.RCDC_PACKET_CONFIG_FAIL_LOG, applicationPacketDTO.getName(), ex.getI18nMessage());
            throw new BusinessException(CommonUpgradeBusinessKey.RCDC_PACKET_CONFIG_FAIL_LOG, ex, applicationPacketDTO.getName(),
                    ex.getI18nMessage());
        }
    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CommonUpgradeBusinessKey.RCDC_PACKET_CONFIG_TASK_SUCCESS).msgArgs(new String[]{applicationPacketDTO.getName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CommonUpgradeBusinessKey.RCDC_PACKET_CONFIG_TASK_FAIL).msgArgs(new String[]{applicationPacketDTO.getName()}).build();
        }
    }
}
