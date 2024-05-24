package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbUpdateDeskNetworkVDIDTO;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 变更应用池网络策略批任务处理器
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月04日
 *
 * @author zhengjingyong
 */
public class RcaUpdateAppPoolNetworkBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaUpdateAppPoolNetworkBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private UUID networkId;

    private String ip;

    private UUID appPoolId;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private RcaAppPoolBaseDTO appPoolBaseDTO;

    public RcaUpdateAppPoolNetworkBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        CbbDeskInfoDTO desktopDTO = cbbVDIDeskMgmtAPI.getByDeskId(taskItem.getItemID());
        String desktopName = desktopDTO.getName();

        try {
            CbbUpdateDeskNetworkVDIDTO cbbUpdateDeskNetwork = buildCbbUpdateDeskNetworkRequest(taskItem.getItemID(), networkId, ip);
            cbbVDIDeskMgmtAPI.updateDeskNetworkVDI(cbbUpdateDeskNetwork);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_TASK_ITEM_SUCCESS,
                    appPoolBaseDTO.getName(), desktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_TASK_ITEM_SUCCESS)
                    .msgArgs(new String[] {appPoolBaseDTO.getName(), desktopName}).build();
        } catch (Exception e) {
            LOGGER.error("编辑云桌面的网络策略失败， 应用主机id:{}，ex :", taskItem.getItemID(), e);
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_TASK_ITEM_FAIL,
                    appPoolBaseDTO.getName(), desktopName, errorMsg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_TASK_ITEM_FAIL)
                    .msgArgs(new String[]{appPoolBaseDTO.getName(), desktopName, errorMsg}).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        try {
            rcaAppPoolAPI.updateAppPoolState(appPoolId, RcaEnum.PoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新应用池[%s]信息失败", appPoolId), e);
        }
        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_SUCCESS, appPoolBaseDTO.getName());

        return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_RESULT);
    }

    private CbbUpdateDeskNetworkVDIDTO buildCbbUpdateDeskNetworkRequest(UUID deskId, UUID networkId, String ip) {
        CbbUpdateDeskNetworkVDIDTO cbbUpdateReq = new CbbUpdateDeskNetworkVDIDTO();
        cbbUpdateReq.setDeskId(deskId);
        cbbUpdateReq.setNetworkId(networkId);
        cbbUpdateReq.setIp(StringUtils.isBlank(ip) ? null : ip);
        return cbbUpdateReq;
    }

    public void setRcaAppPoolAPI(RcaAppPoolAPI rcaAppPoolAPI) {
        this.rcaAppPoolAPI = rcaAppPoolAPI;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public void setAppPoolId(UUID appPoolId) {
        this.appPoolId = appPoolId;
    }

    public void setCbbVDIDeskMgmtAPI(CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI) {
        this.cbbVDIDeskMgmtAPI = cbbVDIDeskMgmtAPI;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setAppPoolBaseDTO(RcaAppPoolBaseDTO appPoolBaseDTO) {
        this.appPoolBaseDTO = appPoolBaseDTO;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }
}
