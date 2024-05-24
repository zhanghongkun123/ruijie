package com.ruijie.rcos.rcdc.rco.module.impl.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.connect.AbstractConnectorManager;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年11月25日
 *
 * @author zhuangchenwu
 */
@Service("rccpConnectManager")
public class RccpConnectManager extends AbstractConnectorManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RccpConnectManager.class);
    
    @Autowired
    private GlobalParameterAPI globalParameterAPI;
    
    private static final String DEFAULT_PROTOCOL = "rest";
    
    // 全局表, 保存 rccp 云平台地址
    private static final String PARAM_RCCP_PLATFORM_IP = "rccp_platform_ip";

    // 全局表, 保存 rccp 云平台端口
    private static final String PARAM_RCCP_PLATFORM_PORT = "rccp_platform_port";
    
    public RccpConnectManager(String protocol) {
        super(protocol);
    }
    
    public RccpConnectManager() {
        super(DEFAULT_PROTOCOL);
    }

    @Override
    public String getAuthToken() {
        String address = globalParameterAPI.findParameter(PARAM_RCCP_PLATFORM_IP);
        String port = globalParameterAPI.findParameter(PARAM_RCCP_PLATFORM_PORT);
        try {
            refresh(address, Integer.valueOf(port));
        } catch (Exception e) {
            LOGGER.error("连接rest服务失败，地址[" + address + "]，端口[" + port + "]", e);
            throw new RuntimeException("连接rest服务失败，地址[" + address + "]，端口[" + port + "]", e);
        }
        // 目前rccp暂不支持ssl认证，返回空
        return null;
    }
}
