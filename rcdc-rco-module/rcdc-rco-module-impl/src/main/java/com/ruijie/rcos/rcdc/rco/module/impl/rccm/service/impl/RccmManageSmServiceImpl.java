package com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.impl;


import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSecurityStrategyAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacMfaConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageSmService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacHardwareCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.IacThirdPartyCertificationConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacThirdPartyCertificationConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacHardwareCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsPwdRecoverDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.constnts.SmsAndScanCodeCheckConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoGlobalParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.constants.HardwareConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.constant.OtpConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.RccmManageConstant;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CertifiedSecurityConfigService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: RCCM管理事务处理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/12
 *
 * @author lihengjing
 */
@Service
public class RccmManageSmServiceImpl implements RccmManageSmService {


    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(RccmManageSmServiceImpl.class);

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
    private static final String THREAD_POOL_NAME = "notify-shine-update-otp";

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    @Autowired
    private RcoGlobalParameterDAO rcoGlobalParameterDAO;

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private IacHardwareCertificationAPI hardwareCertificationAPI;
    
    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;


    @Autowired
    private CertifiedSecurityConfigService certifiedSecurityConfigService;

    @Autowired
    private IacSecurityStrategyAPI iacSecurityPolicyAPI;

    @Override
    public void updateUnifiedLogin(Boolean hasUnifiedLogin, Boolean enableAssistAuth) throws BusinessException {
        Assert.notNull(hasUnifiedLogin, "hasUnifiedLogin must be not null");
        Assert.notNull(enableAssistAuth, "enableAssistAuth must be not null");

        // 记录不一致时，才进行业务处理 避免重放
        if (Boolean.TRUE.equals(hasUnifiedLogin) && Boolean.FALSE.equals(enableAssistAuth)) {
            // 开启统一登录
            backupConfigValue(HardwareConstants.CERTIFICATION_HARDWARE_SUMMARY);
            backupConfigValue(OtpConstants.CERTIFICATION_OTP_SUMMARY);
            backupConfigValue(SmsAndScanCodeCheckConstants.SMS_AUTH_CONFIG);
            backupConfigValue(SmsAndScanCodeCheckConstants.SMS_RECOVER_PWD_CONFIG);
            backupConfigValue(Constants.THIRD_PARTY_AUTH_CODE_CONFIG);
            backupConfigValue(Constants.NOTIFY_LOGIN_TERMINAL_CHANGE);
        } else {
            resetConfigValue(HardwareConstants.CERTIFICATION_HARDWARE_SUMMARY);
            resetConfigValue(OtpConstants.CERTIFICATION_OTP_SUMMARY);
            resetConfigValue(SmsAndScanCodeCheckConstants.SMS_AUTH_CONFIG);
            resetConfigValue(SmsAndScanCodeCheckConstants.SMS_RECOVER_PWD_CONFIG);
            resetConfigValue(Constants.THIRD_PARTY_AUTH_CODE_CONFIG);
            // 如果统一登陆开启 辅助认证关闭，设置为备份
            if (Boolean.TRUE.equals(hasUnifiedLogin)) {
                backupConfigValue(Constants.NOTIFY_LOGIN_TERMINAL_CHANGE);
            } else {
                // 否则 统一登陆关闭 进行 还原
                resetConfigValue(Constants.NOTIFY_LOGIN_TERMINAL_CHANGE);
            }
        }
    }

