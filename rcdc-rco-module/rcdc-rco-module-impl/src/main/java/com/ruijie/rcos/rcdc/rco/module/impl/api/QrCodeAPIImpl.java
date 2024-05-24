package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeConfigDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQrCodeConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacQrCodeType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.QrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr.QrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr.QrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbConfirmQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbGetQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQueryQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.GetQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeClientReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeConfigReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.qr.QrLoginReq;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service.QrCodeService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 二维码登录相关接口实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-21 15:16:00
 *
 * @author zjy
 */
public class QrCodeAPIImpl implements QrCodeAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeAPIImpl.class);

    @Autowired
    private UwsQrCodeAPI cbbQrCodeAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private QrCodeService qrCodeServiceImpl;

    private static final String QR_CODE_BUSINESS = "qr_code_business";

    @Override
    public QrCodeDTO getQrCode(GetQrCodeReq getQrCodeRequest) throws BusinessException {
        Assert.notNull(getQrCodeRequest, "getQrCodeRequest cannot null");

        CbbGetQrCodeReqDTO cbbGetQrCodeReqDTO = new CbbGetQrCodeReqDTO();
        cbbGetQrCodeReqDTO.setClientId(getQrCodeRequest.getClientId());
        cbbGetQrCodeReqDTO.setQrCodeType(getQrCodeRequest.getQrCodeType());
        CbbQrCodeDTO cbbQrCodeDTO = cbbQrCodeAPI.getQrCode(cbbGetQrCodeReqDTO);
        QrCodeDTO qrCodeDTO = new QrCodeDTO();
        BeanUtils.copyProperties(cbbQrCodeDTO, qrCodeDTO);
        return qrCodeDTO;
    }

    @Override
    public QrCodeDTO refreshQrCode(QrCodeClientReq refreshQrCodeReq) throws BusinessException {
        Assert.notNull(refreshQrCodeReq, "refreshQrCodeReq cannot null");

        CbbQrCodeReqDTO cbbQrCodeReqDTO = new CbbQrCodeReqDTO();
        cbbQrCodeReqDTO.setQrCodeType(refreshQrCodeReq.getQrCodeType());
        cbbQrCodeReqDTO.setClientId(refreshQrCodeReq.getClientId());
        cbbQrCodeReqDTO.setQrCode(refreshQrCodeReq.getQrCode());
        CbbQrCodeDTO cbbQrCodeDTO = cbbQrCodeAPI.refreshQrCode(cbbQrCodeReqDTO);
        QrCodeDTO qrCodeDTO = new QrCodeDTO();
        BeanUtils.copyProperties(cbbQrCodeDTO, qrCodeDTO);
        return qrCodeDTO;
    }

    @Override
    public QrCodeDTO queryQrCode(QrCodeMobileReq queryQrCodeReq) throws BusinessException {
        Assert.notNull(queryQrCodeReq, "queryQrCodeReq cannot null");

        CbbQueryQrCodeReqDTO queryQrCodeReqDTO = new CbbQueryQrCodeReqDTO();
        queryQrCodeReqDTO.setQrCodeType(queryQrCodeReq.getQrCodeType());
        queryQrCodeReqDTO.setQrCode(queryQrCodeReq.getQrCode());
        CbbQrCodeDTO cbbQrCodeDTO = cbbQrCodeAPI.queryQrCode(queryQrCodeReqDTO);
        QrCodeDTO qrCodeDTO = new QrCodeDTO();
        BeanUtils.copyProperties(cbbQrCodeDTO, qrCodeDTO);
        return qrCodeDTO;
    }

    @Override
    public void scanQrCode(QrCodeMobileReq qrCodeMobileReq) throws BusinessException {
        Assert.notNull(qrCodeMobileReq, "qrCodeMobileReq cannot null");

        CbbQrCodeMobileReqDTO cbbQrCodeMobileReqDTO = new CbbQrCodeMobileReqDTO();
        cbbQrCodeMobileReqDTO.setQrCodeType(qrCodeMobileReq.getQrCodeType());
        cbbQrCodeMobileReqDTO.setQrCode(qrCodeMobileReq.getQrCode());
        cbbQrCodeAPI.scanQrCode(cbbQrCodeMobileReqDTO);
    }

    @Override
    public void confirmQrLogin(QrLoginReq qrLoginReq) throws BusinessException {
        Assert.notNull(qrLoginReq, "qrCodeMobileReq cannot null");

        // 用户锁定状态查询
        RcoViewUserEntity viewUserEntity = userService.getUserInfoByName(qrLoginReq.getUserName());
        if (viewUserEntity == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_NOT_EXISTS);
        }
        if (certificationHelper.isLocked(viewUserEntity.getUserName())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_LOCKED);
        }
        if (viewUserEntity.getState() == IacUserStateEnum.DISABLE) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_USER_DISABLED);
        }

        // 修改状态并绑定用户名
        CbbConfirmQrCodeMobileReqDTO cbbConfirmQrCodeMobileReqDTO = new CbbConfirmQrCodeMobileReqDTO();
        cbbConfirmQrCodeMobileReqDTO.setQrCodeType(qrLoginReq.getQrCodeType());
        cbbConfirmQrCodeMobileReqDTO.setQrCode(qrLoginReq.getQrCode());
        JSONObject userData = new JSONObject();
        userData.put("uuid", viewUserEntity.getId());
        userData.put("userName", viewUserEntity.getUserName());
        userData.put("displayName", viewUserEntity.getRealName());
        userData.put("password", viewUserEntity.getPassword());
        cbbConfirmQrCodeMobileReqDTO.setUserData(userData.toJSONString());
        cbbQrCodeAPI.confirmQrLogin(cbbConfirmQrCodeMobileReqDTO);
    }

    @Override
    public void cancelQrLogin(QrCodeMobileReq qrCodeMobileReq) throws BusinessException {
        Assert.notNull(qrCodeMobileReq, "qrCodeMobileReq cannot null");

        CbbQrCodeMobileReqDTO cbbQrCodeMobileReqDTO = new CbbQrCodeMobileReqDTO();
        cbbQrCodeMobileReqDTO.setQrCodeType(qrCodeMobileReq.getQrCodeType());
        cbbQrCodeMobileReqDTO.setQrCode(qrCodeMobileReq.getQrCode());
        cbbQrCodeAPI.cancelQrLogin(cbbQrCodeMobileReqDTO);
    }

    @Override
    public QrCodeDTO qrLogin(QrCodeClientReq qrCodeClientReq) throws BusinessException {
        Assert.notNull(qrCodeClientReq, "qrCodeClientReq cannot null");

        CbbQrCodeReqDTO cbbQrCodeReqDTO = new CbbQrCodeReqDTO();
        cbbQrCodeReqDTO.setQrCodeType(qrCodeClientReq.getQrCodeType());
        cbbQrCodeReqDTO.setClientId(qrCodeClientReq.getClientId());
        cbbQrCodeReqDTO.setQrCode(qrCodeClientReq.getQrCode());
        CbbQrCodeDTO cbbQrCodeDTO = cbbQrCodeAPI.qrLogin(cbbQrCodeReqDTO);
        QrCodeDTO qrCodeDTO = new QrCodeDTO();
        BeanUtils.copyProperties(cbbQrCodeDTO, qrCodeDTO);
        return qrCodeDTO;
    }

    @Override
    public QrCodeConfigDTO getQrCodeConfig(CbbQrCodeType qrCodeType) throws BusinessException {
        Assert.notNull(qrCodeType, "qrCodeType cannot null");

        CbbQrCodeConfigDTO qrCodeConfig = cbbQrCodeAPI.getQrCodeConfig(qrCodeType);
        QrCodeConfigDTO codeConfigDTO = new QrCodeConfigDTO();
        BeanUtils.copyProperties(qrCodeConfig, codeConfigDTO);
        return codeConfigDTO;
    }

    @Override
    public void updateQrCodeConfig(QrCodeConfigReq qrCodeConfigReq) throws BusinessException {
        Assert.notNull(qrCodeConfigReq, "qrCodeConfigReq cannot null");

        CbbQrCodeConfigDTO cbbQrCodeConfigDTO = new CbbQrCodeConfigDTO();
        cbbQrCodeConfigDTO.setQrCodeType(qrCodeConfigReq.getQrCodeType());
        cbbQrCodeConfigDTO.setOpenSwitch(qrCodeConfigReq.getSwitchStatus());
        cbbQrCodeConfigDTO.setExpireTime(qrCodeConfigReq.getExpireTime());
        cbbQrCodeConfigDTO.setAdvanceConfig(qrCodeConfigReq.getAdvanceConfig());
        cbbQrCodeConfigDTO.setContentPrefix(qrCodeConfigReq.getContentPrefix());
        cbbQrCodeAPI.saveQrCodeConfig(cbbQrCodeConfigDTO);
        if (CbbQrCodeType.UWS.equals(qrCodeConfigReq.getQrCodeType())) {
            LOGGER.info("修改的配置为UWS类型，发送通知给shine");
            ThreadExecutors.execute(QR_CODE_BUSINESS, () -> {
                try {
                    qrCodeServiceImpl.notifyConfigUpdateToTerminal(cbbQrCodeAPI.getQrCodeConfig(CbbQrCodeType.UWS));
                } catch (Exception ex) {
                    LOGGER.error("扫码登录配置发送通知给shine发生异常，ex:", ex);
                }
            });
        }
    }
}
