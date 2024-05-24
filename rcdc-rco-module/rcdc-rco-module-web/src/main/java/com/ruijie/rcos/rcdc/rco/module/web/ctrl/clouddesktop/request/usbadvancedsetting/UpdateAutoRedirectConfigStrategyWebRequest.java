package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbadvancedsetting;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUSBAutoRedirectType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月17日
 * 
 * @author zhuangchenwu
 */
public class UpdateAutoRedirectConfigStrategyWebRequest implements WebRequest {

    @NotNull
    private CbbUSBAutoRedirectType autoRedirectType;

    public CbbUSBAutoRedirectType getAutoRedirectType() {
        return autoRedirectType;
    }

    public void setAutoRedirectType(CbbUSBAutoRedirectType autoRedirectType) {
        this.autoRedirectType = autoRedirectType;
    }
    
}
