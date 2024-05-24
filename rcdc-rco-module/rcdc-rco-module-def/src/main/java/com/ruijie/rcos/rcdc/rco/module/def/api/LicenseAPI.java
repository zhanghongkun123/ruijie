package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.LicenseTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ObtainRcdcLicenseNumResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainEduLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.license.response.ObtainIdvLicenseAuthStateResponse;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.response.ObtainCpuLicenseInfoResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/26
 *
 * @author nt
 */
public interface LicenseAPI {

    /**
     * 获取rcdc授权数
     * 
     * @param licenseType 请求参数
     * @return 请求响应
     */
    ObtainRcdcLicenseNumResponse acquireLicenseNum(LicenseTypeEnum licenseType);

    /**
     * 获取是否开启KMS服务
     * 
     * @return 请求响应
     */
    Boolean obtainEnableOpenKms();

    /**
     * 获取服务器CPU证书的到期时间
     * 
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    ObtainCpuLicenseInfoResponse obtainLicenseTrialRemainder() throws BusinessException;

    /**
     * 获取教育版CPU证书 与教育版TCI证书
     *
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    ObtainEduLicenseInfoResponse obtainEduLicense() throws BusinessException;

    /**
     * 获取idv授权情况
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    ObtainIdvLicenseAuthStateResponse obtainIdvLicenseAuthState() throws BusinessException;
}
