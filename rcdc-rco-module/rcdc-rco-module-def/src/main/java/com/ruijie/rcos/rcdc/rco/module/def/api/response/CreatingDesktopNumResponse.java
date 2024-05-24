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
public class CreatingDesktopNumResponse extends DefaultResponse {

    private int desktopNum;

    public CreatingDesktopNumResponse(int desktopNum) {
        this.desktopNum = desktopNum;
    }

    public int getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(int desktopNum) {
        this.desktopNum = desktopNum;
    }
}
