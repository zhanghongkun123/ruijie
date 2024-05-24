package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rca.module.def.spi.GetTerminalIpLimitSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.IpLimitAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 获取终端IP登录限制SPI实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/24 下午5:58
 *
 * @author yanlin
 */
public class GetTerminalIpLimitSPIImpl implements GetTerminalIpLimitSPI {

    @Autowired
    private IpLimitAPI ipLimitAPI;

    @Override
    public Boolean isIpLimitEnable() throws BusinessException {
        return ipLimitAPI.isIpLimitEnable(null);
    }

    @Override
    public Boolean isIpLimited(String ip) throws BusinessException {
        Assert.notNull(ip, "terminal ip cannot be null");
        return ipLimitAPI.isIpLimited(ip, (UUID)null);
    }
}
