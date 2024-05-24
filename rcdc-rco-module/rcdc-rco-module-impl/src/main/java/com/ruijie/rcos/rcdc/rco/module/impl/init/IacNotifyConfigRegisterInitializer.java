package com.ruijie.rcos.rcdc.rco.module.impl.init;


import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRestNotifyConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.SslConfigEntry;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacNotifyConfigRestAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.ClusterVirtualIpDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;


/**
 * Description: 初始化注册身份中心回调地址
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024-02-21
 *
 * @author jarman
 */
@Service
public class IacNotifyConfigRegisterInitializer implements SafetySingletonInitializer {


    private static final Logger LOGGER = LoggerFactory.getLogger(IacNotifyConfigRegisterInitializer.class);

    @Autowired
    private IacNotifyConfigRestAPI iacNotifyConfigRestAPI;

    @Autowired
    private CloudPlatformMgmtAPI cloudPlatformMgmtAPI;


    @Value("${rcdc.rcdcserver.trustfile}")
    private String trustStore;

    @Value("${rcdc.rcdcserver.trustpass}")
    private String trustPass;

    @Value("${rcdc.rcdcserver.clientkeyfile}")
    private String keyStore;

    @Value("${rcdc.rcdcserver.keypass}")
    private String keyPass;

    @Value("${rcdc.rcdcserver.clientauth}")
    private Boolean clientauth;

    /**
     * 访问路径前缀
     */
    private static final String CONTEXT_PATH = "rcdc";

    /**
     * 访问的端口，在tomcat配置
     */
    private static final int PORT = 8443;

    @Override
    public void safeInit() {
        ThreadExecutors.execute("iacCallBackConfig", () -> {
            while (true) {
                try {
                    registerCallbackAddress();
                    break;
                } catch (Exception e) {
                    sleep();
                    LOGGER.error("iac回调地址注册失败,重试", e);
                }
            }
        });
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error("休眠失败", e);
        }
    }

    private void registerCallbackAddress() throws BusinessException {
        IacRestNotifyConfigDTO configDTO = new IacRestNotifyConfigDTO();
        configDTO.setType(SubSystem.CDC);
        configDTO.setIpAddress(getLocalIP());
        configDTO.setPort(PORT);
        configDTO.setPathPrefix(CONTEXT_PATH);
        SslConfigEntry sslConfig = new SslConfigEntry();
        sslConfig.setTrustStore(trustStore);
        sslConfig.setTrustPass(trustPass);
        sslConfig.setKeyStore(keyStore);
        sslConfig.setKeyPass(keyPass);
        sslConfig.setHasClientAuth(clientauth);
        configDTO.setSslConfig(sslConfig);
        LOGGER.info("iacCallBackConfig:{}", JSON.toJSONString(configDTO));
        iacNotifyConfigRestAPI.settingRestConfig(configDTO);
    }

    private String getLocalIP() throws BusinessException {
        DtoResponse<ClusterVirtualIpDTO> response = cloudPlatformMgmtAPI.getClusterVirtualIp(new DefaultRequest());
        Assert.notNull(response, "response vip can not be null");

        return response.getDto().getClusterVirtualIpIp();
    }
}
