package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 13:55
 *
 * @author ketb
 */
public class ComputerIdArrWebRequest implements WebRequest {

    @NotNull
    private String[] idArr;

    public ComputerIdArrWebRequest() {
    }

    public ComputerIdArrWebRequest(String[] idArr) {
        this.idArr = idArr;
    }

    public String[] getIdArr() {
        return idArr;
    }

    public void setIdArr(String[] idArr) {
        this.idArr = idArr;
    }
}
