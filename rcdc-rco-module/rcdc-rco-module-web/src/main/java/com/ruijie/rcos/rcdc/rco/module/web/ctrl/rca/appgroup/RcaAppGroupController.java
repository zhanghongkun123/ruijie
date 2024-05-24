package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupEntityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdGroupAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaOneClientNotifyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.CheckDuplicateGroupNameRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.CreateAppGroupRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.EditAppGroupRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.RcaUpdateAppGroupBindRequest;
import com.ruijie.rcos.rcdc.rca.module.def.spi.RcaGroupMemberSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGroupMemberAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserRcaGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.user.common.UserCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask.RcaDeleteAppGroupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask.RcaUnbindAppGroupAdGroupBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask.RcaUnbindAppGroupUserBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask.RcaUpdateAppGroupUserBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.CheckDuplicationResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Description: 应用池分组web接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月08日
 *
 * @author zhengjingyong
 */
@Api(tags = "应用池分组")
@Controller
@RequestMapping("/rca/appGroup")
public class RcaAppGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaAppGroupController.class);

    @Autowired
    private RcaAppGroupAPI rcaAppGroupAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaGroupMemberAPI rcaGroupMemberAPI;

    @Autowired
    private RcoGroupMemberAPI rcoGroupMemberAPI;

    @Autowired
    private UserCommonHelper userCommonHelper;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private IacAdGroupAPI adGroupAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private RcaOneClientNotifyAPI rcaOneClientNotifyAPI;

    @Autowired
    private RcaGroupMemberSPI rcaGroupMemberSPI;


    /**
     * 创建应用池分组
     *
     * @param request        请求参数
     * @param optLogRecorder optLogRecorder
     * @param sessionContext sessionContext
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建应用池分组")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"创建应用池分组"})})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse createAppGroup(RcaCreateAppGroupWebRequest request, ProgrammaticOptLogRecorder optLogRecorder,
                                             SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(optLogRecorder, "ProgrammaticOptLogRecorder不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");

        CreateAppGroupRequest createAppGroupRequest = new CreateAppGroupRequest();
        BeanUtils.copyProperties(request, createAppGroupRequest);
        createAppGroupRequest.setDefaultGroup(Boolean.FALSE);
        generalPermissionHelper.checkPermission(sessionContext, createAppGroupRequest.getAppPoolId(), AdminDataPermissionType.APP_POOL);
        String poolName = "";
        try {
            rcaAppGroupAPI.createAppGroup(createAppGroupRequest);
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(createAppGroupRequest.getAppPoolId());
            poolName = appPoolBaseDTO.getName();
            optLogRecorder.saveOptLog(RcaBusinessKey.RCDC_RCA_POOL_APP_GROUP_CREATE_SUCCESS_LOG,
                    poolName, request.getName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_POOL_APP_GROUP_CREATE_SUCCESS_LOG
                    , new String[]{poolName, request.getName()});
        } catch (BusinessException e) {
            LOGGER.error("创建用户组失败", e);
            optLogRecorder.saveOptLog(RcaBusinessKey.RCDC_RCA_POOL_APP_GROUP_CREATE_FAIL_LOG, poolName,
                    request.getName(), e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_POOL_APP_GROUP_CREATE_FAIL_LOG,
                    new String[]{poolName, request.getName(), e.getI18nMessage()});
        }
    }

    /**
     * 编辑应用池分组
     *
     * @param request        请求参数
     * @param optLogRecorder optLogRecorder
     * @param sessionContext sessionContext
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑应用池分组")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑应用池分组"})})
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editAppGroup(RcaEditAppGroupWebRequest request, ProgrammaticOptLogRecorder optLogRecorder,
                                           SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(optLogRecorder, "ProgrammaticOptLogRecorder不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");

        RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(request.getId());
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());

        EditAppGroupRequest editAppGroupRequest = new EditAppGroupRequest();
        BeanUtils.copyProperties(request, editAppGroupRequest);
        try {
            Boolean isAppChanged = rcaAppGroupAPI.editAppGroup(editAppGroupRequest);
            if (isAppChanged) {
                rcaOneClientNotifyAPI.notifyAllOnlineUserAppChangedByAppGroupId(Lists.newArrayList(request.getId()));
            }
            optLogRecorder.saveOptLog(RcaBusinessKey.RCDC_RCA_POOL_APP_GROUP_EDIT_SUCCESS_LOG, appPoolBaseDTO.getName(),
                    groupDTO.getName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_POOL_APP_GROUP_EDIT_SUCCESS_LOG,
                    new String[]{appPoolBaseDTO.getName(), groupDTO.getName()});
        } catch (BusinessException e) {
            LOGGER.error("编辑用户组失败", e);
            optLogRecorder.saveOptLog(RcaBusinessKey.RCDC_RCA_POOL_APP_GROUP_EDIT_FAIL_LOG, appPoolBaseDTO.getName(),
                    groupDTO.getName(), e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_POOL_APP_GROUP_EDIT_FAIL_LOG,
                    new String[]{appPoolBaseDTO.getName(), groupDTO.getName(), e.getI18nMessage()});
        }
    }

    /**
     * 校验同个池下是否重名
     *
     * @param request 请求参数对象
     * @return 返回是否存在重名组
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/checkDuplication", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"校验同个池下分组名是否重名"})})
    @ApiOperation("校验同个池下是否重名")
    public DefaultWebResponse checkUserGroupDuplication(CheckDuplicateGroupNameWebRequest request) throws BusinessException {
        Assert.notNull(request, "request 不能为null");

        CheckDuplicateGroupNameRequest duplicateGroupNameRequest = new CheckDuplicateGroupNameRequest();
        BeanUtils.copyProperties(request, duplicateGroupNameRequest);
        Boolean hasDuplication = rcaAppGroupAPI.checkNameDuplication(duplicateGroupNameRequest);
        CheckDuplicationResponse duplicationResponse = new CheckDuplicationResponse(hasDuplication);
        return DefaultWebResponse.Builder.success(duplicationResponse);
    }

    /**
     * 获取池分组详情
     *
     * @param request request
     * @return 应用池详情
     * @throws BusinessException ex
     */
    @ApiOperation("应用分组详情")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用分组详情"})})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public DefaultWebResponse detail(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        return DefaultWebResponse.Builder.success(rcaAppGroupAPI.getGroupDetail(request.getId()));
    }

    /**
     * 池分组列表查询
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 池分组
     * @throws BusinessException 业务异常
     */
    @ApiOperation("池分组列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用分组列表"})})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public DefaultWebResponse list(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        AppPoolGroupPageRequest pageReq = new AppPoolGroupPageRequest(request);
        DefaultPageResponse<RcaGroupDTO> defaultPageResponse = rcaAppGroupAPI.pageQuery(pageReq);
        return DefaultWebResponse.Builder.success(defaultPageResponse);
    }

    /**
     * 删除应用池分组
     *
     * @param request        删除请求
     * @param optLogRecorder optLogRecorder
     * @param builder        builder
     * @return res
     * @throws BusinessException ex
     */
    @ApiOperation("删除分组")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"删除应用分组"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse delete(IdArrWebRequest request, ProgrammaticOptLogRecorder optLogRecorder, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(optLogRecorder, "optLogRecorder can not be null");
        Assert.notNull(builder, "builder can not be null");

        if (request.getIdArr().length == 1) {
            UUID appGroupId = request.getIdArr()[0];
            RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(appGroupId);
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());

            rcaAppGroupAPI.deleteAppGroup(appGroupId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_ITEM_SUCCESS,
                    appPoolBaseDTO.getName(), groupDTO.getName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_ITEM_SUCCESS,
                    new String[] {appPoolBaseDTO.getName(), groupDTO.getName()});
        }

        // 批量删除应用池分组
        Iterator<DefaultBatchTaskItem> iterator = Arrays.stream(request.getIdArr())
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(
                        RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_ITEM, new String[]{}).build()).iterator();

        RcaDeleteAppGroupBatchTaskHandler handler = new RcaDeleteAppGroupBatchTaskHandler(iterator, auditLogAPI,
                rcaAppPoolAPI, rcaAppGroupAPI);
        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_NAME)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_TASK_DESC).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 编辑应用池分组分配关系
     *
     * @param request        分配情况
     * @param builder        builder
     * @param sessionContext sessionContext
     * @return 响应消息
     * @throws BusinessException BusinessException
     */
    @ApiOperation("编辑应用池分组分配关系")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑应用池分组分配关系"})})
    @RequestMapping(value = "/updateBind", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse updatePoolBindObject(RcaUpdateAppGroupBindWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder cannot be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");

        RcaUpdateAppGroupBindRequest groupBindRequest = new RcaUpdateAppGroupBindRequest();
        BeanUtils.copyProperties(request, groupBindRequest);

        // 获取管理员的数据权限，超管就是null
        UUID[] userGroupIdArr = new UUID[0];
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            userGroupIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.USER_GROUP);
        }

        Iterator<DefaultBatchTaskItem> iterator = request.getAppGroupIdList().stream()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(
                        RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_UPDATE_BIND_OBJ_TASK_ITEM, new String[]{}).build()).iterator();
        RcaUpdateAppGroupUserBatchHandler handler = new RcaUpdateAppGroupUserBatchHandler(groupBindRequest, userGroupIdArr, iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setRcaAppGroupAPI(rcaAppGroupAPI);
        handler.setRcaAppPoolAPI(rcaAppPoolAPI);
        handler.setRcaGroupMemberAPI(rcaGroupMemberAPI);
        handler.setRcaOneClientNotifyAPI(rcaOneClientNotifyAPI);
        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_UPDATE_BIND_OBJ_TASK_NAME)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_UPDATE_BIND_OBJ_TASK_DESC)
                .registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * * 分页查询应用分组关联的所有用户（用户组下的用户+分配的用户）
     *
     * @param request        页面请求参数
     * @param sessionContext 上下文
     * @return DefaultPageResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页查询应用分组关联的所有用户（用户组下的用户+分配的用户）")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页查询应用分组关联的所有用户"})})
    @RequestMapping(value = "/pageBind", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserListDTO>> pageAppGroupBindUser(PageWebRequest request,
                                                                                    SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        RcaAppGroupBindUserPageRequest pageRequest = new RcaAppGroupBindUserPageRequest(request);
        Assert.notNull(pageRequest.getAppGroupId(), "pageRequest.appGroupId must not be null");

        userCommonHelper.dealNonVisitorUserTypeMatch(pageRequest);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<String> userGroupIdList = getPermissionUserGroupIdList(sessionContext.getUserId());
            if (CollectionUtils.isNotEmpty(userGroupIdList)) {
                UUID[] uuidArr = userGroupIdList.stream().filter(idStr -> !idStr.equals(UserGroupHelper.USER_GROUP_ROOT_ID)).map(UUID::fromString)
                        .toArray(UUID[]::new);
                pageRequest.appendCustomMatchEqual(new MatchEqual(UserPageSearchRequest.GROUP_ID, uuidArr));
            }
        }
        return CommonWebResponse.success(rcoGroupMemberAPI.pageQueryRealBindUser(pageRequest.getAppGroupId(), pageRequest));
    }

    /**
     * 查询用户的应用分组列表
     *
     * @param request        页面请求参数
     * @param sessionContext session信息
     * @return CommonWebResponse<DefaultPageResponse < UserRcaGroupDTO>>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询用户的应用分组列表")
    @RequestMapping(value = "/listByUser", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserRcaGroupDTO>> listByUser(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        RcaGroupPageRequest pageReq = new RcaGroupPageRequest(request);
        Assert.notNull(pageReq.getUserId(), "userId is null.");

        DefaultPageResponse<UserRcaGroupDTO> resp = rcoGroupMemberAPI.pageQueryByUser(pageReq.getUserId(), pageReq);
        return CommonWebResponse.success(resp);
    }

    /**
     * 查询AD域组的应用分组列表
     *
     * @param request        页面请求参数
     * @param sessionContext session信息
     * @return CommonWebResponse<DefaultPageResponse < UserRcaGroupDTO>>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询AD域组的应用分组列表")
    @RequestMapping(value = "/listByAdGroup", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserRcaGroupDTO>> listByAdGroup(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        RcaGroupPageRequest pageReq = new RcaGroupPageRequest(request);
        Assert.notNull(pageReq.getAdGroupId(), "adGroupId is null.");

        DefaultPageResponse<UserRcaGroupDTO> resp = rcoGroupMemberAPI.pageQueryByAdGroup(pageReq.getAdGroupId(), pageReq);
        return CommonWebResponse.success(resp);
    }

    /**
     * 解除应用池分组分配用户
     *
     * @param request 分配情况
     * @param builder builder
     * @return 响应消息
     * @throws BusinessException BusinessException
     */
    @ApiOperation("解除应用池分组分配用户")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"解除应用池分组分配用户"})})
    @RequestMapping(value = "/unbindUser", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse unbindUser(RcaGroupUnbindUserWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder cannot be null");

        RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(request.getAppGroupId());
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());
        if (request.getUserIdList().size() == 1) {
            // 删除单个用户
            UUID userId = request.getUserIdList().get(0);
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);

            rcaGroupMemberAPI.unbindUser(request.getAppGroupId(), userId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_DELETE_BIND_USER_TASK_SUCCESS,
                    appPoolBaseDTO.getName(), groupDTO.getName(), userDetail.getUserName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_DELETE_BIND_USER_TASK_SUCCESS,
                    new String[]{appPoolBaseDTO.getName(), groupDTO.getName(), userDetail.getUserName()});
        } else {
            Iterator<DefaultBatchTaskItem> iterator = request.getUserIdList().stream()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(LocaleI18nResolver.resolve(
                            RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_USER_TASK_ITEM)).build()).iterator();
            RcaUnbindAppGroupUserBatchHandler handler = new RcaUnbindAppGroupUserBatchHandler(iterator,
                    rcaAppPoolAPI, rcaAppGroupAPI, rcaGroupMemberAPI, auditLogAPI);
            handler.setCbbUserAPI(cbbUserAPI);
            handler.setAppGroupId(request.getAppGroupId());
            handler.setRcaOneClientNotifyAPI(rcaOneClientNotifyAPI);
            handler.setRcaGroupMemberSPI(rcaGroupMemberSPI);

            BatchTaskSubmitResult result = builder
                    .setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_USER_TASK_NAME,
                            appPoolBaseDTO.getName(), groupDTO.getName())
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_USER_TASK_DESC,
                            appPoolBaseDTO.getName(), groupDTO.getName())
                    .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }

    /**
     * 解除应用池分组分配安全组
     *
     * @param request 分配情况
     * @param builder builder
     * @return 响应消息
     * @throws BusinessException BusinessException
     */
    @ApiOperation("解除应用池分组分配安全组")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"解除应用池分组分配用户"})})
    @RequestMapping(value = "/unbindAdGroup", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse unbindAdGroup(RcaGroupUnbindAdGroupWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder cannot be null");

        RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(request.getAppGroupId());
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());
        if (request.getAdGroupIdList().size() == 1) {
            // 删除单个安全组
            UUID adGroupId = request.getAdGroupIdList().get(0);
            IacAdGroupEntityDTO adGroupDetail = adGroupAPI.getAdGroupDetail(adGroupId);

            rcaGroupMemberAPI.unbindAdGroup(request.getAppGroupId(), adGroupId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_DELETE_BIND_AD_GROUP_TASK_SUCCESS,
                    appPoolBaseDTO.getName(), groupDTO.getName(), adGroupDetail.getName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_DELETE_BIND_AD_GROUP_TASK_SUCCESS,
                    new String[]{appPoolBaseDTO.getName(), groupDTO.getName(), adGroupDetail.getName()});
        } else {
            Iterator<DefaultBatchTaskItem> iterator = request.getAdGroupIdList().stream()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(LocaleI18nResolver.resolve(
                            RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_AD_GROUP_TASK_ITEM)).build()).iterator();
            RcaUnbindAppGroupAdGroupBatchHandler handler = new RcaUnbindAppGroupAdGroupBatchHandler(iterator,
                    rcaAppPoolAPI, rcaAppGroupAPI, rcaGroupMemberAPI, auditLogAPI);
            handler.setAppGroupId(request.getAppGroupId());
            handler.setCbbAdGroupAPI(adGroupAPI);
            BatchTaskSubmitResult result = builder
                    .setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_AD_GROUP_TASK_NAME,
                            appPoolBaseDTO.getName(), groupDTO.getName())
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_AD_GROUP_TASK_DESC,
                            appPoolBaseDTO.getName(), groupDTO.getName())
                    .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }


    private List<String> getPermissionUserGroupIdList(UUID adminId) throws BusinessException {
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(adminId);
        ListUserGroupIdResponse listUserGroupIdResponse = adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest);
        return listUserGroupIdResponse.getUserGroupIdList();
    }
}
