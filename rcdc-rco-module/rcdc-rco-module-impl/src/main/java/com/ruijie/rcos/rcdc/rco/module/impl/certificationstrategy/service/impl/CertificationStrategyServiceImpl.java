package com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.impl;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacClientAuthSecurityConfigAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSecurityStrategyAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAccountStrategyInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacCheckCommonDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPasswordStrategyInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationStrategyLevelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationStrategyRegexEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.constants.CertificationStrategyConstants;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/09 15:48
 *
 * @author zhang.zhiwen
 */
@Service
public class CertificationStrategyServiceImpl implements CertificationStrategyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationStrategyServiceImpl.class);

    private static final String DEFAULT_LEVEL = "-1";
    
    private static final int DEFAULT_LOCK_TIME = -1;

    @Autowired
    private IacAdminMgmtAPI iacAdminMgmtAPI;

    @Autowired
    private IacSecurityStrategyAPI iacSecurityStrategyAPI;

    @Autowired
    private IacClientAuthSecurityConfigAPI iacClientAuthSecurityConfigAPI;


    @Override
    public PwdStrategyDTO getPwdStrategyParameter() {
        try {
            PwdStrategyDTO pwdStrategyDTO = new PwdStrategyDTO();
            IacAccountStrategyInfoDTO accountStrategyInfo = iacSecurityStrategyAPI.getAccountStrategyInfo(SubSystem.CDC);
            IacPasswordStrategyInfoDTO passwordStrategyInfo = iacSecurityStrategyAPI.getPasswordStrategyInfo(SubSystem.CDC);
            IacClientAuthSecurityDTO clientAuthSecurityDTO = iacClientAuthSecurityConfigAPI.detail();

            pwdStrategyDTO.setChangePassword(clientAuthSecurityDTO.getChangePassword());
            pwdStrategyDTO.setAdminLockedErrorTimes(accountStrategyInfo.getAdminLockedErrorTimes());
            pwdStrategyDTO.setUserLockedErrorTimes(accountStrategyInfo.getAdminLockedErrorTimes());
            pwdStrategyDTO.setPreventsBruteForce(accountStrategyInfo.getPreventsBruteForce());
            Long adminLockTime = accountStrategyInfo.getAdminLockTime();
            setLockTime(pwdStrategyDTO, adminLockTime);
            pwdStrategyDTO.setPwdLevel(passwordStrategyInfo.getPwdLevel());
            pwdStrategyDTO.setPwdExpireRemindDays(passwordStrategyInfo.getPwdExpireRemindDays());
            pwdStrategyDTO.setSecurityStrategyEnable(passwordStrategyInfo.getSecurityStrategyEnable());
            pwdStrategyDTO.setUpdateDays(passwordStrategyInfo.getUpdateDays());
            pwdStrategyDTO.setEnableForceUpdatePassword(passwordStrategyInfo.getEnableForceUpdatePassword());
            pwdStrategyDTO.setTerminalLockedErrorTimes(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_TERMINAL_ERROR_TIMES);
            pwdStrategyDTO.setTerminalLockTime(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_TERMINAL_LOCK_TIME_DEFAULT);
            return pwdStrategyDTO;
        } catch (Exception e) {
            LOGGER.error("获取安全策略失败", e);
            throw new IllegalStateException("获取安全策略失败", e);
        }
    }

    private void setLockTime(PwdStrategyDTO pwdStrategyDTO, Long adminLockTime) {
        if (adminLockTime == null) {
            pwdStrategyDTO.setAdminLockTime(DEFAULT_LOCK_TIME);
            pwdStrategyDTO.setUserLockTime(DEFAULT_LOCK_TIME);
            return;
        }
        int lockTime = adminLockTime.intValue();
        pwdStrategyDTO.setAdminLockTime(lockTime);
        // 发给客户端是分钟，身份中心保存的是秒，需要转为分钟
        Integer userLockTime = lockTime < 0 ? lockTime : lockTime / 60;
        pwdStrategyDTO.setUserLockTime(userLockTime);
    }

    @Override
    public Boolean validatePwdByStrategyLevel(String password) {
        Assert.notNull(password, "password must not be null");
        PwdStrategyDTO pwdStrategyDTO = getPwdStrategyParameter();
        String pwdLevel = pwdStrategyDTO.getSecurityStrategyEnable() ? pwdStrategyDTO.getPwdLevel() : DEFAULT_LEVEL;
        CertificationStrategyLevelEnum strategyLevelEnum = CertificationStrategyLevelEnum.getByLevel(pwdLevel);
        String pattern = "";
        switch (strategyLevelEnum) {
            case LEVEL_ONE:
                pattern = CertificationStrategyRegexEnum.PATTERN_ONE.getPattern();
                break;
            case LEVEL_TWO:
                pattern = CertificationStrategyRegexEnum.PATTERN_TWO.getPattern();
                break;
            case LEVEL_THREE:
                pattern = CertificationStrategyRegexEnum.PATTERN_THREE.getPattern();
                break;
            default:
                pattern = CertificationStrategyRegexEnum.PATTERN_DEFAULT.getPattern();
                break;
        }
        boolean isMatch = Pattern.matches(pattern, password);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("基于配置校验密码复杂度，当前密码级别：{},校验结果：{}", strategyLevelEnum.getLevel(), isMatch);
        }
        return isMatch;
    }

    @Override
    public Boolean shouldUpdateAdminPwdFirstLogin(String newPassword) {
        Assert.notNull(newPassword, "newPassword must not null");

        IacCheckCommonDTO checkResult;
        try {
            checkResult = iacAdminMgmtAPI.validatePwdComplexity(AesUtil.encrypt(newPassword, RedLineUtil.getRealAdminRedLine()), SubSystem.CDC);
            if (checkResult.getHasDuplication()) {
                LOGGER.info("校验密码不通过，异常原因[{}]", checkResult.getErrorMsg());
            }
        } catch (BusinessException e) {
            LOGGER.warn("校验管理员密码出现异常，认为需要修改密码");
            return Boolean.TRUE;
        }
        return checkResult.getHasDuplication();
    }
}
