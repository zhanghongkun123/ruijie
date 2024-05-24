package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;

/**
 *
 * Description: 动态口令认证策略API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月17日
 *
 * @author lihengjing
 */
public interface OtpCertificationAPI {

    /**
     * 获取动态口令认证策略
     * 
     * @return 动态口令认证策略
     */
    OtpCertificationDTO getOtpCertification();

    /**
     * 编辑动态口令认证策略
     * 
     * @param otpDTO 动态口令认证策略
     */
    void updateOtpCertification(OtpCertificationDTO otpDTO);
}
