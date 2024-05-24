package com.ruijie.rcos.rcdc.rco.module.impl.printer.enums;

/**
 * 打印机返回 gt 的信息枚举
 */
public enum PrinterConfigMessageEnum {

    MESSAGE_SUCCESS(0, "S"),
    MESSAGE_FAILURE(1, "F"),//其他错误
    MESSAGE_MANAGER_CLOSE(2, "manager_close"),//打印机配置未打开
    MESSAGE_SAME_NAME(3, "same_name"),//有同名但不覆盖
    MESSAGE_WRONG_DETAIL_MD5(1,"wrong_detail_md5"); //detail的md5不匹配

    private Integer code;
    private String message;


    PrinterConfigMessageEnum(Integer errorCode, String message) {
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
