package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 文件分发任务处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/22 12:15
 *
 * @author zhangyichi
 */
public interface DistributeTaskProcessor {

    /**
     * 策略类型
     * 
     * @return FileDistributionTargetType
     */
    FileDistributionTargetType fileDistributionTargetType();

    /**
     * 文件分发任务前处理
     * 
     * @param subTaskDTO 任务信息
     * @param parameterDTO 任务参数
     * @throws BusinessException 业务异常
     */
    void preProcess(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException;

    /**
     * 文件分发任务下发处理
     * 
     * @param subTaskDTO 任务信息
     * @param parameterDTO 任务参数
     * @throws BusinessException 业务异常
     */
    void doDistribute(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException;

    /**
     * 文件分发任务取消处理
     * 
     * @param subTaskDTO 任务信息
     * @param parameterDTO 任务参数
     * @throws BusinessException 业务异常
     */
    void doCancel(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException;

    /**
     * 文件分发任务后处理
     * 
     * @param subTaskDTO 任务信息
     * @param parameterDTO 任务参数
     * @throws BusinessException 业务异常
     */
    void postProcess(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException;

    /**
     * 文件分发任务异常处理
     * 
     * @param subTaskDTO 任务信息
     * @param parameterDTO 任务参数
     * @param exception 异常
     */
    void errorProcess(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO, Exception exception);

    /**
     * 执行文件分发任务
     *
     * @param subTaskDTO 任务信息
     * @param parameterDTO 任务参数
     * @throws BusinessException 业务异常
     */
    void execute(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException;
}
