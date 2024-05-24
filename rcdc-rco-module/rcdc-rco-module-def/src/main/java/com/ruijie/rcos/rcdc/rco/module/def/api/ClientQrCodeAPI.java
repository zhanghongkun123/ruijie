package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientGetQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrLoginReq;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 移动客户端扫码API
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 12:48
 *
 * @author wanglianyun
 */
public interface ClientQrCodeAPI {

    /**
     * 获取二维码
     *
     * @param clientGetQrCodeReq 参数
     * @return 返回值
     * @throws BusinessException 业务异常
     **/
    ClientQrCodeDTO getQrCode(ClientGetQrCodeReq clientGetQrCodeReq) throws BusinessException;

    /**
     * 刷新二维码
     *
     * @param clientQrCodeReq 参数
     * @return 返回值
     * @throws BusinessException 业务异常
     **/
    ClientQrCodeDTO refreshQrCode(ClientQrCodeReq clientQrCodeReq) throws BusinessException;

    /**
     * 查询二维码状态
     *
     * @param clientQrCodeMobileReq 参数
     * @return 返回值
     * @throws BusinessException 业务异常
     **/
    ClientQrCodeDTO queryQrCode(ClientQrCodeMobileReq clientQrCodeMobileReq) throws BusinessException;

    /**
     * 扫描二维码
     *
     * @param clientQrCodeMobileReq 参数
     * @throws BusinessException 业务异常
     **/
    void scanQrCode(ClientQrCodeMobileReq clientQrCodeMobileReq) throws BusinessException;

    /**
     * 确认登录
     *
     * @param clientQrLoginReq 参数
     * @throws BusinessException 业务异常
     **/
    void confirmQrLogin(ClientQrLoginReq clientQrLoginReq) throws BusinessException;

    /**
     * 取消登录
     *
     * @param clientQrCodeMobileReq 参数
     * @throws BusinessException 业务异常
     **/
    void cancelQrLogin(ClientQrCodeMobileReq clientQrCodeMobileReq) throws BusinessException;

    /**
     * 授权登录
     *
     * @param clientQrCodeReq 参数
     * @return 返回值
     * @throws BusinessException 业务异常
     **/
    ClientQrCodeDTO qrLogin(ClientQrCodeReq clientQrCodeReq) throws BusinessException;

    /**
     * 获取二维码配置
     * @param terminalId 参数
     * @return 返回值
     * @throws BusinessException 业务异常
     **/
    ClientQrCodeConfigDTO getQrCodeConfig(String terminalId) throws BusinessException;

    /**
     * 修改二维码配置
     *
     * @throws BusinessException 业务异常
     **/
    void updateQrCodeConfig() throws BusinessException;

}
