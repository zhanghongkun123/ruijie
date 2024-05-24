package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.CancelRequestRemoteAssistSrcTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.request.CancelRequestRemoteAssistRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.PublicBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-03-27 16:52
 */

@RunWith(JMockit.class)
public class ComputerBusinessServiceImplTest {

    @Tested
    private ComputerBusinessServiceImpl computerBusinessService;

    @Injectable
    private ComputerBusinessDAO computerBusinessDAO;

    @Injectable
    private CbbTerminalOperatorAPI terminalOperatorAPI;


    @Test
    public void testRelieveFault() throws Exception {
        final UUID computerId = UUID.randomUUID();

        ComputerEntity computerEntity = new ComputerEntity();
        computerEntity.setFaultState(true);
        computerEntity.setMac("mac");

        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = computerEntity;

                terminalOperatorAPI.relieveFault(anyString, any);

                computerBusinessDAO.save((ComputerEntity) any);
            }
        };
        try {
            computerBusinessService.relieveFault(computerId);
        } catch (BusinessException e) {
            Assert.fail();
        }
    }

    @Test
    public void testRelieveFaultByFalse() throws Exception {
        final UUID computerId = UUID.randomUUID();

        ComputerEntity computerEntity = new ComputerEntity();
        computerEntity.setFaultState(false);
        computerEntity.setMac("mac");

        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = computerEntity;
            }
        };
        try {
            computerBusinessService.relieveFault(computerId);
        } catch (BusinessException e) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_COMPUTER_NO_FAULT, e.getKey());
        }
    }

    @Test
    public void testRelieveFaultByException() throws Exception {
        final UUID computerId = UUID.randomUUID();

        ComputerEntity computerEntity = new ComputerEntity();
        computerEntity.setFaultState(true);
        computerEntity.setMac("mac");

        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = computerEntity;

                terminalOperatorAPI.relieveFault(anyString, any);
                result = new BusinessException("error");
            }
        };
        try {
            computerBusinessService.relieveFault(computerId);
        } catch (BusinessException e) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_COMPUTER_RELIEVE_FAULT_FAIL_REFRESH, e.getKey());
        }
    }

    @Test
    public void testRelieveFaultByOffline() throws Exception {
        final UUID computerId = UUID.randomUUID();

        ComputerEntity computerEntity = new ComputerEntity();
        computerEntity.setFaultState(true);
        computerEntity.setMac("mac");

        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = computerEntity;

                terminalOperatorAPI.relieveFault(anyString, any);
                result = new BusinessException(PublicBusinessKey.RCDC_TERMINAL_OFFLINE);
            }
        };
        try {
            computerBusinessService.relieveFault(computerId);
        } catch (BusinessException e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetComputerStateByComputerEntityIsNull() throws Exception {
        final UUID deskId = UUID.randomUUID();
        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = null;
            }
        };
        try {
            computerBusinessService.getComputerState(deskId);
            Assert.fail();
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), BusinessKey.RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER_NAME);
        }
    }

    @Test
    public void testGetComputerState() throws Exception {
        final UUID deskId = UUID.randomUUID();

        ComputerEntity entity = new ComputerEntity();
        entity.setState(ComputerStateEnum.OFFLINE);
        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = entity;
            }
        };
        try {
            ComputerStateEnum computerStateEnum = computerBusinessService.getComputerState(deskId);
            Assert.assertEquals(computerStateEnum, ComputerStateEnum.OFFLINE);
        } catch (BusinessException e) {
            Assert.fail();
        }

    }


}
