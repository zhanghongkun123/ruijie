package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.UUID;

import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EnableUserPresetBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
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
 * Description: 批量启用用户任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/1/17
 *
 * @author yangjinheng
 */
public class EnableUserPresetBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnableUserPresetBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private IacUserMgmtAPI cbbUserAPI;

    private UserMgmtAPI userMgmtAPI;

    private boolean isBatch = false;

    private String threadUserName;

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    public EnableUserPresetBatchTaskHandler(EnableUserPresetBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbUserAPI = request.getCbbUserAPI();
        this.userMgmtAPI = SpringBeanHelper.getBean(UserMgmtAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "taskItem can not be null");

        UUID userId = batchTaskItem.getItemID();
        IacUserDetailDTO cbbUserDetailDTO = null;
        String userName;

        try {
            cbbUserDetailDTO = cbbUserAPI.getUserDetail(userId);
            userName = cbbUserDetailDTO.getUserName();
            threadUserName = cbbUserDetailDTO.getUserName();
            // 非本地用户不支持启用
            if (cbbUserDetailDTO.getUserType() == IacUserTypeEnum.AD ||
                    cbbUserDetailDTO.getUserType() == IacUserTypeEnum.LDAP ||
                    cbbUserDetailDTO.getUserType() == IacUserTypeEnum.THIRD_PARTY) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_ENABLE_FAIL_BY_USER_TYPE);
            }
            // 启用用户
            if (cbbUserDetailDTO.getUserState() != IacUserStateEnum.ENABLE) {
                final IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
                BeanUtils.copyProperties(cbbUserDetailDTO, cbbUpdateUserDTO);
                cbbUpdateUserDTO.setUserState(IacUserStateEnum.ENABLE);
                cbbUserAPI.updateUser(cbbUpdateUserDTO);

                //同步用户信息给IDV/TCI终端
                cbbUserDetailDTO.setUserState(IacUserStateEnum.ENABLE);
                userMgmtAPI.syncUserInfoToTerminal(cbbUserDetailDTO);

                // 记录审计日志
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_SUC_LOG, userName);
            }

            // 返回成功
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_SUC_LOG).msgArgs(userName).build();
        } catch (BusinessException e) {
            LOGGER.error("启用用户失败", e);
            userName = cbbUserDetailDTO == null ? userId.toString() : cbbUserDetailDTO.getUserName();
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_FAIL_LOG, userName, exceptionMsg);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_FAIL_LOG,e,userName, exceptionMsg);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_RESULT);
        }

        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_SUCCESS_RESULT).msgArgs(new String[]{threadUserName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_FAIL_RESULT).msgArgs(new String[]{threadUserName}).build();
        }
    }
}
