package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacLoginAdminRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacUpdateAdminRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacValidateAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserListByUserTypeAndUserRoleRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.common.ThirdPartyLoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.RcoLoginAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpgradeAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAdminPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AdminMgmtService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author xiejian
 */
public class AdminMgmtAPIImpl implements AdminMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminMgmtAPIImpl.class);

    @Autowired
    private AdminMgmtService adminMgmtService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private AdminManageAPI adminManageAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager adminLoginOnTerminalCacheManager;


    @Override
    public void updateAdminDataPermission(UpdateAdminDataPermissionRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        adminMgmtService.updateAdminDataPermission(request);
    }

    @Override
    public void upgradeAdmin(UpgradeAdminRequest upgradeAdminRequest) throws BusinessException {
        Assert.notNull(upgradeAdminRequest, "upgradeAdminRequest is not null");
        // 升级管理员
        adminMgmtService.upgradeAdmin(upgradeAdminRequest);
    }

    @Override
    public IacAdminDTO loginAdmin(RcoLoginAdminRequest rcoLoginRequest) throws BusinessException {
        Assert.notNull(rcoLoginRequest, "rcoLoginRequest must not be null");
        LOGGER.debug("收到登录请求[{}]", JSON.toJSONString(rcoLoginRequest));

        String userName = rcoLoginRequest.getUserName();
        String rawPassword = rcoLoginRequest.getRawPassword();
        Boolean isOnlyValidAdminPwd = rcoLoginRequest.getOnlyValidAdminPwd();
        String loginIp = rcoLoginRequest.getLoginIp();
        SubSystem subSystem = rcoLoginRequest.getSubSystem();

        IacLoginAdminRequest request = new IacLoginAdminRequest();
        request.setLoginName(userName);
        String encAdminPwd = AesUtil.encrypt(rawPassword, RedLineUtil.getRealAdminRedLine());
        request.setPwd(encAdminPwd);
        request.setSubSystem(subSystem);
        if (StringUtils.isBlank(loginIp)) {
            request.setIgnoreIpWhitelistCheck(true);
        } else {
            request.setLoginIp(loginIp);
        }
        // 查询管理员表中是否有该管理员
        IacAdminDTO baseAdminDTO;
        try {
            baseAdminDTO = adminMgmtAPI.getAdminByUserName(userName);
        } catch (BusinessException e) {
            LOGGER.info("管理员[{}]不存在", userName);
            // 不是AD域用户则走本地校验
            if (Objects.equals(Boolean.TRUE, isOnlyValidAdminPwd)) {
                return processValidAdminPwd(request);
            }
            return baseAdminMgmtAPI.loginAdmin(request);
        }

        // 查询同名称的用户名
        IacUserDetailDTO userDetail = cbbUserAPI.getUserByName(userName);
        // 用户是否为管理员，并且为AD域或者LDAP域用户
        boolean isDomainUserAndAdmin = userDetail != null && baseAdminDTO != null && UserRoleEnum.ADMIN.name().equals(userDetail.getUserRole())
                && (userDetail.getUserType() == IacUserTypeEnum.AD || userDetail.getUserType() == IacUserTypeEnum.LDAP
                    || userDetail.getUserType() == IacUserTypeEnum.THIRD_PARTY);
        if (isDomainUserAndAdmin) {
            LOGGER.info("管理员[{}]为域用户，用户验证方法进行校验", userName);
            return authUser(request, rawPassword, isOnlyValidAdminPwd);
        }

        // 不是AD域用户则走本地校验
        if (Objects.equals(Boolean.TRUE, isOnlyValidAdminPwd)) {
            return processValidAdminPwd(request);
        }
        return baseAdminMgmtAPI.loginAdmin(request);
    }

    @Override
    public IacAdminDTO loginAdminNoAuth(RcoLoginAdminRequest rcoLoginRequest) throws BusinessException {
        Assert.notNull(rcoLoginRequest, "rcoLoginRequest must not be null");
        LOGGER.debug("收到登录请求[{}]", JSON.toJSONString(rcoLoginRequest));

        IacLoginAdminRequest request = new IacLoginAdminRequest();
        request.setLoginName(rcoLoginRequest.getUserName());
        // 这里是sha256密文用AES加密后的密文
        request.setPwd(rcoLoginRequest.getRawPassword());
        request.setSubSystem(rcoLoginRequest.getSubSystem());
        request.setDeviceId(UUID.randomUUID().toString());
        String loginIp = rcoLoginRequest.getLoginIp();
        request.setLoginIp(loginIp);
        if (StringUtils.isBlank(loginIp)) {
            request.setIgnoreIpWhitelistCheck(true);
        }
        return baseAdminMgmtAPI.loginAdminNoAuth(request);
    }

    /**
     * AD、LDAP域用户的管理员认证
     *
     * @param request 请求参数
     * @param rawPassword 原始密码
     * @return IacAdminDTO
     * @throws BusinessException 业务异常
     */
    private IacAdminDTO authUser(IacLoginAdminRequest request, String rawPassword, Boolean onlyValidAdminPwd) throws BusinessException {
        String userName = request.getLoginName();
        IacAuthUserDTO cbbAuthUserDTO = new IacAuthUserDTO(userName,
                AesUtil.encrypt(rawPassword, RedLineUtil.getRealUserRedLine()));
        // 终端上登录管理员忽略图形校验码
        cbbAuthUserDTO.setShouldCheckCaptchaCode(false);
        cbbAuthUserDTO.setDeviceId(UUID.randomUUID().toString());
        cbbAuthUserDTO.setSubSystem(SubSystem.CDC);
        IacAuthUserResultDTO authUserResultDTO = cbbUserAPI.authUser(cbbAuthUserDTO);
        int authCode = authUserResultDTO.getAuthCode();
        LOGGER.info("域管理员[{}]校验结果为：[{}]", userName, authCode);

        return handleAuthResult(request, authCode, onlyValidAdminPwd);
    }


    private IacAdminDTO handleAuthResult(IacLoginAdminRequest request, int authCode, Boolean onlyValidAdminPwd) throws BusinessException {
        String userName = request.getLoginName();
        switch (authCode) {
            case CommonMessageCode.SUCCESS:
                return unlockAdmin(request);
            case LoginMessageCode.USERNAME_OR_PASSWORD_ERROR:
            case LoginMessageCode.USER_LOCKED:
            case LoginMessageCode.REMIND_ERROR_TIMES:
            case ThirdPartyLoginMessageCode.USER_THIRD_PARTY_AUTH_ERROR:
                // 第三方用户管理员，同样适用随机数的方式触发登录错误，达到账号锁定效果
                return loginWithRandomPwd(request, onlyValidAdminPwd);
            case LoginMessageCode.AD_ACCOUNT_DISABLE:
                throw new BusinessException(BusinessKey.RCDC_RCO_AAA_ADMIN_IS_DISABLED);
            case LoginMessageCode.AD_LOGIN_LIMIT:
                throw new BusinessException(BusinessKey.RCDC_RCO_AAA_ADMIN_NOT_ALLOW_LOGIN_THIS_TIME);
            case LoginMessageCode.AD_ACCOUNT_EXPIRE:
                throw new BusinessException(BusinessKey.RCDC_RCO_AAA_ADMIN_EXPIRED);
            case ThirdPartyLoginMessageCode.UNABLE_THIRD_PARTY_AUTH:
                throw new BusinessException(BusinessKey.RCDC_RCO_AAA_ADMIN_THIRD_PARTY_CONFIG_UNABLE);
            case ThirdPartyLoginMessageCode.UNABLE_USER_THIRD_PARTY_AUTH:
                throw new BusinessException(BusinessKey.RCDC_RCO_AAA_ADMIN_THIRD_PARTY_USER_IDCONFIG_UNABLE);
            default:
                LOGGER.info("AD域管理员[{}]认证码为：[{}]", userName, authCode);
                throw new BusinessException(BusinessKey.RCDC_RCO_AAA_ADMIN_UNKNOWN_EXCEPTION);
        }

    }

    /**
     * 解锁管理员
     * 
     * @param request 请求
     * @return IacAdminDTO
     * @throws BusinessException 业务异常
     */
    private IacAdminDTO unlockAdmin(IacLoginAdminRequest request) throws BusinessException {
        String userName = request.getLoginName();
        // 校验成功，解锁管理员
        IacAdminDTO adminDTO = adminMgmtAPI.getAdminByUserName(userName);
        GetAdminPasswordResponse adminPasswordResponse = adminManageAPI.getAdminPassword(new IdRequest(adminDTO.getId()));
        // 平台没有提供删除管理员密码错误次数的接口，目前值能从数据库中获取密码，然后调用登录接口进行解锁
        request.setPwd(adminPasswordResponse.getPassword());
        return baseAdminMgmtAPI.loginAdminNoAuth(request);
    }

    /**
     * 密码错误，生成随机密码增加密码错误次数
     *
     * @param request 请求
     * @return IacAdminDTO
     * @throws BusinessException 业务异常
     */
    private IacAdminDTO loginWithRandomPwd(IacLoginAdminRequest request, Boolean onlyValidAdminPwd) throws BusinessException {
        if (Objects.equals(Boolean.TRUE, onlyValidAdminPwd)) {
            return processValidAdminPwd(request);
        }

        request.setPwd(UUID.randomUUID().toString());
        return baseAdminMgmtAPI.loginAdmin(request);
    }

    @Override
    public void modifyAdminStatus(IacUserTypeEnum userType) throws BusinessException {
        Assert.notNull(userType, "userType must not be null");
        IacUserListByUserTypeAndUserRoleRequest iacUserListByUserTypeAndUserRoleRequest = new IacUserListByUserTypeAndUserRoleRequest();
        iacUserListByUserTypeAndUserRoleRequest.setUserType(userType);
        iacUserListByUserTypeAndUserRoleRequest.setUserRole(UserRoleEnum.ADMIN.name());
        List<IacUserDetailDTO> userDetailDTOList = cbbUserAPI.getUserListByUserTypeAndUserRole(iacUserListByUserTypeAndUserRoleRequest);
        for (IacUserDetailDTO userDetailDTO : userDetailDTOList) {
            try {
                syncUserStatusToAdmin(userDetailDTO);
            } catch (BusinessException e) {
                LOGGER.info(String.format("同步[%s]域用户[%s]状态失败，失败原因：", userType, userDetailDTO.getUserName()), e);
            }
        }
    }

    @Override
    public void syncUserStatusToAdmin(IacUserDetailDTO userDetail) throws BusinessException {
        Assert.notNull(userDetail, "userDetail must not be null");

        String userName = userDetail.getUserName();
        IacUserTypeEnum userType = userDetail.getUserType();

        // 用户是否为管理员
        boolean isUserAdmin = UserRoleEnum.ADMIN.name().equals(userDetail.getUserRole());
        if (!isUserAdmin) {
            LOGGER.debug("[{}]域用户[{}}不是管理员，无需修改管理员禁用状态", userType, userName);
            return;
        }

        LOGGER.info("[{}]域用户[{}}是管理员，需修改管理员禁用状态", userType, userName);
        IacAdminDTO baseAdminDTO;
        try {
            baseAdminDTO = adminMgmtAPI.getAdminByUserName(userName);
        } catch (BusinessException e) {
            LOGGER.info("[{}]域用户[{}}管理员信息不存在，无需修改管理员禁用状态", userType, userName);
            return;
        }

        // 同步管理员信息到身份中心
        LOGGER.info("同步[{}]域用户[{}}管理员信息", userType, userName);
        IacUpdateAdminRequest updateAdminRequest = new IacUpdateAdminRequest();
        BeanUtils.copyProperties(baseAdminDTO, updateAdminRequest);
        boolean enabled = userDetail.getUserState() == IacUserStateEnum.ENABLE;
        updateAdminRequest.setEnabled(enabled);
        updateAdminRequest.setUserType(IacUserTypeEnum.getUserDomainType(userDetail.getUserType()));
        baseAdminMgmtAPI.updateAdmin(updateAdminRequest);

    }

    private IacAdminDTO processValidAdminPwd(IacLoginAdminRequest request) throws BusinessException {
        IacValidateAdminPwdRequest validRequest = new IacValidateAdminPwdRequest();
        validRequest.setUserName(request.getLoginName());
        validRequest.setPwd(request.getPwd());
        validRequest.setKey(RedLineUtil.getRealAdminRedLine());
        validRequest.setSubSystem(SubSystem.CDC);
        baseAdminMgmtAPI.validateAdminPwd(validRequest);
        return adminMgmtAPI.getAdminByUserName(request.getLoginName());
    }

    @Override
    public UUID preLoginTerminalAndGetSessionId(UUID adminId, String terminalId) throws BusinessException {
        Assert.notNull(adminId, "adminId must not be null");
        Assert.hasText(terminalId, "terminalId must not be null");

        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(adminId);
        return adminLoginOnTerminalCacheManager.add(baseAdminDTO, terminalId);
    }

    @Override
    public IacAdminDTO getAdminByUserName(String userName) throws BusinessException {
        Assert.hasText(userName, "userName must not be null");
        return baseAdminMgmtAPI.getAdminByUserName(userName,SubSystem.CDC);
    }
}
