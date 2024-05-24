package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupRestoreAPI;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbBackupDetailDTO;
import com.ruijie.rcos.rcdc.backup.module.def.enums.MetaType;
import com.ruijie.rcos.rcdc.backup.module.def.enums.RecoverType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.RecoverResourceRequest;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey.*;

/**
 * <br>
 * Description: 恢复CDC的资源对象
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2024/01/29 <br>
 *
 * @author lanzf
 */
public class RecoverCdcResourceSingleTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverCdcResourceSingleTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RecoverResourceRequest webReq;

    private CbbBackupRestoreAPI cbbBackupRestoreAPI;

    private CbbBackupDetailDTO backupDTO;

    public RecoverCdcResourceSingleTaskHandler(BatchTaskItem batchTaskItem, //
                                               BaseAuditLogAPI auditLogAPI, //
                                               RecoverResourceRequest webReq, //
                                               CbbBackupRestoreAPI cbbBackupRestoreAPI,
                                               CbbBackupDetailDTO cbbBackupDetailDTO) {
        super(batchTaskItem);

        Assert.notNull(batchTaskItem,"batchTaskItem can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");
        Assert.notNull(webReq, "webReq can not be null");
        Assert.notNull(cbbBackupRestoreAPI, "cbbBackupRestoreAPI can not be null");
        Assert.notNull(cbbBackupDetailDTO, "cbbBackupDetailDTO can not be null");

        this.auditLogAPI = auditLogAPI;
        this.webReq = webReq;
        this.cbbBackupRestoreAPI = cbbBackupRestoreAPI;
        this.backupDTO = cbbBackupDetailDTO;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        final String backupFormatTime = DateUtil.formatDate(backupDTO.getCreateTime(), DateUtil.YYYY_MM_DD_HH24MISS);
        final boolean isAsync = MetaType.isAyncRecover(backupDTO.getMetaType());
        try {
            LOGGER.info("开始将资源[{}]恢复到时间点：[{}]", backupDTO.getName(), backupFormatTime);
            final RecoverType recoverType = webReq.getRecoverType();
            cbbBackupRestoreAPI.recoverResourceByDetailId(backupDTO.getId(), recoverType);

            final String auditKey = isAsync ? RCDC_SERVER_RECOVER_RESOURCE_ASYNC_SUC_LOG : RCDC_SERVER_RECOVER_RESOURCE_SUC_LOG;
            auditLogAPI.recordLog(auditKey, backupDTO.getName(), backupFormatTime, recoverType.getAliasName());

            final String itemSucKey = isAsync ? RCDC_SERVER_RECOVER_RESOURCE_ITEM_ASYNC_RESULT_SUC : RCDC_SERVER_RECOVER_RESOURCE_ITEM_RESULT_SUC;
            return DefaultBatchTaskItemResult.success(itemSucKey, backupDTO.getName());
        } catch (BusinessException e) {
            final String auditKey = isAsync ? RCDC_SERVER_RECOVER_RESOURCE_ASYNC_FAIL_LOG : RCDC_SERVER_RECOVER_RESOURCE_FAIL_LOG;
            auditLogAPI.recordLog(auditKey, backupDTO.getName(), backupFormatTime, e.getI18nMessage(), webReq.getRecoverType().getAliasName());

            final String itemSucKey = isAsync ? RCDC_SERVER_RECOVER_RESOURCE_ITEM_ASYNC_RESULT_FAIL : RCDC_SERVER_RECOVER_RESOURCE_ITEM_RESULT_FAIL;
            return DefaultBatchTaskItemResult.fail(itemSucKey, backupDTO.getName(), e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        final boolean isAsync = MetaType.isAyncRecover(backupDTO.getMetaType());

        if (failCount == 0) {
            final String sucResultKey = isAsync ? RCDC_SERVER_RECOVER_RESOURCE_ASYNC_RESULT_SUC : RCDC_SERVER_RECOVER_RESOURCE_RESULT_SUC;
            return DefaultBatchTaskFinishResult.builder()
                    .batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(sucResultKey)
                    .build();
        }

        final String failResultKey = isAsync ? RCDC_SERVER_RECOVER_RESOURCE_ASYNC_RESULT_FAIL : RCDC_SERVER_RECOVER_RESOURCE_RESULT_FAIL;
        return DefaultBatchTaskFinishResult.builder()
                .batchTaskStatus(BatchTaskStatus.FAILURE)
                .msgKey(failResultKey)
                .build();
    }
}
