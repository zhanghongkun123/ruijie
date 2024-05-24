package com.ruijie.rcos.rcdc.rco.module.impl.sms.spi;

import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.sms.IacSmsCreateRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacSmsSendResponse;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.SmsAuthCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.sms.response.SmsSendResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 发送短信验证码
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/26
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.SEND_SMS)
public class SendSmsVerifyCodeSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendSmsVerifyCodeSPIImpl.class);
    
    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        String data = request.getData();
        Assert.hasText(data, "报文data不能为空");
        LOGGER.debug("接收到终端请求发送短信，请求参数为:[{}]", data);

        IacSmsCreateRequest smsCreateRequest = JSON.parseObject(data, IacSmsCreateRequest.class);
        CbbResponseShineMessage<?> shineMessage;
        try {
            IacSmsSendResponse smsSendResponse = smsCertificationAPI.sendSmsVerifyCode(smsCreateRequest);
            shineMessage = ShineMessageUtil.buildResponseMessageWithContent(request, CommonMessageCode.SUCCESS, smsSendResponse);
        } catch (BusinessException e) {
            LOGGER.error("终端[{}]请求发送短信验证码出现异常：", request.getTerminalId(), e);
            SmsAuthCodeEnum smsAuthCodeEnum = SmsAuthCodeEnum.getSmsAuthCodeEnum(e.getKey());
            if (smsAuthCodeEnum == SmsAuthCodeEnum.SMS_SEND_FAIL) {
                SmsSendResponse response = new SmsSendResponse();
                response.setErrorCode(UserTipUtil.resolveBusizExceptionMsg(e));
                shineMessage = ShineMessageUtil.buildResponseMessageWithContent(request, smsAuthCodeEnum.getCode(), response);
            } else {
                shineMessage = ShineMessageUtil.buildErrorResponseMessage(request, smsAuthCodeEnum.getCode());
            }
        } catch (Exception ex) {
            LOGGER.error("终端{}请求发送短信验证码失败：", request.getTerminalId(), ex);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(request, CommonMessageCode.CODE_ERR_OTHER);
        }
        messageHandlerAPI.response(shineMessage);
    }
    
}
