package com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.def.api.QrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr.QrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr.QrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.GetQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeClientReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeConfigReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrLoginReq;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.QrCodeRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.qrlogin.enums.QrCodeErrorKeyEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 二维码登录相关接口实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-21 15:12:00
 *
 * @author zjy
 */
public class QrCodeRestServerImpl implements QrCodeRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeRestServerImpl.class);

    @Autowired
    private QrCodeAPI qrCodeAPI;

    @Override
    public QrCodeDTO getQrCode(GetQrCodeReq getQrCodeRequest) throws BusinessException {
        Assert.notNull(getQrCodeRequest, "getQrCodeRequest cannot null");
        LOGGER.info("创建二维码，用户id:[{}], 用户类型:[{}]", getQrCodeRequest.getClientId(), getQrCodeRequest.getQrCodeType().name());

        QrCodeDTO qrCode = null;
        try {
            qrCode = qrCodeAPI.getQrCode(getQrCodeRequest);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("创建二维码发生错误, 二维码类型：%s", getQrCodeRequest.getQrCodeType().name()), ex);
            convertOpenApiBusinessException(ex);
        }
        return qrCode;
    }

    @Override
    public QrCodeDTO refreshQrCode(QrCodeClientReq refreshQrCodeReq) throws BusinessException {
        Assert.notNull(refreshQrCodeReq, "reflashQrCodeReq cannot null");
        LOGGER.info("刷新二维码，用户id:[{}], 用户类型:[{}]", refreshQrCodeReq.getClientId(), refreshQrCodeReq.getQrCodeType().name());

        QrCodeDTO qrCodeDTO = null;
        try {
            qrCodeDTO = qrCodeAPI.refreshQrCode(refreshQrCodeReq);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("刷新二维码发生错误, 二维码类型：%s", refreshQrCodeReq.getQrCodeType().name()), ex);
            convertOpenApiBusinessException(ex);
        }
        return qrCodeDTO;
    }

    @Override
    public QrCodeDTO queryQrCode(QrCodeMobileReq queryQrCodeReq) throws BusinessException {
        Assert.notNull(queryQrCodeReq, "queryQrCodeReq cannot null");

        QrCodeDTO qrCodeDTO = null;
        try {
            qrCodeDTO = qrCodeAPI.queryQrCode(queryQrCodeReq);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("查询二维码发生错误, 二维码类型：%s", queryQrCodeReq.getQrCodeType().name()), ex);
            convertOpenApiBusinessException(ex);
        }
        return qrCodeDTO;
    }

    @Override
    public void scanQrCode(QrCodeMobileReq qrCodeMobileReq) throws BusinessException {
        Assert.notNull(qrCodeMobileReq, "qrCodeMobileReq cannot null");
        LOGGER.info("扫描二维码，二维码:[{}], 用户类型:[{}]", qrCodeMobileReq.getQrCode(), qrCodeMobileReq.getQrCodeType().name());

        try {
            qrCodeAPI.scanQrCode(qrCodeMobileReq);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("扫描二维码发生错误, 二维码类型：%s", qrCodeMobileReq.getQrCodeType().name()), ex);
            convertOpenApiBusinessException(ex);
        }
    }

    @Override
    public void confirmQrLogin(QrLoginReq qrLoginReq) throws BusinessException {
        Assert.notNull(qrLoginReq, "qrCodeMobileReq cannot null");
        LOGGER.info("二维码确认登录，二维码:[{}], 用户类型:[{}], 用户名：[{}]", qrLoginReq.getQrCode(),
                qrLoginReq.getQrCodeType().name(), qrLoginReq.getUserName());

        try {
            qrCodeAPI.confirmQrLogin(qrLoginReq);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("二维码确认登录发生错误, 二维码类型：%s", qrLoginReq.getQrCodeType().name()), ex);
            convertOpenApiBusinessException(ex);
        }
    }

    @Override
    public void cancelQrLogin(QrCodeMobileReq qrCodeMobileReq) throws BusinessException {
        Assert.notNull(qrCodeMobileReq, "qrCodeMobileReq cannot null");
        LOGGER.info("二维码取消登录，二维码:[{}], 用户类型:[{}]", qrCodeMobileReq.getQrCode(), qrCodeMobileReq.getQrCodeType().name());

        try {
            qrCodeAPI.cancelQrLogin(qrCodeMobileReq);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("二维码取消登录发生错误, 二维码类型：%s", qrCodeMobileReq.getQrCodeType().name()), ex);
            convertOpenApiBusinessException(ex);
        }
    }

    @Override
    public QrCodeDTO qrLogin(QrCodeClientReq qrCodeClientReq) throws BusinessException {
        Assert.notNull(qrCodeClientReq, "qrCodeClientReq cannot null");
        LOGGER.info("二维码登录，二维码:[{}], 用户类型:[{}]", qrCodeClientReq.getQrCode(), qrCodeClientReq.getQrCodeType().name());

        QrCodeDTO qrCodeDTO = null;
        try {
            qrCodeDTO = qrCodeAPI.qrLogin(qrCodeClientReq);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("二维码授权登录发生错误, 二维码类型：%s", qrCodeClientReq.getQrCodeType().name()), ex);
            convertOpenApiBusinessException(ex);
        }
        return qrCodeDTO;
    }

    @Override
    public QrCodeConfigDTO getQrCodeConfig(CbbQrCodeType qrCodeType) throws BusinessException {
        Assert.notNull(qrCodeType, "qrCodeType cannot null");

        QrCodeConfigDTO qrCodeConfigDTO = null;
        try {
            qrCodeConfigDTO = qrCodeAPI.getQrCodeConfig(qrCodeType);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("获取二维码配置发生错误, 二维码类型：%s", qrCodeType.name()), ex);
            convertOpenApiBusinessException(ex);
        }
        return qrCodeConfigDTO;
    }

    @Override
    public void updateQrCodeConfig(QrCodeConfigReq qrCodeConfigReq) throws BusinessException {
        Assert.notNull(qrCodeConfigReq, "qrCodeConfigReq cannot null");

        try {
            qrCodeAPI.updateQrCodeConfig(qrCodeConfigReq);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("修改二维码配置发生异常, 二维码类型：%s", qrCodeConfigReq.getQrCodeType().name()), ex);
            convertOpenApiBusinessException(ex);
        }

    }

    private void convertOpenApiBusinessException(BusinessException ex) throws BusinessException {
        if (QrCodeErrorKeyEnum.RCDC_USER_UN_SUPPORT_QR_CODE_TYPE.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.OPEN_API_RCDC_USER_UN_SUPPORT_QR_CODE_TYPE, ex, ex.getArgArr());
        } else if (QrCodeErrorKeyEnum.RCDC_USER_QR_CODE_TYPE_SWITCH_IS_CLOSE.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.OPEN_API_RCDC_USER_QR_CODE_TYPE_SWITCH_IS_CLOSE, ex, ex.getArgArr());
        } else if (QrCodeErrorKeyEnum.RCDC_USER_CLIENT_QR_CODE_NOT_MATCH.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.OPEN_API_RCDC_USER_CLIENT_QR_CODE_NOT_MATCH, ex);
        } else if (QrCodeErrorKeyEnum.RCDC_USER_QR_CODE_STATUS_NOT_EXPECT.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.OPEN_API_RCDC_USER_QR_CODE_STATUS_NOT_EXPECT, ex, ex.getArgArr());
        } else if (QrCodeErrorKeyEnum.RCDC_USER_QR_CODE_NOT_EXIST_OR_EXPIRE.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.OPEN_API_RCDC_USER_QR_CODE_NOT_EXIST_OR_EXPIRE, ex);
        } else if (QrCodeErrorKeyEnum.RCDC_USER_QR_CODE_USER_NOT_EXISTS.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.OPEN_API_USER_NOT_EXISTS, ex);
        } else if (QrCodeErrorKeyEnum.RCDC_USER_QR_CODE_USER_LOCKED.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.OPEN_API_USER_LOGIN_USER_LOCKED, ex);
        } else if (QrCodeErrorKeyEnum.RCDC_USER_QR_RCDC_USER_DISABLE.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.OPEN_API_USER_LOGIN_AD_ACCOUNT_DISABLE, ex);
        } else {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, ex);
        }

    }
}
