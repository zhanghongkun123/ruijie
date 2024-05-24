package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerDeskRemoteAssistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistInfoOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantRemoteResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ChangeAssistStateRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ComputerRemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateRemoteAssistInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistOtherDeskHandleRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.AssistantRemoteResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RemoteAssistInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.computer.ComputerAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author wjp
 */
@RunWith(JMockit.class)
public class ComputerRemoteAssistMgmtServiceImplTest {

    @Tested
    private ComputerRemoteAssistMgmtServiceImpl computerRemoteAssistMgmtServiceImpl;

    @Injectable
    private ComputerDeskRemoteAssistAPI remoteAssistService;

    @Injectable
    private ComputerBusinessService computerService;

    @Injectable
    private RemoteAssistInfoOperateAPI remoteAssistInfoOperateAPI;

    @Before
    public void before() {

        new MockUp<LocaleI18nResolver>() {

            /**
             *
             * @param key key
             * @param args args
             * @return key
             */
            @Mock
            public String resolve(String key, String... args) {
                return key;
            }

        };
    }

    @Test
    public void testParaIsNull() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(
            () -> computerRemoteAssistMgmtServiceImpl.assistInquire(null, null, null), "deskId must not be null.");
        assertTrue(true);
        final UUID deskId = UUID.randomUUID();
        ThrowExceptionTester.throwIllegalArgumentException(
            () -> computerRemoteAssistMgmtServiceImpl.assistInquire(deskId, null, null),
            "adminId must not be null.");
        assertTrue(true);
        final UUID adminId = UUID.randomUUID();
        ThrowExceptionTester.throwIllegalArgumentException(
            () -> computerRemoteAssistMgmtServiceImpl.assistInquire(deskId, adminId, null),
            "adminName can not be blank.");
        assertTrue(true);

