package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 
 * Description: 批量删除网络策略
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月19日
 * 
 * @author huangxiaodan
 */
public class DeleteDeskNetworkBatchHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDeskNetworkBatchHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    private Boolean shouldOnlyDeleteDataFromDb;


    private String prefix;

    public DeleteDeskNetworkBatchHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
            CbbNetworkMgmtAPI cbbNetworkMgmtAPI, Boolean shouldOnlyDeleteDataFromDb) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.cbbNetworkMgmtAPI = cbbNetworkMgmtAPI;
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return WebBatchTaskUtils.buildDefaultFinishResult(successCount, failCount,
                CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_BATCH_DELETE_TASK_RESULT, prefix);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item can not be null");
        UUID id = item.getItemID();
        String networkName = id.toString();
        try {
            networkName = getNetworkName(id);
            if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
                cbbNetworkMgmtAPI.deleteNetworkStrategyFromDb(id);
            } else {
                cbbNetworkMgmtAPI.deleteDeskNetwork(id);
            }

            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SUCCESS, new String[] {networkName, prefix});
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SUCCESS).msgArgs(new String[] {networkName, prefix})
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("DeleteDeskNetworkBatchHandler has BusinessException", e);
            return processDeleteBatchFail(networkName, e);
        }
    }

    private BatchTaskItemResult processDeleteBatchFail(String networkName, BusinessException e) throws BusinessException {
        auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_FAIL, e,
                new String[] {networkName, e.getI18nMessage(), prefix});
        throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_FAIL, e, networkName, e.getI18nMessage(),
                prefix);
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
