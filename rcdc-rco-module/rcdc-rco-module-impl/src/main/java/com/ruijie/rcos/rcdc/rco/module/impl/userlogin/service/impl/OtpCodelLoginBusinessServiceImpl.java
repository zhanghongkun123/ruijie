package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.UserOtpCertificationConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.OtpCertificationCheckResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.service.UserOtpCertificationService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.LoginHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.LoginPostAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 动态验证码用户登录
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/09
 *
 * @author linke
 */
@Service("otpCodeLoginTemplateService")
public class OtpCodelLoginBusinessServiceImpl extends AbstractLoginBusinessServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtpCodelLoginBusinessServiceImpl.class);

    @Autowired
    protected UserService userService;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserOtpCertificationService otpCertificationService;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Override
    public String getKey() {
        return ShineAction.USER_OTP_CODE_LOGIN;
    }

    @Override
    public ShineLoginDTO getShineLoginDTO(String data) throws Exception {
        Assert.hasText(data, "data不能为null");

        return shineMessageHandler.parseObject(data, ShineLoginDTO.class);
    }

    @Override
    public IacAuthUserResultDTO checkUserNameAndOtpCode(String terminalId, String userName, String otpCode) {
        Assert.hasText(terminalId, "terminalId不能为null");
        Assert.hasText(userName, "userName不能为null");
        Assert.hasText(otpCode, "otpCode不能为null");

        try {
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(userName);
            // 访客和非访客用户区别处理
            IacAuthUserResultDTO authUserResponse = new IacAuthUserResultDTO();
            int resultCode = processLogin(terminalId, userDetailDTO);
            if (LoginMessageCode.SUCCESS != resultCode) {
                authUserResponse = new IacAuthUserResultDTO(resultCode);
                authUserResponse.setUserName(userName);
                return authUserResponse;
            }
            // 动态口令校验
            resultCode = validateOtpCode(userDetailDTO, otpCode);
            authUserResponse.setAuthCode(resultCode);
            authUserResponse.setUserName(userName);

            LoginPostAuthDTO loginPostAuth = new LoginPostAuthDTO();
            loginPostAuth.setTerminalId(terminalId);
            loginPostAuth.setUserName(userName);
            loginPostAuth.setHasResetErrorTimes(false);
            loginPostAuth.setNeedAssistAuth(true);
            loginPostAuth.setUserDetailDTO(userDetailDTO);
            postAuth(loginPostAuth, authUserResponse);

            LOGGER.info("用户[{}]登录认证完成，返回认证码：[{}]", userName, authUserResponse.getAuthCode());
            return authUserResponse;
        } catch (Exception e) {
            LOGGER.error("用户[" + userName + "]登录校验失败", e);
            return new IacAuthUserResultDTO(LoginMessageCode.CODE_ERR_OTHER);
        }

    }

    @Override
    public int processLogin(String terminalId, @Nullable IacUserDetailDTO userDetailDTO) throws BusinessException {
        Assert.hasText(terminalId, "terminalId不能为null");
        if (userDetailDTO == null) {
            LOGGER.info("数据库中不存在用户");
            return LoginMessageCode.USERNAME_OR_OTP_ERROR;
        }
        String userName = userDetailDTO.getUserName();
        //同步用户域信息
        if (userDetailDTO.getUserType() == IacUserTypeEnum.AD) {
            cbbUserAPI.syncAdUser(userDetailDTO.getId());
        }
        Integer authCode = checkAdUser(userDetailDTO);
        if (authCode != LoginMessageCode.SUCCESS) {
            return authCode;
        }
        // 检查是否为访客用户，如果是则不允许登录
        if (IacUserTypeEnum.VISITOR == userDetailDTO.getUserType()) {
            LOGGER.info("不允许在终端[{}]登录框中用访客用户[{}]登录", terminalId, userName);
            return LoginMessageCode.VISITOR_LOGIN;
        }

        // 判断用户是否被锁定
        if (certificationHelper.isLocked(userName)) {
            LOGGER.info("用户[{}]被锁定", userName);
            return LoginMessageCode.USER_LOCKED;
        }
        if (userDetailDTO.getUserState() == IacUserStateEnum.DISABLE) {
            LOGGER.info("用户[{}]已禁用，不可登录", userName);
            return LoginMessageCode.AD_ACCOUNT_DISABLE;
        }

        return LoginMessageCode.SUCCESS;
    }

    private Integer checkAdUser(IacUserDetailDTO userDetailDTO) {
        if ((IacUserTypeEnum.AD == userDetailDTO.getUserType() || IacUserTypeEnum.LDAP == userDetailDTO.getUserType())) {
            //域账户禁用且开启域同步的话，会去域服务器获取是否禁用，否则直接返回禁用
            if (IacUserStateEnum.DISABLE == userDetailDTO.getUserState() && Boolean.FALSE.equals(userDetailDTO.getEnableDomainSync())) {
                LOGGER.info("用户[{}]已被禁用", userDetailDTO.getUserName());
                return LoginMessageCode.AD_ACCOUNT_DISABLE;
            }
            if (IacUserTypeEnum.AD != userDetailDTO.getUserType()) {
                return LoginMessageCode.SUCCESS;
            }
            IacDomainUserDTO adUser = cbbAdMgmtAPI.getAdUser(userDetailDTO.getUserName());
            if (adUser == null) {
                //AD域服务器异常、或者不存在用户，走本地验证
                return checkLocalUser(userDetailDTO);
            }
            if (adUser.getUserState() == IacUserStateEnum.DISABLE) {
                return LoginMessageCode.AD_ACCOUNT_DISABLE;
            }
            if (adUser.getAccountExpiresDate() != null && calDays(adUser.getAccountExpiresDate(), new Date()) < 0) {
                return LoginMessageCode.AD_ACCOUNT_EXPIRE;
            }
        }
        //账号正常返回null
        return LoginMessageCode.SUCCESS;
    }

    private int checkLocalUser(IacUserDetailDTO userDetailDTO) {
        if (IacUserStateEnum.DISABLE == userDetailDTO.getUserState()) {
            return LoginMessageCode.AD_ACCOUNT_DISABLE;
        }
        if (userDetailDTO.getAccountExpireDate() != null && userDetailDTO.getAccountExpireDate() != 0) {
            Date accountExpireDate = DateUtil.adDomainTimestampToDate(userDetailDTO.getAccountExpireDate());
            if (calDays(accountExpireDate, new Date()) < 0) {
                return LoginMessageCode.AD_ACCOUNT_EXPIRE;
            }
        }
        return LoginMessageCode.SUCCESS;
    }

    private long calDays(Date start, Date end) {
        LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return startDate.compareTo(endDate);
    }

    @Override
    public void postAuth(LoginPostAuthDTO loginPostAuthDTO, IacAuthUserResultDTO authUserResultDTO) throws BusinessException {
        Assert.notNull(loginPostAuthDTO, "loginPostAuthDTO不能为null");
        Assert.hasText(loginPostAuthDTO.getTerminalId(), "terminalId不能为null");
        Assert.hasText(loginPostAuthDTO.getUserName(), "userName不能为null");
        Assert.notNull(authUserResultDTO, "authUserResultDTO不能为null");

        IacUserDetailDTO userDetailDTO = loginPostAuthDTO.getUserDetailDTO();
        if (userDetailDTO == null) {
            return;
        }

        if (authUserResultDTO.getAuthCode() == LoginMessageCode.SUCCESS) {
            // web客户端不支持硬件特征码校验
            if (Constants.WEB_TERMINAL_ID.equals(loginPostAuthDTO.getTerminalId())) {
                return;
            }
            // 硬件特征码校验
            super.checkUserHardwareAuth(userDetailDTO.getId(), loginPostAuthDTO, authUserResultDTO);
            return;
        }

        // 是否开启密码安全策略并且错误原因是否动态口令不一致
        if (loginHelper.isPreventsBruteForce() && authUserResultDTO.getAuthCode() == LoginMessageCode.OTP_INCONSISTENT) {
            PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
            certificationHelper.changeErrorTimesAndLock(userDetailDTO.getUserName(), pwdStrategyDTO, authUserResultDTO);
        }
    }

    private int validateOtpCode(IacUserDetailDTO userDetailDTO, String otpCode) {
        UserOtpCodeDTO userOtpCodeDto = new UserOtpCodeDTO();
        userOtpCodeDto.setUserId(userDetailDTO.getId());
        userOtpCodeDto.setOtpCode(otpCode);
        String userName = userDetailDTO.getUserName();
        userOtpCodeDto.setUserName(userName);
        // 校验用户动态口令信息
        OtpCertificationCheckResultDTO checkResultDTO = otpCertificationService.checkUserOtpCodeInfo(userOtpCodeDto);
        if (BooleanUtils.isNotTrue(checkResultDTO.getEnableCheck())) {
            return checkResultDTO.getCode();
        }

        // 判断用户是否绑定过动态口令
        try {
            UserOtpCertificationConfigDTO dto = otpCertificationService.getUserOtpCertificationConfigDTO(userOtpCodeDto.getUserId());
            if (BooleanUtils.isNotTrue(dto.getHasBindOtp())) {
                LOGGER.info("用户[{}]未绑定动态口令，登录失败", userName);
                return LoginMessageCode.USER_NO_BIND_OTP;
            }
            return LoginMessageCode.SUCCESS;
        } catch (BusinessException e) {
            LOGGER.error(String.format("用户[%s]，[%s]动态口令登录失败", userName, otpCode), e);
            return LoginMessageCode.CODE_ERR_OTHER;
        }
    }

    @Override
    public String getLoginEvent() {
        return Constants.LOGIN_NORMAL;
    }

    @Override
    public ShineLoginResponseDTO generateResponse(IacUserDetailDTO userDetailDTO) {
        Assert.notNull(userDetailDTO, "userDetailDTO不能为null");

        ShineLoginResponseDTO shineLoginResponseDTO = new ShineLoginResponseDTO();
        shineLoginResponseDTO.setPassword(userDetailDTO.getPassword());
        shineLoginResponseDTO.setUserName(userDetailDTO.getUserName());
        if (IacUserTypeEnum.NORMAL == userDetailDTO.getUserType()) {
            try {
                IacUserDetailDTO iacUserDetailDTO = cbbUserAPI.getUserByName(userDetailDTO.getUserName());
                LOGGER.info("动态口令调用gss返回判断用户是否需要修改密码返回为：{}", JSON.toJSONString(iacUserDetailDTO));
                shineLoginResponseDTO.setNeedUpdatePassword(iacUserDetailDTO.getNeedUpdatePassword());
            } catch (BusinessException ex) {
                LOGGER.error("用户[{}]动态口令登录成功后获取用户信息判断是否需要修改密码异常，", userDetailDTO.getUserName(), ex);
                throw new RuntimeException(ex);
            }
        }
        return shineLoginResponseDTO;
    }
}