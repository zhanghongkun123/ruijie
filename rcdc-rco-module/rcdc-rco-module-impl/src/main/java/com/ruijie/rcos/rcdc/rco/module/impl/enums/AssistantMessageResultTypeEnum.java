package com.ruijie.rcos.rcdc.rco.module.impl.enums;

/**
 * 消息操作结果枚举
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月18日
 * 
 * @author ketb
 */
public enum AssistantMessageResultTypeEnum {

    /** 成功 */
    SUCCESS(0, "success"),

    /** 失败 */
    FAIL(1, "fail");

    AssistantMessageResultTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
