package com.ruijie.rcos.rcdc.rco.module.impl.uws.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbConfirmQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbGetQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQueryQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月16日
 *
 * @author xgx
 */
public interface UwsQrCodeService {
    /**
     * 是否支持该类型
     * 
     * @param qrCodeType 二维码类型
     * @return true/false
     */
    boolean isSupport(CbbQrCodeType qrCodeType);

    /**
     * 获取二维码
     *
     * @param qrCodeReqDTO qrCodeReqDTO
     * @return 二维码唯一标识
     * @throws BusinessException 业务异常
     */
    CbbQrCodeDTO getQrCode(CbbGetQrCodeReqDTO qrCodeReqDTO) throws BusinessException;

    /**
     * 刷新二维码
     *
     * @param qrCodeReqDTO qrCodeReqDTO
     * @return 新的二维码
     * @throws BusinessException 业务异常
     */
    CbbQrCodeDTO refreshQrCode(CbbQrCodeReqDTO qrCodeReqDTO) throws BusinessException;

    /**
     * 查询二维码信息
     *
     * @param queryQrCodeReqDTO queryQrCodeReqDTO
     * @return 二维码信息
     * @throws BusinessException 业务异常
     */
    CbbQrCodeDTO queryQrCode(CbbQueryQrCodeReqDTO queryQrCodeReqDTO) throws BusinessException;

    /**
     * 登录
     * 
     * @param qrCodeReqDTO qrCodeReqDTO
     * @return 二维码信息
     * @throws BusinessException 业务异常
     */
    CbbQrCodeDTO qrLogin(CbbQrCodeReqDTO qrCodeReqDTO) throws BusinessException;



    /**
     * 扫码
     *
     * @param qrCodeMobileReq qrCodeMobileReq
     * @throws BusinessException 业务异常
     */
    default void scanQrCode(CbbQrCodeMobileReqDTO qrCodeMobileReq) throws BusinessException {

    }

    /**
     * 确认登录
     *
     * @param confirmQrCodeMobileReqDTO confirmQrCodeMobileReqDTO
     * @throws BusinessException 业务异常
     */
    default void confirmQrLogin(CbbConfirmQrCodeMobileReqDTO confirmQrCodeMobileReqDTO) throws BusinessException {

    }

    /**
     * 取消登录
     *
     * @param qrCodeMobileReq qrCodeMobileReq
     * @throws BusinessException 业务异常
     */
    default void cancelQrLogin(CbbQrCodeMobileReqDTO qrCodeMobileReq) throws BusinessException {

    }

    /**
     * 保存二维码配置
     * 
     * @param qrCodeConfigReqDTO qrCodeConfigReqDTO
     * @throws BusinessException 业务异常
     */
    void saveQrCodeConfig(CbbQrCodeConfigDTO qrCodeConfigReqDTO) throws BusinessException;

    /**
     * 获取二维码配置
     * 
     * @return 二维码配置
     * @throws BusinessException 业务异常
     */
    CbbQrCodeConfigDTO getQrCodeConfig() throws BusinessException;

}
