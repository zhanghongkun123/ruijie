package com.ruijie.rcos.rcdc.rco.module.impl.uws.service.impl;

import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.SambaServiceAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.base.sysmanage.module.def.dto.SambaConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.FilesOperationType;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.uws.UwsComponentEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.uws.UwsComponentBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.uws.service.UwsComponentService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Description: uws 组件服务实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-27 16:43:00
 *
 * @author zjy
 */
@Service
public class UwsComponentServiceImpl implements UwsComponentService, SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsComponentServiceImpl.class);

    private static final String UWS_COMPONENT = "uws_component";

    private static final String APP_SHARE_NAME = "app";

    // 初始化UWS组件安装包线程名称
    private static final String UWS_COMPONENT_INITIALIZER = "uws_component_initializer";

    // 初始化UWS组件安装包异常重试间隔时间
    private static final Integer SLEEP_TIME = 10 * 1000;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private CbbDeskSoftMgmtAPI deskSoftMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private SambaServiceAPI sambaServiceAPI;

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Override
    public void initCmApp() {
        ThreadExecutors.execute(UWS_COMPONENT_INITIALIZER, this::doInitProc);
    }

    @Override
    public FindParameterResponse getUwsComponentFlag() {
        return rcoGlobalParameterAPI.findParameter(new FindParameterRequest(UWS_COMPONENT));
    }

    @Override
    public Boolean isExistUwsPackageFile() {
        File uwsPackageFile = new File(Constants.UWS_CLIENT_PACKAGE_PATH + Constants.UWS_CLIENT_PACKAGE_NAME);
        return uwsPackageFile.exists();
    }

    /**
     * 同步执行初始化，避免多线程场景出现错误
     *
     * @Date 2022/6/23 20:51
     * @Author zhengjingyong
     **/
    private synchronized void doInitProc() {
        LOGGER.info("开始初始化UWS组件安装包");

        // 启动初始化UWS组件安装包，强关联，直到成功为止
        while (true) {
            try {
                FindParameterResponse response = rcoGlobalParameterAPI.findParameter(new FindParameterRequest(UWS_COMPONENT));
                if (StringUtils.isBlank(response.getValue())) {
                    rcoGlobalParameterAPI.saveParameter(new UpdateParameterRequest(UWS_COMPONENT, UwsComponentEnum.INIT_STATE.name()));
                    response = rcoGlobalParameterAPI.findParameter(new FindParameterRequest(UWS_COMPONENT));
                }

                LOGGER.info("当前UWS组件安装包安装状态为：[{}]", response.getValue());
                String uwsComponent = getUWSComponent().name();
                rcoGlobalParameterAPI.updateParameter(new UpdateParameterRequest(UWS_COMPONENT, uwsComponent));
                initUWSPackage();
                return;
            } catch (Exception e) {
                LOGGER.error("初始化UWS组件安装包失败， ex: ", e);
                sleepForWait();
            }
        }
    }

    private UwsComponentEnum getUWSComponent() throws BusinessException {
        LOGGER.info("调用平台组件获取UWS组件启用情况");
        if (serverModelAPI.isInitModel()) {
            LOGGER.error("获取服务器部署信息失败：未找到对应的模式");
            throw new BusinessException(UwsComponentBusinessKey.RCDC_RCO_GET_UWS_COMPONENT_STATUS_ERROR);
        }
        // 不区分服务器类型
        return UwsComponentEnum.ENABLED_STATE;
    }

    private void initUWSPackage() {
        LOGGER.info("开始检测samba是否就绪，如果未就绪将每10S检测一次");
        waitEnvironmentReady();
        LOGGER.info("samba已就绪或者已超过等待时长，开始初始化UWS客户端安装包...");
        try {
            File assistantFile = new File(Constants.UWS_CLIENT_PACKAGE_PATH + Constants.UWS_CLIENT_PACKAGE_NAME);
            if (!assistantFile.exists()) {
                LOGGER.error("UWS客户端安装包不存在");
                return;
            }

            CbbCreateDeskSoftDTO request = new CbbCreateDeskSoftDTO();
            // 其实这边可以不删 状态机里有删除逻辑了
            // 存在优先删除
            CbbDeskSoftDTO existFile = deskSoftMgmtAPI.findByName(Constants.UWS_CLIENT_PACKAGE_NAME);
            if (existFile != null) {
                deskSoftMgmtAPI.deleteDeskSoft(existFile.getId());
                request.setDeskSoftId(existFile.getId());
            }
            request.setFilePath(Constants.UWS_CLIENT_PACKAGE_PATH + Constants.UWS_CLIENT_PACKAGE_NAME);
            request.setFileName(Constants.UWS_CLIENT_PACKAGE_NAME);
            request.setFilesOperationType(FilesOperationType.COPY);
            deskSoftMgmtAPI.createDeskSoft(request);
        } catch (Exception e) {
            LOGGER.error("初始化UWS客户端安装包异常：", e);
        }
        LOGGER.info("初始化UWS客户端安装包结束...");
    }

    private void waitEnvironmentReady() {
        while (true) {
            try {
                SystemMaintenanceState systemMaintenanceState = maintenanceModeMgmtAPI.getMaintenanceMode();
                if (systemMaintenanceState != SystemMaintenanceState.NORMAL) {
                    LOGGER.info("当前服务器处于维护模式，继续等待");
                } else {
                    SambaConfigDTO sambaConfigDTO = sambaServiceAPI.getSambaConfig(APP_SHARE_NAME);
                    LOGGER.info("uws samba state :{}", sambaConfigDTO.getState());
                    if (sambaConfigDTO.getState().isMounted()) {
                        LOGGER.info("uws samba is ready");
                        break;
                    }
                }
                sleepForWait();
            } catch (Exception e) {
                LOGGER.error("获取samba失败，10S后重试", e);
                sleepForWait();
            }
        }
    }

    private void sleepForWait() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException exception) {
            LOGGER.error("初始化UWS组件安装包失败,间隔{}秒出现异常。", SLEEP_TIME, exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }


    /**
     * 检查并修复mini服务器uws初始化错误问题
     *
     * @Date 2022/6/18 15:07
     * @Author zhengjingyong
     **/
    @Override
    public void safeInit() {
        LOGGER.info("启动时执行UWS组件安装错误状态检查");
        FindParameterResponse response = rcoGlobalParameterAPI.findParameter(new FindParameterRequest(UWS_COMPONENT));
        if (UwsComponentEnum.DISABLED_STATE.name().equals(response.getValue()) && serverModelAPI.isMiniModel()) {
            LOGGER.info("UWS状态为不可用且服务器类型为Mini，重新执行初始化");
            this.initCmApp();
        }
    }

}
