package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * 获取密码返回 gt 的信息枚举
 */
public enum UserPasswordMessageEnum {

    MESSAGE_SUCCESS(0, ""),
    MESSAGE_FAILURE(1, ""),//其他错误
    MESSAGE_USER_NOT_EXIST(2, "user_not_exist"),//用户不存在
    MESSAGE_DESK_NOT_EXIST(3, "desk_not_exist");//桌面不存在

    private Integer code;
    private String message;


    UserPasswordMessageEnum(Integer errorCode, String message) {
        this.code = errorCode;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }



}
