package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-02-25 0:05
 */
@RunWith(JMockit.class)
public class DesServiceImplTest {

    @Tested
    private DesServiceImpl desService;

    @Test
    public void testBCryptEncrypt() {
        final String password = "password";
        try {
            desService.bCryptEncrypt(password);
        } catch (Exception e) {
            Assert.assertEquals(password, e.getMessage());
        }

    }







}
