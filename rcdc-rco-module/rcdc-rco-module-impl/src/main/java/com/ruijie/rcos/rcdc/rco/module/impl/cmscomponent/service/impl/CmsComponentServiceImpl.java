package com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.SambaServiceAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.base.sysmanage.module.def.dto.SambaConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ProtocolType;
import com.ruijie.rcos.rcdc.rco.module.common.condition.ConditionProductOnConfig;
import com.ruijie.rcos.rcdc.rco.module.common.utils.SysInfoUtils;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.cmscomponent.enums.CmsComponentEnum;
import com.ruijie.rcos.rcdc.rco.module.def.constants.FilePathContants;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.service.CmsUpgradeService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.FileUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Description: CMSComponentServiceImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-09-04
 *
 * @author wjp
 */
@Service
@Conditional(ConditionProductOnConfig.class)
public class CmsComponentServiceImpl extends AbstractCmsComponentServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsComponentServiceImpl.class);


    @Autowired
    private SambaServiceAPI sambaServiceAPI;

    @Autowired
    private CbbImageDriverMgmtAPI imageDriverMgmtAPI;

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Autowired
    private CmsUpgradeService cmsUpgradeService;

    @Autowired
    private ServerModelAPI serverModelAPI;

    private static final String APP_SHARE_NAME = "app";

    // 初始化CMS组件安装包线程名称
    private static final String CMS_COMPONENT_INITIALIZER = "cms_component_initializer";

    // 初始化CMSISO线程名称
    private static final String CMS_ISO_INITIALIZER = "cms_iso_initializer";

    // 初始化CMS组件安装包异常重试间隔时间
    private static final Integer SLEEP_TIME = 10 * 1000;

    @Override
    public void initCmApp() {
        ThreadExecutors.execute(CMS_COMPONENT_INITIALIZER, this::doInitProc);
    }

    @Override
    public void initCmISO() {
        ThreadExecutors.execute(CMS_ISO_INITIALIZER, this::doInitCmsISO);
    }

    private void doInitProc() {
        if (SysInfoUtils.cpuArchTypeEqArm()) {
            LOGGER.info("ARM服务器功能限制，禁用CMS组件初始化");
            return;
        }
        LOGGER.info("开始初始化CMS组件安装包");
        // 启动初始化CMS组件安装包，强关联，直到成功为止
        while (true) {
            try {
                FindParameterResponse response = getCMSComponentFlag();
                if (StringUtils.isBlank(response.getValue()) || response.getValue().equals(CmsComponentEnum.INIT_STATE.getName())) {
                    String cmsComponent = getCMSComponent().getName();
                    updateCMSComponentFlag(cmsComponent);
                    LOGGER.info("完成初始化CMS组件安装包，状态为{}", cmsComponent);
                    if (cmsComponent.equals(CmsComponentEnum.ENABLED_STATE.getName())) {
                        initCMSPackage();
                    }
                    return;
                }
                if (response.getValue().equals(CmsComponentEnum.ENABLED_STATE.getName())) {
                    LOGGER.info("已初始化CMS组件安装包，CMS已启用，执行复制CMS安装包");
                    initCMSPackage();
                } else {
                    LOGGER.info("已初始化CMS组件安装包，CMS未启用，无需再次操作");
                }
                return;
            } catch (Exception e) {
                LOGGER.error("初始化CMS组件安装包失败：{}，反复重试", e.getMessage());
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException exception) {
                    LOGGER.error("初始化CMS组件安装包失败,间隔{}秒出现异常。", SLEEP_TIME, exception.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void initCMSPackage() {
        LOGGER.info("开始检测samba是否就绪，如果未就绪将每10S检测一次，最多等待5min");
        waitEnvironmentReady();
        LOGGER.info("samba已就绪或者已超过等待时长，开始初始化CMS客户端安装包...");
        try {
            if (Boolean.FALSE.equals(hasCMSPackage())) {
                LOGGER.error("CMS客户端安装包不存在");
                return;
            }
            synchronized (this) {
                copyCMSPackage();
            }
        } catch (Exception e) {
            LOGGER.error("初始化CMS客户端安装包异常：", e);
        }
        LOGGER.info("初始化CMS客户端安装包结束...");
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
                    LOGGER.debug("cms samba:{}", JSONObject.toJSONString(sambaConfigDTO));
                    if (sambaConfigDTO.getState().isMounted()) {
                        LOGGER.info("cms samba is ready");
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
            LOGGER.error("初始化CMS组件安装包失败,间隔{}秒出现异常。", SLEEP_TIME, exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void doInitCmsISO() {
        if (SysInfoUtils.cpuArchTypeEqArm()) {
            LOGGER.info("ARM服务器，不需要拷贝到samba");
            return;
        }
        LOGGER.info("开始复制CMS的ISO");

        try {
            cmsUpgradeService.copyIsoToSamba(cmsUpgradeService.getCmNewestIsoFromConfig(ProtocolType.FILE), FilePathContants.CMS_ISO_SAMBA_PATH);
        } catch (BusinessException e) {
            LOGGER.error("复制samba模板文件[{}]异常，进行重试", FilePathContants.CMS_ISO_SAMBA_PATH, e);
        }
    }


}
