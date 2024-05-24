package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/9
 *
 * @author hs
 */
public class BaseUpdateSystemTimeDTO {

    private Boolean shouldReboot;

    public BaseUpdateSystemTimeDTO(Boolean shouldReboot) {
        this.shouldReboot = shouldReboot;
    }

    public Boolean getShouldReboot() {
        return shouldReboot;
    }

    public void setShouldReboot(Boolean shouldReboot) {
        this.shouldReboot = shouldReboot;
    }
}
