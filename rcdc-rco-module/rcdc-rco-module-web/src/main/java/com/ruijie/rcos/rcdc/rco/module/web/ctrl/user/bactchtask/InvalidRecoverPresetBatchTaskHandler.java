package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.InvalidRecoverPresetBatchTaskHandlerRequest;
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

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29 19:24
 *
 * @author liuwang1
 */
public class InvalidRecoverPresetBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidRecoverPresetBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private IacUserMgmtAPI cbbUserAPI;

    private String threadUserName;

    public InvalidRecoverPresetBatchTaskHandler(InvalidRecoverPresetBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbUserAPI = request.getCbbUserAPI();
        this.threadUserName = request.getThreadUserName();
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "taskItem can not be null");

        UUID userId = batchTaskItem.getItemID();

        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserDetail(userId);
        if (ObjectUtils.isEmpty(cbbUserDetailDTO)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_NOT_EXIT);
        }

        IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
        BeanUtils.copyProperties(cbbUserDetailDTO, cbbUpdateUserDTO);
        String userName = cbbUserDetailDTO.getUserName();

        cbbUpdateUserDTO.setInvalidRecoverTime(new Date());
        cbbUpdateUserDTO.setInvalid(false);
        cbbUserAPI.updateUser(cbbUpdateUserDTO);

        auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_PRESET_SUCCESS, userName);

        // 返回成功
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_PRESET_SUCCESS).msgArgs(userName).build();

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount + failCount > 1) {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_PRESET_RESULT);
        }

        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_PRESET_SUCCESS_RESULT).msgArgs(new String[]{threadUserName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_PRESET_FAIL_RESULT).msgArgs(new String[]{threadUserName}).build();
        }
    }
}
