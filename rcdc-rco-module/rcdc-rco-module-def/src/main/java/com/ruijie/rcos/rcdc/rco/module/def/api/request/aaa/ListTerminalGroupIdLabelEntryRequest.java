package com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月07日
 *
 * @author xiejian
 */
public class ListTerminalGroupIdLabelEntryRequest implements Request {

    @NotNull
    private UUID adminId;

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }
}
