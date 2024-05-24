package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service;

import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.AuthTokenDTO;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanOutDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 对接外部CAS扫码接口
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/24
 *
 * @author TDabstract
 */
public interface CasExternalScanCodeService {

    /**
     * HTTPS客户端初始化失败
     */
    String RCDC_HTTPS_CLIENT_INITIAL_FAIL = "23200604";

    /**
     * 鉴权服务
     * @param codeAuthDTO 鉴权参数
     * @return 鉴权TOKEN-DTO
     * @throws BusinessException 未知异常
     */
    AuthTokenDTO getAuthTokenService(CasScanCodeAuthDTO codeAuthDTO) throws BusinessException;

    /**
     * 获取二维码服务
     * 
     * @return CAS扫码通用DTO
     * @throws BusinessException 未知异常
     */
    CasScanOutDTO getQrCodeService() throws BusinessException;

    /**
     * 监控扫码结果服务
     * 
     * @param qrCodeId 二维码ID
     * @return CAS扫码通用DTO
     * @throws BusinessException 未知异常
     */
    CasScanOutDTO monitorScanResultService(String qrCodeId) throws BusinessException;

    /**
     * 票据验证服务
     * 
     * @param ticket 票据
     * @param serviceId 二维码ID
     * @return 票据验证服务返回
     * @throws BusinessException 未知异常
     */
    String ticketValidateService(String ticket, String serviceId) throws BusinessException;
}
