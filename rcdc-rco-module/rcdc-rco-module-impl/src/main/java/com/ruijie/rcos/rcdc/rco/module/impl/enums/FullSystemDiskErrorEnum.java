package com.ruijie.rcos.rcdc.rco.module.impl.enums;

import org.springframework.lang.Nullable;

import java.util.Arrays;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 17:08
 *
 * @author yxq
 */
public enum FullSystemDiskErrorEnum {

    SUCCESS(0),

    /**
     * 存在本地盘
     */
    EXISTS_LOCAL_DISK(1),

    /**
     * 终端不存在云桌面
     */
    NOT_EXISTS_CLOUD_DESK(2),

    /**
     * 只支持WIN7和WIN10
     */
    NOT_SUPPORT_OS(3),

    /**
     * 终端上桌面ID和服务器上桌面ID不一致
     */
    NOT_SAME_DESK(4),

    UNKNOWN(99);

    private Integer code;

    FullSystemDiskErrorEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 根据code获取枚举类
     *
     * @param code 错误码
     * @return 枚举
     */
    public static FullSystemDiskErrorEnum getByCode(@Nullable Integer code) {
        return Arrays.stream(FullSystemDiskErrorEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(UNKNOWN);
    }
}