        ThrowExceptionTester.throwIllegalArgumentException(
            () -> computerRemoteAssistMgmtServiceImpl.queryAssisInfo(null, null), "deskId must not be null.");
        assertTrue(true);
        ThrowExceptionTester.throwIllegalArgumentException(
            () -> computerRemoteAssistMgmtServiceImpl.queryAssisInfo(deskId, null), "adminId must not be null.");
        assertTrue(true);
    }

    @Test
    public void assistInquire() throws Exception {
        final UUID deskId = UUID.randomUUID();
        final UUID adminId = UUID.randomUUID();
        final String adminName = "admin";

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();

        ComputerStateEnum state = ComputerStateEnum.ONLINE;

        ComputerEntity entity = new ComputerEntity();

        AssistantRemoteResponseDTO assistantRemoteResponseDTO = new AssistantRemoteResponseDTO();
        assistantRemoteResponseDTO.setCode(0);
        assistantRemoteResponseDTO.setDeskId(deskId);
        AssistantRemoteResponse assistantRemoteResponse = new AssistantRemoteResponse();
        assistantRemoteResponse.setResponseDTO(assistantRemoteResponseDTO);

        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

                computerService.getComputerState((UUID) any);
                result = state;

                computerService.getComputerById((UUID) any);
                result = entity;

                remoteAssistInfoOperateAPI.createRemoteAssistInfo((CreateRemoteAssistInfoRequest) any);

                remoteAssistService.remoteAssistDesk((ComputerRemoteAssistRequest) any);
                result = assistantRemoteResponse;

                remoteAssistInfoOperateAPI.remoteAssistOtherDeskHandle((RemoteAssistOtherDeskHandleRequest) any);
            }
        };
        try {
            computerRemoteAssistMgmtServiceImpl.assistInquire(deskId, adminId, adminName);
            Assert.assertTrue(true);
        } catch (BusinessException e) {
            Assert.assertEquals("", e.getMessage());
        }
    }

    @Test
    public void testUpdateRemoteAssistStateByDeskIdIsNull() throws Exception {
        final UUID deskId = UUID.randomUUID();
        final RemoteAssistState state = RemoteAssistState.LOCK_SCREEN;
        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();

        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;
            }
        };
        try {
            computerRemoteAssistMgmtServiceImpl.updateRemoteAssistState(deskId, state);
            fail();
        } catch (BusinessException e) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_COMPUTER_GET_REMOTE_ASSIST_IS_NULL, e.getKey());
        }
    }

    @Test
    public void testUpdateRemoteAssistStateByStateIsNull() throws Exception {
        final UUID deskId = UUID.randomUUID();
        final RemoteAssistState state = RemoteAssistState.LOCK_SCREEN;
        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(deskId);

        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

                remoteAssistInfoOperateAPI.removeRemoteAssistInfo((IdRequest) any);

                remoteAssistInfoOperateAPI.changeAssistState((ChangeAssistStateRequest) any);

                remoteAssistInfoOperateAPI.resetVncHeartBeat((IdRequest) any);
            }
        };
        try {
            computerRemoteAssistMgmtServiceImpl.updateRemoteAssistState(deskId, state);
        } catch (BusinessException e) {
            fail();
        }
    }

    @Test
    public void testUpdateAssistInfoAfterUserConfirm() {

        final UUID deskId = UUID.randomUUID();
        final int port = 1234;
        final String pwd = "password";
        final String ip = "172.23.12.23";

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(deskId);

        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

                remoteAssistInfoOperateAPI.updateRemoteAssistInfo((RemoteAssistInfoDTO) any);

            }
        };
        try {
            ComputerAssistDTO computerAssistDTO = new ComputerAssistDTO();
            computerAssistDTO.setDeskId(deskId);
            computerAssistDTO.setIp(ip);
            computerAssistDTO.setPort(port);
            computerAssistDTO.setPwd(pwd);
            computerRemoteAssistMgmtServiceImpl.updateAssistInfoAfterUserConfirm(computerAssistDTO);
            assertTrue(true);
        }catch (BusinessException e) {
            assertTrue(false);
        }

    }

    @Test
    public void testComputerUserResponseExpiredTime() throws Exception {

        final UUID deskId = UUID.randomUUID();

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(deskId);
        response.setState(RemoteAssistState.WAITING);
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

                remoteAssistInfoOperateAPI.changeAssistState((ChangeAssistStateRequest) any);

                remoteAssistService.cancelRemoteAssistDesk((UUID) any);

            }
        };
        try {
            computerRemoteAssistMgmtServiceImpl.computerUserResponseExpiredTime(deskId);
            assertTrue(true);
        } catch (BusinessException e) {
            fail();
        }
    }

    @Test
    public void testComputerUserResponseExpiredTimeByException() throws Exception {

        final UUID deskId = UUID.randomUUID();

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;
            }
        };
        try {
            computerRemoteAssistMgmtServiceImpl.computerUserResponseExpiredTime(deskId);
            assertTrue(true);
        } catch (BusinessException e) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_COMPUTER_GET_REMOTE_ASSIST_IS_NULL, e.getKey());
        }
    }

    @Test
    public void testVncHeartbeatHandleByRemoteAssistInfoIsNull() throws Exception {

        final UUID deskId = UUID.randomUUID();
        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;
            }
        };
        computerRemoteAssistMgmtServiceImpl.vncHeartbeatHandle(deskId);
        assertTrue(true);
    }

    @Test
    public void testVncHeartbeatHandleByRemoteAssistInfoState() throws Exception {

        final UUID deskId = UUID.randomUUID();
        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(deskId);
        response.setState(RemoteAssistState.WAITING);
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;
            }
        };
        computerRemoteAssistMgmtServiceImpl.vncHeartbeatHandle(deskId);
        assertTrue(true);
    }


    @Test
    public void testVncHeartbeatHandle() throws Exception {

        final UUID deskId = UUID.randomUUID();
        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(deskId);
        response.setState(RemoteAssistState.IN_REMOTE_ASSIST);
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

                remoteAssistInfoOperateAPI.resetVncHeartBeat((IdRequest) any);
            }
        };
        computerRemoteAssistMgmtServiceImpl.vncHeartbeatHandle(deskId);
        assertTrue(true);
    }

    @Test
    public void testNotifyAssistResultByRemoteAssistInfoIsNull() throws Exception {
        final UUID deskId = UUID.randomUUID();
        final int userOperateType = 3;

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

                remoteAssistService.cancelRemoteAssistDesk((UUID) any);
                result = new BusinessException("error");
            }
        };
        try {
            computerRemoteAssistMgmtServiceImpl.notifyAssistResult(deskId, userOperateType);
            assertTrue(true);
        } catch (BusinessException e) {
            fail();
        }
    }

    @Test
    public void testNotifyAssistResultByShouldHandleNotifyIsFalse() throws Exception {
        final UUID deskId = UUID.randomUUID();
        final int userOperateType = 3;

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(deskId);
        response.setState(RemoteAssistState.IN_REMOTE_ASSIST);
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

                remoteAssistService.cancelRemoteAssistDesk((UUID) any);
            }
        };

        try {
            computerRemoteAssistMgmtServiceImpl.notifyAssistResult(deskId, userOperateType);
            assertTrue(true);
        } catch (BusinessException e) {
            fail();
        }
    }

    @Test
    public void testNotifyAssistResultByShouldHandleNotifyIsTrue() throws Exception {
        final UUID deskId = UUID.randomUUID();
        final int userOperateType = 3;

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(deskId);
        response.setState(RemoteAssistState.WAITING);
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

            }
        };

        try {
            computerRemoteAssistMgmtServiceImpl.notifyAssistResult(deskId, userOperateType);
            assertTrue(true);
        } catch (BusinessException e) {
            fail();
        }

    }

    @Test
    public void testCreateVncChannelResultRemoteAssistInfoNull() throws Exception {
        final UUID deskId = UUID.randomUUID();
        final UUID adminId = UUID.randomUUID();

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(null);
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;
            }
        };

        computerRemoteAssistMgmtServiceImpl.createVncChannelResult(deskId, adminId);
        assertTrue(true);

    }

    @Test
    public void testCreateVncChannelResult() throws Exception {
        final UUID deskId = UUID.randomUUID();
        final UUID adminId = UUID.randomUUID();

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(deskId);
        response.setState(RemoteAssistState.WAITING);
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

            }
        };

        computerRemoteAssistMgmtServiceImpl.createVncChannelResult(deskId, adminId);

        new Verifications() {
            {
                remoteAssistService.createVncChannelResult((UUID) any);
                times = 1;
            }
        };
    }

    @Test
    public void testCreateVncChannelResultException() throws Exception {
        String errMsg = "xxx";
        final UUID deskId = UUID.randomUUID();
        final UUID adminId = UUID.randomUUID();

        RemoteAssistInfoResponse response = new RemoteAssistInfoResponse();
        response.setDeskId(deskId);
        response.setState(RemoteAssistState.WAITING);
        new Expectations() {
            {
                remoteAssistInfoOperateAPI.queryRemoteAssistInfo((IdRequest) any);
                result = response;

                remoteAssistService.createVncChannelResult((UUID) any);
                result = new BusinessException(errMsg);
            }
        };

        try {
            computerRemoteAssistMgmtServiceImpl.createVncChannelResult(deskId, adminId);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), errMsg);
        }
    }

}
