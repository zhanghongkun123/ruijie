package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * *恢复故障云桌面异步批处理类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年6月14日
 * 
 * @author zhuangchenwu
 */
public class RecoverDesktopFromErrorBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverDesktopFromErrorBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private final UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    private final CloudDesktopWebService cloudDesktopWebService;

    private String deskName;

    public RecoverDesktopFromErrorBatchTaskHandler(UserDesktopMgmtAPI cloudDesktopMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                                   BaseAuditLogAPI auditLogAPI, CloudDesktopWebService cloudDesktopWebService) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        this.cloudDesktopWebService = cloudDesktopWebService;
    }

    public RecoverDesktopFromErrorBatchTaskHandler(UserDesktopMgmtAPI cloudDesktopMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                                   BaseAuditLogAPI auditLogAPI, String deskName, CloudDesktopWebService cloudDesktopWebService) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        this.deskName = deskName;
        this.cloudDesktopWebService = cloudDesktopWebService;
    }

    /**
     * set
     * @param cloudDesktopOperateAPI cloudDesktopOperateAPI
     */
    public void setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopOperateAPI, "cloudDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (deskName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SINGLE_RESULT_SUC)
                        .msgArgs(new String[] {deskName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SINGLE_RESULT_FAIL)
                        .msgArgs(new String[] {deskName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_BATCH_RESULT);
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        return recover(taskItem.getItemID());
    }

    private DefaultBatchTaskItemResult recover(UUID id) throws BusinessException {
        String logName;
        if (deskName != null) {
            logName = deskName;
        } else {
            logName = obtainDeskName(id);
        }
        try {
            cloudDesktopWebService.checkThirdPartyDesktop(id, UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_THIRD_PARTY);
            cloudDesktopOperateAPI.failback(new IdRequest(id));
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SUC_LOG, logName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_ITEM_SUC_DESC).msgArgs(new String[] {logName}).build();
        } catch (BusinessException e) {
            LOGGER.error("恢复故障云桌面失败，云桌面id=" + id.toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_FAIL_LOG, logName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_ITEM_FAIL_DESC, e, logName, e.getI18nMessage());
        }
    }

    private String obtainDeskName(UUID deskId) {
        String resultName = deskId.toString();
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
            resultName = cloudDesktopDetailDTO.getDesktopName();
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面数据失败，桌面id[{}]", deskId, e);
        }
        return resultName;
    }
}
