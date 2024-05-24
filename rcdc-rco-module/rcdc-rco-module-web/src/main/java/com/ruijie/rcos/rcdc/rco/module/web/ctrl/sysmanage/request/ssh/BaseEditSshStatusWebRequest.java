package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.ssh;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年04月20日
 *
 * @author GuoZhouYue
 */
public class BaseEditSshStatusWebRequest implements WebRequest {

    @NotNull
    Boolean hasSwitchOn;

    public Boolean getHasSwitchOn() {
        return hasSwitchOn;
    }

    public void setHasSwitchOn(Boolean hasSwitchOn) {
        this.hasSwitchOn = hasSwitchOn;
    }
}
