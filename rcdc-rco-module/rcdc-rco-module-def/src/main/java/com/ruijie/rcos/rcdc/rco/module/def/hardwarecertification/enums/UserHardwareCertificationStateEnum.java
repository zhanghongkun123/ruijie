package com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.enums;

/**
 * 用户硬件特征码状态枚举
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public enum UserHardwareCertificationStateEnum {

    /** 已批准 */
    APPROVED("已批准"),

    /** 已驳回 */
    REJECTED("已驳回"),

    /** 待审批 */
    PENDING_APPROVAL("待审批");

    private String text;

    UserHardwareCertificationStateEnum(String text) {
        this.text = text;
    }

    public String getMessage() {
        return text;
    }
}
