package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.ModifyPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.MailMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ModifyPasswordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.UserSendMailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ResetUserPasswordBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.util.CharRandomUtils;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年1月10日
 *
 * @author Jarman
 */
public class ResetUserPasswordBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetUserPasswordBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private IacUserMgmtAPI cbbUserAPI;

    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    private Boolean passwordRandom;

    private String password;

    private boolean isBatch = true;

    private final ModifyPasswordAPI modifyPasswordAPI;

    private final MailMgmtAPI mailMgmtAPI;

    private UserInfoAPI userInfoAPI;


    /**
     * 角色是否是超级管理员
     */
    private boolean roleIsSuperAdmin;

    public ResetUserPasswordBatchTaskHandler(ResetUserPasswordBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.mailMgmtAPI = request.getMailMgmtAPI();
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbUserAPI = request.getCbbUserAPI();
        this.certificationStrategyParameterAPI = request.getCertificationStrategyParameterAPI();
        this.passwordRandom = request.getPasswordRandom();
        this.password = request.getPassword();
        this.modifyPasswordAPI = request.getModifyPasswordAPI();
        // 角色是否是超级管理员
        this.roleIsSuperAdmin = request.getRoleIsSuperAdmin();
        this.userInfoAPI = request.getUserInfoAPI();
    }

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        UUID userId = taskItem.getItemID();
        String userName = null;
        try {
            IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserDetail(userId);
            userName = cbbUserDetailDTO.getUserName();
            if (decryptNewPassword(password).equals(userName)) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_PASSWORD_EQUALS_USER_NAME);
            }
            // 如果用户已经升级为管理员 并且管理员没有超级管理员权限 抛出异常
            if (UserRoleEnum.ADMIN.name().equals(cbbUserDetailDTO.getUserRole()) && !roleIsSuperAdmin) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IS_ADMIN_NOT_ALLOW_RESET);
            }

            if (IacUserTypeEnum.THIRD_PARTY == cbbUserDetailDTO.getUserType()) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IS_THIRD_PARTY_NOT_ALLOW_RESET);
            }
            this.resetPassword(cbbUserDetailDTO, userId);
            userInfoAPI.sendUserPasswordChange(userId, null);

        } catch (BusinessException e) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            userName = Optional.ofNullable(userName).orElse(String.valueOf(userId));
            String key = e.getKey();
            if (UserBusinessKey.RCDC_RCO_USER_SEND_EMAIL_FAIL_LOG.equals(key)) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_BUT_SEND_EMAIL_FAIL_LOG, e, userName, exceptionMsg);
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_BUT_SEND_EMAIL_FAIL_LOG, e, userName, exceptionMsg);
            } else {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_FAIL_LOG, userName, exceptionMsg);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                        .msgKey(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_FAIL_LOG).msgArgs(userName, exceptionMsg).build();
            }
        }

        auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_SUCCESS_LOG, userName);
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_SUCCESS_LOG).msgArgs(userName).build();
    }

    private void resetPassword(IacUserDetailDTO cbbUserDetailDTO, UUID userId) throws BusinessException {
        String oldPassword = cbbUserDetailDTO.getPassword();
        String userName = cbbUserDetailDTO.getUserName();
        String newPassword = this.password;
        boolean isRandom = Boolean.TRUE.equals(this.passwordRandom);

        if (isRandom && StringUtils.isBlank(userName)) {
            LOGGER.error("重置用户：[{}] 密码失败，失败原因：姓名为空", userId.toString());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_NAME_NO_EXISTS);
        }
        if (isRandom && StringUtils.isEmpty(cbbUserDetailDTO.getEmail())) {
            LOGGER.error("重置用户：[{}] 密码失败，失败原因：邮件为空", userId.toString());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_EMAIL_NO_EXISTS);
        }

        try {
            // 增加随机生成密码功能
            // 密码为空并且邮箱不为空则随机密码并发送邮件,邮件服务器如果没有开启或配置异常也会终止当前用户用户密码发送
            newPassword = isRandom ? CharRandomUtils.passwordCreate(6) : this.decryptNewPassword(newPassword);
            oldPassword = this.decryptOldPassword(oldPassword);
            if (StringUtils.equals(newPassword, oldPassword) && UserRoleEnum.ADMIN.name().equals(cbbUserDetailDTO.getUserRole())) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_PASSWORD_IS_SAME);
            }

            ModifyPasswordDTO modifyPasswordDTO = buildModifyPasswordDTO(userId, oldPassword, newPassword);
            modifyPasswordAPI.resetUserPwdSyncAdminPwd(modifyPasswordDTO);
        } catch (BusinessException e) {
            LOGGER.error(String.format("重置用户：[%s]密码失败", userName), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error(String.format("重置用户：[%s]密码失败", userName), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_UPDATE_FAIL_LOG, e);
        }

        if (isRandom) {
            this.sendMail(cbbUserDetailDTO, newPassword);
        }
    }

    private void sendMail(IacUserDetailDTO cbbUserDetailDTO, String newPassword) throws BusinessException {
        try {
            UserSendMailDTO userSendMailDTO = new UserSendMailDTO();
            userSendMailDTO.setEmail(cbbUserDetailDTO.getEmail());
            userSendMailDTO.setUserName(cbbUserDetailDTO.getUserName());
            userSendMailDTO.setRealName(cbbUserDetailDTO.getRealName());
            userSendMailDTO.setContent(newPassword);
            mailMgmtAPI.sendMail(userSendMailDTO);
        } catch (BusinessException e) {
            LOGGER.error(String.format("发送用户[%s]邮件失败", cbbUserDetailDTO.getUserName()), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_SEND_EMAIL_FAIL_LOG, e, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量删除
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_RESULT);
        }

        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_SUCCESS_RESULT).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_FAIL_RESULT).build();
        }
    }

    private ModifyPasswordDTO buildModifyPasswordDTO(UUID userId, String oldPassword, String newPassword) {
        ModifyPasswordDTO modifyPasswordDTO = new ModifyPasswordDTO();
        modifyPasswordDTO.setId(userId);
        modifyPasswordDTO.setOldPwd(oldPassword);
        modifyPasswordDTO.setNewPwd(newPassword);
        // 重置用户密码时，直接调用身份中心modifyOtherAdminPwd方法，具体是否需要修改密码由身份中心控制
        modifyPasswordDTO.setNeedUpdatePassword(true);
        return modifyPasswordDTO;
    }

    private String decryptOldPassword(String oldPassword) throws BusinessException {
        try {
            return AesUtil.descrypt(oldPassword, RedLineUtil.getRealUserRedLine());
        } catch (Exception e) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_PASSWORD_DECRYPT_FAIL_LOG, e);
        }
    }

    private String decryptNewPassword(String newPassword) throws BusinessException {
        try {
            newPassword = AesUtil.descrypt(newPassword, RedLineUtil.getRealUserRedLine());
            return StringUtils.isEmpty(newPassword) ? Constants.INIT_USER_PASSWORD : newPassword;
        } catch (Exception e) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_PASSWORD_DECRYPT_FAIL_LOG, e);
        }
    }
}
