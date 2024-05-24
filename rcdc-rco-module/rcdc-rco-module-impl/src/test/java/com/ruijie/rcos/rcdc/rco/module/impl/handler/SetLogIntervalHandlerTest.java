package com.ruijie.rcos.rcdc.rco.module.impl.handler;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.sm2.StateTransitionRegistry;
import mockit.Capturing;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(SkyEngineRunner.class)
public class SetLogIntervalHandlerTest {

    @Tested
    private SetLogIntervalHandler handler;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testRegisterStateTransition() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("registry can not be null");
        handler.registerStateTransition(null);
        Assert.assertTrue(true);
    }

    @Test
    public void testRegisterStateTransitionSuccess(@Capturing StateTransitionRegistry registry) {
        handler.registerStateTransition(registry);
        Assert.assertTrue(true);
    }
}
