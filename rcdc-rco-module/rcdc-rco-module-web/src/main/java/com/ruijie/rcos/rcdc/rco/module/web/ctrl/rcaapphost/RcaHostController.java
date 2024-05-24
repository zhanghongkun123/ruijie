package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost;

import com.google.common.net.InetAddresses;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskGtInfoAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.*;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.ViewHostSessionDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.HostAgentCollectLogStateInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.ImageProgramDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rca.module.def.request.PageSearchRcaSessionRequest;
import com.ruijie.rcos.rcdc.rca.module.def.request.RcaHostPageQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportRcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportRcaHostCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ExportRcaHostRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportRcaHostCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportRcaHostFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.batchtask.*;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.dto.CreateThirdPartRcaHostBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.dto.RcaHostSegmentIpDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.dto.RcaHostSingleIpDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.enums.QueryAppHostTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.response.GetRcaHostCollectLogStateWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdArrRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 应用主机管理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月27日
 *
 * @author liuwc
 */
@Api(tags = "应用主机管理")
@Controller
@RequestMapping("/rco/apphost")
public class RcaHostController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RcaHostController.class);

    private static final String LOG_PREFIX = "rca_host-";

    private final static Integer CHARACTER_SIZE = 25;

    private static final String POINT = ".";

    private static final Integer INCREASING = 1;

    private final static Integer LENGTH_SINGLE = 1;

    private final static Integer INITIAL_NUMBER = 0;


    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaHostCollectLogAPI rcaHostCollectLogAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RcaHostAppAPI rcaHostAppAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private ExportRcaHostAPI exportRcaHostAPI;

    @Autowired
    private CbbDeskGtInfoAPI cbbDeskGtInfoAPI;

    /**
     * 添加第三方应用主机
     *
     * @param hostAppThirdPartRequest 请求参数
     * @param batchTaskBuilder        批处理任务结果
     * @return 返回结果
     */
    @ApiOperation("创建第三方应用主机")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"创建第三方应用主机"})})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse createThirdPartyAppHost(RcaHostAppThirdPartRequest hostAppThirdPartRequest, BatchTaskBuilder batchTaskBuilder) {
        Assert.notNull(hostAppThirdPartRequest, "request cannot be null");
        Assert.notNull(batchTaskBuilder, "batchTaskBuilder cannot be null");

        try {
            if (hostAppThirdPartRequest.getName().length() > CHARACTER_SIZE) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_NAME_OUT_OF_LIMIT);
            }

            if (CollectionUtils.isEmpty(hostAppThirdPartRequest.getIpList())
                    && CollectionUtils.isEmpty(hostAppThirdPartRequest.getSegmentIpDTOList())) {
                return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_HOST_CREATE_FAIL, new String[]{});
            }

            List<RcaHostDTO> createAppHostDTOList = buildThreePartyHostParameters(hostAppThirdPartRequest);

            final Iterator<CreateThirdPartRcaHostBatchTaskItem> itemIterator = createAppHostDTOList.stream()
                    .distinct().map(createHostAppDTO -> CreateThirdPartRcaHostBatchTaskItem.builder()
                            .itemId(createHostAppDTO.getId())
                            .itemName(LocaleI18nResolver.resolve(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_TASK_ITEM_NAME))
                            .itemData(createHostAppDTO).build()).iterator();

            CreateThirdPartRcaHostBatchTaskHandler handler = new CreateThirdPartRcaHostBatchTaskHandler(itemIterator, auditLogAPI);
            handler.setRcaHostAPI(rcaHostAPI);
            BatchTaskSubmitResult result =
                    batchTaskBuilder.setTaskName(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_TASK_NAME)
                            .setTaskDesc(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_TASK_DESC)
                            .enableParallel()
                            .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);

        } catch (BusinessException e) {
            LOGGER.error(LOG_PREFIX + "创建第三方应用主机异常，异常原因:", e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_CREATE_FAIL, e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_CREATE_FAIL,
                    new String[]{e.getI18nMessage()});
        }
    }

    /**
     * 编辑第三方应用主机
     *
     * @param updateRequest 请求参数
     * @return 返回结果
     * @throws BusinessException 异常
     */
    @ApiOperation("编辑第三方应用主机")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑第三方应用主机"})})
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editThirdPartyAppHost(UpdateRcaHostAppThirdPartRequest updateRequest) throws BusinessException {
        Assert.notNull(updateRequest, "updateRequest cannot be null");

        RcaHostDTO hostInfo = rcaHostAPI.getById(updateRequest.getId());
        if (ObjectUtils.isEmpty(hostInfo)) {
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_FAIL_NULL,
                    new String[]{});
        }
        try {
            RcaHostDTO rcaHostDTO = new RcaHostDTO();
            BeanUtils.copyProperties(updateRequest, rcaHostDTO);
            rcaHostAPI.editThirdPartyHost(rcaHostDTO);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_INFO_SUCCESS, hostInfo.getName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_INFO_SUCCESS, new String[]{hostInfo.getName()});
        } catch (BusinessException e) {
            LOGGER.error(LOG_PREFIX + "编辑第三方应用主机异常，异常原因:", e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_INFO_FAIL, hostInfo.getName(), e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_INFO_FAIL,
                    new String[]{hostInfo.getName(), e.getI18nMessage()});
        }
    }

    /**
     * 应用主机重名校验
     *
     * @param request 请求参数
     * @return 结果
     */
    @ApiOperation("应用主机重名校验")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用主机重名校验"})})
    @RequestMapping(value = "/checkHostDuplicate", method = RequestMethod.POST)
    public DefaultWebResponse checkHostDuplicate(CheckRcaHostNameDuplicateRequest request) {
        Assert.notNull(request, "request cannot be null");

        RcaHostDTO dto = rcaHostAPI.getRcaHostInfoByNameAndHostUsage(request.getName(), RcaEnum.HostUsage.APP);
        CheckDuplicationWebResponse response = new CheckDuplicationWebResponse();
        response.setHasDuplication(false);
        if ((dto != null) && (!(request.getHostId() != null && request.getHostId().equals(dto.getId())))) {
            response.setHasDuplication(true);
        }
        return DefaultWebResponse.Builder.success(response);
    }


    /**
     * 删除第三方应用主机
     *
     * @param request 删除的主机id列表
     * @param builder builder
     * @return 返回结果
     * @throws BusinessException 异常
     */
    @ApiOperation("删除第三方应用主机")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"删除第三方应用主机"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse deleteThirdPartyAppHost(IdArrRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder不能为null");
        UUID[] idArr = request.getIdArr();
        // 批量删除
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver.resolve(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_TASK_ITEM_NAME)).build())
                .iterator();
        DeleteRcaHostBatchTaskHandler handler =
                new DeleteRcaHostBatchTaskHandler(iterator, auditLogAPI, false);
        handler.setRcaHostAPI(rcaHostAPI);
        handler.setRcaAppPoolAPI(rcaAppPoolAPI);

        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_TASK_NAME, StringUtils.EMPTY)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_TASK_DESC, StringUtils.EMPTY).enableParallel().registerHandler(handler)
                .start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 获取第三方应用主机列表
     *
     * @param pageWebRequest 请求参数
     * @param sessionContext Session上下文
     * @return 结果 app vm list
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页获取第三方应用主机列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取第三方应用主机列表"})})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public DefaultWebResponse getThirdPartyHostList(RcaHostPageQueryRequest pageWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest cannot be null");
        Assert.notNull(sessionContext, "sessionContext is null");
        // 获取第三方主机列表
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] rcaPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext.getUserId(), AdminDataPermissionType.APP_POOL);
            pageWebRequest.setHasPermissionPoolIdArr(rcaPoolIdArr);
        }
        DefaultPageResponse<RcaHostDTO> response = rcaHostAPI.pageQueryThirdPartyHost(pageWebRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 异步第三方应用主机列表到excel
     *
     * @param webRequest     webRequest
     * @param sessionContext Session上下文
     * @return result
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation("导出第三方应用主机")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页导出第三方应用主机列表"})})
    @EnableAuthority
    public CommonWebResponse export(RcaHostExportWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        ExportRcaHostRequest request = buildExportRequest(webRequest, sessionContext);
        exportRcaHostAPI.exportDataAsync(request);
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXPORT_TASK_SUCCESS, new String[]{});
        return CommonWebResponse.success();
    }

    private ExportRcaHostRequest buildExportRequest(RcaHostExportWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        String userId = sessionContext.getUserId().toString();
        ExportRcaHostRequest request = new ExportRcaHostRequest(userId);
        request.setEnableAllDataPermission(false);

        if (Objects.isNull(webRequest.getAppPoolIdArr())) { // null表示主机应用管理-第三方应用主机页面(全部返回)
            request.setRcaPoolPage(false);
            if (permissionHelper.isAllGroupPermission(sessionContext)) { // 拥有全部数据权限
                request.setEnableAllDataPermission(true);
            } else { // 非超级管理员的应用池权限，可能为空
                request.setHasPermissionAppPoolIdList(Stream.of(permissionHelper
                        .getPermissionIdArr(sessionContext, AdminDataPermissionType.APP_POOL)).collect(Collectors.toList()));
            }
        } else { // 表示应用池-第三方应用主机页面
            request.setRcaPoolPage(true);
            if (!permissionHelper.isAllGroupPermission(sessionContext)) {
                for (UUID appPoolId : webRequest.getAppPoolIdArr()) { // 判断是否有导出权限
                    if (!permissionHelper.hasDataPermission(sessionContext.getUserId(), appPoolId.toString(), AdminDataPermissionType.APP_POOL)) {
                        LOGGER.error("导出第三方应用主机列表,存在非法的应用池id={}", appPoolId);
                        throw new BusinessException(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXPORT_APP_POOL_NO_PERMISSION);
                    }
                }
            }
            request.setAppPoolIdList(Arrays.asList(webRequest.getAppPoolIdArr()));
        }
        return request;
    }

    /**
     * 获取云桌面导出任务情况
     *
     * @param sessionContext sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出结果")
    @RequestMapping(value = "/getExportResult", method = RequestMethod.POST)
    public CommonWebResponse<ExportRcaHostCacheDTO> getExportResult(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportRcaHostCacheResponse response = exportRcaHostAPI.getExportDataCache(userId);
        return CommonWebResponse.success(response.getRcaHostCacheDTO());
    }

    /**
     * 下载应用主机数据excel
     *
     * @param sessionContext SessionContext
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/downloadExportData", method = RequestMethod.GET)
    public DownloadWebResponse downloadExportFile(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportRcaHostFileResponse exportFile = exportRcaHostAPI.getExportFile(userId);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXPORT_NAME) + df.format(new Date());
        return builder.setFile(exportFile.getExportFile(), false).setName(fileName, "xlsx").build();
    }

    /**
     * 新建池_获取可添加到池的第三方应用主机列表
     *
     * @param pageWebRequest 请求参数
     * @param sessionContext session信息
     * @return 结果 app vm list
     * @throws BusinessException 业务异常
     */
    @ApiOperation("新建池_获取可添加到池的第三方应用主机列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取第三方应用主机列表"})})
    @RequestMapping(value = "/addPool/list", method = RequestMethod.POST)
    public DefaultWebResponse getThirdPartyHostListForAddPool(RcaHostPageQueryRequest pageWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest cannot be null");
        Assert.notNull(sessionContext, "sessionContext is null");
        // 获取第三方主机列表，过滤已经加到池里面的主机信息
        pageWebRequest.setIsForAddPool(true);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] rcaPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext.getUserId(), AdminDataPermissionType.APP_POOL);
            pageWebRequest.setHasPermissionPoolIdArr(rcaPoolIdArr);
        }

        DefaultPageResponse<RcaHostDTO> response = rcaHostAPI.pageQueryThirdPartyHost(pageWebRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 编辑池_获取可添加到池的第三方应用主机列表
     *
     * @param pageWebRequest 请求参数
     * @param sessionContext session信息
     * @return 结果 app vm list
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑池_获取可添加到池的第三方应用主机列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取第三方应用主机列表"})})
    @RequestMapping(value = "/editPool/list", method = RequestMethod.POST)
    public DefaultWebResponse getThirdPartyHostListForEditPool(RcaHostPageQueryRequest pageWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest cannot be null");
        Assert.notNull(sessionContext, "sessionContext is null");
        // 获取第三方主机列表，过滤已经加到池里面的主机信息
        pageWebRequest.setIsForEditPool(true);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] rcaPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext.getUserId(), AdminDataPermissionType.APP_POOL);
            pageWebRequest.setHasPermissionPoolIdArr(rcaPoolIdArr);
        }

        DefaultPageResponse<RcaHostDTO> response = rcaHostAPI.pageQueryThirdPartyHost(pageWebRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 池_添加应用主机_获取可添加到池的第三方应用主机列表
     *
     * @param pageWebRequest 请求参数
     * @param sessionContext session信息
     * @return 结果 app vm list
     * @throws BusinessException 业务异常
     */
    @ApiOperation("池_添加应用主机_获取可添加到池的第三方应用主机列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取第三方应用主机列表"})})
    @RequestMapping(value = "/plusPool/list", method = RequestMethod.POST)
    public DefaultWebResponse getThirdPartyHostListForPlusPool(RcaHostPageQueryRequest pageWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest cannot be null");
        Assert.notNull(sessionContext, "sessionContext is null");
        // 获取第三方主机列表，过滤已经加到池里面的主机信息
        pageWebRequest.setIsForPlusPool(true);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] rcaPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext.getUserId(), AdminDataPermissionType.APP_POOL);
            pageWebRequest.setHasPermissionPoolIdArr(rcaPoolIdArr);
        }

        DefaultPageResponse<RcaHostDTO> response = rcaHostAPI.pageQueryThirdPartyHost(pageWebRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    private void handleRequestPermission(RcaHostPageQueryRequest request, SessionContext sessionContext) {
        if (permissionHelper.isAllGroupPermission(sessionContext)) {
            return;
        }

    }

    /**
     * 获取第三方应用主机对应的应用列表
     * 1、第三方主机的应用列表：直接根据hostId获取应用表的信息
     * 2、镜像应用列表：若有临时虚机上报的应用，则优先返回临时虚机的应用；
     * 3、镜像应用列表：多版本镜像+通过镜像id获取：后端获取当前正在使用的版本id，该id作为hostId获取应用表的信息
     * 4、镜像应用列表：单版本镜像，直接根据hostId获取应用表的信息
     * 5、镜像应用列表：多版本镜像+通过版本获取：前端的hostId为镜像版本id，去获取应用表的信息
     *
     * @param pageWebRequest 请求参数
     * @return 结果 app vm list
     * @throws BusinessException 异常
     */
    @ApiOperation("获取应用主机对应的应用列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取应用主机对应的应用列表"})})
    @RequestMapping(value = "/appList", method = RequestMethod.POST)
    public DefaultWebResponse getThirdPartyHostAppList(PageWebRequest pageWebRequest) throws BusinessException {
        Assert.notNull(pageWebRequest, "request is null.");

        RcaHostAppPageRequest pageReq = new RcaHostAppPageRequest(pageWebRequest);
        if (pageReq.getHostQueryType() == QueryAppHostTypeEnum.THIRD_HOST) {
            // 第三方主机直接请求应用表
            DefaultPageResponse<ImageProgramDTO> defaultPageResponse = rcaHostAppAPI.pageQuery(pageReq);
            return DefaultWebResponse.Builder.success(defaultPageResponse);
        }

        UUID hostId = pageReq.getHostId();
        // 若有临时虚机的应用，则优先查询历史应用表
        if (rcaHostAppAPI.hasTempVmApp(hostId)) {
            DefaultPageResponse<ImageProgramDTO> defaultPageResponse = rcaHostAppAPI.pageQueryTempVmApp(pageReq);
            return DefaultWebResponse.Builder.success(defaultPageResponse);
        }

        // 镜像卡片触发+多版本，获取正在使用的版本id并获取分页信息
        UUID rootImageId = pageReq.getHostId();
        CbbImageTemplateDetailDTO rootImage = cbbImageTemplateMgmtAPI.getImageTemplateDetail(rootImageId);
        UUID versionSourceSnapshotId = rootImage.getLastRecoveryPointId();
        if (pageReq.getHostQueryType() == QueryAppHostTypeEnum.IMAGE && pageReq.getIsMultipleImage() && Objects.nonNull(versionSourceSnapshotId)) {
            CbbGetImageTemplateInfoDTO currentVersionImage = cbbImageTemplateMgmtAPI.getImageTemplateInfoBySourceSnapshotId(versionSourceSnapshotId);
            LOGGER.info("多版本镜像卡片请求应用列表,镜像id=[{}], 正在使用的版本id=[{}]", rootImageId, currentVersionImage.getId());
            changeAppListQueryHostId(pageReq, currentVersionImage.getId());
            DefaultPageResponse<ImageProgramDTO> defaultPageResponse = rcaHostAppAPI.pageQuery(pageReq);
        }

        DefaultPageResponse<ImageProgramDTO> defaultPageResponse = rcaHostAppAPI.pageQuery(pageReq);
        return DefaultWebResponse.Builder.success(defaultPageResponse);

    }

    private void changeAppListQueryHostId(RcaHostAppPageRequest pageReq, UUID newHostId) {
        MatchEqual[] exactMatchArr = pageReq.getMatchEqualArr();
        if (ArrayUtils.isNotEmpty(exactMatchArr)) {
            for (MatchEqual exactMatch : exactMatchArr) {
                if (StringUtils.equals("hostId", exactMatch.getName())) {
                    UUID[] newHostArr = {newHostId};
                    exactMatch.setValueArr(newHostArr);
                    break;
                }
            }
        }
    }

    /**
     * 获取第三方应用主机详情
     *
     * @param request 请求参数
     * @return 结果 app vm list
     * @throws BusinessException 异常
     */
    @ApiOperation("获取第三方应用主机详情")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"获取第三方应用主机详情"})})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public DefaultWebResponse getThirtPartyHostDetail(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        // 获取第三方主机详情
        RcaHostDTO dto = rcaHostAPI.getById(request.getId());
        if (Objects.nonNull(dto.getPoolId())) {
            // 设置池名称
            RcaAppPoolBaseDTO poolBaseDTO = rcaAppPoolAPI.getAppPoolById(dto.getPoolId());
            dto.setPoolName(poolBaseDTO.getName());
            // 设置标签
            RcaStrategyRelationshipDTO strategyRequestDTO = new RcaStrategyRelationshipDTO();
            strategyRequestDTO.setPoolId(poolBaseDTO.getId());
            RcaMainStrategyDTO mainStrategyConfigDetail = rcaMainStrategyAPI.getStrategyConfigDetail(strategyRequestDTO);
            dto.setRemark(mainStrategyConfigDetail.getDesktopStrategyConfig().getRemark());
        }
        return DefaultWebResponse.Builder.success(dto);
    }

    /**
     * 第三方应用主机关机
     *
     * @param request 需要关机的主机id列表
     * @param builder builder
     * @return 返回结果
     * @throws BusinessException 异常
     */
    @ApiOperation("第三方应用主机关机")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"第三方应用主机关机"})})
    @RequestMapping(value = "/shutdown", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse shutdownThirdPartyRcaHost(IdArrRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder不能为null");
        UUID[] idArr = request.getIdArr();
        if (idArr.length == LENGTH_SINGLE) {
            UUID hostId = idArr[INITIAL_NUMBER];
            return shutdownSingleThirdPartyRcaHost(hostId, auditLogAPI);
        }

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_ITEM_NAME, new String[]{}).build()).iterator();
        final ShutdownThirdPartRcaHostBatchTaskHandler handler = new ShutdownThirdPartRcaHostBatchTaskHandler(iterator, auditLogAPI);
        handler.setRcaHostAPI(rcaHostAPI);

        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_TASK_NAME)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_TASK_DESC)//
                .registerHandler(handler).start();


        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse shutdownSingleThirdPartyRcaHost(UUID hostId, BaseAuditLogAPI auditLogAPI) throws BusinessException {
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(hostId);
        String hostName = rcaHostDTO.getName();
        if (!ObjectUtils.isEmpty(rcaHostDTO) && rcaHostDTO.getStatus() == RcaEnum.HostStatus.OFFLINE) {
            LOGGER.error(LOG_PREFIX + "应用主机[{}]关机失败，应用主机离线", hostId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_OFFLINE, rcaHostDTO.getName());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_OFFLINE,
                    new String[]{rcaHostDTO.getName()});
        }
        try {
            rcaHostAPI.shutdownThirdPartyHost(hostId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_SUCCESS, rcaHostDTO.getName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_SUCCESS,
                    new String[]{rcaHostDTO.getName()});
        } catch (BusinessException e) {
            LOGGER.error(LOG_PREFIX + "应用主机[{}]关机失败，异常原因：", hostId, e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_FAIL, hostName, e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_SHUTDOWN_FAIL,
                    new String[]{hostName, e.getI18nMessage()});
        }
    }

    /**
     * 第三方应用主机重启
     *
     * @param request 需要重启的主机id列表
     * @param builder builder
     * @return 返回结果
     * @throws BusinessException 异常
     */
    @ApiOperation("第三方应用主机重启")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"第三方应用主机重启"})})
    @RequestMapping(value = "/reboot", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse rebootThirdPartyRcaHost(IdArrRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder不能为null");
        UUID[] idArr = request.getIdArr();
        if (idArr.length == LENGTH_SINGLE) {
            UUID hostId = idArr[INITIAL_NUMBER];
            return rebootSingleThirdPartyRcaHost(hostId, auditLogAPI);
        }

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_REBOOT_ITEM_NAME, new String[]{}).build()).iterator();
        final RebootThirdPartRcaHostBatchTaskHandler handler = new RebootThirdPartRcaHostBatchTaskHandler(iterator, auditLogAPI);
        handler.setRcaHostAPI(rcaHostAPI);

        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_REBOOT_TASK_NAME)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_REBOOT_TASK_DESC)//
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse rebootSingleThirdPartyRcaHost(UUID hostId, BaseAuditLogAPI auditLogAPI) throws BusinessException {
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(hostId);
        String hostName = rcaHostDTO.getName();
        if (!ObjectUtils.isEmpty(rcaHostDTO) && rcaHostDTO.getStatus() == RcaEnum.HostStatus.OFFLINE) {
            LOGGER.error(LOG_PREFIX + "应用主机[{}]重启失败，应用主机离线", hostId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_REBOOT_OFFLINE, rcaHostDTO.getName());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_REBOOT_OFFLINE,
                    new String[]{rcaHostDTO.getName()});
        }
        try {
            rcaHostAPI.rebootThirdPartyHost(hostId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_REBOOT_SUCCESS, rcaHostDTO.getName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_REBOOT_SUCCESS,
                    new String[]{rcaHostDTO.getName()});
        } catch (BusinessException e) {
            LOGGER.error(LOG_PREFIX + "应用主机[{}]重启失败，异常原因：", hostId, e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_REBOOT_FAIL, hostName, e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_REBOOT_FAIL,
                    new String[]{hostName, e.getI18nMessage()});
        }
    }

    /**
     * 第三方应用主机批量修改最大会话数
     *
     * @param request 需要修改会话数的主机id列表
     * @param builder builder
     * @return 返回结果
     * @throws BusinessException 异常
     */
    @ApiOperation("第三方应用主机批量修改最大会话数")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"第三方应用主机批量修改最大会话数"})})
    @RequestMapping(value = "/editSessionTotal", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editSessionTotal(EditHostSessionTotalRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder不能为null");
        UUID[] idArr = request.getIdArr();

        if (idArr.length == LENGTH_SINGLE) {
            UUID hostId = idArr[INITIAL_NUMBER];
            RcaHostDTO rcaHostDTO = rcaHostAPI.getById(hostId);
            try {
                rcaHostAPI.editSessionTotal(hostId, request.getSessionTotal());
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_SUCCESS, rcaHostDTO.getName());
                return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_SUCCESS,
                        new String[]{rcaHostDTO.getName()});
            } catch (BusinessException e) {
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_FAIL, rcaHostDTO.getName(), e.getI18nMessage());
                return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_FAIL,
                        new String[]{rcaHostDTO.getName(), e.getMessage()});
            }
        }

        // 批量编辑
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver.resolve(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_TASK_ITEM_NAME)).build())
                .iterator();
        EditHostSessionTotalBatchTaskHandler handler =
                new EditHostSessionTotalBatchTaskHandler(iterator, auditLogAPI, rcaHostAPI);
        handler.setSessionTotal(request.getSessionTotal());
        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_TASK_NAME)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_HOST_EDIT_SESSION_TOTAL_TASK_DESC).enableParallel().registerHandler(handler)
                .start();
        return DefaultWebResponse.Builder.success(result);
    }


    /**
     * 进行日志收集
     *
     * @param request 日志收集请求
     * @return response
     * @throws BusinessException 异常
     */
    @ApiOperation("收集日志")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"收集第三方应用主机日志"})})
    @RequestMapping(value = "/log/collect", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse collect(CollectRcaHostOaLogRequest request)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");

        rcaHostCollectLogAPI.collectLog(request.getHostId());
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(request.getHostId());
        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_COLLECT_LOG_SUCCESS, rcaHostDTO.getName());
        return DefaultWebResponse.Builder.success();
    }

    /**
     * 获取日志收集的状态
     *
     * @param request 请求
     * @return 日志收集状态响应信息
     */
    @ApiOperation("获取日志收集状态")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"收集第三方应用主机日志"})})
    @RequestMapping(value = "/log/getState", method = RequestMethod.POST)
    public CommonWebResponse<GetRcaHostCollectLogStateWebResponse> getState(CollectRcaHostOaLogRequest request) {
        Assert.notNull(request, "request can not be null");

        HostAgentCollectLogStateInfoDTO stateInfoDTO = rcaHostCollectLogAPI.getState(request.getHostId());
        GetRcaHostCollectLogStateWebResponse response = new GetRcaHostCollectLogStateWebResponse();
        BeanUtils.copyProperties(stateInfoDTO, response);

        return CommonWebResponse.success(response);
    }

    /**
     * 下载日志文件
     *
     * @param request 请求信息
     * @return 下载响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("下载日志")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"下载第三方应用主机日志"})})
    @RequestMapping(value = "/log/download", method = RequestMethod.GET)
    @EnableAuthority
    public DownloadWebResponse download(DownloadRcaHostLogWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        UUID hostId = request.getHostId();
        String logName = request.getLogName();
        String fullPath = rcaHostCollectLogAPI.getLogFileFullPath(hostId, logName);
        if (org.springframework.util.StringUtils.isEmpty(fullPath)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_HOST_THIRD_PARTY_LOG_FILE_NOT_EXISTS);
        }

        String logNameWithoutSuffix = logName.substring(0, logName.lastIndexOf('.'));
        String suffix = logName.substring(logName.lastIndexOf('.') + 1);

        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setFile(new File(fullPath), false).setName(logNameWithoutSuffix, suffix).build();
    }

    /**
     * 绑定用户和云主机的关系
     *
     * @param request 请求参数
     * @param builder builder
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("绑定用户")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"绑定用户"})})
    @RequestMapping(value = "/bindUser", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse bindUser(RcaSessionBindUserRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");

        UUID hostId = request.getHostId();
        UUID[] idArr = request.getIdArr();
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(hostId);
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                        .itemId(id).itemName(RcaBusinessKey.RCDC_RCA_HOST_BIND_USER_SESSION_ITEM_NAME, new String[]{}).build())//
                .iterator();

        final BindUserAndRcaHostBatchTaskHandler handler = new BindUserAndRcaHostBatchTaskHandler(iterator, auditLogAPI, hostId);
        handler.setRcaHostAPI(rcaHostAPI);
        handler.setRcaHostSessionAPI(rcaHostSessionAPI);

        BatchTaskSubmitResult result = executeDeleteBatchTask(builder, idArr, rcaHostDTO.getName(), handler);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult executeDeleteBatchTask(BatchTaskBuilder builder, UUID[] idArr, String hostName,
                                                         final BindUserAndRcaHostBatchTaskHandler handler)
            throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(idArr[0]);
            result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_HOST_BIND_USER_SESSION_SINGLE_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_HOST_BIND_USER_SESSION_SINGLE_TASK_DESC, hostName, userDetail.getUserName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_HOST_BIND_USER_SESSION_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_HOST_BIND_USER_SESSION_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 解除用户和云主机的绑定关系
     *
     * @param request 请求参数
     * @param builder builder
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("解绑用户")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"解绑用户"})})
    @RequestMapping(value = "/unbindUser", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse unbindUser(RcaSessionUnbindUserRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");

        if (StringUtils.hasText(request.getUserName())) {
            return dealSingleUnBind(request);
        }

        UUID hostId = request.getHostId();
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(hostId);
        UUID[] idArr = request.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                        .itemId(id).itemName(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_ITEM_NAME, new String[]{}).build())//
                .iterator();

        final UnBindUserAndRcaHostBatchTaskHandler handler = new UnBindUserAndRcaHostBatchTaskHandler(iterator, auditLogAPI, hostId);
        handler.setRcaHostAPI(rcaHostAPI);
        handler.setRcaHostSessionAPI(rcaHostSessionAPI);
        handler.setCbbUserAPI(cbbUserAPI);
        handler.setRcaAppPoolAPI(rcaAppPoolAPI);

        BatchTaskSubmitResult result = executeDeleteBatchTask(builder, idArr, rcaHostDTO.getName(), handler);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult executeDeleteBatchTask(BatchTaskBuilder builder, UUID[] idArr, String hostName,
                                                         final UnBindUserAndRcaHostBatchTaskHandler handler)
            throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(idArr[0]);
            result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_SINGLE_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_SINGLE_TASK_DESC, hostName, userDetail.getUserName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_TASK_DESC)
                    .enableParallel()
                    .registerHandler(handler)
                    .start();
        }
        return result;
    }

    private DefaultWebResponse dealSingleUnBind(RcaSessionUnbindUserRequest request)
            throws BusinessException {
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(request.getHostId());
        String hostName = rcaHostDTO.getName();
        String userName = request.getUserName();
        try {
            UUID hostSingleBindUser = rcaHostSessionAPI.getHostSingleBindUser(request.getHostId(),
                    RcaEnum.HostSessionBindMode.STATIC);
            rcaHostSessionAPI.unbindUser(request.getUserId(), request.getHostId());
            if (RcaEnum.HostSourceType.VDI.equals(rcaHostDTO.getHostSourceType()) && hostSingleBindUser != null) {
                LOGGER.info("应用池[{}]VDI应用主机[{}]解除静态单会话绑定", rcaHostDTO.getPoolId(), rcaHostDTO.getId());
                rcaAppPoolAPI.regressSelfHostToPool(rcaHostDTO.getId(), rcaHostDTO.getPoolId());
            }

        } catch (BusinessException e) {
            LOGGER.error(LOG_PREFIX + "应用主机[{}]取消关联用户会话失败, e={}", hostName, e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_FAIL, hostName, userName, e.getI18nMessage());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_FAIL
                    , new String[]{hostName, userName, e.getI18nMessage()});
        }

        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_SUCCESS, hostName, userName);
        return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_SUCCESS, new String[]{hostName, userName});
    }

    /**
     * 绑定用户和云主机的关系
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取应用主机的用户列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"获取应用主机的用户列表"})})
    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    public DefaultWebResponse userSessionList(PageSearchRcaSessionRequest request) throws BusinessException {
        Assert.notNull(request, "pageWebRequest cannot be null");

        PageQueryResponse<ViewHostSessionDTO> response = rcaHostSessionAPI.pageQueryHostSession(request, Boolean.FALSE);
        return DefaultWebResponse.Builder.success(response);
    }

    List<RcaHostDTO> buildThreePartyHostParameters(RcaHostAppThirdPartRequest hostAppThirdPartRequest) throws BusinessException {
        List<RcaHostDTO> createHostAppDTOArrayList = new ArrayList<>();
        List<RcaHostSingleIpDTO> ipList = hostAppThirdPartRequest.getIpList();
        if (!CollectionUtils.isEmpty(ipList)) {
            Set<String> ipSet = new HashSet<>();
            // 单IP添加
            for (RcaHostSingleIpDTO ipInfo : ipList) {
                if (ipSet.contains(ipInfo.getIp())) {
                    // 682818 后端也增加IP去重
                    continue;
                }
                boolean isInetAddress = InetAddresses.isInetAddress(ipInfo.getIp());
                if (!isInetAddress) {
                    throw new BusinessException(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_DATA_ABNORMAL_OPERATION);
                }
                createHostAppDTOArrayList.add(doThirdPartCreateDTO(hostAppThirdPartRequest, ipInfo));
                ipSet.add(ipInfo.getIp());
            }
        }
        List<RcaHostSegmentIpDTO> networkSegmentList = hostAppThirdPartRequest.getSegmentIpDTOList();
        if (!CollectionUtils.isEmpty(networkSegmentList)) {
            Set<String> ipSet = new HashSet<>();
            for (RcaHostSegmentIpDTO networkSegmentDTO : networkSegmentList) {
                String startIp = networkSegmentDTO.getStartIp();
                String endIp = networkSegmentDTO.getEndIp();
                if (startIp != null && endIp != null) {
                    String prefix = startIp.substring(0, startIp.lastIndexOf(POINT));
                    int start = Integer.parseInt(startIp.substring(startIp.lastIndexOf(POINT) + INCREASING));
                    int end = Integer.parseInt(endIp.substring(endIp.lastIndexOf(POINT) + INCREASING));
                    while (start <= end) {
                        String ip = prefix + POINT + start++;
                        RcaHostSingleIpDTO tempSingleIpDTO = new RcaHostSingleIpDTO();
                        tempSingleIpDTO.setIp(ip);
                        tempSingleIpDTO.setHostAuthName(networkSegmentDTO.getHostAuthName());
                        tempSingleIpDTO.setHostAuthCode(networkSegmentDTO.getHostAuthCode());
                        if (ipSet.contains(ip)) {
                            continue;
                        }
                        createHostAppDTOArrayList.add(doThirdPartCreateDTO(hostAppThirdPartRequest, tempSingleIpDTO));
                        ipSet.add(ip);
                    }
                }
            }

        }
        return createHostAppDTOArrayList;
    }

    /**
     * 执行构建应用主机实体类
     *
     * @param hostAppThirdPartRequest 前端参数
     * @param ipInfo                  ip信息、云主机管理员账号密码
     * @return the trusteeship app vm entity
     */
    private RcaHostDTO doThirdPartCreateDTO(RcaHostAppThirdPartRequest hostAppThirdPartRequest, RcaHostSingleIpDTO ipInfo) throws BusinessException {
        RcaHostDTO createHostAppDTO = new RcaHostDTO();
        BeanUtils.copyProperties(hostAppThirdPartRequest, createHostAppDTO);
        createHostAppDTO.setId(UUID.randomUUID());
        createHostAppDTO.setIp(ipInfo.getIp());
        createHostAppDTO.setHostAuthName(ipInfo.getHostAuthName());
        createHostAppDTO.setHostAuthCode(ipInfo.getHostAuthCode());
        createHostAppDTO.setHostSourceType(RcaEnum.HostSourceType.THIRD_PARTY);
        if (createHostAppDTO.getHostSessionType() == RcaEnum.HostSessionType.SINGLE) {
            createHostAppDTO.setMaxSessionCount(1);
        }

        return createHostAppDTO;
    }

}
