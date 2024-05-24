package com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.tx;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopSessionLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserConnectDesktopFaultLogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 桌面池报表Service事务接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/17 15:39
 *
 * @author yxq
 */
@Service
public class DesktopPoolDashboardServiceTxImpl implements DesktopPoolDashboardServiceTx {

    @Autowired
    private DesktopSessionLogDAO desktopSessionLogDAO;

    @Autowired
    private UserConnectDesktopFaultLogDAO userConnectDesktopFaultLogDAO;

    @Override
    public void updateUserGroupName(UUID userGroupId, @Nullable String userGroupName) {
        Assert.notNull(userGroupId, "userGroupId must not be null");

        desktopSessionLogDAO.updateUserGroupName(userGroupId, userGroupName);
        userConnectDesktopFaultLogDAO.updateUserGroupName(userGroupId, userGroupName);
    }

    @Override
    public void updateDesktopPoolName(UUID desktopPoolId, @Nullable String desktopPoolName) {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");

        desktopSessionLogDAO.updateDesktopPoolName(desktopPoolId, desktopPoolName);
        userConnectDesktopFaultLogDAO.updateDesktopPoolName(desktopPoolId, desktopPoolName);
    }
}
