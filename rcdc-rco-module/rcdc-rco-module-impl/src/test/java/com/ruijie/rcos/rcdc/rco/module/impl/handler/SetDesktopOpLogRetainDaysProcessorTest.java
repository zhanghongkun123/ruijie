package com.ruijie.rcos.rcdc.rco.module.impl.handler;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

@RunWith(SkyEngineRunner.class)
public class SetDesktopOpLogRetainDaysProcessorTest {

    @Tested
    private SetDesktopOpLogRetainDaysProcessor processor;

    @Injectable
    private CbbDesktopOpLogMgmtAPI userDesktopOpLogMgmtAPI;

    @Injectable
    private StateTaskHandle.StateProcessContext context;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void doProcess() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("context is not null");
        processor.doProcess(null);
        Assert.assertTrue(true);
    }

    @Test
    public void doProcessSuccess() throws Exception {
        new Expectations(){{
            context.get(Constants.INTERVAL_DAY_KEY, Integer.class);
            result = 10;
        }};
        processor.doProcess(context);
        Assert.assertTrue(true);
    }

    @Test
    public void undoProcess() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("context is not null");
        processor.undoProcess(null);
        Assert.assertTrue(true);
    }

    @Test
    public void undoProcessSuccess() throws Exception {
        new Expectations(){{
            context.get(Constants.OLD_DAY_KEY, Integer.class);
            result = 10;
        }};
        processor.undoProcess(context);
        Assert.assertTrue(true);
    }
}