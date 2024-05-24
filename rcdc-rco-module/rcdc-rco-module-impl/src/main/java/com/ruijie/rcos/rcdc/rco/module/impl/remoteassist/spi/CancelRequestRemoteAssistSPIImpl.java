package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.spi;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.dto.RemoteAssistCancelRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 用户取消进行远程协助
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/27
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(ShineAction.EST_RA_USER_CANCEL)
public class CancelRequestRemoteAssistSPIImpl implements CbbDispatcherHandlerSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(RequestRemoteAssistSPIImpl.class);

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private RemoteAssistService remoteAssistService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");

        UUID deskId = null;
        if (StringUtils.isNotBlank(request.getData())) {
            RemoteAssistCancelRequestDTO data = JSONObject.parseObject(request.getData(), RemoteAssistCancelRequestDTO.class);
            deskId = data.getDeskId();
        }
        UserDesktopEntity userDesktop;
        // 桌面不存在 则根据查询终端 做兼容处理
        if (deskId == null) {
            userDesktop = userDesktopService.findRunningInTerminalDesktop(request.getTerminalId());
        } else {
            userDesktop = userDesktopService.findByDeskId(deskId);
        }
        remoteAssistService.cancelRemoteAssist(userDesktop.getCbbDesktopId(), request.getTerminalId());
    }
}
