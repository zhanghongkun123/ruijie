package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.RequestParamDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.connectkit.api.data.base.RemoteResponse;
import com.ruijie.rcos.sk.connectkit.api.data.base.RemoteResponseContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Description: Rest构建工具类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/10 14:42
 *
 * @author zqj
 */
@Component
public class RestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestUtil.class);

    /**
     * key 为nodeId,value为JwtToken
     */
    private static final Map<String, String> CLUSTER_JWT_MAP = new ConcurrentHashMap<>();

    /**
     * 重试3次
     */
    private static final Integer MAX_TRY_COUNT = 3;

    /**
     * 同步ftp账号信息失败，等待1s，继续重试
     */
    private static final Integer WAIT_TIME = 1;


    /**
     * 是否本地调试模式-跳关网关
     */
    @Value("${rest.util.local.enable:true}")
    private  boolean isLocalMode;


    /**
     * 尝试请求
     *
     * @param requestParam 请求参数
     * @return RemoteResponse
     * @throws BusinessException 业务异常
     */
    public RemoteResponse<JSONObject> tryRequest(RequestParamDTO<?> requestParam) throws BusinessException {
        Assert.notNull(requestParam, "requestParam can not be null");
        int count = 0;
        RemoteResponseContent responseContent = new RemoteResponseContent();
        while (count++ < MAX_TRY_COUNT) {
            try {
                RemoteResponse<JSONObject> remoteResponse = onceRequest(requestParam);
                if (remoteResponse != null) {
                    return remoteResponse;
                }
            } catch (BusinessException ex) {
                // 最后一次
                if (count == MAX_TRY_COUNT) {
                    LOGGER.error("尝试" + MAX_TRY_COUNT + "次， 请求集群失败！");
                    responseContent.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    responseContent.setMessage("远程请求错误：" + ex.getMessage());
                    throw ex;
                }
            }
            waitSeconds();
        }
        RemoteResponse remoteResponse = new RemoteResponse();
        remoteResponse.setContent(responseContent);
        return remoteResponse;
    }

    /**
     * 单次请求
     * @param requestParam 请求参数
     * @return RemoteResponse
     * @throws BusinessException 业务异常
     */
    public RemoteResponse<JSONObject> onceRequest(RequestParamDTO<?> requestParam) throws BusinessException {
        Assert.notNull(requestParam, "requestParam can not be null");
        try {
            JSONObject rs = request(requestParam, JSONObject.class);
            RemoteResponse<JSONObject> remoteResponse = rs.toJavaObject(RemoteResponse.class);
            if (CommonMessageCode.SUCCESS != remoteResponse.getContent().getCode()) {
                LOGGER.error("请求集群接口返回失败，remoteResponse：{}", JSON.toJSONString(remoteResponse));
                throw new BusinessException(BusinessKey.GATEWAY_REQUEST_ERROR,
                        StringUtils.isBlank(remoteResponse.getContent().getUserTip()) ? remoteResponse.getContent().getMessage()
                                : remoteResponse.getContent().getUserTip());
            }
            return remoteResponse;
        } catch (BusinessException ex) {
            LOGGER.error("请求集群失败,信息异常！", ex);
            throw ex;
        }
    }

    /**
     * 发起请求
     *
     * @param requestParam 请求参数
     * @param responseType 返回类型
     * @param <T> 返回类型
     * @return T
     * @throws BusinessException 业务异常
     */
    private <T, V> T request(RequestParamDTO<V> requestParam, Class<T> responseType) throws BusinessException {
        Assert.notNull(requestParam, "requestParam can not be null");
        Assert.notNull(responseType, "responseType can not be null");
        String path = requestParam.getPath();
        if (isLocalMode) {
            requestParam.setScheme("https");
            path = "/rccm/rest" + requestParam.getPath();
            requestParam.setNeedToken(false);
        }
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance().scheme(requestParam.getScheme()).host(requestParam.getIp())
                .port(requestParam.getPort()).path(path);
        // 设置Http的Header
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        headers.setContentType(type);

        URI url = uriComponentsBuilder.build(Maps.newHashMap());
        Map<String, Object> requestMap = new HashMap<>();
        if (requestParam.getHttpMethod() == HttpMethod.POST) {
            if (requestParam.getRequestData() != null) {
                requestMap.put("content", requestParam.getRequestData());
            } else {
                requestMap.put("content", new HashMap<>(16));
            }
        }
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("请求远程url:{},请求远程参数:{}", url.toString(), JSON.toJSONString(requestMap));
            }
            String rs = "";
            if (requestParam.getHttpMethod() == HttpMethod.GET) {
                rs = new HttpsUtil().doGet(url.toString(), headers, requestMap);
            } else {
                rs = new HttpsUtil().doRequest(url.toString(), headers, requestMap,requestParam.getHttpMethod());
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("获取远程数据:{}", rs);
            }
            return JSON.parseObject(rs, responseType);
        } catch (Exception ex) {
            LOGGER.error("请求失败url[{}],请求远程参数:[{}]", url.toString(), JSON.toJSONString(requestMap), ex);
            if (ex instanceof HttpClientErrorException) {
                HttpClientErrorException clientErrorException = (HttpClientErrorException) ex;
                if (clientErrorException.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    throw new BusinessException(BusinessKey.GATEWAY_REQUEST_NOT_FOUND_ROUTE, ex);
                }
            }
            if (ex instanceof HttpServerErrorException) {
                HttpServerErrorException clientErrorException = (HttpServerErrorException) ex;
                if (clientErrorException.getRawStatusCode() == HttpStatus.SERVICE_UNAVAILABLE.value()
                        || clientErrorException.getRawStatusCode() == HttpStatus.BAD_GATEWAY.value()
                        || clientErrorException.getRawStatusCode() == HttpStatus.GATEWAY_TIMEOUT.value()) {
                    // rcenter宕机异常
                    throw new BusinessException(BusinessKey.RCENTER_SERVICE_UNAVAILABLE, ex);
                }
                if (clientErrorException.getRawStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    throw new BusinessException(BusinessKey.RCENTER_SERVICE_INNER_ERROR, ex);
                }
                if (clientErrorException.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    throw new BusinessException(BusinessKey.GATEWAY_REQUEST_NOT_FOUND_ROUTE, ex);
                }

            }
            throw new BusinessException(BusinessKey.GATEWAY_REQUEST_SERVER_CONNECT_ERROR,ex);
        }

    }

    /**
     * 等待
     */
    private void waitSeconds() {
        try {
            TimeUnit.SECONDS.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            LOGGER.error("thread sleep has error", e);
        }
    }

    /**
     * 构造RCCM请求参数对象
     * @param rccmServerConfig RCCM纳管配置
     * @param requestUrlPath 请求路径
     * @param requestData 请求Body信息
     * @param <T> 请求对象泛型
     * @return RCCM请求参数泛型对象
     */
    public <T> RequestParamDTO<T> buildRccmRequestParamDTO(RccmServerConfigDTO rccmServerConfig, String requestUrlPath, T requestData) {
        Assert.notNull(rccmServerConfig, "rccmServerConfig can not be null");
        Assert.notNull(requestUrlPath, "requestUrlPath can not be null");
        Assert.notNull(requestData, "requestData can not be null");
        RequestParamDTO<T> requestParamDTO = new RequestParamDTO<>();
        requestParamDTO.setAccount(rccmServerConfig.getAccount());
        if (StringUtils.hasText(rccmServerConfig.getPassword())) {
            String decryptPwd = AesUtil.descrypt(rccmServerConfig.getPassword(), RedLineUtil.getRealAdminRedLine());
            requestParamDTO.setPwd(decryptPwd);
        }
        requestParamDTO.setIp(rccmServerConfig.getServerIp());
        requestParamDTO.setPort(rccmServerConfig.getGatewayPort());
        requestParamDTO.setPath(requestUrlPath);
        requestParamDTO.setRequestData(requestData);
        return requestParamDTO;
    }



}
