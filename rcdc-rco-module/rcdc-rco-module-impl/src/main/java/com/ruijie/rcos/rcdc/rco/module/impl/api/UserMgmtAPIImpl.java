package com.ruijie.rcos.rcdc.rco.module.impl.api;

import static com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_USER_CHANGE_PWD_FAIL;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUpdatePasswordType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.*;
import com.ruijie.rcos.rcdc.rco.module.def.enums.ClientType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.IacThirdPartyCertificationRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacDomainAuthRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.IacCertificationResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.common.ThirdPartyLoginMessageCode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.LoginLogBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertifiedSecurityConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ModifyPasswordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.UserDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.*;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserUnifiedLoginCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.CertificationResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.SyncUserPwdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.ThirdPartyCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedUserDesktopResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.RcoAdGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums.UserIdentityEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.spi.UserIdentityHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.common.AccountLastLoginUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.common.RcoInvalidTimeHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.OtpCertificationCheckResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.service.UserOtpCertificationService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoIacPasswordBlacklistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.ChangeUserPwdHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.LoginHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ChangeUserPwdCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ChangeUserPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.PushUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.maintenance.MaintenanceModeValidator;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.RccmUnifiedChangePwdResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.RccmUnifiedLoginResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.PasswordUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;


/**
 * Description: 用户管理接口实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-14 16:58:00
 *
 * @author zjy
 */
public class UserMgmtAPIImpl implements UserMgmtAPI {

    private static final int USER_LOGIN_LOGIN_TIMEOUT_MINUTE = 60;

    private static final String USER_LOGIN_KEY = "user-login-";

    private static final int MAX_USER_QUERY_NUM = 500;

    private static final Long TIME_CONVERSION_UNIT = 24 * 60 * 60 * 1000L;

