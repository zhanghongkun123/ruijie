package com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.LicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.request.KmsRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.thrift.kms.response.KmsClientResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.test.GetSetTester;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * DeskNetworkCtrl类的单元测试类
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年3月23日 <br>
 *
 * @author dan
 */
@RunWith(JMockit.class)
public class KmsServerImplTest {

    @Tested
    private KmsServerImpl kmsServer;

    @Injectable
    private LicenseAPI licenseAPI;

    /**
     * 测试getWindowsAuth方法 kms授权开启
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testGetWindowsAuth() throws BusinessException {

        new Expectations(DefaultWebResponse.Builder.class) {
            {
                licenseAPI.obtainEnableOpenKms();
                result = true;
                
            }
        };
        
        KmsClientResponse kmsResponse = kmsServer.getWindowsAuth(new KmsRequest());

        new Verifications() {
            {
                licenseAPI.obtainEnableOpenKms();
                times = 1;
            }
        };
        
        assertEquals(kmsResponse.getKmsStatus().intValue(), 0);
    }
    
    /**
     * 测试getWindowsAuth方法 kms授权关闭
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testGetWindowsAuth2() throws BusinessException {

        new Expectations(DefaultWebResponse.Builder.class) {
            {
                licenseAPI.obtainEnableOpenKms();
                result = false;
                
            }
        };
        
        KmsClientResponse kmsResponse = kmsServer.getWindowsAuth(new KmsRequest());

        new Verifications() {
            {
                licenseAPI.obtainEnableOpenKms();
                times = 1;
            }
        };
        
        assertEquals(kmsResponse.getKmsStatus().intValue(), 1);
    }
    
    /**
     * KmsRequest 測試類
     * 
     */
    @Test
    public void testKmsRequest() {
        GetSetTester tester = new GetSetTester(KmsRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * KmsRequest 測試類
     * 
     */
    @Test
    public void testKmsClientResponse() {
        GetSetTester tester = new GetSetTester(KmsClientResponse.class);
        tester.runTest();
        
        assertTrue(true);
    }
}
