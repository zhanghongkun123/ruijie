package com.ruijie.rcos.rcdc.rco.module.impl.handler;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogConfigMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.enums.BaseLogType;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(SkyEngineRunner.class)
public class SetBaseAuditLogConfigProcessorTest  {
    @Tested
    private SetBaseAuditLogConfigProcessor processor;

    @Injectable
    private BaseAuditLogConfigMgmtAPI baseAuditLogConfigMgmtAPI;

    @Injectable
    private StateTaskHandle.StateProcessContext context;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testDoProcess() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("context is not null");
        processor.doProcess(null);
        Assert.assertTrue(true);
    }

    @Test
    public void testDoProcessSucces() throws Exception {
        new Expectations(){{
            context.get(Constants.INTERVAL_DAY_KEY, Integer.class);
            result = 10;
            baseAuditLogConfigMgmtAPI.updateLogMaxRetentionTime(anyInt, (BaseLogType) any);
            result = DefaultResponse.Builder.success();
        }};
        processor.doProcess(context);
        Assert.assertTrue(true);
    }

    @Test
    public void testUndoProcess() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("context is not null");
        processor.undoProcess(null);
        Assert.assertTrue(true);
    }

    @Test
    public void testUndoProcessSuccess() throws Exception {
        new Expectations(){{
            context.get(Constants.OLD_DAY_KEY, Integer.class);
            result = 10;
            baseAuditLogConfigMgmtAPI.updateLogMaxRetentionTime(anyInt, (BaseLogType) any);
            result = DefaultResponse.Builder.success();
        }};
        processor.undoProcess(context);
        Assert.assertTrue(true);
    }
}