    private void resetConfigValue(String key) throws BusinessException {
        String backupKey = key + RccmManageConstant.BACKUP_CONFIG_KEY_SUFFIX;
        RcoGlobalParameterEntity backupEntity = rcoGlobalParameterDAO.findByParamKey(backupKey);
        if (backupEntity != null && backupEntity.getParamValue() != null) {
            // 恢复 备份的数据
            switch (key) {
                case OtpConstants.CERTIFICATION_OTP_SUMMARY:
                    OtpCertificationDTO bakOtpCertificationDTO = JSON.parseObject(backupEntity.getParamValue(), OtpCertificationDTO.class);
                    IacMfaConfigDTO iacMfaConfigDTO = iacSecurityPolicyAPI.getMfaConfig(SubSystem.CDC);
                    OtpCertificationDTO otpCertificationDTO = new OtpCertificationDTO(iacMfaConfigDTO);
                    if (bakOtpCertificationDTO.getOpenOtp() != otpCertificationDTO.getOpenOtp()) {
                        otpCertificationDTO.setOpenOtp(bakOtpCertificationDTO.getOpenOtp());
                        THREAD_EXECUTOR.execute(() -> {
                            // 动态口令需要进行shine 在线推送
                            otpCertificationAPI.updateOtpCertification(otpCertificationDTO);
                        });
                    }
                    break;
                case HardwareConstants.CERTIFICATION_HARDWARE_SUMMARY:
                    IacHardwareCertificationDTO bakHardwareCertificationDTO =
                            JSON.parseObject(backupEntity.getParamValue(), IacHardwareCertificationDTO.class);
                    IacHardwareCertificationDTO hardwareCertificationDTO = hardwareCertificationAPI.getHardwareCertification();
                    if (bakHardwareCertificationDTO.getOpenHardware() != hardwareCertificationDTO.getOpenHardware()) {
                        hardwareCertificationDTO.setOpenHardware(bakHardwareCertificationDTO.getOpenHardware());
                        hardwareCertificationAPI.updateHardwareCertification(hardwareCertificationDTO);
                    }
                    break;
                case SmsAndScanCodeCheckConstants.SMS_AUTH_CONFIG:
                    IacSmsCertificationDTO smsCertificationDTO = smsCertificationAPI.getSmsCertificationStrategy();
                    IacSmsCertificationDTO bakSmsCertificationDTO = JSON.parseObject(backupEntity.getParamValue(), IacSmsCertificationDTO.class);
                    if (bakSmsCertificationDTO.getEnable() != smsCertificationDTO.getEnable()) {
                        smsCertificationDTO.setEnable(bakSmsCertificationDTO.getEnable());
                        smsCertificationAPI.editSmsCertificationStrategy(smsCertificationDTO);
                    }
                    break;
                case SmsAndScanCodeCheckConstants.SMS_RECOVER_PWD_CONFIG:
                    IacSmsPwdRecoverDTO smsPwdRecoverDTO = smsCertificationAPI.getSmsPwdRecoverStrategy();
                    IacSmsPwdRecoverDTO bakSmsPwdRecoverDTO = JSON.parseObject(backupEntity.getParamValue(), IacSmsPwdRecoverDTO.class);
                    if (bakSmsPwdRecoverDTO.getEnable() != smsPwdRecoverDTO.getEnable()) {
                        smsPwdRecoverDTO.setEnable(bakSmsPwdRecoverDTO.getEnable());
                        // 密码找回需要进行shine 在线推送
                        THREAD_EXECUTOR.execute(() -> {
                            try {
                                smsCertificationAPI.editSmsPwdRecoverStrategy(smsPwdRecoverDTO);
                            } catch (Exception e) {
                                LOGGER.error("editSmsPwdRecoverStrategy fail", e);
                            }
                        });
                    }
                    break;
                case Constants.THIRD_PARTY_AUTH_CODE_CONFIG:
                    IacThirdPartyCertificationConfigResponse thirdPartyCertificationConfig =
                            thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC);
                    IacThirdPartyCertificationConfigResponse bakThirdPartyCertificationConfig =
                            JSON.parseObject(backupEntity.getParamValue(), IacThirdPartyCertificationConfigResponse.class);
                    if (bakThirdPartyCertificationConfig.getEnable() != thirdPartyCertificationConfig.getEnable()) {
                        thirdPartyCertificationConfig.setEnable(bakThirdPartyCertificationConfig.getEnable());
                        IacThirdPartyCertificationConfigRequest configRequest = new IacThirdPartyCertificationConfigRequest();
                        BeanUtils.copyProperties(thirdPartyCertificationConfig, configRequest);
                        configRequest.setServerList(thirdPartyCertificationConfig.getServerList());
                        thirdPartyCertificationAPI.updateThirdPartyCertificationConfig(configRequest, SubSystem.CDC);
                    }
                    break;

                case Constants.NOTIFY_LOGIN_TERMINAL_CHANGE:
                    // 进行恢复
                    certifiedSecurityConfigService.updateEnableNotifyLoginTerminalChange(backupEntity.getParamValue());
                    break;
            }
            rcoGlobalParameterDAO.delete(backupEntity);
        }
    }

    private void backupConfigValue(String key) throws BusinessException {
        String backupKey = key + RccmManageConstant.BACKUP_CONFIG_KEY_SUFFIX;
        // 恢复默认值 默认是关闭状态的
        switch (key) {
            case OtpConstants.CERTIFICATION_OTP_SUMMARY:
                IacMfaConfigDTO iacMfaConfigDTO = iacSecurityPolicyAPI.getMfaConfig(SubSystem.CDC);
                if (iacMfaConfigDTO != null && BooleanUtils.isNotFalse(iacMfaConfigDTO.getEnableMfa())) {
                    OtpCertificationDTO otpCertificationDTO = new OtpCertificationDTO(iacMfaConfigDTO);
                    backupConfigValue(backupKey, JSONObject.toJSONString(otpCertificationDTO));
                    // 动态口令需要进行shine 在线推送
                    otpCertificationDTO.setOpenOtp(false);
                    THREAD_EXECUTOR.execute(() -> {
                        // 动态口令需要进行shine 在线推送
                        otpCertificationAPI.updateOtpCertification(otpCertificationDTO);
                    });
                }
                break;
            case HardwareConstants.CERTIFICATION_HARDWARE_SUMMARY:
                IacHardwareCertificationDTO hardwareCertificationDTO = hardwareCertificationAPI.getHardwareCertification();
                if (hardwareCertificationDTO != null && BooleanUtils.isNotFalse(hardwareCertificationDTO.getOpenHardware())) {
                    backupConfigValue(backupKey, JSONObject.toJSONString(hardwareCertificationDTO));
                    // 动态口令需要进行shine 在线推送
                    hardwareCertificationDTO.setOpenHardware(false);
                    hardwareCertificationAPI.updateHardwareCertification(hardwareCertificationDTO);
                }
                break;
            case SmsAndScanCodeCheckConstants.SMS_AUTH_CONFIG:
                IacSmsCertificationDTO smsCertificationDTO = smsCertificationAPI.getSmsCertificationStrategy();
                if (smsCertificationDTO != null && BooleanUtils.isNotFalse(smsCertificationDTO.getEnable())) {
                    backupConfigValue(backupKey, JSONObject.toJSONString(smsCertificationDTO));
                    smsCertificationDTO.setEnable(false);
                    smsCertificationAPI.editSmsCertificationStrategy(smsCertificationDTO);
                }
                break;
            case SmsAndScanCodeCheckConstants.SMS_RECOVER_PWD_CONFIG:
                IacSmsPwdRecoverDTO smsPwdRecoverDTO = smsCertificationAPI.getSmsPwdRecoverStrategy();
                if (smsPwdRecoverDTO != null && BooleanUtils.isNotFalse(smsPwdRecoverDTO.getEnable())) {
                    backupConfigValue(backupKey, JSONObject.toJSONString(smsPwdRecoverDTO));
                    smsPwdRecoverDTO.setEnable(false);
                    // 密码找回需要进行shine 在线推送
                    THREAD_EXECUTOR.execute(() -> {
                        try {
                            smsCertificationAPI.editSmsPwdRecoverStrategy(smsPwdRecoverDTO);
                        } catch (BusinessException e) {
                            LOGGER.error("editSmsPwdRecoverStrategy fail", e);
                        }
                    });
                }
                break;
            case Constants.THIRD_PARTY_AUTH_CODE_CONFIG:
                IacThirdPartyCertificationConfigResponse thirdPartyCertificationConfig =
                        thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC);
                if (thirdPartyCertificationConfig != null && BooleanUtils.isNotFalse(thirdPartyCertificationConfig.getEnable())) {
                    backupConfigValue(backupKey, JSONObject.toJSONString(thirdPartyCertificationConfig));
                    thirdPartyCertificationConfig.setEnable(false);
                    IacThirdPartyCertificationConfigRequest configRequest = new IacThirdPartyCertificationConfigRequest();
                    BeanUtils.copyProperties(thirdPartyCertificationConfig, configRequest);
                    configRequest.setServerList(thirdPartyCertificationConfig.getServerList());
                    thirdPartyCertificationAPI.updateThirdPartyCertificationConfig(configRequest, SubSystem.CDC);
                }
                break;
            case Constants.NOTIFY_LOGIN_TERMINAL_CHANGE:
                // 设置为关闭
                certifiedSecurityConfigService.updateEnableNotifyLoginTerminalChange(String.valueOf(Boolean.FALSE));
                break;
        }
    }

    private void backupConfigValue(String backupKey, String backupParamValue) {
        RcoGlobalParameterEntity existBackupEntity = rcoGlobalParameterDAO.findByParamKey(backupKey);
        if (existBackupEntity != null) {
            // 存在备份数据 则更新
            rcoGlobalParameterDAO.updateValueByParamKey(backupKey, backupParamValue);
        } else {
            // 不存在备份数据 则新增备份数据
            RcoGlobalParameterEntity backupEntity = new RcoGlobalParameterEntity();
            backupEntity.setParamKey(backupKey);
            backupEntity.setParamValue(backupParamValue);
            backupEntity.setCreateTime(new Date());
            backupEntity.setUpdateTime(new Date());
            rcoGlobalParameterDAO.save(backupEntity);
        }
    }

}
