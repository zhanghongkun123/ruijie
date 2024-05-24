package com.ruijie.rcos.rcdc.rco.module.impl.datasync.init;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.user.UserSyncDataStrategy;
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
public class UserSyncDataStrategyFactory implements SafetySingletonInitializer {

    @Autowired
    private List<UserSyncDataStrategy> userSyncDataStrategyList;

    private Map<IacUserTypeEnum, UserSyncDataStrategy> userSyncDataStrategyMap;

    @Override
    public void safeInit() {
        userSyncDataStrategyMap =
                userSyncDataStrategyList.stream().collect(Collectors.toMap(UserSyncDataStrategy::getCbbUserType, Function.identity()));
    }

    /**
     * 获取处理更新处理策略
     *
     * @param cbbUserTypeEnum 数据源
     * @return UserSyncDataStrategy 策略
     */
    public UserSyncDataStrategy getUserSyncDataStrategy(IacUserTypeEnum cbbUserTypeEnum) {
        Assert.notNull(cbbUserTypeEnum, "cbbUserTypeEnum must not be null");
        return userSyncDataStrategyMap.get(cbbUserTypeEnum);
    }
}
