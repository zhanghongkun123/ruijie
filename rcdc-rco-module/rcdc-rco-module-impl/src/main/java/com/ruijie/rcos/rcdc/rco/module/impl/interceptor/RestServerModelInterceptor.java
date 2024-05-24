package com.ruijie.rcos.rcdc.rco.module.impl.interceptor;

import com.ruijie.rcos.rcdc.rco.module.def.annotation.ServerModel;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.interceptor.ConnectorInterceptor;
import com.ruijie.rcos.sk.connectkit.api.interceptor.ConnectorInterceptorRole;
import com.ruijie.rcos.sk.connectkit.api.invocation.Invocation;
import com.ruijie.rcos.sk.connectkit.api.invocation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/17 17:40
 *
 * @author yxq
 */
@Service
public class RestServerModelInterceptor implements ConnectorInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestServerModelInterceptor.class);

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Override
    public void preProcess(Invocation invocation) throws Throwable {
        Assert.notNull(invocation, "invocation can not be null");

        if (Objects.nonNull(invocation.getMethod().getAnnotation(ServerModel.class))
                || Objects.nonNull(invocation.getMethod().getDeclaringClass().getAnnotation(ServerModel.class))) {
            ServerModel serverModel = getServerModel(invocation);

            // 判断当前部署模式是否和允许的模式一致
            ServerModelEnum[] supportServerModeArr = serverModel.supportServerMode();
            String currentServerModel = serverModelAPI.getServerModel();
            boolean isMatch = Stream.of(supportServerModeArr).anyMatch(
                serverModelEnum -> Objects.equals(serverModelEnum.getName(), currentServerModel));

            // 若一致则返回
            if (isMatch) {
                return;
            }

            // 不一致的情况下默认抛出异常
            LOGGER.info("[{}]部署模式下不支持请求[{}]接口，进行拦截", currentServerModel, invocation.getMethod().getName());
            throw new BusinessException(serverModel.businessExKey());
        }
    }

    private ServerModel getServerModel(Invocation invocation) {
        ServerModel serverModel = invocation.getMethod().getAnnotation(ServerModel.class);
        if (Objects.nonNull(serverModel)) {
            return serverModel;
        }
        return invocation.getMethod().getDeclaringClass().getAnnotation(ServerModel.class);
    }

    @Override
    public void postProcess(Invocation invocation, Result result) throws Throwable {

    }

    @Override
    public void failureProcess(Invocation invocation, Throwable throwable) {

    }

    @Override
    public boolean processWhenRetrying() {
        return false;
    }

    @Override
    public int getOrder() {
        return AFTER_FRAMEWORK_ORDER + 1;
    }

    @Override
    public ConnectorInterceptorRole[] getInterceptorRole() {
        return new ConnectorInterceptorRole[]{ConnectorInterceptorRole.SERVICE};
    }
}
