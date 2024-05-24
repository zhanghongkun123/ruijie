package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ClearDeskSessionDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


/**
 * Description: 清除桌面会话配置
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-30
 *
 * @author zqj
 */
@DispatcherImplemetion(Constants.CLEAR_DESK_SESSION_CONFIG)
public class ClearDeskSessionConfigHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearDeskSessionConfigHandlerSPIImpl.class);

    @Autowired
    private DesktopAPI desktopAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.hasText(request.getData(), "data in request cannot be blank!");

        ClearDeskSessionDTO clearDeskSessionDTO = JSON.parseObject(request.getData(), ClearDeskSessionDTO.class);
        if (clearDeskSessionDTO != null) {
            LOGGER.info("清除桌面会话配置，deskId={}", clearDeskSessionDTO.getDeskId());
            desktopAPI.clearDeskShutdownDate(clearDeskSessionDTO.getDeskId());
        }

    }
}
