package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.Deencapsulation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/3 10:37
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class AdminLoginOnTerminalCacheTest {

    /**
     * 更新时间戳
     */
    @Test
    public void testUpdateTimeStamp() {
        AdminLoginOnTerminalCache cache = new AdminLoginOnTerminalCache();
        LocalDateTime originTime = LocalDateTime.now().minusHours(1L);
        Deencapsulation.setField(cache, "timestamp", originTime);
        cache.updateTimeStamp();
        LocalDateTime upgradedTime = cache.getTimestamp();
        Assert.assertNotEquals(0, upgradedTime.compareTo(originTime));
    }
}