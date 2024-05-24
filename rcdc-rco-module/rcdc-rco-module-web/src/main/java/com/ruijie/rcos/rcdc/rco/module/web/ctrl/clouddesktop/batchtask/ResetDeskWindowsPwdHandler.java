package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbResetWindowsPwdAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbResetWindowsPwdRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.GuestResponseResetWinPwdEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.MailMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.ServerMailConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.WindowsPwsResetMailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desktop.ResetDeskWindowsPwdHandlerRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Description: 重置桌面windows密码处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/31 18:29
 *
 * @author zjy
 */
public class ResetDeskWindowsPwdHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetDeskWindowsPwdHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbResetWindowsPwdAPI cbbResetWindowsPwdAPI;

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    private MailMgmtAPI mailMgmtAPI;

    private UUID deskId;

    private String account;

    private String password;

    private String email;

    private String desktName = "";

    private String errorMessage;

    private ThreadExecutor threadExecutor;

    public ResetDeskWindowsPwdHandler(ResetDeskWindowsPwdHandlerRequest resetDeskWindowsPwdHandlerRequest) {
        super(resetDeskWindowsPwdHandlerRequest.getBatchTaskItemIterator());
        this.auditLogAPI = resetDeskWindowsPwdHandlerRequest.getAuditLogAPI();
        this.cbbResetWindowsPwdAPI = resetDeskWindowsPwdHandlerRequest.getCbbResetWindowsPwdAPI();
        this.cloudDesktopMgmtAPI = resetDeskWindowsPwdHandlerRequest.getCloudDesktopMgmtAPI();
        this.mailMgmtAPI = resetDeskWindowsPwdHandlerRequest.getMailMgmtAPI();
        this.rcoGlobalParameterAPI = resetDeskWindowsPwdHandlerRequest.getRcoGlobalParameterAPI();
        this.deskId = resetDeskWindowsPwdHandlerRequest.getDeskId();
        this.account = resetDeskWindowsPwdHandlerRequest.getAccount();
        this.password = resetDeskWindowsPwdHandlerRequest.getPassword();
        this.email = resetDeskWindowsPwdHandlerRequest.getEmail();
        this.threadExecutor = resetDeskWindowsPwdHandlerRequest.getThreadExecutor();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        CloudDesktopDetailDTO desktopDetail = cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
        this.desktName = desktopDetail.getDesktopName();
        CbbResetWindowsPwdRequest resetDeskWindowsPwdRequest = new CbbResetWindowsPwdRequest();
        resetDeskWindowsPwdRequest.setDeskId(deskId);
        resetDeskWindowsPwdRequest.setAccount(account);
        resetDeskWindowsPwdRequest.setNewPwd(password);

        try {
            Integer code = cbbResetWindowsPwdAPI.resetWindowsPwdToDesk(resetDeskWindowsPwdRequest);
            LOGGER.info("GT返回的resetWinPwd请求结果为：[{}]", code);
            if (GuestResponseResetWinPwdEnums.SUCCESS.getValue() == code) {
                sendEmail(resetDeskWindowsPwdRequest, email, desktopDetail.getDesktopName());
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_SUCCESS_LOG,
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK),
                        desktopDetail.getDesktopName(), account);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_SUCCESS_LOG).msgArgs(
                                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK),
                                desktopDetail.getDesktopName(), account).build();
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
                this.errorMessage = message;
                throw new BusinessException(key);
            }
        } catch (BusinessException e) {
            this.errorMessage = e.getI18nMessage();
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_FAIL_LOG, e,
                    LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK),
                    desktopDetail.getDesktopName(), account, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_FAIL_LOG, e,
                    LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK),
                    desktopDetail.getDesktopName(), account, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_SUCCESS).msgArgs(
                            new String[]{LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK),
                                this.desktName}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_FAIL).msgArgs(
                            new String[]{LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK),
                                this.desktName}).build();
        }
    }

    private void sendEmail(CbbResetWindowsPwdRequest resetWindowsPwdWebRequest,
                           String email, String desktopName) throws BusinessException {
        String serverMailConfig = "";
        try {
            serverMailConfig = rcoGlobalParameterAPI.findParameter(new FindParameterRequest(Constants.SERVER_MAIL_CONFIG)).getValue();
        } catch (Exception ex) {
            LOGGER.error("获取邮箱配置发生异常，ex : ", ex);
            return;
        }

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(serverMailConfig)) {
            LOGGER.info("用户未配置邮箱或全局邮箱服务器配置为空");
            return;
        }

        ServerMailConfigDTO serverMailConfigDTO = JSONObject.parseObject(serverMailConfig, ServerMailConfigDTO.class);
        if (serverMailConfigDTO == null || Boolean.FALSE.equals(serverMailConfigDTO.getEnableSendMail())) {
            LOGGER.info("全局邮箱服务器配置为空或未开启配置开关");
            return;
        }

        LOGGER.info("开始发送window密码更新成功邮件");
        WindowsPwsResetMailDTO windowsPwsResetMailDTO = new WindowsPwsResetMailDTO();
        windowsPwsResetMailDTO.setEmail(email);
        windowsPwsResetMailDTO.setDesktopName(desktopName);
        windowsPwsResetMailDTO.setWindowsUserName(resetWindowsPwdWebRequest.getAccount());
        windowsPwsResetMailDTO.setPassword(resetWindowsPwdWebRequest.getNewPwd());

        threadExecutor.execute(() -> {
            try {
                mailMgmtAPI.sendResetWindowsPwdMail(windowsPwsResetMailDTO);
            } catch (BusinessException ex) {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_SEND_EMAIL_ERROR, ex,
                        desktopName, ex.getI18nMessage());
            }
        });
    }
}
