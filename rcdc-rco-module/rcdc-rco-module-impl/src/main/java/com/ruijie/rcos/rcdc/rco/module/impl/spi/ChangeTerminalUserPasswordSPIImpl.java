package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import static com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_USER_CHANGE_PWD_FAIL;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.ModifyPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ModifyPasswordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoIacPasswordBlacklistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ChangeUserPwdCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ChangeUserPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.maintenance.MaintenanceModeValidator;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.RccmUnifiedChangePwdResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 修改终端用户密码
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author Jarman
 */
@DispatcherImplemetion(ShineAction.CHANGE_TERMINAL_USER_PASSWORD)
public class ChangeTerminalUserPasswordSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeTerminalUserPasswordSPIImpl.class);

    @Autowired
    protected UserService userService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private MaintenanceModeValidator maintenanceModeValidator;

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private ModifyPasswordAPI modifyPasswordAPI;

    @Autowired
    private ChangeUserPwdHelper changeUserPwdHelper;

    @Autowired
    private UserLoginRccmOperationService userLoginRccmOperationService;

    @Autowired
    private RcoIacPasswordBlacklistService rcoIacPasswordBlacklistService;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");

        boolean isUnderMaintenance = maintenanceModeValidator.validate();
        if (isUnderMaintenance) {
            LOGGER.info("处于维护模式下，无法修改终端密码");
            maintenanceModeValidator.responseUnderMaintenanceMessage(request, ChangeUserPwdCode.IN_MAINTENANCE, StringUtils.EMPTY);
            return;
        }

        String terminalId = request.getTerminalId();
        String data = request.getData();
        LOGGER.info("修改终端用户密码:terminalId:{};data:{}", request.getTerminalId(), data);
        ChangeUserPasswordDTO changeUserPasswordDTO;
        try {
            changeUserPasswordDTO = shineMessageHandler.parseObject(data, ChangeUserPasswordDTO.class);
        } catch (Exception e) {
            LOGGER.error("解析修改终端用户密码报文失败，请检查报文格式是否正确", e);
            return;
        }
        String userName = changeUserPasswordDTO.getUserName();

        // 检测用户名和密码一致返回错误
        if (changeUserPwdHelper.pwdLikeName(changeUserPasswordDTO.getNewPassword(), changeUserPasswordDTO.getUserName())) {
            responseMessage(request, ChangeUserPwdCode.NAME_LIKE_PASWARD, userName);
            return;
        }

        IacUserDetailDTO userInfoDTO = null;
        try {
            userInfoDTO = cbbUserAPI.getUserByName(userName);
        } catch (BusinessException e) {
            LOGGER.error("get username fail", e);
        }

        // 判断rcdc是否开启统一登录
        if (userLoginRccmOperationService.isUnifiedLoginOn(terminalId)) {
            if (requestChangePwdInRccm(request, terminalId, changeUserPasswordDTO)) {
                return;
            }
            // 调rccm报错走单机逻辑
            LOGGER.error("调rccm报错走单机逻辑，request=[{}]", JSON.toJSONString(request));
        }

        if (userInfoDTO == null) {
            LOGGER.info("数据库找不到用户[{}]", userName);
            responseMessage(request, ChangeUserPwdCode.NOT_THIS_USER, userName);
            return;
        }
        if (userInfoDTO.getUserType() == IacUserTypeEnum.AD) {
            LOGGER.info("AD域用户[{}]不允许修改密码", userName);
            responseMessage(request, ChangeUserPwdCode.AD_USER_NOT_ALLOW_CHANGE_PASSWORD, userName);
            return;
        }
        if (userInfoDTO.getUserType() == IacUserTypeEnum.LDAP) {
            LOGGER.info("LDAP用户[{}]不允许修改密码", userName);
            responseMessage(request, ChangeUserPwdCode.LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD, userName);
            return;
        }

        if (userInfoDTO.getUserType() == IacUserTypeEnum.VISITOR) {
            LOGGER.info("访客用户[{}]不允许修改密码", userName);
            responseMessage(request, ChangeUserPwdCode.VISITOR_USER_NOT_ALLOW_CHANGE_PASSWORD, userName);
            return;
        }

        if (userInfoDTO.getUserType() == IacUserTypeEnum.THIRD_PARTY) {
            LOGGER.info("第三方用户[{}]不允许修改密码", userName);
            responseMessage(request, ChangeUserPwdCode.THIRD_PARTY_USER_NOT_ALLOW_CHANGE_PASSWORD, userName);
            return;
        }

        // 检查newPwd是否符合配置复杂度要求
        if (!changeUserPwdHelper.checkNwdPwd(changeUserPasswordDTO.getNewPassword(), ApiCallerTypeEnum.INNER)) {
            LOGGER.info("用户[{}]新密码不符合密码复杂度要求", userName);
            responseMessage(request, ChangeUserPwdCode.CHANGE_PASSWORD_UNABLE_REQUIRE, userName);
            return;
        }

        // 开启防暴力破解并且用户需要处于锁定状态，提前返回
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        RcoViewUserEntity userEntity = userService.getUserInfoByName(userName);
        if (pwdStrategyDTO.getPreventsBruteForce() && certificationHelper.isLocked(userName)) {
            responseContent(request, ChangeUserPwdCode.IN_LOCKED, userName);
            return;
        }

        // 校验oldPwd是否正确
        if (changeUserPwdHelper.checkOldPwd(userInfoDTO.getUserName(), changeUserPasswordDTO.getOldPassword(), ApiCallerTypeEnum.INNER)) {
            // 弱密码校验
            if (rcoIacPasswordBlacklistService.isPasswordBlackList(changeUserPwdHelper.descUserPwd(changeUserPasswordDTO.getNewPassword(),
                    ApiCallerTypeEnum.INNER)))  {
                responseMessage(request, ChangeUserPwdCode.WEAK_PWD, userName);
                return;
            }

            // 修改用户密码,shine发送来的密码已经是加密过的
            ModifyPasswordDTO modifyPasswordDTO = changeUserPwdHelper.buildModifyPasswordDTO(userEntity.getId(),
                    changeUserPasswordDTO.getNewPassword(), changeUserPasswordDTO.getOldPassword(), ApiCallerTypeEnum.INNER);

            boolean isSuccess = false;
            try {
                isSuccess = modifyPasswordAPI.modifUserPwdSyncAdminPwd(modifyPasswordDTO);
                LOGGER.info("修改用户[{}]密码是否成功：[{}]", userName, isSuccess);
            } catch (BusinessException e) {
                LOGGER.error(String.format("修改用户[%s]密码失败", userName), e);
                String errorMsg = e.getAttachment(UserTipContainer.Constants.USER_TIP, e.getI18nMessage());
                auditLogAPI.recordLog(RCDC_RCO_USER_CHANGE_PWD_FAIL, userName, errorMsg);
            }
            int resultCode = isSuccess ? ChangeUserPwdCode.SUCCESS : ChangeUserPwdCode.CODE_ERR_OTHER;
            responseMessage(request, resultCode, userName);
            userInfoAPI.sendUserPasswordChange(userInfoDTO.getId(), terminalId);
            return;
        }
        // 旧密码错误
        LOGGER.info("修改用户[{}]密码失败，原因：原密码错误", userName);

        // 判断错误次数是否超出，是否需要提示剩余次数，超出就锁定
        int responseCode;
        if (pwdStrategyDTO.getPreventsBruteForce()
                && (responseCode = changeUserPwdHelper.checkNeedRemindOrLock(userEntity.getUserName())) != ChangeUserPwdCode.OLD_PASSWORD_ERROR) {
            responseContent(request, responseCode, userName);
            return;
        }
        responseMessage(request, ChangeUserPwdCode.OLD_PASSWORD_ERROR, userName);
    }

    private boolean requestChangePwdInRccm(CbbDispatcherRequest request, String terminalId,
                                                   ChangeUserPasswordDTO changeUserPasswordDTO) {
        RccmUnifiedChangePwdResultDTO unifiedChangePwdResultDTO;

        try {
            unifiedChangePwdResultDTO =
                    userLoginRccmOperationService.requestChangePwdInRccm(request, terminalId, changeUserPasswordDTO);
        } catch (Exception e) {
            LOGGER.error("向RCenter发起修改密码验证处理异常,terminalId:[{}],changeUserPasswordDTO:[{}]",
                    terminalId, JSON.toJSONString(changeUserPasswordDTO), e);
            return false;
        }

        int resultCode = unifiedChangePwdResultDTO.getResultCode();
        if (resultCode == ChangeUserPwdCode.IN_LOCKED ||
            resultCode == ChangeUserPwdCode.REMIND_ERROR_TIMES) {
            responseContentFromRccm(request, resultCode, changeUserPasswordDTO.getUserName(), unifiedChangePwdResultDTO);
        } else {
            responseMessage(request, unifiedChangePwdResultDTO.getResultCode(), changeUserPasswordDTO.getUserName());
        }

        return true;
    }


    private void responseMessage(CbbDispatcherRequest request, int responseCode, String userName) {
        try {
            LOGGER.info("应答修改用户[{}]密码报文,terminalId={}，应答状态码{}", userName, request.getTerminalId(), responseCode);
            shineMessageHandler.response(request, responseCode);
        } catch (Exception e) {
            LOGGER.error("应答修改用户[" + userName + "]密码失败，terminalId=" + request.getTerminalId() + ";应答状态码：" + responseCode, e);
        }
    }

    private void responseContent(CbbDispatcherRequest request, int responseCode, String userName) {
        LoginResultMessageDTO loginResultMessage = certificationHelper.buildPwdStrategyResult(responseCode, userName);
        try {
            LOGGER.info("应答修改用户[{}]密码报文,terminalId={}，应答状态码{}, 响应消息：[{}]", userName, request.getTerminalId(), responseCode,
                    JSON.toJSONString(loginResultMessage));
            shineMessageHandler.responseContent(request, responseCode, loginResultMessage);
        } catch (Exception e) {
            LOGGER.error("应答修改用户[" + userName + "]密码失败，terminalId=" + request.getTerminalId() + ";应答状态码：" + responseCode, e);
        }
    }

    private void responseContentFromRccm(CbbDispatcherRequest request, int responseCode,
                                         String userName, RccmUnifiedChangePwdResultDTO unifiedChangePwdResultDTO) {
        LoginResultMessageDTO loginResultMessage = unifiedChangePwdResultDTO.getLoginResultMessage();
        try {
            LOGGER.info("应答修改用户[{}]密码报文，terminalId[{}]，应答状态码[{}]，响应消息[{}]", userName, request.getTerminalId(), responseCode,
                    JSON.toJSONString(loginResultMessage));
            shineMessageHandler.responseContent(request, responseCode, loginResultMessage);
        } catch (Exception e) {
            LOGGER.error("应答修改用户[{}]密码失败，terminalId[{}]，应答状态码[{}]", userName, request.getTerminalId(), responseCode, e);
        }
    }
}
