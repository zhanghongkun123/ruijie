package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionCreateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.common.UserCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask.CreateDesktopTempPermissionBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask.DeleteDesktopTempPermissionBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask.EditDesktopTempPermissionBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.CheckDesktopTempPermissionNameRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.CreateDesktopTempPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.DeleteDesktopTempPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.DesktopTempPermissionBindDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.DesktopTempPermissionBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.UpdateDesktopTempPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.validation.DesktopTempPermissionValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.DesktopQueryUtil;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * Description: 临时权限Controller
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-04-26
 *
 * @author linke
 */
@Api(tags = "云桌面临时权限")
@Controller
@RequestMapping("/rco/desktopTempPermission")
@EnableCustomValidate(validateClass = DesktopTempPermissionValidation.class)
public class DesktopTempPermissionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopTempPermissionController.class);

    private static final String NAME = "name";

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private DesktopTempPermissionAPI desktopTempPermissionAPI;

    @Autowired
    private CbbDesktopTempPermissionAPI cbbDesktopTempPermissionAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private UserCommonHelper userCommonHelper;

    /**
     * 分页查询
     *
     * @param request        页面请求参数
     * @return CommonWebResponse<DefaultPageResponse < DesktopPoolDTO>>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("云桌面临时权限列表")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "云桌面临时权限")})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<DesktopTempPermissionDetailDTO>> list(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request is null.");
        return CommonWebResponse.success(desktopTempPermissionAPI.pageQuery(request));
    }

    /**
     * 获取云桌面临时权限详情(包含开通对象信息)
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云桌面临时权限详情(包含开通对象信息)")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "云桌面临时权限")})
    @RequestMapping(value = {"/detail","/getInfo"}, method = RequestMethod.POST)
    public CommonWebResponse<DesktopTempPermissionDetailDTO> getDetailInfo(IdWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(webRequest.getId(), "id must not be null");

        return CommonWebResponse.success(desktopTempPermissionAPI.getDetailInfo(webRequest.getId()));
    }

    /**
     * 校验名称是否存在
     *
     * @param request 页面请求参数
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("校验云桌面临时权限名称是否存在")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "云桌面临时权限")})
    @RequestMapping(value = "checkDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckDuplicationWebResponse> checkNameDuplication(CheckDesktopTempPermissionNameRequest request)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");

        PageQueryRequest pageRequest = pageQueryBuilderFactory.newRequestBuilder().eq(NAME, request.getName()).build();
        PageQueryResponse<CbbDesktopTempPermissionDTO> resp = cbbDesktopTempPermissionAPI.pageQuery(pageRequest);

        CheckDuplicationWebResponse webResponse = new CheckDuplicationWebResponse();
        webResponse.setHasDuplication(false);
        if (resp.getTotal() > 0 && ArrayUtils.isNotEmpty(resp.getItemArr())) {
            if (Objects.isNull(request.getId()) || !Objects.equals(request.getId(), resp.getItemArr()[0].getId())) {
                webResponse.setHasDuplication(true);
            }
        }
        return CommonWebResponse.success(webResponse);
    }

    /**
     * 创建云桌面临时权限
     *
     * @param webRequest     请求参数
     * @param sessionContext sessionContext
     * @param builder        builder
     * @return 返回值
     * @throws BusinessException BusinessException
     */
    @ApiOperation("创建云桌面临时权限")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "云桌面临时权限")})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "createDesktopTempPermissionValidate")
    @EnableAuthority
    public CommonWebResponse<?> create(CreateDesktopTempPermissionRequest webRequest, SessionContext sessionContext, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        Assert.notNull(builder, "builder must not be null");

        DesktopTempPermissionCreateDTO createDTO = new DesktopTempPermissionCreateDTO();
        BeanUtils.copyProperties(webRequest, createDTO);

        try {
            // 获取登录用户信息
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            createDTO.setCreatorUserName(baseAdminDTO.getUserName());

            // 磁盘映射
            buildDiskMappingParam(webRequest.getDiskMappingType(), createDTO);

            List<DefaultBatchTaskItem> itemList = Lists.newArrayList(DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                    .itemName(LocaleI18nResolver.resolve(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_CREATE_ITEM_NAME)).build());

            CreateDesktopTempPermissionBatchTaskHandler handler = new CreateDesktopTempPermissionBatchTaskHandler(itemList, createDTO);
            BatchTaskSubmitResult result = builder.setTaskName(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_BATCH_CREATE_TASK_NAME)
                    .setTaskDesc(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_BATCH_CREATE_TASK_DESC)
                    .enableParallel().registerHandler(handler).start();
            return CommonWebResponse.success(result);
        } catch (Exception e) {
            LOGGER.error("创建云桌面临时权限[{}]失败", webRequest.getName(), e);
            String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_CREATE_ERROR, webRequest.getName(), msg);
            return CommonWebResponse.fail(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_CREATE_ERROR,
                    new String[]{webRequest.getName(), msg});
        }
    }

    /**
     * 删除云桌面临时权限
     *
     * @param webRequest 请求参数
     * @param builder    builder
     * @throws BusinessException 业务异常
     * @return 返回值
     */
    @ApiOperation("删除云桌面临时权限")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "云桌面临时权限")})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> delete(DeleteDesktopTempPermissionRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        List<DefaultBatchTaskItem> itemList = webRequest.getIdList().stream()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver.resolve(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_DELETE_ITEM_NAME)).build())
                .collect(Collectors.toList());

        BatchTaskSubmitResult result;
        if (webRequest.getIdList().size() == 1) {
            result = deleteSingleRecord(webRequest.getIdList().get(0), itemList, builder);
        } else {
            DeleteDesktopTempPermissionBatchTaskHandler handler = new DeleteDesktopTempPermissionBatchTaskHandler(itemList);
            result = builder.setTaskName(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_BATCH_DELETE_TASK_NAME)
                    .setTaskDesc(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_BATCH_DELETE_TASK_DESC)
                    .enableParallel().registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult deleteSingleRecord(UUID id, List<DefaultBatchTaskItem> itemList, BatchTaskBuilder builder)
            throws BusinessException {
        String logName = cbbDesktopTempPermissionAPI.getDesktopTempPermission(id).getName();

        DeleteDesktopTempPermissionBatchTaskHandler handler = new DeleteDesktopTempPermissionBatchTaskHandler(itemList, logName);
        return builder.setTaskName(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_SINGLE_DELETE_TASK_NAME)
                .setTaskDesc(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_SINGLE_DELETE_TASK_DESC, logName).enableParallel()
                .registerHandler(handler).start();
    }

    /**
     * 编辑云桌面临时权限
     *
     * @param webRequest 请求参数
     * @param builder    builder
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑云桌面临时权限")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "云桌面临时权限")})
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "createDesktopTempPermissionValidate")
    @EnableAuthority
    public CommonWebResponse<?> edit(UpdateDesktopTempPermissionRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        CbbDesktopTempPermissionDTO permissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(webRequest.getId());
        DesktopTempPermissionUpdateDTO updateDTO = new DesktopTempPermissionUpdateDTO();
        BeanUtils.copyProperties(webRequest, updateDTO);
        try {
            // 磁盘映射
            buildDiskMappingParam(webRequest.getDiskMappingType(), updateDTO);

            List<DefaultBatchTaskItem> itemList = Lists.newArrayList(DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                    .itemName(LocaleI18nResolver.resolve(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_UPDATE_ITEM_NAME)).build());

            EditDesktopTempPermissionBatchTaskHandler handler = new EditDesktopTempPermissionBatchTaskHandler(itemList, updateDTO);
            BatchTaskSubmitResult result = builder.setTaskName(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_BATCH_UPDATE_TASK_NAME)
                    .setTaskDesc(DesktopTempPermissionBusinessKey.RCDC_DESK_TEMP_PERMISSION_BATCH_UPDATE_TASK_DESC)
                    .enableParallel().registerHandler(handler).start();
            return CommonWebResponse.success(result);
        } catch (Exception e) {
            LOGGER.error("编辑云桌面临时权限[{}]失败", permissionDTO.getName(), e);
            String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_UPDATE_ERROR, permissionDTO.getName(), msg);
            return CommonWebResponse.fail(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_UPDATE_ERROR,
                    new String[]{permissionDTO.getName(), msg});
        }
    }

    private void buildDiskMappingParam(DiskMappingEnum diskMappingEnum, DesktopTempPermissionCreateDTO dto) throws BusinessException {
        if (Objects.isNull(diskMappingEnum)) {
            return;
        }
        dto.setEnableDiskMapping(diskMappingEnum.getEnableDiskMapping());
        dto.setEnableDiskMappingWriteable(diskMappingEnum.getEnableDiskMappingWriteable());
    }

    /**
     * * 分页查询关联的用户
     *
     * @param request 页面请求参数
     * @param sessionContext 上下文
     * @return DefaultPageResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页查询临时权限关联的用户")
    @RequestMapping(value = "/user/page", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserListDTO>> pageDesktopPoolBindUser(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        DesktopTempPermissionBindUserRequest pageRequest = new DesktopTempPermissionBindUserRequest(request);
        Assert.notNull(pageRequest.getDesktopTempPermissionId(), "pageRequest.desktopTempPermissionId must not be null");

        if (!userCommonHelper.checkAndAddQueryUserPermission(request, pageRequest, sessionContext)) {
            // 传的组ID不在权限内直接返回空
            return CommonWebResponse.success(new DefaultPageResponse<>());
        }

        return CommonWebResponse.success(desktopTempPermissionAPI.pageBindUser(pageRequest.getDesktopTempPermissionId(), pageRequest));
    }

    /**
     * * 分页查询关联的云桌面
     *
     * @param request 页面请求参数
     * @param sessionContext 上下文
     * @return DefaultPageResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页查询临时权限关联的云桌面")
    @RequestMapping(value = "/desktop/page", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<CloudDesktopDTO>> pageDesktopPoolBindDesktop(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");

        PageSearchRequest pageReq = new DeskPageSearchRequest(request);
        Sort[] sortArr = DesktopQueryUtil.generateDesktopSortArr(pageReq.getSortArr());
        pageReq.setSortArr(sortArr);

        DesktopTempPermissionBindDesktopRequest pageRequest = new DesktopTempPermissionBindDesktopRequest(request);
        Assert.notNull(pageRequest.getDesktopTempPermissionId(), "pageRequest.desktopTempPermissionId must not be null");

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] uuidArr = permissionHelper.getDesktopIdArr(sessionContext);
            pageReq.appendCustomMatchEqual(new MatchEqual("cbbDesktopId", uuidArr));
        }

        DefaultPageResponse<CloudDesktopDTO> response = desktopTempPermissionAPI.pageBindDesktop(pageRequest.getDesktopTempPermissionId(),
                pageRequest);

        DesktopQueryUtil.convertMoreAccurateCloudDesktopInfo(response);
        return CommonWebResponse.success(response);
    }

}
