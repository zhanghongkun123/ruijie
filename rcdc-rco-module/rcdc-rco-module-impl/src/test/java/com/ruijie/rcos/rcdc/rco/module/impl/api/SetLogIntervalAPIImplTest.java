package com.ruijie.rcos.rcdc.rco.module.impl.api;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SetLogIntervalRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.Response;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

@RunWith(SkyEngineRunner.class)
public class SetLogIntervalAPIImplTest {

    @Tested
    private SetLogIntervalAPIImpl setLogIntervalAPI;

    @Injectable
    private StateMachineFactory stateMachineFactory;

    @Injectable
    private CbbDesktopOpLogMgmtAPI userDesktopOpLogMgmtAPI;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSetLogInterval1() throws BusinessException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("request is not null");
        setLogIntervalAPI.setLogInterval(null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("intervalDay is not null");
        setLogIntervalAPI.setLogInterval(new SetLogIntervalRequest());
    }

    @Test
    public void testSetLogInterval2() throws BusinessException {

        new Expectations(){{
            userDesktopOpLogMgmtAPI.getDesktopOpLogRetainDays();
            result = 90;
        }};
        DefaultResponse defaultResponse = setLogIntervalAPI.setLogInterval(new SetLogIntervalRequest(90));

        Assert.assertEquals(defaultResponse.getStatus(), Response.Status.SUCCESS);
    }

}