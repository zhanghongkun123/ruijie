package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseLicenseConfigSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Description: RcdcLicenseConfigSPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020-02-26
 *
 * @author hli
 */
public class RcdcLicenseConfigSPIImpl implements BaseLicenseConfigSPI {

    @Override
    public Map<String, Boolean> getLicenseTimingReductionConfig(Set<String> featureIdSet) throws BusinessException {
        Map<String, Boolean> map = new HashMap<>();
        return map;
    }
}
