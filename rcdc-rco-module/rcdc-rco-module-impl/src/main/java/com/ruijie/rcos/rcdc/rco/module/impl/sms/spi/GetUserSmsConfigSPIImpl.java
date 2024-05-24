package com.ruijie.rcos.rcdc.rco.module.impl.sms.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacSmsCertificationResponse;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.SmsAuthCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 获取用户短信认证配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/21
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.GET_USER_SMS_AUTH_CONFIG)
public class GetUserSmsConfigSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserSmsConfigSPIImpl.class);

    /**
     * 用户名
     */
    private static final String USER_NAME = "userName";
    
    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;
    
    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        Assert.hasText(request.getData(), "报文data不能为空");

        LOGGER.debug("接收到终端获取用户短信认证配置信息请求，请求参数为:[{}]", request.getData());
        JSONObject dataJson = JSONObject.parseObject(request.getData());
        String userName = dataJson.getString(USER_NAME);
        CbbResponseShineMessage<?> shineMessage;
        try {
            IacSmsCertificationResponse certificationResponse = smsCertificationAPI.getClientSmsCertificationStrategy(userName);
            shineMessage = ShineMessageUtil.buildResponseMessageWithContent(request, CommonMessageCode.SUCCESS, certificationResponse);
        } catch (BusinessException e) {
            LOGGER.error("终端{}获取用户{}短信认证信息失败：", request.getTerminalId(), userName, e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(request, SmsAuthCodeEnum.getSmsAuthCodeEnum(e.getKey()).getCode());
        } catch (Exception ex) {
            LOGGER.error("终端{}获取用户{}短信认证信息失败：", request.getTerminalId(), userName, ex);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(request, CommonMessageCode.CODE_ERR_OTHER);
        }
        messageHandlerAPI.response(shineMessage);
    }
}
