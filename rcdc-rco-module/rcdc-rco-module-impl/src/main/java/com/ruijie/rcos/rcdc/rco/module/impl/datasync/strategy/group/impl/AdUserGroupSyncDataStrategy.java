package com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.group.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.datasync.enums.UserGroupTypeEnum;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/21 9:50
 *
 * @author coderLee23
 */
@Service
public class AdUserGroupSyncDataStrategy extends AbstractCommonUserGroupSyncDataStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdUserGroupSyncDataStrategy.class);

    @Override
    public UserGroupTypeEnum getUserGroupType() {
        return UserGroupTypeEnum.AD;
    }


}
