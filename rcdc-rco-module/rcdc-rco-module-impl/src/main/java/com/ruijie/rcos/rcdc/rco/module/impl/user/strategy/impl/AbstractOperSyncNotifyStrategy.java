package com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolUserAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.DataSyncService;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.DiskPoolUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.UserOperSyncNotifyStrategy;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/26 15:07
 *
 * @author coderLee23
 */
public abstract class AbstractOperSyncNotifyStrategy implements UserOperSyncNotifyStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperSyncNotifyStrategy.class);


    @Autowired
    protected IacUserMgmtAPI userAPI;

    @Autowired
    protected IacUserGroupMgmtAPI userGroupAPI;

    @Autowired
    protected IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    protected UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    protected UwsDockingAPI uwsDockingAPI;

    @Autowired
    protected UserNotifyAPI userNotifyAPI;

    @Autowired
    protected RccmManageService rccmManageService;

    @Autowired
    protected WebclientNotifyAPI webclientNotifyAPI;

    @Autowired
    protected UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    protected DiskPoolUserAPI diskPoolUserAPI;

    @Autowired
    protected DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    protected DiskPoolUserDAO diskPoolUserDAO;

    @Autowired
    protected DataSyncService dataSyncService;

}
