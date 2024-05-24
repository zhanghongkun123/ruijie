package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbCloudPlatformRecoverAPI;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbRestorePlatformResourceDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.platform.CloudPlatformManageBaseRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.request.CloudPlatformManageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.CloudPlatformResourceRecoverRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey.*;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2024/01/29 <br>
 *
 * @author lanzf
 */
public class RecoverCloudPlatformResourceSingleTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverCloudPlatformResourceSingleTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CloudPlatformResourceRecoverRequest webReq;

    private CbbCloudPlatformRecoverAPI cbbCloudPlatformRecoverAPI;

    private CloudPlatformManageAPI cloudPlatformManageAPI;


    public RecoverCloudPlatformResourceSingleTaskHandler(BatchTaskItem batchTaskItem, //
                                                         BaseAuditLogAPI auditLogAPI, //
                                                         CloudPlatformResourceRecoverRequest webReq, //
                                                         CbbCloudPlatformRecoverAPI cbbCloudPlatformRecoverAPI,
                                                         CloudPlatformManageAPI cloudPlatformManageAPI) {
        super(batchTaskItem);

        Assert.notNull(batchTaskItem,"batchTaskItem can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");
        Assert.notNull(webReq, "webReq can not be null");
        Assert.notNull(cbbCloudPlatformRecoverAPI, "cbbCloudPlatformRecoverAPI can not be null");

        this.auditLogAPI = auditLogAPI;
        this.webReq = webReq;
        this.cbbCloudPlatformRecoverAPI = cbbCloudPlatformRecoverAPI;
        this.cloudPlatformManageAPI = cloudPlatformManageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        final String platformName = getCloudPlatformNameElseId(batchTaskItem.getItemID());

        try {
            final CbbRestorePlatformResourceDTO recoverDTO = new CbbRestorePlatformResourceDTO();
            BeanUtils.copyProperties(webReq, recoverDTO);
            recoverDTO.setFromCloudPlatformId(webReq.getFromPlatformEntry().getId());
            final CloudPlatformManageWebRequest toPlatform = webReq.getToPlatform();
            final CloudPlatformManageBaseRequest baseRequest = buildToPlatformReq(toPlatform);
            recoverDTO.setToPlatform(baseRequest);

            cloudPlatformManageAPI.testConnected(baseRequest);

            LOGGER.info("开始恢复云平台：[{}]，新关联云平台信息：[{}][{}]", webReq.getFromPlatformEntry().getId(), toPlatform.getName(), toPlatform.getType());
            cbbCloudPlatformRecoverAPI.recoverPlatformResourceRelationship(recoverDTO);
            auditLogAPI.recordLog(RCDC_CLOUD_PLATFORM_RECOVER_RESOURCE_RELATIONSHIP_SUC_LOG, platformName);
            return DefaultBatchTaskItemResult.success(RCDC_CLOUD_PLATFORM_RECOVER_RESOURCE_RELATIONSHIP_ITEM_RESULT_SUC, platformName);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RCDC_CLOUD_PLATFORM_RECOVER_RESOURCE_RELATIONSHIP_FAIL_LOG, platformName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.fail(RCDC_CLOUD_PLATFORM_RECOVER_RESOURCE_RELATIONSHIP_ITEM_RESULT_FAIL,
                    platformName, e.getI18nMessage());
        }
    }

    private CloudPlatformManageBaseRequest buildToPlatformReq(CloudPlatformManageWebRequest toPlatform) {
        final CloudPlatformManageBaseRequest baseRequest = new CloudPlatformManageBaseRequest();
        baseRequest.setDescription(toPlatform.getDescription());
        baseRequest.setName(toPlatform.getName());
        baseRequest.setType(toPlatform.getType());
        baseRequest.setExtendConfig(toPlatform.getExtendConfig());
        return baseRequest;
    }

    private String getCloudPlatformNameElseId(UUID platformId) {
        try {
            return cloudPlatformManageAPI.getInfoById(platformId).getName();
        } catch (Exception e) {
            return String.valueOf(platformId);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder()
                    .batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(RCDC_CLOUD_PLATFORM_RECOVER_RESOURCE_RELATIONSHIP_RESULT_SUC)
                    .build();
        }

        return DefaultBatchTaskFinishResult.builder()
                .batchTaskStatus(BatchTaskStatus.FAILURE)
                .msgKey(RCDC_CLOUD_PLATFORM_RECOVER_RESOURCE_RELATIONSHIP_RESULT_FAIL)
                .build();
    }
}
