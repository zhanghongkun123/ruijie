package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto;

import com.ruijie.rcos.rcdc.rco.module.def.enums.OfflineAutoLockedEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/6
 *
 * @author zhangsiming
 */
public class OfflineLoginConfigDTO {

    @NotNull
    private Boolean enable;

    @NotNull
    private OfflineAutoLockedEnum offlineAutoLocked;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public OfflineAutoLockedEnum getOfflineAutoLocked() {
        return offlineAutoLocked;
    }

    public void setOfflineAutoLocked(OfflineAutoLockedEnum offlineAutoLocked) {
        this.offlineAutoLocked = offlineAutoLocked;
    }
}
