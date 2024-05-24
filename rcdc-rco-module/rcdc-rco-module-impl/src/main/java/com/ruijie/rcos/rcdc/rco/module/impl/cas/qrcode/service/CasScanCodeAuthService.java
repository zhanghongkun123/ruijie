package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service;

import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;

/**
 * Description: CAS认证服务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
public interface CasScanCodeAuthService {

    /**
     * 获取CAS认证信息
     *
     * @return CasScanCodeAuthDTO CAS认证信息
     */
    CasScanCodeAuthDTO getCasScanCodeAuthInfo();

    /**
     * 修改CAS认证信息
     *
     * @param casScanCodeAuthDTO  CAS认证信息
     */
    void updateCasScanCodeAuthInfo(CasScanCodeAuthDTO casScanCodeAuthDTO);
}
