package com.ruijie.rcos.rcdc.rco.module.impl.spi.appcenter.dto;

import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftPackageDiskInfo;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月15日
 *
 * @author xgx
 */
public class AppSoftPackageDiskListDTO {
    private AppSoftPackageDiskInfo[] appDiskArr;

    public AppSoftPackageDiskListDTO(AppSoftPackageDiskInfo[] appDiskArr) {
        this.appDiskArr = appDiskArr;
    }

    public AppSoftPackageDiskInfo[] getAppDiskArr() {
        return appDiskArr;
    }

    public void setAppDiskArr(AppSoftPackageDiskInfo[] appDiskArr) {
        this.appDiskArr = appDiskArr;
    }
}
