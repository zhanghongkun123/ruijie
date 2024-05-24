package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PlatformOverviewDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Description: 平台总览信息缓存
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/30 14:25
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class PlatformOverviewCacheTest {

    @Tested
    private PlatformOverviewCache platformOverviewCache;

    /**
     * 测试addCache、getCache方法
     * @throws BusinessException 异常
     */
    @Test
    public void testAddCacheAndGetCache() throws BusinessException {
        final PlatformOverviewDTO dto = new PlatformOverviewDTO();
        platformOverviewCache.addCache(dto);
        assertEquals(dto, platformOverviewCache.getCache());
    }
}
