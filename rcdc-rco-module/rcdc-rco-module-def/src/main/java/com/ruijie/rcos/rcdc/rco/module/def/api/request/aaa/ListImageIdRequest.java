package com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月07日
 *
 * @author linrenjian
 */
public class ListImageIdRequest implements Request {


    public ListImageIdRequest() {

    }


    public ListImageIdRequest(UUID adminId) {
        this.adminId = adminId;
    }


    /**
     * 管理员ID
     */
    @NotNull
    private UUID adminId;

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }
}
