package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.desknetwork;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 
 * Description: 删除桌面网络策略handler
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月08日
 * 
 * @author nt
 */
public class DeleteSingleDeskNetworkBatchHandler extends AbstractSingleTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    private String networkName = "";

    private Boolean shouldOnlyDeleteDataFromDb;

    private String prefix;

    public DeleteSingleDeskNetworkBatchHandler(CbbNetworkMgmtAPI cbbNetworkMgmtAPI, BatchTaskItem batchTaskItem,
                                               BaseAuditLogAPI auditLogAPI, Boolean shouldOnlyDeleteDataFromDb) {
        super(batchTaskItem);
        Assert.notNull(cbbNetworkMgmtAPI, "cbbNetworkMgmtAPI is not null");

        this.cbbNetworkMgmtAPI = cbbNetworkMgmtAPI;
        this.auditLogAPI = auditLogAPI;
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_TASK_SUCCESS) //
                    .msgArgs(new String[] {networkName, prefix}) //
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_TASK_FAIL) //
                    .msgArgs(new String[] {networkName, prefix}) //
                    .build();
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item can not be null");


        String name = item.getItemID().toString();
        try {
            name = getNetworkName(item.getItemID());
            if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
                cbbNetworkMgmtAPI.deleteNetworkStrategyFromDb(item.getItemID());
            } else {
                cbbNetworkMgmtAPI.deleteDeskNetwork(item.getItemID());
            }

            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SUCCESS, new String[] {name, prefix});
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_RESULT_SUCCESS)
                    .msgArgs(new String[] {name, prefix}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_FAIL, e, name, e.getI18nMessage(), prefix);
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_RESULT_FAIL, e, name,
                    e.getI18nMessage(), prefix);
        } finally {
            networkName = name;
        }
    }

    private String getNetworkName(UUID id) throws BusinessException {
        CbbDeskNetworkDetailDTO dto = cbbNetworkMgmtAPI.getDeskNetwork(id);
        String result = id.toString();
        if (dto != null) {
            result = dto.getDeskNetworkName();
        }
        return result;
    }
}
