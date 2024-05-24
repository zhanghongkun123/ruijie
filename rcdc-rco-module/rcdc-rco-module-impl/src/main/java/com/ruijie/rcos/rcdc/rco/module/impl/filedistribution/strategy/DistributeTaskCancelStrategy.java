package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.strategy;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 文件分发任务取消策略
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/13
 *
 * @author zdc
 */
public interface DistributeTaskCancelStrategy {


    /**
     * 任务类型
     * @return 任务类型
     */
    FileDistributionTargetType targetType();

    /**
     * 取消任务策略
     *
     * @param subTaskDTO   目标任务
     * @param parameterDTO 任务参数
     * @throws BusinessException 业务异常
     */
    void doCancel(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException;

}
