package com.ruijie.rcos.rcdc.rco.module.web.validation;

import com.ruijie.rcos.rcdc.rco.module.def.sms.constnts.SmsAndScanCodeCheckConstants;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.HttpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.HttpResultParserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.SmsPlatformType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.HttpPairRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.SmsConverterUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.SmsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request.SmsGatewayConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request.UpdateSmsGatewayConfigWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * Description: 短信服务器校验
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author TD
 */
@Service
public class SmsGatewayValidation {

    /**
     * 短信服务器配置校验
     * @param webRequest 请求参数
     * @throws BusinessException 业务异常
     */
    public void smsGatewayConfigValidation(UpdateSmsGatewayConfigWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        SmsGatewayConfigWebRequest gatewayConfigWebRequest = webRequest.getGatewayConfigWebRequest();
        // 短信认证服务器未开启，直接不对内容进行校验
        if (!BooleanUtils.toBoolean(webRequest.getEnable()) || Objects.isNull(gatewayConfigWebRequest)) {
            return;
        }
        SmsPlatformType platform = gatewayConfigWebRequest.getPlatformType();
        if (platform == SmsPlatformType.HTTP) {
            HttpConfigDTO httpConfig = gatewayConfigWebRequest.getHttpConfig();
            if (Objects.isNull(httpConfig)) {
                throw new BusinessException(SmsBusinessKey.RCDC_RCO_SMS_GATEWAY_PLATFORM_IS_HTTP_CONFIG_ERROR);
            }
            // 校验编码方式是否正确
            try {
                Charset.forName(httpConfig.getEncodingType());
            } catch (Exception e) {
                throw new BusinessException(SmsBusinessKey.RCDC_RCO_ENCODING_TYPE_ERROR, e);
            }
            // 校验请求头配置个数是否超出限制
            List<HttpPairRequest> pairHeaderList = httpConfig.getHeaderList();
            if (Objects.nonNull(pairHeaderList) && pairHeaderList.size() > SmsAndScanCodeCheckConstants.HEADER_MAX_LIMIT) {
                throw new BusinessException(SmsBusinessKey.RCDC_RCO_HTTP_REQUEST_HEADER_MAX_LIMIT_ERROR, 
                        String.valueOf(SmsAndScanCodeCheckConstants.HEADER_MAX_LIMIT));
            }
            // 校验请求内容配置，是否包含手机和短信验证码
            String body = httpConfig.getBody();
            if (!body.contains(SmsConverterUtils.resolvingJoin(SmsAndScanCodeCheckConstants.MOBILE_NUM)) ||
                    !body.contains(SmsConverterUtils.resolvingJoin(SmsAndScanCodeCheckConstants.SMS_CONTENT))) {
                throw new BusinessException(SmsBusinessKey.RCDC_RCO_HTTP_REQUEST_BODY_CONTAIN_PHONE_CONTENT_PLACEHOLDER);
            }
            // 校验接收配置
            if (Boolean.TRUE.equals(httpConfig.getEnableParser())) {
                HttpResultParserDTO httpResultParser = httpConfig.getHttpResultParser();
                // 开启接收配置的情况下，其中内容都不能为空
                if (Objects.isNull(httpResultParser)) {
                    throw new BusinessException(SmsBusinessKey.RCDC_RCO_HTTP_PARSE_CONFIG_PARAMS_ERROR);
                } 
                // 接收配置类型为STRING的情况下，认证结果配置必须满足如下条件：
                // 1.必须包含$$CODE$$，2.$$必须成对出现，3.$$不能连续出现
                String successKey = httpResultParser.getSuccessKey();
                if (httpResultParser.getMessageType() == MessageType.STRING &&
                        (!successKey.contains(SmsConverterUtils.resolvingJoin(SmsAndScanCodeCheckConstants.STRING_CODE))
                        || !(SmsConverterUtils.getResolvingTemplateIndexList(successKey).size() % 2 == 0)
                        || successKey.contains(SmsAndScanCodeCheckConstants.PLACEHOLDER_RESOLVING + 
                        SmsAndScanCodeCheckConstants.PLACEHOLDER_RESOLVING))) {
                    throw new BusinessException(SmsBusinessKey.RCDC_RCO_HTTP_PARSE_CONFIG_AUTH_RESULT_ERROR);
                }
            }
        }
    }
    
}
