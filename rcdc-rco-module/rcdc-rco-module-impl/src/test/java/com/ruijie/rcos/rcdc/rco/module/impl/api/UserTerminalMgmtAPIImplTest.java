package com.ruijie.rcos.rcdc.rco.module.impl.api;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.service.TerminalFeatureCodeService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.TerminalListServiceImpl;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalDetectAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RunWith(SkyEngineRunner.class)
public class UserTerminalMgmtAPIImplTest {
    @Tested
    private UserTerminalMgmtAPIImpl terminalMgmtAPI;

    @Injectable
    private TerminalService terminalService;

    @Injectable
    private TerminalListServiceImpl terminalListService;

    @Injectable
    private CbbTerminalDetectAPI recordAPI;

    @Injectable
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Injectable
    private UserTerminalDAO userTerminalDAO;

    @Injectable
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Injectable
    private TerminalGroupService terminalGroupService;

    @Injectable
    private QueryCloudDesktopService queryCloudDesktopService;

    @Injectable
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Injectable
    private ViewTerminalDAO viewTerminalDAO;

    @Injectable
    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    @Injectable
    private TerminalFeatureCodeService terminalFeatureCodeService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testQueryListByPlatformAndGroupIdAndState() {
        List<ViewTerminalEntity> terminalEntityList = new ArrayList<>();
        terminalEntityList.add(new ViewTerminalEntity());
        new Expectations() {
            {
                viewTerminalDAO.findViewTerminalEntitiesByPlatformAndTerminalGroupIdAndTerminalState((CbbTerminalPlatformEnums) any, (UUID) any,
                        (CbbTerminalStateEnums) any);
                result = terminalEntityList;
            }
        };
        List<TerminalDTO> terminalList =
                terminalMgmtAPI.queryListByPlatformAndGroupIdAndState(CbbTerminalPlatformEnums.IDV, UUID.randomUUID(), CbbTerminalStateEnums.ONLINE);

        TerminalCloudDesktopDTO terminalCloudDesktopDTO = new TerminalCloudDesktopDTO();
        TerminalDTO terminalDTO = new TerminalDTO();
        terminalDTO.setTerminalCloudDesktop(terminalCloudDesktopDTO);
        Assert.assertEquals(JSON.toJSON(Arrays.asList(terminalDTO)), JSON.toJSON(terminalList));
    }

    @Test
    public void testQueryListByPlatformAndGroupIdAndStateVerify() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("platform must be present");
        terminalMgmtAPI.queryListByPlatformAndGroupIdAndState(null, UUID.randomUUID(), CbbTerminalStateEnums.ONLINE);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("terminalGroupId must be present");
        terminalMgmtAPI.queryListByPlatformAndGroupIdAndState(CbbTerminalPlatformEnums.IDV, null, CbbTerminalStateEnums.ONLINE);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("terminalState must be present");
        terminalMgmtAPI.queryListByPlatformAndGroupIdAndState(CbbTerminalPlatformEnums.IDV, UUID.randomUUID(), null);
    }

    @Test
    public void testQueryListByPlatformAndGroupIdAndState2() {
        // List<ViewTerminalEntity> entities = new ArrayList<>();
        List<ViewTerminalEntity> entityList = null;
        new Expectations() {
            {
                viewTerminalDAO.findViewTerminalEntitiesByPlatformAndTerminalGroupIdAndTerminalState((CbbTerminalPlatformEnums) any, (UUID) any,
                        (CbbTerminalStateEnums) any);
                result = entityList;
            }
        };
        List<TerminalDTO> terminalDtoList =
                terminalMgmtAPI.queryListByPlatformAndGroupIdAndState(CbbTerminalPlatformEnums.IDV, UUID.randomUUID(), CbbTerminalStateEnums.ONLINE);

        TerminalDTO terminalDTO = new TerminalDTO();
        Assert.assertEquals(JSON.toJSON(Lists.newArrayList()), JSON.toJSON(terminalDtoList));
    }
}
