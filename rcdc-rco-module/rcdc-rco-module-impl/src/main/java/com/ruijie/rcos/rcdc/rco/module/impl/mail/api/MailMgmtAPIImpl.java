package com.ruijie.rcos.rcdc.rco.module.impl.mail.api;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.ClusterVirtualIpDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.MailMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.MailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.ServerMailConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.UserSendMailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.WindowsPwsResetMailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.mail.MailBusinessKey;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


/**
 * Description: 邮件管理接口实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-12-26
 *
 * @author liusd
 */
public class MailMgmtAPIImpl implements MailMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailMgmtAPIImpl.class);

    private static final String MAIL_HTML_PATH = "template/mail.html";

    private static final String LOGO_PATH = "template/logo.png";

    private static final String TEMP_FILE_PATH = "img.png";

    private static final String LOGO_CID = "logo";

    private static final String USER_NAME = "userName";

    private static final String REAL_NAME = "realName";

    private static final String PASSWORD = "password";

    private static final String SUBJECT = "初始账号密码";

    private static final String WINDOWS_RESET_PWD_MAIL_HTML_PATH = "template/windows_reset_pwd_mail.html";

    private static final String WINDOWS_USER_NAME = "windowsUserName";

    private static final String DESKTOP_NAME = "desktopName";

    private static final String WINDOWS_PWD = "password";

    private static final String TIMEOUT_KYE = "mail.smtp.connectiontimeout";

    private static final String TIMEOUT = "5000";

    @Autowired
    private CloudPlatformMgmtAPI cloudPlatformMgmtAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Override
    public void sendMail(UserSendMailDTO userSendMailDTO) throws BusinessException {
        Assert.notNull(userSendMailDTO, "userIdList must not null");
        String html;
        try {
            html = new String(FileCopyUtils.copyToByteArray(new ClassPathResource(MAIL_HTML_PATH).getInputStream()),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("解析邮件格式失败,MAIL_HTML_PATH=[{}]", MAIL_HTML_PATH, e);
            throw new BusinessException(MailBusinessKey.RCDC_RCO_MAIL_CONTENT_PARSE_ERROR, e);
        }
        html = html.replace(USER_NAME, userSendMailDTO.getUserName()).replace(REAL_NAME, userSendMailDTO.getRealName())
                .replace(PASSWORD, userSendMailDTO.getContent());
        MailDTO mailDTO = new MailDTO(userSendMailDTO.getEmail(), SUBJECT, html);
        mailDTO.setImgPath(LOGO_PATH);
        mailDTO.setImgCid(LOGO_CID);
        sendEmail(null, mailDTO);
    }

    @Override
    public void testMailConfig(ServerMailConfigDTO serverMailConfigDTO) throws BusinessException {
        Assert.notNull(serverMailConfigDTO, "serverMailConfigDTO must not null");
        UserSendMailDTO userSendMailDTO = new UserSendMailDTO();
        userSendMailDTO.setUserName(USER_NAME);
        userSendMailDTO.setContent(PASSWORD);
        userSendMailDTO.setRealName(REAL_NAME);

        String message = this.getTestEmailHtml(userSendMailDTO);
        MailDTO mailDTO = new MailDTO(serverMailConfigDTO.getFromMailAccount(), "初始账号和密码", message);

        sendEmail(serverMailConfigDTO, mailDTO);
    }

    @Override
    public void sendResetWindowsPwdMail(WindowsPwsResetMailDTO windowsPwsResetMail) throws BusinessException {
        Assert.notNull(windowsPwsResetMail, "userIdList must not null");

        String html = "";
        try {
            html = new String(FileCopyUtils.copyToByteArray(new ClassPathResource(WINDOWS_RESET_PWD_MAIL_HTML_PATH)
                    .getInputStream()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("解析邮件格式失败,MAIL_HTML_PATH=[{}]", WINDOWS_RESET_PWD_MAIL_HTML_PATH, e);
            throw new BusinessException(MailBusinessKey.RCDC_RCO_MAIL_CONTENT_PARSE_ERROR, e);
        }

        html = html.replace(WINDOWS_USER_NAME, windowsPwsResetMail.getWindowsUserName())
                .replace(DESKTOP_NAME, windowsPwsResetMail.getDesktopName())
                .replace(WINDOWS_PWD, windowsPwsResetMail.getPassword());
        MailDTO mailDTO = new MailDTO(windowsPwsResetMail.getEmail(), WindowsPwsResetMailDTO.SUBJECT, html);
        mailDTO.setImgPath(LOGO_PATH);
        mailDTO.setImgCid(LOGO_CID);
        sendEmail(null, mailDTO);
    }

    /**
     * 发送邮件
     * @param mailDTO 发送内容
     * @param serverMailConfigDTO 服务器配置
     * @throws BusinessException 异常
     */
    private void sendEmail(ServerMailConfigDTO serverMailConfigDTO, MailDTO mailDTO) throws BusinessException {
        Assert.notNull(mailDTO, "mailDTO must not be null");
        if (serverMailConfigDTO == null) {
            FindParameterRequest findParameterRequest = new FindParameterRequest(Constants.SERVER_MAIL_CONFIG);
            FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(findParameterRequest);
            serverMailConfigDTO = JSONObject.parseObject(findParameterResponse.getValue(), ServerMailConfigDTO.class);
        }

        try {
            validateMailConfig(serverMailConfigDTO);
            JavaMailSenderImpl javaMailSender = this.getJavaMailSender(serverMailConfigDTO);
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");

            messageHelper.setFrom(serverMailConfigDTO.getFromMailAccount());
            messageHelper.setTo(mailDTO.getToMailAccount());
            messageHelper.setSubject(mailDTO.getSubject());
            messageHelper.setText(mailDTO.getMessage(), true);
            if (!Strings.isNullOrEmpty(mailDTO.getImgPath())) {
                File file = new File(TEMP_FILE_PATH);
                FileCopyUtils.copy(FileCopyUtils.copyToByteArray(new ClassPathResource(mailDTO.getImgPath()).getInputStream()), file);
                messageHelper.addInline(mailDTO.getImgCid(), file);
            }
            Properties properties = new Properties();
            properties.setProperty(TIMEOUT_KYE, TIMEOUT);
            javaMailSender.setJavaMailProperties(properties);
            javaMailSender.testConnection();

            javaMailSender.send(mailMessage);
            LOGGER.info("发送邮件成功，发送邮箱为：{}", new Object[]{serverMailConfigDTO.getFromMailAccount()});
        } catch (Exception e) {
            this.resolveMailException(e, serverMailConfigDTO);
        }
    }

    /**
     *
     * @param serverMailConfigDTO 邮箱配置
     * @throws BusinessException 异常
     */
    public void validateMailConfig(ServerMailConfigDTO serverMailConfigDTO) throws BusinessException {
        Assert.notNull(serverMailConfigDTO, "serverMailConfigDTO must not be null");

        if (serverMailConfigDTO.getEnableSendMail() == null || !serverMailConfigDTO.getEnableSendMail()) {
            throw new BusinessException(MailBusinessKey.RCDC_RCO_MAIL_CONFIG_DISABLED);
        }
        if (serverMailConfigDTO.getServerAddress() == null) {
            throw new BusinessException(MailBusinessKey.RCDC_RCO_MAIL_SERVER_ADDRESS_NULL);
        }
        if (serverMailConfigDTO.getFromMailAccount() == null) {
            throw new BusinessException(MailBusinessKey.RCDC_RCO_MAIL_SENDER_NULL);
        }
    }

    /**
     * 获取发送邮件html  测试发送
     * @param userSendMailDTO 发送内容
     * @return 邮件内容
     */
    private String getTestEmailHtml(UserSendMailDTO userSendMailDTO) {
        Assert.notNull(userSendMailDTO, "userSendMailDTO can not be null");
        StringBuilder contentStringBuilder = new StringBuilder();
        String username = String.format("<p>账号: %s</p>", userSendMailDTO.getUserName());
        String password = String.format("<p>密码: %s</p>", userSendMailDTO.getContent());
        String clusterVirtualIp = null;
        try {
            DtoResponse<ClusterVirtualIpDTO> clusterVirtualIpResponse = cloudPlatformMgmtAPI.getClusterVirtualIp(new DefaultRequest());
            clusterVirtualIp = clusterVirtualIpResponse.getDto().getClusterVirtualIpIp();
        } catch (Exception e) {
            LOGGER.error("获取VIP异常", e);
        }

        String vip = String.format("<p>VIP: %s</p>", clusterVirtualIp);
        contentStringBuilder.append(username).append(password);
        if (clusterVirtualIp != null) {
            String download = "<p>客户端下载链接:</p>";
            contentStringBuilder.append(vip).append(download);
            String downloadWindowsLink = String.format("<a href=\"https://%s:8443/rcdc/rco/terminal/app/windows/download\" " +
                            "style=\"text-decoration:none\">&nbsp;&nbsp;1. windows客户端</a></br>",
                    clusterVirtualIp);
            contentStringBuilder.append(downloadWindowsLink);
        }
        return contentStringBuilder.toString();
    }

    private JavaMailSenderImpl getJavaMailSender(ServerMailConfigDTO serverMailConfigDTO) throws BusinessException {
        String loginName = "";
        String pwd = "";
        if (StringUtils.isNotBlank(serverMailConfigDTO.getLoginName())
            && StringUtils.isNotBlank(serverMailConfigDTO.getLoginPassword())) {
            String decryptPwd = serverMailConfigDTO.getLoginPassword();
            if (!decryptPwd.equals("")) {
                try {
                    decryptPwd = AesUtil.descrypt(decryptPwd, RedLineUtil.getRealAdminRedLine());
                } catch (Exception e) {
                    LOGGER.error("解密用户：{0}密码失败，失败原因：", e, serverMailConfigDTO.getLoginName());
                    throw new BusinessException(BusinessKey.RCDC_RCO_DECRYPT_FAIL, e);
                }
            }
            loginName = serverMailConfigDTO.getLoginName();
            pwd = decryptPwd;
        }
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(serverMailConfigDTO.getServerAddress());
        javaMailSender.setUsername(loginName);
        javaMailSender.setPassword(pwd);
        if (serverMailConfigDTO.getServerPort() != null) {
            javaMailSender.setPort(serverMailConfigDTO.getServerPort().intValue());
        }
        javaMailSender.setDefaultEncoding("utf-8");
        return javaMailSender;
    }

    private void resolveMailException(Throwable e, ServerMailConfigDTO serverMailConfigDTO) throws BusinessException {
        if (e instanceof AuthenticationFailedException) {
            LOGGER.error("发送邮件失败，失败原因：发送方邮箱未开通SMTP服务或用户名密码错误。发送邮箱为：{}", serverMailConfigDTO.getFromMailAccount(), e);
            throw new BusinessException(MailBusinessKey.RCDC_RCO_MAIL_SMTP_CLOSED_OR_AUTH_FAIL, e);
        } else if (e instanceof MessagingException) {
            LOGGER.error("发送邮件失败，失败原因：配置的邮件服务器[{}]无法连通。", serverMailConfigDTO.getServerAddress(), e);
            throw new BusinessException(MailBusinessKey.RCDC_RCO_MAIL_SERVER_DISCONNECTED, e);
        } else if (e instanceof MailSendException) {
            LOGGER.error("发送邮件失败，失败原因：配置的邮件发送方[{}]在邮件服务器[{}]无发送邮件权限。",
                    serverMailConfigDTO.getFromMailAccount(), serverMailConfigDTO.getServerAddress(), e);
            throw new BusinessException(MailBusinessKey.RCDC_RCO_MAIL_SEND_FORBIDDEN, e);
        } else {
            LOGGER.error("发送邮件失败，发送邮箱为：{}", serverMailConfigDTO.getFromMailAccount(), e);
            throw new BusinessException(MailBusinessKey.RCDC_RCO_MAIL_SERVER_ERROR, e);
        }
    }
}
