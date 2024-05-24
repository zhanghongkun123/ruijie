package com.ruijie.rcos.rcdc.rco.module.impl.sms.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.sms.IacSmsCodeVerifyRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacSmsCodeVerifyResponse;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.SmsAuthCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 短信验证码校验
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/26
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.VERIFY_SMS_CODE)
public class SmsVerifyCodeCheckSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsVerifyCodeCheckSPIImpl.class);

    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UserLoginRecordService userLoginRecordService;
    
    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        String data = request.getData();
        Assert.hasText(data, "报文data不能为空");
        LOGGER.debug("接收到终端校验短信验证码请求，请求参数为:{}", data);
        IacSmsCodeVerifyRequest verifyRequest = JSON.parseObject(data, IacSmsCodeVerifyRequest.class);
        CbbResponseShineMessage<?> shineMessage;
        try {
            IacSmsCodeVerifyResponse response = smsCertificationAPI.authSmsVerifyCode(verifyRequest);
            shineMessage = ShineMessageUtil.buildResponseMessageWithContent(request, response.getBusinessCode(), response);
        } catch (BusinessException e) {
            LOGGER.error("终端[{}]请求校验短信验证码出现异常：", request.getTerminalId(), e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(request, SmsAuthCodeEnum.getSmsAuthCodeEnum(e.getKey()).getCode());
        } catch (Exception ex) {
            LOGGER.error("终端[{}]请求校验短信验证码出现异常：", request.getTerminalId(), ex);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(request, CommonMessageCode.CODE_ERR_OTHER);
        }
        userLoginRecordService.saveUserAuthInfo(request.getTerminalId(), verifyRequest.getUserName());

        messageHandlerAPI.response(shineMessage);
    }
}
