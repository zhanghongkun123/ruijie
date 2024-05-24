package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rca.module.def.response.UserCloudDockConfigDTO;
import com.ruijie.rcos.rcdc.rca.module.def.spi.GetUserCloudDockConfigSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 获取用户云坞关系spi实现
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/5/30 4:09 下午
 *
 * @author zwd
 */
public class GetUserCloudDockConfigSPIImpl implements GetUserCloudDockConfigSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserCloudDockConfigSPIImpl.class);


    @Override
    public List<UserCloudDockConfigDTO> getUserCloudDockConfigByRcaStrategyId(UUID rcaStrategyId) throws BusinessException {
        Assert.notNull(rcaStrategyId, "rcaStrategyId cannot be null");

        return Lists.newArrayList();
    }

    @Override
    public UserCloudDockConfigDTO findFirstByUserId(UUID userId) {
        Assert.notNull(userId, "userId cannot be null");

        return new UserCloudDockConfigDTO();
    }

    @Override
    public Integer countUserRcaStrategyBindNum(UUID rcaStrategyId) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId cannot be null");
        //方法弃用
        return null;
    }
}
