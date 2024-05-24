package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.tx;

import java.util.UUID;

/**
 * Description: 桌面池报表Service事务接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/17 15:39
 *
 * @author yxq
 */
public interface DesktopPoolDashboardServiceTx {


    /**
     * 修改会话连接表、连接失败表中的用户组名为新的名称
     *
     * @param userGroupId 用户组id
     * @param userGroupName 新的用户组名
     */
    void updateUserGroupName(UUID userGroupId, String userGroupName);

    /**
     * 修改会话连接表、连接失败表中的桌面池名称名为新的名称
     *
     * @param desktopPoolId 用户组id
     * @param desktopPoolName 新的桌面池名称
     */
    void updateDesktopPoolName(UUID desktopPoolId, String desktopPoolName);
}
