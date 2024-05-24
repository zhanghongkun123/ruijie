package com.ruijie.rcos.rcdc.rco.module.impl.datasync.init;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.datasync.enums.UserGroupTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.group.UserGroupSyncDataStrategy;
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
public class UserGroupSyncDataStrategyFactory implements SafetySingletonInitializer {

    @Autowired
    private List<UserGroupSyncDataStrategy> userGroupSyncDataStrategyList;

    private Map<UserGroupTypeEnum, UserGroupSyncDataStrategy> userGroupSyncDataStrategyMap;

    @Override
    public void safeInit() {
        userGroupSyncDataStrategyMap =
                userGroupSyncDataStrategyList.stream().collect(Collectors.toMap(UserGroupSyncDataStrategy::getUserGroupType, Function.identity()));
    }

    /**
     * 获取处理更新处理策略
     *
     * @param userGroupType 数据源
     * @return UserGroupSyncDataStrategy 策略
     */
    public UserGroupSyncDataStrategy getUserGroupSyncDataStrategy(UserGroupTypeEnum userGroupType) {
        Assert.notNull(userGroupType, "userGroupType must not be null");
        return userGroupSyncDataStrategyMap.get(userGroupType);
    }
}
