package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;


import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.EditDesktopStrategyRequest;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 批量修改策略
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年3月25日
 *
 * @author zhengjingyong
 */
public class RcaUpdateAppPoolMainStrategyBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaUpdateAppPoolMainStrategyBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UUID strategyId;

    private RcaAppPoolBaseDTO appPoolBaseDTO;

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    public RcaUpdateAppPoolMainStrategyBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, UUID strategyId,
                                                        BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(iterator, "iterator is not null");
        Assert.notNull(strategyId, "strategyId is not null");
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");

        this.strategyId = strategyId;
        this.auditLogAPI = auditLogAPI;
    }

    public void setCloudDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
    }

    public void setAppPoolBaseDTO(RcaAppPoolBaseDTO appPoolBaseDTO) {
        this.appPoolBaseDTO = appPoolBaseDTO;
    }

    public void setRcaAppPoolAPI(RcaAppPoolAPI rcaAppPoolAPI) {
        this.rcaAppPoolAPI = rcaAppPoolAPI;
    }



    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        UUID deskId = taskItem.getItemID();
        CloudDesktopDetailDTO desktopDTO = cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
        String tmpDesktopName = desktopDTO.getDesktopName();
        try {
            CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(desktopDTO.getDesktopCategory());
            LOGGER.info("准备编辑修改云主机[id:{}]策略", desktopDTO.getId());
            switch (deskType) {
                case VDI:
                    cloudDesktopMgmtAPI.configStrategy(new EditDesktopStrategyRequest(taskItem.getItemID(), strategyId));
                    break;
                default:
                    throw new BusinessException("不支持的云主机类型:{0}", deskType.name());
            }

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_ITEM_SUCCESS,
                    appPoolBaseDTO.getName(), tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_ITEM_SUCCESS)
                    .msgArgs(new String[]{appPoolBaseDTO.getName(), tmpDesktopName}).build();
        } catch (Exception e) {
            LOGGER.error("编辑云主机的策略失败，云桌面id=" + taskItem.getItemID().toString(), e);
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_ITEM_FAIL,
                    appPoolBaseDTO.getName(), tmpDesktopName, errorMsg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_ITEM_FAIL)
                    .msgArgs(new String[]{appPoolBaseDTO.getName(), tmpDesktopName, errorMsg}).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        try {
            rcaAppPoolAPI.updateAppPoolState(appPoolBaseDTO.getId(), RcaEnum.PoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新应用池[%s]信息失败", appPoolBaseDTO.getId()), e);
        }
        auditLogAPI.recordLog(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_SUCCESS, appPoolBaseDTO.getName());

        return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_RESULT);
    }

}
