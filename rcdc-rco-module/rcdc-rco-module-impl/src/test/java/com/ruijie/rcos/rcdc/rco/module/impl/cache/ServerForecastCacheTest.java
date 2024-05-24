package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import static org.junit.Assert.assertEquals;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.ServerForecastDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import java.util.UUID;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: 服务器预测数据缓存
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/30 14:32
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class ServerForecastCacheTest {

    @Tested
    private ServerForecastCache serverForecastCache;

    /**
     * 测试addCache、getCache方法
     */
    @Test
    public void testAddCacheAndGetCache() {
        ServerForecastDTO dto = new ServerForecastDTO();
        UUID uuid = UUID.randomUUID();
        serverForecastCache.addCache(uuid, ResourceTypeEnum.CPU, dto);
        assertEquals(dto, serverForecastCache.getCache(uuid, ResourceTypeEnum.CPU));
    }

    /**
     * 测试清空缓存方法
     */
    @Test
    public void testClear() {
        ServerForecastDTO dto = new ServerForecastDTO();
        UUID uuid = UUID.randomUUID();
        serverForecastCache.addCache(uuid, ResourceTypeEnum.CPU, dto);
        serverForecastCache.clear();
        ServerForecastDTO serverForecastDTO = serverForecastCache.getCache(uuid, ResourceTypeEnum.CPU);
        Assert.assertTrue(true);
    }

}
