package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 接收shine上报的桌面id，重新绑定终端
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/5/24
 *
 * @author Jarman
 */
@DispatcherImplemetion(ShineAction.BIND_DESKTOP_TERMINAL)
public class DesktopBindTerminalSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopBindTerminalSPIImpl.class);

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot null");
        LOGGER.info("终端上报桌面id，rcdc重新对桌面和终端进行绑定。接收到的报文：{}", request.getData());
        UUID desktopId;
        try {
            JSONObject dataJson = JSONObject.parseObject(request.getData());
            desktopId = UUID.fromString(String.valueOf(dataJson.get("id")));
        } catch (Exception e) {
            LOGGER.error("解析shine报文错误", e);
            return;
        }
        // 重新对桌面和终端进行绑定
        cloudDesktopOperateService.bindDesktopTerminal(desktopId, request.getTerminalId());
        // 重新生成当前桌面缓存
        DesktopRequestDTO desktopRequestDTO = new DesktopRequestDTO();
        desktopRequestDTO.setDesktopRunInTerminal(true);
        desktopRequestDTO.setCbbDispatcherRequest(request);
        desktopOperateRequestCache.addCache(desktopId, desktopRequestDTO);
    }
}
