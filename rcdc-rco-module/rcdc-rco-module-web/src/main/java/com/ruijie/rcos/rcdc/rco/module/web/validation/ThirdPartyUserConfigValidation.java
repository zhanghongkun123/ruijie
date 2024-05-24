package com.ruijie.rcos.rcdc.rco.module.web.validation;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyHttpConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyHttpPairRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyResultParserConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserSyncConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.MessageType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.ThirdPartyUserSyncTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.ThirdUserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ThirdPartyConfigBaseRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ThirdPartyConfigTestAuthRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ThirdPartyConfigWithUserSyncRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 第三方认证服务器校验
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/26
 *
 * @author zjy
 */
@Service
public class ThirdPartyUserConfigValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyUserConfigValidation.class);

    private static final int MAX_HTTP_BODY_LENGTH = 1024;

    private static final int MIN_TIMEOUT = 1;

    private static final int MAX_TIMEOUT = 120;

    private static final int MAX_SYNC_TIMEOUT = 600;

    private static final int MAX_HEADER_SIZE = 10;

    private static final int MAX_HEAD_KEY_TEXT_SIZE = 32;

    private static final int MAX_HEAD_VALUE_TEXT_SIZE = 128;

    private static final String USER_NAME = "$$USERNAME$$";

    private static final String PASSWORD = "$$PASSWORD$$";

    private static final String STRING_RESULT_KEY = "$$CODE$$";

    /**
     * 第三方认证服务器校验
     * @param webRequest 请求参数
     * @throws BusinessException 业务异常
     */
    public void thirdPartyUserConfigValidation(ThirdPartyConfigWithUserSyncRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        // 认证服务器未开启，直接不对内容进行校验
        if (!BooleanUtils.toBoolean(webRequest.getThirdPartyEnable())) {
            return;
        }

        checkHttpAndResultConfig(webRequest);

        // 用户同步解析校验
        BaseThirdPartyUserSyncConfigDTO userSyncConfig = webRequest.getUserSyncConfig();
        if (userSyncConfig != null) {
            if (userSyncConfig.getUserSyncType() == null) {
                throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_SYNC_USER_TYPE_NOT_NULL);
            }
            if (userSyncConfig.getUserSyncType() == ThirdPartyUserSyncTypeEnum.TIMER
                    && StringUtils.isBlank(userSyncConfig.getUserSyncTime())) {
                throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_SYNC_USER_TIME_ERROR);
            }
            if (userSyncConfig.getUserSyncType() == ThirdPartyUserSyncTypeEnum.PERIOD
                    && (userSyncConfig.getUserSyncPeriod() == null || userSyncConfig.getUserSyncPeriod() <= 0
                        || userSyncConfig.getUserSyncPeriod() > 23)) {
                throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_SYNC_USER_PERIOD_ERROR);
            }
            if (userSyncConfig.getUserSyncTimeout() == null || userSyncConfig.getUserSyncTimeout() < MIN_TIMEOUT
                    || userSyncConfig.getUserSyncTimeout() > MAX_SYNC_TIMEOUT) {
                throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_SYNC_USER_TIMEOUT_INVALID);
            }
            if (userSyncConfig.getUserSyncCoverType() == null) {
                throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_SYNC_USER_COVER_NOT_NULL);
            }
        }
    }

    /**
     * 第三方认证测试校验
     * @param webRequest 请求参数
     * @throws BusinessException 业务异常
     */
    public void thirdPartyUserAuthValidation(ThirdPartyConfigTestAuthRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        // 认证服务器未开启，直接不对内容进行校验
        if (!BooleanUtils.toBoolean(webRequest.getThirdPartyEnable())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_UNABLE);
        }

        checkHttpAndResultConfig(webRequest);
    }

    private void checkHttpAndResultConfig(ThirdPartyConfigBaseRequest webRequest) throws BusinessException {
        // http请求校验
        if (webRequest.getAuthOrigin() == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_PLATFORM_NOT_NULL);
        }
        BaseThirdPartyHttpConfigDTO httpConfig = webRequest.getHttpConfig();
        if (httpConfig == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_NOT_NULL);
        }
        if (httpConfig.getHttpMethod() == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_HTTP_METHOD_NOT_NULL);
        }
        if (StringUtils.isBlank(httpConfig.getUrl())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_HTTP_URL_NOT_NULL);
        }

        try {
            URL url = new URL(httpConfig.getUrl());
        } catch (MalformedURLException e) {
            LOGGER.error("请求地址存在异常，url 为 [{}]", httpConfig.getUrl(), e);
            throw new BusinessException(ThirdUserBusinessKey.RCDC_USER_THIRD_PARTY_GET_USER_URL_INVALID, e);
        }
        if (!StringUtils.isBlank(httpConfig.getEncodingType())) {
            try {
                Charset.forName(httpConfig.getEncodingType());
            } catch (Exception e) {
                throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_ENCODING_TYPE_ERROR, e);
            }
        }
        Integer timeout = httpConfig.getTimeout();
        if (timeout == null || timeout < MIN_TIMEOUT || timeout > MAX_TIMEOUT) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_TIMEOUT_INVALID);
        }
        // 校验请求头配置个数是否超出限制
        List<BaseThirdPartyHttpPairRequest> httpHeaderList = httpConfig.getHttpHeaderList();
        if (!CollectionUtils.isEmpty(httpHeaderList)) {
            if (httpHeaderList.size() > MAX_HEADER_SIZE) {
                throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_HEADER_MAX_LIMIT_ERROR,
                        String.valueOf(MAX_HEADER_SIZE));
            }

            for (BaseThirdPartyHttpPairRequest cbbThirdPartyHttpPairRequest : httpHeaderList) {
                if (StringUtils.isBlank(cbbThirdPartyHttpPairRequest.getKey()) ||
                        cbbThirdPartyHttpPairRequest.getKey().length() > MAX_HEAD_KEY_TEXT_SIZE) {
                    throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_HEADER_KEY_MAX_LIMIT_ERROR,
                            String.valueOf(MAX_HEAD_KEY_TEXT_SIZE));
                }

                if (StringUtils.isBlank(cbbThirdPartyHttpPairRequest.getValue()) ||
                        cbbThirdPartyHttpPairRequest.getValue().length() > MAX_HEAD_VALUE_TEXT_SIZE) {
                    throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_HEADER_VALUE_MAX_LIMIT_ERROR,
                            String.valueOf(MAX_HEAD_VALUE_TEXT_SIZE));
                }
            }
        }

        if (httpConfig.getEnableHide() == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_BODY_HIDE_NOT_NULL);
        }
        String httpBody = httpConfig.getHttpBody();
        if (StringUtils.isEmpty(httpBody) || httpBody.length() > MAX_HTTP_BODY_LENGTH) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_BODY_TOO_LONG);
        }
        if (!httpBody.contains(USER_NAME) || !httpBody.contains(PASSWORD)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_HTTP_CONFIG_BODY_CONTAIN_USERNAME_PASSWORD);
        }

        // 返回结果解析校验
        BaseThirdPartyResultParserConfigDTO resultParserConfig = webRequest.getResultParserConfig();
        if (resultParserConfig == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_RESULT_PARSER_CONFIG_NOT_NULL);
        }
        if (resultParserConfig.getMessageType() == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_RESULT_PARSER_TYPE_NOT_NULL);
        }
        String successKey = resultParserConfig.getSuccessKey();
        if (StringUtils.isBlank(successKey)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_RESULT_PARSER_SUCCESS_KEY_NOT_NULL);
        }
        if (StringUtils.isBlank(resultParserConfig.getSuccessValue())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_RESULT_PARSER_SUCCESS_VALUE_NOT_NULL);
        }
        // 接收配置类型为STRING的情况下，认证结果配置必须满足如下条件：
        // 1.必须包含$$CODE$$
        if (resultParserConfig.getMessageType() == MessageType.STRING && !successKey.contains(STRING_RESULT_KEY)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_CONFIG_RESULT_PARSER_STRING_AUTH_RESULT_ERROR);
        }
    }

}
