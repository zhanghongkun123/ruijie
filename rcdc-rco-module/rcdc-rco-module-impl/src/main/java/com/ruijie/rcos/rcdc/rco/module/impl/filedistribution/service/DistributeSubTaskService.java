package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionStashTaskStatus;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryAPI;

/**
 * Description: 文件分发任务（子任务）Service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 15:50
 *
 * @author zhangyichi
 */
public interface DistributeSubTaskService extends PageQueryAPI<DistributeSubTaskDTO> {

    /**
     * 创建任务
     * 
     * @param subTaskDTO 子任务信息
     * @return 新任务ID
     */
    UUID createTask(DistributeSubTaskDTO subTaskDTO);

    /**
     * 根据父任务ID查询子任务
     * 
     * @param parentId 父任务ID
     * @return 子任务列表
     */
    List<DistributeSubTaskDTO> findByParentId(UUID parentId);

    /**
     * 根据ID查找子任务
     * 
     * @param subTaskId 子任务ID
     * @return 子任务信息
     * @throws BusinessException 业务异常
     */
    DistributeSubTaskDTO findById(UUID subTaskId) throws BusinessException;

    /**
     * 根据分发对象ID查找子任务
     * 
     * @param targetId 分发对象ID
     * @return 子任务列表
     */
    List<DistributeSubTaskDTO> findByTargetId(String targetId);

    /**
     * 根据暂存状态查找子任务
     * 
     * @param stashStatus 暂存状态
     * @return 子任务列表
     */
    List<DistributeSubTaskDTO> findByStashStatus(FileDistributionStashTaskStatus stashStatus);

    /**
     * 根据ID删除子任务
     * 
     * @param subTaskId 子任务ID
     */
    void deleteById(UUID subTaskId);

    /**
     * 将子任务设为取消状态
     * 
     * @param subTaskDTO 任务信息
     * @throws BusinessException 业务异常
     */
    void changeSubTaskToCancel(DistributeSubTaskDTO subTaskDTO) throws BusinessException;

    /**
     * 将子任务设为分发状态
     * 
     * @param subTaskDTO 任务信息
     */
    void changeSubTaskToRunning(DistributeSubTaskDTO subTaskDTO);

    /**
     * 将子任务设为失败状态
     * 
     * @param subTaskDTO 任务信息
     * @param failMessage 失败信息
     */
    void changeSubTaskToFail(DistributeSubTaskDTO subTaskDTO, @Nullable String failMessage);

    /**
     * 将子任务设为成功状态
     * 
     * @param subTaskDTO 任务信息
     */
    void changeSubTaskToSuccess(DistributeSubTaskDTO subTaskDTO);

    /**
     * 将子任务设为等待状态
     * 
     * @param subTaskDTO 任务信息
     */
    void changeSubTaskToWaiting(DistributeSubTaskDTO subTaskDTO);

    /**
     * 将子任务设为等待状态
     * 
     * @param subTaskDTO 任务信息
     */
    void changeRunningSubTaskToWaiting(DistributeSubTaskDTO subTaskDTO);

    /**
     * 修改子任务暂存状态
     * 
     * @param subTaskDTO 任务信息
     * @param stashStatus 暂存状态
     */
    void changeStashStatus(DistributeSubTaskDTO subTaskDTO, FileDistributionStashTaskStatus stashStatus);

    /**
     * 更新子任务时间
     * 
     * @param subTaskDTO 当前任务信息
     */
    void updateSubTaskTime(DistributeSubTaskDTO subTaskDTO);

    /**
     * 根据获取可执行的任务
     * 识别方式 targetId【云桌面】。相同云桌面同一时间只能执行一个任务。 可能存在多个父任务，必须优先执行最早的未完成的任务，如果该任务已经在队列或者线程中，则不在将该任务放入到队列中。
     *
     * @return 子任务列表
     */
    List<DistributeSubTaskDTO> findExecutableTaskList();

    /**
     * 根据targetID删除子任务
     *
     * @param targetId 业务ID，如云桌面ID
     */
    void deleteByTargetId(UUID targetId);

}
