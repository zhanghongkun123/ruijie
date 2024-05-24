package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacCreateUserGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacGetUserGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbCreateDeskSpecDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserGroupVO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserGroupDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktop.dto.GroupFilterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.constants.HardwareConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.AssistCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.PrimaryCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.constants.OtpConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.IdvDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.VoiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.AdUserGroupAuthorityBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.ApplyDesktopBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.ApplyIDVDesktopBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.ApplyVOIDesktopBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.UserConfigHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.CheckDuplicationDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.UserGroupDetailWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.validation.UserGroupValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.UserGroupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.batchtask.DeleteUserGroupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.batchtask.DeleteUserGroupBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.request.DeleteUserGroupBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Stream;

/**
 * Description: 用户组管理控制器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月10日
 *
 * @author jarman
 */
@Api(tags = "用户组管理")
@Controller
@RequestMapping("/rco/user/group")
@EnableCustomValidate(validateClass = UserGroupValidation.class)
public class UserGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupController.class);

    private static final int INVALID_TIME_MAX_VALUE = 1000;

    private static final int INVALID_TIME_MIN_VALUE = 0;

    private static final String DATE_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private UserGroupHelper userGroupHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserConfigHelper userConfigHelper;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolDashboardAPI desktopPoolDashboardAPI;

    @Autowired
    private DataSyncAPI dataSyncAPI;

    @Autowired
    private IacAdminMgmtAPI iacAdminMgmtAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private RcaGroupMemberAPI rcaGroupMemberAPI;

    @Autowired
    private UserEventNotifyAPI userEventNotifyAPI;


    /**
     * 获取所有用户分组数据，封装成树形结构
     *
     * @param request        空请求
     * @param sessionContext session信息
     * @return 返回树形结构
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse listUserGroupWidthTree(@Nullable ListUserGroupWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is not null.");

        IacUserGroupDetailDTO[] userGroupDTOArr = userGroupAPI.getAllUserGroup();
        if (userGroupDTOArr.length == 0) {
            return buildUserGroupItemArrResponse(Collections.emptyList());
        }

        // 根据权限过滤组列表并组装成树结构
        List<UserGroupVO> resultUserGroupList = buildTreeAndFilterUserGroupByPermission(request, sessionContext, userGroupDTOArr);
        if (CollectionUtils.isEmpty(resultUserGroupList)) {
            return buildUserGroupItemArrResponse(Collections.emptyList());
        }

        GroupFilterDTO groupFilterDTO = buildGroupFilterDTO(request);
        groupFilterDTO.setUserGroupVOList(resultUserGroupList);
        // 这里只是过滤，旧代码方法名未修改
        List<UserGroupVO> resultList = userGroupHelper.buildUserGroupTree(groupFilterDTO);
        return buildUserGroupItemArrResponse(resultList);
    }

    private DefaultWebResponse buildUserGroupItemArrResponse(List<? extends UserGroupVO> userGroupVOList) {
        return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", userGroupVOList));
    }

    private List<UserGroupVO> buildTreeAndFilterUserGroupByPermission(ListUserGroupWebRequest request, SessionContext sessionContext,
                                                                      IacUserGroupDetailDTO[] userGroupDTOArr) throws BusinessException {
        List<UserGroupVO> userGroupVOList = buildUserGroupVO(userGroupDTOArr);

        Boolean isNoPermission = Objects.requireNonNull(request).getNoPermission();
        // 无需校验权限或者拥有所有的权限情况，PS:这里根据原来旧代码改的
        if (Boolean.TRUE.equals(isNoPermission) || permissionHelper.isAllGroupPermission(sessionContext)) {
            refreshNoPermissionInfo(userGroupVOList);
            // 添加池分配信息
            userGroupHelper.addPoolAssignInfo(request, userGroupVOList);
            if (enableFilterPoolGroup(request)) {
                // 过滤池未选组
                List<UserGroupVO> poolRelaGroupVOList = userGroupHelper.filterPoolNotRelaGroupVOArr(request, userGroupVOList);
                return findTreeUserGroupVOArr(poolRelaGroupVOList, userGroupVOList);
            }
            return userGroupVOList;
        }

        // 根据管理员用户的权限过滤
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(sessionContext.getUserId());
        ListUserGroupIdResponse listUserGroupIdResponse = adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest);
        List<UserGroupVO> hasPermissionUserGroupList = filterUserGroupVOArr(userGroupVOList, listUserGroupIdResponse.getUserGroupIdList());
        // 添加池分配信息
        userGroupHelper.addPoolAssignInfo(request, userGroupVOList);
        if (enableFilterPoolGroup(request)) {
            // 过滤池未选组
            hasPermissionUserGroupList = userGroupHelper.filterPoolNotRelaGroupVOArr(request, hasPermissionUserGroupList);
        }
        // 递归组装树结构
        return findTreeUserGroupVOArr(hasPermissionUserGroupList, userGroupVOList);
    }

    private boolean enableFilterPoolGroup(ListUserGroupWebRequest request) {
        // 池ID为空|开关为：false，则不需要过滤
        return BooleanUtils.toBoolean(request.getEnableFilterPoolUserGroup())
                && (Objects.nonNull(request.getDiskPoolId()) || Objects.nonNull(request.getDesktopPoolId()) ||
                Objects.nonNull(request.getAppGroupId()));
    }

    /**
     * 获取需要过滤的信息
     *
     * @param request ListUserGroupWebRequest
     * @return GroupFilterDTO
     */
    private GroupFilterDTO buildGroupFilterDTO(ListUserGroupWebRequest request) {
        GroupFilterDTO groupFilterDTO = new GroupFilterDTO();
        groupFilterDTO.setEnableFilterAdGroup(false);
        groupFilterDTO.setEnableFilterLdapGroup(false);
        groupFilterDTO.setEnableFilterDefaultGroup(false);
        groupFilterDTO.setEnableFilterThirdPartyGroup(false);


        if (Objects.isNull(request)) {
            return groupFilterDTO;
        }

        // 指定过滤掉的用户组id
        groupFilterDTO.setFilterGroupId(request.getFilterGroupId());
        // 是否过滤掉AD域用户组
        groupFilterDTO.setEnableFilterAdGroup(Optional.ofNullable(request.getEnableFilterAdGroup()).orElse(false));
        // 是否过滤掉LDAP域用户组
        groupFilterDTO.setEnableFilterLdapGroup(Optional.ofNullable(request.getEnableFilterLdapGroup()).orElse(false));
        // 是否过滤掉默认未分组
        groupFilterDTO.setEnableFilterDefaultGroup(Optional.ofNullable(request.getEnableFilterDefaultGroup()).orElse(false));
        // 是否过滤掉第三方用户组
        groupFilterDTO.setEnableFilterThirdPartyGroup(Optional.ofNullable(request.getEnableFilterThirdPartyGroup()).orElse(false));
        return groupFilterDTO;
    }

    private List<UserGroupVO> buildUserGroupVO(IacUserGroupDetailDTO[] userGroupDTOArr) {
        List<UserGroupVO> voList = new ArrayList<>();
        for (IacUserGroupDetailDTO dto : userGroupDTOArr) {
            UserGroupVO vo = new UserGroupVO();
            vo.setId(dto.getId().toString());
            vo.setLabel(dto.getName());
            vo.setEnableDefault(dto.isDefault());
            vo.setAllowDelete(dto.isAllowDelete());
            vo.setEnableAd(dto.isAdGroup());
            vo.setEnableLdap(dto.isLdapGroup());
            vo.setEnableThirdParty(dto.isThirdPartyGroup());
            vo.setDisabled(true);
            if (dto.getParentId() == null) {
                vo.setParentId(UserGroupHelper.USER_GROUP_ROOT_ID);
            } else {
                vo.setParentId(dto.getParentId().toString());
            }
            vo.setAccountExpireDate(dto.getAccountExpireDate());
            vo.setInvalidTime(dto.getInvalidTime());
            voList.add(vo);
        }
        voList.add(addRootUserGroupVO());
        return voList;
    }

    private UserGroupVO addRootUserGroupVO() {
        UserGroupVO vo = new UserGroupVO();
        vo.setId(UserGroupHelper.USER_GROUP_ROOT_ID);
        vo.setLabel(UserGroupHelper.USER_GROUP_ROOT_NAME);
        vo.setParentId(null);
        vo.setDisabled(true);
        vo.setEnableAd(false);
        vo.setAllowDelete(false);
        vo.setEnableDefault(false);
        return vo;
    }

    private void refreshNoPermissionInfo(List<UserGroupVO> userGroupVOList) {
        for (UserGroupVO vo : userGroupVOList) {
            vo.setDisabled(false);
        }
    }

    private List<UserGroupVO> filterUserGroupVOArr(List<UserGroupVO> userGroupVOList, List<String> hasPermissionUUIDList) {
        if (CollectionUtils.isEmpty(hasPermissionUUIDList)) {
            return Collections.emptyList();
        }

        List<UserGroupVO> hasPermissionDTOList = new ArrayList<>();
        for (UserGroupVO vo : userGroupVOList) {
            for (String id : hasPermissionUUIDList) {
                if (vo.getId().equals(id)) {
                    vo.setDisabled(false);
                    hasPermissionDTOList.add(vo);
                    break;
                }
            }
        }

        return hasPermissionDTOList;
    }

    private List<UserGroupVO> findTreeUserGroupVOArr(List<UserGroupVO> hasPermissionUserGroupList, List<UserGroupVO> allUserGroupVOArr) {
        List<UserGroupVO> resultList = Lists.newArrayList();
        for (UserGroupVO vo : hasPermissionUserGroupList) {
            List<UserGroupVO> treeNodeList = Lists.newArrayList();
            treeNodeList.add(vo);
            recursiveTree(treeNodeList, vo, allUserGroupVOArr);
            resultList.addAll(treeNodeList);
        }
        return resultList;
    }

    private void recursiveTree(List<UserGroupVO> treeNodeList, UserGroupVO vo, List<UserGroupVO> allUserGroupVOArr) {
        if (vo == null || vo.getParentId() == null) {
            return;
        }

        Optional<UserGroupVO> parentNodeStream =
                allUserGroupVOArr.stream().filter(userGroupVO -> userGroupVO.getId().equals(vo.getParentId())).findFirst();
        if (!parentNodeStream.isPresent()) {
            return;
        }
        UserGroupVO parentNode = parentNodeStream.get();
        if (!treeNodeList.contains(parentNode)) {
            treeNodeList.add(parentNode);
        }
        recursiveTree(treeNodeList, parentNode, allUserGroupVOArr);
    }

    /**
     * @api {POST} /rco/user/group/create 创建用户组
     * @apiName 创建用户组
     * @apiGroup 用户组管理相关
     * @apiDescription 创建用户组
     * @apiParam (请求路径字段说明) {String} groupName 用户组名
     * @apiParam (请求体字段说明) {Object[]} parent 父类用户组
     * @apiParam (请求体字段说明) {String} loginIdentityLevel 双网身份验证
     * @apiParam (请求体字段说明) {Object[]} vdiDesktopConfig VDI云桌面配置
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "groupName": "test",
     *                  "parent": {
     *                  "id": "fc95708f-6314-48a4-96cf-5e7b7ce9f923",
     *                  "label": "test",
     *                  "row": {
     *                  "allowDelete": true,
     *                  "children": [],
     *                  "disabled": false,
     *                  "enableAd": false,
     *                  "enableDefault": false,
     *                  "id": "fc95708f-6314-48a4-96cf-5e7b7ce9f923",
     *                  "label": "test",
     *                  "parentId": "root"
     *                  }
     *                  },
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
     *                  "cloudNumber": 11,
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
     *                  "clouldDeskopNumOfPersonal": 6,
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
     *                  "refCount": 10,
     *                  "totalCount": 51
     *                  }],
     *                  "ipType": "IPV4",
     *                  "networkType": "FLAT",
     *                  "refCount": 10,
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
     *                    "content": null,
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
     * 创建分组
     *
     * @param request        分组参数对象
     * @param sessionContext session上下文
     * @return 返回成功或失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建用户组")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/create")
    @EnableAuthority
    public DefaultWebResponse createUserGroup(CreateUserGroupWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "CreateUserGroupWebRequest不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");

        //先校验UPM策略是否适合
        userConfigHelper.checkUserProfile(request.getIdvDesktopConfig(), request.getVoiDesktopConfig(), request.getVdiDesktopConfig());

        IacCreateUserGroupDTO createUserGroupRequest = CreateUserGroupWebRequest.convertFor(request);
        IacUserGroupDetailDTO response;
        try {
            // 校验认证策略
            userConfigHelper.validateUserCertification(request.getPrimaryCertificationVO(),
                    request.getAssistCertification());
            // 校验IDV，VOI，IDV配置正确性
            if (request.getVdiDesktopConfig() != null) {

                validateUserVDIConfig(request.getVdiDesktopConfig());
            }
            userConfigHelper.validateDesktopConfig(request.getIdvDesktopConfig(), request.getVoiDesktopConfig());
            validateAccountExpires(request.getAccountExpireDate());
            validInvalidTime(request.getInvalidTime());

            // 此处不触发同步集群操作
            createUserGroupRequest.setEnableSyncData(false);

            response = userGroupAPI.createUserGroup(createUserGroupRequest);
            UUID groupId = response.getId();
            // 创建桌面配置
            createDesktopConfig(request, groupId);
            if (!permissionHelper.isAllGroupPermission(sessionContext)) {
                permissionHelper.saveAdminGroupPermission(sessionContext, groupId, AdminDataPermissionType.USER_GROUP);
            }
            IacUserLoginIdentityLevelEnum loginIdentityLevel = request.getLoginIdentityLevel();
            Assert.notNull(loginIdentityLevel, "loginIdentityLevel不能为null");
            saveUserIdentityConfig(loginIdentityLevel, groupId, request.getAssistCertification(), request.getPrimaryCertificationVO());

            // 此处触发同步集群操作
            dataSyncAPI.activeSyncUserGroupData(groupId);

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_CREATE_USER_GROUP_SUCCESS_LOG, request.getGroupName());
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, groupId);
        } catch (BusinessException e) {
            LOGGER.error("创建用户组失败", e);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_CREATE_USER_GROUP_FAIL_LOG, e, request.getGroupName(), exceptionMsg);
            String tip = getErrorTip(sessionContext, e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, tip);
        }
    }

    private void createDesktopConfig(CreateUserGroupWebRequest request, UUID groupId) throws BusinessException {
        // 保存VDI云桌面配置
        VdiDesktopConfigVO vdiDesktopConfigVO = request.getVdiDesktopConfig();
        if (vdiDesktopConfigVO != null) {
            CreateUserGroupDesktopConfigRequest configRequest = new CreateUserGroupDesktopConfigRequest(groupId, UserCloudDeskTypeEnum.VDI);
            configRequest.setNetworkId(vdiDesktopConfigVO.getNetwork().getAddress().getId());
            configRequest.setStrategyId(vdiDesktopConfigVO.getStrategy().getId());
            configRequest.setImageTemplateId(vdiDesktopConfigVO.getImage().getId());
            if (Objects.nonNull(vdiDesktopConfigVO.getUserProfileStrategy())) {
                configRequest.setUserProfileStrategyId(vdiDesktopConfigVO.getUserProfileStrategy().getId());
            }
            if (Objects.nonNull(vdiDesktopConfigVO.getSoftwareStrategy())) {
                configRequest.setSoftwareStrategyId(vdiDesktopConfigVO.getSoftwareStrategy().getId());
            }
            if (vdiDesktopConfigVO.getCluster() != null) {
                configRequest.setClusterId(vdiDesktopConfigVO.getCluster().getId());
                // 先保存规格信息
                CbbCreateDeskSpecDTO createDeskSpecDTO = new CbbCreateDeskSpecDTO(deskSpecAPI.buildCbbDeskSpec(configRequest.getClusterId(),
                        vdiDesktopConfigVO.toDeskSpec()));
                configRequest.setDeskSpecId(cbbDeskSpecAPI.create(createDeskSpecDTO));
            }
            if (vdiDesktopConfigVO.getCloudPlatform() != null) {
                configRequest.setPlatformId(vdiDesktopConfigVO.getCloudPlatform().getId());
            }
            userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(configRequest);
        }
        // 保存IDV云桌面配置
        IdvDesktopConfigVO idvDesktopConfigVO = request.getIdvDesktopConfig();
        if (idvDesktopConfigVO != null) {
            CreateUserGroupDesktopConfigRequest idvConfigRequest = new CreateUserGroupDesktopConfigRequest(groupId, UserCloudDeskTypeEnum.IDV);
            idvConfigRequest.setStrategyId(idvDesktopConfigVO.getStrategy().getId());
            idvConfigRequest.setImageTemplateId(idvDesktopConfigVO.getImage().getId());
            if (idvDesktopConfigVO.getSoftwareStrategy() != null) {
                idvConfigRequest.setSoftwareStrategyId(idvDesktopConfigVO.getSoftwareStrategy().getId());
            }
            if (Objects.nonNull(idvDesktopConfigVO.getUserProfileStrategy())) {
                idvConfigRequest.setUserProfileStrategyId(idvDesktopConfigVO.getUserProfileStrategy().getId());
            }
            userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(idvConfigRequest);
        }
        // 保存voi云桌面配置
        VoiDesktopConfigVO voiDesktopConfig = request.getVoiDesktopConfig();
        if (voiDesktopConfig != null) {
            CreateUserGroupDesktopConfigRequest voiConfigRequest = new CreateUserGroupDesktopConfigRequest(groupId, UserCloudDeskTypeEnum.VOI);
            voiConfigRequest.setStrategyId(voiDesktopConfig.getStrategy().getId());
            voiConfigRequest.setImageTemplateId(voiDesktopConfig.getImage().getId());
            if (voiDesktopConfig.getSoftwareStrategy() != null) {
                voiConfigRequest.setSoftwareStrategyId(voiDesktopConfig.getSoftwareStrategy().getId());
            }
            if (Objects.nonNull(voiDesktopConfig.getUserProfileStrategy())) {
                voiConfigRequest.setUserProfileStrategyId(voiDesktopConfig.getUserProfileStrategy().getId());
            }
            userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(voiConfigRequest);

        }
    }

    /**
     * @api {POST} /rco/user/group/edit 编辑用户组
     * @apiName 编辑用户组
     * @apiGroup 用户组管理相关
     * @apiDescription 编辑用户组
     * @apiParam (请求路径字段说明) {String} groupName 用户组名
     * @apiParam (请求体字段说明) {Object[]} parent 父类用户组
     * @apiParam (请求体字段说明) {String} loginIdentityLevel 双网身份验证
     * @apiParam (请求体字段说明) {Object[]} vdiDesktopConfig VDI云桌面配置
     * @apiParam (请求体字段说明) {String} id 用户ID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "groupName": "test1",
     *                  "parent": {
     *                  "id": "fc95708f-6314-48a4-96cf-5e7b7ce9f923",
     *                  "label": "test"
     *                  },
     *                  "loginIdentityLevel": "MANUAL_LOGIN",
     *                  "vdiDesktopConfig": {
     *                  "desktopRole": "NORMAL",
     *                  "saveAndContinue": false,
     *                  "strategy": {
     *                  "id": "ab2ebda2-2238-4628-8841-33b6f4494297",
     *                  "label": "ljm",
     *                  "row": {
     *                  "canUsed": null,
     *                  "clipBoardMode": null,
     *                  "cloudNumber": null,
     *                  "cpu": 4,
     *                  "deskStrategyState": null,
     *                  "desktopType": "PERSONAL",
     *                  "enableDoubleScreen": null,
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
     *                  "id": "714b71d5-dc96-4070-8636-4fb0bb32a557",
     *                  "imageName": "win7-2",
     *                  "imageOsName": "WIN_7_32",
     *                  "imageState": null,
     *                  "systemDisk": 20
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
     *                  "refCount": null,
     *                  "totalCount": null
     *                  }],
     *                  "ipType": "IPV4",
     *                  "networkType": "FLAT",
     *                  "refCount": 10,
     *                  "totalCount": 51,
     *                  "vlan": null
     *                  }
     *                  }
     *                  }
     *                  },
     *                  "id": "bfe634db-c73b-42e9-8724-f531e0434d93"
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
     *                    "content": null,
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
     * 编辑分组
     *
     * @param request        编辑分组请求参数对象
     * @param sessionContext session信息
     * @return 返回成功或失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑用户组")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/edit")
    @EnableAuthority
    @EnableCustomValidate(validateMethod = "editUserGroupValidate")
    public DefaultWebResponse editUserGroup(UpdateUserGroupWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "UserGroupWebRequest不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");
        // 参数合法性校验
        IacUpdateUserGroupDTO userGroupRequest = UpdateUserGroupWebRequest.convertFor(request);
        try {
            IacUserGroupDetailDTO userGroupDetail = userGroupAPI.getUserGroupDetail(request.getId());
            // 校验认证策略
            userConfigHelper.validateUserCertification(request.getPrimaryCertificationVO(),
                    request.getAssistCertification());
            // 校验IDV，VOI，IDV配置正确性
            if (request.getVdiDesktopConfig() != null) {

                validateUserVDIConfig(request.getVdiDesktopConfig());
            }
            userConfigHelper.validateDesktopConfig(request.getIdvDesktopConfig(), request.getVoiDesktopConfig());
            //规避编辑本身已过期的用户组信息被拦截
            Long accountExpireDate;
            if (userGroupDetail.isAdGroup()) {
                Long accountExpire = userGroupDetail.getAccountExpireDate();
                accountExpireDate = DateUtil.adDomainTimestampToDate(accountExpire).getTime();
            } else {
                accountExpireDate = userGroupDetail.getAccountExpireDate();
            }
            if (!accountExpireDate.equals(request.getAccountExpireDate())) {
                validateAccountExpires(request.getAccountExpireDate());
            }
            validInvalidTime(request.getInvalidTime());

            // 此处不触发同步集群操作
            userGroupRequest.setEnableSyncData(false);

            userGroupAPI.updateUserGroup(userGroupRequest);
            updateGroupDesktopConfig(request);
            updateDashboardUserGroupName(userGroupDetail.getName(), request);
            IacUserLoginIdentityLevelEnum loginIdentityLevel = request.getLoginIdentityLevel();
            Assert.notNull(loginIdentityLevel, "loginIdentityLevel不能为null");
            saveUserIdentityConfig(loginIdentityLevel, request.getId(), request.getAssistCertification(), request.getPrimaryCertificationVO());

            // 此处触发同步集群操作
            dataSyncAPI.activeSyncUserGroupData(request.getId());

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_GROUP_SUCCESS_LOG, request.getGroupName());
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑用户组失败", e);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_USER_GROUP_FAIL_LOG, e, request.getGroupName(), exceptionMsg);
            String tip = getErrorTip(sessionContext, e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, tip);
        }
    }

    private void validateUserVDIConfig(VdiDesktopConfigVO vdiDesktopConfig) throws BusinessException {
        VgpuInfoDTO vgpuInfoDTO = deskSpecAPI.checkAndBuildVGpuInfo(vdiDesktopConfig.getCluster().getId(), vdiDesktopConfig.getVgpuType(),
                vdiDesktopConfig.getVgpuExtraInfo());
        vdiDesktopConfig.setVgpuType(vgpuInfoDTO.getVgpuType());
        if (vgpuInfoDTO.getVgpuExtraInfo() instanceof VgpuExtraInfo) {
            vdiDesktopConfig.setVgpuExtraInfo((VgpuExtraInfo) vgpuInfoDTO.getVgpuExtraInfo());
        } else {
            vdiDesktopConfig.setVgpuExtraInfo(new VgpuExtraInfo());
        }
        userConfigHelper.validateUserVDIConfig(vdiDesktopConfig);
    }

    private void validateAccountExpires(Long expireTime) throws BusinessException {
        if (ObjectUtils.isEmpty(expireTime) || expireTime == 0L) {
            return;
        }
        if (new Date().getTime() > expireTime) {
            String expireTimeFormat = DateUtil.formatDate(new Date(expireTime), DATE_FORMAT_TIME);
            String currentTimeFormat = DateUtil.formatDate(new Date(), DATE_FORMAT_TIME);
            throw new BusinessException(UserGroupBusinessKey.RCO_USER_GROUP_EXPIRE_TIME_FAIL,
                    expireTimeFormat, currentTimeFormat);
        }
    }

    private void validInvalidTime(Integer invalidTime) throws BusinessException {
        if (ObjectUtils.isEmpty(invalidTime)) {
            return;
        }
        if (invalidTime > INVALID_TIME_MAX_VALUE || invalidTime < INVALID_TIME_MIN_VALUE) {
            throw new BusinessException(UserGroupBusinessKey.RCDC_RCO_USER_GROUP_INVALID_TIME_VALIDATE_ERROR, invalidTime.toString());
        }
    }

    /**
     * 修改桌面池报表中相关表里面用户组的名称
     *
     * @param oldUserGroupName 旧的用户组名称
     * @param request          请求
     */
    private void updateDashboardUserGroupName(String oldUserGroupName, UpdateUserGroupWebRequest request) {
        if (!Objects.equals(request.getGroupName(), oldUserGroupName)) {
            LOGGER.info("用户组[{}]修改组名为[{}]，需要修改桌面池报表记录中的组名", oldUserGroupName, request.getGroupName());
            desktopPoolDashboardAPI.updateUserGroupName(request.getId(), request.getGroupName());
        }
    }


    /**
     * 更新用户组桌面配置
     *
     * @param request
     */
    private void updateGroupDesktopConfig(UpdateUserGroupWebRequest request) throws BusinessException {
        // 更新用户组桌面IDV配置
        updateGroupDesktopIdvConfig(request);
        // 更新用户组桌面VDI配置
        updateGroupDesktopVdiConfig(request);
        // 更新用户组桌面VOI配置
        updateGroupDesktopVoiConfig(request);
    }

    /**
     * 更新用户组桌面VOI配置
     *
     * @param request
     */
    private void updateGroupDesktopVoiConfig(UpdateUserGroupWebRequest request) throws BusinessException {
        VoiDesktopConfigVO voiDesktopConfigVO = request.getVoiDesktopConfig();
        // 当用户组VOI云桌面为空删除
        if (voiDesktopConfigVO == null) {
            userDesktopConfigAPI.deleteUserGroupDesktopConfig(request.getId(), UserCloudDeskTypeEnum.VOI);
        } else {
            // 进行用户组VOI云桌面 配置更新
            CreateUserGroupDesktopConfigRequest voiConfigRequest =
                    new CreateUserGroupDesktopConfigRequest(request.getId(), UserCloudDeskTypeEnum.VOI);
            voiConfigRequest.setStrategyId(voiDesktopConfigVO.getStrategy().getId());
            voiConfigRequest.setImageTemplateId(voiDesktopConfigVO.getImage().getId());
            if (Objects.nonNull(voiDesktopConfigVO.getUserProfileStrategy())) {
                voiConfigRequest.setUserProfileStrategyId(voiDesktopConfigVO.getUserProfileStrategy().getId());
            }
            if (Objects.nonNull(voiDesktopConfigVO.getSoftwareStrategy())) {
                voiConfigRequest.setSoftwareStrategyId(voiDesktopConfigVO.getSoftwareStrategy().getId());
            }
            userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(voiConfigRequest);
        }
    }

    /**
     * 更新用户组桌面IDV配置
     *
     * @param request
     */
    private void updateGroupDesktopIdvConfig(UpdateUserGroupWebRequest request) throws BusinessException {
        IdvDesktopConfigVO idvDesktopConfigVO = request.getIdvDesktopConfig();
        // 当用户组IDV云桌面为空删除
        if (idvDesktopConfigVO == null) {
            userDesktopConfigAPI.deleteUserGroupDesktopConfig(request.getId(), UserCloudDeskTypeEnum.IDV);
        } else {
            // 进行用户组IDV云桌面 配置更新
            CreateUserGroupDesktopConfigRequest idvConfigRequest =
                    new CreateUserGroupDesktopConfigRequest(request.getId(), UserCloudDeskTypeEnum.IDV);
            idvConfigRequest.setStrategyId(idvDesktopConfigVO.getStrategy().getId());
            idvConfigRequest.setImageTemplateId(idvDesktopConfigVO.getImage().getId());
            if (Objects.nonNull(idvDesktopConfigVO.getSoftwareStrategy())) {
                idvConfigRequest.setSoftwareStrategyId(idvDesktopConfigVO.getSoftwareStrategy().getId());
            }
            if (Objects.nonNull(idvDesktopConfigVO.getUserProfileStrategy())) {
                idvConfigRequest.setUserProfileStrategyId(idvDesktopConfigVO.getUserProfileStrategy().getId());
            }
            userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(idvConfigRequest);
        }
    }

    /**
     * 更新用户组桌面VDI配置
     *
     * @param request
     */
    private void updateGroupDesktopVdiConfig(UpdateUserGroupWebRequest request) throws BusinessException {
        VdiDesktopConfigVO vdiDesktopConfigVO = request.getVdiDesktopConfig();
        // 当用户组VDI云桌面为空删除
        if (vdiDesktopConfigVO == null) {
            userDesktopConfigAPI.deleteUserGroupDesktopConfig(request.getId(), UserCloudDeskTypeEnum.VDI);
        } else {
            // 进行用户组VDI云桌面 配置更新
            CreateUserGroupDesktopConfigRequest vdiConfigRequest =
                    new CreateUserGroupDesktopConfigRequest(request.getId(), UserCloudDeskTypeEnum.VDI);
            // 先保存规格信息
            vdiConfigRequest.setImageTemplateId(vdiDesktopConfigVO.getImage().getId());
            vdiConfigRequest.setStrategyId(vdiDesktopConfigVO.getStrategy().getId());
            vdiConfigRequest.setNetworkId(vdiDesktopConfigVO.getNetwork().getAddress().getId());
            if (Objects.nonNull(vdiDesktopConfigVO.getSoftwareStrategy())) {
                vdiConfigRequest.setSoftwareStrategyId(vdiDesktopConfigVO.getSoftwareStrategy().getId());
            }
            if (Objects.nonNull(vdiDesktopConfigVO.getUserProfileStrategy())) {
                vdiConfigRequest.setUserProfileStrategyId(vdiDesktopConfigVO.getUserProfileStrategy().getId());
            }
            if (vdiDesktopConfigVO.getCluster() != null) {
                vdiConfigRequest.setClusterId(vdiDesktopConfigVO.getCluster().getId());
                CbbCreateDeskSpecDTO createDeskSpecDTO = new CbbCreateDeskSpecDTO(deskSpecAPI.buildCbbDeskSpec(vdiConfigRequest.getClusterId(),
                        vdiDesktopConfigVO.toDeskSpec()));
                vdiConfigRequest.setDeskSpecId(cbbDeskSpecAPI.create(createDeskSpecDTO));
            }
            if (vdiDesktopConfigVO.getCloudPlatform() != null) {
                vdiConfigRequest.setPlatformId(vdiDesktopConfigVO.getCloudPlatform().getId());
            }
            userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(vdiConfigRequest);
        }
    }

    private void saveUserIdentityConfig(IacUserLoginIdentityLevelEnum loginIdentityLevel, UUID groupId, AssistCertificationVO assistCertificationVO,
                                        PrimaryCertificationVO primaryCertificationVO) throws BusinessException {
        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USERGROUP, groupId);

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
     * 删除分组
     *
     * @param request        分组id请求参数对象
     * @param sessionContext session信息
     * @param builder        builder
     * @return 返回成功或失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除分组")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse deleteUserGroup(DelUserGroupWebRequest request, SessionContext sessionContext, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");
        Assert.notNull(builder, "builder不能为null");

        UUID[] userGroupIdArr = request.getIdArr();
        UUID moveGroupId = request.getMoveGroupId();
        LOGGER.debug("deleteUserGroup info userGroupIdArr={},moveGroupId={}", Arrays.toString(userGroupIdArr), moveGroupId);

        final Iterator<DeleteUserGroupBatchTaskItem> iterator =
                Stream.of(userGroupIdArr).map(userGroupId -> DeleteUserGroupBatchTaskItem.builder().itemId(userGroupId) //
                        .itemName(UserBusinessKey.RCDC_RCO_USER_GROUP_DELETE_ITEM_NAME, new String[]{}).itemUser(moveGroupId).build()).iterator();
        DeleteUserGroupBatchTaskHandlerRequest deleteUserGroupBatchTaskHandlerRequest = new DeleteUserGroupBatchTaskHandlerRequest();
        deleteUserGroupBatchTaskHandlerRequest.setBatchTaskItemIterator(iterator);
        deleteUserGroupBatchTaskHandlerRequest.setAuditLogAPI(auditLogAPI);
        deleteUserGroupBatchTaskHandlerRequest.setCbbUserGroupAPI(userGroupAPI);
        deleteUserGroupBatchTaskHandlerRequest.setUserDesktopConfigAPI(userDesktopConfigAPI);
        deleteUserGroupBatchTaskHandlerRequest.setPermissionHelper(permissionHelper);
        deleteUserGroupBatchTaskHandlerRequest.setSessionContext(sessionContext);
        deleteUserGroupBatchTaskHandlerRequest.setDesktopPoolMgmtAPI(desktopPoolMgmtAPI);
        deleteUserGroupBatchTaskHandlerRequest.setUserGroupHelper(userGroupHelper);
        deleteUserGroupBatchTaskHandlerRequest.setRcaGroupMemberAPI(rcaGroupMemberAPI);
        DeleteUserGroupBatchTaskHandler handler = new DeleteUserGroupBatchTaskHandler(deleteUserGroupBatchTaskHandlerRequest);
        handler.setAdminDTO(iacAdminMgmtAPI.getLoginUserInfo());
        if (Arrays.asList(userGroupIdArr).size() == 1) {
            handler.setBatch(false);
        }
        BatchTaskSubmitResult batchTaskSubmitResult = builder.setTaskName(UserBusinessKey.RCDC_RCO_USER_GROUP_DELETE_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_USER_GROUP_DELETE_TASK_DESC).enableParallel().registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
    }

    private String getErrorTip(SessionContext sessionContext, BusinessException e) {
        String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            if (e.getKey().equals(UserConstants.IAC_USER_API_SUB_USER_GROUP_NUM_OVER)) {
                return LocaleI18nResolver.resolve(UserBusinessKey.RCDC_USER_SUB_USERGROUP_NUM_OVER_FOR_SYSADMIN, exceptionMsg);
            }
            if (e.getKey().equals(UserConstants.IAC_USER_API_USER_GROUP_HAS_DUPLICATION_NAME)) {
                return LocaleI18nResolver.resolve(UserBusinessKey.RCDC_USER_USERGROUP_HAS_DUPLICATION_NAME_FOR_SYSADMIN, exceptionMsg);
            }
            if (e.getKey().equals(UserConstants.IAC_USER_API_DELETE_USER_GROUP_SUB_GROUP_NAME_DUPLICATION_WITH_MOVE_SUB_GROUP)) {
                return LocaleI18nResolver.resolve(
                        UserBusinessKey.RCDC_USER_DELETE_USER_GROUP_SUB_GROUP_NAME_DUPLICATION_WITH_MOVE_SUB_GROUP_FOR_SYSADMIN, exceptionMsg);
            }
            if (e.getKey().equals(UserConstants.IAC_USER_API_USER_GROUP_NUM_OVER)) {
                return LocaleI18nResolver.resolve(UserBusinessKey.RCDC_USER_USERGROUP_NUM_OVER_FOR_SYSADMIN, exceptionMsg);
            }
        }
        return exceptionMsg;
    }

    /**
     * @api {POST} /rco/user/group/detail 获取用户组详细信息
     * @apiName 获取用户组详细信息
     * @apiGroup 用户组管理相关
     * @apiDescription 获取用户组详细信息
     * @apiParam (请求体字段说明) {String} id 用户ID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "id": "bfe634db-c73b-42e9-8724-f531e0434d93"
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
     *                    "content": {
     *                    "groupName": "112",
     *                    "id": "bfe634db-c73b-42e9-8724-f531e0434d93",
     *                    "loginIdentityLevel": "MANUAL_LOGIN",
     *                    "parent": {
     *                    "id": "fc95708f-6314-48a4-96cf-5e7b7ce9f923",
     *                    "label": "test"
     *                    },
     *                    "vdiDesktopConfig": {
     *                    "image": {
     *                    "id": "714b71d5-dc96-4070-8636-4fb0bb32a557",
     *                    "imageName": "win7-2",
     *                    "imageOsName": "WIN_7_32",
     *                    "imageState": null,
     *                    "systemDisk": 20
     *                    },
     *                    "network": {
     *                    "createTime": 1583821747566,
     *                    "deskNetworkName": "ljm2",
     *                    "deskNetworkState": "AVAILABLE",
     *                    "dnsPrimary": "114.114.114.114",
     *                    "dnsSecondary": null,
     *                    "gateway": "172.28.84.1",
     *                    "id": "6222590d-5219-41f2-9cc1-c96ad9460c48",
     *                    "ipCidr": "172.28.0.0/16",
     *                    "ipPoolArr": [{
     *                    "id": "a5f54b75-d5b3-446b-9279-c350449455e6",
     *                    "ipEnd": "172.28.84.250",
     *                    "ipPoolType": "BUSINESS",
     *                    "ipStart": "172.28.84.200",
     *                    "refCount": null,
     *                    "totalCount": null
     *                    }],
     *                    "ipType": "IPV4",
     *                    "networkType": "FLAT",
     *                    "refCount": 10,
     *                    "totalCount": 51,
     *                    "vlan": null
     *                    },
     *                    "strategy": {
     *                    "canUsed": null,
     *                    "clipBoardMode": null,
     *                    "cloudNumber": null,
     *                    "cpu": 4,
     *                    "deskStrategyState": null,
     *                    "desktopType": "PERSONAL",
     *                    "enableDoubleScreen": null,
     *                    "enableInternet": true,
     *                    "enableUsbReadOnly": false,
     *                    "id": "ab2ebda2-2238-4628-8841-33b6f4494297",
     *                    "memory": 8,
     *                    "personalDisk": 60,
     *                    "strategyName": "ljm",
     *                    "systemDisk": 40,
     *                    "usbTypeIdArr": ["78b75b29-0f59-46c0-9de0-0f172eb23063", "e3f8d1ee-1a5e-4b54-a6cb-3249f2239c6a",
     *                    "476cf4dd-68d7-474a-859a-41644933fd5e", "7b5c3b19-bf20-4f46-86d3-4c1e9d42bea7", "fec22ab4-f565-4c91-9401-dd7b5465edcf",
     *                    "5077c12c-bde9-4396-b8fa-e566dea92cbc"]
     *                    }
     *                    }
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
     * 获取用户分组详细信息
     *
     * @param request 分组id请求参数对象
     * @return 返回分组详情
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse queryUserGroupDetail(UserGroupIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "UserGroupIdWebRequest不能为null");

        UUID groupId = request.getId();
        IacUserGroupDetailDTO response = userGroupAPI.getUserGroupDetail(groupId);
        UserGroupDetailWebResponse webResponse = userGroupHelper.buildUserGroupDetailForWebResponse(response);

        IacUserIdentityConfigRequest identityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USERGROUP, groupId);
        IacUserIdentityConfigResponse userIdentityConfigResponse = userIdentityConfigAPI.findUserIdentityConfigByRelated(identityConfigRequest);
        webResponse.setLoginIdentityLevel(userIdentityConfigResponse.getLoginIdentityLevel());
        webResponse.setOpenHardwareCertification(Optional.ofNullable(userIdentityConfigResponse.getOpenHardwareCertification())
                .orElse(HardwareConstants.RCDC_HARDWARE_CERTIFICATION_DEFAULT));
        webResponse.setMaxHardwareNum(userIdentityConfigResponse.getMaxHardwareNum());
        webResponse.setOpenOtpCertification(
                Optional.ofNullable(userIdentityConfigResponse.getOpenOtpCertification()).orElse(OtpConstants.RCDC_OTP_CERTIFICATION_DEFAULT));
        webResponse.setOpenAccountPasswordCertification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenAccountPasswordCertification()));
        webResponse.setOpenThirdPartyCertification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenThirdPartyCertification()));
        webResponse.setOpenCasCertification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenCasCertification()));
        webResponse.setOpenSmsCertification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenSmsCertification()));
        webResponse.setOpenRadiusCertification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenRadiusCertification()));
        webResponse.setOpenWorkWeixinCertification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenWorkWeixinCertification()));
        webResponse.setOpenFeishuCertification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenFeishuCertification()));
        webResponse.setOpenDingdingCertification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenDingdingCertification()));
        webResponse.setOpenOauth2Certification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenOauth2Certification()));
        webResponse.setOpenRjclientCertification(BooleanUtils.toBoolean(userIdentityConfigResponse.getOpenRjclientCertification()));
        return DefaultWebResponse.Builder.success(webResponse);
    }


    /**
     * 校验同组下的子组名称是否已存在
     *
     * @param request 请求参数对象
     * @return 返回是否存在重名组
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/checkGroupNameDuplication")
    public DefaultWebResponse checkUserGroupDuplication(CheckDuplicationGroupNameWebRequest request) throws BusinessException {
        Assert.notNull(request, "CheckDuplicationGroupNameWebRequest不能为null");
        UUID parentId = request.getParent();
        IacGetUserGroupDTO userGroupDTO = new IacGetUserGroupDTO(parentId, request.getGroupName());
        boolean hasDuplication = isReservedName(request.getGroupName());

        if (!hasDuplication) {
            List<IacUserGroupDetailDTO> userGroupDetailDTOList = userGroupAPI.listByParentIdAndName(userGroupDTO);
            hasDuplication = hasDuplication(userGroupDetailDTOList, request.getId());
        }

        return DefaultWebResponse.Builder.success(new CheckDuplicationDTO(hasDuplication));
    }

    private boolean hasDuplication(List<IacUserGroupDetailDTO> userGroupDetailDTOList, UUID id) {
        if (CollectionUtils.isEmpty(userGroupDetailDTOList)) {
            return false;
        }

        // 数据库有对应组名，但是页面id为null说明是创建组，否则为更新组
        if (id == null) {
            return true;
        }

        // 编辑更新
        for (IacUserGroupDetailDTO groupDetailDTO : userGroupDetailDTOList) {
            if (!groupDetailDTO.getId().equals(id)) {
                return true;
            }
        }

        return false;


    }

    private boolean isReservedName(String groupName) {
        return getReserveGroupName().contains(groupName);
    }

    private List<String> getReserveGroupName() {
        List<String> reserveGroupNameList = Lists.newArrayList();
        reserveGroupNameList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_USER_DEFAULT_USER_GROUP_NAME_1));
        reserveGroupNameList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_USER_DEFAULT_USER_GROUP_NAME_2));
        return reserveGroupNameList;
    }

    /**
     * 应用分组的云桌面配置到未配置云桌面的用户
     *
     * @param request        请求参数对象
     * @param builder        批量任务
     * @return 返回成功失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量应用VDI云桌面")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/applyDesktop")
    @EnableAuthority
    public DefaultWebResponse applyDesktop(IdWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "IdWebRequest不能为null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        UUID groupId = request.getId();
        // 查询分组是否配置了云桌面,没有配置则不用继续执行配置用户桌面
        UserGroupDesktopConfigDTO userGroupDesktopConfigDTO = userDesktopConfigAPI.getUserGroupDesktopConfig(groupId, UserCloudDeskTypeEnum.VDI);
        if (userGroupDesktopConfigDTO == null) {
            LOGGER.info("用户分组[{}]没有配置vdi云桌面，不应用分组配置到用户", request.getId());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_NOT_CONFIG_VDI_DESKTOP);
        }
        // 查询分组下的用户,只获取未配置vdi云桌面的用户
        UUID[] idArr = userGroupHelper.getUnBindDesktopUserListByGroupId(groupId, UserCloudDeskTypeEnum.VDI);
        if (idArr.length == 0) {
            LOGGER.info("分组[{}]下没有需要配置桌面的普通用户以及AD域用户，不应用云桌面配置", groupId);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_NOT_EXIST_NEED_CONFIG_DESKTOP);
        }
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()//
                .map(id -> DefaultBatchTaskItem.builder()//
                        .itemId(id)//
                        .itemName(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_TASK_NAME, new String[]{})//
                        .build())
                .iterator();
        UserGroupDesktopConfigDTO vdiConfig = userDesktopConfigAPI.getUserGroupDesktopConfig(groupId, UserCloudDeskTypeEnum.VDI);
        ApplyDesktopBatchTaskHandler handler = new ApplyDesktopBatchTaskHandler(vdiConfig, iterator);

        final UUID uniqueTaskThreadPoolId = UUID.nameUUIDFromBytes("applyDesktopThreadPool".getBytes());
        BatchTaskSubmitResult submitResult =
                builder.setTaskName(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_TASK_NAME).setTaskDesc(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_TASK_DESC)
                        .enableParallel().enablePerformanceMode(uniqueTaskThreadPoolId, 30).registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(submitResult);
    }

    /**
     * 应用分组的云桌面配置到未配置云桌面的用户
     *
     * @param request        请求参数对象
     * @param builder        批量任务
     * @return 返回成功失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量应用IDV云桌面")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/applyIDVDesktop")
    @EnableAuthority
    public DefaultWebResponse applyIDVDesktop(IdWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "IdWebRequest不能为null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        UUID groupId = request.getId();
        // 查询分组是否配置了云桌面,没有配置则不用继续执行配置用户桌面
        UserGroupDesktopConfigDTO userGroupDesktopConfigDTO = userDesktopConfigAPI.getUserGroupDesktopConfig(groupId, UserCloudDeskTypeEnum.IDV);
        if (userGroupDesktopConfigDTO == null) {
            LOGGER.info("用户分组[{}]没有配置idv云桌面，不应用分组配置到用户", request.getId());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_NOT_CONFIG_IDV_DESKTOP);
        }
        // 查询分组下的用户,只获取未配置idv云桌面的用户
        UUID[] idArr = userGroupHelper.getUnBindDesktopUserListByGroupId(groupId, UserCloudDeskTypeEnum.IDV);
        if (idArr.length == 0) {
            LOGGER.info("分组[{}]下没有需要配置idv桌面的普通用户以及AD域用户，不应用idv云桌面配置", groupId);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_NOT_EXIST_NEED_CONFIG_IDV_DESKTOP);
        }
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()//
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)//
                        .itemName(UserBusinessKey.RCDC_RCO_APPLY_IDV_DESKTOP_TASK_NAME, new String[]{})//
                        .build())
                .iterator();
        IacUserGroupDetailDTO groupInfo = userGroupAPI.getUserGroupDetail(groupId);
        ApplyIDVDesktopBatchTaskHandler handler = new ApplyIDVDesktopBatchTaskHandler(groupInfo, iterator);

        final UUID uniqueTaskThreadPoolId = UUID.nameUUIDFromBytes("applyIDVDesktopThreadPool".getBytes());
        BatchTaskSubmitResult submitResult = builder.setTaskName(UserBusinessKey.RCDC_RCO_APPLY_IDV_DESKTOP_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_TASK_DESC).enableParallel().enablePerformanceMode(uniqueTaskThreadPoolId, 30)
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(submitResult);
    }

    /**
     * 应用分组的云桌面配置到未配置云桌面的用户
     *
     * @param request        请求参数对象
     * @param builder        批量任务
     * @return 返回成功失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量应用TCI云桌面")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/applyVOIDesktop")
    @EnableAuthority
    public DefaultWebResponse applyVOIDesktop(IdWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "IdWebRequest不能为null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        UUID groupId = request.getId();
        // 查询分组是否配置了云桌面,没有配置则不用继续执行配置用户桌面
        UserGroupDesktopConfigDTO userGroupDesktopConfigDTO = userDesktopConfigAPI.getUserGroupDesktopConfig(groupId, UserCloudDeskTypeEnum.VOI);
        if (userGroupDesktopConfigDTO == null) {
            LOGGER.info("用户分组[{}]没有配置voi云桌面，不应用分组配置到用户", request.getId());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_NOT_CONFIG_VOI_DESKTOP);
        }
        // 查询分组下的用户,只获取未配置voi云桌面的用户
        UUID[] idArr = userGroupHelper.getUnBindDesktopUserListByGroupId(groupId, UserCloudDeskTypeEnum.VOI);
        if (idArr.length == 0) {
            LOGGER.info("分组[{}]下没有需要配置voi桌面的普通用户以及AD域用户，不应用voi云桌面配置", groupId);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_NOT_EXIST_NEED_CONFIG_VOI_DESKTOP);
        }
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()//
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)//
                        .itemName(UserBusinessKey.RCDC_RCO_APPLY_VOI_DESKTOP_TASK_NAME, new String[]{})//
                        .build())
                .iterator();
        IacUserGroupDetailDTO groupInfo = userGroupAPI.getUserGroupDetail(groupId);
        ApplyVOIDesktopBatchTaskHandler handler = new ApplyVOIDesktopBatchTaskHandler(groupInfo, iterator);

        final UUID uniqueTaskThreadPoolId = UUID.nameUUIDFromBytes("applyVOIDesktopThreadPool".getBytes());
        BatchTaskSubmitResult submitResult = builder.setTaskName(UserBusinessKey.RCDC_RCO_APPLY_VOI_DESKTOP_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_TASK_DESC).enableParallel().enablePerformanceMode(uniqueTaskThreadPoolId, 30)
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(submitResult);
    }



    /**
     * 修改ad域用户权限
     *
     * @param request        页面请求参数
     * @param builder        批处理任务结果
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("修改ad域用户权限")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "adUserAuthority/edit")
    @EnableAuthority
    public DefaultWebResponse editAdUserAuthority(AdUserGroupAuthorityEditWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "AdUserGroupAuthorityEditWebRequest can not null");
        Assert.notNull(builder, "BatchTaskBuilder can not null");

        UUID[] idArr = new UUID[]{request.getId()};
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()//
                .map(uid -> DefaultBatchTaskItem.builder().itemId(uid)//
                        .itemName(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_TASK_NAME, new String[]{}).build())//
                .iterator();

        AdUserGroupAuthorityBatchTaskHandler adUserAuthorityBatchTaskHandler =
                new AdUserGroupAuthorityBatchTaskHandler(iterator, auditLogAPI, userGroupAPI, request.getAdUserAuthority());
        adUserAuthorityBatchTaskHandler.setUserEventNotifyAPI(userEventNotifyAPI);

        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_BATCH_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_TASK_DESC).registerHandler(adUserAuthorityBatchTaskHandler)
                .start();

        return DefaultWebResponse.Builder.success(result);

    }

    /**
     * 获取ad域用户组权限信息
     *
     * @param request 页面请求参数
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "adUserAuthority/detail")
    public DefaultWebResponse detailAdUserGroupAuthority(AdUserGroupAuthorityDetailWebRequest request) throws BusinessException {
        Assert.notNull(request, "AdUserGroupAuthorityDetailWebRequest can not null");
        IacUserGroupDetailDTO response = userGroupAPI.getUserGroupDetail(request.getId());

        return DefaultWebResponse.Builder.success(response);
    }

}
