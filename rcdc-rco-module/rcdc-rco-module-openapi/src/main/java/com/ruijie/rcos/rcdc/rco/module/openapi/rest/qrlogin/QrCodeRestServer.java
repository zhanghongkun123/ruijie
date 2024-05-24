package com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr.QrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr.QrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.GetQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeClientReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeConfigReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrLoginReq;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

/**
 * Description: 二维码登录相关接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-21 15:11:00
 *
 * @author zjy
 */
@OpenAPI
@Path("/v1/qrCode")
public interface QrCodeRestServer {

    /**
     * 获取二维码
     *
     * @param getQrCodeRequest 二维码请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 15:03
     * @Author zjy
     **/
    @POST
    @Path("/create")
    QrCodeDTO getQrCode(GetQrCodeReq getQrCodeRequest) throws BusinessException;

    /**
     * 刷新二维码
     *
     * @param refreshQrCodeReq 二维码刷新参数
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/24 10:37
     * @Author zjy
     **/
    @POST
    @Path("/refresh")
    QrCodeDTO refreshQrCode(QrCodeClientReq refreshQrCodeReq) throws BusinessException;

    /**
     * 查询二维码
     *
     * @param queryQrCodeReq 二维码信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 15:33
     * @Author zjy
     **/
    @POST
    @Path("/detail")
    QrCodeDTO queryQrCode(QrCodeMobileReq queryQrCodeReq) throws BusinessException;

    /**
     * 扫描二维码
     *
     * @param qrCodeMobileReq 扫描信息
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 15:47
     * @Author zjy
     **/
    @PUT
    @Path("/scan")
    void scanQrCode(QrCodeMobileReq qrCodeMobileReq) throws BusinessException;

    /**
     * 确认登录
     *
     * @param qrLoginReq 确认登录信息
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 16:28
     * @Author zjy
     **/
    @PUT
    @Path("/confirmLogin")
    void confirmQrLogin(QrLoginReq qrLoginReq) throws BusinessException;

    /**
     * 取消登录
     *
     * @param qrCodeMobileReq 取消登录信息
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 16:28
     * @Author zjy
     **/
    @PUT
    @Path("/cancelLogin")
    void cancelQrLogin(QrCodeMobileReq qrCodeMobileReq) throws BusinessException;

    /**
     * 授权登录
     *
     * @param qrCodeClientReq 登录信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/24 10:48
     * @Author zjy
     **/
    @PUT
    @Path("/login")
    QrCodeDTO qrLogin(QrCodeClientReq qrCodeClientReq) throws BusinessException;

    /**
     * 查询二维码配置
     *
     * @param qrCodeType 二维码类型
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/24 14:27
     * @Author zjy
     **/
    @GET
    @Path("/config/{qrCodeType}")
    QrCodeConfigDTO getQrCodeConfig(@PathParam("qrCodeType") @NotNull CbbQrCodeType qrCodeType) throws BusinessException;

    /**
     * 修改二维码配置
     *
     * @param qrCodeConfigReq 二维码配置
     * @throws BusinessException 业务异常
     * @Date 2022/1/24 14:43
     * @Author zjy
     **/
    @POST
    @Path("/config")
    void updateQrCodeConfig(QrCodeConfigReq qrCodeConfigReq) throws BusinessException;
}
