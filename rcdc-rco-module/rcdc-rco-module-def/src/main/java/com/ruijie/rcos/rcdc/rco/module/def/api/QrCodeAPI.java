package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr.QrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr.QrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.GetQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeClientReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeConfigReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrLoginReq;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 二维码相关API
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-21 15:14:00
 *
 * @author zjy
 */
public interface QrCodeAPI {


    /**
     * 获取二维码
     *
     * @param getQrCodeRequest 客户信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 15:19
     * @Author zjy
     **/
    QrCodeDTO getQrCode(GetQrCodeReq getQrCodeRequest) throws BusinessException;

    /**
     * 刷新二维码
     *
     * @param refreshQrCodeReq 刷新二维码信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/24 10:33
     * @Author zjy
     **/
    QrCodeDTO refreshQrCode(QrCodeClientReq refreshQrCodeReq) throws BusinessException;

    /**
     * 查询二维码状态
     *
     * @param queryQrCodeReq 二维码信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 15:30
     * @Author zjy
     **/
    QrCodeDTO queryQrCode(QrCodeMobileReq queryQrCodeReq) throws BusinessException;

    /**
     * 扫描二维码
     *
     * @param qrCodeMobileReq 扫描信息
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 15:30
     * @Author zjy
     **/
    void scanQrCode(QrCodeMobileReq qrCodeMobileReq) throws BusinessException;

    /**
     * 确认登录
     *
     * @param qrLoginReq 登录参数
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 15:52
     * @Author zjy
     **/
    void confirmQrLogin(QrLoginReq qrLoginReq) throws BusinessException;

    /**
     * 取消登录
     *
     * @param qrCodeMobileReq 登录参数
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 15:52
     * @Author zjy
     **/
    void cancelQrLogin(QrCodeMobileReq qrCodeMobileReq) throws BusinessException;

    /**
     * 授权登录
     *
     * @param qrCodeClientReq 登录参数
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/24 10:42
     * @Author zjy
     **/
    QrCodeDTO qrLogin(QrCodeClientReq qrCodeClientReq) throws BusinessException;

    /**
     * 获取二维码配置
     *
     * @param qrCodeType 二维码类型
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/24 14:26
     * @Author zjy
     **/
    QrCodeConfigDTO getQrCodeConfig(CbbQrCodeType qrCodeType) throws BusinessException;

    /**
     * 修改二维码配置
     *
     * @param qrCodeConfigReq 二维码开关配置
     * @throws BusinessException 业务异常
     * @Date 2022/1/24 14:32
     * @Author zjy
     **/
    void updateQrCodeConfig(QrCodeConfigReq qrCodeConfigReq) throws BusinessException;
}
