package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.strategy.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.RcacRestClient;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request.CancelDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.strategy.DistributeTaskCancelStrategy;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 对象为云应用客户端的文件分发任务取消策略
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/13
 *
 * @author zdc
 */
@Service
public class AppClientDistributeTaskCancelStrategy implements DistributeTaskCancelStrategy {


    @Autowired
    private RcacRestClient rcacRestClient;

    @Override
    public FileDistributionTargetType targetType() {
        return FileDistributionTargetType.APP_CLIENT;
    }

    @Override
    public void doCancel(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO,"subTaskDTO must not be null");
        Assert.notNull(parameterDTO,"parameterDTO must not be null");
        CancelDistributeTaskRequest request = new CancelDistributeTaskRequest();
        request.setRcaClientId(resolveTargetId(subTaskDTO));
        request.setTaskId(subTaskDTO.getId());
        rcacRestClient.cancelDistributeTask(request);
        
    }

    private Integer resolveTargetId(DistributeSubTaskDTO subTaskDTO) {
        return Integer.valueOf(subTaskDTO.getTargetId());
    }

}
