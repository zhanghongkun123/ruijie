package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.WebClientLoginInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.WatermarkConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.common.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.common.VirtualInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.*;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.CustomerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.AppDownloadRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.FtpConfigInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.TerminalDownloadResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.ThemeInfoResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: 通用功能
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-17 17:30:00
 *
 * @author zjy
 */
@OpenAPI
@Path("/v1/common")
public interface CommonServer {

    /**
     * 获取集群信息
     *
     * @Date 2022/1/18 13:49
     * @Author zjy
     * @return 返回值
     **/
    @GET
    @Path("/clusterInfo")
    ClusterInfoDTO getClusterInfo();

    /**
     * 获取虚拟信息
     *
     * @Date 2022/1/18 13:49
     * @Author zjy
     * @return 返回值
     **/
    @GET
    @Path("/virtualInfo")
    VirtualInfoDTO getVirtualInfo();

    /**
     * 获取客户信息
     *
     * @Date 2022/1/18 13:49
     * @Author zjy
     * @return 返回值
     **/
    @GET
    @Path("/customInfo")
    CustomerInfoDTO getCustomInfo();

    /**
     * 获取水印信息
     *
     * @Date 2022/1/18 13:49
     * @Author zjy
     * @return 返回值
     * @throws BusinessException 配置不存在
     **/
    @GET
    @Path("/watermarkInfo")
    WatermarkConfigDTO getWatermarkConfigInfo() throws BusinessException;

    /**
     * 获取安全配置开关配置
     *
     * @Date 2022-04-27
     * @Author zqj
     * @return 返回值
     * @throws BusinessException 业务异常
     **/
    @GET
    @Path("/certifiedSecurityConfig")
    CertifiedSecurityResponse queryCertifiedSecurityConfig() throws BusinessException;

    /**
     * 获取是否可以下载终端APP
     * @param request request
     * @return 终端下载信息
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/queryTerminalAppDownload")
    TerminalDownloadResponse queryTerminalAppDownload(AppDownloadRequest request) throws BusinessException;

    /**
     * 获取Ftp账号密码信息
     * @return Ftp账号密码信息
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/getFtpAccountInfo")
    FtpConfigInfoResponse getFtpAccountInfo() throws BusinessException;

    /**
     * 获取主题策略信息
     * @return 主题策略信息
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/getThemeInfo")
    ThemeInfoResponse getThemeInfo() throws BusinessException;

    /**
     * 获取登录页登录认证方式信息
     * @param request 请求
     * @return 登录页登录认证方式信息
     */
    @POST
    @Path("/getLoginPageInfo")
    LoginPageInfoResponse getLoginPageInfo(ClientLoginPageInfoRequest request);

    /**
     * 获取图形校验码
     * @param request 请求
     * @return 图形校验码
     */
    @POST
    @Path("/getLoginCaptcha")
    CaptchaDataResponse getCaptcha(ClientObtainCaptchaRequest request);

    /**
     * 客户端绑定加域桌面密码
     *
     * @param request 请求
     * @return 绑定结果返回
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/bindAdPassword")
    BindAdPasswordResponse bindAdPassword(ClientBindAdPasswordRequest request);

    /**
     * 用户授权码认证
     *
     * @param request    请求
     * @return 结果
     */
    @POST
    @Path("/authorizationCodeAuth")
    AuthCodeResponse authorizationCodeAuth(ClientObtainAuthRequest request);

    /**
     * 获取第三方认证配置信息
     *
     * @param request    请求
     * @return 结果
     */
    @POST
    @Path("/getThirdPartyAuthConfig")
    ObtainThirdPartAuthConfigResponse getThirdPartAuthConfig(ClientThirdPartAuthConfigRequest request);

    /**
     * 移动客户端扫码功能获取二维码信息
     * @param request request
     * @return 二维码信息
     */
    @POST
    @Path("/getQrCodeContent")
    ClientQrCodeResponse getQrCodeContent(MobileClientQrCodeRequest request);

    /**
     * 移动客户端扫码功能获取二维码状态
     * @param request request
     * @return 二维码状态信息
     */
    @POST
    @Path("/getQrCodeState")
    ClientQrCodeResponse getQrCodeState(MobileClientQueryQrStateRequest request);

    /**
     * 移动客户端扫码功能获取二维码配置
     * @param request request
     * @return 二维码配置信息
     */
    @POST
    @Path("/getQrCodeConfig")
    ClientQrCodeConfigRespnose getQrCodeConfig(MobileClientQrCodeRequest request);

    /**
     * 移动客户端扫码功能二维码登录
     * @param request request
     * @return 登录返回
     */
    @POST
    @Path("/qrCodeLogin")
    WebClientLoginInfoDTO qrCodeLogin(MobileClientQrLoginRequest request);
}
