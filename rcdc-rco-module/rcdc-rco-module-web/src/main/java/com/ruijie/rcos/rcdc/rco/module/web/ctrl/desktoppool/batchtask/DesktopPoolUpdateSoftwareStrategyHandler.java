package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 桌面池编辑软件管控策略批量处理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-06
 *
 * @author linke
 */
public class DesktopPoolUpdateSoftwareStrategyHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolUpdateSoftwareStrategyHandler.class);

    private UUID softwareStrategyId;

    private String poolName;

    private BaseAuditLogAPI auditLogAPI;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    public DesktopPoolUpdateSoftwareStrategyHandler(Iterator<DefaultBatchTaskItem> iterator, UUID softwareStrategyId, String poolName) {
        super(iterator);
        this.softwareStrategyId = softwareStrategyId;
        this.poolName = poolName;
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.cbbDeskMgmtAPI = SpringBeanHelper.getBean(CbbDeskMgmtAPI.class);
        this.userDesktopMgmtAPI = SpringBeanHelper.getBean(UserDesktopMgmtAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "Param [batchTaskItem] must not be null");
        UUID desktopId = batchTaskItem.getItemID();
        String tempDesktopName = desktopId.toString();
        try {
            CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopId);
            tempDesktopName = cbbDeskDTO.getName();
            userDesktopMgmtAPI.updateDesktopSoftwareStrategy(desktopId, softwareStrategyId);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SOFT_EDIT_TASK_LOG_SUC, poolName, tempDesktopName);
            return DefaultBatchTaskItemResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SOFT_EDIT_TASK_LOG_SUC)
                    .msgArgs(poolName, tempDesktopName).batchTaskItemStatus(BatchTaskItemStatus.SUCCESS).build();
        } catch (Exception e) {
            String msg;
            if (e instanceof BusinessException) {
                msg = ((BusinessException) e).getI18nMessage();
            } else {
                msg = e.getMessage();
            }
            LOGGER.error(String.format("桌面池[%s]的云桌面[%s]编辑软件管控策略失败", poolName, tempDesktopName), e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SOFT_EDIT_TASK_LOG_FAIL, poolName, tempDesktopName, msg);
            return DefaultBatchTaskItemResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SOFT_EDIT_TASK_LOG_FAIL)
                    .msgArgs(poolName, tempDesktopName, msg).batchTaskItemStatus(BatchTaskItemStatus.FAILURE).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        return buildDefaultFinishResult(sucCount, failCount, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SOFT_EDIT_BATCH_RESULT);
    }
}
