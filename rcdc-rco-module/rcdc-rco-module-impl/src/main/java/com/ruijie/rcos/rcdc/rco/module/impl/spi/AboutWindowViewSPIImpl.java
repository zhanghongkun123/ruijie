package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.aboutwindowview.AboutWindowViewRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.aboutwindowview.AboutWindowViewResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: shine查询关于特征码
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/04/06 17:57
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.ACTION_ABOUT_WINDOW_VIEW_INIT)
public class AboutWindowViewSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AboutWindowViewSPIImpl.class);

    private static final Integer FAILURE = -1;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        String dataJsonString = request.getData();
        Assert.hasText(dataJsonString, "data string cannot be blank!");

        CbbResponseShineMessage shineMessage;
        try {
            AboutWindowViewRequestDTO requestDTO = JSONObject.parseObject(dataJsonString, AboutWindowViewRequestDTO.class);
            Assert.notNull(requestDTO, "request parse error!");
            String signature = userTerminalMgmtAPI.getTerminalFeatureCode(requestDTO.getTerminalId());
            AboutWindowViewResponseDTO dto = new AboutWindowViewResponseDTO();
            if (signature != null) {
                dto.setSignature(signature);
            }
            shineMessage = ShineMessageUtil.buildResponseMessage(request, dto);
        } catch (Exception e) {
            LOGGER.error("管理员在终端登出失败", e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(request, FAILURE);
        }
        messageHandlerAPI.response(shineMessage);
    }
}
