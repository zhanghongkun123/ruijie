package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoGlobalParameterDAO;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;

import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/21
 *
 * @author chen zj
 */
@RunWith(SkyEngineRunner.class)
public class GlobalParameterServiceImplTest {

    @Tested
    private GlobalParameterServiceImpl impl;

    @Injectable
    private RcoGlobalParameterDAO rcoGlobalParameterDAO;

    /**
     * testUpdateParameter
     *
     * @throws Exception 异常
     */
    @Test
    public void testUpdateParameter() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> impl.updateParameter(null, ""), "paramKey is not empty");

        impl.updateParameter("paramKey", "value");
        Assert.assertTrue(true);

        new Verifications() {
            {
                rcoGlobalParameterDAO.updateValueByParamKey(anyString, anyString);
                times = 1;
            }
        };
    }


    /**
     * testFindParameter
     *
     * @throws Exception 异常
     */
    @Test
    public void testFindParameter() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> impl.findParameter(null), "key is not empty");

        impl.findParameter("paramKey");
        Assert.assertTrue(true);

        new Verifications() {
            {
                rcoGlobalParameterDAO.findValueByParamKey(anyString);
                times = 1;
            }
        };
    }


}
