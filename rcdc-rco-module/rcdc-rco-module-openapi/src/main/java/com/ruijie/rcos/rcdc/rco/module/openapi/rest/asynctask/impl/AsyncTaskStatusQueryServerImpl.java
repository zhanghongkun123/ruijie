package com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.ForwardRcdcRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.WebClientRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ForwardRcdcResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.dto.OpenApiTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.InterfaceMethodUtil;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.AsyncTaskStatusQueryServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.response.AsyncTaskStatusQueryResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Description: 获取异步任务状态openapi业务实现
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/26 18:36
 *
 * @author lyb
 */
@Service
public class AsyncTaskStatusQueryServerImpl implements AsyncTaskStatusQueryServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskStatusQueryServerImpl.class);

    @Autowired
    private OpenApiTaskInfoAPI openApiTaskInfoAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    private static final String TASK_ID = "task_id";

    @Override
    public AsyncTaskStatusQueryResponse asyncTaskStatusQuery(UUID taskId) throws BusinessException {
        Assert.notNull(taskId, "taskId must not be null");

        AsyncTaskStatusQueryResponse response = new AsyncTaskStatusQueryResponse();

        try {
            OpenApiTaskInfoDTO dto = openApiTaskInfoAPI.findByTaskId(taskId);
            if (dto.getId() == null) {
                throw new NoSuchElementException("未查询到对应的异步任务，taskId:{" + taskId + "}");
            }
            response.dtoToResponse(dto);
        } catch (NoSuchElementException e) {
            LOGGER.error("OpenAPI获取异步任务状态异常", e);
            throw new BusinessException(RestErrorCode.OPEN_API_TASK_CAN_NOT_FOUND, e);
        } catch (Exception e) {
            LOGGER.error("OpenAPI获取异步任务状态系统异常", e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }
        return response;
    }

    @Override
    public AsyncTaskStatusQueryResponse asyncTaskStatusQuery(UUID taskId, @Nullable WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(taskId, "taskId must not be null");
        if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
            String methodName = InterfaceMethodUtil.getCurrentMethodName();
            // RCenter进行请求转发
            UUID clusterId = webClientRequest.getClusterId();
            Map<String, String> pathParam = new HashMap<>();
            pathParam.put(TASK_ID, taskId.toString());

            ForwardRcdcRequest request = getForwardRcdcRequest(clusterId, pathParam, methodName);
            ForwardRcdcResponse forwardRcdcResponse = rccmManageAPI.forwardRequestByClusterId(request);
            if (forwardRcdcResponse.getResultCode() == CommonMessageCode.SUCCESS) {
                return forwardRcdcResponse.getContent().toJavaObject(AsyncTaskStatusQueryResponse.class);
            }
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_FORWARD_RCDC_REQUEST_ERROR, forwardRcdcResponse.getMessage());
        }

        return asyncTaskStatusQuery(taskId);
    }

    private ForwardRcdcRequest getForwardRcdcRequest(UUID clusterId, Map<String, String> pathParam, String methodName) {
        Class<?> openAPIInterface = getOpenAPIInterface(this.getClass());
        Method interfaceMethod = InterfaceMethodUtil.selectInterfaceMethod(openAPIInterface, methodName, UUID.class, WebClientRequest.class);

        String openApiUrl = InterfaceMethodUtil.getOpenAPIUrl(interfaceMethod, pathParam);
        String httpMethod = InterfaceMethodUtil.getHttpMethod(interfaceMethod);
        return new ForwardRcdcRequest(clusterId, openApiUrl, httpMethod, null);
    }


    /**
     * 获得类对应的OpenAPI接口类对象
     *
     * @param clazz 类
     * @return 类对应的OpenAPI接口类对象
     */
    private Class<?> getOpenAPIInterface(Class<? extends AsyncTaskStatusQueryServerImpl> clazz) {
        Class<?>[] interfaceArr = clazz.getInterfaces();
        for (Class<?> anInterface : interfaceArr) {
            OpenAPI annotation = anInterface.getAnnotation(OpenAPI.class);
            if (annotation != null) {
                return anInterface;
            }
        }
        //存在找不到方法
        return null;
    }
}
