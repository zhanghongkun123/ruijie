package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月08日
 *
 * @author zhanghongkun
 */
@DispatcherImplemetion(ShineAction.GET_GLOBAL_PARAMETER)
public class GetGlobalParameterSPIImpl implements CbbDispatcherHandlerSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetGlobalParameterSPIImpl.class);

    @Autowired
    OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "request is null");

        // 动态口令相关全局信息
        OtpCertificationDTO otpCertification = otpCertificationAPI.getOtpCertification();
        if (otpCertification == null) {
            LOGGER.info("查询全局配置信息失败[{}]", cbbDispatcherRequest.getTerminalId());
            response(cbbDispatcherRequest, CommonMessageCode.CODE_ERR_OTHER, new Object());
            return;
        }

        // IDV和VOI终端需要进一步判断用户是否开启
        if (BooleanUtils.isTrue(otpCertification.getOpenOtp())) {
            try {
                TerminalDTO terminalDto = userTerminalMgmtAPI.getTerminalById(cbbDispatcherRequest.getTerminalId());
                if (terminalDto != null) {
                    if (terminalDto.getPlatform() == CbbTerminalPlatformEnums.VOI || terminalDto.getPlatform() == CbbTerminalPlatformEnums.IDV) {
                        LOGGER.info("用户[{}]动态口令状态[{}]", terminalDto.getBindUserId(), terminalDto.getOpenOtpCertification());
                        otpCertification.setOpenOtp(BooleanUtils.isTrue(terminalDto.getOpenOtpCertification()));

                        if (BooleanUtils.isTrue(otpCertification.getHasOtpCodeTab())) {
                            LOGGER.info("用户[{}]动态口令绑定[{}]", terminalDto.getBindUserId(), terminalDto.getHasBindOtp());
                            otpCertification.setHasOtpCodeTab(BooleanUtils.isTrue(terminalDto.getHasBindOtp()));
                        }
                    }
                }
                response(cbbDispatcherRequest, CommonMessageCode.SUCCESS, otpCertification);
                return;
            } catch (BusinessException e) {
                LOGGER.info("查询用终端绑定用户动态口令信息失败[{}]", cbbDispatcherRequest.getTerminalId());
                response(cbbDispatcherRequest, CommonMessageCode.CODE_ERR_OTHER, new Object());
                return;
            }
        }
        response(cbbDispatcherRequest, CommonMessageCode.SUCCESS, otpCertification);
    }


    private void response(CbbDispatcherRequest request, Integer code, Object content) {
        LOGGER.info("登录之前获取全局配置信息：terminalId={}, object={}", request.getTerminalId(), JSONObject.toJSONString(content));
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, content);
        } catch (Exception e) {
            LOGGER.error("终端{}获取全局配置信息失败，e={}", request.getTerminalId(), e);
        }
    }


}
