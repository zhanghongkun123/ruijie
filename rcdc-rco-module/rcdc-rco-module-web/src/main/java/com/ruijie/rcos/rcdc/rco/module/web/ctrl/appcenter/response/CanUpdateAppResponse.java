package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年03月31日
 *
 * @author xgx
 */
public class CanUpdateAppResponse {
    private boolean cauUpdateApp;

    public CanUpdateAppResponse(boolean cauUpdateApp) {
        this.cauUpdateApp = cauUpdateApp;
    }

    public boolean isCauUpdateApp() {
        return cauUpdateApp;
    }

    public void setCauUpdateApp(boolean cauUpdateApp) {
        this.cauUpdateApp = cauUpdateApp;
    }
}
