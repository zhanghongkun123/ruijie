package com.ruijie.rcos.rcdc.rco.module.impl.uws.service.impl;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbConfirmQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbGetQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQueryQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeLoginStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.impl.uws.service.UwsQrCodeService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月16日
 *
 * @author xgx
 */
@Service
public class UwsQrCodeServiceImpl implements UwsQrCodeService, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(UwsQrCodeServiceImpl.class);

    /**
     * 缓存大小，默认支持存放10w个二维码数据
     */
    private static final long DEFAULT_CACHE_SIZE = 100000;

    /**
     * 默认缓存超时时间，默认15分钟
     */
    private static final Long DEFAULT_EXPIRE_MILLIS = TimeUnit.MINUTES.toMillis(15);

    private static final String UWS_QR_CODE_LOGIN_CONFIG = "uws_qr_code_login_config";

    private Cache<String, CbbQrCodeDTO> cache;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;


    @Override
    public boolean isSupport(CbbQrCodeType qrCodeType) {
        Assert.notNull(qrCodeType, "qrCodeType can not be null");
        return qrCodeType == CbbQrCodeType.UWS;
    }

    @Override
    public CbbQrCodeDTO getQrCode(CbbGetQrCodeReqDTO qrCodeReqDTO) throws BusinessException {
        Assert.notNull(qrCodeReqDTO, "qrCodeReqDTO can not be null");

        validateSwitchIsOpen();

        final CbbQrCodeDTO qrCodeDTO = generateQrCode(qrCodeReqDTO.getClientId());
        cache.put(qrCodeDTO.getQrCode(), qrCodeDTO);

        return cloneCbbQrCodeDTO(qrCodeDTO);
    }


    @Override
    public CbbQrCodeDTO refreshQrCode(CbbQrCodeReqDTO qrCodeReqDTO) throws BusinessException {
        Assert.notNull(qrCodeReqDTO, "qrCodeReqDTO can not be null");

        validateSwitchIsOpen();

        final CbbQrCodeDTO qrCodeDTO = cache.getIfPresent(qrCodeReqDTO.getQrCode());
        if (null != qrCodeDTO) {
            validateClientIdIsLegal(qrCodeReqDTO.getClientId(), qrCodeDTO.getClientId());

            LOGGER.info("移除二维码[{}]，客户端[{}]", qrCodeReqDTO.getQrCode(), qrCodeReqDTO.getClientId());
            cache.invalidate(qrCodeReqDTO.getQrCode());
        }

        final CbbQrCodeDTO newQrCodeDTO = generateQrCode(qrCodeReqDTO.getClientId());
        cache.put(newQrCodeDTO.getQrCode(), newQrCodeDTO);
        LOGGER.info("刷新二维码[{}]，客户端[{}]，新二维码[{}]", qrCodeReqDTO.getQrCode(), qrCodeReqDTO.getClientId(), newQrCodeDTO.getQrCode());

        return cloneCbbQrCodeDTO(newQrCodeDTO);
    }

    @Override
    public CbbQrCodeDTO queryQrCode(CbbQueryQrCodeReqDTO queryQrCodeReqDTO) throws BusinessException {
        Assert.notNull(queryQrCodeReqDTO, "queryQrCodeReqDTO can not be null");

        validateSwitchIsOpen();

        final CbbQrCodeDTO qrCodeDTO = Optional.ofNullable(cache.getIfPresent(queryQrCodeReqDTO.getQrCode())) //
                .orElseGet(() -> { //
                    final CbbQrCodeDTO newQrCodeDTO = new CbbQrCodeDTO();
                    newQrCodeDTO.setQrCode(queryQrCodeReqDTO.getQrCode());
                    newQrCodeDTO.setStatus(CbbQrCodeLoginStatus.INVALID);
                    return newQrCodeDTO;
                });

        return cloneCbbQrCodeDTO(qrCodeDTO);
    }

    @Override
    public CbbQrCodeDTO qrLogin(CbbQrCodeReqDTO qrCodeReqDTO) throws BusinessException {
        Assert.notNull(qrCodeReqDTO, "qrCodeReqDTO can not be null");

        validateSwitchIsOpen();

        final CbbQrCodeDTO qrCodeDTO = findQrCodeInfoIfPresent(qrCodeReqDTO.getQrCode());
        validateClientIdIsLegal(qrCodeReqDTO.getClientId(), qrCodeDTO.getClientId());
        validateQrCodeExpectStatus(qrCodeDTO.getStatus(), CbbQrCodeLoginStatus.CONFIRMED);

        qrCodeDTO.setStatus(CbbQrCodeLoginStatus.LOGIN);
        LOGGER.info("客户端[{}][{}]登录成功", qrCodeReqDTO.getClientId(), qrCodeReqDTO.getQrCode());

        return cloneCbbQrCodeDTO(qrCodeDTO);
    }


    @Override
    public void scanQrCode(CbbQrCodeMobileReqDTO qrCodeMobileReq) throws BusinessException {
        Assert.notNull(qrCodeMobileReq, "qrCodeMobileReq can not be null");

        validateSwitchIsOpen();

        final CbbQrCodeDTO qrCodeDTO = findQrCodeInfoIfPresent(qrCodeMobileReq.getQrCode());
        validateQrCodeExpectStatus(qrCodeDTO.getStatus(), CbbQrCodeLoginStatus.NO_SCAN);

        qrCodeDTO.setStatus(CbbQrCodeLoginStatus.SCANNED);
        LOGGER.info("二维码[{}]扫码成功", qrCodeMobileReq.getQrCode());

    }

    @Override
    public void confirmQrLogin(CbbConfirmQrCodeMobileReqDTO confirmQrCodeMobileReqDTO) throws BusinessException {
        Assert.notNull(confirmQrCodeMobileReqDTO, "confirmQrCodeMobileReqDTO can not be null");

        validateSwitchIsOpen();

        final CbbQrCodeDTO qrCodeDTO = findQrCodeInfoIfPresent(confirmQrCodeMobileReqDTO.getQrCode());
        validateQrCodeExpectStatus(qrCodeDTO.getStatus(), CbbQrCodeLoginStatus.SCANNED);

        qrCodeDTO.setStatus(CbbQrCodeLoginStatus.CONFIRMED);
        qrCodeDTO.setUserData(confirmQrCodeMobileReqDTO.getUserData());

        LOGGER.info("二维码[{}]扫码确认成功", confirmQrCodeMobileReqDTO.getQrCode());
    }

    @Override
    public void cancelQrLogin(CbbQrCodeMobileReqDTO qrCodeMobileReq) throws BusinessException {
        Assert.notNull(qrCodeMobileReq, "qrCodeMobileReq can not be null");

        validateSwitchIsOpen();

        final CbbQrCodeDTO qrCodeDTO = findQrCodeInfoIfPresent(qrCodeMobileReq.getQrCode());
        validateQrCodeExpectStatus(qrCodeDTO.getStatus(), CbbQrCodeLoginStatus.SCANNED);

        qrCodeDTO.setStatus(CbbQrCodeLoginStatus.INVALID);

        LOGGER.info("二维码[{}]扫码取消成功", qrCodeMobileReq.getQrCode());
    }

    @Override
    public void saveQrCodeConfig(CbbQrCodeConfigDTO qrCodeConfigReqDTO) throws BusinessException {
        Assert.notNull(qrCodeConfigReqDTO, "qrCodeConfigReqDTO can not be null");

        globalParameterAPI.updateParameter(UWS_QR_CODE_LOGIN_CONFIG, JSON.toJSONString(qrCodeConfigReqDTO));
        LOGGER.info("更新二维码配置成功,二维码类型[{}]，开关[{}]，超时时间[{}]", qrCodeConfigReqDTO.getQrCodeType(), //
                qrCodeConfigReqDTO.getOpenSwitch(), qrCodeConfigReqDTO.getExpireTime());
    }

    @Override
    public CbbQrCodeConfigDTO getQrCodeConfig() throws BusinessException {
        return getConfig();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final CbbQrCodeConfigDTO cbbQrCodeConfigDTO = getConfig();
        long expireTime = Optional.ofNullable(cbbQrCodeConfigDTO.getExpireTime()).orElse(DEFAULT_EXPIRE_MILLIS);
        cache = CacheBuilder.newBuilder() //
                .maximumSize(DEFAULT_CACHE_SIZE) //
                .expireAfterWrite(expireTime, TimeUnit.MILLISECONDS) //
                .build();

    }

    private CbbQrCodeDTO generateQrCode(String clientId) throws BusinessException {
        CbbQrCodeConfigDTO qrCodeConfig = getQrCodeConfig();
        String contentPrefix = StringUtils.defaultString(qrCodeConfig.getContentPrefix(), StringUtils.EMPTY);
        final String qrCode = UUID.randomUUID().toString();
        final CbbQrCodeDTO qrCodeDTO = new CbbQrCodeDTO();
        qrCodeDTO.setClientId(clientId);
        qrCodeDTO.setQrCode(qrCode);
        qrCodeDTO.setStatus(CbbQrCodeLoginStatus.NO_SCAN);
        qrCodeDTO.setExpireTime(System.currentTimeMillis());
        qrCodeDTO.setContent(contentPrefix + qrCode);

        return qrCodeDTO;
    }


    private CbbQrCodeConfigDTO getConfig() {
        final String uwsScanQrCodeConfig = globalParameterAPI.findParameter(UWS_QR_CODE_LOGIN_CONFIG);
        return Optional.ofNullable(JSON.parseObject(uwsScanQrCodeConfig, CbbQrCodeConfigDTO.class)).orElseGet(() -> { //
            CbbQrCodeConfigDTO cbbQrCodeConfigDTO = new CbbQrCodeConfigDTO();
            cbbQrCodeConfigDTO.setOpenSwitch(false);
            return cbbQrCodeConfigDTO;
        });
    }

    private CbbQrCodeDTO findQrCodeInfoIfPresent(String qrCode) throws BusinessException {
        return Optional.ofNullable(cache.getIfPresent(qrCode))
                .orElseThrow(() -> new BusinessException(BusinessKey.RCDC_UWS_QR_CODE_NOT_EXIST_OR_EXPIRE));
    }

    private void validateClientIdIsLegal(String reqClientId, String qrCodeClientId) throws BusinessException {
        if (StringUtils.equals(reqClientId, qrCodeClientId)) {
            return;
        }
        throw new BusinessException(BusinessKey.RCDC_UWS_CLIENT_QR_CODE_NOT_MATCH);
    }

    private void validateQrCodeExpectStatus(CbbQrCodeLoginStatus currentStatus, CbbQrCodeLoginStatus expectStatus) throws BusinessException {
        if (currentStatus != expectStatus) {
            throw new BusinessException(BusinessKey.RCDC_UWS_QR_CODE_STATUS_NOT_EXPECT, currentStatus.toString(), expectStatus.toString());
        }
    }

    private void validateSwitchIsOpen() throws BusinessException {
        if (getConfig().getOpenSwitch() != Boolean.TRUE) {
            throw new BusinessException(BusinessKey.RCDC_UWS_QR_CODE_TYPE_SWITCH_IS_CLOSE, CbbQrCodeType.UWS.toString());
        }
    }


    private CbbQrCodeDTO cloneCbbQrCodeDTO(CbbQrCodeDTO qrCodeDTO) {
        CbbQrCodeDTO returnQrCodeDTO = new CbbQrCodeDTO();
        BeanUtils.copyProperties(qrCodeDTO, returnQrCodeDTO);
        return returnQrCodeDTO;
    }
}
