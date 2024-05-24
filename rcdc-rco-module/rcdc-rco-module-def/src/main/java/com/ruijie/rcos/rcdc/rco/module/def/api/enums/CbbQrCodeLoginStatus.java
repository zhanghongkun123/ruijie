package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月16日
 *
 * @author xgx
 */
public enum CbbQrCodeLoginStatus {
    /**
     * 待扫描
     */
    NO_SCAN,
    /**
     * 已扫码待确认
     */
    SCANNED,
    /**
     * 已确认
     */
    CONFIRMED,
    /**
     * 已登录
     */
    LOGIN,
    /**
     * 已失效
     */
    INVALID;
}
