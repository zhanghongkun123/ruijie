package com.ruijie.rcos.rcdc.rco.module.web.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/27 10:17
 *
 * @author conghaifeng
 */
@RunWith(SkyEngineRunner.class)
public class CapacityUnitUtilsTest {

    /**
     * 测试 gb2Mb
     */
    @Test
    public void testGb2Mb() {
        Assert.assertEquals(CapacityUnitUtils.gb2Mb(10D), 10240);
    }

    /**
     * 测试 mb2Gb
     */
    @Test
    public void testMb2Gb() {
        Double result = CapacityUnitUtils.mb2Gb(10240);
        Assert.assertEquals(result, Double.valueOf(10));
    }

}