package com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.init;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.UserOperSyncNotifyStrategy;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 19:45
 *
 * @author coderLee23
 */
@Service
public class UserOperSyncNotifyStrategyFactory implements SafetySingletonInitializer {

    @Autowired
    private List<UserOperSyncNotifyStrategy> userSyncDataStrategyList;

    private Map<String, UserOperSyncNotifyStrategy> userSyncDataStrategyMap;

    @Override
    public void safeInit() {
        userSyncDataStrategyMap =
                userSyncDataStrategyList.stream().collect(Collectors.toMap(UserOperSyncNotifyStrategy::getOper, Function.identity()));
    }

    /**
     * 获取处理更新处理策略
     *
     * @param oper 策略类型
     * @return UserOperSyncNotifyStrategy 策略
     */
    public UserOperSyncNotifyStrategy getUserOperSyncNotifyStrategy(String oper) {
        Assert.hasText(oper, "oper must not be null or empty");
        return userSyncDataStrategyMap.get(oper);
    }
}
