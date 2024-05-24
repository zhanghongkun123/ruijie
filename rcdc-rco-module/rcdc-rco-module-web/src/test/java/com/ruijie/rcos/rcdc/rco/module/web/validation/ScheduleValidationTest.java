package com.ruijie.rcos.rcdc.rco.module.web.validation;

import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.CycleScheduleTaskValidatorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;

import mockit.Expectations;
import mockit.Tested;
import mockit.Verifications;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月08日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class ScheduleValidationTest {
    @Tested
    private ScheduleValidation scheduleValidation;

    /**
     * 测试定时任务校验方法
     * 
     * @throws Exception 异常
     */
    @Test
    public void testScheduleTaskValidate() throws Exception {
        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        ThrowExceptionTester.throwIllegalArgumentException(() -> scheduleValidation.scheduleTaskValidate(null), "请求不能为空");
        new Expectations(CycleScheduleTaskValidatorHandler.class) {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(scheduleTaskRequest);

            }
        };
        scheduleValidation.scheduleTaskValidate(scheduleTaskRequest);
        new Verifications() {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(scheduleTaskRequest);
                times = 1;
            }
        };
    }



}
