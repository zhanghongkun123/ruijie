package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.GetUserOtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoShineUserLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserOtpConfigDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: shine请求查询用户动态口令配置信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月21日
 *
 * @author lihengjing
 */
@DispatcherImplemetion(ShineAction.GET_USER_OTP_CONFIG)
public class GetUserOtpConfigSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserOtpConfigSPIImpl.class);

    /**
     * 用户名
     */
    private static final String USER_NAME = "userName";

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserOtpCertificationAPI userOtpCertificationAPI;

    @Autowired
    private RcoShineUserLoginService rcoShineUserLoginService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        Assert.hasText(request.getData(), "报文data不能为空");

        LOGGER.info("接收到终端获取用户动态口令配置信息请求，请求参数为:[{}]", request.getData());
        JSONObject dataJson = JSONObject.parseObject(request.getData());
        String userName = dataJson.getString(USER_NAME);

        GetUserOtpConfigDTO getUserOtpConfigDTO = new GetUserOtpConfigDTO();
        getUserOtpConfigDTO.setUserName(userName);
        UserOtpConfigDTO userOtpConfig = rcoShineUserLoginService.getUserOtpConfig(request.getTerminalId(), getUserOtpConfigDTO);
        if (userOtpConfig.getCode() == CommonMessageCode.SUCCESS) {
            response(request, CommonMessageCode.SUCCESS, userOtpConfig);
        } else {
            response(request, userOtpConfig.getCode(), null);
        }
    }


    private void response(CbbDispatcherRequest request, Integer code, UserOtpConfigDTO userOtpConfigDTO) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, userOtpConfigDTO);
        } catch (Exception e) {
            LOGGER.error("终端{}获取用户信息失败，e={}", request.getTerminalId(), e);
        }
    }
}
