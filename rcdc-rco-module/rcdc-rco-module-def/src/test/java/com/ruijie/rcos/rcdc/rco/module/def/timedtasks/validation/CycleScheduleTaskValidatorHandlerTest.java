package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;

import mockit.Mocked;
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
public class CycleScheduleTaskValidatorHandlerTest {

    /**
     * 测试校验方法
     * 
     * @param weekScheduleTaskValidator 周校验器
     * @throws Exception 异常
     */
    @Test
    public void testValidateScheduleTask(@Mocked WeekScheduleTaskValidator weekScheduleTaskValidator) throws Exception {
        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        scheduleTaskRequest.setTaskCycle(TaskCycleEnum.WEEK);

        ThrowExceptionTester.throwIllegalArgumentException(() -> CycleScheduleTaskValidatorHandler.validateScheduleTask(null),
                "scheduleTaskRequest can not be null");
        CycleScheduleTaskValidatorHandler.validateScheduleTask(scheduleTaskRequest);
        new Verifications() {
            {
                weekScheduleTaskValidator.validate(scheduleTaskRequest);
                times = 1;
            }
        };
    }



}
