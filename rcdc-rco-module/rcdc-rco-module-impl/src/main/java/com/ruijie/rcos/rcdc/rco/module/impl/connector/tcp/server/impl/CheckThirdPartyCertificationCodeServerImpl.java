package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.IacThirdPartyCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.ThirdPartyCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.CertificationResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.CheckThirdPartyCertificationCodeServer;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.Date;


/**
 * Description: 第三方认证服务认证
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月15日
 *
 * @author xwx
 */
public class CheckThirdPartyCertificationCodeServerImpl implements CheckThirdPartyCertificationCodeServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckThirdPartyCertificationCodeServerImpl.class);

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    public CertificationResultDTO checkThirdCertificationCode(String terminalId, ThirdPartyCertificationRequest request) {
        Assert.notNull(request, "request must not null");
        Assert.notNull(terminalId, "terminalId must not null");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("【第三方认证】收到客户端认证请求：{}", JSON.toJSONString(request));
        }
        CertificationResultDTO certificationResultDTO = new CertificationResultDTO();
        try {
            certificationResultDTO = userMgmtAPI.checkThirdCertificationCode(request);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("【第三方认证】认证结果为[{}]", JSON.toJSONString(certificationResultDTO));
            }
            if (!certificationResultDTO.isResult()) {
                certificationResultDTO.setMessage(LocaleI18nResolver.resolve(BusinessKey.CHECK_THIRD_PARTY_CODE_FAIL)
                        + certificationResultDTO.getMessage());
            }
            return certificationResultDTO;
        } catch (Exception e) {
            LOGGER.error("【第三方认证】认证失败，请求为{}", JSON.toJSONString(request), e);
            certificationResultDTO.setResult(false);
            certificationResultDTO.setMessage(LocaleI18nResolver.resolve(BusinessKey.CHECK_THIRD_PARTY_CODE_FAIL) + e.getMessage());
            return certificationResultDTO;
        } finally {
            userLoginRecordService.saveUserAuthInfo(terminalId, request.getUserName());
        }
    }
}
