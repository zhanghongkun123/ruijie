package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity.DayScheduleTask;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;

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
public class DayScheduleTaskValidatorTest {
    @Tested
    private DayScheduleTaskValidator dayScheduleTaskValidator;

    /**
     * 测试校验方法
     * 
     * @throws Exception 异常
     */
    @Test
    public void testValidate() throws Exception {
        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        scheduleTaskRequest.setTaskCycle(TaskCycleEnum.DAY);
        scheduleTaskRequest.setScheduleTypeCode("scheduleTypeCode");
        scheduleTaskRequest.setScheduleTime("10:10:10");

        DayScheduleTask dayScheduleTask = new DayScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, dayScheduleTask);

        ThrowExceptionTester.throwIllegalArgumentException(() -> dayScheduleTaskValidator.validate(null), "scheduleTaskRequest can not be null");
        dayScheduleTaskValidator.validate(scheduleTaskRequest);
        new Verifications() {
            {
                BeanValidationUtil.validateBean(DayScheduleTask.class, super.withEqual(dayScheduleTask));
                times = 1;
                LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());
                times = 1;
            }
        };
    }



}
