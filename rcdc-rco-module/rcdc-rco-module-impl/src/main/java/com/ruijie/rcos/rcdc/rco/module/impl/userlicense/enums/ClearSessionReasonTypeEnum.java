package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.enums;

/**
 * Description:会话清理原因类型
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月26日
 *
 * @author lihengjing
 */
public enum ClearSessionReasonTypeEnum {
    /**
     * 终端离线
     */
    TERMINAL_OFFLINE,
    /**
     * 资源离线
     */
    RESOURCE_OFFLINE,
    /**
     * 超时未上报
     */
    TIMEOUT_REPORT,
    /**
     * 客户端不存在会话清空
     */
    TERMINAL_CLEAR
}
