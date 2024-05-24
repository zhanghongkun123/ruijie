package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbNoticeRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年2月22日
 *
 * @author wjp
 */
@RunWith(SkyEngineRunner.class)
public class ComputerOfflineSPIImplTest {

    @Tested
    private ComputerOfflineSPIImpl computerOfflineSPIImpl;

    @Injectable
    private ComputerBusinessDAO computerBusinessDAO;

    /**
     * testNotifyRequestIsNull
     * 
     * @throws Exception 异常
     */
    @Test
    public void testNotifyRequestIsNull() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> computerOfflineSPIImpl.notify(null), "request can not be null.");
        assertTrue(true);

        final CbbNoticeRequest request = new CbbNoticeRequest();
        CbbShineTerminalBasicInfo terminalBasicInfo  = new CbbShineTerminalBasicInfo();
        terminalBasicInfo.setTerminalId("");
        request.setTerminalBasicInfo(terminalBasicInfo);
        ThrowExceptionTester.throwIllegalArgumentException(() -> computerOfflineSPIImpl.notify(request), "terminalId can not be blank.");
        assertTrue(true);
    }

    /**
     * testNotifyCannotFindComputer
     * 
     * @throws BusinessException 异常
     */
    @Test
    public void testNotifyCannotFindComputer() throws Exception {
        final CbbNoticeRequest request = new CbbNoticeRequest();
        CbbShineTerminalBasicInfo terminalBasicInfo  = new CbbShineTerminalBasicInfo();
        terminalBasicInfo.setTerminalId("111");
        request.setTerminalBasicInfo(terminalBasicInfo);
        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityByMac(anyString);
                result = null;
            }
        };
        computerOfflineSPIImpl.notify(request);
        new Verifications() {
            {
                computerBusinessDAO.save((ComputerEntity) any);
                times = 0;
            }
        };
    }

    /**
     * testNotifyOffline
     * 
     * @throws BusinessException 异常
     */
    @Test
    public void testNotifyOffline() throws Exception {
        final CbbNoticeRequest request = new CbbNoticeRequest();
        CbbShineTerminalBasicInfo terminalBasicInfo  = new CbbShineTerminalBasicInfo();
        terminalBasicInfo.setTerminalId("111");
        request.setTerminalBasicInfo(terminalBasicInfo);
        final ComputerEntity computerEntity = new ComputerEntity();
        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityByMac(anyString);
                result = computerEntity;
            }
        };
        computerOfflineSPIImpl.notify(request);
        new Verifications() {
            {
                computerBusinessDAO.save((ComputerEntity) any);
                times = 1;
            }
        };
    }
}
