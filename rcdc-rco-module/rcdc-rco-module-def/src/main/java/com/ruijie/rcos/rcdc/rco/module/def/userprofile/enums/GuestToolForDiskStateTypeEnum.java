package com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums;

/**
 * Description: 个人盘异常状态
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/18
 *
 * @author zwf
 */
public enum GuestToolForDiskStateTypeEnum {

    /** 挂载失败 */
    FAIL(1),

    /** 忽略 */
    IGNORE(2),

    /** 禁用 */
    BAN(3),

    /** 不存在 */
    WITHOUT(4);

    GuestToolForDiskStateTypeEnum(Integer code) {
        this.code = code;
    }

    private Integer code;

    public Integer getCode() {
        return code;
    }
}
