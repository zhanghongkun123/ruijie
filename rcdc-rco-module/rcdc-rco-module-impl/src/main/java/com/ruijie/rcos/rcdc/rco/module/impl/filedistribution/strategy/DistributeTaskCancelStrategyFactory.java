package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.strategy;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 文件分发任务取消策略工厂类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/13
 *
 * @author zdc
 */
@Service
public class DistributeTaskCancelStrategyFactory implements SafetySingletonInitializer {


    @Autowired
    private List<DistributeTaskCancelStrategy> distributeTaskCancelStrategyList;

    private Map<FileDistributionTargetType, DistributeTaskCancelStrategy> distributeTaskCancelStrategyMap;

    @Override
    public void safeInit() {
        distributeTaskCancelStrategyMap = distributeTaskCancelStrategyList.stream()
                .collect(Collectors.toMap(DistributeTaskCancelStrategy::targetType, Function.identity()));
    }

    /**
     * 获取分发任务取消策略
     *
     * @param targetType 数据源
     * @return DistributeTaskCancelStrategy 策略
     */
    public DistributeTaskCancelStrategy getDistributeTaskCancelStrategy(FileDistributionTargetType targetType) {
        Assert.notNull(targetType, "targetType must not be null");
        return distributeTaskCancelStrategyMap.get(targetType);
    }
}
