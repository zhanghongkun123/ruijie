package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dto;

/**
 * Description: SoftwareControlGuestToolMsgDTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/28
 *
 * @author wuShengQiang
 */
public class SoftwareControlGuestToolMsgDTO {

    private int code;

    private String message;

    private SoftwareControlGuestToolMsgContentDTO content;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SoftwareControlGuestToolMsgContentDTO getContent() {
        return content;
    }

    public void setContent(SoftwareControlGuestToolMsgContentDTO content) {
        this.content = content;
    }

}
