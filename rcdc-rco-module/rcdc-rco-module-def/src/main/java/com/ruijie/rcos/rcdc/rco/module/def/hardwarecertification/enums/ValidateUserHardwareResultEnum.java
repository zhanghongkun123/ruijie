package com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.enums;

/**
 * 用户硬件特征码校验结果枚举
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public enum ValidateUserHardwareResultEnum {

    /** 通过 */
    SUCCESS(0, "通过"),

    /** 不通过，已驳回 */
    FAIL_REJECTED(1, "已驳回"),

    /** 不通过，待审批 */
    FAIL_PENDING_APPROVAL(2, "待审批"),

    /** 不通过，超过最大值 */
    FAIL_OVER_MAX(3, "超过最大值");

    private Integer code;
    private String message;

    ValidateUserHardwareResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
