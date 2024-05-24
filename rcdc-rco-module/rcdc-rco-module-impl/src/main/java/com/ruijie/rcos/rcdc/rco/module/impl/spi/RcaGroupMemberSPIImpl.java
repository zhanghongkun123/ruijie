package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rca.module.def.api.*;
import com.ruijie.rcos.rcdc.rca.module.def.spi.RcaGroupMemberSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 应用分组SPI实现类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月16日
 *
 * @author zhengjingyong
 */
public class RcaGroupMemberSPIImpl implements RcaGroupMemberSPI {

    @Autowired
    private RcaGroupMemberAPI rcaGroupMemberAPI;

    @Autowired
    private RcaUserCustomStrategyAPI rcaUserCustomStrategyAPI;

    @Autowired
    private RcaGroupMemberHostSessionAPI rcaGroupMemberHostSessionAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private RcaPeripheralStrategyAPI rcaPeripheralStrategyAPI;

    @Override
    public void unbindUser(UUID poolId, UUID groupId, List<UUID> userIdList) throws BusinessException {
        Assert.notNull(poolId, "poolId can not be null");
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notEmpty(userIdList, "userIdList can not be empty");

        rcaGroupMemberAPI.unbindUserPostProcessor(poolId, groupId, userIdList);

        rcaUserCustomStrategyAPI.deleteUserCustomStrategyDetail(poolId, userIdList);

        rcaMainStrategyAPI.deleteSlaveAffiliationOfPoolIdAndUserIdIn(poolId, userIdList);
        rcaPeripheralStrategyAPI.deleteSlaveAffiliationOfPoolIdAndUserIdIn(poolId, userIdList);

        for (UUID userId : userIdList) {
            rcaGroupMemberHostSessionAPI.clearOfflineSessionWhenUnbind(userId);
        }


    }
}
