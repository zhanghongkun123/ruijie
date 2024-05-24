package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 批处理云桌面的个性化配置策略的变更
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/18
 *
 * @author zwf
 */
public class DesktopPoolUpdateUserProfileStrategyHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolUpdateUserProfileStrategyHandler.class);

    private UUID userProfileStrategyId;

    private String poolName;

    private BaseAuditLogAPI auditLogAPI;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    public DesktopPoolUpdateUserProfileStrategyHandler(Iterator<DefaultBatchTaskItem> iterator, UserDesktopMgmtAPI userDesktopMgmtAPI,
                                                       CbbDeskMgmtAPI cbbDeskMgmtAPI) {
        super(iterator);
        this.cbbDeskMgmtAPI = cbbDeskMgmtAPI;
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public void setUserProfileStrategyId(UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "Param [batchTaskItem] must not be null");
        UUID desktopId = batchTaskItem.getItemID();
        String tempDesktopName = desktopId.toString();
        try {
            CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopId);
            tempDesktopName = cbbDeskDTO.getName();
            userDesktopMgmtAPI.updateDesktopUserProfileStrategy(desktopId, userProfileStrategyId);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_USER_PROFILE_EDIT_TASK_LOG_SUC, poolName, tempDesktopName);
            return DefaultBatchTaskItemResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_USER_PROFILE_EDIT_TASK_LOG_SUC)
                    .msgArgs(poolName, tempDesktopName).batchTaskItemStatus(BatchTaskItemStatus.SUCCESS).build();
        } catch (BusinessException e) {
            String msg = e.getI18nMessage();
            LOGGER.error(String.format("桌面池[%s]的云桌面[%s]编辑UPM配置策略失败", poolName, tempDesktopName), e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_USER_PROFILE_EDIT_TASK_LOG_FAIL, poolName, tempDesktopName, msg);
            return DefaultBatchTaskItemResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_USER_PROFILE_EDIT_TASK_LOG_FAIL)
                    .msgArgs(poolName, tempDesktopName, msg).batchTaskItemStatus(BatchTaskItemStatus.FAILURE).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        return buildDefaultFinishResult(sucCount, failCount, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_USER_PROFILE_EDIT_BATCH_RESULT);
    }
}
