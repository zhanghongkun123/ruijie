package com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.batchtask;

import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/17 22:26
 *
 * @author zhangsiming
 */
public interface UserSnapshotOperationListener {

    /**
     * @param actionCode est操作方法
     * @param terminalId 终端ID
     * @param deskId 桌面id
     */
    void onFinish(String actionCode, String terminalId, UUID deskId);

    /**
     * @param actionCode est操作方法
     * @param terminalId 终端ID
     * @param deskId 桌面id
     * @param msg 错误信息
     */
    void onException(String actionCode, String terminalId, UUID deskId, @Nullable String msg);
}
