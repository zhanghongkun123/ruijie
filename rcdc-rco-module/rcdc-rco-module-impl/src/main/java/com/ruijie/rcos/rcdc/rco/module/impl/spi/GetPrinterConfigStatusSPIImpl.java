package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 获取打印机配置开关的状态
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/27
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(ShineAction.GET_PRINTER_CONFIG_STATUS)
public class GetPrinterConfigStatusSPIImpl implements CbbDispatcherHandlerSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(GetPrinterConfigStatusSPIImpl.class);

    @Autowired
    private PrinterManageServiceAPI printerManageServiceAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");

        String status = printerManageServiceAPI.getPrinterConfigStatus();

        response(request, 0, status);
    }


    private void response(CbbDispatcherRequest request, Integer code, String status) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, status);
        } catch (Exception e) {
            LOGGER.error("终端{}获取用户信息失败，e={}", request.getTerminalId(), e);
        }
    }
}
