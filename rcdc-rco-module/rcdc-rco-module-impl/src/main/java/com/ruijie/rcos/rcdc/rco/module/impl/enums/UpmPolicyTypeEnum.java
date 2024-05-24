package com.ruijie.rcos.rcdc.rco.module.impl.enums;

/**
 * Description: UPM启动机制枚举
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/28
 *
 * @author zwf
 */
public enum UpmPolicyTypeEnum {
    /** UPM不能启动 */
    UPM_UNABLE_START(0),

    /** UPM自动启动 */
    UPM_AUTO_START(1),

    /** UPM非自动登录 */
    UPM_MANUAL_START(2);

    private Integer code;

    UpmPolicyTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
