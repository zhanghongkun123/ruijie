package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.strategy.CheckDeskStrategyNameRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterGpuInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetAllUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbDeskStrategyUsage;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementConfigRequestDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rca.module.def.util.CommonStrategyHelper;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.query.DefaultConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskTopAllowLoginTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.AppTestDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.CdRomMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.NetDiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy.DeleteDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy.GetDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy.GetUsbTypeWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy.ValidateDeskStrategyNameWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy.DeskStrategyVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.request.CommonPageQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.response.VgpuVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.common.DesktopStrategyHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.utils.DesktopStrategyUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.validation.DeskStrategyValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktopspec.utils.DeskSpecUtils;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory.RequestBuilder;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.response.PageResponseContent;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.appcenter.module.def.constant.Constants.COMMA_SEPARATION_CHARACTER;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018O
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月22日
 *
 * @author zhfw
 */
@Api(tags = "云桌面策略页面行为")
@Controller
@RequestMapping("/rco/deskStrategy")
@EnableCustomValidate(validateClass = DeskStrategyValidation.class)
public class DeskStrategyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyController.class);

    /**
     * 开启系统盘自动扩容时，设置的系统盘大小。
     */
    private static final int DEFAULT_SYSTEM_SIZE_WHEN_ENABLE_FULL_SYSTEM_DISK = 0;

    private static final String PERMISSION_FILTER = "id";

    private static final String STRATEGY_ID = "strategyId";

    private static final String PERSONAL_DISK = "personalDisk";

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    @Autowired
    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private DeskSnapshotAPI deskSnapshotAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DesktopStrategyHelper desktopStrategyHelper;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    @Autowired
    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    @Autowired
    private UamAppTestAPI uamAppTestAPI;

    @Autowired
    private CbbDeskStrategyCommonAPI cbbDeskStrategyCommonAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    private static ThreadExecutor SCHEDULED_THREAD_POOL =
            ThreadExecutors.newBuilder("delete_delivery_thread").maxThreadNum(5).queueSize(10).build();

    private static final ThreadExecutor RCO_DESK_STRATEGY_CHANGE_NOTIFY_RUNNING_DESK_EXECUTOR =
            ThreadExecutors.newBuilder("rco-desk-strategy-change-notify-running-desk").maxThreadNum(5).queueSize(20).build();

    /**
     * 分页获取云桌面策略信息
     *
     * @param pageQueryRequest 请求参数
     * @param sessionContext 请sessionContext
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页获取云桌面策略信息")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse getPageDeskStrategy(PageQueryRequest pageQueryRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] idArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.DESKTOP_STRATEGY);
            if (ArrayUtils.isEmpty(idArr)) {
                return CommonWebResponse.success(new DefaultPageResponse<>());
            }
            builder.in(PERMISSION_FILTER, idArr);
        }
        builder.eq("strategyUsage", CbbDeskStrategyUsage.DESK);

        PageQueryResponse<DeskStrategyDTO> pageQueryResponse = deskStrategyAPI.pageDeskStrategyQuery(builder.build());
        return CommonWebResponse.success(pageQueryResponse);
    }


    /**
     * 根据特定Id限制查询云桌面策略列表
     *
     * @param pageWebRequest 请求参数
     * @param sessionContext 请sessionContext
     * @return 策略列表
     * @throws BusinessException 异常
     */
    @ApiOperation("根据特定Id限制查询云桌面策略列表")
    @RequestMapping(value = "condition/list", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<DeskStrategyDTO>> getPageDeskStrategyById(CommonPageQueryRequest pageWebRequest,
                                                                                         SessionContext sessionContext) throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder().setPageLimit(pageWebRequest.getPage(), pageWebRequest.getLimit());
        UUID[] deskIdArr = null;
        UUID imageId = null;
        UUID[] desktopPoolIdArr = null;
        String desktopPoolModel = null;
        String sessionType = null;
        UUID userProfileStrategyId = null;
        Integer personalDisk = null;
        Match[] exactMatchArr = pageWebRequest.getMatchArr();
        if (ArrayUtils.isNotEmpty(exactMatchArr)) {
            for (Match exactMatch1 : exactMatchArr) {
                ExactMatch exactMatch = (ExactMatch) exactMatch1;
                if (StringUtils.equals("cbbDesktopId", exactMatch.getFieldName())) {
                    deskIdArr = getUUIDArray(exactMatch.getValueArr());
                    continue;
                }

                if (StringUtils.equals("imageTemplateId", exactMatch.getFieldName())) {
                    Object[] valueArr = exactMatch.getValueArr();
                    if (valueArr != null && valueArr.length > 0) {
                        imageId = UUID.fromString(String.valueOf(exactMatch.getValueArr()[0]));
                    }
                    continue;
                }

                if (StringUtils.equals("desktopPoolId", exactMatch.getFieldName())) {
                    desktopPoolIdArr = getUUIDArray(exactMatch.getValueArr());
                    continue;
                }
                if (StringUtils.equals("desktopPoolModel", exactMatch.getFieldName())) {
                    desktopPoolModel = exactMatch.getValueArr()[0].toString();
                    continue;
                }
                if (StringUtils.equals("sessionType", exactMatch.getFieldName())) {
                    sessionType = exactMatch.getValueArr()[0].toString();
                    requestBuilder.in(exactMatch.getFieldName(), exactMatch.getValueArr());
                    continue;
                }
                if (StringUtils.equals("userProfileStrategyId", exactMatch.getFieldName())) {
                    Object[] valueArr = exactMatch.getValueArr();
                    if (valueArr != null && valueArr.length > 0) {
                        userProfileStrategyId = UUID.fromString(String.valueOf(exactMatch.getValueArr()[0]));
                    }
                    continue;
                }
                if (StringUtils.equals(PERSONAL_DISK, exactMatch.getFieldName())) {
                    personalDisk = Integer.valueOf(exactMatch.getValueArr()[0].toString());
                    continue;
                }
                requestBuilder.in(exactMatch.getFieldName(), exactMatch.getValueArr());
            }
        }
        requestBuilder.neq("strategyUsage", CbbDeskStrategyUsage.APP);
        desktopStrategyHelper.buildPageQueryRequestSort(pageWebRequest.getSortArr(), requestBuilder);

        if (!Objects.equals(Boolean.TRUE, pageWebRequest.getNoPermission()) && !permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] idArr = permissionHelper.getPermissionIdArr(sessionContext, AdminDataPermissionType.DESKTOP_STRATEGY);
            if (ArrayUtils.isEmpty(idArr)) {
                return CommonWebResponse.success(new PageQueryResponse<>());
            }
            requestBuilder.in(PERMISSION_FILTER, idArr);
        }

        PageQueryResponse<DeskStrategyDTO> pageQueryResponse = deskStrategyAPI.pageDeskStrategyQuery(requestBuilder.build());
        if (imageId != null) {
            getPageDeskStrategyByImageId(pageQueryResponse, imageId);
        }
        if (deskIdArr != null && deskIdArr.length > 0) {
            getPageDeskStrategyByDeskId(pageQueryResponse, deskIdArr);
        }
        if (sessionType == null) {
            sessionType = CbbDesktopSessionType.SINGLE.name();
        }
        if (userProfileStrategyId != null && !Objects.equals(sessionType, CbbDesktopSessionType.MULTIPLE.name())) {
            getPageDeskStrategyByUserProfileStrategyId(pageQueryResponse, userProfileStrategyId);
        }
        desktopStrategyHelper.checkDeskStrategyCanUserByDesktopPool(pageQueryResponse, desktopPoolIdArr,
                desktopPoolModel, sessionType);
        desktopStrategyHelper.checkDeskStrategyCanUseByPersonalDisk(pageQueryResponse, personalDisk);
        return CommonWebResponse.success(pageQueryResponse);
    }

    private UUID[] getUUIDArray(Object[] valueArr) {
        List<UUID> uuidList = new ArrayList<>();
        if (valueArr != null) {
            for (Object o : valueArr) {
                uuidList.add(UUID.fromString(String.valueOf(o)));
            }
            return uuidList.toArray(new UUID[0]);
        }
        // 无条件就返回空
        return null;
    }

    private void getPageDeskStrategyByDeskId(PageQueryResponse<DeskStrategyDTO> pageQueryResponse, UUID[] deskIdArr) {
        Arrays.stream(deskIdArr).forEach(deskId -> {
            Arrays.stream(pageQueryResponse.getItemArr()).forEach(dto -> {
                if (!dto.getCanUsed()) {
                    return;
                }
                String canUsedMessage;
                try {
                    if (Objects.equals(CbbStrategyType.VDI.name(), dto.getStrategyType())) {
                        canUsedMessage = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByDeskId(deskId, dto.getId());
                    } else if (Objects.equals(CbbStrategyType.IDV.name(), dto.getStrategyType())) {
                        canUsedMessage = getIDVCanUseMessage(deskId, dto);
                    } else if (Objects.equals(CbbStrategyType.VOI.name(), dto.getStrategyType())) {
                        canUsedMessage = getVOICanUseMessage(deskId, dto);
                    } else if (Objects.equals(CbbStrategyType.THIRD.name(), dto.getStrategyType())) {
                        canUsedMessage = cbbThirdPartyDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByDeskId(deskId, dto.getId());
                    } else {
                        throw new BusinessException(DeskStrategyBusinessKey.RCDC_RCO_STRATEGY_TYPE_NOT_MATCH, dto.getStrategyType().toString());
                    }
                } catch (BusinessException e) {
                    LOGGER.error("根据云桌面Id限制查询云桌面策略列表异常", e);
                    canUsedMessage = LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_QUERY_EXCEPTIONS);
                }
                if (!canUsedMessage.isEmpty()) {
                    dto.setCanUsed(false);
                }
                dto.setCanUsedMessage(canUsedMessage);
            });
        });
    }

    private String getVOICanUseMessage(UUID deskId, DeskStrategyDTO dto) throws BusinessException {
        Boolean isDeskEnableFullSystemDisk = userDesktopMgmtAPI.getDeskEnableFullSystemDiskByDeskId(deskId);
        // 开启系统盘自动扩容的桌面，不允许使用未开启系统盘自动扩容的策略
        if (Boolean.TRUE.equals(isDeskEnableFullSystemDisk)
                && !Boolean.TRUE.equals(dto.getEnableFullSystemDisk())) {
            return LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCDC_RCO_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_NOT_FULL_STRATEGY_VOI);
        }
        // 没有开启系统盘自动扩容的桌面，不允许使用开启系统盘自动扩容的策略
        if (!Boolean.TRUE.equals(isDeskEnableFullSystemDisk)
                && Boolean.TRUE.equals(dto.getEnableFullSystemDisk())) {
            return LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCDC_RCO_NOT_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_FULL_STRATEGY_VOI);
        }

        return cbbVOIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByDeskId(deskId, dto.getId());
    }

    private String getIDVCanUseMessage(UUID deskId, DeskStrategyDTO dto) throws BusinessException {
        Boolean isDeskEnableFullSystemDisk = userDesktopMgmtAPI.getDeskEnableFullSystemDiskByDeskId(deskId);
        // 开启系统盘自动扩容的桌面，不允许使用未开启系统盘自动扩容的策略
        if (Boolean.TRUE.equals(isDeskEnableFullSystemDisk)
                && !Boolean.TRUE.equals(dto.getEnableFullSystemDisk())) {
            return LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCDC_RCO_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_NOT_FULL_STRATEGY_IDV);
        }
        // 没有开启系统盘自动扩容的桌面，不允许使用开启系统盘自动扩容的策略
        if (!Boolean.TRUE.equals(isDeskEnableFullSystemDisk)
                && Boolean.TRUE.equals(dto.getEnableFullSystemDisk())) {
            return LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCDC_RCO_NOT_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_FULL_STRATEGY_IDV);
        }

        return cbbIDVDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByDeskId(deskId, dto.getId());
    }

    private void getPageDeskStrategyByImageId(PageQueryResponse<DeskStrategyDTO> pageQueryResponse, UUID imageId) {
        Arrays.stream(pageQueryResponse.getItemArr()).forEach(dto -> {
            String canUsedMessage;
            try {
                if (Objects.equals(CbbStrategyType.VDI.name(), dto.getStrategyType())) {
                    canUsedMessage = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByImageId(imageId, dto.getId());
                } else if (Objects.equals(CbbStrategyType.IDV.name(), dto.getStrategyType())) {
                    canUsedMessage = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByImageId(imageId, dto.getId());
                } else {
                    canUsedMessage = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByImageId(imageId, dto.getId());
                }
            } catch (BusinessException e) {
                LOGGER.error("根据云桌面Id限制查询云桌面策略列表异常", e);
                canUsedMessage = LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_QUERY_EXCEPTIONS);
            }
            if (!canUsedMessage.isEmpty()) {
                dto.setCanUsed(false);
            }
            dto.setCanUsedMessage(canUsedMessage);
        });
    }

    private void getPageDeskStrategyByUserProfileStrategyId(PageQueryResponse<DeskStrategyDTO> pageQueryResponse, UUID userProfileStrategyId) {
        Arrays.stream(pageQueryResponse.getItemArr()).forEach(dto -> {
            String canUsedMessage;
            try {
                // 不需要区分桌面类型
                canUsedMessage = deskStrategyAPI.getDeskStrategyUsedMessageByUserProfileStrategyId(dto.getId());
            } catch (BusinessException e) {
                LOGGER.error("根据用户配置策略Id:[{}]限制查询云桌面策略列表异常", userProfileStrategyId, e);
                canUsedMessage = LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_QUERY_EXCEPTIONS);
            }
            if (!canUsedMessage.isEmpty()) {
                dto.setCanUsed(false);
                dto.setCanUsedMessage(canUsedMessage);
            }
        });
    }

    /**
     * 创建VDI云桌面策略
     *
     * @param webRequest     请求参数
     * @param sessionContext session上下文
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建VDI云桌面策略")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "create/vdi", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "createVDIDeskStrategyValidate")
    @EnableAuthority
    public CommonWebResponse createVDIDeskStrategy(CreateVDIDeskStrategyWebRequest webRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        try {
            // 获取登录用户信息
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            CbbCreateDeskStrategyVDIDTO vdiRequest = buildCbbCreateDeskStrategyVDIRequest(webRequest, baseAdminDTO);

            UUID deskStrategyId = cbbVDIDeskStrategyMgmtAPI.createDeskStrategyVDI(vdiRequest);
            cloudDeskComputerNameConfigAPI.createCloudDeskComputerNameConfig(webRequest.getComputerName(), deskStrategyId);
            // 策略创建通知
            rccmManageAPI.createNotify(deskStrategyId);
            // 添加数据权限
            permissionHelper.saveAdminGroupPermission(sessionContext, deskStrategyId, AdminDataPermissionType.DESKTOP_STRATEGY);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_SUCCESS_LOG, webRequest.getStrategyName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_FAIL_LOG, webRequest.getStrategyName(),
                    e.getI18nMessage());
            return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }


    /**
     * 创建IDV云桌面策略
     *
     * @param webRequest     请求参数
     * @param sessionContext session上下文
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建IDV云桌面策略")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "create/idv", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "createIDVDeskStrategyValidate")
    @EnableAuthority
    public CommonWebResponse createIDVDeskStrategy(CreateIDVDeskStrategyWebRequest webRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        try {
            // 获取登录用户信息
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            CbbDeskStrategyIDVDTO idvRequest = buildCbbDeskStrategyIDVDTO(webRequest, baseAdminDTO);
            cbbIDVDeskStrategyMgmtAPI.createDeskStrategyIDV(idvRequest);
            // 建议cbb改接口
            CbbDeskStrategyIDVDTO cbbDeskStrategyIDVDTO = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDVByName(webRequest.getStrategyName());
            cloudDeskComputerNameConfigAPI.createCloudDeskComputerNameConfig(webRequest.getComputerName(), cbbDeskStrategyIDVDTO.getId());
            // 策略创建通知
            rccmManageAPI.createNotify(cbbDeskStrategyIDVDTO.getId());
            // 添加数据权限
            permissionHelper.saveAdminGroupPermission(sessionContext, cbbDeskStrategyIDVDTO.getId(), AdminDataPermissionType.DESKTOP_STRATEGY);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_SUCCESS_LOG, webRequest.getStrategyName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_FAIL_LOG, webRequest.getStrategyName(),
                    e.getI18nMessage());
            return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }

    /**
     * 创建VDI云桌面策略
     *
     * @param webRequest     请求参数
     * @param sessionContext session上下文
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建VOI云桌面策略")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "create/voi", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "createVOIDeskStrategyValidate")
    @EnableAuthority
    public CommonWebResponse createVOIDeskStrategy(CreateVOIDeskStrategyWebRequest webRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        try {
            // VOI云桌面类型校验
            validateCloudDeskPattern(webRequest.getDesktopType(), CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_VOI_PATTERN_ERR);
            // 获取登录用户信息
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            CbbDeskStrategyVOIDTO voiRequest = buildCbbDeskStrategyVOIDTO(webRequest, baseAdminDTO);
            // VOI云桌面类型校验
            validateCloudDeskPattern(webRequest.getDesktopType(), CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_VOI_PATTERN_ERR);
            cbbVOIDeskStrategyMgmtAPI.createDeskStrategyVOI(voiRequest);

            CbbDeskStrategyVOIDTO cbbDeskStrategyVOIDTO = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOIByName(webRequest.getStrategyName());
            cloudDeskComputerNameConfigAPI.createCloudDeskComputerNameConfig(webRequest.getComputerName(), cbbDeskStrategyVOIDTO.getId());
            // 策略创建通知
            rccmManageAPI.createNotify(cbbDeskStrategyVOIDTO.getId());
            // 添加数据权限
            permissionHelper.saveAdminGroupPermission(sessionContext, cbbDeskStrategyVOIDTO.getId(), AdminDataPermissionType.DESKTOP_STRATEGY);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_SUCCESS_LOG, webRequest.getStrategyName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_FAIL_LOG, webRequest.getStrategyName(),
                    e.getI18nMessage());
            return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }

    /**
     * 创建第三方云桌面策略
     * @param webRequest 请求参数
     * @param sessionContext session上下文
     * @return 返回值
     */
    @ApiOperation("创建第三方云桌面策略")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "create/third", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "createThirdPartyDeskStrategyValidate")
    @EnableAuthority
    public CommonWebResponse createThirdPartyDeskStrategy(CreateThirdPartyDeskStrategyWebRequest webRequest, SessionContext sessionContext) {
        Assert.notNull(webRequest, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        try {
            // 获取登录用户信息
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            CbbDeskStrategyThirdPartyDTO thirdPartyDTO = buildCbbDeskStrategyThirdPartyDTO(webRequest, baseAdminDTO);
            UUID deskStrategyThirdPartyId = cbbThirdPartyDeskStrategyMgmtAPI.createDeskStrategyThirdParty(thirdPartyDTO);
            // 策略创建通知
            rccmManageAPI.createNotify(deskStrategyThirdPartyId);
            // 添加数据权限
            permissionHelper.saveAdminGroupPermission(sessionContext, deskStrategyThirdPartyId, AdminDataPermissionType.DESKTOP_STRATEGY);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_SUCCESS_LOG, webRequest.getStrategyName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_FAIL_LOG, webRequest.getStrategyName(),
                    e.getI18nMessage());
            return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }

    private CbbDeskStrategyThirdPartyDTO buildCbbDeskStrategyThirdPartyDTO(CreateThirdPartyDeskStrategyWebRequest webRequest,
                                                                           IacAdminDTO baseAdminDTO) throws BusinessException {
        CbbDeskStrategyThirdPartyDTO cbbDeskStrategyThirdPartyDTO = new CbbDeskStrategyThirdPartyDTO();
        BeanUtils.copyProperties(webRequest, cbbDeskStrategyThirdPartyDTO);
        cbbDeskStrategyThirdPartyDTO.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        cbbDeskStrategyThirdPartyDTO.setEnableUsbReadOnly(webRequest.getEnableUsbReadOnly());
        cbbDeskStrategyThirdPartyDTO.setForbidCatchScreen(webRequest.getEnableForbidCatchScreen());
        cbbDeskStrategyThirdPartyDTO.setOpenDoubleScreen(webRequest.getEnableDoubleScreen());
        cbbDeskStrategyThirdPartyDTO.setEnableWebClient(false);
        // 剪切板
        cbbDeskStrategyThirdPartyDTO.setClipBoardMode(webRequest.getClipBoardMode());
        cbbDeskStrategyThirdPartyDTO.setClipBoardSupportTypeArr(webRequest.getClipBoardSupportTypeArr());
        // 本地磁盘映射
        DiskMappingEnum diskMappingType = webRequest.getDiskMappingType();
        cbbDeskStrategyThirdPartyDTO.setEnableDiskMapping(diskMappingType.getEnableDiskMapping());
        cbbDeskStrategyThirdPartyDTO.setEnableDiskMappingWriteable(diskMappingType.getEnableDiskMappingWriteable());
        // 安全访问网关
        cbbDeskStrategyThirdPartyDTO.setEnableAgreementAgency(webRequest.getEnableAgreementAgency());
        cbbDeskStrategyThirdPartyDTO.setEnableForceUseAgreementAgency(webRequest.getEnableForceUseAgreementAgency());
        // ip访问规则
        cbbDeskStrategyThirdPartyDTO.setIpLimitMode(webRequest.getIpLimitMode());
        if (CbbIpLimitModeEnum.NOT_USE != webRequest.getIpLimitMode()) {
            cbbDeskStrategyThirdPartyDTO.setIpSegmentDTOList(webRequest.getIpSegmentDTOList());
        }
        // 创建者登录账号
        cbbDeskStrategyThirdPartyDTO.setCreateUserName(baseAdminDTO.getUserName());
        // 水印配置
        DesktopStrategyUtils.dealWatermarkInfo(webRequest.getWatermarkInfo(), cbbDeskStrategyThirdPartyDTO::setEnableWatermark,
                cbbDeskStrategyThirdPartyDTO::setWatermarkInfo);
        // 描述信息
        cbbDeskStrategyThirdPartyDTO.setNote(webRequest.getNote());
        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(webRequest.getRemark())) {
            cbbDeskStrategyThirdPartyDTO.setRemark(null);
        } else {
            // 设置云桌面标签备注
            cbbDeskStrategyThirdPartyDTO.setRemark(webRequest.getRemark());
        }
        // 电源计划，默认为睡眠，并且是关闭状态
        cbbDeskStrategyThirdPartyDTO.setPowerPlan(Optional.ofNullable(webRequest.getPowerPlan()).orElse(CbbPowerPlanEnum.SLEEP));
        cbbDeskStrategyThirdPartyDTO.setPowerPlanTime(Optional.ofNullable(webRequest.getPowerPlanTime()).orElse(0));
        cbbDeskStrategyThirdPartyDTO.setEstIdleOverTime(Optional.ofNullable(webRequest.getEstIdleOverTime()).orElse(0));

        // 网盘映射
        NetDiskMappingEnum netDiskMappingType = webRequest.getNetDiskMappingType();
        if (netDiskMappingType != null) {
            cbbDeskStrategyThirdPartyDTO.setEnableNetDiskMapping(netDiskMappingType.getEnableNetDiskMapping());
            cbbDeskStrategyThirdPartyDTO.setEnableNetDiskMappingWriteable(netDiskMappingType.getEnableNetDiskMappingWriteable());
        }

        // CDROM映射
        CdRomMappingEnum cdRomMappingType = webRequest.getCdRomMappingType();
        if (cdRomMappingType != null) {
            cbbDeskStrategyThirdPartyDTO.setEnableCDRomMapping(cdRomMappingType.getEnableCdRomMapping());
            cbbDeskStrategyThirdPartyDTO.setEnableCDRomMappingWriteable(cdRomMappingType.getEnableCDRomMappingWriteable());
        }
        // 验证允许桌面登录时间
        verifyDesktopAllowLoginTime(webRequest, cbbDeskStrategyThirdPartyDTO);
        cbbDeskStrategyThirdPartyDTO.setIpLimitMode(Objects.isNull(webRequest.getIpLimitMode()) ?
                CbbIpLimitModeEnum.NOT_USE : webRequest.getIpLimitMode());
        // 多会话不支持文件审计 && 多会话桌面权限配置
        if (CbbDesktopSessionType.MULTIPLE == webRequest.getSessionType()) {
            cbbDeskStrategyThirdPartyDTO.setEnableAuditFile(Boolean.FALSE);
            cbbDeskStrategyThirdPartyDTO.setDesktopSyncLoginAccount(Boolean.TRUE);
            cbbDeskStrategyThirdPartyDTO.setDesktopSyncLoginAccountPermission(webRequest.getWindowsAccountPermission());
        } else {
            boolean enableAuditFile = Boolean.TRUE.equals(webRequest.getEnableAuditFile());
            cbbDeskStrategyThirdPartyDTO.setEnableAuditFile(enableAuditFile);
            EditAuditFileWebRequest auditFileInfo = webRequest.getAuditFileInfo();
            if (enableAuditFile && Objects.nonNull(auditFileInfo)) {
                CbbAuditFileConfigDTO cbbAuditFileConfigDTO = new CbbAuditFileConfigDTO();
                BeanUtils.copyProperties(auditFileInfo, cbbAuditFileConfigDTO);
                cbbDeskStrategyThirdPartyDTO.setAuditFileInfo(cbbAuditFileConfigDTO);
            }
        }
        // 连接协议
        cbbDeskStrategyThirdPartyDTO.setEstProtocolType(webRequest.getEstProtocolType());
        // 协议配置
        cbbDeskStrategyThirdPartyDTO.setAgreementInfo(CommonStrategyHelper.convertAgreementConfig(
                webRequest.getEstProtocolType(), webRequest.getAgreementInfo()));
        cbbDeskStrategyThirdPartyDTO.setStrategyUsage(CbbDeskStrategyUsage.DESK);
        // USB带宽控制与压缩加速
        cbbDeskStrategyThirdPartyDTO.setUsbStorageDeviceAcceleration(webRequest.getUsbStorageDeviceAcceleration());
        cbbDeskStrategyThirdPartyDTO.setEnableUsbCompressAcceleration(webRequest.getEnableUsbCompressAcceleration());
        cbbDeskStrategyThirdPartyDTO.setEnableUsbBandwidth(webRequest.getEnableUsbBandwidth());
        cbbDeskStrategyThirdPartyDTO.setUsbBandwidthInfo(webRequest.getUsbBandwidthInfo());
        return cbbDeskStrategyThirdPartyDTO;
    }

    private void validateCloudDeskPattern(CbbCloudDeskPattern desktopType, String businessKey) throws BusinessException {
        if (desktopType == CbbCloudDeskPattern.PERSONAL || desktopType == CbbCloudDeskPattern.RECOVERABLE) {
            return;
        }
        throw new BusinessException(businessKey, desktopType.name());
    }

    private CbbCreateDeskStrategyVDIDTO buildCbbCreateDeskStrategyVDIRequest(CreateVDIDeskStrategyWebRequest webRequest, IacAdminDTO baseAdminDTO)
            throws BusinessException {

        CbbCreateDeskStrategyVDIDTO request = new CbbCreateDeskStrategyVDIDTO();
        request.setName(webRequest.getStrategyName());
        request.setAllowLocalDisk(false);
        request.setPattern(webRequest.getDesktopType());
        // vdi设备默认音频开启
        UUID[] usbTypeArr = DesktopStrategyUtils.buildVDIUsbTypeArr(webRequest.getUsbTypeIdArr());
        request.setUsbTypeIdArr(usbTypeArr);
        request.setSessionTypeEnum(webRequest.getSessionType());
        request.setIsOpenInternet(webRequest.getEnableInternet());
        request.setIsOpenUsbReadOnly(webRequest.getEnableUsbReadOnly());
        request.setIsOpenDoubleScreen(webRequest.getEnableDoubleScreen());
        request.setEnableSharedPrinting(webRequest.getEnableSharedPrinting());
        request.setForbidCatchScreen(webRequest.getEnableForbidCatchScreen() != null && webRequest.getEnableForbidCatchScreen());
        request.setEnableWebClient(webRequest.getEnableWebClient() == null || webRequest.getEnableWebClient());
        request.setClipBoardMode(webRequest.getClipBoardMode());
        request.setClipBoardSupportTypeArr(webRequest.getClipBoardSupportTypeArr());
        request.setIsOpenDesktopRedirect(Boolean.TRUE.equals(webRequest.getEnableOpenDesktopRedirect()));
        request.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        request.setDeskPersonalConfigStrategyType(getCbbDeskPersonalConfigStrategyType(webRequest.getDesktopType()));
        request.setEnableNested(webRequest.getEnableNested() == null ? false : webRequest.getEnableNested());
        // 创建者登录账号
        request.setCreatorUserName(baseAdminDTO.getUserName());
        request.setAdOu(webRequest.getAdOu());
        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(webRequest.getRemark())) {
            request.setRemark(null);
        } else {
            // 设置云桌面标签备注
            request.setRemark(webRequest.getRemark());
        }
        // 磁盘映射
        DiskMappingEnum diskMappingType = webRequest.getDiskMappingType();
        request.setEnableDiskMapping(diskMappingType.getEnableDiskMapping());
        request.setEnableDiskMappingWriteable(diskMappingType.getEnableDiskMappingWriteable());
        // 局域网自动检测
        request.setEnableLanAutoDetection(webRequest.getEnableLanAutoDetection());

        // 请求参数中，桌面创建方式为空，则默认为链接克隆方式
        if (Objects.isNull(webRequest.getDeskCreateMode())) {
            request.setDeskCreateMode(DeskCreateMode.LINK_CLONE);
        } else {
            validateVDIStrategyCreateMode(webRequest);
            request.setDeskCreateMode(webRequest.getDeskCreateMode());
        }

        // 电源计划，默认为睡眠，并且是关闭状态
        request.setPowerPlan(Optional.ofNullable(webRequest.getPowerPlan()).orElse(CbbPowerPlanEnum.SLEEP));
        request.setPowerPlanTime(Optional.ofNullable(webRequest.getPowerPlanTime()).orElse(0));
        request.setEstIdleOverTime(Optional.ofNullable(webRequest.getEstIdleOverTime()).orElse(0));

        request.setKeyboardEmulationType(Optional.ofNullable(webRequest.getKeyboardEmulationType()).orElse(CbbKeyboardEmulationType.PS2));

        // 网盘映射
        NetDiskMappingEnum netDiskMappingType = webRequest.getNetDiskMappingType();
        if (netDiskMappingType != null) {
            request.setEnableNetDiskMapping(netDiskMappingType.getEnableNetDiskMapping());
            request.setEnableNetDiskMappingWriteable(netDiskMappingType.getEnableNetDiskMappingWriteable());
        }

        // CDROM映射
        CdRomMappingEnum cdRomMappingType = webRequest.getCdRomMappingType();
        if (cdRomMappingType != null) {
            request.setEnableCDRomMapping(cdRomMappingType.getEnableCdRomMapping());
            request.setEnableCDRomMappingWriteable(cdRomMappingType.getEnableCDRomMappingWriteable());
        }
        // 验证允许桌面登录时间
        verifyDesktopAllowLoginTime(webRequest, request);

        //用户自助快照数量,默认为0
        if (!Objects.isNull(webRequest.getEnableUserSnapshot())) {
            request.setEnableUserSnapshot(webRequest.getEnableUserSnapshot());
        } else {
            request.setEnableUserSnapshot(false);
        }

        request.setDesktopOccupyDriveArr(webRequest.getDesktopOccupyDriveArr());
        request.setIpLimitMode(Objects.isNull(webRequest.getIpLimitMode()) ? CbbIpLimitModeEnum.NOT_USE : webRequest.getIpLimitMode());
        request.setIpSegmentDTOList(webRequest.getIpSegmentDTOList());

        // 水印配置
        DesktopStrategyUtils.dealWatermarkInfo(webRequest.getWatermarkInfo(), request::setEnableWatermark, request::setWatermarkInfo);

        request.setNote(webRequest.getNote());
        // 安全审计
        convert2AuditConfig(webRequest, request);
        // USB存储设备映射模式
        request.setUsbStorageDeviceMappingMode(webRequest.getUsbStorageDeviceMappingMode());

        // 安全配置-桌面账号权限
        if (CbbDesktopSessionType.SINGLE == webRequest.getSessionType()) {
            request.setDesktopSyncLoginAccount(webRequest.getDesktopSyncLoginAccount());
            request.setDesktopSyncLoginAccountPermission(webRequest.getDesktopSyncLoginAccountPermission());
            request.setDesktopSyncLoginPassword(webRequest.getDesktopSyncLoginPassword());
        } else {
            request.setDesktopSyncLoginAccount(Boolean.TRUE);
            request.setDesktopSyncLoginAccountPermission(webRequest.getWindowsAccountPermission());
        }

        // vdi桌面高可用
        request.setEnableHa(webRequest.getEnableHa());
        request.setHaPriority(webRequest.getHaPriority());
        request.setUsbStorageDeviceAcceleration(webRequest.getUsbStorageDeviceAcceleration());
        request.setEstProtocolType(webRequest.getEstProtocolType());
        request.setAgreementInfo(CommonStrategyHelper.convertAgreementConfig(webRequest.getEstProtocolType(), webRequest.getAgreementInfo()));

        // 串并口重定向
        request.setEnableSerialPortRedirect(webRequest.getEnableSerialPortRedirect());
        request.setEnableParallelPortRedirect(webRequest.getEnableParallelPortRedirect());
        // Usb压缩加速
        request.setEnableUsbCompressAcceleration(webRequest.getEnableUsbCompressAcceleration());
        // Usb带宽控制
        request.setEnableUsbBandwidth(webRequest.getEnableUsbBandwidth());
        request.setUsbBandwidthInfo(webRequest.getUsbBandwidthInfo());

        request.setEnableAgreementAgency(webRequest.getEnableAgreementAgency());
        request.setEnableForceUseAgreementAgency(webRequest.getEnableForceUseAgreementAgency());


        request.setEnableTransparentEncrypt(Objects.nonNull(webRequest.getEnableTransparentEncrypt()) && webRequest.getEnableTransparentEncrypt());
        if (Boolean.TRUE.equals(webRequest.getEnableTransparentEncrypt())) {
            CbbTransparentEncryptDTO encryptInfo = webRequest.getTransparentEncryptInfo();
            if (Objects.nonNull(encryptInfo)) {
                encryptInfo.setCustomAddressVersion(System.currentTimeMillis());
            }
            request.setTransparentEncryptInfo(encryptInfo);
        }

        request.setStrategyUsage(CbbDeskStrategyUsage.DESK);
        return request;
    }

    private void convert2AuditConfig(CreateVDIDeskStrategyWebRequest webRequest, CbbCreateDeskStrategyVDIDTO request) {
        // 多会话不支持文件审批
        if (CbbDesktopSessionType.MULTIPLE == webRequest.getSessionType()) {
            request.setEnableAuditFile(false);
        } else {
            boolean enableAuditFile = Boolean.TRUE.equals(webRequest.getEnableAuditFile());
            request.setEnableAuditFile(enableAuditFile);
            EditAuditFileWebRequest auditFileInfo = webRequest.getAuditFileInfo();
            if (enableAuditFile && Objects.nonNull(auditFileInfo)) {
                CbbAuditFileConfigDTO cbbAuditFileConfigDTO = new CbbAuditFileConfigDTO();
                BeanUtils.copyProperties(auditFileInfo, cbbAuditFileConfigDTO);
                request.setAuditFileInfo(cbbAuditFileConfigDTO);
            }
        }

        boolean enableAuditPrinter = Boolean.TRUE.equals(webRequest.getEnableAuditPrinter());
        request.setEnableAuditPrinter(enableAuditPrinter);
        EditAuditPrinterWebRequest auditPrinterInfo = webRequest.getAuditPrinterInfo();
        if (enableAuditPrinter && Objects.nonNull(auditPrinterInfo)) {
            CbbAuditPrinterConfigDTO cbbAuditPrinterConfigDTO = new CbbAuditPrinterConfigDTO();
            BeanUtils.copyProperties(auditPrinterInfo, cbbAuditPrinterConfigDTO);
            request.setAuditPrinterInfo(cbbAuditPrinterConfigDTO);
        }
    }

    private void verifyDesktopAllowLoginTime(CreateVDIDeskStrategyWebRequest webRequest,
                                             CbbCreateDeskStrategyVDIDTO request) throws BusinessException {
        if (webRequest.getEnableOpenLoginLimit() != null && webRequest.getEnableOpenLoginLimit() &&
                webRequest.getDesktopAllowLoginTimeArr() != null) {
            DesktopStrategyUtils.verifyDesktopAllowLoginTime(webRequest.getDesktopAllowLoginTimeArr());

            CbbDeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr = Arrays.stream(webRequest.getDesktopAllowLoginTimeArr()).map(s ->
                    new CbbDeskTopAllowLoginTimeDTO(s.getStartTime(), s.getEndTime(), s.getWeekArr())).toArray(CbbDeskTopAllowLoginTimeDTO[]::new);
            request.setDesktopAllowLoginTimeArr(desktopAllowLoginTimeArr);
        }
    }

    private void verifyDesktopAllowLoginTime(CreateThirdPartyDeskStrategyWebRequest webRequest,
                                             CbbDeskStrategyThirdPartyDTO request) throws BusinessException {
        if (webRequest.getEnableOpenLoginLimit() != null && webRequest.getEnableOpenLoginLimit() &&
                webRequest.getDesktopAllowLoginTimeArr() != null) {
            DesktopStrategyUtils.verifyDesktopAllowLoginTime(webRequest.getDesktopAllowLoginTimeArr());
            CbbDeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr = Arrays.stream(webRequest.getDesktopAllowLoginTimeArr()).map(s ->
                    new CbbDeskTopAllowLoginTimeDTO(s.getStartTime(), s.getEndTime(), s.getWeekArr())).toArray(CbbDeskTopAllowLoginTimeDTO[]::new);
            request.setDesktopAllowLoginTimeArr(desktopAllowLoginTimeArr);
        }
    }

    private CbbDeskStrategyIDVDTO buildCbbDeskStrategyIDVDTO(CreateIDVDeskStrategyWebRequest webRequest, IacAdminDTO baseAdminDTO)
            throws BusinessException {

        CbbDeskStrategyIDVDTO cbbDeskStrategyIDVDTO = new CbbDeskStrategyIDVDTO();
        BeanUtils.copyProperties(webRequest, cbbDeskStrategyIDVDTO);
        cbbDeskStrategyIDVDTO.setName(webRequest.getStrategyName());
        cbbDeskStrategyIDVDTO.setPattern(webRequest.getDesktopType());
        cbbDeskStrategyIDVDTO.setOpenUsbReadOnly(webRequest.getEnableUsbReadOnly());
        cbbDeskStrategyIDVDTO.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        cbbDeskStrategyIDVDTO.setDeskPersonalConfigStrategyType(getCbbDeskPersonalConfigStrategyType(webRequest.getDesktopType()));
        cbbDeskStrategyIDVDTO.setEnableNested(BooleanUtils.isTrue(webRequest.getEnableNested()));
        cbbDeskStrategyIDVDTO.setKeyboardEmulationType(webRequest.getKeyboardEmulationType());
        if (Boolean.TRUE.equals(webRequest.getEnableFullSystemDisk())) {
            cbbDeskStrategyIDVDTO.setEnableFullSystemDisk(webRequest.getEnableFullSystemDisk());
            cbbDeskStrategyIDVDTO.setSystemSize(DEFAULT_SYSTEM_SIZE_WHEN_ENABLE_FULL_SYSTEM_DISK);
            cbbDeskStrategyIDVDTO.setAllowLocalDisk(Boolean.FALSE);
            cbbDeskStrategyIDVDTO.setOpenDesktopRedirect(Boolean.FALSE);
        } else {
            cbbDeskStrategyIDVDTO.setEnableFullSystemDisk(Boolean.FALSE);
            cbbDeskStrategyIDVDTO.setAllowLocalDisk(webRequest.getEnableAllowLocalDisk());
            cbbDeskStrategyIDVDTO.setSystemSize(webRequest.getSystemDisk());
            cbbDeskStrategyIDVDTO.setOpenDesktopRedirect(webRequest.getEnableOpenDesktopRedirect());
        }
        // 创建者登录账号
        cbbDeskStrategyIDVDTO.setCreatorUserName(baseAdminDTO.getUserName());
        cbbDeskStrategyIDVDTO.setEnableNested(webRequest.getEnableNested() == null ? false : webRequest.getEnableNested());

        DesktopStrategyUtils.dealWatermarkInfo(webRequest.getWatermarkInfo(), cbbDeskStrategyIDVDTO::setEnableWatermark,
                cbbDeskStrategyIDVDTO::setWatermarkInfo);
        cbbDeskStrategyIDVDTO.setNote(webRequest.getNote());

        // 桌面登录同步
        cbbDeskStrategyIDVDTO.setDesktopSyncLoginAccount(webRequest.getDesktopSyncLoginAccount());
        cbbDeskStrategyIDVDTO.setDesktopSyncLoginAccountPermission(webRequest.getDesktopSyncLoginAccountPermission());
        cbbDeskStrategyIDVDTO.setDesktopSyncLoginPassword(webRequest.getDesktopSyncLoginPassword());
        cbbDeskStrategyIDVDTO.setUsbStorageDeviceAcceleration(webRequest.getUsbStorageDeviceAcceleration());
        return cbbDeskStrategyIDVDTO;
    }

    private CbbDeskStrategyVOIDTO buildCbbDeskStrategyVOIDTO(CreateVOIDeskStrategyWebRequest webRequest, IacAdminDTO baseAdminDTO)
            throws BusinessException {

        CbbDeskStrategyVOIDTO cbbDeskStrategyVOIDTO = new CbbDeskStrategyVOIDTO();
        BeanUtils.copyProperties(webRequest, cbbDeskStrategyVOIDTO);
        cbbDeskStrategyVOIDTO.setName(webRequest.getStrategyName());
        cbbDeskStrategyVOIDTO.setPattern(webRequest.getDesktopType());
        // VOI 不支持 USB 存储设备只读
        cbbDeskStrategyVOIDTO.setOpenUsbReadOnly(false);
        cbbDeskStrategyVOIDTO.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        cbbDeskStrategyVOIDTO.setDeskPersonalConfigStrategyType(getCbbDeskPersonalConfigStrategyType(webRequest.getDesktopType()));
        // 创建者登录账号
        cbbDeskStrategyVOIDTO.setCreatorUserName(baseAdminDTO.getUserName());
        if (Boolean.TRUE.equals(webRequest.getEnableFullSystemDisk())) {
            cbbDeskStrategyVOIDTO.setEnableFullSystemDisk(webRequest.getEnableFullSystemDisk());
            cbbDeskStrategyVOIDTO.setSystemSize(DEFAULT_SYSTEM_SIZE_WHEN_ENABLE_FULL_SYSTEM_DISK);
            cbbDeskStrategyVOIDTO.setAllowLocalDisk(Boolean.FALSE);
            cbbDeskStrategyVOIDTO.setOpenDesktopRedirect(Boolean.FALSE);
        } else {
            cbbDeskStrategyVOIDTO.setAllowLocalDisk(webRequest.getEnableAllowLocalDisk());
            cbbDeskStrategyVOIDTO.setSystemSize(webRequest.getSystemDisk());
            cbbDeskStrategyVOIDTO.setOpenDesktopRedirect(webRequest.getEnableOpenDesktopRedirect());
        }

        DesktopStrategyUtils.dealWatermarkInfo(webRequest.getWatermarkInfo(), cbbDeskStrategyVOIDTO::setEnableWatermark,
                cbbDeskStrategyVOIDTO::setWatermarkInfo);
        cbbDeskStrategyVOIDTO.setNote(webRequest.getNote());

        // 桌面登录同步
        cbbDeskStrategyVOIDTO.setDesktopSyncLoginAccount(webRequest.getDesktopSyncLoginAccount());
        cbbDeskStrategyVOIDTO.setDesktopSyncLoginAccountPermission(webRequest.getDesktopSyncLoginAccountPermission());
        cbbDeskStrategyVOIDTO.setDesktopSyncLoginPassword(webRequest.getDesktopSyncLoginPassword());
        return cbbDeskStrategyVOIDTO;
    }


    /**
     * 删除云桌面策略
     *
     * @param webRequest     请求参数
     * @param builder        批量任务处理对象
     * @return 返回值
     * @throws BusinessException 業務
     */
    @ApiOperation("删除云桌面策略")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse deleteDeskStrategy(DeleteDeskStrategyWebRequest webRequest,
                                                BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        UUID[] idArr = webRequest.getIdArr();

        // 变更策略状态
        for (UUID id : idArr) {
            changeStrategyState(id, CbbDeskStrategyState.DELETING);
        }

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_DELETE_ITEM_NAME)).build())
                .iterator();

        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            result = deleteSingleRecord(builder, idArr, iterator);
        } else {
            final DeleteDeskStrategyBatchTaskHandler handler = new DeleteDeskStrategyBatchTaskHandler(auditLogAPI, iterator);
            result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_BATCH_DELETE_TASK_NAME)
                    .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_BATCH_DELETE_TASK_DESC).enableParallel().registerHandler(handler)
                    .start();
        }
        return CommonWebResponse.success(result);
    }


    private BatchTaskSubmitResult deleteSingleRecord(BatchTaskBuilder builder, UUID[] idArr,
                                                     final Iterator<DefaultBatchTaskItem> iterator) throws BusinessException {
        String logName = deskStrategyAPI.obtainStrategyName(idArr[0]);

        final DeleteDeskStrategyBatchTaskHandler handler = new DeleteDeskStrategyBatchTaskHandler(auditLogAPI, iterator, logName);
        return builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_SINGLE_DELETE_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_SINGLE_DELETE_TASK_DESC, logName).enableParallel()
                .registerHandler(handler).start();
    }

    /**
     * 获取云桌面策略详情
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云桌面策略详情")
    @RequestMapping(value = {"detail", "getInfo"}, method = RequestMethod.POST)
    public CommonWebResponse getDeskStrategy(GetDeskStrategyWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        DeskStrategyVO vo = getDeskStrategyByStrategyType(webRequest);
        return CommonWebResponse.success(vo);
    }

    private DeskStrategyVO getDeskStrategyByStrategyType(GetDeskStrategyWebRequest webRequest) throws BusinessException {
        CbbStrategyType type = obtainStrategyType(webRequest.getId());
        switch (type) {
            case VDI:
                return getVDIDeskStrategyVO(webRequest);
            case IDV:
                return getIDVDeskStrategyVO(webRequest);
            case VOI:
                return getVOIDeskStrategyVO(webRequest);
            case THIRD:
                return getThirdPartyDeskStrategyVO(webRequest);
            default:
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_TYPE_NOT_EXIST, type.toString());
        }
    }

    private DeskStrategyVO getThirdPartyDeskStrategyVO(GetDeskStrategyWebRequest webRequest) throws BusinessException {
        CbbDeskStrategyThirdPartyDTO dto = cbbThirdPartyDeskStrategyMgmtAPI.getDeskStrategyThirdParty(webRequest.getId());
        DeskStrategyVO vo = new DeskStrategyVO();
        vo.setId(webRequest.getId());
        vo.setStrategyName(dto.getStrategyName());
        vo.setCreatorUserName(dto.getCreateUserName());
        vo.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        vo.setEnableSharedPrinting(dto.getEnableSharedPrinting());
        vo.setSessionType(dto.getSessionType());
        vo.setEnableUsbReadOnly(dto.getEnableUsbReadOnly());
        vo.setSessionType(dto.getSessionType());
        vo.setDesktopType(dto.getPattern());
        vo.setEnableDoubleScreen(dto.getOpenDoubleScreen());
        vo.setEnableForbidCatchScreen(dto.getForbidCatchScreen());
        vo.setDeskStrategyState(dto.getDeskStrategyState());
        vo.setStrategyType(CbbStrategyType.THIRD);
        vo.setEstIdleOverTime(dto.getEstIdleOverTime());
        vo.setEstProtocolType(dto.getEstProtocolType());
        // 协议配置
        CbbAgreementDTO cbbAgreementDTO = dto.getAgreementInfo();
        if (Objects.nonNull(cbbAgreementDTO)) {
            AgreementDTO agreement = new AgreementDTO();
            agreement.setLanEstConfig(buildEstConfig(cbbAgreementDTO.getLanEstConfig()));
            agreement.setWanEstConfig(buildEstConfig(cbbAgreementDTO.getWanEstConfig()));
            vo.setAgreementInfo(agreement);
        }

        // 协议代理开关
        vo.setEnableAgreementAgency(dto.getEnableAgreementAgency());
        vo.setEnableForceUseAgreementAgency(dto.getEnableForceUseAgreementAgency());
        // 设置云桌面标签
        vo.setRemark(dto.getRemark());
        // 磁盘映射
        DiskMappingEnum diskMappingEnum = DesktopStrategyUtils.getDiskMappingEnum(dto.getEnableDiskMapping(), dto.getEnableDiskMappingWriteable());
        vo.setDiskMappingType(diskMappingEnum);
        vo.setEnableLanAutoDetection(dto.getEnableLanAutoDetection());
        // 关联的云桌面数量
        vo.setCloudNumber(dto.getRefCount());

        // 网盘映射
        NetDiskMappingEnum netDiskMappingEnum = DesktopStrategyUtils.getNetDiskMappingEnum(dto.getEnableNetDiskMapping(),
                dto.getEnableNetDiskMappingWriteable());
        vo.setNetDiskMappingType(netDiskMappingEnum);

        // CDROM映射
        CdRomMappingEnum cdRomMappingEnum = DesktopStrategyUtils.getCDRomMappingEnum(dto.getEnableCDRomMapping(),
                dto.getEnableCDRomMappingWriteable());
        vo.setCdRomMappingType(cdRomMappingEnum);
        vo.setEnableOpenLoginLimit(dto.getEnableOpenLoginLimit());
        // 桌面登录时间范围
        if (dto.getDesktopAllowLoginTimeInfo() != null) {
            List<DeskTopAllowLoginTimeDTO> deskTopAllowLoginTimeList = JSON.parseArray(dto.getDesktopAllowLoginTimeInfo()
                    , DeskTopAllowLoginTimeDTO.class);
            if (CollectionUtils.isNotEmpty(deskTopAllowLoginTimeList)) {
                vo.setEnableOpenLoginLimit(true);
                vo.setDesktopAllowLoginTimeList(deskTopAllowLoginTimeList);
            }
        }
        // ip访问规则网段
        vo.setIpLimitMode(dto.getIpLimitMode());
        if (CollectionUtils.isNotEmpty(dto.getIpSegmentDTOList())) {
            vo.setIpSegmentDTOList(dto.getIpSegmentDTOList());
        }
        // 剪切板
        vo.setClipBoardMode(dto.getClipBoardMode());
        vo.setClipBoardSupportTypeArr(dto.getClipBoardSupportTypeArr());
        // 云桌面创建方式
        vo.setDeskCreateMode(dto.getDeskCreateMode());
        // 水印配置
        vo.setEnableWatermark(dto.getEnableWatermark());
        vo.setWatermarkInfo(DesktopStrategyUtils.convertWatermarkConfig(dto.getWatermarkInfo()));

        vo.setNote(dto.getNote());
        // 安全审计
        vo.setEnableAuditFile(dto.getEnableAuditFile());
        vo.setAuditFileInfo(dto.getAuditFileInfo());

        // USB存储设备映射
        vo.setUsbStorageDeviceMappingMode(dto.getUsbStorageDeviceMappingMode());
        // 桌面登录账号同步
        vo.setDesktopSyncLoginAccount(dto.getDesktopSyncLoginAccount());
        vo.setDesktopSyncLoginAccountPermission(dto.getDesktopSyncLoginAccountPermission());
        // USB带宽压缩配置
        vo.setUsbStorageDeviceAcceleration(dto.getUsbStorageDeviceAcceleration());
        vo.setEnableUsbCompressAcceleration(dto.getEnableUsbCompressAcceleration());
        vo.setEnableUsbBandwidth(dto.getEnableUsbBandwidth());
        vo.setUsbBandwidthInfo(dto.getUsbBandwidthInfo());
        return vo;
    }

    private DeskStrategyVO getVDIDeskStrategyVO(GetDeskStrategyWebRequest webRequest) throws BusinessException {
        CbbDeskStrategyVDIDTO dto = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(webRequest.getId());
        DeskStrategyVO vo = new DeskStrategyVO();
        vo.setId(dto.getId());
        vo.setStrategyName(dto.getName());
        vo.setDesktopType(dto.getPattern());
        vo.setSessionType(dto.getSessionTypeEnum());
        vo.setEnableSharedPrinting(dto.getEnableSharedPrinting());
        vo.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        vo.setEnableInternet(dto.getOpenInternet());
        vo.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        vo.setEnableDoubleScreen(dto.getOpenDoubleScreen());
        vo.setEnableForbidCatchScreen(dto.getForbidCatchScreen());
        vo.setEnableWebClient(dto.getEnableWebClient());
        vo.setEnableNested(dto.getEnableNested());
        vo.setDeskCreateMode(dto.getDeskCreateMode());
        vo.setDeskStrategyState(dto.getState());
        vo.setClipBoardMode(dto.getClipBoardMode());
        vo.setClipBoardSupportTypeArr(dto.getClipBoardSupportTypeArr());
        vo.setEnableOpenDesktopRedirect(dto.getOpenDesktopRedirect());
        vo.setStrategyType(CbbStrategyType.VDI);
        vo.setCanUsed(dto.getCanUsed());
        vo.setComputerName(obtainComputerName(dto.getId()));
        // 协议代理开关
        vo.setEnableAgreementAgency(dto.getEnableAgreementAgency());
        // 创建者登录账号
        vo.setCreatorUserName(dto.getCreatorUserName());
        // 设置云桌面标签
        vo.setRemark(dto.getRemark());
        vo.setAdOu(dto.getAdOu());
        // 磁盘映射
        DiskMappingEnum diskMappingEnum = DesktopStrategyUtils.getDiskMappingEnum(dto.getEnableDiskMapping(), dto.getEnableDiskMappingWriteable());
        vo.setDiskMappingType(diskMappingEnum);
        // 关联的云桌面数量
        vo.setCloudNumber(dto.getRefCount());
        // 是否开启局域网自动检测
        vo.setEnableLanAutoDetection(dto.getEnableLanAutoDetection());
        // 电源计划
        vo.setPowerPlan(dto.getPowerPlan());
        vo.setPowerPlanTime(dto.getPowerPlanTime());
        vo.setEstIdleOverTime(dto.getEstIdleOverTime());

        vo.setKeyboardEmulationType(dto.getKeyboardEmulationType());
        vo.setDesktopOccupyDriveArr(dto.getDesktopOccupyDriveArr());

        // 网盘映射
        NetDiskMappingEnum netDiskMappingEnum = DesktopStrategyUtils.getNetDiskMappingEnum(dto.getEnableNetDiskMapping(),
                dto.getEnableNetDiskMappingWriteable());
        vo.setNetDiskMappingType(netDiskMappingEnum);

        // CDROM映射
        CdRomMappingEnum cdRomMappingEnum = DesktopStrategyUtils.getCDRomMappingEnum(dto.getEnableCDRomMapping(),
                dto.getEnableCDRomMappingWriteable());
        vo.setCdRomMappingType(cdRomMappingEnum);
        vo.setEnableOpenLoginLimit(false);
        // 桌面登录时间范围
        if (dto.getDesktopAllowLoginTimeInfo() != null) {
            List<DeskTopAllowLoginTimeDTO> deskTopAllowLoginTimeList = JSON.parseArray(dto.getDesktopAllowLoginTimeInfo()
                    , DeskTopAllowLoginTimeDTO.class);
            if (CollectionUtils.isNotEmpty(deskTopAllowLoginTimeList)) {
                vo.setEnableOpenLoginLimit(true);
                vo.setDesktopAllowLoginTimeList(deskTopAllowLoginTimeList);
            }
        }
        vo.setEnableUserSnapshot(dto.getEnableUserSnapshot());
        vo.setIpLimitMode(dto.getIpLimitMode());
        vo.setIpSegmentDTOList(dto.getIpSegmentDTOList());

        // 水印配置
        vo.setWatermarkInfo(DesktopStrategyUtils.convertWatermarkConfig(dto.getWatermarkInfo()));
        vo.setEnableWatermark(dto.getEnableWatermark());

        vo.setNote(dto.getNote());
        // 安全审计
        vo.setEnableAuditFile(dto.getEnableAuditFile());
        vo.setAuditFileInfo(dto.getAuditFileInfo());
        vo.setEnableAuditPrinter(dto.getEnableAuditPrinter());
        vo.setAuditPrinterInfo(dto.getAuditPrinterInfo());

        // 桌面登录账号同步
        vo.setDesktopSyncLoginAccount(dto.getDesktopSyncLoginAccount());
        vo.setDesktopSyncLoginAccountPermission(dto.getDesktopSyncLoginAccountPermission());
        vo.setDesktopSyncLoginPassword(dto.getDesktopSyncLoginPassword());

        // VDI 桌面高可用
        vo.setEnableHa(dto.getEnableHa());
        vo.setHaPriority(dto.getHaPriority());

        // USB存储设备映射
        vo.setUsbStorageDeviceMappingMode(dto.getUsbStorageDeviceMappingMode());

        vo.setUsbStorageDeviceAcceleration(dto.getUsbStorageDeviceAcceleration());
        vo.setEstProtocolType(dto.getEstProtocolType());
        CbbAgreementDTO cbbAgreementDTO = dto.getAgreementInfo();
        if (Objects.nonNull(cbbAgreementDTO)) {
            AgreementDTO agreement = new AgreementDTO();
            agreement.setLanEstConfig(buildEstConfig(cbbAgreementDTO.getLanEstConfig()));
            agreement.setWanEstConfig(buildEstConfig(cbbAgreementDTO.getWanEstConfig()));
            vo.setAgreementInfo(agreement);
        }
        vo.setEnableTransparentEncrypt(dto.getEnableTransparentEncrypt());
        vo.setTransparentEncryptInfo(dto.getTransparentEncryptInfo());
        // 串并口重定向
        vo.setEnableSerialPortRedirect(dto.getEnableSerialPortRedirect());
        vo.setEnableParallelPortRedirect(dto.getEnableParallelPortRedirect());

        vo.setEnableUsbCompressAcceleration(dto.getEnableUsbCompressAcceleration());
        vo.setEnableUsbBandwidth(dto.getEnableUsbBandwidth());
        vo.setUsbBandwidthInfo(dto.getUsbBandwidthInfo());
        vo.setEnableForceUseAgreementAgency(dto.getEnableForceUseAgreementAgency());
        return vo;
    }

    private AgreementConfigRequestDTO buildEstConfig(CbbHestConfigDTO estConfig) {
        AgreementConfigRequestDTO agreementConfig = new AgreementConfigRequestDTO();
        BeanUtils.copyProperties(estConfig, agreementConfig);
        if (estConfig.getWebAdvanceSettingInfo() != null) {
            agreementConfig.setWebAdvanceSettingInfo(JSON.toJSONString(estConfig.getWebAdvanceSettingInfo()));
        }
        return agreementConfig;
    }

    private DeskStrategyVO getIDVDeskStrategyVO(GetDeskStrategyWebRequest webRequest) throws BusinessException {
        CbbDeskStrategyIDVDTO dto = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(webRequest.getId());
        DeskStrategyVO vo = new DeskStrategyVO();
        vo.setId(dto.getId());
        vo.setStrategyName(dto.getName());
        vo.setDesktopType(dto.getPattern());
        vo.setEnableSharedPrinting(dto.getEnableSharedPrinting());
        vo.setSystemDisk(dto.getSystemSize());
        vo.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        vo.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        vo.setDeskStrategyState(dto.getState());
        vo.setEnableOpenDesktopRedirect(dto.getOpenDesktopRedirect());
        vo.setEnableAllowLocalDisk(dto.getAllowLocalDisk());
        vo.setStrategyType(CbbStrategyType.IDV);
        vo.setCanUsed(dto.getCanUsed());
        vo.setComputerName(obtainComputerName(dto.getId()));
        vo.setAdOu(dto.getAdOu());
        vo.setEnableNested(dto.getEnableNested());
        // 创建者登录账号
        vo.setCreatorUserName(dto.getCreatorUserName());
        vo.setAdOu(dto.getAdOu());
        vo.setEnableNested(dto.getEnableNested());
        // 关联的云桌面数量
        vo.setCloudNumber(dto.getRefCount());
        // 系统盘自动扩容
        vo.setEnableFullSystemDisk(dto.getEnableFullSystemDisk());
        vo.setKeyboardEmulationType(dto.getKeyboardEmulationType());
        vo.setDesktopOccupyDriveArr(dto.getDesktopOccupyDriveArr());
        // 水印配置
        vo.setWatermarkInfo(DesktopStrategyUtils.convertWatermarkConfig(dto.getWatermarkInfo()));
        vo.setEnableWatermark(dto.getEnableWatermark());
        vo.setNote(dto.getNote());

        // 桌面登录账号同步
        vo.setDesktopSyncLoginAccount(dto.getDesktopSyncLoginAccount());
        vo.setDesktopSyncLoginAccountPermission(dto.getDesktopSyncLoginAccountPermission());
        vo.setDesktopSyncLoginPassword(dto.getDesktopSyncLoginPassword());
        vo.setUsbStorageDeviceAcceleration(dto.getUsbStorageDeviceAcceleration());
        return vo;
    }

    private DeskStrategyVO getVOIDeskStrategyVO(GetDeskStrategyWebRequest webRequest) throws BusinessException {
        CbbDeskStrategyVOIDTO dto = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(webRequest.getId());
        DeskStrategyVO vo = new DeskStrategyVO();
        vo.setId(dto.getId());
        vo.setStrategyName(dto.getName());
        vo.setDesktopType(dto.getPattern());
        vo.setEnableSharedPrinting(dto.getEnableSharedPrinting());
        vo.setSystemDisk(dto.getSystemSize());
        vo.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        vo.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        vo.setDeskStrategyState(dto.getState());
        vo.setEnableOpenDesktopRedirect(dto.getOpenDesktopRedirect());
        vo.setEnableAllowLocalDisk(dto.getAllowLocalDisk());
        vo.setStrategyType(CbbStrategyType.VOI);
        vo.setCanUsed(dto.getCanUsed());
        vo.setComputerName(obtainComputerName(dto.getId()));
        vo.setEnableNested(dto.getEnableNested());
        vo.setAdOu(dto.getAdOu());
        // 创建者登录账号
        vo.setCreatorUserName(dto.getCreatorUserName());
        // 关联的云桌面数量
        vo.setCloudNumber(dto.getRefCount());
        // 系统盘自动扩容
        vo.setEnableFullSystemDisk(dto.getEnableFullSystemDisk());
        vo.setDesktopOccupyDriveArr(dto.getDesktopOccupyDriveArr());
        // 水印配置
        vo.setWatermarkInfo(DesktopStrategyUtils.convertWatermarkConfig(dto.getWatermarkInfo()));
        vo.setEnableWatermark(dto.getEnableWatermark());
        vo.setNote(dto.getNote());

        // 桌面登录账号同步
        vo.setDesktopSyncLoginAccount(dto.getDesktopSyncLoginAccount());
        vo.setDesktopSyncLoginAccountPermission(dto.getDesktopSyncLoginAccountPermission());
        vo.setDesktopSyncLoginPassword(dto.getDesktopSyncLoginPassword());

        return vo;
    }

    /**
     * 变更VDI云桌面策略信息
     *
     * @param webRequest     请求参数
     * @param builder        builder
     * @param sessionContext sessionContext
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("变更VDI云桌面策略信息")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "edit/vdi", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "updateVDIDeskStrategyValidate")
    @EnableAuthority
    public CommonWebResponse updateVDIDeskStrategy(UpdateVDIDeskStrategyWebRequest webRequest,
                                                   BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        String logName = deskStrategyAPI.obtainStrategyName(webRequest.getId());
        try {
            CommonWebResponse response = doUpdateVDIDeskStrategy(webRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_SUCCESS_LOG, logName);
            return response;
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_FAIL_LOG, logName, e.getI18nMessage());
            return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }

    private CommonWebResponse doUpdateVDIDeskStrategy(UpdateVDIDeskStrategyWebRequest webRequest) throws BusinessException {

        // 获取策略信息
        CbbDeskStrategyVDIDTO oldDeskStrategyVDIDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(webRequest.getId());
        checkBeforeUpdateStrategy(oldDeskStrategyVDIDTO, webRequest);

        // 修改保存策略信息
        CbbUpdateDeskStrategyVDIDTO request = DesktopStrategyUtils.convert2CbbUpdateDeskStrategyVDIDTO(webRequest, oldDeskStrategyVDIDTO);
        cbbVDIDeskStrategyMgmtAPI.updateDeskStrategyVDI(request);
        cloudDeskComputerNameConfigAPI.updateCloudDeskComputerNameConfig(webRequest.getComputerName(), webRequest.getId());

        // 从集群修改同步策略，则变更为本地
        if (!rccmManageAPI.isClusterUnifiedManageNeedRefresh(UnifiedManageFunctionKeyEnum.DESK_STRATEGY)) {
            rccmManageAPI.deleteUnifiedManage(new UnifiedManageDataRequest(webRequest.getId(), UnifiedManageFunctionKeyEnum.DESK_STRATEGY));
        }

        // 通知在线云桌面事件耗时较长，使用异步方式
        RCO_DESK_STRATEGY_CHANGE_NOTIFY_RUNNING_DESK_EXECUTOR.execute(() -> {
            try {
                // 策略修订通知
                rccmManageAPI.updateNotify(webRequest.getId());

                // 水印功能：云桌面策略变了就发送给关联的在线桌面，如果云桌面策略关掉了水印功能，需要发送全局的水印策略
                deskStrategyAPI.doWatermarkAfterUpdateStrategy(request.getId(), oldDeskStrategyVDIDTO.getWatermarkInfo());
                // 如果配置有变化，才需要下发给桌面
                if (oldDeskStrategyVDIDTO.getPowerPlan() != request.getPowerPlan()
                        || !Objects.equals(request.getPowerPlanTime(), oldDeskStrategyVDIDTO.getPowerPlanTime())) {
                    cbbVDIDeskStrategyMgmtAPI.sendPowerPlanConfigToDeskByStrategyId(request.getId(), CbbStrategyType.VDI);
                }

                // 安全审计
                deskStrategyAPI.doAuditUpdateStrategy(request.getId());
            } catch (BusinessException e) {
                LOGGER.error("异步通知云桌面策略变化发生异常（通知RCenter同步策略、通知水印功能、通知电源计划、通知安全审计）");
            }
        });

        return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
    }

    private void checkBeforeUpdateStrategy(CbbDeskStrategyVDIDTO oldDeskStrategyVDIDTO, UpdateVDIDeskStrategyWebRequest webRequest)
            throws BusinessException {
        if (oldDeskStrategyVDIDTO.getState() != CbbDeskStrategyState.AVAILABLE) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_NOT_AVAILABLE);
        }

        // 关联的桌面检查
        checkBindDesktopBeforeUpdateStrategy(oldDeskStrategyVDIDTO, webRequest);

        // 关联的桌面池检查
        checkBindDesktopPoolBeforeUpdateStrategy(oldDeskStrategyVDIDTO, webRequest);

        // 关联的用户组检查
        checkBindUserGroupBeforeUpdateStrategy(oldDeskStrategyVDIDTO, webRequest);
    }

    private void checkBindDesktopBeforeUpdateStrategy(CbbDeskStrategyVDIDTO oldDeskStrategyVDIDTO, UpdateVDIDeskStrategyWebRequest webRequest)
            throws BusinessException {
        List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllDesktopByStrategyId(webRequest.getId());
        if (CollectionUtils.isEmpty(desktopList)) {
            return;
        }
        if (needCheckBindPersonDisk(oldDeskStrategyVDIDTO, webRequest)
                && desktopList.stream().anyMatch(desk -> Optional.ofNullable(desk.getPersonDisk()).orElse(0) <= 0)) {
            throw new BusinessException(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_DESK_REDIRECT_BIND_DESK_MUST_HAS_PERSON_DISK,
                    oldDeskStrategyVDIDTO.getName());
        }
    }

    private void checkBindDesktopPoolBeforeUpdateStrategy(CbbDeskStrategyVDIDTO oldDeskStrategyVDIDTO, UpdateVDIDeskStrategyWebRequest webRequest)
            throws BusinessException {
        ConditionQueryRequestBuilder deskPoolBuilder = new DefaultConditionQueryRequestBuilder();
        deskPoolBuilder.eq(STRATEGY_ID, webRequest.getId());
        List<DesktopPoolBasicDTO> deskPoolList = desktopPoolMgmtAPI.listByConditions(deskPoolBuilder.build());
        if (CollectionUtils.isEmpty(deskPoolList)) {
            return;
        }
        if (needCheckBindPersonDisk(oldDeskStrategyVDIDTO, webRequest)
                && deskPoolList.stream().anyMatch(pool -> Optional.ofNullable(pool.getPersonDisk()).orElse(0) <= 0)) {
            throw new BusinessException(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_DESK_REDIRECT_BIND_POOL_MUST_HAS_PERSON_DISK,
                    oldDeskStrategyVDIDTO.getName());
        }
    }

    private void checkBindUserGroupBeforeUpdateStrategy(CbbDeskStrategyVDIDTO oldDeskStrategyVDIDTO, UpdateVDIDeskStrategyWebRequest webRequest)
            throws BusinessException {
        List<UserGroupDesktopConfigDTO> groupConfigList = userDesktopConfigAPI.getUserGroupDesktopConfigListByStrategyId(webRequest.getId());
        if (CollectionUtils.isEmpty(groupConfigList)) {
            return;
        }

        if (needCheckBindPersonDisk(oldDeskStrategyVDIDTO, webRequest)) {
            List<UUID> specIdList = groupConfigList.stream().map(UserGroupDesktopConfigDTO::getDeskSpecId).filter(Objects::nonNull).distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(specIdList)) {
                return;
            }
            List<CbbDeskSpecDTO> specList = cbbDeskSpecAPI.listByIds(specIdList);
            if (specList.stream().anyMatch(spec -> Optional.ofNullable(spec.getPersonSize()).orElse(0) <= 0)) {
                throw new BusinessException(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_DESK_REDIRECT_BIND_USER_GROUP_MUST_HAS_PERSON_DISK,
                        oldDeskStrategyVDIDTO.getName());
            }
        }
    }

    private boolean needCheckBindPersonDisk(CbbDeskStrategyVDIDTO oldDeskStrategyVDIDTO, UpdateVDIDeskStrategyWebRequest request) {
        return !BooleanUtils.isTrue(oldDeskStrategyVDIDTO.getOpenDesktopRedirect()) && BooleanUtils.isTrue(request.getEnableOpenDesktopRedirect());
    }

    /**
     * 变更IDV云桌面策略信息
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("变更IDV云桌面策略信息")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "edit/idv", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "updateIDVDeskStrategyValidate")
    @EnableAuthority
    public CommonWebResponse updateIDVDeskStrategy(UpdateIDVDeskStrategyWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");

        CbbDeskStrategyIDVDTO deskStrategyIDV = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(webRequest.getId());
        String logName = deskStrategyIDV.getName();
        try {
            List<CloudDesktopDTO> allDesktopList = userDesktopMgmtAPI.getAllDesktopByStrategyId(webRequest.getId());
            validHasTestingDesktop(allDesktopList, logName);
            boolean isPersonalToOther =
                    isPersonalStrategyToOther(deskStrategyIDV, webRequest.getDesktopType());
            CbbUpdateDeskStrategyIDVDTO cbbUpdateDeskStrategyIDVRequest = buildDeskStrategyIDVDTO(webRequest);
            DesktopStrategyUtils.dealWatermarkInfo(webRequest.getWatermarkInfo(), cbbUpdateDeskStrategyIDVRequest::setEnableWatermark,
                    cbbUpdateDeskStrategyIDVRequest::setWatermarkInfo);
            cbbIDVDeskStrategyMgmtAPI.updateDeskStrategyIDV(cbbUpdateDeskStrategyIDVRequest);
            cloudDeskComputerNameConfigAPI.updateCloudDeskComputerNameConfig(webRequest.getComputerName(), webRequest.getId());
            // 策略修订通知
            rccmManageAPI.updateNotify(webRequest.getId());
            // 水印配置修改后操作
            deskStrategyAPI.doWatermarkAfterUpdateStrategy(webRequest.getId(), deskStrategyIDV.getWatermarkInfo());
            deleteDeskRelatedUserProfileStrategy(webRequest.getId(), cbbUpdateDeskStrategyIDVRequest.getPattern());
            deleteRelateDesktopDeliveryObjectAndTestTarget(webRequest.getId(), isPersonalToOther, allDesktopList);
            // 从集群修改同步策略，则变更为本地
            if (!rccmManageAPI.isClusterUnifiedManageNeedRefresh(UnifiedManageFunctionKeyEnum.DESK_STRATEGY)) {
                rccmManageAPI.deleteUnifiedManage(new UnifiedManageDataRequest(webRequest.getId(), UnifiedManageFunctionKeyEnum.DESK_STRATEGY));
            }
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_SUCCESS_LOG, logName);
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_FAIL_LOG, logName, e.getI18nMessage());
            return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }

    private CbbUpdateDeskStrategyIDVDTO buildDeskStrategyIDVDTO(UpdateIDVDeskStrategyWebRequest webRequest) {
        CbbUpdateDeskStrategyIDVDTO cbbUpdateDeskStrategyIDVRequest = new CbbUpdateDeskStrategyIDVDTO();
        // 支持修改云桌面类型(个性，还原，应用分发) 设置云桌面类型
        cbbUpdateDeskStrategyIDVRequest.setPattern(webRequest.getDesktopType());
        cbbUpdateDeskStrategyIDVRequest.setName(webRequest.getStrategyName());
        cbbUpdateDeskStrategyIDVRequest.setIsAllowLocalDisk(webRequest.getEnableAllowLocalDisk());
        cbbUpdateDeskStrategyIDVRequest.setUsbTypeIdArr(webRequest.getUsbTypeIdArr());
        cbbUpdateDeskStrategyIDVRequest.setIsOpenUsbReadOnly(webRequest.getEnableUsbReadOnly());
        cbbUpdateDeskStrategyIDVRequest.setId(webRequest.getId());
        cbbUpdateDeskStrategyIDVRequest.setIsOpenDesktopRedirect(webRequest.getEnableOpenDesktopRedirect());
        cbbUpdateDeskStrategyIDVRequest.setIsOpenUsbReadOnly(webRequest.getEnableUsbReadOnly());
        cbbUpdateDeskStrategyIDVRequest.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        cbbUpdateDeskStrategyIDVRequest.setName(webRequest.getStrategyName());
        cbbUpdateDeskStrategyIDVRequest.setEnableNested(BooleanUtils.isTrue(webRequest.getEnableNested()));
        cbbUpdateDeskStrategyIDVRequest.setAdOu(webRequest.getAdOu());
        cbbUpdateDeskStrategyIDVRequest.setEnableNested(webRequest.getEnableNested() == null ? false : webRequest.getEnableNested());
        cbbUpdateDeskStrategyIDVRequest.setDeskPersonalConfigStrategyType(getCbbDeskPersonalConfigStrategyType(webRequest.getDesktopType()));
        cbbUpdateDeskStrategyIDVRequest.setKeyboardEmulationType(webRequest.getKeyboardEmulationType());
        cbbUpdateDeskStrategyIDVRequest.setDesktopOccupyDriveArr(webRequest.getDesktopOccupyDriveArr());
        cbbUpdateDeskStrategyIDVRequest.setNote(webRequest.getNote());
        // 桌面登录同步
        cbbUpdateDeskStrategyIDVRequest.setDesktopSyncLoginAccount(webRequest.getDesktopSyncLoginAccount());
        cbbUpdateDeskStrategyIDVRequest.setDesktopSyncLoginAccountPermission(webRequest.getDesktopSyncLoginAccountPermission());
        cbbUpdateDeskStrategyIDVRequest.setDesktopSyncLoginPassword(webRequest.getDesktopSyncLoginPassword());
        cbbUpdateDeskStrategyIDVRequest.setUsbStorageDeviceAcceleration(webRequest.getUsbStorageDeviceAcceleration());
        return cbbUpdateDeskStrategyIDVRequest;
    }

    private CbbDeskPersonalConfigStrategyType getCbbDeskPersonalConfigStrategyType(CbbCloudDeskPattern cbbCloudDeskPattern) {
        if (cbbCloudDeskPattern == CbbCloudDeskPattern.RECOVERABLE || cbbCloudDeskPattern == CbbCloudDeskPattern.PERSONAL) {
            return CbbDeskPersonalConfigStrategyType.USE_DATA_DISK;
        }
        return CbbDeskPersonalConfigStrategyType.NOT_USE;
    }


    /**
     * 变更VOI云桌面策略信息
     *
     * @param webRequest 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation("变更VOI云桌面策略信息")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "edit/voi", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "updateVOIDeskStrategyValidate")
    @EnableAuthority
    public CommonWebResponse updateVOIDeskStrategy(UpdateVOIDeskStrategyWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");

        CbbDeskStrategyVOIDTO deskStrategyVOI = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(webRequest.getId());
        String logName = deskStrategyVOI.getName();

        CbbUpdateDeskStrategyVOIDTO cbbUpdateDeskStrategyVOIRequest = new CbbUpdateDeskStrategyVOIDTO();
        // 支持修改云桌面类型(个性，还原) 设置云桌面类型
        cbbUpdateDeskStrategyVOIRequest.setPattern(webRequest.getDesktopType());
        cbbUpdateDeskStrategyVOIRequest.setName(webRequest.getStrategyName());
        cbbUpdateDeskStrategyVOIRequest.setIsAllowLocalDisk(webRequest.getEnableAllowLocalDisk());
        cbbUpdateDeskStrategyVOIRequest.setUsbTypeIdArr(webRequest.getUsbTypeIdArr());
        cbbUpdateDeskStrategyVOIRequest.setId(webRequest.getId());
        cbbUpdateDeskStrategyVOIRequest.setIsOpenDesktopRedirect(webRequest.getEnableOpenDesktopRedirect());
        cbbUpdateDeskStrategyVOIRequest.setIsOpenUsbReadOnly(false);
        cbbUpdateDeskStrategyVOIRequest.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        cbbUpdateDeskStrategyVOIRequest.setName(webRequest.getStrategyName());
        cbbUpdateDeskStrategyVOIRequest.setAdOu(webRequest.getAdOu());
        cbbUpdateDeskStrategyVOIRequest.setDesktopOccupyDriveArr(webRequest.getDesktopOccupyDriveArr());
        DesktopStrategyUtils.dealWatermarkInfo(webRequest.getWatermarkInfo(), cbbUpdateDeskStrategyVOIRequest::setEnableWatermark,
                cbbUpdateDeskStrategyVOIRequest::setWatermarkInfo);
        cbbUpdateDeskStrategyVOIRequest.setNote(webRequest.getNote());
        // 桌面登录同步
        cbbUpdateDeskStrategyVOIRequest.setDesktopSyncLoginAccount(webRequest.getDesktopSyncLoginAccount());
        cbbUpdateDeskStrategyVOIRequest.setDesktopSyncLoginAccountPermission(webRequest.getDesktopSyncLoginAccountPermission());
        cbbUpdateDeskStrategyVOIRequest.setDesktopSyncLoginPassword(webRequest.getDesktopSyncLoginPassword());

        try {
            List<CloudDesktopDTO> allDesktopList = userDesktopMgmtAPI.getAllDesktopByStrategyId(webRequest.getId());
            validHasTestingDesktop(allDesktopList, logName);
            boolean isPersonalToOther =
                    isPersonalStrategyToOther(deskStrategyVOI, webRequest.getDesktopType());
            cbbVOIDeskStrategyMgmtAPI.updateDeskStrategyVOI(cbbUpdateDeskStrategyVOIRequest);
            cloudDeskComputerNameConfigAPI.updateCloudDeskComputerNameConfig(webRequest.getComputerName(), webRequest.getId());
            // 策略修订通知
            rccmManageAPI.updateNotify(webRequest.getId());
            // 发送水印配置
            deskStrategyAPI.doWatermarkAfterUpdateStrategy(webRequest.getId(), deskStrategyVOI.getWatermarkInfo());
            // 通知TCI 在线公共终端，策略变更，需要重新获取启动参数
            deskStrategyTciNotifyAPI.notifyFetchStartParams(webRequest.getId());
            // 从集群修改同步策略，则变更为本地
            if (!rccmManageAPI.isClusterUnifiedManageNeedRefresh(UnifiedManageFunctionKeyEnum.DESK_STRATEGY)) {
                rccmManageAPI.deleteUnifiedManage(new UnifiedManageDataRequest(webRequest.getId(), UnifiedManageFunctionKeyEnum.DESK_STRATEGY));
            }

            deleteDeskRelatedUserProfileStrategy(webRequest.getId(), cbbUpdateDeskStrategyVOIRequest.getPattern());
            deleteRelateDesktopDeliveryObjectAndTestTarget(webRequest.getId(), isPersonalToOther, allDesktopList);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_SUCCESS_LOG, logName);
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_FAIL_LOG, logName, e.getI18nMessage());
            return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }



    private boolean isPersonalStrategyToOther(AbstractDeskStrategyLocalModeDTO localModeDTO,
                                              CbbCloudDeskPattern targetPattern) throws BusinessException {
        return localModeDTO.getPattern() == CbbCloudDeskPattern.PERSONAL && targetPattern != CbbCloudDeskPattern.PERSONAL;
    }

    private void deleteRelateDesktopDeliveryObjectAndTestTarget(UUID strategyId, boolean isPersonalToOther, List<CloudDesktopDTO> desktopList) {
        if (!isPersonalToOther) {
            return;
        }
        LOGGER.info("策略[{}]由个性改为其他，删除关联桌面绑定的交付组", strategyId);
        SCHEDULED_THREAD_POOL.execute(() -> {
            LOGGER.info("策略[{}]开始执行删除关联桌面绑定的交付组", strategyId);
            for (CloudDesktopDTO cloudDesktopDTO : desktopList) {
                UUID deskId = cloudDesktopDTO.getId();
                appDeliveryMgmtAPI.deleteDeliveryObjectWhenStrategyModify(deskId);
                try {
                    uamAppTestAPI.deleteCompletedTestDeskWhenStrategyModify(deskId);
                } catch (BusinessException e) {
                    LOGGER.error("删除桌面[{}]测试组异常", deskId, e);
                }
            }
        });

    }


    private void deleteDeskRelatedUserProfileStrategy(UUID deskStrategyId, CbbCloudDeskPattern cloudDeskPattern) {
        if (CbbCloudDeskPattern.RECOVERABLE == cloudDeskPattern) {
            return;
        }
        // 如果不是还原类型则删除用户配置策略关联记录
        userProfileMgmtAPI.deleteDeskRelatedUserProfileStrategy(deskStrategyId);
    }

    /**
     * 变更第三方云桌面策略信息
     * @param webRequest 请求参数
     * @return 通用返回对象
     * @throws BusinessException 业务异常
     */
    @ApiOperation("变更第三方云桌面策略信息")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "edit/third", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "updateThirdPartyDeskStrategyValidate")
    @EnableAuthority
    public CommonWebResponse updateThirdPartyDeskStrategy(UpdateThirdPartyDeskStrategyWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");

        CbbDeskStrategyThirdPartyDTO deskStrategyThirdParty = cbbThirdPartyDeskStrategyMgmtAPI.getDeskStrategyThirdParty(webRequest.getId());
        if (deskStrategyThirdParty.getDeskStrategyState() != CbbDeskStrategyState.AVAILABLE) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_NOT_AVAILABLE);
        }
        String strategyName = deskStrategyThirdParty.getStrategyName();
        try {
            CbbUpdateDeskStrategyThirdPartyDTO cbbUpdateDeskStrategyThirdPartyDTO =
                    DesktopStrategyUtils.convert2CbbUpdateDeskStrategyThirdPartyDTO(webRequest, deskStrategyThirdParty);
            List<CloudDesktopDTO> allDesktopList = userDesktopMgmtAPI.getAllDesktopByStrategyId(webRequest.getId());
            validHasTestingDesktop(allDesktopList, strategyName);
            cbbThirdPartyDeskStrategyMgmtAPI.updateDeskStrategyThirdParty(cbbUpdateDeskStrategyThirdPartyDTO);

            // 从集群修改同步策略，则变更为本地
            if (!rccmManageAPI.isClusterUnifiedManageNeedRefresh(UnifiedManageFunctionKeyEnum.DESK_STRATEGY)) {
                rccmManageAPI.deleteUnifiedManage(new UnifiedManageDataRequest(webRequest.getId(), UnifiedManageFunctionKeyEnum.DESK_STRATEGY));
            }
            // 通知在线云桌面事件耗时较长，使用异步方式
            RCO_DESK_STRATEGY_CHANGE_NOTIFY_RUNNING_DESK_EXECUTOR.execute(() -> {
                try {
                    // 策略修订通知
                    rccmManageAPI.updateNotify(webRequest.getId());
                    // 发送水印配置
                    deskStrategyAPI.doWatermarkAfterUpdateStrategy(webRequest.getId(), deskStrategyThirdParty.getWatermarkInfo());
                    // 如果配置有变化，才需要下发给桌面
                    if (deskStrategyThirdParty.getPowerPlan() != webRequest.getPowerPlan()
                            || !Objects.equals(webRequest.getPowerPlanTime(), deskStrategyThirdParty.getPowerPlanTime())) {
                        cbbVDIDeskStrategyMgmtAPI.sendPowerPlanConfigToDeskByStrategyId(webRequest.getId(), CbbStrategyType.THIRD);
                    }
                    // 安全审计
                    deskStrategyAPI.doAuditUpdateStrategy(webRequest.getId());
                } catch (Exception e) {
                    LOGGER.error("异步通知云桌面第三方策略[{}]变化发生异常（通知RCenter同步策略、通知水印功能、通知电源计划）", webRequest.getId(), e);
                }
            });
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_SUCCESS_LOG, strategyName);
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_UPDATE_FAIL_LOG, strategyName, e.getI18nMessage());
            return CommonWebResponse.fail(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }

    /**
     * 获取usb设备类型信息
     *
     * @param webRequest 請求参数
     * @return usb设备类型信息
     * @throws BusinessException 獲取外设策略类型信息
     */
    @RequestMapping(value = "getUsbType", method = RequestMethod.POST)
    public CommonWebResponse getUsbType(GetUsbTypeWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        CbbGetAllUSBTypeDTO request = new CbbGetAllUSBTypeDTO();
        CbbUSBTypeDTO[] dtoArr = cbbUSBTypeMgmtAPI.getAllUSBType(request);
        List<IdLabelEntry> voList = Lists.newArrayList();
        for (CbbUSBTypeDTO dto : dtoArr) {
            IdLabelEntry idLabel = new IdLabelEntry();
            idLabel.setId(dto.getId());
            idLabel.setLabel(dto.getUsbTypeName());
            voList.add(idLabel);
        }
        IdLabelEntry[] voArr = new IdLabelEntry[voList.size()];
        PageResponseContent<IdLabelEntry> pageResponseContent = new PageResponseContent<>(voList.toArray(voArr), voList.size());
        return CommonWebResponse.success(pageResponseContent);
    }

    /**
     * 校验云桌面策略名称是否存在
     *
     * @param webRequest 页面请求参数
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "checkDuplication", method = RequestMethod.POST)
    public CommonWebResponse checkDeskStrategyNameDuplication(ValidateDeskStrategyNameWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");

        CheckDeskStrategyNameRequest request = new CheckDeskStrategyNameRequest();
        request.setId(webRequest.getId());
        request.setName(webRequest.getStrategyName());
        CheckDuplicationWebResponse webResponse = new CheckDuplicationWebResponse();
        webResponse.setHasDuplication(Boolean.TRUE.equals(cbbVDIDeskStrategyMgmtAPI.checkDeskStrategyNameDuplicate(request)));
        return CommonWebResponse.success(webResponse);
    }

    /**
     * 获取VGpu选项等相关信息
     *
     * @param request web请求参数
     * @return 返回值CommonWebResponse
     */
    @ApiOperation("获取VGpu相关选项")
    @RequestMapping(value = "vgpu/list", method = RequestMethod.POST)
    public CommonWebResponse getVGpuList(GetVgpuListWebRequest request) {
        Assert.notNull(request, "request must not be null");
        return CommonWebResponse.success(DeskSpecUtils.convert2VgpuItemResponse(getGpuList(request.getClusterId())));
    }

    /**
     * 向cbb接口获取指定集群的GPU信息，
     *
     * @param clusterId 计算集群ID
     * @return VgpuVO
     */
    private List<VgpuVO> getGpuList(UUID clusterId) {
        List<CbbClusterGpuInfoDTO> clusterGpuInfoList = Lists.newArrayList();
        if (Objects.isNull(clusterId)) {
            clusterGpuInfoList.addAll(deskSpecAPI.getClusterGpuInfo(null));
        } else {
            clusterGpuInfoList.addAll(deskSpecAPI.getClusterGpuInfo(clusterId));
        }
        Map<String, VgpuVO> map = new HashMap<>();
        clusterGpuInfoList.forEach(cbbClusterGpuInfoDTO -> {
            VgpuExtraInfo vgpuExtraInfo = new VgpuExtraInfo();
            vgpuExtraInfo.setModel(cbbClusterGpuInfoDTO.getModel());
            vgpuExtraInfo.setParentGpuModel(cbbClusterGpuInfoDTO.getParentGpuModel());
            vgpuExtraInfo.setGraphicsMemorySize(cbbClusterGpuInfoDTO.getGraphicsMemorySize());
            vgpuExtraInfo.setVgpuModelType(cbbClusterGpuInfoDTO.getVgpuModelType());
            String value = deskSpecAPI.buildDefaultAmdModel(vgpuExtraInfo);
            if (!map.containsKey(value)) {
                VgpuVO dto = new VgpuVO();
                dto.setLabel(cbbClusterGpuInfoDTO.getModel());
                dto.setValue(value);
                map.put(value, dto);
            }
        });
        return new ArrayList<>(map.values());
    }

    /**
     * Description: 批量删除云桌面策略处理类
     * Copyright: Copyright (c) 2018
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019年1月16日
     *
     * @author zhuangchenwu
     */
    protected class DeleteDeskStrategyBatchTaskHandler extends AbstractBatchTaskHandler {

        private BaseAuditLogAPI auditLogAPI;

        private String strategyName;

        protected DeleteDeskStrategyBatchTaskHandler(BaseAuditLogAPI auditLogAPI, Iterator<? extends BatchTaskItem> iterator) {
            super(iterator);
            Assert.notNull(auditLogAPI, "auditLogAPI is not null");
            this.auditLogAPI = auditLogAPI;
        }

        protected DeleteDeskStrategyBatchTaskHandler(BaseAuditLogAPI auditLogAPI, Iterator<? extends BatchTaskItem> iterator,
                                                     String strategyName) {
            super(iterator);
            Assert.notNull(auditLogAPI, "auditLogAPI is not null");
            this.auditLogAPI = auditLogAPI;
            this.strategyName = strategyName;
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
            Assert.notNull(item, "item is not null");
            String logName;
            if (strategyName != null) {
                logName = strategyName;
            } else {
                logName = deskStrategyAPI.obtainStrategyName(item.getItemID());
            }
            try {
                // 判断是否系统内置策略，不允许删除
                if (Constants.THIRD_PARTY_STRATEGY_ID.equals(item.getItemID().toString())) {
                    throw new BusinessException(CloudDesktopBusinessKey.RCO_DESK_STRATEGY_THIRD_PARTY_BUILT_IN, logName);
                }
                deskStrategyAPI.checkDeskStrategyCanChange(item.getItemID(), null);
                deskStrategyAPI.deleteDeskStrategy(item.getItemID());
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_DELETE_SUCCESS_LOG, logName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_DELETE_ITEM_SUCCESS_DESC).msgArgs(new String[]{logName}).build();
            } catch (BusinessException e) {
                LOGGER.error("批量删除云桌面策略失败，[{}]", e);
                changeStrategyState(item.getItemID(), CbbDeskStrategyState.AVAILABLE);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_DELETE_FAIL_LOG, logName, e.getI18nMessage());
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_DELETE_ITEM_FAIL_DESC, e, logName, e.getI18nMessage());
            }
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            if (strategyName != null) {
                if (successCount == 1) {
                    return DefaultBatchTaskFinishResult.builder().msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_SINGLE_DELETE_SUCCESS)
                            .msgArgs(new String[]{strategyName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
                } else {
                    return DefaultBatchTaskFinishResult.builder().msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_SINGLE_DELETE_FAIL)
                            .msgArgs(new String[]{strategyName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
                }
            } else {
                return buildDefaultFinishResult(successCount, failCount, CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_BATCH_DELETE_RESULT);
            }
        }
    }

    private void changeStrategyState(UUID id, CbbDeskStrategyState state) throws BusinessException {
        CbbStrategyType cbbStrategyType = obtainStrategyType(id);
        switch (cbbStrategyType) {
            case VDI:
            case IDV:
            case VOI:
            case THIRD:
                cbbDeskStrategyCommonAPI.updateDeskStrategyState(id, state);
                break;
            default:
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_TYPE_NOT_EXIST, cbbStrategyType.toString());
        }
    }

    private CbbStrategyType obtainStrategyType(UUID strategyId) throws BusinessException {
        CbbDeskStrategyDTO deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyId);
        CbbStrategyType cbbStrategyType = deskStrategyDTO.getStrategyType();
        if (cbbStrategyType == null) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_TYPE_IS_NULL, deskStrategyDTO.getName());
        }
        return cbbStrategyType;
    }

    private String obtainComputerName(UUID deskStrategyId) {
        try {
            return cloudDeskComputerNameConfigAPI.findCloudDeskComputerName(deskStrategyId);
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面计算机名数据失败", e);
        }
        return StringUtils.EMPTY;
    }


    /**
     * 校验云桌面创建方式
     * @param request request
     * @throws BusinessException 业务异常
     */
    private void validateVDIStrategyCreateMode(CreateVDIDeskStrategyWebRequest request) throws BusinessException {
        // 限制vdi非个性桌面云桌面策略使用非链接克隆的方式创建
        DeskCreateMode createMode = request.getDeskCreateMode();
        CbbCloudDeskPattern cloudDeskPattern = request.getDesktopType();
        if (!CbbCloudDeskPattern.PERSONAL.equals(cloudDeskPattern) && !DeskCreateMode.LINK_CLONE.equals(createMode)) {
            LOGGER.error("VDI云桌面创建方式[{}]非法!", createMode);
            throw new BusinessException (
                    DeskStrategyBusinessKey.RCDC_CLOUDDESKTOP_DESK_STRATEGY_INVALID_CREATE_MODE_VDI,
                    String.valueOf(createMode)
            );
        }
    }

    private void validHasTestingDesktop(List<CloudDesktopDTO> allDesktopList, String strategyName) throws BusinessException {
        List<UUID> deskIdList = allDesktopList.stream().map(CloudDesktopDTO::getId).collect(Collectors.toList());
        List<AppTestDesktopInfoDTO> desktopInfoDTOList = uamAppTestAPI.findAllByDeskIdInAndTestStateIn(deskIdList,
                DesktopTestStateEnum.getProcessingStateList());
        if (CollectionUtils.isNotEmpty(desktopInfoDTOList)) {
            String desktopName =
                    desktopInfoDTOList.stream().map(AppTestDesktopInfoDTO::getDesktopName).collect(Collectors.joining(COMMA_SEPARATION_CHARACTER));
            throw new BusinessException(BusinessKey.RCDC_RCO_HAS_TESTING_DESKTOP_CAN_NOT_MODIFY_STRATEGY, strategyName, desktopName);
        }
    }
}
