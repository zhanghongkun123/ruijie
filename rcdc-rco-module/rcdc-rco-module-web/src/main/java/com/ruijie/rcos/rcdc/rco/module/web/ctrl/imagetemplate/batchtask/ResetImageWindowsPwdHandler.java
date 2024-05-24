package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbResetWindowsPwdAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbResetWindowsPwdRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.GuestResponseResetWinPwdEnums;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request.ResetImageWindowsPwdHandlerRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.UUID;

/**
 * Description: 重置windows密码处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/31 18:29
 *
 * @author zjy
 */
public class ResetImageWindowsPwdHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetImageWindowsPwdHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbResetWindowsPwdAPI cbbResetWindowsPwdAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private UUID imageTemplateId;

    private String account;

    private String password;

    private String imageName = "";

    private String errorMessage;

    public ResetImageWindowsPwdHandler(ResetImageWindowsPwdHandlerRequest resetImageWindowsPwdHandlerRequest) {
        super(resetImageWindowsPwdHandlerRequest.getBatchTaskItemIterator());
        this.auditLogAPI = resetImageWindowsPwdHandlerRequest.getAuditLogAPI();
        this.cbbResetWindowsPwdAPI = resetImageWindowsPwdHandlerRequest.getCbbResetWindowsPwdAPI();
        this.cbbImageTemplateMgmtAPI = resetImageWindowsPwdHandlerRequest.getCbbImageTemplateMgmtAPI();
        this.imageTemplateId = resetImageWindowsPwdHandlerRequest.getImageTemplateId();
        this.account = resetImageWindowsPwdHandlerRequest.getAccount();
        this.password = resetImageWindowsPwdHandlerRequest.getPassword();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(imageTemplateId);
        this.imageName = cbbImageTemplateDTO.getImageTemplateName();
        CbbResetWindowsPwdRequest resetDeskWindowsPwdRequest = new CbbResetWindowsPwdRequest();
        resetDeskWindowsPwdRequest.setTemplateImageId(imageTemplateId);
        resetDeskWindowsPwdRequest.setAccount(account);
        resetDeskWindowsPwdRequest.setNewPwd(password);

        try {
            Integer code = cbbResetWindowsPwdAPI.resetWindowsPwdToImage(resetDeskWindowsPwdRequest);
            LOGGER.info("GT返回的resetWinPwd请求结果为：[{}]", code);
            if (GuestResponseResetWinPwdEnums.SUCCESS.getValue() == code) {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_SUCCESS_LOG,
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE),
                        cbbImageTemplateDTO.getImageTemplateName(), account);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_SUCCESS_LOG).msgArgs(
                                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE),
                                cbbImageTemplateDTO.getImageTemplateName(), account).build();
            } else {
                String key = CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_FAIL_OTHER_ERROR;
                if (GuestResponseResetWinPwdEnums.ACCOUNT_NOT_EXIST.getValue() == code) {
                    key = CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_FAIL_ACCOUNT_ERROR;
                } else if (GuestResponseResetWinPwdEnums.INVALID_PASSWORD.getValue() == code) {
                    key = CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_FAIL_PASSWORD_ERROR;
                } else if (GuestResponseResetWinPwdEnums.PASSWORD_TOO_SHORT.getValue() == code) {
                    key = CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_FAIL_PASSWORD_TOO_SHORT;
                }
                String message = LocaleI18nResolver.resolve(key);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_FAIL_LOG,
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE),
                        cbbImageTemplateDTO.getImageTemplateName(), account,
                        message);
                this.errorMessage = message;
                throw new BusinessException(key);
            }
        } catch (BusinessException e) {
            this.errorMessage = e.getI18nMessage();
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_FAIL_LOG,
                    LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE),
                    cbbImageTemplateDTO.getImageTemplateName(), account, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_FAIL_LOG, e,
                    LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE),
                    cbbImageTemplateDTO.getImageTemplateName(), account, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_SUCCESS).msgArgs(
                            new String[]{LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE),
                                imageName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_FAIL).msgArgs(
                            new String[]{LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE),
                                imageName}).build();
        }
    }
}
