package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseLicenseSerialNumberSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: RcdcLicenseSerialNumberSPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020-02-26
 *
 * @author hli
 */
public class RcdcLicenseSerialNumberSPIImpl implements BaseLicenseSerialNumberSPI {
    @Override
    public String getPlatformSerialNumber() throws BusinessException {

        return " ";
    }

    @Override
    public String[] getValidatePlatformSerialNumber() throws BusinessException {

        return new String[0];
    }
}
