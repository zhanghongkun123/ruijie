package com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.service.impl;

import com.ruijie.rcos.rcdc.rco.module.common.condition.ConditionDebugOnConfig;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

/**
 * Description: CMSComponentServiceImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-09-04
 *
 * @author wjp
 */
@Service
@Conditional(ConditionDebugOnConfig.class)
public class DebugCmsComponentServiceImpl extends AbstractCmsComponentServiceImpl {

    @Override
    public void initCmApp() {
        // 空实现
    }

    @Override
    public void initCmISO() {

    }
}
