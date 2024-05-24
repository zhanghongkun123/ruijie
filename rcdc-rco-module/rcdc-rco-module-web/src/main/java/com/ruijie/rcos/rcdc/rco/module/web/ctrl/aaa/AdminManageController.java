package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.base.iac.module.enums.AuthUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.gss.base.iac.module.dto.IacLoginUserDTO;
import com.ruijie.rcos.gss.base.iac.module.enums.AssistAuthTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAuthTypeMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPermissionMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSecurityStrategyAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.IacThirdPartyCertificationRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacAdminLockQueryRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacLoginAdminRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacModifyAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacModifyOtherAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacUpdateAdminRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacValidateAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacValidateAdminUserNameExistRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacVerifyAdminMfaRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacCheckCommonDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacMfaConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.authtype.IacSupportAssistAuthenticationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacLockStatus;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacSecurityPolicy;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.IacThirdPartyCertificationType;
import com.ruijie.rcos.rcdc.maintenance.module.def.annotate.NoBusinessMaintenanceUrl;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.ModifyPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.RcoLoginAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalSimplifyDeploymentConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.uws.UwsComponentEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.CreateAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpgradeAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAdminPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SuperPrivilegeResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListImageIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;
import com.ruijie.rcos.rcdc.rco.module.def.enums.TokenSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo.AdminVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo.DataPermissionVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo.MfaAuthVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo.NameDuplicationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.EditAdminAndTerminalMngPwdRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.EditAdminPwdWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.SessionContextRegistry;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.validation.AdminCustomValidation;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbChangePasswordDTO;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.session.SessionEventSPI;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月19日
 *
 * @author zhuangchenwu
 */
