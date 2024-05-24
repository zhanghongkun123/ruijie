package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/28
 *
 * @author hs
 */
public class UserVdiDesktopNumResponse extends DefaultResponse {

    private long vdiDesktopNum;

    public UserVdiDesktopNumResponse(long vdiDesktopNum) {
        this.vdiDesktopNum = vdiDesktopNum;
    }

    public long getVdiDesktopNum() {
        return vdiDesktopNum;
    }

    public void setVdiDesktopNum(long vdiDesktopNum) {
        this.vdiDesktopNum = vdiDesktopNum;
    }
}
