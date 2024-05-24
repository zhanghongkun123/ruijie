package com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.init;

import com.ruijie.rcos.rcdc.rco.module.common.condition.ConditionProductOnConfig;
import com.ruijie.rcos.rcdc.rco.module.common.service.samba.SambaCommonService;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.service.impl.CmsUpgradeServiceImpl;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.service.CmsComponentService;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 * Description:初始化CMS安装包
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/26 16:59
 *
 * @author ljm
 */
@Service
@Conditional(ConditionProductOnConfig.class)
public class CmsComponentInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsComponentInitializer.class);

    @Autowired
    private CmsComponentService cmsComponentService;

    @Autowired
    private SambaCommonService sambaCommonService;

    private static final String CMS_WAIT_SAMBA_READY = "CMS_WAIT_SAMBA_READY";

    private static final String SAMBA_SHARE_NAME = "iso";

    @Override
    public void safeInit() {
        cmsComponentService.initCmApp();
        //LOGGER.info("initCmISO waitSambaReady");
        //sambaCommonService.waitSambaReady(CMS_WAIT_SAMBA_READY, SAMBA_SHARE_NAME, cmsComponentService::initCmISO);
    }

}
