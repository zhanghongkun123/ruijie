package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rca.module.def.response.TerminalCloudDockConfigDTO;
import com.ruijie.rcos.rcdc.rca.module.def.spi.GetTerminalCloudDockConfigSPI;
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
public class GetTerminalCloudDockConfigSPIImpl implements GetTerminalCloudDockConfigSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTerminalCloudDockConfigSPIImpl.class);



    @Override
    public TerminalCloudDockConfigDTO findFirstByTerminalId(String terminalId) {
        Assert.notNull(terminalId, "terminalId cannot be null");

        return new TerminalCloudDockConfigDTO();
    }

    @Override
    public List<TerminalCloudDockConfigDTO> getTerminalCloudDockConfigByRcaStrategyId(UUID rcaStrategyId) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId cannot be null");

        return Lists.newArrayList();
    }

    @Override
    public Integer countTerminalRcaStrategyBindNum(UUID rcaStrategyId) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId cannot be null");

        //方法弃用
        return null;
    }
}
