package com.ruijie.rcos.rcdc.rco.module.impl.interceptor;

import com.ruijie.rcos.rcdc.rco.module.def.annotation.ServerModel;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.interceptor.OrderdHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Description: ServerModelOrderdHandlerInterceptor
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月04日
 *
 * @author wjp
 */

@Service
public class ServerModelOrderdHandlerInterceptor implements OrderdHandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerModelOrderdHandlerInterceptor.class);

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(response, "response can not be null");
        Assert.notNull(handler, "handler can not be null");

        if (!(handler instanceof HandlerMethod)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("框架自身封装处理器类型{}，不做处理", handler.getClass().getName());
            }
            return OrderdHandlerInterceptor.super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (Objects.nonNull(handlerMethod.getMethodAnnotation(ServerModel.class))
                || Objects.nonNull(handlerMethod.getMethod().getDeclaringClass().getAnnotation(ServerModel.class))) {
            ServerModel serverModel = getServerModel(handlerMethod);

            // 判断当前部署模式是否和允许的模式一致
            ServerModelEnum[] supportServerModeArr = serverModel.supportServerMode();
            String currentServerModel = serverModelAPI.getServerModel();
            boolean isMatch = Stream.of(supportServerModeArr).anyMatch(
                serverModelEnum -> Objects.equals(serverModelEnum.getName(), currentServerModel));

            // 若一致则返回
            if (isMatch) {
                return OrderdHandlerInterceptor.super.preHandle(request, response, handler);
            }

            // 不一致的情况下返回false
            LOGGER.info("[{}]部署模式下不支持请求[{}]接口，进行拦截", currentServerModel, handlerMethod.getMethod().getName());
            return false;
        }

        return OrderdHandlerInterceptor.super.preHandle(request, response, handler);
    }

    private ServerModel getServerModel(HandlerMethod handlerMethod) {
        ServerModel serverModel = handlerMethod.getMethodAnnotation(ServerModel.class);
        if (Objects.isNull(serverModel)) {
            serverModel = handlerMethod.getMethod().getDeclaringClass().getAnnotation(ServerModel.class);
        }
        return serverModel;
    }

    @Override
    public int getOrder() {
        return OrderdHandlerInterceptor.AFTER_FRAMEWORK + 1;
    }
}