@Api(tags = "管理员管理功能")
@Controller
@RequestMapping("/rco/admin")
@EnableCustomValidate(validateClass = AdminCustomValidation.class)
public class AdminManageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminManageController.class);

    public static final String HAS_DEFAULT = "hasDefault";

    public static final String CREATE_TIME = "createTime";

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private IacPermissionMgmtAPI basePermissionMgmtAPI;

    @Autowired
    private PermissionMgmtAPI permissionMgmtAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private CmsDockingAPI cmsDockingAPI;

    @Autowired
    private AdminManageAPI adminManageAPI;

    @Autowired
    private RccpTokenAPI rccpTokenAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private SessionContextRegistry sessionContextRegistry;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private ModifyPasswordAPI modifyPasswordAPI;


    @Autowired
    private EvaluationAPI evaluationAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private RcdcTokenAPI rcdcTokenAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private EnableFullSystemDiskAPI enableFullSystemDiskAPI;

    @Autowired
    private TerminalSimplifyDeploymentConfigAPI terminalSimplifyDeploymentConfigAPI;

    @Autowired
    private RcaSupportAPI rcaSupportAPI;

    @Autowired
    private IacAdminMgmtAPI iacAdminMgmtAPI;

    @Autowired
    private IacSecurityStrategyAPI iacSecurityPolicyAPI;

    @Autowired
    private IacAuthTypeMgmtAPI iacAuthTypeMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private AdminTokenLoginAPI adminTokenLoginAPI;

    @Autowired
    private UserOtpCertificationAPI userOtpCertificationAPI;

    private static final String TIMESTAMP = "timestamp";

    private static final String FIRST_ENABLE_SUFFIX = "_first_enable";

    /**
     * 超级管理员admin
     */
    private static final String ADMIN_NAME = "admin";

    /**
     * 管理员密码最大错误次数
     */
    private static final int MAX_PWD_ERROR_TIMES = 10;


    /**
     * 生成锐捷动态口令二维码id用
     */
    private static final String SYSTEM_HOST = "ruijie.com.cn";


    /**
     * 获取超级管理员信息请求
     *
     * @return 获取结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取超级管理员信息请求")
    @RequestMapping(value = "getSuperAdmin", method = RequestMethod.POST)
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<AdminVO> getSuperAdmin() throws BusinessException {
        IacAdminDTO baseAdminDTO = getAdminInfo();

        IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[]{baseAdminDTO};
        Map<UUID, IacRoleDTO> roleMap = addRoleToRoleMap(baseAdminDTOArr);

        AdminVO adminVO = buildAdminVO(baseAdminDTO, roleMap);

        //设置终端组
        adminVO.setTerminalGroupArr(getTerminalGroupIdLabelEntryArr(adminVO.getId()));
        //设置用户组
        adminVO.setUserGroupArr(getUserGroupIdLabelEntryArr(adminVO.getId()));
        //设置镜像组
        adminVO.setImageArr(getImageIdLabelEntryArr(adminVO.getId()));
        adminVO.setDesktopPoolArr(getDesktopPoolIdLabelEntryArr(adminVO.getId()));
        adminVO.setDiskPoolArr(getDiskPoolIdLabelEntryArr(adminVO.getId()));
        adminVO.setDeskStrategyArr(getDeskStrategyIdLabelEntryArr(adminVO.getId()));

        String[] menuNameArr = getMenuNameArr(baseAdminDTOArr[0]);
        adminVO.setMenuNameArr(menuNameArr);
        // 功能
        String[] funNameArr = getFunNameArr(baseAdminDTO.getRoleIdArr(), baseAdminDTO.getId());
        adminVO.setFunNameArr(funNameArr);

        return CommonWebResponse.success(adminVO);
    }

    /**
     * rccp配置向导单点登录请求
     *
     * @param request        rccp传过来的token值
     * @param sessionContext session上下文
     * @return 登录结果
     * @throws BusinessException 异常
     */
    @ApiOperation("rccp配置向导单点登录请求")
    @RequestMapping(value = "tokenLogin", method = RequestMethod.POST)
    @NoAuthUrl
    public CommonWebResponse<AdminVO> wizardSso(WizardSsoWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        String source = request.getSource();
        TokenSourceEnum tokenSourceEnum = TokenSourceEnum.RCCP;
        if (StringUtils.isNotEmpty(source)) {
            tokenSourceEnum = TokenSourceEnum.convert(source);
        }

        adminTokenLoginAPI.isTokenValid(tokenSourceEnum, request.getToken());
        SubSystem subSystem = TokenSourceEnum.CLOUD_DOCK == tokenSourceEnum ? SubSystem.CDC : SubSystem.CCP;
        try {
            IacAdminDTO adminDTO = getAdminInfo();
            GetAdminPasswordResponse adminPasswordResp = adminManageAPI.getAdminPassword(new IdRequest(adminDTO.getId()));
            RcoLoginAdminRequest rcoLoginAdminRequest = new RcoLoginAdminRequest().setUserName(adminDTO.getUserName())
                    .setRawPassword(adminPasswordResp.getPassword())
                    .setSubSystem(subSystem).setLoginIp(sessionContext.getClientIp());
            IacAdminDTO baseAdminDTO = adminMgmtAPI.loginAdminNoAuth(rcoLoginAdminRequest);

            loginSessionHandleForSso(sessionContext, baseAdminDTO);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_SUCCESS);

            IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[]{baseAdminDTO};
            Map<UUID, IacRoleDTO> roleMap = addRoleToRoleMap(baseAdminDTOArr);

            AdminVO adminVO = buildAdminVO(baseAdminDTO, roleMap);
            adminVO.setToken(baseAdminDTO.getToken());

            String[] menuNameArr = getMenuNameArr(baseAdminDTO);
            adminVO.setMenuNameArr(menuNameArr);

            return CommonWebResponse.success(adminVO);
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_FAIL, ex,
                    ex.getAttachment(UserTipContainer.Constants.USER_TIP, ex.getI18nMessage()));
            throw ex;
        }
    }

    /**
     * rcdc 免认证登录
     *
     * @param request        rccm传过来的token值
     * @param sessionContext session上下文
     * @return 登录结果
     * @throws BusinessException 异常
     */
    @ApiOperation("rcdc 免认证登录")
    @RequestMapping(value = "noAuthLogin", method = RequestMethod.POST)
    @NoAuthUrl
    public CommonWebResponse<AdminVO> noAuthLogin(WizardSsoWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        try {
            UUID adminId = rcdcTokenAPI.checkLoginToken(UUID.fromString(request.getToken()));

            IacAdminDTO adminDTO = baseAdminMgmtAPI.getAdmin(adminId);
            GetAdminPasswordResponse adminPasswordResp = adminManageAPI.getAdminPassword(new IdRequest(adminDTO.getId()));
            RcoLoginAdminRequest rcoLoginAdminRequest = new RcoLoginAdminRequest().setUserName(adminDTO.getUserName())
                    .setRawPassword(adminPasswordResp.getPassword())
                    .setSubSystem(SubSystem.CDC ).setLoginIp(sessionContext.getClientIp());
            IacAdminDTO baseAdminDTO = adminMgmtAPI.loginAdminNoAuth(rcoLoginAdminRequest);

            LOGGER.info("baseAdminDTO:{}", JSON.toJSONString(baseAdminDTO));
            loginSessionHandleForSso(sessionContext, baseAdminDTO);

            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_SUCCESS);
            sessionContextRegistry.addSessionContext(sessionContext);

            IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[]{baseAdminDTO};
            Map<UUID, IacRoleDTO> roleMap = addRoleToRoleMap(baseAdminDTOArr);

            AdminVO adminVO = buildAdminVO(baseAdminDTO, roleMap);
            setAdminWeakPassword(adminVO, baseAdminDTO);
            adminVO.setToken(baseAdminDTO.getToken());
            return CommonWebResponse.success(adminVO);
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_FAIL, ex,
                    ex.getAttachment(UserTipContainer.Constants.USER_TIP, ex.getI18nMessage()));
            throw ex;
        }
    }

    private void loginSessionHandleForSso(SessionContext sessionContext, IacAdminDTO baseAdminDTO) throws BusinessException {
        // 先注销之前账号
        if (sessionContext.isLogin()) {
            logout(sessionContext);
        }

        // 重建session信息
        sessionContext.recreate();

        // SessionEventOptLogRecord
        SessionEventOptLogRecord record = new SessionEventOptLogRecord(baseAdminDTO.getUserName());

        // 记录最新的登录数据
        sessionContext.loginSuccess(baseAdminDTO.getId(), baseAdminDTO.getUserName(), record);
        sessionContext.addAttribute(TIMESTAMP, System.currentTimeMillis());
    }

    private IacAdminDTO getAdminInfo() throws BusinessException {
        return adminMgmtAPI.getAdminByUserName(ADMIN_NAME);
    }

    /**
     * 管理员登录请求
     *
     * @param request        请求参数
     * @param sessionContext session上下文
     * @return 登录结果
     * @throws BusinessException 异常
     */
    @ApiOperation("管理员登录请求")
    @RequestMapping(value = "loginAdmin", method = RequestMethod.POST)
    @NoAuthUrl
    @EnableCustomValidate(validateMethod = "loginAdminValidate")
    @NoBusinessMaintenanceUrl
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = "管理员登录"),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"判断管理员、终端管理密码是否为弱密码"})})
    public CommonWebResponse<AdminVO> loginAdmin(LoginAdminWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        LOGGER.info("收到管理员登录请求请求，请求信息为：{}", JSON.toJSONString(request));
        String userName = request.getUserName();
        String decryptPwd;
        try {
            decryptPwd = AesUtil.descrypt(request.getPwd(), RedLineUtil.getRealAdminRedLine());
        } catch (Exception e) {
            LOGGER.error("解密用户：{}密码失败，失败原因：", userName, e);
            throw new BusinessException(BusinessKey.RCDC_RCO_DECRYPT_FAIL, e);
        }

        try {
            validateLoginAdmin(request, sessionContext);

            IacLoginAdminRequest rcoLoginAdminRequest = new IacLoginAdminRequest();
            BeanUtils.copyProperties(request, rcoLoginAdminRequest);
            rcoLoginAdminRequest.setDeviceId(sessionContext.getClientIp());
            rcoLoginAdminRequest.setLoginName(userName);
            rcoLoginAdminRequest.setPwd(request.getPwd());
            rcoLoginAdminRequest.setSubSystem(SubSystem.CDC);
            rcoLoginAdminRequest.setLoginIp(sessionContext.getClientIp());
            IacAdminDTO baseAdminDTO = iacAdminMgmtAPI.loginAdmin(rcoLoginAdminRequest);

            loginSessionHandle(request, sessionContext, baseAdminDTO);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_SUCCESS);
            sessionContextRegistry.addSessionContext(sessionContext);

            IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[]{baseAdminDTO};
            Map<UUID, IacRoleDTO> roleMap = addRoleToRoleMap(baseAdminDTOArr);

            AdminVO adminVO = buildAdminVO(baseAdminDTO, roleMap);
            setAdminWeakPassword(adminVO, baseAdminDTO);
            // 返回开启辅助认证相关字段
            setMfaAuthParam(adminVO);
            return CommonWebResponse.success(adminVO);
        } catch (BusinessException ex) {
            LOGGER.error("管理员[{}]登录异常，异常原因：{}", userName, ex.getMessage());
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_FAIL_BY_ADMIN_NAME, ex, sessionContext.getClientIp(), userName,
                    ex.getAttachment(UserTipContainer.Constants.USER_TIP, ex.getI18nMessage()));
            throw ex;
        }
    }

    private void setMfaAuthParam(AdminVO adminVO) throws BusinessException {
        IacSupportAssistAuthenticationDTO supportAssistAUth = iacAuthTypeMgmtAPI.assistAuthentication(SubSystem.CDC, AuthUserTypeEnum.ADMIN,
                adminVO.getId());
        List<AssistAuthTypeEnum> assistAuthenticationList = supportAssistAUth.getAssistAuthenticationList();
        if (CollectionUtils.isEmpty(assistAuthenticationList)) {
            // 未开启辅助认证
            adminVO.setMfa(new MfaAuthVO());
            return;
        }
        // 管理员只支持锐捷动态口令和Radius动态口令作为辅助认证
        List<AssistAuthTypeEnum> mfaAuthList = assistAuthenticationList.stream()
                .filter(auth -> auth == AssistAuthTypeEnum.MFA_CERTIFICATION || auth == AssistAuthTypeEnum.THIRD_PARTY_CERTIFICATION)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(mfaAuthList)) {
            // 未开启动态口令作为辅助认证
            adminVO.setMfa(new MfaAuthVO());
            return;
        }
        // 两种都开启的情况下，优先用Radius
        if (mfaAuthList.contains(AssistAuthTypeEnum.THIRD_PARTY_CERTIFICATION)) {
            MfaAuthVO mfaAuthVO = new MfaAuthVO();
            // 开启Radius动态口令
            mfaAuthVO.setEnableRadiusOtp(true);
            adminVO.setMfa(mfaAuthVO);
            return;
        }
        // 开启的是锐捷动态口令
        IacMfaConfigDTO baseMfaConfigDTO = iacSecurityPolicyAPI.getMfaConfig(SubSystem.CDC);
        String secretKey = baseAdminMgmtAPI.generateMfaCode(adminVO.getId(), SubSystem.CDC);
        String qrCodeId = SYSTEM_HOST + adminVO.getUserName() + secretKey;
        MfaAuthVO mfaAuthVO = new MfaAuthVO();
        mfaAuthVO.setEnableRuijieOtp(true);
        mfaAuthVO.setSecretKey(secretKey);
        mfaAuthVO.setDigits(baseMfaConfigDTO.getDigits());
        mfaAuthVO.setAlgorithm(baseMfaConfigDTO.getAlgorithm());
        mfaAuthVO.setPeriod(baseMfaConfigDTO.getPeriod());
        mfaAuthVO.setQrCodeId(UUID.nameUUIDFromBytes(qrCodeId.getBytes(StandardCharsets.UTF_8)).toString());

        OtpParamRequest otpParamRequest = new OtpParamRequest();
        otpParamRequest.setUserType(OtpUserType.ADMIN);
        mfaAuthVO.setOtpParams(userOtpCertificationAPI.obtainOtpAttachmentParams(otpParamRequest));

        adminVO.setMfa(mfaAuthVO);
    }

    /**
     * 锐捷动态口令作为主认证
     *
     * @param request 请求参数
     * @param sessionContext sessionContext
     * @return 登录结果
     * @throws BusinessException 异常
     */
    @NoAuthUrl
    @RequestMapping(value = "rjOtpLogin", method = RequestMethod.POST)
    public CommonWebResponse ruijieOtpLogin(RjOtpLoginWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        IacAdminDTO iacAdminDTO = verifyRjMfaCode(request.getCode(), request.getUserName(),sessionContext.getClientIp());
        IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[] {iacAdminDTO};
        Map<UUID, IacRoleDTO> roleMap = addRoleToRoleMap(baseAdminDTOArr);
        AdminVO adminVO = buildAdminVO(iacAdminDTO, roleMap);
        auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_SUCCESS);
        return CommonWebResponse.success(adminVO);
    }

    /**
     * 锐捷动态口令作为辅助认证
     *
     * @param request 请求参数
     * @param sessionContext sessionContext
     * @return 登录结果
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "rjOtpAssistLogin", method = RequestMethod.POST)
    public CommonWebResponse ruijieOtpAssistLogin(RjOtpAssistLoginWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        IacLoginUserDTO userDTO = baseAdminMgmtAPI.getLoginUserInfo();
        IacAdminDTO iacAdminDTO = verifyRjMfaCode(request.getCode(), userDTO.getUserName(), sessionContext.getClientIp());
        IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[] {iacAdminDTO};
        Map<UUID, IacRoleDTO> roleMap = addRoleToRoleMap(baseAdminDTOArr);
        AdminVO adminVO = buildAdminVO(iacAdminDTO, roleMap);
        auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_SUCCESS);
        return CommonWebResponse.success(adminVO);
    }

    private IacAdminDTO verifyRjMfaCode(String code, String userName, String clientIp) throws BusinessException {
        IacVerifyAdminMfaRequest verifyAdminMfaRequest = new IacVerifyAdminMfaRequest();
        verifyAdminMfaRequest.setUserName(userName);
        verifyAdminMfaRequest.setCode(code);
        verifyAdminMfaRequest.setSubSystem(SubSystem.CDC);
        verifyAdminMfaRequest.setLoginIp(clientIp);
        IacAdminDTO iacAdminDTO;
        try {
            iacAdminDTO = baseAdminMgmtAPI.verifyMfaCode(verifyAdminMfaRequest);
        } catch (BusinessException ex) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(ex);
            LOGGER.error("管理员[{}]rj动态口令登录失败，失败原因：{}", userName, exceptionMsg);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_FAIL_BY_ADMIN_NAME, ex, userName, exceptionMsg);
            throw ex;
        }
        return iacAdminDTO;
    }

    /**
     * Radius动态口令作为辅助认证
     *
     * @param request 请求参数
     * @param sessionContext sessionContext
     * @return 登录结果
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "radiusOtpAssistLogin", method = RequestMethod.POST)
    public CommonWebResponse radiusOtpAssistLogin(RadiusOtpAssistLoginWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        IacLoginUserDTO userDTO = baseAdminMgmtAPI.getLoginUserInfo();
        String userName = userDTO.getUserName();
        IacAdminDTO iacAdminDTO;
        try {
            IacThirdPartyCertificationRequest iacThirdPartyCertificationRequest = new IacThirdPartyCertificationRequest();
            iacThirdPartyCertificationRequest.setCertificationCode(request.getCode());
            iacThirdPartyCertificationRequest.setCertificationType(IacThirdPartyCertificationType.RADIUS);
            iacThirdPartyCertificationRequest.setUserName(userName);
            iacThirdPartyCertificationRequest.setLoginIp(sessionContext.getClientIp());
            iacThirdPartyCertificationRequest.setSubSystem(SubSystem.CDC);
            iacAdminDTO = baseAdminMgmtAPI.verifyThirdPartyCertification(iacThirdPartyCertificationRequest);
        } catch (BusinessException ex) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(ex);
            LOGGER.error("管理员[{}]Radius动态口令登录失败，失败原因：{}", userName, exceptionMsg);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_FAIL_BY_ADMIN_NAME, ex, userName, exceptionMsg);
            throw ex;
        }
        IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[] {iacAdminDTO};
        Map<UUID, IacRoleDTO> roleMap = addRoleToRoleMap(baseAdminDTOArr);
        AdminVO adminVO = buildAdminVO(iacAdminDTO, roleMap);
        auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_SUCCESS);
        return CommonWebResponse.success(adminVO);
    }

    /**
     * 管理员登出请求
     *
     * @param sessionContext session上下文
     * @return 登出结果
     * @throws BusinessException 异常
     */
    @ApiOperation("管理员登出请求")
    @RequestMapping(value = "logoutAdmin", method = RequestMethod.POST)
    public CommonWebResponse logoutAdmin(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext can not be null");
        baseAdminMgmtAPI.logout();
        logout(sessionContext);

        // 成功日志由框架扩展接口记录，防止重复记录，或超时记录丢失
        return CommonWebResponse.success();
    }

    /**
     * 获取当前登录用户信息
     *
     * @param sessionContext session上下文
     * @return 验证结果
     * @throws BusinessException 异常
     */
    @ApiOperation("获取当前登录用户信息")
    @RequestMapping(value = "getCurrentUserInfo", method = RequestMethod.POST)
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<AdminVO> getCurrentUserInfo(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is not null");
        IacLoginUserDTO loginUserInfo = baseAdminMgmtAPI.getLoginUserInfo();

        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(loginUserInfo.getId());
        IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[]{baseAdminDTO};
        Map<UUID, IacRoleDTO> roleMap = addRoleToRoleMap(baseAdminDTOArr);

        AdminVO adminVO = buildAdminVO(baseAdminDTO, roleMap);

        // 菜单
        String[] menuNameArr = getMenuNameArr(baseAdminDTO);
        adminVO.setMenuNameArr(menuNameArr);

        //功能
        String[] funNameArr = getFunNameArr(baseAdminDTO.getRoleIdArr(), baseAdminDTO.getId());
        adminVO.setFunNameArr(funNameArr);

        // 是否为弱密码
        setAdminWeakPassword(adminVO, baseAdminDTO);

        return CommonWebResponse.success(adminVO);
    }

    /**
     * 获取功能
     *
     * @param roleIdArr
     * @param adminId
     * @return
     */
    private String[] getFunNameArr(UUID[] roleIdArr, UUID adminId) throws BusinessException {
        if (ArrayUtils.isEmpty(roleIdArr)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        Set<String> menuNameSet = new HashSet<>();
        SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
        superPrivilegeRequest.setRoleIdArr(roleIdArr);
        SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
        // 角色权限关联表中未记载超级管理员信息，直接通过是否为超级管理员来返回所有菜单列表
        if (superPrivilegeResponse.isSuperPrivilege()) {
            List<IacPermissionDTO> basePermissionDTOList = permissionMgmtAPI.listAllPermissionByServerModel();
            if (basePermissionDTOList != null) {
                // 遍历权限 获取功能权限菜单 根据key为FUNTYPE
                List<String> menuNameList = basePermissionDTOList.stream().filter(permissionDTO -> permissionDTO.getTags() != null)
                        .filter(permissionDTO -> FunTypes.FUN.equals(((JSONObject) permissionDTO.getTags()).getString(FunTypes.FUNTYPE)))
                        .map(IacPermissionDTO::getPermissionCode).collect(Collectors.toList());
                menuNameSet.addAll(menuNameList);
            }
        } else {
            List<IacPermissionDTO> basePermissionDTOList = basePermissionMgmtAPI.listPermissionByAdminIdAndSource(adminId, SubSystem.CDC);
            if (basePermissionDTOList != null) {
                // 遍历权限 获取功能权限菜单 根据key为FUNTYPE
                List<String> menuNameList = basePermissionDTOList.stream().filter(permissionDTO -> permissionDTO.getTags() != null)
                        .filter(permissionDTO -> FunTypes.FUN.equals(((JSONObject) permissionDTO.getTags()).getString(FunTypes.FUNTYPE)))
                        .map(IacPermissionDTO::getPermissionCode).collect(Collectors.toList());
                menuNameSet.addAll(menuNameList);
            }
        }
        try {
            List<String> unsupportedMenuNameList = permissionMgmtAPI.getCurrentServerModelUnsupportedMenuNameList();
            LOGGER.info("获取当前服务器模式不支持的菜单列表{}:{}", unsupportedMenuNameList.size(), unsupportedMenuNameList.toArray());
            menuNameSet.removeAll(unsupportedMenuNameList);
        } catch (Exception e) {
            LOGGER.error("获取当前服务器模式不支持的菜单列表失败", e);
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return menuNameSet.toArray(new String[menuNameSet.size()]);
    }

    /**
     * 升级为管理员请求
     *
     * @param upgradeAdminWebRequest 请求参数
     * @param sessionContext         session上下文
     * @return 创建结果
     * @throws BusinessException 异常
     */
    @ApiOperation("升级为管理员请求")
    @RequestMapping(value = "upgradeAdmin", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "upgradeAdminValidate")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"升级为管理员请求"})})
    @EnableAuthority
    public CommonWebResponse upgradeAdmin(UpgradeAdminWebRequest upgradeAdminWebRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(upgradeAdminWebRequest, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        String userName = null;
        // 获取普通用户基本信息
        IacUserDetailDTO userDetail = null;
        try {
            // 获取普通用户基本信息
            userDetail = userAPI.getUserDetail(upgradeAdminWebRequest.getUserId());
            // 如果用户是AD域用户或者LDAP域用户或者第三方用户，不允许管理员状态与用户状态不一致 即用户是启用，管理员也是启动，用户禁用，管理员禁用
            if ((userDetail.getUserType() == IacUserTypeEnum.AD || userDetail.getUserType() == IacUserTypeEnum.LDAP
                    || userDetail.getUserType() == IacUserTypeEnum.THIRD_PARTY)
                    && ((userDetail.getUserState() == IacUserStateEnum.ENABLE) != upgradeAdminWebRequest.getEnabled())) {
                // AD域或者LDAP域用户升级为管理员，不允许修改状态
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_AD_USER_UPGRADE_ADMIN_CAN_NOT_EDIT_STATUS);
            }
            // 构造升级管理员请求request
            UpgradeAdminRequest upgradeAdminRequest = buildUpgradeAdminRequest(upgradeAdminWebRequest, userDetail);
            // 设置并且校验用户名称
            userName = upgradeAdminRequest.getUserName();
            if (validateAdminUserNameDefaultAdminName(upgradeAdminRequest.getUserName())) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_NAME_IS_DEFAULT_ADMIN_NAME);
            }
            // 获取需要创建的角色信息
            IacRoleDTO role = baseRoleMgmtAPI.getRole(upgradeAdminWebRequest.getRoleId());
            // 如果需要创建的角色是超级管理员
            if (role != null && RoleType.ADMIN.getName().equals(role.getRoleName())) {
                LOGGER.info("当前创建的管理员的角色是超级管理员[{}]", upgradeAdminWebRequest.getUserName());
                IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
                LOGGER.info("判断当前登陆的管理员用户信息是否为内置管理员[{}]", baseAdminDTO);
                if (!DefaultAdmin.ADMIN.getName().equals(baseAdminDTO.getUserName())) {
                    // 仅内置超级管理员admin允许创建超级管理员 抛出异常
                    throw new BusinessException(AaaBusinessKey.RCDC_AAA_NOT_DEFAULT_ADMIN_NOT_ALLOW_CREATE_SUPERADMIN);
                }
            }
            // 升级管理员
            LOGGER.info("upgradeAdminRequest:{}", JSON.toJSONString(upgradeAdminRequest));
            adminMgmtAPI.upgradeAdmin(upgradeAdminRequest);
            CreateAdminRequest baseRequest = new CreateAdminRequest();
            // 拷贝 upgradeAdminRequest 到 baseRequest cmc数据埋点
            BeanUtils.copyProperties(upgradeAdminRequest, baseRequest);
            // CMS对接：新建管理员触发同步
            syncAdminByCreate(baseRequest);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_UPGRADE_ADMIN_SUCCESS, userName);
            return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException ex) {
            userName = userDetail == null ? upgradeAdminWebRequest.getUserId().toString() : userDetail.getUserName();
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_UPGRADE_ADMIN_FAIL, ex, userName, ex.getI18nMessage());
            throw ex;
        }
    }


    /**
     * @api {POST} /rco/admin/detail 管理员详情
     * @apiName 管理员详情
     * @apiGroup 管理员管理相关
     * @apiDescription 管理员详情
     *
     * @apiParam (请求路径字段说明) {String} id 管理员UUID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "id": "6e20f88f-8d7d-4182-a7b0-e29ed0dd5df9"
     *                  }
     *
     *
     * @apiSuccess (响应字段说明) {int} code 响应码
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {String} status 成功失败状态
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} content.id UUID
     * @apiSuccess (响应字段说明) {Number} content.createTime 创建时间
     * @apiSuccess (响应字段说明) {String} content.userName 用户名
     * @apiSuccess (响应字段说明) {String} content.realName 真实名称
     * @apiSuccess (响应字段说明) {Object[]} content.roleArr 角色信息数组
     * @apiSuccess (响应字段说明) {String} content.roleArr.id 角色id
     * @apiSuccess (响应字段说明) {String} content.roleArr.label 角色名称
     * @apiSuccess (响应字段说明) {String} content.describe 描述
     * @apiSuccess (响应字段说明) {string} content.email 邮箱
     * @apiSuccess (响应字段说明) {boolean} content.enabled 是否启用
     * @apiSuccess (响应字段说明) {boolean} content.firstEnable 是否第一次启用过
     * @apiSuccess (响应字段说明) {boolean} content.hasFirstTimeLoggedIn 是否第一次登录过
     * @apiSuccess (响应字段说明) {boolean} content.hasSuperPrivilege 是否超级权限
     * @apiSuccess (响应字段说明) {Object[]} content.userGroupArr 用户组信息
     * @apiSuccess (响应字段说明) {Object[]} content.menuNameArr 菜单数组
     * @apiSuccess (响应字段说明) {Object[]} content.terminalGroupArr 终端组信息
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "id": "6e20f88f-8d7d-4182-a7b0-e29ed0dd5df9"
     *                  }
     * @apiSuccess (响应字段说明) {int} code 响应码
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} content 响应数据
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content": {
     *                    "createTime": 1582602656917,
     *                    "describe": null,
     *                    "email": null,
     *                    "enabled": true,
     *                    "firstEnable": null,
     *                    "hasDefault": false,
     *                    "hasFirstTimeLoggedIn": true,
     *                    "hasSuperPrivilege": null,
     *                    "id": "6e20f88f-8d7d-4182-a7b0-e29ed0dd5df9",
     *                    "menuNameArr": ["cloudDesktopManage", "terminalManage"],
     *                    "realName": "test6666",
     *                    "roleArr": [{
     *                    "id": "6d297d40-a4ba-416d-b612-140d40d03e22",
     *                    "label": "系统管理员"
     *                    }],
     *                    "terminalGroupArr": [{
     *                    "id": "18fe38f2-8604-49ae-a610-db218e4d6b0e",
     *                    "label": "linster"
     *                    }, {
     *                    "id": "399e4127-47bc-4c65-a7c8-0faf028955a3",
     *                    "label": "12"
     *                    }, {
     *                    "id": "7769c0c6-473c-4d4c-9f47-5a62bdeb30ba",
     *                    "label": "未分组"
     *                    }],
     *                    "userGroupArr": [{
     *                    "id": "90b6dc24-18e1-41e1-b57c-a3155772dd5a",
     *                    "label": "ff"
     *                    }, {
     *                    "id": "911046f6-bbad-4f71-85e4-c515275cec36",
     *                    "label": "ddd"
     *                    }, {
     *                    "id": "a4209626-1df1-499f-93ca-de15c3f17fe7",
     *                    "label": "未分组"
     *                    }],
     *                    "userName": "test6666"
     *                    },
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
     *                    "status": "SUCCESS"
     *                    }
     *
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "content": null,
     *                  "message": "系统内部错误，请联系管理员",
     *                  "msgArgArr": [],
     *                  "msgKey": "sk_webmvckit_internal_error",
     *                  "status": "ERROR"
     *                  }
     *
     */

    /**
     * 获取单个管理员信息请求
     *
     * @param request 请求参数
     * @return 获取结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取单个管理员信息请求")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public CommonWebResponse<AdminVO> getAdmin(GetAdminWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(request.getId());

        IacAdminDTO[] baseAdminDTOArr = new IacAdminDTO[]{baseAdminDTO};
        Map<UUID, IacRoleDTO> roleMap = addRoleToRoleMap(baseAdminDTOArr);

        AdminVO adminVO = buildAdminVO(baseAdminDTO, roleMap);
        //设置终端组
        adminVO.setTerminalGroupArr(getTerminalGroupIdLabelEntryArr(adminVO.getId()));
        //设置用户组
        adminVO.setUserGroupArr(getUserGroupIdLabelEntryArr(adminVO.getId()));
        //设置镜像组
        adminVO.setImageArr(getImageIdLabelEntryArr(adminVO.getId()));
        adminVO.setDesktopPoolArr(getDesktopPoolIdLabelEntryArr(adminVO.getId()));
        adminVO.setDiskPoolArr(getDiskPoolIdLabelEntryArr(adminVO.getId()));
        adminVO.setDeskStrategyArr(getDeskStrategyIdLabelEntryArr(adminVO.getId()));
        adminVO.setAppPoolArr(getAppPoolIdLabelEntryArr(adminVO.getId()));
        adminVO.setAppMainStrategyArr(getAppMainStrategyIdLabelEntryArr(adminVO.getId()));
        adminVO.setAppPeripheralStrategyArr(getAppPeripheralStrategyLabelEntryArr(adminVO.getId()));
        String[] menuNameArr = getMenuNameArr(baseAdminDTOArr[0]);
        adminVO.setMenuNameArr(menuNameArr);

        return CommonWebResponse.success(adminVO);
    }

    /**
     * 获取单个管理员数据权限信息请求
     * @param request request
     * @return 数据权限
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取单个管理员数据权限信息请求")
    @RequestMapping(value = "/dataPermission/detail", method = RequestMethod.POST)
    public CommonWebResponse<DataPermissionVO> getAdminDataPermission(GetAdminWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(request.getId());
        DataPermissionVO dataPermissionVO = new DataPermissionVO();
        //设置终端组
        dataPermissionVO.setTerminalGroupArr(getTerminalGroupIdLabelEntryArr(baseAdminDTO.getId()));
        //设置用户组
        dataPermissionVO.setUserGroupArr(getUserGroupIdLabelEntryArr(baseAdminDTO.getId()));
        //设置镜像组
        dataPermissionVO.setImageArr(getImageIdLabelEntryArr(baseAdminDTO.getId()));
        dataPermissionVO.setDesktopPoolArr(getDesktopPoolIdLabelEntryArr(baseAdminDTO.getId()));
        dataPermissionVO.setDiskPoolArr(getDiskPoolIdLabelEntryArr(baseAdminDTO.getId()));
        dataPermissionVO.setDeskStrategyArr(getDeskStrategyIdLabelEntryArr(baseAdminDTO.getId()));
        dataPermissionVO.setAppPoolArr(getAppPoolIdLabelEntryArr(baseAdminDTO.getId()));
        dataPermissionVO.setAppMainStrategyArr(getAppMainStrategyIdLabelEntryArr(baseAdminDTO.getId()));
        dataPermissionVO.setAppPeripheralStrategyArr(getAppPeripheralStrategyLabelEntryArr(baseAdminDTO.getId()));

        return CommonWebResponse.success(dataPermissionVO);
    }

    /**
     * 更新单个管理员数据权限信息请求
     * @param request request
     * @return 数据权限
     * @throws BusinessException 业务异常
     */
    @ApiOperation("更新单个管理员数据权限信息请求")
    @RequestMapping(value = "/dataPermission/edit", method = RequestMethod.POST)
    public CommonWebResponse editAdminDataPermission(UpdateAdminDataPermissionWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(request.getId());

        UpdateAdminDataPermissionRequest updateAdminRequest = buildUpdateAdminDataPermissionRequest(request);
        adminMgmtAPI.updateAdminDataPermission(updateAdminRequest);
        auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_EDIT_ADMIN_DATA_PERMISSION_SUCCESS, baseAdminDTO.getUserName());

        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, StringUtils.EMPTY);
    }

    /**
     * 校验密码复杂度
     *
     * @param validReq 请求参数
     * @return 校验结果
     * @throws BusinessException 异常
     */
    @ApiOperation("校验密码复杂度")
    @RequestMapping(value = "validatePwdComplexity", method = RequestMethod.POST)
    public CommonWebResponse<IacCheckCommonDTO> validatePwdComplexity(ValidateAdminPwdWebRequest validReq) throws BusinessException {
        Assert.notNull(validReq, "validReq is not null");
        IacCheckCommonDTO baseCheckCommonDTO = baseAdminMgmtAPI.validatePwdComplexity(validReq.getPwd(),SubSystem.CDC);
        return CommonWebResponse.success(baseCheckCommonDTO);
    }


    private String[] getMenuNameArr(IacAdminDTO baseAdminDTO) throws BusinessException {
        UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();
        if (ArrayUtils.isEmpty(roleIdArr)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        Set<String> menuNameSet = new HashSet<>();
        SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
        superPrivilegeRequest.setRoleIdArr(roleIdArr);
        SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("是否拥有超级管理员权限:{}", JSON.toJSONString(superPrivilegeResponse));
        }
        // 角色权限关联表中未记载超级管理员信息，直接通过是否为超级管理员来返回所有菜单列表
        if (superPrivilegeResponse.isSuperPrivilege()) {
            List<IacPermissionDTO> basePermissionDTOList = permissionMgmtAPI.listAllPermissionByServerModel();
            basePermissionDTOList = permissionMgmtAPI.listPermissionFilterByBaseAdminDTO(basePermissionDTOList, baseAdminDTO.getUserName());
            if (basePermissionDTOList != null) {
                // 遍历tag key为菜单标签 value 为菜单
                List<String> menuNameList = basePermissionDTOList.stream().filter(permissionDTO -> permissionDTO.getTags() != null)
                        .filter(permissionDTO -> FunTypes.MENU.equals(((JSONObject) permissionDTO.getTags()).getString(FunTypes.FUNTYPE)))
                        .map(IacPermissionDTO::getPermissionCode).collect(Collectors.toList());
                menuNameSet.addAll(menuNameList);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("当前角色信息:{}", JSON.toJSONString(roleIdArr));
            }
            List<IacPermissionDTO> basePermissionDTOList = basePermissionMgmtAPI
                    .listPermissionByAdminIdAndSource(baseAdminDTO.getId(), SubSystem.CDC);
            basePermissionDTOList = permissionMgmtAPI.listPermissionFilterByBaseAdminDTO(basePermissionDTOList, baseAdminDTO.getUserName());
            if (basePermissionDTOList != null) {
                // 遍历tag key为菜单标签 value 为菜单
                List<String> menuNameList = basePermissionDTOList.stream().filter(permissionDTO -> permissionDTO.getTags() != null)
                        .filter(permissionDTO -> FunTypes.MENU.equals(((JSONObject) permissionDTO.getTags()).getString(FunTypes.FUNTYPE)))
                        .map(IacPermissionDTO::getPermissionCode).collect(Collectors.toList());
                menuNameSet.addAll(menuNameList);
            }
        }
        try {
            List<String> unsupportedMenuNameList = permissionMgmtAPI.getCurrentServerModelUnsupportedMenuNameList();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("获取当前服务器模式不支持的菜单列表{}:{}", unsupportedMenuNameList.size(), unsupportedMenuNameList.toArray());
            }
            menuNameSet.removeAll(unsupportedMenuNameList);
        } catch (Exception e) {
            LOGGER.error("获取当前服务器模式不支持的菜单列表失败", e);
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        filterUwsMenuByUwsComponentFlag(menuNameSet);
//        filterVirtualApplicationMenu(menuNameSet);
        filterMaintainDeskRecoverMenu(menuNameSet);
        return menuNameSet.toArray(new String[menuNameSet.size()]);
    }

    /**
     * 根据uws安装情况过滤uws菜单
     *
     * @param menuNameSet 菜单
     * @Date 2022/4/27 17:21
     * @Author zhengjingyong
     **/
    private void filterUwsMenuByUwsComponentFlag(Set<String> menuNameSet) {
        String uwsComponentFlag = uwsDockingAPI.getUwsComponentFlag();
        if (!UwsComponentEnum.ENABLED_STATE.name().equals(uwsComponentFlag)) {
            LOGGER.debug("uws未执行初始化，隐藏uws菜单");
            menuNameSet.remove(MenuType.UWS_MANAGE.getMenuName());
        }
    }

    /**
     * 根据虚拟应用开关控制虚拟应用的菜单项
     *
     * @param menuNameSet 菜单
     * @Date 2023/5/8 14:21
     * @Author zhengjingyong
     **/
    private void filterVirtualApplicationMenu(Set<String> menuNameSet) {
        if (!rcaSupportAPI.getVirtualApplicationState()) {
            LOGGER.debug("虚拟应用状态处于关闭，隐藏虚拟应用菜单");
            menuNameSet.remove(MenuType.APP_MANAGEMENT.getMenuName());
            menuNameSet.remove(MenuType.RCA_CLOUD_DESKTOP_MANAGE.getMenuName());
            menuNameSet.remove(MenuType.APP_POOL_MANAGE.getMenuName());
            menuNameSet.remove(MenuType.RCA_SESSION_MANAGE.getMenuName());
            menuNameSet.remove(MenuType.APP_STRATEGY.getMenuName());
            menuNameSet.remove(MenuType.APP_TERMINAL.getMenuName());
        }
    }

    /**
     * 根据评测功能开关来控制“桌面恢复”显示隐藏
     * @param menuNameSet 菜单
     */
    private void filterMaintainDeskRecoverMenu(Set<String> menuNameSet) {
        if (!evaluationAPI.getEvaluationStrategy()) {
            menuNameSet.remove(MenuType.CLOUD_DESKTOP_RECOVER.getMenuName());
        }
    }


    private GroupIdLabelEntry[] getTerminalGroupIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListTerminalGroupIdLabelEntryRequest request = new ListTerminalGroupIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListTerminalGroupIdLabelEntryResponse response = adminDataPermissionAPI.listTerminalGroupIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getTerminalGroupIdLabelEntryList();
        for (GroupIdLabelEntry entry : idLabelEntryList) {
            if (entry.getId().equals(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID)) {
                entry.setLabel(TerminalGroupHelper.TERMINAL_GROUP_ROOT_NAME);
                break;
            }
        }
        return idLabelEntryList.toArray(new GroupIdLabelEntry[idLabelEntryList.size()]);
    }

    private GroupIdLabelEntry[] getUserGroupIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListUserGroupIdLabelEntryRequest request = new ListUserGroupIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListUserGroupIdLabelEntryResponse response = adminDataPermissionAPI.listUserGroupIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getUserGroupIdLabelEntryList();
        for (GroupIdLabelEntry entry : idLabelEntryList) {
            if (entry.getId().equals(UserGroupHelper.USER_GROUP_ROOT_ID)) {
                entry.setLabel(UserGroupHelper.USER_GROUP_ROOT_NAME);
                break;
            }
        }
        return idLabelEntryList.toArray(new GroupIdLabelEntry[idLabelEntryList.size()]);
    }

    /**
     * 镜像组
     *
     * @param adminId
     * @return
     * @throws BusinessException
     */
    private GroupIdLabelEntry[] getImageIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListImageIdLabelEntryRequest request = new ListImageIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListImageIdLabelEntryResponse response = adminDataPermissionAPI.listImageIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getImageIdLabelEntryList();
        return idLabelEntryList.toArray(new GroupIdLabelEntry[idLabelEntryList.size()]);
    }

    private GroupIdLabelEntry[] getDesktopPoolIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListIdLabelEntryRequest request = new ListIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListIdLabelEntryResponse response = adminDataPermissionAPI.listDesktopPoolIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getIdLabelEntryList();
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    private GroupIdLabelEntry[] getDiskPoolIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListIdLabelEntryRequest request = new ListIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListIdLabelEntryResponse response = adminDataPermissionAPI.listDiskPoolIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getIdLabelEntryList();
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    private GroupIdLabelEntry[] getDeskStrategyIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListIdLabelEntryRequest request = new ListIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListIdLabelEntryResponse response = adminDataPermissionAPI.listDeskStrategyIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getIdLabelEntryList();
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    private GroupIdLabelEntry[] getAppPoolIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListIdLabelEntryRequest request = new ListIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListIdLabelEntryResponse response = adminDataPermissionAPI.listAppPoolIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getIdLabelEntryList();
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    private GroupIdLabelEntry[] getAppMainStrategyIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListIdLabelEntryRequest request = new ListIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListIdLabelEntryResponse response = adminDataPermissionAPI.listAppMainStrategyIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getIdLabelEntryList();
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    private GroupIdLabelEntry[] getAppPeripheralStrategyLabelEntryArr(UUID adminId) throws BusinessException {
        ListIdLabelEntryRequest request = new ListIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListIdLabelEntryResponse response = adminDataPermissionAPI.listAppPeripheralStrategyIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getIdLabelEntryList();
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }


    /**
     * 修改管理员密码
     *
     * @param request        请求参数
     * @param sessionContext session上下文
     * @return 修改结果
     * @throws BusinessException 异常
     */
    @ApiOperation("修改管理员密码")
    @RequestMapping(value = "modifyAdminPwd", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "modifyAdminPwdValidate")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = {"修改管理员自身密码"}),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"前端传输密码改为加密方式加密", "管理员密码不能和名称相同"})})
    public CommonWebResponse modifyAdminPwd(ModifyAdminPwdWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        String userName = sessionContext.getUserName();
        validateAdminNameAndPwd(userName, request.getNewPwd());
        try {
            // 不能修改从域用户升级的管理员的密码
            if (isDomainAdmin(userName)) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_AD_ADMIN_CAN_NOT_MODIFY_PASSWORD);
            }

            IacAdminDTO baseAdminDTO = adminMgmtAPI.getAdminByUserName(userName);
            // 修改管理员密码，并且同步修改用户密码
            ModifyPasswordDTO modifyPasswordDTO = buildModifyAdminPwdDTO(baseAdminDTO.getId(), request.getNewPwd(), request.getOldPwd());
            modifyPasswordAPI.modifyAdminPasswordSynUserPwd(modifyPasswordDTO);

            // 将修改的管理员密码与CMS同步
            IacModifyAdminPwdRequest baseRequest = buildModifyAdminPwdRequest(request, sessionContext);
            syncAdminByUpdatePwd(baseRequest);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_EDIT_ADMIN_PASSWORD_SUCCESS, userName);

            return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("管理员[%s]修改密码异常，异常原因：", userName), ex);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_EDIT_ADMIN_PASSWORD_FAIL, ex, userName,
                    ex.getAttachment(UserTipContainer.Constants.USER_TIP, ex.getI18nMessage()));

            throw ex;
        }
    }


    /**
     * 超级管理员首次登陆时，需要修改自身密码和终端管理密码
     *
     * @param request        请求参数
     * @param sessionContext session上下文
     * @return 修改结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("修改管理员和终端管理密码")
    @RequestMapping(value = "modifyAdminAndTerminalMngPwd", method = RequestMethod.POST)
    @ApiVersions(@ApiVersion(value = Version.V3_1_1, descriptions = {"超级管理员首次登录要求修改终端管理密码和自身密码", "管理员密码和名称不能相同"}))
    public CommonWebResponse<?> modifyAdminAndTerminalMngPwd(EditAdminAndTerminalMngPwdRequest request,
                                                             SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        // 校验管理员密码是否正确
        IacModifyAdminPwdRequest modifyAdminPwdRequest = buildModifyAdminPwdRequest(
                request.getModifyAdminPwdWebRequest(), sessionContext);
        EditAdminPwdWebRequest editAdminPwdWebRequest = request.getModifyTerminalMngPwdRequest();
        String terminalMngPwd;
        try {
            terminalMngPwd = AesUtil.descrypt(editAdminPwdWebRequest.getPwd(), RedLineUtil.getRealAdminRedLine());
        } catch (Exception e) {
            LOGGER.error("解密失败，失败原因：", e);
            return CommonWebResponse.fail(BusinessKey.RCDC_RCO_DECRYPT_FAIL, new String[]{});
        }

        CbbChangePasswordDTO changePwdRequest = new CbbChangePasswordDTO();
        changePwdRequest.setPassword(terminalMngPwd);
        // 修改密码
        try {
            // 校验终端管理密码是否正确
            checkTerminalMngPwdIsLegal(terminalMngPwd);
            // 校验管理员名称和密码是否相同
            validateAdminNameAndPwd(sessionContext.getUserName(), editAdminPwdWebRequest.getPwd());

            // 先修改管理员密码，再修改终端管理密码，防止出现管理员密码符合规范，但是终端管理密码不符合规范的情况
            ModifyPasswordDTO modifyPasswordDTO =
                    buildModifyAdminPwdDTO(modifyAdminPwdRequest.getId(), modifyAdminPwdRequest.getNewPwd(), modifyAdminPwdRequest.getOldPwd());
            modifyPasswordAPI.modifyAdminPasswordSynUserPwd(modifyPasswordDTO);
            terminalOperatorAPI.changePassword(changePwdRequest);
            syncAdminByUpdatePwd(modifyAdminPwdRequest);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MODIFY_SUPER_ADMIN_AND_TERMINAL_MNG_PWD_SUCCESS);

            return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("修改超级管理员密码和终端管理密码失败，失败原因：", e);

            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MODIFY_SUPER_ADMIN_AND_TERMINAL_MNG_PWD_FAIL, e, e.getI18nMessage());
            throw e;
        }
    }

    private void checkTerminalMngPwdIsLegal(String password) throws BusinessException {
        LOGGER.info("校验终端管理密码是否合法");
        if (!Pattern.matches(Constants.TERMINAL_MNG_PWD_REGEX, password)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_MNG_PWD_ILLEGAL);
        }
    }

    /**
     * 管理员重置密码
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 重置结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("管理员重置密码")
    @RequestMapping(value = "modifyOtherAdminPwd", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "modifyOtherAdminPwdValidate")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = {"修改其他管理员密码"}),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"前端传输密码改为加密方式加密", "管理员密码和名称不能相同"})})
    @EnableAuthority
    public CommonWebResponse modifyOtherAdminPwd(ModifyOtherAdminPwdWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        IacAdminDTO baseAdminDTO = null;
        UUID resetUserId = request.getId();
        IacModifyOtherAdminPwdRequest baseRequest = new IacModifyOtherAdminPwdRequest(resetUserId, request.getNewPwd());
        try {
            baseAdminDTO = baseAdminMgmtAPI.getAdmin(resetUserId);
            // 不能修改从域用户升级的管理员的密码
            if (isDomainAdmin(baseAdminDTO.getUserName())) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_AD_ADMIN_CAN_NOT_MODIFY_PASSWORD);
            }
            validateAdminNameAndPwd(baseAdminDTO.getUserName(), request.getNewPwd());
            /** 不能对自身进行操作 */
            if (baseAdminDTO.getId().equals(sessionContext.getUserId())) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CAN_NOT_OPRATE_YOUSELF);
            }
            // 当前管理员是内置安全管理员或者内置超级管理员 不需要做权限判断
            if (!permissionHelper.validateAdminIsAdminOrSecadmin(sessionContext.getUserName())) {
            }
            ModifyPasswordDTO modifyPasswordDTO = buildModifyAdminPwdDTO(resetUserId, request.getNewPwd(), request.getNewPwd());
            modifyPasswordAPI.modifyOtherAdminPwdSyncUserPwd(modifyPasswordDTO);
            IacAdminDTO dto = baseAdminMgmtAPI.getAdmin(resetUserId);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_RESET_ADMIN_PASSWORD_SUCCESS, dto.getUserName());
            syncAdminByResetPwd(dto, baseRequest);
            // 将目标管理员会话清空
            sessionContextRegistry.logoutSessionContext(baseAdminDTO.getId());
            return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException ex) {
            String userName = baseAdminDTO == null ? resetUserId.toString() : baseAdminDTO.getUserName();
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_RESET_ADMIN_PASSWORD_FAIL, ex, userName, ex.getI18nMessage());
            throw ex;
        }
    }


    private ModifyPasswordDTO buildModifyAdminPwdDTO(UUID adminId, String newPwd, String oldPwd) {
        ModifyPasswordDTO modifyPasswordDTO = new ModifyPasswordDTO();
        modifyPasswordDTO.setId(adminId);
        modifyPasswordDTO.setNewPwd(AesUtil.descrypt(newPwd, RedLineUtil.getRealAdminRedLine()));
        modifyPasswordDTO.setOldPwd(AesUtil.descrypt(oldPwd, RedLineUtil.getRealAdminRedLine()));

        return modifyPasswordDTO;
    }

    /**
     * 验证登录管理员密码
     *
     * @param request        请求参数
     * @param sessionContext session上下文
     * @return 验证结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("验证登录管理员密码")
    @RequestMapping(value = "validateAdminPwd", method = RequestMethod.POST)
    public CommonWebResponse validateAdminPwd(ValidateAdminPwdWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        try {
            AesUtil.descrypt(request.getPwd(), RedLineUtil.getRealAdminRedLine());
        } catch (Exception e) {
            LOGGER.error("解密失败，失败原因：", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_DECRYPT_FAIL, e);
        }
        try {
            IacLoginUserDTO loginUserInfo = baseAdminMgmtAPI.getLoginUserInfo();
            IacValidateAdminPwdRequest validateAdminPwdRequest = new IacValidateAdminPwdRequest();
            validateAdminPwdRequest.setPwd(request.getPwd());
            validateAdminPwdRequest.setUserName(loginUserInfo.getUserName());
            validateAdminPwdRequest.setKey(RedLineUtil.getRealAdminRedLine());
            validateAdminPwdRequest.setSubSystem(SubSystem.CDC);
            baseAdminMgmtAPI.validateAdminPwd(validateAdminPwdRequest);

        } catch (BusinessException ex) {
            handleValidatePwdFailException(ex, sessionContext);
            throw ex;
        }
        return CommonWebResponse.success();
    }

    private void handleValidatePwdFailException(BusinessException ex, SessionContext sessionContext) throws BusinessException {
        String userName = sessionContext.getUserName();
        LOGGER.error(String.format("管理员[%s]二次密码确认异常，异常原因：", userName), ex);

        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_ADMIN_VALIDATE_PASSWORD_FAIL,
                ex.getAttachment(UserTipContainer.Constants.USER_TIP, ex.getI18nMessage()));
    }

    /**
     * 验证管理员用户名是否已存在
     *
     * @param request 请求参数
     * @return 验证结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("验证管理员用户名是否已存在")
    @RequestMapping(value = "validateAdminUserNameExist", method = RequestMethod.POST)
    public CommonWebResponse validateAdminUserNameExist(ValidateAdminUserNameExistRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        IacValidateAdminUserNameExistRequest validateRequest = new IacValidateAdminUserNameExistRequest();
        validateRequest.setSubSystem(SubSystem.CDC);
        validateRequest.setUserName(request.getUserName());
        if (request.getId() != null) {
            validateRequest.setId(request.getId());
        }
        boolean isExit = baseAdminMgmtAPI.validateAdminUserNameExist(validateRequest);
        NameDuplicationVO nameDuplicationVO = new NameDuplicationVO();
        nameDuplicationVO.setHasDuplication(isExit || validateAdminUserNameDefaultAdminName(request.getUserName()));
        return CommonWebResponse.success(nameDuplicationVO);
    }

    /**
     * 判断要创建的管理员名是否为系统默认管理员名
     *
     * @param username
     * @return
     */
    private boolean validateAdminUserNameDefaultAdminName(String username) {
        DefaultAdmin[] adminArr = DefaultAdmin.values();
        for (DefaultAdmin admin : adminArr) {
            if (admin.getDescribe().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是内置管理员
     *
     * @param username
     * @return
     */
    private boolean validateAdminIsDefaultAdmin(String username) {
        DefaultAdmin[] adminArr = DefaultAdmin.values();
        for (DefaultAdmin admin : adminArr) {
            if (admin.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }


    /**
     * @api {POST} /rco/admin/enableAdmin 启用管理员操作
     * @apiName 启用管理员操作
     * @apiGroup 管理员管理相关
     * @apiDescription 启用管理员操作
     * @apiParam (请求路径字段说明) {String} id 管理员ID，取值为UUID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "id": "6e20f88f-8d7d-4182-a7b0-e29ed0dd5df9"
     *                  }
     * @apiSuccess (响应字段说明) {int} code 响应码
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} content 响应数据
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content": "",
     *                    "message": "操作成功",
     *                    "msgArgArr": null,
     *                    "msgKey": "rcdc_aaa_operator_success",
     *                    "status": "SUCCESS"
     *                    }
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "content": null,
     *                  "message": "系统内部错误，请联系管理员",
     *                  "msgArgArr": [],
     *                  "msgKey": "sk_webmvckit_internal_error",
     *                  "status": "ERROR"
     *                  }
     *
     */

    /**
     * 启用管理员请求
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 删除结果
     * @throws BusinessException 异常
     */
    @ApiOperation("启用管理员请求")
    @RequestMapping(value = "enableAdmin", method = RequestMethod.POST)
    public CommonWebResponse enableAdmin(EnableOrDisableAdminWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        IacAdminDTO baseAdminDTO = null;
        try {
            baseAdminDTO = baseAdminMgmtAPI.getAdmin(request.getId());
            /** 已经启用不重复操作 */
            if (baseAdminDTO.getEnabled()) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_ALREAD_ENABLE_ADMIN_ERROR, baseAdminDTO.getUserName());
            }
            /** 不能对自身进行操作 */
            if (baseAdminDTO.getId().equals(sessionContext.getUserId())) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CAN_NOT_OPRATE_YOUSELF);
            }
            // 不能启用或者禁用从域用户升级的管理员
            if (isEditDomainAdminStatus(baseAdminDTO.getUserName(), baseAdminDTO.getEnabled(), Boolean.TRUE)) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_AD_ADMIN_CAN_NOT_ENABLE_OR_DISABLE);
            }
            // 当前管理员是内置安全管理员或者内置超级管理员 不需要做权限判断
            if (!permissionHelper.validateAdminIsAdminOrSecadmin(sessionContext.getUserName())) {
                // 获取当前管理员信息
                IacAdminDTO sessionIacAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
                // 获取当前管理员角色
                IacRoleDTO sessionBaseRole = baseRoleMgmtAPI.getRole(sessionIacAdminDTO.getRoleIdArr()[0]);
                // 获取需要目标的角色信息
                IacRoleDTO targetBaseRole = baseRoleMgmtAPI.getRole(baseAdminDTO.getRoleIdArr()[0]);

                LOGGER.info("禁用管理员，查询当前管理员信息为{}，将禁用管理员信息为{}", JSON.toJSONString(sessionIacAdminDTO), JSON.toJSONString(baseAdminDTO));
                if (Objects.equals(sessionBaseRole.getRoleName(), targetBaseRole.getRoleName())) {
                    // 管理员不能对同级别管理员进行操作
                    throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CAN_NOT_OPRATE_SAME_LEVEL_ADMIN);
                }
            }
            enableOrDisableAdmin(baseAdminDTO, true);
            // 记录成功到审计日志
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_ENABLE_ADMIN_LOG, baseAdminDTO.getUserName());
            // 返回成功
            return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException ex) {
            String userName = baseAdminDTO == null ? request.getId().toString() : baseAdminDTO.getUserName();
            // 记录失败信息到审计日志
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_ENABLE_ADMIN_FAIL_LOG, userName, ex.getI18nMessage());
            // 返回操作失败
            return CommonWebResponse.fail(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, new String[]{ex.getI18nMessage()});
        }
    }

    /**
     * @api {POST} /rco/admin/firstEnableDefaultAdmin 内置管理员首次启用操作
     * @apiName 内置管理员首次启用操作
     * @apiGroup 管理员管理相关
     * @apiDescription 内置管理员首次启用操作
     * @apiParam (请求路径字段说明) {String} id 管理员ID，取值为UUID
     * @apiParam (请求路径字段说明) {String} newPwd 管理员密码
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "id": "6e20f88f-8d7d-4182-a7b0-e29ed0dd5df9",
     *                  "newPwd": "rcos2019"
     *                  }
     * @apiSuccess (响应字段说明) {int} code 响应码
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} content 响应数据
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content": "",
     *                    "message": "操作成功",
     *                    "msgArgArr": null,
     *                    "msgKey": "rcdc_aaa_operator_success",
     *                    "status": "SUCCESS"
     *                    }
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "content": null,
     *                  "message": "系统内部错误，请联系管理员",
     *                  "msgArgArr": [],
     *                  "msgKey": "sk_webmvckit_internal_error",
     *                  "status": "ERROR"
     *                  }
     *
     */

    /**
     * 启用内置管理员请求
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 删除结果
     * @throws BusinessException 异常
     */
    @ApiOperation("启用内置管理员请求")
    @RequestMapping(value = "firstEnableDefaultAdmin", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "firstEnableDefaultAdminPwdValidate")
    public CommonWebResponse firstEnableDefaultAdmin(FirstEnableDefaultAdminWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        IacModifyOtherAdminPwdRequest baseRequest = new IacModifyOtherAdminPwdRequest(request.getId(), request.getNewPwd());
        baseAdminMgmtAPI.modifyOtherAdminPwd(baseRequest);

        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(request.getId());

        /** 已经启用不再执行 */
        if (baseAdminDTO.getEnabled()) {
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_ALREAD_ENABLE_ADMIN_ERROR, baseAdminDTO.getUserName());
        }
        /** 只能对sysadmin、secadmin、audadmin进行操作 */
        validateAdminName(baseAdminDTO);
        /** 不能对自身进行操作 */
        if (baseAdminDTO.getId().equals(sessionContext.getUserId())) {
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CAN_NOT_OPRATE_YOUSELF);
        }

        handleFirstEnable(baseAdminDTO);
        enableOrDisableAdmin(baseAdminDTO, true);

        auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_ENABLE_ADMIN_LOG, baseAdminDTO.getUserName());

        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, StringUtils.EMPTY);
    }

    private void handleFirstEnable(IacAdminDTO dto) throws BusinessException {
        String key = dto.getUserName() + FIRST_ENABLE_SUFFIX;
        FindParameterResponse response = rcoGlobalParameterAPI.findParameter(new FindParameterRequest(key));
        String value = response.getValue();
        if (value != null && Boolean.FALSE.toString().equals(value)) {
            rcoGlobalParameterAPI.updateParameter(new UpdateParameterRequest(key, Boolean.TRUE.toString()));
        } else {
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CAN_NOT_OPRATE_TWICE);
        }
    }

    private void validateAdminName(IacAdminDTO baseAdminDTO) throws BusinessException {
        String adminName = baseAdminDTO.getUserName();
        if (DefaultAdmin.AUDADMIN.getName().equals(adminName) || DefaultAdmin.SECADMIN.getName().equals(adminName)
                || DefaultAdmin.SYSADMIN.getName().equals(adminName)) {
            return;
        }

        throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CAN_NOT_OPRATE);
    }

    /**
     * @api {POST} /rco/admin/disableAdmin 禁用管理员操作
     * @apiName 禁用管理员操作
     * @apiGroup 管理员管理相关
     * @apiDescription 禁用管理员操作
     * @apiParam (请求路径字段说明) {String} id 管理员ID，取值为UUID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "id": "6e20f88f-8d7d-4182-a7b0-e29ed0dd5df9"
     *                  }
     * @apiSuccess (响应字段说明) {int} code 响应码
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} content 响应数据
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content": "",
     *                    "message": "操作成功",
     *                    "msgArgArr": null,
     *                    "msgKey": "rcdc_aaa_operator_success",
     *                    "status": "SUCCESS"
     *                    }
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "content": null,
     *                  "message": "系统内部错误，请联系管理员",
     *                  "msgArgArr": [],
     *                  "msgKey": "sk_webmvckit_internal_error",
     *                  "status": "ERROR"
     *                  }
     *
     */

    /**
     * 禁用管理员请求
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 删除结果
     * @throws BusinessException 异常
     */
    @ApiOperation("禁用管理员请求")
    @RequestMapping(value = "disableAdmin", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = {"禁用管理员"}), @ApiVersion(value = Version.V3_1_1, //
            descriptions = {"禁止禁用admin"})})
    public CommonWebResponse disableAdmin(EnableOrDisableAdminWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        IacAdminDTO baseAdminDTO = null;
        try {
            baseAdminDTO = baseAdminMgmtAPI.getAdmin(request.getId());

            if (!baseAdminDTO.getEnabled()) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_ALREAD_DISABLE_ADMIN_ERROR, baseAdminDTO.getUserName());
            }
            /** 不能对自身进行操作 */
            if (baseAdminDTO.getId().equals(sessionContext.getUserId())) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CAN_NOT_OPRATE_YOUSELF);
            }

            // 不能启用或者禁用从域用户升级的管理员
            if (isEditDomainAdminStatus(baseAdminDTO.getUserName(), baseAdminDTO.getEnabled(), Boolean.FALSE)) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_AD_ADMIN_CAN_NOT_ENABLE_OR_DISABLE);
            }

            if (Objects.equals(DefaultAdmin.ADMIN.getName(), baseAdminDTO.getUserName())) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CAN_NOT_DISABLE, DefaultAdmin.ADMIN.getName());
            }

            // 当前管理员是内置安全管理员或者内置超级管理员 不需要做权限判断
            if (!permissionHelper.validateAdminIsAdminOrSecadmin(sessionContext.getUserName())) {
                // 获取当前管理员信息
                IacAdminDTO sessionIacAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
                // 获取当前管理员角色
                IacRoleDTO sessionBaseRole = baseRoleMgmtAPI.getRole(sessionIacAdminDTO.getRoleIdArr()[0]);
                // 获取需要目标的角色信息
                IacRoleDTO targetBaseRole = baseRoleMgmtAPI.getRole(baseAdminDTO.getRoleIdArr()[0]);

                LOGGER.info("禁用管理员，查询当前管理员信息为{}，将禁用管理员信息为{}", JSON.toJSONString(sessionIacAdminDTO), JSON.toJSONString(baseAdminDTO));
                if (Objects.equals(sessionBaseRole.getRoleName(), targetBaseRole.getRoleName())) {
                    // 管理员不能对同级别管理员进行操作
                    throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CAN_NOT_OPRATE_SAME_LEVEL_ADMIN);
                }
            }

            enableOrDisableAdmin(baseAdminDTO, false);
            // 记录禁用管理员成功审计日志
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_DISABLE_ADMIN_LOG, baseAdminDTO.getUserName());
            // 退出会话
            sessionContextRegistry.logoutSessionContext(request.getId());
            // 返回操作成功
            return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException ex) {
            String userName = baseAdminDTO == null ? request.getId().toString() : baseAdminDTO.getUserName();
            // 记录禁用管理员失败审计日志
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_DISABLE_ADMIN_FAIL_LOG, userName, ex.getI18nMessage());
            // 返回操作失败
            return CommonWebResponse.fail(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, new String[]{ex.getI18nMessage()});
        }
    }

    private void enableOrDisableAdmin(IacAdminDTO baseAdminDTO, boolean enable) throws BusinessException {
        IacUpdateAdminRequest request = new IacUpdateAdminRequest();
        request.setEnabled(enable);
        request.setId(baseAdminDTO.getId());
        request.setUserName(baseAdminDTO.getUserName());
        request.setDescribe(baseAdminDTO.getDescribe());
        request.setEmail(baseAdminDTO.getEmail());
        request.setRealName(baseAdminDTO.getRealName());
        request.setRoleIdArr(baseAdminDTO.getRoleIdArr());
        IacUserDetailDTO userDetail = cbbUserAPI.getUserByName(baseAdminDTO.getUserName());
        if (Objects.nonNull(userDetail)) {
            request.setUserType(IacUserTypeEnum.getUserDomainType(userDetail.getUserType()));
        }
        baseAdminMgmtAPI.updateAdmin(request);
    }

    /**
     * 管理员登录验证
     *
     * @param request        登录请求对象
     * @param sessionContext session上下文
     * @throws BusinessException 业务异常
     */
    private void validateLoginAdmin(LoginAdminWebRequest request, SessionContext sessionContext) throws BusinessException {
        Long lastTimestamp = sessionContext.getAttribute(TIMESTAMP);
        if (request.getTimestamp().equals(lastTimestamp)) {
            LOGGER.debug("当前时刻管理员[{}]已登录，登录失败", request.getUserName());
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_ALREADY_LOGIN_CURRENT_TIME, request.getUserName());
        }
    }

    /**
     * 登录操作session处理
     *
     * @param request        登录请求对象
     * @param sessionContext session上下文
     * @param baseAdminDTO   API层登录验证返回结果对象
     */
    private void loginSessionHandle(LoginAdminWebRequest request, SessionContext sessionContext, IacAdminDTO baseAdminDTO) {
        // 先注销之前账号
        sessionContext.logout();

        // 重建session信息
        sessionContext.recreate();

        // SessionEventOptLogRecord
        SessionEventOptLogRecord record = new SessionEventOptLogRecord(request.getUserName());

        // 记录最新的登录数据
        sessionContext.loginSuccess(baseAdminDTO.getId(), request.getUserName(), record);
        sessionContext.addAttribute(TIMESTAMP, request.getTimestamp());

    }

    /**
     * 构建API层新增管理员Request对象
     *
     * @param request controller层新增管理员request对象
     * @return API层新增管理员Request对象
     */
    private CreateAdminRequest buildCreateAdminRequest(CreateAdminWebRequest request) {
        CreateAdminRequest createAdminRequest = new CreateAdminRequest();
        createAdminRequest.setUserName(request.getUserName());
        createAdminRequest.setRealName(request.getRealName());
        createAdminRequest.setPwd(request.getPwd());
        createAdminRequest.setEmail(request.getEmail());
        createAdminRequest.setDescribe(request.getDescribe());
        createAdminRequest.setTerminalGroupIdArr(request.getTerminalGroupArr());
        createAdminRequest.setUserGroupIdArr(request.getUserGroupArr());
        createAdminRequest.setRoleId(request.getRoleId());
        createAdminRequest.setEnabled(request.getEnabled());
        // 镜像组
        createAdminRequest.setImageArr(request.getImageArr());
        createAdminRequest.setDesktopPoolArr(request.getDesktopPoolArr());
        createAdminRequest.setDiskPoolArr(request.getDiskPoolArr());
        createAdminRequest.setDeskStrategyArr(request.getDeskStrategyArr());
        return createAdminRequest;
    }

    /**
     * 构建API层升级管理员Request对象
     *
     * @param request    controller层新增管理员request对象
     * @param userDetail 用户信息
     * @return API层新增管理员Request对象
     */
    private UpgradeAdminRequest buildUpgradeAdminRequest(UpgradeAdminWebRequest request, IacUserDetailDTO userDetail) {
        // 将前端修改的邮箱 设置给用户
        userDetail.setEmail(request.getEmail());
        BeanUtils.copyProperties(userDetail, request);
        UpgradeAdminRequest upgradeAdminRequest = new UpgradeAdminRequest();
        BeanUtils.copyProperties(request, upgradeAdminRequest);
        // 终端组
        upgradeAdminRequest.setTerminalGroupIdArr(request.getTerminalGroupArr());
        // 用户组
        upgradeAdminRequest.setUserGroupIdArr(request.getUserGroupArr());
        // 角色ID
        upgradeAdminRequest.setRoleId(request.getRoleId());

        upgradeAdminRequest.setEnabled(request.getEnabled());
        // 镜像组
        upgradeAdminRequest.setImageArr(request.getImageArr());
        // 升级管理员需要新增映射表的数据，并且创建随机符合AAA组件管理员密码的写入映射表中，
        // 关联管理员和用户，并且更新CBB组件维护的用户表的user_role角色维度为管理
        // 设置账户名
        upgradeAdminRequest.setRealName(userDetail.getRealName());
        // 用户名
        upgradeAdminRequest.setUserName(userDetail.getUserName());
        // CBB用户使用的AES加密KEY和AAA不一致，将密码从CBB密文转成AAA密文
        String decryptPwd = AesUtil.descrypt(userDetail.getPassword(), RedLineUtil.getRealUserRedLine());
        String encryptPwd = AesUtil.encrypt(decryptPwd, RedLineUtil.getRealAdminRedLine());

        // 密码
        upgradeAdminRequest.setPwd(encryptPwd);
        upgradeAdminRequest.setDesktopPoolArr(request.getDesktopPoolArr());
        upgradeAdminRequest.setDiskPoolArr(request.getDiskPoolArr());
        upgradeAdminRequest.setDeskStrategyArr(request.getDeskStrategyArr());
        upgradeAdminRequest.setAppPoolArr(request.getAppPoolArr());
        upgradeAdminRequest.setAppMainStrategyArr(request.getAppMainStrategyArr());
        upgradeAdminRequest.setAppPeripheralStrategyArr(request.getAppPeripheralStrategyArr());

        return upgradeAdminRequest;
    }

    /**
     * 删除管理员验证
     *
     * @param sessionContext
     * @param idArr
     * @throws BusinessException
     */
    private void validateDeleteAdmin(SessionContext sessionContext, UUID[] idArr) throws BusinessException {
        for (UUID id : idArr) {
            if (id.equals(sessionContext.getUserId())) {
                LOGGER.debug("删除管理员操作，禁止删除当前登录的管理员，管理员id[{}]", id);
                auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_DELETE_ADMIN_DO_FAIL, sessionContext.getUserName(),
                        LocaleI18nResolver.resolve(AaaBusinessKey.RCDC_AAA_ADMIN_DELETE_CURRENT_LOGIN_FORBID, sessionContext.getUserName()));
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_DELETE_CURRENT_LOGIN_FORBID, sessionContext.getUserName());
            }
        }
    }

    /**
     * 构建API层更新管理员Request对象
     *
     * @param request controller层更新管理员request对象
     * @return API层更新管理员Request对象
     */
    private UpdateAdminRequest buildUpdateAdminRequest(UpdateAdminWebRequest request) {
        UpdateAdminRequest updateAdminRequest = new UpdateAdminRequest();
        updateAdminRequest.setId(request.getId());
        updateAdminRequest.setUserName(request.getUserName());
        updateAdminRequest.setRealName(request.getRealName());
        updateAdminRequest.setEmail(request.getEmail());
        updateAdminRequest.setDescribe(request.getDescribe());
        updateAdminRequest.setEnabled(request.getEnabled());
        updateAdminRequest.setRoleId(request.getRoleId());
        updateAdminRequest.setTerminalGroupIdArr(request.getTerminalGroupArr());
        updateAdminRequest.setUserGroupIdArr(request.getUserGroupArr());
        // 镜像权限
        updateAdminRequest.setImageIdArr(request.getImageArr());
        updateAdminRequest.setDesktopPoolArr(request.getDesktopPoolArr());
        updateAdminRequest.setDiskPoolArr(request.getDiskPoolArr());
        updateAdminRequest.setDeskStrategyArr(request.getDeskStrategyArr());
        return updateAdminRequest;
    }

    /**
     * 构建API层更新管理员数据权限Request对象
     *
     * @param request controller层更新管理员request对象
     * @return API层更新管理员Request对象
     */
    private UpdateAdminDataPermissionRequest buildUpdateAdminDataPermissionRequest(UpdateAdminDataPermissionWebRequest request) {
        UpdateAdminDataPermissionRequest updateAdminRequest = new UpdateAdminDataPermissionRequest();
        updateAdminRequest.setId(request.getId());
        updateAdminRequest.setTerminalGroupIdArr(obtainStringIdArrFromIdLabelArr(request.getTerminalGroupArr()));
        updateAdminRequest.setUserGroupIdArr(obtainStringIdArrFromIdLabelArr(request.getUserGroupArr()));
        // 镜像权限
        updateAdminRequest.setImageIdArr(obtainUUIdArrFromIdLabelArr(request.getImageArr()));
        updateAdminRequest.setDesktopPoolArr(obtainUUIdArrFromIdLabelArr(request.getDesktopPoolArr()));
        updateAdminRequest.setDiskPoolArr(obtainUUIdArrFromIdLabelArr(request.getDiskPoolArr()));
        updateAdminRequest.setDeskStrategyArr(obtainUUIdArrFromIdLabelArr(request.getDeskStrategyArr()));
        updateAdminRequest.setAppPoolArr(obtainUUIdArrFromIdLabelArr(request.getAppPoolArr()));
        updateAdminRequest.setAppMainStrategyArr(obtainUUIdArrFromIdLabelArr(request.getAppMainStrategyArr()));
        updateAdminRequest.setAppPeripheralStrategyArr(obtainUUIdArrFromIdLabelArr(request.getAppPeripheralStrategyArr()));
        return updateAdminRequest;
    }

    private String[] obtainStringIdArrFromIdLabelArr(GroupIdLabelWebEntry[] terminalGroupArr) {

        if (ArrayUtils.isEmpty(terminalGroupArr)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        return Arrays.stream(terminalGroupArr).map(idLabelEntry -> idLabelEntry.getId()).toArray(String[]::new);
    }

    private UUID[] obtainUUIdArrFromIdLabelArr(IdLabelEntry[] terminalGroupArr) {
        if (ArrayUtils.isEmpty(terminalGroupArr)) {
            return new UUID[0];
        }

        return Arrays.stream(terminalGroupArr).map(idLabelEntry -> idLabelEntry.getId()).toArray(UUID[]::new);
    }

    /**
     * 获取指定AdminDTO数组的角色ID与角色名称对应的缓存
     *
     * @param baseAdminDTOArr 管理员DTO数组
     * @return 角色DTO的缓存
     */
    private Map<UUID, IacRoleDTO> addRoleToRoleMap(IacAdminDTO[] baseAdminDTOArr) throws BusinessException {
        // 遍历管理员对象数组
        Set<UUID> roleIdSet = Sets.newHashSet();
        buildAllAdminRoleIdSet(baseAdminDTOArr, roleIdSet);

        Map<UUID, IacRoleDTO> roleMap = new HashMap<>();
        // 管理员均无角色信息，直接返回空map
        if (CollectionUtils.isEmpty(roleIdSet)) {
            return roleMap;
        }
        UUID[] roleIdArr = new UUID[roleIdSet.size()];

        List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdSet.toArray(roleIdArr));
        buildAllRoleIdAndDTOMap(roleMap, baseRoleDTOList);
        return roleMap;
    }

    /**
     * 构建指定管理员数组的全部角色ID集合
     *
     * @param baseAdminDTOArr 管理员DTO数组
     * @param roleIdSet       角色id集合
     */
    private void buildAllAdminRoleIdSet(IacAdminDTO[] baseAdminDTOArr, Set<UUID> roleIdSet) throws BusinessException {
        for (IacAdminDTO baseAdminDTO : baseAdminDTOArr) {
            IacAdminDTO adminDTO = baseAdminMgmtAPI.getAdmin(baseAdminDTO.getId());
            if (ArrayUtils.isEmpty(adminDTO.getRoleIdArr())) {
                LOGGER.debug("管理员[{}]无角色信息", adminDTO.getUserName());
                continue;
            }
            roleIdSet.addAll(Arrays.asList(adminDTO.getRoleIdArr()));
        }
    }

    /**
     * 添加指定角色集的id与角色DTO关系到map中
     *
     * @param roleMap         角色id与角色DTO对应关系map
     * @param baseRoleDTOList 角色DTO集合
     */
    private void buildAllRoleIdAndDTOMap(Map<UUID, IacRoleDTO> roleMap, List<IacRoleDTO> baseRoleDTOList) {
        for (IacRoleDTO baseRoleDTO : baseRoleDTOList) {
            if (roleMap.containsKey(baseRoleDTO.getId())) {
                LOGGER.debug("map缓存中已包含角色信息，角色id[{}]", baseRoleDTO.getId());
                continue;
            }
            roleMap.put(baseRoleDTO.getId(), baseRoleDTO);
        }
    }

    /**
     * 构建返回对象AdminVO
     *
     * @param baseAdminDTO adminDTO对象
     * @param roleMap      包含角色ID与角色DTO的缓存
     * @return 构建完成的AdminVO
     */
    private AdminVO buildAdminVO(IacAdminDTO baseAdminDTO, Map<UUID, IacRoleDTO> roleMap) throws BusinessException {
        AdminVO adminVO = new AdminVO();
        LOGGER.info("baseAdminDTO: {}", JSON.toJSONString(baseAdminDTO));
        BeanUtils.copyProperties(baseAdminDTO, adminVO);
        LOGGER.info("adminVO: {}", JSON.toJSONString(adminVO));
        adminVO.setHasDefault(baseAdminDTO.isHasDefault());
        IacAdminLockQueryRequest request = new IacAdminLockQueryRequest();
        request.setUserName(baseAdminDTO.getUserName());
        request.setSecurityPolicy(IacSecurityPolicy.ACCOUNT);
        request.setSubSystem(SubSystem.CDC);

        IacLockInfoDTO lockInfo = baseAdminMgmtAPI.getLockInfo(request);
        if (lockInfo != null && lockInfo.getLockStatus() == IacLockStatus.LOCKED) {
            adminVO.setLock(true);
        } else {
            adminVO.setLock(false);
        }
        Boolean enableEvaluation = evaluationAPI.getEvaluationStrategy();
        adminVO.setEnableEvaluation(enableEvaluation);
        buildRoleIdArr(adminVO, baseAdminDTO, roleMap);
        // 通过管理员名称查询普通用户信息
        IacUserDetailDTO userDetail = userAPI.getUserByName(baseAdminDTO.getUserName());
        // 如果当前用户不为空 并且是已设置为管理员 添加进入
        if (userDetail != null && UserRoleEnum.ADMIN.name().equals(userDetail.getUserRole())) {
            adminVO.setUserDetailDTO(userDetail);

            // 非普通用户管理员，无需修改密码
            if (userDetail.getUserType() == IacUserTypeEnum.THIRD_PARTY ||
                    userDetail.getUserType() == IacUserTypeEnum.AD || userDetail.getUserType() == IacUserTypeEnum.LDAP) {
                adminVO.setHasFirstTimeLoggedIn(false);
            }
        }
        adminVO.setEnableFullSystemDisk(enableFullSystemDiskAPI.getEnableFullSystemDisk());
        //设置极简部署
        TerminalSimplifyDeploymentConfigDTO config = terminalSimplifyDeploymentConfigAPI.getTerminalSimplifyDeploymentConfig();
        adminVO.setEnableTerminalSimplifyDeployment(config.getEnableTerminalSimplifyDeployment());

        return adminVO;
    }

    /**
     * 构建角色Id与角色名称的数组对象
     *
     * @param adminVO      VO对象
     * @param baseAdminDTO DTO对象
     * @param roleMap      缓存对象
     */
    private void buildRoleIdArr(AdminVO adminVO, IacAdminDTO baseAdminDTO, Map<UUID, IacRoleDTO> roleMap) {
        if (ArrayUtils.isEmpty(baseAdminDTO.getRoleIdArr())) {
            LOGGER.debug("管理员[{}]无角色信息", baseAdminDTO.getUserName());
            return;
        }
        List<IdLabelEntry> idLabelEntryList = new ArrayList<>();
        for (int i = 0; i < baseAdminDTO.getRoleIdArr().length; i++) {
            UUID roleId = baseAdminDTO.getRoleIdArr()[i];
            IacRoleDTO baseRoleDTO = roleMap.get(roleId);
            String roleName = baseRoleDTO.getRoleName();
            idLabelEntryList.add(IdLabelEntry.build(roleId, roleName));
            if (baseRoleDTO.getHasSuperPrivilege()) {
                adminVO.setHasSuperPrivilege(true);
            }
        }
        adminVO.setRoleArr(idLabelEntryList.toArray(new IdLabelEntry[idLabelEntryList.size()]));
    }


    /**
     * 管理员退出
     *
     * @param sessionContext session上下文
     */
    private void logout(SessionContext sessionContext) throws BusinessException {
        UUID userId = sessionContext.getUserId();
        sessionContext.logout();
        sessionContextRegistry.removeSessionContext(userId);
    }

    /**
     * 构建修改密码API层请求对象
     *
     * @param request controller层请求对象
     * @return API层请求对象
     */
    private IacModifyAdminPwdRequest buildModifyAdminPwdRequest(ModifyAdminPwdWebRequest request, SessionContext sessionContext) {
        UUID id = sessionContext.getUserId();
        IacModifyAdminPwdRequest baseRequest = new IacModifyAdminPwdRequest();
        baseRequest.setId(id);
        baseRequest.setNewPwd(request.getNewPwd());
        baseRequest.setOldPwd(request.getOldPwd());
        return baseRequest;
    }

    /**
     * CMS对接：新建管理员触发同步
     */
    private void syncAdminByCreate(CreateAdminRequest baseRequest) throws BusinessException {
        SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
        // 角色ID
        List<UUID> idList = new ArrayList<>();
        idList.add(baseRequest.getRoleId());
        UUID[] idArr = idList.toArray(new UUID[0]);
        superPrivilegeRequest.setRoleIdArr(idArr);
        SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
        if (superPrivilegeResponse.isSuperPrivilege()) {
            SyncAdminRequest syncAdminRequest = new SyncAdminRequest();
            syncAdminRequest.setOper(CmsDockingAdminOperNotifyEnum.SYNC_ADMIN.getOper());
            syncAdminRequest.setName(baseRequest.getUserName());
            LOGGER.info("同步CMS创建管理员的信息为：[{}]", JSON.toJSONString(syncAdminRequest));
            cmsDockingAPI.syncAdmin(syncAdminRequest);
        }
    }

    /**
     * CMS对接：修改管理员信息触发同步
     */
    private void syncAdminByUpdate(UpdateAdminRequest baseRequest, IacAdminDTO baseAdminDTO) {
        try {
            SuperPrivilegeResponse oldResponse = adminManageAPI.isSuperPrivilege(new SuperPrivilegeRequest(baseAdminDTO.getRoleIdArr()));
            // 添加角色数组
            List<UUID> idList = new ArrayList<>();
            idList.add(baseRequest.getRoleId());
            UUID[] idArr = idList.toArray(new UUID[idList.size()]);
            SuperPrivilegeResponse newResponse = adminManageAPI.isSuperPrivilege(new SuperPrivilegeRequest(idArr));
            boolean isSuperPrivilegeOld = oldResponse.isSuperPrivilege();
            boolean isSuperPrivilegeNew = newResponse.isSuperPrivilege();
            if (isSuperPrivilegeOld && !isSuperPrivilegeNew) {
                SyncAdminRequest syncAdminRequest = new SyncAdminRequest();
                syncAdminRequest.setOper(CmsDockingAdminOperNotifyEnum.SYNC_DEL_ADMIN.getOper());
                syncAdminRequest.setName(baseRequest.getUserName());
                cmsDockingAPI.syncAdmin(syncAdminRequest);
                return;
            }

            if ((isSuperPrivilegeNew && !isSuperPrivilegeOld)
                    || (isSuperPrivilegeNew && isSuperPrivilegeOld && !baseRequest.getUserName().equals(baseAdminDTO.getUserName()))) {
                SyncAdminRequest syncAdminRequest = new SyncAdminRequest();
                syncAdminRequest.setOper(CmsDockingAdminOperNotifyEnum.SYNC_ADMIN.getOper());
                syncAdminRequest.setName(baseRequest.getUserName());
                IdRequest idRequest = new IdRequest();
                idRequest.setId(baseRequest.getId());
                GetAdminPasswordResponse getAdminPasswordResponse = adminManageAPI.getAdminPassword(idRequest);
                syncAdminRequest.setPassword(getAdminPasswordResponse.getPassword());
                cmsDockingAPI.syncAdmin(syncAdminRequest);
            }
        } catch (Exception e) {
            LOGGER.error("CMS对接：修改管理员信息触发同步失败。adminName = {}", baseRequest.getUserName(), e);
        }
    }

    /**
     * CMS对接：修改管理员密码触发同步
     */
    private void syncAdminByUpdatePwd(IacModifyAdminPwdRequest baseRequest) {
        try {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(baseRequest.getId());

            SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
            superPrivilegeRequest.setRoleIdArr(baseAdminDTO.getRoleIdArr());
            SuperPrivilegeResponse response = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
            if (response.isSuperPrivilege()) {
                SyncAdminRequest syncAdminRequest = new SyncAdminRequest();
                syncAdminRequest.setOper(CmsDockingAdminOperNotifyEnum.SYNC_ADMIN.getOper());
                syncAdminRequest.setName(baseAdminDTO.getUserName());
                LOGGER.info("同步CMS管理员修改密码的信息为：[{}]", JSON.toJSONString(syncAdminRequest));
                String decryptPassword = AesUtil.descrypt(baseRequest.getNewPwd(), RedLineUtil.getRealAdminRedLine());
                syncAdminRequest.setPassword(baseRequest.getNewPwd());

                cmsDockingAPI.syncAdmin(syncAdminRequest);
            }
        } catch (Exception e) {
            LOGGER.error("CMS对接：修改管理员密码触发同步失败。id = {}", baseRequest.getId(), e);
        }
    }

    /**
     * CMS对接：管理员重置密码触发同步
     */
    private void syncAdminByResetPwd(IacAdminDTO baseAdminDTO, IacModifyOtherAdminPwdRequest baseRequest) throws BusinessException {
        SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
        superPrivilegeRequest.setRoleIdArr(baseAdminDTO.getRoleIdArr());
        SuperPrivilegeResponse response = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
        if (response.isSuperPrivilege()) {
            SyncAdminRequest syncAdminRequest = new SyncAdminRequest();
            syncAdminRequest.setOper(CmsDockingAdminOperNotifyEnum.SYNC_ADMIN.getOper());
            syncAdminRequest.setName(baseAdminDTO.getUserName());
            LOGGER.info("同步CMS管理员重置密码的信息为：[{}]", JSON.toJSONString(syncAdminRequest));
            String decryptPassword = AesUtil.descrypt(baseRequest.getNewPwd(), RedLineUtil.getRealAdminRedLine());
            syncAdminRequest.setPassword(decryptPassword);

            cmsDockingAPI.syncAdmin(syncAdminRequest);
        }
    }

    private void validateAdminNameAndPwd(String userName, String pwd) throws BusinessException {
        String decryptPwd = AesUtil.descrypt(pwd, RedLineUtil.getRealAdminRedLine());
        if (Objects.equals(userName, decryptPwd)) {
            LOGGER.error("管理员名称和密码不能相同，管理员名称：[{}]", userName);
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_NAME_SHOULD_NOT_EQUAL_PASSWORD);
        }
    }

    /**
     * 设置管理员密码、终端管理密码是否为弱密码
     *
     * @param adminVO      管理员VO
     * @param baseAdminDTO 管理员DTO
     */
    private void setAdminWeakPassword(AdminVO adminVO, IacAdminDTO baseAdminDTO) {
        // 终端管理密码是否为弱密码
        boolean isWeakTerminalPwd = isWeakTerminalPassword();
        // 管理员是否为弱密码
        adminVO.setWeakAdminPassword(Boolean.TRUE.equals(baseAdminDTO.getWeakPassword()));
        adminVO.setWeakTerminalPassword(isWeakTerminalPwd);
    }

    /**
     * 终端管理密码是否为弱密码
     *
     * @return 是否弱密码
     */
    private boolean isWeakTerminalPassword() {
        String encryptPwd;
        try {
            encryptPwd = cbbTerminalOperatorAPI.queryPassword();
        } catch (BusinessException e) {
            LOGGER.error("获取终端管理密码失败：", e);
            return false;
        }
        String decryptPwd = AesUtil.descrypt(encryptPwd, RedLineUtil.getRealAdminRedLine());

        return !Pattern.matches(Constants.TERMINAL_MNG_PWD_REGEX, decryptPwd);
    }

    /**
     * 管理员密码是否为弱密码
     *
     * @param adminVO  管理员VO类
     * @param password 管理员密码
     * @return 是否弱密码
     */
    private boolean isWeakAdminPassword(AdminVO adminVO, String password) {
        // 如果管理员是从AD域用户、LDAP域用户升级上来，则不需要提示是否为弱密码
        IacUserDetailDTO userDetail = adminVO.getUserDetailDTO();
        if (userDetail != null && (IacUserTypeEnum.AD == userDetail.getUserType()
                || IacUserTypeEnum.LDAP == userDetail.getUserType()
                || IacUserTypeEnum.THIRD_PARTY == userDetail.getUserType())) {
            return false;
        }
        return !Pattern.matches(Constants.ADMIN_PASSWORD_REGEX, password);
    }

    /**
     * 判断是否修改了域管理员的禁用状态
     *
     * @param userName      管理员名称
     * @param isAdminEnable 数据库中管理员是否启用
     * @param newStatus     新的启用状态
     * @return boolean
     */
    private boolean isEditDomainAdminStatus(String userName, Boolean isAdminEnable, Boolean newStatus) throws BusinessException {
        return isDomainAdmin(userName) && !newStatus.equals(isAdminEnable);
    }

    /**
     * 判断管理员是否为域用户升级上来的
     *
     * @param userName 管理员名称
     * @return boolean
     */
    private boolean isDomainAdmin(String userName) throws BusinessException {
        IacUserDetailDTO userDetail = cbbUserAPI.getUserByName(userName);
        // 用户为管理员，并且用户类型为AD或者LDAP
        return userDetail != null && UserRoleEnum.ADMIN.name().equals(userDetail.getUserRole())
                && (userDetail.getUserType() == IacUserTypeEnum.AD || userDetail.getUserType() == IacUserTypeEnum.LDAP
                || userDetail.getUserType() == IacUserTypeEnum.THIRD_PARTY);
    }

    /**
     * SessionEventSPI实现类
     * Description: SessionEventSPI实现类
     * Copyright: Copyright (c) 2018
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019年3月19日
     *
     * @author lin
     */
    private class SessionEventOptLogRecord implements SessionEventSPI {

        private String userName;

        SessionEventOptLogRecord(String userName) {
            this.userName = userName;
        }

        @Override
        public void onLogout() {
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGOUT_SUCCESS);
        }

        @Override
        public void onKickout() {
            LOGGER.info("管理员[{}]被踢出,记录日志", userName);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGOUT_KICKOUT_LOG,  userName);
        }
    }
}
