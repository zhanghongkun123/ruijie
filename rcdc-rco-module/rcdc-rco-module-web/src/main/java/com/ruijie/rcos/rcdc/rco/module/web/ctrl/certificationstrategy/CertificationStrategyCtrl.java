package com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserLockQueryRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserUnLockRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacLockStatus;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacSecurityPolicy;
import com.ruijie.rcos.rcdc.rco.module.def.api.AutoLogonAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.autologon.dto.AutoLogonDTO;
import com.ruijie.rcos.rcdc.rco.module.def.autologon.enums.AutoLogonEnum;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy.request.certificationstrategy.ConfigWindowAutoWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy.response.ConfigWindowAutoWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalIdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * Description: 认证策略Controller
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年02月25日
 *
 * @author zhang.zhiwen
 */
@Api(tags = "本地密码认证策略")
@Controller
@RequestMapping("/rco/certificationStrategy")
public class CertificationStrategyCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationStrategyCtrl.class);

    /**
     * 密码有效期为永久时的默认值
     */
    private static final int FOREVER_DEFAULT_VALUE = -1;


    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private AutoLogonAPI autoLogonAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;


    /**
     * 自动设置Windows本地管理员密码
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "autoLoginWindow/edit", method = RequestMethod.POST)
    public CommonWebResponse<?> configWindowsAutoLogon(ConfigWindowAutoWebRequest request) {
        Assert.notNull(request, "request cannot be null");
        try {
            AutoLogonDTO autoLogonDTO = new AutoLogonDTO();
            autoLogonDTO.setAutoLogonEnum(AutoLogonEnum.AUTO_LOGON);
            autoLogonDTO.setAutoLogon(request.getEnableAutoLogin());
            autoLogonAPI.updateAutoLogonStrategy(autoLogonDTO);
            auditLogAPI.recordLog(CertificationStrategyBusinessKey.RCDC_USER_WINDOW_AUTO_LOGIN_SUCCESS);

            return CommonWebResponse.success(CertificationStrategyBusinessKey.RCDC_USER_WINDOW_AUTO_LOGIN_SUCCESS,
                    new String[] {});
        } catch (BusinessException e) {
            LOGGER.error("自动设置Windows本地管理员密码失败：", e);
            auditLogAPI.recordLog(CertificationStrategyBusinessKey.RCDC_USER_WINDOW_AUTO_LOGIN_FAIL);

            return CommonWebResponse.fail(CertificationStrategyBusinessKey.RCDC_USER_WINDOW_AUTO_LOGIN_FAIL);
        }
    }

    /**
     * 获取自动设置Windows本地管理员密码策略
     *
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "autoLoginWindow/detail", method = RequestMethod.POST)
    public CommonWebResponse<?> getConfigWindowsAutoLogin() {
        boolean enableAutoLogin = autoLogonAPI.getGlobalAutoLogonStrategy(AutoLogonEnum.AUTO_LOGON);

        return CommonWebResponse.success(new ConfigWindowAutoWebResponse(enableAutoLogin));
    }

    /**
     * 解锁锁定用户
     * @param request 请求参数
     *
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("解锁锁定用户")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    @RequestMapping(value = "unLockUser", method = RequestMethod.POST)
    public CommonWebResponse unLockUser(IdWebRequest request) {
        Assert.notNull(request, "request can not be null");
        try {
            String username = userInfoAPI.getUserNameById(request.getId());
            IacUserLockQueryRequest iacUserLockQueryRequest = new IacUserLockQueryRequest();
            iacUserLockQueryRequest.setSubSystem(SubSystem.CDC);
            iacUserLockQueryRequest.setUserName(username);
            iacUserLockQueryRequest.setSecurityPolicy(IacSecurityPolicy.ACCOUNT);
            IacLockInfoDTO lockInfo = iacUserMgmtAPI.getLockInfo(iacUserLockQueryRequest);
            if (IacLockStatus.UN_LOCKED == lockInfo.getLockStatus()) {
                auditLogAPI.recordLog(CertificationStrategyBusinessKey.RCDC_LOCK_USER_UNLOCK_FAIL_WITH_USERNAME, username);
                return CommonWebResponse.fail(CertificationStrategyBusinessKey.RCDC_LOCK_USER_HAS_BEEN_UNLOCKED, new String[] {});
            }
            IacUserUnLockRequest iacUserUnLockRequest = new IacUserUnLockRequest();
            iacUserUnLockRequest.setSubSystem(SubSystem.CDC);
            iacUserUnLockRequest.setUserName(username);
            iacUserUnLockRequest.setSecurityPolicy(IacSecurityPolicy.ACCOUNT);
            iacUserMgmtAPI.unLock(iacUserUnLockRequest);

            certificationStrategyParameterAPI.notifyTerminalUserUnlocked(request.getId());
            auditLogAPI.recordLog(CertificationStrategyBusinessKey.RCDC_LOCK_USER_UNLOCK_SUCCESS_WITH_USERNAME, username);
            return CommonWebResponse.success(CertificationStrategyBusinessKey.RCDC_LOCK_USER_UNLOCK_SUCCESS, new String[] {});
        } catch (Exception e) {
            LOGGER.error("锁定用户解锁失败", e);
            auditLogAPI.recordLog(CertificationStrategyBusinessKey.RCDC_LOCK_USER_UNLOCK_FAIL, e.getMessage());
            return CommonWebResponse.fail(CertificationStrategyBusinessKey.RCDC_LOCK_USER_UNLOCK_FAIL, new String[] {e.getMessage()});
        }
    }

    /**
     * 解锁终端的终端管理密码
     * @param request 终端ID数组
     * @throws BusinessException 业务异常
     * @return 响应结果
     */
    @ApiOperation("解锁锁定终端")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "unlockTerminalMngPwd", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse unlockTerminalMngPwd(TerminalIdArrWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        String terminalId = request.getIdArr()[0];
        userTerminalMgmtAPI.verifyTerminalIdExist(new String[] {terminalId});
        Assert.notNull(terminalId, "terminalId can not be null");
        TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);

        try {
            UUID realTerminalId = terminalDTO.getRealTerminalId();
            boolean isLocked = certificationStrategyParameterAPI.getTerminalLockedStatusById(realTerminalId);
            if (!isLocked) {
                auditLogAPI.recordLog(CertificationStrategyBusinessKey.RCDC_TERMINAL_HAVE_NOT_CLOCK_WITH_NAME,
                        terminalDTO.getUpperMacAddrOrTerminalId());

                return CommonWebResponse.fail(CertificationStrategyBusinessKey.RCDC_TERMINAL_HAVE_NOT_CLOCK, new String[] {});
            }
            certificationStrategyParameterAPI.unlockTerminalManagePwd(terminalId);
            auditLogAPI.recordLog(CertificationStrategyBusinessKey.RCDC_LOCK_TERMINAL_UNLOCK_SUCCESS_WITH_NAME,
                    terminalDTO.getUpperMacAddrOrTerminalId());

            return CommonWebResponse.success(CertificationStrategyBusinessKey.RCDC_LOCK_TERMINAL_UNLOCK_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error(String.format("锁定终端[%s}解锁失败", terminalId), e);
            auditLogAPI.recordLog(CertificationStrategyBusinessKey.RCDC_LOCK_TERMINAL_UNLOCK_FAIL_WITH_NAME,
                    terminalDTO.getUpperMacAddrOrTerminalId());

            return CommonWebResponse.fail(CertificationStrategyBusinessKey.RCDC_LOCK_TERMINAL_UNLOCK_FAIL, new String[] {});
        }
    }

    /**
     * IDV解锁终端的终端管理密码
     * 
     * @param request 终端ID数组
     * @throws BusinessException 业务异常
     * @return 响应结果
     */
    @ApiOperation("解锁锁定终端")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "idv/unlockTerminalMngPwd", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse idvUnlockTerminalMngPwd(TerminalIdArrWebRequest request) throws BusinessException {
        return unlockTerminalMngPwd(request);
    }

    /**
     * soft解锁终端的终端管理密码
     *
     * @param request 终端ID数组
     * @throws BusinessException 业务异常
     * @return 响应结果
     */
    @ApiOperation("解锁锁定终端")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "soft/unlockTerminalMngPwd", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse softUnlockTerminalMngPwd(TerminalIdArrWebRequest request) throws BusinessException {
        return unlockTerminalMngPwd(request);
    }

    /**
     * 参考optional的ofNullable方法重写校验方法
     * @param value
     * @param defaultValue
     * @param <T>
     * @return
     */
    private <T> T checkToGetDefaultValue(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
