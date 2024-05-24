package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rca.module.def.response.UserGroupCloudDockConfigDTO;
import com.ruijie.rcos.rcdc.rca.module.def.spi.GetUserGroupCloudDockConfigSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 获取用户组云坞关系spi实现
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/5/30 4:09
 *
 * @author zwd
 */
public class GetUserGroupCloudDockConfigSPIImpl implements GetUserGroupCloudDockConfigSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserGroupCloudDockConfigSPIImpl.class);



    @Override
    public List<UserGroupCloudDockConfigDTO> getUserGroupCloudDockConfigByRcaStrategyId(UUID rcaStrategyId) throws BusinessException {
        Assert.notNull(rcaStrategyId, "rcaStrategyId cannot be null");

        return Lists.newArrayList();
    }

    @Override
    public Integer countUserGroupRcaStrategyBindNum(UUID rcaStrategyId) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId cannot be null");
        //方法弃用
        return null;
    }
}
