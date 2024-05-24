package com.ruijie.rcos.rcdc.rco.module.openapi.rest.webclient.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserLicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.ForwardRcdcRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.WebClientRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ForwardRcdcResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.UserLicenseBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.request.UserSessionInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.InterfaceMethodUtil;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.utils.UserLicenseHelper;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.consts.RestApiConstants;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.webclient.WebClientServer;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description: 网页版客户端RESTful 接口实现
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
@Service
public class WebClientServerImpl implements WebClientServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientServerImpl.class);

    @Autowired
    private UserLicenseAPI userLicenseAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    private static final String DESK_ID = "desk_id";

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 100;

    /**
     * 线程池名称
     */
    private static final String THREAD_POOL_NAME = "cluster-forward-update-current-session-info";

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    /**
     * 转发RCDC集群等待时长
     */
    private static final int WAIT_TIME = 15;

    private static volatile Set<UUID> SUCCESS_REPORT_CLUSTER_ID_LIST = new CopyOnWriteArraySet<>();

    @Override
    public Result updateCurrentSessionInfo(WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(webClientRequest, "webClientRequest can not be null");

        UUID currentClusterId = getCurrentClusterId();
        UserSessionInfoRequest sessionInfoRequest = webClientRequest.getUserSessionInfoRequest();

        if (webClientRequest.getAccessClusterId() == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("本集群网页版客户端接入用户上报会话详情，原始请求：{}", JSONObject.toJSONString(webClientRequest));
            }
            // 属于本集群fengbo发送的，需要进行分流，设置接入集群为本集群
            UUID accessClusterId = getAccessClusterId(webClientRequest);
            webClientRequest.setAccessClusterId(accessClusterId);

            Map<UUID, WebClientRequest> clusterRequestMap = getClusterRequestMap(currentClusterId, accessClusterId, webClientRequest);
            String methodName = InterfaceMethodUtil.getCurrentMethodName();

            return asyncUpdateCurrentSessionInfoByCluster(currentClusterId, methodName, clusterRequestMap);
        }

        if (Objects.equals(currentClusterId, webClientRequest.getClusterId())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("归属本集群网页版客户端上报会话详情，真实请求：{}", JSONObject.toJSONString(webClientRequest));
            }
            // 更新网页版客户端上报时间
            UserLicenseHelper.updateTerminalReportTime(TerminalTypeEnum.WEB_CLIENT, String.valueOf(webClientRequest.getAccessClusterId()));

            if (sessionInfoRequest == null || CollectionUitls.isEmpty(sessionInfoRequest.getSessionInfoList())) {
                // 根据终端ID清理会话
                userLicenseAPI.clearUserSessionByTerminalId(TerminalTypeEnum.WEB_CLIENT, String.valueOf(webClientRequest.getAccessClusterId()));
            } else {

                List<UserSessionDTO> sessionInfoList = sessionInfoRequest.getSessionInfoList();
                // 根据用户分组会话
                Map<UUID, List<UserSessionDTO>> groupSessionInfoListByUserIdMap =
                        sessionInfoList.stream().collect(Collectors.groupingBy(UserSessionDTO::getUserId));
                // 获取网页版客户端当前所有会话用户ID列表
                List<UUID> allUserIdList = userLicenseAPI.findSessionUserIdListByTerminalId(TerminalTypeEnum.WEB_CLIENT,
                        String.valueOf(webClientRequest.getAccessClusterId()));
                for (UUID userId : allUserIdList) {
                    if (!groupSessionInfoListByUserIdMap.containsKey(userId)) {
                        groupSessionInfoListByUserIdMap.put(userId, new ArrayList<>());
                    }
                }
                for (Map.Entry<UUID, List<UserSessionDTO>> userSessionMapEntry : groupSessionInfoListByUserIdMap.entrySet()) {
                    userLicenseAPI.updateUserSessionAndLicense(userSessionMapEntry.getKey(), String.valueOf(webClientRequest.getAccessClusterId()),
                            TerminalTypeEnum.WEB_CLIENT, userSessionMapEntry.getValue());
                }
            }
        }
        Result response = new Result();
        response.setCode(CommonMessageCode.SUCCESS);

        return response;
    }

    private Result asyncUpdateCurrentSessionInfoByCluster(UUID currentClusterId, String methodName, Map<UUID, WebClientRequest> clientRequestMap)
            throws BusinessException {
        Set<UUID> successClusterSet = new CopyOnWriteArraySet<>();
        Map<UUID, Result> clusterResponseMap = new ConcurrentHashMap<>();

        CountDownLatch countDownLatch = new CountDownLatch(clientRequestMap.entrySet().size());
        for (Map.Entry<UUID, WebClientRequest> clusterSessionMapEntry : clientRequestMap.entrySet()) {
            WebClientRequest targetWebClientRequest = clusterSessionMapEntry.getValue();
            UUID targetClusterId = targetWebClientRequest.getClusterId();
            THREAD_EXECUTOR.execute(() -> {
                Result response = new Result();
                clusterResponseMap.put(targetClusterId, response);
                // 转发对应集群处理
                try {
                    if (rccmManageAPI.isOtherClusterRequest(targetWebClientRequest)) {
                        response = getForwardRcdcResponse(null, targetWebClientRequest, methodName, Result.class);
                    } else if (Objects.equals(currentClusterId, targetClusterId)) {
                        response = updateCurrentSessionInfo(targetWebClientRequest);
                    } else {
                        throw new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_UNIFIED_LOGIN_EXCEPTION_CANNOT_FORWARD_CLUSTER,
                                String.valueOf(targetClusterId));
                    }
                } catch (BusinessException e) {
                    // 捕获异常不做抛出
                    LOGGER.error("用户并发授权模式下，OPENAPI转发网页版客户端在线会话信息时，集群ID[{}]消息转发异常，转发消息：{}，异常：", targetClusterId,
                            JSONObject.toJSONString(targetWebClientRequest), e);
                    response.setCode(CommonMessageCode.FAIL);
                    response.setException(e);
                } finally {
                    countDownLatch.countDown();
                }
                if (response.getCode() == CommonMessageCode.SUCCESS) {
                    successClusterSet.add(targetClusterId);
                }
            });
        }

        try {
            countDownLatch.await(WAIT_TIME, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("等待所有RCDC请求结果，超时", e);
            Thread.currentThread().interrupt();
        }

        // 发生成功的集群ID记录下来，用于清空时给相应集群进行请求
        SUCCESS_REPORT_CLUSTER_ID_LIST = successClusterSet;

        if (clientRequestMap.entrySet().size() == 1 && clusterResponseMap.containsKey(currentClusterId)) {
            Result currentClusterResponse = clusterResponseMap.get(currentClusterId);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("单集群模式下，处理网页版客户端上报会话信息，请求信息:{}，响应信息：{}", JSONObject.toJSONString(clientRequestMap),
                        JSONObject.toJSONString(clusterResponseMap));
            }
            if (currentClusterResponse.getCode() == CommonMessageCode.FAIL && currentClusterResponse.getException() != null) {
                LOGGER.error("单集群模式下，处理网页版客户端上报会话信息，请求信息:{}，响应信息：{}，出现异常：{}", JSONObject.toJSONString(clientRequestMap),
                        JSONObject.toJSONString(clusterResponseMap), currentClusterResponse.getException());
                throw currentClusterResponse.getException();
            } else {
                return currentClusterResponse;
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("多集群模式下，处理网页版客户端上报会话信息，请求信息:{}，响应信息：{}", JSONObject.toJSONString(clientRequestMap),
                    JSONObject.toJSONString(clusterResponseMap));
        }
        Result finalResponse = new Result();
        if (successClusterSet.size() == clientRequestMap.entrySet().size()) {
            finalResponse.setCode(CommonMessageCode.SUCCESS);
            return finalResponse;
        } else {
            List<UUID> failClusterIdList =
                    clusterResponseMap.keySet().stream().filter(clusterId -> !successClusterSet.contains(clusterId)).collect(Collectors.toList());
            LOGGER.error("多集群模式下，处理网页版客户端上报会话信息，请求信息:{}，响应信息：{}，出现异常，异常上报集群：{}", JSONObject.toJSONString(clientRequestMap),
                    JSONObject.toJSONString(clusterResponseMap), JSONObject.toJSONString(failClusterIdList));
            throw new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_UPDATE_CURRENT_SESSION_INFO_PART_ERROR,
                    JSONObject.toJSONString(failClusterIdList));
        }
    }

    private Map<UUID, WebClientRequest> getClusterRequestMap(UUID currentClusterId, UUID accessClusterId, WebClientRequest webClientRequest) {
        UserSessionInfoRequest sessionInfoRequest = webClientRequest.getUserSessionInfoRequest();
        Assert.notNull(sessionInfoRequest, "sessionInfoRequest cannot be null");

        if (sessionInfoRequest.getSessionInfoList() == null) {
            sessionInfoRequest.setSessionInfoList(new ArrayList<>());
        }
        Map<UUID, List<UserSessionDTO>> clusterSessionInfoListMap;
        clusterSessionInfoListMap = sessionInfoRequest.getSessionInfoList().stream().peek(session -> {
            // 如果fengbo没有设置resourceClusterId，则代表单集群模式下设置为当前集群ID
            if (session.getResourceClusterId() == null) {
                session.setResourceClusterId(currentClusterId);
            }
            // 设置接入集群ID作为terminalId
            session.setTerminalId(String.valueOf(accessClusterId));
            session.setTerminalType(TerminalTypeEnum.WEB_CLIENT);
        }).collect(Collectors.groupingBy(UserSessionDTO::getResourceClusterId));

        // 需要缓存记录对应集群列表，如果新增会话列表不含对应集群 则发送空
        if (CollectionUitls.isEmpty(SUCCESS_REPORT_CLUSTER_ID_LIST)) {
            // 初始化后首次为空，则默认添加本集群
            SUCCESS_REPORT_CLUSTER_ID_LIST.add(currentClusterId);
        }

        // 目的是为了向曾经上报过的集群进行清空会话操作
        for (UUID clusterId : SUCCESS_REPORT_CLUSTER_ID_LIST) {
            if (!clusterSessionInfoListMap.containsKey(clusterId)) {
                clusterSessionInfoListMap.put(clusterId, new ArrayList<>());
            }
        }

        Map<UUID, WebClientRequest> clusterRequestMap = new HashMap<>();
        clusterSessionInfoListMap.forEach((targetClusterId, userSessionDTOList) -> {
            // 深拷贝对象进行并发请求
            String webClientRequestStr = JSONObject.toJSONString(webClientRequest);
            WebClientRequest targetWebClientRequest = JSONObject.parseObject(webClientRequestStr, WebClientRequest.class);

            // 设置目标集群ID
            targetWebClientRequest.setClusterId(targetClusterId);
            // 设置目标集群会话记录
            targetWebClientRequest.setUserSessionInfoRequest(new UserSessionInfoRequest(userSessionDTOList));
            clusterRequestMap.put(targetClusterId, targetWebClientRequest);
        });

        return clusterRequestMap;
    }


    private UUID getAccessClusterId(WebClientRequest webClientRequest) {
        UUID accessClusterId;
        if (webClientRequest.getAccessClusterId() != null) {
            accessClusterId = webClientRequest.getAccessClusterId();
        } else {
            accessClusterId = getCurrentClusterId();
        }
        return accessClusterId;
    }

    private UUID getCurrentClusterId() {
        UUID currentClusterId = Constants.DEFAULT_CLUSTER_ID;
        RccmServerConfigDTO rccmServerConfig = rccmManageAPI.getRccmServerConfig();
        if (rccmServerConfig != null && rccmServerConfig.getClusterId() != null) {
            currentClusterId = rccmServerConfig.getClusterId();
        }
        return currentClusterId;
    }

    private <T> T getForwardRcdcResponse(@Nullable UUID deskId, WebClientRequest webClientRequest, String methodName, Class<T> responseType)
            throws BusinessException {
        UUID clusterId = webClientRequest.getClusterId();
        Map<String, String> pathParam = new HashMap<>();
        if (deskId != null) {
            pathParam.put(DESK_ID, deskId.toString());
        }
        ForwardRcdcRequest request = getForwardRcdcRequest(clusterId, pathParam, methodName, (JSONObject) JSON.toJSON(webClientRequest));
        ForwardRcdcResponse forwardRcdcResponse = rccmManageAPI.forwardRequestByClusterId(request);
        if (forwardRcdcResponse.getResultCode() == CommonMessageCode.SUCCESS) {
            return forwardRcdcResponse.getContent().toJavaObject(responseType);
        }
        throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_FORWARD_RCDC_REQUEST_ERROR, forwardRcdcResponse.getMessage());
    }

    private ForwardRcdcRequest getForwardRcdcRequest(UUID clusterId, Map<String, String> pathParam, String methodName, JSONObject body) {
        Class<?> restAPIInterface = getRestAPIInterface(this.getClass());

        Method interfaceMethod = InterfaceMethodUtil.selectInterfaceMethod(restAPIInterface, methodName, WebClientRequest.class);

        String openApiUrl = InterfaceMethodUtil.getOpenAPIUrl(interfaceMethod, pathParam);
        String httpMethod = InterfaceMethodUtil.getHttpMethod(interfaceMethod);
        return new ForwardRcdcRequest(clusterId, openApiUrl, httpMethod, body);
    }


    /**
     * 获得类对应的RestAPI接口类对象
     *
     * @param clazz 类
     * @return 类对应的OpenAPI接口类对象
     */
    private Class<?> getRestAPIInterface(Class<? extends WebClientServerImpl> clazz) {
        Class<?>[] interfaceArr = clazz.getInterfaces();
        for (Class<?> anInterface : interfaceArr) {
            Path path = anInterface.getAnnotation(Path.class);
            if (path != null && RestApiConstants.WEBCLIENT_REST_API_PATH.equals(path.value())) {
                return anInterface;
            }
        }
        // 存在找不到方法
        return null;
    }
}
