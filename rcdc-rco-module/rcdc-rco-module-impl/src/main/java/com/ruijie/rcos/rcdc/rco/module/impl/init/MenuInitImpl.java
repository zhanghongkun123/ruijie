package com.ruijie.rcos.rcdc.rco.module.impl.init;

import java.io.IOException;

import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.tool.IacAnnotationPermissionTool;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.service.ServerModelService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoIacPermissionService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/20 10:04
 *
 * @author linrenjian
 */
@Service
public class MenuInitImpl implements SafetySingletonInitializer {

    public static final String MENU_INIT_SCAN_BASE_PATH = "com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.menu.";

    @Autowired
    private IacAnnotationPermissionTool annotationPermissionTool;

    @Autowired
    private ServerModelService serverModelService;

    @Autowired
    private RcoIacPermissionService rcoIacPermissionService;

    /**
     *日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuInitImpl.class);

    @Override
    public void safeInit() {
        ThreadExecutors.execute("initMenuPermissionData", () -> {
            // 初始化权限：该方法删除不存在的权限数据 并 返回删除的条数
            while (true) {
                try {
                    initMenuPemissionAndAdmin();
                    return;
                } catch (Exception e) {
                    LOGGER.error(" 权限初始化异常", e);
                }

                waitMoment();
            }
        });
    }

    private void initMenuPemissionAndAdmin() throws IOException, BusinessException {
        LOGGER.info("进行5.4权限数据初始化");
        ServerModelEnum serverModel = obtainServerModel();

        annotationPermissionTool.initPermissionsWithForceDelete(SubSystem.CDC,
                new String[] {MENU_INIT_SCAN_BASE_PATH + serverModel.getName()}, true);
        LOGGER.info("通知IAC全局开关");
        rcoIacPermissionService.notifyAllMenuGlobalEnable(null);
    }

    private ServerModelEnum obtainServerModel() {
        while (true) {
            try {
                FindParameterResponse response = serverModelService.getServerModelFlag();
                if (StringUtils.isNotBlank(response.getValue()) && !response.getValue().equals(ServerModelEnum.INIT_SERVER_MODEL.getName())) {
                    return ServerModelEnum.getByName(response.getValue());
                }
                ServerModelEnum serverModel = serverModelService.getServerModel();
                LOGGER.info("获取到的serverModel:{}", serverModel.name());
                if (ServerModelEnum.INIT_SERVER_MODEL != serverModel) {
                    return serverModel;
                }
            } catch (BusinessException e) {
                LOGGER.error("部署模式获取异常，等待进行初始化", e);
            }

            waitMoment();
        }

    }

    private void waitMoment() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            LOGGER.error("部署模式获取，等待进行初始化异常", e);
            Thread.currentThread().interrupt();
        }
    }

}