    private static final Long EXPIRE_TIME_ZERO = 0L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMgmtAPIImpl.class);

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private UserIdentityHelper userIdentityHelper;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    private MaintenanceModeValidator maintenanceModeValidator;

    @Autowired
    private ChangeUserPwdHelper changeUserPwdHelper;

    @Autowired
    private ModifyPasswordAPI modifyPasswordAPI;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private UserLoginRccmOperationService userLoginRccmOperationService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private UserOtpCertificationService otpCertificationService;

    @Autowired
    private UserOtpCertificationAPI userOtpCertificationAPI;

    @Resource(name = "otpCodeLoginTemplateService")
    private LoginBusinessService otpCodeLoginTemplateService;

    @Autowired
    private RcoAdGroupService rcoAdGroupService;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CertifiedSecurityConfigAPI certifiedSecurityConfigAPI;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private IacThirdPartyUserAPI thirdPartyUserAPI;

    @Autowired
    private RcoInvalidTimeHelper rcoInvalidTimeHelper;

    @Autowired
    private RcoIacPasswordBlacklistService rcoIacPasswordBlacklistService;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Override
    public UserInfoDTO getUserInfoById(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        return convertUserInfo(userService.getUserInfoById(userId));
    }

    @Override
    public UserInfoDTO getUserInfoByName(String userName) {
        Assert.notNull(userName, "userName must not be null");

        return convertUserInfo(userService.getUserInfoByName(userName));
    }

    @Override
    public List<UserInfoDTO> getUserInfoByIdList(List<UUID> userIdList) {
        Assert.notNull(userIdList, "userIdList must not be empty");

        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();
        List<RcoViewUserEntity> viewUserEntityList = userService.getUserInfoByIdList(userIdList);
        viewUserEntityList.forEach(item -> userInfoDTOList.add(convertUserInfo(item)));
        return userInfoDTOList;
    }

    private UserInfoDTO convertUserInfo(RcoViewUserEntity viewUserEntity) {
        if (viewUserEntity == null) {
            // 用户不存在时，直接返回空
            return null;
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUuid(viewUserEntity.getId());
        userInfoDTO.setUserName(viewUserEntity.getUserName());
        userInfoDTO.setDisplayName(viewUserEntity.getRealName());
        userInfoDTO.setUserGroupId(viewUserEntity.getGroupId());
        userInfoDTO.setUserType(viewUserEntity.getUserType());
        userInfoDTO.setState(viewUserEntity.getState());
        userInfoDTO.setAccountExpireDate(viewUserEntity.getAccountExpireDate());
        userInfoDTO.setInvalidTime(viewUserEntity.getInvalidTime());
        userInfoDTO.setDescription(viewUserEntity.getUserDescription());
        userInfoDTO.setInvalid(viewUserEntity.getInvalid());
        userInfoDTO.setCreateTime(viewUserEntity.getCreateTime());
        userInfoDTO.setLoginOutTime(viewUserEntity.getLoginOutTime());
        userInfoDTO.setInvalidRecoverTime(viewUserEntity.getInvalidRecoverTime());
        userInfoDTO.setPassword(viewUserEntity.getPassword());
        return userInfoDTO;
    }

    private String dealInvalidDescription(RcoViewUserEntity entity) {

        Integer invalidTime = entity.getInvalidTime();
        if (ObjectUtils.isEmpty(invalidTime)) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_FOREVER_INVALID);
        }

        Date invalidRecoverTime = entity.getInvalidRecoverTime();
        List<CloudDesktopDTO> dtoList = userDesktopMgmtAPI.findByUserId(entity.getId());
        // 恢复失效时间不为空则以恢复时间为准
        if (ObjectUtils.isNotEmpty(invalidRecoverTime)) {
            return AccountLastLoginUtil.setInvalidDescription(invalidRecoverTime.getTime(), invalidTime);
        } else {
            // 以最后一次登录时间为准
            Long lastLoginTime = AccountLastLoginUtil.offerLastLoginTime(entity.getId(), dtoList);
            return AccountLastLoginUtil.setInvalidDescription(lastLoginTime, invalidTime);
        }
    }

    @Override
    public UserLoginInfoDTO userLogin(UserLoginRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be empty");
        UserLoginInfoDTO userLoginInfoDTO = localUserLogin(request);
        userLoginInfoDTO.setHasUnifiedLogin(false);
        return userLoginInfoDTO;
    }

    @Override
    public UserLoginInfoDTO webClientLogin(WebClientLoginRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be empty");
        boolean hasUnifiedLogin = checkUnifiedLoginRequest(request.getTerminalId());

        UserLoginInfoDTO userLoginInfoDTO = new UserLoginInfoDTO();
        userLoginInfoDTO.setUserName(request.getUserName());

        try {
            userLoginInfoDTO = login(request, hasUnifiedLogin);
        } catch (BusinessException e) {
            // 当图形验证错误会直接返回错误，属于异常情况，当用户存在需要记录系统日志再抛出异常
            userLoginInfoDTO.setBusinessCode(Integer.valueOf(e.getKey()));
            handleWebClientLoginError(hasUnifiedLogin, userLoginInfoDTO);

            LOGGER.error("网页客户端用户[{}]登录异常", e);
            throw e;
        }

        userLoginInfoDTO.setHasUnifiedLogin(Boolean.FALSE);
        // 是否开启统一登录
        if (hasUnifiedLogin) {
            try {
                // 本地认证成功或者不存在用户下，会转发rcenter，否则返回认证失败
                if (CommonMessageCode.SUCCESS == userLoginInfoDTO.getBusinessCode()
                        || CommonMessageCode.USER_NOT_EXIST == userLoginInfoDTO.getBusinessCode()) {
                    // 需要先判断本rcdc是否登录成功，用来修改终端的用户绑定信息和userLoginSession信息缓存
                    return unifiedUserLogin(request, userLoginInfoDTO);

                } else {
                    // 开启统一登录，当前连接集群认证失败。记录审计并返回失败结果.
                    String failMsg = buildFailMsg(userLoginInfoDTO);
                    saveSystemLog(failMsg);
                    return userLoginInfoDTO;
                }

            } catch (Exception e) {
                LOGGER.error("网页客户端用户[{}]登录异常", request.getUserName(), e);
            }
        }
        switchingSpecialCode(userLoginInfoDTO);
        return userLoginInfoDTO;
    }

    private void handleWebClientLoginError(boolean hasUnifiedLogin, UserLoginInfoDTO userLoginInfoDTO) throws BusinessException {
        // 开启统一登录且用户存在，记录系统日志，和shine登录保存一致
        if (!hasUnifiedLogin) {
            return;
        }

        IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(userLoginInfoDTO.getUserName());

        if (Objects.isNull(userDetailDTO)) {
            return;
        }

        String failMsg = buildFailMsg(userLoginInfoDTO);
        saveSystemLog(failMsg);
    }

    @Override
    public UserLoginInfoDTO obtainUserLoginInfoByUserName(String userName) {
        Assert.notNull(userName, "userName must not be empty");

        // 用户锁定判断
        UserLoginInfoDTO userLoginInfoDTO = new UserLoginInfoDTO();
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
        if (certificationHelper.isLocked(userName)) {
            LOGGER.info("webclient登录用户被锁定，用户名为：{}", userName);
            userLoginInfoDTO.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
            userLoginInfoDTO.setUserName(userName);
            return userLoginInfoDTO;
        }
        // 是否启用账号锁定策略配置
        if (pwdStrategyDTO.getPreventsBruteForce() != null && !pwdStrategyDTO.getPreventsBruteForce()) {
            userLoginInfoDTO.setPreventsBruteForce(pwdStrategyDTO.getPreventsBruteForce());
            return userLoginInfoDTO;
        }
        // 密码错误剩余次数
        IacLockInfoDTO userAccountLockInfo = certificationHelper.getUserAccountLockInfo(userName);
        userLoginInfoDTO.setRemainingTimes(pwdStrategyDTO.getUserLockedErrorTimes() - userAccountLockInfo.getFailCount());

        return userLoginInfoDTO;
    }

    private void switchingSpecialCode(UserLoginInfoDTO authResult) {
        // 特殊code 需要切换一下authCode
        int authCode = CommonMessageCode.USER_NOT_EXIST == authResult.getBusinessCode() ? LoginMessageCode.USERNAME_OR_PASSWORD_ERROR
                : authResult.getBusinessCode();
        authResult.setBusinessCode(authCode);
    }

    private UserLoginInfoDTO login(WebClientLoginRequest request, boolean hasUnifiedLogin) throws BusinessException {
        UserLoginInfoDTO authResult = new UserLoginInfoDTO();
        IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(request.getUserName());
        // 开启统一登录走用户不存在AD域认证，否则走本地认证
        if (hasUnifiedLogin && userDetailDTO == null) {
            LOGGER.info("数据库中不存在用户[{}]", request.getUserName());
            try {
                IacDomainAuthRequest iacDomainAuthRequest = new IacDomainAuthRequest();
                BeanUtils.copyProperties(request, iacDomainAuthRequest);
                iacDomainAuthRequest.setPassword(request.getPassword());
                iacDomainAuthRequest.setUserName(request.getUserName());
                int authCode = cbbUserAPI.domainAuth(iacDomainAuthRequest);
                authResult.setBusinessCode(authCode);
                return authResult;
            } catch (Exception e) {
                LOGGER.error("用户[{}]登录域服务器认证失败", request.getUserName(), e);
                // 异常时本地不存在该用户，其他集群可能存在该普通用户，返回本集群不存在用户
                authResult.setBusinessCode(CommonMessageCode.USER_NOT_EXIST);
                return authResult;
            }
        } else {
            return checkLocalUserLogin(request);
        }

    }

    private String buildFailMsg(UserLoginInfoDTO authResult) {
        UserUnifiedLoginCodeEnum codeEnum = UserUnifiedLoginCodeEnum.getByCode(authResult.getBusinessCode());
        String failKey = codeEnum.getMessage();
        String failMsg = "";
        if (failKey.equals(LoginLogBusinessKey.REMIND_ERROR_TIMES)) {

            String times = String.valueOf(authResult.getRemainingTimes());
            failMsg = LocaleI18nResolver.resolve(LoginLogBusinessKey.REMIND_ERROR_TIMES, times);
        } else if (failKey.equals(LoginLogBusinessKey.USER_LOCKED)) {
            failMsg = LocaleI18nResolver.resolve(
                    authResult.getPwdLockTime() < 0 ? LoginLogBusinessKey.USER_LOCKED_PERMANENT : LoginLogBusinessKey.USER_LOCKED,
                    authResult.getPwdLockTime().toString());
        } else {
            failMsg = LocaleI18nResolver.resolve(failKey);
        }
        LOGGER.info("开启统一登录，当前连接集群用户[{}]认证结果码:[{}],提示信息：[{}]", authResult.getUserName(), authResult.getBusinessCode(),
                LocaleI18nResolver.resolve(codeEnum.getMessage()));
        return LocaleI18nResolver.resolve(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_UNIFIED_LOGIN_RESULT, authResult.getUserName(),
                failMsg);
    }

    private void saveSystemLog(String content) {
        BaseSystemLogDTO logDTO = new BaseSystemLogDTO();
        logDTO.setId(UUID.randomUUID());
        logDTO.setContent(content);
        logDTO.setCreateTime(new Date());
        baseSystemLogMgmtAPI.createSystemLog(logDTO);
    }

    private UserLoginInfoDTO checkLocalUserLogin(WebClientLoginRequest request) throws BusinessException {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        BeanUtils.copyProperties(request, userLoginRequest);
        userLoginRequest.setDeviceId(request.getIp());
        UserLoginInfoDTO userLoginInfoDTO = localUserLogin(userLoginRequest);
        userLoginInfoDTO.setHasUnifiedLogin(false);
        return userLoginInfoDTO;
    }



    @Override
    public LoginInfoChangeDTO loginInfoChangeTip(LoginInfoChangeRequestDTO loginInfoChangeRequestDTO) throws BusinessException {
        Assert.notNull(loginInfoChangeRequestDTO, "request can not be null");
        // 登陆成功 配置IP 登陆提示
        // 检查用户是否存在
        RcoViewUserEntity userEntity = userService.getUserInfoByName(loginInfoChangeRequestDTO.getUserName());
        if (userEntity == null) {
            return new LoginInfoChangeDTO();
        }
        // 生成最近登陆时间
        loginInfoChangeRequestDTO.buildLastLoginTerminalTime();
        LoginInfoChangeDTO loginInfoChangeDTO = userEntity.buildLoginInfoChangeDTO();
        if (userEntity.getUserType() != IacUserTypeEnum.VISITOR) {
            if (Boolean.TRUE.equals(certifiedSecurityConfigAPI.queryNotifyLoginTerminalChangeConfigByUnifiedLoginConfig())) {
                LOGGER.debug("全局策略开启用户登录并作出终端IP变更提示，更新用户[{}]登录信息", userEntity.getUserName());
                // 判断是否需要变更 通知
                loginInfoChangeDTO.buildNeedNotifyLoginTerminalChange(loginInfoChangeRequestDTO.getIp());
            } else {
                // 未开启 则设置为不提示
                loginInfoChangeDTO.setNeedNotifyLoginTerminalChange(Boolean.FALSE);
            }
            // 更新用户表
            cbbUserAPI.updateLastLoginInfo(loginInfoChangeRequestDTO.buildNewCbbLastLoginInfoDTO(userEntity.getId()));
        }
        return loginInfoChangeDTO;
    }



    private UserLoginInfoDTO unifiedUserLogin(WebClientLoginRequest request, UserLoginInfoDTO userLoginInfoDTO) throws BusinessException {

        String terminalId = request.getTerminalId();

        ShineLoginDTO shineLoginDTO = new ShineLoginDTO();
        shineLoginDTO.setUserName(request.getUserName());
        shineLoginDTO.setPassword(request.getPassword());
        shineLoginDTO.setIp(request.getIp());
        shineLoginDTO.setHasLocalAuth(CommonMessageCode.SUCCESS == userLoginInfoDTO.getBusinessCode());
        CbbDispatcherRequest cbbDispatcherRequest = new CbbDispatcherRequest();
        cbbDispatcherRequest.setDispatcherKey(ShineAction.LOGIN);
        cbbDispatcherRequest.setTerminalId(terminalId);
        // 构造终端请求数据
        cbbDispatcherRequest.setData(JSON.toJSONString(shineLoginDTO));
        RccmUnifiedLoginResultDTO rccmUnifiedLoginResultDTO =
                userLoginRccmOperationService.requestLoginValidateInRccm(cbbDispatcherRequest, terminalId, shineLoginDTO);

        if (CommonMessageCode.CODE_ERR_OTHER == rccmUnifiedLoginResultDTO.getResultCode()
                || CommonMessageCode.CODE_ERR_OTHER == rccmUnifiedLoginResultDTO.getAuthResultCode()) {
            // 认证接口内部异常
            userLoginInfoDTO.setBusinessCode(CommonMessageCode.CODE_ERR_OTHER);
        } else {
            // 认证接口处理正常返回数据
            buildLoginResult(request, userLoginInfoDTO, rccmUnifiedLoginResultDTO);
        }

        return userLoginInfoDTO;
    }

    private void buildLoginResult(WebClientLoginRequest request, UserLoginInfoDTO userLoginInfoDTO,
            RccmUnifiedLoginResultDTO rccmUnifiedLoginResultDTO) {
        userLoginInfoDTO.setBusinessCode(rccmUnifiedLoginResultDTO.getAuthResultCode());
        userLoginInfoDTO.setNeedUpdatePassword(rccmUnifiedLoginResultDTO.getNeedUpdatePassword());
        LoginResultMessageDTO loginResultMessage = rccmUnifiedLoginResultDTO.getLoginResultMessage();
        if (loginResultMessage != null) {
            userLoginInfoDTO.setPwdLockTime(loginResultMessage.getPwdLockTime());
            int userLockedErrorsTimes = loginResultMessage.getUserLockedErrorsTimes() != null ? loginResultMessage.getUserLockedErrorsTimes() : 0;
            int errorTimes = loginResultMessage.getErrorTimes() != null ? loginResultMessage.getErrorTimes() : 0;
            userLoginInfoDTO.setRemainingTimes(userLockedErrorsTimes - errorTimes);
            userLoginInfoDTO.setPwdSurplusDays(loginResultMessage.getPwdSurplusDays());
            userLoginInfoDTO.setPasswordExpired(loginResultMessage.getIsPasswordExpired());
            userLoginInfoDTO.setPasswordLevelChange(loginResultMessage.getIsPasswordLevelChange());
            userLoginInfoDTO.setErrorMsg(loginResultMessage.getErrorMsg());
        }
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(request.getUserName());
        if (userDetailDTO != null) {
            needUpdatePasswordWork(userDetailDTO, userLoginInfoDTO);
        }
        userLoginInfoDTO.setHasUnifiedLogin(Boolean.TRUE);
        userLoginInfoDTO.setUserName(request.getUserName());
    }

    private UserLoginInfoDTO localUserLogin(UserLoginRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        String userName = request.getUserName();
        LOGGER.info("用户[{}]开始登录", userName);
        UserLoginInfoDTO result = new UserLoginInfoDTO();

        // 检查用户是否存在
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(userName);
        if (userDetailDTO == null) {
            LOGGER.info("webclient用户不存在，用户名为：[{}]", userName);
            // 用户AD域用户安全组检测
            boolean isSuccess = rcoAdGroupService.checkUserAdGroupResult(userName);
            if (!isSuccess) {
                return handleResultWhenUserNotExists(request);
            }

            // 获取增量同步的用户
            userDetailDTO = userService.getUserDetailByName(userName);
        }

        UUID userId = userDetailDTO.getId();
        // 检查用户是否允许作为访客
        if (IacUserTypeEnum.VISITOR == userDetailDTO.getUserType()) {
            LOGGER.info("用户[{}]为访客用户，不可登录", userName);
            result.setBusinessCode(LoginMessageCode.VISITOR_LOGIN);
            return result;
        }

        // 用户锁定不做密码校验
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
        result.setPreventsBruteForce(pwdStrategyDTO.getPreventsBruteForce());
        if (certificationHelper.isLocked(userName)) {
            LOGGER.info("用户[{}]已被锁定", userName);
            result.setBusinessCode(LoginMessageCode.USER_LOCKED);
            result.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
            return result;
        }

        // 用户禁用
        if (userDetailDTO.getUserState() == IacUserStateEnum.DISABLE && Boolean.FALSE.equals(userDetailDTO.getEnableDomainSync())) {
            LOGGER.info("用户[{}]已禁用，不可登录", userName);
            result.setBusinessCode(LoginMessageCode.AD_ACCOUNT_DISABLE);
            return result;
        }

        // 未开启账号密码验证
        if (IacUserTypeEnum.THIRD_PARTY != userDetailDTO.getUserType()) {
            if (!userIdentityHelper.checksUserIdentity(userDetailDTO.getId(), UserIdentityEnum.ACCOUNT_PASSWORD)) {
                LOGGER.info("用户[{}]未开启账号密码认证，不允许使用账号密码登录", userName);
                result.setBusinessCode(LoginMessageCode.USERNAME_OR_PASSWORD_UNOPENED);
                return result;
            }
        } else {
            if (!thirdPartyUserAPI.getThirdPartyAuthState()) {
                LOGGER.info("未开启全局第三方认证，不允许使用账号密码登录", userName);
                result.setBusinessCode(ThirdPartyLoginMessageCode.UNABLE_THIRD_PARTY_AUTH);
                return result;
            }

            if (!userIdentityHelper.checksUserIdentity(userDetailDTO.getId(), UserIdentityEnum.THIRD_PARTY)) {
                LOGGER.info("用户[{}]未开启第三方认证，不允许使用账号密码登录", userName);
                result.setBusinessCode(ThirdPartyLoginMessageCode.UNABLE_USER_THIRD_PARTY_AUTH);
                return result;
            }
        }

        try {
            AesUtil.descrypt(request.getPassword(), RedLineUtil.getRealUserRedLine());
        } catch (Exception ex) {
            LOGGER.error(String.format("AES解密失败, 用户名[%s],密码[%s]", userName, request.getPassword()), ex);
            result.setBusinessCode(LoginMessageCode.DESCRYPT_PWD_ERROR);
            return result;
        }

        // 进行密码校验
        IacAuthUserDTO authUserRequest = new IacAuthUserDTO(userName, request.getPassword());
        BeanUtils.copyProperties(request, authUserRequest);
        authUserRequest.setSubSystem(SubSystem.CDC);
        // UWS电脑端/移动端登录不需校验图形验证码
        if (ClientType.UWS.equals(request.getSource())) {
            authUserRequest.setShouldCheckCaptchaCode(Boolean.FALSE);
        }
        IacAuthUserResultDTO authUserResponse = new IacAuthUserResultDTO();
        LOGGER.info("webclient用户登录请求为：{}", JSON.toJSONString(authUserRequest));
        if (Boolean.TRUE.equals(request.getOtherLoginMethod())) {
            LOGGER.info("非账号密码登录，跳过登录步骤");
            authUserResponse.setAuthCode(CommonMessageCode.SUCCESS);
        } else {
            IacAuthUserResultDTO userResultDTO = cbbUserAPI.authUser(authUserRequest);
            BeanUtils.copyProperties(userResultDTO, authUserResponse);
        }
        LOGGER.info("weclient登录调authUser接口返回为：{}", JSON.toJSONString(authUserResponse));
        // 密码错误的情况下需要更新错误次数及锁定状态
        if (LoginMessageCode.USERNAME_OR_PASSWORD_ERROR == authUserResponse.getAuthCode()
                || ThirdPartyLoginMessageCode.USER_THIRD_PARTY_AUTH_ERROR == authUserResponse.getAuthCode()) {
            LOGGER.info("用户[{}]密码校验未通过", userName);
            final  IacUserDetailDTO userDetail = userDetailDTO;
            LockableExecutor.executeWithTryLock(USER_LOGIN_KEY + userId, () -> {
                if (userDetail != null) {
                    postAuth(userDetail, authUserResponse, pwdStrategyDTO);
                }
            }, USER_LOGIN_LOGIN_TIMEOUT_MINUTE);
        }

        result.setBusinessCode(authUserResponse.getAuthCode());
        result.setErrorMsg(authUserResponse.getErrorMsg());
        // 锁定及剩余次数处理, 当为第三方用户登录失败且开启了防爆破解，也需要提示剩余次数
        if (LoginMessageCode.REMIND_ERROR_TIMES == authUserResponse.getAuthCode()
                || (ThirdPartyLoginMessageCode.USER_THIRD_PARTY_AUTH_ERROR == authUserResponse.getAuthCode()
                        && pwdStrategyDTO.getPreventsBruteForce())) {
            LOGGER.info("提示用户[{}]锁定次数", userName);
            IacLockInfoDTO userAccountLockInfo = certificationHelper.getUserAccountLockInfo(userName);
            result.setRemainingTimes(pwdStrategyDTO.getUserLockedErrorTimes() - userAccountLockInfo.getFailCount());
            return result;
        }
        if (LoginMessageCode.USER_LOCKED == authUserResponse.getAuthCode()) {
            LOGGER.info("用户[{}]已被锁定", userName);
            result.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
            return result;
        }
        if (LoginMessageCode.SUCCESS != authUserResponse.getAuthCode()) {
            LOGGER.warn("用户[{}]校验失败，code={}", userName, authUserResponse.getAuthCode());
            return result;
        }
        if (ObjectUtils.isNotEmpty(userDetailDTO)) {
            Integer authCode = rcoInvalidTimeHelper.obtainLoginStateCode(authUserResponse.getAuthCode(), userDetailDTO);
            result.setBusinessCode(authCode);
        }
        if (LoginMessageCode.SUCCESS != result.getBusinessCode()) {
            LOGGER.warn("用户[{}]校验失败，code={}", userName, result.getBusinessCode());
            return result;
        }
        result.setBusinessCode(LoginMessageCode.SUCCESS);
        result.setUuid(userDetailDTO.getId());
        result.setUserName(userDetailDTO.getUserName());
        result.setDisplayName(userDetailDTO.getRealName());
        result.setUserGroupId(userDetailDTO.getGroupId());
        result.setState(userDetailDTO.getUserState());
        result.setUserType(userDetailDTO.getUserType());
        result.setNeedUpdatePassword(false);
        result.setPasswordExpired(false);
        result.setLastLoginTerminalTime(userDetailDTO.getLastLoginTerminalTime());
        result.setLastLoginTerminalIp(userDetailDTO.getLastLoginTerminalIp());
        needUpdatePasswordWork(userDetailDTO, result);
        LOGGER.info("用户登录校验成功, 用户名[{}], 用户id[{}]", userName, userDetailDTO.getId());

        return result;
    }

    /**
     * 认证后处理事件：记录错误次数，是否需要锁定等
     */
    private void postAuth(IacUserDetailDTO userDetailDTO , IacAuthUserResultDTO authUserResponse, PwdStrategyDTO pwdStrategyDTO) {
        // 当authCode==1且userEntity存在且是本地用户，这时才是密码输错
        if (userDetailDTO == null) {
            return;
        }

        if (authUserResponse.getAuthCode() == LoginMessageCode.SUCCESS) {
            return;
        }
        // 是否启用
        if (!pwdStrategyDTO.getPreventsBruteForce()) {
            return;
        }
        if (LoginMessageCode.USERNAME_OR_PASSWORD_ERROR != authUserResponse.getAuthCode()
                && ThirdPartyLoginMessageCode.USER_THIRD_PARTY_AUTH_ERROR != authUserResponse.getAuthCode()) {
            return;
        }
        certificationHelper.changeErrorTimesAndLock(userDetailDTO.getUserName(), pwdStrategyDTO, authUserResponse);
    }

    private void needUpdatePasswordWork(IacUserDetailDTO userDetailDTO, UserLoginInfoDTO userLoginInfoDTO) {
        Boolean isNeedUpdatePassword = userDetailDTO.getNeedUpdatePassword();
        IacUpdatePasswordType updatePasswordType = userDetailDTO.getUpdatePasswordType();
        if (BooleanUtils.isFalse(isNeedUpdatePassword)) {
            userLoginInfoDTO.setNeedUpdatePassword(false);
            if (updatePasswordType == IacUpdatePasswordType.ALARM) {
                userLoginInfoDTO.setPwdSurplusDays(userDetailDTO.getPasswordRemindDays());
            }
            return;
        }
        userLoginInfoDTO.setNeedUpdatePassword(isNeedUpdatePassword);
        if (updatePasswordType == IacUpdatePasswordType.NORMAL) {
            userLoginInfoDTO.setNeedUpdatePassword(false);
            return;
        }
        if (updatePasswordType == IacUpdatePasswordType.ALARM) {
            userLoginInfoDTO.setPwdSurplusDays(userDetailDTO.getPasswordRemindDays());
            userLoginInfoDTO.setNeedUpdatePassword(false);
            return;
        }
        // 密码是否过期
        if (updatePasswordType == IacUpdatePasswordType.EXPIRE) {
            userLoginInfoDTO.setPasswordExpired(true);
        }
        // 校验密码复杂度
        if (updatePasswordType == IacUpdatePasswordType.WEAK) {
            userLoginInfoDTO.setPasswordLevelChange(true);
        }
        // 是否重置密码
        if (updatePasswordType == IacUpdatePasswordType.RESET) {
            userLoginInfoDTO.setUpdatePasswordByReset(true);
        }
        // 是否是初始化密码
        if (updatePasswordType == IacUpdatePasswordType.INITIALIZE) {
            userLoginInfoDTO.setForceUpdatePassword(true);
        }
        // 是否是弱密码
        if (updatePasswordType == IacUpdatePasswordType.POPULAR) {
            userLoginInfoDTO.setIsWeakPassword(true);
        }
        userLoginInfoDTO.setNeedUpdatePassword(true);
        LOGGER.info("webclient用户登录处理是否修改密码返回为：{}", JSON.toJSONString(userLoginInfoDTO));
    }


    @Override
    public UserUpdatePwdDTO updatePwd(UserUpdatePwdRequest request) throws BusinessException {
        Assert.notNull(request, "request must not null");
        Assert.hasText(request.getUserName(), "userName 不能为空");
        Assert.hasText(request.getOldPassword(), "oldPassword 不能为空");
        Assert.hasText(request.getNewPassword(), "newPassword 不能为空");

        UserUpdatePwdDTO userUpdatePwdDto = new UserUpdatePwdDTO();
        if (maintenanceModeValidator.validate()) {
            LOGGER.info("服务器处于维护模式下，无法修改用户密码");
            userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.IN_MAINTENANCE);
            return userUpdatePwdDto;
        }

        // 随机生成一个UUID作为terminalId配合统一修改密码
        String terminalId = UUID.randomUUID().toString();
        // 是否开启统一登录
        if (checkUnifiedLoginRequest(terminalId)) {
            ChangeUserPasswordDTO changeUserPasswordDTO = new ChangeUserPasswordDTO();
            BeanUtils.copyProperties(request, changeUserPasswordDTO);
            if (requestChangePwdInRccm(terminalId, changeUserPasswordDTO, userUpdatePwdDto)) {
                return userUpdatePwdDto;
            }
            // 调rccm报错走单机逻辑
            LOGGER.error("调rccm报错走单机逻辑，request=[{}]", JSON.toJSONString(request));
        }

        String userName = request.getUserName();
        IacUserDetailDTO userInfoDTO = cbbUserAPI.getUserByName(userName);
        if (userInfoDTO == null) {
            LOGGER.info("数据库找不到用户[{}]信息", userName);
            userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.OLD_PASSWORD_ERROR);
            return userUpdatePwdDto;
        }

        final UUID userId = userInfoDTO.getId();
        if (userInfoDTO.getUserType() == IacUserTypeEnum.AD) {
            LOGGER.info("AD域用户[{}]不允许修改密码", userName);
            userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.AD_USER_NOT_ALLOW_CHANGE_PASSWORD);
            return userUpdatePwdDto;
        }

        if (userInfoDTO.getUserType() == IacUserTypeEnum.LDAP) {
            LOGGER.info("LDAP用户[{}]不允许修改密码", userName);
            userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD);
            return userUpdatePwdDto;
        }

        if (userInfoDTO.getUserType() == IacUserTypeEnum.VISITOR) {
            LOGGER.info("访客用户[{}]不允许修改密码", userName);
            userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.VISITOR_USER_NOT_ALLOW_CHANGE_PASSWORD);
            return userUpdatePwdDto;
        }

        if (userInfoDTO.getUserType() == IacUserTypeEnum.THIRD_PARTY) {
            LOGGER.info("第三方用户[{}]不允许修改密码", userName);
            userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.THIRD_PARTY_USER_NOT_ALLOW_CHANGE_PASSWORD);
            return userUpdatePwdDto;
        }

        // 检查newPwd是否符合配置复杂度要求
        boolean isNewPwdValid = false;
        try {
            isNewPwdValid = changeUserPwdHelper.checkNwdPwd(request.getNewPassword(), request.getApiCallerTypeEnum());
        } catch (Exception ex) {
            LOGGER.error("检查新密码发生异常, ex ：", ex);
        }
        if (!isNewPwdValid) {
            LOGGER.info("用户[{}]新密码不符合密码复杂度要求", userName);
            userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.CHANGE_PASSWORD_UNABLE_REQUIRE);
            return userUpdatePwdDto;
        }

        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        if (pwdStrategyDTO.getPreventsBruteForce() && certificationHelper.isLocked(userName)) {
            LOGGER.info("用户[{}]被锁定", userName);
            userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.IN_LOCKED);
            userUpdatePwdDto.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
            return userUpdatePwdDto;
        }
        boolean isOldPwdValid = false;
        try {
            isOldPwdValid = changeUserPwdHelper.checkOldPwd(userName, request.getOldPassword(), request.getApiCallerTypeEnum());
        } catch (Exception ex) {
            LOGGER.error("检查旧密码发生异常, ex ：", ex);
        }
        if (!isOldPwdValid) {
            LOGGER.info("修改用户[{}]密码失败，原因：原密码错误", userName);
            if (!pwdStrategyDTO.getPreventsBruteForce()) {
                userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.OLD_PASSWORD_ERROR);
                return userUpdatePwdDto;
            }

            LockableExecutor.executeWithTryLock(USER_LOGIN_KEY + userId.toString(), () -> {
                if (ChangeUserPwdCode.REMIND_ERROR_TIMES == changeUserPwdHelper.checkNeedRemindOrLock(userName)) {
                    userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.REMIND_ERROR_TIMES);
                    IacLockInfoDTO userAccountLockInfo = certificationHelper.getUserAccountLockInfo(userName);
                    LOGGER.info("密码错误次数，{}，线程id: {}", userAccountLockInfo.getFailCount(), Thread.currentThread().getName());
                    userUpdatePwdDto.setRemainingTimes(pwdStrategyDTO.getUserLockedErrorTimes() - userAccountLockInfo.getFailCount());
                } else {
                    userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.IN_LOCKED);
                    userUpdatePwdDto.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
                }
            }, USER_LOGIN_LOGIN_TIMEOUT_MINUTE);
            return userUpdatePwdDto;
        }

        final AtomicBoolean isSuccess = new AtomicBoolean(false);

        // 校验弱密码
        if (rcoIacPasswordBlacklistService
                .isPasswordBlackList(changeUserPwdHelper.descUserPwd(request.getNewPassword(), request.getApiCallerTypeEnum()))) {
            userUpdatePwdDto.setBusinessCode(ChangeUserPwdCode.WEAK_PWD);
            return userUpdatePwdDto;
        }

        try {
            LockableExecutor.executeWithTryLock(USER_LOGIN_KEY + userId.toString(), () -> {
                isSuccess.set(modifyPasswordAPI.modifUserPwdSyncAdminPwd(changeUserPwdHelper.buildModifyPasswordDTO(userInfoDTO.getId(),
                        request.getNewPassword(), request.getOldPassword(), request.getApiCallerTypeEnum())));
            }, USER_LOGIN_LOGIN_TIMEOUT_MINUTE);

            LOGGER.info("修改用户[{}]密码是否成功：[{}]", userName, isSuccess);
        } catch (BusinessException e) {
            LOGGER.error(String.format("修改用户[%s]密码同步管理员密码失败", userName), e);
            String errorMsg = e.getAttachment(UserTipContainer.Constants.USER_TIP, e.getI18nMessage());
            auditLogAPI.recordLog(RCDC_RCO_USER_CHANGE_PWD_FAIL, userName, errorMsg);
        }
        if (!isSuccess.get()) {
            userUpdatePwdDto.setBusinessCode(CommonMessageCode.CODE_ERR_OTHER);
            return userUpdatePwdDto;
        }

        userUpdatePwdDto.setBusinessCode(CommonMessageCode.SUCCESS);
        return userUpdatePwdDto;
    }

    @Override
    public List<UserDesktopInfoDTO> getDesktopInfoByUserName(UserDesktopDTO userDesktopDTO) throws Exception {
        Assert.notNull(userDesktopDTO, "request must not null");
        List<UserDesktopInfoDTO> desktopInfoDTOList = new ArrayList<>();
        // 是否开启统一登录
        if (checkUnifiedLoginRequest(userDesktopDTO.getTerminalId())) {
            // 统一登录只支持VDI
            if (userDesktopDTO.getDesktopCategoryList().size() != 1
                    || !CbbImageType.VDI.name().equals(userDesktopDTO.getDesktopCategoryList().get(0))) {
                throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_CLUSTER_DESK_MUST_EQUALS_VDI);
            }
            try {
                List<UnifiedUserDesktopResultDTO> desktopResultDTOList =
                        userLoginRccmOperationService.requestUserVDIDesktopInRccm(userDesktopDTO.getTerminalId(), false);
                // 获取桌面信息
                getUserDesktopList(desktopInfoDTOList, desktopResultDTOList);
            } catch (Exception ex) {
                // rcenter 缓存失效，获取单集群
                getAllDesktopByUserName(userDesktopDTO, desktopInfoDTOList);
                return desktopInfoDTOList;
            }
        } else {
            getAllDesktopByUserName(userDesktopDTO, desktopInfoDTOList);
        }
        // 桌面列表以桌面名称排序
        if (!desktopInfoDTOList.isEmpty()) {
            desktopInfoDTOList.sort(Comparator.comparing(UserDesktopInfoDTO::getDesktopName));
        }
        return desktopInfoDTOList;
    }

    @Override
    public CheckUserOtpCodeResultDTO checkOtpCode(CheckUserOtpCodeDTO checkUserOtpCodeDTO) {
        Assert.notNull(checkUserOtpCodeDTO, "checkUserOtpCodeDTO must not null");
        CheckUserOtpCodeResultDTO resultDTO = new CheckUserOtpCodeResultDTO();
        String userName = checkUserOtpCodeDTO.getUserName();
        IacUserDetailDTO userDetailDTO = null;
        try {
            userDetailDTO = iacUserMgmtAPI.getUserByName(userName);
        } catch (BusinessException e) {
            throw new IllegalStateException("获取用户信息失败:" + userName, e);
        }
        if (userDetailDTO == null) {
            LOGGER.warn("校验用户[{}]动态口令码失败，原因:用户不存在", userName);
            resultDTO.setBusinessCode(LoginMessageCode.USERNAME_OR_OTP_ERROR);
            return resultDTO;
        }
        if (userDetailDTO.getUserState() == IacUserStateEnum.DISABLE) {
            LOGGER.info("校验用户[{}]动态口令码失败，原因：用户已禁用", userName);
            resultDTO.setBusinessCode(LoginMessageCode.AD_ACCOUNT_DISABLE);
            return resultDTO;
        }

        Boolean isFirstCheck = checkUserOtpCodeDTO.getFirstCheck() == null ? Boolean.FALSE : checkUserOtpCodeDTO.getFirstCheck();
        // 绑定动态口令时，不进行防爆校验
        if (!isFirstCheck && certificationHelper.isLocked(checkUserOtpCodeDTO.getUserName())) {
            LoginResultMessageDTO loginResultMessageDTO = certificationHelper.buildPwdStrategyResult(LoginMessageCode.USER_LOCKED, userName);
            resultDTO.setLoginResultMessageDTO(loginResultMessageDTO);
            resultDTO.setBusinessCode(LoginMessageCode.USER_LOCKED);
            LOGGER.warn("校验用户[{}]动态口令码失败，原因:用户被锁定", userName);
            return resultDTO;
        }

        checkUserOtpCodeDTO.setUserId(userDetailDTO.getId());
        // 校验用户动态口令信息
        UserOtpCodeDTO userOtpCodeDTO = new UserOtpCodeDTO();
        BeanUtils.copyProperties(checkUserOtpCodeDTO, userOtpCodeDTO);
        OtpCertificationCheckResultDTO checkResultDTO = otpCertificationService.checkUserOtpCodeInfo(userOtpCodeDTO);
        boolean enableCheck = BooleanUtils.isTrue(checkResultDTO.getEnableCheck());
        if (!enableCheck) {
            resultDTO.setBusinessCode(checkResultDTO.getCode());
            return handleCheckOtpCodeFail(resultDTO, checkResultDTO.getCode(), userDetailDTO, isFirstCheck);
        }

        // 第一次登录用户需要进行动态口令绑定
        if (BooleanUtils.isTrue(isFirstCheck)) {
            try {
                userOtpCertificationAPI.bindById(checkUserOtpCodeDTO.getUserId());
            } catch (BusinessException e) {
                LOGGER.error("用户[{}]，绑定[{}]动态口令失败", userName, checkUserOtpCodeDTO.getOtpCode());
                resultDTO.setBusinessCode(LoginMessageCode.USER_BIND_OTP_ERROR);
                return resultDTO;
            }
        }

        if (!isFirstCheck) {
            LoginResultMessageDTO loginResultMessageDTO = new LoginResultMessageDTO();
            loginHelper.warpSafetyRedLine(userDetailDTO.getUserName(), loginResultMessageDTO);
            resultDTO.setLoginResultMessageDTO(loginResultMessageDTO);
        }

        resultDTO.setBusinessCode(CommonMessageCode.SUCCESS);
        String resultString = JSON.toJSONString(resultDTO);
        LOGGER.info("用户[{}]动态口令校验成功,状态码为[{}],响应信息[{}]", userName, resultDTO.getBusinessCode(), resultString);
        return resultDTO;
    }

    @Override
    public OtpCodeLoginResultDTO otpCodeLogin(OtpCodeLoginRequest codeLoginRequest) throws BusinessException {
        Assert.notNull(codeLoginRequest, "codeLoginRequest must not null");
        String userName = codeLoginRequest.getUserName();
        OtpCodeLoginResultDTO otpCodeLoginResultDTO = new OtpCodeLoginResultDTO();
        // 检查用户是否存在
        RcoViewUserEntity userEntity = userService.getUserInfoByName(userName);
        if (userEntity == null) {
            LOGGER.warn("动态口令登录,用户[{}]不存在", userName);
            otpCodeLoginResultDTO.setBusinessCode(LoginMessageCode.USERNAME_OR_OTP_ERROR);
            return otpCodeLoginResultDTO;
        }

        IacAuthUserResultDTO authUserResponse =
                otpCodeLoginTemplateService.checkUserNameAndOtpCode(Constants.WEB_TERMINAL_ID, userName, codeLoginRequest.getOtpCode());
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        if (CommonMessageCode.SUCCESS == authUserResponse.getAuthCode()) {
            otpCodeLoginResultDTO.setBusinessCode(authUserResponse.getAuthCode());
            userInfoDTO.setUuid(userEntity.getId());
            userInfoDTO.setUserName(userEntity.getUserName());
            userInfoDTO.setDisplayName(userEntity.getRealName());
            userInfoDTO.setUserGroupId(userEntity.getGroupId());
            userInfoDTO.setState(userEntity.getState());
            userInfoDTO.setUserType(userEntity.getUserType());
            userInfoDTO.setLastLoginTerminalIp(userEntity.getLastLoginTerminalIp());
            userInfoDTO.setLastLoginTerminalTime(userEntity.getLastLoginTerminalTime());
            otpCodeLoginResultDTO.setUserInfoDTO(userInfoDTO);

            // 同步用户域信息
            if (userEntity.getUserType() == IacUserTypeEnum.AD) {
                cbbUserAPI.syncAdUser(userEntity.getId());
            }
            LoginResultMessageDTO loginResultMessageDTO = new LoginResultMessageDTO();
            // 设置返回登陆结果
            otpCodeLoginResultDTO.setLoginResultMessageDTO(loginResultMessageDTO);
            return otpCodeLoginResultDTO;
        }
        // 认证失败
        ShineLoginResponseDTO shineLoginResponseDTO = otpCodeLoginTemplateService.responseLoginFail(authUserResponse);
        otpCodeLoginResultDTO.setBusinessCode(authUserResponse.getAuthCode());
        otpCodeLoginResultDTO.setLoginResultMessageDTO(shineLoginResponseDTO.getLoginResultMessage());
        otpCodeLoginResultDTO.setUserInfoDTO(userInfoDTO);
        return otpCodeLoginResultDTO;
    }

    @Override
    public CertificationResultDTO checkThirdCertificationCode(ThirdPartyCertificationRequest request) throws BusinessException {
        Assert.notNull(request, "request must not null");
        IacThirdPartyCertificationRequest iacThirdPartyCertificationRequest = new IacThirdPartyCertificationRequest();
        iacThirdPartyCertificationRequest.setSubSystem(SubSystem.CDC);
        iacThirdPartyCertificationRequest.setCertificationType(request.getCertificationType());
        iacThirdPartyCertificationRequest.setCertificationCode(request.getCertificationCode());
        iacThirdPartyCertificationRequest.setUserName(request.getUserName());
        iacThirdPartyCertificationRequest.setLoginIp(request.getLoginIp());
        IacCertificationResultDTO iacCertificationResultDTO = iacUserMgmtAPI.verifyThirdPartyCertification(iacThirdPartyCertificationRequest);
        CertificationResultDTO resultDTO = new CertificationResultDTO();
        resultDTO.setResult(iacCertificationResultDTO.isResult());
        resultDTO.setMessage(iacCertificationResultDTO.getMessage());
        return resultDTO;
    }

    @Override
    public List<UUID> listGroupIdByUserIdList(List<UUID> userIdList) {
        Assert.notNull(userIdList, "userIdList must not null");
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }

        List<List<UUID>> tempUserIdList = Lists.partition(userIdList, MAX_USER_QUERY_NUM);
        Set<UUID> groupIdSet = new HashSet<>();
        for (List<UUID> idList : tempUserIdList) {
            List<RcoViewUserEntity> viewUserEntityList = userService.getUserInfoByIdList(idList);
            if (CollectionUtils.isEmpty(viewUserEntityList)) {
                continue;
            }
            groupIdSet.addAll(viewUserEntityList.stream().map(RcoViewUserEntity::getGroupId).collect(Collectors.toList()));
        }
        return Lists.newArrayList(groupIdSet);
    }

    private CheckUserOtpCodeResultDTO handleCheckOtpCodeFail(CheckUserOtpCodeResultDTO resultDTO, int authCode, IacUserDetailDTO userDetailDTO,
            Boolean firstCheck) {
        PwdStrategyDTO pwdStrategy = certificationStrategyParameterAPI.getPwdStrategy();
        String userName = userDetailDTO.getUserName();
        IacAuthUserResultDTO authUserResultDTO = new IacAuthUserResultDTO(authCode);
        authUserResultDTO.setUserName(userName);

        // 绑定动态口令时，不进行防爆处理
        if (!firstCheck && pwdStrategy.getPreventsBruteForce() && authCode == LoginMessageCode.OTP_INCONSISTENT) {
            certificationHelper.changeErrorTimesAndLock(userName, pwdStrategy, authUserResultDTO);
            LoginResultMessageDTO loginResultMessageDTO = new LoginResultMessageDTO();
            loginHelper.warpSafetyRedLine(userName, loginResultMessageDTO);
            resultDTO.setLoginResultMessageDTO(loginResultMessageDTO);
        }
        LOGGER.warn("用户[{}]动态口令校验失败,结果状态码为[{}]", userName, authUserResultDTO.getAuthCode());
        resultDTO.setBusinessCode(authUserResultDTO.getAuthCode());
        return resultDTO;
    }

    private void getAllDesktopByUserName(UserDesktopDTO userDesktopDTO, List<UserDesktopInfoDTO> desktopInfoDTOList) throws BusinessException {
        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(userDesktopDTO.getUserName());
        if (Objects.isNull(cbbUserDetailDTO)) {
            return;
        }
        // 没有时随机给一个terminalId
        String terminalId = Optional.ofNullable(userDesktopDTO.getTerminalId()).orElse(UUID.randomUUID().toString());
        List<CloudDesktopDTO> cloudDesktopDTOList = userDesktopMgmtAPI.listUserVDIDesktop(cbbUserDetailDTO, terminalId);
        getCloudDesktopList(desktopInfoDTOList, cloudDesktopDTOList, userDesktopDTO.getDesktopCategoryList());
    }

    private void getUserDesktopList(List<UserDesktopInfoDTO> desktopInfoDTOList, List<UnifiedUserDesktopResultDTO> desktopResultDTOList) {
        if (!CollectionUtils.isEmpty(desktopResultDTOList)) {
            desktopResultDTOList.forEach(item -> {
                UserDesktopInfoDTO desktopInfoDTO = new UserDesktopInfoDTO();
                BeanUtils.copyProperties(item, desktopInfoDTO);
                desktopInfoDTO.setDesktopId(item.getId());
                desktopInfoDTO.setDesktopCategory(CbbImageType.VDI.name());
                desktopInfoDTOList.add(desktopInfoDTO);
            });

        }
    }

    private boolean checkUnifiedLoginRequest(String terminalId) {
        return rccmManageService.isUnifiedLogin() && StringUtils.hasText(terminalId);
    }

    private void getCloudDesktopList(List<UserDesktopInfoDTO> desktopInfoDTOList, List<CloudDesktopDTO> cloudDesktopDTOList,
            List<String> desktopCategoryList) {
        if (CollectionUtils.isEmpty(cloudDesktopDTOList)) {
            return;
        }
        cloudDesktopDTOList.forEach(item -> {
            if (desktopCategoryList.isEmpty() || desktopCategoryList.stream().anyMatch(s -> s.equals(item.getDesktopCategory()))) {
                UserDesktopInfoDTO desktopInfoDTO = new UserDesktopInfoDTO();
                BeanUtils.copyProperties(item, desktopInfoDTO);
                desktopInfoDTO.setDesktopId(item.getId().toString());
                desktopInfoDTO.setIsPool(Objects.nonNull(item.getDesktopPoolId()));
                desktopInfoDTO.setIsOffline(CloudPlatformStatus.isAvailable(item.getPlatformStatus()));
                desktopInfoDTOList.add(desktopInfoDTO);
            }
        });
    }

    @Override
    public void syncUserInfoToTerminal(IacUserDetailDTO cbbUserDetailDTO) {
        Assert.notNull(cbbUserDetailDTO, "cbbUserDetailDTO not allow null");
        PushUserInfoDTO userInfoDTO = new PushUserInfoDTO();
        BeanUtils.copyProperties(cbbUserDetailDTO, userInfoDTO);
        userInfoDTO.setName(cbbUserDetailDTO.getUserName());
        userInfoDTO.setState(cbbUserDetailDTO.getUserState());
        CbbTerminalBasicInfoDTO terminal;
        String userName = cbbUserDetailDTO.getUserName();
        String logFlag = null;

        List<UserTerminalEntity> terminalEntityList = userTerminalDAO.findByBindUserId(cbbUserDetailDTO.getId());
        for (UserTerminalEntity userTerminalEntity : terminalEntityList) {
            try {
                String terminalId = userTerminalEntity.getTerminalId();
                terminal = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
                String mac = terminal.getMacAddr();
                String name = terminal.getTerminalName();
                logFlag = isBlank(mac) ? name : mac;
                if (terminal.getState() != CbbTerminalStateEnums.ONLINE) {
                    LOGGER.info("终端[{}]不在线，不推送用户信息", terminalId);
                    continue;
                }
                if (terminal.getTerminalPlatform() == CbbTerminalPlatformEnums.IDV
                        || terminal.getTerminalPlatform() == CbbTerminalPlatformEnums.VOI) {
                    notifyTerminalUserInfo(terminalId, terminal.getUpperMacAddrOrTerminalId(), userInfoDTO);
                }
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_USER_INFO_SYNC_TERMINAL_SUCCESS_LOG, userName, logFlag);
            } catch (Exception e) {
                LOGGER.error(String.format("同步用户[%s]信息给终端[%s]失败", userName, logFlag), e);
                String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_USER_INFO_SYNC_TERMINAL_FAIL_LOG, e, userName, logFlag, msg);
            }
        }
    }

    @Override
    public UserUpdatePwdDTO syncUserPassword(SyncUserPwdRequest syncUserPwdRequest) throws BusinessException {
        Assert.notNull(syncUserPwdRequest, "syncUserPwdRequest can not be null");
        IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(syncUserPwdRequest.getUserName());
        UserUpdatePwdDTO userUpdatePwdDTO = new UserUpdatePwdDTO();
        if (userDetailDTO == null) {
            userUpdatePwdDTO.setBusinessCode(ChangeUserPwdCode.NOT_THIS_USER);
            return userUpdatePwdDTO;
        }
        // 判断新旧密码是否一致
        if (PasswordUtil.checkNewOldPwdEquals(userDetailDTO.getPassword(), syncUserPwdRequest.getPassword())) {
            LOGGER.warn("用户[{}]修改密码，新旧密码一致不允许修改", syncUserPwdRequest.getUserName());
            userUpdatePwdDTO.setBusinessCode(ChangeUserPwdCode.NEW_OLD_PWD_NOT_EQUAL);
            return userUpdatePwdDTO;
        }
        // 修改密码
        UserUpdatePwdRequest pwdRequest = new UserUpdatePwdRequest();
        pwdRequest.setUserName(syncUserPwdRequest.getUserName());
        pwdRequest.setNewPassword(syncUserPwdRequest.getPassword());
        pwdRequest.setOldPassword(userDetailDTO.getPassword());
        userUpdatePwdDTO = updatePwd(pwdRequest);
        return userUpdatePwdDTO;
    }

    private void notifyTerminalUserInfo(String terminalId, String terminalMacAddr, PushUserInfoDTO userInfoDTO) throws BusinessException {
        CbbShineMessageRequest<String> shineMessageRequest = CbbShineMessageRequest.create(ShineAction.SYNC_USER_INFO, terminalId);
        shineMessageRequest.setContent(JSON.toJSONString(userInfoDTO));
        CbbShineMessageResponse<?> cbbShineMessageResponse;
        try {
            LOGGER.info("同步终端[{}]用户信息", terminalId);
            cbbShineMessageResponse = cbbTranspondMessageHandlerAPI.syncRequest(shineMessageRequest);
        } catch (Exception e) {
            LOGGER.error("同步终端[{}]用户信息失败，失败原因：", terminalId, e);
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_REQUEST_TERMINAL_TIME_OUT, e, terminalMacAddr);
        }

        // 如果shine返回失败
        if (cbbShineMessageResponse == null || cbbShineMessageResponse.getCode() != CommonMessageCode.SUCCESS) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_TERMINAL_SYNC_USER_INFO_FAIL, terminalMacAddr);
        }
    }

    private boolean forceUpdateUserPwdResponse(RcoViewUserEntity userEntity, UserLoginInfoDTO userLoginInfoDTO) {
        if (IacUserTypeEnum.NORMAL != userEntity.getUserType()) {
            LOGGER.debug("当前用户类型为[{}]，不进行用户首次登录或重置密码校验", userEntity.getUserType());
            return false;
        }
        boolean isFirstLogin = checkUserFirstLoginForceUpdatePwd(userEntity, userLoginInfoDTO);
        boolean isResetPwdByAdmin = checkUserResetPwdByAdmin(userEntity, userLoginInfoDTO);
        return isFirstLogin || isResetPwdByAdmin;
    }

    private boolean checkUserFirstLoginForceUpdatePwd(RcoViewUserEntity userEntity, UserLoginInfoDTO userLoginInfoDTO) {
        if (!userEntity.getUserModifyPassword() && certificationStrategyParameterAPI.isNeedForceUpdatePwd()) {
            LOGGER.debug("当前全局策略开启用户首次登录强制修改密码，并且用户[{}]未修改过密码", userEntity.getId());
            userLoginInfoDTO.setForceUpdatePassword(Boolean.TRUE);
            return true;
        }
        return false;
    }

    private boolean checkUserResetPwdByAdmin(RcoViewUserEntity userEntity, UserLoginInfoDTO userLoginInfoDTO) {
        if (userEntity.getResetPasswordByAdmin() && certificationStrategyParameterAPI.isNeedForceUpdatePwd()) {
            LOGGER.debug("当前全局策略开启用户重置密码强制修改密码，并且用户[{}]被重置密码", userEntity.getId());
            userLoginInfoDTO.setUpdatePasswordByReset(Boolean.TRUE);
            return true;
        }
        return false;
    }

    private boolean requestChangePwdInRccm(String terminalId, ChangeUserPasswordDTO changeUserPasswordDTO, UserUpdatePwdDTO userUpdatePwdDto) {
        RccmUnifiedChangePwdResultDTO unifiedChangePwdResultDTO;
        try {
            // RCenter依赖了Shine请求字段，保持跟原有Shine业务请求的接口保持一致
            CbbDispatcherRequest request = buildDispatcherRequest(terminalId, changeUserPasswordDTO);
            unifiedChangePwdResultDTO = userLoginRccmOperationService.requestChangePwdInRccm(request, terminalId, changeUserPasswordDTO);
        } catch (Exception e) {
            LOGGER.error("向rccm发起修改密码验证处理异常,terminalId:[{}],changeUserPasswordDTO:{}", terminalId, JSON.toJSONString(changeUserPasswordDTO), e);
            return false;
        }

        int resultCode = unifiedChangePwdResultDTO.getResultCode();
        LoginResultMessageDTO loginResultMessage = unifiedChangePwdResultDTO.getLoginResultMessage();
        userUpdatePwdDto.setBusinessCode(resultCode);
        if (resultCode == ChangeUserPwdCode.IN_LOCKED) {
            userUpdatePwdDto.setPwdLockTime(loginResultMessage.getPwdLockTime());
        } else if (resultCode == ChangeUserPwdCode.REMIND_ERROR_TIMES) {
            PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
            userUpdatePwdDto.setRemainingTimes(pwdStrategyDTO.getUserLockedErrorTimes() - loginResultMessage.getErrorTimes());
        }
        return true;
    }

    private CbbDispatcherRequest buildDispatcherRequest(String terminalId, ChangeUserPasswordDTO changeUserPasswordDTO) {
        CbbDispatcherRequest dispatcherRequest = new CbbDispatcherRequest();
        dispatcherRequest.setDispatcherKey(ShineAction.CHANGE_TERMINAL_USER_PASSWORD);
        dispatcherRequest.setRequestId(terminalId);
        dispatcherRequest.setNewConnection(Boolean.FALSE);
        dispatcherRequest.setTerminalId(terminalId);
        dispatcherRequest.setData(JSON.toJSONString(changeUserPasswordDTO));
        return dispatcherRequest;
    }

    private UserLoginInfoDTO handleResultWhenUserNotExists(UserLoginRequest request) throws BusinessException {
        UserLoginInfoDTO result = new UserLoginInfoDTO();
        String userName = request.getUserName();
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();

        IacAuthUserDTO authUserRequest = new IacAuthUserDTO(userName, request.getPassword());
        BeanUtils.copyProperties(request, authUserRequest);
        authUserRequest.setSubSystem(SubSystem.CDC);

        IacAuthUserResultDTO baseAuthUserResultDTO = cbbUserAPI.authUser(authUserRequest);
        LOGGER.info("webclient用户不存在登录接口返回，返回信息为：{}", JSON.toJSONString(baseAuthUserResultDTO));

        // 用户不存在时，也要提示错误次数或锁定
        result.setBusinessCode(baseAuthUserResultDTO.getAuthCode());

        if (LoginMessageCode.REMIND_ERROR_TIMES == baseAuthUserResultDTO.getAuthCode()) {
            LOGGER.info("提示用户[{}]锁定次数", userName);
            IacLockInfoDTO userAccountLockInfo = certificationHelper.getUserAccountLockInfo(userName);
            result.setRemainingTimes(pwdStrategyDTO.getUserLockedErrorTimes() - userAccountLockInfo.getFailCount());
            return result;
        }
        if (LoginMessageCode.USER_LOCKED == baseAuthUserResultDTO.getAuthCode()) {
            LOGGER.info("用户[{}]已被锁定", userName);
            result.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
            return result;
        }

        return result;
    }

}
