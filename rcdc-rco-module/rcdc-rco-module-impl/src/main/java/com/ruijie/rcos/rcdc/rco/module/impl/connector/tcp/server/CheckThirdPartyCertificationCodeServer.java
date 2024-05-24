package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.rco.module.def.user.dto.CertificationResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.ThirdPartyCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月20日
 *
 * @author xwx
 */
@Tcp
public interface CheckThirdPartyCertificationCodeServer {

    /**
     * 校验第三方认证动态口令
     * @param terminalId 终端ID
     * @param certificationRequest 请求
     * @return 认证结果
     */
    @ApiAction(ShineAction.CHECK_THIRD_PARTY_CERTIFICATION_CODE)
    CertificationResultDTO checkThirdCertificationCode(@SessionAlias String terminalId, ThirdPartyCertificationRequest certificationRequest);
}
