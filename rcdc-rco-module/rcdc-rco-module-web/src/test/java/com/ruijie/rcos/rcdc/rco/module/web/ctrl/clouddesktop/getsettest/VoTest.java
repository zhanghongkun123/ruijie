package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.getsettest;

import static org.junit.Assert.assertTrue;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.NetworkDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.NetworkInfoDTO;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desknetwork.*;
import com.ruijie.rcos.sk.base.test.GetSetTester;

import mockit.integration.junit4.JMockit;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月25日
 * 
 * @author zouqi
 */
@RunWith(JMockit.class)
public class VoTest {
    
    /**
     * AddDeskIpPoolWebRequest 測試類
     * 
     */
    @Test
    public void testAddDeskIpPoolWebRequest() {
        GetSetTester tester = new GetSetTester(AddDeskIpPoolWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * CreateDeskNetworkWebRequest 測試類
     * 
     */
    @Test
    public void testCreateDeskNetworkWebRequest() {
        GetSetTester tester = new GetSetTester(CreateDeskNetworkWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * CbbDeleteDeskIpPoolRequest 測試類
     * 
     */
    @Test
    public void testDeleteDeskIpPoolWebRequest() {
        GetSetTester tester = new GetSetTester(DeleteDeskIpPoolWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * DeleteDeskNetworkBatchWebRequest 測試類
     * 
     */
    @Test
    public void testDeleteDeskNetworkBatchWebRequest() {
        GetSetTester tester = new GetSetTester(DeleteDeskNetworkBatchWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * DetailDeskNetworkWebRequest 測試類
     * 
     */
    @Test
    public void testDetailDeskNetworkWebRequest() {
        GetSetTester tester = new GetSetTester(DetailDeskNetworkWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * DetailIpPoolWebRequest 測試類
     * 
     */
    @Test
    public void testDetailIpPoolWebRequest() {
        GetSetTester tester = new GetSetTester(DetailIpPoolWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * EditDeskIpPoolWebRequest 測試類
     * 
     */
    @Test
    public void testEditDeskIpPoolWebRequest() {
        GetSetTester tester = new GetSetTester(EditDeskIpPoolWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * CbbAddDeskIpPoolRequest 測試類
     * 
     */
    @Test
    public void testEditDeskNetworkWebRequest() {
        GetSetTester tester = new GetSetTester(EditDeskNetworkWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * ListDeskIpPoolWebRequest 測試類
     * 
     */
    @Test
    public void testListDeskIpPoolWebRequest() {
        GetSetTester tester = new GetSetTester(ListDeskIpPoolWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * ListDeskNetworkWebRequest 測試類
     * 
     */
    @Test
    public void testListDeskNetworkWebRequest() {
        GetSetTester tester = new GetSetTester(ListDeskNetworkWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * CheckSoftNameDuplicationWebRequest 測試類
     * 
     */
    @Test
    public void testListCheckDuplicationWebRequest() {
        GetSetTester tester = new GetSetTester(CheckNetworkDuplicationWebRequest.class);
        tester.runTest();
        
        assertTrue(true);
    }
    
    /**
     * CbbNetworkConfigDTO 測試類
     * 
     */
    @Test
    public void testListCbbNetworkConfigDTO() {
        GetSetTester tester = new GetSetTester(CbbDeskNetworkConfigDTO.class);
        tester.runTest();
        
        assertTrue(true);
    }

    /**
     * NetworkDetailDTO 測試類
     *
     */
    @Test
    public void testNetworkDetailDTO() {
        GetSetTester tester = new GetSetTester(NetworkDetailDTO.class);
        tester.runTest();

        assertTrue(true);
    }

    /**
     * CbbNetworkConfigDTO 測試類
     *
     */
    @Test
    public void testNetworkInfoDTO() {
        GetSetTester tester = new GetSetTester(NetworkInfoDTO.class);
        tester.runTest();

        assertTrue(true);
    }
    
}
