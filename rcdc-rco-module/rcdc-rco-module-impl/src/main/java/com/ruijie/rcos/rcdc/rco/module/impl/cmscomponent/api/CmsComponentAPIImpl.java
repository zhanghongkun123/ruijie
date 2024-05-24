package com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.api;

import org.springframework.beans.factory.annotation.Autowired;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsComponentAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.service.CmsComponentService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: CmsComponentAPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 *
 * @author wjp
 */
public class CmsComponentAPIImpl implements CmsComponentAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsComponentAPIImpl.class);

    @Autowired
    private CmsComponentService cmsComponentService;

    @Override
    public String getCmsComponent() throws BusinessException {
        String cmsComponentFlag = cmsComponentService.getCMSComponentFlag().getValue();
        LOGGER.info("获取获取CMS启用情况成功。CMS启用情况 = {}", cmsComponentFlag);
        return cmsComponentFlag;
    }

    @Override
    public void initCmApp() {
        cmsComponentService.initCmApp();
    }
}
