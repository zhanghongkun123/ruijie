package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.base.task.module.def.dto.ScheduleTaskTypeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.RcoScheduleTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoCreateScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoEditScheduleTaskRequest;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月16日
 *
 * @author xgx
 */
public interface ScheduleAPI {
    /**
     * 获取定时任务详情
     * 
     * @param taskId 请求对象
     * @return 定时任务详情
     * @throws BusinessException 异常
     */

    RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>> queryScheduleTask(UUID taskId) throws BusinessException;

    /**
     * 分页获取定时任务列表
     * 
     * @param pageRequest 分页请求对象
     * @return 定时任务列表
     * @throws BusinessException 异常
     */

    DefaultPageResponse<RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>>> queryScheduleTaskByPage(DefaultPageRequest pageRequest)
            throws BusinessException;

    /**
     * 创建定时任务
     * 
     * @param rcoCreateScheduleTaskRequest 请求对象
     * @throws BusinessException 异常
     * @throws AnnotationValidationException 注解校验异常
     */

    void createScheduleTask(RcoCreateScheduleTaskRequest rcoCreateScheduleTaskRequest) throws BusinessException, AnnotationValidationException;

    /**
     * 编辑定时任务
     * 
     * @param rcoEditScheduleTaskRequest 请求对象
     * @throws BusinessException 异常
     * @throws AnnotationValidationException 注解校验异常
     */

    void editScheduleTask(RcoEditScheduleTaskRequest rcoEditScheduleTaskRequest) throws BusinessException, AnnotationValidationException;

    /**
     * 定时任务类型排序
     * @param scheduleTaskTypeDTOS 定时任务类型数组
     * @return 排序后的DTO
     */
    ScheduleTaskTypeDTO[] sortScheduleTaskTypeList(ScheduleTaskTypeDTO[] scheduleTaskTypeDTOS);
}
