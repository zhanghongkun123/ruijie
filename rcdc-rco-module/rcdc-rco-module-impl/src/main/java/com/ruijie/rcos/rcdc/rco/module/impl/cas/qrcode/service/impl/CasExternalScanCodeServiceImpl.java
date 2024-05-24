package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.AuthTokenDTO;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanOutDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.connect.HttpsClient;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums.CasQrUrlEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service.CasExternalScanCodeService;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.service.CasScanCodeAuthService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.connect.SslConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/24
 *
 * @author TD
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CasExternalScanCodeServiceImpl implements CasExternalScanCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CasExternalScanCodeServiceImpl.class);

    private static final String ROUTING = "routing";

    private static final String SLASH = "/";

    @Autowired
    private CasScanCodeAuthService codeAuthService;

    private AuthTokenDTO authTokenDTO;

    private HttpsClient httpsClient;

    private synchronized HttpsClient obtainHttpsClient(SslConfig sslConfig) throws BusinessException {
        if (httpsClient == null) {
            try {
                httpsClient = new HttpsClient(sslConfig);
            } catch (Exception e) {
                LOGGER.error("初始化HTTPS请求客户端出错", e);
                // 记得新增国际化解释
                throw new BusinessException(RCDC_HTTPS_CLIENT_INITIAL_FAIL, e);
            }
        }
        return httpsClient;
    }

    @Override
    public AuthTokenDTO getAuthTokenService(CasScanCodeAuthDTO codeAuthDTO) throws BusinessException {
        Assert.notNull(codeAuthDTO, "CasScanCodeAuthDTO is not null");
        Map<String, Object> body = new HashMap<>();
        body.put("serviceId", codeAuthDTO.getApplyAuthCode());
        String result = obtainHttpsClient(codeAuthDTO.getSslConfig())
                .doPost(addressJoin(codeAuthDTO.getApplyServicePrefix(), CasQrUrlEnum.GET_CONFIG.getName()), null, body);
        LOGGER.info("getAuthTokenService back content: {}", result);
        // 初始化接口返回没有routing字段，认定为失败
        if (StringUtils.isEmpty(result) || !JSON.parseObject(result).containsKey(ROUTING)) {
            LOGGER.error("getAuthTokenService result content：{}，not exist routing", result);
            throw new BusinessException(RCDC_HTTPS_CLIENT_INITIAL_FAIL);
        }
        AuthTokenDTO authToken = JSON.parseObject(result, AuthTokenDTO.class);
        authToken.setExpireTime(System.currentTimeMillis());
        return authToken;
    }

    @Override
    public CasScanOutDTO getQrCodeService() throws BusinessException {
        CasScanCodeAuthDTO codeAuthDTO = codeAuthService.getCasScanCodeAuthInfo();
        Map<String, Object> header = new HashMap<>();
        header.put(ROUTING, this.obtainInitAuthService().getRouting());
        String result = obtainHttpsClient(codeAuthDTO.getSslConfig())
                .doGet(addressJoin(codeAuthDTO.getApplyServicePrefix(), CasQrUrlEnum.GET_QR_CODE.getName()), header);
        return JSON.parseObject(result, CasScanOutDTO.class);
    }

    @Override
    public CasScanOutDTO monitorScanResultService(String qrCodeId) throws BusinessException {
        Assert.notNull(qrCodeId, "监听二维码ID not null");

        CasScanCodeAuthDTO codeAuthDTO = codeAuthService.getCasScanCodeAuthInfo();
        Map<String, Object> header = new HashMap<>();
        header.put(ROUTING, this.obtainInitAuthService().getRouting());
        Map<String, Object> body = new HashMap<>();
        body.put("id", qrCodeId);
        String result = obtainHttpsClient(codeAuthDTO.getSslConfig())
                .doGet(addressJoin(codeAuthDTO.getApplyServicePrefix(), CasQrUrlEnum.MONITOR.getName()), header, body);
        LOGGER.info("monitorScanResultService back content: {}", result);
        return JSON.parseObject(result, CasScanOutDTO.class);
    }

    @Override
    public String ticketValidateService(String ticket, String serviceId) throws BusinessException {
        Assert.notNull(serviceId, "二维码ID not null");
        Assert.notNull(ticket, "票据ticket not null");

        CasScanCodeAuthDTO codeAuthDTO = codeAuthService.getCasScanCodeAuthInfo();
        Map<String, Object> header = new HashMap<>();
        header.put(ROUTING, this.obtainInitAuthService().getRouting());
        Map<String, Object> body = new HashMap<>();
        body.put("st", ticket);
        body.put("service", serviceId);
        return obtainHttpsClient(codeAuthDTO.getSslConfig())
                .doPost(addressJoin(codeAuthDTO.getApplyServicePrefix(), CasQrUrlEnum.VALIDATE.getName()), header, body);
    }

    private synchronized AuthTokenDTO obtainInitAuthService() throws BusinessException {
        if (this.authTokenDTO != null && this.authTokenDTO.isTokenExpires()) {
            return authTokenDTO;
        }
        this.authTokenDTO = this.getAuthTokenService(codeAuthService.getCasScanCodeAuthInfo());
        return this.authTokenDTO;
    }

    private String addressJoin(String urlPrefix, String urlSuffix) throws BusinessException {
        if (StringUtils.isEmpty(urlPrefix) || StringUtils.isEmpty(urlSuffix)) {
            LOGGER.error("CAS service urlPrefix：{}，OR urlSuffix：{}，null", urlPrefix, urlSuffix);
            throw new BusinessException(RCDC_HTTPS_CLIENT_INITIAL_FAIL);
        }
        return urlPrefix.endsWith(SLASH) ? urlPrefix.concat(urlSuffix) : urlPrefix.concat(SLASH).concat(urlSuffix);
    }
}
