package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.RccpDockerContainerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.ConnectNotfiyRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.docker.EditServiceConfigRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.docker.ConfigValueDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.docker.DefaultValueDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.spi.AfterConnectedBusinessHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: 添加云平台后启动桌面备份容器SPI
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/19
 *
 * @author TD
 */
public class DeskBackupDockerContainerSPIImpl implements AfterConnectedBusinessHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskBackupDockerContainerSPIImpl.class);

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private RccpDockerContainerMgmtAPI dockerContainerMgmtAPI;
    
    public static final int REPLICAS = 1;

    public static final String BACK_UP_CONTAINER_NAME = "rcontrol";
    
    @Override
    public void afterConnected(ConnectNotfiyRequest request) {
        Assert.notNull(request, "request can be not null");
        startDeskBackupDockerContainer(request.getPlatformId());
    }

    private void startDeskBackupDockerContainer(UUID platformId) {
        while (true) {
            if (serverModelAPI.isInitModel()) {
                LOGGER.info("[启动云桌面备份容器] 获取服务器部署信息失败，未找到对应的模式");
                waitSeconds();
                continue;
            }
            
            if (!serverModelAPI.isVdiModel()) {
                LOGGER.info("[启动云桌面备份容器] 非 VDI 部署模式不支持开启云桌面备份");
                return;
            }

            LOGGER.info("[启动云桌面备份容器] 开始调用接口启动云桌面备份容器");
            DefaultValueDTO defaultValueDTO = new DefaultValueDTO();
            defaultValueDTO.setReplicas(REPLICAS);
            ConfigValueDTO configValueDTO = new ConfigValueDTO();
            configValueDTO.setDefaultValueDTO(defaultValueDTO);
            EditServiceConfigRequest request = new EditServiceConfigRequest();
            request.setConfigValue(configValueDTO);
            request.setServiceName(BACK_UP_CONTAINER_NAME);
            request.setPlatformId(platformId);
            LOGGER.info("[启动云桌面备份容器] 请求参数为[{}]", JSON.toJSONString(request));
            try {
                dockerContainerMgmtAPI.editServiceConfig(request);
                return;
            } catch (Exception e) {
                LOGGER.error("[启动云桌面备份容器] 发生错误error!", e);
                waitSeconds();
            }
        }
    }

    private void waitSeconds() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException error", e);
            Thread.currentThread().interrupt();
        }
    }
}
