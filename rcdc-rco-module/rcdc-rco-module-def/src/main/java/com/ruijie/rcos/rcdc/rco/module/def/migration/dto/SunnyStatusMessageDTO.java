package com.ruijie.rcos.rcdc.rco.module.def.migration.dto;


/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/1
 *
 * @author chenl
 */
public class SunnyStatusMessageDTO {


    private Integer code;

    private String message;

    private SunnyStatusDTO content;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SunnyStatusDTO getContent() {
        return content;
    }

    public void setContent(SunnyStatusDTO content) {
        this.content = content;
    }
}
