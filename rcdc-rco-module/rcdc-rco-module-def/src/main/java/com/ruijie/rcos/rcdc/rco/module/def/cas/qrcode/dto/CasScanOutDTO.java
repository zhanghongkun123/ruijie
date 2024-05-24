package com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto;

/**
 * Description: CAS扫码返回通用DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/24
 *
 * @author TD
 */
public class CasScanOutDTO {

    /**
     * 通用返回数据体
     */
    private String data;

    /**
     * 返回结果Boolean类型，true为成功，false为失败
     */
    private Boolean success;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
