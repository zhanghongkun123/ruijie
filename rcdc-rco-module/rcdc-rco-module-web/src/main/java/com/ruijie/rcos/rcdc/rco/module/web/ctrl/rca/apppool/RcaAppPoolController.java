package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.*;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseWithGroupDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolOverviewDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaUserCustomStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rca.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.query.DefaultConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.utils.StateMachineUtils;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.DeliveryObjectType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.DeliveryAppImageTemplateVersionWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask.RcaCreateGroupAndBindBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask.RcaCreateGroupWithBindBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request.RcaGroupBindWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.validation.AppPoolImageTemplateValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.validation.AppPoolValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.RcoBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.CheckDuplicationResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilderFactory;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
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

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 融合应用池接口层
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月26日
 *
 * @author zhengjingyong
 */

@Api(tags = "应用池管理")
@Controller
@RequestMapping("/rca/appPool")
@EnableCustomValidate(validateClass = AppPoolValidation.class)
public class RcaAppPoolController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaAppPoolController.class);

    private static final UUID CREATE_POOL_HOST_TASK_ID = UUID.nameUUIDFromBytes("create_pool_host".getBytes(StandardCharsets.UTF_8));

    private static final int SINGLE_POOL_MAX_HOST_NUM = 1000;

    private static final String ID = "id";

    private static final String RCA_POOL_ID_ARR = "rcaPoolIdArr";

    private static final String ROOT_IMAGE_ID = "rootImageId";

    private static final String IMAGE_TEMPLATE_ID = "imageTemplateId";

    private static final String SYSTEM_SIZE = "systemSize";

    private static final String POOL_STATE = "poolState";

    private static final String HOST_SOURCE_TYPE = "hostSourceType";

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private StateMachineUtils stateMachineUtils;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private CreateRcaHostDesktopAPI createRcaHostDesktopAPI;

    @Autowired
    private RcaHostDesktopMgmtAPI rcaHostDesktopMgmtAPI;

    @Autowired
    private RcaAppGroupAPI rcaAppGroupAPI;

    @Autowired
    private RcaGroupMemberAPI rcaGroupMemberAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private RcaHostAppAPI rcaHostAppAPI;

    @Autowired
    private RcaOneClientNotifyAPI rcaOneClientNotifyAPI;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private AppPoolImageTemplateValidation appPoolImageTemplateValidation;

    @Autowired
    private BatchTaskBuilderFactory batchTaskBuilderFactory;

    @Autowired
    private DeliveryHostImageBatchTaskService deliveryChangeHostImageBatchTaskService;

    @Autowired
    private RcaUserCustomStrategyAPI rcaUserCustomStrategyAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private RcaMainStrategyWatermarkAPI rcaMainStrategyWatermarkAPI;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    /**
     * 创建融合应用池
     *
     * @param request        创建请求
     * @param optLogRecorder optLogRecorder
     * @param builder        builder
     * @param sessionContext session
     * @return CommonWebResponse
     * @throws BusinessException ex
     */
    @ApiOperation("创建应用池")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"创建应用池"})})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse create(CreateRcaAppPoolWebRequest request, ProgrammaticOptLogRecorder optLogRecorder,
                                     BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(optLogRecorder, "optLogRecorder can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        CreateAppPoolRequest createAppPoolRequest = new CreateAppPoolRequest();
        BeanUtils.copyProperties(request, createAppPoolRequest);
        DeskSpecRequest deskSpecRequest = request.getDeskSpecRequest();
        if (deskSpecRequest != null) {
            CbbDeskSpecDTO cbbDeskSpecDTO = deskSpecAPI.buildCbbDeskSpec(request.getClusterId(), request.getDeskSpecRequest());
            createAppPoolRequest.setCbbDeskSpecDTO(cbbDeskSpecDTO);
        }

        // 检查第三方应用主机是否存在或被绑定
        if (RcaEnum.HostSourceType.THIRD_PARTY == createAppPoolRequest.getHostSourceType()) {
            UUID[] hostIdArr = createAppPoolRequest.getHostIdArr();
            List<RcaHostDTO> hostDTOList = rcaHostAPI.findAllByIdIn(Arrays.asList(createAppPoolRequest.getHostIdArr()));
            if (CollectionUtils.isEmpty(hostDTOList) || hostDTOList.size() != hostIdArr.length) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_NOT_EXISTS);
            }

            for (RcaHostDTO rcaHostDTO : hostDTOList) {
                if (rcaHostDTO.getPoolId() != null) {
                    throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_HOST_BOUND_FAIL, rcaHostDTO.getName());
                }
            }
        } else {
            RcaAppPoolBaseDTO checkImageWithOtherConfig = new RcaAppPoolBaseDTO();
            checkImageWithOtherConfig.setPoolType(createAppPoolRequest.getPoolType());
            checkImageWithOtherConfig.setPlatformId(createAppPoolRequest.getPlatformId());
            checkImageWithOtherConfig.setNetworkId(createAppPoolRequest.getNetworkId());
            checkImageWithOtherConfig.setMainStrategyId(createAppPoolRequest.getMainStrategyId());
            checkImageWithOtherConfig.setClusterId(createAppPoolRequest.getClusterId());
            if (createAppPoolRequest.getCbbDeskSpecDTO() != null) {
                CbbDeskSpecDTO cbbDeskSpecDTO = createAppPoolRequest.getCbbDeskSpecDTO();
                checkImageWithOtherConfig.setStoragePoolId(cbbDeskSpecDTO.getSystemDiskStoragePoolId());
                checkImageWithOtherConfig.setPersonDiskStoragePoolId(cbbDeskSpecDTO.getPersonDiskStoragePoolId());
                VgpuInfoDTO vgpuInfoDTO = cbbDeskSpecDTO.getVgpuInfoDTO();
                if (vgpuInfoDTO != null) {
                    checkImageWithOtherConfig.setVgpuType(vgpuInfoDTO.getVgpuType());
                    checkImageWithOtherConfig.setVgpuExtraInfo(JSON.toJSONString(vgpuInfoDTO.getVgpuExtraInfo()));
                }
            }
            appPoolImageTemplateValidation.validate(checkImageWithOtherConfig, createAppPoolRequest.getImageTemplateId());
        }

        try {
            // 创建应用池基本信息
            rcaAppPoolAPI.createAppPool(createAppPoolRequest);
            optLogRecorder.saveOptLog(RcaBusinessKey.RCDC_RCA_APP_POOL_CREATE_SUCCESS, request.getName());

            // 添加数据权限
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolByName(createAppPoolRequest.getName());
            permissionHelper.saveAdminGroupPermission(sessionContext, appPoolBaseDTO.getId(), AdminDataPermissionType.APP_POOL);

            // 派生主机需要根据配置进行创建（预启动交给定时任务）
            List<RcaCreateCloudDesktopRequest> cloudDesktopRequestList = buildCreateDesktopRequest(createAppPoolRequest);
            if (CollectionUtils.isEmpty(cloudDesktopRequestList)) {
                // 不需要创建云桌面
                return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_APP_POOL_CREATE_SUCCESS
                        , new String[]{request.getName(), appPoolBaseDTO.getId().toString()});
            }

            // 将池置为创建中
            rcaAppPoolAPI.updateAppPoolState(appPoolBaseDTO.getId(), RcaEnum.PoolState.CREATING);

            Iterator<CreateAppPoolHostBatchTaskItem> iterator = cloudDesktopRequestList.stream()
                    .map(item -> CreateAppPoolHostBatchTaskItem.builder().itemId(UUID.randomUUID())
                            .itemName(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_ITEM, new String[]{})
                            .itemData(item).build()).iterator();
            int maxIndex = rcaHostDesktopMgmtAPI.getMaxIndexNumWhenAddDesktop(appPoolBaseDTO.getId());
            RcaCreateAppPoolHostBatchTaskHandler handler = new RcaCreateAppPoolHostBatchTaskHandler(iterator, auditLogAPI,
                    rcaAppPoolAPI, maxIndex);
            handler.setRcaAppPoolBaseDTO(appPoolBaseDTO);
            handler.setCreateRcaHostDesktopAPI(createRcaHostDesktopAPI);
            handler.setCbbDeskMgmtAPI(cbbDeskMgmtAPI);
            BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_TASK_DESC, createAppPoolRequest.getName())
                    .enablePerformanceMode(CREATE_POOL_HOST_TASK_ID, 30).enableParallel().registerHandler(handler).start();

            // 直接返回创建成功通知，派生桌面创建成功不作为判断条件
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_APP_POOL_CREATE_SUCCESS,
                    new String[] {request.getName(), appPoolBaseDTO.getId().toString()}, result);
        } catch (BusinessException e) {
            optLogRecorder.saveOptLog(RcaBusinessKey.RCDC_RCA_APP_POOL_CREATE_FAIL_WITH_REASON, createAppPoolRequest.getName(),
                    e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_POOL_CREATE_FAIL_WITH_REASON,
                    new String[] {createAppPoolRequest.getName(), e.getI18nMessage()});
        }
    }

    /**
     * 编辑融合应用池
     *
     * @param request request
     * @param builder builder
     * @param sessionContext sessionContext
     * @return CommonWebResponse
     * @throws BusinessException ex
     */
    @ApiOperation("编辑应用池")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑应用池"})})
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse edit(EditRcaAppPoolWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        validDataPermission(sessionContext.getUserId(), request.getId());

        EditAppPoolRequest editAppPoolRequest = new EditAppPoolRequest();
        BeanUtils.copyProperties(request, editAppPoolRequest);
        String oldPoolName = "";
        try {
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(request.getId());
            oldPoolName = appPoolBaseDTO.getName();
            Iterator<DefaultBatchTaskItem> iterator = Lists.newArrayList(request).stream()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                            .itemName(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_EDIT_TASK_ITEM, new String[]{})
                            .build())
                    .iterator();
            RcaEditAppPoolBatchTaskHandler handler = new RcaEditAppPoolBatchTaskHandler(iterator, auditLogAPI, rcaAppPoolAPI);
            handler.setEditAppPoolRequest(editAppPoolRequest);
            handler.setRcaHostAPI(rcaHostAPI);
            handler.setBaseAlarmAPI(baseAlarmAPI);
            BatchTaskSubmitResult batchTaskSubmitResult =
                    builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_EDIT_TASK_NAME, oldPoolName)
                            .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_EDIT_TASK_DESC, oldPoolName)
                            .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_FAIL, oldPoolName, e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_FAIL,
                    new String[]{oldPoolName, e.getI18nMessage()});
        }

    }

    /**
     * 校验应用池名是否有重名
     *
     * @param request 校验是否重名请求
     * @return res
     * @throws BusinessException ex
     */
    @ApiOperation(value = "校验应用池名是否有重名")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"校验应用池名是否有重名"})})
    @RequestMapping(value = "/checkNameDuplication", method = RequestMethod.POST)
    public DefaultWebResponse checkNameDuplication(RcaAppPoolNameWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");

        RcaAppPoolBaseDTO appPoolBaseInfo = rcaAppPoolAPI.getAppPoolByName(request.getName());
        boolean hasDuplication = false;
        if (appPoolBaseInfo != null) {
            hasDuplication = !appPoolBaseInfo.getId().equals(request.getId());
        }
        CheckDuplicationResponse duplicationResponse = new CheckDuplicationResponse(hasDuplication);

        return DefaultWebResponse.Builder.success(duplicationResponse);
    }

    /**
     * 删除融合应用池
     *
     * @param request 删除请求
     * @param sessionContext sessionContext
     * @param builder builder
     * @return res
     * @throws BusinessException ex
     */
    @ApiOperation("删除应用池")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"删除应用池"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse delete(DeleteRcaPoolRequest request, BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        Boolean shouldOnlyDeleteDataFromDb = request.getShouldOnlyDeleteDataFromDb();
        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);

        // 批量删除动态应用池
        Iterator<DefaultBatchTaskItem> iterator = Arrays.stream(request.getIdArr())
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(
                        RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_DELETE_TASK_ITEM, new String[]{prefix}).build()).iterator();

        RcaDeleteAppPoolBatchTaskHandler handler =
                new RcaDeleteAppPoolBatchTaskHandler(iterator, auditLogAPI, rcaAppPoolAPI, rcaHostAPI, sessionContext.getUserId());
        handler.setPermissionHelper(permissionHelper);
        handler.setRcaOneClientNotifyAPI(rcaOneClientNotifyAPI);
        handler.setRcaAppGroupAPI(rcaAppGroupAPI);
        handler.setShouldOnlyDeleteDataFromDb(shouldOnlyDeleteDataFromDb);

        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_DELETE_TASK_NAME, prefix)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_DELETE_TASK_DESC, prefix).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 获取应用池详情
     *
     * @param request request
     * @return 应用池详情
     * @throws BusinessException ex
     */
    @ApiOperation("应用池详情")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用池详情"})})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public DefaultWebResponse detail(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        return DefaultWebResponse.Builder.success(rcaAppPoolAPI.getDetail(request.getId()));
    }

    /**
     * 获取应用池详情
     *
     * @param request request
     * @return 应用池详情
     * @throws BusinessException ex
     */
    @ApiOperation("根据应用名查询应用池详情")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"根据应用名查询应用池详情"})})
    @RequestMapping(value = "/detailByName", method = RequestMethod.POST)
    public DefaultWebResponse detailByName(SearchAppPoolWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        return DefaultWebResponse.Builder.success(rcaAppPoolAPI.getDetailByName(request.getName()));
    }

    /**
     * 获取融合应用池总览
     *
     * @param request request
     * @param sessionContext session信息
     * @return 融合应用池总览
     * @throws BusinessException ex
     */
    @ApiOperation("应用池总览")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用池总览"})})
    @RequestMapping(value = "/overview", method = RequestMethod.POST)
    public DefaultWebResponse overview(AppPoolOverviewWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        AppPoolOverviewRequest appPoolOverviewRequest = new AppPoolOverviewRequest();
        BeanUtils.copyProperties(request, appPoolOverviewRequest);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] appPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.APP_POOL);
            if (ArrayUtils.isEmpty(appPoolIdArr)) {
                return DefaultWebResponse.Builder.success(new RcaAppPoolOverviewDTO());
            }
            appPoolOverviewRequest.setIdArr(appPoolIdArr);
        }

        return DefaultWebResponse.Builder.success(rcaAppPoolAPI.getOverview(appPoolOverviewRequest));
    }


    /**
     * 应用池列表查询
     *
     * @param pageQueryRequest 请求参数
     * @param sessionContext session信息
     * @return 主机列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用池列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用池列表"})})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public DefaultWebResponse list(PageQueryRequest pageQueryRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(pageQueryRequest, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] appPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.APP_POOL);
            if (ArrayUtils.isEmpty(appPoolIdArr)) {
                return DefaultWebResponse.Builder.success(new PageQueryResponse<RcaAppPoolBaseDTO>());
            }
            builder.in(PermissionHelper.DATA_PERMISSION_ID_KEY, appPoolIdArr);
        }

        final PageQueryResponse<RcaAppPoolBaseDTO> queryResponse = rcaAppPoolAPI.pageQueryAppPool(builder.build());
        return DefaultWebResponse.Builder.success(queryResponse);
    }

    /**
     * 镜像交付选择使用还原策略的应用池列表
     *
     * @param pageQueryRequest 请求参数
     * @param sessionContext session信息
     * @return 主机列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("镜像交付选择使用还原策略的应用池列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"镜像交付选择使用还原策略的应用池列表"})})
    @RequestMapping(value = "/imageVersionDelivery/list", method = RequestMethod.POST)
    public DefaultWebResponse imageVersioDeliveryList(PageQueryRequest pageQueryRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(pageQueryRequest, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        List<UUID> poolIdList = rcaMainStrategyAPI.getRecoverablePoolIdList();
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] appPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.APP_POOL);
            if (ArrayUtils.isEmpty(appPoolIdArr)) {
                return DefaultWebResponse.Builder.success(new PageQueryResponse<>());
            }
            if (!CollectionUtils.isEmpty(poolIdList)) {
                poolIdList = poolIdList.stream().filter(Arrays.asList(appPoolIdArr)::contains).collect(Collectors.toList());
            } else {
                poolIdList = Arrays.stream(appPoolIdArr).collect(Collectors.toList());
            }
        }
        if (!CollectionUtils.isEmpty(poolIdList)) {
            builder.in(PermissionHelper.DATA_PERMISSION_ID_KEY, poolIdList.toArray(new UUID[0]));
        }
        final PageQueryResponse<RcaAppPoolBaseDTO> queryResponse = rcaAppPoolAPI.pageQueryAppPool(builder.build());
        return DefaultWebResponse.Builder.success(queryResponse);
    }

    /**
     * 应用池列表(带分组信息)查询
     *
     * @param request        请求参数
     * @param sessionContext session信息
     * @return 主机列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用池列表(带分组信息)")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用池列表"})})
    @RequestMapping(value = "/listWithGroup", method = RequestMethod.POST)
    public DefaultWebResponse listWithGroup(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        AppPoolPageRequest pageReq = new AppPoolPageRequest(request);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] appPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.APP_POOL);
            if (ArrayUtils.isEmpty(appPoolIdArr)) {
                return DefaultWebResponse.Builder.success(new PageQueryResponse<>());
            }
            pageReq.appendCustomMatchEqual(new MatchEqual(PermissionHelper.DATA_PERMISSION_ID_KEY, appPoolIdArr));
        }

        DefaultPageResponse<RcaAppPoolBaseWithGroupDTO> defaultPageResponse = rcaAppPoolAPI.pageQueryWithGroup(pageReq,
                pageReq.getIsContainDefaultGroup());
        return DefaultWebResponse.Builder.success(defaultPageResponse);
    }

    /**
     * 变更应用池网络策略
     *
     * @param request        请求信息
     * @param optLogRecorder 日志
     * @param builder        builder
     * @param sessionContext session信息
     * @return 批任务
     * @throws BusinessException 业务异常
     */
    @ApiOperation("变更应用池网络")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"变更应用池网络"})})
    @RequestMapping(value = "/editNetwork", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editNetwork(EditNetworkWebRequest request, ProgrammaticOptLogRecorder optLogRecorder, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(optLogRecorder, "optLogRecorder must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        validDataPermission(sessionContext.getUserId(), request.getAppPoolId());
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(request.getAppPoolId());
        RcaEnum.PoolState oldPoolState = appPoolBaseDTO.getPoolState();
        if (RcaEnum.HostSourceType.THIRD_PARTY == appPoolBaseDTO.getHostSourceType()) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_NOT_DERIVE, appPoolBaseDTO.getName());
        }

        try {
            // 校验桌面池网络策略
            clusterAPI.validateVDIDesktopNetwork(appPoolBaseDTO.getClusterId(), request.getNetworkId());
            rcaAppPoolAPI.updateNetwork(request.getAppPoolId(), request.getNetworkId());

            List<RcaHostDTO> hostDTOList = rcaHostAPI.findAllByPoolIdIn(Lists.newArrayList(request.getAppPoolId()));
            if (CollectionUtils.isEmpty(hostDTOList)) {
                return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_NO_DESK, new String[]{});
            }
            // 更新应用池状态为编辑中
            rcaAppPoolAPI.updateAppPoolState(request.getAppPoolId(), RcaEnum.PoolState.UPDATING);

            List<UUID> desktopIdList = hostDTOList.stream().map(RcaHostDTO::getId).collect(Collectors.toList());
            final Iterator<DefaultBatchTaskItem> iterator = desktopIdList.stream().map(item -> DefaultBatchTaskItem.builder().itemId(item)
                    .itemName(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_TASK_ITEM, new String[]{}).build()).iterator();
            final UUID taskPoolId = UUID.nameUUIDFromBytes("editHostNetworkPool".getBytes());
            final RcaUpdateAppPoolNetworkBatchTaskHandler handler = new RcaUpdateAppPoolNetworkBatchTaskHandler(iterator);
            handler.setAppPoolId(request.getAppPoolId());
            handler.setNetworkId(request.getNetworkId());
            handler.setAuditLogAPI(auditLogAPI);
            handler.setRcaAppPoolAPI(rcaAppPoolAPI);
            handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
            handler.setAppPoolBaseDTO(appPoolBaseDTO);
            BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .enablePerformanceMode(taskPoolId, 30).start();
            return DefaultWebResponse.Builder.success(result);
        } catch (Exception e) {
            if (oldPoolState != null) {
                rcaAppPoolAPI.updateAppPoolState(request.getAppPoolId(), oldPoolState);
            }
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            LOGGER.error("应用池[{}]修改网络策略发生异常", request.getAppPoolId(), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_FAIL, appPoolBaseDTO.getName(), errorMsg);
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_FAIL,
                    new String[]{appPoolBaseDTO.getName(), errorMsg});
        }
    }

    /**
     * * 应用池修改镜像模板
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用池修改镜像模板")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用池修改镜像模板"})})
    @RequestMapping(value = "/editImage", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editImageTemplate(EditImageTemplateWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID appPoolId = request.getAppPoolId();

        validDataPermission(sessionContext.getUserId(), appPoolId);

        UUID imageTemplateId = request.getImageTemplateId();
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(appPoolId);
        RcaEnum.PoolState oldPoolState = appPoolBaseDTO.getPoolState();

        if (RcaEnum.HostSourceType.THIRD_PARTY == appPoolBaseDTO.getHostSourceType()) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_NOT_DERIVE, appPoolBaseDTO.getName());
        }

        try {
            appPoolImageTemplateValidation.validate(appPoolBaseDTO, imageTemplateId);
            rcaAppPoolAPI.updateImageTemplateId(appPoolId, imageTemplateId);

            List<RcaHostDTO> hostDTOList = rcaHostAPI.findAllByPoolIdIn(Lists.newArrayList(request.getAppPoolId()));
            if (CollectionUtils.isEmpty(hostDTOList)) {
                return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_NO_DESK,
                        new String[]{appPoolBaseDTO.getName()});
            }
            // 更新应用池状态为编辑中
            rcaAppPoolAPI.updateAppPoolState(request.getAppPoolId(), RcaEnum.PoolState.UPDATING);

            List<UUID> desktopIdList = hostDTOList.stream().map(RcaHostDTO::getId).collect(Collectors.toList());
            final Iterator<DefaultBatchTaskItem> iterator = desktopIdList.stream().map(item -> DefaultBatchTaskItem.builder().itemId(item)
                    .itemName(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_ITEM, new String[]{}).build()).iterator();
            final UUID taskPoolId = UUID.nameUUIDFromBytes("editHostImagePool".getBytes());
            final RcaUpdateAppPoolImageBatchTaskHandler handler = buildUpdateAppPoolImageHandler(request, iterator);
            BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .enablePerformanceMode(taskPoolId, 30).start();
            return DefaultWebResponse.Builder.success(result);
        } catch (Exception e) {
            if (oldPoolState != null) {
                rcaAppPoolAPI.updateAppPoolState(request.getAppPoolId(), oldPoolState);
            }
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            LOGGER.error("应用池[{}]修改镜像模板发生异常", request.getAppPoolId(), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_FAIL, appPoolBaseDTO.getName(), errorMsg);
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_FAIL,
                    new String[]{appPoolBaseDTO.getName(), errorMsg});
        }
    }

    /**
     * * 应用池修改规格
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用池修改规格")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用池修改规格"})})
    @RequestMapping(value = "/editSpec", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "editAppPoolSpecValidate")
    @EnableAuthority
    public DefaultWebResponse editAppPoolSpec(EditSpecWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID appPoolId = request.getAppPoolId();

        validDataPermission(sessionContext.getUserId(), appPoolId);

        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(appPoolId);
        RcaEnum.PoolState oldPoolState = appPoolBaseDTO.getPoolState();

        if (RcaEnum.HostSourceType.THIRD_PARTY == appPoolBaseDTO.getHostSourceType()) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_NOT_DERIVE, appPoolBaseDTO.getName());
        }

        EditAppPoolSpecRequest editAppPoolSpecRequest = new EditAppPoolSpecRequest();
        CbbDeskSpecDTO cbbDeskSpecDTO = null;
        try {
            cbbDeskSpecDTO = deskSpecAPI.buildCbbDeskSpec(appPoolBaseDTO.getClusterId(), request);
            BeanUtils.copyProperties(cbbDeskSpecDTO, editAppPoolSpecRequest);
            editAppPoolSpecRequest.setAppPoolId(request.getAppPoolId());
            VgpuInfoDTO vgpuInfoDTO = editAppPoolSpecRequest.getVgpuInfoDTO();
            if (vgpuInfoDTO == null) {
                vgpuInfoDTO = new VgpuInfoDTO();
                editAppPoolSpecRequest.setVgpuInfoDTO(vgpuInfoDTO);
            }
        } catch (BusinessException e) {
            LOGGER.error("应用池[{}]修改规格发生异常", request.getAppPoolId(), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_FAIL, appPoolBaseDTO.getName(), e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_FAIL,
                    new String[]{appPoolBaseDTO.getName(), e.getI18nMessage()});
        }

        try {
            // 校验vgpu 和 镜像的兼容性
            checkVgpuInfo(appPoolBaseDTO, cbbDeskSpecDTO, editAppPoolSpecRequest.getVgpuInfoDTO());
            rcaAppPoolAPI.updateSpec(appPoolId, editAppPoolSpecRequest);
            List<RcaHostDTO> hostDTOList = rcaHostAPI.findAllByPoolIdIn(Lists.newArrayList(request.getAppPoolId()));
            if (CollectionUtils.isEmpty(hostDTOList)) {
                LOGGER.info("主机列表为空，跳过处理");
                return DefaultWebResponse.Builder.success(RcoBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
            }
            List<UUID> hostIdList = hostDTOList.stream().map(RcaHostDTO::getId).collect(Collectors.toList());
            List<RcaHostDesktopDTO> rcaHostDesktopDTOList = rcaHostDesktopMgmtAPI.listByHostIdIn(hostIdList);
            List<UUID> desktopIdList = rcaHostDesktopDTOList.stream().filter(item -> !item.getEnableCustom()).
                    map(RcaHostDesktopDTO::getCbbId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(desktopIdList)) {
                LOGGER.info("非自定义规格主机列表为空，跳过处理");
                return DefaultWebResponse.Builder.success(RcoBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
            }

            // 更新应用池状态为编辑中
            rcaAppPoolAPI.updateAppPoolState(request.getAppPoolId(), RcaEnum.PoolState.UPDATING);

            final Iterator<DefaultBatchTaskItem> iterator = desktopIdList.stream().map(item -> DefaultBatchTaskItem.builder().itemId(item)
                    .itemName(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_ITEM, new String[]{}).build()).iterator();
            final UUID taskPoolId = UUID.nameUUIDFromBytes("editAppPoolSpec".getBytes());
            final RcaUpdateAppPoolSpecBatchTaskHandler handler = new RcaUpdateAppPoolSpecBatchTaskHandler(iterator, auditLogAPI,
                    rcaAppPoolAPI, editAppPoolSpecRequest);
            handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
            handler.setCbbVDIDeskDiskAPI(cbbVDIDeskDiskAPI);
            handler.setCbbVDIDeskStrategyMgmtAPI(cbbVDIDeskStrategyMgmtAPI);
            handler.setAppPoolBaseDTO(appPoolBaseDTO);
            handler.setDeskSpecAPI(deskSpecAPI);
            BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .enablePerformanceMode(taskPoolId, 30).start();
            return DefaultWebResponse.Builder.success(result);
        } catch (Exception e) {
            if (oldPoolState != null) {
                rcaAppPoolAPI.updateAppPoolState(request.getAppPoolId(), oldPoolState);
            }
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            LOGGER.error("应用池[{}]修改规格发生异常", request.getAppPoolId(), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_FAIL, appPoolBaseDTO.getName(), errorMsg);
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_FAIL, new String[]{appPoolBaseDTO.getName(), errorMsg});
        }
    }

    private void checkVgpuInfo(RcaAppPoolBaseDTO appPoolBaseDTO, CbbDeskSpecDTO cbbDeskSpecDTO, VgpuInfoDTO vgpuInfoDTO) throws BusinessException {
        RcaAppPoolBaseDTO checkImageWithOtherConfig = new RcaAppPoolBaseDTO();
        checkImageWithOtherConfig.setPoolType(appPoolBaseDTO.getPoolType());
        checkImageWithOtherConfig.setPlatformId(appPoolBaseDTO.getPlatformId());
        checkImageWithOtherConfig.setNetworkId(appPoolBaseDTO.getNetworkId());
        checkImageWithOtherConfig.setMainStrategyId(appPoolBaseDTO.getMainStrategyId());
        checkImageWithOtherConfig.setClusterId(appPoolBaseDTO.getClusterId());
        checkImageWithOtherConfig.setStoragePoolId(cbbDeskSpecDTO.getSystemDiskStoragePoolId());
        checkImageWithOtherConfig.setPersonDiskStoragePoolId(cbbDeskSpecDTO.getPersonDiskStoragePoolId());
        checkImageWithOtherConfig.setVgpuType(vgpuInfoDTO.getVgpuType());
        checkImageWithOtherConfig.setVgpuExtraInfo(JSON.toJSONString(vgpuInfoDTO.getVgpuExtraInfo()));
        appPoolImageTemplateValidation.validate(checkImageWithOtherConfig, appPoolBaseDTO.getImageTemplateId());
    }

    /**
     * 应用池添加云主机
     *
     * @param request 请求参数
     * @param builder 批任务
     * @param sessionContext session信息
     * @return 批任务
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用池添加云主机")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用池添加云主机"})})
    @RequestMapping(value = "/addHost", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse addHost(AddAppPoolHostWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        validDataPermission(sessionContext.getUserId(), request.getId());

        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(request.getId());

        String poolName = appPoolBaseDTO.getName();
        RcaEnum.PoolState oldPoolState = appPoolBaseDTO.getPoolState();
        try {
            checkBeforeAddDesktop(appPoolBaseDTO, request.getAddNum());
            // 更新应用池状态为编辑中
            rcaAppPoolAPI.updateAppPoolState(request.getId(), RcaEnum.PoolState.UPDATING);

            List<RcaCreateCloudDesktopRequest> cloudDesktopRequestList = buildAddDesktopRequest(appPoolBaseDTO, request.getAddNum());
            if (CollectionUtils.isEmpty(cloudDesktopRequestList)) {
                return DefaultWebResponse.Builder.success();
            }

            Iterator<CreateAppPoolHostBatchTaskItem> iterator = cloudDesktopRequestList.stream()
                    .map(item -> CreateAppPoolHostBatchTaskItem.builder().itemId(UUID.randomUUID()).itemName(
                                    RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_ITEM, new String[]{})
                            .itemData(item).build()).iterator();
            int maxIndex = rcaHostDesktopMgmtAPI.getMaxIndexNumWhenAddDesktop(appPoolBaseDTO.getId());
            RcaAddPoolImageHostBatchTaskHandler handler = new RcaAddPoolImageHostBatchTaskHandler(iterator,
                    cbbDeskMgmtAPI, rcaAppPoolAPI, maxIndex);
            handler.setRcaAppPoolBaseDTO(appPoolBaseDTO);
            handler.setAuditLogAPI(auditLogAPI);
            handler.setCreateRcaHostDesktopAPI(createRcaHostDesktopAPI);
            BatchTaskSubmitResult batchTaskSubmitResult =
                    builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_TASK_NAME)
                            .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_CREATE_HOST_TASK_DESC, appPoolBaseDTO.getName())
                            .enablePerformanceMode(CREATE_POOL_HOST_TASK_ID, 30).enableParallel()
                            .registerHandler(handler).start();
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_ADD_HOST_SUCCESS_LOG, poolName);
            return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                // 数量超过1000，提示最大可剩余添加桌面数量
                if (RcaBusinessKey.RCDC_RCA_APP_POOL_HOST_NUM_OVER_MAX.equals(businessException.getKey())) {
                    return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_POOL_HOST_NUM_OVER_MAX, businessException.getArgArr());
                }
                errorMsg = businessException.getI18nMessage();
            }
            LOGGER.error("应用池添加云主机发生异常，异常原因：", e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_ADD_HOST_FAIL_LOG, poolName, errorMsg);
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_POOL_ADD_HOST_FAIL_LOG, new String[]{poolName, errorMsg});
        } finally {
            if (oldPoolState != null) {
                rcaAppPoolAPI.updateAppPoolState(request.getId(), oldPoolState);
            }
        }
    }


    /**
     * 第三方应用主机解绑
     *
     * @param request 需要解绑的主机id列表
     * @param builder builder
     * @param sessionContext session信息
     * @return 返回结果
     * @throws BusinessException 异常
     */
    @ApiOperation("第三方应用主机解绑")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"第三方应用主机解绑"})})
    @RequestMapping(value = "/unbindThirdPartyHost", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse unbindThirdPartyHost(BindRcaPoolThirdPartyHostWebRequest request, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder不能为null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        validDataPermission(sessionContext.getUserId(), request.getPoolId());
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(request.getPoolId());
        if (appPoolBaseDTO.getHostSourceType().equals(RcaEnum.HostSourceType.VDI)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_UNBIND_HOST_FAIL_BY_SOURCE_TYPE_ERROR);
        }

        try {
            Iterator<DefaultBatchTaskItem> iterator = request.getHostIdList().stream()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(
                            RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_UNBIND_HOST_TASK_ITEM, new String[]{}).build()).iterator();
            RcaPoolUnbindThirdPartyHostBatchTaskHandler handler = new RcaPoolUnbindThirdPartyHostBatchTaskHandler(iterator,
                    auditLogAPI, rcaAppPoolAPI, rcaHostAPI, request.getPoolId());
            BatchTaskSubmitResult batchTaskSubmitResult =
                    builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_UNBIND_HOST_TASK_NAME)
                            .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_UNBIND_HOST_TASK_DESC, appPoolBaseDTO.getName())
                            .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_UNBIND_HOST_FAIL, appPoolBaseDTO.getName(), e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_POOL_UNBIND_HOST_FAIL,
                    new String[]{appPoolBaseDTO.getName(), e.getI18nMessage()});
        }
    }

    /**
     * 第三方应用主机绑定
     *
     * @param request 需要绑定的主机id列表
     * @param builder builder
     * @param sessionContext session信息
     * @return 返回结果
     * @throws BusinessException 异常
     */
    @ApiOperation("第三方应用主机绑定")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"第三方应用主机绑定"})})
    @RequestMapping(value = "/bindThirdPartyHost", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse bindThirdPartyHost(BindRcaPoolThirdPartyHostWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder不能为null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        validDataPermission(sessionContext.getUserId(), request.getPoolId());

        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(request.getPoolId());
        if (appPoolBaseDTO.getHostSourceType().equals(RcaEnum.HostSourceType.VDI)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_UNBIND_HOST_FAIL_BY_SOURCE_TYPE_ERROR);
        }

        try {
            Iterator<DefaultBatchTaskItem> iterator = request.getHostIdList().stream()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(
                            RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_BIND_HOST_TASK_ITEM, new String[]{}).build()).iterator();
            RcaPoolBindThirdPartyHostBatchTaskHandler handler = new RcaPoolBindThirdPartyHostBatchTaskHandler(iterator,
                    auditLogAPI, rcaAppPoolAPI, rcaHostAPI, request.getPoolId());
            BatchTaskSubmitResult batchTaskSubmitResult =
                    builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_BIND_HOST_TASK_NAME)
                            .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_BIND_HOST_TASK_DESC, appPoolBaseDTO.getName())
                            .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BIND_HOST_FAIL, appPoolBaseDTO.getName(), e.getI18nMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_RCA_APP_POOL_BIND_HOST_FAIL,
                    new String[]{appPoolBaseDTO.getName(), e.getI18nMessage()});
        }
    }

    /**
     * 开启维护模式
     *
     * @param request request
     * @param builder builder
     * @param sessionContext session信息
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("开启维护模式")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"开启维护模式"})})
    @RequestMapping(value = "/openMaintenance", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse openMaintenance(IdArrWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notNull(idArr, "idArr can not be null");

        String taskName = LocaleI18nResolver.resolve(RcaBusinessKey.RCDC_APP_POOL_OPEN_MAINTENANCE);
        List<DefaultBatchTaskItem> taskList = Arrays.stream(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).collect(Collectors.toList());

        ChangeAppPoolMaintenanceBatchHandler handler =
                new ChangeAppPoolMaintenanceBatchHandler(true, taskList, rcaAppPoolAPI, auditLogAPI, sessionContext.getUserId());
        handler.setPermissionHelper(permissionHelper);

        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_APP_POOL_OPEN_MAINTENANCE)
                .setTaskDesc(RcaBusinessKey.RCDC_APP_POOL_OPEN_MAINTENANCE_TASK_DESC).setUniqueId(request.getIdArr()[0]).enableParallel()
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 关闭维护模式
     *
     * @param request request
     * @param builder builder
     * @param sessionContext session信息
     * @return response
     * @throws BusinessException ex
     */
    @ApiOperation("关闭维护模式")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"关闭维护模式"})})
    @RequestMapping(value = "/closeMaintenance", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse closeMaintenance(IdArrWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID[] idArr = request.getIdArr();
        Assert.notNull(idArr, "idArr can not be null");

        String taskName = LocaleI18nResolver.resolve(RcaBusinessKey.RCDC_APP_POOL_CLOSE_MAINTENANCE);
        List<DefaultBatchTaskItem> taskList = Arrays.stream(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).collect(Collectors.toList());

        ChangeAppPoolMaintenanceBatchHandler handler =
                new ChangeAppPoolMaintenanceBatchHandler(false, taskList, rcaAppPoolAPI, auditLogAPI, sessionContext.getUserId());
        handler.setPermissionHelper(permissionHelper);
        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_APP_POOL_CLOSE_MAINTENANCE)
                .setTaskDesc(RcaBusinessKey.RCDC_APP_POOL_CLOSE_MAINTENANCE_TASK_DESC)
                .setUniqueId(request.getIdArr()[0]).enableParallel().registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * * 修改云应用策略
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("修改云应用策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"修改云应用策略"})})
    @RequestMapping(value = "/editMainStrategy", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editMainStrategy(EditPoolStrategyWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID appPoolId = request.getAppPoolId();
        validDataPermission(sessionContext.getUserId(), appPoolId);
        UUID strategyId = request.getStrategyId();
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(appPoolId);
        RcaEnum.PoolState oldPoolState = appPoolBaseDTO.getPoolState();

        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        relationshipDTO.setStrategyId(strategyId);
        RcaMainStrategyDTO mainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);
        if (mainStrategyDTO.getHostSourceType() == RcaEnum.HostSourceType.VDI) {
            CbbDeskStrategyDTO strategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(mainStrategyDTO.getDeskStrategyId());
            if (RcaEnum.PoolType.DYNAMIC == appPoolBaseDTO.getPoolType() && CbbCloudDeskPattern.PERSONAL == strategyDTO.getPattern()) {
                throw new BusinessException(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_FAIL_BY_STRATEGY_PATTERN);
            }
        }

        RcaStrategyRelationshipDTO relationshipDTO2 = new RcaStrategyRelationshipDTO();
        relationshipDTO2.setPoolId(appPoolId);
        RcaMainStrategyDTO currentMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO2);
        if (mainStrategyDTO.getStrategyId().equals(currentMainStrategyDTO.getStrategyId())) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_SUCCESS, appPoolBaseDTO.getName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_SUCCESS,
                    new String[]{appPoolBaseDTO.getName()});
        }

        // 若第三方应用池，若数据保存路径重定向从关 -> 开，需要检测池里面是否所有主机都存在已发布的应用
        if (mainStrategyDTO.getHostSourceType() == RcaEnum.HostSourceType.THIRD_PARTY) {
            checkHasRedirectDiskError(appPoolBaseDTO, currentMainStrategyDTO, mainStrategyDTO);
        }

        try {
            rcaAppPoolAPI.updateMainStrategyId(appPoolId, strategyId);

            rcaMainStrategyWatermarkAPI.handleNotifyWatermarkConfig(mainStrategyDTO);

            if (RcaEnum.HostSourceType.THIRD_PARTY == appPoolBaseDTO.getHostSourceType()) {
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_SUCCESS, appPoolBaseDTO.getName());
                return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_SUCCESS,
                        new String[]{appPoolBaseDTO.getName()});
            }

            List<RcaHostDTO> hostDTOList = rcaHostAPI.findAllByPoolIdIn(Lists.newArrayList(request.getAppPoolId()));
            if (CollectionUtils.isEmpty(hostDTOList)) {
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_SUCCESS, appPoolBaseDTO.getName());
                return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_SUCCESS,
                        new String[]{appPoolBaseDTO.getName()});
            }

            // 更新应用池状态为编辑中
            rcaAppPoolAPI.updateAppPoolState(request.getAppPoolId(), RcaEnum.PoolState.UPDATING);
            List<UUID> desktopIdList = hostDTOList.stream().map(RcaHostDTO::getId).collect(Collectors.toList());
            final Iterator<DefaultBatchTaskItem> iterator = desktopIdList.stream().map(item -> DefaultBatchTaskItem.builder().itemId(item)
                    .itemName(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_ITEM, new String[]{}).build()).iterator();
            CbbDeskStrategyDTO strategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(mainStrategyDTO.getDeskStrategyId());
            final RcaUpdateAppPoolMainStrategyBatchTaskHandler handler = new RcaUpdateAppPoolMainStrategyBatchTaskHandler(
                    iterator, strategyDTO.getId(), auditLogAPI);
            handler.setCloudDesktopMgmtAPI(cloudDesktopMgmtAPI);
            handler.setAppPoolBaseDTO(appPoolBaseDTO);
            handler.setRcaAppPoolAPI(rcaAppPoolAPI);
            BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_NAME)
                    .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        } catch (Exception e) {
            if (oldPoolState != null) {
                rcaAppPoolAPI.updateAppPoolState(request.getAppPoolId(), oldPoolState);
            }
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }

            LOGGER.error("应用池[{}]修改云应用策略发生异常", request.getAppPoolId(), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_FAIL, appPoolBaseDTO.getName(), e.getMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_APP_POOL_EDIT_MAIN_STRATEGY_FAIL,
                    new String[]{appPoolBaseDTO.getName(), errorMsg});
        }
    }

    private void checkHasRedirectDiskError(RcaAppPoolBaseDTO appPoolBaseDTO, RcaMainStrategyDTO currentStrategyDTO, RcaMainStrategyDTO toStrategyDTO)
            throws BusinessException {
        Boolean isCurrentEnable = currentStrategyDTO.getAppHostDiskConfig().getAppDataPathRedirect().getEnable();
        Boolean isToEnable = toStrategyDTO.getAppHostDiskConfig().getAppDataPathRedirect().getEnable();

        if (appPoolBaseDTO.getAppBalanced()) {
            return;
        }

        if (!(Boolean.FALSE.equals(isCurrentEnable) && Boolean.TRUE.equals(isToEnable))) {
            return;
        }
        List<String> hostNameList = rcaHostAppAPI.getAppUnBalancedPoolHostNameList(appPoolBaseDTO.getId());
        String message = hostNameList.stream().collect(Collectors.joining(Constants.DELIMITER));
        throw new BusinessException(RcaBusinessKey.RCDC_RCA_POOL_CHANGE_STRATEGY_REDIRECT_DISK_ERROR_MSG_HOST, message);
    }

    /**
     * 修改云应用外设策略
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("修改云应用外设策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"修改云应用外设策略"})})
    @RequestMapping(value = "/editPeripheralStrategy", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editPeripheralStrategy(EditPoolStrategyWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID appPoolId = request.getAppPoolId();
        validDataPermission(sessionContext.getUserId(), appPoolId);
        UUID strategyId = request.getStrategyId();
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(appPoolId);

        try {
            rcaAppPoolAPI.updatePeripheralStrategyId(appPoolId, strategyId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_APP_POOL_EDIT_PERIPHERAL_STRATEGY_SUCCESS, appPoolBaseDTO.getName());
            return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_STRATEGY_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("应用池[{}]修改云应用外设策略发生异常", request.getAppPoolId(), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_APP_POOL_EDIT_PERIPHERAL_STRATEGY_FAIL, appPoolBaseDTO.getName(), e.getMessage());
            return DefaultWebResponse.Builder.fail(RcaBusinessKey.RCDC_APP_POOL_EDIT_PERIPHERAL_STRATEGY_FAIL,
                    new String[]{appPoolBaseDTO.getName(), e.getMessage()});
        }
    }

    /**
     * 向导批量分配关系
     *
     * @param request        分配情况
     * @param builder        builder
     * @param sessionContext sessionContext
     * @return 响应消息
     * @throws BusinessException BusinessException
     */
    @ApiOperation("向导批量分配关系")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"向导批量分配关系"})})
    @RequestMapping(value = "/batchUpdateBind", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse batchUpdatePoolBindObject(RcaGroupBindWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder cannot be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");

        RcaUpdateAppGroupBindRequest groupBindRequest = new RcaUpdateAppGroupBindRequest();
        BeanUtils.copyProperties(request, groupBindRequest);
        validDataPermission(sessionContext.getUserId(), request.getPoolId());
        // 获取管理员的数据权限，超管就是null
        UUID[] userGroupIdArr = new UUID[0];
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            userGroupIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.USER_GROUP);
        }

        final Iterator<RcaCreateGroupWithBindBatchTaskItem> iterator = request.getBindInfoList().stream()
                .map(bindInfo -> RcaCreateGroupWithBindBatchTaskItem.builder().itemId(UUID.randomUUID())
                        .itemName(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_UPDATE_BIND_OBJ_TASK_ITEM, new String[]{})
                        .itemPoolId(request.getPoolId()).itemBind(bindInfo).build()).iterator();
        RcaCreateGroupAndBindBatchHandler handler = new RcaCreateGroupAndBindBatchHandler(userGroupIdArr, iterator);
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
     * 编辑用户自定义策略
     *
     * @param request        请求参数
     * @param sessionContext sessionContext
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑用户自定义策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑用户自定义策略"})})
    @RequestMapping(value = "/editUserCustomStrategy", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editUserCustomStrategy(RcaEditUserCustomStrategyWebRequest request,
                                                     SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");

        rcaUserCustomStrategyAPI.batchUpdateAuthorityOperationConfig(request.getPoolId(), request.getUserIdList()
                 , request.getUserAuthorityOperationConfig());

        return DefaultWebResponse.Builder.success();
    }

    /**
     * 编辑用户自定义策略
     *
     * @param request        请求参数
     * @param sessionContext sessionContext
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查看用户自定义策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"查看用户自定义策略"})})
    @RequestMapping(value = "/getUserCustomStrategy", method = RequestMethod.POST)
    public DefaultWebResponse getUserCustomStrategy(RcaGetUserCustomStrategyWebRequest request,
                                                     SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");

        RcaUserCustomStrategyDTO userCustomStrategyDetail = rcaUserCustomStrategyAPI.getUserCustomStrategyDetail(
                request.getPoolId(), request.getUserId());
        return DefaultWebResponse.Builder.success(userCustomStrategyDetail);
    }

    private void validDataPermission(UUID adminId, UUID id) throws BusinessException {
        if (!permissionHelper.isAllGroupPermission(adminId)
                && !permissionHelper.hasDataPermission(adminId, String.valueOf(id), AdminDataPermissionType.APP_POOL)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCO_RCA_HAS_NO_DATA_PERMISSION);
        }
    }

    private void checkBeforeAddDesktop(RcaAppPoolBaseDTO appPoolBaseDTO, int addNum) throws BusinessException {
        if (appPoolBaseDTO.getHostNum() >= SINGLE_POOL_MAX_HOST_NUM
                || addNum > SINGLE_POOL_MAX_HOST_NUM - appPoolBaseDTO.getHostNum()) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_HOST_NUM_OVER_MAX,
                    String.valueOf(SINGLE_POOL_MAX_HOST_NUM),
                    String.valueOf(SINGLE_POOL_MAX_HOST_NUM - appPoolBaseDTO.getHostNum()));
        }
    }

    private RcaUpdateAppPoolImageBatchTaskHandler buildUpdateAppPoolImageHandler(EditImageTemplateWebRequest request,
                                                                                 Iterator<DefaultBatchTaskItem> iterator) throws BusinessException {
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(request.getAppPoolId());

        final RcaUpdateAppPoolImageBatchTaskHandler handler = new RcaUpdateAppPoolImageBatchTaskHandler(iterator);
        handler.setAppPoolBaseDTO(appPoolBaseDTO);
        handler.setTemplateImageId(request.getImageTemplateId());
        handler.setRcaAppPoolAPI(rcaAppPoolAPI);
        handler.setCbbDeskMgmtAPI(cbbDeskMgmtAPI);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        handler.setRcaHostAppAPI(rcaHostAppAPI);
        return handler;
    }

    private List<RcaCreateCloudDesktopRequest> buildCreateDesktopRequest(CreateAppPoolRequest createAppPoolRequest) throws BusinessException {
        List<RcaCreateCloudDesktopRequest> desktopRequestList = new ArrayList<>();
        if (RcaEnum.HostSourceType.VDI != createAppPoolRequest.getHostSourceType()) {
            return desktopRequestList;
        }

        RcaStrategyRelationshipDTO rcaStrategyRelationshipDTO = new RcaStrategyRelationshipDTO();
        rcaStrategyRelationshipDTO.setStrategyId(createAppPoolRequest.getMainStrategyId());
        RcaMainStrategyDTO strategyConfigDetail = rcaMainStrategyAPI.getStrategyConfigDetail(rcaStrategyRelationshipDTO);
        for (int i = 0; i < createAppPoolRequest.getHostNum(); i++) {
            RcaCreateCloudDesktopRequest createDesktopRequest = new RcaCreateCloudDesktopRequest();
            // 名称在批任务中计算
            createDesktopRequest.setPlatformId(createAppPoolRequest.getPlatformId());
            createDesktopRequest.setPoolId(createDesktopRequest.getPoolId());
            createDesktopRequest.setDesktopImageId(createAppPoolRequest.getImageTemplateId());
            createDesktopRequest.setNetworkId(createAppPoolRequest.getNetworkId());
            createDesktopRequest.setStrategyId(strategyConfigDetail.getDeskStrategyId());
            createDesktopRequest.setUserType(IacUserTypeEnum.NORMAL);
            createDesktopRequest.setClusterId(createAppPoolRequest.getClusterId());
            // 单独拆出来的桌面规格
            createDesktopRequest.setCbbDeskSpecDTO(createAppPoolRequest.getCbbDeskSpecDTO());
            desktopRequestList.add(createDesktopRequest);
        }

        return desktopRequestList;
    }

    private List<RcaCreateCloudDesktopRequest> buildAddDesktopRequest(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, int addNum) throws BusinessException {
        List<RcaCreateCloudDesktopRequest> desktopRequestList = new ArrayList<>();
        RcaStrategyRelationshipDTO rcaStrategyRelationshipDTO = new RcaStrategyRelationshipDTO();
        rcaStrategyRelationshipDTO.setStrategyId(rcaAppPoolBaseDTO.getMainStrategyId());
        RcaMainStrategyDTO strategyConfigDetail = rcaMainStrategyAPI.getStrategyConfigDetail(rcaStrategyRelationshipDTO);
        CbbDeskSpecDTO cbbDeskSpecDTO = new CbbDeskSpecDTO();
        cbbDeskSpecDTO.setCpu(rcaAppPoolBaseDTO.getCpu());
        cbbDeskSpecDTO.setMemory(CapacityUnitUtils.gb2Mb(rcaAppPoolBaseDTO.getMemory()));
        cbbDeskSpecDTO.setSystemSize(rcaAppPoolBaseDTO.getSystemSize());
        cbbDeskSpecDTO.setPersonSize(rcaAppPoolBaseDTO.getPersonalConfigDiskSize());
        cbbDeskSpecDTO.setSystemDiskStoragePoolId(rcaAppPoolBaseDTO.getStoragePoolId());
        cbbDeskSpecDTO.setPersonDiskStoragePoolId(rcaAppPoolBaseDTO.getPersonDiskStoragePoolId());
        if (Objects.nonNull(rcaAppPoolBaseDTO.getVgpuExtraInfo())) {
            VgpuExtraInfoSupport vgpuExtraInfoSupport = JSON.parseObject(rcaAppPoolBaseDTO.getVgpuExtraInfo(), VgpuExtraInfo.class);
            VgpuInfoDTO vgpuInfoDTO =
                    deskSpecAPI.checkAndBuildVGpuInfo(rcaAppPoolBaseDTO.getClusterId(), rcaAppPoolBaseDTO.getVgpuType(), vgpuExtraInfoSupport);
            cbbDeskSpecDTO.setVgpuInfoDTO(vgpuInfoDTO);
        }

        for (int i = 0; i < addNum; i++) {
            RcaCreateCloudDesktopRequest createDesktopRequest = new RcaCreateCloudDesktopRequest();
            // 名称在批任务中计算
            createDesktopRequest.setPoolId(createDesktopRequest.getPoolId());
            createDesktopRequest.setPlatformId(rcaAppPoolBaseDTO.getPlatformId());
            createDesktopRequest.setDesktopImageId(rcaAppPoolBaseDTO.getImageTemplateId());
            createDesktopRequest.setNetworkId(rcaAppPoolBaseDTO.getNetworkId());
            createDesktopRequest.setStrategyId(strategyConfigDetail.getDeskStrategyId());
            createDesktopRequest.setUserType(IacUserTypeEnum.NORMAL);
            createDesktopRequest.setCbbDeskSpecDTO(cbbDeskSpecDTO);
            desktopRequestList.add(createDesktopRequest);
        }

        return desktopRequestList;
    }

    /**
     * 交付应用镜像版本
     *
     * @param request        请求对象
     * @param builder        批任务建造者
     * @param sessionContext session上下文
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("交付应用镜像版本")
    @ApiVersions({@ApiVersion(value = Version.V3_2_156)})
    @EnableAuthority
    @RequestMapping(value = "/appImage/delivery", method = RequestMethod.POST)
    public CommonWebResponse<?> delivery(DeliveryAppImageTemplateVersionWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        UUID[] rcaPoolIdArr = Optional.ofNullable(request.getRcaPoolIdArr()).orElse(new UUID[0]);

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getImageVersionId());
        Assert.notNull(imageTemplateDetail.getRootImageId(), "imageTemplateDetail.getRootImageId() can not be null");

        generalPermissionHelper.checkPermission(sessionContext, imageTemplateDetail.getRootImageId(), AdminDataPermissionType.IMAGE);

        UUID rootImageId = imageTemplateDetail.getRootImageId();
        UUID imageVersionId = imageTemplateDetail.getId();
        String imageVersionName = imageTemplateDetail.getImageName();

        if (request.getDeliveryObjectType() == DeliveryObjectType.PART) {
            if (ObjectUtils.isEmpty(rcaPoolIdArr)) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCO_IMAGE_VERSION_DELIVER_POOL_ID_IS_EMPTY);
            }
        } else {
            // 获取所有应用池池列表
            rcaPoolIdArr = getRcaPoolIdArr(rootImageId, imageVersionId);
            if (ObjectUtils.isEmpty(rcaPoolIdArr)) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCO_IMAGE_VERSION_DELIVERY_NOT_BIND_RCA_POOL, imageTemplateDetail.getRootImageName());
            }
        }

        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(rcaPoolIdArr)
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_ITEM_NAME)).build())
                        .iterator();
        RcaDeliveryAppPoolImageBatchTaskHandler handler = buildDeliveryAppPoolImageHandler(request, imageVersionName, iterator);
        handler.setIdentityId(sessionContext.getUserId().toString());

        BatchTaskSubmitResult result = builder.setTaskName(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_TASK_NAME)
                .setTaskDesc(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_TASK_DESC)
                .enableParallel()
                .registerHandler(handler)
                .start();

        return CommonWebResponse.success(result);
    }

    private UUID[] getRcaPoolIdArr(UUID rootImageId, UUID imageVersionId) throws BusinessException {
        UUID[] deskPoolIdArr;
        // 获取所有关联桌面池列表
        List<RcaAppPoolBaseDTO> desktopPoolBasicDTOList = getRcaPoolList(rootImageId, imageVersionId);
        deskPoolIdArr = desktopPoolBasicDTOList.stream() //
                .filter(dto -> {
                    RcaStrategyRelationshipDTO strategyRelationshipDTO = new RcaStrategyRelationshipDTO();
                    strategyRelationshipDTO.setPoolId(dto.getId());
                    try {
                        RcaMainStrategyDTO rcaMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(strategyRelationshipDTO);
                        return rcaMainStrategyDTO.getDesktopStrategyConfig().getPattern() == CbbCloudDeskPattern.RECOVERABLE;
                    } catch (BusinessException e) {
                        LOGGER.error("获取策略详情异常, 过滤该池信息,e={}", e);
                        return false;
                    }
                })
                .map(RcaAppPoolBaseDTO::getId) //
                .toArray(UUID[]::new);
        return deskPoolIdArr;
    }

    private List<RcaAppPoolBaseDTO> getRcaPoolList(UUID rootImageId, UUID imageVersionId) throws BusinessException {
        RcaEnum.PoolState[] desktopPoolStateArr = {RcaEnum.PoolState.AVAILABLE, RcaEnum.PoolState.UPDATING};
        Map<String, Object> map = new HashMap<>();
        map.put(ROOT_IMAGE_ID, rootImageId);
        map.put(IMAGE_TEMPLATE_ID, imageVersionId);
        map.put(POOL_STATE, desktopPoolStateArr);
        map.put(HOST_SOURCE_TYPE, RcaEnum.HostSourceType.VDI);

        ConditionQueryRequest rcaPoolListQueryRequest = buildRcaPoolConditionQueryRequest(map);
        return rcaAppPoolAPI.listByConditions(rcaPoolListQueryRequest);
    }

    @SuppressWarnings({"checkstyle:ParameterNumber"})
    private ConditionQueryRequest buildRcaPoolConditionQueryRequest(Map<String, Object> map) {
        ConditionQueryRequestBuilder deskPoolBuilder = new DefaultConditionQueryRequestBuilder();
        if (!ObjectUtils.isEmpty(map.get(ROOT_IMAGE_ID))) {
            deskPoolBuilder.eq(ROOT_IMAGE_ID, map.get(ROOT_IMAGE_ID));
        }
        if (!ObjectUtils.isEmpty(map.get(IMAGE_TEMPLATE_ID))) {
            deskPoolBuilder.neq(IMAGE_TEMPLATE_ID, map.get(IMAGE_TEMPLATE_ID));
        }
        if (!ObjectUtils.isEmpty(map.get(POOL_STATE))) {
            deskPoolBuilder.in(POOL_STATE, (RcaEnum.PoolState[]) map.get(POOL_STATE));
        }
        if (!ObjectUtils.isEmpty(map.get(SYSTEM_SIZE))) {
            deskPoolBuilder.ge(SYSTEM_SIZE, map.get(SYSTEM_SIZE));
        }
        if (!ObjectUtils.isEmpty(map.get(HOST_SOURCE_TYPE))) {
            deskPoolBuilder.eq(HOST_SOURCE_TYPE, map.get(HOST_SOURCE_TYPE));
        }
        return deskPoolBuilder.build();
    }

    private RcaDeliveryAppPoolImageBatchTaskHandler buildDeliveryAppPoolImageHandler(DeliveryAppImageTemplateVersionWebRequest request, String imageVersionName,
                                                                                 Iterator<DefaultBatchTaskItem> iterator) throws BusinessException {
        final RcaDeliveryAppPoolImageBatchTaskHandler handler = new RcaDeliveryAppPoolImageBatchTaskHandler(iterator);
        handler.setAppPoolImageTemplateValidation(appPoolImageTemplateValidation);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setRcaAppPoolAPI(rcaAppPoolAPI);
        handler.setRcaHostAPI(rcaHostAPI);
        handler.setBatchTaskBuilderFactory(batchTaskBuilderFactory);
        handler.setImageTemplateId(request.getImageVersionId());
        handler.setImageVersionName(imageVersionName);
        handler.setStartChangeHostImageBatchTaskService(deliveryChangeHostImageBatchTaskService);

        return handler;
    }
}
