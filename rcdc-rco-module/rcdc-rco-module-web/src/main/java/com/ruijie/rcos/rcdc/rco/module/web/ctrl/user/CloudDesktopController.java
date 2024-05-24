package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbChangeDeskNicMacDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.vm.VmGraphicsDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.VdiDesktopDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DesktopType;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.DeskSpecDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.DesktopExportSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.DesktopUpdateImageHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.EditDeskRootPwdConfigHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.ResetDeskWindowsPwdHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desktop.ResetDeskWindowsPwdHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils.TerminalIdMappingUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask.EditVDIDeskRemarkBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask.EditVDIDeskRemarkBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask.RefreshStrategyBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask.RefreshVDIDesktopStrategyBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.SoftwareControlBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.param.EditNetworkBatchParam;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.DesktopQueryUtil;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.desktop.VdiWebDesktopDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.DeskNetworkDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.DesktopNetworkDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.validation.CloudDesktopValidation;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 云桌面WEB控制器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author artom
 */
@Controller
@RequestMapping("/rco/user/cloudDesktop")
@EnableCustomValidate(validateClass = CloudDesktopValidation.class)
@Api(tags = "云桌面行为")
public class CloudDesktopController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDesktopController.class);

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private DeskFaultInfoAPI deskFaultInfoAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private ExportCloudDesktopAPI exportCloudDesktopAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacUserMgmtAPI cbbAdMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private CbbDeskDiskAPI cbbDeskDiskAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;


    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;


    @Autowired
    private CbbVDIDeskGtMgmtAPI cbbVDIDeskGtMgmtAPI;

    @Autowired
    private DesktopGuestToolMgmtAPI desktopGuestToolMgmtAPI;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private CbbResetWindowsPwdAPI cbbResetWindowsPwdAPI;

    @Autowired
    private MailMgmtAPI mailMgmtAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private HostUserAPI hostUserAPI;

    @Autowired
    private CbbThirdPartyDeskOperateMgmtAPI cbbThirdPartyDeskOperateMgmtAPI;

    private static final String CREATE_CLOUDDESKTOP_THREAD_POOL_ID = "createBatchCloudDesktopThreadPoolId";

    private static final String UPDATE_SPEC_CLOUDDESKTOP_THREAD_POOL_ID = "updateSpecBatchCloudDesktopThreadPoolId";

    private static final UUID CHECK_DESKTOP_PORT_TASK_ID = UUID.nameUUIDFromBytes("check_desktop_port".getBytes(StandardCharsets.UTF_8));

    private static final ThreadExecutor THREAD_EXECUTOR_SEND_EMAIL =
            ThreadExecutors.newBuilder("send_email_executor").maxThreadNum(5).queueSize(1).build();

    /**
     * *查询
     *
     * @param request 页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("云桌面列表")
    public CommonWebResponse<DefaultPageResponse<CloudDesktopDTO>> list(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        PageSearchRequest pageReq = new DeskPageSearchRequest(request);
        generateDesktopQueryRequest(pageReq, sessionContext);

        DefaultPageResponse<CloudDesktopDTO> resp = cloudDesktopMgmtAPI.pageQuery(pageReq);

        DesktopQueryUtil.convertMoreAccurateCloudDesktopInfo(resp);
        return CommonWebResponse.success(resp);
    }


    /**
     * 查询 附带分配信息
     *
     * @param request        页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listWithAssignment", method = RequestMethod.POST)
    @ApiOperation("云桌面列表附带分配信息")
    public CommonWebResponse<DefaultPageResponse<CloudDesktopDTO>> listWithAssignment(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        DesktopAssignmentPageSearchRequest pageReq = new DesktopAssignmentPageSearchRequest(request);
        generateDesktopQueryRequest(pageReq, sessionContext);

        DefaultPageResponse<CloudDesktopDTO> resp = cloudDesktopMgmtAPI.pageQueryWithAssignment(pageReq);
        DesktopQueryUtil.convertMoreAccurateCloudDesktopInfo(resp);
        return CommonWebResponse.success(resp);
    }

    private void generateDesktopQueryRequest(PageSearchRequest pageReq, SessionContext sessionContext) throws BusinessException {
        Sort[] sortArr = DesktopQueryUtil.generateDesktopSortArr(pageReq.getSortArr());
        pageReq.setSortArr(sortArr);

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            appendDesktopIdMatchEqual(pageReq, sessionContext);
        }
    }

    /**
     * 异步导出云桌面数据到excel
     *
     * @param webRequest     webRequest
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

        ExportCloudDesktopRequest request = buildExportRequest(webRequest, sessionContext);
        request.setImageUsage(ImageUsageTypeEnum.DESK);
        exportCloudDesktopAPI.exportDataAsync(request);
        auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EXPORT_SUCCESS_LOG, new String[]{});
        return CommonWebResponse.success();
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
            return request;
        }

        if (ArrayUtils.isNotEmpty(webRequest.getDesktopPoolIdArr()) && webRequest.getExportSource() == DesktopExportSourceEnum.DESKTOP_POOL) {
            // 池桌面导出
            List<UUID> poolIdList = Lists.newArrayList(webRequest.getDesktopPoolIdArr());
            if (!permissionHelper.isAllGroupPermission(sessionContext)) {
                UUID[] permissionPoolIdArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.DESKTOP_POOL);
                Set<UUID> permissionPoolIdSet = Sets.newHashSet(permissionPoolIdArr);
                poolIdList = poolIdList.stream().filter(permissionPoolIdSet::contains).collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(poolIdList)) {
                request.setDesktopIdList(cbbDeskMgmtAPI.listDeskIdByDesktopPoolIds(poolIdList, false));
                return request;
            }
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
    public CommonWebResponse<ExportCloudDesktopCacheDTO> getExportResult(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportCloudDesktopCacheResponse response = exportCloudDesktopAPI.getExportDataCache(userId);
        return CommonWebResponse.success(response.getCloudDesktopCacheDTO());
    }


    /**
     * 下载云桌面数据excel
     *
     * @param request        页面请求参数
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
        String fileName = "桌面信息(CloudDesktop)" + df.format(new Date());
        return builder.setFile(exportFile.getExportFile(), false).setName(fileName, "xlsx").build();
    }


    /**
     * * 为策略查询云桌面提供的接口，该接口查询所有云桌面（包括回收站中数据）
     *
     * @param request 页面请求参数
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/forStrategy/list")
    public DefaultWebResponse listForStrategy(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null.");
        PageSearchRequest pageReq = new DeskPageSearchRequest(request);
        DefaultPageResponse<CloudDesktopDTO> resp = cloudDesktopMgmtAPI.pageQuery(pageReq);
        DesktopQueryUtil.convertMoreAccurateCloudDesktopInfo(resp);
        return DefaultWebResponse.Builder.success(resp);
    }

    /**
     * *根据网络ID查询云桌面列表
     *
     * @param request 页面请求参数
     * @return default response
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listByNetwork")
    public DefaultWebResponse listByNetwork(IdPageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(request.getId(), "id is null.");

        PageSearchRequest pageReq = new PageSearchRequest(request);

        MatchEqual[] matchEqualArr = pageReq.getMatchEqualArr();
        if (matchEqualArr == null || matchEqualArr.length == 0) {
            matchEqualArr = new MatchEqual[]{new MatchEqual()};
            matchEqualArr[0].setName("cbbNetworkId");
            matchEqualArr[0].setValueArr(new Object[]{request.getId()});
        } else {
            MatchEqual[] newArr = new MatchEqual[matchEqualArr.length + 1];
            System.arraycopy(matchEqualArr, 0, newArr, 0, matchEqualArr.length);
            newArr[matchEqualArr.length] = new MatchEqual();
            newArr[matchEqualArr.length].setName("cbbNetworkId");
            newArr[matchEqualArr.length].setValueArr(new Object[]{request.getId()});
            matchEqualArr = newArr;
        }
        pageReq.setMatchEqualArr(matchEqualArr);
        DefaultPageResponse<CloudDesktopDTO> resp = cloudDesktopMgmtAPI.pageQuery(pageReq);
        DesktopQueryUtil.convertMoreAccurateCloudDesktopInfo(resp);
        return DefaultWebResponse.Builder.success(resp);
    }

    /**
     * *根据策略ID查询云桌面列表
     *
     * @param request 页面请求参数
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listByStrategy")
    public DefaultWebResponse listByStrategy(IdPageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(request.getId(), "id is null.");

        PageSearchRequest pageReq = new PageSearchRequest(request);

        MatchEqual[] matchEqualArr = pageReq.getMatchEqualArr();
        if (matchEqualArr == null || matchEqualArr.length == 0) {
            matchEqualArr = new MatchEqual[]{new MatchEqual()};
            matchEqualArr[0].setName("cbbStrategyId");
            matchEqualArr[0].setValueArr(new Object[]{request.getId()});
        } else {
            MatchEqual[] newArr = new MatchEqual[matchEqualArr.length + 1];
            System.arraycopy(matchEqualArr, 0, newArr, 0, matchEqualArr.length);
            newArr[matchEqualArr.length] = new MatchEqual();
            newArr[matchEqualArr.length].setName("cbbStrategyId");
            newArr[matchEqualArr.length].setValueArr(new Object[]{request.getId()});
            matchEqualArr = newArr;
        }
        pageReq.setMatchEqualArr(matchEqualArr);
        DefaultPageResponse<CloudDesktopDTO> resp = cloudDesktopMgmtAPI.pageQuery(pageReq);
        DesktopQueryUtil.convertMoreAccurateCloudDesktopInfo(resp);
        return DefaultWebResponse.Builder.success(resp);
    }

    /**
     * *根据用户ID查询云桌面列表
     *
     * @param request 页面请求参数
     * @return default response
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listByUser")
    public DefaultWebResponse listByUser(IdPageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(request.getId(), "id is null.");
        PageSearchRequest pageReq = new PageSearchRequest(request);

        MatchEqual[] matchEqualArr = pageReq.getMatchEqualArr();
        if (matchEqualArr == null || matchEqualArr.length == 0) {
            matchEqualArr = new MatchEqual[]{new MatchEqual()};
            matchEqualArr[0].setName("userId");
            matchEqualArr[0].setValueArr(new Object[]{request.getId()});
        } else {
            MatchEqual[] newArr = new MatchEqual[matchEqualArr.length + 1];
            System.arraycopy(matchEqualArr, 0, newArr, 0, matchEqualArr.length);
            newArr[matchEqualArr.length] = new MatchEqual();
            newArr[matchEqualArr.length].setName("userId");
            newArr[matchEqualArr.length].setValueArr(new Object[]{request.getId()});
            matchEqualArr = newArr;
        }
        pageReq.setMatchEqualArr(matchEqualArr);

        DefaultPageResponse<CloudDesktopDTO> resp = cloudDesktopMgmtAPI.pageQuery(pageReq);
        DesktopQueryUtil.convertMoreAccurateCloudDesktopInfo(resp);
        return DefaultWebResponse.Builder.success(resp);
    }

    /**
     * *查询明细
     *
     * @param request 页面请求参数
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云桌面详情")
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public CommonWebResponse<CloudDesktopDetailDTO> getInfo(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "request id must not be null");
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(request.getId());
        if (DesktopType.APP_LAYER.toString().equals(cloudDesktopDetailDTO.getDesktopType())
                && Objects.nonNull(cloudDesktopDetailDTO.getSystemDisk())) {
            cloudDesktopDetailDTO.setSystemDisk(cloudDesktopDetailDTO.getSystemDisk() + Constants.SYSTEM_DISK_CAPACITY_INCREASE_SIZE);
        }
        if (CbbCloudDeskType.IDV.name().equals(cloudDesktopDetailDTO.getDesktopCategory())) {
            cloudDesktopDetailDTO.setDesktopCategory(cloudDesktopDetailDTO.getCbbImageType());
            cloudDesktopDetailDTO.setDeskType(cloudDesktopDetailDTO.getCbbImageType());
        }
        return CommonWebResponse.success(cloudDesktopDetailDTO);
    }

    /**
     * * 创建云桌面
     *
     * @param request web request
     * @param builder batch task builder
     * @return web response
     * @throws BusinessException business exception
     */
    @RequestMapping(value = "/create")
    @EnableCustomValidate(validateMethod = "createValidate")
    @ApiOperation("创建云桌面")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public DefaultWebResponse create(CreateDesktopWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        CreateCloudDesktopRequest createRequest = buildCreateCloudDesktopRequest(request);

        // 填写默认值
        checkAndFillCreateDefaultValue(createRequest);

        Integer desktopNum = request.getVdiDesktopConfig().getVisitorDesktopNum();
        if (desktopNum == null || desktopNum == 0) {
            desktopNum = 1;
        }
        UUID userId = request.getId();
        List<DefaultBatchTaskItem> taskItemList = new ArrayList<>(desktopNum);
        for (int i = 0; i < desktopNum; i++) {
            taskItemList.add(DefaultBatchTaskItem.builder().itemId(userId)
                    .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_ITEM_NAME, new String[]{}).build());
        }
        IacUserDetailDTO userDetailResponse = userAPI.getUserDetail(userId);
        String userName = userDetailResponse.getUserName();
        CreateDesktopBatchTaskHandler handler = new CreateDesktopBatchTaskHandler(taskItemList, createRequest, auditLogAPI).setUserName(userName)
                .setCloudDesktopWebService(cloudDesktopWebService).setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        BatchTaskSubmitResult result;
        if (desktopNum == 1) {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_SINGLE_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_SINGLE_TASK_DESC, new String[]{userName}).registerHandler(handler).start();
        } else {
            UUID createId = UUID.nameUUIDFromBytes(CREATE_CLOUDDESKTOP_THREAD_POOL_ID.getBytes());
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .enablePerformanceMode(createId, 30).start();
        }
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * @param webReq request
     * @return api request
     */
    private CreateCloudDesktopRequest buildCreateCloudDesktopRequest(CreateDesktopWebRequest webReq) throws BusinessException {
        CreateCloudDesktopRequest req = new CreateCloudDesktopRequest();
        VdiDesktopConfigVO vdiDesktopConfig = webReq.getVdiDesktopConfig();
        req.setDesktopImageId(vdiDesktopConfig.getImage().getId());
        req.setStrategyId(vdiDesktopConfig.getStrategy().getId());
        req.setSoftwareStrategyId(vdiDesktopConfig.getSoftwareStrategy() != null ? vdiDesktopConfig.getSoftwareStrategy().getId() : null);
        req.setNetworkId(vdiDesktopConfig.getNetwork().getAddress().getId());
        req.setDesktopIp(vdiDesktopConfig.getNetwork().getAddress().getIp());
        req.setUserId(webReq.getId());
        req.setDesktopRole(vdiDesktopConfig.getDesktopRole());
        IdLabelEntry userProfileStrategy = vdiDesktopConfig.getUserProfileStrategy();
        if (userProfileStrategy != null) {
            req.setUserProfileStrategyId(userProfileStrategy.getId());
        }
        IdLabelEntry cluster = vdiDesktopConfig.getCluster();
        if (cluster != null) {
            req.setClusterId(cluster.getId());
            req.setDeskSpec(deskSpecAPI.buildCbbDeskSpec(req.getClusterId(), vdiDesktopConfig.toDeskSpec()));
        }
        req.setPlatformId(vdiDesktopConfig.getCloudPlatform().getId());
        return req;
    }

    private void checkAndFillCreateDefaultValue(CreateCloudDesktopRequest createRequest) throws BusinessException {
        if (Objects.isNull(createRequest.getClusterId())) {
            Optional.ofNullable(cbbImageTemplateMgmtAPI.getImageTemplateDetail(createRequest.getDesktopImageId()))
                    .map(CbbImageTemplateDetailDTO::getClusterInfo).ifPresent(clusterInfoDTO -> createRequest.setClusterId(clusterInfoDTO.getId()));
        }
    }

    /**
     * * 批量修改云桌面规格：cpu，内存，个人盘和系统盘
     *
     * @param request web request
     * @param builder batch task builder
     * @return web response
     * @throws BusinessException business exception
     */
    @ApiOperation("批量修改云桌面规格")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, //
            descriptions = {"分级分权-区分权限，加入权限控制,批量设置用户身份验证配置"})})
    @RequestMapping(value = "/strategy/editIndependentVDIStrategy", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "editIndependentVDIStrategyValidate")
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> editIndependentVDIStrategy(UpdateDesktopSpecWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        List<UpdateDesktopSpecBatchTaskItem> taskItemList = buildUpdateSpecTask(request);
        UpdateDesktopSpecBatchTaskHandler handler = new UpdateDesktopSpecBatchTaskHandler(taskItemList);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        BatchTaskSubmitResult result;
        if (request.getIdArr().length == 1) {
            handler.setIsBatch(false);
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(request.getIdArr()[0]);
            String desktopName = cloudDesktopDetailDTO.getDesktopName();
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_SINGLE_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_SINGLE_TASK_DESC, new String[]{desktopName}).registerHandler(handler)
                    .start();
        } else {
            UUID createId = UUID.nameUUIDFromBytes(UPDATE_SPEC_CLOUDDESKTOP_THREAD_POOL_ID.getBytes());
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .enablePerformanceMode(createId, 30).start();
        }
        return CommonWebResponse.success(result);
    }

    private List<UpdateDesktopSpecBatchTaskItem> buildUpdateSpecTask(UpdateDesktopSpecWebRequest request) {
        UpdateDesktopSpecBatchTaskItem batchTaskItem;
        List<UpdateDesktopSpecBatchTaskItem> taskList = new ArrayList<>(request.getIdArr().length);
        for (UUID id : request.getIdArr()) {
            batchTaskItem = new UpdateDesktopSpecBatchTaskItem();
            batchTaskItem.setItemId(id);
            batchTaskItem.setCpu(request.getEnableChangeCpu() ? request.getCpu() : null);
            batchTaskItem.setMemory(request.getEnableChangeMemory() ? CapacityUnitUtils.gb2Mb(request.getMemory()) : null);
            batchTaskItem.setSystemSize(request.getEnableChangeSystemDisk() ? request.getSystemDisk() : null);
            batchTaskItem.setPersonSize(request.getEnableChangePersonalDisk() ? request.getPersonalDisk() : null);
            if (Objects.nonNull(batchTaskItem.getPersonSize()) && batchTaskItem.getPersonSize() > 0
                    && Objects.nonNull(request.getPersonDiskStoragePool())) {
                batchTaskItem.setPersonDiskStoragePoolId(request.getPersonDiskStoragePool().getId());
            }
            batchTaskItem.setPersonSize(request.getEnableChangePersonalDisk() ? request.getPersonalDisk() : null);
            // 额外盘
            batchTaskItem.setEnableChangeExtraDisk(BooleanUtils.isTrue(request.getEnableChangeExtraDisk()));
            batchTaskItem.setExtraDiskList(convertToExtraDiskDTO(request.getExtraDiskArr()));

            batchTaskItem.setEnableHyperVisorImprove(request.getEnableChangeHyperVisorImprove() ? request.getEnableHyperVisorImprove() : null);
            batchTaskItem.setItemName(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_ITEM_NAME));
            if (request.getEnableChangeVgpu()) {
                batchTaskItem.setEnableChangeVgpu(true);
                if (request.getVgpuType() == VgpuType.QXL || Objects.isNull(request.getVgpuExtraInfo())
                        || StringUtils.isEmpty(request.getVgpuExtraInfo().getModel())) {
                    batchTaskItem.setVgpuType(VgpuType.QXL);
                    batchTaskItem.setVgpuExtraInfo(null);
                } else {
                    // 各个桌面所在的云平台不同，所以显卡先不校验补充详细信息
                    batchTaskItem.setVgpuType(request.getVgpuType());
                    batchTaskItem.setVgpuExtraInfo(request.getVgpuExtraInfo());
                }
            }
            taskList.add(batchTaskItem);
        }
        return taskList;
    }

    private List<CbbAddExtraDiskDTO> convertToExtraDiskDTO(ExtraDiskDTO[] extraDiskArr) {
        if (ArrayUtils.isEmpty(extraDiskArr)) {
            return new ArrayList<>();
        }
        return Arrays.stream(extraDiskArr).map(item -> {
            CbbAddExtraDiskDTO cbbAddExtraDiskDTO = new CbbAddExtraDiskDTO();
            cbbAddExtraDiskDTO.setDiskId(item.getDiskId());
            cbbAddExtraDiskDTO.setAssignedStoragePoolId(Objects.nonNull(item.getExtraDiskStoragePool()) ?
                    item.getExtraDiskStoragePool().getId() : null);
            cbbAddExtraDiskDTO.setIndex(item.getIndex());
            cbbAddExtraDiskDTO.setExtraSize(item.getExtraSize());
            return cbbAddExtraDiskDTO;
        }).collect(Collectors.toList());
    }

    /**
     * * 云桌面强制关机
     *
     * @param request req
     * @param builder builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "强制关机")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/powerOff")
    @EnableAuthority
    public DefaultWebResponse powerOff(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        String desktopType = cloudDesktopMgmtAPI.getImageUsageByDeskId(idArr[0]);
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_ITEM_NAME, new String[]{desktopType}).build()).iterator();

        final PowerOffDesktopBatchTaskHandler handler =
                new PowerOffDesktopBatchTaskHandler(iterator, auditLogAPI).setCloudDesktopWebService(cloudDesktopWebService)
                        .setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI).setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        handler.setDesktopType(desktopType);
        BatchTaskSubmitResult result = executePowerOffTask(builder, idArr, handler, desktopType);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult executePowerOffTask(BatchTaskBuilder builder, UUID[] idArr, final PowerOffDesktopBatchTaskHandler handler
            , String desktopType) throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_SINGLE_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName(), desktopType)
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_BATCH_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_BATCH_TASK_DESC, desktopType).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * * 云桌面关机
     *
     * @param request req
     * @param builder builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "关机")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/shutdown")
    @EnableAuthority
    public DefaultWebResponse shutdown(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        String desktopType = cloudDesktopMgmtAPI.getImageUsageByDeskId(idArr[0]);

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_ITEM_NAME, new String[]{desktopType}).build()).iterator();
        final ShutdownDesktopBatchTaskHandler handler =
                new ShutdownDesktopBatchTaskHandler(iterator, auditLogAPI).setCloudDesktopWebService(cloudDesktopWebService)
                        .setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI).setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        handler.setDesktopType(desktopType);
        BatchTaskSubmitResult result = executeShutdownBatchTask(builder, idArr, handler, desktopType);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult executeShutdownBatchTask(BatchTaskBuilder builder, UUID[] idArr, final ShutdownDesktopBatchTaskHandler handler
            , String desktopType) throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_SINGLE_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName(), desktopType)
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_BATCH_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_BATCH_TASK_DESC, desktopType).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 查询云桌面是否存在旧数据盘
     *
     * @param request 云桌面
     * @return 是否存在旧数据盘
     */
    @ApiOperation(value = "查询是否有旧数据盘")
    @RequestMapping(value = "/existOldDataDisk")
    public DefaultWebResponse existOldDataDisk(IdArrWebRequest request) {
        Assert.notNull(request, "request cant be null");

        List<UUID> deskIdList = Arrays.stream(request.getIdArr()).collect(Collectors.toList());

        List<CbbDeskDiskDTO> diskList = cbbVDIDeskDiskAPI.findByDeskIdsAndDiskType(deskIdList, CbbDiskType.BACKUP);

        Boolean existDisk =
                diskList.stream().anyMatch(disk -> disk.getType() == CbbDiskType.BACKUP && disk.getActiveStatus() != CbbDiskActiveStatus.ACTIVE);

        return DefaultWebResponse.Builder.success(existDisk);
    }

    /**
     * * 云桌面开机
     *
     * @param request req
     * @param builder builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "开机")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, //
            descriptions = {"分级分权-区分权限，加入权限控制,批量设置用户身份验证配置"})})
    @RequestMapping(value = "/start")
    @EnableAuthority
    public DefaultWebResponse start(StartDesktopWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        String desktopType = cloudDesktopMgmtAPI.getImageUsageByDeskId(idArr[0]);
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(UserBusinessKey.RCDC_RCO_DESKTOP_START_ITEM_NAME, new String[]{desktopType}).build()).iterator();
        final StartDesktopBatchTaskHandler handler = new StartDesktopBatchTaskHandler(iterator, auditLogAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        handler.setEnableMountOldData(request.getEnableMountOldData());
        handler.setDesktopType(desktopType);
        BatchTaskSubmitResult result = executeStartBatchTask(builder, idArr, handler, desktopType);
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * * 云桌面强制唤醒
     *
     * @param request req
     * @param builder builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "强制唤醒")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_2_0, //
            descriptions = {"分级分权-区分权限，加入权限控制,批量设置用户身份验证配置"})})
    @RequestMapping(value = "/forceWakeUp")
    @EnableAuthority
    public DefaultWebResponse forceWakeUp(StartDesktopWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_ITEM_NAME, new String[]{}).build()).iterator();
        final ForceWakeUpDesktopBatchTaskHandler handler = new ForceWakeUpDesktopBatchTaskHandler(iterator, auditLogAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        handler.setEnableMountOldData(request.getEnableMountOldData());

        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SINGLE_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_BATCH_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult executeStartBatchTask(BatchTaskBuilder builder, UUID[] idArr, final StartDesktopBatchTaskHandler handler
            , String desktopType) throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_START_SINGLE_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_START_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName(), desktopType)
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_START_BATCH_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_START_BATCH_TASK_DESC, desktopType).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * * 云桌面启用维护模式
     *
     * @param request req
     * @param builder builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "启用维护模式")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/enableMaintenanceMode")
    @EnableAuthority
    public DefaultWebResponse enableMaintenanceMode(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(UserBusinessKey.RCDC_DESKTOP_OPEN_MAINTENANCE, new String[]{}).build()).iterator();
        final DesktopMaintenanceBatchTaskHandler handler =
                new DesktopMaintenanceBatchTaskHandler(iterator, true).setCloudDesktopWebService(cloudDesktopWebService)
                        .setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI).setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_DESKTOP_OPEN_MAINTENANCE)
                .setTaskDesc(UserBusinessKey.RCDC_DESKTOP_OPEN_MAINTENANCE_TASK_DESC).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 云桌面禁用维护模式
     *
     * @param request req
     * @param builder builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "禁用维护模式")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/disableMaintenanceMode")
    public DefaultWebResponse disableMaintenanceMode(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(UserBusinessKey.RCDC_DESKTOP_CLOSE_MAINTENANCE, new String[]{}).build()).iterator();
        final DesktopMaintenanceBatchTaskHandler handler =
                new DesktopMaintenanceBatchTaskHandler(iterator, false).setCloudDesktopWebService(cloudDesktopWebService)
                        .setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI).setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_DESKTOP_CLOSE_MAINTENANCE)
                .setTaskDesc(UserBusinessKey.RCDC_DESKTOP_CLOSE_MAINTENANCE_TASK_DESC).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }


    /**
     * @param request 桌面ID列表
     * @param builder builder
     * @return Result
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "重启")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0)})
    @RequestMapping(value = "/restart")
    @EnableAuthority
    public DefaultWebResponse restart(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        String desktopType = cloudDesktopMgmtAPI.getImageUsageByDeskId(idArr[0]);

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct() //
                .map(id -> DefaultBatchTaskItem.builder().itemId(id) //
                        .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_ITEM_NAME, new String[]{}).build())
                .iterator();
        final RestartDesktopBatchTaskHandler handler = new RestartDesktopBatchTaskHandler(iterator, auditLogAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        handler.setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
        handler.setDesktopType(desktopType);
        BatchTaskSubmitResult result = executeRestartBatchTask(builder, idArr, handler, desktopType);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult executeRestartBatchTask(BatchTaskBuilder builder, UUID[] idArr
            , final RestartDesktopBatchTaskHandler handler, String desktopType) throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_SINGLE_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName(), desktopType)
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_BATCH_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_BATCH_TASK_DESC, desktopType).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }


    /**
     * * 云桌面删除
     *
     * @param request 页面请求参数
     * @param builder builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "云桌面删除")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, //
            descriptions = {"分级分权-区分权限，加入权限控制,批量设置用户身份验证配置"})})
    @RequestMapping(value = "/delete")
    @EnableAuthority
    public DefaultWebResponse delete(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        Boolean shouldOnlyDeleteDataFromDb = request.getShouldOnlyDeleteDataFromDb();
        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_ITEM_NAME, new String[] {Constants.CLOUD_DESKTOP, prefix}).build())//
                .iterator();
        final DeleteDesktopBatchTaskhandler handler = new DeleteDesktopBatchTaskhandler(iterator, auditLogAPI, shouldOnlyDeleteDataFromDb)
                .setCloudDesktopWebService(cloudDesktopWebService).setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setUserProfileMgmtAPI(userProfileMgmtAPI);
        BatchTaskSubmitResult result = executeDeleteBatchTask(builder, idArr, handler, prefix);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult executeDeleteBatchTask(BatchTaskBuilder builder, UUID[] idArr, final DeleteDesktopBatchTaskhandler handler, String prefix)
            throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_SINGLE_TASK_NAME, Constants.CLOUD_DESKTOP, prefix)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName(), Constants.CLOUD_DESKTOP, prefix)
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_BATCH_TASK_NAME, Constants.CLOUD_DESKTOP, prefix)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_BATCH_TASK_DESC, Constants.CLOUD_DESKTOP, prefix).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 获取云桌面详细网络配置
     *
     * @param request 云桌面id
     * @return resp
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云桌面网络配置信息")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0, descriptions = {"分级分权-区分权限，获取云桌面网络配置信息"})})
    @RequestMapping(value = "idv/network/detail")
    public DefaultWebResponse getDesktopNetworkDetail(DeskIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbDeskDTO cbbDeskDTO = cbbIDVDeskMgmtAPI.getDeskIDV(request.getId());
        DeskNetworkDetailVO deskNetworkDetailVO = new DeskNetworkDetailVO();
        // ip冲突时ip格式为ip1/ip2,返回ip1
        String deskIp = cbbDeskDTO.getDeskIp();
        if (Objects.isNull(deskIp)) {
            deskNetworkDetailVO.setIpAddr(null);
        } else {
            String deskIpFirst = deskIp.split("/")[0];
            deskNetworkDetailVO.setIpAddr(deskIpFirst);
        }
        // ip冲突时mask格式为ip1/ip2,返回ip1
        String deskMask = cbbDeskDTO.getMask();
        if (Objects.isNull(deskMask)) {
            deskNetworkDetailVO.setMask(null);
        } else {
            String deskMaskFirst = deskMask.split("/")[0];
            deskNetworkDetailVO.setMask(deskMaskFirst);
        }
        deskNetworkDetailVO.setGateway(cbbDeskDTO.getGateWay());
        deskNetworkDetailVO.setDns(cbbDeskDTO.getDnsPrimary());
        deskNetworkDetailVO.setDnsBack(cbbDeskDTO.getDnsSecondary());
        deskNetworkDetailVO.setAutoDhcp(cbbDeskDTO.getAutoDhcp());
        deskNetworkDetailVO.setAutoDns(cbbDeskDTO.getAutoDns());
        return DefaultWebResponse.Builder.success(deskNetworkDetailVO);
    }

    /**
     * 单台编辑IDV云桌面网络配置单台
     *
     * @param request 页面请求参数
     * @param builder 任务构造类
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("单台编辑IDV云桌面网络配置")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0, descriptions = {"分级分权-区分权限，单台编辑IDV云桌面网络配置"})})
    @RequestMapping(value = "idv/editNetwork", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> editIDVDesktopNetwork(EditIDVDesktopNetworkWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "editIDVDesktopNetworkWebRequest can not be null");
        Assert.notNull(builder, "builder can not be null");
        UUID deskId = request.getId();
        // 判断是否IDV云桌面
        Boolean isDeskTypeIDV = isDeskTypeIDV(deskId);
        if (!isDeskTypeIDV) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_NOT_IDV, userDesktopMgmtAPI.getDesktopDetailById(deskId).getDesktopName());
        }
        DefaultBatchTaskItem batchTaskItem = DefaultBatchTaskItem.builder().itemId(deskId)
                .itemName(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_ITEM_NAME)).build();
        EditIDVDesktopNetworkBatchTaskHandler handler =
                new EditIDVDesktopNetworkBatchTaskHandler(request, batchTaskItem, auditLogAPI, cbbIDVDeskMgmtAPI, cloudDesktopWebService);
        BatchTaskSubmitResult result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_TASK_DESC).registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * 批量编辑IDV云桌面DNS
     *
     * @param request 页面请求参数
     * @param builder 任务构造类
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量编辑IDV云桌面DNS")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0, descriptions = {"分级分权-区分权限，批量编辑IDV云桌面DNS"})})
    @RequestMapping(value = "idv/editDns", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> editIDVDesktopDns(EditIDVDesktopDnsWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "editIDVDesktopDnsWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        UUID[] idArr = request.getIdArr();
        for (UUID deskId : idArr) {
            Boolean isDeskTypeIDV = isDeskTypeIDV(deskId);
            if (!isDeskTypeIDV) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_BATCH_NOT_IDV);
            }
        }
        BatchTaskSubmitResult result = execEditIDVDesktopDnsBatchTask(request, builder);
        return CommonWebResponse.success(result);
    }

    private Boolean isDeskTypeIDV(UUID desktopId) throws BusinessException {
        CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
        return Objects.equals(desktopDetailDTO.getCbbImageType(), CbbCloudDeskType.IDV.name());
    }

    /**
     * @param request 页面请求参数
     * @param builder 任务构造类
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("全部编辑IDV云桌面DNS")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0, descriptions = {"分级分权-区分权限，全部编辑IDV云桌面DNS"})})
    @RequestMapping(value = "idv/editDnsAll", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> editIDVDesktopDnsAll(EditIDVDesktopDnsAllWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "editIDVDesktopDnsWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        // 获取IDV云桌面idArr
        List<UUID> allIDVDesktopIdList = cbbIDVDeskMgmtAPI.findIDVDesktopId();
        if (CollectionUtils.isEmpty(allIDVDesktopIdList)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_NO_OPERATE_DESKTOP);
        }
        UUID[] idArr = new UUID[allIDVDesktopIdList.size()];
        allIDVDesktopIdList.toArray(idArr);

        EditIDVDesktopDnsWebRequest editIDVDesktopDnsWebRequest = new EditIDVDesktopDnsWebRequest();
        editIDVDesktopDnsWebRequest.setIdArr(idArr);
        editIDVDesktopDnsWebRequest.setAutoDns(request.getAutoDns());
        editIDVDesktopDnsWebRequest.setDns(request.getDns());
        editIDVDesktopDnsWebRequest.setDnsBack(request.getDnsBack());

        BatchTaskSubmitResult result = execEditIDVDesktopDnsBatchTask(editIDVDesktopDnsWebRequest, builder);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult execEditIDVDesktopDnsBatchTask(EditIDVDesktopDnsWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        final Iterator<EditIDVDesktopDnsBatchTaskItem> iterator = Stream.of(request.getIdArr()).map(id -> {
            EditIDVDesktopDnsBatchTaskItem batchTaskItem = new EditIDVDesktopDnsBatchTaskItem();
            batchTaskItem.setItemId(id);
            batchTaskItem.setAutoDns(request.getAutoDns());
            batchTaskItem.setDns(request.getDns());
            batchTaskItem.setDnsBack(request.getDnsBack());
            batchTaskItem.setItemName(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_ITEM_NAME));
            return batchTaskItem;
        }).iterator();

        EditIDVDesktopDnsBatchTaskHandler handler = new EditIDVDesktopDnsBatchTaskHandler(iterator, auditLogAPI);
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        handler.setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
        handler.setcbbIDVDeskMgmtAPI(cbbIDVDeskMgmtAPI);

        UUID[] idArr = request.getIdArr();
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_SINGLE_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getDesktopName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_BATCH_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 还原云桌面操作
     *
     * @param request 还原云桌面请求实体
     * @param builder 任务构造类
     * @return DefaultWebResponse
     * @throws BusinessException 任务构造失败
     */
    @ApiOperation("批量还原云桌面")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/revert", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> revert(RevertDesktopWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        String desktopType = cloudDesktopMgmtAPI.getImageUsageByDeskId(idArr[0]);
        verifyTaskIdExist(idArr);

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                        .itemId(id).itemName(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_ITEM_NAME, new String[]{desktopType}).build())//
                .iterator();
        final RevertDesktopBatchTaskHandler handler = new RevertDesktopBatchTaskHandler(iterator, auditLogAPI)
                .setCloudDesktopWebService(cloudDesktopWebService).setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        handler.setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
        handler.setDesktopFaultInfoAPI(deskFaultInfoAPI);
        handler.setEnableStoreSystemDisk(request.getEnableStoreSystemDisk());
        handler.setNeedRestoreDataTemplateDisk(request.getNeedRestoreDataTemplateDisk());
        handler.setDesktopType(desktopType);
        BatchTaskSubmitResult result = executeRevertBatchTask(builder, idArr, handler, desktopType);
        return CommonWebResponse.success(result);
    }

    /**
     * 还原应用磁盘
     *
     * @param request 还原应用磁盘请求实体
     * @param builder 任务构造类
     * @return DefaultWebResponse
     * @throws BusinessException 任务构造失败
     */
    @ApiOperation("批量还原应用磁盘")
    @RequestMapping(value = "/revertAppDisk", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse revertAppDisk(RevertDesktopWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        final List<DefaultBatchTaskItem> defaultBatchTaskItemList = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder()//
                        .itemId(id).itemName(BusinessKey.RCDC_RCO_APPCENTER_RESTORE_APP_DISK_ITEM_NAME, new String[]{}).build())//
                .collect(Collectors.toList());
        final RevertDesktopAppDiskBatchTaskHandler handler = new RevertDesktopAppDiskBatchTaskHandler(defaultBatchTaskItemList);
        BatchTaskSubmitResult result = builder.setTaskName(BusinessKey.RCDC_RCO_APPCENTER_RESTORE_APP_DISK_ITEM_NAME)
                .setTaskDesc(BusinessKey.RCDC_RCO_APPCENTER_RESTORE_APP_DISK_TASK_DESC).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * idv还原云桌面操作
     *
     * @param request 还原云桌面请求实体
     * @param builder 任务构造类
     * @return DefaultWebResponse
     * @throws BusinessException 任务构造失败
     */
    @ApiOperation("批量还原云桌面")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/idv/revert", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> idvRevert(RevertDesktopWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");
        return revert(request, builder);
    }

    private void verifyTaskIdExist(UUID[] idArr) throws BusinessException {
        for (UUID id : idArr) {
            cloudDesktopMgmtAPI.getDesktopDetailById(id);
        }
    }

    private BatchTaskSubmitResult executeRevertBatchTask(BatchTaskBuilder builder, UUID[] idArr, final RevertDesktopBatchTaskHandler handler
            , String desktopType) throws BusinessException {
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(idArr[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_SINGLE_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName(), desktopType)
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_BATCH_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_BATCH_TASK_DESC, desktopType).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }

    /**
     * * 云桌面策略修改
     *
     * @param request 页面请求参数
     * @return 云桌面策略详情
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/strategy/detail")
    public DefaultWebResponse strategyDetail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        IdRequest idRequest = new IdRequest(request.getId());
        CloudDesktopDetailDTO desktopDTO = cloudDesktopMgmtAPI.getDesktopStrategyByDesktopId(idRequest);
        Map<String, CloudDesktopDetailDTO> resultMap = ImmutableMap.of("strategy", desktopDTO);

        return DefaultWebResponse.Builder.success(resultMap);
    }

    /**
     * * 云桌面策略修改
     *
     * @param request 页面请求参数
     * @param builder builder/strategy/
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/strategy/edit")
    @ApiOperation(value = "云桌面策略修改")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public DefaultWebResponse editStrategy(EditStrategyWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notEmpty(request.getIdArr(), "request id must not be null");
        Assert.notNull(request.getStrategy().getId(), "request strategy id must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_ITEM_NAME, new String[]{}).build()).iterator();
        final EditStrategyBatchTaskHandler handler = new EditStrategyBatchTaskHandler(iterator, request.getStrategy().getId(), auditLogAPI)
                .setCloudDesktopWebService(cloudDesktopWebService).setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI)
                .setcbbIDVDeskMgmtAPI(cbbIDVDeskMgmtAPI).setDeskStrategyAPI(deskStrategyAPI).setDeskStrategyTciNotifyAPI(deskStrategyTciNotifyAPI);
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(request.getIdArr()[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_SINGLE_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getDesktopName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_BATCH_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 刷新云桌面的策略，应该无用了
     *
     * @param request 请求参数
     * @param builder builder
     * @return DefaultWebResponse
     * @throws BusinessException BusinessException
     */
    @Deprecated
    @ApiOperation("刷新云桌面的策略")
    @RequestMapping(value = "/strategy/refresh", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse refreshStrategy(RefreshStrategyWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notEmpty(request.getIdArr(), "request idArr must not be empty");

        Iterator<RefreshStrategyBatchTaskItem> iterator = makeRefreshItems(request);
        RefreshVDIDesktopStrategyBatchTaskHandler handler = new RefreshVDIDesktopStrategyBatchTaskHandler(iterator, auditLogAPI);

        if (request.getIdArr() != null && request.getIdArr().length == 1) {
            // 单个操作
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(request.getIdArr()[0]);
            return CommonWebResponse.success(builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SINGLE_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getDesktopName())
                    .registerHandler(handler).start());
        }
        // 全部和批量
        return CommonWebResponse.success(builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_BATCH_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_BATCH_TASK_DESC).enableParallel().registerHandler(handler).start());
    }

    /**
     * 构建更新策略的子任务列表
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    private Iterator<RefreshStrategyBatchTaskItem> makeRefreshItems(RefreshStrategyWebRequest request) throws BusinessException {
        // 批量查询云桌面详情
        List<UUID> idList = Arrays.stream(request.getIdArr()).distinct().collect(Collectors.toList());
        List<CloudDesktopDTO> cloudDesktopList = cloudDesktopMgmtAPI.listDesktopByDesktopIds(idList);
        // build子任务
        return cloudDesktopList.stream()
                .map(desktop -> new RefreshStrategyBatchTaskItem(desktop.getId(),
                        LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_ITEM_NAME), desktop.getDesktopStrategyId()))
                .iterator();
    }

    /**
     * * 云桌面网络详情
     *
     * @param request 页面请求参数
     * @return 云桌面网络详情
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/network/detail")
    public DefaultWebResponse networkDetail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        IdRequest idRequest = new IdRequest(request.getId());
        DesktopNetworkDTO networkDTO = cloudDesktopMgmtAPI.getNetworkByDesktopId(idRequest);
        DesktopNetworkDetailVO detail = new DesktopNetworkDetailVO(networkDTO);
        return DefaultWebResponse.Builder.success(detail);
    }

    /**
     * * 云桌面网络修改
     *
     * @param request 页面请求参数
     * @param builder task builder
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/network/edit")
    @EnableCustomValidate(validateMethod = "networkIpValidate")

    @EnableAuthority
    public DefaultWebResponse editNetwork(EditNeworkWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_ITEM_NAME, new String[]{}).build()).iterator();
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            result = editNetworkSingleRecord(builder, iterator, request);
        } else {
            final UUID taskPoolId = UUID.nameUUIDFromBytes("editDeskNetworkPool".getBytes());
            final EditNetworkBatchTaskHandler handler =
                    new EditNetworkBatchTaskHandler(iterator, request.getNetwork().getAddress().getId(), auditLogAPI)
                            .setCloudDesktopWebService(cloudDesktopWebService).setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .enablePerformanceMode(taskPoolId, 31).start();
        }
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult editNetworkSingleRecord(BatchTaskBuilder builder, final Iterator<DefaultBatchTaskItem> iterator,
                                                          EditNeworkWebRequest request) throws BusinessException {
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(request.getIdArr()[0]);
        EditNetworkBatchParam param = buildEditNetworkBatchParam(request);
        final EditNetworkBatchTaskHandler handler = new EditNetworkBatchTaskHandler(iterator, param, auditLogAPI)
                .setCloudDesktopWebService(cloudDesktopWebService).setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_SINGLE_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getDesktopName())
                .registerHandler(handler).start();
    }

    private EditNetworkBatchParam buildEditNetworkBatchParam(EditNeworkWebRequest request) {
        EditNetworkBatchParam param = new EditNetworkBatchParam();
        param.setIdArr(request.getIdArr());
        param.setNetwork(request.getNetwork());
        return param;
    }

    /**
     * 获取用户桌面配置
     *
     * @param request request
     * @return 返回桌面配置
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse getVisitorDesktopDetail(UserIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not null");
        VdiDesktopDetailResponse desktopDetail = cloudDesktopMgmtAPI.getVisitorVdiDesktopConfig(new IdRequest(request.getId()));
        VdiWebDesktopDetailResponse webResponse = new VdiWebDesktopDetailResponse();
        BeanUtils.copyProperties(desktopDetail, webResponse);
        return DefaultWebResponse.Builder.success(ImmutableMap.of("vdiDesktopConfig", webResponse));
    }

    /**
     * 编辑云桌面标签
     *
     * @param webRequest 请求参数
     * @param builder    批量任务
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑云桌面标签")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "editVDIDeskRemark", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editVDIDeskRemark(EditStrategyRemakeWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        Assert.notNull(builder, "builder must not be null");
        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isEmpty(webRequest.getRemark())) {
            webRequest.setRemark(null);
        }
        // 云桌面标签
        String remark = webRequest.getRemark();

        UUID[] idArr = webRequest.getIdArr();
        final Iterator<EditVDIDeskRemarkBatchTaskItem> iterator = Stream.of(idArr) //
                .distinct() //
                .map(id -> { //
                    String name = LocaleI18nResolver.resolve(UserBusinessKey.RCO_DESKTOP_EDIT_REMARK_ITEM_NAME);
                    return new EditVDIDeskRemarkBatchTaskItem(id, name, remark);
                }).iterator();
        // 构建统一的子任务处理器
        final EditVDIDeskRemarkBatchTaskHandler editVDIDeskRemarkBatchTaskHandler = new EditVDIDeskRemarkBatchTaskHandler(iterator, auditLogAPI);
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(webRequest.getIdArr()[0]);
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_REMARK_SINGLE_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_REMARK_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getDesktopName())
                    .enableParallel().registerHandler(editVDIDeskRemarkBatchTaskHandler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCO_DESKTOP_EDIT_REMARK_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCO_DESKTOP_EDIT_REMARK_BATCH_TASK_DESC).enableParallel()
                    .registerHandler(editVDIDeskRemarkBatchTaskHandler).start();
        }

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * * 恢复云桌面
     *
     * @param request 请求故障恢复的云桌面id数组
     * @param builder 批量任务参数
     * @return web response
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "恢复云桌面")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/recoverFromError")
    @EnableAuthority
    public DefaultWebResponse recoverFromError(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getIdArr();
        BatchTaskSubmitResult result = recoverDeskFromError(builder, idArr);
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult recoverDeskFromError(BatchTaskBuilder builder, final UUID[] idArr) throws BusinessException {
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_ITEM_NAME, new String[]{}).build()).iterator();
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(idArr[0]);
            result = recoverSingleRecord(builder, iterator, cloudDesktopDetailDTO.getDesktopName());
        } else {
            result = batchRecoverTaskSubmit(builder, iterator);
        }
        return result;
    }

    private BatchTaskSubmitResult recoverSingleRecord(BatchTaskBuilder builder, final Iterator<DefaultBatchTaskItem> iterator, String desktopName)
            throws BusinessException {
        final RecoverDesktopFromErrorBatchTaskHandler handler =
                new RecoverDesktopFromErrorBatchTaskHandler(cloudDesktopMgmtAPI, iterator, auditLogAPI, desktopName, cloudDesktopWebService);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SINGLE_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_SINGLE_TASK_DESC, desktopName).registerHandler(handler).start();
    }

    private BatchTaskSubmitResult batchRecoverTaskSubmit(BatchTaskBuilder builder, Iterator<DefaultBatchTaskItem> iterator) throws BusinessException {
        final RecoverDesktopFromErrorBatchTaskHandler handler =
                new RecoverDesktopFromErrorBatchTaskHandler(cloudDesktopMgmtAPI, iterator, auditLogAPI, cloudDesktopWebService);
        handler.setCbbUserDesktopOperateAPI(cloudDesktopOperateAPI);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_BATCH_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_RECOVER_FROM_ERROR_BATCH_TASK_DESC).enableParallel().registerHandler(handler).start();
    }

    /**
     * 编辑云桌面角色
     *
     * @param webRequest WEB请求
     * @param builder    builder
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "编辑云桌面角色(VIP桌面)")
    @RequestMapping(value = "/role/edit", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "desktopRoleValidate")
    public DefaultWebResponse editDesktopRole(EditDesktopRoleWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        Assert.notNull(builder, "builder can not be null");

        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(webRequest.getId());
        String desktopName = cloudDesktopDetailDTO.getDesktopName();

        DesktopRole desktopRole = webRequest.getDesktopRole();

        String logMsg = obtainLogMsg(desktopRole);
        try {
            cloudDesktopWebService.checkThirdPartyDesktop(webRequest.getId(), UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_VIP_ROLE_TEXT_THIRD_PARTY);
            EditDesktopRoleRequest editRequest = new EditDesktopRoleRequest();
            editRequest.setId(webRequest.getId());
            editRequest.setDesktopRole(desktopRole);
            cloudDesktopMgmtAPI.editDesktopRole(editRequest);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_ROLE_SUCCESS_LOG, desktopName, logMsg);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑云桌面 [" + desktopName + "]" + "级别 [" + desktopRole.toString() + "] 出错", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_ROLE_FAIL_LOG, e, desktopName, logMsg, e.getI18nMessage());
            throw e;
        }
    }

    private String obtainLogMsg(DesktopRole desktopRole) {
        String normalText = LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NORMAL_ROLE_TEXT);
        String vipText = LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_VIP_ROLE_TEXT);
        return desktopRole == DesktopRole.VIP ? vipText : normalText;
    }

    private void appendDesktopIdMatchEqual(PageSearchRequest request, SessionContext sessionContext) throws BusinessException {
        UUID[] uuidArr = permissionHelper.getDesktopIdArr(sessionContext);
        request.appendCustomMatchEqual(new MatchEqual("cbbDesktopId", uuidArr));
    }

    /**
     * 取消报障
     *
     * @param request web请求
     * @param builder 批量出来器
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "取消报障")
    @RequestMapping(value = "/relieveFault")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public DefaultWebResponse relieveFault(DesktopRelieveFaultWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(builder, "builder不能为null");

        String[] desktopIdArr = request.getIdArr();
        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(desktopIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        String desktopType = cloudDesktopMgmtAPI.getImageUsageByDeskId(UUID.fromString(desktopIdArr[0]));

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_TASK_NAME, new String[]{desktopType}).build()).iterator();
        final RelieveFaultDesktopBatchTaskHandler handler = new RelieveFaultDesktopBatchTaskHandler(idMap, iterator, auditLogAPI);
        handler.setDesktopFaultInfoAPI(deskFaultInfoAPI);
        handler.setCbbUserDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setDesktopType(desktopType);
        BatchTaskSubmitResult result = relieveFaultForDesktop(builder, desktopIdArr, handler, desktopType);
        return DefaultWebResponse.Builder.success(result);

    }

    /**
     * 变更云桌面
     *
     * @param request web请求
     * @param builder 批处理器
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("云桌面变更镜像")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/editImage")
    @EnableAuthority
    @EnableCustomValidate(validateMethod = "editImageValidate")
    public DefaultWebResponse editImage(EditDesktopImageWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "Param [request] must not be null");
        Assert.notNull(builder, "Param [builder] must not be null");

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(DefaultBatchTaskItem.builder().itemId(request.getId())
                .itemName(CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_NAME, new String[]{}).build()).iterator();
        CloudDesktopDetailDTO desktopDetail = cloudDesktopMgmtAPI.getDesktopDetailById(request.getId());
        final DesktopUpdateImageHandler handler =
                new DesktopUpdateImageHandler(iterator, request.getImageTemplateId(), desktopDetail.getDesktopName());
        handler.setCloudDesktopWebService(cloudDesktopWebService);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbDeskMgmtAPI(cbbDeskMgmtAPI);
        handler.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_NAME)//
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_RCO_DESKTOP_IMAGE_EDIT_TASK_DESC, desktopDetail.getDesktopName())//
                .registerHandler(handler)//
                .setUniqueId(UUID.randomUUID())//
                .start();

        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult relieveFaultForDesktop(BatchTaskBuilder builder, String[] desktopIdArr,
            final RelieveFaultDesktopBatchTaskHandler handler, String desktopType) throws BusinessException {
        BatchTaskSubmitResult result;
        if (desktopIdArr.length == 1) {
            handler.setIsBatch(false);
            UUID desktopId = UUID.fromString(desktopIdArr[0]);
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(desktopId);
            handler.setDesktopName(cloudDesktopDetailDTO.getDesktopName());
            handler.setUserName(cloudDesktopDetailDTO.getUserName());
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_ONE_TASK_DESC, cloudDesktopDetailDTO.getUserName(),
                            cloudDesktopDetailDTO.getDesktopName(), desktopType)
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_TASK_NAME, desktopType)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_TASK_DESC, desktopType).enableParallel()
                    .registerHandler(handler).start();
        }
        return result;
    }


    /**
     * 重置云桌面MAC地址
     *
     * @param request 请求
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("重置云桌面MAC地址")
    @RequestMapping(value = "/resetMac", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "重置云桌面MAC地址")})
    @EnableAuthority
    @EnableCustomValidate(validateMethod = "resetMacValidate")
    public DefaultWebResponse resetMac(ResetCloudDeskMacRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        CloudDesktopDetailDTO desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(request.getId());
        String desktopType = Constants.CLOUD_DESKTOP;
        if (desktopDetail.getImageUsage() == ImageUsageTypeEnum.APP) {
            desktopType = Constants.APP_CLOUD_DESKTOP;
        }

        if (!Objects.equals(CbbCloudDeskState.CLOSE.toString(), desktopDetail.getDesktopState())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CLOUD_DESK_NOT_CLOSED, new String[]{desktopType});
        }

        try {
            // 校验是否为第三方桌面
            cloudDesktopWebService.checkThirdPartyDesktop(request.getId(), UserBusinessKey.RCDC_RCO_RESET_CLOUD_DESK_MAC_THIRD_PARTY);
            CbbChangeDeskNicMacDTO cbbChangeDeskNicMacDTO = convertRetMacRequest2DTO(request);
            userDesktopMgmtAPI.resetDesktopMac(cbbChangeDeskNicMacDTO);
            CloudDesktopDetailDTO desktopDetailNew = userDesktopMgmtAPI.getDesktopDetailById(request.getId());
            // 发送水印配置
            deskStrategyAPI.sendDesktopStrategyWatermark(desktopDetailNew);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RESET_CLOUD_DESK_MAC_SUCCESS, desktopType, desktopDetail.getDesktopName(),
                    desktopDetail.getDesktopMac(), desktopDetailNew.getDesktopMac());
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RESET_CLOUD_DESK_MAC_FAIL, e, desktopType, desktopDetail.getDesktopName(),
                    e.getI18nMessage());
            LOGGER.error("云桌面[{}]重置mac地址失败，失败原因：{}", desktopDetail.getDesktopName(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * * 获取桌面vnc地址
     *
     * @param request web request
     * @return web response
     * @throws BusinessException business exception
     */
    @ApiOperation("获取桌面vnc地址")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1,
            descriptions = {"分级分权-区分权限，加入权限控制,批量设置用户身份验证配置"})})
    @RequestMapping(value = "/vnc", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse getVnc(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UUID deskId = request.getId();

        LOGGER.info("收到云桌面 [{}] 获取VNC地址请求", deskId);

        VmGraphicsDTO vncInfo = userDesktopMgmtAPI.queryVncByDeskId(deskId);
        return DefaultWebResponse.Builder.success(vncInfo);
    }

    /**
     * 变更软件管控策略
     *
     * @param request web请求
     * @param builder 批处理器
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("云桌面变更软件管控策略")
    @RequestMapping(value = "/editSoftwareStrategy")
    @ApiVersions({@ApiVersion(value = Version.V3_1_461), @ApiVersion(value = Version.V3_1_461, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public DefaultWebResponse editSoftwareStrategy(EditDesktopSoftwareStrategyWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "Param [request] must not be null");
        Assert.notNull(builder, "Param [builder] must not be null");

        UUID[] idArr = request.getIdArr();
        BatchTaskSubmitResult result = editDeskSoftwareStrategy(builder, idArr, request.getSoftwareStrategyId());
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 重置windows密码
     *
     * @param resetWindowsPwdWebRequest 重置windows密码请求参数
     * @param builder                   批任务
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("重置windows密码")
    @RequestMapping(value = "/resetWinPwd")
    public CommonWebResponse<?> resetWinPwd(ResetWindowsPwdWebRequest resetWindowsPwdWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(resetWindowsPwdWebRequest, "请求参数不能为空");
        Assert.notNull(builder, "builder must not null");
        Assert.notNull(resetWindowsPwdWebRequest.getDeskId(), "deskId 不能为空");

        String newPwd = "";
        try {
            newPwd = AesUtil.descrypt(resetWindowsPwdWebRequest.getNewPwd(), RedLineUtil.getRealAdminRedLine());
        } catch (Exception e) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DECRYPT_FAIL, e);
        }
        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(resetWindowsPwdWebRequest.getDeskId());
        if (deskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_RESET_WIN_PWD_MULTI_SESSION_TYPE_NOT_SUPPORT);
        }
        RcoUserDesktopDTO desktopDTO = cloudDesktopMgmtAPI.findByDesktopId(resetWindowsPwdWebRequest.getDeskId());
        IacUserDetailDTO userDetail = null;
        if (desktopDTO.getUserId() != null) {
            userDetail = userAPI.getUserDetail(desktopDTO.getUserId());
            if (IacUserTypeEnum.AD == userDetail.getUserType() || IacUserTypeEnum.LDAP == userDetail.getUserType()) {
                throw new BusinessException(BusinessKey.RCDC_RCO_GT_UPDATE_WINDOWS_ACCOUNT_ERROR_USER_TYPE_NOT_SUPPORT);
            }
        }

        final Iterator<DefaultBatchTaskItem> iterator = Stream
                .of(resetWindowsPwdWebRequest.getDeskId()).map(id -> DefaultBatchTaskItem.builder().itemId(id) //
                        .itemName(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_NAME,
                                new String[]{LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK)})
                        .build())
                .iterator();

        ResetDeskWindowsPwdHandlerRequest request = new ResetDeskWindowsPwdHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setCloudDesktopMgmtAPI(cloudDesktopMgmtAPI);
        request.setCbbResetWindowsPwdAPI(cbbResetWindowsPwdAPI);
        request.setAuditLogAPI(auditLogAPI);
        request.setMailMgmtAPI(mailMgmtAPI);
        request.setRcoGlobalParameterAPI(rcoGlobalParameterAPI);
        request.setDeskId(resetWindowsPwdWebRequest.getDeskId());
        request.setAccount(resetWindowsPwdWebRequest.getAccount());
        request.setPassword(newPwd);
        request.setEmail(userDetail != null ? userDetail.getEmail() : "");
        request.setThreadExecutor(THREAD_EXECUTOR_SEND_EMAIL);
        ResetDeskWindowsPwdHandler resetDeskWindowsPwdHandler = new ResetDeskWindowsPwdHandler(request);
        BatchTaskSubmitResult batchTaskSubmitResult = builder
                .setTaskName(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_NAME,
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK))
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_DESC,
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_DESK), desktopDTO.getDesktopName())
                .setUniqueId(resetWindowsPwdWebRequest.getDeskId()).registerHandler(resetDeskWindowsPwdHandler).start();
        return CommonWebResponse.success(batchTaskSubmitResult);
    }

    private BatchTaskSubmitResult editDeskSoftwareStrategy(BatchTaskBuilder builder, final UUID[] idArr, UUID softwareStrategyId)
            throws BusinessException {
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct()
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_ITEM_NAME, new String[]{}).build())
                        .iterator();
        BatchTaskSubmitResult result;
        String softwareStrategyName = LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_EMPTY);
        if (Objects.nonNull(softwareStrategyId)) {
            SoftwareStrategyDTO softwareStrategyDTO = softwareControlMgmtAPI.findSoftwareStrategyById(softwareStrategyId);
            softwareStrategyName = softwareStrategyDTO.getName();
        }
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(idArr[0]);
            result = editSingleDeskSoftwareStrategy(builder, iterator, softwareStrategyId, softwareStrategyName,
                    cloudDesktopDetailDTO.getDesktopName());
        } else {
            result = batchDeskSoftwareStrategyTaskSubmit(builder, iterator, softwareStrategyId, softwareStrategyName);
        }
        return result;
    }

    private BatchTaskSubmitResult editSingleDeskSoftwareStrategy(BatchTaskBuilder builder, final Iterator<DefaultBatchTaskItem> iterator,
            UUID softwareStrategyId, String softwareStrategyName, String desktopName) throws BusinessException {
        final EditDesktopSoftwareStrategyBatchTaskHandler handler =
                new EditDesktopSoftwareStrategyBatchTaskHandler(cloudDesktopMgmtAPI, iterator, auditLogAPI, desktopName);
        handler.setSoftwareStrategyId(softwareStrategyId);
        handler.setSoftwareStrategyName(softwareStrategyName);
        return builder.setTaskName(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_SINGLE_TASK_NAME)
                .setTaskDesc(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_SINGLE_TASK_DESC, desktopName, softwareStrategyName)
                .registerHandler(handler).start();
    }

    private BatchTaskSubmitResult batchDeskSoftwareStrategyTaskSubmit(BatchTaskBuilder builder, Iterator<DefaultBatchTaskItem> iterator,
                                                                      UUID softwareStrategyId, String softwareStrategyName) throws BusinessException {
        final EditDesktopSoftwareStrategyBatchTaskHandler handler =
                new EditDesktopSoftwareStrategyBatchTaskHandler(cloudDesktopMgmtAPI, iterator, auditLogAPI);
        handler.setSoftwareStrategyId(softwareStrategyId);
        handler.setSoftwareStrategyName(softwareStrategyName);
        return builder.setTaskName(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_BATCH_TASK_NAME)
                .setTaskDesc(SoftwareControlBusinessKey.RCDC_RCO_DESKTOP_SOFTWARE_STRATEGY_EDIT_BATCH_TASK_DESC).enableParallel()
                .registerHandler(handler).start();
    }


    /**
     * 变更用户配置策略
     *
     * @param request web请求
     * @param builder 批处理器
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("云桌面变更用户配置策略")
    @RequestMapping(value = "/editUserProfileStrategy")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public DefaultWebResponse editUserProfileStrategy(EditDesktopUserProfileStrategyWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "Param [request] must not be null");
        Assert.notNull(builder, "Param [builder] must not be null");

        UUID[] idArr = request.getIdArr();
        BatchTaskSubmitResult result = editUserProfileStrategy(builder, idArr, request.getUserProfileStrategyId());
        return DefaultWebResponse.Builder.success(result);
    }


    private BatchTaskSubmitResult editUserProfileStrategy(BatchTaskBuilder builder, final UUID[] idArr, UUID userProfileStrategyId)
            throws BusinessException {
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct()
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_ITEM_NAME, new String[]{}).build())
                        .iterator();
        BatchTaskSubmitResult result;
        String userProfileStrategyName = LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_EMPTY);
        if (Objects.nonNull(userProfileStrategyId)) {
            UserProfileStrategyDTO userProfileStrategy = userProfileMgmtAPI.findUserProfileStrategyById(userProfileStrategyId);
            userProfileStrategyName = userProfileStrategy.getName();
        }
        if (idArr.length == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(idArr[0]);
            result = editSingleUserProfileStrategy(builder, iterator, userProfileStrategyId, userProfileStrategyName,
                    cloudDesktopDetailDTO.getDesktopName());
        } else {
            result = batchUserProfileStrategyTaskSubmit(builder, iterator, userProfileStrategyId, userProfileStrategyName);
        }
        return result;
    }

    private BatchTaskSubmitResult editSingleUserProfileStrategy(BatchTaskBuilder builder, final Iterator<DefaultBatchTaskItem> iterator,
            UUID userProfileStrategyId, String userProfileStrategyName, String desktopName) throws BusinessException {
        final EditDesktopUserProfileStrategyBatchTaskHandler handler =
                new EditDesktopUserProfileStrategyBatchTaskHandler(cloudDesktopMgmtAPI, iterator, auditLogAPI, desktopName, cloudDesktopWebService);
        handler.setUserProfileStrategyId(userProfileStrategyId);
        handler.setUserProfileStrategyName(userProfileStrategyName);
        return builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_SINGLE_TASK_NAME)
                .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_SINGLE_TASK_DESC, desktopName,
                        userProfileStrategyName)
                .registerHandler(handler).start();
    }

    private BatchTaskSubmitResult batchUserProfileStrategyTaskSubmit(BatchTaskBuilder builder, Iterator<DefaultBatchTaskItem> iterator,
            UUID userProfileStrategyId, String userProfileStrategyName) throws BusinessException {
        final EditDesktopUserProfileStrategyBatchTaskHandler handler =
                new EditDesktopUserProfileStrategyBatchTaskHandler(cloudDesktopMgmtAPI, iterator, auditLogAPI, cloudDesktopWebService);
        handler.setUserProfileStrategyId(userProfileStrategyId);
        handler.setUserProfileStrategyName(userProfileStrategyName);
        return builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_BATCH_TASK_NAME)
                .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_BATCH_TASK_DESC).enableParallel()
                .registerHandler(handler).start();
    }

    private CbbChangeDeskNicMacDTO convertRetMacRequest2DTO(ResetCloudDeskMacRequest request) {
        CbbChangeDeskNicMacDTO dto = new CbbChangeDeskNicMacDTO();
        dto.setDeskId(request.getId());
        dto.setMac(request.getMac() == null ? request.getMac() : request.getMac().toUpperCase());
        return dto;
    }

    /**
     * 校验mac地址是否合法
     *
     * @param mac mac地址
     * @return 是否合法
     */
    private boolean isMac(String mac) {
        // mac地址正则表达式
        String macRegex = "^[A-Fa-f0-9]{2}(:[A-Fa-f0-9]{2}){5}$";
        Pattern pattern = Pattern.compile(macRegex);
        Matcher matcher = pattern.matcher(mac);
        return matcher.matches();
    }

    /**
     * 检测云桌面端口
     *
     * @param request web请求
     * @param builder builder
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("检测云桌面端口")
    @RequestMapping(value = "/checkDeskPort", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_221), @ApiVersion(value = Version.V3_2_221, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public DefaultWebResponse checkDesktopPort(CheckDesktopPortWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "Param [request] must not be null");
        Assert.notNull(builder, "Param [builder] must not be null");

        // 检测输入的端口范围
        List<Integer> validPortList = getValidPortList(request.getPortArr());
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(request.getIdArr()).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_ITEM_NAME, new String[]{}).build()).iterator();

        CheckDesktopPortBatchTaskHandler handler = new CheckDesktopPortBatchTaskHandler(iterator);
        handler.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
        handler.setDesktopAPI(desktopAPI);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setValidPortList(validPortList);

        BatchTaskSubmitResult result = builder.setTaskName(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_TASK_NAME)
                // 不允许同一时间多次触发检测端口
                .setUniqueId(CHECK_DESKTOP_PORT_TASK_ID).setTaskDesc(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_TASK_DESC).enableParallel()
                .enablePerformanceMode(CHECK_DESKTOP_PORT_TASK_ID, 100).registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private List<Integer> getValidPortList(Integer[] portArr) throws BusinessException {
        List<Integer> validPortList = Lists.newArrayList();
        if (portArr != null) {
            // 先去重后再校验端口范围(1-65535)，超出抛出异常
            List<Integer> portList = Arrays.stream(portArr).distinct().collect(Collectors.toList());
            for (Integer port : portList) {
                if (port >= 1 && port <= 65535) {
                    validPortList.add(port);
                } else {
                    throw new BusinessException(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_RANGE_FAIL, String.valueOf(port));
                }
            }
        }
        return validPortList;
    }

    /**
     * 移动分组
     *
     * @param request web请求
     * @param builder builder
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("移动分组")
    @RequestMapping(value = "/moveDesktop", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_221), @ApiVersion(value = Version.V3_2_221, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> moveDesktop(MoveDesktopWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        // 校验
        checkBeforeMoveTask(request.getDesktopPoolId(), request.getIdArr());
        final List<DefaultBatchTaskItem> taskItemList = Stream.of(request.getIdArr()).distinct().map(id -> DefaultBatchTaskItem.builder()//
                .itemId(id).itemName(BusinessKey.RCDC_RCO_MOVE_DESKTOP_ITEM_NAME, new String[]{}).build()).collect(Collectors.toList());

        MoveDesktopBatchTaskHandler handler = new MoveDesktopBatchTaskHandler(taskItemList, request.getDesktopPoolId());
        BatchTaskSubmitResult result = builder.setTaskName(BusinessKey.RCDC_RCO_MOVE_DESKTOP_TASK_NAME)
                .setTaskDesc(BusinessKey.RCDC_RCO_MOVE_DESKTOP_TASK_DESC).enableParallel().registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    private void checkBeforeMoveTask(UUID desktopPoolId, UUID[] idArr) throws BusinessException {
        DesktopPoolBasicDTO desktopPool = desktopPoolMgmtAPI.getDesktopPoolBasicById(desktopPoolId);
        if (desktopPool.getPoolState() != CbbDesktopPoolState.AVAILABLE && desktopPool.getPoolState() != CbbDesktopPoolState.UPDATING) {
            throw new BusinessException(BusinessKey.RCDC_RCO_MOVE_DESKTOP_POOL_STATE_ERROR);
        }

        // 检查这些桌面中是否有一个用户绑定多个池桌面且这些桌面属于同一个静态池
        if (ArrayUtils.isNotEmpty(idArr) && idArr.length > 1 && desktopAPI.existUserBindMoreDesktop(Arrays.asList(idArr))) {
            throw new BusinessException(BusinessKey.RCDC_RCO_MOVE_DESKTOP_EXIST_USER_ONE_MORE_DESK);
        }
    }

    /**
     * 关联用户，绑定桌面和用户的关联关系
     *
     * @param request web请求
     * @param builder builder
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("关联用户")
    @RequestMapping(value = "/bindUser", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_221), @ApiVersion(value = Version.V3_2_221, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public DefaultWebResponse bindUser(DesktopBindUserWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getUserIdArr();

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_ITEM_NAME, new String[]{}).build()).iterator();


        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            result = bindSingleUser(builder, idArr, iterator, request.getId());
        } else {
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(idArr[0]);
            String logName = userDetailDTO.getUserName();
            final BindUserBatchTaskHandler handler = new BindUserBatchTaskHandler(iterator);
            handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
            handler.setAuditLogAPI(auditLogAPI);
            handler.setCbbUserAPI(cbbUserAPI);
            handler.setCbbDeskMgmtAPI(cbbDeskMgmtAPI);
            handler.setUserName(logName);
            handler.setDeskId(request.getId());
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .start();
        }
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult bindSingleUser(BatchTaskBuilder builder, UUID[] idArr,
                                                 final Iterator<DefaultBatchTaskItem> iterator, UUID deskId) throws BusinessException {
        IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(idArr[0]);
        String logName = userDetailDTO.getUserName();
        final BindUserBatchTaskHandler handler = new BindUserBatchTaskHandler(iterator);
        handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbUserAPI(cbbUserAPI);
        handler.setCbbDeskMgmtAPI(cbbDeskMgmtAPI);
        handler.setUserName(logName);
        handler.setDeskId(deskId);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_SINGLE_TASK_NAME, logName)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_SINGLE_TASK_DESC, logName).registerHandler(handler).start();
    }

    /**
     * 取消关联用户
     *
     * @param request web请求
     * @param builder builder
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("取消关联用户")
    @RequestMapping(value = "/unbindUser", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_221), @ApiVersion(value = Version.V3_2_221, descriptions = {"取消关联用户"})})
    @EnableAuthority
    public DefaultWebResponse unbindUser(DesktopBindUserWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = request.getUserIdArr();

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_DESKTOP_UN_BIND_USER_ITEM_NAME, new String[]{}).build()).iterator();


        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            result = unbindSingleUser(builder, idArr, iterator, request.getId());
        } else {
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(idArr[0]);
            String logName = userDetailDTO.getUserName();
            final UnBindUserBatchTaskHandler handler = new UnBindUserBatchTaskHandler(iterator);
            handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
            handler.setAuditLogAPI(auditLogAPI);
            handler.setCbbUserAPI(cbbUserAPI);
            handler.setHostUserAPI(hostUserAPI);
            handler.setCbbDeskMgmtAPI(cbbDeskMgmtAPI);
            handler.setUserName(logName);
            handler.setDeskId(request.getId());
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_BATCH_TASK_DESC).enableParallel().registerHandler(handler)
                    .start();
        }
        return DefaultWebResponse.Builder.success(result);
    }

    private BatchTaskSubmitResult unbindSingleUser(BatchTaskBuilder builder, UUID[] idArr,
                                                   final Iterator<DefaultBatchTaskItem> iterator, UUID deskId) throws BusinessException {
        IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(idArr[0]);
        String logName = userDetailDTO.getUserName();
        final UnBindUserBatchTaskHandler handler = new UnBindUserBatchTaskHandler(iterator);
        handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbUserAPI(cbbUserAPI);
        handler.setHostUserAPI(hostUserAPI);
        handler.setCbbDeskMgmtAPI(cbbDeskMgmtAPI);
        handler.setUserName(logName);
        handler.setDeskId(deskId);
        return builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_UN_BIND_USER_SINGLE_TASK_NAME, logName)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_UN_BIND_USER_SINGLE_TASK_DESC, logName).registerHandler(handler).start();
    }

    /**
     * 获取桌面关联用户列表
     *
     * @param request web请求
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取桌面关联用户列表")
    @RequestMapping(value = "/bindUserList", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_221), @ApiVersion(value = Version.V3_2_221, descriptions = {"获取桌面关联用户列表"})})
    public CommonWebResponse<?> bindUserList(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(request.getId());
        List<HostUserDTO> hostUserDTOList = new ArrayList<>();
        if (cbbDeskDTO.getSessionType() == CbbDesktopSessionType.SINGLE) {
            RcoUserDesktopDTO userDesktopDTO = cloudDesktopMgmtAPI.findByDesktopId(request.getId());
            if (userDesktopDTO.getUserId() != null) {
                IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userDesktopDTO.getUserId());
                HostUserDTO hostUserDTO = new HostUserDTO();
                hostUserDTO.setId(userDetail.getId());
                hostUserDTO.setDesktopId(request.getId());
                hostUserDTO.setUserId(userDetail.getId());
                hostUserDTO.setUserName(userDetail.getUserName());

                hostUserDTOList.add(hostUserDTO);
            }
        } else {
            hostUserDTOList = hostUserAPI.findByDeskId(request.getId());

        }
        return CommonWebResponse.success(hostUserDTOList);
    }


    /**
     * 多会话桌面取消关联用户
     *
     * @param request web请求
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("多会话桌面取消关联用户")
    @RequestMapping(value = "multiSession/unbindUser", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"多会话桌面取消关联用户"})})
    public CommonWebResponse<?> multiSessionUnbindUser(IdArrWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notEmpty(request.getIdArr(), "idArr must not be null");
        HostUserDTO hostUserDTO = hostUserAPI.findById(request.getIdArr()[0]);
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(hostUserDTO.getDesktopId());
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(hostUserDTO.getUserId());
        try {
            hostUserAPI.removeById(request.getIdArr()[0]);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_DESKTOP_MULTI_SESSION_UNBIND_USER_FAIL,
                    e, cbbDeskDTO.getName(), userDetail.getUserName(), e.getI18nMessage());
            return CommonWebResponse.fail(BusinessKey.RCDC_RCO_DESKTOP_MULTI_SESSION_UNBIND_USER_FAIL,
                    new String[]{cbbDeskDTO.getName(), userDetail.getUserName(), e.getI18nMessage()});
        }

        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_DESKTOP_MULTI_SESSION_UNBIND_USER_SUCCESS, cbbDeskDTO.getName(), userDetail.getUserName());
        return CommonWebResponse.success(BusinessKey.RCDC_RCO_DESKTOP_MULTI_SESSION_UNBIND_USER_SUCCESS,
                new String[]{cbbDeskDTO.getName(), userDetail.getUserName()});
    }

    /**
     * 编辑云桌面密码显示配置
     *
     * @param webRequest web请求
     * @param builder    批处理器
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑云桌面密码显示配置")
    @RequestMapping(value = "/editRootPwdConfig")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"新增编辑云桌面密码显示配置接口"})})
    @EnableAuthority
    public DefaultWebResponse editRootPwdConfig(EditRootPwdWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = webRequest.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_ITEM_NAME, new String[]{}).build()).iterator();

        final EditDeskRootPwdConfigHandler handler = new EditDeskRootPwdConfigHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCloudDesktopMgmtAPI(cloudDesktopMgmtAPI);
        handler.setShowRootPwd(webRequest.getShowRootPwd());

        BatchTaskSubmitResult result;
        if (webRequest.getIdArr().length == 1) {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_SINGLE_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_SINGLE_TASK_DESC).enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 获取桌面真实规格详情
     *
     * @param request web请求
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取桌面规格详情")
    @RequestMapping(value = "/spec/detail")
    public DefaultWebResponse getDeskSpecDetail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        DeskSpecDetailDTO specDetailDTO = cloudDesktopMgmtAPI.getDesktopSpecDetail(request.getId());

        return DefaultWebResponse.Builder.success(specDetailDTO);
    }
}
