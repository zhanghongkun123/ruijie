package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: 创建用户消息批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年1月10日
 *
 * @author nt
 */
public class DeleteUserMessageBatchTaskHandler extends AbstractBatchTaskHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserMessageBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserMessageAPI userMessageAPI;

    public DeleteUserMessageBatchTaskHandler(UserMessageAPI userMessageAPI, Iterator<? extends BatchTaskItem> iterator,
                                             BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.auditLogAPI = auditLogAPI;
        this.userMessageAPI = userMessageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        return deleteAddOptLog(taskItem);

    }

    private BatchTaskItemResult deleteAddOptLog(BatchTaskItem taskItem) throws BusinessException {
        String messageTitle = taskItem.getItemID().toString();
        try {
            IdRequest idRequest = new IdRequest(taskItem.getItemID());
            UserMessageDTO userMessageDTO = userMessageAPI.getById(idRequest);
            messageTitle = userMessageDTO.getTitle();
            userMessageAPI.deleteUserMessage(idRequest);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_SUCCESS_LOG,
                    messageTitle);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_RESULT_SUCCESS)
                    .msgArgs(new String[] {messageTitle}).build();
        } catch (BusinessException e) {
            LOGGER.error("delete user message fail", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_FAIL_LOG,
                    messageTitle, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_RESULT_FAIL, e, messageTitle, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_USER_DELETE_USER_MESSAGE_RESULT);
    }

}
