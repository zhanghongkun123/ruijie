package com.ruijie.rcos.rcdc.rco.module.openapi.rest.notify;

import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.request.BaseThirdPartyGetUserRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.request.BaseThirdPartyWrapAuthRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.response.BaseThirdPartyWrapAuthResponse;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

/**
 * Description: 第三方用户自定义业务处理接口 (后面mq支持持久化后，考虑切换成mq)
 * @Deprecated 身份中心实现后，可以移除该接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月31日
 *
 * @author jarman
 */
@OpenAPI
@Path("/v1/user/third")
@Deprecated
public interface ThirdUserNotifyServer {


    /**
     * 包装第三方认证请求信息
     *
     * @param wrapAuthRequest 请求信息
     * @return 包装后的请求参数
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/wrapAuthRequest")
    BaseThirdPartyWrapAuthResponse wrapAuthRequest(@NotNull BaseThirdPartyWrapAuthRequest wrapAuthRequest) throws BusinessException;


    /**
     * 获取用户列表
     *
     * @param getUserRequest 获取用户请求
     * @return Map<String, List<ThirdPartyUserDTO>> 用户组及用户列表
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/userList")
    Map<String, List<BaseThirdPartyUserDTO>> getUserList(BaseThirdPartyGetUserRequest getUserRequest) throws BusinessException;

}
