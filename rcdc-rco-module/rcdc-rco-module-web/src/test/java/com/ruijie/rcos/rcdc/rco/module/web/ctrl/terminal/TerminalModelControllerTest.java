package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalPlatformWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalModelAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/10
 *
 * @author fang
 */
@RunWith(SkyEngineRunner.class)
public class TerminalModelControllerTest {

    @Tested
    private TerminalModelController terminalModelController;

    @Injectable
    private CbbTerminalModelAPI terminalModelAPI;

    /**
     * testListTerminalOsType
     */
    @Test
    public void testListTerminalOsType() {

        TerminalPlatformWebRequest webRequest = new TerminalPlatformWebRequest();
        webRequest.setPlatformArr(new CbbTerminalPlatformEnums[] {CbbTerminalPlatformEnums.APP});

        new Expectations() {
            {
                terminalModelAPI.listTerminalOsType(new CbbTerminalPlatformEnums[] {CbbTerminalPlatformEnums.APP});
                result = Lists.newArrayList("Windows");
            }
        };

        CommonWebResponse webResponse = terminalModelController.listTerminalOsType(webRequest);
        Map<String, IdLabelStringEntry[]> map = (Map<String, IdLabelStringEntry[]>) webResponse.getContent();
        IdLabelStringEntry[] idLabelArr = map.get("itemArr");
        Assert.assertEquals(idLabelArr.length, 1);
        Assert.assertEquals(idLabelArr[0].getId(), "Windows");
        Assert.assertEquals(idLabelArr[0].getLabel(), "Windows");
    }

    /**
     * testListTerminalOsType
     */
    @Test
    public void testListTerminalOsTypeIsEmpty() {

        TerminalPlatformWebRequest webRequest = new TerminalPlatformWebRequest();
        webRequest.setPlatformArr(new CbbTerminalPlatformEnums[] {CbbTerminalPlatformEnums.APP});

        new Expectations() {
            {
                terminalModelAPI.listTerminalOsType(new CbbTerminalPlatformEnums[] {CbbTerminalPlatformEnums.APP});
                result = null;
            }
        };

        CommonWebResponse webResponse = terminalModelController.listTerminalOsType(webRequest);
        Assert.assertNull(webResponse.getContent());
    }
}
