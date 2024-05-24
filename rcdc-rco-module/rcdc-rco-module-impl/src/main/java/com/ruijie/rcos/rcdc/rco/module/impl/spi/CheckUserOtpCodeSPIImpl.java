package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.CheckUserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.CheckUserOtpCodeResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.CheckUserOtpCodeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.constant.OtpConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoShineUserLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: shine请求校验用户动态口令
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月21日
 *
 * @author lihengjing
 */
@DispatcherImplemetion(ShineAction.CHECK_USER_OTP_CODE)
public class CheckUserOtpCodeSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckUserOtpCodeSPIImpl.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    public static final String RESULT = "result";

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Autowired
    private RcoShineUserLoginService rcoShineUserLoginService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        Assert.hasText(request.getData(), "报文data不能为空");

        LOGGER.info("接收到终端校验用户动态口令请求，请求data参数为:{}", request.getData());
        String terminalId = request.getTerminalId();
        UserOtpCodeDTO userOtpCodeDto = JSONObject.parseObject(request.getData(), UserOtpCodeDTO.class);
        JSONObject resultJson = new JSONObject();
        CheckUserOtpCodeResponse checkUserOtpCodeResponse = new CheckUserOtpCodeResponse();

        if (userOtpCodeDto == null) {
            LOGGER.info("终端[{}]校验用户动态口令请求为空", terminalId);
            response(request, CommonMessageCode.CODE_ERR_OTHER, checkUserOtpCodeResponse);
            return;
        }

        checkUserOtpCodeResponse = rcoShineUserLoginService.checkUserOtpCode(terminalId, userOtpCodeDto);
        response(request, checkUserOtpCodeResponse.getCode(), checkUserOtpCodeResponse);
    }

    private void response(CbbDispatcherRequest request, Integer code, CheckUserOtpCodeResponse response) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, response);
        } catch (Exception e) {
            LOGGER.error("终端{}获取用户信息失败，e={}", request.getTerminalId(), e);
        }
    }
}
