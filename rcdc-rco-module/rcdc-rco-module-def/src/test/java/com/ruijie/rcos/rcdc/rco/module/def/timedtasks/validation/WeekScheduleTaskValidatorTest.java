package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity.WeekScheduleTask;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
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
public class WeekScheduleTaskValidatorTest {
    @Tested
    private WeekScheduleTaskValidator weekScheduleTaskValidator;

    /**
     * 测试校验方法
     *
     * @throws Exception 异常
     */
    @Test
    public void testValidate() throws Exception {

        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        scheduleTaskRequest.setTaskCycle(TaskCycleEnum.WEEK);
        scheduleTaskRequest.setScheduleTypeCode("scheduleTypeCode");
        scheduleTaskRequest.setScheduleTime("10:10:10");
        scheduleTaskRequest.setDayOfWeekArr(new Integer[] {1, 2, 3, 4, 5, 6, 7});
        WeekScheduleTask weekScheduleTask = new WeekScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, weekScheduleTask);

        ThrowExceptionTester.throwIllegalArgumentException(() -> weekScheduleTaskValidator.validate(null), "scheduleTaskRequest can not be null");

        weekScheduleTaskValidator.validate(scheduleTaskRequest);
        new Verifications() {
            {
                BeanValidationUtil.validateBean(WeekScheduleTask.class, super.withEqual(weekScheduleTask));
                times = 1;
                LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());
                times = 1;
            }
        };
    }

    /**
     * 测试校验方法当参数异常时
     * 
     * @throws AnnotationValidationException 参数校验异常
     */
    @Test
    public void testValidateWhileWeekValLteZero() throws AnnotationValidationException {
        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        scheduleTaskRequest.setTaskCycle(TaskCycleEnum.WEEK);
        scheduleTaskRequest.setScheduleTypeCode("scheduleTypeCode");
        scheduleTaskRequest.setScheduleTime("10:10:10");
        scheduleTaskRequest.setDayOfWeekArr(new Integer[] {0, 1});
        WeekScheduleTask weekScheduleTask = new WeekScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, weekScheduleTask);

        try {
            weekScheduleTaskValidator.validate(scheduleTaskRequest);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "星期几参数格式错误");
        }

        new Verifications() {
            {
                BeanValidationUtil.validateBean(WeekScheduleTask.class, super.withEqual(weekScheduleTask));
                times = 1;
                LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());
                times = 0;
            }
        };
    }

    /**
     * 测试校验方法当参数异常时
     * 
     * @throws AnnotationValidationException 参数校验异常
     */
    @Test
    public void testValidateWhileWeekValGteSeven() throws AnnotationValidationException {
        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        scheduleTaskRequest.setTaskCycle(TaskCycleEnum.WEEK);
        scheduleTaskRequest.setScheduleTypeCode("scheduleTypeCode");
        scheduleTaskRequest.setScheduleTime("10:10:10");
        scheduleTaskRequest.setDayOfWeekArr(new Integer[] {1, 8});
        WeekScheduleTask weekScheduleTask = new WeekScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, weekScheduleTask);

        try {
            weekScheduleTaskValidator.validate(scheduleTaskRequest);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "星期几参数格式错误");
        }

        new Verifications() {
            {
                BeanValidationUtil.validateBean(WeekScheduleTask.class, super.withEqual(weekScheduleTask));
                times = 1;
                LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());
                times = 0;
            }
        };
    }
}
