package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.ClientLoginPageInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ClientObtainDomainSsoConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ClientThirdPartAuthConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ClientBindAdPasswordRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ClientObtainAuthRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ClientObtainCaptchaRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;


/**
 * Description: shine和OneClient关于身份中心认证相关接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:52
 *
 * @author wanglianyun
 */
@Tcp
public interface IacAuthTcpServer {

    /**
     * 授权码认证
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 结果
     */
    @ApiAction(ShineAction.AUTHORIZATION_CODE_AUTH)
    AuthCodeResponse authorizationCodeAuth(@SessionAlias String terminalId, ClientObtainAuthRequest request);

    /**
     * 获取验证码
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 结果
     */
    @ApiAction(ShineAction.GET_CAPTCHA)
    CaptchaDataResponse getCaptcha(@SessionAlias String terminalId, ClientObtainCaptchaRequest request);

    /**
     * 查询支持的登录认证方式
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 结果
     */
    @ApiAction(ShineAction.GET_LOGIN_PAGE_INFO)
    LoginPageInfoResponse getLoginPageInfo(@SessionAlias String terminalId, ClientLoginPageInfoRequest request);

    /**
     * 客户端绑定加域桌面密码
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 结果
     */
    @ApiAction(ShineAction.BIND_AD_PASSWORD)
    BindAdPasswordResponse bindAdPassword(@SessionAlias String terminalId, ClientBindAdPasswordRequest request);

    /**
     * 查询桌面sso配置接口
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 结果
     */
    @ApiAction(ShineAction.GET_DOMAIN_SSO_CONFIG)
    BindDomainPasswordResponse getDomainSsoConfig(@SessionAlias String terminalId, ClientObtainDomainSsoConfigRequest request);

    /**
     * 获取第三方认证配置信息
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 结果
     */
    @ApiAction(ShineAction.GET_THIRD_PARTY_AUTH_CONFIG)
    ObtainThirdPartAuthConfigResponse getThirdPartAuthConfig(@SessionAlias String terminalId, ClientThirdPartAuthConfigRequest request);
}
