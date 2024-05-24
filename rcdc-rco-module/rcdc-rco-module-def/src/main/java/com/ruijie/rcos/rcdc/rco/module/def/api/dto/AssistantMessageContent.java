package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月17日
 *
 * @author ketb
 */
public class AssistantMessageContent {

    private Integer code;

    private String message;

    private Object content;

    private Integer type;

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

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "code = " + code + ", message = " + message + ", type = " + type;
    }

}
