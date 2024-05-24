package com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeConfigDTO;

/**
 * Description: 扫码登录相关功能
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年3月9日
 *
 * @author zjy
 */
public interface QrCodeService {

    /**
     * 通知配置信息发生变更
     *
     * @param cbbQrCodeConfigDTO 扫码登录配置
     * @Date 2022/3/9 18:17
     * @Author zhengjingyong
     **/
    void notifyConfigUpdateToTerminal(CbbQrCodeConfigDTO cbbQrCodeConfigDTO);

    /**
     * 移动客户端扫码功能-通知配置信息发生变更
     *
     * @param cbbQrCodeConfigDTO 扫码登录配置
     **/
    void notifyQrConfigUpdateToTerminal(IacQrCodeConfigDTO cbbQrCodeConfigDTO);

}
