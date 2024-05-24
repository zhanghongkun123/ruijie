package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CasScanCodeAuthParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums.CasQrCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Description: 获取CAS扫码配置信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/28
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.CAS_QUERY_QR_CONFIG)
public class GetCasQrCodeConfigInfoSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCasQrCodeConfigInfoSPIImpl.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CasScanCodeAuthParameterAPI scanCodeAuthParameterAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        CasScanCodeAuthDTO casScanCodeAuthInfo = scanCodeAuthParameterAPI.getCasScanCodeAuthInfo();
        if (ObjectUtils.isEmpty(casScanCodeAuthInfo)) {
            LOGGER.error("CAS扫码认证信息不存在,终端查询失败");
            response(request, CasQrCodeEnum.FAIL_CODE.getCode(), null);
            return;
        }
        response(request, CasQrCodeEnum.SUCCESS.getCode(), casScanCodeAuthInfo);
    }

    private void response(CbbDispatcherRequest request, Integer code, CasScanCodeAuthDTO casScanCodeAuthInfo) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, casScanCodeAuthInfo);
        } catch (Exception e) {
            LOGGER.error("终端{}获取CAS扫码信息失败，e={}", request.getTerminalId(), e);
        }
    }
}
