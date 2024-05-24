package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.license.module.def.dto.CbbAuthInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.GeneralAuthDetailInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.LicenseActiveDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.LicenseActiveResultDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;

/**
 * 授权处理
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月18日
 * 
 * @author lin
 */
public interface LicenseService {

    /**
     * 
     * @param licenseActiveDTO licenseActiveDTO
     * @return LicenseActiveResultDTO
     * @throws BusinessException ex
     */
    LicenseActiveResultDTO matchWindowsLicense(LicenseActiveDTO licenseActiveDTO);
    
    /**
     * 成功激活
     */
    void activeSuccess();


    /**
     * @return 获取所有证书种类的使用情况
     */
    List<CbbAuthInfoDTO> getLicenseUsageSnapshot();


    /**
     * @param licenseType 授权种类
     * @return 获取该证书种类的使用情况
     */
    List<CbbAuthInfoDTO> getLicenseUsageSnapshot(String licenseType);

    /**
     * @param licenseType 授权种类
     * @return 获取该证书种类的使用情况
     */
    List<GeneralAuthDetailInfoDTO> getLicenseUsageDetail(String licenseType);

}
