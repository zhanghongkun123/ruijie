package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation;

import com.ruijie.rcos.base.task.module.def.util.LocalDateTimeUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.TimedTaskBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity.OnceScheduleTask;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月08日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class OnceScheduleTaskValidatorTest {
    @Tested
    private OnceScheduleTaskValidator onceScheduleTaskValidator;

    /**
     * 测试校验方法
     * 
     * @throws Exception 异常
     */
    @Test
    public void testValidate() throws Exception {

        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        scheduleTaskRequest.setTaskCycle(TaskCycleEnum.ONCE);
        scheduleTaskRequest.setScheduleTypeCode("scheduleTypeCode");
        scheduleTaskRequest.setScheduleTime("10:10:10");
        scheduleTaskRequest.setScheduleDate(LocalDateTime.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        OnceScheduleTask onceScheduleTask = new OnceScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, onceScheduleTask);

        ThrowExceptionTester.throwIllegalArgumentException(() -> onceScheduleTaskValidator.validate(null), "scheduleTaskRequest can not be null");

        onceScheduleTaskValidator.validate(scheduleTaskRequest);
        new Verifications() {
            {
                BeanValidationUtil.validateBean(OnceScheduleTask.class, super.withEqual(onceScheduleTask));
                times = 1;
                LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());
                times = 1;
                LocalDateTimeUtil.validateScheduleDate(scheduleTaskRequest.getScheduleDate());
                times = 1;
            }
        };
    }

    /**
     * 测试校验方法当时间过期时
     *
     * @throws Exception 异常
     */
    @Test
    public void testValidateWhileTimeExpire() throws Exception {

        RcoScheduleTaskRequest scheduleTaskRequest = new RcoScheduleTaskRequest();
        scheduleTaskRequest.setTaskCycle(TaskCycleEnum.ONCE);
        scheduleTaskRequest.setScheduleTypeCode("scheduleTypeCode");
        scheduleTaskRequest.setScheduleTime("10:10:10");
        scheduleTaskRequest.setQuartzTaskState(QuartzTaskState.START);
        scheduleTaskRequest.setScheduleDate(LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        OnceScheduleTask onceScheduleTask = new OnceScheduleTask();
        BeanUtils.copyProperties(scheduleTaskRequest, onceScheduleTask);


        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return "x";
            }
        };

        try {
            onceScheduleTaskValidator.validate(scheduleTaskRequest);
            Assert.fail();
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), TimedTaskBusinessKey.RCDC_RCO_TIMED_TASK_QUARTZ_ONCE_CYCLE_TIME_EXPIRE);
        }
        new Verifications() {
            {
                BeanValidationUtil.validateBean(OnceScheduleTask.class, super.withEqual(onceScheduleTask));
                times = 1;
                LocalDateTimeUtil.validateScheduleTime(scheduleTaskRequest.getScheduleTime());
                times = 1;
                LocalDateTimeUtil.validateScheduleDate(scheduleTaskRequest.getScheduleDate());
                times = 1;
                LocalDate.parse(scheduleTaskRequest.getScheduleDate(), DateTimeFormatter.ofPattern(LocalDateTimeUtil.YYYY_MM_DD));
                times = 1;
                LocalTime.parse(scheduleTaskRequest.getScheduleTime(), DateTimeFormatter.ofPattern(LocalDateTimeUtil.HH_MM_SS));
                times = 1;
            }
        };
    }

}
