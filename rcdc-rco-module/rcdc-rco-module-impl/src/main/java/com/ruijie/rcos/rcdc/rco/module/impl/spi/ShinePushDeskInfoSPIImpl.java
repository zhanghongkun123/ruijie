package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShinePushDeskInfoDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/19 9:27
 *
 * @author zjy
 */
@DispatcherImplemetion(ShineAction.REPORT_TERMINAL_DESK_INFO)
public class ShinePushDeskInfoSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShinePushDeskInfoSPIImpl.class);

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request must not be null");

        LOGGER.info("终端[{}]上报桌面信息为[{}]", request.getTerminalId(), request.getData());
        ShinePushDeskInfoDTO shinePushDeskInfoDTO = JSON.parseObject(request.getData(), ShinePushDeskInfoDTO.class);
        if (shinePushDeskInfoDTO == null || shinePushDeskInfoDTO.getDeskId() == null) {
            LOGGER.info("终端[{}]上报的云桌面id为空，无需处理", request.getTerminalId());
            return;
        }

        cbbIDVDeskMgmtAPI.updateRealUseImageId(shinePushDeskInfoDTO.getDeskId(), shinePushDeskInfoDTO.getImageId());
    }
}
