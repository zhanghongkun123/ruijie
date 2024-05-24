package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacLastLoginInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.RcoAdGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.common.RcoInvalidTimeHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.NameAndPwdCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessServiceFactory;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.RcoUserLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;


/**
 *
 * Description: 多集群统一登录Service
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
@Service
public class RcoUserLoginServiceImpl implements RcoUserLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoUserLoginServiceImpl.class);

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private LoginBusinessServiceFactory loginBusinessServiceFactory;

    @Autowired
    private RcoAdGroupService rcoAdGroupService;

    @Autowired
    private RcoInvalidTimeHelper invalidTimeUtil;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Override
    public ShineLoginResponseDTO userLoginValidate(CbbDispatcherRequest dispatcherRequest) {
        Assert.notNull(dispatcherRequest, "dispatcherRequest不能为null");
        Assert.hasText(dispatcherRequest.getData(), "dispatcherRequest.data不能为null");
        Assert.hasText(dispatcherRequest.getTerminalId(), "dispatcherRequest.terminalId不能为null");

        String terminalId = dispatcherRequest.getTerminalId();


        try {
            LoginBusinessService loginBusinessService = loginBusinessServiceFactory.getLoginBusinessService(dispatcherRequest.getDispatcherKey());

            ShineLoginDTO shineLoginDTO = shineMessageHandler.parseObject(dispatcherRequest.getData(), ShineLoginDTO.class);

            IacAuthUserResultDTO cbbAuthUserResultDTO = checkUserNameAndPassword(terminalId, shineLoginDTO, loginBusinessService);

            if (LoginMessageCode.SUCCESS != cbbAuthUserResultDTO.getAuthCode()) {
                return loginBusinessService.responseLoginFail(cbbAuthUserResultDTO);
            }
            IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(cbbAuthUserResultDTO.getUserName());
            if (Objects.isNull(cbbUserDetailDTO)) {
                LOGGER.error("用户[{}]不存在", cbbAuthUserResultDTO.getUserName());
                ShineLoginResponseDTO shineLoginResponseDTO = new ShineLoginResponseDTO();
                shineLoginResponseDTO.setAuthResultCode(LoginMessageCode.USERNAME_OR_PASSWORD_ERROR);
                return shineLoginResponseDTO;
            }
            updateUserLastLoginInfo(cbbUserDetailDTO, shineLoginDTO);
            return loginBusinessService.responseSuccess(cbbUserDetailDTO, cbbAuthUserResultDTO);
        } catch (Exception e) {
            LOGGER.error(String.format("用户统一登录校验异常, CbbDispatcherRequest[%s]", JSON.toJSONString(dispatcherRequest)), e);
            ShineLoginResponseDTO shineLoginResponseDTO = new ShineLoginResponseDTO();
            shineLoginResponseDTO.setAuthResultCode(CommonMessageCode.CODE_ERR_OTHER);
            return shineLoginResponseDTO;
        }
    }

    @Override
    public ShineLoginResponseDTO collectUserLoginValidate(CbbDispatcherRequest dispatcherRequest) {
        Assert.notNull(dispatcherRequest, "dispatcherRequest不能为null");
        Assert.hasText(dispatcherRequest.getData(), "dispatcherRequest.data不能为null");
        Assert.hasText(dispatcherRequest.getTerminalId(), "dispatcherRequest.terminalId不能为null");

        String terminalId = dispatcherRequest.getTerminalId();
        try {
            LoginBusinessService loginBusinessService = loginBusinessServiceFactory.getLoginBusinessService(dispatcherRequest.getDispatcherKey());

            ShineLoginDTO shineLoginDTO = shineMessageHandler.parseObject(dispatcherRequest.getData(), ShineLoginDTO.class);

            IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(shineLoginDTO.getUserName());
            //走安全组逻辑,匹配上获取用户信息
            if (Objects.isNull(cbbUserDetailDTO)) {
                if (rcoAdGroupService.checkUserAdGroupResult(shineLoginDTO.getUserName())) {
                    cbbUserDetailDTO = cbbUserAPI.getUserByName(shineLoginDTO.getUserName());
                } else {
                    return buildLoginFailResult(LoginMessageCode.USERNAME_OR_PASSWORD_ERROR);
                }
            }
            // 用户存在且不是cas认证则获取用户信息，如果是LDAP/AD域用户且账户正常，记录登录时间，返回登录成功，否则返回登录失败。
            boolean isCasLogin = Objects.equals(dispatcherRequest.getDispatcherKey(), ShineAction.CAS_ACTION_LOGIN);
            boolean isDomainUser = cbbUserDetailDTO.getUserType() == IacUserTypeEnum.AD || cbbUserDetailDTO.getUserType() == IacUserTypeEnum.LDAP;
            if (!isCasLogin && isDomainUser) {
                return checkAdUserResult(loginBusinessService, shineLoginDTO, cbbUserDetailDTO, loginBusinessService, terminalId);
            }
            IacAuthUserResultDTO cbbAuthUserResultDTO = checkUserNameAndPassword(terminalId, shineLoginDTO, loginBusinessService);
            if (LoginMessageCode.SUCCESS != cbbAuthUserResultDTO.getAuthCode()) {
                return loginBusinessService.responseLoginFail(cbbAuthUserResultDTO);
            }
            updateUserLastLoginInfo(cbbUserDetailDTO, shineLoginDTO);
            return loginBusinessService.responseSuccess(cbbUserDetailDTO, cbbAuthUserResultDTO);
        } catch (Exception e) {
            LOGGER.error(String.format("用户统一登录校验异常, CbbDispatcherRequest[%s]", JSON.toJSONString(dispatcherRequest)), e);
            return buildLoginFailResult(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    private ShineLoginResponseDTO checkAdUserResult(LoginBusinessService loginBusinessService, ShineLoginDTO shineLoginDTO,
                                                    IacUserDetailDTO cbbUserDetailDTO, LoginBusinessService businessService,
                                                    String terminalId) throws BusinessException {
        int resultCode = businessService.processLogin(terminalId, cbbUserDetailDTO);
        // 认证不通过直接返回
        if (CommonMessageCode.SUCCESS != resultCode) {
            LOGGER.info("用户[{}]登录认证失败，返回认证码：[{}]", shineLoginDTO.getUserName(), resultCode);
            return buildLoginFailResult(resultCode);
        }
        Integer authCode = checkAdUser(cbbUserDetailDTO.getUserName(), cbbUserDetailDTO);
        if (CommonMessageCode.SUCCESS != authCode) {
            LOGGER.info("用户[{}]登录认证失败，返回认证码：[{}]", shineLoginDTO.getUserName(), resultCode);
            return buildLoginFailResult(resultCode);
        }
        Boolean isAccountInvalid = invalidTimeUtil.isAccountInvalid(cbbUserDetailDTO);
        if (Boolean.TRUE.equals(isAccountInvalid)) {
            return buildLoginFailResult(LoginMessageCode.ACCOUNT_INVALID);
        }
        updateUserLastLoginInfo(cbbUserDetailDTO, shineLoginDTO);
        return buildLoginSuccessResult(shineLoginDTO, loginBusinessService, cbbUserDetailDTO);

    }

    private Integer checkAdUser(String userName, IacUserDetailDTO userEntity) {
        if ((IacUserTypeEnum.AD == userEntity.getUserType() || IacUserTypeEnum.LDAP == userEntity.getUserType())) {
            //域账户禁用且开启域同步的话，会去域服务器获取是否禁用，否则直接返回禁用
            if (IacUserStateEnum.DISABLE == userEntity.getUserState() && Boolean.FALSE.equals(userEntity.getEnableDomainSync())) {
                LOGGER.info("用户[{}]已被禁用", userName);
                return LoginMessageCode.AD_ACCOUNT_DISABLE;
            }
            // 不是AD域用户不需要验证域用户状态
            if (IacUserTypeEnum.AD != userEntity.getUserType()) {
                return LoginMessageCode.SUCCESS;
            }
            IacDomainUserDTO adUser = cbbAdMgmtAPI.getAdUser(userName);
            if (adUser == null) {
                //AD域服务器异常、或者不存在用户，走本地验证
                return checkLocalUser(userEntity);
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

    private int checkLocalUser(IacUserDetailDTO userEntity) {
        if (IacUserStateEnum.DISABLE == userEntity.getUserState()) {
            return LoginMessageCode.AD_ACCOUNT_DISABLE;
        }
        if (userEntity.getAccountExpireDate() != null && userEntity.getAccountExpireDate() != 0) {
            Date accountExpireDate = DateUtil.adDomainTimestampToDate(userEntity.getAccountExpireDate());
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

    private void updateUserLastLoginInfo(IacUserDetailDTO request, ShineLoginDTO shineLoginDTO) throws BusinessException {
        IacLastLoginInfoDTO cbbLastLoginInfoDTO = new IacLastLoginInfoDTO();
        cbbLastLoginInfoDTO.setLastLoginTerminalTime(new Date());
        cbbLastLoginInfoDTO.setId(request.getId());
        if (StringUtils.isNotBlank(shineLoginDTO.getPassword())) {
            cbbLastLoginInfoDTO.setPassword(shineLoginDTO.getPassword());
        }
        cbbUserAPI.updateLastLoginInfo(cbbLastLoginInfoDTO);
    }

    private ShineLoginResponseDTO buildLoginSuccessResult(ShineLoginDTO shineLoginDTO, LoginBusinessService loginBusinessService,
                                                          IacUserDetailDTO cbbUserDetailDTO) {
        IacAuthUserResultDTO cbbAuthUserResultDTO = new IacAuthUserResultDTO(LoginMessageCode.SUCCESS);
        cbbAuthUserResultDTO.setUserName(shineLoginDTO.getUserName());
        return loginBusinessService.responseSuccess(cbbUserDetailDTO, cbbAuthUserResultDTO);
    }

    private ShineLoginResponseDTO buildLoginFailResult(int authResultCode) {
        ShineLoginResponseDTO shineLoginResponseDTO = new ShineLoginResponseDTO();
        shineLoginResponseDTO.setAuthResultCode(authResultCode);
        return shineLoginResponseDTO;
    }

    private IacAuthUserResultDTO checkUserNameAndPassword(String terminalId, ShineLoginDTO shineLoginDTO,
                                                          LoginBusinessService loginBusinessService) {
        NameAndPwdCheckDTO checkDTO = new NameAndPwdCheckDTO();
        checkDTO.setTerminalId(terminalId);
        checkDTO.setUserName(shineLoginDTO.getUserName());
        checkDTO.setPassword(shineLoginDTO.getPassword());
        checkDTO.setHasResetErrorTimes(BooleanUtils.isFalse(shineLoginDTO.getHasResetErrorTimes()));
        checkDTO.setNeedAssistAuth(Boolean.FALSE);
        // RCenter转发的认证忽略图形校验码验证
        checkDTO.setShouldCheckCaptchaCode(false);
        return loginBusinessService.checkUserNameAndPassword(checkDTO);
    }

}
