package com.ruijie.rcos.rcdc.rco.module.impl.mail;

/**
 * 邮件相关key
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/26
 *
 * @author liusd
 */
public interface MailBusinessKey {

    /**
     * 邮箱服务器配置错误信息
     */
    String RCDC_RCO_MAIL_CONFIG_DISABLED = "23200638";
    String RCDC_RCO_MAIL_SERVER_ADDRESS_NULL = "23200639";
    String RCDC_RCO_MAIL_SENDER_NULL = "23200640";
    String RCDC_RCO_MAIL_CONTENT_PARSE_ERROR = "23200641";
    String RCDC_RCO_MAIL_SMTP_CLOSED_OR_AUTH_FAIL = "23200642";
    String RCDC_RCO_MAIL_SERVER_DISCONNECTED = "23200643";
    String RCDC_RCO_MAIL_SEND_FORBIDDEN = "23200644";
    String RCDC_RCO_MAIL_SERVER_ERROR = "23200645";

}
