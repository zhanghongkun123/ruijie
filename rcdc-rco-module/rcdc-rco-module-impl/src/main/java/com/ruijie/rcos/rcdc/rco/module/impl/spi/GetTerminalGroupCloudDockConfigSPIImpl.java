package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rca.module.def.response.TerminalGroupCloudDockConfigDTO;
import com.ruijie.rcos.rcdc.rca.module.def.spi.GetTerminalGroupCloudDockConfigSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 获取终端组云坞关系spi实现
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/5/30 4:09 下午
 *
 * @author zwd
 */
public class GetTerminalGroupCloudDockConfigSPIImpl implements GetTerminalGroupCloudDockConfigSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetTerminalGroupCloudDockConfigSPIImpl.class);



    @Override
    public List<TerminalGroupCloudDockConfigDTO> getTerminalGroupCloudDockConfigByRcaStrategyId(UUID rcaStrategyId) throws BusinessException {
        Assert.notNull(rcaStrategyId, "rcaStrategyId cannot be null");

        return Lists.newArrayList();
    }

    @Override
    public TerminalGroupCloudDockConfigDTO findFirstByTerminalGroupId(UUID terminalGroupId) {
        Assert.notNull(terminalGroupId, "terminalGroupId cannot be null");

        return new TerminalGroupCloudDockConfigDTO();
    }

    @Override
    public Integer countTerminalGroupRcaStrategyBindNum(UUID rcaStrategyId) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId cannot be null");

        //方法弃用
        return null;
    }
}
