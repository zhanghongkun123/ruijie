package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationUpgradeAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.request.AppGetVersionRequest;
import com.ruijie.rcos.base.upgrade.module.def.dto.AppUpgradeVersionDTO;
import com.ruijie.rcos.base.upgrade.module.def.enums.ArchType;
import com.ruijie.rcos.base.upgrade.module.def.enums.OsType;
import com.ruijie.rcos.base.upgrade.module.def.enums.PacketProductType;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbEditVersionBaseInfoRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.util.GtAgentUtil;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.query.DefaultConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.utils.PageRequestUtils;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ImageNotCreateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RemoteTerminalEditImageStateResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateCmLauncherStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateUwsLauncherStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.VgpuExtraInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.RemoteTerminalEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.ViewTerminalWithImageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.request.RemoteTerminalEditImageDownStateRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.request.RemoteTerminalEditImageTemplateRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ImageTemplateUtil;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.PublicBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.builder.CloudDesktopAPIRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.DeliveryObjectType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.RemoteTerminalEditImageLog;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.SnapshotRestoreType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.imagetemplate.CheckSpaceResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.DeskStrategyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.utils.DesktopStrategyUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktopspec.utils.DeskSpecUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.dto.CreateImageTemplateByCloneDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.dto.RcoImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.enums.ImageTemplateStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.validation.ImageTemplateValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.vo.ImageTemplateDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ResetWindowsPwdWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.CreateImageTemplateHandlerFactory;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.StartTerminalImageTemplateHandlerFactory;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.StopImageTemplateHandlerFactory;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.StopTerminalImageTemplateHandlerFactory;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.request.CreateImageTemplateByOsFileRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.DiskBaseInfoMgmtWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.*;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalModelAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalModelDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.VersionCompareResultEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.util.VersionUtil;
import com.ruijie.rcos.sk.base.I18nKey;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.PageResponseContent;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.*;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/3/29 <br>
 *
 * @author yyz
 */
