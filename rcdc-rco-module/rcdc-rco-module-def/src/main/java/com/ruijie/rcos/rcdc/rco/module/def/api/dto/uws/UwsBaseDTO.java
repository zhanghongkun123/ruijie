package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

/**
 * Description: UWS 统一字段
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-22 18:31:00
 *
 * @author zjy
 */
public class UwsBaseDTO {

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UwsBaseDTO() {
    }

    public UwsBaseDTO(int code) {
        this.code = code;
    }
}
