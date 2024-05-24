package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.base.iac.module.enums.AuthUserTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.MainAuthTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.authtype.IacMainAuthenticationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.authtype.IacSupportMainAuthenticationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacHardwareCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQrCodeConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacCreateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacQrCodeType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVOIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UpdateUserConfigNotifyContentDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.UserIdentityConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.UserSendMailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.common.UserCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.InvalidTimeHelpUtil;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.CasScanCodeAuthBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.constants.HardwareConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.AssistCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.PrimaryCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.constants.OtpConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.IdvDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.VoiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.UserConfigHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.constant.UserConstant;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.UserOrGroupBatchStateCache;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.CheckDuplicationResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.UserDetailWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.UserInfoWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.UserListWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.CreateUserBatchTaskSubmitResult;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.SessionContextRegistry;
import com.ruijie.rcos.rcdc.rco.module.web.util.CharRandomUtils;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.rcdc.rco.module.web.validation.UserCustomValidation;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月12日
 *
 * @author jarman
 */
@Api(tags = "用户管理")
@Controller
@RequestMapping("/rco/user")
@EnableCustomValidate(validateClass = UserCustomValidation.class)
public class UserController {

    private static final Cache<String, String> CACHE_MAP;

    /**
     * 普通用户初始密码
     */
    private static final String INIT_PASSWORD = "123456";

    /**
     * 访客用户密码
     */
    private static final String VISITOR_PASSWORD = "e29da0335b078bbe46b8bf327692246f";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * 过期时间 0
     */
    public static final long EXPIRE_DATE_ZERO = 0L;

    private static final int INVALID_TIME_MAX_VALUE = 1000;

    private static final int INVALID_TIME_MIN_VALUE = 0;

    private static final Long TIME_CONVERSION_UNIT = 24 * 60 * 60 * 1000L;

    private static final Long EXPIRE_TIME_ZERO = 0L;

    private static final String DATE_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

    private static final Integer DESCRIPTION_MAX_LENGTH = 128;

    static {
        CACHE_MAP = CacheBuilder.newBuilder().expireAfterWrite(6L, TimeUnit.SECONDS).build();
    }

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    private RcoUserIdentityConfigAPI rcoUserIdentityConfigAPI;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserMessageAPI userMessageAPI;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserConfigHelper userConfigHelper;

    @Autowired
    private ModifyPasswordAPI modifyPasswordAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private CasScanCodeAuthParameterAPI scanCodeAuthParameterAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private IacHardwareCertificationAPI hardwareCertificationAPI;

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private DiskPoolUserAPI diskPoolUserAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Autowired
    private SessionContextRegistry sessionContextRegistry;

    @Autowired
    private MailMgmtAPI mailMgmtAPI;

    @Autowired
    private CbbStoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private DesktopTempPermissionAPI desktopTempPermissionAPI;

    @Autowired
    private UserCommonHelper userCommonHelper;

    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private InvalidTimeHelpUtil invalidTimeHelpUtil;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;

    @Autowired
    private IacThirdPartyUserAPI cbbThirdPartyUserAPI;

    @Autowired
    private DataSyncAPI dataSyncAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private RcaGroupMemberAPI rcaGroupMemberAPI;

    @Autowired
    private HostUserAPI hostUserAPI;

    @Autowired
    private IacAuthTypeMgmtAPI iacAuthTypeMgmtAPI;

    @Autowired
    private IacQrCodeAPI iacQrCodeAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;
    
    @Autowired
    private UserEventNotifyAPI userEventNotifyAPI;

    /**
     * @api {POST} /rco/user/create 创建用户
     * @apiName 创建用户
     * @apiGroup 用户管理相关
     * @apiDescription 创建用户
     * @apiParam (请求体字段说明) {String} userType 用户类型
     * @apiParam (请求路径字段说明) {String} userName 用户名
     * @apiParam (请求体字段说明) {Object[]} userGroup 用户组
     * @apiParam (请求路径字段说明) {String} realName 真实名称
     * @apiParam (请求路径字段说明) {String} pwd 密码
     * @apiParam (请求路径字段说明) {string} email 邮箱
     * @apiParam (请求路径字段说明) {string} phoneNum 手机号
     * @apiParam (请求体字段说明) {String} loginIdentityLevel 双网身份验证
     * @apiParam (请求体字段说明) {Object[]} vdiDesktopConfig VDI云桌面配置
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "userType": "NORMAL",
     *                  "userName": "test",
     *                  "userGroup": {
     *                  "id": "a6cfb26b-9b86-4795-afca-8f412412f1e0",
     *                  "label": "123",
     *                  "row": {
     *                  "allowDelete": true,
     *                  "children": [],
     *                  "disabled": false,
     *                  "enableAd": false,
     *                  "enableDefault": false,
     *                  "id": "a6cfb26b-9b86-4795-afca-8f412412f1e0",
     *                  "label": "123",
     *                  "parentId": "root"
     *                  }
     *                  },
     *                  "realName": "test",
     *                  "password": "123456",
     *                  "phoneNum": "12345678911",
     *                  "email": "12345678911@ruijie.com.cn",
     *                  "loginIdentityLevel": "MANUAL_LOGIN",
     *                  "vdiDesktopConfig": {
     *                  "desktopRole": "NORMAL",
     *                  "saveAndContinue": false,
     *                  "strategy": {
     *                  "id": "ab2ebda2-2238-4628-8841-33b6f4494297",
     *                  "label": "ljm",
     *                  "row": {
     *                  "canUsed": true,
     *                  "clipBoardMode": "NO_LIMIT",
     *                  "cloudNumber": 9,
     *                  "cpu": 4,
     *                  "deskStrategyState": "AVAILABLE",
     *                  "desktopType": "PERSONAL",
     *                  "enableDoubleScreen": false,
     *                  "enableInternet": true,
     *                  "enableUsbReadOnly": false,
     *                  "id": "ab2ebda2-2238-4628-8841-33b6f4494297",
     *                  "memory": 8,
     *                  "personalDisk": 60,
     *                  "strategyName": "ljm",
     *                  "systemDisk": 40,
     *                  "usbTypeIdArr": ["78b75b29-0f59-46c0-9de0-0f172eb23063", "e3f8d1ee-1a5e-4b54-a6cb-3249f2239c6a",
     *                  "476cf4dd-68d7-474a-859a-41644933fd5e", "7b5c3b19-bf20-4f46-86d3-4c1e9d42bea7", "fec22ab4-f565-4c91-9401-dd7b5465edcf",
     *                  "5077c12c-bde9-4396-b8fa-e566dea92cbc"]
     *                  }
     *                  },
     *                  "image": {
     *                  "id": "714b71d5-dc96-4070-8636-4fb0bb32a557",
     *                  "label": "win7-2",
     *                  "row": {
     *                  "canUsed": true,
     *                  "clouldDeskopNumOfAppLayer": 0,
     *                  "clouldDeskopNumOfPersonal": 4,
     *                  "clouldDeskopNumOfRecoverable": 0,
     *                  "editErrorMessage": null,
     *                  "editErrorMessageKey": null,
     *                  "id": "714b71d5-dc96-4070-8636-4fb0bb32a557",
     *                  "imageName": "win7-2",
     *                  "imageState": "AVAILABLE",
     *                  "imageSystemSize": 20,
     *                  "imageSystemType": "WIN_7_32",
     *                  "note": null,
     *                  "supportGoldenImage": true,
     *                  "vmState": "NOT"
     *                  }
     *                  },
     *                  "network": {
     *                  "address": {
     *                  "id": "6222590d-5219-41f2-9cc1-c96ad9460c48",
     *                  "label": "ljm2",
     *                  "row": {
     *                  "createTime": 1583821747566,
     *                  "deskNetworkName": "ljm2",
     *                  "deskNetworkState": "AVAILABLE",
     *                  "dnsPrimary": "114.114.114.114",
     *                  "dnsSecondary": null,
     *                  "gateway": "172.28.84.1",
     *                  "id": "6222590d-5219-41f2-9cc1-c96ad9460c48",
     *                  "ipCidr": "172.28.0.0/16",
     *                  "ipPoolArr": [{
     *                  "id": "a5f54b75-d5b3-446b-9279-c350449455e6",
     *                  "ipEnd": "172.28.84.250",
     *                  "ipPoolType": "BUSINESS",
     *                  "ipStart": "172.28.84.200",
     *                  "refCount": 9,
     *                  "totalCount": 51
     *                  }],
     *                  "ipType": "IPV4",
     *                  "networkType": "FLAT",
     *                  "refCount": 9,
     *                  "totalCount": 51,
     *                  "vlan": null
     *                  }
     *                  }
     *                  }
     *                  }
     *                  }
     *
     * @apiSuccess (响应字段说明) {Object[]} content 响应内容
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} msgArgArr 响应信息参数
     * @apiSuccess (响应字段说明) {String} msgKey 响应信息Key
     * @apiSuccess (响应字段说明) {String} status 成功失败状态
     *
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content":
     *                    {
     *                    "id": "8453c7d1-ea99-4683-bea1-43f1c32b7421",
     *                    "taskDesc": "正在创建用户[test]的云桌面",
     *                    "taskId": "cc6a2208-a6a4-4275-9cfa-b97902d4d7d8",
     *                    "taskName": "创建云桌面",
     *                    "taskStatus": "PROCESSING"
     *                    },
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
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
     * 创建用户
     *
     * @param request        页面请求参数
     * @param builder        批量任务
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "创建用户")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = {"创建用户"}),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"创建用户前端传递密码加密"})})
    @EnableCustomValidate(validateMethod = "createUserValidate")
    @RequestMapping(value = "/create")
    @EnableAuthority
    public DefaultWebResponse createUser(CreateUserWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "CreateUserRequest不能为null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        IacCreateUserDTO createUserRequest = buildCreateUserRequest(request);
        String userName = request.getUserName();
        IacUserDetailDTO userDetailDTO;
        try {
            // 先校验UPM策略是否合适
            userConfigHelper.checkUserProfile(request.getIdvDesktopConfig(), request.getVoiDesktopConfig(), request.getVdiDesktopConfig());

            // 新建的普通用户需要校验主要认证策略
            if (IacUserTypeEnum.VISITOR != request.getUserType()) {
                // 校验主要认证策略
                userConfigHelper.validateUserCertification(request.getPrimaryCertificationVO(), request.getAssistCertification());
            }
            // 校验VDI配置
            if (request.getVdiDesktopConfig() != null) {
                userConfigHelper.validateUserVDIConfig(request.getVdiDesktopConfig());
            }
            // 校验用户的身份配置
            if (hasUserIdentityConfig(request.getUserType()) && Objects.isNull(request.getLoginIdentityLevel())) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_CREATE_WITHOUT_LOGIN_IDENTITY_LEVEL, request.getUserType().name());
            }
            // 校验IDV配置和VOI配置配置正确性
            userConfigHelper.validateDesktopConfig(request.getIdvDesktopConfig(), request.getVoiDesktopConfig());
            validateAccountExpireTime(request.getAccountExpireDate());
            validateInvalidTime(request.getInvalidTime());
            validateDescription(request.getUserDescription());
            // 此处不触发同步集群操作
            createUserRequest.setEnableSyncData(false);
            userDetailDTO = userAPI.createUser(createUserRequest);
            createUserDesktopConfig(request, userDetailDTO);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_CREATE_USER_SUCCESS_LOG, userName);
        } catch (BusinessException e) {
            LOGGER.error("创建用户失败", e);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_CREATE_USER_FAIL_LOG, userName, exceptionMsg);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, exceptionMsg);
        }

        UUID userId = userDetailDTO.getId();

        if (hasUserIdentityConfig(userDetailDTO.getUserType())) {
            saveUserIdentityConfig(request.getLoginIdentityLevel(), userId, request.getAssistCertification(), request.getPrimaryCertificationVO());
        }

        // 此处触发同步集群操作
        dataSyncAPI.activeSyncUserData(userId);

        CreateCloudDesktopRequest cloudDesktopRequest = buildCreateDesktopRequest(request, userId);
        //判断是否创建云桌面
        CreateUserBatchTaskSubmitResult userSubmitResult = null;
        if (cloudDesktopRequest != null) {
            VdiDesktopConfigVO desktopConfig = request.getVdiDesktopConfig();
            // 创建云桌面个数，创建普通用户，默认为1
            int createDesktopNum = 1;
            // 配置了云桌面，且是访客用户,获取指定创建的云桌面个数
            if (desktopConfig != null && IacUserTypeEnum.VISITOR == request.getUserType()) {
                createDesktopNum = request.getVdiDesktopConfig().getVisitorDesktopNum();
            }
            // 异步创建用户桌面
            List<DefaultBatchTaskItem> taskItemList = new ArrayList<>(createDesktopNum);
            for (int i = 0; i < createDesktopNum; i++) {
                taskItemList.add(DefaultBatchTaskItem.builder().itemId(userId)
                        .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_ITEM_NAME, new String[] {}).build());
            }

            // 创建异步执行任务
            CreateDesktopBatchTaskHandler handler = new CreateDesktopBatchTaskHandler(taskItemList, cloudDesktopRequest, auditLogAPI)
                    .setCloudDesktopWebService(cloudDesktopWebService).setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI).setUserName(userName);

            BatchTaskSubmitResult submitResult;
            if (createDesktopNum == 1) {
                submitResult = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_SINGLE_TASK_NAME)
                        .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_SINGLE_TASK_DESC, new String[]{userName})
                        .registerHandler(handler).start();
            } else {
                submitResult = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_BATCH_TASK_NAME)
                        .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_BATCH_TASK_DESC).enableParallel().registerHandler(handler).start();
            }
            userSubmitResult = buildUserSubmitResult(userDetailDTO, submitResult);
        }
        //随机密码
        resetRandomPassword(request, createUserRequest, userName);
        return getDefaultWebResponse(userDetailDTO, userSubmitResult);
    }

    private DefaultWebResponse getDefaultWebResponse(IacUserDetailDTO userDetailDTO, CreateUserBatchTaskSubmitResult userSubmitResult) {
        if (userSubmitResult == null) {
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, userDetailDTO);
        } else {
            return DefaultWebResponse.Builder.success(userSubmitResult);
        }
    }

    private void resetRandomPassword(CreateUserWebRequest request, IacCreateUserDTO createUserRequest, String userName) {
        try {
            //随机密码，发送邮件
            if (Boolean.TRUE.equals(request.getPasswordRandom())) {
                //如果用户名查不到则不发送，因为密码是需要发送到对应的用户上
                UUID uuid = userInfoAPI.getUserIdByUserName(createUserRequest.getUserName());
                if (uuid != null) {
                    UserSendMailDTO userSendMailDTO = new UserSendMailDTO();
                    // 密文解密
                    String plaintextPassword = AesUtil.descrypt(createUserRequest.getPassword(), RedLineUtil.getRealUserRedLine());
                    userSendMailDTO.setContent(plaintextPassword);
                    userSendMailDTO.setUserName(createUserRequest.getUserName());
                    userSendMailDTO.setRealName(createUserRequest.getRealName());
                    userSendMailDTO.setEmail(createUserRequest.getEmail());
                    mailMgmtAPI.sendMail(userSendMailDTO);
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_PSW_SEND_SUCCESS_LOG, userName);
                }
            }
        } catch (BusinessException e) {
            LOGGER.error("用户随机密码发送邮箱失败,用户：[{}]", userName, e);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_CREATE_SUCCESS_BUT_PSW_SEND_FAIL, userName, exceptionMsg);
            String mailSendMsg = LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_CREATE_SUCCESS_BUT_PSW_SEND_FAIL,
                    userName,exceptionMsg);
            CACHE_MAP.put(userName, mailSendMsg);
        }
    }

    /**
     * 获取用户邮件发送状态
     *
     * @param userNameWebRequest 用户名称信息
     * @return 返回结果
     */
    @ApiOperation(value = "获取用户密码邮件发送状态")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取用户密码邮件发送状态"})})
    @RequestMapping(value = "/getMailSendStatus")
    @EnableAuthority
    public DefaultWebResponse getMailSendStatus(UserNameWebRequest userNameWebRequest) {
        Assert.notNull(userNameWebRequest, "userNameWebRequest must be not null");
        String mailSendMsg = CACHE_MAP.getIfPresent(userNameWebRequest.getUserName());
        return DefaultWebResponse.Builder.success(StringUtils.defaultString(mailSendMsg));
    }

