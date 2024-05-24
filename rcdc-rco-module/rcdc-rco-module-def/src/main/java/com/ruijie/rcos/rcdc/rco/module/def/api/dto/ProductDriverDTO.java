package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalModelDTO;

/**
 * Description: 产品驱动类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/4
 *
 * @author songxiang
 */
public class ProductDriverDTO extends CbbTerminalModelDTO {

    private Boolean install;

    private String hardwareVersion;

    public Boolean getInstall() {
        return install;
    }

    public void setInstall(Boolean install) {
        this.install = install;
    }

    @Override
    public String getHardwareVersion() {
        return hardwareVersion;
    }

    @Override
    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }
}
