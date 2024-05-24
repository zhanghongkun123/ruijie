package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.DistributeTaskFactory;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.DistributeTaskProcessor;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 * Description: 文件分发任务工厂
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/22 12:12
 *
 * @author zhangyichi
 */
@Service
public class DistributeTaskFactoryImpl implements DistributeTaskFactory, SafetySingletonInitializer {


    private Map<FileDistributionTargetType, DistributeTaskProcessor> processorMap;

    @Autowired
    private List<DistributeTaskProcessor> desktopProcessorList;



    @Override
    public void safeInit() {
        processorMap =
                desktopProcessorList.stream().collect(Collectors.toMap(DistributeTaskProcessor::fileDistributionTargetType, Function.identity()));
    }


    /**
     * 构造文件分发任务
     * 
     * @param fileDistributionTargetType 任务信息
     * @return 文件分发任务
     */
    @Override
    public DistributeTaskProcessor buildDistributeTaskProcessor(FileDistributionTargetType fileDistributionTargetType) {
        Assert.notNull(fileDistributionTargetType, "subTaskDTO cannot be null!");
        return processorMap.get(fileDistributionTargetType);

    }

}
