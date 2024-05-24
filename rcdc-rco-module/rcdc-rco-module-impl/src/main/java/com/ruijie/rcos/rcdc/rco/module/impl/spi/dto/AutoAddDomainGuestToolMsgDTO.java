package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

/**
 * Description: IDV桌面 SHINE => CDC 获取域控信息请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/10
 *
 * @author WuShengQiang
 */
public class AutoAddDomainGuestToolMsgDTO {

    private int code;

    private String message;

    private AutoAddDomainRequestDTO content;

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

    public AutoAddDomainRequestDTO getContent() {
        return content;
    }

    public void setContent(AutoAddDomainRequestDTO content) {
        this.content = content;
    }
}
