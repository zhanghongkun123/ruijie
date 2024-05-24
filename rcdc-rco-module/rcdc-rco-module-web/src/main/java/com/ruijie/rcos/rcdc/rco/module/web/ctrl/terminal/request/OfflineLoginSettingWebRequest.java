package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.rcdc.rco.module.def.enums.OfflineAutoLockedEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/8 18:20
 *
 * @author conghaifeng
 */
public class OfflineLoginSettingWebRequest implements WebRequest {

    @NotNull
    private OfflineAutoLockedEnum offlineAutoLocked;

    public OfflineAutoLockedEnum getOfflineAutoLocked() {
        return offlineAutoLocked;
    }

    public void setOfflineAutoLocked(OfflineAutoLockedEnum offlineAutoLocked) {
        this.offlineAutoLocked = offlineAutoLocked;
    }

}