    private void createUserDesktopConfig(CreateUserWebRequest createUserRequest, IacUserDetailDTO userDetailDTO) throws BusinessException {
        // IDV 配置不为空 进行保存
        IdvDesktopConfigVO idvDesktopConfig = createUserRequest.getIdvDesktopConfig();
        if (idvDesktopConfig != null && idvDesktopConfig.getImage() != null && idvDesktopConfig.getStrategy() != null) {
            CreateUserDesktopConfigRequest desktopConfigRequest =
                    new CreateUserDesktopConfigRequest(userDetailDTO.getId(), UserCloudDeskTypeEnum.IDV);
            desktopConfigRequest.setImageTemplateId(idvDesktopConfig.getImage().getId());
            desktopConfigRequest.setStrategyId(idvDesktopConfig.getStrategy().getId());
            if (idvDesktopConfig.getSoftwareStrategy() != null) {
                desktopConfigRequest.setSoftwareStrategyId(idvDesktopConfig.getSoftwareStrategy().getId());
            }
            IdLabelEntry userProfileStrategy = idvDesktopConfig.getUserProfileStrategy();
            if (userProfileStrategy != null) {
                desktopConfigRequest.setUserProfileStrategyId(userProfileStrategy.getId());
            }
            userDesktopConfigAPI.createOrUpdateUserDesktopConfig(desktopConfigRequest);
        }
        // VOI 配置不为空 进行保存
        VoiDesktopConfigVO voiDesktopConfig = createUserRequest.getVoiDesktopConfig();
        if (voiDesktopConfig != null && voiDesktopConfig.getImage() != null && voiDesktopConfig.getStrategy() != null) {
            CreateUserDesktopConfigRequest desktopConfigRequest =
                    new CreateUserDesktopConfigRequest(userDetailDTO.getId(), UserCloudDeskTypeEnum.VOI);
            desktopConfigRequest.setImageTemplateId(voiDesktopConfig.getImage().getId());
            desktopConfigRequest.setStrategyId(voiDesktopConfig.getStrategy().getId());
            IdLabelEntry userProfileStrategy = voiDesktopConfig.getUserProfileStrategy();
            if (userProfileStrategy != null) {
                desktopConfigRequest.setUserProfileStrategyId(userProfileStrategy.getId());
            }
            if (voiDesktopConfig.getSoftwareStrategy() != null) {
                desktopConfigRequest.setSoftwareStrategyId(voiDesktopConfig.getSoftwareStrategy().getId());
            }
            userDesktopConfigAPI.createOrUpdateUserDesktopConfig(desktopConfigRequest);
        }
    }

    private CreateCloudDesktopRequest buildCreateDesktopRequest(CreateUserWebRequest request, UUID userId) throws BusinessException {
        VdiDesktopConfigVO desktopConfig = request.getVdiDesktopConfig();
        if (desktopConfig == null) {
            // 没有配置云桌面
            return null;
        }
        // 用户配置了云桌面
        CreateCloudDesktopRequest createDesktopRequest = new CreateCloudDesktopRequest();
        createDesktopRequest.setDesktopImageId(desktopConfig.getImage().getId());
        createDesktopRequest.setNetworkId(desktopConfig.getNetwork().getAddress().getId());
        createDesktopRequest.setDesktopIp(desktopConfig.getNetwork().getAddress().getIp());
        createDesktopRequest.setStrategyId(desktopConfig.getStrategy().getId());
        createDesktopRequest.setSoftwareStrategyId(desktopConfig.getSoftwareStrategy() != null ? desktopConfig.getSoftwareStrategy().getId() : null);
        createDesktopRequest.setUserId(userId);
        createDesktopRequest.setDesktopRole(desktopConfig.getDesktopRole());
        IdLabelEntry userProfileStrategy = desktopConfig.getUserProfileStrategy();
        if (userProfileStrategy != null) {
            createDesktopRequest.setUserProfileStrategyId(userProfileStrategy.getId());
        }
        IdLabelEntry cluster = desktopConfig.getCluster();
        if (Objects.nonNull(cluster)) {
            createDesktopRequest.setClusterId(cluster.getId());
            createDesktopRequest.setDeskSpec(deskSpecAPI.buildCbbDeskSpec(cluster.getId(), desktopConfig.toDeskSpec()));
        }
        IdLabelEntry cloudPlatform = desktopConfig.getCloudPlatform();
        if (cloudPlatform != null) {
            createDesktopRequest.setPlatformId(cloudPlatform.getId());
        }
        return createDesktopRequest;
    }

    private IacCreateUserDTO buildCreateUserRequest(CreateUserWebRequest request) throws BusinessException {
        IacCreateUserDTO apiRequest = new IacCreateUserDTO();
        BeanUtils.copyProperties(request, apiRequest);
        if (StringUtils.isNotBlank(request.getRealName())) {
            // 过滤真实姓名前后空格字符
            apiRequest.setRealName(request.getRealName().trim());
        }
        apiRequest.setGroupId(request.getUserGroup().getId());
        // 重置密码,密码支持自定义
        // 如果不是访客，则判断登录是否需要修改密码
        String password = AesUtil.encrypt(VISITOR_PASSWORD, RedLineUtil.getRealUserRedLine());
        if (IacUserTypeEnum.VISITOR != request.getUserType() && IacUserTypeEnum.THIRD_PARTY != request.getUserType()) {
            String initPassword = this.getInitPassword(request);
            Boolean shouldUpdatePwd = certificationStrategyParameterAPI.isNeedUpdatePassword(initPassword);
            LOGGER.info("新建普通用户[{}]是否需要需要修改密码结果为：[{}]", request.getUserName(), shouldUpdatePwd);
            apiRequest.setShouldChangePassword(shouldUpdatePwd);
            password = initPassword;
        }
        apiRequest.setPassword(password);
        long expireTime = request.getAccountExpireDate() == null ? 0L : request.getAccountExpireDate();
        apiRequest.setAccountExpires(expireTime);
        return apiRequest;
    }

    private String getInitPassword(CreateUserWebRequest request) throws BusinessException {
        if (request.getPasswordRandom()) {
            return AesUtil.encrypt(CharRandomUtils.passwordCreate(6), RedLineUtil.getRealUserRedLine());
        }
        try {
            return StringUtils.isBlank(request.getPassword()) ? AesUtil.encrypt(INIT_PASSWORD, RedLineUtil.getRealUserRedLine())
                    : request.getPassword();
        } catch (Exception e) {
            LOGGER.error("解密用户：[{}]失败，失败原因：", e, request.getUserName());
            throw new BusinessException(BusinessKey.RCDC_RCO_DECRYPT_FAIL, e);
        }
    }

    private CreateUserBatchTaskSubmitResult buildUserSubmitResult(IacUserDetailDTO response, BatchTaskSubmitResult submitResult) {
        CreateUserBatchTaskSubmitResult userSubmitResult = new CreateUserBatchTaskSubmitResult();
        userSubmitResult.setId(response.getId());
        userSubmitResult.setTaskId(submitResult.getTaskId());
        userSubmitResult.setTaskName(submitResult.getTaskName());
        userSubmitResult.setTaskDesc(submitResult.getTaskDesc());
        userSubmitResult.setTaskStatus(submitResult.getTaskStatus());
        return userSubmitResult;
    }

