package com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service.QrLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.dto.ShineQrLoginDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 扫码登录获取登录用户信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/6
 *
 * @author zhang.zhiwen
 */
@DispatcherImplemetion(ShineAction.QRY_QR_INFO)
public class GetQrLoginInfoSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetQrLoginInfoSPIImpl.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private QrLoginService qrLoginService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");
        Assert.hasText(request.getData(), "报文data不能为空");
        String data = request.getData();
        String terminalId = request.getTerminalId();
        LOGGER.info("收到扫码登录报文:terminalId:{};data:{}", terminalId, data);

        ShineQrLoginDTO shineQrLoginDTO;
        try {
            shineQrLoginDTO = shineMessageHandler.parseObject(data, ShineQrLoginDTO.class);
            ShineLoginDTO resDto = qrLoginService.qryLoginDto(shineQrLoginDTO.getLoginId(), null);
            // resDto需要手动处理下置空" "
            resDto.setPassword(" ");
            response(request, CommonMessageCode.SUCCESS, resDto);
        } catch (Exception e) {
            LOGGER.error("收到扫码登录报文异常{}", e);
            response(request, CommonMessageCode.CODE_ERR_OTHER, null);
        }
    }

    private void response(CbbDispatcherRequest request, Integer code, Object content) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, content);
        } catch (Exception e) {
            LOGGER.error("终端{}收到扫码登录报文异常，e={}", request.getTerminalId(), e);
        }
    }
}
