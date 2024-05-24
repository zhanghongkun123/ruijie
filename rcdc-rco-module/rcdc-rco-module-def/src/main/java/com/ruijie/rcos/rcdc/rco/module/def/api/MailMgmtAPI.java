package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.ServerMailConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.UserSendMailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.WindowsPwsResetMailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 邮件管理接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-12-26
 *
 * @author liusd
 */
public interface MailMgmtAPI {
    /**
     *  用户邮件发送
     * @param userSendMailDTO 用户邮件发送对象
     * @throws BusinessException 业务异常
     */
    void sendMail(UserSendMailDTO userSendMailDTO) throws BusinessException;

    /**
     * 保存配置前测试使用
     * @param serverMailConfigDTO 邮件服务器配置
     * @throws BusinessException  业务异常
     */
    void testMailConfig(ServerMailConfigDTO serverMailConfigDTO) throws BusinessException;

    /**
     * 发送重置windows密码邮件通知
     * @description: 发送重置windows密码邮件通知
     * @param windowsPwsResetMail  通知内容
     * @throws BusinessException 业务异常
     * @author zjy
     * @date: 2023/8/28 13:48
     */
    void sendResetWindowsPwdMail(WindowsPwsResetMailDTO windowsPwsResetMail) throws BusinessException;
}
