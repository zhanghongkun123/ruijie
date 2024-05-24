package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.spi;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DeskFaultInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DeskFaultInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.constant.RemoteAssistBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户请求进行远程协助
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/27
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(ShineAction.EST_RA_USER_REQ)
public class RequestRemoteAssistSPIImpl implements CbbDispatcherHandlerSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(RequestRemoteAssistSPIImpl.class);

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private RemoteAssistService remoteAssistService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");

        String data = request.getData();
        UserDesktopEntity userDesktop = null;

        // 如果 data 字段不是空，则尝试使用 JSON 解析它，并创建用户桌面对象
        if (data != null) {
            try {
                final JSONObject jsonObject = JSONObject.parseObject(data);
                final String deskId = jsonObject.getString("deskId");
                userDesktop = userDesktopService.findByDeskId(UUID.fromString(deskId));
            } catch (JSONException e)  {
                LOGGER.error("获取多桌面模式下的桌面 ID 失败，数据为 = {}", data);
            }
        }

        // 无法从 data 字段中获取到多桌面模式下的桌面 ID，那么退回到一个用户只有一个桌面的模式
        if (userDesktop == null) {
            userDesktop = userDesktopService.findRunningInTerminalDesktop(request.getTerminalId());
        }

        try {
            remoteAssistService.requestRemoteAssist(userDesktop.getCbbDesktopId(), request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("rcdc_terminal_not_found_terminal, terminal id = {}", request.getTerminalId());
        }

    }

}
