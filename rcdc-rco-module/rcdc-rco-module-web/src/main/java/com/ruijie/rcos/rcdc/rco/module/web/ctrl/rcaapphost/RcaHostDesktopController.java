package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportCloudDesktopCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ExportCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RcaHostPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.DesktopExportSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request.EditHostSpecWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.batchtask.DeleteRcaHostBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.batchtask.RcaUpdateAppHostSpecBatchTaskHandler;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.validation.AppHostValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.DesktopQueryUtil;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop.DesktopExportWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 派生应用主机桌面管理
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月11日
 *
 * @author liuwc
 */
@Api(tags = "派生应用主机桌面管理")
@Controller
@RequestMapping("/rco/apphost/cloudDesktop")
@EnableCustomValidate(validateClass = AppHostValidation.class)
public class RcaHostDesktopController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RcaHostDesktopController.class);

    private static final String UPDATE_SPEC_APP_HOST_THREAD_POOL_ID = "updateSpecBatchAppHostThreadPoolId";

    @Autowired
    private RcaHostDesktopMgmtAPI rcaHostDesktopMgmtAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private ExportCloudDesktopAPI exportCloudDesktopAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    /**
     * 获取第三方应用主机列表
     *
     * @param request 页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页获取派生应用主机桌面列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取派生应用主机桌面列表"})})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<RcaHostDesktopDTO>> list(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        PageSearchRequest pageReq = new RcaHostPageSearchRequest(request);

        setDefaultSortIfEmpty(pageReq);

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID inputRcaPoolId = findInputRcaPoolId(pageReq);
            if (Objects.nonNull(inputRcaPoolId))   {
                if (!permissionHelper.
                        hasDataPermission(sessionContext.getUserId(), inputRcaPoolId.toString(), AdminDataPermissionType.APP_POOL)) {
                    return CommonWebResponse.success(new DefaultPageResponse<RcaHostDesktopDTO>());
                }
            } else {
                UUID[] rcaPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext.getUserId(), AdminDataPermissionType.APP_POOL);
                setDataPermissiontoRequest(pageReq, rcaPoolIdArr);
            }
        }

        DefaultPageResponse<RcaHostDesktopDTO> resp = rcaHostDesktopMgmtAPI.pageQuery(pageReq);

        return CommonWebResponse.success(resp);
    }

    private void setDefaultSortIfEmpty(PageSearchRequest pageReq) {
        if (ArrayUtils.isEmpty(pageReq.getSortArr())) {
            Sort defaultSort = new Sort();
            defaultSort.setSortField(RcaHostPageSearchRequest.DEFAULT_SORT_CREATE_TIME);
            defaultSort.setDirection(Sort.Direction.DESC);
            pageReq.setSortArr(new Sort[] { defaultSort });
        }
    }

    private UUID findInputRcaPoolId(PageSearchRequest pageReq) {
        if (Objects.isNull(pageReq.getMatchEqualArr())) {
            // 内部方法外层有判空
            return null;
        }
        Optional<MatchEqual> optionalMatchEqual = Arrays.stream(pageReq.getMatchEqualArr())
                .filter(matchEqual -> matchEqual.getName().equals(RcaHostPageSearchRequest.RCA_POOL_ID))
                .findFirst();
        return optionalMatchEqual.map(matchEqual -> {
            Object[] valueArr = matchEqual.getValueArr();
            return ArrayUtils.isNotEmpty(valueArr) ? (UUID) valueArr[0] : null;
        }).orElse(null);
    }

    private void setDataPermissiontoRequest(PageSearchRequest pageReq, UUID[] rcaPoolIdArr) {
        List<MatchEqual> matchEqualList = new ArrayList<>();
        if (Objects.nonNull(pageReq.getMatchEqualArr())) {
            matchEqualList = new ArrayList<>(Arrays.asList(pageReq.getMatchEqualArr()));
        }
        matchEqualList.add(new MatchEqual(RcaHostPageSearchRequest.RCA_POOL_ID, rcaPoolIdArr));
        pageReq.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[matchEqualList.size()]));
    }

    /**
     * 获取第三方应用主机列表
     *
     * @param request 页面请求参数
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页获取派生应用主机桌面详情")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取派生应用主机桌面详情"})})
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public CommonWebResponse<RcaHostDesktopDTO> getInfo(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "request id must not be null");
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(request.getId());

        RcaHostDesktopDTO rcaHostDesktopDTO = new RcaHostDesktopDTO();
        BeanUtils.copyProperties(cloudDesktopDetailDTO, rcaHostDesktopDTO);
        rcaHostDesktopDTO.setServerName(rcaHostDesktopDTO.getServerName());
        // 补充获取RCA相关配置:最大会话数、预启动数、会话保持时长、负载均衡配置
        RcaHostDTO rcaHostDTO = rcaHostAPI.getByDeskId(cloudDesktopDetailDTO.getId());
        RcaAppPoolBaseDTO rcaPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(rcaHostDTO.getPoolId());
        rcaHostDesktopDTO.setRcaMaxSessionCount(rcaHostDTO.getMaxSessionCount());
        rcaHostDesktopDTO.setRcaPreStartHostNum(rcaPoolBaseDTO.getPreStartHostNum());
        rcaHostDesktopDTO.setRcaSessionHoldTime(rcaPoolBaseDTO.getSessionHoldTime());
        rcaHostDesktopDTO.setRcaLoadBalanceMode(rcaPoolBaseDTO.getLoadBalanceMode());
        rcaHostDesktopDTO.setRcaSessionHoldConfigMode(rcaPoolBaseDTO.getSessionHoldConfigMode());
        rcaHostDesktopDTO.setRcaPoolName(rcaPoolBaseDTO.getName());
        rcaHostDesktopDTO.setRcaPoolType(rcaPoolBaseDTO.getPoolType());
        rcaHostDesktopDTO.setRcaHostSessionType(rcaPoolBaseDTO.getSessionType());
        rcaHostDesktopDTO.setOneAgentVersion(rcaHostDTO.getOneAgentVersion());
        // 根据池去更新对应的主机策略和外设策略，应用策略需要根据实际绑定的来展示
        UUID desktopStrategyId = cloudDesktopDetailDTO.getDesktopStrategyId();
        RcaMainStrategyDTO mainStrategyDTO = rcaMainStrategyAPI.getStrategyDetailByDesktopStrategyId(desktopStrategyId);
        rcaHostDesktopDTO.setDesktopStrategyId(mainStrategyDTO.getId());
        rcaHostDesktopDTO.setStrategyName(mainStrategyDTO.getName());
        rcaHostDesktopDTO.setRcaPeripheralStrategyId(rcaPoolBaseDTO.getPeripheralStrategyId());
        rcaHostDesktopDTO.setRcaPeripheralStrategyName(rcaPoolBaseDTO.getPeripheralStrategyName());
        List<RcaHostDesktopDTO> rcaHostDesktopDTOList = rcaHostDesktopMgmtAPI.listByHostIdIn(
                Lists.newArrayList(request.getId()));
        if (!CollectionUtils.isEmpty(rcaHostDesktopDTOList)) {
            rcaHostDesktopDTO.setEnableHyperVisorImprove(rcaHostDesktopDTOList.get(0).getEnableHyperVisorImprove());
        }

        return CommonWebResponse.success(rcaHostDesktopDTO);
    }

    /**
     * * 云桌面删除
     *
     * @param request 页面请求参数
     * @param builder builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "分页获取派生应用主机桌面删除")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取派生应用主机桌面删除"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse delete(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        Boolean shouldOnlyDeleteDataFromDb = request.getShouldOnlyDeleteDataFromDb();
        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_ITEM_NAME, new String[] {Constants.APP_CLOUD_DESKTOP, prefix}).build())//
                .iterator();
        final DeleteRcaHostBatchTaskHandler handler = new DeleteRcaHostBatchTaskHandler(iterator, auditLogAPI, true);
        handler.setCloudDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setUserProfileMgmtAPI(userProfileMgmtAPI);
        handler.setRcaHostAPI(rcaHostAPI);
        handler.setRcaAppPoolAPI(rcaAppPoolAPI);
        handler.setRcaHostSessionAPI(rcaHostSessionAPI);
        handler.setShouldOnlyDeleteDataFromDb(shouldOnlyDeleteDataFromDb);

        BatchTaskSubmitResult result = executeDeleteBatchTask(builder, idArr, handler, prefix);
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 异步导出云桌面数据到excel
     *
     * @param webRequest webRequest
     * @param sessionContext Session上下文
     * @return result
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation("导出云桌面")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"}),
        @ApiVersion(value = Version.V3_2_221, descriptions = {"加入桌面池桌面导出"})})
    @EnableAuthority
    public CommonWebResponse export(DesktopExportWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        webRequest.setImageUsage(ImageUsageTypeEnum.APP);
        ExportCloudDesktopRequest request = buildExportRequest(webRequest, sessionContext);
        exportCloudDesktopAPI.exportRcaHostDataAsync(request);
        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_RCA_HOST_DESKTOP_EXPORT_SUCCESS_LOG, new String[] {});
        return CommonWebResponse.success();
    }

    /**
     * 获取云桌面导出任务情况
     *
     * @param sessionContext sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出结果")
    @RequestMapping(value = "/getExportResult", method = RequestMethod.POST)
    public CommonWebResponse<ExportCloudDesktopCacheDTO> getExportResult(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportCloudDesktopCacheResponse response = exportCloudDesktopAPI.getExportDataCache(userId);
        return CommonWebResponse.success(response.getCloudDesktopCacheDTO());
    }

    /**
     * 下载云桌面数据excel
     *
     * @param request 页面请求参数
     * @param sessionContext SessionContext
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/downloadExportData", method = RequestMethod.GET)
    public DownloadWebResponse downloadExportFile(EmptyDownloadWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportCloudDesktopFileResponse exportFile = exportCloudDesktopAPI.getExportFile(userId);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "主机信息" + df.format(new Date());
        return builder.setFile(exportFile.getExportFile(), false).setName(fileName, "xlsx").build();
    }

    /**
     * * 应用主机修改规格
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用主机修改规格")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用主机修改规格"})})
    @RequestMapping(value = "/editAppHostSpec", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "editAppHostSpecValidate")
    @EnableAuthority
    public DefaultWebResponse editAppHostSpec(EditHostSpecWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        final Iterator<DefaultBatchTaskItem> iterator = request.getHostIdList().stream().map(item -> DefaultBatchTaskItem.builder().itemId(item)
                .itemName(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_ITEM, new String[]{}).build()).iterator();
        final RcaUpdateAppHostSpecBatchTaskHandler handler = new RcaUpdateAppHostSpecBatchTaskHandler(iterator, auditLogAPI,
                rcaAppPoolAPI, rcaHostAPI, request);
        handler.setDeskSpecAPI(deskSpecAPI);
        handler.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
        handler.setRcaHostSessionAPI(rcaHostSessionAPI);
        BatchTaskSubmitResult result;
        if (request.getHostIdList().size() == 1) {
            RcaHostDTO hostDTO = rcaHostAPI.getById(request.getHostIdList().get(0));
            result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_SINGLE_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_SINGLE_TASK_DESC, hostDTO.getName())
                    .registerHandler(handler).start();
        } else {
            UUID createId = UUID.nameUUIDFromBytes(UPDATE_SPEC_APP_HOST_THREAD_POOL_ID.getBytes());
            result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .enablePerformanceMode(createId, 30).start();
        }
        return DefaultWebResponse.Builder.success(result);
    }


    private BatchTaskSubmitResult executeDeleteBatchTask(BatchTaskBuilder builder, UUID[] idArr
            , final DeleteRcaHostBatchTaskHandler handler, String prefix) throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(idArr[0]);
            result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_SINGLE_TASK_NAME, prefix)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getDesktopName(), prefix)
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_TASK_NAME, prefix)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_TASK_DESC, prefix).enableParallel().registerHandler(handler).start();
        }
        return result;
    }


    private ExportCloudDesktopRequest buildExportRequest(DesktopExportWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Sort[] sortArr = DesktopQueryUtil.generateDesktopSortArr(null);
        String userId = sessionContext.getUserId().toString();
        ExportCloudDesktopRequest request = new ExportCloudDesktopRequest(userId, sortArr, Lists.newArrayList());
        request.setEnableAllGroupPermission(false);
        request.setImageUsage(webRequest.getImageUsage());

        if (Objects.isNull(webRequest.getExportSource()) || webRequest.getExportSource() == DesktopExportSourceEnum.DESKTOP) {
            // 桌面管理桌面导出
            if (permissionHelper.isAllGroupPermission(sessionContext)) {
                // 是否拥有全部数据权限
                request.setEnableAllGroupPermission(true);
            } else {
                request.setDesktopIdList(Stream.of(permissionHelper.getDesktopIdArr(sessionContext)).collect(Collectors.toList()));
            }

            // 应用池界面中到出应用主机列表
            UUID[] appPoolArr = webRequest.getAppPoolIdArr();
            if (!Objects.isNull(appPoolArr) && ArrayUtils.isNotEmpty(webRequest.getAppPoolIdArr())) {
                List<UUID> appPoolIdList = Lists.newArrayList(appPoolArr);
                filterByAppPoolId(appPoolIdList, request);
            }
            return request;
        }
        return request;
    }

    /**
     * 过滤出应用池下的应用主机
     * 
     * @param appPoolIdList 应用池Id列表
     * @param request 请求参数
     * @throws BusinessException ex
     */
    private void filterByAppPoolId(List<UUID> appPoolIdList, ExportCloudDesktopRequest request) throws BusinessException {
        List<RcaHostDTO> rcaHostDTOList = rcaHostAPI.findAllByPoolIdIn(appPoolIdList);
        List<UUID> desktopIdList = rcaHostDTOList.stream().filter(
            rcaHostDTO -> rcaHostDTO.getHostSourceType() == RcaEnum.HostSourceType.VDI && rcaHostDTO.getHostUsage() == RcaEnum.HostUsage.APP)
            .map(RcaHostDTO::getId).collect(Collectors.toList());

        // 在有应用池id的约束下，不需要导出所有的主机信息
        request.setEnableAllGroupPermission(Boolean.FALSE);
        request.setDesktopIdList(desktopIdList);
    }

}
