package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.AuthCodeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.BindAdPasswordRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ObtainCaptchaRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.*;
import com.ruijie.rcos.rcdc.rco.module.def.enums.AuthType;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 用户认证授权对接iac管理API
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:18
 *
 * @author wanglianyun
 */
public interface UserAuthMgmtAPI {

    /**
     * 查询支持的登录认证方式
     *
     * @param deviceId 请求
     * @return 登录界面信息
     * @throws BusinessException 业务异常
     */
    LoginPageInfoResponse getLoginPageInfo(String deviceId) throws BusinessException;

    /**
     * 获取图形校验码
     * @param request 请求
     * @return 图形校验码
     */
    CaptchaDataResponse getCaptcha(ObtainCaptchaRequest request);

    /**
     * 客户端绑定加域桌面密码
     *
     * @param request 请求
     * @return 绑定结果返回
     */
    BindAdPasswordResponse bindAdPassword(BindAdPasswordRequest request);

    /**
     * 用户扫码认证
     *
     * @param request 请求
     * @return 认证结果返回
     * @throws BusinessException 业务异常
     */
    AuthCodeResponse authorizationCodeAuth(AuthCodeRequest request) throws BusinessException;

    /**
     * 查询桌面sso配置接口
     *
     * @return 结果返回
     */
    BindDomainPasswordResponse getDomainSsoConfig();

    /**
     * 获取第三方认证配置信息
     *
     * @param request 请求
     * @return 第三方配置信息返回
     */
    ObtainThirdPartAuthConfigResponse getThirdPartAuthConfig(AuthType request);

}
