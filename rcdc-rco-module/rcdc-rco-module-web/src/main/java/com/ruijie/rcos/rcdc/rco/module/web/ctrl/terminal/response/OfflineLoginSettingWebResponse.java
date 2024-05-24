package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response;

import com.ruijie.rcos.rcdc.rco.module.def.enums.OfflineAutoLockedEnum;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/2 14:40
 *
 * @author conghaifeng
 */
public class OfflineLoginSettingWebResponse extends DefaultWebResponse {

    private OfflineAutoLockedEnum offlineAutoLocked;

    public OfflineAutoLockedEnum getOfflineAutoLocked() {
        return offlineAutoLocked;
    }

    public void setOfflineAutoLocked(OfflineAutoLockedEnum offlineAutoLocked) {
        this.offlineAutoLocked = offlineAutoLocked;
    }

}
