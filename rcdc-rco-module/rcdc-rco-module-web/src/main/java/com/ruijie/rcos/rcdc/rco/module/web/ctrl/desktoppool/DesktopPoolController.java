package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbCreateDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCheckDeskPoolNameDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCreateDeskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbHostOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbLoadBalanceStrategyEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.DeskSpecDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants.DesktopPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.EditPoolDeskSpecRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UpdateDesktopPoolRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UpdateLoadBalanceRequest;
import com.ruijie.rcos.rcdc.rco.module.def.user.common.UserCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.request.CommonPageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.common.DesktopPoolWebHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.validation.DesktopPoolValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop.EditNeworkWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop.EditStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.CheckDuplicationResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.DesktopNetworkDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 池桌面WEB控制器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月12日
 *
 * @author linke
 */
@Api(tags = "池桌面管理")
@Controller
@RequestMapping("/rco/cloudDesktop/pool")
@EnableCustomValidate(validateClass = DesktopPoolValidation.class)
public class DesktopPoolController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolController.class);

    private static final String SOFTWARE_STRATEGY = "softwareStrategy";

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private DesktopPoolWebHelper desktopPoolWebHelper;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private UserCommonHelper userCommonHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private UserProfileValidateAPI userProfileValidaAPI;

    @Autowired
    private DesktopPoolDashboardAPI desktopPoolDashboardAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Autowired
    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private DesktopPoolThirdPartyMgmtAPI desktopPoolThirdPartyMgmtAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    @Autowired
    private DesktopPoolComputerMgmtAPI desktopPoolComputerMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private static final String ROOT_IMAGE_ID = "rootImageId";

    /**
     * 查询
     *
     * @param request        页面请求参数
     * @param sessionContext session信息
     * @return CommonWebResponse<DefaultPageResponse < DesktopPoolDTO>>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("桌面池列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<DesktopPoolDTO>> list(CommonPageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        DesktopPoolPageRequest pageReq = new DesktopPoolPageRequest(request);
        if (!Objects.equals(Boolean.TRUE, request.getNoPermission()) && !permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] desktopPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.DESKTOP_POOL);
            if (ArrayUtils.isEmpty(desktopPoolIdArr)) {
                return CommonWebResponse.success(new DefaultPageResponse<>());
            }
            pageReq.appendCustomMatchEqual(new MatchEqual(DesktopPoolConstants.DATA_PERMISSION_KEY, desktopPoolIdArr));
        }

        DefaultPageResponse<DesktopPoolDTO> resp = desktopPoolMgmtAPI.pageQuery(pageReq);
        if (pageReq.getNeedDealCanUsed()) {
            return CommonWebResponse.success(desktopPoolWebHelper.dealCanUsed(pageReq, resp));
        }

        // 镜像模板交付-桌面池列表过滤多会话静态池
        for (ExactMatch exactMatch : request.getExactMatchArr()) {
            if (ROOT_IMAGE_ID.equals(exactMatch.getName())) {
                CommonWebResponse.success(desktopPoolWebHelper.dealCanUsedStaticPool(resp));
            }
        }
        return CommonWebResponse.success(resp);
    }

    /**
     * 查询用户的桌面池列表
     *
     * @param request        页面请求参数
     * @param sessionContext session信息
     * @return CommonWebResponse<DefaultPageResponse < DesktopPoolDTO>>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询用户的桌面池列表")
    @RequestMapping(value = "/listByUser", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<DesktopPoolDTO>> listByUser(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        DesktopPoolPageRequest pageReq = new DesktopPoolPageRequest(request);
        Assert.notNull(pageReq.getUserId(), "userId is null.");

        DefaultPageResponse<DesktopPoolDTO> resp = desktopPoolMgmtAPI.pageQueryByUser(pageReq.getUserId(), pageReq);
        return CommonWebResponse.success(resp);
    }

    /**
     * 查询AD域组的桌面池列表
     *
     * @param request        页面请求参数
     * @param sessionContext session信息
     * @return CommonWebResponse<DefaultPageResponse < DesktopPoolDTO>>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询AD域组的桌面池列表")
    @RequestMapping(value = "/listByAdGroup", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<DesktopPoolDTO>> listByAdGroup(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        DesktopPoolPageRequest pageReq = new DesktopPoolPageRequest(request);
        Assert.notNull(pageReq.getAdGroupId(), "adGroupId is null.");

        DefaultPageResponse<DesktopPoolDTO> resp = desktopPoolMgmtAPI.pageQueryByAdGroup(pageReq.getAdGroupId(), pageReq);
        return CommonWebResponse.success(resp);
    }

    /**
     * 桌面池总览
     *
     * @param request 页面请求参数
     * @return CommonWebResponse<DesktopPoolOverviewDTO>
     */
    @ApiOperation("桌面池总览")
    @RequestMapping(value = "/overview", method = RequestMethod.POST)
    public CommonWebResponse<DesktopPoolOverviewDTO> overview(DesktopPoolOverviewRequest request) {
        Assert.notNull(request, "request must not be null");
        DesktopPoolOverviewDTO overviewDTO = desktopPoolMgmtAPI.getDesktopPoolOverview(request.getPoolModel());
        return CommonWebResponse.success(overviewDTO);
    }

    /**
     * 基本查询明细
     *
     * @param request 页面请求参数
     * @return DesktopPoolDetailDTO
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取桌面池基本信息")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public CommonWebResponse<DesktopPoolDetailDTO> detail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "request id must not be null");
        DesktopPoolDetailDTO desktopPoolDetailDTO = desktopPoolMgmtAPI.getDesktopPoolDetail(request.getId());

        return CommonWebResponse.success(desktopPoolDetailDTO);
    }

    /**
     * 桌面池云桌面策略详情
     *
     * @param request 页面请求参数
     * @return 桌面池云桌面策略详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("桌面池云桌面策略详情")
    @RequestMapping(value = "/strategy/detail", method = RequestMethod.POST)
    public CommonWebResponse<Map<String, CloudDesktopDetailDTO>> detailStrategy(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        // 同CloudDesktopController.strategyDetail方法返回Map<String, CloudDesktopDetailDTO>
        CloudDesktopDetailDTO desktopDTO = desktopPoolMgmtAPI.queryDesktopPoolStrategyDetail(request);
        Map<String, CloudDesktopDetailDTO> resultMap = ImmutableMap.of("strategy", desktopDTO);

        return CommonWebResponse.success(resultMap);
    }

    /**
     * * 桌面池云桌面网络详情
     *
     * @param request 页面请求参数
     * @return 桌面池云桌面网络详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("池桌面云桌面网络详情")
    @RequestMapping(value = "/network/detail", method = RequestMethod.POST)
    public CommonWebResponse<DesktopNetworkDetailVO> detailNetwork(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        DesktopNetworkDTO networkDTO = desktopPoolMgmtAPI.queryDesktopPoolNetworkDetail(request);
        DesktopNetworkDetailVO detail = new DesktopNetworkDetailVO(networkDTO);
        return CommonWebResponse.success(detail);
    }

    /**
     * 桌面池镜像模板详情
     *
     * @param request 页面请求参数
     * @return 桌面池镜像模板详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("桌面池镜像模板详情")
    @RequestMapping(value = "/image/detail", method = RequestMethod.POST)
    public CommonWebResponse<Map<String, DesktopPoolImageTemplateDTO>> detailImageTemplate(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "request.id must not be null");

        DesktopPoolImageTemplateDTO imageTemplate = desktopPoolMgmtAPI.queryDesktopPoolImageTemplate(request.getId());
        Map<String, DesktopPoolImageTemplateDTO> resultMap = ImmutableMap.of("imageTemplate", imageTemplate);

        return CommonWebResponse.success(resultMap);
    }

    /**
     * 桌面池软件管控策略详情
     *
     * @param request 页面请求参数
     * @return 桌面池软件管控策略详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("桌面池软件管控策略详情")
    @RequestMapping(value = "/softwareStrategy/detail", method = RequestMethod.POST)
    public CommonWebResponse<Map<String, DesktopPoolSoftwareStrategyDTO>> detailSoftwareStrategy(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        DesktopPoolSoftwareStrategyDTO softwareStrategy = desktopPoolMgmtAPI.queryDesktopPoolSoftwareStrategy(request.getId());
        if (Objects.isNull(softwareStrategy)) {
            softwareStrategy = new DesktopPoolSoftwareStrategyDTO();
        }
        return CommonWebResponse.success(ImmutableMap.of(SOFTWARE_STRATEGY, softwareStrategy));
    }

    /**
     * 桌面池规格详情
     *
     * @param request 页面请求参数
     * @return 桌面池规格详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("桌面池规格详情")
    @RequestMapping(value = "/spec/detail", method = RequestMethod.POST)
    public CommonWebResponse<DeskSpecDetailDTO> detailDeskSpec(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        DeskSpecDetailDTO specDetailDTO = desktopPoolMgmtAPI.getDesktopPoolSpecDetail(request.getId());
        return CommonWebResponse.success(specDetailDTO);
    }

    /**
     * * 分页查询桌面池关联的所有用户（用户组下的用户+分配的用户）
     *
     * @param request        页面请求参数
     * @param sessionContext 上下文
     * @return DefaultPageResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页查询桌面池关联的所有用户（用户组下的用户+分配的用户）")
    @RequestMapping(value = "/realBindUser/page", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserListDTO>> pageDesktopPoolRealBindUser(PageWebRequest request,
                                                                                           SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        DesktopPoolRealBindUserPageRequest pageRequest = new DesktopPoolRealBindUserPageRequest(request);
        Assert.notNull(pageRequest.getDesktopPoolId(), "pageRequest.desktopPoolId must not be null");

        userCommonHelper.dealNonVisitorUserTypeMatch(pageRequest);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<String> userGroupIdList = getPermissionUserGroupIdList(sessionContext.getUserId());
            if (CollectionUtils.isNotEmpty(userGroupIdList)) {
                UUID[] uuidArr = userGroupIdList.stream().filter(idStr -> !idStr.equals(UserGroupHelper.USER_GROUP_ROOT_ID)).map(UUID::fromString)
                        .toArray(UUID[]::new);
                pageRequest.appendCustomMatchEqual(new MatchEqual(UserPageSearchRequest.GROUP_ID, uuidArr));
            }
        }
        return CommonWebResponse.success(desktopPoolUserMgmtAPI.pageQueryRealBindUser(pageRequest.getDesktopPoolId(), pageRequest));
    }


    /**
     * * 获取已分配到桌面池的PC终端列表（pc终端组下的终端+分配的终端）
     *
     * @param request        页面请求参数
     * @param sessionContext 上下文
     * @return DefaultPageResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取已分配到桌面池的PC终端列表（pc终端组下的终端+分配的终端）")
    @RequestMapping(value = "/realBindComputer/page", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<DefaultPageResponse<ComputerDTO>> pageDesktopPoolRealBindComputer(PageWebRequest request,
                                                                                               SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        DesktopPoolRealBindComputerPageRequest pageRequest = new DesktopPoolRealBindComputerPageRequest(request);
        Assert.notNull(pageRequest.getDesktopPoolId(), "pageRequest.desktopPoolId must not be null");
        Boolean isPermissionLimit = checkPermission(request, sessionContext, pageRequest);
        if (Boolean.FALSE.equals(isPermissionLimit)) {
            return CommonWebResponse.success();
        }
        return CommonWebResponse.success(desktopPoolComputerMgmtAPI.pageQueryRealBindComputer(pageRequest.getDesktopPoolId(), pageRequest));
    }

    private Boolean checkPermission(PageWebRequest request, SessionContext session, PageSearchRequest pageReq) throws BusinessException {
        if (!permissionHelper.isAllGroupPermission(session)) {
            UUID[] terminalGroupIdArr = permissionHelper.getTerminalGroupIdArr(session.getUserId());
            List<UUID> terminalGroupIdList = Arrays.asList(terminalGroupIdArr);
            String terminalGroupId = getTerminalGroupId(request);
            if (StringUtils.isEmpty(terminalGroupId)) {
                MatchEqual matchEqual = new MatchEqual();
                matchEqual.setName("terminalGroupId");
                matchEqual.setValueArr(terminalGroupIdArr);
                pageReq.appendCustomMatchEqual(matchEqual);
            } else {
                if (!terminalGroupIdList.contains(UUID.fromString(terminalGroupId))) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getTerminalGroupId(PageWebRequest request) {
        if (org.apache.commons.lang3.ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (exactMatch.getName().equals("terminalGroupId") && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        return "";
    }

    /**
     * * 查询桌面池关联的所有PC终端/终端组信息
     *
     * @param request        页面请求参数
     * @param sessionContext 上下文
     * @return DefaultPageResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询桌面池关联的所有PC终端/终端组信息")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"查询桌面池关联的所有PC终端/终端组信息"})})
    @RequestMapping(value = "/realBindComputer/info", method = RequestMethod.POST)
    public CommonWebResponse<DesktopPoolComputerDetailDTO> desktopPoolRealBindTerminalGroupList(IdWebRequest request,
                                                                                                SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        List<DesktopPoolComputerDTO> poolComputerGroupList = desktopPoolThirdPartyMgmtAPI.listDesktopPoolUser(request.getId(),
                ComputerRelatedType.COMPUTER_GROUP);
        List<DesktopPoolComputerDTO> poolComputerList = desktopPoolThirdPartyMgmtAPI.listDesktopPoolUser(request.getId(),
                ComputerRelatedType.COMPUTER);
        DesktopPoolComputerDetailDTO desktopPoolComputerDetailDTO = new DesktopPoolComputerDetailDTO();
        desktopPoolComputerDetailDTO.setComputerList(poolComputerList);
        desktopPoolComputerDetailDTO.setComputerGroupList(poolComputerGroupList);

        return CommonWebResponse.success(desktopPoolComputerDetailDTO);
    }

    private List<String> getPermissionUserGroupIdList(UUID adminId) throws BusinessException {
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(adminId);
        ListUserGroupIdResponse listUserGroupIdResponse = adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest);
        return listUserGroupIdResponse.getUserGroupIdList();
    }

    /**
     * 编辑桌面池用户或用户组
     *
     * @param request        编辑桌面池request
     * @param builder        builder
     * @param sessionContext sessionContext
     * @return 响应消息
     * @throws BusinessException BusinessException
     */
    @ApiOperation("编辑桌面池分配用户和用户组")
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> updatePoolBindObject(UpdatePoolBindObjectWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder cannot be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");

        String poolName = request.getDesktopPoolId().toString();
        CbbDesktopPoolDTO desktopPoolDTO;
        try {
            desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(request.getDesktopPoolId());
            poolName = desktopPoolDTO.getName();
        } catch (BusinessException e) {
            LOGGER.error("编辑桌面池[{}]用户或用户组发生异常", poolName, e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UPDATE_BIND_OBJ_FAIL_LOG, poolName, e.getI18nMessage());
            return CommonWebResponse.fail(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UPDATE_BIND_OBJ_FAIL,
                    new String[]{poolName, e.getI18nMessage()});
        }

        UpdatePoolBindObjectDTO bindObjectDTO = new UpdatePoolBindObjectDTO();
        BeanUtils.copyProperties(request, bindObjectDTO);
        bindObjectDTO.setPoolId(request.getDesktopPoolId());

        // 获取管理员的数据权限，超管就是null
        UUID[] groupIdArr = null;
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            groupIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.USER_GROUP);
            desktopPoolWebHelper.checkGroupPermission(bindObjectDTO, groupIdArr);
        }

        String taskName = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_UPDATE_BIND_OBJ);
        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(request.getDesktopPoolId()).itemName(taskName).build();
        Iterator<DefaultBatchTaskItem> iterator = Lists.newArrayList(taskItem).iterator();

        UpdateDesktopPoolUserBatchHandler handler = new UpdateDesktopPoolUserBatchHandler(bindObjectDTO, groupIdArr, iterator);
        BatchTaskSubmitResult result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_UPDATE_BIND_OBJ_TASK_NAME)
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_UPDATE_BIND_OBJ_TASK_DESC, poolName)
                .setUniqueId(request.getDesktopPoolId()).registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * * 池桌面云桌面策略修改
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @return DataResult
     */
    @ApiOperation("池桌面云桌面策略修改")
    @RequestMapping(value = "/strategy/edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> editStrategy(EditStrategyWebRequest request, BatchTaskBuilder builder) {
        Assert.notNull(request, "request must not be null");
        Assert.notEmpty(request.getIdArr(), "request id must not be null");
        Assert.notNull(request.getStrategy().getId(), "request strategy id must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID desktopPoolId = request.getIdArr()[0];
        UUID strategyId = request.getStrategy().getId();

        DesktopPoolBasicDTO desktopPoolDTO = null;
        String poolName = desktopPoolId.toString();

        try {
            desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(desktopPoolId);
            poolName = desktopPoolDTO.getName();

            desktopPoolMgmtAPI.updateStrategyId(desktopPoolId, strategyId);

            if (desktopPoolWebHelper.isSkipAutoUpdateStrategy(desktopPoolDTO)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_STRATEGY_NO_DESK, desktopPoolDTO.getName());
                return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
            }

            List<PoolDesktopInfoDTO> deskInfoList = desktopPoolMgmtAPI.listNormalDeskInfoByDesktopPoolId(desktopPoolId);
            if (CollectionUtils.isEmpty(deskInfoList)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_STRATEGY_NO_DESK, desktopPoolDTO.getName());
                return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
            }

            // 更新桌面池状态为编辑中
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.UPDATING);

            final Iterator<DefaultBatchTaskItem> iterator = deskInfoList.stream().map(item -> DefaultBatchTaskItem.builder().itemId(item.getDeskId())
                    .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_ITEM_NAME, new String[]{}).build()).iterator();
            final EditStrategyBatchTaskHandler handler = new EditStrategyBatchTaskHandler(iterator, request.getStrategy().getId(), auditLogAPI)
                    .setCloudDesktopWebService(cloudDesktopWebService).setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI)
                    .setcbbIDVDeskMgmtAPI(cbbIDVDeskMgmtAPI)
                    .setDeskStrategyTciNotifyAPI(deskStrategyTciNotifyAPI);
            handler.setDesktopPoolId(desktopPoolId);
            BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_BATCH_TASK_DESC).enableParallel().registerHandler(handler).start();
            return CommonWebResponse.success(result);
        } catch (BusinessException e) {
            LOGGER.error("桌面池[{}]修改云桌面策略发生异常", poolName, e);
            return poolUpdateConfigFail(desktopPoolDTO, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_STRATEGY_FAIL, e.getI18nMessage(),
                    poolName);
        } catch (Exception e) {
            LOGGER.error("桌面池[{}]修改云桌面策略发生异常", poolName, e);
            return poolUpdateConfigFail(desktopPoolDTO, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_STRATEGY_FAIL, e.getMessage(), poolName);
        }
    }

    /**
     * * 池桌面云桌面网络修改
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @return DataResult
     */
    @ApiOperation("池桌面云桌面网络修改")
    @RequestMapping(value = "/network/edit", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "networkIpValidate")
    @EnableAuthority
    public CommonWebResponse<?> editNetwork(EditNeworkWebRequest request, BatchTaskBuilder builder) {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID desktopPoolId = request.getIdArr()[0];
        UUID networkId = request.getNetwork().getAddress().getId();

        DesktopPoolBasicDTO desktopPoolDTO = null;
        String poolName = desktopPoolId.toString();
        try {
            desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(desktopPoolId);
            poolName = desktopPoolDTO.getName();
            // 校验桌面池网络策略
            clusterAPI.validateVDIDesktopNetwork(desktopPoolDTO.getClusterId(), networkId);
            desktopPoolMgmtAPI.updateNetworkId(desktopPoolId, networkId);

            if (desktopPoolWebHelper.isSkipAutoUpdateStrategy(desktopPoolDTO)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NETWORK_EDIT_NO_DESK, desktopPoolDTO.getName());
                return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
            }

            List<PoolDesktopInfoDTO> deskInfoList = desktopPoolMgmtAPI.listNormalDeskInfoByDesktopPoolId(desktopPoolId);
            if (CollectionUtils.isEmpty(deskInfoList)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NETWORK_EDIT_NO_DESK, poolName);
                return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
            }

            // 更新桌面池状态为编辑中
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.UPDATING);

            final Iterator<DefaultBatchTaskItem> iterator = deskInfoList.stream().map(item -> DefaultBatchTaskItem.builder().itemId(item.getDeskId())
                    .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_ITEM_NAME, new String[]{}).build()).iterator();
            final UUID taskPoolId = UUID.nameUUIDFromBytes("editDeskNetworkPool".getBytes());
            final EditNetworkBatchTaskHandler handler = new EditNetworkBatchTaskHandler(iterator, request.getNetwork().getAddress().getId(),
                    auditLogAPI);
            handler.setCloudDesktopWebService(cloudDesktopWebService).setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
            handler.setDesktopPoolId(desktopPoolId);
            BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .enablePerformanceMode(taskPoolId, 30).start();
            return CommonWebResponse.success(result);
        } catch (BusinessException e) {
            LOGGER.error("桌面池[{}]修改网络策略发生异常", poolName, e);
            return poolUpdateConfigFail(desktopPoolDTO, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NETWORK_EDIT_FAIL, e.getI18nMessage(), poolName);
        } catch (Exception e) {
            LOGGER.error("桌面池[{}]修改网络策略发生异常", poolName, e);
            return poolUpdateConfigFail(desktopPoolDTO, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NETWORK_EDIT_FAIL, e.getMessage(), poolName);
        }
    }

    /**
     * * 池桌面修改镜像模板
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @return DataResult
     */
    @ApiOperation("桌面池修改镜像模板")
    @RequestMapping(value = "/image/edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> editImageTemplate(DesktopPoolEditImageWebRequest request, BatchTaskBuilder builder) {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "request id must not be null");
        Assert.notNull(request.getImageTemplateId(), "request imageTemplateId must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID desktopPoolId = request.getId();
        UUID imageTemplateId = request.getImageTemplateId();
        DesktopPoolBasicDTO desktopPoolDTO = null;
        String poolName = request.getId().toString();
        try {
            desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(desktopPoolId);
            poolName = desktopPoolDTO.getName();

            desktopPoolMgmtAPI.updateImageTemplateId(desktopPoolId, imageTemplateId);

            // 单会话动态支持实时变更，其他不支持
            if (desktopPoolWebHelper.isSkipAutoUpdateStrategy(desktopPoolDTO)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_NO_DESK, desktopPoolDTO.getName());
                return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
            }

            List<PoolDesktopInfoDTO> deskInfoList = desktopPoolMgmtAPI.listNormalDeskInfoByDesktopPoolId(desktopPoolId);
            if (CollectionUtils.isEmpty(deskInfoList)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_NO_DESK, desktopPoolDTO.getName());
                return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
            }

            // 更新桌面池状态为编辑中
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.UPDATING);
            CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);

            final Iterator<DefaultBatchTaskItem> iterator = buildEditImageTaskItem(deskInfoList);
            final DesktopPoolUpdateImageHandler handler = new DesktopPoolUpdateImageHandler(iterator, imageTemplateId, cbbDesktopPoolDTO);
            BatchTaskSubmitResult result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_STRATEGY_ITEM_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_TASK_DESC, desktopPoolDTO.getName())
                    .enableParallel().registerHandler(handler).start();
            return CommonWebResponse.success(result);
        } catch (BusinessException e) {
            LOGGER.error("桌面池[{}]修改镜像模板发生异常", poolName, e);
            return poolUpdateConfigFail(desktopPoolDTO, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_FAIL, e.getI18nMessage(), poolName);
        } catch (Exception e) {
            LOGGER.error("桌面池[{}]修改镜像模板发生异常", poolName, e);
            return poolUpdateConfigFail(desktopPoolDTO, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_EDIT_FAIL, e.getMessage(), poolName);
        }
    }

    private Iterator<DefaultBatchTaskItem> buildEditImageTaskItem(List<PoolDesktopInfoDTO> deskInfoList) {
        return deskInfoList.stream().map(desktop -> DefaultBatchTaskItem.builder().itemId(desktop.getDeskId())
                .itemName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_STRATEGY_ITEM_NAME, new String[]{}).build()).iterator();
    }

    /**
     * 池桌面修改软件管控策略，目前支持单一修改
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("桌面池修改软件管控策略")
    @RequestMapping(value = "/softwareStrategy/edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> editSoftwareStrategy(UpdatePoolSoftwareStrategyRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "request id must not be null");
        Assert.notNull(builder, "builder must not be null");

        DesktopPoolBasicDTO desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(request.getId());
        checkDesktopPoolState(desktopPoolDTO);
        if (Objects.isNull(request.getSoftwareStrategyId())) {
            desktopPoolMgmtAPI.unbindSoftwareStrategy(request.getId());
        } else {
            desktopPoolMgmtAPI.updateSoftwareStrategy(request.getId(), request.getSoftwareStrategyId());
        }

        if (desktopPoolWebHelper.isSkipAutoUpdateStrategy(desktopPoolDTO)) {
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SOFTWARE_NO_DESK, desktopPoolDTO.getName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        }

        LOGGER.info("修改桌面池[{}]的软件管控策略成功，开始批量应用到关联桌面中，request:{}", desktopPoolDTO.getName(), JSON.toJSONString(request));

        // 修改关联云桌面的软件管控策略
        List<PoolDesktopInfoDTO> deskInfoList = desktopPoolMgmtAPI.listNormalDeskInfoByDesktopPoolId(request.getId());
        if (CollectionUtils.isEmpty(deskInfoList)) {
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SOFTWARE_NO_DESK, desktopPoolDTO.getName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        }

        String poolName = desktopPoolDTO.getName();
        final Iterator<DefaultBatchTaskItem> iterator = deskInfoList.stream().map(desktop -> buildSoftwareStrategyTaskItem(desktop.getDeskId()))
                .iterator();
        final DesktopPoolUpdateSoftwareStrategyHandler handler = new DesktopPoolUpdateSoftwareStrategyHandler(iterator,
                request.getSoftwareStrategyId(), poolName);
        BatchTaskSubmitResult result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_SOFT_STRATEGY_ITEM_NAME)
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SOFTWARE_EDIT_TASK_DESC, poolName)
                .enableParallel().registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    private DefaultBatchTaskItem buildSoftwareStrategyTaskItem(UUID desktopId) {
        return DefaultBatchTaskItem.builder().itemId(desktopId)
                .itemName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_SOFT_STRATEGY_ITEM_NAME, new String[]{}).build();
    }

    /**
     * 池桌面修改UPM配置策略，目前支持单一修改
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("桌面池修改UPM配置策略")
    @RequestMapping(value = "userProfileStrategy/edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> editUserProfileStrategy(UpdatePoolUserProfileStrategyRequest request,
                                                        BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        DesktopPoolBasicDTO desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(request.getId());
        updatePoolUserProfileStrategy(request, desktopPoolDTO);

        if (desktopPoolWebHelper.isSkipAutoUpdateStrategy(desktopPoolDTO)) {
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_USER_PROFILE_NO_DESK, desktopPoolDTO.getName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        }

        LOGGER.info("修改桌面池[{}]的UPM配置策略成功，开始批量应用到关联桌面中，request:{}", desktopPoolDTO.getName(), JSON.toJSONString(request));

        // 修改关联云桌面的UPM配置策略
        List<PoolDesktopInfoDTO> deskInfoList = desktopPoolMgmtAPI.listNormalDeskInfoByDesktopPoolId(request.getId());
        if (CollectionUtils.isEmpty(deskInfoList)) {
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_USER_PROFILE_NO_DESK, desktopPoolDTO.getName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        }

        String poolName = desktopPoolDTO.getName();
        final Iterator<DefaultBatchTaskItem> iterator = deskInfoList.stream().map(desktop -> buildUserProfileStrategyTaskItem(desktop.getDeskId()))
                .iterator();
        final DesktopPoolUpdateUserProfileStrategyHandler handler = new DesktopPoolUpdateUserProfileStrategyHandler(iterator,
                cloudDesktopMgmtAPI, cbbDeskMgmtAPI);
        handler.setUserProfileStrategyId(request.getUserProfileStrategyId());
        handler.setPoolName(poolName);
        handler.setAuditLogAPI(auditLogAPI);
        BatchTaskSubmitResult result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_USER_PROFILE_STRATEGY_ITEM_NAME)
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_USER_PROFILE_EDIT_TASK_DESC, poolName)
                .enableParallel().registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    private void updatePoolUserProfileStrategy(UpdatePoolUserProfileStrategyRequest request, DesktopPoolBasicDTO desktopPoolDTO)
            throws BusinessException {
        checkDesktopPoolState(desktopPoolDTO);
        if (desktopPoolDTO.getPoolType() == CbbDesktopPoolType.THIRD && desktopPoolDTO.getSessionType() == CbbDesktopSessionType.SINGLE) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_PROFILE_STRATEGY_SINGLE_THIRD_PARTY_NOT_SUPPORT, desktopPoolDTO.getName());
        }
        if (Objects.isNull(request.getUserProfileStrategyId())) {
            desktopPoolMgmtAPI.unbindUserProfileStrategy(request.getId());
            return;
        }
        if (desktopPoolDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            userProfileValidaAPI.validateStorageMustFileServer(request.getUserProfileStrategyId());
            desktopPoolMgmtAPI.updateUserProfileStrategy(request.getId(), request.getUserProfileStrategyId());
            return;
        }
        if (desktopPoolDTO.getPoolModel() == CbbDesktopPoolModel.STATIC && desktopPoolDTO.getDesktopType() == CbbCloudDeskPattern.PERSONAL) {
            // 个性桌面池不需要配置UPM配置策略
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_STATIC_EDIT_UPM_FAIL, desktopPoolDTO.getName());
        }
        userProfileValidaAPI.validateUserProfileStrategyMustStoragePersonal(request.getUserProfileStrategyId());
        userProfileValidaAPI.validateUserProfileStrategyImageRefuse(desktopPoolDTO.getImageTemplateId());
        desktopPoolMgmtAPI.updateUserProfileStrategy(request.getId(), request.getUserProfileStrategyId());
    }

    private DefaultBatchTaskItem buildUserProfileStrategyTaskItem(UUID desktopId) {
        return DefaultBatchTaskItem.builder().itemId(desktopId)
                .itemName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_USER_PROFILE_STRATEGY_ITEM_NAME, new String[]{}).build();
    }

    /**
     * 桌面池UPM配置策略详情
     *
     * @param request 页面请求参数
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @ApiOperation("桌面池UPM配置策略详情")
    @RequestMapping(value = "userProfileStrategy/detail", method = RequestMethod.POST)
    public CommonWebResponse<UserProfileStrategyDTO> getUserProfileStrategyDetail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        UserProfileStrategyDTO userProfileStrategy = new UserProfileStrategyDTO();

        DesktopPoolUserProfileStrategyDTO desktopPoolUserProfileStrategyDTO =
                desktopPoolMgmtAPI.queryDesktopPoolUserProfileStrategy(request.getId());

        UUID userProfileStrategyId = desktopPoolUserProfileStrategyDTO.getUserProfileStrategyId();
        if (userProfileStrategyId != null) {
            userProfileStrategy = userProfileMgmtAPI.findUserProfileStrategyById(userProfileStrategyId);
        }

        return CommonWebResponse.success(userProfileStrategy);
    }

    /**
     * * 应用池桌面策略
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用池桌面策略")
    @RequestMapping(value = "/syncConfig", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> syncConfig(IdWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "desktopPoolId must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID desktopPoolId = request.getId();

        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);

        List<PoolDesktopInfoDTO> deskInfoList = desktopPoolMgmtAPI.listNormalDeskInfoByDesktopPoolId(desktopPoolId);
        if (CollectionUtils.isEmpty(deskInfoList)) {
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_NO_DESK, desktopPoolDTO.getName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        }
        String taskName = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG, new String[]{});
        final Iterator<SyncConfigBatchTaskItem> iterator = deskInfoList.stream()
                .map(item -> buildSyncConfigTask(desktopPoolDTO, item, taskName)).iterator();

        final SyncDesktopPoolConfigBatchTaskHandler handler = new SyncDesktopPoolConfigBatchTaskHandler(iterator);
        BatchTaskSubmitResult result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_BATCH_TASK_NAME)
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_BATCH_TASK_DESC)
                .enableParallel().registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    private SyncConfigBatchTaskItem buildSyncConfigTask(CbbDesktopPoolDTO desktopPoolDTO, PoolDesktopInfoDTO desktopInfo, String taskName) {
        SyncConfigBatchTaskItem taskItem = new SyncConfigBatchTaskItem(desktopInfo.getDeskId(), taskName);
        taskItem.setDesktopPool(desktopPoolDTO);
        return taskItem;
    }

    /**
     * 创建桌面池
     *
     * @param request        创建请求参数
     * @param builder        BatchTaskBuilder
     * @param sessionContext session信息
     * @return 响应消息
     * @throws BusinessException 异常信息
     */
    @ApiOperation("创建桌面池")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> create(CreateDesktopPoolWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");

        String poolName = request.getName();
        DesktopPoolBasicDTO desktopPoolDTO = null;
        try {
            // 非Windows Server镜像模板，不支持多会话类型
            if (request.getImageTemplateId() != null) {
                CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(request.getImageTemplateId());
                if (cbbImageTemplateDTO != null && !CbbOsType.isWinServerOs(cbbImageTemplateDTO.getOsType())
                        && request.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_IMAGE_TEMPALTE_FAIL);
                }
            }

            // 构造创建桌面池参数
            CbbCreateDeskPoolDTO createDeskPoolDTO = buildCreateDeskPoolRequest(request);
            createDeskPoolDTO.setPoolState(CbbDesktopPoolState.CREATING);
            desktopPoolMgmtAPI.createDesktopPool(createDeskPoolDTO);

            // 添加数据权限
            permissionHelper.saveAdminGroupPermission(sessionContext, createDeskPoolDTO.getId(), AdminDataPermissionType.DESKTOP_POOL);

            // 绑定软件策略和UPM策略
            saveDesktopPoolConfig(createDeskPoolDTO.getId(), request);

            desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(createDeskPoolDTO.getId());

            // 创建异步执行任务--创建桌面
            if (request.getDesktopNum() <= 0) {
                desktopPoolWebHelper.updatePoolStateFinish(desktopPoolDTO);
                LOGGER.info("创建桌面池[{}]成功,容量为0", desktopPoolDTO.getName());
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_SUCCESS_LOG, poolName);
                return CommonWebResponse.success();
            }

            return startCreatePoolDesktop(request.getDesktopNum(), desktopPoolDTO, builder);
        } catch (BusinessException e) {
            return buildCreatePoolBusinessExceptionResponse(poolName, desktopPoolDTO, e);
        } catch (Exception e) {
            return buildCreatePoolExceptionResponse(poolName, desktopPoolDTO, e);
        }
    }

    /**
     * 创建第三方桌面池
     *
     * @param request        创建请求参数
     * @param builder        BatchTaskBuilder
     * @param sessionContext session信息
     * @return 响应消息
     * @throws BusinessException 异常信息
     */
    @ApiOperation("创建第三方桌面池")
    @RequestMapping(value = "/createThirdParty", method = RequestMethod.POST)
    public CommonWebResponse<?> createThirdParty(CreateThirdPartyDesktopPoolWebRequest request,
                                                 BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");

        String poolName = request.getName();
        DesktopPoolBasicDTO desktopPoolDTO = null;
        try {
            // 构造创建桌面池参数
            CbbCreateDeskPoolDTO createDeskPoolDTO = buildCreateThirdPartyDeskPoolRequest(request);

            // 检查并获取已关联终端id集合
            Set<String> computerGroupArr = desktopPoolThirdPartyMgmtAPI.getDesktopPoolRelationComputerGroup(createDeskPoolDTO.getId());
            DesktopPoolAddComputerWebRequest desktopPoolAddComputerWebRequest = new DesktopPoolAddComputerWebRequest();
            desktopPoolAddComputerWebRequest.setId(createDeskPoolDTO.getId());
            desktopPoolAddComputerWebRequest.setComputerIdArr(request.getComputerIdArr());
            desktopPoolAddComputerWebRequest.setTerminalGroupIdArr(request.getTerminalGroupIdArr());
            // 检查并获取新增终端id集合
            List<UUID> addGroupIdList = checkBeforeAddComputerDesktop(desktopPoolAddComputerWebRequest, computerGroupArr);
            // 获取管理员的数据权限，超管就是null
            UUID[] groupIdArr = null;
            if (!permissionHelper.isAllGroupPermission(sessionContext)) {
                groupIdArr = permissionHelper.getTerminalGroupIdArr(sessionContext.getUserId());
                desktopPoolWebHelper.checkTerminalGroupPermission(desktopPoolAddComputerWebRequest, groupIdArr);
            }

            createDeskPoolDTO.setPoolState(CbbDesktopPoolState.CREATING);
            desktopPoolMgmtAPI.createDesktopPool(createDeskPoolDTO);

            // 添加数据权限
            permissionHelper.saveAdminGroupPermission(sessionContext, createDeskPoolDTO.getId(), AdminDataPermissionType.DESKTOP_POOL);

            // 绑定软件策略和UPM策略
            saveThirdPartyDesktopPoolConfig(createDeskPoolDTO.getId(), request);

            desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(createDeskPoolDTO.getId());
            ThirdPartyDesktopRequest thirdPartyDesktopRequest = new ThirdPartyDesktopRequest();
            thirdPartyDesktopRequest.setGroupIdArr(groupIdArr);
            thirdPartyDesktopRequest.setComputerGroupArr(computerGroupArr);
            thirdPartyDesktopRequest.setAddGroupIdList(addGroupIdList);
            desktopPoolDTO.setHasSecondAdd(Boolean.FALSE);
            thirdPartyDesktopRequest.setPoolBasicDTO(desktopPoolDTO);
            thirdPartyDesktopRequest.setBuilder(builder);
            thirdPartyDesktopRequest.setDesktopPoolAddComputerWebRequest(desktopPoolAddComputerWebRequest);

            return addThirdPartyDesktop(thirdPartyDesktopRequest);
        } catch (BusinessException e) {
            return buildCreatePoolBusinessExceptionResponse(poolName, desktopPoolDTO, e);
        } catch (Exception e) {
            return buildCreatePoolExceptionResponse(poolName, desktopPoolDTO, e);
        }
    }

    private CommonWebResponse<Object> buildCreatePoolExceptionResponse(String poolName, DesktopPoolBasicDTO desktopPoolDTO, Exception e) {
        LOGGER.error("创建桌面池发生异常，异常原因：", poolName, e);
        if (Objects.nonNull(desktopPoolDTO)) {
            desktopPoolWebHelper.updatePoolStateFinish(desktopPoolDTO);
        }
        auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_FAIL_LOG, poolName, e.getMessage());
        return CommonWebResponse.fail(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_OPERATE_FAIL, new String[]{e.getMessage()});
    }

    private CommonWebResponse<Object> buildCreatePoolBusinessExceptionResponse(String poolName,
                                                                               DesktopPoolBasicDTO desktopPoolDTO, BusinessException e) {
        LOGGER.error("创建桌面池[{}]发生异常，异常原因：", poolName, e);
        if (Objects.nonNull(desktopPoolDTO)) {
            desktopPoolWebHelper.updatePoolStateFinish(desktopPoolDTO);
        }
        auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_FAIL_LOG, poolName, e.getI18nMessage());
        return CommonWebResponse.fail(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
    }

    private void saveThirdPartyDesktopPoolConfig(UUID poolId, CreateThirdPartyDesktopPoolWebRequest request) {
        DesktopPoolConfigDTO desktopPoolConfigDTO = new DesktopPoolConfigDTO();
        desktopPoolConfigDTO.setDesktopPoolId(poolId);
        desktopPoolConfigDTO.setSoftwareStrategyId(request.getSoftwareStrategyId());
        // 第三方单会话不支持UPM策略
        if (request.getSessionType() != CbbDesktopSessionType.SINGLE) {
            desktopPoolConfigDTO.setUserProfileStrategyId(request.getUserProfileStrategyId());
        }
        desktopPoolMgmtAPI.saveDesktopPoolConfig(desktopPoolConfigDTO);
    }

    private CbbCreateDeskPoolDTO buildCreateThirdPartyDeskPoolRequest(CreateThirdPartyDesktopPoolWebRequest request) {

        CbbCreateDeskPoolDTO cbbCreateDeskPoolDTO = new CbbCreateDeskPoolDTO();
        cbbCreateDeskPoolDTO.setId(UUID.randomUUID());
        BeanUtils.copyProperties(request, cbbCreateDeskPoolDTO);
        return cbbCreateDeskPoolDTO;
    }

    private CbbCreateDeskPoolDTO buildCreateDeskPoolRequest(CreateDesktopPoolWebRequest request) throws BusinessException {
        CbbCreateDeskPoolDTO cbbCreateDeskPoolDTO = new CbbCreateDeskPoolDTO();
        cbbCreateDeskPoolDTO.setId(UUID.randomUUID());
        BeanUtils.copyProperties(request, cbbCreateDeskPoolDTO);
        if (StringUtils.isBlank(cbbCreateDeskPoolDTO.getDesktopNamePrefix())) {
            cbbCreateDeskPoolDTO.setDesktopNamePrefix(request.getName());
        }
        if (Objects.equals(request.getPoolModel(), CbbDesktopPoolModel.STATIC)) {
            cbbCreateDeskPoolDTO.setPreStartDesktopNum(0);
        }
        if (Objects.nonNull(request.getDeskSpec())) {
            CbbCreateDeskSpecDTO createDeskSpecDTO = new CbbCreateDeskSpecDTO(deskSpecAPI.buildCbbDeskSpec(cbbCreateDeskPoolDTO.getClusterId(),
                    request.getDeskSpec()));
            cbbCreateDeskPoolDTO.setDeskSpec(createDeskSpecDTO);
        }
        return cbbCreateDeskPoolDTO;
    }

    /**
     * 保存配置信息
     *
     * @param poolId  池ID
     * @param request 配置信息
     */
    private void saveDesktopPoolConfig(UUID poolId, CreateDesktopPoolWebRequest request) {
        DesktopPoolConfigDTO desktopPoolConfigDTO = new DesktopPoolConfigDTO();
        desktopPoolConfigDTO.setDesktopPoolId(poolId);
        desktopPoolConfigDTO.setSoftwareStrategyId(request.getSoftwareStrategyId());
        desktopPoolConfigDTO.setUserProfileStrategyId(request.getUserProfileStrategyId());
        desktopPoolMgmtAPI.saveDesktopPoolConfig(desktopPoolConfigDTO);
    }

    private CommonWebResponse<?> startCreatePoolDesktop(Integer desktopNum, DesktopPoolBasicDTO desktopPoolDTO, BatchTaskBuilder builder)
            throws BusinessException {
        BatchTaskSubmitResult result = desktopPoolWebHelper.batchCreatePoolDesktop(desktopNum, desktopPoolDTO, builder);

        auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_SUCCESS_LOG, desktopPoolDTO.getName());
        return CommonWebResponse.success(result);
    }

    /**
     * 桌面池编辑规格
     *
     * @param request 桌面池编辑规格request
     * @param builder BatchTaskBuilder
     * @return 响应消息
     */
    @ApiOperation("桌面池编辑规格")
    @RequestMapping(value = "/spec/edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> editDeskSpec(DesktopPoolEditSpecWebRequest request, BatchTaskBuilder builder) {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");

        UUID poolId = request.getId();
        DesktopPoolBasicDTO desktopPoolDTO = null;
        String poolName = request.getId().toString();
        try {
            desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(poolId);
            poolName = desktopPoolDTO.getName();

            EditPoolDeskSpecRequest editPoolDeskSpecRequest = checkAndBuildUpdateSpecRequest(desktopPoolDTO, request);
            desktopPoolMgmtAPI.updateDeskSpec(editPoolDeskSpecRequest);

            List<PoolDesktopInfoDTO> deskInfoList = desktopPoolMgmtAPI.listNormalDeskInfoByDesktopPoolId(poolId);
            if (CollectionUtils.isEmpty(deskInfoList)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_EDIT_NO_DESK_UPDATE, desktopPoolDTO.getName());
                return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
            }

            CbbDeskSpecDTO cbbDeskSpec = desktopPoolMgmtAPI.getDesktopPoolCbbDeskSpec(poolId);
            // 更新桌面池状态为编辑中
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.UPDATING);
            List<UpdateDesktopSpecBatchTaskItem> taskItemList = buildUpdateSpecTask(deskInfoList, cbbDeskSpec);
            // 批处理和桌面变更规格共用一个handler
            UpdateDesktopSpecBatchTaskHandler handler = new UpdateDesktopSpecBatchTaskHandler(taskItemList);
            handler.setDesktopPoolId(poolId);
            BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_BATCH_TASK_DESC, desktopPoolDTO.getName())
                    .enableParallel()
                    .registerHandler(handler)
                    .start();
            return CommonWebResponse.success(result);
        } catch (BusinessException e) {
            LOGGER.error("桌面池[{}]编辑规格发生异常", poolName, e);
            return poolUpdateConfigFail(desktopPoolDTO, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_EDIT_FAIL, e.getI18nMessage(), poolName);
        } catch (Exception e) {
            LOGGER.error("桌面池[{}]编辑规格发生异常", poolName, e);
            return poolUpdateConfigFail(desktopPoolDTO, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_EDIT_FAIL, e.getMessage(), poolName);
        }
    }

    private EditPoolDeskSpecRequest checkAndBuildUpdateSpecRequest(DesktopPoolBasicDTO desktopPoolDTO, DesktopPoolEditSpecWebRequest request)
            throws BusinessException {
        EditPoolDeskSpecRequest specRequest = new EditPoolDeskSpecRequest();
        specRequest.setId(request.getId());
        // 检测显卡
        CbbDeskSpecDTO cbbDeskSpecDTO = deskSpecAPI.buildCbbDeskSpec(desktopPoolDTO.getClusterId(), request);
        specRequest.setDeskSpec(cbbDeskSpecDTO);
        return specRequest;
    }

    private List<UpdateDesktopSpecBatchTaskItem> buildUpdateSpecTask(List<PoolDesktopInfoDTO> deskInfoList, CbbDeskSpecDTO cbbDeskSpecDTO) {
        UpdateDesktopSpecBatchTaskItem batchTaskItem;
        List<UpdateDesktopSpecBatchTaskItem> taskList = new ArrayList<>();
        for (PoolDesktopInfoDTO desktopInfo : deskInfoList) {
            batchTaskItem = new UpdateDesktopSpecBatchTaskItem();
            batchTaskItem.setItemId(desktopInfo.getDeskId());
            batchTaskItem.setItemName(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_ITEM_NAME));
            batchTaskItem.setCpu(cbbDeskSpecDTO.getCpu());
            batchTaskItem.setMemory(cbbDeskSpecDTO.getMemory());
            batchTaskItem.setSystemSize(cbbDeskSpecDTO.getSystemSize());
            batchTaskItem.setPersonSize(cbbDeskSpecDTO.getPersonSize());
            batchTaskItem.setEnableHyperVisorImprove(cbbDeskSpecDTO.getEnableHyperVisorImprove());
            batchTaskItem.setEnableChangeVgpu(true);
            VgpuInfoDTO vgpuInfoDTO = cbbDeskSpecDTO.getVgpuInfoDTO();
            if (Objects.isNull(vgpuInfoDTO)) {
                vgpuInfoDTO = new VgpuInfoDTO();
            }
            batchTaskItem.setVgpuType(vgpuInfoDTO.getVgpuType());
            if (vgpuInfoDTO.getVgpuExtraInfo() instanceof VgpuExtraInfo) {
                batchTaskItem.setVgpuExtraInfo((VgpuExtraInfo) vgpuInfoDTO.getVgpuExtraInfo());
            } else {
                batchTaskItem.setVgpuExtraInfo(null);
            }

            // 额外盘不变化
            batchTaskItem.setEnableChangeExtraDisk(false);
            taskList.add(batchTaskItem);
        }
        return taskList;
    }

    /**
     * 桌面池添加云桌面
     *
     * @param request 添加云桌面request
     * @param builder BatchTaskBuilder
     * @return 响应消息
     */
    @ApiOperation("桌面池添加云桌面")
    @RequestMapping(value = "/addDesktop", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> addDesktop(DesktopPoolAddDesktopWebRequest request, BatchTaskBuilder builder) {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");

        DesktopPoolBasicDTO desktopPoolDTO = null;
        String poolName = request.getId().toString();
        try {
            desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(request.getId());
            poolName = desktopPoolDTO.getName();
            int addNum = request.getAddNum();
            checkBeforeAddDesktop(desktopPoolDTO, addNum);

            // 更新桌面池状态为编辑中
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.UPDATING);

            if (addNum <= 0) {
                LOGGER.info("桌面池添加云桌面成功addNum:{}", addNum);
                return CommonWebResponse.success();
            }

            // 创建异步执行任务--创建桌面
            BatchTaskSubmitResult result = desktopPoolWebHelper.batchCreatePoolDesktop(addNum, desktopPoolDTO, builder);
            LOGGER.info("桌面池添加云桌面成功");
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_DESKTOP_SUCCESS_LOG, poolName);
            return CommonWebResponse.success(result);
        } catch (BusinessException e) {
            return getAddDesktopBusinessExceptionWebResponse(desktopPoolDTO, poolName, e);
        } catch (Exception e) {
            return getAddDesktopExceptionWebResponse(desktopPoolDTO, poolName, e);
        }
    }


    /**
     * 桌面池添加PC终端
     *
     * @param request        添加云桌面request
     * @param builder        BatchTaskBuilder
     * @param sessionContext sessionContext
     * @return 响应消息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("桌面池添加PC终端")
    @RequestMapping(value = "/addThirdPartyDesktop", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"桌面池添加PC终端"})})
    @EnableAuthority
    public CommonWebResponse<?> addThirdPartyDesktop(DesktopPoolAddComputerWebRequest request, BatchTaskBuilder builder,
                                                     SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");

        DesktopPoolBasicDTO desktopPoolDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(request.getId());
        checkDesktopPoolState(desktopPoolDTO);
        // 检查并获取新增终端id集合
        Set<String> computerGroupArr = desktopPoolThirdPartyMgmtAPI.getDesktopPoolRelationComputerGroup(request.getId());
        // 检查并获取新增终端id集合
        List<UUID> addGroupIdList = checkBeforeAddComputerDesktop(request, computerGroupArr);
        // 获取管理员的数据权限，超管就是null
        UUID[] groupIdArr = null;
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            groupIdArr = permissionHelper.getTerminalGroupIdArr(sessionContext.getUserId());
            desktopPoolWebHelper.checkTerminalGroupPermission(request, groupIdArr);
        }
        ThirdPartyDesktopRequest thirdPartyDesktopRequest = new ThirdPartyDesktopRequest();
        thirdPartyDesktopRequest.setGroupIdArr(groupIdArr);
        thirdPartyDesktopRequest.setComputerGroupArr(computerGroupArr);
        thirdPartyDesktopRequest.setAddGroupIdList(addGroupIdList);
        desktopPoolDTO.setHasSecondAdd(Boolean.TRUE);
        thirdPartyDesktopRequest.setPoolBasicDTO(desktopPoolDTO);
        thirdPartyDesktopRequest.setBuilder(builder);
        thirdPartyDesktopRequest.setDesktopPoolAddComputerWebRequest(request);
        return addThirdPartyDesktop(thirdPartyDesktopRequest);
    }

    private CommonWebResponse<?> addThirdPartyDesktop(ThirdPartyDesktopRequest thirdPartyDesktopRequest) throws BusinessException {
        DesktopPoolBasicDTO desktopPoolDTO = thirdPartyDesktopRequest.getPoolBasicDTO();
        String poolName = desktopPoolDTO.getName();
        try {
            // 更新桌面池状态为编辑中
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.UPDATING);
            int addNum = 0;
            List<ComputerDTO> computerDTOList = new ArrayList<>();
            List<UUID> addGroupIdUuidList = new ArrayList<>();
            if (!thirdPartyDesktopRequest.getAddGroupIdList().isEmpty()) {
                // 构建终端组下的PC终端
                addGroupIdUuidList = new ArrayList<>(thirdPartyDesktopRequest.getAddGroupIdList());
                computerDTOList = computerBusinessAPI.getComputerInfoByGroupIdList(addGroupIdUuidList);
                //筛选第三方
                computerDTOList = computerDTOList.stream().filter(s -> s.getType() ==
                        ComputerTypeEnum.THIRD && StringUtils.isNotBlank(s.getOs()) && StringUtils.isNotBlank(s.getName()) && s.getWorkModel() == null
                        && StringUtils.isNotBlank(s.getIp()) && CbbHostOsType.getOsType(s.getOs()) != null).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(computerDTOList)) {
                    computerDTOList = filterExistComputer(computerDTOList);
                }
            }

            // 如果不是超管需要把非权限内的终端组加回来
            checkGroupAuthAndAddDefaultGroup(desktopPoolDTO.getId(), thirdPartyDesktopRequest.getDesktopPoolAddComputerWebRequest()
                    , thirdPartyDesktopRequest.getGroupIdArr());
            DesktopPoolAddComputerWebRequest request = thirdPartyDesktopRequest.getDesktopPoolAddComputerWebRequest();
            buildWebAddComputerDTO(computerDTOList, request);
            computerDTOList = computerDTOList.stream().filter(item -> StringUtils.isNotBlank(item.getName()) && StringUtils.isNotBlank(item.getOs())
                    && StringUtils.isNotBlank(item.getIp())).collect(Collectors.toList());
            if (desktopPoolDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                // 排除多会话系统不支持数据
                computerDTOList = computerDTOList.stream().filter(s -> CbbHostOsType.isMultiSession(
                        CbbHostOsType.getOsType(s.getOs()))).collect(Collectors.toList());
            }
            //去重
            computerDTOList = new ArrayList<>(computerDTOList.stream().collect(Collectors.toMap(ComputerDTO::getId, e -> e, (e1, e2) -> e1))
                    .values());

            updateThirdPartyPoolBindObject(thirdPartyDesktopRequest, thirdPartyDesktopRequest.getComputerGroupArr(), addGroupIdUuidList);
            addNum = computerDTOList.size();
            if (addNum == 0) {
                LOGGER.info("桌面池添加ThirdParty云桌面成功addNum:{}", addNum);
                getBatchTaskBuilder(thirdPartyDesktopRequest, desktopPoolDTO, poolName);
                return CommonWebResponse.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, desktopPoolDTO.getId());
            }

            // 创建异步执行任务--创建桌面
            BatchTaskSubmitResult result = desktopPoolWebHelper.batchCreatePoolThirdPartyDesktop(computerDTOList, desktopPoolDTO,
                    thirdPartyDesktopRequest.getBuilder());
            LOGGER.info("桌面池添加云桌面成功");
            return CommonWebResponse.success(result);
        } catch (BusinessException e) {
            return getAddDesktopBusinessExceptionWebResponse(desktopPoolDTO, poolName, e);
        } catch (Exception e) {
            return getAddDesktopExceptionWebResponse(desktopPoolDTO, poolName, e);
        }
    }

    private List<ComputerDTO> filterExistComputer(List<ComputerDTO> computerDTOList) {
        UUID[] computerIdArr = computerDTOList.stream().map(ComputerDTO::getId).toArray(UUID[]::new);
        List<DesktopPoolComputerDTO> computerList = desktopPoolThirdPartyMgmtAPI.getDesktopPoolRelationComputerList(computerIdArr);
        List<UUID> existComputerIdList = computerList.stream().map(DesktopPoolComputerDTO::getRelatedId).collect(Collectors.toList());
        // 排除已被添加过PC终端
        computerDTOList = computerDTOList.stream().filter(s -> !existComputerIdList.contains(s.getId()))
                .collect(Collectors.toList());
        return computerDTOList;
    }

    private void buildWebAddComputerDTO(List<ComputerDTO> computerDTOList, DesktopPoolAddComputerWebRequest request) {
        if (request.getComputerIdArr() != null && request.getComputerIdArr().length > 0) {
            List<ComputerDTO> computerInfoByIdList = computerBusinessAPI.getComputerInfoByIdList(Arrays.asList(request.getComputerIdArr()));
            if (CollectionUtils.isNotEmpty(computerInfoByIdList)) {
                computerInfoByIdList = computerInfoByIdList.stream().filter(s -> s.getType() ==
                        ComputerTypeEnum.THIRD && StringUtils.isNotBlank(s.getOs()) && StringUtils.isNotBlank(s.getName()) && s.getWorkModel() == null
                        && StringUtils.isNotBlank(s.getIp()) && CbbHostOsType.getOsType(s.getOs()) != null).collect(Collectors.toList());
                computerDTOList.addAll(computerInfoByIdList);
            }
        }
    }

    private BatchTaskBuilder getBatchTaskBuilder(ThirdPartyDesktopRequest thirdPartyDesktopRequest,
                                                 DesktopPoolBasicDTO desktopPoolDTO, String poolName) {
        desktopPoolWebHelper.updatePoolStateFinish(desktopPoolDTO);
        auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_COMPUTER_ZERO_SUCCESS_LOG, poolName);
        BatchTaskBuilder builder = thirdPartyDesktopRequest.getBuilder();
        builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_DESKTOP_TASK_NAME);
        builder.setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_COMPUTER_ZERO_SUCCESS_LOG, desktopPoolDTO.getName());
        return builder;
    }

    private void checkGroupAuthAndAddDefaultGroup(UUID desktopPoolId, DesktopPoolAddComputerWebRequest request, UUID[] groupIdArr) {
        if (ArrayUtils.isEmpty(groupIdArr)) {
            return;
        }
        List<DesktopPoolComputerDTO> userGroupList = desktopPoolThirdPartyMgmtAPI.listDesktopPoolUser(
                desktopPoolId, ComputerRelatedType.COMPUTER_GROUP);
        if (CollectionUtils.isEmpty(userGroupList)) {
            return;
        }
        List<UUID> poolBindGroupIdList = userGroupList.stream().map(DesktopPoolComputerDTO::getRelatedId).collect(Collectors.toList());
        List<UUID> selectedGroupIdList = new ArrayList<>();
        if (request.getTerminalGroupIdArr() != null) {
            selectedGroupIdList = Arrays.asList(request.getTerminalGroupIdArr());
        }
        Set<UUID> authGroupIdSet = Sets.newHashSet(groupIdArr);
        // 把非本管理员的组ID，都加进去
        selectedGroupIdList.addAll(poolBindGroupIdList.stream().filter(id -> !authGroupIdSet.contains(id)).collect(Collectors.toList()));
        selectedGroupIdList = selectedGroupIdList.stream().distinct().collect(Collectors.toList());
        request.setTerminalGroupIdArr(selectedGroupIdList.toArray(new UUID[0]));
    }

    /**
     * 更新桌面与终端关系
     *
     * @param request          web请求参数
     * @param computerGroupArr 桌面池已关联终端组
     * @param addGroupIdList   新增终端组
     * @throws BusinessException
     */
    private void updateThirdPartyPoolBindObject(ThirdPartyDesktopRequest request, Set<String> computerGroupArr,
                                                List<UUID> addGroupIdList) throws BusinessException {
        Set<String> removeGroupIdArr;
        DesktopPoolAddComputerWebRequest webRequest = request.getDesktopPoolAddComputerWebRequest();
        if (webRequest.getTerminalGroupIdArr() != null && webRequest.getTerminalGroupIdArr().length > 0) {
            List<String> selGroupIdList = Arrays.stream(webRequest.getTerminalGroupIdArr()).map(UUID::toString).collect(Collectors.toList());
            // 算出被移除的终端组Id
            removeGroupIdArr = computerGroupArr.stream().filter(s -> !selGroupIdList.contains(s)).collect(Collectors.toSet());
        } else {
            removeGroupIdArr = computerGroupArr;
        }

        UpdatePoolThirdPartyBindObjectDTO updatePoolThirdPartyBindObjectDTO = new UpdatePoolThirdPartyBindObjectDTO();
        updatePoolThirdPartyBindObjectDTO.setPoolId(webRequest.getId());
        if (CollectionUtils.isNotEmpty(request.getComputerIdList())) {
            updatePoolThirdPartyBindObjectDTO.setAddComputerByIdList(request.getComputerIdList());
        }

        if (webRequest.getTerminalGroupIdArr() != null) {
            List<UUID> terminalGroupIdList = Arrays.stream(webRequest.getTerminalGroupIdArr()).collect(Collectors.toList());
            updatePoolThirdPartyBindObjectDTO.setSelectedGroupIdList(terminalGroupIdList);
        }

        updatePoolThirdPartyBindObjectDTO.setAddGroupIdList(addGroupIdList);

        List<String> removeGroupIdList = new ArrayList<>(new ArrayList<>(removeGroupIdArr));
        updatePoolThirdPartyBindObjectDTO.setRemoveGroupIdList(removeGroupIdList.stream().map(UUID::fromString).collect(Collectors.toList()));

        desktopPoolThirdPartyMgmtAPI.updatePoolBindObject(updatePoolThirdPartyBindObjectDTO);
    }

    private CommonWebResponse<Object> getAddDesktopExceptionWebResponse(DesktopPoolBasicDTO desktopPoolDTO, String poolName, Exception e) {
        if (Objects.nonNull(desktopPoolDTO)) {
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.AVAILABLE);
        }
        String errorMsg = e.getMessage();
        LOGGER.error("桌面池[{}]添加云桌面发生异常", poolName, e);
        auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_DESKTOP_FAIL_LOG, poolName, errorMsg);
        return CommonWebResponse.fail(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_DESKTOP_OPERATE_FAIL, new String[]{poolName, errorMsg});
    }

    private CommonWebResponse<Object> getAddDesktopBusinessExceptionWebResponse(DesktopPoolBasicDTO desktopPoolDTO,
                                                                                String poolName, BusinessException e) {
        if (Objects.nonNull(desktopPoolDTO)) {
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.AVAILABLE);
        }
        String errorMsg = e.getI18nMessage();
        LOGGER.error("桌面池[{}]添加云桌面发生异常", poolName, e);
        auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_DESKTOP_FAIL_LOG, poolName, errorMsg);
        // 添加桌面池桌面时，数量超过1000，提示最大可剩余添加桌面数量
        if (DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_NUM_OVER_MAX.equals(e.getKey())) {
            return CommonWebResponse.fail(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_NUM_OVER_MAX, e.getArgArr());
        }
        return CommonWebResponse.fail(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_DESKTOP_OPERATE_FAIL, new String[]{poolName, errorMsg});
    }

    private List<UUID> checkBeforeAddComputerDesktop(DesktopPoolAddComputerWebRequest request, Set<String> desktopPoolRelationComputerGroup)
            throws BusinessException {
        // 检验终端组存在情况
        Set<UUID> addGroupIdArr = new HashSet<>();
        if (request.getTerminalGroupIdArr() != null && request.getTerminalGroupIdArr().length > 0) {
            addGroupIdArr = Arrays.stream(request.getTerminalGroupIdArr()).filter(s ->
                    !desktopPoolRelationComputerGroup.contains(s.toString())).collect(Collectors.toSet());
        }
        if (request.getComputerIdArr() != null && request.getComputerIdArr().length > 0) {
            List<DesktopPoolComputerDTO> computerList = desktopPoolThirdPartyMgmtAPI.getDesktopPoolRelationComputerList(request.getComputerIdArr());
            if (!computerList.isEmpty()) {
                List<UUID> computerIdList = Arrays.asList(request.getComputerIdArr());
                List<DesktopPoolComputerDTO> existList = computerList.stream().filter(s -> computerIdList.contains(s.getRelatedId())
                        && s.getDesktopPoolId() != request.getId()).collect(Collectors.toList());
                List<UUID> terminalIdList = existList.stream().map(DesktopPoolComputerDTO::getRelatedId).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(terminalIdList)) {
                    return new ArrayList<>();
                }
                List<ComputerDTO> computerInfoIdList = computerBusinessAPI.getComputerInfoByIdList(terminalIdList);
                String names = computerInfoIdList.stream().map(ComputerDTO::getName).collect(Collectors.joining(","));
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_ADD_DESKTOP_COMPUTER_EXIST, names);
            }
        }
        if (!addGroupIdArr.isEmpty()) {
            List<DesktopPoolComputerDTO> computerGroupList = desktopPoolThirdPartyMgmtAPI.
                    getDesktopPoolRelationComputerGroupList(new ArrayList<>(addGroupIdArr));
            if (!computerGroupList.isEmpty()) {
                Set<UUID> finalAddGroupIdArr = addGroupIdArr;
                List<DesktopPoolComputerDTO> existList = computerGroupList.stream().filter(s -> finalAddGroupIdArr.contains(s.getRelatedId())
                        && s.getDesktopPoolId() != request.getId()).collect(Collectors.toList());
                List<UUID> terminalGroupIdList = existList.stream().map(DesktopPoolComputerDTO::getRelatedId).collect(Collectors.toList());
                List<CbbTerminalGroupDetailDTO> groupDetailDTOList = cbbTerminalGroupMgmtAPI.findByIdList(terminalGroupIdList);
                String groupNames = groupDetailDTOList.stream().map(CbbTerminalGroupDetailDTO::getGroupName).collect(Collectors.joining(","));
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_ADD_DESKTOP_TERMINAL_GROUP_EXIST, groupNames);
            }
        }
        return new ArrayList<>(addGroupIdArr);
    }

    private void checkBeforeAddDesktop(DesktopPoolBasicDTO desktopPoolDTO, int addNum) throws BusinessException {
        checkDesktopPoolState(desktopPoolDTO);
        if (desktopPoolDTO.getDesktopNum() >= DesktopPoolConstants.SINGLE_POOL_MAX_DESK_NUM
                || addNum > DesktopPoolConstants.SINGLE_POOL_MAX_DESK_NUM - desktopPoolDTO.getDesktopNum()) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_NUM_OVER_MAX,
                    String.valueOf(DesktopPoolConstants.SINGLE_POOL_MAX_DESK_NUM),
                    String.valueOf(DesktopPoolConstants.SINGLE_POOL_MAX_DESK_NUM - desktopPoolDTO.getDesktopNum()));
        }
    }

    private Set<UUID> convertStringSetToUUIDSet(Set<String> stringSet) {
        Set<UUID> uuidSet = new HashSet<>();

        for (String string : stringSet) {
            // 将每个 String 转换为 UUID
            UUID uuid = UUID.fromString(string);
            uuidSet.add(uuid);
        }

        return uuidSet;
    }

    /**
     * 编辑桌面池基本信息
     *
     * @param request 编辑桌面池request
     * @return 响应消息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑桌面池基本信息")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> edit(UpdateDesktopPoolWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        String poolName = request.getId().toString();
        try {

            // 非Windows Server镜像模板，不支持多会话类型
            if (request.getVdiDesktopConfig() != null &&
                    request.getVdiDesktopConfig().getImageEdition() != null &&
                    request.getVdiDesktopConfig().getImageEdition().getId() != null) {
                UUID id = request.getVdiDesktopConfig().getImageEdition().getId();
                CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(id);
                if (cbbImageTemplateDTO != null && !CbbOsType.isWinServerOs(cbbImageTemplateDTO.getOsType())
                        && request.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_IMAGE_TEMPALTE_FAIL);
                }
            }

            DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(request.getId());
            poolName = desktopPoolBasicDTO.getName();

            // 修改池
            updateDesktopPool(request);

            // 修改桌面池报表相关表中桌面池名称
            updateDashboardDesktopPoolName(poolName, request);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("编辑桌面池[{}]基本信息成功, 参数request={}", request.getId(), JSON.toJSONString(request));
            }
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_SUCCESS_LOG, poolName);
            return CommonWebResponse.success(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑桌面池[{}]基本信息发生异常", poolName, e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_FAIL_LOG, poolName, e.getI18nMessage());
            return CommonWebResponse.fail(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }


    /**
     * 编辑第三方桌面池基本信息
     *
     * @param request 编辑桌面池request
     * @return 响应消息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑第三方桌面池基本信息")
    @RequestMapping(value = "/editThirdParty", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> editThirdParty(UpdateThirdPartyDesktopPoolWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        String poolName = request.getId().toString();
        try {
            DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(request.getId());
            poolName = desktopPoolBasicDTO.getName();

            // 修改池
            updateThirdPartyDesktopPool(request);
            UpdateDesktopPoolWebRequest updateDesktopPoolWebRequest = new UpdateDesktopPoolWebRequest();
            updateDesktopPoolWebRequest.setId(request.getId());
            updateDesktopPoolWebRequest.setName(request.getName());
            // 修改桌面池报表相关表中桌面池名称
            updateDashboardDesktopPoolName(poolName, updateDesktopPoolWebRequest);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("编辑桌面池[{}]基本信息成功, 参数request={}", request.getId(), JSON.toJSONString(request));
            }
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_SUCCESS_LOG, poolName);
            return CommonWebResponse.success(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑桌面池[{}]基本信息发生异常", poolName, e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_FAIL_LOG, poolName, e.getI18nMessage());
            return CommonWebResponse.fail(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }

    /**
     * 编辑负载均衡
     *
     * @param request 编辑桌面池request
     * @return 响应消息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑负载均衡")
    @RequestMapping(value = "/loadBalance/edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> editLoadBalance(UpdateLoadBalanceWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        String poolName = request.getId().toString();
        try {
            DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolMgmtAPI.getDesktopPoolDetail(request.getId());
            poolName = desktopPoolBasicDTO.getName();


            desktopPoolBasicDTO.setLoadBalanceStrategy(request.getLoadBalanceStrategy());
            desktopPoolBasicDTO.setCpuUsage(request.getCpuUsage());
            desktopPoolBasicDTO.setSystemDiskUsage(request.getSystemDiskUsage());
            desktopPoolBasicDTO.setMemoryUsage(request.getMemoryUsage());

            // 修改池
            updateDesktopPoolLoadBalance(request, desktopPoolBasicDTO);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("编辑桌面池负载均衡[{}]信息成功, 参数request={}", request.getId(), JSON.toJSONString(request));
            }
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_LOAD_BALANCE_SUCCESS_LOG, poolName);
            return CommonWebResponse.success(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_LOAD_BALANCE_SUCCESS_LOG,
                    new String[]{poolName});
        } catch (BusinessException e) {
            LOGGER.error("编辑桌面池负载均衡[{}]信息发生异常", poolName, e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_LOAD_BALANCE_FAIL_LOG, poolName, e.getI18nMessage());
            return CommonWebResponse.fail(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EDIT_LOAD_BALANCE_FAIL_LOG,
                    new String[]{poolName, e.getI18nMessage()});
        }
    }

    private void updateDesktopPoolLoadBalance(UpdateLoadBalanceWebRequest request, DesktopPoolBasicDTO desktopPoolBasicDTO) throws BusinessException {
        UpdateLoadBalanceRequest updateLoadBalanceRequest = new UpdateLoadBalanceRequest();
        boolean hasMaxSessionChange = hasMaxSessionChange(request.getMaxSession(), desktopPoolBasicDTO.getMaxSession());
        updateLoadBalanceRequest.setHasMaxSessionChange(hasMaxSessionChange);
        BeanUtils.copyProperties(request, updateLoadBalanceRequest);
        CbbDesktopPoolDTO desktopPoolDTO = new CbbDesktopPoolDTO();
        BeanUtils.copyProperties(desktopPoolBasicDTO, desktopPoolDTO);
        desktopPoolMgmtAPI.updateLoadBalance(updateLoadBalanceRequest, desktopPoolDTO);

    }

    private void updateDesktopPool(UpdateDesktopPoolWebRequest request) throws BusinessException {
        UpdateDesktopPoolRequest updateDesktopPoolRequest = new UpdateDesktopPoolRequest();
        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(request.getId());


        boolean hasMaxSessionChange = hasMaxSessionChange(request.getMaxSession(), desktopPoolDTO.getMaxSession());
        boolean hasIdleDesktopRecoverChange = hasIdleDesktopRecoverChange(request.getIdleDesktopRecover(), desktopPoolDTO);

        // 更新基本桌面池基本信息
        BeanUtils.copyProperties(request, desktopPoolDTO);

        // 防止前端未传负载均衡配置参数,读取数据库配置重新赋值(数据库参数也为null的话，赋默认值90)
        CbbDesktopPoolDTO detail = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(request.getId());
        if (request.getCpuUsage() == null) {
            desktopPoolDTO.setCpuUsage(Optional.ofNullable(detail.getCpuUsage()).orElse(90));
        }
        if (request.getMemoryUsage() == null) {
            desktopPoolDTO.setMemoryUsage(Optional.ofNullable(detail.getMemoryUsage()).orElse(90));
        }
        if (request.getSystemDiskUsage() == null) {
            desktopPoolDTO.setSystemDiskUsage(Optional.ofNullable(detail.getSystemDiskUsage()).orElse(90));
        }
        if (request.getLoadBalanceStrategy() == null) {
            desktopPoolDTO.setLoadBalanceStrategy(Optional.ofNullable(detail.getLoadBalanceStrategy())
                    .orElse(CbbLoadBalanceStrategyEnum.CPU_PRIORITY));
        }
        if (request.getMaxSession() == null) {
            desktopPoolDTO.setMaxSession(Optional.ofNullable(detail.getMaxSession()).orElse(0));
        }

        VdiDesktopConfigVO vdiDesktopConfig = request.getVdiDesktopConfig();
        DesktopPoolConfigDTO desktopPoolConfigDTO = null;
        DeskSpecRequest deskSpecRequest = null;
        // 桌面池里有桌面时禁止修改下面的配置，已有前置检查
        if (Objects.nonNull(vdiDesktopConfig)) {
            updateDesktopPoolRequest.setNeedUpdateConfig(true);
            deskSpecRequest = vdiDesktopConfig.toDeskSpec();
            UUID imageTemplateId = vdiDesktopConfig.getImage().getId();
            desktopPoolDTO.setImageTemplateId(imageTemplateId);
            desktopPoolDTO.setStrategyId(vdiDesktopConfig.getStrategy().getId());
            desktopPoolDTO.setNetworkId(vdiDesktopConfig.getNetwork().getAddress().getId());
            desktopPoolDTO.setClusterId(vdiDesktopConfig.getCluster().getId());
            desktopPoolDTO.setStoragePoolId(vdiDesktopConfig.getSystemDiskStoragePool().getId());
            desktopPoolDTO.setPlatformId(vdiDesktopConfig.getCloudPlatform().getId());
            desktopPoolDTO.setSessionType(request.getSessionType());

            desktopPoolConfigDTO = new DesktopPoolConfigDTO();
            desktopPoolConfigDTO.setDesktopPoolId(request.getId());
            // 修改UPM策略
            IdLabelEntry userProfileStrategy = vdiDesktopConfig.getUserProfileStrategy();
            if (Objects.nonNull(userProfileStrategy)) {
                desktopPoolConfigDTO.setUserProfileStrategyId(userProfileStrategy.getId());
            }
            // 修改软件管控策略
            IdLabelEntry softwareStrategy = vdiDesktopConfig.getSoftwareStrategy();
            desktopPoolConfigDTO.setSoftwareStrategyId(Objects.isNull(softwareStrategy) ? null : softwareStrategy.getId());
            updateDesktopPoolRequest.setCbbDesktopPoolDTO(desktopPoolDTO);
            updateDesktopPoolRequest.setDesktopPoolConfigDTO(desktopPoolConfigDTO);
            updateDesktopPoolRequest.setCbbDeskSpecDTO(deskSpecAPI.buildCbbDeskSpec(desktopPoolDTO.getClusterId(), deskSpecRequest));
            if (updateDesktopPoolRequest.getCbbDesktopPoolDTO() != null) {
                CbbDesktopPoolDTO cbbDesktopPoolDTO = updateDesktopPoolRequest.getCbbDesktopPoolDTO();
                cbbDesktopPoolDTO.setHasIdleDesktopRecoverChange(hasIdleDesktopRecoverChange);
                cbbDesktopPoolDTO.setHasIdleDesktopRecoverChange(hasIdleDesktopRecoverChange);
            }
            desktopPoolMgmtAPI.updateDesktopPoolWithConfig(updateDesktopPoolRequest);
        } else {
            desktopPoolDTO.setHasMaxSessionChange(hasMaxSessionChange);
            desktopPoolDTO.setHasIdleDesktopRecoverChange(hasIdleDesktopRecoverChange);
            desktopPoolMgmtAPI.updateDesktopPoolWithoutConfig(desktopPoolDTO);
        }
    }

    private void updateThirdPartyDesktopPool(UpdateThirdPartyDesktopPoolWebRequest request) throws BusinessException {
        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(request.getId());

        boolean hasMaxSessionChange = hasMaxSessionChange(request.getMaxSession(), desktopPoolDTO.getMaxSession());
        boolean hasIdleDesktopRecoverChange = hasIdleDesktopRecoverChange(request.getIdleDesktopRecover(), desktopPoolDTO);

        // 更新基本桌面池基本信息
        BeanUtils.copyProperties(request, desktopPoolDTO);

        // 防止前端未传负载均衡配置参数,读取数据库配置重新赋值(数据库参数也为null的话，赋默认值90)
        CbbDesktopPoolDTO detail = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(request.getId());
        if (request.getCpuUsage() == null) {
            desktopPoolDTO.setCpuUsage(Optional.ofNullable(detail.getCpuUsage()).orElse(90));
        }
        if (request.getMemoryUsage() == null) {
            desktopPoolDTO.setMemoryUsage(Optional.ofNullable(detail.getMemoryUsage()).orElse(90));
        }
        if (request.getSystemDiskUsage() == null) {
            desktopPoolDTO.setSystemDiskUsage(Optional.ofNullable(detail.getSystemDiskUsage()).orElse(90));
        }
        if (request.getLoadBalanceStrategy() == null) {
            desktopPoolDTO.setLoadBalanceStrategy(Optional.ofNullable(detail.getLoadBalanceStrategy()).orElse(CbbLoadBalanceStrategyEnum.CPU_PRIORITY));
        }
        if (request.getMaxSession() == null) {
            desktopPoolDTO.setMaxSession(Optional.ofNullable(detail.getMaxSession()).orElse(0));
        }

        desktopPoolDTO.setHasMaxSessionChange(hasMaxSessionChange);
        desktopPoolDTO.setHasIdleDesktopRecoverChange(hasIdleDesktopRecoverChange);
        cbbDesktopPoolMgmtAPI.updateDesktopPool(desktopPoolDTO);

        // 修改UPM和软件管控
        if (Objects.nonNull(request.getSoftwareStrategyId()) || Objects.nonNull(request.getUserProfileStrategyId())) {
            DesktopPoolConfigDTO desktopPoolConfigDTO = new DesktopPoolConfigDTO();
            desktopPoolConfigDTO.setDesktopPoolId(request.getId());
            desktopPoolConfigDTO.setSoftwareStrategyId(request.getSoftwareStrategyId());
            desktopPoolConfigDTO.setUserProfileStrategyId(request.getUserProfileStrategyId());
            desktopPoolMgmtAPI.saveDesktopPoolConfig(desktopPoolConfigDTO);
        }
    }

    private boolean hasIdleDesktopRecoverChange(Integer idleDesktopRecover, CbbDesktopPoolDTO desktopPoolDTO) {
        if (idleDesktopRecover != null && !idleDesktopRecover.equals(desktopPoolDTO.getIdleDesktopRecover())) {
            return true;
        }
        return false;
    }

    private boolean hasMaxSessionChange(Integer maxSession, Integer oldMaxSession) {
        if (maxSession != null && !maxSession.equals(oldMaxSession)) {
            return true;
        }
        return false;
    }

    /**
     * 修改桌面池报表中相关表里面桌面池的名称
     *
     * @param oldDesktopPoolName 旧的桌面池名称
     * @param request            前端发送的请求
     */
    private void updateDashboardDesktopPoolName(String oldDesktopPoolName, UpdateDesktopPoolWebRequest request) {
        String newDesktopPoolName = request.getName();
        if (!Objects.equals(oldDesktopPoolName, newDesktopPoolName)) {
            LOGGER.info("桌面池[{}]修改桌面池名称为[{}]，需要修改桌面池报表记录中的桌面池名称", oldDesktopPoolName, newDesktopPoolName);
            desktopPoolDashboardAPI.updateDesktopPoolName(request.getId(), newDesktopPoolName);
        }
    }

    private void checkDesktopPoolState(DesktopPoolBasicDTO desktopPoolDTO) throws BusinessException {
        if (desktopPoolDTO.getPoolState() != CbbDesktopPoolState.AVAILABLE) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_STATE_NOT_AVAILABLE, desktopPoolDTO.getName());
        }
    }

    /**
     * 校验桌面池名称重复
     *
     * @param request 请求参数
     * @return 校验结果返回
     * @throws BusinessException 异常信息
     */
    @ApiOperation("校验桌面池重名")
    @RequestMapping(value = "/checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckDuplicationResponse> checkPoolNameDuplication(CheckDesktopPoolNameWebRequest request)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");

        CbbCheckDeskPoolNameDTO cbbCheckDeskPoolNameDTO = new CbbCheckDeskPoolNameDTO();
        BeanUtils.copyProperties(request, cbbCheckDeskPoolNameDTO);
        Boolean hasDuplicate = cbbDesktopPoolMgmtAPI.checkDeskPoolNameDuplicate(cbbCheckDeskPoolNameDTO);
        CheckDuplicationResponse response = new CheckDuplicationResponse(hasDuplicate);

        return CommonWebResponse.success(response);
    }

    /**
     * 删除桌面池请求
     *
     * @param request request
     * @param builder builder
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("删除桌面池")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> deleteDesktopPool(DeleteDesktopPoolRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notEmpty(idArr, "idArr can not be empty");

        Boolean shouldOnlyDeleteDataFromDb = request.getShouldOnlyDeleteDataFromDb();
        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);

        String taskName = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DELETE_DESKTOP_POOL, prefix);
        List<DefaultBatchTaskItem> taskItemList = Arrays.stream(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).collect(Collectors.toList());

        DeleteDesktopPoolBatchHandler handler = new DeleteDesktopPoolBatchHandler(taskItemList, request.getShouldOnlyDeleteDataFromDb());
        BatchTaskSubmitResult result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DELETE_DESKTOP_POOL, prefix)
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DESKTOP_POOL_BATCH_DELETE_TASK_DESC, prefix)
                .setUniqueId(request.getIdArr()[0]).enableParallel().registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * 开启维护模式
     *
     * @param request request
     * @param builder builder
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("开启维护模式")
    @RequestMapping(value = "openMaintenance", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> openMaintenance(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notNull(idArr, "idArr can not be null");

        String taskName = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_OPEN_MAINTENANCE);
        List<DefaultBatchTaskItem> taskList = Arrays.stream(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).collect(Collectors.toList());

        ChangeDesktopPoolMaintenanceBatchHandler handler = new ChangeDesktopPoolMaintenanceBatchHandler(true, taskList);
        BatchTaskSubmitResult result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_OPEN_MAINTENANCE)
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_OPEN_MAINTENANCE_TASK_DESC)
                .setUniqueId(request.getIdArr()[0]).enableParallel().registerHandler(handler).start();

        return CommonWebResponse.success(result);
    }

    /**
     * 关闭维护模式
     *
     * @param request request
     * @param builder builder
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("关闭维护模式")
    @RequestMapping(value = "closeMaintenance", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> closeMaintenance(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notNull(idArr, "idArr can not be null");

        String taskName = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_CLOSE_MAINTENANCE);
        List<DefaultBatchTaskItem> taskList = Arrays.stream(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).collect(Collectors.toList());

        ChangeDesktopPoolMaintenanceBatchHandler handler = new ChangeDesktopPoolMaintenanceBatchHandler(false, taskList);
        BatchTaskSubmitResult result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_CLOSE_MAINTENANCE)
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_CLOSE_MAINTENANCE_TASK_DESC)
                .setUniqueId(request.getIdArr()[0]).enableParallel().registerHandler(handler).start();

        return CommonWebResponse.success(result);
    }

    /**
     * 桌面池开机
     *
     * @param request request
     * @param builder builder
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("桌面池开机")
    @RequestMapping(value = "desktopPoolStart", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> desktopPoolStart(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notNull(idArr, "idArr can not be null");
        List<UUID> desktopIdList = desktopPoolWebHelper.listDesktopIdByPoolIds(idArr);
        if (CollectionUtils.isEmpty(desktopIdList)) {
            return CommonWebResponse.success();
        }

        final Iterator<DefaultBatchTaskItem> iterator = desktopIdList.stream().distinct().map(id -> DefaultBatchTaskItem.builder()
                .itemId(id).itemName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_START_ITEM_NAME, new String[]{}).build()).iterator();
        final StartDesktopBatchTaskHandler handler = new StartDesktopBatchTaskHandler(iterator, auditLogAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setCbbUserDesktopOperateAPI(userDesktopOperateAPI);
        BatchTaskSubmitResult result;
        if (desktopIdList.size() == 1) {
            CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(idArr[0]);
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_START_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SINGLE_START_TASK_DESC, cbbDesktopPoolDTO.getName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_START_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_START_TASK_DESC)
                    .enableParallel().registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     * 桌面池开机
     *
     * @param request request
     * @param builder builder
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("桌面池强制唤醒")
    @RequestMapping(value = "desktopPoolForceWakeUp", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> desktopPoolForceWakeUp(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notNull(idArr, "idArr can not be null");
        List<UUID> desktopIdList = desktopPoolWebHelper.listDesktopIdByPoolIds(idArr);
        if (CollectionUtils.isEmpty(desktopIdList)) {
            return CommonWebResponse.success();
        }

        final Iterator<DefaultBatchTaskItem> iterator = desktopIdList.stream().distinct().map(id -> DefaultBatchTaskItem.builder()
                .itemId(id).itemName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_FORCE_WAKE_UP_ITEM_NAME, new String[]{}).build()).iterator();
        final ForceWakeUpDesktopBatchTaskHandler handler = new ForceWakeUpDesktopBatchTaskHandler(iterator, auditLogAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setCbbUserDesktopOperateAPI(userDesktopOperateAPI);
        BatchTaskSubmitResult result;
        if (desktopIdList.size() == 1) {
            CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(idArr[0]);
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_FORCE_WAKE_UP_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SINGLE_FORCE_WAKE_UP_TASK_DESC, cbbDesktopPoolDTO.getName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_FORCE_WAKE_UP_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_FORCE_WAKE_UP_TASK_DESC)
                    .enableParallel().registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     * 桌面池重启
     *
     * @param request request
     * @param builder builder
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("桌面池重启")
    @RequestMapping(value = "desktopPoolReboot", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> desktopPoolReboot(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notNull(idArr, "idArr can not be null");
        List<UUID> desktopIdList = desktopPoolWebHelper.listDesktopIdByPoolIds(idArr);
        if (CollectionUtils.isEmpty(desktopIdList)) {
            return CommonWebResponse.success();
        }

        final Iterator<DefaultBatchTaskItem> iterator = desktopIdList.stream().distinct().map(id -> DefaultBatchTaskItem.builder()
                .itemId(id).itemName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_REBOOT_ITEM_NAME, new String[]{}).build()).iterator();
        final RestartDesktopBatchTaskHandler handler = new RestartDesktopBatchTaskHandler(iterator, auditLogAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        handler.setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
        BatchTaskSubmitResult result;
        if (desktopIdList.size() == 1) {
            CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(idArr[0]);
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_REBOOT_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SINGLE_REBOOT_TASK_DESC, cbbDesktopPoolDTO.getName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_REBOOT_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_REBOOT_TASK_DESC)
                    .enableParallel().registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     * 桌面池关机
     *
     * @param request request
     * @param builder builder
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("桌面池关机")
    @RequestMapping(value = "desktopPoolShutdown", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> desktopPoolShutdown(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notNull(idArr, "idArr can not be null");
        List<UUID> desktopIdList = desktopPoolWebHelper.listDesktopIdByPoolIds(idArr);
        if (CollectionUtils.isEmpty(desktopIdList)) {
            return CommonWebResponse.success();
        }

        final Iterator<DefaultBatchTaskItem> iterator = desktopIdList.stream().distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SHUTDOWN_ITEM_NAME, new String[]{}).build()).iterator();
        final ShutdownDesktopBatchTaskHandler handler = new ShutdownDesktopBatchTaskHandler(iterator, auditLogAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService)
                .setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI)
                .setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI)
                .setCbbUserDesktopOperateAPI(userDesktopOperateAPI);
        BatchTaskSubmitResult result;
        if (desktopIdList.size() == 1) {
            CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(idArr[0]);
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SHUTDOWN_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SINGLE_SHUTDOWN_TASK_DESC, cbbDesktopPoolDTO.getName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SHUTDOWN_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SHUTDOWN_TASK_DESC)
                    .enableParallel().registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     * 桌面池强制关机
     *
     * @param request request
     * @param builder builder
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("桌面池强制关机")
    @RequestMapping(value = "desktopPoolPowerOff", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> desktopPoolPowerOff(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notNull(idArr, "idArr can not be null");
        List<UUID> desktopIdList = desktopPoolWebHelper.listDesktopIdByPoolIds(idArr);
        if (CollectionUtils.isEmpty(desktopIdList)) {
            return CommonWebResponse.success();
        }

        final Iterator<DefaultBatchTaskItem> iterator = desktopIdList.stream().distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_POWER_OFF_ITEM_NAME, new String[]{}).build()).iterator();

        final PowerOffDesktopBatchTaskHandler handler = new PowerOffDesktopBatchTaskHandler(iterator, auditLogAPI)
                .setCloudDesktopWebService(cloudDesktopWebService)
                .setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI)
                .setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI)
                .setCbbUserDesktopOperateAPI(userDesktopOperateAPI);
        BatchTaskSubmitResult result;
        if (desktopIdList.size() == 1) {
            CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(idArr[0]);
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_POWER_OFF_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SINGLE_POWER_OFF_TASK_DESC, cbbDesktopPoolDTO.getName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_POWER_OFF_TASK_NAME)
                    .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_POWER_OFF_TASK_DESC)
                    .enableParallel().registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    private CommonWebResponse<?> poolUpdateConfigFail(DesktopPoolBasicDTO desktopPoolDTO, String key, String errorMsg, String poolName) {
        if (Objects.nonNull(desktopPoolDTO)) {
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.AVAILABLE);
        }
        auditLogAPI.recordLog(key, poolName, errorMsg);
        return CommonWebResponse.fail(key, new String[]{poolName, errorMsg});
    }

    /**
     * * 应用池镜像模板
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用池镜像模板")
    @RequestMapping(value = "/apply/image", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> applyPoolImageTemplate(IdWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "desktopPoolId must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID desktopPoolId = request.getId();

        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);

        List<PoolDesktopInfoDTO> deskInfoList = desktopPoolMgmtAPI.listNormalDeskInfoByDesktopPoolId(desktopPoolId);
        if (CollectionUtils.isEmpty(deskInfoList)) {
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_STATIC_POOL_APPLY_IMAGE_TEMPLATE_NO_DESK, desktopPoolDTO.getName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        }
        String taskName = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_STATIC_POOL_APPLY_IMAGE_TEMPLATE);
        final Iterator<SyncConfigBatchTaskItem> iterator = deskInfoList.stream()
                .map(item -> buildSyncConfigTask(desktopPoolDTO, item, taskName)).iterator();
        String desktopName = deskInfoList.size() == 1 ? deskInfoList.get(0).getDesktopName() : null;
        final ApplyDesktopPoolAssignConfigBatchTaskHandler handler =
                new ApplyDesktopPoolAssignConfigBatchTaskHandler(iterator, desktopPoolDTO.getName(), desktopName);
        BatchTaskSubmitResult result = builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_STATIC_POOL_APPLY_IMAGE_TEMPLATE_NAME)
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_STATIC_POOL_APPLY_IMAGE_TEMPLATE_BATCH_TASK_DESC)
                .enableParallel().registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }
}

