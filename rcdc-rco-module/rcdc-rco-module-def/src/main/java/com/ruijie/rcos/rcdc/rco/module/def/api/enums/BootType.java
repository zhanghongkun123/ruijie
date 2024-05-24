package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

import org.springframework.util.Assert;

/**
 * Description: TCI开机模式
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/19 10:44
 *
 * @author yxq
 */
public enum BootType {

    /**
     * 兼容模式
     */
    UEFI("uefi"),

    /**
     * 极速模式
     */
    TC("tc"),

    /**
     * SHINE会转成UEFI模式再上传给RCDC
     */
    AUTO("auto");

    String type;

    BootType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 根据字符串type获取启动方式枚举
     *
     * @param type 字符串类型
     * @return BootType
     */
    public static BootType findByType(String type) {
        Assert.hasText(type, "type cannot be empty");
        for (BootType bootType : BootType.values()) {
            if (bootType.getType().equals(type)) {
                return bootType;
            }
        }
        throw new IllegalArgumentException("未定义的启动方式[" + type + "]");
    }
}
