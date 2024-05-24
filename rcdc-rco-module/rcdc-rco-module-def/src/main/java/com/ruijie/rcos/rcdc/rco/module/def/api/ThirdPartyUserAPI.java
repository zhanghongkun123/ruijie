package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.Map;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.request.BaseThirdPartyGetUserRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.request.BaseThirdPartyWrapAuthRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.response.BaseThirdPartyWrapAuthResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月31日
 *
 * @author jarman
 */
public interface ThirdPartyUserAPI {


    /**
     * 包装第三方认证请求信息
     *
     * @param wrapAuthRequest 请求信息
     * @return 包装后的请求参数
     * @throws BusinessException 业务异常
     */
    BaseThirdPartyWrapAuthResponse wrapAuthRequest(BaseThirdPartyWrapAuthRequest wrapAuthRequest) throws BusinessException;

    /**
     * 获取用户列表
     *
     * @param getUserRequest 获取用户请求
     * @return Map<String, List<ThirdPartyUserDTO>> 用户组及用户列表
     * @throws BusinessException 业务异常
     */
    Map<String, List<BaseThirdPartyUserDTO>> getUserList(BaseThirdPartyGetUserRequest getUserRequest) throws BusinessException;
}
