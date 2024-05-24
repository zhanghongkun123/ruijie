package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.AuthTokenDTO;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: CAS认证API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
public interface CasScanCodeAuthParameterAPI {

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

    /**
     * 测试CAS服务的连通性
     * @param casScanCodeAuthDTO 鉴权参数
     * @return AuthTokenDTO 鉴权后的信息：token
     * @throws BusinessException 连接异常
     */
    AuthTokenDTO testCasServiceConnectivity(CasScanCodeAuthDTO casScanCodeAuthDTO) throws BusinessException;
}