    /**
     * @api {POST} /rco/user/edit 编辑用户
     * @apiName 编辑用户
     * @apiGroup 用户管理相关
     * @apiDescription 编辑用户
     * @apiParam (请求体字段说明) {String} userType 用户类型
     * @apiParam (请求路径字段说明) {String} userName 用户名
     * @apiParam (请求体字段说明) {Object[]} userGroup 用户组
     * @apiParam (请求路径字段说明) {String} realName 真实名称
     * @apiParam (请求路径字段说明) {string} email 邮箱
     * @apiParam (请求路径字段说明) {string} phoneNum 手机号
     * @apiParam (请求体字段说明) {String} loginIdentityLevel 双网身份验证
     * @apiParam (请求体字段说明) {String} id 用户ID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "userType": "NORMAL",
     *                  "userName": "test",
     *                  "userGroup": {
     *                  "id": "a6cfb26b-9b86-4795-afca-8f412412f1e0"
     *                  },
     *                  "realName": "test",
     *                  "phoneNum": "12345678911",
     *                  "email": "12345678911@ruijie.com.cn",
     *                  "loginIdentityLevel": "MANUAL_LOGIN",
     *                  "id": "ab2ebda2-2238-4628-8841-33b6f4494297"
     *                  }
     *
     * @apiSuccess (响应字段说明) {Object[]} content 响应内容
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} msgArgArr 响应信息参数
     * @apiSuccess (响应字段说明) {String} msgKey 响应信息Key
     * @apiSuccess (响应字段说明) {String} status 成功失败状态
     *
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content":null,
     *                    "message": "操作成功",
     *                    "msgArgArr": [],
     *                    "msgKey": "rcdc_rco_module_operate_success",
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
     * 提交编辑用户
     *
     * @param request        页面请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @EnableCustomValidate(validateMethod = "updateUserValidate")
    @ApiOperation(value = "编辑用户")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"编辑用户"})})
    @RequestMapping(value = "/edit")
    @EnableAuthority
    public DefaultWebResponse editUser(UpdateUserBasicInfoWebRequest request) throws BusinessException {
        Assert.notNull(request, "UpdateUserBasicInfo不能为null");

        try {
            // 编辑的非访客用户需要校验主要认证策略
            if (IacUserTypeEnum.VISITOR != request.getUserType()) {
                // 校验主要认证策略
                userConfigHelper.validateUserCertification(request.getPrimaryCertificationVO(), request.getAssistCertification());
            }
            UUID userId = request.getId();
            IacUserDetailDTO response = userAPI.getUserDetail(userId);
            IacUpdateUserDTO apiRequest = buildUpdateUserRequest(response, request);
            //校验用户账号失效时间,规避编辑本身已过期的用户信息被拦截
            Long accountExpireDate;
            if (IacUserTypeEnum.AD == response.getUserType()) {
                Long accountExpire = response.getAccountExpireDate();
                accountExpireDate = DateUtil.adDomainTimestampToDate(accountExpire).getTime();
            } else {
                accountExpireDate = response.getAccountExpireDate();
            }
            if (!accountExpireDate.equals(request.getAccountExpireDate())) {
                validateAccountExpireTime(request.getAccountExpireDate());
            }
            validateInvalidTime(request.getInvalidTime());
            validateDescription(request.getUserDescription());
            // 设置不在此处触发
            apiRequest.setEnableSyncData(false);
            userAPI.updateUser(apiRequest);
            if (hasUserIdentityConfig(response.getUserType())) {
                IacUserLoginIdentityLevelEnum loginIdentityLevel = request.getLoginIdentityLevel();
                Assert.notNull(loginIdentityLevel, "loginIdentityLevel不能为null");
                saveUserIdentityConfig(loginIdentityLevel, request.getId(), request.getAssistCertification(), request.getPrimaryCertificationVO());
                // 用户绑定IDV/TCI并修改了动态口令，通知shine
                if (request.getAssistCertification() != null) {
                    LOGGER.info("用户[{}]辅助认证策略产生变更，变更信息为[{}]", response.getUserName(), request.getAssistCertification());
                    UpdateUserConfigNotifyContentDTO dto = new UpdateUserConfigNotifyContentDTO();
                    dto.setUserId(request.getId());
                    boolean enableOpenOtp = BooleanUtils.isTrue(request.getAssistCertification().getOpenOtpCertification());
                    dto.setOpenOtp(enableOpenOtp);
                    rcoUserIdentityConfigAPI.editUserIdentityConfigNotifyShine(dto);
                }
            }

            // 此处触发同步集群操作
            dataSyncAPI.activeSyncUserData(userId);

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_SUCCESS_LOG, request.getUserName());
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑用户失败", e);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_FAIL_LOG, request.getUserName(), exceptionMsg);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, exceptionMsg);
        }
    }

    /**
     * @api {POST} /rco/user/idvConfig/edit 编辑用户idv配置
     * @apiName 编辑用户idv配置
     * @apiGroup 用户管理相关
     * @apiDescription 编辑用户idv配置
     * @apiParam (请求体字段说明) {Object} idvDesktopConfig IDV配置信息
     * @apiParam (请求体字段说明) {String} id 用户ID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "idvDesktopConfig": {
     *                  "strategy": {
     *                  "id": "ab2ebda2-2238-4628-8841-33b6f4494297"
     *                  },
     *                  "image": {
     *                  * "id": "ab2ebda2-2238-4628-8841-33b6f4494297"
     *                  * }
     *                  },
     *                  "id": "ab2ebda2-2238-4628-8841-33b6f4494297"
     *                  }
     *
     * @apiSuccess (响应字段说明) {Object[]} content 响应内容
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} msgArgArr 响应信息参数
     * @apiSuccess (响应字段说明) {String} msgKey 响应信息Key
     * @apiSuccess (响应字段说明) {String} status 成功失败状态
     *
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content":null,
     *                    "message": "操作成功",
     *                    "msgArgArr": [],
     *                    "msgKey": "rcdc_rco_module_operate_success",
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
     * 编辑IDV云桌面
     *
     * @param request        页面请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "编辑IDV云桌面")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"编辑IDV云桌面"})})
    @RequestMapping(value = "/idvConfig/edit")
    @EnableAuthority
    public DefaultWebResponse editIdvConfig(UpdateUserIdvConfigWebRequest request) throws BusinessException {
        Assert.notNull(request, "UpdateUserIdvConfigWebRequest不能为null");

        IacUpdateUserDTO apiRequest = new IacUpdateUserDTO();
        apiRequest.setId(request.getId());
        String userName = request.getId().toString();
        try {
            userConfigHelper.validateDesktopConfig(request.getIdvDesktopConfig(), null);
            UUID userId = request.getId();
            IacUserDetailDTO userDetailDTO = userAPI.getUserDetail(userId);
            userName = userDetailDTO.getUserName();
            BeanUtils.copyProperties(userDetailDTO, apiRequest);

            apiRequest.setUserName(userDetailDTO.getUserName());
            apiRequest.setPhoneNum(userDetailDTO.getPhoneNum());
            apiRequest.setEmail(userDetailDTO.getEmail());
            apiRequest.setGroupId(userDetailDTO.getGroupId());
            apiRequest.setRealName(userDetailDTO.getRealName());
            apiRequest.setUserState(userDetailDTO.getUserState());
            apiRequest.setUserType(userDetailDTO.getUserType());
            apiRequest.setAdUserAuthority(userDetailDTO.getAdUserAuthority());
            apiRequest.setUserRole(userDetailDTO.getUserRole());
            // 设置是否是管理员 标识
            apiRequest.setUserRole(userDetailDTO.getUserRole());
            apiRequest.setAccountExpires(userDetailDTO.getAccountExpireDate());
            userAPI.updateUser(apiRequest);
            CreateUserDesktopConfigRequest createUserDesktopConfigRequest =
                    new CreateUserDesktopConfigRequest(userDetailDTO.getId(), UserCloudDeskTypeEnum.IDV);
            // 获取IDV配置信息
            IdvDesktopConfigVO idvDesktopConfig = request.getIdvDesktopConfig();
            if (null != idvDesktopConfig && idvDesktopConfig.getImage() != null && idvDesktopConfig.getStrategy() != null) {
                createUserDesktopConfigRequest.setImageTemplateId(idvDesktopConfig.getImage().getId());
                createUserDesktopConfigRequest.setStrategyId(idvDesktopConfig.getStrategy().getId());
                if (idvDesktopConfig.getSoftwareStrategy() != null) {
                    createUserDesktopConfigRequest.setSoftwareStrategyId(idvDesktopConfig.getSoftwareStrategy().getId());
                }
                IdLabelEntry userProfileStrategy = idvDesktopConfig.getUserProfileStrategy();
                if (userProfileStrategy != null) {
                    createUserDesktopConfigRequest.setUserProfileStrategyId(userProfileStrategy.getId());
                }
            }
            userDesktopConfigAPI.createOrUpdateUserDesktopConfig(createUserDesktopConfigRequest);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_SUCCESS_LOG, userName);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑用户失败", e);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_FAIL_LOG, userName, exceptionMsg);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, exceptionMsg);
        }
    }

    /**
     * 编辑VOI云桌面
     *
     * @param request        页面请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "编辑VOI云桌面")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"编辑VOI云桌面"})})
    @RequestMapping(value = "/voiConfig/edit")

    @EnableAuthority
    public DefaultWebResponse editVoiConfig(UpdateUserVoiConfigWebRequest request) throws BusinessException {
        Assert.notNull(request, "UpdateUserVoiConfigWebRequest不能为null");

        IacUpdateUserDTO apiRequest = new IacUpdateUserDTO();
        apiRequest.setId(request.getId());
        String userName = request.getId().toString();
        try {
            userConfigHelper.validateDesktopConfig(null, request.getVoiDesktopConfig());

            UUID userId = request.getId();
            IacUserDetailDTO userDetailDTO = userAPI.getUserDetail(userId);
            userName = userDetailDTO.getUserName();

            apiRequest.setUserName(userDetailDTO.getUserName());
            apiRequest.setPhoneNum(userDetailDTO.getPhoneNum());
            apiRequest.setEmail(userDetailDTO.getEmail());
            apiRequest.setGroupId(userDetailDTO.getGroupId());
            apiRequest.setRealName(userDetailDTO.getRealName());
            apiRequest.setUserState(userDetailDTO.getUserState());
            apiRequest.setUserType(userDetailDTO.getUserType());
            apiRequest.setAdUserAuthority(userDetailDTO.getAdUserAuthority());
            // 设置是否是管理员 标识
            apiRequest.setUserRole(userDetailDTO.getUserRole());
            apiRequest.setAccountExpires(userDetailDTO.getAccountExpireDate());
            apiRequest.setInvalidTime(userDetailDTO.getInvalidTime());
            apiRequest.setUserDescription(userDetailDTO.getUserDescription());
            userAPI.updateUser(apiRequest);
            CreateUserDesktopConfigRequest createUserDesktopConfigRequest =
                    new CreateUserDesktopConfigRequest(userDetailDTO.getId(), UserCloudDeskTypeEnum.VOI);
            // 获取VOI配置信息
            VoiDesktopConfigVO voiDesktopConfig = request.getVoiDesktopConfig();
            if (null != voiDesktopConfig && voiDesktopConfig.getImage() != null && voiDesktopConfig.getStrategy() != null) {
                createUserDesktopConfigRequest.setImageTemplateId(voiDesktopConfig.getImage().getId());
                createUserDesktopConfigRequest.setStrategyId(voiDesktopConfig.getStrategy().getId());
                if (voiDesktopConfig.getSoftwareStrategy() != null) {
                    createUserDesktopConfigRequest.setSoftwareStrategyId(voiDesktopConfig.getSoftwareStrategy().getId());
                }
                IdLabelEntry userProfileStrategy = voiDesktopConfig.getUserProfileStrategy();
                if (userProfileStrategy != null) {
                    createUserDesktopConfigRequest.setUserProfileStrategyId(userProfileStrategy.getId());
                }
            }
            userDesktopConfigAPI.createOrUpdateUserDesktopConfig(createUserDesktopConfigRequest);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_SUCCESS_LOG, userName);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑用户失败", e);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_FAIL_LOG, userName, exceptionMsg);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, exceptionMsg);
        }
    }

    private IacUpdateUserDTO buildUpdateUserRequest(IacUserDetailDTO response, UpdateUserBasicInfoWebRequest request) throws BusinessException {
        if (request.getUserType() != null && request.getUserType() != response.getUserType()) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_TYPE_NOT_ALLOW_CHANGE);
        }

        if (!StringUtils.isEmpty(request.getUserName()) && !request.getUserName().equals(response.getUserName())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_NAME_NOT_ALLOW_CHANGE);
        }

        // 访客用户只支持设置用户组属性，其他属性非访客支持设置
        IacUpdateUserDTO apiRequest = new IacUpdateUserDTO();
        if (IacUserTypeEnum.VISITOR != request.getUserType()) {
            BeanUtils.copyProperties(request, apiRequest, "userName");
            if (StringUtils.isNotBlank(request.getRealName())) {
                // 过滤真实姓名前后空格字符
                apiRequest.setRealName(request.getRealName().trim());
            }
            // 域用户不支持在CDC上修改手机号和邮箱信息
            if (IacUserTypeEnum.NORMAL != request.getUserType() && IacUserTypeEnum.THIRD_PARTY != request.getUserType()) {
                apiRequest.setPhoneNum(response.getPhoneNum());
                apiRequest.setEmail(response.getEmail());
            }
        }
        if (request.getState() != null) {
            apiRequest.setUserState(request.getState());
        }
        apiRequest.setGroupId(request.getUserGroup().getId());
        apiRequest.setId(request.getId());
        // 设置用户是否为管理员标识
        apiRequest.setUserRole(response.getUserRole());
        apiRequest.setUserDescription(request.getUserDescription());
        if (ObjectUtils.isNotEmpty(request.getAccountExpireDate())) {
            apiRequest.setAccountExpires(request.getAccountExpireDate());
        }
        //为了退出登录时能够设置失效恢复时间为null
        if (ObjectUtils.isEmpty(request.getInvalidRecoverTime())) {
            apiRequest.setInvalidRecoverTime(response.getInvalidRecoverTime());
        }

        //AD LDAP域的过期时间 使用数据库的时间
        if (IacUserTypeEnum.AD == response.getUserType() || IacUserTypeEnum.LDAP == response.getUserType()) {
            apiRequest.setAccountExpires(response.getAccountExpireDate());
        }
        return apiRequest;
    }

    private void saveUserIdentityConfig(IacUserLoginIdentityLevelEnum loginIdentityLevel, UUID userId, AssistCertificationVO assistCertificationVO,
                                        PrimaryCertificationVO primaryCertificationVO) throws BusinessException {
        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId);

        userIdentityConfigRequest.setLoginIdentityLevel(loginIdentityLevel);
        userIdentityConfigRequest.setOpenAccountPasswordCertification(primaryCertificationVO.getOpenAccountPasswordCertification());
        userIdentityConfigRequest.setOpenCasCertification(primaryCertificationVO.getOpenCasCertification());
        userIdentityConfigRequest.setOpenThirdPartyCertification(primaryCertificationVO.getOpenThirdPartyCertification());
        userIdentityConfigRequest.setOpenWorkWeixinCertification(primaryCertificationVO.getOpenWorkWeixinCertification());
        userIdentityConfigRequest.setOpenFeishuCertification(primaryCertificationVO.getOpenFeishuCertification());
        userIdentityConfigRequest.setOpenDingdingCertification(primaryCertificationVO.getOpenDingdingCertification());
        userIdentityConfigRequest.setOpenOauth2Certification(primaryCertificationVO.getOpenOauth2Certification());
        userIdentityConfigRequest.setOpenRjclientCertification(primaryCertificationVO.getOpenRjclientCertification());
        if (assistCertificationVO != null) {
            userIdentityConfigRequest.setOpenHardwareCertification(assistCertificationVO.getOpenHardwareCertification());
            userIdentityConfigRequest.setMaxHardwareNum(assistCertificationVO.getMaxHardwareNum());
            userIdentityConfigRequest.setOpenOtpCertification(assistCertificationVO.getOpenOtpCertification());
            userIdentityConfigRequest.setOpenSmsCertification(assistCertificationVO.getOpenSmsCertification());
            userIdentityConfigRequest.setOpenRadiusCertification(assistCertificationVO.getOpenRadiusCertification());
        }
        userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);
    }

    /**
     * 获取用户详细基本信息，
     * 用于编辑回显信息，包括IDV云桌面配置信息
     *
     * @api {POST} /rco/user/detail 获取用户详细基本信息
     * @apiName 获取用户详细基本信息
     * @apiGroup 用户管理相关
     * @apiDescription 用户详细基本信息, 用于编辑回显信息，不包括桌面配置
     * @apiParam (请求体字段说明) {String} id 用户ID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "id": "ab2ebda2-2238-4628-8841-33b6f4494297"
     *                  }
     *
     * @apiSuccess (响应字段说明) {Object[]} content 响应内容
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} msgArgArr 响应信息参数
     * @apiSuccess (响应字段说明) {String} msgKey 响应信息Key
     * @apiSuccess (响应字段说明) {String} status 成功失败状态
     *
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content":{
     *                    "content": null,
     *                    "email": "12345678911@ruijie.com.cn",
     *                    "id": "24ec08ba-3c0a-4c94-9bf1-84cbd6006d5f",
     *                    "loginIdentityLevel": "MANUAL_LOGIN",
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
     *                    "phoneNum": "12345678911",
     *                    "realName": "test",
     *                    "state": "ENABLE",
     *                    "status": null,
     *                    "userGroup": {
     *                    "id": "a6cfb26b-9b86-4795-afca-8f412412f1e0",
     *                    "label": "123"
     *                    },
     *                    "userName": "哈哈",
     *                    "userType": "NORMAL"
     *                    },
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
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
     * 获取用户详细基本信息， 用于编辑回显信息，不包括桌面配置
     *
     * @param request 页面请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取用户基本信息,不包括桌面配置")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取用户基本信息"})})
    @RequestMapping(value = "/detail")
    public DefaultWebResponse getUserDetail(UserIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "UserIdWebRequest不能为null");

        UUID userId = request.getId();
        IacUserDetailDTO response = userAPI.getUserDetail(userId);

        return DefaultWebResponse.Builder.success(buildUserDetailForWebResponse(response));
    }

    private UserDetailWebResponse buildUserDetailForWebResponse(IacUserDetailDTO response) throws BusinessException {
        UserDetailWebResponse detailWebResponse = new UserDetailWebResponse();

        BeanUtils.copyProperties(response, detailWebResponse);
        detailWebResponse.setState(response.getUserState());
        detailWebResponse.setUserGroup(IdLabelEntry.build(response.getGroupId(), response.getGroupName()));
        UserDesktopConfigDTO userDesktopConfigDTO = userDesktopConfigAPI.getUserDesktopConfig(response.getId(), UserCloudDeskTypeEnum.IDV);
        if (userDesktopConfigDTO != null && null != userDesktopConfigDTO.getImageTemplateId() && null != userDesktopConfigDTO.getStrategyId()) {
            UUID imageTemplateId = userDesktopConfigDTO.getImageTemplateId();
            CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
            IdvDesktopConfigVO idvDesktopConfigVO = new IdvDesktopConfigVO();
            if (cbbImageTemplateDetailDTO == null) {
                throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_NOT_FOUND, imageTemplateId.toString());
            }
            UUID strategyId = userDesktopConfigDTO.getStrategyId();
            idvDesktopConfigVO.setImage(IdLabelEntry.build(imageTemplateId, cbbImageTemplateDetailDTO.getImageName()));
            CbbDeskStrategyIDVDTO cbbDeskStrategyIDVDTO = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(strategyId);
            if (cbbDeskStrategyIDVDTO == null) {
                LOGGER.warn("不存在idv云桌面策略id：[" + strategyId + "]");
                throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_IDV_NOT_FOUND, strategyId.toString());
            }
            idvDesktopConfigVO.setStrategy(IdLabelEntry.build(strategyId, cbbDeskStrategyIDVDTO.getName()));

            UUID softwareStrategyId = userDesktopConfigDTO.getSoftwareStrategyId();
            if (softwareStrategyId != null) {
                try {
                    SoftwareStrategyDTO softwareStrategyDTO = softwareControlMgmtAPI.findSoftwareStrategyById(softwareStrategyId);
                    idvDesktopConfigVO.setSoftwareStrategy(IdLabelEntry.build(softwareStrategyId, softwareStrategyDTO.getName()));
                } catch (BusinessException e) {
                    LOGGER.warn("用户桌面配置: {} 对应的软控策略: {} 已被删除", response.getId(), softwareStrategyId);
                }
            }
            UUID userProfileStrategyId = userDesktopConfigDTO.getUserProfileStrategyId();
            if (userProfileStrategyId != null) {
                String userProfileStrategyName = getUserProfileStrategyName(userProfileStrategyId);
                idvDesktopConfigVO.setUserProfileStrategy(IdLabelEntry.build(userProfileStrategyId, userProfileStrategyName));
            }
            detailWebResponse.setIdvDesktopConfig(idvDesktopConfigVO);

        }
        // 查询VOI 云桌面配置
        buildVoiUserDesktopConfigDTO(detailWebResponse, response);
        IacUserLoginIdentityLevelEnum userLoginIdentityLevel = getUserLoginIdentityLevel(response.getUserType(), response.getId());
        detailWebResponse.setLoginIdentityLevel(userLoginIdentityLevel);
        // 原查询双网身份认证，更新查询双网认证和辅助认证
        getUserLoginIdentityForDetail(response.getUserType(), response.getId(), detailWebResponse);
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(response.getId());
        detailWebResponse.setInvalid(userDetail.getInvalid());
        detailWebResponse.setAccountExpireDate(invalidTimeHelpUtil.expireDateFormat(response.getAccountExpireDate(), response.getUserType()));
        detailWebResponse.setInvalidDescription(invalidTimeHelpUtil.expireDateFormat(userDetail));
        return detailWebResponse;
    }


    private String setInvalidDescription(Long lastLoginTime, Integer invalidTime) {

        long timeGapValue = new Date().getTime() - lastLoginTime - invalidTime * TIME_CONVERSION_UNIT;
        Long invalidDay;
        if (timeGapValue < EXPIRE_TIME_ZERO) {
            invalidDay = Math.abs(timeGapValue) / TIME_CONVERSION_UNIT;
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ALREADY_INVALID, invalidDay.toString());
        } else {
            invalidDay = timeGapValue / TIME_CONVERSION_UNIT;
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_REMAIN_INVALID_DAY, invalidDay.toString());
        }
    }

    private void getUserLoginIdentityForDetail(IacUserTypeEnum userType, UUID userId, UserDetailWebResponse userDetailWebResponse)
            throws BusinessException {
        if (hasUserIdentityConfig(userType)) {
            IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId);
            IacUserIdentityConfigResponse response = userIdentityConfigAPI.findUserIdentityConfigByRelated(userIdentityConfigRequest);
            userDetailWebResponse.setLoginIdentityLevel(response.getLoginIdentityLevel());
            // 用户特征码开关和全局一致，取用户；不一致取全局；未配置则默认false
            IacHardwareCertificationDTO hardwareCertification = hardwareCertificationAPI.getHardwareCertification();
            boolean enableOpenHardware = HardwareConstants.RCDC_HARDWARE_CERTIFICATION_DEFAULT;
            if (hardwareCertification.getOpenHardware()) {
                enableOpenHardware = BooleanUtils.isTrue(response.getOpenHardwareCertification());
            }
            userDetailWebResponse.setOpenHardwareCertification(enableOpenHardware);
            userDetailWebResponse.setMaxHardwareNum(response.getMaxHardwareNum());

            // 用户动态口令开关和全局一致，取用户；不一致取全局；未配置则默认false
            OtpCertificationDTO otpCertification = otpCertificationAPI.getOtpCertification();
            boolean enableOpenOtp = OtpConstants.RCDC_OTP_CERTIFICATION_DEFAULT;
            if (otpCertification.getOpenOtp()) {
                enableOpenOtp = BooleanUtils.isTrue(response.getOpenOtpCertification());
            }
            IacSmsCertificationDTO smsCertificationDTO = smsCertificationAPI.getBusSmsCertificationStrategy();
            boolean enableSmsAuth = OtpConstants.RCDC_OTP_CERTIFICATION_DEFAULT;
            if (smsCertificationDTO.getEnable()) {
                enableSmsAuth = BooleanUtils.isTrue(response.getOpenSmsCertification());
            }
            userDetailWebResponse.setHasBindOtp(BooleanUtils.isTrue(response.getHasBindOtp()));
            userDetailWebResponse.setOpenOtpCertification(enableOpenOtp);
            userDetailWebResponse.setOpenAccountPasswordCertification(BooleanUtils.toBoolean(response.getOpenAccountPasswordCertification()));
            userDetailWebResponse.setOpenCasCertification(BooleanUtils.toBoolean(response.getOpenCasCertification()));
            userDetailWebResponse.setOpenSmsCertification(enableSmsAuth);
            userDetailWebResponse.setOpenRadiusCertification(response.getOpenRadiusCertification());
            userDetailWebResponse.setOpenThirdPartyCertification(BooleanUtils.toBoolean(response.getOpenThirdPartyCertification()));
            userDetailWebResponse.setOpenWorkWeixinCertification(BooleanUtils.toBoolean(response.getOpenWorkWeixinCertification()));
            userDetailWebResponse.setOpenFeishuCertification(BooleanUtils.toBoolean(response.getOpenFeishuCertification()));
            userDetailWebResponse.setOpenDingdingCertification(BooleanUtils.toBoolean(response.getOpenDingdingCertification()));
            userDetailWebResponse.setOpenOauth2Certification(BooleanUtils.toBoolean(response.getOpenOauth2Certification()));
            userDetailWebResponse.setOpenRjclientCertification(BooleanUtils.toBoolean(response.getOpenRjclientCertification()));
        }
    }


    /**
     * 构造查询VOI 云桌面配置
     *
     * @param detailWebResponse
     * @param response
     * @throws BusinessException
     */
    private void buildVoiUserDesktopConfigDTO(UserDetailWebResponse detailWebResponse, IacUserDetailDTO response) throws BusinessException {
        UserDesktopConfigDTO userDesktopConfigDTO = userDesktopConfigAPI.getUserDesktopConfig(response.getId(), UserCloudDeskTypeEnum.VOI);
        // VOI 云桌面配置不为空
        if (userDesktopConfigDTO != null && null != userDesktopConfigDTO.getImageTemplateId() && null != userDesktopConfigDTO.getStrategyId()) {
            UUID imageTemplateId = userDesktopConfigDTO.getImageTemplateId();
            CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
            VoiDesktopConfigVO voiDesktopConfigVO = new VoiDesktopConfigVO();
            if (cbbImageTemplateDetailDTO == null) {
                throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_NOT_FOUND, imageTemplateId.toString());
            }
            UUID strategyId = userDesktopConfigDTO.getStrategyId();
            voiDesktopConfigVO.setImage(IdLabelEntry.build(imageTemplateId, cbbImageTemplateDetailDTO.getImageName()));
            CbbDeskStrategyVOIDTO cbbDeskStrategyVOIDTO = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(strategyId);
            if (cbbDeskStrategyVOIDTO == null) {
                LOGGER.warn("不存在voi云桌面策略id：[" + strategyId + "]");
                throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_VOI_NOT_FOUND, strategyId.toString());
            }
            voiDesktopConfigVO.setStrategy(IdLabelEntry.build(strategyId, cbbDeskStrategyVOIDTO.getName()));
            UUID softwareStrategyId = userDesktopConfigDTO.getSoftwareStrategyId();
            if (softwareStrategyId != null) {
                try {
                    SoftwareStrategyDTO softwareStrategyDTO = softwareControlMgmtAPI.findSoftwareStrategyById(softwareStrategyId);
                    voiDesktopConfigVO.setSoftwareStrategy(IdLabelEntry.build(softwareStrategyId, softwareStrategyDTO.getName()));
                } catch (BusinessException e) {
                    LOGGER.warn("用户桌面配置: {} 对应的软控策略: {} 已被删除", response.getId(), softwareStrategyId);
                }
            }
            UUID userProfileStrategyId = userDesktopConfigDTO.getUserProfileStrategyId();
            if (userProfileStrategyId != null) {
                String userProfileStrategyName = getUserProfileStrategyName(userProfileStrategyId);
                voiDesktopConfigVO.setUserProfileStrategy(IdLabelEntry.build(userProfileStrategyId, userProfileStrategyName));
            }
            detailWebResponse.setVoiDesktopConfig(voiDesktopConfigVO);
        }
    }

    /**
     * 根据策略ID查询策略名称
     *
     * @param strategyId 用户配置策略ID
     * @return 策略名称
     * @throws BusinessException 业务异常
     */
    private String getUserProfileStrategyName(UUID strategyId) throws BusinessException {
        UserProfileStrategyDTO userProfileStrategyDTO = userProfileMgmtAPI.findUserProfileStrategyById(strategyId);
        return userProfileStrategyDTO.getName();
    }

    /**
     * 获取用户消息记录
     *
     * @param request        请求参数对象
     * @param sessionContext session信息
     * @return 返回消息记录列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取用户列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取用户列表"})})
    @RequestMapping(value = "/list")
    public DefaultWebResponse getUserList(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageWebRequest 不能为null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        PageSearchRequest apiRequest = new UserPageSearchRequest(request);
        if (!userCommonHelper.checkAndAddQueryUserPermission(request, apiRequest, sessionContext)) {
            return buildUserListWebResponse(new DefaultPageResponse<>());
        }
        DefaultPageResponse<UserListDTO> pageResponse = userDesktopConfigAPI.pageQuery(apiRequest);

        return buildUserListWebResponse(pageResponse);
    }



    /**
     * 获取磁盘池用户及分配信息列表
     * <p>
     * 因为前端已在使用这个url，不删除，和"/listWithAssignment"是一样的逻辑
     *
     * @param request        请求参数对象
     * @param sessionContext session信息
     * @return 返回消息记录列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取用户及分配信息列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取用户及分配信息列表"})})
    @RequestMapping(value = "/listWithDiskAssignment", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserListDTO>> getUserWithDiskAssignmentList(
            PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageWebRequest 不能为null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        UserAssignmentPageRequest pageRequest = new UserAssignmentPageRequest(request);
        Assert.notNull(pageRequest.getDiskPoolId(), "diskPoolId can not be null");

        if (!userCommonHelper.checkAndAddQueryUserPermission(request, pageRequest, sessionContext)) {
            // 传的组ID不在权限内直接返回空
            return CommonWebResponse.success(new DefaultPageResponse<>());
        }

        return CommonWebResponse.success(userDesktopConfigAPI.pageQueryWithAssignment(pageRequest));
    }

    /**
     * 获取用户及分配信息列表
     *
     * @param request        请求参数对象
     * @param sessionContext session信息
     * @return 返回消息记录列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取用户及分配信息列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取用户及分配信息列表"})})
    @RequestMapping(value = "/listWithAssignment", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserListDTO>> getUserWithAssignmentList(
            PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageWebRequest 不能为null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        UserAssignmentPageRequest pageRequest = new UserAssignmentPageRequest(request);
        // 添加数据权限条件
        if (!userCommonHelper.checkAndAddQueryUserPermission(request, pageRequest, sessionContext)) {
            // 传的组ID不在权限内直接返回空
            return CommonWebResponse.success(new DefaultPageResponse<>());
        }
        return CommonWebResponse.success(userDesktopConfigAPI.pageQueryWithAssignment(pageRequest));
    }


    /**
     * @api {POST} /rco/user/getInfo 获取用户详细信息
     * @apiName 获取用户详细信息
     * @apiGroup 用户管理相关
     * @apiDescription 用户详细信息, 包括基本信息和绑定的桌面信息
     * @apiParam (请求体字段说明) {String} id 用户ID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "id": "ab2ebda2-2238-4628-8841-33b6f4494297"
     *                  }
     *
     * @apiSuccess (响应字段说明) {Object[]} content 响应内容
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} msgArgArr 响应信息参数
     * @apiSuccess (响应字段说明) {String} msgKey 响应信息Key
     * @apiSuccess (响应字段说明) {String} status 成功失败状态
     *
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content":{
     *                    "adUserAuthority": "GUESTS",
     *                    "createTime": 1583912655418,
     *                    "creatingDesktopNum": 0,
     *                    "desktopNum": 1,
     *                    "email": "12345678911@ruijie.com.cn",
     *                    "groupId": "a6cfb26b-9b86-4795-afca-8f412412f1e0",
     *                    "groupName": "123",
     *                    "groupNameArr": ["123"],
     *                    "hasLogin": false,
     *                    "id": "24ec08ba-3c0a-4c94-9bf1-84cbd6006d5f",
     *                    "loginIdentityLevel": "MANUAL_LOGIN",
     *                    "phoneNum": "12345678911",
     *                    "realName": "test",
     *                    "userName": "哈哈",
     *                    "userState": "ENABLE",
     *                    "userType": "NORMAL"
     *                    },
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
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
     * 获取用户详细基本信息,包括基本信息和绑定的桌面信息
     *
     * @param request 页面请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取用户基本信息,包括桌面配置")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取用户基本信息,包括桌面配置"})})
    @RequestMapping(value = "/getInfo")
    public DefaultWebResponse getUserInfo(UserIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "UserIdWebRequest不能为null");
        UUID userId = request.getId();

        IacUserDetailDTO response = userAPI.getUserDetail(userId);
        UserInfoWebResponse userInfoWebResponse = new UserInfoWebResponse();
        BeanUtils.copyProperties(response, userInfoWebResponse);
        dealAccountExpireDate(response, userInfoWebResponse);
        IacUserLoginIdentityLevelEnum userLoginIdentityLevel = getUserLoginIdentityLevel(response.getUserType(), response.getId());
        userInfoWebResponse.setLoginIdentityLevel(userLoginIdentityLevel);
        userInfoWebResponse.setUserDescription(response.getUserDescription());

        UserCertificationDTO userCertificationDTO = userInfoAPI.getUserCertificationDTO(userId);
        BeanUtils.copyProperties(userCertificationDTO, userInfoWebResponse);
        CbbDesktopTempPermissionDTO permissionDTO = desktopTempPermissionAPI.getPermissionByRelatedIdAndType(userId,
                DesktopTempPermissionRelatedType.USER);
        userInfoWebResponse.setDesktopTempPermissionName(Objects.nonNull(permissionDTO) ? permissionDTO.getName() : null);
        try {
            if (ObjectUtils.isNotEmpty(response.getId())) {
                IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserDetail(response.getId());
                userInfoWebResponse.setInvalid(response.getInvalid());
                userInfoWebResponse.setInvalidDescription(invalidTimeHelpUtil.obtainInvalidDescription(cbbUserDetailDTO));
                userInfoWebResponse.setAccountExpireDate(invalidTimeHelpUtil.expireDateFormat(cbbUserDetailDTO));
            }
        } catch (BusinessException e) {
            LOGGER.error("查询用户[{}]详情出错", response.getId());
            throw new RuntimeException(e);
        }

        return DefaultWebResponse.Builder.success(userInfoWebResponse);
    }

    private void dealAccountExpireDate(IacUserDetailDTO response, UserInfoWebResponse userInfoWebResponse) {
        if (response.getAccountExpireDate() != null) {
            if (response.getAccountExpireDate() == EXPIRE_DATE_ZERO) {
                userInfoWebResponse.setAccountExpireDateStr(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE));
            } else {
                Date accountExpireDate;
                if (response.getUserType() == IacUserTypeEnum.AD) {
                    accountExpireDate = DateUtil.adDomainTimestampToDate(response.getAccountExpireDate());
                } else {
                    accountExpireDate = new Date(response.getAccountExpireDate());
                }
                userInfoWebResponse.setAccountExpireDateStr(DateUtil.dateToLocalDate(accountExpireDate).
                        format(DateTimeFormatter.ofPattern(DateUtil.YYYY_MM_DD)));
            }
        }
    }


    private IacUserLoginIdentityLevelEnum getUserLoginIdentityLevel(IacUserTypeEnum userType, UUID userId) throws BusinessException {
        if (hasUserIdentityConfig(userType)) {
            IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId);
            IacUserIdentityConfigResponse response = userIdentityConfigAPI.findUserIdentityConfigByRelated(userIdentityConfigRequest);
            return response.getLoginIdentityLevel();
        }
        // 不支持双网
        return null;
    }

    private boolean hasUserIdentityConfig(IacUserTypeEnum userType) throws BusinessException {

        return userIdentityConfigAPI.hasUserIdentityConfig(userType);
    }

    /**
     * 删除用户
     *
     * @param request        页面请求参数
     * @param builder        批量任务
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "删除用户")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"删除用户"})})
    @RequestMapping(value = "/delete")
    @EnableAuthority
    public DefaultWebResponse delete(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "UserIdWebRequest不能为null");
        Assert.notEmpty(request.getIdArr(), "id不能为空");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        UUID[] idArr = request.getIdArr();

        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct()
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(UserBusinessKey.RCDC_RCO_USER_DELETE_ITEM_NAME, new String[]{}).build()).iterator();

        DeleteUserBatchTaskHandler handler = new DeleteUserBatchTaskHandler(this.userAPI, iterator, auditLogAPI);
        handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        handler.setUserDesktopConfigAPI(userDesktopConfigAPI);
        handler.setUserMessageAPI(userMessageAPI);
        handler.setDesktopOpLogMgmtAPI(desktopOpLogMgmtAPI);
        handler.setUserHardwareCertificationAPI(userHardwareCertificationAPI);
        handler.setRccmManageAPI(rccmManageAPI);
        handler.setUwsDockingAPI(uwsDockingAPI);
        handler.setDiskPoolMgmtAPI(userDiskMgmtAPI);
        handler.setDesktopTempPermissionAPI(desktopTempPermissionAPI);
        handler.setRcaGroupMemberAPI(rcaGroupMemberAPI);
        handler.setRcaHostSessionAPI(rcaHostSessionAPI);
        BatchTaskSubmitResult result = startDeleteBatchTask(idArr, handler, builder);

        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult startDeleteBatchTask(UUID[] idArr, DeleteUserBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 删除单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            IacUserDetailDTO userDetailResponse = userAPI.getUserDetail(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_DELETE_SINGLE_TASK_NAME, new String[]{})
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_DELETE_SINGLE_TASK_DESC, new String[]{userDetailResponse.getUserName()})
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_DELETE_TASK_NAME, new String[]{})
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_DELETE_TASK_DESC, new String[]{}).enableParallel().registerHandler(handler).start();
        }

        return result;
    }

    /**
     * 校验用户名是否有重名
     *
     * @param request 页面请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "校验用户名是否有重名")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"校验用户名是否有重名"})})
    @RequestMapping(value = "/checkUserNameDuplication")
    public DefaultWebResponse checkUserNameDuplication(UserNameWebRequest request) throws BusinessException {
        Assert.notNull(request, "UserNameWebRequest不能为null");
        IacUserDetailDTO userInfoDTO = userAPI.getUserByName(request.getUserName());
        CheckDuplicationResponse duplicationResponse = new CheckDuplicationResponse(userInfoDTO != null);
        return DefaultWebResponse.Builder.success(duplicationResponse);
    }

    /**
     * 重置用户密码
     *
     * @param request        页面请求参数
     * @param builder        批量任务
     * @param sessionContext session上下文
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "重置用户密码")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"重置用户密码", "重置用户密码时，同步修改对应管理员密码，并且前端传递的为密文"})})
    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "resetPasswordValidate")
    @EnableAuthority
    public DefaultWebResponse resetPassword(IdArrWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(builder, "builder is not null");
        Assert.notNull(request.getPassword(), "request.getPassword() must not null");
        Assert.notNull(sessionContext, "sessionContext must not null");

        Boolean isRandom = UserConstant.PASSWORD_RANDOM_MODE.equals(request.getPasswordResetMode());
        // 批量操作
        UUID[] userIdArr = request.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(userIdArr).distinct().map(userId -> DefaultBatchTaskItem.builder().itemId(userId)
                .itemName(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_TASK_NAME, new String[]{}).build()).iterator();
        ResetUserPasswordBatchTaskHandlerRequest resetUserPasswordBatchTaskHandlerRequest = new ResetUserPasswordBatchTaskHandlerRequest();
        resetUserPasswordBatchTaskHandlerRequest.setBatchTaskItemIterator(iterator);
        resetUserPasswordBatchTaskHandlerRequest.setAuditLogAPI(auditLogAPI);
        resetUserPasswordBatchTaskHandlerRequest.setCbbUserAPI(userAPI);
        resetUserPasswordBatchTaskHandlerRequest.setCertificationStrategyParameterAPI(certificationStrategyParameterAPI);
        resetUserPasswordBatchTaskHandlerRequest.setPasswordRandom(isRandom);
        resetUserPasswordBatchTaskHandlerRequest.setPassword(request.getPassword());
        resetUserPasswordBatchTaskHandlerRequest.setModifyPasswordAPI(modifyPasswordAPI);
        resetUserPasswordBatchTaskHandlerRequest.setMailMgmtAPI(mailMgmtAPI);
        resetUserPasswordBatchTaskHandlerRequest.setUserInfoAPI(userInfoAPI);
        // 是否是超级管理员角色
        if (permissionHelper.roleIsSuperAdmin(sessionContext)) {
            resetUserPasswordBatchTaskHandlerRequest.setRoleIsSuperAdmin(Boolean.TRUE);
        }
        ResetUserPasswordBatchTaskHandler handler = new ResetUserPasswordBatchTaskHandler(resetUserPasswordBatchTaskHandlerRequest);
        if (Arrays.asList(userIdArr).size() == 1) {
            handler.setBatch(false);
        }
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_RESET_PASSWORD_TASK_DESC).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 修改用户所属分组
     *
     * @param request        页面请求参数
     * @return 返回结果
     * @throws BusinessException business exception
     */
    @ApiOperation(value = "修改用户所属分组")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"修改用户所属分组"})})
    @RequestMapping(value = "/modifyUserGroup")
    @EnableAuthority
    public DefaultWebResponse modifyUserGroup(ModifyUserGroupWebRequest request) throws BusinessException {
        Assert.notNull(request, "ModifyUserGroupWebRequest can not null");

        int successNum = 0;
        int failNum = 0;
        UUID[] idArr = request.getIdArr();
        UUID groupId = request.getGroupId();
        for (UUID id : idArr) {
            boolean isSuccess = modifyUserGroupAddOptLog(id, groupId);
            if (isSuccess) {
                successNum++;
            } else {
                failNum++;
            }
        }

        if (failNum == 0) {
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_USER_UPDATE_USER_GROUP_RESULT,
                    new String[]{String.valueOf(successNum), String.valueOf(failNum)});
        } else {
            return DefaultWebResponse.Builder.fail(UserBusinessKey.RCDC_RCO_USER_UPDATE_USER_GROUP_RESULT,
                    new String[]{String.valueOf(successNum), String.valueOf(failNum)});
        }
    }

    private boolean modifyUserGroupAddOptLog(UUID id, UUID groupId) {
        String userName = null;
        try {
            IacUserDetailDTO response = userAPI.getUserDetail(id);
            userName = response.getUserName();
            if (IacUserTypeEnum.AD == response.getUserType()) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_AD_NOT_ALLOW_CHANGE_GROUP);
            }
            if (IacUserTypeEnum.LDAP == response.getUserType()) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_LDAP_NOT_ALLOW_CHANGE_GROUP);
            }
            if (IacUserTypeEnum.THIRD_PARTY == response.getUserType()) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_THIRD_PARTY_NOT_ALLOW_CHANGE_GROUP);
            }
            IacUpdateUserDTO updateUserRequest = new IacUpdateUserDTO();
            updateUserRequest.setId(id);
            updateUserRequest.setGroupId(groupId);
            updateUserRequest.setRealName(response.getRealName());
            updateUserRequest.setEmail(response.getEmail());
            updateUserRequest.setPhoneNum(response.getPhoneNum());
            // 管理员标识
            updateUserRequest.setUserRole(response.getUserRole());
            userAPI.updateUser(updateUserRequest);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_UPDATE_USER_GROUP_SUCCESS_LOG, userName);
            return true;
        } catch (BusinessException e) {
            LOGGER.error("修改用户[" + userName + "]所属分组失败", e);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_UPDATE_USER_GROUP_FAIL_LOG, new String[]{userName, exceptionMsg});
            return false;
        }
    }

    /**
     * 修改ad域用户权限
     *
     * @param request        页面请求参数
     * @param builder        批处理任务结果
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "修改ad域用户权限")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制,修改ad域用户权限"})})
    @RequestMapping(value = "adUserAuthority/edit")
    @EnableAuthority
    public DefaultWebResponse editAdUserAuthority(AdUserAuthorityEditWebRequest request,
                                                  BatchTaskBuilder builder) throws BusinessException {

        Assert.notNull(request, "AdUserAuthorityEditWebRequest can not null");
        Assert.notNull(builder, "BatchTaskBuilder can not null");
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(request.getIdArr()).distinct()//
                .map(uid -> DefaultBatchTaskItem.builder().itemId(uid)//
                        .itemName(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_AUTHORITY_TASK_NAME, new String[]{}).build())//
                .iterator();

        AdUserAuthorityBatchTaskHandler adUserAuthorityBatchTaskHandler =
                new AdUserAuthorityBatchTaskHandler(iterator, auditLogAPI, userAPI, request.getAdUserAuthority());
        adUserAuthorityBatchTaskHandler.setUserEventNotifyAPI(userEventNotifyAPI);
        BatchTaskSubmitResult result = startEditAdUserAuthority(request.getIdArr(), adUserAuthorityBatchTaskHandler, builder);

        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult startEditAdUserAuthority(UUID[] idArr, AdUserAuthorityBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 更新单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            IacUserDetailDTO userDetailResponse = userAPI.getUserDetail(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_UPDATE_AUTH_SINGLE_TASK_NAME, new String[]{})
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_UPDATE_AUTH_SINGLE_TASK_DESC, new String[]{userDetailResponse.getUserName()})
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_AUTHORITY_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_AUTHORITY_TASK_DESC).registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 获取ad域用户权限信息
     *
     * @param request 页面请求参数
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取AD域用户权限信息")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取AD域用户权限信息"})})
    @RequestMapping(value = "adUserAuthority/detail")
    public DefaultWebResponse detailAdUserAuthority(AdUserAuthorityDetailWebRequest request) throws BusinessException {
        Assert.notNull(request, "AdUserAuthorityDetailWebRequest can not null");
        IacUserDetailDTO response = userAPI.getUserDetail(request.getId());

        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * @api {POST} /loginIdentity/edit 批量设置用户身份验证配置
     * @apiName 批量设置用户身份验证配置
     * @apiGroup 用户管理相关
     * @apiDescription 批量设置用户身份验证配置
     * @apiParam (请求体字段说明) {String} loginIdentityLevel 双网身份验证
     * @apiParam (请求体字段说明) {Object[]} idArr 用户ID数组
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "loginIdentityLevel": "MANUAL_LOGIN",
     *                  "idArr": ["c5b69f55-a961-4b4f-9837-532c415b4a5b", "d8948296-7d24-40ec-95d5-89e4f547dd72"]
     *                  }
     *
     * @apiSuccess (响应字段说明) {Object[]} content 响应内容
     * @apiSuccess (响应字段说明) {String} message 响应信息
     * @apiSuccess (响应字段说明) {Object[]} msgArgArr 响应信息参数
     * @apiSuccess (响应字段说明) {String} msgKey 响应信息Key
     * @apiSuccess (响应字段说明) {String} status 成功失败状态
     *
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content":
     *                    {
     *                    "id": "8453c7d1-ea99-4683-bea1-43f1c32b7421",
     *                    "taskDesc": "正在批量更新用户双网身份验证",
     *                    "taskId": "cc6a2208-a6a4-4275-9cfa-b97902d4d7d8",
     *                    "taskName": "批量更新用户双网身份验证",
     *                    "taskStatus": "PROCESSING"
     *                    },
     *                    "message": null,
     *                    "msgArgArr": null,
     *                    "msgKey": null,
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
     * 批量设置用户身份验证配置
     *
     * @param request        页面请求参数
     * @param builder        批处理任务结果
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "批量设置用户身份验证配置")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制,批量设置用户身份验证配置"})})
    @RequestMapping(value = "/loginIdentity/edit")
    @EnableAuthority
    public DefaultWebResponse editLoginIdentity(UpdateUserLoginIdentityWebRequest request,
                                                BatchTaskBuilder builder) throws BusinessException {

        Assert.notNull(request, "UpdateUserLoginIdentityWebRequest can not null");
        Assert.notNull(builder, "BatchTaskBuilder can not null");

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(request.getIdArr()).distinct()//
                .map(uid -> DefaultBatchTaskItem.builder().itemId(uid)//
                        .itemName(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_TASK_NAME, new String[]{}).build())//
                .iterator();

        UserLoginIdentityBatchTaskHandler handler = new UserLoginIdentityBatchTaskHandler(iterator, auditLogAPI, userAPI);
        handler.init(userIdentityConfigAPI, request.getLoginIdentityLevel());
        handler.setDataSyncAPI(dataSyncAPI);

        BatchTaskSubmitResult result = startPatchEditLoginIdentity(request.getIdArr(), handler, builder);

        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult startPatchEditLoginIdentity(UUID[] idArr, UserLoginIdentityBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 更新单条用户
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            IacUserDetailDTO userDetailResponse = userAPI.getUserDetail(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SINGLE_TASK_NAME, new String[]{})
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_SINGLE_TASK_DESC, new String[]{userDetailResponse.getUserName()})
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_EDIT_USER_LOGIN_IDENTITY_BATCH_TASK_DESC).registerHandler(handler).start();
        }
        return result;
    }


    /**
     * 批量设置用户或用户组认证策略
     *
     * @param request 页面请求参数
     * @param builder 批处理任务结果
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "批量配置用户及用户组认证策略")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制,批量设置用户或用户组身份验证配置"})})
    @RequestMapping(value = "/batch/certification", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "validateUserCertification")
    @EnableAuthority
    public DefaultWebResponse batchConfigureUserAndGroupCertification(BatchUserOrGroupCertificationRequest request, BatchTaskBuilder builder)
            throws BusinessException {

        Assert.notNull(request, "BatchUserOrGroupCertificationRequest can not null");
        Assert.notNull(builder, "certification BatchTaskBuilder can not null");
        // 只允许一个批量应用用户或用户组策略任务在执行
        if (!UserOrGroupBatchStateCache.STATE.addSyncTask(UserOrGroupBatchStateCache.USER_OR_GROUP_KEY)) {
            throw new BusinessException(UserBusinessKey.RCDC_USER_OR_GROUP_ONLY_ONE_BATCH_TASK_SYNCHRONIZE);
        }
        //校验过期时间
        validateAccountExpireTime(request.getAccountExpireDate());
        validateInvalidTime(request.getInvalidTime());
        try {
            final Iterator<UpdateUserCertificationBatchTaskItem> itemIterator = buildCertificationBatchTaskItem(request);

            UserCertificationBatchTaskHandler handler =
                    new UserCertificationBatchTaskHandler(itemIterator, auditLogAPI, userGroupAPI, userAPI, userInfoAPI)
                            .setUserIdentityConfigAPI(userIdentityConfigAPI).setDataSyncAPI(dataSyncAPI)
                            .setUserIdentityConfigDTO(buildUserIdentityConfigDTO(request));

            BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_TASK_DESC).registerHandler(handler)
                    .enableParallel().start();
            return DefaultWebResponse.Builder.success(result);
        } catch (BusinessException e) {
            LOGGER.error("创建用或用户组批量修改认证策略任务失败", e);
            UserOrGroupBatchStateCache.STATE.removeSyncTask(UserOrGroupBatchStateCache.USER_OR_GROUP_KEY);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            throw new BusinessException(UserBusinessKey.RCDC_USER_OR_GROUP_CREATE_BATCH_TASK_FAIL, e, exceptionMsg);
        }
    }

    private Iterator<UpdateUserCertificationBatchTaskItem> buildCertificationBatchTaskItem(BatchUserOrGroupCertificationRequest request) {
        UUID[] userSortGroupIdArr = request.getUserSortGroupIdArr();
        UUID[] userIdArr = request.getUserIdArr();
        // 有用户夹杂组ID送进来,查询出组下用户
        AtomicInteger initial = new AtomicInteger(0);
        int userLength = userIdArr.length;
        int userSortGroupLength = userSortGroupIdArr.length;
        UUID[] totalArr = this.arrayCopyUUID(this.arrayCopyUUID(userIdArr, userSortGroupIdArr), request.getGroupIdArr());
        return Stream.of(totalArr).map(id -> {
            UpdateUserCertificationBatchTaskItem batchTaskItem = new UpdateUserCertificationBatchTaskItem();
            batchTaskItem.setItemId(id);
            batchTaskItem.setItemName(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_TASK_NAME));
            if (initial.get() < userLength) {
                batchTaskItem.setRelatedType(IacConfigRelatedType.USER);
                batchTaskItem.setSuccessKey(UserBusinessKey.RCDC_RCO_EDIT_USER_IDENTITY_CERTIFICATION_SUCCESS);
                batchTaskItem.setFailKey(UserBusinessKey.RCDC_RCO_EDIT_USER_IDENTITY_CERTIFICATION_FAIL);
            } else if (initial.get() >= userLength && initial.get() < (userSortGroupLength + userLength)) {
                batchTaskItem.setRelatedType(IacConfigRelatedType.USERGROUP);
                batchTaskItem.setFilterUserIdArr(request.getFilterUserIdArr());
                batchTaskItem.setSuccessKey(UserBusinessKey.RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_PROMPT_SUCCESS);
                batchTaskItem.setFailKey(UserBusinessKey.RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_PROMPT_FAIL);
            } else {
                batchTaskItem.setRelatedType(IacConfigRelatedType.USERGROUP);
                batchTaskItem.setSuccessKey(UserBusinessKey.RCDC_RCO_EDIT_USER_GROUP_IDENTITY_CERTIFICATION_SUCCESS);
                batchTaskItem.setFailKey(UserBusinessKey.RCDC_RCO_EDIT_USER_GROUP_IDENTITY_CERTIFICATION_FAIL);
            }
            initial.incrementAndGet();
            return batchTaskItem;
        }).iterator();
    }

    private UUID[] arrayCopyUUID(UUID[] src, UUID[] dest) {
        UUID[] totalArr = new UUID[src.length + dest.length];
        System.arraycopy(src, 0, totalArr, 0, src.length);
        System.arraycopy(dest, 0, totalArr, src.length, dest.length);
        return totalArr;
    }

    private UserIdentityConfigDTO buildUserIdentityConfigDTO(BatchUserOrGroupCertificationRequest request) throws BusinessException {
        UserIdentityConfigDTO userIdentityConfigDTO = new UserIdentityConfigDTO();
        Optional<BatchUserOrGroupCertificationRequest> optional = Optional.ofNullable(request);
        // 设置主要策略
        optional.map(BatchUserOrGroupCertificationRequest::getPrimaryCertificationVO).ifPresent(primaryCertificationVO -> {
            userIdentityConfigDTO
                    .setOpenAccountPasswordCertification(BooleanUtils.toBoolean(primaryCertificationVO.getOpenAccountPasswordCertification()));
            userIdentityConfigDTO.setOpenCasCertification(BooleanUtils.toBoolean(primaryCertificationVO.getOpenCasCertification()));
            userIdentityConfigDTO.setOpenWorkWeixinCertification(BooleanUtils.toBoolean(primaryCertificationVO.getOpenWorkWeixinCertification()));
            userIdentityConfigDTO.setOpenFeishuCertification(BooleanUtils.toBoolean(primaryCertificationVO.getOpenFeishuCertification()));
            userIdentityConfigDTO.setOpenDingdingCertification(BooleanUtils.toBoolean(primaryCertificationVO.getOpenDingdingCertification()));
            userIdentityConfigDTO.setOpenOauth2Certification(BooleanUtils.toBoolean(primaryCertificationVO.getOpenOauth2Certification()));
            userIdentityConfigDTO.setOpenRjclientCertification(BooleanUtils.toBoolean(primaryCertificationVO.getOpenRjclientCertification()));
        });

        // 校验辅助策略
        if (optional.isPresent() && request.getAssistCertificationVO() != null) {
            // 动态口令开关不为null, 进行校验
            if (request.getAssistCertificationVO().getOpenOtpCertification() != null
                    && !BooleanUtils.toBoolean(otpCertificationAPI.getOtpCertification().getOpenOtp())) {
                throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_OTP_FAIL_RESULT);
            }
            // Radius认证不为null，进行校验
            if (request.getAssistCertificationVO().getOpenRadiusCertification() != null
                    && !BooleanUtils.toBoolean(thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC).getEnable())) {
                throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_RADIUS_FAIL_RESULT);
            }
            request.getAssistCertificationVO().checkParam();
        }
        // 设置辅助策略
        optional.map(BatchUserOrGroupCertificationRequest::getAssistCertificationVO).ifPresent(assistCertificationVO -> {
            userIdentityConfigDTO.setOpenOtpCertification(BooleanUtils.toBoolean(assistCertificationVO.getOpenOtpCertification()));
            userIdentityConfigDTO.setOpenRadiusCertification(BooleanUtils.toBoolean(assistCertificationVO.getOpenRadiusCertification()));
            if (BooleanUtils.toBoolean(assistCertificationVO.getOpenHardwareCertification())) {
                userIdentityConfigDTO.setOpenHardwareCertification(true);
                userIdentityConfigDTO.setMaxHardwareNum(assistCertificationVO.getMaxHardwareNum());
            } else {
                userIdentityConfigDTO.setOpenHardwareCertification(false);
            }
            userIdentityConfigDTO.setOpenSmsCertification(BooleanUtils.toBoolean(assistCertificationVO.getOpenSmsCertification()));
        });
        // 设置其它策略
        optional.map(BatchUserOrGroupCertificationRequest::getOtherCertificationVO)
                .ifPresent(otherCertificationVO -> userIdentityConfigDTO.setLoginIdentityLevel(otherCertificationVO.getLoginIdentityLevel()));
        if (ObjectUtils.isNotEmpty(request.getAccountExpireDate())) {
            userIdentityConfigDTO.setAccountExpireDate(request.getAccountExpireDate());
        }
        userIdentityConfigDTO.setInvalidTime(request.getInvalidTime());
        return userIdentityConfigDTO;
    }

    /**
     * 禁用用户
     *
     * @param request        页面请求参数
     * @param builder        批量任务
     * @param sessionContext session上下文
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "禁用用户")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"禁用用户", "禁用用户，退出用户登录会话，关闭虚拟机"})})
    @RequestMapping(value = "/disableUser", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse disableUser(IdArrWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "IdArrWebRequest不能为null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        Assert.notNull(sessionContext, "sessionContext must not null");
        Assert.notEmpty(request.getIdArr(), "request.getIdArr()不能为空");

        // 是否是管理员角色
        if (!permissionHelper.roleIsSuperAdmin(sessionContext)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IS_NOT_ADMIN_PERMISSION);
        }

        // 批量操作
        UUID[] userIdArr = request.getIdArr();
        LOGGER.info("禁用用户[{}]", Arrays.toString(userIdArr));
        LOGGER.debug("disable user info userIdArr={}", Arrays.toString(userIdArr));

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(userIdArr)
                .distinct().map(userId -> DefaultBatchTaskItem.builder().itemId(userId)
                        .itemName(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_TASK_NAME, new String[]{}).build()
                ).iterator();

        // 声明任务参数
        DisableUserPresetBatchTaskHandlerRequest disableUserPresetBatchTaskHandlerRequest = new DisableUserPresetBatchTaskHandlerRequest();
        disableUserPresetBatchTaskHandlerRequest.setBatchTaskItemIterator(iterator);
        disableUserPresetBatchTaskHandlerRequest.setAuditLogAPI(auditLogAPI);
        disableUserPresetBatchTaskHandlerRequest.setCbbUserAPI(cbbUserAPI);
        disableUserPresetBatchTaskHandlerRequest.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        disableUserPresetBatchTaskHandlerRequest.setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
        disableUserPresetBatchTaskHandlerRequest.setCloudDesktopOperateAPI(cloudDesktopOperateAPI);
        disableUserPresetBatchTaskHandlerRequest.setSessionContextRegistry(sessionContextRegistry);
        disableUserPresetBatchTaskHandlerRequest.setUserInfoAPI(userInfoAPI);
        disableUserPresetBatchTaskHandlerRequest.setUserMgmtAPI(userMgmtAPI);
        disableUserPresetBatchTaskHandlerRequest.setUwsDockingAPI(uwsDockingAPI);

        // 创建任务
        DisableUserPresetBatchTaskHandler handler = new DisableUserPresetBatchTaskHandler(disableUserPresetBatchTaskHandlerRequest);
        if (Arrays.asList(userIdArr).size() > 1) {
            handler.setBatch(true);
        }

        // 执行任务
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_DISABLE_PRESET_TASK_DESC).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 启用用户
     *
     * @param request        页面请求参数
     * @param builder        批量任务
     * @param sessionContext session上下文
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "启用用户")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"启用用户", "启用用户，允许用户登录"})})
    @RequestMapping(value = "/enableUser", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse enableUser(IdArrWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "IdArrWebRequest不能为null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        Assert.notNull(sessionContext, "sessionContext must not null");
        Assert.notEmpty(request.getIdArr(), "request.getIdArr()不能为空");

        // 是否是管理员角色
        if (!permissionHelper.roleIsSuperAdmin(sessionContext)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IS_NOT_ADMIN_PERMISSION);
        }

        // 批量操作
        UUID[] userIdArr = request.getIdArr();
        LOGGER.info("启用用户[{}]", Arrays.toString(userIdArr));
        LOGGER.debug("enable user info userIdArr={}", Arrays.toString(userIdArr));

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(userIdArr)
                .distinct().map(userId -> DefaultBatchTaskItem.builder().itemId(userId)
                        .itemName(UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_TASK_NAME, new String[]{}).build()
                ).iterator();

        // 声明任务参数
        EnableUserPresetBatchTaskHandlerRequest enableUserPresetBatchTaskHandlerRequest = new EnableUserPresetBatchTaskHandlerRequest();
        enableUserPresetBatchTaskHandlerRequest.setBatchTaskItemIterator(iterator);
        enableUserPresetBatchTaskHandlerRequest.setAuditLogAPI(auditLogAPI);
        enableUserPresetBatchTaskHandlerRequest.setCbbUserAPI(cbbUserAPI);

        // 创建任务
        EnableUserPresetBatchTaskHandler handler = new EnableUserPresetBatchTaskHandler(enableUserPresetBatchTaskHandlerRequest);
        if (Arrays.asList(userIdArr).size() > 1) {
            handler.setBatch(true);
        }

        // 执行任务
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_ENABLE_PRESET_TASK_DESC).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }


    /**
     * 账号失效恢复
     *
     * @param request        页面请求参数
     * @param builder        批量任务
     * @param sessionContext session上下文
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "失效账号恢复")
    @RequestMapping(value = "/recoverInvalidTime", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse recoverInvalidTime(IdArrWebRequest request,
                                                 BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not null");
        Assert.notNull(builder, "builder can not null");
        Assert.notNull(sessionContext, "sessionContext can not null");
        Assert.notNull(request.getIdArr(), "request.getIdArr() can not null");

        UUID[] userIdArr = request.getIdArr();
        LOGGER.info("解除账号[{}]失效", Arrays.toString(userIdArr));
        LOGGER.debug("invalid recover user info userIdArr={}", Arrays.toString(userIdArr));

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(userIdArr)
                .distinct().map(userId -> DefaultBatchTaskItem.builder().itemId(userId)
                        .itemName(UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_PRESET_TASK_NAME, new String[]{}).build()
                ).iterator();

        InvalidRecoverPresetBatchTaskHandlerRequest invalidPresetBatchTaskHandlerRequest = new InvalidRecoverPresetBatchTaskHandlerRequest();
        invalidPresetBatchTaskHandlerRequest.setBatchTaskItemIterator(iterator);
        invalidPresetBatchTaskHandlerRequest.setAuditLogAPI(auditLogAPI);
        invalidPresetBatchTaskHandlerRequest.setCbbUserAPI(userAPI);
        if (Arrays.asList(userIdArr).size() == 1) {
            IacUserDetailDTO userDetail = userAPI.getUserDetail(userIdArr[0]);
            invalidPresetBatchTaskHandlerRequest.setThreadUserName(userDetail.getUserName());
        }

        InvalidRecoverPresetBatchTaskHandler handler = new InvalidRecoverPresetBatchTaskHandler(invalidPresetBatchTaskHandlerRequest);


        // 执行任务
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_PRESET_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_PRESET_TASK_DESC).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);

    }

    /**
     * 获取桌面关联用户分页
     *
     * @param request web请求
     * @param sessionContext sessionContext
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取桌面关联用户分页")
    @RequestMapping(value = "/deskBindUserPage", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"获取桌面关联用户分页"})})
    public DefaultWebResponse deskBindUserPage(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageWebRequest 不能为null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        PageSearchRequest apiRequest = new UserPageSearchRequest(request);
        if (!userCommonHelper.checkAndAddQueryUserPermission(request, apiRequest, sessionContext)) {
            return buildUserListWebResponse(new DefaultPageResponse<>());
        }
        DefaultPageResponse<UserListDTO> pageResponse = hostUserAPI.deskBindUserPage(apiRequest);
        return buildUserListWebResponse(pageResponse);

    }

    private void validateAccountExpireTime(Long accountExpireTime) throws BusinessException {
        if (ObjectUtils.isEmpty(accountExpireTime) || accountExpireTime == 0L) {
            return;
        }
        //账户到期时间不能小于当前时间
        if (accountExpireTime < new Date().getTime()) {
            String expireTime = DateUtil.formatDate(new Date(accountExpireTime), DATE_FORMAT_TIME);
            String currentTime = DateUtil.formatDate(new Date(), DATE_FORMAT_TIME);
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE_TIME_ERROR, expireTime, currentTime);
        }
    }

    private void validateInvalidTime(Integer invalidTime) throws BusinessException {
        if (ObjectUtils.isEmpty(invalidTime)) {
            return;
        }
        if (invalidTime > INVALID_TIME_MAX_VALUE || invalidTime < INVALID_TIME_MIN_VALUE) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_INVALID_TIME_VALIDATE_ERROR, invalidTime.toString());
        }
    }

    private void validateDescription(String description) throws BusinessException {
        if (StringUtils.isEmpty(description)) {
            return;
        }
        if (description.length() > DESCRIPTION_MAX_LENGTH) {
            LOGGER.error("描述信息长度超过限制");
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_DESCRIPTION_VALIDATE_FAIL);
        }
    }

    private DefaultWebResponse buildUserListWebResponse(DefaultPageResponse<UserListDTO> pageResponse) throws BusinessException {
        UserListWebResponse userListWebResponse = new UserListWebResponse();
        userListWebResponse.setItemArr(pageResponse.getItemArr());
        userListWebResponse.setTotal(pageResponse.getTotal());
        Boolean hasHardware = hardwareCertificationAPI.getHardwareCertification().getOpenHardware();
        Boolean hasOpenOtp = otpCertificationAPI.getOtpCertification().getOpenOtp();
        Boolean hasOpenCas = scanCodeAuthParameterAPI.getCasScanCodeAuthInfo().getApplyOpen();
        Boolean hasOpenRadiusCertification = thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC).getEnable();
        userListWebResponse.setOpenCas(hasOpenCas);
        userListWebResponse.setOpenHardware(hasHardware);
        userListWebResponse.setOpenOtp(hasOpenOtp);
        userListWebResponse.setOpenSmsCertification(smsCertificationAPI.getBusSmsCertificationStrategy().getEnable());
        userListWebResponse.setOpenRadiusCertification(BooleanUtils.isTrue(hasOpenRadiusCertification));
        userListWebResponse.setOpenThirdPartyCertification(cbbThirdPartyUserAPI.getThirdPartyAuthState());
        IacSupportMainAuthenticationDTO mainAuthenticationDTO = iacAuthTypeMgmtAPI.mainAuthentication(SubSystem.CDC, AuthUserTypeEnum.USER);
        List<IacMainAuthenticationDTO> mainAuthenticationList = mainAuthenticationDTO.getMainAuthenticationList();
        List<MainAuthTypeEnum> typeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mainAuthenticationList)) {
            typeList = mainAuthenticationList.stream().map(IacMainAuthenticationDTO::getAuthType).collect(Collectors.toList());
        }
        userListWebResponse.setOpenWorkWeixinCertification(typeList.contains(MainAuthTypeEnum.WORK_WEIXIN));
        userListWebResponse.setOpenFeishuCertification(typeList.contains(MainAuthTypeEnum.FEISHU));
        userListWebResponse.setOpenDingdingCertification(typeList.contains(MainAuthTypeEnum.DINGDING));
        userListWebResponse.setOpenOauth2Certification(typeList.contains(MainAuthTypeEnum.AUTH2));
        IacQrCodeConfigDTO qrCodeConfig = iacQrCodeAPI.getQrCodeConfig(IacQrCodeType.RJ_CLIENT);
        userListWebResponse.setOpenRjclientCertification(BooleanUtils.isTrue(qrCodeConfig.getOpenSwitch()));
        return DefaultWebResponse.Builder.success(userListWebResponse);
    }
}
