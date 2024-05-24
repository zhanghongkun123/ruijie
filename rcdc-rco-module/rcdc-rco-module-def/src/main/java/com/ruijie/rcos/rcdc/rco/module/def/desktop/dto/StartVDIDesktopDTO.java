package com.ruijie.rcos.rcdc.rco.module.def.desktop.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;

/**
 * Description: VDI启动参数DTO类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/03/02
 *
 * @author linke
 */
public class StartVDIDesktopDTO extends AbstractStartDesktopDTO {

    /**
     * 云桌面模式：个性/还原
     */
    private CbbCloudDeskPattern cbbCloudDeskPattern;


    public CbbCloudDeskPattern getCbbCloudDeskPattern() {
        return cbbCloudDeskPattern;
    }

    public void setCbbCloudDeskPattern(CbbCloudDeskPattern cbbCloudDeskPattern) {
        this.cbbCloudDeskPattern = cbbCloudDeskPattern;
    }
}
