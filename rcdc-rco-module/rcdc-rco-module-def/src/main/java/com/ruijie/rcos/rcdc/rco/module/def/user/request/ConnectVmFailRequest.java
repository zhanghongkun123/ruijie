package com.ruijie.rcos.rcdc.rco.module.def.user.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/8 14:07
 *
 * @author zdc
 */
public class ConnectVmFailRequest {

    /**
     * 桌面id
     */
    @NotNull
    private UUID deskId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
