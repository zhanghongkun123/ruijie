package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.api;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.ModifyPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ModifyPasswordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserUnifiedLoginAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedChangePwdResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedLoginResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.ChangeUserPwdHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ChangeUserPwdCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ChangeUserPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.maintenance.MaintenanceModeValidator;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.RcoUserLoginService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 *
 * Description: rcdc统一登录API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class UserUnifiedLoginAPIImpl implements UserUnifiedLoginAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserUnifiedLoginAPIImpl.class);

    @Autowired
    private RcoUserLoginService rcoUserLoginService;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private MaintenanceModeValidator maintenanceModeValidator;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private ChangeUserPwdHelper changeUserPwdHelper;

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    protected RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private ModifyPasswordAPI modifyPasswordAPI;

    @Override
    public UnifiedLoginResultDTO unifiedUserLoginValidate(CbbDispatcherRequest dispatcherRequest) {
        Assert.notNull(dispatcherRequest, "dispatcherRequest不能为null");
        LOGGER.info("rcdc统一登录，CbbDispatcherRequest：{}", JSON.toJSONString(dispatcherRequest));

        ShineLoginResponseDTO shineLoginResponseDTO = rcoUserLoginService.userLoginValidate(dispatcherRequest);

        UnifiedLoginResultDTO unifiedLoginResultDTO = new UnifiedLoginResultDTO();
        BeanUtils.copyProperties(shineLoginResponseDTO, unifiedLoginResultDTO);


        LOGGER.info("rcdc统一登录结果，UnifiedLoginResultDTO：{}", JSON.toJSONString(unifiedLoginResultDTO));
        return unifiedLoginResultDTO;
    }

    @Override
    public UnifiedLoginResultDTO collectUserLoginValidate(CbbDispatcherRequest dispatcherRequest) {
        Assert.notNull(dispatcherRequest, "dispatcherRequest不能为null");
        LOGGER.info("rcdc统一登录，CbbDispatcherRequest：{}", JSON.toJSONString(dispatcherRequest));

        ShineLoginResponseDTO shineLoginResponseDTO = rcoUserLoginService.collectUserLoginValidate(dispatcherRequest);

        UnifiedLoginResultDTO unifiedLoginResultDTO = new UnifiedLoginResultDTO();
        BeanUtils.copyProperties(shineLoginResponseDTO, unifiedLoginResultDTO);


        LOGGER.info("rcdc统一登录结果，UnifiedLoginResultDTO：{}", JSON.toJSONString(unifiedLoginResultDTO));
        return unifiedLoginResultDTO;
    }

    @Override
    public Boolean validateUnifiedLoginAuth(UUID clusterId) {
        Assert.notNull(clusterId, "clusterId不能为null");

        if (!rccmManageService.isUnifiedLogin()) {
            return false;
        }

        RccmServerConfigDTO serverConfigDTO = rccmManageService.getRccmServerConfig();
        Assert.notNull(serverConfigDTO, "serverConfigDTO不能为null");
        return Objects.equals(serverConfigDTO.getClusterId(), clusterId);
    }

    @Override
    public UnifiedChangePwdResultDTO unifiedChangePwdValidate(CbbDispatcherRequest dispatcherRequest) {
        Assert.notNull(dispatcherRequest, "dispatcherRequest can not be null");

        boolean isUnderMaintenance = maintenanceModeValidator.validate();
        if (isUnderMaintenance) {
            LOGGER.info("处于维护模式下，无法修改终端密码");
            return getResult(ChangeUserPwdCode.IN_MAINTENANCE, StringUtils.EMPTY);
        }

        String data = dispatcherRequest.getData();
        LOGGER.info("修改终端用户密码:terminalId:[{}];data:[{}]", dispatcherRequest.getTerminalId(), data);
        ChangeUserPasswordDTO changeUserPasswordDTO;
        try {
            changeUserPasswordDTO = shineMessageHandler.parseObject(data, ChangeUserPasswordDTO.class);
        } catch (Exception e) {
            LOGGER.error("解析修改终端用户密码报文失败，请检查报文格式是否正确", e);
            return getResult(ChangeUserPwdCode.CODE_ERR_OTHER, StringUtils.EMPTY);
        }

        String userName = changeUserPasswordDTO.getUserName();
        IacUserDetailDTO userInfoDTO;
        try {
            userInfoDTO = cbbUserAPI.getUserByName(userName);
        } catch (BusinessException e) {
            LOGGER.error("获取用户[" + userName + "]失败", e);
            return getResult(ChangeUserPwdCode.NOT_THIS_USER, userName);
        }
        if (userInfoDTO == null) {
            LOGGER.info("数据库找不到用户[{}]", userName);
            return getResult(ChangeUserPwdCode.NOT_THIS_USER, userName);
        }
        if (userInfoDTO.getUserType() == IacUserTypeEnum.AD) {
            LOGGER.info("AD域用户[{}]不允许修改密码", userName);
            return getResult(ChangeUserPwdCode.AD_USER_NOT_ALLOW_CHANGE_PASSWORD, userName);
        }
        if (userInfoDTO.getUserType() == IacUserTypeEnum.LDAP) {
            LOGGER.info("LDAP用户[{}]不允许修改密码", userName);
            return getResult(ChangeUserPwdCode.LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD, userName);
        }

        if (userInfoDTO.getUserType() == IacUserTypeEnum.VISITOR) {
            LOGGER.info("访客用户[{}]不允许修改密码", userName);
            return getResult(ChangeUserPwdCode.VISITOR_USER_NOT_ALLOW_CHANGE_PASSWORD, userName);
        }

        // 检查newPwd是否符合配置复杂度要求
        if (!changeUserPwdHelper.checkNwdPwd(changeUserPasswordDTO.getNewPassword(), ApiCallerTypeEnum.INNER)) {
            LOGGER.info("用户[{}]新密码不符合密码复杂度要求", userName);
            return getResult(ChangeUserPwdCode.CHANGE_PASSWORD_UNABLE_REQUIRE, userName);
        }

        // 开启防暴力破解并且用户需要处于锁定状态，提前返回
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        if (pwdStrategyDTO.getPreventsBruteForce() && certificationHelper.isLocked(userName)) {
            return getResultWithContent(ChangeUserPwdCode.IN_LOCKED, userName);
        }

        // 校验oldPwd是否正确
        if (changeUserPwdHelper.checkOldPwd(userInfoDTO.getUserName(), changeUserPasswordDTO.getOldPassword(), ApiCallerTypeEnum.INNER)) {
            // 修改用户密码
            ModifyPasswordDTO modifyPasswordDTO = changeUserPwdHelper.buildModifyPasswordDTO(userInfoDTO.getId(),
                    changeUserPasswordDTO.getNewPassword(), changeUserPasswordDTO.getOldPassword(), ApiCallerTypeEnum.INNER);
            boolean isSuccess = false;
            try {
                isSuccess = modifyPasswordAPI.modifUserPwdSyncAdminPwd(modifyPasswordDTO);
                LOGGER.info("修改用户[{}]密码是否成功：[{}]", userName, isSuccess);
            } catch (BusinessException e) {
                LOGGER.error("修改用户[{}]密码同步管理员密码失败", userName, e);
            }
            int resultCode = isSuccess ? ChangeUserPwdCode.SUCCESS : ChangeUserPwdCode.CODE_ERR_OTHER;
            return getResult(resultCode, userName);
        }
        // 旧密码错误
        LOGGER.info("修改用户[{}]密码失败，原因：原密码错误", userName);

        // 判断错误次数是否超出，是否需要提示剩余次数，超出就锁定
        int responseCode;
        if (pwdStrategyDTO.getPreventsBruteForce()
                && (responseCode = changeUserPwdHelper.checkNeedRemindOrLock(userName)) != ChangeUserPwdCode.OLD_PASSWORD_ERROR) {
            return getResultWithContent(responseCode, userName);
        }
        return getResult(ChangeUserPwdCode.OLD_PASSWORD_ERROR, userName);
    }

    private UnifiedChangePwdResultDTO getResult(int responseCode, String userName) {
        UnifiedChangePwdResultDTO resultDTO = new UnifiedChangePwdResultDTO(responseCode);
        LOGGER.info("应答修改用户[{}]密码报文，应答状态码[{}]", userName, responseCode);
        return resultDTO;
    }

    private UnifiedChangePwdResultDTO getResultWithContent(int responseCode, String userName) {
        UnifiedChangePwdResultDTO resultDTO = new UnifiedChangePwdResultDTO(responseCode);
        LoginResultMessageDTO loginResultMessage = certificationHelper.buildPwdStrategyResult(responseCode, userName);
        resultDTO.setLoginResultMessage(loginResultMessage);
        LOGGER.info("应答修改用户[{}]密码报文, 应答状态码[{}], 响应消息：[{}]", userName, responseCode,
                JSON.toJSONString(loginResultMessage));
        return resultDTO;
    }

}
