package com.ruijie.rcos.rcdc.rco.module.def.api.response.rccm;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: RccmManageResponse
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
public class RccmManageResponse extends DefaultResponse {

    private int code;

    private String msg;

    public RccmManageResponse(int code) {
        this.code = code;
    }

    public RccmManageResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
