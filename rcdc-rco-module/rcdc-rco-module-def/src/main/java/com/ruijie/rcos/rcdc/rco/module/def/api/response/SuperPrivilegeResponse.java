package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/02/07
 *
 * @author ljm
 */
public class SuperPrivilegeResponse extends DefaultResponse {


    private boolean superPrivilege;

    public SuperPrivilegeResponse(boolean superPrivilege) {
        this.superPrivilege = superPrivilege;
    }

    public boolean isSuperPrivilege() {
        return superPrivilege;
    }

    public void setSuperPrivilege(boolean superPrivilege) {
        this.superPrivilege = superPrivilege;
    }
}
