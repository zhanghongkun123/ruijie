package com.ruijie.rcos.rcdc.rco.module.impl.uws.init;

import com.ruijie.rcos.rcdc.rco.module.common.condition.ConditionProductOnConfig;
import com.ruijie.rcos.rcdc.rco.module.common.service.samba.SambaCommonService;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.uws.service.UwsComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.uws.service.UwsComponentService;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 * Description:初始化UWS安装包
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/24 16:59
 *
 * @author lifeng
 */
@Service
@Conditional(ConditionProductOnConfig.class)
public class UwsComponentInitializer implements SafetySingletonInitializer {

    @Autowired
    private UwsComponentService uwsComponentService;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private SambaCommonService sambaCommonService;

    private static final String UWS_WAIT_SAMBA_READY = "UWS_WAIT_SAMBA_READY";

    private static final String SAMBA_SHARE_NAME = "iso";

    @Override
    public void safeInit() {
        if (!uwsComponentService.isExistUwsPackageFile()) {
            // 不存在则代表UWS未安装或者已安装被移动到samba目录,不需要重新初始化
            return;
        }
        uwsDockingAPI.initCmApp();
        //sambaCommonService.waitSambaReady(UWS_WAIT_SAMBA_READY, SAMBA_SHARE_NAME, uwsDockingAPI::initCmISO);
    }

}
