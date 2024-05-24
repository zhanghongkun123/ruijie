package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.base.sysmanage.module.def.dto.license.NotifyLicenseChangeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.enums.LicenseNotifyType;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseLicenseFeatureNotifySPI;

/**
 * Description: RcdcLicenseNotifySPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020-02-26
 *
 * @author hli
 */
public class RcdcLicenseNotifySPIImpl implements BaseLicenseFeatureNotifySPI {

    @Override
    public void notifyLicenseChange(LicenseNotifyType licenseNotifyType, NotifyLicenseChangeDTO[] licenseChangeDtoArr) {

        // // Do nothing because of X and Y.
    }
}
