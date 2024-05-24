package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.log.Logger;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月09日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class AbstractPageQueryAllHandleTest {
    @Tested
    private AbstractPageQueryAllHandle abstractPageQueryAllHandle;


    /**
     * 测试queryAll方法
     * 
     * @param logger 日志对象
     * @throws Exception 异常
     */
    @Test
    public void testQueryAll(@Mocked Logger logger) throws Exception {
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            resultList.add(String.valueOf(i));
        }
        new Expectations(abstractPageQueryAllHandle) {
            {
                abstractPageQueryAllHandle.queryByPage((Pageable) any);
                returns(resultList, Arrays.asList("String"));
            }
        };
        Set<String> resultSet = abstractPageQueryAllHandle.queryAll();
        for (int i = 0; i < 1000; i++) {
            Assert.assertTrue(resultSet.contains(String.valueOf(i)));
        }
        Assert.assertTrue(resultSet.contains("String"));
        new Verifications() {
            {
                abstractPageQueryAllHandle.queryByPage((Pageable) any);
                times = 2;
            }
        };
    }

    /**
     * 测试queryAll方法当结果为空时
     * 
     * @param logger 日志对象
     * @throws Exception 异常
     */
    @Test
    public void testQueryAllWhileResultIsEmpty(@Mocked Logger logger) throws Exception {

        new Expectations(abstractPageQueryAllHandle) {
            {
                abstractPageQueryAllHandle.queryByPage((Pageable) any);
                result = new ArrayList<>();
            }
        };
        abstractPageQueryAllHandle.queryAll();
        Set<String> resultSet = abstractPageQueryAllHandle.queryAll();

        Assert.assertTrue(resultSet.size() == 0);
        new Verifications() {
            {
                abstractPageQueryAllHandle.queryByPage((Pageable) any);
                times = 2;
            }
        };
    }

}
