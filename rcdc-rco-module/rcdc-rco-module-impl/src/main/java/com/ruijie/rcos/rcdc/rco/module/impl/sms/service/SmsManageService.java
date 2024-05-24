package com.ruijie.rcos.rcdc.rco.module.impl.sms.service;

import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsGatewayConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.SmsSendRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.adaptor.SmsPlatformTypeSupport;
import com.ruijie.rcos.sk.base.exception.BusinessException;


/**
 * Description: 短信管理服务
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/8
 *
 * @author TD
 */
public interface SmsManageService {

    /**
     * 发送短信消息
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    void sendSmsMessage(SmsSendRequest request) throws BusinessException;

    /**
     * 通知在线终端
     *
     * @param pwdRecoverDTO pwdRecoverDTO
     */
    void notifyOnlineClient(SmsPwdRecoverDTO pwdRecoverDTO);
}
