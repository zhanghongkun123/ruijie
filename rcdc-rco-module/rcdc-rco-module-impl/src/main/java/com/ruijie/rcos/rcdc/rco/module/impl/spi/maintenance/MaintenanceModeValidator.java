package com.ruijie.rcos.rcdc.rco.module.impl.spi.maintenance;

import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/3
 *
 * @author nt
 */
@Service
public class MaintenanceModeValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaintenanceModeValidator.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    /**
     *  维护模式校验
     *
     * @return boolean 结果
     */
    public boolean validate() {

        return maintenanceModeMgmtAPI.getMaintenanceMode() != SystemMaintenanceState.NORMAL;
    }

    /**
     * 维护模式下, 响应终端
     * @param request 请求
     * @param code 错误码
     * @param errMsg 错误信息
     */
    public void responseUnderMaintenanceMessage(CbbDispatcherRequest request, int code, String errMsg) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(code, "code can not be null");
        Assert.notNull(errMsg, "errMsg can not be null");

        try {
            shineMessageHandler.responseMessage(request, code, errMsg);
            LOGGER.info("应答报文给终端[{}]成功，应答状态码：{},应答内容：{}", request.getTerminalId(), code, errMsg);
        } catch (Exception e) {
            LOGGER.error("应答报文给终端[" + request.getTerminalId() + "]失败，应答状态码：" + code + "应答内容：" + errMsg, e);
        }
    }

}