@Api(tags = "镜像模板")
@Controller
@RequestMapping("/rco/clouddesktop/imageTemplate")
@EnableCustomValidate(validateClass = ImageTemplateValidation.class)
public class ImageTemplateController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ImageTemplateController.class);

    private static final String IMAGE_ROLE_TYPE = "imageRoleType";

    private static final String IMAGE_ID = "id";

    private static final String ROOT_IMAGE_ID = "rootImageId";

    private static final String CREATE_TIME = "createTime";

    private static final String CBB_DESKTOP_ID = "cbbDesktopId";

    private static final String DESKTOP_POOL_ID = "desktopPoolId";

    private static final String IMAGE_TEMPLATE_ID = "imageTemplateId";

    private static final String DESK_STATE = "deskState";

    private static final String PATTERN = "pattern";

    private static final String DESKTOP_POOL_TYPE = "desktopPoolType";

    private static final String DESKTOP_TYPE = "desktopType";

    private static final String SYSTEM_SIZE = "systemSize";

    private static final String POOL_STATE = "poolState";

    private static final String SOURCE_SNAPSHOT_ID = "sourceSnapshotId";

    private static final String IMAGE_TEMPLATE_STATE = "imageTemplateState";

    private static final String POOL_MODEL = "poolModel";

    private static final String TERMINAL_DOWNLOAD_IMAGE_COMPLETED = "COMPLETED";

    private static final String IMAGE_VERSION_ID = "imageVersionId";

    private static final String DESK_ID_ARR = "deskIdArr";

    private static final String DESK_POOL_ID_ARR = "deskPoolIdArr";

    private static final String DESK_STATE_ARR = "deskStateArr";

    private static final String PATTERN_ARR = "patternArr";

    private static final String POOL_TYPE_ARR = "poolTypeArr";

    private static final String POOL_TYPE = "poolType";

    private static final String SESSION_TYPE_ARR = "sessionTypeArr";

    public static final String SESSION_TYPE = "sessionType";

    @Autowired
    private TerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private CbbTerminalModelAPI cbbTerminalModelAPI;

    @Autowired
    private CbbRemoteTerminalImageMgmtAPI cbbRemoteTerminalImageMgmtAPI;

    @Autowired
    private CbbRemoteTerminalImageAPI cbbRemoteTerminalImageAPI;

    @Autowired
    private PageQueryBuilderFactory requestBuilderFactory;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private EstClientMgmtAPI estClientMgmtAPI;

    @Autowired
    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    @Autowired
    private StopImageTemplateHandlerFactory stopImageTemplateHandlerFactory;

    @Autowired
    private CreateImageTemplateHandlerFactory createImageTemplateHandlerFactory;

    @Autowired
    private StartTerminalImageTemplateHandlerFactory startTerminalImageTemplateHandlerFactory;

    @Autowired
    private StopTerminalImageTemplateHandlerFactory stopTerminalImageTemplateHandlerFactory;

    @Autowired
    private TerminalImageEditAPI terminalImageEditAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CmsUpgradeAPI cmsUpgradeAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private TerminalSelectAPI terminalSelectAPI;

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private RemoteAssistInfoOperateAPI remoteAssistInfoOperateAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private StoragePoolAPI storagePoolAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private UamAppDiskAPI uamAppDiskAPI;

    @Autowired
    private CbbUamAppTestAPI cbbUamAppTestAPI;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private CbbResetWindowsPwdAPI cbbResetWindowsPwdAPI;

    @Autowired
    private CbbGuestToolIsoInfoAPI guestToolIsoInfoAPI;

    @Autowired
    private DiskBaseInfoMgmtWebService webService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;


    private static final String GT_VERSION_LOW_TIP =
            LocaleI18nResolver.resolve(com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_CLOUDDESKTOP_GUEST_TOOL_VERSION_LOW);

    private static final String GT_NOT_AVAILABLE_TIP =
            LocaleI18nResolver.resolve(com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_CLOUDDESKTOP_GUEST_TOOL_NOT_AVAILABLE);

    private static final String RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_GT_LOW_TIP =
            LocaleI18nResolver.resolve(RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_GT_LOW);

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private BaseApplicationUpgradeAPI baseApplicationUpgradeAPI;

    @Autowired
    private ImageTemplateValidation imageTemplateValidation;

    private static final int DEFAULT_IMAGE_REMAIN_SPACE_PERCENT = 10;

    private static final UUID RUN_IMAGE_TEMPLATE_UNIQUEID = UUID.nameUUIDFromBytes("start_image".getBytes(StandardCharsets.UTF_8));

    private static final String USABLE_SIZE_UNIT_G = "G";


    /**
     * 多会话支持系统版本列表
     */
    private static final List<String> MULTI_SESSION_IMAGE_OS_LIST = Arrays.asList(CbbOsType.WIN_SERVER_2016_64.getOsName(),
            CbbOsType.WIN_SERVER_2019_64.getOsName(), CbbOsType.WIN_SERVER_2022_64.getOsName());

    /**
     * 放弃镜像编辑
     *
     * @param webRequest 请求参数
     * @param builder 批处理
     * @param sessionContext sessionContext
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("放弃镜像编辑")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "giveup", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> abortEditImageTemplate(IdWebRequest webRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        // 检测管理员镜像编辑信息
        CbbImageTemplateEditDTO cbbImageTemplateEditDTO =
                new CbbImageTemplateEditDTO(webRequest.getId(), sessionContext.getUserId(), sessionContext.getUserName());
        cbbImageTemplateMgmtAPI.checkIfEditingByOtherAdminWhenAbort(cbbImageTemplateEditDTO);

        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(webRequest.getId());
        String imageName = cbbGetImageTemplateInfoDTO.getImageName();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(webRequest.getId()).map(id -> DefaultBatchTaskItem.builder().itemId(id) //
                .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_ABORT_TASK_NAME)).build()).iterator();
        AbortEditImageTemplateHandlerRequest request = new AbortEditImageTemplateHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        request.setCmsUpgradeAPI(cmsUpgradeAPI);
        AbortEditImageTemplateHandler handler = new AbortEditImageTemplateHandler(request);
        BatchTaskSubmitResult batchTaskSubmitResult = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_ABORT_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_ABORT_DESC, imageName).setUniqueId(webRequest.getId())
                .registerHandler(handler).start();

        return CommonWebResponse.success(batchTaskSubmitResult);
    }

    /**
     * 编辑镜像请求
     *
     * @param request 请求参数，镜像模板ID
     * @param sessionContext 会话
     * @return web response
     * @throws BusinessException ex
     */
    @ApiOperation("驱动安装并启动")
    @RequestMapping(value = "/editRequest")
    public DefaultWebResponse editRequest(IdWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null!");

        CbbImageTemplateEditDTO imageTemplateEditRequest = new CbbImageTemplateEditDTO();
        imageTemplateEditRequest.setAdminId(sessionContext.getUserId());
        imageTemplateEditRequest.setImageTemplateId(request.getId());
        imageTemplateEditRequest.setAdminName(sessionContext.getUserName());

        // 校验EstClient数量
        checkEstClientNum(request.getId());

        cbbImageTemplateMgmtAPI.editImageTemplate(imageTemplateEditRequest);
        return DefaultWebResponse.Builder.success();
    }

    /**
     * 创建并启动镜像编辑
     *
     * @param webRequest 请求参数
     * @param builder 批处理
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建并启动镜像编辑")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableCustomValidate(validateMethod = "updateImageTemplateValidate")
    @RequestMapping(value = "start", method = RequestMethod.POST)
    public CommonWebResponse<?> createAndRunVmForEditImage(UpdateImageTemplateWebRequest webRequest, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");
        UUID imageTemplateId = webRequest.getId();
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
        // 存在链接克隆桌面，进行系统盘大小和数据盘大小的校验
        validateCanEditSystemDiskSize(imageTemplateDetail, webRequest);
        validateCanEditDataDiskSize(imageTemplateDetail, webRequest);

        // 校验VGPU信息
        validateVgpuInfo(webRequest.getAdvancedConfig(), webRequest.getImageSystemType(), imageTemplateDetail.getCbbImageType(), imageTemplateId);

        validTciLinuxDataDisk(imageTemplateDetail.getCbbImageType(), webRequest.getImageSystemType(),
                webRequest.getAdvancedConfig().getImageDiskList());

        // 校验EstClient数量
        checkEstClientNum(imageTemplateId);

        // 校验计算集群信息
        validateVmClusterStoragePool(webRequest.getAdvancedConfig(), imageTemplateDetail);

        // 检查版本数量是否达到最大值
        cbbImageTemplateMgmtAPI.validateVersionNumLimit(imageTemplateId);

        // 检查变更的操作系统是否支持镜像类型（操作系统有变更时才进行校验，避免存量镜像模板不可编辑）
        if (!webRequest.getImageSystemType().equals(imageTemplateDetail.getOsType())) {
            imageTemplateValidation.imageTypeAndImageOsTypeValidate(imageTemplateDetail.getCbbImageType(), webRequest.getImageSystemType());
        }

        final CbbUpdateImageTemplateDTO cbbUpdateImageTemplateDTO = CloudDesktopAPIRequestBuilder.buildUpdateImageTemplateRequest(webRequest);

        final CbbConfigVmForEditImageTemplateDTO vmConfig =
                CloudDesktopAPIRequestBuilder.buildConfigVmForEditImageTemplateRequest(webRequest.getAdvancedConfig());
        vmConfig.setImageTemplateId(cbbUpdateImageTemplateDTO.getImageTemplateId());

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(webRequest.getId()).map(id -> DefaultBatchTaskItem.builder().itemId(id) //
                .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_VM_TASK_NAME)).build()).iterator();
        CreateAndRunVmForEditImageHandlerRequest request = new CreateAndRunVmForEditImageHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        request.setCmsUpgradeAPI(cmsUpgradeAPI);
        request.setCbbUpdateImageTemplateDTO(cbbUpdateImageTemplateDTO);
        request.setVmConfig(vmConfig);
        request.setCbbImageDriverMgmtAPI(cbbImageDriverMgmtAPI);
        request.setWebService(webService);
        CreateAndRunVmForEditImageHandler handler = new CreateAndRunVmForEditImageHandler(request);
        BatchTaskSubmitResult batchTaskSubmitResult;

        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
        // 更改提示语
        resetVmStartTaskDesc(cbbGetImageTemplateInfoDTO, builder);
        if (serverModelAPI.isIdvModel() || serverModelAPI.isMiniModel()) {
            // rcm或mini模式下只允许一个镜像在编辑
            imageTemplateAPI.checkHasImageRunning(webRequest.getId());
            builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_VM_TASK_NAME).setUniqueId(RUN_IMAGE_TEMPLATE_UNIQUEID)
                    .registerHandler(handler);
        } else {
            builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_VM_TASK_NAME).setUniqueId(webRequest.getId())
                    .registerHandler(handler);
        }
        batchTaskSubmitResult = builder.start();
        return CommonWebResponse.success(batchTaskSubmitResult);
    }

    /**
     * @param cbbGetImageTemplateInfoDTO
     * @param builder
     */
    private void resetVmStartTaskDesc(CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO, BatchTaskBuilder builder) {
        UUID imageId = cbbGetImageTemplateInfoDTO.getId();
        String mac = cbbGetImageTemplateInfoDTO.getMac();
        String imageName = cbbGetImageTemplateInfoDTO.getImageName();
        if (StringUtils.isBlank(mac)) {
            builder.setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_VM_DESC, imageName);
            return;
        }

        List<CbbImageTemplateDTO> cbbImageTemplateDTOList = cbbImageTemplateMgmtAPI.getImageTemplatesExistMac(imageId, mac);
        if (CollectionUtils.isNotEmpty(cbbImageTemplateDTOList)) {
            String imageNames = cbbImageTemplateDTOList.stream().map(CbbImageTemplateDTO::getImageTemplateName)
                    .collect(Collectors.joining(Constants.COMMA_SEPARATION_CHARACTER));
            builder.setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_INFO_MAC_EXIST, imageNames, mac);
            return;
        }
        List<CbbDeskDTO> cbbDeskDTOList = cbbVDIDeskMgmtAPI.getDesksExistMac(mac);
        if (CollectionUtils.isNotEmpty(cbbDeskDTOList)) {
            String deskNames = cbbDeskDTOList.stream().map(CbbDeskDTO::getName).collect(Collectors.joining(Constants.COMMA_SEPARATION_CHARACTER));
            builder.setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESK_INFO_MAC_EXIST, deskNames, mac);
            return;
        }
        builder.setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_VM_DESC, imageName);
    }

    /**
     * 使用已有镜像克隆新镜像
     *
     * @param webRequest 请求参数
     * @param builder 批处理
     * @param sessionContext session上下文
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("使用已有镜像克隆新镜像")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "clone", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "createImageTemplateByCloneValidate")
    @EnableAuthority
    public CommonWebResponse<?> createImageTemplateByClone(CloneImageTemplateWebRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(webRequest.getAdvancedConfig(), "advancedConfig is not null");
        Assert.notNull(builder, "builder is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        // 检查是否能运行创建镜像 1.由于存在终端上传镜像 2.终端克隆新镜像需要检查是否允许创建镜像 （模仿镜像创建）

        UUID platformId = webRequest.getAdvancedConfig().getPlatformId();
        AllowCreateImageTemplateDTO allowCreateImageTemplateDTO =
                imageTemplateAPI.checkIsAllowCreateAndHasImage(sessionContext.getUserId(), null, platformId);
        if (!allowCreateImageTemplateDTO.getEnableCreate()) {
            // 服务器备份过程中，不允许创建
            if (allowCreateImageTemplateDTO.getImageNotCreateEnum() == ImageNotCreateEnum.BACKUP) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SERVER_BACKUP_NOT_ALLOW_CREATING);
            }

            // 如果管理员有这个镜像的数据权限 提示只允许同时只有一个镜像模板创建中，请稍后重试
            if (allowCreateImageTemplateDTO.getHasImage()) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_ONLY_ONE_IMAGE_TEMPLATE_ALLOW_CREATING);
            } else {
                // 其他管理员目前也在创建镜像模板中，请稍后重试
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OTHER_ADMIN_CREATE_IMAGE_TEMPLATE);
            }
        }

        // 检查是否重名
        CbbCheckImageNameDuplicationDTO nameDuplicationDTO = new CbbCheckImageNameDuplicationDTO();
        nameDuplicationDTO.setImageName(webRequest.getImageName());
        if (cbbImageTemplateMgmtAPI.checkImageNameDuplication(nameDuplicationDTO)) {
            CbbGetImageTemplateInfoDTO getImageTemplateInfoDTO =
                    cbbImageTemplateMgmtAPI.getImageTemplateInfoByImageTemplateName(webRequest.getImageName());
            if (StringUtils.isEmpty(getImageTemplateInfoDTO.getRootImageName())) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CLONE_NAME_DUPLICATION, webRequest.getImageName());
            } else {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CLONE_VERSION_NAME_DUPLICATION,
                        getImageTemplateInfoDTO.getImageName(), getImageTemplateInfoDTO.getRootImageName());
            }
        }

        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(webRequest.getId());
        String sourceImageName = cbbGetImageTemplateInfoDTO.getImageName();

        // 添加VGPU信息
        validateVgpuInfo(webRequest.getAdvancedConfig(), cbbGetImageTemplateInfoDTO.getCbbOsType(), cbbGetImageTemplateInfoDTO.getCbbImageType(),
                webRequest.getId());

        validTciLinuxDataDisk(webRequest.getCbbImageType(), cbbGetImageTemplateInfoDTO.getCbbOsType(),
                webRequest.getAdvancedConfig().getImageDiskList());

        CreateImageTemplateByCloneDTO createImageTemplateByCloneDTO = new CreateImageTemplateByCloneDTO();
        BeanUtils.copyProperties(webRequest, createImageTemplateByCloneDTO);
        // 镜像多版本, 未填则为false
        createImageTemplateByCloneDTO
                .setEnableMultipleVersion(webRequest.getEnableMultipleVersion() != null ? webRequest.getEnableMultipleVersion() : false);
        // 设置管理员ID
        createImageTemplateByCloneDTO.setAdminId(sessionContext.getUserId());
        List<CreateImageTemplateByCloneDTO> imageList = new ArrayList<>();
        imageList.add(createImageTemplateByCloneDTO);
        CreateImageTemplateByCloneDTO[] imageArr = imageList.toArray(new CreateImageTemplateByCloneDTO[0]);
        final Iterator<CreateImageTemplateByCloneBatchTaskItem> iterator = Stream.of(imageArr)
                .map(dto -> CreateImageTemplateByCloneBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                        .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CLONE_TASK_NAME)) //
                        .itemData(dto).build())
                .iterator();
        CreateImageTemplateByCloneHandlerRequest request = new CreateImageTemplateByCloneHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        request.setCmsUpgradeAPI(cmsUpgradeAPI);
        request.setAdminDataPermissionAPI(adminDataPermissionAPI);
        CreateImageTemplateByCloneHandler handler = new CreateImageTemplateByCloneHandler(request);
        //克隆过程描述
        String cloneDesc;
        if (Objects.nonNull(cbbGetImageTemplateInfoDTO.getRootImageId())) {
            //若父镜像不为空，说明从镜像模板版本克隆
            cloneDesc = CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_VERSION_CLONE_DESC;
        } else {
            //若父镜像为空，说明从镜像模板克隆
            cloneDesc = CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CLONE_DESC;
        }

        BatchTaskSubmitResult batchTaskSubmitResult =
                builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CLONE_TASK_NAME, sourceImageName, webRequest.getImageName())
                        .setTaskDesc(cloneDesc, sourceImageName, webRequest.getImageName())
                        .registerHandler(handler).start();

        return CommonWebResponse.success(batchTaskSubmitResult);
    }

    /**
     * 通过镜像文件创建镜像
     *
     * @param webRequest 请求参数
     * @param builder 批处理
     * @param sessionContext session上下文
     * @return 创建结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("通过镜像文件创建镜像")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "createImageTemplateByOsFileValidate")
    @EnableAuthority
    public CommonWebResponse<?> createImageTemplateByOsFile(CreateImageTemplateByOsFileWebRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        Assert.notNull(builder, "builder is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        UUID platformId = webRequest.getAdvancedConfig().getPlatformId();
        AllowCreateImageTemplateDTO allowCreateImageTemplateDTO =
                imageTemplateAPI.checkIsAllowCreateAndHasImage(sessionContext.getUserId(), null, platformId);
        if (!allowCreateImageTemplateDTO.getEnableCreate()) {
            // 服务器备份过程中，不允许创建
            if (allowCreateImageTemplateDTO.getImageNotCreateEnum() == ImageNotCreateEnum.BACKUP) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SERVER_BACKUP_NOT_ALLOW_CREATING);
            }

            // 如果管理员有这个镜像的数据权限 提示只允许同时只有一个镜像模板创建中，请稍后重试
            if (allowCreateImageTemplateDTO.getHasImage()) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_ONLY_ONE_IMAGE_TEMPLATE_ALLOW_CREATING);
            } else {
                // 其他管理员目前也在创建镜像模板中，请稍后重试
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_OTHER_ADMIN_CREATE_IMAGE_TEMPLATE);
            }
        }

        // vgpu信息
        validateVgpuInfo(webRequest.getAdvancedConfig(), webRequest.getImageSystemType(), webRequest.getCbbImageType(), null);
        validTciLinuxDataDisk(webRequest.getCbbImageType(), webRequest.getImageSystemType(), webRequest.getAdvancedConfig().getImageDiskList());

        // 创建hadle请求
        CreateImageTemplateByOsFileRequest createImageTemplateByOsFileRequest = new CreateImageTemplateByOsFileRequest();
        createImageTemplateByOsFileRequest.buildCreateImageTemplateByOsFileRequest(webRequest);
        // 设置管理员ID
        createImageTemplateByOsFileRequest.setAdminId(sessionContext.getUserId());
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CREATE_TASK_NAME));
        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_CREATE_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_IMAGE_DESC, webRequest.getImageName())
                .registerHandler(createImageTemplateHandlerFactory.createHandler(batchTaskItem, auditLogAPI, createImageTemplateByOsFileRequest))
                .start();

        return CommonWebResponse.success(result);
    }

    /**
     * tci 国产化镜像不支持数据盘
     *
     * @param cbbImageType 镜像类型
     * @param cbbOsType 系统类型
     * @param imageDiskList 数据破列表
     * @throws BusinessException 业务异常
     */
    private void validTciLinuxDataDisk(CbbImageType cbbImageType, CbbOsType cbbOsType, @Nullable List<CbbImageDiskDTO> imageDiskList)
            throws BusinessException {
        Assert.notNull(cbbImageType, "cbbImageType can not be null");
        Assert.notNull(cbbOsType, "cbbOsType can not be null");
        if (CollectionUtils.isEmpty(imageDiskList)) {
            return;
        }

        if (cbbImageType == CbbImageType.VOI && (cbbOsType == CbbOsType.KYLIN_64 || cbbOsType == CbbOsType.UOS_64)) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_CREATE_TCI_LINUX_NOT_SUPPORT_DISK);
        }
    }

    /**
     * 删除镜像
     *
     * @param webRequest 请求参数
     * @param builder 批量任务生成器
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除镜像")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse deleteImageTemplate(DeleteImageTemplateWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");

        final UUID[] idArr = webRequest.getIdArr();
        Boolean shouldOnlyDeleteDataFromDb = webRequest.getShouldOnlyDeleteDataFromDb();
        Map<UUID, String> imageNameMap = new HashMap<>();
        // 将原来的代码移到beforeBatchTask，并添加审计日志
        boolean isDeleteTemplate = beforeBatchTask(shouldOnlyDeleteDataFromDb, idArr, imageNameMap);

        String taskNameKey = isDeleteTemplate ? CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_BATCH_DELETE_TASK_NAME
                : CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_VERSION_BATCH_DELETE_TASK_NAME;
        String prefix = WebBatchTaskUtils.getDeletePrefix(webRequest.getIdArr(), webRequest.getShouldOnlyDeleteDataFromDb());
        String itemPrefix = WebBatchTaskUtils.getDeletePrefix(webRequest.getShouldOnlyDeleteDataFromDb());
        Iterator<? extends I18nBatchTaskItem<?>> iterator = Stream.of(idArr).distinct() //
                .map(id -> I18nBatchTaskItem.Builder.build(id, () -> LocaleI18nResolver.resolve(taskNameKey, itemPrefix), imageNameMap.get(id)))
                .iterator();
        Boolean shouldForceDelete = webRequest.getShouldOnlyDeleteDataFromDb();
        final DeleteImageTemplateBatchHandler handler = new DeleteImageTemplateBatchHandler(cbbImageTemplateMgmtAPI, iterator, shouldForceDelete);
        BatchTaskSubmitResult result = I18nBatchTaskBuilder.wrap(builder).setAuditLogAPI(auditLogAPI).setTaskName(taskNameKey, prefix)
                .setUniqueId(webRequest.getIdArr()[0]).registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private boolean beforeBatchTask(Boolean shouldOnlyDeleteDataFromDb, UUID[] idArr, Map<UUID, String> imageNameMap) throws BusinessException {
        boolean isDeleteTemplate = true;
        CbbImageTemplateDetailDTO imageTemplateDetail = null;
        try {
            for (UUID imageId : idArr) {
                if (!cbbImageTemplateMgmtAPI.existsImageTemplate(imageId)) {
                    throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DELETE_NOT_EXIST);
                }
                imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
                if (imageTemplateDetail.getImageRoleType() == ImageRoleType.VERSION) {
                    isDeleteTemplate = false;
                }
                imageNameMap.put(imageId, imageTemplateDetail.getImageName());
                if (imageTemplateDetail.getNewestVersion()) {
                    // 如果是最新的版本，不允许删除
                    throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DIRECT_DELETE_LATEST_IMAGE_VERSION_NOT_SUPPORTED);
                }
                List<UUID> idCandidateList = new ArrayList<>();
                if (imageTemplateDetail.getEnableMultipleVersion()) {
                    List<CbbImageTemplateDetailDTO> imageVersionList =
                            cbbImageTemplateMgmtAPI.listImageTemplateVersionByRootImageId(imageTemplateDetail.getId());
                    idCandidateList = imageVersionList.stream().map(CbbImageTemplateDetailDTO::getId).collect(Collectors.toList());
                } else {
                    idCandidateList.add(imageTemplateDetail.getId());
                }
                for (UUID id : idCandidateList) {
                    cbbUamAppTestAPI.isExistRelateAppTestByImageTemplateIdThrowEx(id);
                    cbbAppDeliveryMgmtAPI.isExistRelateDeliveryGroupByImageTemplateIdThrowEx(id);
                    uamAppDiskAPI.isExistRelateAppByImageIdThrowEx(id);
                    desktopPoolMgmtAPI.isExistRelateDesktopPoolByImageIdThrowEx(id);
                }
                // 应用池绑定检测
                rcaHostAPI.isExistRelateRcaPoolByImageIdThrowEx(imageTemplateDetail.getId());

            }
        } catch (BusinessException e) {
            String prefix = WebBatchTaskUtils.getDeletePrefix(idArr, shouldOnlyDeleteDataFromDb);
            LOGGER.error("删除镜像模板[{}]失败", JSON.toJSONString(idArr), e);
            logDeleteImageFail(prefix, idArr, e.getI18nMessage(), imageTemplateDetail, isDeleteTemplate);
            throw e;
        } catch (Exception e) {
            String prefix = WebBatchTaskUtils.getDeletePrefix(idArr, shouldOnlyDeleteDataFromDb);
            LOGGER.error("删除镜像模板[{}]失败", JSON.toJSONString(idArr), e);
            logDeleteImageFail(prefix, idArr, e.getMessage(), imageTemplateDetail, isDeleteTemplate);
            throw e;
        }
        return isDeleteTemplate;
    }

    private void logDeleteImageFail(String prefix, UUID[] idArr, String message, CbbImageTemplateDetailDTO imageTemplateDetail,
            boolean isDeleteTemplate) {
        if (idArr.length == 1 && Objects.nonNull(imageTemplateDetail)) {
            String businessKey = isDeleteTemplate ? CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DELETE_ITEM_FAIL_DESC
                    : CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_VERSION_DELETE_ITEM_FAIL_DESC;
            auditLogAPI.recordLog(businessKey, imageTemplateDetail.getImageName(), message, prefix);
            return;
        }
        String businessKey = isDeleteTemplate ? CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DELETE_FAIL_LOG
                : CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_VERSION_DELETE_FAIL_LOG;
        auditLogAPI.recordLog(businessKey, message, prefix);
    }

    /**
     * 分页查询镜像模板
     *
     * @param webRequest 请求参数
     * @param sessionContext session信息
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页查询镜像模板")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DefaultWebResponse listImageTemplate(ListImageWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        LocalImageTemplatePageRequest pageSearchRequest = LocalImageTemplatePageRequestCovertUtils.covert(webRequest);

        // 管理员管理 查询镜像列表 会传标示位 是否不需要数据权限查询
        Boolean isNoPermission = Objects.requireNonNull(webRequest).getNoPermission();
        // 如果是管理员管理发的请求 不需要数据权限 放行
        if (Boolean.TRUE.equals(isNoPermission)) {
            LOGGER.info("管理员管理，查询镜像列表，不需要数据权限拦截");
        } else if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            // 如果是超级管理员 查询全部 不是超级管理员或者内置的系统管理员
            ListImageIdRequest listImageIdRequest = new ListImageIdRequest();
            listImageIdRequest.setAdminId(sessionContext.getUserId());
            List<String> imageIdStrList = adminDataPermissionAPI.listImageIdByAdminId(listImageIdRequest).getImageIdList();
            if (CollectionUtils.isEmpty(imageIdStrList)) {
                // 没有镜像关联权限 直接空
                return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", Collections.emptyList()));
            } else {
                Optional<ImageRoleType> imageRoleTypeOptional =
                        PageRequestUtils.getObject(webRequest.getExactMatchArr(), IMAGE_ROLE_TYPE, ImageRoleType.class);
                if (imageRoleTypeOptional.isPresent() && imageRoleTypeOptional.get() == ImageRoleType.VERSION) {
                    // 添加镜像ID 筛选
                    appendImageIdMatchEqual(pageSearchRequest, imageIdStrList, ROOT_IMAGE_ID);
                } else {
                    // 添加镜像ID 筛选
                    appendImageIdMatchEqual(pageSearchRequest, imageIdStrList, IMAGE_ID);
                }

            }
        }

        // 如果是应用镜像，并且传递的是云主机策略，则找对应的主策略
        if (pageSearchRequest.getImageUsage() == ImageUsageTypeEnum.APP && pageSearchRequest.getMainStrategyId() != null) {
            RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
            relationshipDTO.setId(pageSearchRequest.getMainStrategyId());
            RcaMainStrategyDTO rcaMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);
            if (rcaMainStrategyDTO != null) {
                LOGGER.info("应用镜像筛选，通过主机策略={}，查询到主策略={}", pageSearchRequest.getMainStrategyId(), rcaMainStrategyDTO.getDeskStrategyId());
                pageSearchRequest.setStrategyId(rcaMainStrategyDTO.getDeskStrategyId());
            }
        }

        DefaultPageResponse<CbbImageTemplateDetailDTO> response = cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate(pageSearchRequest);
        if (response.getItemArr() == null || response.getItemArr().length == 0) {
            return DefaultWebResponse.Builder.success();
        }
        // 根据用户配置策略id查询镜像模板是否可用
        if (pageSearchRequest.getUserProfileStrategyId() != null) {
            getImageTemplateByUserProfileStrategyId(response, pageSearchRequest.getUserProfileStrategyId(), pageSearchRequest.getDesktopPoolId(),
                    pageSearchRequest.getDesktopId());
        }

        // 根据用桌面id查询镜像模板是否可用
        if (pageSearchRequest.getDesktopId() != null) {
            getImageTemplateByDesktopId(response, pageSearchRequest.getDesktopId());
        }

        // 根据用桌面池id查询镜像模板是否可用
        if (pageSearchRequest.getDesktopPoolId() != null) {
            getImageTemplateByDesktopPoolId(response, pageSearchRequest.getDesktopPoolId());
        }
        List<ImageTemplateDetailVO> templateDetailVOArrayList = new ArrayList<>();

        // 绑定这个云桌面的终端是否支持TC引导
        Boolean isSupportTc = Boolean.TRUE;
        // 桌面是否开启系统盘自动扩容
        boolean enableFullSystemDisk = false;
        Integer currentImageSystemSize = 0;
        Integer currentImageDataSize = 0;
        CbbOsType currentCbbOsType = null;
        UUID currentImageId = null;
        if (pageSearchRequest.getDesktopId() != null) {
            // 变更桌面镜像模板 从磁盘表中获取系统盘大小（VDI/IDV/TCI） 镜像数据盘（镜像D盘，只有IDV/TCI,磁盘类型为DATA）大小
            isSupportTc = userTerminalMgmtAPI.getSupportTcByBindDeskIdAndPlatform(pageSearchRequest.getDesktopId(), CbbTerminalPlatformEnums.VOI);
            enableFullSystemDisk = userDesktopMgmtAPI.getDeskEnableFullSystemDiskByDeskId(pageSearchRequest.getDesktopId());
            currentImageId = cbbIDVDeskMgmtAPI.getDeskIDV(pageSearchRequest.getDesktopId()).getImageTemplateId();
            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(currentImageId);
            currentCbbOsType = imageTemplateDetail.getOsType();
            List<CbbDeskDiskDTO> deskDiskList = cbbVDIDeskDiskAPI.listDeskDisk(pageSearchRequest.getDesktopId());
            if (CollectionUtils.isNotEmpty(deskDiskList)) {
                currentImageSystemSize = deskDiskList.stream().filter(diskDTO -> diskDTO.getType() == CbbDiskType.SYSTEM).findFirst()
                        .map(CbbDeskDiskDTO::getCapacity).orElse(0);
                currentImageDataSize = deskDiskList.stream().filter(diskDTO -> diskDTO.getType() == CbbDiskType.DATA).findFirst()
                        .map(CbbDeskDiskDTO::getCapacity).orElse(0);
            }
        }

        ImageTemplateCompareDTO imageTemplateCompare =
                new ImageTemplateCompareDTO(isSupportTc, enableFullSystemDisk, currentImageSystemSize, currentImageDataSize);

        imageTemplateCompare.setCbbOsType(currentCbbOsType);

        // 查看全部的计算集群和存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap = storagePoolAPI.queryAllStoragePool().stream()
                .collect(Collectors.toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));
        // 获取筛选的存储池所属计算集群信息
        Set<UUID> clusterIdSet = Sets.newHashSet();
        if (Objects.nonNull(pageSearchRequest.getStoragePoolId())) {
            clusterIdSet.addAll(clusterAPI.queryClusterToSetByStoragePoolId(pageSearchRequest.getStoragePoolId()));
        }

        final List<UUID> rootImageIdList = Stream.of(response.getItemArr()).map(item -> item.getId()).collect(Collectors.toList());
        final Map<UUID, List<UUID>> versionImageMap = cbbImageTemplateMgmtAPI.listByRootImageIdList(rootImageIdList);
        for (CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO : response.getItemArr()) {
            ImageTemplateDetailVO imageTemplateDetailVO =
                    buildImageTemplateVO(cbbImageTemplateDetailDTO, pageSearchRequest.getDesktopId(), imageTemplateCompare);
            // 构建返回的计算集群和存储池信息
            buildClusterAndStoragePool(cbbImageTemplateDetailDTO, clusterInfoAllMap, storagePoolAllMap, pageSearchRequest, clusterIdSet,
                    webRequest.getPlatformId());
            if (versionImageMap.containsKey(cbbImageTemplateDetailDTO.getId())) {
                final List<UUID> imageVersionList = versionImageMap.get(cbbImageTemplateDetailDTO.getId());
                imageVersionList.add(cbbImageTemplateDetailDTO.getId());
                imageTemplateDetailVO.setImageVersionIdList(imageVersionList);
            } else {
                final List<UUID> imageVersionList = Lists.newArrayList(cbbImageTemplateDetailDTO.getId());
                imageTemplateDetailVO.setImageVersionIdList(imageVersionList);
            }

            templateDetailVOArrayList.add(imageTemplateDetailVO);

        }


        ImageTemplateDetailVO[] imageTemplateDetailVOArr = templateDetailVOArrayList.toArray(new ImageTemplateDetailVO[0]);
        PageResponseContent<ImageTemplateDetailVO> pageResponseContent = new PageResponseContent<>(imageTemplateDetailVOArr, response.getTotal());
        return DefaultWebResponse.Builder.success(pageResponseContent);
    }

    private void getImageTemplateByDesktopPoolId(DefaultPageResponse<CbbImageTemplateDetailDTO> response, UUID desktopPoolId) {
        Arrays.stream(response.getItemArr()).forEach(dto -> {
            String canUsedMessage = null;
            try {
                CbbDesktopPoolDTO desktopPoolDetail = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
                buildSessionCanImage(dto, desktopPoolDetail);
            } catch (BusinessException e) {
                LOGGER.error("根据桌面池Id:[{}]限制查询镜像模板列表异常", desktopPoolId, e);
                canUsedMessage = LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCO_DESK_POOL_QUERY_EXCEPTIONS);
            }
            if (canUsedMessage != null) {
                dto.setCanUsed(false);
                dto.setCanUsedMessage(canUsedMessage);
            }
        });
    }

    private static void buildSessionCanImage(CbbImageTemplateDetailDTO dto, CbbDesktopPoolDTO desktopPoolDetail) {
        if (desktopPoolDetail.getSessionType() == CbbDesktopSessionType.MULTIPLE
                && !MULTI_SESSION_IMAGE_OS_LIST.contains(dto.getOsType().getOsName())) {
            dto.setCanUsed(false);
            dto.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_MULTI_SESSION_IMAGE_OS_NONSUPPORT));
        }
    }

    private void getImageTemplateByDesktopId(DefaultPageResponse<CbbImageTemplateDetailDTO> response, UUID desktopId) {
        Arrays.stream(response.getItemArr()).forEach(dto -> {
            String canUsedMessage = null;
            try {
                CbbDeskDTO deskVDI = cbbVDIDeskMgmtAPI.getDeskVDI(desktopId);
                if (deskVDI.getSessionType() == CbbDesktopSessionType.MULTIPLE
                        && !MULTI_SESSION_IMAGE_OS_LIST.contains(dto.getOsType().getOsName())) {
                    dto.setCanUsed(false);
                    dto.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_MULTI_SESSION_IMAGE_OS_NONSUPPORT));
                }
            } catch (BusinessException e) {
                LOGGER.error("根据桌面Id:[{}]限制查询镜像模板列表异常", desktopId, e);
                canUsedMessage = LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCO_DESK_QUERY_EXCEPTIONS);
            }
            if (canUsedMessage != null) {
                dto.setCanUsed(false);
                dto.setCanUsedMessage(canUsedMessage);
            }
        });
    }

    private void getImageTemplateByUserProfileStrategyId(DefaultPageResponse<CbbImageTemplateDetailDTO> response, UUID userProfileStrategyId,
                                                         UUID desktopPoolId, UUID desktopId) {
        if (Objects.nonNull(desktopPoolId)) {
            // 多会话桌面池，已配置了个性化配置策略，无需校验是否是win7以上系统，因为多会话只支持winserver系统
            try {
                CbbDesktopPoolDTO desktopPoolDetail = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
                if (desktopPoolDetail.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                    return;
                }
            } catch (BusinessException e) {
                // 查询池信息失败就按没有池信息逻辑走
                LOGGER.error("根据桌面池Id:[{}]限制查询镜像模板列表异常", desktopPoolId, e);
            }
        }
        if (Objects.nonNull(desktopId)) {
            // 多会话桌面，已配置了个性化配置策略，无需校验是否是win7以上系统，因为多会话只支持winserver系统
            try {
                CbbDeskDTO deskVDI = cbbVDIDeskMgmtAPI.getDeskVDI(desktopId);
                if (deskVDI.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                    return;
                }
            } catch (BusinessException e) {
                // 查询桌面信息失败就按没有桌面信息逻辑走
                LOGGER.error("根据桌面Id:[{}]限制查询镜像模板列表异常", desktopId, e);
            }
        }

        Arrays.stream(response.getItemArr()).forEach(dto -> {
            String canUsedMessage;
            try {
                canUsedMessage = imageTemplateAPI.getImageTemplateUsedMessageByUserProfileStrategyId(dto.getId());
            } catch (BusinessException e) {
                LOGGER.error("根据用户配置策略Id:[{}]限制查询镜像模板列表异常", userProfileStrategyId, e);
                canUsedMessage = LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_QUERY_EXCEPTIONS);
            }
            if (!canUsedMessage.isEmpty()) {
                dto.setCanUsed(false);
                dto.setCanUsedMessage(canUsedMessage);
            }
        });
    }

    /**
     * 分页查询镜像模板 这个是从管理员管理那边发起的 ，由于前端不支持分页的做法，而后端需要返回
     *
     * @param webRequest 请求参数
     * @param sessionContext session信息
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页查询镜像模板")
    @RequestMapping(value = "all/list", method = RequestMethod.POST)
    public DefaultWebResponse allListImageTemplate(ListImageWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        LocalImageTemplatePageRequest pageSearchRequest = LocalImageTemplatePageRequestCovertUtils.covert(webRequest);

        // 管理员管理 查询镜像列表 会传标示位 是否不需要数据权限查询
        Boolean isNoPermission = Objects.requireNonNull(webRequest).getNoPermission();
        // 如果是管理员管理发的请求 不需要数据权限 放行
        if (isNoPermission != null && isNoPermission) {
            LOGGER.info("管理员管理，查询镜像列表，不需要数据权限拦截");
        } else if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            // 如果是超级管理员 查询全部 不是超级管理员或者内置的系统管理员
            ListImageIdRequest listImageIdRequest = new ListImageIdRequest();
            listImageIdRequest.setAdminId(sessionContext.getUserId());
            List<String> imageIdStrList = adminDataPermissionAPI.listImageIdByAdminId(listImageIdRequest).getImageIdList();
            if (CollectionUtils.isEmpty(imageIdStrList)) {
                // 没有镜像关联权限 直接空
                return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", Collections.emptyList()));
            } else {
                // 添加镜像ID 筛选
                appendImageIdMatchEqual(pageSearchRequest, imageIdStrList, "id");
            }
        }

        DefaultPageResponse<CbbImageTemplateDetailDTO> response = allListImageTemplate(pageSearchRequest);
        if (response.getItemArr() == null || response.getItemArr().length == 0) {
            return DefaultWebResponse.Builder.success();
        }
        List<ImageTemplateDetailVO> templateDetailVOArrayList = new ArrayList<>();

        // 绑定这个云桌面的终端是否支持TC引导
        Boolean isSupportTc = Boolean.TRUE;
        // 桌面是否开启系统盘自动扩容
        boolean enableFullSystemDisk = false;
        Integer currentImageSystemSize = 0;
        Integer currentImageDataSize = 0;
        if (pageSearchRequest.getDesktopId() != null) {
            isSupportTc = userTerminalMgmtAPI.getSupportTcByBindDeskIdAndPlatform(pageSearchRequest.getDesktopId(), CbbTerminalPlatformEnums.VOI);
            enableFullSystemDisk = userDesktopMgmtAPI.getDeskEnableFullSystemDiskByDeskId(pageSearchRequest.getDesktopId());
            List<CbbDeskDiskDTO> deskDiskList = cbbVDIDeskDiskAPI.listDeskDisk(pageSearchRequest.getDesktopId());
            if (CollectionUtils.isNotEmpty(deskDiskList)) {
                currentImageSystemSize = deskDiskList.stream().filter(diskDTO -> diskDTO.getType() == CbbDiskType.SYSTEM).findFirst()
                        .map(CbbDeskDiskDTO::getCapacity).orElse(0);
                currentImageDataSize = deskDiskList.stream().filter(diskDTO -> diskDTO.getType() == CbbDiskType.DATA).findFirst()
                        .map(CbbDeskDiskDTO::getCapacity).orElse(0);
            }
        }
        ImageTemplateCompareDTO imageTemplateCompare =
                new ImageTemplateCompareDTO(isSupportTc, enableFullSystemDisk, currentImageSystemSize, currentImageDataSize);
        // 查看全部的计算集群和存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap = storagePoolAPI.queryAllStoragePool().stream()
                .collect(Collectors.toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));
        // 获取筛选的存储池所属计算集群信息
        Set<UUID> clusterIdSet = Sets.newHashSet();
        if (Objects.nonNull(pageSearchRequest.getStoragePoolId())) {
            clusterIdSet.addAll(clusterAPI.queryClusterToSetByStoragePoolId(pageSearchRequest.getStoragePoolId()));
        }
        for (CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO : response.getItemArr()) {
            ImageTemplateDetailVO imageTemplateDetailVO =
                    buildImageTemplateVO(cbbImageTemplateDetailDTO, pageSearchRequest.getDesktopId(), imageTemplateCompare);

            // 多会话桌面池仅桌面池类型为VDI时，支持编辑镜像模板，多会话桌面池只能选择windows server镜像模板，镜像模板必须和云桌面策略相匹配
            checkMultiSession(pageSearchRequest, cbbImageTemplateDetailDTO);

            // 构建返回的计算集群和存储池信息
            buildClusterAndStoragePool(cbbImageTemplateDetailDTO, clusterInfoAllMap, storagePoolAllMap, pageSearchRequest, clusterIdSet, null);

            templateDetailVOArrayList.add(imageTemplateDetailVO);
        }
        ImageTemplateDetailVO[] imageTemplateDetailVOArr = templateDetailVOArrayList.toArray(new ImageTemplateDetailVO[0]);
        PageResponseContent<ImageTemplateDetailVO> pageResponseContent = new PageResponseContent<>(imageTemplateDetailVOArr, response.getTotal());
        return DefaultWebResponse.Builder.success(pageResponseContent);
    }

    private static void checkMultiSession(LocalImageTemplatePageRequest pageSearchRequest, CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO) {
        if (pageSearchRequest.getSessionType() != null && pageSearchRequest.getSessionType() == CbbDesktopSessionType.MULTIPLE
                && !MULTI_SESSION_IMAGE_OS_LIST.contains(cbbImageTemplateDetailDTO.getOsType().getOsName())) {
            cbbImageTemplateDetailDTO.setCanUsed(false);
            cbbImageTemplateDetailDTO.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_MULTI_SESSION_IMAGE_OS_NONSUPPORT));
        }
    }


    @SuppressWarnings({"checkstyle:ParameterNumber"})
    private void buildClusterAndStoragePool(CbbImageTemplateDetailDTO imageTemplateDetailDTO, Map<UUID, ClusterInfoDTO> clusterInfoAllMap,
            Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap, LocalImageTemplatePageRequest request, Set<UUID> clusterIdSet, UUID platformId)
            throws BusinessException {
        // 构建集群信息
        ClusterInfoDTO clusterInfo = imageTemplateDetailDTO.getClusterInfo();
        if (Objects.nonNull(clusterInfo) && clusterInfoAllMap.containsKey(clusterInfo.getId())) {
            BeanUtils.copyProperties(clusterInfoAllMap.get(clusterInfo.getId()), clusterInfo);
        }
        PlatformStoragePoolDTO storagePool = imageTemplateDetailDTO.getStoragePool();
        if (Objects.nonNull(storagePool) && storagePoolAllMap.containsKey(storagePool.getId())) {
            BeanUtils.copyProperties(storagePoolAllMap.get(storagePool.getId()), storagePool);
        }
        UUID clusterId = request.getClusterId();
        // 根据选择计算集群id查询镜像模板是否可用
        if (Objects.nonNull(clusterId)) {
            getImageTemplateByClusterId(imageTemplateDetailDTO, clusterId, clusterInfoAllMap);
        }
        UUID networkId = request.getNetworkId();
        // 根据选择网络策略id查询镜像模板是否可用
        if (Objects.nonNull(networkId) && Objects.nonNull(clusterInfo)) {
            getImageTemplateByNetworkId(imageTemplateDetailDTO, networkId, clusterInfoAllMap);
        }
        // 根据选择存储池id查询镜像模板是否可用
        UUID storagePoolId = request.getStoragePoolId();
        if (Objects.nonNull(storagePoolId)) {
            boolean allowMatch = clusterIdSet.stream().allMatch(tempClusterId -> SetUtils
                    .intersection(clusterInfoAllMap.get(tempClusterId).getArchSet(), imageTemplateDetailDTO.getClusterInfo().getArchSet()).isEmpty());
            if (allowMatch) {
                imageTemplateDetailDTO.setCanUsed(false);
                imageTemplateDetailDTO
                        .setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_STORAGE_POOL_CLUSTER_CPU_IMAGE_TEMPLATE_NOT_AGREEMENT));
            }
        }

        boolean isSingleVersionImageNotInSamePlatform = Objects.nonNull(platformId)
                && Boolean.FALSE.equals(imageTemplateDetailDTO.getEnableMultipleVersion())
                && imageTemplateDetailDTO.getImageRoleType() == ImageRoleType.TEMPLATE && !imageTemplateDetailDTO.getPlatformId().equals(platformId);
        if (isSingleVersionImageNotInSamePlatform) {
            imageTemplateDetailDTO.setCanUsed(false);
            imageTemplateDetailDTO
                    .setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_DESKTOP_IS_SINGLE_IMAGE_CAN_NOT_SELECT_TO_OTHER_PLATFORM));
        }
    }

    private void getImageTemplateByClusterId(CbbImageTemplateDetailDTO imageTemplateDetailDTO, UUID clusterId,
            Map<UUID, ClusterInfoDTO> clusterInfoAllMap) {
        ClusterInfoDTO currentClusterInfo = clusterInfoAllMap.get(clusterId);
        // 输入查询的计算集群ID信息为空，直接都返回可选择
        if (Objects.isNull(currentClusterInfo)) {
            return;
        }
        // 当前计算集群支持的架构
        Set<String> currentArchSet = currentClusterInfo.getArchSet();
        if (Objects.isNull(imageTemplateDetailDTO.getClusterInfo()) || Objects.isNull(imageTemplateDetailDTO.getClusterInfo().getId())) {
            return;
        }
        Set<String> tempArchSet = Optional.ofNullable(clusterInfoAllMap.get(imageTemplateDetailDTO.getClusterInfo().getId()))
                .map(ClusterInfoDTO::getArchSet).orElse(new HashSet<>());
        if (CollectionUtils.isEmpty(currentArchSet) || CollectionUtils.isEmpty(SetUtils.intersection(currentArchSet, tempArchSet))) {
            imageTemplateDetailDTO.setCanUsed(false);
            imageTemplateDetailDTO.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_IMAGE_CLUSTER_CPU_FRAMEWORK_NOT_AGREEMENT,
                    imageTemplateDetailDTO.getImageName(), currentClusterInfo.getClusterName()));
        }
    }

    private void getImageTemplateByNetworkId(CbbImageTemplateDetailDTO imageTemplateDetailDTO, UUID networkId,
            Map<UUID, ClusterInfoDTO> clusterInfoAllMap) throws BusinessException {

        ClusterInfoDTO imageClusterInfo = imageTemplateDetailDTO.getClusterInfo();
        if (Objects.isNull(imageClusterInfo)) {
            return;
        }

        CbbDeskNetworkDetailDTO networkDetailDTO = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
        ClusterInfoDTO currentClusterInfo = clusterInfoAllMap.get(networkDetailDTO.getClusterId());
        // 输入查询的计算集群ID信息为空，直接都返回可选择
        if (Objects.isNull(currentClusterInfo)) {
            return;
        }
        // 当前计算集群支持的架构
        Set<String> currentArchSet = currentClusterInfo.getArchSet();
        Set<String> tempArchSet = clusterInfoAllMap.get(imageClusterInfo.getId()).getArchSet();
        SetUtils.SetView<String> intersection = SetUtils.intersection(currentArchSet, tempArchSet);
        if (CollectionUtils.isEmpty(intersection)) {
            imageTemplateDetailDTO.setCanUsed(false);
            imageTemplateDetailDTO
                    .setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NETWORK_STRATEGY_AND_IMAGE_TEMPLATE_BE_NOT_CLUSTER));
        }
    }


    private DefaultPageResponse<CbbImageTemplateDetailDTO> allListImageTemplate(LocalImageTemplatePageRequest pageSearchRequest)
            throws BusinessException {
        // 初始化分页0
        int page = 0;
        pageSearchRequest.setPage(page);
        // 每页最大1000
        pageSearchRequest.setLimit(1000);
        // 默认返回
        DefaultPageResponse<CbbImageTemplateDetailDTO> response = new DefaultPageResponse();
        // 缓存镜像集合
        List<CbbImageTemplateDetailDTO> imageList = new ArrayList<>();
        while (true) {
            DefaultPageResponse<CbbImageTemplateDetailDTO> pageResponse = cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate(pageSearchRequest);
            CbbImageTemplateDetailDTO[] itemArr = pageResponse.getItemArr();
            if (ArrayUtils.isEmpty(itemArr)) {
                // 设置总数量
                response.setTotal(pageResponse.getTotal());
                // 设置返回的集合
                response.setItemArr(imageList.stream().toArray(CbbImageTemplateDetailDTO[]::new));
                return response;
            }
            // 页码数量自增
            pageSearchRequest.setPage(++page);
            for (CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO : itemArr) {
                imageList.add(cbbImageTemplateDetailDTO);
            }

        }
    }

    /**
     * 添加镜像ID 筛选
     *
     * @param request
     * @param userGroupIdStrList
     */
    private void appendImageIdMatchEqual(LocalImageTemplatePageRequest request, List<String> userGroupIdStrList, String fieldName) {
        List<UUID> uuidList = userGroupIdStrList.stream().map(UUID::fromString).collect(Collectors.toList());
        UUID[] uuidArr = uuidList.toArray(new UUID[uuidList.size()]);
        request.appendCustomMatchEqual(new MatchEqual(fieldName, uuidArr));
    }

    /**
     * 准备编辑镜像
     *
     * @param webRequest 请求结果
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "prepareEdit")
    public DefaultWebResponse prepareEditImageTemplate(PrepareEditImageTemplateWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        String imageName = null;
        try {
            imageName = getImageTemplateName(webRequest.getImageTemplateId());
            cbbImageTemplateMgmtAPI.prepareEditImageTemplate(webRequest.getImageTemplateId());
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PREPARE_EDIT_IMAGETEMPLATE_SUCCESS_LOG, imageName);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PREPARE_EDIT_IMAGETEMPLATE_SUCCESS_LOG,
                    new String[] {imageName});
        } catch (BusinessException e) {
            LOGGER.error("准备编辑出错", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PREPARE_EDIT_IMAGETEMPLATE_FAIL_LOG, e, imageName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PREPARE_EDIT_IMAGETEMPLATE_FAIL_LOG, e, imageName,
                    e.getI18nMessage());
        }

    }

    /**
     * 发布镜像
     *
     * @param webRequest 发布镜像的web请求
     * @param builder 批处理
     * @return 发布结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("发布镜像")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "publish", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> publishImageTemplate(PublishImageTemplateWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {

        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");
        CreateImageTemplatePublishTaskWebRequest request = new CreateImageTemplatePublishTaskWebRequest();
        request.setImageId(webRequest.getId());
        request.setEnableForcePublish(false);
        request.setSnapshotName(UUID.randomUUID().toString());
        request.setSyncType(webRequest.getSyncMode());
        request.setStoragePoolIdArr(request.getStoragePoolIdArr());
        return CommonWebResponse.success(publishImageTemplate(request, builder));
    }

    /**
     * 更新镜像其他参数
     *
     * @param webRequest 请求参
     * @return 更新结果
     * @throws BusinessException BusinessException
     */
    @ApiOperation("更新镜像其他参数")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    @EnableCustomValidate(validateMethod = "updateImageTemplateValidate")
    public CommonWebResponse<?> updateImageTemplate(UpdateImageTemplateWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        UUID imageTemplateId = webRequest.getId();
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
        // 存在链接克隆桌面，进行系统盘和数据盘大小的校验
        validateCanEditSystemDiskSize(imageTemplateDetail, webRequest);
        validateCanEditDataDiskSize(imageTemplateDetail, webRequest);

        // 验证是否能够打开或者关闭数据D盘：
        validateCanOperatorSwitchDataDiskSwitch(webRequest.getAdvancedConfig(), imageTemplateDetail);

        // 校验VGPU信息
        validateVgpuInfo(webRequest.getAdvancedConfig(), webRequest.getImageSystemType(), imageTemplateDetail.getCbbImageType(), webRequest.getId());

        // 检查变更的操作系统是否支持镜像类型（操作系统有变更时才进行校验，避免存量镜像模板不可编辑）
        if (!webRequest.getImageSystemType().equals(imageTemplateDetail.getOsType())) {
            imageTemplateValidation.imageTypeAndImageOsTypeValidate(imageTemplateDetail.getCbbImageType(), webRequest.getImageSystemType());
        }

        final CbbUpdateImageTemplateDTO request = CloudDesktopAPIRequestBuilder.buildUpdateImageTemplateRequest(webRequest);
        try {
            cbbImageTemplateMgmtAPI.updateImageTemplate(request);
            final CbbConfigVmForEditImageTemplateDTO vmConfig =
                    CloudDesktopAPIRequestBuilder.buildConfigVmForEditImageTemplateRequest(webRequest.getAdvancedConfig());
            vmConfig.setImageTemplateId(request.getImageTemplateId());
            // tip：如果配置D盘从开启到关闭，在run接口的状态机的磁盘操作：
            cbbImageTemplateMgmtAPI.configVmForEditImageTemplate(vmConfig);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EDIT_BASE_IMAGETEMPLATE_SUCCESS_LOG, webRequest.getImageName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EDIT_BASE_IMAGETEMPLATE_SUCCESS_LOG,
                    new String[] {webRequest.getImageName()});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EDIT_BASE_IMAGETEMPLATE_FAIL_LOG, e, webRequest.getImageName(),
                    e.getI18nMessage());
            return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EDIT_BASE_IMAGETEMPLATE_FAIL_LOG,
                    new String[] {webRequest.getImageName(), e.getI18nMessage()});
        }
    }

    private void validateCanOperatorSwitchDataDiskSwitch(ConfigVmForEditImageTemplateWebRequest advanceConfig,
            CbbImageTemplateDetailDTO imageTemplateDetailDTO) throws BusinessException {

        if (advanceConfig == null) {
            return;
        }
        // 原先是否开启了磁盘：
        Boolean isOldEnableDataDisk = true;

        // 原先没有磁盘，默认原先是关闭的数据盘：
        if (CollectionUtils.isEmpty(imageTemplateDetailDTO.getImageDiskList())) {
            isOldEnableDataDisk = false;
        }

        // 获取镜像可用，或者正在创建的磁盘的数量：
        long availableDiskCount = imageTemplateDetailDTO.getImageDiskList().stream() //
                .filter(image -> image.getImageDiskState() == CbbImageDiskState.AVAILABLE || //
                        image.getImageDiskState() == CbbImageDiskState.CREATING)//
                .count();//
        // 数量为0，代表该镜像没有磁盘，默认原先是关闭的数据盘：
        if (availableDiskCount == 0) {
            isOldEnableDataDisk = false;
        }
        // 如果页面的开关跟原先的不一致：

        Boolean isNewEnableDataDisk = CollectionUtils.isNotEmpty(advanceConfig.getImageDiskList());

        if (isNewEnableDataDisk == isOldEnableDataDisk) {
            return;
        }

        // FIXME 这个虽然是VDI的接口，但是实际上VDI IDV通用的，不知道在搞什么鬼
        List<CbbDeskInfoDTO> deskInfoDTOList = cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate(imageTemplateDetailDTO.getId());
        if (CollectionUtils.isNotEmpty(deskInfoDTOList)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_NOT_OPERATOR_DATA_DISK_ENABLE);
        }


    }

    private void validateCanEditSystemDiskSize(CbbImageTemplateDetailDTO imageTemplateDetail, UpdateImageTemplateWebRequest webRequest)
            throws BusinessException {
        if (imageTemplateDetail.getClouldDeskopNum() > 0) {
            boolean isImageCanEditSystemDiskSize = imageTemplateAPI.isImageCanEditSystemDiskSize(webRequest.getId());
            if (!isImageCanEditSystemDiskSize && !imageTemplateDetail.getSystemDisk().equals(webRequest.getAdvancedConfig().getSystemDisk())) {
                // 说明已有绑定关系，对比传入参数和数据库存储参数，不允许修改系统盘
                LOGGER.error("存在绑定关系的镜像模板[{}]不允许修改系统盘大小", webRequest.getId());
                throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_BIND_DESK_CAN_NOT_EDIT_SYSTEM_DISK);
            }
        }
    }

    private void validateCanEditDataDiskSize(CbbImageTemplateDetailDTO imageTemplateDetail, UpdateImageTemplateWebRequest webRequest)
            throws BusinessException {
        if (imageTemplateDetail.getClouldDeskopNum() > 0 && isChangeDataDiskInfo(imageTemplateDetail, webRequest)) {
            // 存在云桌面使用并且尝试修订数据盘，检查数据盘是否支持修改
            if (!imageTemplateAPI.isImageCanEditDataDiskSize(webRequest.getId())) {
                LOGGER.error("存在绑定关系的镜像模板[{}]不允许修改数据盘大小", webRequest.getId());
                throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_BIND_DESK_CAN_NOT_EDIT_DATA_DISK);
            }
        }
    }

    private boolean isChangeDataDiskInfo(CbbImageTemplateDetailDTO imageTemplateDetail, UpdateImageTemplateWebRequest webRequest) {
        List<CbbImageDiskInfoDTO> imageTemplateDataDiskList = imageTemplateDetail.getImageDiskList().stream()
                .filter(item -> CbbImageDiskType.DATA.equals(item.getImageDiskType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(imageTemplateDataDiskList)) {
            imageTemplateDataDiskList = new ArrayList<>();
        }
        List<CbbImageDiskDTO> advancedDataDiskList = webRequest.getAdvancedConfig().getImageDiskList();
        if (CollectionUtils.isEmpty(advancedDataDiskList)) {
            advancedDataDiskList = new ArrayList<>();
        }

        // 都没有数据盘信息，判定为未修订
        if (CollectionUtils.isEmpty(imageTemplateDataDiskList) && CollectionUtils.isEmpty(advancedDataDiskList)) {
            return false;
        }

        if (imageTemplateDataDiskList.size() != advancedDataDiskList.size()) {
            LOGGER.info("数据盘数据不一致，判定为修改数据盘");
            return true;
        }

        // 入参数据中存在id为空的数据盘记录，判定为新增数据盘，包含了先移除现有数据盘，再添加新数据盘的情况
        List<CbbImageDiskDTO> addDataDiskList = advancedDataDiskList.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(addDataDiskList)) {
            LOGGER.info("入参数据中存在id为空的数据盘记录，判定为新增数据盘");
            return true;
        }

        // 数据盘数量一致的情况，对比每个数据盘的多个属性是否变更
        Map<UUID, CbbImageDiskInfoDTO> imageTemplateDataDiskMap =
                imageTemplateDataDiskList.stream().collect(Collectors.toMap(CbbImageDiskInfoDTO::getId, item -> item));
        Map<UUID, CbbImageDiskDTO> advancedDataDiskMap =
                advancedDataDiskList.stream().collect(Collectors.toMap(CbbImageDiskDTO::getId, item -> item));

        for (Map.Entry<UUID, CbbImageDiskInfoDTO> uuidCbbImageDiskInfoDTOEntry : imageTemplateDataDiskMap.entrySet()) {
            CbbImageDiskDTO cbbImageDiskDTO = advancedDataDiskMap.get(uuidCbbImageDiskInfoDTOEntry.getKey());
            if (cbbImageDiskDTO == null) {
                LOGGER.info("镜像磁盘数据和入参不一致");
                return true;
            }
            CbbImageDiskInfoDTO cbbImageDiskInfoDTO = uuidCbbImageDiskInfoDTOEntry.getValue();
            if (!Objects.equals(cbbImageDiskDTO.getVmDiskSize(), cbbImageDiskInfoDTO.getDiskSize())
                    || !Objects.equals(cbbImageDiskDTO.getDiskSymbol(), cbbImageDiskInfoDTO.getDiskSymbol())
                    || !Objects.equals(cbbImageDiskDTO.getHiddenDisk(), cbbImageDiskInfoDTO.getHiddenDisk())) {
                LOGGER.info("镜像磁盘属性不一致");
                return true;
            }
        }
        return false;
    }

    private void validateVgpuInfo(ConfigVmForEditImageTemplateWebRequest advancedConfig, CbbOsType osType, CbbImageType imageType, UUID imageId)
            throws BusinessException {
        if (advancedConfig == null) {
            return;
        }
        VgpuExtraInfoDTO vgpuExtraInfo = advancedConfig.getVgpuExtraInfo();
        if (Objects.isNull(vgpuExtraInfo) || StringUtils.isEmpty(vgpuExtraInfo.getModel())
                || VgpuType.QXL.name().equals(advancedConfig.getVgpuType())) {
            return;
        }
        if (imageType != CbbImageType.VDI) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_IMAGE_TEMPLATE_IDV_NOT_SUPPORT_GPU);
        }

        // 前端首次创建镜像模板时传Cluster,创建镜像后传VmCluster
        IdLabelEntry cluster = Objects.isNull(advancedConfig.getVmCluster()) ? advancedConfig.getCluster() : advancedConfig.getVmCluster();
        UUID clusterId = Objects.isNull(cluster) ? null : cluster.getId();
        List<CbbClusterGpuInfoDTO> gpuInfoDTOList = deskSpecAPI.getClusterGpuInfo(clusterId);
        // 校验计算集群是否存在对应VGPU资源
        CbbClusterGpuInfoDTO cbbClusterGpuInfo = DesktopStrategyUtils.getMatchVgpuInfo(vgpuExtraInfo, gpuInfoDTOList);
        advancedConfig.setVgpuType(cbbClusterGpuInfo.getVgpuType().name());

        checkOsTypeSupportGpu(osType, imageId, cbbClusterGpuInfo.getModel());
    }

    private void validateVmClusterStoragePool(ConfigVmForEditImageTemplateWebRequest request, CbbImageTemplateDetailDTO imageTemplateDetail)
            throws BusinessException {
        // 非VDI镜像模板不校验计算集群和存储池·
        if (imageTemplateDetail.getCbbImageType() != CbbImageType.VDI) {
            if (Objects.nonNull(request.getCluster()) || Objects.nonNull(request.getStoragePool()) || Objects.nonNull(request.getVmCluster())
                    || Objects.nonNull(request.getVmStoragePool())) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_IMAGE_TEMPLATE_NOT_VDI_WITH_STORAGE_CLUSTRE_ERROR);
            }
            return;
        }
        // 办公VDI场景：镜像和临时虚拟机计算集群不能为空
        ClusterInfoDTO clusterInfo = imageTemplateDetail.getClusterInfo();
        IdLabelEntry vmCluster = request.getVmCluster();
        if (Objects.isNull(clusterInfo) || Objects.isNull(vmCluster)) {
            throw new BusinessException(PublicBusinessKey.RCDC_RCO_CLUSTER_NOT_NULL_ERROR);
        }
        // 办公VDI场景：镜像和临时虚拟机存储池不能为空
        PlatformStoragePoolDTO storagePool = imageTemplateDetail.getStoragePool();
        IdLabelEntry vmStoragePool = request.getVmStoragePool();
        if (Objects.isNull(storagePool) || Objects.isNull(vmStoragePool)) {
            throw new BusinessException(PublicBusinessKey.RCDC_RCO_STORAGE_POOL_NOT_NULL_ERROR);
        }
        // 校验镜像计算集群和虚拟机计算集群CPU架构是否一致
        clusterAPI.validateComputerClusterFramework(clusterInfo.getId(), vmCluster.getId());
        // 校验虚拟机计算集群和存储池
        clusterAPI.validateComputerClusterStoragePool(vmCluster.getId(), vmStoragePool.getId(), null);
        // 校验虚拟机计算集群和网络策略
        clusterAPI.validateVDIDesktopNetwork(vmCluster.getId(), request.getNetwork().getId());
    }

    /**
     * 获取镜像详细参数
     *
     * @param webRequest 请求参数
     * @return 更新结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取镜像详细参数")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public DefaultWebResponse detailImageTemplate(DetailImageTemplateWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getId());
        // 过滤可用的磁盘才返回：
        filterImageDiskList(cbbImageTemplateDetailDTO);

        // Mb转换为Gb
        return DefaultWebResponse.Builder.success(mb2Gb(cbbImageTemplateDetailDTO));
    }

    private ImageTemplateDetailVO buildImageTemplateVO(CbbImageTemplateDetailDTO dto, UUID desktopId, ImageTemplateCompareDTO imageTemplateCompareDTO)
            throws BusinessException {
        RcoImageTemplateDetailDTO rcoImageTemplateDetailDTO = new RcoImageTemplateDetailDTO();
        if (!ObjectUtils.isEmpty(dto.getMemory())) {
            // 将Mb转换为Gb
            rcoImageTemplateDetailDTO.setMemoryForGb(CapacityUnitUtils.mb2Gb(dto.getMemory()));
        }
        List<String> editErrorMessageList = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(dto.getEditErrorMessageList())) {
            editErrorMessageList.addAll(dto.getEditErrorMessageList());
        }

        if (StringUtils.isNotBlank(dto.getEditErrorMessage()) && !editErrorMessageList.contains(dto.getEditErrorMessage())) {
            editErrorMessageList.add(dto.getEditErrorMessage());
        }
        ImageTemplateStateEnum imageTemplateStateEnum = checkImageTemplateState(dto);
        if (StringUtils.isNotBlank(imageTemplateStateEnum.getMessage())) {
            editErrorMessageList.add(imageTemplateStateEnum.getMessage());
        }
        if (dto.getOsType().getOsPlatform() == OsPlatform.WINDOWS && (serverModelAPI.isVdiModel() || serverModelAPI.isIdvModel())) {
            ImageTemplateUwsLauncherStateEnum uwsLauncherState = cmsUpgradeAPI.getUwsLauncherState(dto.getId());
            if (StringUtils.isNotBlank(uwsLauncherState.getMessage())) {
                editErrorMessageList.add(uwsLauncherState.getMessage());
            }

            ImageTemplateCmLauncherStateEnum cmLauncherState = cmsUpgradeAPI.getCmLauncherState(dto.getId());
            if (StringUtils.isNotBlank(cmLauncherState.getMessage())) {
                editErrorMessageList.add(cmLauncherState.getMessage());
            }
        }

        // 不再支持Guesttool的操作系统类型镜像，不再提示Guesttool相关版本错误信息
        // 没有绑定个性桌面、并且镜像gt版本号和服务器gt版本号不一致
        if (!CbbOsType.isUnsupportedGuesttoolOS(dto.getOsType()) && isExistGTErrorMessage(editErrorMessageList) && needShowTip(dto)
                && checkImageGuestToolVersionIsLatest(dto)) {
            if (dto.getRootImageId() == null) {
                editErrorMessageList.add(GT_VERSION_LOW_TIP);
            } else {
                editErrorMessageList.add(LocaleI18nResolver.resolve(RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_GT_LOW));
            }
        }

        //判断镜像版本是否低于oa初始版本
        try {
            String guestToolVersion = GtAgentUtil.generateGtVersion(dto.getGuestToolVersion());
            VersionCompareResultEnum compareValue = VersionUtil.compare(guestToolVersion,
                    com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants.ONE_AGENT_VERSION_INIT);
            if (VersionCompareResultEnum.VERSION_SMALLER == compareValue) {
                if (editErrorMessageList.contains(GT_VERSION_LOW_TIP)) {
                    editErrorMessageList.remove(GT_VERSION_LOW_TIP);
                    editErrorMessageList.add(GT_NOT_AVAILABLE_TIP);
                }

                if (editErrorMessageList.contains(RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_GT_LOW_TIP)) {
                    editErrorMessageList.remove(RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_GT_LOW_TIP);
                    editErrorMessageList.add(LocaleI18nResolver.resolve(RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_GT_UNABLE));
                }

            }
        } catch (Exception e) {
            LOGGER.warn("镜像[{}]的gt版本号[{}]解析失败", dto.getId(), dto.getGuestToolVersion());
        }


        if (!CollectionUtils.isEmpty(dto.getImageDriverList()) && !dto.getHasImportDriverPackage()) {
            editErrorMessageList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_IMAGE_NOT_IMPORT_DRIVER));
        }

        String[] editErrorMessageArr = new String[editErrorMessageList.size()];
        editErrorMessageList.toArray(editErrorMessageArr);
        rcoImageTemplateDetailDTO.setEditErrorMessageArr(editErrorMessageArr);

        // GPU信息
        buildImageTemplateVgpuInfo(rcoImageTemplateDetailDTO, dto.getVgpuInfoDTO());

        ImageTemplatePublishTaskDTO imageTemplatePublishTaskDTO =
                packageImageTemplatePublishTaskDTO(dto.getId(), dto.getCbbImageType(), dto.getImageState());
        rcoImageTemplateDetailDTO.setPublishTaskDTO(imageTemplatePublishTaskDTO);

        // 不支持TC引导的TCI，不允许修改为WIN7_32
        if (!imageTemplateCompareDTO.getSupportTc() && dto.getOsType() == CbbOsType.WIN_7_32) {
            dto.setCanUsed(false);
            String terminalId = userTerminalMgmtAPI.getTerminalIdByBindDeskId(desktopId);
            String terminalAddr = terminalId;
            try {
                CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
                terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
            } catch (BusinessException e) {
                LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
            }

            String message = LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_TCI_TERMINAL_NOT_SUPPORT_CAN_NOT_USE_WIN7_32, terminalAddr);
            dto.setCanUsedMessage(message);
        }
        if (dto.getImageRoleType() == ImageRoleType.TEMPLATE && dto.getEnableMultipleVersion()) {
            // 检查根镜像是否存在版本的系统盘小于桌面系统盘，如果没有则设置canUsed为false
            if (imageTemplateCompareDTO.getCurrentImageSystemSize() > 0 && !cbbImageTemplateMgmtAPI
                    .existsVersionSystemDiskSizeNotGreaterThan(dto.getId(), imageTemplateCompareDTO.getCurrentImageSystemSize())) {
                dto.setCanUsed(false);
                String message =
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_MULTIPLE_IMAGE_SYSTEM_SIZE_SHOULD_NOT_GREATER_CURRENT_DESK_DISK);
                dto.setCanUsedMessage(message);
            }
        } else {
            // 不允许选择系统盘大于当前桌面的镜像模板
            if (imageTemplateCompareDTO.getCurrentImageSystemSize() > 0
                    && dto.getSystemDisk() > imageTemplateCompareDTO.getCurrentImageSystemSize()) {
                dto.setCanUsed(false);
                String msg = "";
                if (dto.getImageRoleType() == ImageRoleType.TEMPLATE) {
                    msg = LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_IMAGE_SYSTEM_SIZE_SHOULD_NOT_GREATER_CURRENT_IMAGE_SYSTEM_DISK);
                } else {
                    msg = LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_IMAGE_VERSION_SYSTEM_SIZE_SHOULD_NOT_GREATER_CURRENT_DESK_DISK);
                }

                dto.setCanUsedMessage(msg);
            }
        }


        if (StringUtils.isEmpty(dto.getCanUsedMessage()) && imageTemplateCompareDTO.getCurrentImageDataSize() > 0) {
            // 数据盘大小优先级最低, 目标镜像的数据盘大小，不可大于当前镜像的数据盘大小，终端空间可能不足
            Integer imageDataDiskSize = getImageDataDiskSize(dto);
            if (imageDataDiskSize > imageTemplateCompareDTO.getCurrentImageDataSize()) {
                dto.setCanUsed(false);
                String message =
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_IMAGE_DATA_SIZE_SHOULD_EQUALS_OR_LESS_CURRENT_IMAGE_DATA_DISK);
                dto.setCanUsedMessage(message);
            }
        }

        ImageTemplateDetailVO imageTemplateDetailVO = new ImageTemplateDetailVO();
        imageTemplateDetailVO.setRcoImageTemplateDetailDTO(rcoImageTemplateDetailDTO);
        imageTemplateDetailVO.setCbbImageTemplateDetailDTO(dto);

        ImageSnapshotRestoreTaskDTO restoreTaskDTO = packageImageSnapshotRestoreTaskDTO(dto.getId(), dto.getCbbImageType());
        imageTemplateDetailVO.setRestoreTaskDTO(restoreTaskDTO);

        filterImageDiskList(dto);

        if (StringUtils.isNotBlank(dto.getGuestToolVersion())) {
            String lastOneAgentVersion = obtainUpgradeVersionForImage(dto);
            rcoImageTemplateDetailDTO.setGuestToolVersionLastest(Objects.equals(lastOneAgentVersion, dto.getGuestToolVersion()));
        } else {
            rcoImageTemplateDetailDTO.setGuestToolVersionLastest(false);
        }

        if (Objects.nonNull(imageTemplateCompareDTO.getCbbOsType())) {
            if (!CbbOsType.isWindowsOs(imageTemplateCompareDTO.getCbbOsType()) && CbbOsType.isWindowsOs(dto.getOsType())
                    || CbbOsType.isWindowsOs(imageTemplateCompareDTO.getCbbOsType()) && !CbbOsType.isWindowsOs(dto.getOsType())) {
                dto.setCanUsed(false);
                String message = CbbOsType.isWindowsOs(imageTemplateCompareDTO.getCbbOsType())
                        ? LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_IMAGE_OS_TYPE_CAN_NOT_CHANGE_TO_LINUX)
                        : LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_IMAGE_OS_TYPE_CAN_NOT_CHANGE_TO_WINDOWS);
                dto.setCanUsedMessage(message);
            }
        }
        if (dto.getCbbImageType() == CbbImageType.VDI && !CloudPlatformStatus.isAvailableBotNotMaintenance(dto.getPlatformStatus())) {
            dto.setCanUsed(false);
            dto.setCanUsedMessage(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_CLOUD_PLATFORM_IS_UN_AVAILABLE, //
                    dto.getPlatformStatus().getDesc()));
        }
        rcoImageTemplateDetailDTO.setLock(imageTemplateAPI.isLockImage(dto.getId()));

        return imageTemplateDetailVO;
    }

    private String obtainUpgradeVersionForImage(CbbImageTemplateDetailDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto can not be null");
        UUID imageId = dto.getId();
        CbbOsType osType = dto.getOsType();
        ArchType archType = ArchType.X86_64;
        // 默认X86，如果镜像不为空则更新
        if (Objects.nonNull(dto.getCpuArch()) && CbbCpuArchType.OTHER != dto.getCpuArch()) {
            archType = ArchType.convert(dto.getCpuArch().getArchName());
        }
        AppGetVersionRequest packetOsArch = new AppGetVersionRequest();
        packetOsArch.setArchType(archType);
        packetOsArch.setOsType(OsType.convert(osType.getOsPlatform().name()));
        packetOsArch.setProductType(PacketProductType.ONE_AGENT);

        AppUpgradeVersionDTO applicationUpgradeVersionResponse = baseApplicationUpgradeAPI.getVersionSimple(packetOsArch);
        LOGGER.debug("镜像[{}]获取最新oneAgent的版本值为[{}]", imageId, applicationUpgradeVersionResponse.getVersion());
        return applicationUpgradeVersionResponse.getVersion();
    }


    private void filterImageDiskList(CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO) {
        if (CollectionUtils.isNotEmpty(cbbImageTemplateDetailDTO.getImageDiskList())) {
            List<CbbImageDiskInfoDTO> newImageDiskList = cbbImageTemplateDetailDTO.getImageDiskList().stream()
                    .filter(disk -> disk.getImageDiskState() == CbbImageDiskState.AVAILABLE).collect(Collectors.toList());
            cbbImageTemplateDetailDTO.setImageDiskList(newImageDiskList);
        }
    }

    private void buildImageTemplateVgpuInfo(RcoImageTemplateDetailDTO rcoImageTemplateDetailDTO, VgpuInfoDTO vgpuInfoDTO) {
        if (Objects.isNull(vgpuInfoDTO)) {
            return;
        }
        rcoImageTemplateDetailDTO.setEnableGpu(vgpuInfoDTO.getVgpuType() != null && !vgpuInfoDTO.getVgpuType().equals(VgpuType.QXL));
        rcoImageTemplateDetailDTO.setVgpuType(vgpuInfoDTO.getVgpuType());
        if (checkVgpuExtraInfo(vgpuInfoDTO.getVgpuExtraInfo())) {
            VgpuExtraInfo vgpuExtraInfo = (VgpuExtraInfo) vgpuInfoDTO.getVgpuExtraInfo();
            rcoImageTemplateDetailDTO.setVgpuModel(deskSpecAPI.buildDefaultAmdModel(vgpuExtraInfo));
            rcoImageTemplateDetailDTO.setGraphicsMemorySize(DeskSpecUtils.formatGpuSize(vgpuExtraInfo.getGraphicsMemorySize()));
        }
    }

    private ImageTemplateStateEnum checkImageTemplateState(CbbImageTemplateDetailDTO dto) {
        try {
            if (dto.getImageState() == ImageTemplateState.PUBLISHING && dto.getCbbImageType() != CbbImageType.VDI) {
                CbbQueryPublishImageSpaceInfoResponseDTO publishImageSpaceDTO = cbbImageTemplateMgmtAPI.queryIDVImagePublishNeedSpace(dto.getId());
                LOGGER.debug("镜像总空间：{}，当前剩余空间：{}，本次发布需要空间：{}", publishImageSpaceDTO.getTotalSpace(), //
                        publishImageSpaceDTO.getUsableSpace(), publishImageSpaceDTO.getPublishNeedSpace());
                if (publishImageSpaceDTO.getUsableSpace() < publishImageSpaceDTO.getPublishNeedSpace()) {
                    return ImageTemplateStateEnum.PUBLISH_NEED_SPACE_LOW;
                }
            }
        } catch (BusinessException e) {
            LOGGER.error("检查镜像状态异常", e);
            return ImageTemplateStateEnum.CHECK_ERROR;
        }
        return ImageTemplateStateEnum.VERSION_SUCCESS;
    }

    private ImageTemplateDetailDTO mb2Gb(CbbImageTemplateDetailDTO dto) throws BusinessException {
        ImageTemplateDetailDTO imageVO = new ImageTemplateDetailDTO();
        // 构建集群信息
        ClusterInfoDTO clusterInfo = dto.getClusterInfo();
        if (Objects.nonNull(clusterInfo)) {
            dto.setClusterInfo(clusterAPI.queryAvailableClusterById(clusterInfo.getId()));
        }
        PlatformStoragePoolDTO storagePool = dto.getStoragePool();
        if (Objects.nonNull(storagePool)) {
            dto.setStoragePool(storagePoolAPI.getStoragePoolDetail(storagePool.getId()));
        }
        BeanUtils.copyProperties(dto, imageVO);
        imageVO.setCanEditSystemDiskSize(imageTemplateAPI.isImageCanEditSystemDiskSize(dto.getId()));
        imageVO.setCanEditDataDiskSize(imageTemplateAPI.isImageCanEditDataDiskSize(dto.getId()));
        if (!ObjectUtils.isEmpty(dto.getMemory())) {
            imageVO.setMemory(CapacityUnitUtils.mb2Gb(dto.getMemory()));
        }
        if (Objects.nonNull(dto.getVmClusterId())) {
            imageVO.setVmClusterInfo(clusterAPI.queryAvailableClusterById(dto.getVmClusterId()));
        }
        if (Objects.nonNull(dto.getVmStoragePoolId())) {
            imageVO.setVmStoragePool(storagePoolAPI.getStoragePoolDetail(dto.getVmStoragePoolId()));
        }
        // 镜像临时虚拟机为空而且镜像类型不为BY_ISO时，可修改虚拟机配置信息
        imageVO.setCanModifyVm(dto.getCreateMode() != CbbImageTemplateCreateMode.BY_ISO && Objects.isNull(dto.getTempVmId()));
        imageVO.setVgpuItem(VgpuType.QXL.name());
        VgpuInfoDTO vgpuInfoDTO = dto.getVgpuInfoDTO();
        if (Objects.isNull(vgpuInfoDTO)) {
            return imageVO;
        }
        imageVO.setEnableGpu(Objects.nonNull(vgpuInfoDTO.getVgpuType()) && vgpuInfoDTO.getVgpuType() != VgpuType.QXL);
        imageVO.setVgpuType(Objects.isNull(vgpuInfoDTO.getVgpuType()) ? VgpuType.QXL : vgpuInfoDTO.getVgpuType());
        if (checkVgpuExtraInfo(vgpuInfoDTO.getVgpuExtraInfo())) {
            VgpuExtraInfo vgpuExtraInfo = (VgpuExtraInfo) vgpuInfoDTO.getVgpuExtraInfo();
            String model = deskSpecAPI.buildDefaultAmdModel(vgpuExtraInfo);
            imageVO.setVgpuItem(model);
            imageVO.setVgpuModel(model);
        }
        imageVO.setImageDriverList(buildImageDriverList(dto.getImageDriverList()));
        imageVO.setPlatformId(dto.getPlatformId());
        imageVO.setPlatformName(dto.getPlatformName());
        imageVO.setImageUsage(dto.getImageUsage());
        return imageVO;
    }


    /**
     * 关闭镜像编辑
     *
     * @param builder 异步任务
     * @param webRequest 请求参数
     * @param sessionContext sessionContext
     * @return 更新结果
     * @throws BusinessException 异常
     */
    @ApiOperation("停止")
    @RequestMapping(value = "stop")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public DefaultWebResponse closeImageTemplate(CloseImageTemplateWebRequest webRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        // 检测管理员镜像编辑信息
        CbbImageTemplateEditDTO cbbImageTemplateEditDTO =
                new CbbImageTemplateEditDTO(webRequest.getId(), sessionContext.getUserId(), sessionContext.getUserName());
        cbbImageTemplateMgmtAPI.checkIfEditingByOtherAdminWhenStop(cbbImageTemplateEditDTO);

        // 判断是IDV终端镜像编辑还是服务端上的编辑:
        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(webRequest.getId());
        if (imageTemplateInfo.getTerminalId() != null && imageTemplateInfo.getImageVmHost() == ImageVmHost.TERMINAL) {
            // 终端镜像编辑主动关闭
            return closeTerminalImageTemplate(imageTemplateInfo, builder);
        }
        // IDV 服务端、VDI关闭镜像流程、在终端镜像绑定状态失效的时候，有可能走到此流程：
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_STOP_TASK_NAME));
        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_STOP_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_STOP_DESC, imageTemplateInfo.getImageName())
                .setUniqueId(webRequest.getId()).setUniqueId(webRequest.getId())
                .registerHandler(stopImageTemplateHandlerFactory.createHandler(batchTaskItem, webRequest)).start();
        return DefaultWebResponse.Builder.success(result);

    }


    /**
     * 查询镜像信息
     *
     * @param webRequest 请求参数
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询镜像信息")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0, descriptions = {"镜像D盘"})})

    @RequestMapping(value = "getInfo", method = RequestMethod.POST)
    public DefaultWebResponse getImageTemplateInfo(GetImageTemplateInfoWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(webRequest.getId());
        // 待发布 VDI 镜像需要判断是否存在定时发布
        ImageTemplatePublishTaskDTO imageTemplatePublishTaskDTO =
                packageImageTemplatePublishTaskDTO(imageTemplateInfo.getId(), imageTemplateInfo.getCbbImageType(), imageTemplateInfo.getImageState());
        ImageSnapshotRestoreTaskDTO restoreTaskDTO =
                packageImageSnapshotRestoreTaskDTO(imageTemplateInfo.getId(), imageTemplateInfo.getCbbImageType());
        return DefaultWebResponse.Builder.success(buildImageTemplateInfoDTO(imageTemplateInfo, imageTemplatePublishTaskDTO, restoreTaskDTO));
    }

    private ImageTemplateInfoDTO buildImageTemplateInfoDTO(CbbGetImageTemplateInfoDTO imageTemplateInfo,
            ImageTemplatePublishTaskDTO imageTemplatePublishTaskDTO, ImageSnapshotRestoreTaskDTO restoreTaskDTO) throws BusinessException {

        ImageTemplateInfoDTO imageTemplateInfoDTO = new ImageTemplateInfoDTO();
        imageTemplateInfoDTO.setPublishTaskDTO(imageTemplatePublishTaskDTO);
        imageTemplateInfoDTO.setRestoreTaskDTO(restoreTaskDTO);
        imageTemplateInfoDTO.setCpu(imageTemplateInfo.getCpu());
        imageTemplateInfoDTO.setImageFileName(imageTemplateInfo.getImageFileName());
        imageTemplateInfoDTO.setCreateTime(imageTemplateInfo.getCreateTime().getTime());
        imageTemplateInfoDTO.setDescribe(imageTemplateInfo.getNote());
        imageTemplateInfoDTO.setGuestTool(imageTemplateInfo.getGuestToolVersion());
        imageTemplateInfoDTO.setId(imageTemplateInfo.getId());
        imageTemplateInfoDTO.setImageName(imageTemplateInfo.getImageName());
        imageTemplateInfoDTO.setImageSystemSize(imageTemplateInfo.getImageSystemSize());
        imageTemplateInfoDTO.setImageState(imageTemplateInfo.getImageState());
        imageTemplateInfoDTO.setControlState(imageTemplateInfo.getControlState());
        imageTemplateInfoDTO.setEnableNested(imageTemplateInfo.getEnableNested());
        imageTemplateInfoDTO.setFtpUploadState(imageTemplateInfo.getFtpUploadState());
        imageTemplateInfoDTO.setImageVmHost(imageTemplateInfo.getImageVmHost());
        imageTemplateInfoDTO.setIp(imageTemplateInfo.getIpAdress());
        imageTemplateInfoDTO.setOsVersion(imageTemplateInfo.getOsVersion());
        if (imageTemplateInfo.isVOIorIDVImage()) {
            setImageFileSize(imageTemplateInfoDTO);
        }

        if (imageTemplateInfo.getLastEditTime() != null) {
            imageTemplateInfoDTO.setLastEditTime(imageTemplateInfo.getLastEditTime().getTime());
        }
        imageTemplateInfoDTO.setImageSystemType(imageTemplateInfo.getCbbOsType().name());
        imageTemplateInfoDTO.setMemory(CapacityUnitUtils.mb2Gb(imageTemplateInfo.getMemory()));

        imageTemplateInfoDTO.setCbbImageType(imageTemplateInfo.getCbbImageType());
        imageTemplateInfoDTO.setNetwork(imageTemplateInfo.getNetworkName());
        if (!CbbImageType.VDI.equals(imageTemplateInfo.getCbbImageType())) {
            buildImageTemplateDriverInfo(imageTemplateInfoDTO);
        }
        VgpuInfoDTO vgpuInfoDTO = imageTemplateInfo.getVgpuInfoDTO();
        if (vgpuInfoDTO != null) {
            imageTemplateInfoDTO.setEnableGpu(vgpuInfoDTO.getVgpuType() != null && vgpuInfoDTO.getVgpuType() != VgpuType.QXL);
            imageTemplateInfoDTO.setVgpuType(vgpuInfoDTO.getVgpuType());
            if (checkVgpuExtraInfo(vgpuInfoDTO.getVgpuExtraInfo())) {
                VgpuExtraInfo vgpuExtraInfo = (VgpuExtraInfo) vgpuInfoDTO.getVgpuExtraInfo();
                imageTemplateInfoDTO.setVgpuModel(deskSpecAPI.buildDefaultAmdModel(vgpuExtraInfo));
                imageTemplateInfoDTO.setVgpuItem(vgpuExtraInfo.getModel());
            }
        }
        imageTemplateInfoDTO.setVgpuInfoDTOHistoryList(imageTemplateInfo.getVgpuInfoDTOHistoryList());
        imageTemplateInfoDTO.setMac(imageTemplateInfo.getMac());
        imageTemplateInfoDTO.setAd(imageTemplateInfo.getAd());
        imageTemplateInfoDTO.setComputerName(imageTemplateInfo.getComputerName());
        UUID clusterId = imageTemplateInfo.getClusterId();
        if (Objects.nonNull(clusterId)) {
            imageTemplateInfoDTO.setClusterInfoDTO(clusterAPI.queryAvailableClusterById(clusterId));
        }
        UUID storagePoolId = imageTemplateInfo.getStoragePoolId();
        if (Objects.nonNull(storagePoolId)) {
            imageTemplateInfoDTO.setStoragePoolName(storagePoolAPI.getStoragePoolDetail(storagePoolId).getName());
        }
        if (imageTemplateInfo.getCbbImageType() == CbbImageType.IDV || imageTemplateInfo.getCbbImageType() == CbbImageType.VOI) {
            imageTemplateInfoDTO.setImageDiskList(cbbImageTemplateMgmtAPI.getAvailableImageDiskInfoList(imageTemplateInfo.getId()));
        }

        imageTemplateInfoDTO.setRootImageId(imageTemplateInfo.getRootImageId());
        imageTemplateInfoDTO.setRootImageName(imageTemplateInfo.getRootImageName());
        imageTemplateInfoDTO.setSourceSnapshotId(imageTemplateInfo.getSourceSnapshotId());
        imageTemplateInfoDTO.setImageRoleType(imageTemplateInfo.getImageRoleType());
        imageTemplateInfoDTO.setNewestVersion(imageTemplateInfo.getNewestVersion());
        imageTemplateInfoDTO.setEnableMultipleVersion(imageTemplateInfo.getEnableMultipleVersion());
        imageTemplateInfoDTO.setImageDriverList(buildImageDriverList(imageTemplateInfo.getImageDriverList()));
        imageTemplateInfoDTO.setHasImportDriverPackage(imageTemplateInfo.getHasImportDriverPackage());
        imageTemplateInfoDTO.setRemoteTerminalImageEditState(imageTemplateInfo.getRemoteTerminalImageEditState());
        imageTemplateInfoDTO.setPlatformName(imageTemplateInfo.getPlatformName());
        imageTemplateInfoDTO.setPlatformId(imageTemplateInfo.getPlatformId());
        imageTemplateInfoDTO.setPlatformType(imageTemplateInfo.getPlatformType());
        imageTemplateInfoDTO.setPlatformStatus(imageTemplateInfo.getPlatformStatus());
        imageTemplateInfoDTO.setCpuArch(imageTemplateInfo.getCpuArch());

        return imageTemplateInfoDTO;
    }

    private void buildImageTemplateDriverInfo(ImageTemplateInfoDTO imageTemplateInfoDTO) {
        try {
            ProductDriverDTO[] driverArr = terminalSelectAPI.listSortedTerminalModel(imageTemplateInfoDTO.getId());
            imageTemplateInfoDTO.setDriverInfoArr(driverArr);
        } catch (BusinessException e) {
            LOGGER.error("镜像[{}]驱动信息查询失败", imageTemplateInfoDTO.getId(), e);
        }
    }

    private void setImageFileSize(ImageTemplateInfoDTO dto) {
        CbbIDVDeskImageTemplateDTO templateDTO;
        List<CbbIDVImageDiskDTO> imageDiskFileInfoList;
        try {
            templateDTO = cbbIDVDeskMgmtAPI.getIDVDeskImageTemplate(dto.getId());
            imageDiskFileInfoList = templateDTO.getImageDiskList();
            LOGGER.debug("从CBB获取到的镜像信息为{}", JSON.toJSONString(templateDTO));
        } catch (Exception e) {
            LOGGER.error("调用cbbIDVDeskMgmtAPI获取的镜像[{}]信息异常，异常信息: ", dto.getId(), e);
            return;
        }
        long sumSystemDiskSize = getImageFileSize(imageDiskFileInfoList, CbbImageDiskType.SYSTEM);
        long sumDataDiskSize = getImageFileSize(imageDiskFileInfoList, CbbImageDiskType.DATA);
        dto.setSystemDiskFileSize(CapacityUnitUtils.byte2GbByCeil(sumSystemDiskSize));
        // 为0说明没有磁盘，设为null
        dto.setDataDiskFileSize(sumDataDiskSize == Constants.DEFAULT_IMAGE_DISK_SIZE ? null : CapacityUnitUtils.byte2GbByCeil(sumDataDiskSize));
    }

    private long getImageFileSize(List<CbbIDVImageDiskDTO> imageDiskFileInfoList, CbbImageDiskType diskType) {
        long sumDiskSize = Constants.DEFAULT_IMAGE_DISK_SIZE;
        for (CbbIDVImageDiskDTO cbbIDVImageDiskDTO : imageDiskFileInfoList) {
            Optional<IDVImageFileDTO> idvImageFileDTO = Optional.ofNullable(cbbIDVImageDiskDTO.getIdvImageFileDTO());
            if (cbbIDVImageDiskDTO.getImageDiskType() == diskType) {
                if (idvImageFileDTO.isPresent()) {
                    long imageSizeFromDTO = ImageTemplateUtil.getImageSizeFromDTO(idvImageFileDTO.get());
                    sumDiskSize += imageSizeFromDTO;
                }
            }
        }
        return sumDiskSize;
    }

    /**
     * 启动镜像编辑
     *
     * @param webRequest 请求参数
     * @param builder 异步任务
     * @param sessionContext sessionContext
     * @return 更新结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("启动镜像编辑")
    @RequestMapping(value = "run", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> runImageTemplate(IdWebRequest webRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(webRequest.getId());
        String imageName = cbbGetImageTemplateInfoDTO.getImageName();
        CbbImageTemplateEditDTO cbbImageTemplateEditDTO =
                new CbbImageTemplateEditDTO(webRequest.getId(), sessionContext.getUserId(), sessionContext.getUserName());
        cbbImageTemplateMgmtAPI.checkImageTemplateEditing(cbbImageTemplateEditDTO);

        // 校验EstClient数量
        checkEstClientNum(webRequest.getId());
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(webRequest.getId()).map(id -> DefaultBatchTaskItem.builder().itemId(id) //
                .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_RUN_TASK_NAME)).build()).iterator();
        RunImageTemplateHandlerRequest request = new RunImageTemplateHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        request.setCmsUpgradeAPI(cmsUpgradeAPI);
        request.setCbbImageDriverMgmtAPI(cbbImageDriverMgmtAPI);
        RunImageTemplateHandler handler = new RunImageTemplateHandler(request);
        BatchTaskSubmitResult batchTaskSubmitResult;
        if (serverModelAPI.isIdvModel() || serverModelAPI.isMiniModel()) {
            // rcm或mini模式下只允许一个镜像在编辑
            imageTemplateAPI.checkHasImageRunning(webRequest.getId());
            batchTaskSubmitResult = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_RUN_TASK_NAME)
                    .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_RUN_DESC, imageName).setUniqueId(RUN_IMAGE_TEMPLATE_UNIQUEID)
                    .registerHandler(handler).start();
        } else {
            batchTaskSubmitResult = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_RUN_TASK_NAME)
                    .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_RUN_DESC, imageName).setUniqueId(webRequest.getId())
                    .registerHandler(handler).start();
        }
        return CommonWebResponse.success(batchTaskSubmitResult);
    }

    /**
     * @apiIgnore
     * @api {POST} /rco/clouddesktop/imageTemplate/startTerminalImage
     * @apiName /startTerminalImage
     * @apiGroup /rco/clouddesktop/imageTemplate
     * @apiDescription 启动编辑镜像的虚机
     * @apiParam (请求体字段说明) {UUID} id 镜像ID
     * @apiParam (请求体字段说明) {String} terminalId 终端Id
     * @apiParam (请求体字段说明) {String} cpuType cpu类型
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "cpuType":"cpuType",
     *                  "id":"7a752038-0dbd-4d4c-89c4-ec7fc6ae4bab",
     *                  "terminalId":"terminalId"
     *                  }
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content":{
     *                    "taskDesc":"正在启动终端镜像模板[{0}]",
     *                    "taskId":"12f6cfba-08d5-4420-9357-31b73f164cac",
     *                    "taskName":"开启终端编辑镜像",
     *                    "taskStatus":"PROCESSING"
     *                    },
     *                    "message":"",
     *                    "msgArgArr":[],
     *                    "msgKey":"",
     *                    "status":"SUCCESS"
     *                    }
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "message":"",
     *                  "msgArgArr":[],
     *                  "msgKey":"",
     *                  "status":"ERROR"
     *                  }
     */
    /**
     * 驱动安装
     *
     * @param webRequest StartTerminalImageWebRequest
     * @param builder BatchTaskBuilder
     * @param sessionContext SessionContext
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("驱动安装")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "startTerminalImage", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse startTerminalVm(StartTerminalImageWebRequest webRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        return doStartTerminalVm(webRequest, builder, sessionContext);

    }

    /**
     * 获取终端列表
     *
     * @param request 参数
     * @param sessionContext sessionContext
     * @return PageQueryResponse
     * @throws BusinessException 异常
     */
    @ApiOperation("获取终端列表")
    @RequestMapping(path = "/terminal/list")
    public DefaultPageResponse list(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        PageSearchRequest pageSearchRequest = new PageSearchRequest(request);

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<String> terminalGroupIdStrList = getPermissionTerminalGroupIdList(sessionContext.getUserId());
            String terminalGroupId = getTerminalGroupId(request);
            if (org.apache.commons.lang3.StringUtils.isEmpty(terminalGroupId)) {
                appendTerminalGroupIdMatchEqual(pageSearchRequest, terminalGroupIdStrList);
            } else {
                if (!terminalGroupIdStrList.contains(terminalGroupId)) {
                    DefaultPageResponse response = new DefaultPageResponse();
                    response.setItemArr(Collections.emptyList().toArray());
                    return response;
                }
            }
        }

        return imageTemplateAPI.queryTerminalList(pageSearchRequest);

    }

    private List<String> getPermissionTerminalGroupIdList(UUID adminId) throws BusinessException {
        ListTerminalGroupIdRequest listTerminalGroupIdRequest = new ListTerminalGroupIdRequest();
        listTerminalGroupIdRequest.setAdminId(adminId);
        ListTerminalGroupIdResponse listTerminalGroupIdResponse = adminDataPermissionAPI.listTerminalGroupIdByAdminId(listTerminalGroupIdRequest);
        return listTerminalGroupIdResponse.getTerminalGroupIdList();
    }

    private void appendTerminalGroupIdMatchEqual(PageSearchRequest request, List<String> userGroupIdStrList) throws BusinessException {
        List<UUID> uuidList = userGroupIdStrList.stream().filter(idStr -> !idStr.equals(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID))
                .map(UUID::fromString).collect(Collectors.toList());
        UUID[] uuidArr = uuidList.toArray(new UUID[uuidList.size()]);
        request.appendCustomMatchEqual(new com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual("terminalGroupId", uuidArr));
    }

    /**
     * 当前前端只会上传一个groupId进行查询
     */
    private String getTerminalGroupId(PageWebRequest request) {
        if (ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (exactMatch.getName().equals("groupId") && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        return "";
    }


    private DefaultWebResponse doStartTerminalVm(StartTerminalImageWebRequest webRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {

        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(webRequest.getId());

        if (cbbGetImageTemplateInfoDTO.getCbbOsType().isLinux()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DRIVER_INSTALL_NOT_SUPPORT_LINUX);
        }

        switch (cbbGetImageTemplateInfoDTO.getCbbImageType()) {
            case IDV:
                return startIdvImageInstallDriver(webRequest, builder, sessionContext);

            case VOI:
                return startTciImageInstallDriver(webRequest, builder, sessionContext, cbbGetImageTemplateInfoDTO);

            default:
                throw new BusinessException(BusinessKey.RCDC_RCO_DRIVER_INSTALL_NOT_SUPPORT);
        }

    }

    private DefaultWebResponse startIdvImageInstallDriver(StartTerminalImageWebRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {

        if (!StringUtils.hasText(webRequest.getCpuType())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CPU_TYPE_CANNOT_NULL);
        }

        // 检测终端是否在线且为有线连接：
        terminalImageEditAPI.checkTerminalOnlineAndWired(webRequest.getTerminalId());
        // 校验是idv终端是否存在编辑镜像如果存在则不允许编辑
        userTerminalMgmtAPI.checkIdvExistsLocalEditImageTemplate(webRequest.getTerminalId());
        // 校验EstClient数量
        checkEstClientNum(webRequest.getId());
        // VNC 放入缓存：
        CbbImageTemplateEditDTO imageTemplateEditRequest = new CbbImageTemplateEditDTO();
        imageTemplateEditRequest.setAdminId(sessionContext.getUserId());
        imageTemplateEditRequest.setImageTemplateId(webRequest.getId());
        imageTemplateEditRequest.setAdminName(sessionContext.getUserName());
        cbbImageTemplateMgmtAPI.editImageTemplate(imageTemplateEditRequest);

        final String imageName = getImageTemplateName(webRequest.getId());
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_TERMINAL_IMAGE_TASK_NAME));
        BatchTaskSubmitResult result = builder//
                .setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_TERMINAL_IMAGE_TASK_NAME)//
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CREATE_TERMINAL_IMAGE_DESC, imageName)//
                .setUniqueId(webRequest.getId())//
                .registerHandler(startTerminalImageTemplateHandlerFactory.createHandler(batchTaskItem, auditLogAPI, webRequest))//
                .start();
        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse startTciImageInstallDriver(StartTerminalImageWebRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext, CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO) throws BusinessException {
        RemoteTerminalEditImageTemplateRequest terminalEditImageTemplateRequest = new RemoteTerminalEditImageTemplateRequest();
        terminalEditImageTemplateRequest.setImageTemplateName(cbbGetImageTemplateInfoDTO.getImageName());
        terminalEditImageTemplateRequest.setTerminalId(webRequest.getTerminalId());
        terminalEditImageTemplateRequest.setImageEditType(ImageEditType.REMOTE_EDIT);
        terminalEditImageTemplateRequest.setCbbImageType(cbbGetImageTemplateInfoDTO.getCbbImageType());
        terminalEditImageTemplateRequest.setImageTemplateId(webRequest.getId());
        terminalEditImageTemplateRequest.setMode(webRequest.getMode());
        return startTerminalEditImage(terminalEditImageTemplateRequest, cbbGetImageTemplateInfoDTO, builder, sessionContext);
    }


    private DefaultWebResponse closeTerminalImageTemplate(CbbGetImageTemplateInfoDTO imageTemplateInfo, BatchTaskBuilder builder)
            throws BusinessException {
        // 检测终端是否在线：
        terminalImageEditAPI.checkTerminalOnline(imageTemplateInfo.getTerminalId());

        CbbCloseTerminalVmDTO closeTerminalVmRequest = new CbbCloseTerminalVmDTO();
        closeTerminalVmRequest.setImageTemplateId(imageTemplateInfo.getId());
        closeTerminalVmRequest.setTerminalId(imageTemplateInfo.getTerminalId());
        String upperMacOrTerminalId = cbbTerminalOperatorAPI.getUpperMacOrTerminalId(imageTemplateInfo.getTerminalId());

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_IMAGE_TASK_NAME));
        BatchTaskSubmitResult result = builder
                .setTaskName(
                        getAbortLocalEditImageTaskNameKey(imageTemplateInfo.getImageVmHost(), imageTemplateInfo.getRemoteTerminalImageEditState()),
                        RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(
                                CbbDriverInstallMode.getValueByImageTerminalLocalEditType(imageTemplateInfo.getTerminalLocalEditType())))
                .setTaskDesc(
                        getAbortLocalEditImageTaskDeskKey(imageTemplateInfo.getImageVmHost(), imageTemplateInfo.getRemoteTerminalImageEditState()),
                        getAbortLocalEditImageTaskArgs(imageTemplateInfo, upperMacOrTerminalId))
                .setUniqueId(imageTemplateInfo.getId()).setUniqueId(imageTemplateInfo.getId())//
                .registerHandler(stopTerminalImageTemplateHandlerFactory.createHandler(batchTaskItem, closeTerminalVmRequest))//
                .start();
        return DefaultWebResponse.Builder.success(result);
    }


    /**
     * 重新远程编辑
     *
     * @param request 请求
     * @param builder BatchTaskBuilder
     * @param sessionContext SessionContext
     * @return BatchTaskSubmitResult
     * @throws BusinessException 异常
     */
    @ApiOperation("启动远程终端重新编辑镜像")
    @RequestMapping(path = "/terminal/reEdit")
    @EnableAuthority
    public DefaultWebResponse startTerminalReEdit(RemoteTerminalEditImageTemplateRequest request, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(request.getImageTemplateId());
        RemoteTerminalEditImageTemplateDTO remoteTerminalEditImageTemplateDTO = new RemoteTerminalEditImageTemplateDTO();
        BeanUtils.copyProperties(request, remoteTerminalEditImageTemplateDTO);
        request.setTerminalId(imageTemplateInfo.getTerminalId());
        request.setImageEditType(ImageEditType.REMOTE_REEDIT);
        return startTerminalEditImage(request, imageTemplateInfo, builder, sessionContext);

    }

    private DefaultWebResponse startTerminalEditImage(RemoteTerminalEditImageTemplateRequest request, CbbGetImageTemplateInfoDTO imageTemplateInfo,
            BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        checkEstClientNum(request.getImageTemplateId());

        RemoteTerminalEditImageTemplateDTO remoteTerminalEditImageTemplateDTO = new RemoteTerminalEditImageTemplateDTO();
        BeanUtils.copyProperties(request, remoteTerminalEditImageTemplateDTO);

        remoteTerminalEditImageTemplateDTO.setImageEditType(request.getImageEditType());
        remoteTerminalEditImageTemplateDTO.setCbbImageType(imageTemplateInfo.getCbbImageType());
        remoteTerminalEditImageTemplateDTO.setImageTemplateName(imageTemplateInfo.getImageName());
        remoteTerminalEditImageTemplateDTO.setEnableNested(imageTemplateInfo.getEnableNested());
        remoteTerminalEditImageTemplateDTO.setMode(request.getMode());
        validRemoteEdit(remoteTerminalEditImageTemplateDTO, imageTemplateInfo, sessionContext);
        CbbTerminalBasicInfoDTO basicInfoByTerminalDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
        remoteTerminalEditImageTemplateDTO.setTerminalMac(basicInfoByTerminalDTO.getUpperMacAddrOrTerminalId());

        if (imageTemplateInfo.getCbbImageType() == CbbImageType.IDV) {
            CbbTerminalModelDTO cbbTerminalModelDTO = cbbTerminalModelAPI.findByProductIdAndPlatform(basicInfoByTerminalDTO.getProductId(),
                    basicInfoByTerminalDTO.getTerminalPlatform());
            remoteTerminalEditImageTemplateDTO.setCpuType(cbbTerminalModelDTO.getCpuType());
        }

        // 设置操作管理员，传给shine做分级分权会话校验
        remoteTerminalEditImageTemplateDTO.setAdminName(sessionContext.getUserName());
        remoteTerminalEditImageTemplateDTO
                .setAdminSessionId(adminMgmtAPI.preLoginTerminalAndGetSessionId(sessionContext.getUserId(), request.getTerminalId()));

        // VNC 放入缓存：
        CbbImageTemplateEditDTO imageTemplateEditRequest = new CbbImageTemplateEditDTO();
        imageTemplateEditRequest.setAdminId(sessionContext.getUserId());
        imageTemplateEditRequest.setImageTemplateId(request.getImageTemplateId());
        imageTemplateEditRequest.setAdminName(sessionContext.getUserName());
        cbbImageTemplateMgmtAPI.editImageTemplate(imageTemplateEditRequest);

        DefaultBatchTaskItem batchTaskItem = new DefaultBatchTaskItem(request.getImageTemplateId(), imageTemplateInfo.getImageName());

        RemoteTerminalEditImageTemplateBatchHandler handler =
                new RemoteTerminalEditImageTemplateBatchHandler(batchTaskItem, remoteTerminalEditImageTemplateDTO);

        handler.setImageTemplateAPI(imageTemplateAPI);
        handler.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        handler.setCbbRemoteTerminalImageMgmtAPI(cbbRemoteTerminalImageMgmtAPI);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbRemoteTerminalImageAPI(cbbRemoteTerminalImageAPI);
        handler.setTerminalOperatorAPI(terminalOperatorAPI);
        RemoteTerminalEditImageLog remoteTerminalEditImageLog =
                RemoteTerminalEditImageLog.getKeyByCbbImageType(imageTemplateInfo.getCbbImageType(), request.getImageEditType());
        BatchTaskSubmitResult result = builder
                .setTaskName(remoteTerminalEditImageLog.getTaskName(),
                        RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()))
                .setTaskDesc(remoteTerminalEditImageLog.getTaskDesc(),
                        RemoteTerminalEditImageLog.getEditImageLogMsgArgsByEditType(remoteTerminalEditImageTemplateDTO))
                .setUniqueId(request.getImageTemplateId()).registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }


    /**
     * 完成远程编辑
     *
     * @param request 请求
     * @param builder BatchTaskBuilder
     * @param sessionContext SessionContext
     * @return BatchTaskSubmitResult
     * @throws BusinessException 异常
     */
    @ApiOperation("完成TCI镜像模板远程编辑")
    @RequestMapping(value = "/terminal/finishEdit", method = RequestMethod.POST)
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse finishRemoteTerminalEdit(RemoteTerminalEditImageTemplateRequest request, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "webRequest can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(request.getImageTemplateId());
        RemoteTerminalEditImageTemplateDTO remoteTerminalEditImageTemplateDTO = new RemoteTerminalEditImageTemplateDTO();
        BeanUtils.copyProperties(request, remoteTerminalEditImageTemplateDTO);
        remoteTerminalEditImageTemplateDTO.setTerminalId(imageTemplateInfo.getTerminalId());
        remoteTerminalEditImageTemplateDTO.setCbbImageType(imageTemplateInfo.getCbbImageType());
        remoteTerminalEditImageTemplateDTO.setImageEditType(ImageEditType.FINISH_EDIT);
        remoteTerminalEditImageTemplateDTO.setImageTemplateName(imageTemplateInfo.getImageName());
        remoteTerminalEditImageTemplateDTO
                .setMode(CbbDriverInstallMode.getValueByImageTerminalLocalEditType(imageTemplateInfo.getTerminalLocalEditType()));

        // 上传失败时，按钮显示“重新上传”，匹配提示语
        if (imageTemplateInfo.getRemoteTerminalImageEditState() == CbbRemoteTerminalImageEditEnum.UPLOAD_TIMEOUT
                || imageTemplateInfo.getRemoteTerminalImageEditState() == CbbRemoteTerminalImageEditEnum.UPLOAD_FAIL) {
            remoteTerminalEditImageTemplateDTO.setImageEditType(ImageEditType.RE_UPLOAD);
        }

        validRemoteEdit(remoteTerminalEditImageTemplateDTO, imageTemplateInfo, sessionContext);

        CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO =
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(remoteTerminalEditImageTemplateDTO.getTerminalId());
        remoteTerminalEditImageTemplateDTO.setTerminalMac(cbbTerminalBasicInfoDTO.getUpperMacAddrOrTerminalId());

        // 只有VOI才有完成编辑
        DefaultBatchTaskItem batchTaskItem = new DefaultBatchTaskItem(request.getImageTemplateId(), imageTemplateInfo.getImageName());
        RemoteTerminalFinishEditImageTemplateBatchHandler handler =
                new RemoteTerminalFinishEditImageTemplateBatchHandler(batchTaskItem, remoteTerminalEditImageTemplateDTO);

        handler.setImageTemplateAPI(imageTemplateAPI);
        handler.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        handler.setCbbRemoteTerminalImageMgmtAPI(cbbRemoteTerminalImageMgmtAPI);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbRemoteTerminalImageAPI(cbbRemoteTerminalImageAPI);
        handler.setTerminalOperatorAPI(terminalOperatorAPI);
        RemoteTerminalEditImageLog remoteTerminalEditImageLog = RemoteTerminalEditImageLog.getKeyByCbbImageType(imageTemplateInfo.getCbbImageType(),
                remoteTerminalEditImageTemplateDTO.getImageEditType());
        BatchTaskSubmitResult result = builder
                .setTaskName(remoteTerminalEditImageLog.getTaskName(),
                        RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(remoteTerminalEditImageTemplateDTO.getMode()))
                .setTaskDesc(remoteTerminalEditImageLog.getTaskDesc(),
                        RemoteTerminalEditImageLog.getEditImageLogMsgArgsByEditType(remoteTerminalEditImageTemplateDTO))
                .setUniqueId(request.getImageTemplateId()).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);

    }

    /**
     * 取消上传
     *
     * @param request 请求
     * @param builder builder
     * @param sessionContext sessionContext
     * @return CommonWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("取消上传")
    @RequestMapping(value = "/terminal/abortUpload", method = RequestMethod.POST)
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse abortRemoteEditImageUpload(RemoteTerminalEditImageTemplateRequest request, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(request.getImageTemplateId());

        RemoteTerminalEditImageTemplateDTO remoteTerminalEditImageTemplateDTO = new RemoteTerminalEditImageTemplateDTO();
        BeanUtils.copyProperties(request, remoteTerminalEditImageTemplateDTO);
        remoteTerminalEditImageTemplateDTO.setImageEditType(ImageEditType.CANCLE_UPLOAD);
        remoteTerminalEditImageTemplateDTO.setCbbImageType(imageTemplateInfo.getCbbImageType());
        remoteTerminalEditImageTemplateDTO.setImageTemplateName(imageTemplateInfo.getImageName());
        remoteTerminalEditImageTemplateDTO
                .setMode(CbbDriverInstallMode.getValueByImageTerminalLocalEditType(imageTemplateInfo.getTerminalLocalEditType()));

        validRemoteEdit(remoteTerminalEditImageTemplateDTO, imageTemplateInfo, sessionContext);
        DefaultBatchTaskItem batchTaskItem = new DefaultBatchTaskItem(request.getImageTemplateId(), imageTemplateInfo.getImageName());

        RemoteTerminalEditImageTemplateCancelUploadBatchHandler handler =
                new RemoteTerminalEditImageTemplateCancelUploadBatchHandler(batchTaskItem, remoteTerminalEditImageTemplateDTO);

        handler.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        handler.setCbbTerminalOperatorAPI(cbbTerminalOperatorAPI);
        handler.setAuditLogAPI(auditLogAPI);
        BatchTaskSubmitResult result = builder
                .setTaskName(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_TASK_NAME,
                        RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()))
                .setTaskDesc(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_DESC,
                        RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()),
                        remoteTerminalEditImageTemplateDTO.getTerminalMac(), imageTemplateInfo.getImageName())
                .setUniqueId(request.getImageTemplateId()).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);

    }


    /**
     * 远程编辑镜像获取终端状态、镜像下载状态
     *
     * @param request 请求
     * @param sessionContext 会话
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("远程编辑镜像获取终端状态和镜像下载状态")
    @RequestMapping(value = "/terminal/getTerminalStateAndImageDownState", method = RequestMethod.POST)
    public DefaultWebResponse getTerminalStateAndImageDownState(RemoteTerminalEditImageDownStateRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "webRequest can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        RemoteTerminalEditImageStateResponse remoteTerminalEditImageStateResponse = new RemoteTerminalEditImageStateResponse();
        CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
        remoteTerminalEditImageStateResponse.setTerminalState(cbbTerminalBasicInfoDTO.getState());
        remoteTerminalEditImageStateResponse.setHasDownloadSuc(true);

        ViewTerminalWithImageDTO viewTerminalWithImageDTO =
                imageTemplateAPI.getTerminalWithImage(String.valueOf(request.getImageTemplateId()), request.getTerminalId());

        if (TERMINAL_DOWNLOAD_IMAGE_COMPLETED.equals(viewTerminalWithImageDTO.getDownloadState())) {
            return DefaultWebResponse.Builder.success(remoteTerminalEditImageStateResponse);
        }

        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(request.getImageTemplateId());

        if (cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState() == CbbRemoteTerminalImageEditEnum.DOWNLOAD_FAIL
                || cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState() == CbbRemoteTerminalImageEditEnum.DOWNLOADING) {
            remoteTerminalEditImageStateResponse.setHasDownloadSuc(false);
            return DefaultWebResponse.Builder.success(remoteTerminalEditImageStateResponse);
        }

        CbbTerminalImageDownloadProgressDTO cbbTerminalImageDownloadProgressDTO =
                cbbImageTemplateMgmtAPI.findDownloadByImageIdAndTerminalId(request.getImageTemplateId(), request.getTerminalId());

        if (CbbRemoteTerminalImageEditEnum.DOWNLOAD_SUCCESS.name().equals(cbbTerminalImageDownloadProgressDTO.getDownloadStatus())) {
            return DefaultWebResponse.Builder.success(remoteTerminalEditImageStateResponse);
        }

        if (cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState() == null
                || cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState() == CbbRemoteTerminalImageEditEnum.NONE
                || cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState() == CbbRemoteTerminalImageEditEnum.WAITING) {
            remoteTerminalEditImageStateResponse.setHasDownloadSuc(false);
        }

        return DefaultWebResponse.Builder.success(remoteTerminalEditImageStateResponse);
    }

    /**
     * we
     * 校验是否能开启远程编辑
     *
     * @param request
     * @param imageTemplateInfo
     * @param sessionContext
     * @throws BusinessException
     */
    private void validRemoteEdit(RemoteTerminalEditImageTemplateDTO request, CbbGetImageTemplateInfoDTO imageTemplateInfo,
            SessionContext sessionContext) throws BusinessException {
        UUID adminId = sessionContext.getUserId();

        String terminalId = Objects.nonNull(request.getTerminalId()) ? request.getTerminalId() : imageTemplateInfo.getTerminalId();
        if (Objects.isNull(terminalId)) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_FAIL_TERMINAL_NOT_EXIT);
        }

        CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        if (Objects.isNull(cbbTerminalBasicInfoDTO)) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_FAIL_TERMINAL_NOT_EXIT);
        }

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {

            Boolean hasImageTemplatePermission =
                    adminDataPermissionAPI.hasDataPermission(adminId, request.getImageTemplateId().toString(), AdminDataPermissionType.IMAGE);
            if (!hasImageTemplatePermission) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_TERMINAL_EDIT_HAS_NO_PERMISSION,
                        cbbTerminalBasicInfoDTO.getUpperMacAddrOrTerminalId(), request.getImageTemplateName(), request.getImageTemplateName());
            }
        }

        if (imageTemplateInfo.getCbbImageType() == CbbImageType.IDV
                && (StringUtils.isBlank(cbbTerminalBasicInfoDTO.getProductId()) || Objects.isNull(cbbTerminalBasicInfoDTO.getTerminalPlatform()))) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_TERMINAL_EDIT_HAS_NO_SUPPORT,
                    cbbTerminalBasicInfoDTO.getUpperMacAddrOrTerminalId(), request.getImageTemplateName());
        }

    }


    private List<ImageRelateDriverInfoDTO> buildImageDriverList(List<DriverWithImageDTO> driverWithImageDTOList) {
        if (!CollectionUtils.isEmpty(driverWithImageDTOList)) {
            return driverWithImageDTOList.stream().map(item -> {
                ImageRelateDriverInfoDTO imageRelateDriverInfoDTO = new ImageRelateDriverInfoDTO();
                imageRelateDriverInfoDTO.setId(item.getDriverId());
                imageRelateDriverInfoDTO.setLabel(item.getDriverName());
                imageRelateDriverInfoDTO.setDriverType(item.getDriverType());
                return imageRelateDriverInfoDTO;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * * 校验文件唯一性
     *
     * @param webRequest request
     * @return DefaultWebResponse 返回结果
     */
    @RequestMapping(value = "/checkDuplication")
    public DefaultWebResponse checkDuplication(CheckImageNameDuplicationWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest is null");
        final CbbCheckImageNameDuplicationDTO request = new CbbCheckImageNameDuplicationDTO();
        request.setImageName(webRequest.getImageName());
        request.setId(webRequest.getId());
        ImageNameDuplicationDTO dto = new ImageNameDuplicationDTO(cbbImageTemplateMgmtAPI.checkImageNameDuplication(request));

        return DefaultWebResponse.Builder.success(dto);

    }

    /**
     * 校验存储空间是否充足
     *
     * @param webRequest request
     * @return response
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "checkSpace")
    public DefaultWebResponse checkSpace(IdWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        CbbImageType imageType = getCbbImageType(webRequest.getId());

        // 只有IDV/VOI才校验
        if (CbbImageType.IDV == imageType || CbbImageType.VOI == imageType) {
            CbbQueryImageSpaceInfoResponseDTO apiResponse = cbbImageTemplateMgmtAPI.queryImageSpaceInfo();
            Long usableSpace = apiResponse.getUsableSpace();
            String usableSpaceFormat = String.format("%.2f%s", CapacityUnitUtils.byte2Gb(usableSpace), USABLE_SIZE_UNIT_G);
            LOGGER.info("当前可用空间[{}]", usableSpaceFormat);
            float currentUsableSpacePercent = usableSpace.floatValue() / apiResponse.getTotalSpace() * 100;
            if (currentUsableSpacePercent < DEFAULT_IMAGE_REMAIN_SPACE_PERCENT) {
                LOGGER.warn("镜像可用空间[{}]，总空间[{}]", apiResponse.getUsableSpace(), apiResponse.getTotalSpace());
                String errorMsg = LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CHECK_IMAGE_SPACE_NOT_ENOUGH,
                        usableSpaceFormat, String.valueOf(DEFAULT_IMAGE_REMAIN_SPACE_PERCENT));
                return DefaultWebResponse.Builder.success(new CheckSpaceResponse(errorMsg));
            }
        }

        return DefaultWebResponse.Builder.success(new CheckSpaceResponse());
    }

    /**
     * 取消锁定 >>放弃IDV本地编辑
     *
     * @param idWebRequest request
     * @param builder builder
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("取消锁定")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/unlockedLocalEditImageTemplate", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse unlockedLocalEditImageTemplate(IdWebRequest idWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idWebRequest, "webRequest can not be null");
        Assert.notNull(builder, "builder can not be null");

        return abortLocalEditImageTemplate(idWebRequest, builder);
    }

    /**
     * 放弃IDV本地编辑
     *
     * @param idWebRequest request
     * @param builder builder
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("放弃IDV本地镜像模板创建")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/abortLocalEditImageTemplate", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse abortLocalEditImageTemplate(IdWebRequest idWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idWebRequest, "webRequest can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID id = idWebRequest.getId();

        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(id);

        CbbImageType imageType = imageTemplateInfo.getCbbImageType();

        // 只有IDV/VOI才校验
        if (CbbImageType.IDV == imageType || CbbImageType.VOI == imageType) {

            String upperMacOrTerminalId = cbbTerminalOperatorAPI.getUpperMacOrTerminalId(imageTemplateInfo.getTerminalId());

            DefaultBatchTaskItem batchTaskItem = new DefaultBatchTaskItem(idWebRequest.getId(), imageTemplateInfo.getImageName());
            AbortTerminalLocalEditImageHandler abortLocalEditImageTemplateHandler =
                    new AbortTerminalLocalEditImageHandler(batchTaskItem, imageTemplateInfo, upperMacOrTerminalId);
            abortLocalEditImageTemplateHandler.setImageTemplateAPI(imageTemplateAPI);

            abortLocalEditImageTemplateHandler.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
            abortLocalEditImageTemplateHandler.setAuditLogAPI(auditLogAPI);
            abortLocalEditImageTemplateHandler.setUpperMacOrTerminalId(upperMacOrTerminalId);
            BatchTaskSubmitResult result = builder.setTaskName(
                    getAbortLocalEditImageTaskNameKey(imageTemplateInfo.getImageVmHost(), imageTemplateInfo.getRemoteTerminalImageEditState()),
                    RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(
                            CbbDriverInstallMode.getValueByImageTerminalLocalEditType(imageTemplateInfo.getTerminalLocalEditType())))
                    .setTaskDesc(
                            getAbortLocalEditImageTaskDeskKey(imageTemplateInfo.getImageVmHost(),
                                    imageTemplateInfo.getRemoteTerminalImageEditState()),
                            getAbortLocalEditImageTaskArgs(imageTemplateInfo, upperMacOrTerminalId))
                    .setUniqueId(imageTemplateInfo.getId())//
                    .registerHandler(abortLocalEditImageTemplateHandler)//
                    .start();
            return DefaultWebResponse.Builder.success(result);

        }

        throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_ABORT_LOCAL_EDIT_IMAGE_TYPE_NO_SUPPORT);

    }


    private String getAbortLocalEditImageTaskNameKey(ImageVmHost imageVmHost, CbbRemoteTerminalImageEditEnum cbbRemoteTerminalImageEditEnum) {
        if (imageVmHost == ImageVmHost.TERMINAL) {
            return CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_IMAGE_TASK_NAME;
        }
        
        if (CbbRemoteTerminalImageEditEnum.hasImageInRemoteEdit(cbbRemoteTerminalImageEditEnum)) {
            return ImageTemplateBusinessKey.RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_NAME;
        }
        return CloudDesktopBusinessKey.RCDC_RCO_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_TASK_NAME;
    }

    private String getAbortLocalEditImageTaskDeskKey(ImageVmHost imageVmHost, CbbRemoteTerminalImageEditEnum cbbRemoteTerminalImageEditEnum) {
        if (imageVmHost == ImageVmHost.TERMINAL) {
            return CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CLOSE_TERMINAL_IMAGE_DESC;
        }

        if (CbbRemoteTerminalImageEditEnum.hasImageInRemoteEdit(cbbRemoteTerminalImageEditEnum)) {
            return ImageTemplateBusinessKey.RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_DESC;
        }
        return CloudDesktopBusinessKey.RCDC_RCO_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_TASK_DESC;
    }

    private String[] getAbortLocalEditImageTaskArgs(CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO, String terminalId) {

        if (cbbGetImageTemplateInfoDTO.getImageVmHost() == ImageVmHost.TERMINAL) {
            return new String[] {cbbGetImageTemplateInfoDTO.getImageName()};
        }

        if (CbbRemoteTerminalImageEditEnum.hasImageInRemoteEdit(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState())) {
            CbbDriverInstallMode cbbDriverInstallMode =
                    CbbDriverInstallMode.getValueByImageTerminalLocalEditType(cbbGetImageTemplateInfoDTO.getTerminalLocalEditType());
            return new String[] {RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(cbbDriverInstallMode), terminalId,
                cbbGetImageTemplateInfoDTO.getImageName()};
        }
        return new String[] {terminalId, cbbGetImageTemplateInfoDTO.getImageName()};
    }


    /**
     * 放弃IDV本地镜像模板的上传（个性和黄金镜像上传）接口
     *
     * @param idWebRequest request
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("放弃IDV本地镜像模板的上传（个性和黄金镜像上传）接口")
    @RequestMapping(value = "/abortLocalEditNewImageTemplate", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public CommonWebResponse<?> abortLocalEditNewImageTemplate(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "webRequest can not be null");
        UUID id = idWebRequest.getId();
        CbbImageType imageType = getCbbImageType(id);
        String imageName = getImageTemplateName(id);
        // 只有IDV/VOI才校验
        if (CbbImageType.IDV == imageType || CbbImageType.VOI == imageType) {
            try {
                imageTemplateAPI.abortLocalTerminalImageExtract(id);
                // 4. 记录操作日志
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_SUCCESS, imageName);
            } catch (BusinessException e) {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_FAIL, e, imageName,
                        e.getI18nMessage());
                return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_FAIL_LOG,
                        new String[] {imageName, e.getI18nMessage()});
            }
        }
        return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_SUCCESS_LOG,
                new String[] {imageName});
    }


    private String getImageTemplateName(UUID imageTemplateId) throws BusinessException {
        return cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId).getImageName();
    }

    private CbbImageType getCbbImageType(UUID imageId) throws BusinessException {
        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        return imageTemplateInfo.getCbbImageType();
    }

    private void checkOsTypeSupportGpu(CbbOsType osType, UUID imageTemplateId, String model) throws BusinessException {
        if (osType == null && imageTemplateId != null) {
            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
            if (imageTemplateDetail != null) {
                osType = imageTemplateDetail.getOsType();
            }
        }

        deskSpecAPI.checkOsTypeSupportGpuModel(model, osType);
    }

    /**
     * 查询镜像模板定时发布任务
     *
     * @param webRequest 请求参数
     * @return 结果
     * @throws BusinessException 转换 CRON 异常
     */
    @ApiOperation("查询镜像模板定时发布任务")
    @RequestMapping(value = "getPublishTask", method = RequestMethod.POST)
    public DefaultWebResponse getImageTemplatePublishTask(GetImageTemplatePublishTaskWebRequest webRequest) throws BusinessException {

        Assert.notNull(webRequest, "webRequest is not null");

        ImageTemplatePublishTaskDTO imageTemplatePublishTaskDTO = packageImageTemplatePublishTaskDTO(webRequest.getImageId());
        return DefaultWebResponse.Builder.success(imageTemplatePublishTaskDTO == null ? new Object() : imageTemplatePublishTaskDTO);
    }

    /**
     * 查询镜像模板定时发布任务
     *
     * @param webRequest 请求参数
     * @return 结果
     * @throws BusinessException 转换 CRON 异常
     */
    @ApiOperation("查询镜像模板定时恢复发布任务")
    @RequestMapping(value = "getRestoreTask", method = RequestMethod.POST)
    public DefaultWebResponse getImageSnapshotRestoreTask(GetSnapshotRestoreTaskWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        ImageSnapshotRestoreTaskDTO restoreTaskDTO = packageImageSnapshotRestoreTaskDTO(webRequest.getImageTemplateId(), null);
        if (restoreTaskDTO != null && webRequest.getSnapshotId().equals(restoreTaskDTO.getSnapshotId())) {
            return DefaultWebResponse.Builder.success(restoreTaskDTO);
        } else {
            return DefaultWebResponse.Builder.success(new Object());
        }
    }

    /**
     * 创建镜像模板定时发布任务
     *
     * @param webRequest 请求参数
     * @param builder 批处理
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建镜像模板定时发布任务")
    @RequestMapping(value = "createOrUpdatePublishTask", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "validateCreateOrUpdatePublishTask")
    @ApiVersions({@ApiVersion(value = Version.V3_2_156, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @EnableAuthority
    public CommonWebResponse<?> createOrUpdatePublishTask(CreateImageTemplatePublishTaskWebRequest webRequest, BatchTaskBuilder builder)
            throws BusinessException {

        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");

        // 校验是否存在
        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(webRequest.getImageId());

        if (!Boolean.TRUE.equals(cbbGetImageTemplateInfoDTO.getEnableMultipleVersion())) {
            if (Objects.isNull(webRequest.getSnapshotName())) {
                throw new BusinessException(I18nKey.SK_VALIDATION_NOTNULL, "snapshotName");
            }
        }

        // 立即发布场景，判断是否存在定时发布任务
        if (webRequest.getPublishType() == ImagePublishType.NORMAL) {
            // 立即发布
            LOGGER.info("镜像 {} 操作立即发布", webRequest.getImageId());
            return CommonWebResponse.success(publishImageTemplate(webRequest, builder));
        }


        if (StringUtils.isEmpty(webRequest.getScheduleTime())) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_TIME_EMPTY_LOG,
                    new String[] {cbbGetImageTemplateInfoDTO.getImageName()});
        }
        // 判断镜像发布时间是否小于当前服务器时间
        Date scheduleDateTime = DateUtil.parseDate(webRequest.getScheduleTime(), DateUtil.YYYY_MM_DD_HH24MISS);
        Date currDate = new Date();
        if (scheduleDateTime == null || scheduleDateTime.before(currDate)) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_TIME_CHECK_ERROR_LOG,
                    new String[] {cbbGetImageTemplateInfoDTO.getImageName()});
        }
        if (cbbGetImageTemplateInfoDTO.getCbbImageType() != CbbImageType.VDI) {
            LOGGER.warn("镜像 {} 不是VDI类型, 无法发布定时任务", webRequest.getImageId());
            return CommonWebResponse.fail(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_VERIFICATION_VDI_LOG,
                    new String[] {CbbImageType.VDI.toString()});
        }
        if (cbbGetImageTemplateInfoDTO.getImageState() != ImageTemplateState.PREPUBLISH) {
            LOGGER.warn("镜像 {} 不是待发布状态, 无法发布定时任务", webRequest.getImageId());
            return CommonWebResponse.fail(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_VERIFICATION_PRE_LOG);
        }

        // 调用参数封装
        CbbPublishImageTemplateTaskDTO cbbPublishImageTemplateTaskDTO = packagePublishTaskRequestDTO(webRequest);
        // 调用 CBB 接口实现定时发布
        final String imageName = cbbGetImageTemplateInfoDTO.getImageName();

        try {
            CbbImageTemplateOperateTaskDTO restoreImageTemplateTask = cbbImageTemplateMgmtAPI.getRestoreImageTemplateTask(webRequest.getImageId());
            if (restoreImageTemplateTask != null && restoreImageTemplateTask.getStatus() == ImageTaskStatus.RUNNING) {
                // 已有定时恢复任务，则取消上次的定时任务
                cbbImageTemplateMgmtAPI.cancelRestoreImageTemplateTask(webRequest.getImageId());
            }

            CbbPublishImageTemplateTaskDTO selectTemplateTaskDTO = cbbImageTemplateMgmtAPI.getPublishImageTemplateTask(webRequest.getImageId());
            if (selectTemplateTaskDTO == null || selectTemplateTaskDTO.getStatus() != ImageTaskStatus.RUNNING) {
                cbbImageTemplateMgmtAPI.createCronPublishImageTemplateTask(cbbPublishImageTemplateTaskDTO);
            } else {
                cbbImageTemplateMgmtAPI.updateCronPublishImageTemplateTask(cbbPublishImageTemplateTaskDTO);
            }
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_SUCCESS_LOG, imageName);
            return CommonWebResponse.success(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_SUCCESS_LOG, new String[] {imageName});
        } catch (BusinessException ex) {
            LOGGER.error(String.format("镜像标识[%s]编辑镜像模板定时发布任务失败", webRequest.getImageId()), ex);
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_ERROR_LOG, ex, imageName, ex.getI18nMessage());
            return CommonWebResponse.fail(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_ERROR_LOG,
                    new String[] {imageName, ex.getI18nMessage()});
        }
    }

    /**
     * 取消镜像模板定时发布任务
     *
     * @param webRequest 请求参数
     * @return 处理结果
     * @throws BusinessException 镜像未存在
     */
    @ApiOperation("取消镜像模板定时发布任务")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "cancelPublishTask", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> cancelImageTemplatePublishTask(CancelImageTemplatePublishTaskWebRequest webRequest) throws BusinessException {

        Assert.notNull(webRequest, "webRequest is not null");

        // 校验是否存在
        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(webRequest.getImageId());
        final String imageName = cbbGetImageTemplateInfoDTO.getImageName();

        try {
            cbbImageTemplateMgmtAPI.cancelPublishImageTemplateTask(webRequest.getImageId());
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_CANCEL_IMAGE_PUBLISH_TASK_SUCCESS_LOG, imageName);
            return CommonWebResponse.success(ImageTemplateBusinessKey.RCDC_RCO_CANCEL_IMAGE_PUBLISH_TASK_SUCCESS_LOG, new String[] {imageName});
        } catch (BusinessException ex) {
            LOGGER.error(String.format("镜像标识[%s]取消镜像模板定时发布任务失败", webRequest.getImageId()), ex);
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_CANCEL_IMAGE_PUBLISH_TASK_ERROR_LOG, ex, imageName, ex.getI18nMessage());
            return CommonWebResponse.fail(ImageTemplateBusinessKey.RCDC_RCO_CANCEL_IMAGE_PUBLISH_TASK_ERROR_LOG,
                    new String[] {imageName, ex.getI18nMessage()});
        }
    }

    /**
     * 交付镜像版本
     *
     * @param request 请求对象
     * @param builder 批任务建造者
     * @param sessionContext session上下文
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("镜像版本回退")
    @ApiVersions({@ApiVersion(value = Version.V3_2_156)})
    @EnableAuthority
    @RequestMapping(value = "/rollback", method = RequestMethod.POST)
    public CommonWebResponse<?> rollback(IdWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(request.getId());
        String imageName = imageTemplateInfo.getImageName();

        generalPermissionHelper.checkPermission(sessionContext, request.getId(), AdminDataPermissionType.IMAGE);

        if (imageTemplateInfo.getImageRoleType() == ImageRoleType.VERSION) {
            // 版本不支持回退版本
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_IS_VERSION_NOT_SUPPORT_ROLLBACK, imageName);
        }
        if (BooleanUtils.isNotTrue(imageTemplateInfo.getEnableMultipleVersion())) {
            // 镜像为开启
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_UN_ENABLE_MULTIPLE_VERSION_NOT_SUPPORT_ROLLBACK, imageName);
        }
        List<ImageTemplateState> allowRollbackStateList =
                Arrays.asList(ImageTemplateState.AVAILABLE, ImageTemplateState.DISABLED, ImageTemplateState.ERROR);
        if (!allowRollbackStateList.contains(imageTemplateInfo.getImageState())) {
            // 镜像模板为发布过
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_ONLY_AVAILABLE_STATE_SUPPORT_ROLLBACK, imageName);
        }

        if (imageTemplateInfo.getLastRecoveryPointId() == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_NOT_EXIST_VERSION_NOT_SUPPORT_ROLLBACK, imageName);
        }

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getId());
        if (imageTemplateDetail.getTempVmId() != null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_HAS_VM, imageName);
        }

        CbbImageTemplateDTO currentImageTemplateVersion = getCurrentImageTemplateVersion(request, imageTemplateInfo);
        CbbImageTemplateDTO previousImageTemplateVersion = getPreviousImageTemplateVersion(request, imageTemplateInfo);

        // 上一个版本的镜像用途和主镜像用途不一致，则不允许回退
        if (previousImageTemplateVersion.getImageUsage() != imageTemplateDetail.getImageUsage()) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCO_IMAGE_TEMPLATE_ROLLBACK_VERSION_USAGE_NO_SAME, imageTemplateInfo.getImageName());
        }

        // 若当前版本有和池做绑定，不允许回退
        if (currentImageTemplateVersion.getImageUsage() == ImageUsageTypeEnum.APP) {
            // 应用池绑定检测
            rcaHostAPI.isExistRelateRcaPoolByImageIdThrowEx(currentImageTemplateVersion.getId());
        }

        String taskNameKey = CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_VERSION_ROLLBACK_TASK_NAME;
        String itemNameKey = CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_VERSION_ROLLBACK_TASK_ITEM_NAME;

        Iterator<? extends I18nBatchTaskItem<?>> iterator = Stream.of(request.getId()) //
                .map(id -> I18nBatchTaskItem.Builder.build(id, itemNameKey, imageName)) //
                .iterator();

        RollbackImageTemplateVersionHandler rollbackImageTemplateVersionHandler = applicationContext.getBean(
                RollbackImageTemplateVersionHandler.class, iterator, currentImageTemplateVersion.getId(), previousImageTemplateVersion.getId());

        BatchTaskSubmitResult result = I18nBatchTaskBuilder.wrap(builder) //
                .setAuditLogAPI(auditLogAPI).setTaskName(taskNameKey) //
                .setUniqueId(request.getId()) //
                .registerHandler(rollbackImageTemplateVersionHandler).start();
        return CommonWebResponse.success(result);
    }

    private CbbImageTemplateDTO getCurrentImageTemplateVersion(IdWebRequest request, CbbGetImageTemplateInfoDTO imageTemplateInfo)
            throws BusinessException {
        PageQueryRequest currentPageQueryRequest = requestBuilderFactory.newRequestBuilder() //
                .eq(ROOT_IMAGE_ID, request.getId()) //
                .eq(SOURCE_SNAPSHOT_ID, imageTemplateInfo.getLastRecoveryPointId()) //
                .setPageLimit(0, 1) //
                .desc(CREATE_TIME) //
                .build();
        PageQueryResponse<CbbImageTemplateDTO> cbbImageTemplatePageResponse = cbbImageTemplateMgmtAPI.pageQuery(currentPageQueryRequest);
        CbbImageTemplateDTO[] cbbImageTemplateDTOArr = cbbImageTemplatePageResponse.getItemArr();

        if (ObjectUtils.isEmpty(cbbImageTemplateDTOArr)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_NOT_EXIST_VERSION_NOT_SUPPORT_ROLLBACK, imageTemplateInfo.getImageName());
        }

        return cbbImageTemplateDTOArr[0];
    }

    private CbbImageTemplateDTO getPreviousImageTemplateVersion(IdWebRequest request, CbbGetImageTemplateInfoDTO imageTemplateInfo)
            throws BusinessException {
        PageQueryRequest previousPageQueryRequest = requestBuilderFactory.newRequestBuilder() //
                .eq(ROOT_IMAGE_ID, request.getId()) //
                .nin(IMAGE_TEMPLATE_STATE, new ImageTemplateState[] {ImageTemplateState.DELETING, ImageTemplateState.CREATING})
                .neq(SOURCE_SNAPSHOT_ID, imageTemplateInfo.getLastRecoveryPointId()) //
                .setPageLimit(0, 1) //
                .desc(CREATE_TIME) //
                .build();
        PageQueryResponse<CbbImageTemplateDTO> cbbImageTemplatePageResponse = cbbImageTemplateMgmtAPI.pageQuery(previousPageQueryRequest);
        CbbImageTemplateDTO[] cbbImageTemplateDTOArr = cbbImageTemplatePageResponse.getItemArr();
        if (ObjectUtils.isEmpty(cbbImageTemplateDTOArr)) {
            // 上一个非删除中的版本不存在
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_NOT_EXIST_PRE_USABLE_VERSION_NOT_SUPPORT_ROLLBACK,
                    imageTemplateInfo.getImageName());
        }
        return cbbImageTemplateDTOArr[0];
    }

    /**
     * 交付镜像版本
     *
     * @param request 请求对象
     * @param builder 批任务建造者
     * @param sessionContext session上下文
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("交付镜像版本")
    @ApiVersions({@ApiVersion(value = Version.V3_2_156)})
    @EnableAuthority
    @RequestMapping(value = "/delivery", method = RequestMethod.POST)
    public CommonWebResponse<?> delivery(DeliveryImageTemplateVersionWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        UUID[] deskPoolIdArr = Optional.ofNullable(request.getDeskPoolIdArr()).orElse(new UUID[0]);
        UUID[] deskIdArr = Optional.ofNullable(request.getDeskIdArr()).orElse(new UUID[0]);

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getImageVersionId());
        checkImageTemplateVersion(imageTemplateDetail);
        Assert.notNull(imageTemplateDetail.getRootImageId(), "imageTemplateDetail.getRootImageId() can not be null");

        generalPermissionHelper.checkPermission(sessionContext, imageTemplateDetail.getRootImageId(), AdminDataPermissionType.IMAGE);

        UUID rootImageId = imageTemplateDetail.getRootImageId();
        UUID imageVersionId = imageTemplateDetail.getId();
        String imageVersionName = imageTemplateDetail.getImageName();

        // 由于批任务未提供扩展暂时先对镜像模板上锁，在批任务结束时释放
        LockUtils.tryLock(String.valueOf(rootImageId));
        try {

            if (request.getDeliveryObjectType() == DeliveryObjectType.PART) {
                checkPartDelivery(deskPoolIdArr, deskIdArr, imageTemplateDetail);
            } else {
                // 获取所有桌面池列表
                deskPoolIdArr = getDeskPoolIdArr(rootImageId, imageVersionId);
                // 获取所有桌面列表
                deskIdArr = getDeskIdArr(rootImageId, imageVersionId);
                if (ObjectUtils.isEmpty(deskPoolIdArr) && ObjectUtils.isEmpty(deskIdArr)) {
                    throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_NOT_BIND_DESK_OR_POOL, imageTemplateDetail.getRootImageName());
                }
            }

            CbbCloudDeskPattern[] patternArr = {CbbCloudDeskPattern.RECOVERABLE};
            List<CloudDesktopDTO> cloudDesktopDTOList = Collections.emptyList();
            if (!ObjectUtils.isEmpty(deskPoolIdArr)) {
                Map<String, Object> map = new HashMap<>();
                map.put(ROOT_IMAGE_ID, rootImageId);
                map.put(IMAGE_VERSION_ID, imageVersionId);
                map.put(DESK_POOL_ID_ARR, deskPoolIdArr);
                map.put(PATTERN_ARR, patternArr);

                cloudDesktopDTOList = getDeskList(map);
                // 桌面池状态校验
                checkDeskPoolState(deskPoolIdArr);

                if (ObjectUtils.isEmpty(deskIdArr) && ObjectUtils.isEmpty(cloudDesktopDTOList)) {
                    // 更新关联桌面池镜像模板ID、状态为更新中
                    cbbDesktopPoolMgmtAPI.changeDesktopPoolImageTemplateId(deskPoolIdArr, imageVersionId);
                    LockUtils.unLock(String.valueOf(rootImageId));
                    auditLogAPI.recordLog(BusinessKey.RCDC_RCO_IMAGE_DELIVERY_SUCCESS, imageVersionName);
                    return CommonWebResponse.success(BusinessKey.RCDC_RCO_IMAGE_DELIVERY_SUCCESS, new String[] {imageVersionName});
                } else {
                    cbbDesktopPoolMgmtAPI.changeDesktopPoolImageTemplateIdAndState(deskPoolIdArr, imageVersionId);
                }
            }

            List<UUID> deskIdList = cloudDesktopDTOList.stream() //
                    .map(cloudDesktopDTO -> cloudDesktopDTO.getCbbId()) //
                    .collect(Collectors.toList());

            deskIdList.addAll(Arrays.asList(deskIdArr));

            BatchTaskSubmitResult batchTaskSubmitResult =
                    startDeliveryImageVersionBatchTask(request, builder, imageVersionName, rootImageId, deskIdList, deskPoolIdArr);
            return CommonWebResponse.success(batchTaskSubmitResult);

        } catch (BusinessException e) {
            LockUtils.unLock(String.valueOf(rootImageId));
            imageVersionName = Optional.ofNullable(imageVersionName).orElse(String.valueOf(request.getImageVersionId()));
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_DELIVERY_IMAGE_TEMPLATE_VERSION_FAIL, imageVersionName, e.getI18nMessage());
            return CommonWebResponse.fail(BusinessKey.RCDC_RCO_DELIVERY_IMAGE_TEMPLATE_VERSION_FAIL,
                    new String[] {imageVersionName, e.getI18nMessage()});
        } catch (Throwable ex) {
            LockUtils.unLock(String.valueOf(rootImageId));
            throw ex;
        }

    }

    private void checkDeskPoolState(UUID[] deskPoolIdArr) throws BusinessException {
        CbbDesktopPoolState[] desktopPoolStateArr = {CbbDesktopPoolState.AVAILABLE};
        Map<String, Object> map = new HashMap<>();
        map.put(DESK_POOL_ID_ARR, deskPoolIdArr);
        map.put(POOL_STATE, desktopPoolStateArr);
        ConditionQueryRequest conditionQueryRequest = buildDeskPoolConditionQueryRequest(map);
        if (desktopPoolMgmtAPI.countByConditions(conditionQueryRequest) < deskPoolIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EXIST_CREATING_OR_DELETING_DESK_POOL);
        }
    }

    private void checkImageTemplateVersion(CbbImageTemplateDetailDTO imageTemplateDetail) throws BusinessException {
        if (imageTemplateDetail.getImageRoleType() != ImageRoleType.VERSION) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_VERSION_NOT_EXIST, String.valueOf(imageTemplateDetail.getId()));
        }
        if (imageTemplateDetail.getImageState() != ImageTemplateState.AVAILABLE) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_VERSION_NOT_AVAILABLE_CAN_NOT_DELIVERY,
                    imageTemplateDetail.getImageName());
        }

        CbbImageTemplateDetailDTO rootImageDto = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateDetail.getRootImageId());
        if (rootImageDto.getImageUsage() != imageTemplateDetail.getImageUsage()) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_VERSION_USAGE_NO_SAME, imageTemplateDetail.getImageName());
        }
    }

    private void checkPartDelivery(UUID[] deskPoolIdArr, UUID[] deskIdArr, CbbImageTemplateDetailDTO imageTemplateDetail) throws BusinessException {
        UUID rootImageId = imageTemplateDetail.getRootImageId();
        if (ObjectUtils.isEmpty(deskPoolIdArr) && ObjectUtils.isEmpty(deskIdArr)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESK_AND_POOL_ID_IS_EMPTY);
        }
        checkDesk(deskIdArr, rootImageId, imageTemplateDetail.getRootImageName(), imageTemplateDetail.getImageSystemSize());
        checkDeskPool(deskPoolIdArr, rootImageId, imageTemplateDetail.getRootImageName(), imageTemplateDetail.getImageSystemSize());
    }

    @SuppressWarnings({"checkstyle:ParameterNumber"})
    private BatchTaskSubmitResult startDeliveryImageVersionBatchTask(DeliveryImageTemplateVersionWebRequest request, BatchTaskBuilder builder,
            String imageVersionName, UUID rootImageId, List<UUID> deskIdList, UUID[] deskPoolIdArr) throws BusinessException {
        List<DefaultBatchTaskItem> batchTaskItemList = deskIdList.stream().distinct() //
                .map(deskId -> { //
                    return DefaultBatchTaskItem.builder() //
                            .itemId(deskId) //
                            .itemName(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_ITEM_NAME)) //
                            .build();
                }).collect(Collectors.toList());

        DeliveryImageTemplateVersionHandler deliveryImageTemplateVersionHandler = applicationContext
                .getBean(DeliveryImageTemplateVersionHandler.class, batchTaskItemList, request, imageVersionName, rootImageId, deskPoolIdArr);


        return builder.setTaskName(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_TASK_NAME) //
                .setTaskDesc(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_DELIVERY_IMAGE_VERSION_TASK_DESC) //
                .registerHandler(deliveryImageTemplateVersionHandler) //
                .enableParallel() //
                .start();
    }

    private UUID[] getDeskIdArr(UUID rootImageId, UUID imageVersionId) throws BusinessException {
        UUID[] deskIdArr;
        CbbCloudDeskState[] deskStateArr = new CbbCloudDeskState[] {CbbCloudDeskState.COMPLETE_DELETING, CbbCloudDeskState.RECYCLE_BIN};
        CbbCloudDeskPattern[] patternArr = new CbbCloudDeskPattern[] {CbbCloudDeskPattern.RECOVERABLE};
        DesktopPoolType[] poolTypeArr = new DesktopPoolType[] {DesktopPoolType.COMMON, DesktopPoolType.STATIC};

        Map<String, Object> map = new HashMap<>();
        map.put(ROOT_IMAGE_ID, rootImageId);
        map.put(IMAGE_VERSION_ID, imageVersionId);
        map.put(DESK_STATE_ARR, deskStateArr);
        map.put(PATTERN_ARR, patternArr);
        map.put(POOL_TYPE_ARR, poolTypeArr);

        List<CloudDesktopDTO> cloudDesktopDTOList = getDeskList(map);
        deskIdArr = cloudDesktopDTOList.stream().filter(dto -> !Objects.equals(CbbCloudDeskType.THIRD.name(), dto.getDesktopCategory()))
                .map(CloudDesktopDTO::getCbbId) //
                .toArray(UUID[]::new);
        return deskIdArr;
    }

    private UUID[] getDeskPoolIdArr(UUID rootImageId, UUID imageVersionId) throws BusinessException {
        UUID[] deskPoolIdArr;
        // 获取所有关联桌面池列表
        CbbDesktopPoolState[] desktopPoolStateArr = {CbbDesktopPoolState.AVAILABLE, CbbDesktopPoolState.UPDATING};
        List<DesktopPoolBasicDTO> desktopPoolBasicDTOList =
                getDesktopPoolList(rootImageId, imageVersionId, null, desktopPoolStateArr, CbbDesktopPoolModel.DYNAMIC);
        deskPoolIdArr = desktopPoolBasicDTOList.stream() //
                .filter(dto -> dto.getPoolType() != CbbDesktopPoolType.THIRD)
                .filter(dto -> dto.getPoolModel() != CbbDesktopPoolModel.STATIC && CbbDesktopSessionType.MULTIPLE != dto.getSessionType())
                .map(DesktopPoolBasicDTO::getId) //
                .toArray(UUID[]::new);
        return deskPoolIdArr;
    }

    private void checkDesk(UUID[] deskIdArr, UUID rootImageId, String rootImageName, Integer systemSize) throws BusinessException {
        if (ObjectUtils.isEmpty(deskIdArr)) {
            return;
        }

        // 部分桌面不存在
        Map<String, Object> partDeskNotExistMap = new HashMap<>();
        partDeskNotExistMap.put(DESK_ID_ARR, deskIdArr);
        ConditionQueryRequest conditionQueryRequest = buildDeskConditionQueryRequest(partDeskNotExistMap);
        if (userDesktopMgmtAPI.countByConditions(conditionQueryRequest) < deskIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_NOT_EXIST);
        }

        CbbCloudDeskPattern[] patternArr = new CbbCloudDeskPattern[] {CbbCloudDeskPattern.RECOVERABLE};
        // 存在非还原桌面
        Map<String, Object> nonRecoverAbleDeskMap = new HashMap<>();
        nonRecoverAbleDeskMap.put(DESK_ID_ARR, deskIdArr);
        nonRecoverAbleDeskMap.put(PATTERN_ARR, patternArr);
        conditionQueryRequest = buildDeskConditionQueryRequest(nonRecoverAbleDeskMap);
        if (userDesktopMgmtAPI.countByConditions(conditionQueryRequest) < deskIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EXIST_APPLAYER_OR_PERSONAL_DESK);
        }

        // 部分云桌面未关联当前镜像模板
        Map<String, Object> partDesktopUncorrelatedImageTemplateMap = new HashMap<>();
        partDesktopUncorrelatedImageTemplateMap.put(ROOT_IMAGE_ID, rootImageId);
        partDesktopUncorrelatedImageTemplateMap.put(DESK_ID_ARR, deskIdArr);
        partDesktopUncorrelatedImageTemplateMap.put(PATTERN_ARR, patternArr);
        conditionQueryRequest = buildDeskConditionQueryRequest(partDesktopUncorrelatedImageTemplateMap);
        if (userDesktopMgmtAPI.countByConditions(conditionQueryRequest) < deskIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_NOT_RELATIVE_IMAGE, rootImageName);
        }
        // 存在桌面系统盘大小小于版本的桌面
        Map<String, Object> sysTemDiskLessThanImageTemplateDiskMap = new HashMap<>();
        sysTemDiskLessThanImageTemplateDiskMap.put(ROOT_IMAGE_ID, rootImageId);
        sysTemDiskLessThanImageTemplateDiskMap.put(DESK_ID_ARR, deskIdArr);
        sysTemDiskLessThanImageTemplateDiskMap.put(PATTERN_ARR, patternArr);
        sysTemDiskLessThanImageTemplateDiskMap.put(SYSTEM_SIZE, systemSize);
        conditionQueryRequest = buildDeskConditionQueryRequest(sysTemDiskLessThanImageTemplateDiskMap);
        if (userDesktopMgmtAPI.countByConditions(conditionQueryRequest) < deskIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_SYSTEM_SIZE_LE_IMAGE, rootImageName);
        }

        // 静态池还原类型桌面支持单独交付
        DesktopPoolType[] poolTypeArr = new DesktopPoolType[] {DesktopPoolType.STATIC, DesktopPoolType.COMMON};
        Map<String, Object> staticRecoverAbleDeskMap = new HashMap<>();
        staticRecoverAbleDeskMap.put(ROOT_IMAGE_ID, rootImageId);
        staticRecoverAbleDeskMap.put(DESK_ID_ARR, deskIdArr);
        staticRecoverAbleDeskMap.put(PATTERN_ARR, patternArr);
        staticRecoverAbleDeskMap.put(POOL_TYPE_ARR, poolTypeArr);
        staticRecoverAbleDeskMap.put(SYSTEM_SIZE, systemSize);
        conditionQueryRequest = buildDeskConditionQueryRequest(staticRecoverAbleDeskMap);
        if (userDesktopMgmtAPI.countByConditions(conditionQueryRequest) < deskIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_IS_POOL_DESK);
        }

        // 第三方桌面不支持交付
        CbbCloudDeskType[] cbbCloudDeskTypeArr = new CbbCloudDeskType[] {CbbCloudDeskType.VDI};
        Map<String, Object> thirdPartyDeskMap = new HashMap<>();
        thirdPartyDeskMap.put(DESK_ID_ARR, deskIdArr);
        thirdPartyDeskMap.put(DESKTOP_TYPE, cbbCloudDeskTypeArr);
        conditionQueryRequest = buildDeskConditionQueryRequest(thirdPartyDeskMap);
        if (userDesktopMgmtAPI.countByConditions(conditionQueryRequest) < deskIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PART_DESK_NOT_DELIVERY);
        }
    }

    private void checkDeskPool(UUID[] deskPoolIdArr, UUID rootImageId, String rootImageName, Integer systemSize) throws BusinessException {
        if (ObjectUtils.isEmpty(deskPoolIdArr)) {
            return;
        }
        // 部分桌面池不存在
        Map<String, Object> partDeskTopPool = new HashMap<>();
        partDeskTopPool.put(DESK_POOL_ID_ARR, deskPoolIdArr);
        partDeskTopPool.put(SYSTEM_SIZE, systemSize);
        ConditionQueryRequest conditionQueryRequest = buildDeskPoolConditionQueryRequest(partDeskTopPool);
        if (desktopPoolMgmtAPI.countByConditions(conditionQueryRequest) < deskPoolIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_POOL_NOT_EXIST);
        }

        // 桌面池未关联镜像模板
        Map<String, Object> deskTopPoolUncorrelatedImageTemplateMap = new HashMap<>();
        deskTopPoolUncorrelatedImageTemplateMap.put(ROOT_IMAGE_ID, rootImageId);
        deskTopPoolUncorrelatedImageTemplateMap.put(DESK_POOL_ID_ARR, deskPoolIdArr);
        conditionQueryRequest = buildDeskPoolConditionQueryRequest(deskTopPoolUncorrelatedImageTemplateMap);
        if (desktopPoolMgmtAPI.countByConditions(conditionQueryRequest) < deskPoolIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_POOL_NOT_RELATIVE_IMAGE, rootImageName);
        }
        // 存在系统盘大小小于版本系统盘大小的桌面
        Map<String, Object> sysTemDiskLessThanImageTemplateDiskMap = new HashMap<>();
        sysTemDiskLessThanImageTemplateDiskMap.put(ROOT_IMAGE_ID, rootImageId);
        sysTemDiskLessThanImageTemplateDiskMap.put(DESK_POOL_ID_ARR, deskPoolIdArr);
        sysTemDiskLessThanImageTemplateDiskMap.put(SYSTEM_SIZE, systemSize);
        conditionQueryRequest = buildDeskPoolConditionQueryRequest(sysTemDiskLessThanImageTemplateDiskMap);
        if (desktopPoolMgmtAPI.countByConditions(conditionQueryRequest) < deskPoolIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_POOL_SYSTEM_SIZE_LT_IMAGE, rootImageName);
        }
        // 部分为非动态模式桌面池
        CbbDesktopPoolModel[] poolModelArr = new CbbDesktopPoolModel[] {CbbDesktopPoolModel.DYNAMIC};
        Map<String, Object> nonDynamicDesktopPool = new HashMap<>();
        nonDynamicDesktopPool.put(DESK_POOL_ID_ARR, deskPoolIdArr);
        nonDynamicDesktopPool.put(POOL_MODEL, poolModelArr);
        conditionQueryRequest = buildDeskPoolConditionQueryRequest(nonDynamicDesktopPool);
        if (desktopPoolMgmtAPI.countByConditions(conditionQueryRequest) < deskPoolIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_POOL_NOT_DYNAMIC);
        }

        // 第三方桌面池不支持交付
        Map<String, Object> thirdPartyDeskMap = new HashMap<>();
        thirdPartyDeskMap.put(DESK_POOL_ID_ARR, deskPoolIdArr);
        thirdPartyDeskMap.put(POOL_TYPE, CbbDesktopPoolType.VDI);
        conditionQueryRequest = buildDeskPoolConditionQueryRequest(thirdPartyDeskMap);
        if (desktopPoolMgmtAPI.countByConditions(conditionQueryRequest) < deskPoolIdArr.length) {
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PART_DESK_NOT_DELIVERY);
        }
    }

    @SuppressWarnings({"checkstyle:ParameterNumber"})
    private List<CloudDesktopDTO> getDeskList(Map<String, Object> map) throws BusinessException {
        ConditionQueryRequest userDesktopListQueryRequest = buildDeskConditionQueryRequest(map);
        return userDesktopMgmtAPI.listByConditions(userDesktopListQueryRequest);
    }

    @SuppressWarnings({"checkstyle:ParameterNumber"})
    private ConditionQueryRequest buildDeskConditionQueryRequest(Map<String, Object> map) {
        ConditionQueryRequestBuilder userDesktopBuilder = new DefaultConditionQueryRequestBuilder();
        if (!ObjectUtils.isEmpty(map.get(DESK_ID_ARR))) {
            userDesktopBuilder.in(CBB_DESKTOP_ID, (UUID[]) map.get(DESK_ID_ARR));
        }
        if (!ObjectUtils.isEmpty(map.get(DESK_POOL_ID_ARR))) {
            userDesktopBuilder.in(DESKTOP_POOL_ID, (UUID[]) map.get(DESK_POOL_ID_ARR));
        }
        if (!ObjectUtils.isEmpty(map.get(ROOT_IMAGE_ID))) {
            userDesktopBuilder.eq(ROOT_IMAGE_ID, map.get(ROOT_IMAGE_ID));
        }
        if (!ObjectUtils.isEmpty(map.get(IMAGE_VERSION_ID))) {
            userDesktopBuilder.neq(IMAGE_TEMPLATE_ID, map.get(IMAGE_VERSION_ID));
        }
        if (!ObjectUtils.isEmpty(map.get(DESK_STATE_ARR))) {
            userDesktopBuilder.nin(DESK_STATE, (CbbCloudDeskState[]) map.get(DESK_STATE_ARR));
        }
        if (!ObjectUtils.isEmpty(map.get(PATTERN_ARR))) {
            userDesktopBuilder.in(PATTERN, (CbbCloudDeskPattern[]) map.get(PATTERN_ARR));
        }
        if (!ObjectUtils.isEmpty(map.get(POOL_TYPE_ARR))) {
            userDesktopBuilder.in(DESKTOP_POOL_TYPE, (DesktopPoolType[]) map.get(POOL_TYPE_ARR));
        }
        if (!ObjectUtils.isEmpty(map.get(SYSTEM_SIZE))) {
            userDesktopBuilder.ge(SYSTEM_SIZE, map.get(SYSTEM_SIZE));
        }
        if (!ObjectUtils.isEmpty(map.get(SESSION_TYPE_ARR))) {
            userDesktopBuilder.in(SESSION_TYPE, (CbbDesktopSessionType[]) map.get(SESSION_TYPE_ARR));
        }
        if (!ObjectUtils.isEmpty(map.get(DESKTOP_TYPE))) {
            userDesktopBuilder.in(DESKTOP_TYPE, (CbbCloudDeskType[]) map.get(DESKTOP_TYPE));
        }

        return userDesktopBuilder.build();
    }

    private List<DesktopPoolBasicDTO> getDesktopPoolList(UUID rootImageId, UUID imageVersionId, UUID[] deskPoolIdArr,
            CbbDesktopPoolState[] poolStateArr, CbbDesktopPoolModel poolModel) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        map.put(ROOT_IMAGE_ID, rootImageId);
        map.put(IMAGE_VERSION_ID, imageVersionId);
        map.put(DESK_POOL_ID_ARR, deskPoolIdArr);
        map.put(POOL_STATE, poolStateArr);

        CbbDesktopPoolModel[] poolModelArr = new CbbDesktopPoolModel[] {poolModel};
        map.put(POOL_MODEL, poolModelArr);

        ConditionQueryRequest deskPoolListQueryRequest = buildDeskPoolConditionQueryRequest(map);
        return desktopPoolMgmtAPI.listByConditions(deskPoolListQueryRequest);
    }

    @SuppressWarnings({"checkstyle:ParameterNumber"})
    private ConditionQueryRequest buildDeskPoolConditionQueryRequest(Map<String, Object> map) {
        ConditionQueryRequestBuilder deskPoolBuilder = new DefaultConditionQueryRequestBuilder();
        if (!ObjectUtils.isEmpty(map.get(DESK_POOL_ID_ARR))) {
            deskPoolBuilder.in(IMAGE_ID, (UUID[]) map.get(DESK_POOL_ID_ARR));
        }
        if (!ObjectUtils.isEmpty(map.get(ROOT_IMAGE_ID))) {
            deskPoolBuilder.eq(ROOT_IMAGE_ID, map.get(ROOT_IMAGE_ID));
        }
        if (!ObjectUtils.isEmpty(map.get(IMAGE_TEMPLATE_ID))) {
            deskPoolBuilder.neq(IMAGE_TEMPLATE_ID, map.get(IMAGE_TEMPLATE_ID));
        }
        if (!ObjectUtils.isEmpty(map.get(POOL_STATE))) {
            deskPoolBuilder.in(POOL_STATE, (CbbDesktopPoolState[]) map.get(POOL_STATE));
        }
        if (!ObjectUtils.isEmpty(map.get(SYSTEM_SIZE))) {
            deskPoolBuilder.ge(SYSTEM_SIZE, map.get(SYSTEM_SIZE));
        }
        if (!ObjectUtils.isEmpty(map.get(POOL_MODEL))) {
            deskPoolBuilder.in(POOL_MODEL, (CbbDesktopPoolModel[]) map.get(POOL_MODEL));
        }
        if (!ObjectUtils.isEmpty(map.get(SESSION_TYPE_ARR))) {
            deskPoolBuilder.in(SESSION_TYPE, (CbbDesktopSessionType[]) map.get(SESSION_TYPE_ARR));
        }
        if (!ObjectUtils.isEmpty(map.get(POOL_TYPE))) {
            deskPoolBuilder.eq(POOL_TYPE, map.get(POOL_TYPE));
        }

        return deskPoolBuilder.build();
    }

    /**
     * 判断 EST Client 是否满足数量
     *
     * @throws BusinessException 未找到部署模式/超出限制大小
     */
    private void checkEstClientNum(UUID imageTemplateId) throws BusinessException {

        if (!cbbImageTemplateMgmtAPI.checkVncEditingImageInfo(imageTemplateId)) {
            long vncCount = cbbImageTemplateMgmtAPI.getVncEditingImageNum() + remoteAssistInfoOperateAPI.remoteAssistNum();
            int vncLimit = estClientMgmtAPI.estClientLimit();
            LOGGER.info("imageTemplateId：{} vncCount：{} vncLimit：{}", imageTemplateId, vncCount, vncLimit);
            if (vncCount + 1 > vncLimit) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PREPARE_EDIT_IMAGETEMPLATE_VNX_LIMIT);
            }
        }
    }

    /**
     * 通过镜像标识查询定时发布镜像模板详细信息
     *
     * @param webRequest 请求入参
     * @return CbbPublishImageTemplateTaskDTO
     */
    private CbbPublishImageTemplateTaskDTO packagePublishTaskRequestDTO(CreateImageTemplatePublishTaskWebRequest webRequest) {

        CbbPublishImageTemplateTaskDTO cbbPublishImageTemplateTaskDTO = new CbbPublishImageTemplateTaskDTO();
        cbbPublishImageTemplateTaskDTO.setImageTemplateId(webRequest.getImageId());
        cbbPublishImageTemplateTaskDTO.setEnableForcePublish(webRequest.getEnableForcePublish());
        cbbPublishImageTemplateTaskDTO.setType(webRequest.getPublishType());
        cbbPublishImageTemplateTaskDTO.setSnapshotName(webRequest.getSnapshotName());
        cbbPublishImageTemplateTaskDTO.setRemark(webRequest.getRemark());
        // 初始化
        Date scheduleDateTime = DateUtil.parseDate(webRequest.getScheduleTime(), DateUtil.YYYY_MM_DD_HH24MISS);
        // 发布时间解析成 cron 表达式
        String date = DateUtil.formatDate(scheduleDateTime, DateUtil.YYYY_MM_DD);
        String time = DateUtil.formatDate(scheduleDateTime, DateUtil.HH24MISS);
        cbbPublishImageTemplateTaskDTO.setCronExpression(imageTemplateAPI.generateExpression(date, time));

        if (!StringUtils.isEmpty(webRequest.getNoticeTime())) {
            cbbPublishImageTemplateTaskDTO.setTipMsgSendTime(DateUtil.parseDate(webRequest.getNoticeTime(), DateUtil.YYYY_MM_DD_HH24MISS));
            cbbPublishImageTemplateTaskDTO.setTipMsg(webRequest.getTipMsg());
        }

        return cbbPublishImageTemplateTaskDTO;
    }

    private ImageTemplatePublishTaskDTO packageImageTemplatePublishTaskDTO(UUID imageId) throws BusinessException {
        // 类型、状态为空则表示必须查询定时发布信息
        return packageImageTemplatePublishTaskDTO(imageId, null, null);
    }

    /**
     * @param webRequest 镜像信息
     * @param builder 批处理
     * @return 批处理结果
     * @throws BusinessException 业务异常
     */
    private BatchTaskSubmitResult publishImageTemplate(CreateImageTemplatePublishTaskWebRequest webRequest, BatchTaskBuilder builder)
            throws BusinessException {
        UUID imageId = webRequest.getImageId();
        Boolean enableForcePublish = webRequest.getEnableForcePublish();
        String snapshotName = webRequest.getSnapshotName();
        String remark = webRequest.getRemark();

        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        String imageName = cbbGetImageTemplateInfoDTO.getImageName();

        isExistTempVmRunning(imageId);

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(imageId).map(id -> DefaultBatchTaskItem.builder().itemId(id) //
                .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_TASK_NAME)).build()).iterator();
        PublishImageTemplateHandlerRequest request = new PublishImageTemplateHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        request.setCmsUpgradeAPI(cmsUpgradeAPI);
        request.setVersionImageName(webRequest.getVersionImageName());
        request.setDesc(webRequest.getDesc());
        request.setSyncMode(webRequest.getSyncType());
        request.setStoragePoolIdArr(webRequest.getStoragePoolIdArr());
        PublishImageTemplateHandler handler = new PublishImageTemplateHandler(request, enableForcePublish, snapshotName, remark);
        handler.setImageSyncMode(webRequest.getSyncType());
        if (Objects.nonNull(webRequest.getStoragePoolIdArr())) {
            handler.setTargetStorageIdList(Arrays.asList(webRequest.getStoragePoolIdArr()));
        }
        return builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_PUBLISH_DESC, imageName).setUniqueId(imageId).registerHandler(handler)
                .start();
    }


    /**
     * 通过镜像标识查询定时发布镜像模板详细信息
     *
     * @param imageId 镜像模板标识
     * @param imageType 镜像模板类型
     * @param imageTemplateState 镜像模板状态
     * @return 定时发布镜像模板详细信息，没有定时发布则返回 null
     */
    private ImageTemplatePublishTaskDTO packageImageTemplatePublishTaskDTO(UUID imageId, CbbImageType imageType,
            ImageTemplateState imageTemplateState) throws BusinessException {

        Assert.notNull(imageId, "imageId is not null");
        ImageTemplatePublishTaskDTO imageTemplatePublishTaskDTO = null;

        // 非待发布状态没有定时发布信息
        if (imageTemplateState != ImageTemplateState.PREPUBLISH && imageTemplateState != null) {
            LOGGER.debug("镜像 {} 非待发布状态没有定时发布信息", imageId);
            // 不存在返回null
            return null;
        }
        // 非 VDI 没有定时发布信息
        if (imageType != CbbImageType.VDI && imageType != null) {
            LOGGER.debug("镜像 {} 非 VDI 没有定时发布信息", imageId);
            // 不存在返回null
            return null;
        }

        CbbPublishImageTemplateTaskDTO cbbPublishImageTemplateTaskDTO = cbbImageTemplateMgmtAPI.getPublishImageTemplateTask(imageId);
        if (cbbPublishImageTemplateTaskDTO != null) {
            LOGGER.debug("镜像 {} 存在定时发布任务, 封装出参", imageId);
            imageTemplatePublishTaskDTO = new ImageTemplatePublishTaskDTO();
            imageTemplatePublishTaskDTO.setId(cbbPublishImageTemplateTaskDTO.getImageTemplateId());

            if (!org.springframework.util.StringUtils.isEmpty(cbbPublishImageTemplateTaskDTO.getCronExpression())) {
                imageTemplatePublishTaskDTO.setScheduleTime(imageTemplateAPI.parseCronExpression(cbbPublishImageTemplateTaskDTO.getCronExpression()));
            }

            if (cbbPublishImageTemplateTaskDTO.getTipMsgSendTime() != null) {
                imageTemplatePublishTaskDTO
                        .setNoticeTime(DateUtil.formatDate(cbbPublishImageTemplateTaskDTO.getTipMsgSendTime(), DateUtil.YYYY_MM_DD_HH24MISS));
            }
            imageTemplatePublishTaskDTO.setModifyTime(cbbPublishImageTemplateTaskDTO.getModifyTime());
            imageTemplatePublishTaskDTO.setTipMsg(cbbPublishImageTemplateTaskDTO.getTipMsg());
            imageTemplatePublishTaskDTO.setErrorMsg(cbbPublishImageTemplateTaskDTO.getErrorMsg());
            imageTemplatePublishTaskDTO.setEnableForcePublish(cbbPublishImageTemplateTaskDTO.getEnableForcePublish());
            imageTemplatePublishTaskDTO.setStatus(cbbPublishImageTemplateTaskDTO.getStatus());
            imageTemplatePublishTaskDTO.setPublishType(cbbPublishImageTemplateTaskDTO.getType());
            imageTemplatePublishTaskDTO.setSnapshotName(cbbPublishImageTemplateTaskDTO.getSnapshotName());
            imageTemplatePublishTaskDTO.setRemark(cbbPublishImageTemplateTaskDTO.getRemark());
        }
        return imageTemplatePublishTaskDTO;
    }

    private boolean checkVgpuExtraInfo(VgpuExtraInfoSupport vgpuExtraInfoSupport) {
        if (vgpuExtraInfoSupport instanceof VgpuExtraInfo) {
            VgpuExtraInfo vgpuExtraInfo = (VgpuExtraInfo) vgpuExtraInfoSupport;
            return Objects.nonNull(vgpuExtraInfo.getGraphicsMemorySize());
        }
        return false;
    }

    private ImageSnapshotRestoreTaskDTO packageImageSnapshotRestoreTaskDTO(UUID imageId, CbbImageType imageType) throws BusinessException {
        Assert.notNull(imageId, "imageId is not null");

        // 非 VDI 没有定时恢复信息
        if (imageType != CbbImageType.VDI && imageType != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("镜像 {} 非 VDI 没有定时恢复信息", imageId);
            }
            // 不存在返回null
            return null;
        }

        CbbImageTemplateOperateTaskDTO operateTaskDTO = cbbImageTemplateMgmtAPI.getRestoreImageTemplateTask(imageId);
        if (operateTaskDTO == null) {
            // 无任务返回空
            return null;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("镜像 {} 存在定时发布任务, 封装出参", imageId);
        }
        ImageSnapshotRestoreTaskDTO imageSnapshotRestoreTaskDTO = new ImageSnapshotRestoreTaskDTO();
        imageSnapshotRestoreTaskDTO.setId(operateTaskDTO.getImageTemplateId());

        if (!StringUtils.isEmpty(operateTaskDTO.getCronExpression())) {
            imageSnapshotRestoreTaskDTO.setScheduleTime(imageTemplateAPI.parseCronExpression(operateTaskDTO.getCronExpression()));
        }

        if (operateTaskDTO.getTipMsgSendTime() != null) {
            imageSnapshotRestoreTaskDTO.setNoticeTime(DateUtil.formatDate(operateTaskDTO.getTipMsgSendTime(), DateUtil.YYYY_MM_DD_HH24MISS));
        }

        imageSnapshotRestoreTaskDTO.setTipMsg(operateTaskDTO.getTipMsg());
        imageSnapshotRestoreTaskDTO.setErrorMsg(operateTaskDTO.getErrorMsg());
        imageSnapshotRestoreTaskDTO.setEnableForceRestore(operateTaskDTO.getEnableForcePublish());
        imageSnapshotRestoreTaskDTO.setStatus(operateTaskDTO.getStatus());
        imageSnapshotRestoreTaskDTO.setRestoreType(operateTaskDTO.getType());
        imageSnapshotRestoreTaskDTO.setSnapshotId(operateTaskDTO.getSnapshotId());
        try {
            ImageRestorePointDTO imageSnapshot = cbbImageTemplateMgmtAPI.getImageSnapshotById(operateTaskDTO.getSnapshotId());
            if (imageSnapshot != null) {
                imageSnapshotRestoreTaskDTO.setName(imageSnapshot.getName());
            }
        } catch (BusinessException ex) {
            LOGGER.error("获取还原快照名称发生异常, ex: ", ex);
        }
        imageSnapshotRestoreTaskDTO.setModifyTime(operateTaskDTO.getModifyTime());
        return imageSnapshotRestoreTaskDTO;
    }

    /**
     * 分页查询镜像模板
     *
     * @param webRequest 请求参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询快照列表")
    @RequestMapping(value = "listSnapshot")
    public DefaultWebResponse listSnapshot(IdWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        List<ImageRestorePointDTO> snapshotList = cbbImageTemplateMgmtAPI.listImageSnapshot(webRequest.getId());
        if (CollectionUtils.isEmpty(snapshotList)) {
            return DefaultWebResponse.Builder.success();
        }

        CbbImageTemplateDetailDTO image = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getId());
        if (image == null) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_IMAGE_TEMPLATE_NOT_FOUND);
        }

        List<SnapshotInfoDTO> snapshotInfoDTOList = new ArrayList<>();
        for (ImageRestorePointDTO restorePointDTO : snapshotList) {
            SnapshotInfoDTO snapshotInfoDTO = new SnapshotInfoDTO();
            BeanUtils.copyProperties(restorePointDTO, snapshotInfoDTO);
            snapshotInfoDTO.setImageName(image.getImageName());
            // 绑定链接克隆个性云桌面数量
            snapshotInfoDTO.setClouldDeskopNumOfPersonal(image.getClouldDeskopNumOfPersonal());
            // 绑定链接克隆还原的云桌面数量
            snapshotInfoDTO.setClouldDeskopNumOfRecoverable(image.getClouldDeskopNumOfRecoverable());
            // 绑定链接克隆应用分层的云桌面数量
            snapshotInfoDTO.setClouldDeskopNumOfAppLayer(image.getClouldDeskopNumOfAppLayer());
            // 镜像类型 VDI个性镜像不支持还原镜像快照
            snapshotInfoDTO.setCbbImageType(image.getCbbImageType());
            snapshotInfoDTO.setPlatformName(image.getPlatformName());
            snapshotInfoDTO.setPlatformId(image.getPlatformId());
            snapshotInfoDTO.setPlatformType(image.getPlatformType());
            snapshotInfoDTO.setPlatformStatus(image.getPlatformStatus());
            snapshotInfoDTOList.add(snapshotInfoDTO);
        }

        SnapshotInfoDTO[] snapshotInfoArr = new SnapshotInfoDTO[snapshotInfoDTOList.size()];
        snapshotInfoDTOList.toArray(snapshotInfoArr);
        PageResponseContent<SnapshotInfoDTO> pageResponseContent = new PageResponseContent<>(snapshotInfoArr, snapshotInfoArr.length);
        return DefaultWebResponse.Builder.success(pageResponseContent);
    }


    /**
     * 创建镜像快照定时恢复任务
     *
     * @param webRequest 请求参数
     * @param builder 批处理
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建镜像快照定时恢复任务")
    @RequestMapping(value = "createOrUpdateSnapshotRestoreTask", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "validateCreateOrUpdateSnapshotRestoreTask")
    @EnableAuthority
    public CommonWebResponse<?> createOrUpdateSnapshotRestoreTask(ImageSnapshotRestoreTaskWebRequest webRequest, BatchTaskBuilder builder)
            throws BusinessException {

        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");

        LOGGER.info("创建[{}]镜像快照定时恢复任务:[{}]", webRequest.getImageTemplateId(), webRequest.getSnapshotId());
        CbbImageTemplateDetailDTO image = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getImageTemplateId());

        ImageRestorePointDTO restorePointDTO = cbbImageTemplateMgmtAPI.getImageSnapshotById(webRequest.getSnapshotId());
        if (restorePointDTO == null) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_NOT_FOUND);
        }

        isExistTempVmRunning(webRequest.getImageTemplateId());

        // 校验是否允许恢复快照
        imageTemplateAPI.vaildImageRestoreForSnapshot(webRequest.getImageTemplateId());

        if (webRequest.getRestoreType() == SnapshotRestoreType.NORMAL) {
            LOGGER.info("镜像[{}]操作立即恢复快照[{}]", webRequest.getImageTemplateId(), webRequest.getSnapshotId());
            return CommonWebResponse.success(restoreSnapshot(webRequest, builder));
        }

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getImageTemplateId());
        if (org.springframework.util.StringUtils.isEmpty(webRequest.getScheduleTime())) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_TASK_TIME_EMPTY_LOG,
                    new String[] {imageTemplateDetail.getImageName(), restorePointDTO.getName()});
        }
        // 判断镜像发布时间是否小于当前服务器时间
        Date scheduleDateTime = DateUtil.parseDate(webRequest.getScheduleTime(), DateUtil.YYYY_MM_DD_HH24MISS);
        Date currDate = new Date();
        if (scheduleDateTime == null || scheduleDateTime.before(currDate)) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_TASK_TIME_CHECK_ERROR_LOG,
                    new String[] {imageTemplateDetail.getImageName(), restorePointDTO.getName()});
        }

        // 调用参数封装
        CbbImageTemplateOperateTaskDTO operateTaskDTO = packageRestoreSnapshotTaskRequestDTO(webRequest);

        // 调用 CBB 接口实现定时发布
        try {
            cancelLastCronTask(webRequest.getImageTemplateId());
            cbbImageTemplateMgmtAPI.createCronRestoreImageTemplateTask(operateTaskDTO);
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_TASK_SUCCESS_LOG, image.getImageName(), restorePointDTO.getName());
            return CommonWebResponse.success(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_TASK_SUCCESS_LOG,
                    new String[] {image.getImageName(), restorePointDTO.getName()});
        } catch (BusinessException ex) {
            LOGGER.error(String.format("镜像[%s]创建快照[%s]定时恢复任务失败", image.getImageName(), webRequest.getSnapshotId()), ex);
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_TASK_ERROR_LOG, image.getImageName(), restorePointDTO.getName(),
                    ex.getI18nMessage());
            return CommonWebResponse.fail(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_TASK_ERROR_LOG,
                    new String[] {image.getImageName(), restorePointDTO.getName(), ex.getI18nMessage()});
        }
    }

    private void cancelLastCronTask(UUID imageId) throws BusinessException {
        CbbPublishImageTemplateTaskDTO publishImageTemplateTask = cbbImageTemplateMgmtAPI.getPublishImageTemplateTask(imageId);
        if (publishImageTemplateTask != null && publishImageTemplateTask.getStatus() == ImageTaskStatus.RUNNING) {
            // 已有定时发布任务，则取消上次的定时任务
            cbbImageTemplateMgmtAPI.cancelPublishImageTemplateTask(imageId);
        }

        CbbImageTemplateOperateTaskDTO restoreImageTemplateTask = cbbImageTemplateMgmtAPI.getRestoreImageTemplateTask(imageId);
        if (restoreImageTemplateTask != null && restoreImageTemplateTask.getStatus() == ImageTaskStatus.RUNNING) {
            // 已有定时恢复任务，则取消上次的定时任务
            cbbImageTemplateMgmtAPI.cancelRestoreImageTemplateTask(imageId);
        }
    }

    private CbbImageTemplateOperateTaskDTO packageRestoreSnapshotTaskRequestDTO(ImageSnapshotRestoreTaskWebRequest webRequest) {
        CbbImageTemplateOperateTaskDTO operateTaskDTO = new CbbImageTemplateOperateTaskDTO();
        operateTaskDTO.setImageTemplateId(webRequest.getImageTemplateId());
        operateTaskDTO.setEnableForcePublish(webRequest.getEnableForceRestore());
        operateTaskDTO.setType(ImagePublishType.CRON);
        operateTaskDTO.setOperate(CbbImageOperateType.RESTORE);
        operateTaskDTO.setSnapshotId(webRequest.getSnapshotId());

        // 通知时间不为空
        if (!StringUtils.isEmpty(webRequest.getNoticeTime())) {
            // 通知时间
            operateTaskDTO.setTipMsgSendTime(DateUtil.parseDate(webRequest.getNoticeTime(), DateUtil.YYYY_MM_DD_HH24MISS));
            // 通知信息
            operateTaskDTO.setTipMsg(webRequest.getTipMsg());

        }
        // 初始化
        Date scheduleDateTime = DateUtil.parseDate(webRequest.getScheduleTime(), DateUtil.YYYY_MM_DD_HH24MISS);
        // 发布时间解析成 cron 表达式
        String date = DateUtil.formatDate(scheduleDateTime, DateUtil.YYYY_MM_DD);
        String time = DateUtil.formatDate(scheduleDateTime, DateUtil.HH24MISS);
        operateTaskDTO.setCronExpression(imageTemplateAPI.generateExpression(date, time));
        return operateTaskDTO;
    }

    /**
     * 取消镜像模板定时发布任务
     *
     * @param webRequest 请求参数
     * @return 处理结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("取消镜像模板快照定时恢复任务")
    @RequestMapping(value = "cancelSnapshotRestoreTask", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> cancelSnapshotRestoreTask(IdWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        LOGGER.info("取消镜像模板[{}]定时恢复快照", webRequest.getId());
        CbbImageTemplateDetailDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getId());
        if (cbbGetImageTemplateInfoDTO == null) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_IMAGE_TEMPLATE_NOT_FOUND);
        }

        try {
            cbbImageTemplateMgmtAPI.cancelRestoreImageTemplateTask(webRequest.getId());
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_CANCEL_IMAGE_SNAPSHOT_TASK_SUCCESS_LOG,
                    cbbGetImageTemplateInfoDTO.getImageName());
            return CommonWebResponse.success(ImageTemplateBusinessKey.RCDC_RCO_CANCEL_IMAGE_SNAPSHOT_TASK_SUCCESS_LOG,
                    new String[] {cbbGetImageTemplateInfoDTO.getImageName()});
        } catch (BusinessException ex) {
            LOGGER.error(String.format("镜像[%s]取消镜像模板定时恢复快照任务失败", cbbGetImageTemplateInfoDTO.getImageName()), ex);
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_CANCEL_IMAGE_SNAPSHOT_TASK_ERROR_LOG, cbbGetImageTemplateInfoDTO.getImageName(),
                    ex.getI18nMessage());
            return CommonWebResponse.fail(ImageTemplateBusinessKey.RCDC_RCO_CANCEL_IMAGE_SNAPSHOT_TASK_ERROR_LOG,
                    new String[] {cbbGetImageTemplateInfoDTO.getImageName(), ex.getI18nMessage()});
        }
    }

    private BatchTaskSubmitResult restoreSnapshot(ImageSnapshotRestoreTaskWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        UUID imageId = webRequest.getImageTemplateId();
        Boolean enableForcePublish = webRequest.getEnableForceRestore();
        UUID snapshotId = webRequest.getSnapshotId();
        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        String imageName = cbbGetImageTemplateInfoDTO.getImageName();
        ImageRestorePointDTO restorePointDTO = cbbImageTemplateMgmtAPI.getImageSnapshotById(snapshotId);
        String snapshotName = restorePointDTO.getName();

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(imageId).map(id -> DefaultBatchTaskItem.builder().itemId(id) //
                .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_IMAGE_SNAPSHOT_RESTORE_TASK_NAME)).build()).iterator();
        RestoreSnapshotHandlerRequest request = new RestoreSnapshotHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        RestoreSnapshotHandler handler = new RestoreSnapshotHandler(request, enableForcePublish, snapshotId, snapshotName);
        return builder.setTaskName(CloudDesktopBusinessKey.RCDC_IMAGE_SNAPSHOT_RESTORE_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_IMAGE_SNAPSHOT_RESTORE_DESC, imageName, snapshotName).setUniqueId(imageId)
                .registerHandler(handler).start();
    }


    /**
     * 锁定镜像快照
     *
     * @param webRequest 请求参数
     * @return 处理结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("锁定镜像快照")
    @RequestMapping(value = "lockSnapshot", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> lockSnapshot(LockSnapshotWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        LOGGER.info("锁定镜像:[{}]快照:[{}]", webRequest.getImageTemplateId(), webRequest.getSnapshotId());
        CbbImageTemplateDetailDTO image = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getImageTemplateId());
        if (image == null) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_IMAGE_TEMPLATE_NOT_FOUND);
        }

        // 镜像在发布状态下不允许锁定或解锁快照
        if (ImageTemplateState.PUBLISHING == image.getImageState()) {
            throw new BusinessException(webRequest.getEnableLocked() ? ImageTemplateBusinessKey.RCDC_RCO_NOT_ALLOW_LOCK_SNAPSHOT_WHEN_IMAGE_PUBLISHING
                    : ImageTemplateBusinessKey.RCDC_RCO_NOT_ALLOW_UNLOCK_SNAPSHOT_WHEN_IMAGE_PUBLISHING);
        }

        ImageRestorePointDTO restorePointDTO = cbbImageTemplateMgmtAPI.getImageSnapshotById(webRequest.getSnapshotId());
        if (restorePointDTO == null) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_NOT_FOUND);
        }

        Boolean enableLocked = webRequest.getEnableLocked();
        try {
            CbbSetSnapshotLockRequest request = new CbbSetSnapshotLockRequest();
            request.setRestorePointId(webRequest.getSnapshotId());
            request.setEnableLocked(webRequest.getEnableLocked());
            cbbImageTemplateMgmtAPI.setImageSnapshotLock(request);
            if (enableLocked) {
                auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_LOCK_SUCCESS, image.getImageName(), restorePointDTO.getName());
                return CommonWebResponse.success(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_LOCK_SUCCESS,
                        new String[] {image.getImageName(), restorePointDTO.getName()});
            } else {
                auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_UNLOCK_SUCCESS, image.getImageName(),
                        restorePointDTO.getName());
                return CommonWebResponse.success(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_UNLOCK_SUCCESS,
                        new String[] {image.getImageName(), restorePointDTO.getName()});
            }
        } catch (BusinessException e) {
            if (enableLocked) {
                LOGGER.error(String.format("镜像[%s]锁定快照[%s]失败", image.getImageName(), restorePointDTO.getName()), e);
                auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_LOCK_FAIL, image.getImageName(), restorePointDTO.getName(),
                        e.getI18nMessage());
                return CommonWebResponse.fail(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_LOCK_FAIL,
                        new String[] {image.getImageName(), restorePointDTO.getName(), e.getI18nMessage()});
            } else {
                LOGGER.error(String.format("镜像[%s]解锁快照[%s]失败", image.getImageName(), restorePointDTO.getName()), e);
                auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_UNLOCK_FAIL, image.getImageName(), restorePointDTO.getName(),
                        e.getI18nMessage());
                return CommonWebResponse.fail(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_UNLOCK_FAIL,
                        new String[] {image.getImageName(), restorePointDTO.getName(), e.getI18nMessage()});
            }
        }
    }

    /**
     * 删除快照
     *
     * @param webRequest 请求参数
     * @param builder 批处理
     * @return 处理结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除快照")
    @RequestMapping(value = "deleteSnapshot", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> deleteSnapshot(DeleteSnapshotWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {

        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");

        LOGGER.info("删除镜像快照:[{}]", webRequest.getSnapshotIdArr());
        cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getImageTemplateId());
        return CommonWebResponse.success(deleteSnapshotHandle(webRequest, builder));
    }

    private BatchTaskSubmitResult deleteSnapshotHandle(DeleteSnapshotWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        UUID imageId = webRequest.getImageTemplateId();
        CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        String imageName = cbbGetImageTemplateInfoDTO.getImageName();

        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(webRequest.getSnapshotIdArr())
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(LocaleI18nResolver.resolve(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_DELETE_TASK_NAME)).build())
                        .iterator();
        DeleteSnapshotHandlerRequest request = new DeleteSnapshotHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        DeleteSnapshotHandler handler = new DeleteSnapshotHandler(request, imageId);
        return builder.setTaskName(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_DELETE_TASK_NAME)
                .setTaskDesc(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_DELETE_DESC, imageName).setUniqueId(imageId).registerHandler(handler)
                .start();
    }

    /**
     * * 校验快照唯一性
     *
     * @param webRequest request
     * @return DefaultWebResponse 返回结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/checkSnapshotNameDuplication")
    public DefaultWebResponse checkSnapshotNameDuplication(CheckImageSnapshotNameDuplicationWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        CbbCheckImageSnapshotNameDuplicationDTO snapshotNameDuplicationDTO = new CbbCheckImageSnapshotNameDuplicationDTO();
        snapshotNameDuplicationDTO.setSnapshotId(webRequest.getSnapshotId());
        snapshotNameDuplicationDTO.setSnapshotName(webRequest.getSnapshotName());
        ImageNameDuplicationDTO dto =
                new ImageNameDuplicationDTO(cbbImageTemplateMgmtAPI.checkImageSnapshotNameDuplication(snapshotNameDuplicationDTO));
        return DefaultWebResponse.Builder.success(dto);
    }

    /**
     * 快照重命名
     *
     * @param webRequest 请求参数
     * @return 处理结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("快照重命名")
    @RequestMapping(value = "renameSnapshot", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> renameSnapshot(RenameSnapshotWebRequest webRequest) throws BusinessException {

        Assert.notNull(webRequest, "webRequest is not null");

        LOGGER.info("[{}]快照重命名[{}]", webRequest.getSnapshotId(), webRequest.getName());
        CbbImageTemplateDetailDTO image = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getImageTemplateId());


        ImageRestorePointDTO restorePointDTO = cbbImageTemplateMgmtAPI.getImageSnapshotById(webRequest.getSnapshotId());
        if (restorePointDTO == null) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_NOT_FOUND);
        }

        CbbCheckImageSnapshotNameDuplicationDTO snapshotNameDuplicationDTO = new CbbCheckImageSnapshotNameDuplicationDTO();
        snapshotNameDuplicationDTO.setSnapshotId(webRequest.getSnapshotId());
        snapshotNameDuplicationDTO.setSnapshotName(webRequest.getName());
        if (cbbImageTemplateMgmtAPI.checkImageSnapshotNameDuplication(snapshotNameDuplicationDTO)) {
            return CommonWebResponse.fail(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_RENAME_FAIL_LOG, new String[] {image.getImageName(),
                webRequest.getName(), LocaleI18nResolver.resolve(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_RENAME_FAIL_REASON)});
        }


        CbbSetSnapshotInfoRequest request = new CbbSetSnapshotInfoRequest();
        request.setRestorePointId(webRequest.getSnapshotId());
        request.setSnapshotName(webRequest.getName());
        request.setRemark(webRequest.getRemark());
        cbbImageTemplateMgmtAPI.setImageSnapInfo(request);
        auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_RENAME_SUCCESS_LOG, image.getImageName(), webRequest.getName());
        return CommonWebResponse.success(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_RENAME_SUCCESS_LOG,
                new String[] {image.getImageName(), webRequest.getName()});
    }

    /**
     * 设置快照最大数量
     *
     * @param webRequest 请求参数
     * @return 处理结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("设置快照最大数量")
    @RequestMapping(value = "configSnapshotLimit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> configSnapshotLimit(SetSnapshotMaxNumWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        LOGGER.info("设置镜像[{}]快照最大数量:[{}]", webRequest.getImageTemplateId(), webRequest.getMaxNum());
        CbbImageTemplateDetailDTO image = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getImageTemplateId());
        if (image == null) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_IMAGE_TEMPLATE_NOT_FOUND);
        }

        try {

            List<ImageRestorePointDTO> restorePointDTOList = cbbImageTemplateMgmtAPI.listImageSnapshot(webRequest.getImageTemplateId());
            if (!CollectionUtils.isEmpty(restorePointDTOList) && restorePointDTOList.size() > webRequest.getMaxNum()) {
                String businessKey = image.getEnableMultipleVersion() ? ImageTemplateBusinessKey.RCDC_RCO_IMAGE_VERSION_SET_MAX_NUM_FAIL
                        : ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_SET_MAX_NUM_FAIL;
                throw new BusinessException(businessKey, image.getImageName(), Integer.toString(restorePointDTOList.size()),
                        Integer.toString(webRequest.getMaxNum()));
            }

            CbbSetSnapshotNumRequest request = new CbbSetSnapshotNumRequest();
            request.setImageId(webRequest.getImageTemplateId());
            request.setMaxNum(webRequest.getMaxNum());
            // 自动生成快照
            request.setEnableCreateSnapshot(Boolean.TRUE);
            cbbImageTemplateMgmtAPI.setMaxImageSnapshotNum(request);
            String businessKey = image.getEnableMultipleVersion() ? ImageTemplateBusinessKey.RCDC_RCO_IMAGE_VERSION_SET_MAX_NUM_SUCCESS_LOG
                    : ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_SET_MAX_NUM_SUCCESS_LOG;
            auditLogAPI.recordLog(businessKey, image.getImageName(), webRequest.getMaxNum().toString());
            return CommonWebResponse.success(businessKey, new String[] {image.getImageName(), webRequest.getMaxNum().toString()});
        } catch (BusinessException ex) {
            LOGGER.error(String.format("镜像[%s]设置快照最大数量[%s]失败", image.getImageName(), webRequest.getMaxNum()), ex);
            String businessKey = image.getEnableMultipleVersion() ? ImageTemplateBusinessKey.RCDC_RCO_IMAGE_VERSION_SET_MAX_NUM_FAIL_LOG
                    : ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_SET_MAX_NUM_FAIL_LOG;
            auditLogAPI.recordLog(businessKey, image.getImageName(), webRequest.getMaxNum().toString(), ex.getI18nMessage());
            return CommonWebResponse.fail(businessKey, new String[] {image.getImageName(), webRequest.getMaxNum().toString(), ex.getI18nMessage()});
        }
    }

    /**
     * 查询快照最大数量
     *
     * @param webRequest 请求参数
     * @return 处理结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询快照最大数量")
    @RequestMapping(value = "getSnapshotLimit", method = RequestMethod.POST)
    public CommonWebResponse<?> getSnapshotLimit(GetSnapshotMaxNumWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        GetSnapshotMaxNumDTO getSnapshotMaxNumDTO = new GetSnapshotMaxNumDTO();
        CbbSetSnapshotConfigDTO maxSnapshotConfig = cbbImageTemplateMgmtAPI.getMaxSnapshotConfig(webRequest.getImageTemplateId());
        getSnapshotMaxNumDTO.setImageTemplateId(maxSnapshotConfig.getImageId());
        getSnapshotMaxNumDTO.setEnableCreateSnapshot(maxSnapshotConfig.getEnableCreateSnapshot());
        getSnapshotMaxNumDTO.setMaxNum(maxSnapshotConfig.getMaxNum());
        getSnapshotMaxNumDTO.setGlobalMaxNum(maxSnapshotConfig.getGlobalMaxNum());
        getSnapshotMaxNumDTO.setEnableMultipleVersion(maxSnapshotConfig.getEnableMultipleVersion());
        return CommonWebResponse.success(getSnapshotMaxNumDTO);
    }

    /**
     * * 校验镜像计算机名称唯一性
     *
     * @param webRequest 入参
     * @return DefaultWebResponse 返回结果
     */
    @RequestMapping(value = "checkComputerNameDuplication")
    public DefaultWebResponse checkComputerNameDuplication(CheckComputerNameDuplicationWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest is null");
        final CbbCheckComputerNameDuplicationDTO request = new CbbCheckComputerNameDuplicationDTO();
        request.setComputerName(webRequest.getComputerName());
        request.setId(webRequest.getId());
        ComputerNameDuplicationDTO dto = new ComputerNameDuplicationDTO(cbbImageTemplateMgmtAPI.checkComputerNameDuplication(request));

        return DefaultWebResponse.Builder.success(dto);
    }

    /**
     * 修复镜像
     *
     * @param webRequest 请求参数
     * @param builder 批任务构造器
     * @return 处理结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("修复镜像")
    @RequestMapping(value = "repairImageTemplate", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> repairImageTemplate(IdWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");

        CbbImageTemplateDetailDTO image = cbbImageTemplateMgmtAPI.getImageTemplateDetail(webRequest.getId());
        if (image == null) {
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_IMAGE_TEMPLATE_NOT_FOUND);
        }

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(webRequest.getId()).map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_IMAGE_REPAIR_TASK_NAME)).build()).iterator();
        RepairImageTemplateHandler handler = new RepairImageTemplateHandler(iterator, cbbImageTemplateMgmtAPI, auditLogAPI, image.getImageName());

        return CommonWebResponse.success(builder.setTaskName(BusinessKey.RCDC_RCO_IMAGE_REPAIR_TASK_NAME)
                .setTaskDesc(BusinessKey.RCDC_RCO_IMAGE_REPAIR_DESC, image.getImageName()).setUniqueId(webRequest.getId()).registerHandler(handler)
                .start());
    }

    private void isExistTempVmRunning(UUID imageId) throws BusinessException {
        uamAppDiskAPI.isExistTempVmRunningThrowEx(imageId);
    }

    private Integer getImageDataDiskSize(CbbImageTemplateDetailDTO imageTemplateDetail) {
        Optional<CbbImageDiskInfoDTO> imageDiskInfo =
                imageTemplateDetail.getImageDiskList().stream().filter(item -> CbbImageDiskType.DATA.equals(item.getImageDiskType())).findFirst();
        return imageDiskInfo.isPresent() ? imageDiskInfo.get().getDiskSize() : 0;
    }

    /**
     * 编辑镜像请求
     *
     * @param request 请求参数，镜像模板ID
     * @param sessionContext 会话
     * @return web response
     * @throws BusinessException ex
     */
    @ApiOperation("编辑版本基本信息")
    @RequestMapping(value = "/editBaseInfo")
    public DefaultWebResponse editVersionBaseInfo(EditVersionBaseInfoWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null!");

        if (!cbbImageTemplateMgmtAPI.existsImageTemplate(request.getId())) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DELETE_NOT_EXIST);
        }

        CbbEditVersionBaseInfoRequest editVersionBaseInfoRequest = new CbbEditVersionBaseInfoRequest();
        BeanUtils.copyProperties(request, editVersionBaseInfoRequest);
        CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getId());
        try {
            cbbImageTemplateMgmtAPI.editVersionBaseInfo(editVersionBaseInfoRequest);
            boolean isImageNameChanged = !Objects.equals(editVersionBaseInfoRequest.getImageName(), cbbImageTemplateDetailDTO.getImageName());
            boolean isImageDescChanged = !Objects.equals(editVersionBaseInfoRequest.getNote(), cbbImageTemplateDetailDTO.getNote());
            if (isImageNameChanged && isImageDescChanged) {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_SUCCESS_LOG,
                        cbbImageTemplateDetailDTO.getRootImageName(), cbbImageTemplateDetailDTO.getImageName());
            } else if (isImageNameChanged) {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_NAME_SUCCESS_LOG,
                        cbbImageTemplateDetailDTO.getRootImageName(), cbbImageTemplateDetailDTO.getImageName());
            } else {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_DESC_SUCCESS_LOG,
                        cbbImageTemplateDetailDTO.getRootImageName(), cbbImageTemplateDetailDTO.getImageName());
            }
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EDIT_VERSION_IMAGETEMPLATE_FAIL_LOG,
                    cbbImageTemplateDetailDTO.getRootImageName(), request.getImageName(), e.getI18nMessage());
            throw e;
        }
    }

    /**
     * 重置windows密码
     *
     * @param resetWindowsPwdWebRequest 重置windows密码请求参数
     * @param builder 批处理
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("重置镜像windows密码")
    @RequestMapping(value = "/resetWinPwd")
    public CommonWebResponse<?> resetWinPwd(ResetWindowsPwdWebRequest resetWindowsPwdWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(resetWindowsPwdWebRequest, "请求参数不能为空");
        Assert.notNull(builder, "builder must not null");
        Assert.notNull(resetWindowsPwdWebRequest.getImageTemplateId(), "imageTemplateId 不能为空");

        String newPwd = "";
        try {
            newPwd = AesUtil.descrypt(resetWindowsPwdWebRequest.getNewPwd(), RedLineUtil.getRealAdminRedLine());
        } catch (Exception e) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DECRYPT_FAIL, e);
        }
        CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(resetWindowsPwdWebRequest.getImageTemplateId());

        final Iterator<DefaultBatchTaskItem> iterator = Stream
                .of(resetWindowsPwdWebRequest.getImageTemplateId()).map(id -> DefaultBatchTaskItem.builder().itemId(id) //
                        .itemName(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_NAME,
                                new String[] {LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE)})
                        .build())
                .iterator();

        ResetImageWindowsPwdHandlerRequest request = new ResetImageWindowsPwdHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        request.setCbbResetWindowsPwdAPI(cbbResetWindowsPwdAPI);
        request.setAuditLogAPI(auditLogAPI);
        request.setImageTemplateId(resetWindowsPwdWebRequest.getImageTemplateId());
        request.setAccount(resetWindowsPwdWebRequest.getAccount());
        request.setPassword(newPwd);
        ResetImageWindowsPwdHandler resetImageWindowsPwdHandler = new ResetImageWindowsPwdHandler(request);
        BatchTaskSubmitResult batchTaskSubmitResult = builder
                .setTaskName(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_NAME,
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE))
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TASK_DESC,
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_RESET_WINDOWS_PWD_TYPE_IMAGE),
                        cbbImageTemplateDTO.getImageTemplateName())
                .setUniqueId(resetWindowsPwdWebRequest.getImageTemplateId()).registerHandler(resetImageWindowsPwdHandler).start();
        return CommonWebResponse.success(batchTaskSubmitResult);
    }


    @ApiOperation("查询镜像类型支持操作系统版本")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "查询镜像类型支持的操作系统版本")})
    @RequestMapping(value = "getImageTypeSupportOsVersionConfig", method = RequestMethod.POST)
    public DefaultWebResponse getImageTypeSupportOsVersionConfig() {
        return DefaultWebResponse.Builder.success(imageTemplateAPI.findAllImageTypeSupportOsVersionConfig());
    }


    /**
     * 应用镜像和桌面镜像的转换
     *
     * @param webRequest 请求参数
     * @param optLogRecorder 日志
     * @param builder 批处理
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用镜像和桌面镜像的转换")
    @RequestMapping(value = "/transferImageUsage", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "transferImageUsageValidate")
    @EnableAuthority
    public CommonWebResponse<?> transferImageUsage(TransferImageUsageWebRequest webRequest, ProgrammaticOptLogRecorder optLogRecorder,
            BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        Assert.notNull(builder, "builder is not null");
        Assert.notNull(optLogRecorder, "optLogRecorder is not null");

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_TASK_NAME));
        TransferImageUsageBatchHandler handler = new TransferImageUsageBatchHandler(batchTaskItem, auditLogAPI, cbbImageTemplateMgmtAPI, webRequest);
        handler.setImageName(webRequest.getImageName());

        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TRANSFER_USAGE_DESC, webRequest.getImageName())
                .setUniqueId(webRequest.getId()).registerHandler(handler).start();

        return CommonWebResponse.success(result);

    }


    /**
     * 需要展示提示的条件 无个性桌面、镜像多版本、非VDI镜像、镜像版本
     *
     * @param dto 镜像dto
     * @return 是否允许展示
     */
    private Boolean needShowTip(CbbImageTemplateDetailDTO dto) {
        return dto.getClouldDeskopNumOfPersonal() == 0 || dto.getEnableMultipleVersion() || CbbImageType.VDI != dto.getCbbImageType()
                || dto.getRootImageId() != null;
    }

    /**
     * @param editErrorMessageList 错误信息
     * @return 是否存在
     */
    private Boolean isExistGTErrorMessage(List<String> editErrorMessageList) {
        return !editErrorMessageList.contains(GT_VERSION_LOW_TIP)
                && !editErrorMessageList.contains(LocaleI18nResolver.resolve(RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_GT_LOW));
    }

    /**
     * @param dto 镜像dto
     * @return 是否存在
     */
    private Boolean checkImageGuestToolVersionIsLatest(CbbImageTemplateDetailDTO dto) {
        return StringUtils.hasText(dto.getGuestToolVersion())
                && !StringUtils.equals(dto.getGuestToolVersion(), guestToolIsoInfoAPI.obtainUpgradeVersionForImage(dto.getId()));
    }
}
