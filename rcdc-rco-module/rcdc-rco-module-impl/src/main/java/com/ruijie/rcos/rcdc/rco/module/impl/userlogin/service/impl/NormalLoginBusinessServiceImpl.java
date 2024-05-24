package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacLockStatus;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUpdatePasswordType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacValidateUserHardwareResultEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.common.ThirdPartyLoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserEventNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserHardwareCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums.UserIdentityEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.spi.UserIdentityHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.LoginHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.LoginPostAuthDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description: 用户登录
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/09
 *
 * @author linke
 */
@Service("normalLoginTemplateService")
public class NormalLoginBusinessServiceImpl extends AbstractLoginBusinessServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(NormalLoginBusinessServiceImpl.class);


    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    protected UserService userService;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private UserIdentityHelper userIdentityHelper;
    
    @Autowired
    private UserEventNotifyAPI userEventNotifyAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Override
    public String getKey() {
        return ShineAction.LOGIN;
    }

    @Override
    public ShineLoginDTO getShineLoginDTO(String data) throws Exception {
        Assert.hasText(data, "data不能为null");

        return shineMessageHandler.parseObject(data, ShineLoginDTO.class);
    }

    @Override
    public int processLogin(String terminalId, @Nullable IacUserDetailDTO userDetailDTO) {
        Assert.hasText(terminalId, "terminalId不能为null");
        if (userDetailDTO == null) {
            LOGGER.info("数据库中不存在用户");
            return LoginMessageCode.USERNAME_OR_PASSWORD_ERROR;
        }
        String userName = userDetailDTO.getUserName();
        // 检查是否为访客用户，如果是则不允许登录
        if (IacUserTypeEnum.VISITOR == userDetailDTO.getUserType()) {
            LOGGER.info("不允许在终端[{}]登录框中用访客用户[{}]登录", terminalId, userName);
            return LoginMessageCode.VISITOR_LOGIN;
        }
        if (userDetailDTO.getUserState() == IacUserStateEnum.DISABLE && Boolean.FALSE.equals(userDetailDTO.getEnableDomainSync())) {
            LOGGER.info("用户[{}]被禁用", userName);
            return LoginMessageCode.AD_ACCOUNT_DISABLE;
        }
        if (!userIdentityHelper.checksUserIdentity(userDetailDTO.getId(), UserIdentityEnum.ACCOUNT_PASSWORD)) {
            LOGGER.info("用户[{}]未开启账号密码认证，不允许使用账号密码登录", userName);
            return LoginMessageCode.USERNAME_OR_PASSWORD_UNOPENED;
        }
        IacLockInfoDTO accountLockInfo = certificationHelper.getUserAccountLockInfo(userDetailDTO.getUserName());
        return accountLockInfo.getLockStatus() == IacLockStatus.LOCKED ? LoginMessageCode.USER_LOCKED : LoginMessageCode.SUCCESS;
    }

    @Override
    public void postAuth(LoginPostAuthDTO loginPostAuthDTO, IacAuthUserResultDTO authUserResultDTO)
            throws BusinessException {
        Assert.notNull(loginPostAuthDTO, "loginPostAuthDTO不能为null");
        Assert.hasText(loginPostAuthDTO.getTerminalId(), "terminalId不能为null");
        Assert.hasText(loginPostAuthDTO.getUserName(), "userName不能为null");
        Assert.notNull(authUserResultDTO, "authUserResultDTO不能为null");
        IacUserDetailDTO userDetailDTO = loginPostAuthDTO.getUserDetailDTO();
        // 当authCode==1且userEntity存在且是本地用户，这时才是密码输错
        if (userDetailDTO == null) {
            return;
        }
        if (authUserResultDTO.getAuthCode() == LoginMessageCode.SUCCESS) {
            // 不需要辅助认证跳过
            if (Boolean.FALSE.equals(loginPostAuthDTO.getNeedAssistAuth())) {
                return;
            }
            // 硬件特征码校验
            validateHardwareCertification(loginPostAuthDTO.getTerminalId(), userDetailDTO, authUserResultDTO);
            return;
        }

        // 是否开启密码安全策略
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
        boolean enablePreventsBruteForce = BooleanUtils.isTrue(pwdStrategyDTO.getPreventsBruteForce());
        if (!enablePreventsBruteForce) {
            return;
        }
        if (authUserResultDTO.getAuthCode() != LoginMessageCode.USERNAME_OR_PASSWORD_ERROR
                && authUserResultDTO.getAuthCode() != ThirdPartyLoginMessageCode.USER_THIRD_PARTY_AUTH_ERROR) {
            return;
        }

        certificationHelper.changeErrorTimesAndLock(userDetailDTO.getUserName(), pwdStrategyDTO, authUserResultDTO);
    }

    protected void validateHardwareCertification(String terminalId, IacUserDetailDTO userDetailDTO, IacAuthUserResultDTO authUserResponse)
            throws BusinessException {
        // 硬件特征码校验
        UserHardwareCheckDTO userHardwareCheckDTO = new UserHardwareCheckDTO(userDetailDTO.getId(), terminalId);
        userHardwareCheckDTO.setUserName(userDetailDTO.getUserName());
        IacValidateUserHardwareResultEnum resultEnum = userHardwareCertificationAPI.validateCertification(userHardwareCheckDTO);
        authUserResponse.setAuthCode(hardwareConverLoginMap.get(resultEnum.getCode()));
    }

    @Override
    public String getLoginEvent() {
        return Constants.LOGIN_NORMAL;
    }

    @Override
    public ShineLoginResponseDTO responseSuccess(IacUserDetailDTO userDetailDTO, IacAuthUserResultDTO authUserResponse) {
        Assert.notNull(userDetailDTO, "userDetailDTO不能为null");
        Assert.notNull(authUserResponse, "authUserResponse不能为null");

        UUID userId = userDetailDTO.getId();
        String noticeEvent = getLoginEvent();
        UserLoginNoticeDTO noticeRequest = new UserLoginNoticeDTO(noticeEvent, userId);

        // 获取配置
        DtoResponse<?> eventResponse = userEventNotifyAPI.userLoginSuccess(noticeRequest);
        Object eventContent = eventResponse.getDto();

        ShineLoginResponseDTO shineLoginResponseDTO = generateResponse(userDetailDTO);
        shineLoginResponseDTO.setContent(eventContent);

        // 返回策略、用户等错误信息给shine
        LoginResultMessageDTO loginResultMessageDTO = shineLoginResponseDTO.getLoginResultMessage();
        loginHelper.warpSafetyRedLine(authUserResponse.getUserName(), loginResultMessageDTO);
        LOGGER.info("用户{}登录成功，shineLoginResponseDTO={}", authUserResponse.getUserName(), JSON.toJSONString(shineLoginResponseDTO));

        return shineLoginResponseDTO;
    }

    @Override
    public ShineLoginResponseDTO generateResponse(IacUserDetailDTO userDetailDTO) {
        Assert.notNull(userDetailDTO, "userDetailDTO must not be null");

        ShineLoginResponseDTO shineLoginResponseDTO = new ShineLoginResponseDTO();
        shineLoginResponseDTO.setUserName(userDetailDTO.getUserName());
        shineLoginResponseDTO.setPassword(userDetailDTO.getPassword());
        if (IacUserTypeEnum.NORMAL == userDetailDTO.getUserType()) {
            LOGGER.info("用户[{}}为普通用户，判断用户是否需要修改密码", userDetailDTO.getUserName());
            shineLoginResponseDTO.setNeedUpdatePassword(needUpdatePasswordWork(userDetailDTO, shineLoginResponseDTO));
            return shineLoginResponseDTO;
        }
        shineLoginResponseDTO.setLoginResultMessage(new LoginResultMessageDTO());

        return shineLoginResponseDTO;
    }

    /**
     * 判断是否需要修改密码，需要就修改用户needUpdatePassword标识
     *
     * @param userDetailDTO userDetailDTO
     * @param shineLoginResponseDTO ShineLoginResponseDTO
     * @return true 需要修改； false 不需要
     */
    private boolean needUpdatePasswordWork(IacUserDetailDTO userDetailDTO, ShineLoginResponseDTO shineLoginResponseDTO) {
        LoginResultMessageDTO resultMessageDTO = new LoginResultMessageDTO();
        shineLoginResponseDTO.setLoginResultMessage(resultMessageDTO);

        Boolean isNeedUpdatePassword = userDetailDTO.getNeedUpdatePassword();
        IacUpdatePasswordType updatePasswordType = userDetailDTO.getUpdatePasswordType();
        if (BooleanUtils.isFalse(isNeedUpdatePassword)) {
            if (updatePasswordType == IacUpdatePasswordType.ALARM) {
                resultMessageDTO.setPwdSurplusDays(userDetailDTO.getPasswordRemindDays());
            }
            return false;
        }

        // 用户正常无需修改密码
        if (updatePasswordType == IacUpdatePasswordType.NORMAL) {
            return false;
        }
        // 提醒密码即将到期
        if (updatePasswordType == IacUpdatePasswordType.ALARM) {
            resultMessageDTO.setPwdSurplusDays(userDetailDTO.getPasswordRemindDays());
            return false;
        }
        // 密码是否过期
        if (updatePasswordType == IacUpdatePasswordType.EXPIRE) {
            resultMessageDTO.setIsPasswordExpired(true);
        }
        // 校验密码复杂度
        if (updatePasswordType == IacUpdatePasswordType.WEAK) {
            resultMessageDTO.setIsPasswordLevelChange(true);
        }
        // 是否重置密码
        if (updatePasswordType == IacUpdatePasswordType.RESET) {
            resultMessageDTO.setUpdatePasswordByReset(true);
        }
        // 是否是初始化密码
        if (updatePasswordType == IacUpdatePasswordType.INITIALIZE) {
            resultMessageDTO.setForceUpdatePassword(true);
        }
        // 是否是弱密码
        if (updatePasswordType == IacUpdatePasswordType.POPULAR) {
            resultMessageDTO.setIsWeakPassword(true);
        }
        LOGGER.info("普通用户登录处理是否修改密码返回为：{}", JSON.toJSONString(shineLoginResponseDTO));

        return true;
    }
}