package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrLoginReq;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.response.MobileClientQrCodeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: 移动客户端扫码登录
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 19:38
 *
 * @author wanglianyun
 */
@Tcp
public interface QrCodeServer {

    /**
     * 用户扫码
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 认证结果
     */
    @ApiAction(ShineAction.MOBILE_CLIENT_USER_SCAN_QR_CODE)
    MobileClientQrCodeResponse scanQrCode(@SessionAlias String terminalId, ClientQrCodeMobileReq request);

    /**
     * 用户确认登录
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 认证结果
     */
    @ApiAction(ShineAction.MOBILE_CLIENT_USER_CONFIRM_LOGIN)
    MobileClientQrCodeResponse confirmLogin(@SessionAlias String terminalId, ClientQrLoginReq request);

    /**
     * 取消登录
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 认证结果
     */
    @ApiAction(ShineAction.MOBILE_CLIENT_USER_CANCEL_QR_CODE)
    MobileClientQrCodeResponse cancelQrCode(@SessionAlias String terminalId, ClientQrCodeMobileReq request);

}
