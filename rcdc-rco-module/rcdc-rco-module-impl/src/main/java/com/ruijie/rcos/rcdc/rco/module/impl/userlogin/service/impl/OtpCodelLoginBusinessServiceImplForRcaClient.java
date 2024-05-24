package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import org.springframework.stereotype.Service;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 动态口令登录(不校验硬件特征码)实现
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/4/24 10:14 上午
 *
 * @author zhouhuan
 */
@Service("otpCodeLoginTemplateServiceForRcaClient")
public class OtpCodelLoginBusinessServiceImplForRcaClient extends OtpCodelLoginBusinessServiceImpl {

    @Override
    public String getKey() {
        return ShineAction.RCA_CLIENT_USER_OTP_CODE_LOGIN;
    }

    protected void validateHardwareCertification(String terminalId, RcoViewUserEntity userEntity, IacAuthUserResultDTO authUserResponse)
        throws BusinessException {

    }
}
