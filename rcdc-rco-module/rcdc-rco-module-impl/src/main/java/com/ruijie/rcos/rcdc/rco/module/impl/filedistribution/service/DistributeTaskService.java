package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 文件分发任务（父任务）Service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 15:04
 *
 * @author zhangyichi
 */
public interface DistributeTaskService {

    /**
     * 创建文件分发任务（父任务）记录
     * @param taskDTO 任务信息
     * @return 父任务ID
     */
    UUID createTask(DistributeTaskDTO taskDTO);

    /**
     * 根据父任务ID查找任务信息
     * @param taskId 父任务ID
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    DistributeTaskDTO findTaskById(UUID taskId) throws BusinessException;

    /**
     * 根据任务名称查找任务
     * @param name 任务名称
     * @return 任务列表
     */
    List<DistributeTaskDTO> findTaskByName(String name);

    /**
     * 根据参数ID查找文件分发任务列表
     * @param parameterId 参数ID
     * @return 任务列表
     */
    List<DistributeTaskDTO> findTaskByParameterId(UUID parameterId);

    /**
     * 根据ID删除文件分发任务（父任务）
     * @param taskId 任务ID
     */
    void deleteById(UUID taskId);
}
