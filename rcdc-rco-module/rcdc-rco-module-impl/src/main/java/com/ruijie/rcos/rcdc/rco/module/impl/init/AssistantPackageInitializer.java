package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.SambaServiceAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.base.sysmanage.module.def.dto.SambaConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.FilesOperationType;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Description:初始化小助手安装包
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/26 16:59
 *
 * @author ketb
 */
@Service
public class AssistantPackageInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssistantPackageInitializer.class);

    private static final String APP_SHARE_NAME = "app";

    /**
     * 初始化小助手安装包线程名称
     */
    private static final String ASSISTANT_INITIALIZER = "assistant_initializer";

    /**
     * 初始化小助手安装包异常重试间隔时间
     */
    private static final Integer SLEEP_TIME = 10 * 1000;

    @Autowired
    private CbbDeskSoftMgmtAPI deskSoftMgmtAPI;

    @Autowired
    private SambaServiceAPI sambaServiceAPI;

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Override
    public void safeInit() {
        ThreadExecutors.execute(ASSISTANT_INITIALIZER, this::initAssistant);
    }

    private void initAssistant() {
        LOGGER.info("初始化小助手安装包开始...");
        CbbCreateDeskSoftDTO request = generatorAssistantPackage();
        if (request == null) {
            LOGGER.error("assistant package is not exist");
            return;
        }
        LOGGER.info("开始检测samba是否就绪，如果未就绪将每10S检测一次，最多等待5min");
        waitEnvironmentReady();
        LOGGER.info("samba已就绪或者已超过等待时长，开始初始化小助手安装包...");
        try {
            deskSoftMgmtAPI.createDeskSoft(request);
        } catch (Exception e) {
            LOGGER.error("初始化小助手安装包异常：", e);
        }
        LOGGER.info("初始化小助手安装包结束...");
    }

    private CbbCreateDeskSoftDTO generatorAssistantPackage() {
        File assistantFile = new File(Constants.ASSISTANT_APP_COMPONENT_PATH + Constants.ASSISTANT_APP_PACKAGE_NAME);
        if (!assistantFile.exists()) {
            LOGGER.error("小助手升级包文件不存在");
            // 小助手升级包文件不存在
            return null;
        }
        CbbCreateDeskSoftDTO request = new CbbCreateDeskSoftDTO();
        CbbDeskSoftDTO existFile = deskSoftMgmtAPI.findByName(Constants.ASSISTANT_APP_PACKAGE_NAME);
        if (existFile != null) {
            request.setDeskSoftId(existFile.getId());
        }
        request.setFilePath(Constants.ASSISTANT_APP_COMPONENT_PATH + Constants.ASSISTANT_APP_PACKAGE_NAME);
        request.setFileName(Constants.ASSISTANT_APP_PACKAGE_NAME);
        request.setFilesOperationType(FilesOperationType.COPY);
        return request;
    }

    private void waitEnvironmentReady() {
        while (true) {
            try {
                SystemMaintenanceState systemMaintenanceState = maintenanceModeMgmtAPI.getMaintenanceMode();
                if (systemMaintenanceState != SystemMaintenanceState.NORMAL) {
                    LOGGER.info("当前服务器处于维护模式，继续等待");
                } else {
                    SambaConfigDTO sambaConfigDTO = sambaServiceAPI.getSambaConfig(APP_SHARE_NAME);
                    // 挂载信息不打印在info级别日志中
                    LOGGER.debug("assistant samba:{}", JSONObject.toJSONString(sambaConfigDTO));
                    if (sambaConfigDTO.getState().isMounted()) {
                        LOGGER.info("assistant samba is ready");
                        break;
                    }
                }
                sleepForWait();

            } catch (Exception e) {
                LOGGER.error("获取samba失败，10S后重试：{}", e.getMessage());
                sleepForWait();
            }
        }
    }

    private void sleepForWait() {
        try {
            LOGGER.info("10S后进行重试");
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException exception) {
            LOGGER.error("初始化小助手安装包失败,间隔{}秒出现异常。", SLEEP_TIME, exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
