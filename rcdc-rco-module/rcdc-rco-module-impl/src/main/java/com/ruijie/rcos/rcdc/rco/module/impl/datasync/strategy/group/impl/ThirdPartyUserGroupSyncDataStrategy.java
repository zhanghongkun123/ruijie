package com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.group.impl;


import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.rco.module.impl.datasync.enums.UserGroupTypeEnum;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 19:07
 *
 * @author coderLee23
 */
@Service
public class ThirdPartyUserGroupSyncDataStrategy extends AbstractCommonUserGroupSyncDataStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyUserGroupSyncDataStrategy.class);

    @Override
    public UserGroupTypeEnum getUserGroupType() {
        return UserGroupTypeEnum.THIRD_PARTY;
    }

}
