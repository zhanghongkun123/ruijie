package com.ruijie.rcos.rcdc.rco.module.impl.sms.spi;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserUpdatePwdDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.sms.IacCheckSmsRestUserPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacUserUpdatePwdDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserUpdatePwdRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ChangeUserPwdCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 短信验证码重置密码
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/26
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.SMS_REST_USER_PASSWORD)
public class SmsRestUserPasswordSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsRestUserPasswordSPIImpl.class);

    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can be not null");
        String data = request.getData();
        Assert.hasText(data, "报文data不能为空");
        LOGGER.debug("接收到终端重置密码请求，请求参数为:[{}]", data);
        CbbResponseShineMessage<?> shineMessage;
        try {
            IacCheckSmsRestUserPwdRequest userPwdRequest = JSON.parseObject(data, IacCheckSmsRestUserPwdRequest.class);
            Assert.notNull(userPwdRequest, "userPwdRequest can be not null");
            IacUserUpdatePwdDTO iacUserUpdatePwdDTO = smsCertificationAPI.checkSmsRestUserPwd(userPwdRequest);
            if (iacUserUpdatePwdDTO.getBusinessCode() == ChangeUserPwdCode.SUCCESS) {
                // 修改密码
                IacUserDetailDTO userInfoDTO = cbbUserAPI.getUserByName(userPwdRequest.getUserName());
                UserUpdatePwdRequest pwdRequest = new UserUpdatePwdRequest();
                pwdRequest.setUserName(userPwdRequest.getUserName());
                pwdRequest.setNewPassword(userPwdRequest.getNewPassword());
                pwdRequest.setOldPassword(userInfoDTO.getPassword());
                pwdRequest.setApiCallerTypeEnum(ApiCallerTypeEnum.INNER);
                UserUpdatePwdDTO userUpdatePwdDTO = userMgmtAPI.updatePwd(pwdRequest);
                iacUserUpdatePwdDTO.setBusinessCode(userUpdatePwdDTO.getBusinessCode());
                iacUserUpdatePwdDTO.setPwdLockTime(userUpdatePwdDTO.getPwdLockTime());
                iacUserUpdatePwdDTO.setRemainingTimes(userUpdatePwdDTO.getRemainingTimes());
                smsCertificationAPI.invalidateToken(userPwdRequest.getUserName());
            }

            shineMessage = ShineMessageUtil.buildResponseMessageWithContent(request, iacUserUpdatePwdDTO.getBusinessCode(), iacUserUpdatePwdDTO);
        } catch (Exception ex) {
            LOGGER.error("终端{}重置密码请求失败：", request.getTerminalId(), ex);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(request, CommonMessageCode.CODE_ERR_OTHER);
        }
        messageHandlerAPI.response(shineMessage);
    }
}
