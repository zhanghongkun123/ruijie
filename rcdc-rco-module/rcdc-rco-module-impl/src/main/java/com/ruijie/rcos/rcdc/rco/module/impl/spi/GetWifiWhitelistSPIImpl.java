package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.response.GetWifiWhitelistResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.util.Assert;

/**
 * @ClassName: GetWifiWhitelistSPIImpl
 * @Description: shine主动获取无线白名单信息
 * @author: zhiweiHong
 * @date: 2020/8/18
 **/
@DispatcherImplemetion(ShineAction.GET_WIFI_WHITELIST)
public class GetWifiWhitelistSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetWifiWhitelistSPIImpl.class);

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private WifiWhitelistAPI wifiWhitelistAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");
        String terminalId = request.getTerminalId();
        try {
            TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
            UUID terminalGroupId = terminalDTO.getTerminalGroupId();
            GetWifiWhitelistResponse whitelistResp = wifiWhitelistAPI.getWifiWhitelistByTerminalGroupId(terminalGroupId);
            LOGGER.info("终端[{}]，终端组[{}]，获取白名单[{}]", terminalId, terminalGroupId, JSON.toJSON(whitelistResp));
            response(request, CommonMessageCode.SUCCESS, whitelistResp);
        } catch (BusinessException e) {
            LOGGER.error("获取终端{}信息失败，e={}", terminalId, e);
            response(request, CommonMessageCode.CODE_ERR_OTHER, null);
        }
    }

    private void response(CbbDispatcherRequest request, Integer code, Object content) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, content);
        } catch (Exception e) {
            LOGGER.error("终端{}回应白名单信息失败，e={}", request.getTerminalId(), e);
        }
    }
}
