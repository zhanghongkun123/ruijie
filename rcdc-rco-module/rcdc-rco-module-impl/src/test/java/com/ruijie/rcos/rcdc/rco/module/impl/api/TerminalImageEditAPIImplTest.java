package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNetworkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

/**
 * Description: 终端镜像编辑API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/28
 *
 * @author songxiang
 */

public class TerminalImageEditAPIImplTest {

    @Tested
    private TerminalImageEditAPIImpl terminalImageEditAPI;

    @Injectable
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    /**
     * 测试check终端状态
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testCheckTerminalOnline() throws BusinessException {
        String terminalId = UUID.randomUUID().toString();
        CbbTerminalBasicInfoDTO CbbTerminalBasicInfoDTO = new CbbTerminalBasicInfoDTO();
        CbbTerminalBasicInfoDTO.setState(CbbTerminalStateEnums.UPGRADING);
        new Expectations() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                result = CbbTerminalBasicInfoDTO;
            }
        };
        try {
            terminalImageEditAPI.checkTerminalOnline(terminalId);
            Assert.fail();
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), BusinessKey.RCDC_RCO_TERMINAL_NOT_ONLINE);
        }
        new Verifications() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                times = 1;
            }
        };

        // 设置为ONLINE
        CbbTerminalBasicInfoDTO.setState(CbbTerminalStateEnums.ONLINE);
        new Expectations() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                result = CbbTerminalBasicInfoDTO;
            }
        };
        terminalImageEditAPI.checkTerminalOnline(terminalId);

        new Verifications() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                times = 1;
            }
        };
    }

    @Test
    public void testCheckTerminalOnlineAndWiredNormal() throws BusinessException {
        String terminalId = UUID.randomUUID().toString();
        CbbTerminalBasicInfoDTO response = new CbbTerminalBasicInfoDTO();
        response.setState(CbbTerminalStateEnums.ONLINE);
        response.setNetworkAccessMode(CbbNetworkModeEnums.WIRED);

        new Expectations() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                result = response;
            }
        };

        terminalImageEditAPI.checkTerminalOnlineAndWired(terminalId);
        new Verifications() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                times = 1;
            }
        };
    }

    @Test
    public void testCheckTerminalOnlineAndWiredWhenOffline() throws BusinessException {
        String terminalId = UUID.randomUUID().toString();
        CbbTerminalBasicInfoDTO response = new CbbTerminalBasicInfoDTO();
        response.setState(CbbTerminalStateEnums.OFFLINE);
        response.setNetworkAccessMode(CbbNetworkModeEnums.WIRED);

        new Expectations() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                result = response;
            }
        };
        thrown.expect(BusinessException.class);
        thrown.expectMessage("23200491");
        terminalImageEditAPI.checkTerminalOnlineAndWired(terminalId);

    }

    @Test
    public void testCheckTerminalOnlineAndWiredWhenOnlineButWireless() throws BusinessException {
        String terminalId = UUID.randomUUID().toString();
        CbbTerminalBasicInfoDTO response = new CbbTerminalBasicInfoDTO();
        response.setState(CbbTerminalStateEnums.ONLINE);
        response.setNetworkAccessMode(CbbNetworkModeEnums.WIRELESS);

        new Expectations() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                result = response;
            }
        };
        thrown.expect(BusinessException.class);
        thrown.expectMessage("23200492");
        terminalImageEditAPI.checkTerminalOnlineAndWired(terminalId);

    }

    @Test
    public void testCheckTerminalOnlineAndWiredWhenNotWiredButWireless() throws BusinessException {
        String terminalId = UUID.randomUUID().toString();
        CbbTerminalBasicInfoDTO response = new CbbTerminalBasicInfoDTO();
        response.setState(CbbTerminalStateEnums.UPGRADING);
        response.setNetworkAccessMode(CbbNetworkModeEnums.WIRELESS);

        new Expectations() {
            {
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString);
                result = response;
            }
        };
        thrown.expect(BusinessException.class);
        thrown.expectMessage("23200491");
        terminalImageEditAPI.checkTerminalOnlineAndWired(terminalId);

    }
}
