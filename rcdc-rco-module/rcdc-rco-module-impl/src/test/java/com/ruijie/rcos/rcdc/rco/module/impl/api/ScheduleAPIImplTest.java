package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.task.module.def.api.ScheduleTaskAPI;
import com.ruijie.rcos.base.task.module.def.api.ScheduleTaskTypeAPI;
import com.ruijie.rcos.base.task.module.def.api.request.CreateScheduleTaskRequest;
import com.ruijie.rcos.base.task.module.def.api.request.EditScheduleTaskRequest;
import com.ruijie.rcos.base.task.module.def.api.request.ScheduleTaskPageRequest;
import com.ruijie.rcos.base.task.module.def.dto.CronExpressionDTO;
import com.ruijie.rcos.base.task.module.def.dto.ScheduleTaskDTO;
import com.ruijie.rcos.base.task.module.def.dto.ScheduleTaskTypeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.RcoScheduleTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoCreateScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoEditScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.CycleScheduleTaskValidatorHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.conversion.data.DataConversionChain;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.Response;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月08日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class ScheduleAPIImplTest {

    @Tested
    private ScheduleAPIImpl scheduleAPI;

    @Injectable
    DataConversionChain dataConversionChain;

    @Injectable
    private ScheduleTaskAPI scheduleTaskAPI;

    @Injectable
    private ScheduleTaskTypeAPI scheduleTaskTypeAPI;

    /**
     * 测试查询定时任务方法
     * 
     * @param toScheduleDataDTO dto
     * @throws Exception 异常
     */
    @Test
    public void testQueryScheduleTask(@Injectable ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> toScheduleDataDTO) throws Exception {

        ThrowExceptionTester.throwIllegalArgumentException(() -> scheduleAPI.queryScheduleTask(null), "taskId can not be null");
        UUID id = UUID.randomUUID();

        ScheduleDataDTO<UUID, String> scheduleDataDTO = new ScheduleDataDTO<>();
        scheduleDataDTO.setDeskArr(new UUID[] {id});

        ScheduleTaskDTO scheduleTaskDTO = new ScheduleTaskDTO();
        scheduleTaskDTO.setId(id);
        scheduleTaskDTO.setTaskName("taskName");
        scheduleTaskDTO.setScheduleTypeCode("scheduleTaskCode");
        scheduleTaskDTO.setTaskTypeName("taskTypeName");
        scheduleTaskDTO.setCronExpression("0 0 0 * * ? *");
        scheduleTaskDTO.setBusinessData(JSON.toJSONString(scheduleDataDTO));
        scheduleTaskDTO.setQuartzTaskState(QuartzTaskState.START);

        new Expectations() {
            {
                scheduleTaskAPI.queryById(id);
                result = scheduleTaskDTO;
                dataConversionChain.conversion(scheduleDataDTO);
                result = toScheduleDataDTO;
            }
        };

        scheduleAPI.queryScheduleTask(id);

        new Verifications() {
            {
                scheduleTaskAPI.queryById(id);
                times = 1;
                dataConversionChain.conversion(super.withEqual(scheduleDataDTO));
                times = 1;
            }
        };
    }


    /**
     * 测试查询定时任务方法
     *
     * @param toScheduleDataDTO dto
     * @throws Exception 异常
     */
    @Test
    public void testQueryScheduleTaskWhileDataIsNull(@Injectable ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> toScheduleDataDTO)
            throws Exception {
        UUID id = UUID.randomUUID();

        ScheduleTaskDTO scheduleTaskDTO = new ScheduleTaskDTO();
        scheduleTaskDTO.setId(id);
        scheduleTaskDTO.setTaskName("taskName");
        scheduleTaskDTO.setScheduleTypeCode("scheduleTaskCode");
        scheduleTaskDTO.setTaskTypeName("taskTypeName");
        scheduleTaskDTO.setCronExpression("0 0 0 * * ? *");
        scheduleTaskDTO.setBusinessData(null);
        scheduleTaskDTO.setQuartzTaskState(QuartzTaskState.START);

        new Expectations() {
            {
                scheduleTaskAPI.queryById(id);
                result = scheduleTaskDTO;
            }
        };

        scheduleAPI.queryScheduleTask(id);

        new Verifications() {
            {
                scheduleTaskAPI.queryById(id);
                times = 1;
                dataConversionChain.conversion((ScheduleDataDTO<UUID, String>) any);
                times = 0;
            }
        };
    }


    /**
     * 测试分页查询方法
     * 
     * @throws Exception 异常
     */
    @Test
    public void testQueryScheduleTaskByPageWhileNoResult() throws Exception {
        DefaultPageRequest pageRequest = new DefaultPageRequest();

        ScheduleTaskPageRequest scheduleTaskPageRequest = new ScheduleTaskPageRequest();
        scheduleTaskPageRequest.setGroup(TaskGroup.ADMIN_CONFIG);
        BeanUtils.copyProperties(pageRequest, scheduleTaskPageRequest);

        DefaultPageResponse<ScheduleTaskDTO> defaultPageResponse =
                DefaultPageResponse.Builder.success(pageRequest.getLimit(), 0, new ScheduleTaskDTO[] {});

        new Expectations() {
            {
                scheduleTaskAPI.pageQuery((ScheduleTaskPageRequest) any);
                result = defaultPageResponse;
                scheduleTaskTypeAPI.listQuery();
                result = new ScheduleTaskTypeDTO[0];
            }
        };

        DefaultPageResponse<RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>>> defaultResponse =
                scheduleAPI.queryScheduleTaskByPage(pageRequest);
        Assert.assertEquals(defaultResponse.getTotal(), 0);
        Assert.assertTrue(defaultResponse.getItemArr().length == 0);
        Assert.assertEquals(defaultResponse.getStatus(), Response.Status.SUCCESS);
        new Verifications() {
            {
                scheduleTaskAPI.pageQuery((ScheduleTaskPageRequest) any);
                times = 1;
                scheduleTaskTypeAPI.listQuery();
                times = 1;
                dataConversionChain.conversion((ScheduleDataDTO<UUID, String>) any);
                times = 0;
            }
        };
    }


    /**
     * 测试分页查询
     * 
     * @throws Exception 异常
     */
    @Test
    public void testQueryScheduleTaskByPage() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> scheduleAPI.queryScheduleTaskByPage(null), "pageRequest can not be null");
        testQueryByPage();
        Assert.assertTrue(true);
    }

    /**
     * 测试分页查询当为debug状态时
     *
     */
    @Test
    public void testQueryScheduleTaskByPageWhileIsDebug() throws BusinessException {
        new MockUp<Logger>() {
            @Mock
            public boolean isDebugEnabled() {
                return true;
            }
        };
        testQueryByPage();
        Assert.assertTrue(true);

    }

    private void testQueryByPage() throws BusinessException {
        DefaultPageRequest pageRequest = new DefaultPageRequest();

        ScheduleTaskPageRequest scheduleTaskPageRequest = new ScheduleTaskPageRequest();
        scheduleTaskPageRequest.setGroup(TaskGroup.ADMIN_CONFIG);
        BeanUtils.copyProperties(pageRequest, scheduleTaskPageRequest);

        UUID id = UUID.randomUUID();

        ScheduleDataDTO<UUID, String> scheduleDataDTO = new ScheduleDataDTO<>();
        scheduleDataDTO.setDeskArr(new UUID[] {id});

        ScheduleTaskDTO scheduleTaskDTO = new ScheduleTaskDTO();
        scheduleTaskDTO.setId(id);
        scheduleTaskDTO.setTaskName("taskName");
        scheduleTaskDTO.setScheduleTypeCode("scheduleTaskCode");
        scheduleTaskDTO.setTaskTypeName("taskTypeName");
        scheduleTaskDTO.setCronExpression("0 0 0 * * ? *");
        scheduleTaskDTO.setBusinessData(JSON.toJSONString(scheduleDataDTO));
        scheduleTaskDTO.setQuartzTaskState(QuartzTaskState.START);

        DefaultPageResponse<ScheduleTaskDTO> defaultPageResponse =
                DefaultPageResponse.Builder.success(pageRequest.getLimit(), 1, new ScheduleTaskDTO[] {scheduleTaskDTO});

        new Expectations() {
            {
                scheduleTaskAPI.pageQuery((ScheduleTaskPageRequest) any);
                result = defaultPageResponse;
                scheduleTaskTypeAPI.listQuery();
                result = new ScheduleTaskTypeDTO[] {new ScheduleTaskTypeDTO("scheduleTaskCode", "label")};
            }
        };

        DefaultPageResponse<RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>>> defaultResponse =
                scheduleAPI.queryScheduleTaskByPage(pageRequest);
        Assert.assertEquals(defaultResponse.getTotal(), 1);
        Assert.assertTrue(defaultResponse.getItemArr().length == 1);
        Assert.assertEquals(defaultResponse.getStatus(), Response.Status.SUCCESS);
        new Verifications() {
            {
                scheduleTaskAPI.pageQuery((ScheduleTaskPageRequest) any);
                times = 1;
                scheduleTaskTypeAPI.listQuery();
                times = 1;
            }
        };
    }

    /**
     * 测试创建定时任务方法当数据为空时
     * 
     * @throws Exception 异常
     */
    @Test
    public void testCreateScheduleTaskWhileDataIsNull() throws Exception {

        ThrowExceptionTester.throwIllegalArgumentException(() -> scheduleAPI.createScheduleTask(null),
                "rcoCreateScheduleTaskRequest can not be null");
        RcoCreateScheduleTaskRequest rcoCreateScheduleTaskRequest = new RcoCreateScheduleTaskRequest();
        rcoCreateScheduleTaskRequest.setTaskCycle(TaskCycleEnum.WEEK);
        rcoCreateScheduleTaskRequest.setScheduleTime("10:10:10");
        rcoCreateScheduleTaskRequest.setDayOfWeekArr(new Integer[] {1, 2});

        CronExpressionDTO cronExpressionDTO = new CronExpressionDTO();
        BeanUtils.copyProperties(rcoCreateScheduleTaskRequest, cronExpressionDTO);


        new Expectations(CycleScheduleTaskValidatorHandler.class) {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoCreateScheduleTaskRequest);
            }
        };
        scheduleAPI.createScheduleTask(rcoCreateScheduleTaskRequest);

        new Verifications() {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoCreateScheduleTaskRequest);
                times = 1;
                scheduleTaskAPI.add((CreateScheduleTaskRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 测试创建定时任务方法
     *
     * @throws Exception 异常
     */
    @Test
    public void testCreateScheduleTask() throws Exception {

        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> scheduleDataDTO = new ScheduleDataDTO<>();
        scheduleDataDTO.setDeskArr(new IdLabelEntry[]{
                IdLabelEntry.build(UUID.randomUUID(), "label")
        });
        scheduleDataDTO.setTerminalArr(new GenericIdLabelEntry[] {
                GenericIdLabelEntry.build("id", "label")
        });

        RcoCreateScheduleTaskRequest rcoCreateScheduleTaskRequest = new RcoCreateScheduleTaskRequest();
        rcoCreateScheduleTaskRequest.setTaskCycle(TaskCycleEnum.WEEK);
        rcoCreateScheduleTaskRequest.setScheduleTime("10:10:10");
        rcoCreateScheduleTaskRequest.setDayOfWeekArr(new Integer[] {1, 2});
        rcoCreateScheduleTaskRequest.setData(scheduleDataDTO);
        CronExpressionDTO cronExpressionDTO = new CronExpressionDTO();
        BeanUtils.copyProperties(rcoCreateScheduleTaskRequest, cronExpressionDTO);


        new Expectations(CycleScheduleTaskValidatorHandler.class) {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoCreateScheduleTaskRequest);
            }
        };
        scheduleAPI.createScheduleTask(rcoCreateScheduleTaskRequest);

        new Verifications() {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoCreateScheduleTaskRequest);
                times = 1;
                scheduleTaskAPI.add((CreateScheduleTaskRequest) any);
                times = 1;
            }
        };
    }


    /**
     * 测试创建定时任务方法
     *
     * @throws Exception 异常
     */
    @Test
    public void testCreateScheduleTaskWhileTerminalIsNull() throws Exception {

        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> scheduleDataDTO = new ScheduleDataDTO<>();
        scheduleDataDTO.setDeskArr(new IdLabelEntry[]{
                IdLabelEntry.build(UUID.randomUUID(), "label")
        });

        RcoCreateScheduleTaskRequest rcoCreateScheduleTaskRequest = new RcoCreateScheduleTaskRequest();
        rcoCreateScheduleTaskRequest.setTaskCycle(TaskCycleEnum.WEEK);
        rcoCreateScheduleTaskRequest.setScheduleTime("10:10:10");
        rcoCreateScheduleTaskRequest.setDayOfWeekArr(new Integer[] {1, 2});
        rcoCreateScheduleTaskRequest.setData(scheduleDataDTO);
        CronExpressionDTO cronExpressionDTO = new CronExpressionDTO();
        BeanUtils.copyProperties(rcoCreateScheduleTaskRequest, cronExpressionDTO);


        new Expectations(CycleScheduleTaskValidatorHandler.class) {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoCreateScheduleTaskRequest);
            }
        };
        scheduleAPI.createScheduleTask(rcoCreateScheduleTaskRequest);

        new Verifications() {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoCreateScheduleTaskRequest);
                times = 1;
                scheduleTaskAPI.add((CreateScheduleTaskRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 测试编辑定时任务方法当数据为null时
     * 
     * @throws Exception 异常
     */
    @Test
    public void testEditScheduleTaskWhileDataIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        ThrowExceptionTester.throwIllegalArgumentException(() -> scheduleAPI.editScheduleTask(null), "rcoEditScheduleTaskRequest can not be null");
        RcoEditScheduleTaskRequest rcoEditScheduleTaskRequest = new RcoEditScheduleTaskRequest();
        rcoEditScheduleTaskRequest.setTaskCycle(TaskCycleEnum.WEEK);
        rcoEditScheduleTaskRequest.setScheduleTime("10:10:10");
        rcoEditScheduleTaskRequest.setDayOfWeekArr(new Integer[] {1, 2});
        rcoEditScheduleTaskRequest.setId(id);

        CronExpressionDTO cronExpressionDTO = new CronExpressionDTO();
        BeanUtils.copyProperties(rcoEditScheduleTaskRequest, cronExpressionDTO);


        new Expectations(CycleScheduleTaskValidatorHandler.class) {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoEditScheduleTaskRequest);
            }
        };
        scheduleAPI.editScheduleTask(rcoEditScheduleTaskRequest);

        new Verifications() {
            {
                CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoEditScheduleTaskRequest);
                times = 1;
                scheduleTaskAPI.update((EditScheduleTaskRequest) any);
                times = 1;
            }
        };
    }



}
