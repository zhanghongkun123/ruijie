package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月16日
 *
 * @author ljm
 */
public class SuperPrivilegeRequest implements Request {

    @NotNull
    private UUID[] roleIdArr;

    public SuperPrivilegeRequest() {

    }

    public SuperPrivilegeRequest(UUID[] roleIdArr) {
        this.roleIdArr = roleIdArr;
    }

    public UUID[] getRoleIdArr() {
        return roleIdArr;
    }

    public void setRoleIdArr(UUID[] roleIdArr) {
        this.roleIdArr = roleIdArr;
    }
}
