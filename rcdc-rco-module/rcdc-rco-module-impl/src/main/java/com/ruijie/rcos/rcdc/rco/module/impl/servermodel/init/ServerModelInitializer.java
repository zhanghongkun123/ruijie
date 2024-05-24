package com.ruijie.rcos.rcdc.rco.module.impl.servermodel.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Service;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.service.ServerModelService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 * 初始化服务器部署模式
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 * 
 * @author wjp
 */
@Service
public class ServerModelInitializer implements SafetySingletonInitializer, PriorityOrdered {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerModelInitializer.class);

    @Autowired
    private ServerModelService serverModelService;

    // 初始化服务器部署模式线程名称
    private static final String SERVER_MODEL_INITIALIZER = "server_model_initializer";

    // 初始化服务器部署模式异常重试间隔时间
    private static final Integer SLEEP_TIME = 10 * 1000;

    @Override
    public void safeInit() {
        LOGGER.info("开始初始化服务器部署模式");
        ThreadExecutors.execute(SERVER_MODEL_INITIALIZER, this::doInitProc);
    }

    private void doInitProc() {
        // 启动初始化服务器部署模式，强关联，直到成功为止
        while (true) {
            try {
                FindParameterResponse response = serverModelService.getServerModelFlag();
                if (StringUtils.isBlank(response.getValue()) || response.getValue().equals(ServerModelEnum.INIT_SERVER_MODEL.getName())) {
                    String serverModel = serverModelService.getServerModel().getName();
                    serverModelService.updateServerModelFlag(serverModel);
                    LOGGER.info("完成初始化服务器部署模式，模式为{}", serverModel);
                } else {
                    LOGGER.info("已初始化服务器部署模式，无需再次初始化");
                }
                return;
            } catch (Exception e) {
                LOGGER.error("初始化服务器部署模式失败：{}，反复重试", e.getMessage());
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException exception) {
                    LOGGER.error("初始化服务器部署模式失败,间隔{}秒出现异常。", SLEEP_TIME, exception.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }


    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
