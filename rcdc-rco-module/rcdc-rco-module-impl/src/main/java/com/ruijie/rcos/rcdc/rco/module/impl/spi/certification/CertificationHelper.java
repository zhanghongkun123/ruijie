package com.ruijie.rcos.rcdc.rco.module.impl.spi.certification;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserLockQueryRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacLockStatus;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacSecurityPolicy;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.common.ThirdPartyLoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.LoginHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 *
 * Description: 用户登录认证SPI相关协助类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
@Service("rcoCertificationHelper")
public class CertificationHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationHelper.class);

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;


    @Autowired
    LoginHelper loginHelper;
    
    @Autowired
    private UserService userService;


    /**
     * 构建错误信息 LoginErrorResponse
     *
     * @param autCode 登录结果码
     * @param userName 用户名称
     * @return LoginErrorResponse
     */
    public LoginResultMessageDTO buildLoginErrorResponse(Integer autCode, String userName) {
        Assert.notNull(autCode, "autCode can not be null");
        Assert.notNull(userName, "buildLoginErrorResponse()的userName can not be null");

        // 密码认证策略的错误
        if (autCode == LoginMessageCode.USER_LOCKED || autCode == LoginMessageCode.REMIND_ERROR_TIMES
            || autCode == ThirdPartyLoginMessageCode.USER_THIRD_PARTY_AUTH_ERROR ||
                autCode == LoginMessageCode.CAPTCHA_ERROE || autCode == LoginMessageCode.INVALID_CAPTCHA) {
            return buildPwdStrategyResult(autCode, userName);
        }
        // 硬件特征码的错误
        if (autCode == LoginMessageCode.HARDWARE_OVER_MAX) {
            return buildHardwareOverMaxResult(userName);
        }
        return new LoginResultMessageDTO();
    }

    /**
     * 构建登录硬件特征码数值超过最大值的错误结果
     *
     * @param userName 用户名称
     * @return LoginResultMessageDTO
     */
    private LoginResultMessageDTO buildHardwareOverMaxResult(String userName) {
        Assert.notNull(userName, "buildHardwareOverMaxResult()的userName can not be null");

        LoginResultMessageDTO errorResponse = new LoginResultMessageDTO();
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(userName);
        if (userDetailDTO == null) {
            LOGGER.info("获取用户RcoViewUserEntity信息为空，用户[{}]", userName);
            return errorResponse;
        }
        IacUserIdentityConfigRequest request = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userDetailDTO.getId());
        try {
            IacUserIdentityConfigResponse response = userIdentityConfigAPI.findUserIdentityConfigByRelated(request);
            errorResponse.setHardwareCodeMaxNum(response.getMaxHardwareNum());
        } catch (BusinessException e) {
            LOGGER.error("获取用户[{}]配置的硬件特征码最大数量异常，异常：{}", userName, e.getI18nMessage());
        }
        return errorResponse;
    }

    /**
     * 构建登录认证策略的结果信息
     *
     * @param autCode 登录结果码
     * @param userName 用户名称
     * @return LoginResultMessageDTO
     */
    public LoginResultMessageDTO buildPwdStrategyResult(Integer autCode, String userName) {
        Assert.notNull(autCode, "autCode can not be null");
        Assert.notNull(userName, "buildPwdStrategyResult()的userName can not be null");

        LoginResultMessageDTO errorResponse = new LoginResultMessageDTO();
        RcoViewUserEntity userEntity = rcoViewUserDAO.findByUserName(userName);

        loginHelper.warpSafetyRedLine(userName, errorResponse);
        return errorResponse;
    }

    /**
     * 判断认证策略是否开启，用户是否已被锁定
     *
     * @param userName userName
     * @return true已锁定；false未锁定
     */
    public boolean isLocked(String userName) {
        Assert.hasText(userName, "userName can not be empty");

        IacLockInfoDTO userAccountLockInfo = getUserAccountLockInfo(userName);
        return userAccountLockInfo.getLockStatus() == IacLockStatus.LOCKED;
    }

    /**
     * 密码错误后修改错误次数，判断是否需要锁定，需要锁定就锁定用户添加解锁时间信息
     *
     * @param userName userName
     * @param pwdStrategyDTO pwdStrategyDTO
     * @param authUserResponse authUserResponse
     */
    public void changeErrorTimesAndLock(String userName, PwdStrategyDTO pwdStrategyDTO, IacAuthUserResultDTO authUserResponse) {
        Assert.notNull(userName, "userName can not be null");
        Assert.notNull(pwdStrategyDTO, "pwdStrategyDTO can not be null");
        Assert.notNull(authUserResponse, "authUserResponse can not be null");

        boolean enablePreventsBruteForce = BooleanUtils.isTrue(pwdStrategyDTO.getPreventsBruteForce());
        if (!enablePreventsBruteForce) {
            return;
        }
        // 提示剩余错误次数
        if (authUserResponse.getAuthCode() != ThirdPartyLoginMessageCode.USER_THIRD_PARTY_AUTH_ERROR) {
            authUserResponse.setAuthCode(LoginMessageCode.REMIND_ERROR_TIMES);
        }
        IacLockInfoDTO lockInfo = getUserAccountLockInfo(userName);

        // 未被锁定
        if (lockInfo.getLockStatus() == IacLockStatus.UN_LOCKED) {
            return;
        }
        authUserResponse.setAuthCode(LoginMessageCode.USER_LOCKED);
    }

    /**
     * 获取用户账号锁信息
     * 
     * @param userName 用户名
     * @return IacLockInfoDTO
     */
    public IacLockInfoDTO getUserAccountLockInfo(String userName) {
        Assert.hasText(userName, "userName cannot be empty");
        IacUserLockQueryRequest iacUserLockQueryRequest = new IacUserLockQueryRequest();
        iacUserLockQueryRequest.setUserName(userName);
        iacUserLockQueryRequest.setSecurityPolicy(IacSecurityPolicy.ACCOUNT);
        iacUserLockQueryRequest.setSubSystem(SubSystem.CDC);
        try {
            return iacUserMgmtAPI.getLockInfo(iacUserLockQueryRequest);
        } catch (Exception e) {
            throw new IllegalStateException("getUserAccountLockInfo error", e);
        }
    }
}
