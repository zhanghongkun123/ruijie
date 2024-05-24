package com.ruijie.rcos.rcdc.rco.module.impl.api;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_DESK_UPDATE_IMAGE_EXIST_SYNC_TASK_EXCEPTION;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_IMAGE_REPLICATION_SYNC_OVER_MAX_WAIT_NUM;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskStateRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.VMMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.vm.VmIdRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.vm.VmGraphicsDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.StoragePoolType;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDesktopEventDTO;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.common.query.DefaultConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.QueryPlatformTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.VdiDesktopDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ImageTemplateConstants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PageQuerySourceConstants;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.DeskSpecDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktopsession.DesktopSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.CompensateSessionType;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.GuestToolForDiskStateTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dao.CloudDeskAppConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.DesktopBindUserDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.DesktopUserOpTcpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.*;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto.DesktopTempPermissionRelationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.service.DesktopTempPermissionService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopImageService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoploginlog.service.DesktopOnlineLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.UserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.*;
import com.ruijie.rcos.rcdc.rco.module.impl.service.*;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.RccmCloudDesktopViewService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.VisitorAvailableDesktopQuery;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.RepeatStartVmTerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.sql.SqlConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.DesktopPoolServiceTx;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.modulekit.api.ds.DataSourceNames;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.repositorykit.api.ds.JdbcTemplateHolder;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;


/**
 * 云桌面管理API接口实现.
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author chenzj
 */
public class UserDesktopMgmtAPIImpl implements UserDesktopMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDesktopMgmtAPIImpl.class);

    private static final String IMAGE_TEMPLATE_FIELD_ID = "id";

    private static final String STRATEGY_ADDITIONAL_FIELD_ATTRIBUTE_STRATEGY_ID = "strategyId";

    private static final String STRATEGY_ADDITIONAL_FIELD_ATTRIBUTE_STRATEGY_KEY = "strategyKey";


    /**
     * 分配100个线程数处理池桌面回收
     */
    private static final ExecutorService THREAD_POOL =
            ThreadExecutors.newBuilder("PoolDesktopConnectCloseTimeInit").maxThreadNum(50).queueSize(10000).build();

    private static final String MATCH_EQUAL_FIELD = "faultState";

    private static final String SORT_FIELD = "faultTime";

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private CreateDesktopService createCloudDesktop;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private CloudDesktopViewService cloudDesktopViewService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private DesktopVisitorConfigDAO desktopVisitorConfigDAO;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserMessageUserDAO userMessageUserDAO;

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private DesktopImageService desktopImageService;

    @Autowired
    private CloudDeskAppConfigDAO cloudDeskAppConfigDAO;

    @Autowired
    private DeskFaultInfoService deskFaultInfoService;

    @Autowired
    private VisitorAvailableDesktopQuery visitorAvailableDesktopQuery;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private SoftwareStrategyNotifyAPI softwareStrategyNotifyAPI;

    @Autowired
    private ImageDownloadStateService imageDownloadStateService;

    @Autowired
    private DeskFaultInfoDAO deskFaultInfoDAO;

    @Autowired
    private RemoteAssistService remoteAssistService;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private DesktopOnlineLogService desktopOnlineLogService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private UserProfileStrategyNotifyAPI userProfileStrategyNotifyAPI;

    @Autowired
    private UserDiskDAO userDiskDAO;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private CbbOsFileMgmtAPI cbbOsFileMgmtAPI;

    @Autowired
    private VMMgmtAPI vmMgmtAPI;

    @Autowired
    private ViewDesktopImageRelatedDAO viewDesktopImageRelatedDAO;

    @Autowired
    private CbbUamAppTestTargetAPI cbbUamAppTestTargetAPI;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private RccmCloudDesktopViewService rccmCloudDesktopViewService;

    @Autowired
    private DesktopTempPermissionService desktopTempPermissionService;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;

    @Autowired
    private DesktopPoolServiceTx desktopPoolServiceTx;

    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private StoragePoolAPI storagePoolAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private DesktopUserOpTcpAPI desktopUserOpTcpAPI;


    @Autowired
    private DesktopSessionServiceAPI desktopSessionServiceAPI;

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    @Autowired
    private UserProfileValidateAPI userProfileValidateAPI;

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    private static final String THREAD_POOL_NAME = "desk-pool-notify-agent-cloud-user-op";

    private static final int MAX_THREAD_NUM = 30;

    private static final int QUEUE_SIZE = 1000;


    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    @Override
    public DefaultPageResponse<CloudDesktopDTO> pageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Page<ViewUserDesktopEntity> page = cloudDesktopViewService.pageQuery(request);

        return buildPageQueryResult(page);
    }

    @Override
    public DefaultPageResponse<CloudDesktopDTO> pageQuery(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        PageQueryResponse<ViewUserDesktopEntity> viewUserDesktopPageResponse = cloudDesktopViewService.pageQuery(request);
        ViewUserDesktopEntity[] itemArr = viewUserDesktopPageResponse.getItemArr();
        if (ObjectUtils.isEmpty(itemArr)) {
            return new DefaultPageResponse<>();
        }
        List<ViewUserDesktopEntity> viewUserDesktopList = Arrays.asList(itemArr);
        PageRequest pageable = PageRequest.of(request.getPage(), request.getLimit());
        PageImpl<ViewUserDesktopEntity> userDeskTopPage = new PageImpl<>(viewUserDesktopList, pageable, //
                viewUserDesktopPageResponse.getTotal());

        return buildPageQueryResult(userDeskTopPage);
    }

    @Override
    public DefaultPageResponse<CloudDesktopDTO> pageQueryInDesktopTempPermission(UUID permissionId, PageSearchRequest request)
            throws BusinessException {
        Assert.notNull(permissionId, "permissionId must not be null");
        Assert.notNull(request, "request must not be null");
        Page<ViewUserDesktopEntity> page = cloudDesktopViewService.pageQueryInDesktopTempPermission(permissionId, request);

        return buildPageQueryResult(page);
    }

    private DefaultPageResponse<CloudDesktopDTO> buildPageQueryResult(Page<ViewUserDesktopEntity> page) throws BusinessException {
        DefaultPageResponse<CloudDesktopDTO> response = queryCloudDesktopService.convertPageInfoAndQuery(page);
        List<UUID> deskIdList = Arrays.stream(response.getItemArr()).map(CloudDesktopDTO::getCbbId).collect(Collectors.toList());
        Map<UUID, List<CbbAddExtraDiskDTO>> extraDiskMap = cbbVDIDeskDiskAPI.getDeskExtraDiskByIds(deskIdList);


        Map<UUID, StoragePoolType> storagePoolTypeMap = storagePoolAPI.queryAllStoragePool().stream() //
                .filter(i -> Objects.nonNull(i.getStoragePoolType()))
                .collect(Collectors.toMap(PlatformStoragePoolDTO::getId, PlatformStoragePoolDTO::getStoragePoolType, (v1, v2) -> v2));

        for (CloudDesktopDTO cloudDesktopDTO : response.getItemArr()) {
            List<CbbDeskDiskDTO> diskList = cbbVDIDeskDiskAPI.listDeskDisk(cloudDesktopDTO.getCbbId());
            List<CbbAddExtraDiskDTO> addExtraDiskList = buildForCbbAddExtraDiskList(diskList);
            cloudDesktopDTO.setExtraDiskList(addExtraDiskList);
            if (cloudDesktopDTO.getNetworkAccessMode() == CbbNetworkAccessModeEnums.WIRELESS) {
                cloudDesktopDTO.setWirelessInfoToCurrent();
            }
            // 设置镜像数据盘的信息
            if (CbbCloudDeskType.VDI.name().equals(cloudDesktopDTO.getDesktopCategory())) {
                setImageDiskInfoDTOList(cloudDesktopDTO);
                boolean isExistDiskInPos = diskList.stream().filter(i -> Objects.nonNull(i.getAssignStoragePoolIds()))
                        .anyMatch(i -> storagePoolTypeMap.get(UUID.fromString(i.getAssignStoragePoolIds())) == StoragePoolType.RG_PDS);
                cloudDesktopDTO.setHasAllDiskInExtraStorage(!isExistDiskInPos);
            }
        }

        // 判断桌面是否有应用磁盘
        Stream.of(response.getItemArr()).filter(cloudDesktopDTO -> CbbCloudDeskPattern.PERSONAL.name().equals(cloudDesktopDTO.getDesktopType()))
                .filter(cloudDesktopDTO -> {
                    boolean hasAppDiskTest = cbbUamAppTestTargetAPI.existResourceInTest(AppResourceTypeEnum.CLOUD_DESKTOP, cloudDesktopDTO.getId());
                    cloudDesktopDTO.setHasAppDiskTest(hasAppDiskTest);
                    return !hasAppDiskTest;
                }).forEach(cloudDesktopDTO -> cloudDesktopDTO.setHasAppDisk(cbbAppDeliveryMgmtAPI.existAppDisk(cloudDesktopDTO.getId())));
        for (CloudDesktopDTO cloudDesktopDTO : response.getItemArr()) {
            // 设置镜像数据盘的信息
            if (CbbCloudDeskType.VDI.name().equals(cloudDesktopDTO.getDesktopCategory())) {
                setImageDiskInfoDTOList(cloudDesktopDTO);
            }
            // 设置第三方桌面会话情况
            setThirdPartySession(cloudDesktopDTO);
        }

        return response;

    }

    private List<CbbAddExtraDiskDTO> buildForCbbAddExtraDiskList(List<CbbDeskDiskDTO> diskList) {

        List<CbbAddExtraDiskDTO> resultList = new ArrayList<>();
        for (int index = 0; index < diskList.size(); index++) {
            CbbDeskDiskDTO cbbDeskDiskDTO = diskList.get(index);
            if (cbbDeskDiskDTO.getType() != CbbDiskType.USER_EXTRA) {
                continue;
            }
            CbbAddExtraDiskDTO addExtraDiskDTO = new CbbAddExtraDiskDTO();
            BeanUtils.copyProperties(cbbDeskDiskDTO, addExtraDiskDTO);
            addExtraDiskDTO.setDiskId(cbbDeskDiskDTO.getId());
            addExtraDiskDTO.setExtraSize(cbbDeskDiskDTO.getCapacity());
            addExtraDiskDTO.setIndex(index);
            addExtraDiskDTO.setAssignedStoragePoolId(UUID.fromString(cbbDeskDiskDTO.getAssignStoragePoolIds()));
            resultList.add(addExtraDiskDTO);
        }

        return resultList;
    }

    private void setThirdPartySession(CloudDesktopDTO cloudDesktopDTO) {
        cloudDesktopDTO.setHasSession(Boolean.FALSE);
        if (CbbCloudDeskType.THIRD.name().equals(cloudDesktopDTO.getDesktopCategory())) {
            int num = desktopSessionServiceAPI.countByDeskId(cloudDesktopDTO.getId());
            if (num > 0) {
                cloudDesktopDTO.setHasSession(Boolean.TRUE);
            }
        }
    }

    @Override
    public DefaultPageResponse<CloudDesktopDTO> pageQueryWithAssignment(DesktopAssignmentPageSearchRequest searchRequest) throws BusinessException {
        Assert.notNull(searchRequest, "searchRequest must not be null");
        DefaultPageResponse<CloudDesktopDTO> response = this.pageQuery(searchRequest);
        if (ArrayUtils.isEmpty(response.getItemArr())) {
            return response;
        }

        if (Objects.equals(searchRequest.getQuerySource(), PageQuerySourceConstants.DESKTOP_TEMP_PERMISSION)
                || Objects.nonNull(searchRequest.getDesktopTempPermissionId())) {
            // 临时权限要处理disable，云桌面不能同时绑定两个及以上的临时权限
            return covertWithDesktopTempPermissionAssignment(response, searchRequest.getDesktopTempPermissionId());
        }

        return response;
    }

    /**
     * 设置镜像数据盘的信息（云桌面列表）
     *
     * @param cloudDesktopDTO 云桌面DTO
     * @throws BusinessException
     */
    private void setImageDiskInfoDTOList(CloudDesktopDTO cloudDesktopDTO) throws BusinessException {
        CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(cloudDesktopDTO.getDesktopCategory());
        DeskCreateMode deskCreateMode = DeskCreateMode.valueOf(cloudDesktopDTO.getDeskCreateMode());

        if (cloudDesktopDTO.getSystemDisk() == null) {
            return;
        }

        List<CbbImageDiskInfoDTO> imageDiskInfoList;
        try {
            imageDiskInfoList = cbbImageTemplateMgmtAPI.getPublishedImageDiskInfoList(cloudDesktopDTO.getImageId());
            cloudDesktopDTO.setImageDiskInfoDTOList(imageDiskInfoList);
        } catch (BusinessException e) {
            if (deskType == CbbCloudDeskType.VDI && deskCreateMode == DeskCreateMode.FULL_CLONE) {
                // 异常只可能在镜像模板不存在的时候抛出，当全量克隆的时候允许镜像模板不存在(不抛出异常)
                return;
            }
            throw e;
        }
    }


    private DefaultPageResponse<CloudDesktopDTO> covertWithDesktopTempPermissionAssignment(DefaultPageResponse<CloudDesktopDTO> response,
            UUID queryPermissionId) {
        // 临时权限要处理disable，云桌面不能同时绑定两个及以上的临时权限
        List<UUID> desktopIdList = Arrays.stream(response.getItemArr()).map(CloudDesktopDTO::getCbbId).distinct().collect(Collectors.toList());
        List<DesktopTempPermissionRelationDTO> relationList =
                desktopTempPermissionService.listRelationByRelatedIdsAndRelatedType(desktopIdList, DesktopTempPermissionRelatedType.DESKTOP);
        Map<UUID, UUID> desktopPermissionIdMap = new HashMap<>();
        for (DesktopTempPermissionRelationDTO dto : relationList) {
            desktopPermissionIdMap.put(dto.getRelatedId(), dto.getDesktopTempPermissionId());
        }
        UUID permissionId;
        for (CloudDesktopDTO desktopDTO : response.getItemArr()) {
            permissionId = desktopPermissionIdMap.get(desktopDTO.getCbbId());
            desktopDTO.setDisabled(Objects.nonNull(permissionId) && !Objects.equals(permissionId, queryPermissionId));
        }
        return response;
    }

    @Override
    public CloudDesktopDetailDTO getDesktopDetailById(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "request must not be null");
        CloudDesktopDetailDTO cloudDesktopDetailDTO = queryCloudDesktopService.queryDeskDetail(deskId);
        if (cloudDesktopDetailDTO.getNetworkAccessMode() == CbbNetworkAccessModeEnums.WIRELESS) {
            cloudDesktopDetailDTO.setWirelessNetworkToCurrent();
        }
        List<CbbAddExtraDiskDTO> extraDiskList = cbbVDIDeskDiskAPI.listDeskExtraDisk(deskId);
        cloudDesktopDetailDTO.setExtraDiskList(extraDiskList);

        // 临时权限名称
        CbbDesktopTempPermissionDTO permissionDTO =
                desktopTempPermissionService.getPermissionDTOByRelatedObj(deskId, DesktopTempPermissionRelatedType.DESKTOP);
        cloudDesktopDetailDTO.setDesktopTempPermissionName(Objects.nonNull(permissionDTO) ? permissionDTO.getName() : null);

        return cloudDesktopDetailDTO;
    }

    @Override
    public CreateDesktopResponse create(CreateCloudDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        UserDesktopEntity entity = createCloudDesktop.create(request);
        return new CreateDesktopResponse(entity.getCbbDesktopId(), entity.getDesktopName());
    }

    @Override
    public void createThirdParty(CreateThirdPartyDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        createCloudDesktop.createThirdParty(request);
    }

    @Override
    public CreateDesktopResponse createIDV(IDVCloudDesktopDTO idvCloudDesktopDTO) throws BusinessException {
        Assert.notNull(idvCloudDesktopDTO, "idvCloudDesktopDTO must not be null");
        UserDesktopEntity entity = createCloudDesktop.createIDV(idvCloudDesktopDTO);
        return new CreateDesktopResponse(entity.getCbbDesktopId(), entity.getDesktopName());
    }

    @Override
    public CreateDesktopResponse createVOI(IDVCloudDesktopDTO idvCloudDesktopDTO) throws BusinessException {
        Assert.notNull(idvCloudDesktopDTO, "VOI idvCloudDesktopDTO must not be null");
        UserDesktopEntity entity = createCloudDesktop.createVOI(idvCloudDesktopDTO);
        return new CreateDesktopResponse(entity.getCbbDesktopId(), entity.getDesktopName());
    }

    @Override
    public void moveDesktopToRecycleBin(MoveDesktopToRecycleBinRequest moveDesktopToRecycleBinRequest) throws BusinessException {
        Assert.notNull(moveDesktopToRecycleBinRequest, "moveDesktopToRecycleBinRequest must not be null");

        CbbSoftDeleteDeskDTO cbbSoftDeleteDeskDTO = new CbbSoftDeleteDeskDTO();
        cbbSoftDeleteDeskDTO.setDeskId(moveDesktopToRecycleBinRequest.getDesktopId());
        if (Objects.nonNull(moveDesktopToRecycleBinRequest.getCustomTaskId())) {
            cbbSoftDeleteDeskDTO.setCustomTaskId(moveDesktopToRecycleBinRequest.getCustomTaskId());
        }
        cbbVDIDeskMgmtAPI.deleteDeskVDI(cbbSoftDeleteDeskDTO);

        // 删除桌面关联用户消息
        deleteDesktopRelativeUserMessage(moveDesktopToRecycleBinRequest.getDesktopId());
        // 删除桌面关联的应用ISO挂载信息
        deleteDesktopAppConfig(moveDesktopToRecycleBinRequest.getDesktopId());

        rcaHostAPI.removeWithDeleteDesktopHostEvent(new RcaHostDesktopEventDTO(moveDesktopToRecycleBinRequest.getDesktopId()));
    }

    @Override
    public void forceDeleteDesktop(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId must not be null");

        cbbVDIDeskMgmtAPI.forceDelete(desktopId);
        // 删除桌面关联用户消息
        deleteDesktopRelativeUserMessage(desktopId);
        // 删除桌面关联的应用ISO挂载信息
        deleteDesktopAppConfig(desktopId);
    }



    @Override
    public void configStrategy(EditDesktopStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        CbbDeskStrategyVDIDTO deskStrategyVDIDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(request.getStrategyId());
        Assert.notNull(deskStrategyVDIDTO, "find strategy fail, strategyId=" + request.getStrategyId());

        CbbUpdateDeskStrategyIdVDIDTO cbbUpdateReq = buildCbbUpdateDeskStrategyRequest(request);
        cbbVDIDeskMgmtAPI.updateDeskStrategyVDI(cbbUpdateReq);
    }

    private CbbUpdateDeskStrategyIdVDIDTO buildCbbUpdateDeskStrategyRequest(EditDesktopStrategyRequest request) {
        CbbUpdateDeskStrategyIdVDIDTO cbbUpdateReq = new CbbUpdateDeskStrategyIdVDIDTO();
        cbbUpdateReq.setStrategyId(request.getStrategyId());
        cbbUpdateReq.setDeskId(request.getId());
        return cbbUpdateReq;
    }

    @Override
    public void configNetwork(EditDesktopNetworkRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        CbbUpdateDeskNetworkVDIDTO cbbUpdateReq = buildCbbUpdateDeskNetworkRequest(request);
        cbbVDIDeskMgmtAPI.updateDeskNetworkVDI(cbbUpdateReq);
    }

    private CbbUpdateDeskNetworkVDIDTO buildCbbUpdateDeskNetworkRequest(EditDesktopNetworkRequest request) {
        CbbUpdateDeskNetworkVDIDTO cbbUpdateReq = new CbbUpdateDeskNetworkVDIDTO();
        cbbUpdateReq.setDeskId(request.getId());
        cbbUpdateReq.setNetworkId(request.getNetworkId());
        cbbUpdateReq.setIp(request.getIp());
        return cbbUpdateReq;
    }

    @Override
    public VdiDesktopDetailResponse getVisitorVdiDesktopConfig(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(request.getId());
        if (userDetail == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_USER_ENTITY_IS_NOT_EXIST);
        }
        // 非访客用户抛异常
        if (userDetail.getUserType() != IacUserTypeEnum.VISITOR) {
            throw new BusinessException(BusinessKey.RCDC_USER_IS_NOT_VISITOR_NOT_ALLOW_GET_DESKTOP_CONFIG);
        }
        // 访客用户，直接通过访客用户云桌面配置表获取配置信息
        DesktopVisitorConfigEntity desktopVisitorConfigEntity = desktopVisitorConfigDAO.getOne(request.getId());
        if (desktopVisitorConfigEntity == null) {
            return new VdiDesktopDetailResponse();
        }
        return buildVisitorVdiDesktopDetailResponse(desktopVisitorConfigEntity, userDetail);
    }

    private VdiDesktopDetailResponse buildVisitorVdiDesktopDetailResponse(DesktopVisitorConfigEntity desktopVisitorConfigEntity,
            IacUserDetailDTO userDetail) throws BusinessException {
        VdiDesktopDetailResponse response = new VdiDesktopDetailResponse();
        response.setDesktopImageId(desktopVisitorConfigEntity.getImageTemplateId());
        response.setDesktopNetworkId(desktopVisitorConfigEntity.getNetworkId());
        response.setDesktopStrategyId(desktopVisitorConfigEntity.getStrategyId());
        obtainVdiDesktopDetailResponseNames(response, desktopVisitorConfigEntity.getImageTemplateId(), desktopVisitorConfigEntity.getStrategyId(),
                desktopVisitorConfigEntity.getNetworkId());

        List<UserDesktopEntity> userDesktopList = userDesktopDAO.findByUserId(userDetail.getId());
        response.setCanEdit(CollectionUtils.isEmpty(userDesktopList));
        return response;
    }

    private void obtainVdiDesktopDetailResponseNames(VdiDesktopDetailResponse response, UUID imageId, UUID strategyId, UUID networkId)
            throws BusinessException {
        String imageName = queryCloudDesktopService.checkAndGetImageByIdAndImageType(imageId, CbbImageType.VDI).getImageName();
        response.setDesktopImageName(imageName);

        String networkName = queryCloudDesktopService.checkAndGetNetworkById(networkId).getDeskNetworkName();
        response.setDesktopNetworkName(networkName);

        String strategyName = queryCloudDesktopService.checkAndGetVDIStrategyById(strategyId).getName();
        response.setDesktopStrategyName(strategyName);
    }

    @Override
    public CloudDesktopDetailDTO getDesktopStrategyByDesktopId(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        return queryCloudDesktopService.queryDeskDetail(request.getId());
    }

    @Override
    public DesktopNetworkDTO getNetworkByDesktopId(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        CloudDesktopDetailDTO cloudDesktopDTO = queryCloudDesktopService.queryDeskDetail(request.getId());
        CbbDeskNetworkDetailDTO dto = cbbNetworkMgmtAPI.getDeskNetwork(cloudDesktopDTO.getDesktopNetworkId());
        DesktopNetworkDTO desktopNetworkDTO = new DesktopNetworkDTO();
        DesktopNetworkAddressDTO addressDTO = new DesktopNetworkAddressDTO();
        addressDTO.setRow(dto);
        addressDTO.setId(cloudDesktopDTO.getDesktopNetworkId());
        addressDTO.setLabel(cloudDesktopDTO.getDesktopNetworkName());
        addressDTO.setIp(cloudDesktopDTO.getConfigIp());
        desktopNetworkDTO.setAddress(addressDTO);
        return desktopNetworkDTO;
    }

    @Override
    public void editDesktopRole(EditDesktopRoleRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        cloudDesktopOperateService.editDesktopRole(request.getId(), request.getDesktopRole());
    }

    @Override
    public List<DesktopStateNumDTO> listDesktopNumByUserGroup(UUID[] uuidArr) throws BusinessException {
        Assert.notNull(uuidArr, "uuidArr cannot be null.");

        List<UUID> uuidList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(uuidArr)) {
            uuidList = Arrays.asList(uuidArr);
        }

        return cloudDesktopViewService.countByDeskState(uuidList, false);
    }

    @Override
    public CloudDesktopDetailListDTO getAllRunningDesktopDetail() {
        CloudDesktopDetailListDTO cloudDesktopDetailListDTO = new CloudDesktopDetailListDTO();
        List<ViewUserDesktopEntity> runningDesktopList = viewDesktopDetailDAO.findAllByDeskStateAndIsDelete(CbbCloudDeskState.RUNNING.name(), false);
        List<CloudDesktopDetailDTO> resultList = new ArrayList<>(runningDesktopList.size());
        for (ViewUserDesktopEntity entity : runningDesktopList) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
            CloudDesktopDTO cloudDesktopDTO = ViewUserDesktopEntity.convertEntityToDTO(entity);
            BeanUtils.copyProperties(cloudDesktopDTO, cloudDesktopDetailDTO);
            resultList.add(cloudDesktopDetailDTO);
        }
        cloudDesktopDetailListDTO.setCloudDesktopDetailList(resultList);
        return cloudDesktopDetailListDTO;
    }

    @Override
    public List<CloudDesktopDetailDTO> getSendGlobalWatermarkDesktopList() throws BusinessException {
        List<ViewUserDesktopEntity> desktopList =
                viewDesktopDetailDAO.findAllByDeskStateAndStrategyEnableWatermarkAndIsDelete(CbbCloudDeskState.RUNNING.name(), false, false);
        if (CollectionUtils.isEmpty(desktopList)) {
            return Collections.emptyList();
        }
        List<CloudDesktopDetailDTO> resultList = new ArrayList<>(desktopList.size());
        for (ViewUserDesktopEntity entity : desktopList) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
            CloudDesktopDTO cloudDesktopDTO = ViewUserDesktopEntity.convertEntityToDTO(entity);
            BeanUtils.copyProperties(cloudDesktopDTO, cloudDesktopDetailDTO);
            resultList.add(cloudDesktopDetailDTO);
        }
        return resultList;
    }

    @Override
    public List<UUID> getDesktopIdArr(GetAllByGroupIdAndIsDeleteRequest request) {
        Assert.notNull(request, "request must not be null");
        Assert.notEmpty(request.getUserGroupIdArr(), "userGroupIds must not be null");
        Assert.notEmpty(request.getTerminalGroupIdArr(), "terminalGroupIds must not be null");
        Assert.notNull(request.getDesktopPoolArr(), "desktopPoolArr must not be null");

        // 处理桌面池组为空的情况
        if (ArrayUtils.isEmpty(request.getDesktopPoolArr())) {
            return viewDesktopDetailDAO.findIdsByUserGroupIdInAndTerminalGroupIdIn(Arrays.asList(request.getUserGroupIdArr()),
                    Arrays.asList(request.getTerminalGroupIdArr()));
        }
        return viewDesktopDetailDAO.findIdsByUserGroupIdInAndTerminalGroupIdInAndDesktopPoolIdIn(Arrays.asList(request.getUserGroupIdArr()),
                Arrays.asList(request.getTerminalGroupIdArr()), Arrays.asList(request.getDesktopPoolArr()));
    }

    @Override
    public long getUserVdiDesktopNum(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        return userService.countUserDesktopNumContainCreatingNum(userId);
    }

    @Override
    public int getCreatingDesktopNum(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        return userService.getCreatingDesktopNum(userId);
    }

    @Override
    public boolean isUserHasLogin(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId must not be null");

        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
        return userLoginSession.hasLogin(userDetail.getUserName());
    }

    @Override
    public List<CloudDesktopDTO> listUserVDIDesktop(IacUserDetailDTO cbbUserDetailDTO, String terminalId) {
        Assert.notNull(cbbUserDetailDTO, "Param [cbbUserDetailDTO] must not be null");
        Assert.hasText(terminalId, "Param [terminalId] must not be null or empty");

        // 查询用户绑定的云桌面（不包括回收站中的桌面）
        List<CloudDesktopDTO> userDesktopList = this.getAllDesktopByUserId(cbbUserDetailDTO.getId());

        List<CbbDesktopPoolDTO> desktopPoolList =
                getDesktopPoolList(cbbUserDetailDTO.getUserType(), cbbUserDetailDTO.getId(), cbbUserDetailDTO.getUserName());

        Map<UUID, CloudDesktopDTO> bindPoolDesktopMap =
                userDesktopList.stream().filter(desk -> Objects.equals(desk.getDesktopPoolType(), DesktopPoolType.STATIC.name()))
                        .collect(Collectors.toMap(CloudDesktopDTO::getDesktopPoolId, Function.identity()));

        // 去除删除中和非VDI的桌面
        userDesktopList = userDesktopList.stream().filter(this::filterUserVDIDesk).collect(Collectors.toList());

        // 合并池和普通的桌面列表
        userDesktopList.addAll(desktopPoolList.stream().map(item -> convertFromDesktopPoolInfo(item, cbbUserDetailDTO, bindPoolDesktopMap))
                .collect(Collectors.toList()));

        if (CollectionUtils.isEmpty(userDesktopList)) {
            LOGGER.info("用户[{}]未配置云桌面；action:{}", cbbUserDetailDTO.getUserName(), ShineAction.RCDC_SHINE_CMM_QUERY_DESKTOP);
            return Collections.emptyList();
        }
        // 非访客用户直接返回查询结果
        if (cbbUserDetailDTO.getUserType() != IacUserTypeEnum.VISITOR) {
            LOGGER.info("普通用户[{}]查询到[{}]个云桌面", cbbUserDetailDTO.getUserName(), userDesktopList.size());
            // 按桌面创建时间排序
            userDesktopList.sort(Comparator.comparing(CloudDesktopDTO::getCreateTime));
            return userDesktopList;
        }
        // 访客用户获取云桌面
        ViewUserDesktopEntity visitorDesktop;
        synchronized (queryCloudDesktopService) {
            visitorDesktop = visitorAvailableDesktopQuery.queryVisitorAvailableDesktop(cbbUserDetailDTO.getId(), terminalId);
        }
        if (visitorDesktop != null) {
            CloudDesktopDTO cloudDesktopDTO = ViewUserDesktopEntity.convertEntityToDTO(visitorDesktop);
            return ImmutableList.of(cloudDesktopDTO);
        } else {
            LOGGER.info("当前访客[{}]在终端[{}]没有可用云桌面", cbbUserDetailDTO.getUserName(), terminalId);
            return Collections.emptyList();
        }
    }

    // 性能优化由于时间问题，担心影响其他功能逻辑因此单独拷贝一份代码进行优化
    @Override
    public List<QueryDesktopItemDTO> listUserVDIDesktop(ListUserVdiDesktopRequest request) {
        Assert.notNull(request, "request can not be null");

        UUID userId = request.getUserId();
        String userName = request.getUserName();
        IacUserTypeEnum userType = request.getUserType();
        String terminalId = request.getTerminalId();

        // 查询用户绑定的云桌面（不包括回收站中的桌面）
        JdbcTemplate jdbcTemplate = jdbcTemplateHolder.loadJdbcTemplate(DataSourceNames.DEFAULT_DATASOURCE_BEAN_NAME);
        List<QueryDesktopItemDTO> userDesktopList = jdbcTemplate.query(SqlConstants.QUERY_USER_DESKTOP_SQL,
                new BeanPropertyRowMapper<>(QueryDesktopItemDTO.class), request.getUserId(), false);


        Map<UUID, QueryDesktopItemDTO> bindPoolDesktopMap = userDesktopList.stream() //
                .filter(desk -> Objects.equals(desk.getDesktopPoolType(), DesktopPoolType.STATIC.name())) //
                .collect(Collectors.toMap(QueryDesktopItemDTO::getDesktopPoolId, Function.identity()));

        // 去除删除中和非VDI的桌面
        userDesktopList = userDesktopList.stream().filter(this::filterUserVDIDesk).collect(Collectors.toList());


        List<CbbDesktopPoolDTO> desktopPoolList = getDesktopPoolList(userType, userId, userName);
        if (ObjectUtils.isNotEmpty(desktopPoolList)) {
            List<UUID> imageTemplateIdList = new ArrayList<>();
            List<UUID> strategyIdList = new ArrayList<>();
            for (CbbDesktopPoolDTO cbbDesktopPoolDTO : desktopPoolList) {
                if (cbbDesktopPoolDTO.getImageTemplateId() != null) {
                    imageTemplateIdList.add(cbbDesktopPoolDTO.getImageTemplateId());
                }
                if (cbbDesktopPoolDTO.getStrategyId() != null) {
                    strategyIdList.add(cbbDesktopPoolDTO.getStrategyId());
                }
            }

            // 获取镜像模版id——>镜像模板MAP
            Map<UUID, CbbImageTemplateDTO> imageTemplateId2ImageTemplateDTOMap = getImageTemplateId2ImageTemplateDTOMap(imageTemplateIdList);
            // 获取策略ID——>云桌面策略MAP
            Map<UUID, CbbDeskStrategyDTO> deskStrategyId2StrategyMap = getStrategyId2DeskStrategyDTOMap(strategyIdList);

            for (CbbDesktopPoolDTO cbbDesktopPoolDTO : desktopPoolList) {
                CloudDesktopDTO cloudDesktopDTO = convertFromDesktopPoolInfo(cbbDesktopPoolDTO, request, bindPoolDesktopMap,
                        imageTemplateId2ImageTemplateDTOMap, deskStrategyId2StrategyMap);
                QueryDesktopItemDTO queryDesktopItemDTO = QueryDesktopItemDTO.convertToItem(cloudDesktopDTO);

                // 合并池和普通的桌面列表
                userDesktopList.add(queryDesktopItemDTO);
            }
        }

        if (CollectionUtils.isEmpty(userDesktopList)) {
            LOGGER.info("用户[{}]未配置云桌面；action:{}", userName, ShineAction.RCDC_SHINE_CMM_QUERY_DESKTOP);
            return Collections.emptyList();
        }
        // 非访客用户直接返回查询结果
        if (userType != IacUserTypeEnum.VISITOR) {
            LOGGER.info("普通用户[{}]查询到[{}]个云桌面", userName, userDesktopList.size());
            // 按桌面创建时间排序
            userDesktopList.sort(Comparator.comparing(QueryDesktopItemDTO::getCreateTime));
            return userDesktopList;
        }
        // 访客用户获取云桌面
        ViewUserDesktopEntity visitorDesktop;
        synchronized (userId.toString().intern()) {
            visitorDesktop = visitorAvailableDesktopQuery.queryVisitorAvailableDesktop(userId, terminalId);
        }
        if (visitorDesktop != null) {
            CloudDesktopDTO cloudDesktopDTO = ViewUserDesktopEntity.convertEntityToDTO(visitorDesktop);
            List<QueryDesktopItemDTO> vistorDesktopList = new ArrayList<>();
            vistorDesktopList.add(QueryDesktopItemDTO.convertToItem(cloudDesktopDTO));
            return vistorDesktopList;
        } else {
            LOGGER.info("当前访客[{}]在终端[{}]没有可用云桌面", userName, terminalId);
            return Collections.emptyList();
        }
    }


    private Map<UUID, CbbDeskStrategyDTO> getStrategyId2DeskStrategyDTOMap(List<UUID> strategyIdList) {
        if (ObjectUtils.isEmpty(strategyIdList)) {
            return new HashMap<>();
        }
        List<CbbDeskStrategyDTO> cbbDeskStrategyDTOList = cbbVDIDeskStrategyMgmtAPI.listDeskStrategyVDI();
        return cbbDeskStrategyDTOList.stream() //
                .filter(deskStrategy -> strategyIdList.contains(deskStrategy.getId())) //
                .collect(Collectors.toMap(CbbDeskStrategyDTO::getId, Function.identity()));
    }

    private Map<UUID, CbbImageTemplateDTO> getImageTemplateId2ImageTemplateDTOMap(List<UUID> imageTemplateIdList) {
        if (ObjectUtils.isEmpty(imageTemplateIdList)) {
            return new HashMap<>();
        }
        List<CbbImageTemplateDTO> cbbImageTemplateDTOList = getCbbImageTemplateDTOList(imageTemplateIdList);
        return cbbImageTemplateDTOList.stream() //
                .collect(Collectors.toMap(CbbImageTemplateDTO::getId, Function.identity()));
    }


    private List<CbbImageTemplateDTO> getCbbImageTemplateDTOList(List<UUID> imageTemplateIdList) {
        if (ObjectUtils.isEmpty(imageTemplateIdList)) {
            return new ArrayList<>();
        }
        ConditionQueryRequest conditionQueryRequest = new DefaultConditionQueryRequestBuilder() //
                .in(IMAGE_TEMPLATE_FIELD_ID, imageTemplateIdList.toArray(new UUID[0])) //
                .build(); //
        return cbbImageTemplateMgmtAPI.listByConditions(conditionQueryRequest);
    }

    private List<CbbDesktopPoolDTO> getDesktopPoolList(IacUserTypeEnum userType, UUID userId, String userName) {
        if (userType != IacUserTypeEnum.VISITOR) {
            try {
                return desktopPoolMgmtAPI.listDesktopPoolByUserId(userId, userType);
            } catch (BusinessException e) {
                LOGGER.error(String.format("普通用户[%s]查询桌面池列表异常", userName), e);
            }
        }
        return new ArrayList<>();
    }

    /**
     * 去除删除中和非VDI和THIRD的桌面
     *
     * @param desk 桌面
     * @return true
     */
    private boolean filterUserVDIDesk(CloudDesktopDTO desk) {
        return !CbbCloudDeskState.DELETING.name().equals(desk.getDesktopState())
                && (Objects.equals(CbbCloudDeskType.VDI.toString(), desk.getDesktopCategory())
                        || Objects.equals(CbbCloudDeskType.THIRD.toString(), desk.getDesktopCategory()))
                && (StringUtils.isEmpty(desk.getDesktopPoolType()) || Objects.equals(DesktopPoolType.COMMON.name(), desk.getDesktopPoolType())
                        && (desk.getImageUsage() == null || desk.getImageUsage() == ImageUsageTypeEnum.DESK));
    }

    private boolean filterUserVDIDesk(QueryDesktopItemDTO desk) {
        return !CbbCloudDeskState.DELETING.name().equals(desk.getDesktopState())
                && (Objects.equals(CbbCloudDeskType.VDI.toString(), desk.getDesktopCategory())
                        || Objects.equals(CbbCloudDeskType.THIRD.toString(), desk.getDesktopCategory()))
                && (StringUtils.isEmpty(desk.getDesktopPoolType()) || Objects.equals(DesktopPoolType.COMMON.name(), desk.getDesktopPoolType())
                        && (desk.getImageUsage() == null || ImageUsageTypeEnum.DESK == desk.getImageUsage()));
    }

    private CloudDesktopDTO convertFromDesktopPoolInfo(CbbDesktopPoolDTO desktopPoolDTO, ListUserVdiDesktopRequest request,
            Map<UUID, QueryDesktopItemDTO> bindPoolDesktopMap, Map<UUID, CbbImageTemplateDTO> imageTemplateId2ImageTemplateDTOMap,
            Map<UUID, CbbDeskStrategyDTO> deskStrategyId2StrategyMap) {
        CloudDesktopDTO cloudDesktopDTO = new CloudDesktopDTO();
        cloudDesktopDTO.setId(desktopPoolDTO.getId());
        cloudDesktopDTO.setSessionType(desktopPoolDTO.getSessionType());
        cloudDesktopDTO.setDesktopType(desktopPoolDTO.getPoolType().name());
        cloudDesktopDTO.setCbbId(desktopPoolDTO.getId());
        cloudDesktopDTO.setDesktopName(desktopPoolDTO.getName());
        cloudDesktopDTO.setDesktopPoolType(DesktopPoolType.convertToPoolDeskType(desktopPoolDTO.getPoolModel()).name());
        cloudDesktopDTO.setDesktopPoolId(desktopPoolDTO.getId());
        cloudDesktopDTO.setDesktopState(desktopPoolDTO.getPoolState().name());
        cloudDesktopDTO.setIsOpenMaintenance(desktopPoolDTO.getIsOpenMaintenance());
        cloudDesktopDTO.setUserId(request.getUserId());
        cloudDesktopDTO.setUserName(request.getUserName());
        cloudDesktopDTO.setDesktopState(CbbCloudDeskState.RUNNING.name());
        cloudDesktopDTO.setDesktopCategory(desktopPoolDTO.getPoolType().name());
        cloudDesktopDTO.setOsName("");
        if (desktopPoolDTO.getPoolType() == CbbDesktopPoolType.THIRD) {
            List<String> osList = cbbDeskMgmtAPI.listOsTypeByPoolId(desktopPoolDTO.getId());
            if (osList.size() == 1) {
                cloudDesktopDTO.setOsName(osList.get(0));
            }
        }

        if (desktopPoolDTO.getPoolType() == CbbDesktopPoolType.VDI) {
            // 云平台相关
            UUID platformId = desktopPoolDTO.getPlatformId();
            fillPlatformInfo(platformId, cloudDesktopDTO);

            // 填充镜像信息
            fillImageInfo(desktopPoolDTO, bindPoolDesktopMap, imageTemplateId2ImageTemplateDTOMap, cloudDesktopDTO);

            CbbDeskStrategyDTO cbbDeskStrategyDTO = deskStrategyId2StrategyMap.get(desktopPoolDTO.getStrategyId());
            // 填充是否开启代理
            boolean enableAgreementAgency = Boolean.TRUE.equals(cbbDeskStrategyDTO.getEnableAgreementAgency());
            cloudDesktopDTO.setEnableAgreementAgency(enableAgreementAgency);

            // 填充是否开启网页客户端
            if (cbbDeskStrategyDTO != null) {
                cloudDesktopDTO.setEnableWebClient(cbbDeskStrategyDTO.getEnableWebClient());
            }
        }

        cloudDesktopDTO.setCreateTime(desktopPoolDTO.getCreateTime());
        return cloudDesktopDTO;
    }

    private static void fillImageInfo(CbbDesktopPoolDTO desktopPoolDTO, Map<UUID, QueryDesktopItemDTO> bindPoolDesktopMap,
            Map<UUID, CbbImageTemplateDTO> imageTemplateId2ImageTemplateDTOMap, CloudDesktopDTO cloudDesktopDTO) {

        UUID imageId = desktopPoolDTO.getImageTemplateId();
        UUID id = desktopPoolDTO.getId();

        CbbImageTemplateDTO cbbImageTemplateDTO = imageTemplateId2ImageTemplateDTOMap.get(imageId);
        if (cbbImageTemplateDTO != null) {
            cloudDesktopDTO.setImageName(cbbImageTemplateDTO.getImageTemplateName());
            cloudDesktopDTO.setOsName(cbbImageTemplateDTO.getOsType().name());
        } else if (bindPoolDesktopMap.containsKey(id)) {
            QueryDesktopItemDTO bindDesktopDTO = bindPoolDesktopMap.get(id);
            // 桌面的镜像可能会被删除，这里默认先赋值
            cloudDesktopDTO.setOsName(bindDesktopDTO.getOsName());
            cloudDesktopDTO.setImageName(bindDesktopDTO.getImageName());
        }
    }

    private CloudDesktopDTO convertFromDesktopPoolInfo(CbbDesktopPoolDTO desktopPoolDTO, IacUserDetailDTO cbbUserDetailDTO,
            Map<UUID, CloudDesktopDTO> bindPoolDesktopMap) {
        CloudDesktopDTO cloudDesktopDTO = new CloudDesktopDTO();
        cloudDesktopDTO.setId(desktopPoolDTO.getId());
        cloudDesktopDTO.setSessionType(desktopPoolDTO.getSessionType());
        cloudDesktopDTO.setDesktopType(desktopPoolDTO.getPoolType().name());
        cloudDesktopDTO.setCbbId(desktopPoolDTO.getId());
        cloudDesktopDTO.setDesktopName(desktopPoolDTO.getName());
        cloudDesktopDTO.setDesktopPoolType(DesktopPoolType.convertToPoolDeskType(desktopPoolDTO.getPoolModel()).name());
        cloudDesktopDTO.setDesktopPoolId(desktopPoolDTO.getId());
        cloudDesktopDTO.setDesktopState(desktopPoolDTO.getPoolState().name());
        cloudDesktopDTO.setIsOpenMaintenance(desktopPoolDTO.getIsOpenMaintenance());
        cloudDesktopDTO.setUserId(cbbUserDetailDTO.getId());
        cloudDesktopDTO.setUserName(cbbUserDetailDTO.getUserName());
        cloudDesktopDTO.setDesktopState(CbbCloudDeskState.RUNNING.name());
        cloudDesktopDTO.setDesktopCategory(desktopPoolDTO.getPoolType().name());
        cloudDesktopDTO.setOsName("");
        if (desktopPoolDTO.getPoolType() == CbbDesktopPoolType.THIRD) {
            List<String> osList = cbbDeskMgmtAPI.listOsTypeByPoolId(desktopPoolDTO.getId());
            if (osList.size() == 1) {
                cloudDesktopDTO.setOsName(osList.get(0));
            }
        }
        if (desktopPoolDTO.getPoolType() == CbbDesktopPoolType.VDI) {

            // 云平台相关
            try {
                fillPlatformInfo(desktopPoolDTO.getPlatformId(), cloudDesktopDTO);
            } catch (Exception e) {
                LOGGER.error("获取桌面池的云平台信息失败", e);
            }

            UUID imageId = desktopPoolDTO.getImageTemplateId();
            if (bindPoolDesktopMap.containsKey(desktopPoolDTO.getId())) {
                CloudDesktopDTO bindDesktopDTO = bindPoolDesktopMap.get(desktopPoolDTO.getId());
                imageId = bindDesktopDTO.getImageId();
                // 桌面的镜像可能会被删除，这里默认先赋值
                cloudDesktopDTO.setOsName(bindDesktopDTO.getOsName());
                cloudDesktopDTO.setImageName(bindDesktopDTO.getImageName());
            }
            try {
                CbbGetImageTemplateInfoDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
                cloudDesktopDTO.setImageName(imageTemplateDetail.getImageName());
                cloudDesktopDTO.setOsName(imageTemplateDetail.getCbbOsType().name());
            } catch (Exception e) {
                LOGGER.error("获取桌面池的镜像失败", e);
            }

            try {
                CbbDeskStrategyVDIDTO strategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(desktopPoolDTO.getStrategyId());
                cloudDesktopDTO.setEnableWebClient(strategyVDI.getEnableWebClient());
                cloudDesktopDTO.setEnableAgreementAgency(strategyVDI.getEnableAgreementAgency());
            } catch (Exception e) {
                LOGGER.error("获取桌面池的策略失败", e);
            }
        }

        cloudDesktopDTO.setCreateTime(desktopPoolDTO.getCreateTime());
        return cloudDesktopDTO;
    }

    @Override
    public List<CloudDesktopDTO> getAllDesktopByUserId(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        List<ViewUserDesktopEntity> bindDesktopList = viewDesktopDetailDAO.findByUserIdAndIsDelete(userId, false);
        return getCloudDesktopList(bindDesktopList);
    }

    @Override
    public List<CloudDesktopDTO> getAllRunningVDIDesktopByUserIdList(List<UUID> userIdList) {
        Assert.notNull(userIdList, "uuidList must not be null");

        List<ViewUserDesktopEntity> bindDesktopList = viewDesktopDetailDAO.findByUserIdInAndIsDeleteAndDeskStateAndCbbImageType(userIdList, false,
                CbbCloudDeskState.RUNNING.toString(), CbbImageType.VDI.toString());
        return getCloudDesktopList(bindDesktopList);
    }

    @Override
    public List<CloudDesktopDTO> getAllRunningVDIDesktopByDeskIdIn(List<UUID> uuidList) {
        Assert.notNull(uuidList, "uuidList must not be null");

        List<ViewUserDesktopEntity> bindDesktopList = viewDesktopDetailDAO.findBycbbDesktopIdInAndIsDeleteAndDeskStateAndCbbImageType(uuidList, false,
                CbbCloudDeskState.RUNNING.toString(), CbbImageType.VDI.toString());
        return getCloudDesktopList(bindDesktopList);
    }


    @Override
    public List<CloudDesktopDTO> getAllDesktopByDeskIdInDeskStateInRecycleBin(List<UUID> uuidList) {
        Assert.notNull(uuidList, "uuidList must not be null");

        // 回收站 状态 删除中
        List<String> deskStateList = new ArrayList<>();
        deskStateList.add(CbbCloudDeskState.RECYCLE_BIN.toString());
        deskStateList.add(CbbCloudDeskState.DELETING.toString());

        List<ViewUserDesktopEntity> bindDesktopList = viewDesktopDetailDAO.findBycbbDesktopIdInAndDeskStateIn(uuidList, deskStateList);
        return getCloudDesktopList(bindDesktopList);
    }

    @Override
    public void deleteDesktop(UUID desktopId, CbbCloudDeskType deskType) throws BusinessException {
        Assert.notNull(desktopId, "desktopId can not be null");
        Assert.notNull(deskType, "deskType can not be null");

        userService.deleteDesktop(desktopId, deskType);
    }

    @Override
    public void updateDesktopImage(CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO) throws BusinessException {
        Assert.notNull(cbbDesktopImageUpdateDTO, "cbbDesktopImageUpdateDTO must not be null");

        desktopImageService.updateDesktopImage(cbbDesktopImageUpdateDTO);
    }

    @Override
    public boolean doWaitUpdateDesktopImage(DesktopImageUpdateDTO desktopImageUpdateDTO) {
        Assert.notNull(desktopImageUpdateDTO, "desktopImageUpdateDTO must not be null");
        String desktopName = "";
        UUID willApplyImageId = null;
        String imageName = "";
        try {
            CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopImageUpdateDTO.getDesktopId());
            // 不存在变更镜像模板跳过
            if (cbbDeskDTO.getWillApplyImageId() == null) {
                return false;
            }
            desktopName = cbbDeskDTO.getName();
            willApplyImageId = cbbDeskDTO.getWillApplyImageId();
            LOGGER.info("开始更新云桌面[{}]镜像版本，cbbDeskDTO.willApplyImageId：{}", desktopName, willApplyImageId);
            if (cbbDeskDTO.getWillApplyImageId().equals(cbbDeskDTO.getImageTemplateId())) {
                LOGGER.info("云桌面[{}]关联镜像[{}]未发生变更，无需更新", desktopImageUpdateDTO.getDesktopId(), cbbDeskDTO.getImageTemplateId());
                // 清空willApplyImageId
                cbbDeskMgmtAPI.clearWillApplyImageId(desktopImageUpdateDTO.getDesktopId());
                return false;
            }
            boolean isImageTemplateExist = cbbImageTemplateMgmtAPI.checkImageTemplateExist(cbbDeskDTO.getWillApplyImageId());

            checkImageState(cbbDeskDTO, isImageTemplateExist, BusinessKey.RCDC_RCO_DESKTOP_EDIT_IMAGE_NOT_EXIST);
            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(cbbDeskDTO.getWillApplyImageId());
            imageName = imageTemplateDetail.getImageName();
            // 目前支持其他中间态，保存待替换镜像模板，等关机后或者中间态结束后执行以下的方法
            if (checkDeskState(desktopImageUpdateDTO, desktopName, imageName, cbbDeskDTO)) {
                return false;
            }

            checkImageState(cbbDeskDTO, ImageTemplateConstants.IMAGE_CAN_UPDATE_SET.contains(imageTemplateDetail.getImageState()),
                    BusinessKey.RCDC_RCO_DESKTOP_EDIT_IMAGE_STATE_NOT_ALLOW);
            // 如果云桌面是独立配置校验云桌面自己的系统盘大小
            if (Boolean.TRUE.equals(cbbDeskDTO.getEnableCustom())) {
                validateDesktopSystemSize(cbbDeskDTO, imageTemplateDetail);
            } else {
                // 检验策略
                validateDeskStrategy(cbbDeskDTO, imageTemplateDetail);
            }

            // 变更镜像
            updateImage(desktopImageUpdateDTO, cbbDeskDTO);

            saveSystemLog(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_DESKTOP_IMAGE_CHANGE_TASK_LOG_SUC, desktopName,
                    imageTemplateDetail.getImageName()));
            return true;
        } catch (BusinessException e) {
            LOGGER.error("变更云桌面[{}]镜像模板[{}]异常：", desktopImageUpdateDTO.getDesktopId(), willApplyImageId, e);
            boolean isSkipClear = e.getKey().equals(RCDC_DESK_UPDATE_IMAGE_EXIST_SYNC_TASK_EXCEPTION)
                    || e.getKey().equals(RCDC_IMAGE_REPLICATION_SYNC_OVER_MAX_WAIT_NUM);
            if (!isSkipClear) {
                // 异常情况，清空willApplyImageId
                cbbDeskMgmtAPI.clearWillApplyImageId(desktopImageUpdateDTO.getDesktopId());
            }
            saveSystemLog(
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_DESKTOP_IMAGE_CHANGE_TASK_LOG_FAIL, desktopName, imageName, e.getI18nMessage()));
            return false;
        }

    }

    private boolean checkDeskState(DesktopImageUpdateDTO desktopImageUpdateDTO, String desktopName, String imageName, CbbDeskDTO cbbDeskDTO)
            throws BusinessException {
        if (Boolean.TRUE.equals(cbbDeskMgmtAPI.markWillApplyImageInfo(desktopImageUpdateDTO.getDesktopId(), cbbDeskDTO.getWillApplyImageId()))) {
            LOGGER.warn("云桌面[{}]状态[{}]不允许变更", desktopName, cbbDeskDTO.getDeskState());
            String deskStateI18n = LocaleI18nResolver.resolve(BusinessKey.I18N_DESK_STATE_PREFIX + cbbDeskDTO.getDeskState().name().toLowerCase());
            String failMsg = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_DESKTOP_IMAGE_CHANGE_DESK_STATE_NOT_ALLOW, deskStateI18n);
            saveSystemLog(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_DESKTOP_IMAGE_CHANGE_TASK_LOG_FAIL, desktopName, imageName, failMsg));
            return true;
        }
        return false;
    }

    private void updateImage(DesktopImageUpdateDTO desktopImageUpdateDTO, CbbDeskDTO cbbDeskDTO) throws BusinessException {
        CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO = new CbbDesktopImageUpdateDTO();
        cbbDesktopImageUpdateDTO.setDesktopId(desktopImageUpdateDTO.getDesktopId());
        cbbDesktopImageUpdateDTO.setImageId(cbbDeskDTO.getWillApplyImageId());
        // 清空willApplyImageId
        cbbDeskMgmtAPI.clearWillApplyImageId(desktopImageUpdateDTO.getDesktopId());
        updateDesktopImage(cbbDesktopImageUpdateDTO);
    }

    private void checkImageState(CbbDeskDTO cbbDeskDTO, boolean contains, String rcdcRcoDesktopEditImageStateNotAllow) throws BusinessException {
        if (!contains) {
            throw new BusinessException(rcdcRcoDesktopEditImageStateNotAllow, cbbDeskDTO.getName());
        }
    }

    private void validateDeskStrategy(CbbDeskDTO cbbDeskDTO, CbbImageTemplateDetailDTO imageTemplateDetail) throws BusinessException {
        String errorMessage;
        switch (imageTemplateDetail.getCbbImageType()) {
            case VDI:
                errorMessage =
                        cbbVDIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByImageId(cbbDeskDTO.getWillApplyImageId(), cbbDeskDTO.getStrategyId());
                break;
            case IDV:
                errorMessage =
                        cbbIDVDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByImageId(cbbDeskDTO.getWillApplyImageId(), cbbDeskDTO.getStrategyId());
                break;
            case VOI:
                errorMessage =
                        cbbVOIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByImageId(cbbDeskDTO.getWillApplyImageId(), cbbDeskDTO.getStrategyId());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + imageTemplateDetail.getCbbImageType());
        }
        if (StringUtils.isNotBlank(errorMessage)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_EDIT_IMAGE_VALID_NO_PASS, errorMessage);
        }
    }

    private void validateDesktopSystemSize(CbbDeskDTO cbbDeskDTO, CbbImageTemplateDetailDTO imageTemplateDetail) throws BusinessException {
        if (cbbDeskDTO.getSystemSize() < imageTemplateDetail.getSystemDisk()) {
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_DESKTOP_SYSTEM_SIZE_MUST_NOT_SMALLER_THAN_IMAGE_TEMPLATE);
        }
    }

    private void saveSystemLog(String content) {
        BaseSystemLogDTO logDTO = new BaseSystemLogDTO();
        logDTO.setId(UUID.randomUUID());
        logDTO.setContent(content);
        logDTO.setCreateTime(new Date());
        baseSystemLogMgmtAPI.createSystemLog(logDTO);
    }

    @Override
    public List<CloudDesktopDTO> getAllDesktopByStrategyId(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId must not be null");
        List<ViewUserDesktopEntity> bindDesktopList = viewDesktopDetailDAO.findByCbbStrategyIdAndIsDelete(strategyId, false);

        List<CloudDesktopDTO> desktopDTOList = new ArrayList<>(bindDesktopList.size());
        for (ViewUserDesktopEntity desktopEntity : bindDesktopList) {
            CloudDesktopDTO cloudDesktopDTO = ViewUserDesktopEntity.convertEntityToDTO(desktopEntity);
            desktopDTOList.add(cloudDesktopDTO);
        }
        return desktopDTOList;
    }

    @Override
    public List<CloudDesktopDTO> listDesktopByDesktopIds(List<UUID> desktopIdList) {
        Assert.notEmpty(desktopIdList, "desktopIdList must not be empty");
        List<ViewUserDesktopEntity> desktopEntityList = viewDesktopDetailDAO.findAllByCbbDesktopIdIn(desktopIdList);
        List<CloudDesktopDTO> desktopDTOList = new ArrayList<>(desktopEntityList.size());
        for (ViewUserDesktopEntity desktopEntity : desktopEntityList) {
            CloudDesktopDTO cloudDesktopDTO = ViewUserDesktopEntity.convertEntityToDTO(desktopEntity);
            desktopDTOList.add(cloudDesktopDTO);
        }
        return desktopDTOList;
    }

    @Override
    public List<CloudDesktopDTO> listUserDesktopByGroupId(UUID userGroupId, UserCloudDeskTypeEnum desktopType) {
        Assert.notNull(userGroupId, "userGroupId must not be null");
        Assert.notNull(desktopType, "desktopType must not be null");

        List<ViewUserDesktopEntity> userDesktopList = viewDesktopDetailDAO
                .findAllByUserGroupIdInAndDesktopTypeAndIsDelete(Collections.singletonList(userGroupId), desktopType.name(), false);
        return userDesktopList.stream().map(ViewUserDesktopEntity::convertEntityToDTO).collect(Collectors.toList());
    }


    @Override
    public void resetDesktopMac(CbbChangeDeskNicMacDTO cbbChangeDeskNicMacDTO) throws BusinessException {
        Assert.notNull(cbbChangeDeskNicMacDTO, "cbbChangeDeskNicMacDTO must not be null");

        // 调用CBB方法，修改MAC地址
        String newMac = cbbVDIDeskMgmtAPI.changeDeskMac(cbbChangeDeskNicMacDTO);
        // 修改RCO维护的表的MAC地址
        LOGGER.info("云桌面[{}]的MAC地址重置为[{}]", cbbChangeDeskNicMacDTO.getDeskId(), newMac);
        deskFaultInfoService.updateMacByDeskId(cbbChangeDeskNicMacDTO.getDeskId(), newMac);
        LOGGER.info("重置MAC地址同步修改报障表成功");
    }


    @Override
    public DefaultPageResponse<CloudDesktopDTO> desktopFaultPageQuery(PageSearchRequest pageRequest) throws BusinessException {
        Assert.notNull(pageRequest, "pageRequest must not be null");
        buildSearchRequest(pageRequest);
        return pageQuery(pageRequest);
    }

    @Override
    public Long findCountByDeskState(CbbCloudDeskState deskState) {
        Assert.notNull(deskState, "deskState must not be null");
        return cloudDesktopViewService.findCountByDeskState(deskState);
    }

    @Override
    public Long findCount() {
        return cloudDesktopViewService.findCount();
    }

    @Override
    public Long findOnlineDesktopCount() {
        return desktopOnlineLogService.countCurrentOnlineDesktop();
    }

    private void buildSearchRequest(PageSearchRequest pageRequest) {
        MatchEqual stateMatch = new MatchEqual();
        stateMatch.setName(MATCH_EQUAL_FIELD);
        stateMatch.setValueArr(new Object[] {Boolean.TRUE});
        Sort sort = new Sort();
        sort.setSortField(SORT_FIELD);
        sort.setDirection(Sort.Direction.DESC);
        pageRequest.setMatchEqualArr(new MatchEqual[] {stateMatch});
        pageRequest.setSortArr(new Sort[] {sort});
    }

    @Override
    public void unbindCloudDeskFromUser(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        List<UUID> cloudDeskIdList = userDesktopDAO.findAllCloudDeskIdList(userId);
        LOGGER.info("用户[{}]需要解绑的云桌面数量为[{}]", userId, cloudDeskIdList.size());
        cloudDeskIdList.forEach(cloudDeskId -> userDesktopDAO.updateUserIdByDesktopId(cloudDeskId, null));
    }

    @Override
    public void updateDesktopSoftwareStrategy(UUID desktopId, @Nullable UUID softwareStrategyId) throws BusinessException {
        Assert.notNull(desktopId, "Param [desktopId] must not be null");

        RcoDeskInfoEntity rcoDeskInfo = rcoDeskInfoDAO.findByDeskId(desktopId);
        if (rcoDeskInfo == null) {
            rcoDeskInfo = new RcoDeskInfoEntity();
            rcoDeskInfo.setDeskId(desktopId);
        }
        rcoDeskInfo.setSoftwareStrategyId(softwareStrategyId);
        rcoDeskInfoDAO.save(rcoDeskInfo);
        softwareStrategyNotifyAPI
                .notifyAllDesk(Arrays.asList(new DeskInfoSoftwareStrategyDTO(rcoDeskInfo.getDeskId(), rcoDeskInfo.getSoftwareStrategyId())));
    }

    @Override
    public void clearDownloadStateInfo(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        List<String> terminalIdList = userDesktopDAO.findBindTerminalIdList(userId, CbbCloudDeskType.IDV);
        // 如果为空，则证明用户没有绑定终端，不需要清空下载信息
        if (CollectionUtils.isEmpty(terminalIdList)) {
            return;
        }

        for (String terminalId : terminalIdList) {
            if (terminalId == null) {
                continue;
            }
            ImageDownloadStateDTO updateRequest = new ImageDownloadStateDTO();
            updateRequest.setDownloadState(DownloadStateEnum.NONE);
            updateRequest.setTerminalId(terminalId);
            imageDownloadStateService.update(updateRequest);
        }
    }

    @Override
    public List<CloudDesktopDTO> getAllDesktopByUserName(String userName) {
        Assert.notNull(userName, "userName must not be null");
        List<ViewUserDesktopEntity> bindDesktopList = viewDesktopDetailDAO.findByUserNameAndIsDelete(userName, false);
        return getCloudDesktopList(bindDesktopList);
    }

    @Override
    public void requestRemoteAssist(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        remoteAssistService.requestRemoteAssist(deskId);
    }

    @Override
    public void cancelRemoteAssist(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");
        remoteAssistService.cancelRemoteAssist(deskId);
    }

    @Override
    public boolean queryRemoteAssistStatus(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");
        try {
            boolean hasRequest = remoteAssistService.hasRequestRemoteAssist(deskId);
            LOGGER.debug("desk <{}> request remote status <{}>", deskId, hasRequest);
            return hasRequest;
        } catch (Exception e) {
            LOGGER.error("查询桌面远程状态异常:桌面Id:{}", deskId, e);
        }
        return false;
    }

    @Override
    public void desktopIsRobbed(UUID deskId, String currentTerminalId) {
        Assert.notNull(deskId, "deskId can not null");
        Assert.notNull(currentTerminalId, "currentTerminalId can not null");
        DesktopRequestDTO desktopRequestDTO = Optional.ofNullable(desktopOperateRequestCache.getCache(deskId)).orElse(new DesktopRequestDTO());
        try {
            CloudDesktopDetailDTO desktopDetailDTO = this.getDesktopDetailById(deskId);
            // 判断云桌面原来是否在其他终端运行，是则发送桌面被抢占消息
            String lastLoginTerminalId = desktopDetailDTO.getTerminalId();
            if (StringUtils.isBlank(lastLoginTerminalId)) {
                // 为空时可能是网页客户端端互抢
                UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(deskId);
                desktopOperateRequestCache.addCache(deskId, desktopRequestDTO);
                // 网页客户端强登后清空终端ID
                handUserDesktopWhenWebClientLogin(deskId);
                LOGGER.info("桌面[{}]最近登录终端为空，不发送抢占通知", deskId);
                return;
            }
            if (Objects.equals(lastLoginTerminalId, currentTerminalId)) {
                desktopOperateRequestCache.addCache(deskId, desktopRequestDTO);
                // 网页客户端强登后清空终端ID
                handUserDesktopWhenWebClientLogin(deskId);
                LOGGER.info("桌面[{}]登录终端[{}]与最近登录终端[{}]一致，不发送抢占通知", deskId, currentTerminalId, lastLoginTerminalId);
                return;
            }
            LOGGER.info("抢占登录桌面,terminalId={},desktopId={}", currentTerminalId, deskId);
            RepeatStartVmTerminalDTO repeatStartVmTerminalDTO = new RepeatStartVmTerminalDTO();
            // 网页客户端IP,MAC,终端名称暂时不做处理
            String userName = desktopDetailDTO.getUserName();
            repeatStartVmTerminalDTO.setUserName(userName);
            repeatStartVmTerminalDTO.setStartVmTime(Instant.now().toEpochMilli());
            repeatStartVmTerminalDTO.setDesktopName(desktopDetailDTO.getDesktopName());
            repeatStartVmTerminalDTO.setDesktopId(deskId);
            LOGGER.info("桌面[{}]被抢占发送抢占消息给终端[{}]，登录用户={}", deskId, lastLoginTerminalId, userName);
            desktopOperateRequestCache.addCache(deskId, desktopRequestDTO);

            // 网页客户端强登后清空终端ID
            handUserDesktopWhenWebClientLogin(deskId);
            shineMessageHandler.requestContent(lastLoginTerminalId, ShineAction.REPEAT_START_VM_IN_DIFFERENT_PLACE, repeatStartVmTerminalDTO);
        } catch (Exception e) {
            LOGGER.error("云桌面[{}]抢占出现异常", deskId, e);
        } finally {
            final CountDownLatch latch = desktopRequestDTO.getLatch();
            latch.countDown();
        }
        // 更新缓存信息，桌面运行在终端上
        desktopRequestDTO.setDesktopRunInTerminal(true);
        desktopOperateRequestCache.addCache(deskId, desktopRequestDTO);
    }

    private void handUserDesktopWhenWebClientLogin(UUID deskId) {
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(deskId);
        if (Objects.isNull(userDesktopEntity)) {
            return;
        }
        userDesktopEntity.setTerminalId(null);
        userDesktopEntity.setHasTerminalRunning(true);
        userDesktopEntity.setLatestLoginTime(new Date());
        userDesktopDAO.save(userDesktopEntity);
    }

    @Override
    public List<CloudDesktopDTO> getAllDesktopByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");

        List<ViewUserDesktopEntity> bindDesktopList = viewDesktopDetailDAO.findByDesktopPoolIdAndIsDelete(desktopPoolId, false);
        List<CloudDesktopDTO> desktopDTOList = new ArrayList<>(bindDesktopList.size());
        for (ViewUserDesktopEntity desktopEntity : bindDesktopList) {
            CloudDesktopDTO cloudDesktopDTO = ViewUserDesktopEntity.convertEntityToDTO(desktopEntity);
            desktopDTOList.add(cloudDesktopDTO);
        }
        return desktopDTOList;
    }

    @Override
    public void desktopBindUser(UserDesktopBindUserRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(request.getDesktopId());
        request.setDesktopName(cbbDeskDTO.getName());

        // 只有关机状态支持关联用户
        if (cbbDeskDTO.getDeskType() == CbbCloudDeskType.VDI && CbbCloudDeskState.CLOSE != cbbDeskDTO.getDeskState()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_BIND_USER_DESKTOP_MUST_CLOSE, cbbDeskDTO.getName());
        }

        if (DesktopPoolType.isPoolDesktop(cbbDeskDTO.getDesktopPoolType())) {
            desktopPoolUserService.poolDesktopBindUser(request);
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(request.getUserId());
            // 异步通知oa
            THREAD_EXECUTOR.execute(() -> {
                if (cbbDeskDTO.getDeskType() == CbbCloudDeskType.THIRD && cbbDeskDTO.getSessionType() == CbbDesktopSessionType.SINGLE) {
                    DesktopBindUserDTO desktopBindUserDTO = new DesktopBindUserDTO();
                    desktopBindUserDTO.setUserId(request.getUserId());
                    desktopBindUserDTO.setUserName(userDetail.getUserName());
                    LOGGER.info("通知OA主机[{}]用户[{}]绑定", request.getDesktopId(), request.getUserId());
                    desktopUserOpTcpAPI.notifyOaDeskBindUser(request.getDesktopId().toString(), desktopBindUserDTO);
                }
            });
        } else {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_BIND_USER_ERROR_NOT_STATIC_POOL_DESK, cbbDeskDTO.getName());
        }
    }

    @Override
    public Boolean getDeskEnableFullSystemDiskByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");

        return Optional.ofNullable(userDesktopDAO.findByCbbDesktopId(deskId)).map(UserDesktopEntity::getEnableFullSystemDisk).orElse(Boolean.FALSE);
    }

    @Override
    public void updateDesktopUserProfileStrategy(UUID desktopId, @Nullable UUID userProfileStrategyId) throws BusinessException {
        Assert.notNull(desktopId, "Param [desktopId] must not be null");

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopId);

        if (Objects.nonNull(userProfileStrategyId)) {
            if (cbbDeskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                userProfileValidateAPI.validateStorageMustFileServer(userProfileStrategyId);
            }
            if (cbbDeskDTO.getSessionType() == CbbDesktopSessionType.SINGLE && cbbDeskDTO.getDeskType() == CbbCloudDeskType.VDI) {
                userProfileValidateAPI.validateUserProfileStrategyMustStoragePersonal(userProfileStrategyId);
            }
            if (cbbDeskDTO.getDeskType() == CbbCloudDeskType.IDV || cbbDeskDTO.getDeskType() == CbbCloudDeskType.VOI) {
                userProfileValidateAPI.validateUserProfileStrategyMustStorageLocal(userProfileStrategyId);
            }
        }

        RcoDeskInfoEntity rcoDeskInfo = rcoDeskInfoDAO.findByDeskId(desktopId);
        if (rcoDeskInfo == null) {
            rcoDeskInfo = new RcoDeskInfoEntity();
            rcoDeskInfo.setDeskId(desktopId);
            rcoDeskInfo.setCreateTime(new Date());
        }
        rcoDeskInfo.setUserProfileStrategyId(userProfileStrategyId);
        rcoDeskInfo.setUpdateTime(new Date());
        rcoDeskInfoDAO.save(rcoDeskInfo);
        // 通知云桌面,策略变更
        userProfileStrategyNotifyAPI.updateDesktopUserProfileStrategy(desktopId);
    }

    @Override
    public GuestToolForDiskStateTypeEnum checkDesktopWithPersonalDiskState(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId must be not null");

        UUID userId = userDesktopDAO.findUserIdByCbbDesktopId(desktopId);
        if (userId == null) {
            LOGGER.info("云桌面[{}]未绑定用户，此时无法判断是否关联磁盘，直接略过，进入下一层校验", desktopId);
            return GuestToolForDiskStateTypeEnum.IGNORE;
        }

        List<UUID> diskIdList = userDiskDAO.findDiskIdByUserId(userId);
        if (CollectionUtils.isEmpty(diskIdList)) {
            return GuestToolForDiskStateTypeEnum.WITHOUT;
        }


        int banDiskNum = 0;
        List<CbbDeskDiskDTO> cbbDeskDiskDTOList = cbbVDIDeskDiskAPI.listDiskDetail(diskIdList);
        for (CbbDeskDiskDTO cbbDeskDiskDTO : cbbDeskDiskDTOList) {
            UUID diskDesktopId = cbbDeskDiskDTO.getDeskId();
            GuestToolForDiskStateTypeEnum result = checkUserDiskStatus(desktopId, cbbDeskDiskDTO);
            if (result != null) {
                return result;
            }
            if (Objects.nonNull(diskDesktopId)) {
                if (Objects.equals(desktopId, diskDesktopId)) {
                    // 该磁盘已经挂载上目标云桌面
                    LOGGER.info("该磁盘[{}]已经挂载上目标云桌面[{}]，忽略", desktopId, desktopId);
                    return GuestToolForDiskStateTypeEnum.IGNORE;
                }

                LOGGER.debug("磁盘[{}]已被云桌面[{}]绑定,当前云桌面[{}]无法挂载", cbbDeskDiskDTO.getId(), diskDesktopId, desktopId);
                continue;
            }

            // 有磁盘还处于空置状态，在云桌面用户已经登录的情况下，视为不可用，略过
            banDiskNum++;
        }

        // 全部磁盘不可用
        if (banDiskNum == diskIdList.size()) {
            return GuestToolForDiskStateTypeEnum.BAN;
        }

        return GuestToolForDiskStateTypeEnum.WITHOUT;
    }

    private GuestToolForDiskStateTypeEnum checkUserDiskStatus(UUID desktopId, CbbDeskDiskDTO cbbDeskDiskDTO) {
        ViewUserDesktopEntity userDesktopEntity = viewDesktopDetailDAO.findByCbbDesktopId(desktopId);
        if (storagePoolServerMgmtAPI.getStoragePoolByComputeClusterId(userDesktopEntity.getClusterId()).stream()
                .noneMatch(dto -> Objects.equals(cbbDeskDiskDTO.getAssignStoragePoolIds(), dto.getId().toString()))) {
            LOGGER.warn("磁盘[{}]与桌面[{}]所处的计算集群不一致，不支持桌面磁盘挂载", cbbDeskDiskDTO.getId(), userDesktopEntity.getDesktopName());
            return GuestToolForDiskStateTypeEnum.FAIL;
        }
        // 没有找到数据
        return null;
    }

    @Override
    public List<UUID> findAllDesktopId() {
        return viewDesktopDetailDAO.findCbbDesktopIdByIsDelete(false);
    }

    @Override
    public List<UUID> findAllIDVDesktopId() {
        return viewDesktopDetailDAO.findCbbDesktopIdByIsDeleteAndByDesktopType(false, UserCloudDeskTypeEnum.IDV.name());
    }

    @Override
    public void compensateConnectClosedTime(CompensateSessionType compensateSessionType) {
        Assert.notNull(compensateSessionType, "compensateSessionType must not be null");

        List<CbbDesktopPoolModel> poolModelList = Lists.newArrayList(CbbDesktopPoolModel.DYNAMIC);
        List<DesktopPoolBasicDTO> desktopPoolList = desktopPoolService.listDesktopPoolByPoolModel(poolModelList);
        if (CollectionUtils.isEmpty(desktopPoolList)) {
            return;
        }

        List<UUID> poolIdList = desktopPoolList.stream().map(DesktopPoolBasicDTO::getId).collect(Collectors.toList());
        List<PoolDesktopInfoDTO> desktopList = desktopPoolService.listDesktopByDesktopPoolIds(poolIdList);
        if (CollectionUtils.isEmpty(desktopList)) {
            return;
        }
        for (PoolDesktopInfoDTO desktop : desktopList) {
            THREAD_POOL.execute(() -> {
                if (filterBindAndRunningDesktop(desktop, compensateSessionType)) {
                    checkDesktopConnect(desktop);
                }
            });
        }
    }

    @Override
    public Integer countByCloudDesktop(CountCloudDesktopDTO countCloudDesktopDTO) {
        Assert.notNull(countCloudDesktopDTO, "countCloudDesktopDTO must not be null");
        return cloudDesktopViewService.countByCloudDesktop(countCloudDesktopDTO);
    }

    private boolean filterBindAndRunningDesktop(PoolDesktopInfoDTO desktop, CompensateSessionType compensateSessionType) {
        if (desktop.getIsDelete()) {
            // 已删除的桌面，不检查
            return false;
        }

        CbbCloudDeskState deskState = null;
        // // 获取云桌面状态，启动时从RCCP获取，定时任务直接查表
        if (compensateSessionType == CompensateSessionType.START_UP) {
            try {
                cbbVDIDeskMgmtAPI.queryDeskState(desktop.getDeskId());
            } catch (Exception e) {
                LOGGER.error("查询云桌面[" + desktop.getDeskId() + "]云桌面状态失败，失败原因：", e);
            }
        } else {
            deskState = desktop.getDeskState();
        }
        return Objects.nonNull(desktop.getUserId()) && deskState == CbbCloudDeskState.RUNNING;
    }

    private void checkDesktopConnect(PoolDesktopInfoDTO desktop) {
        UUID desktopId = desktop.getDeskId();
        try {
            if (desktopAPI.isAnyConnectedChannel(desktopId)) {
                LOGGER.info("桌面[{}]存在未断开的est连接不标记断连时间", desktopId);
                return;
            }
            UserDesktopEntity userDesktopEntity = userDesktopService.findByDeskId(desktopId);
            if (Objects.nonNull(userDesktopEntity.getConnectClosedTime()) || Objects.isNull(userDesktopEntity.getUserId())) {
                return;
            }
            userDesktopService.setConnectClosedTime(desktopId, new Date());
        } catch (Exception e) {
            LOGGER.error("桌面[{}]查询qemu信息异常", desktopId, e);
        }
    }

    @Override
    public void updateState(UUID deskId, CbbCloudDeskState state) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(state, "state must not be null");

        CbbUpdateDeskStateRequest stateRequest = new CbbUpdateDeskStateRequest();
        stateRequest.setDeskId(deskId);
        stateRequest.setState(state);

        cbbDeskMgmtAPI.updateState(stateRequest);
    }

    @Override
    public VmGraphicsDTO queryVncByDeskId(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        DtoResponse<VmGraphicsDTO> response = vmMgmtAPI.queryVncById(new VmIdRequest(cbbDeskMgmtAPI.getDeskById(deskId).getPlatformId(), deskId));

        return response.getDto();
    }

    @Override
    public CloudDesktopDTO findByTerminalId(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "Param [terminalId] must not be null or empty");
        ViewUserDesktopEntity entity = viewDesktopDetailDAO.findByTerminalId(terminalId);
        return Optional.ofNullable(entity).map(ViewUserDesktopEntity::convertEntityToDTO).orElseThrow(() -> {
            String terminalBindDesktopNotExist = com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_TERMINAL_BIND_DESKTOP_NOT_EXIST;
            return new BusinessException(terminalBindDesktopNotExist, terminalId);
        });

    }

    @Override
    public List<DeskImageRelatedDTO> findByDeskIdIn(List<UUID> deskIdList) {
        Assert.notEmpty(deskIdList, "Param [deskIdList] must not be empty");
        List<DeskImageRelatedDTO> deskImageRelatedDTOList = new ArrayList<>();
        List<ViewDesktopImageRelatedEntity> desktopImageRelatedEntityList = viewDesktopImageRelatedDAO.findByDeskIdIn(deskIdList);
        desktopImageRelatedEntityList.forEach(viewDesktopImageRelatedEntity -> {
            DeskImageRelatedDTO deskImageRelatedDTO = new DeskImageRelatedDTO();
            BeanUtils.copyProperties(viewDesktopImageRelatedEntity, deskImageRelatedDTO);
            deskImageRelatedDTOList.add(deskImageRelatedDTO);
        });
        return deskImageRelatedDTOList;
    }

    @Override
    public DefaultPageResponse<CloudDesktopDTO> pageQuery(PageSearchRequest request, QueryPlatformTypeEnum queryPlatformType)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(queryPlatformType, "queryPlatformType must not be null");
        // 后续根据不同的queryPlatformType获取queryService
        Page<ViewUserDesktopEntity> page = rccmCloudDesktopViewService.pageQuery(request, ViewUserDesktopEntity.class);
        return queryCloudDesktopService.convertPageInfoAndQuery(page);
    }

    @Override
    public List<CloudDesktopDTO> listByConditions(ConditionQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        List<ViewUserDesktopEntity> viewUserDesktopEntityList = viewDesktopDetailDAO.listByConditions(request);
        return viewUserDesktopEntityList.stream() //
                .map(viewUserDesktopEntity -> ViewUserDesktopEntity.convertEntityToDTO(viewUserDesktopEntity)) //
                .collect(Collectors.toList());
    }

    @Override
    public long countByConditions(ConditionQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        return viewDesktopDetailDAO.countByConditions(request);
    }


    @Override
    public boolean isAllowDesktopLogin(UUID strategyId, String desktopName, Boolean isPool, String deskType) throws BusinessException {
        Assert.notNull(strategyId, "strategyId must not be null");
        Assert.hasText(desktopName, "desktopName must not be empty");
        Assert.hasText(deskType, "deskType must not be empty");
        List<CbbDeskTopAllowLoginTimeDTO> allowLoginTimeDTOList = new ArrayList<>();
        if (Objects.equals(deskType, CbbCloudDeskType.VDI.name())) {
            CbbDeskStrategyDTO deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyId);
            allowLoginTimeDTOList = deskStrategyDTO.getDesktopAllowLoginTimeList();
        } else {
            CbbDeskStrategyThirdPartyDTO deskStrategyThirdParty = cbbThirdPartyDeskStrategyMgmtAPI.getDeskStrategyThirdParty(strategyId);
            // 桌面登录时间范围
            if (deskStrategyThirdParty.getDesktopAllowLoginTimeInfo() != null) {
                allowLoginTimeDTOList = JSON.parseArray(deskStrategyThirdParty.getDesktopAllowLoginTimeInfo(), CbbDeskTopAllowLoginTimeDTO.class);
            }
        }
        if (!CollectionUtils.isEmpty(allowLoginTimeDTOList)) {
            for (CbbDeskTopAllowLoginTimeDTO cbbDeskTopAllowLoginTimeDTO : allowLoginTimeDTOList) {
                if (cbbDeskTopAllowLoginTimeDTO.getWeekArr() != null) {
                    LocalTime now = LocalTime.now();
                    LocalTime startTime = DateUtil.getLocalTimeByHMS(cbbDeskTopAllowLoginTimeDTO.getStartTime());
                    LocalTime endTime = DateUtil.getLocalTimeByHMS(cbbDeskTopAllowLoginTimeDTO.getEndTime());
                    DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
                    boolean exist = Arrays.stream(cbbDeskTopAllowLoginTimeDTO.getWeekArr()).anyMatch(s -> s == dayOfWeek.getValue());
                    if (exist && now.isAfter(startTime) && now.isBefore(endTime)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public RcoUserDesktopDTO findByDesktopId(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId must not be null");
        final UserDesktopEntity desktopEntity = userDesktopService.findByDeskId(desktopId);
        return Optional.ofNullable(desktopEntity).map(e -> {
            RcoUserDesktopDTO userDesktopDTO = new RcoUserDesktopDTO();
            BeanUtils.copyProperties(e, userDesktopDTO);
            return userDesktopDTO;
        }).orElseThrow(() -> new BusinessException(BusinessKey.RCDC_RCO_WATERMARK_DESKTOP_NOT_EXIST, String.valueOf(desktopId)));
    }


    @Override
    public List<CloudDesktopDTO> findByUserId(UUID userId) {
        Assert.notNull(userId, "userId is not null");

        List<UserDesktopEntity> entityList = userDesktopService.findByUserId(userId);
        if (CollectionUtils.isEmpty(entityList)) {
            return new ArrayList<>();
        }

        List<CloudDesktopDTO> dtoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<>();
        }
        for (int i = 0; i < entityList.size(); i++) {
            BeanUtils.copyProperties(entityList.get(i), dtoList.get(i));
        }
        return dtoList;
    }

    @Override
    public void desktopUnBindUser(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId is not null");
        CloudDesktopDetailDTO desktopDetailDTO = queryCloudDesktopService.queryDeskDetail(desktopId);
        // 只有关机状态支持关联用户
        if (Objects.equals(desktopDetailDTO.getDesktopPoolType(), CbbDesktopPoolType.VDI.name())
                && !Objects.equals(CbbCloudDeskState.CLOSE.name(), desktopDetailDTO.getDesktopState())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_UNBIND_USER_DESKTOP_MUST_CLOSE, desktopDetailDTO.getDesktopName());
        }

        if (CbbDesktopPoolType.THIRD.name().equals(desktopDetailDTO.getDesktopCategory())) {
            List<DesktopSessionDTO> desktopSessionDTOList = desktopSessionServiceAPI.findByDeskId(desktopId);
            if (!CollectionUtils.isEmpty(desktopSessionDTOList)) {
                throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_HAS_SESSION_NOT_CANCEL);
            }
        }

        if (Objects.equals(DesktopPoolType.STATIC.name(), desktopDetailDTO.getDesktopPoolType())) {
            desktopPoolServiceTx.unbindUserAndDisableDesktop(desktopId);
            // 异步通知oa
            THREAD_EXECUTOR.execute(() -> {
                LOGGER.info("通知OA解除主机[{}]用户绑定", desktopId);
                desktopUserOpTcpAPI.notifyOaDeskUnbindUser(desktopId.toString());
            });
        } else {
            // 非静态池桌面不支持取消关联
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_UNBIND_USER_ERROR_NOT_STATIC_POOL_DESK, desktopDetailDTO.getDesktopName());
        }
    }

    @Override
    public List<CloudDesktopDTO> listByDeskIdsOrPoolIdsOrUserIdsOrUserGroupIdsAndPlatformId(List<UUID> deskIdList, List<UUID> deskPoolIdList,
            List<UUID> userIdList, List<UUID> userGroupIdList, UUID platformId) {
        Assert.notNull(deskIdList, "deskIdList can not be null");
        Assert.notNull(deskPoolIdList, "deskPoolIdList can not be null");
        Assert.notNull(userIdList, "userIdList can not be null");
        Assert.notNull(userGroupIdList, "userGroupIdList can not be null");
        Assert.notNull(platformId, "platformId can not be null");
        List<ViewUserDesktopEntity> viewUserDesktopEntityList =
                viewDesktopDetailDAO.findByPlatformIdAndCbbDesktopIdInOrDesktopPoolIdInOrUserIdInOrUserGroupIdIn(platformId, deskIdList,
                        deskPoolIdList, userIdList, userGroupIdList);
        return viewUserDesktopEntityList.stream() //
                .map(viewUserDesktopEntity -> ViewUserDesktopEntity.convertEntityToDTO(viewUserDesktopEntity)) //
                .collect(Collectors.toList());
    }

    @Override
    public void editDeskRootPwdConfig(CbbEditDeskPwdConfigDTO request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        LOGGER.info("修改云桌面[{}]密码限制配置为[{}]", request.getDeskId(), JSON.toJSONString(request));
        CloudDesktopDetailDTO deskInfo = this.getDesktopDetailById(request.getDeskId());
        String userType = deskInfo.getUserType();
        CbbOsType osType = deskInfo.getDesktopImageType();
        String terminalModel = deskInfo.getIdvTerminalModel();
        // 只有UOS、KYLIN的公共或访客桌面才能编辑
        if (!isOsSupport(osType) || !(IdvTerminalModeEnums.PUBLIC.name().equals(terminalModel) || IacUserTypeEnum.VISITOR.name().equals(userType))) {
            LOGGER.error("桌面[{}]用户类型为[{}]部署模式为[{}]操作系统为[{}]，不允许编辑云桌面密码显示配置", request.getDeskId(), userType, terminalModel, osType);
            throw new BusinessException(BusinessKey.RCDC_RCO_DESK_NOT_ALLOW_EDIT_ROOT_PWD_CONFIG);
        }

        cbbDeskMgmtAPI.editDeskRootPwdConfig(request);
    }

    @Override
    public DeskSpecDetailDTO getDesktopSpecDetail(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        ViewUserDesktopEntity deskInfo = viewDesktopDetailDAO.findByCbbDesktopId(deskId);
        if (Objects.isNull(deskInfo)) {
            return new DeskSpecDetailDTO();
        }
        DeskSpecDetailDTO specDetailDTO = new DeskSpecDetailDTO();
        specDetailDTO.setId(deskInfo.getId());
        specDetailDTO.setCpu(deskInfo.getCpu());
        if (Objects.nonNull(deskInfo.getMemory())) {
            specDetailDTO.setMemory(CapacityUnitUtils.mb2GbMaintainOneFraction(deskInfo.getMemory()));
        }
        specDetailDTO.setEnableHyperVisorImprove(deskInfo.getEnableHyperVisorImprove());
        if (Objects.equals(deskInfo.getPattern(), CbbCloudDeskPattern.APP_LAYER.name())) {
            // 应用分发系统盘+15
            specDetailDTO.setSystemDisk(deskInfo.getSystemSize() + Constants.SYSTEM_DISK_CAPACITY_INCREASE_SIZE);
        } else {
            specDetailDTO.setSystemDisk(deskInfo.getSystemSize());
        }
        specDetailDTO.setPersonalDisk(deskInfo.getPersonSize());
        specDetailDTO.setVgpuType(deskInfo.getVgpuType());
        if (StringUtils.isBlank(deskInfo.getVgpuExtraInfo())) {
            specDetailDTO.setVgpuExtraInfo(new VgpuExtraInfo());
        } else {
            specDetailDTO.setVgpuExtraInfo(JSON.parseObject(deskInfo.getVgpuExtraInfo(), VgpuExtraInfo.class));
        }

        List<CbbDeskDiskDTO> deskDiskList = cbbVDIDeskDiskAPI.listDeskDisk(deskId);
        if (CollectionUtils.isEmpty(deskDiskList)) {
            return specDetailDTO;
        }
        Map<UUID, PlatformStoragePoolDTO> storagePoolMap = storagePoolAPI.queryAllStoragePool().stream()
                .collect(Collectors.toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Optional<CbbDeskDiskDTO> optionalDisk = deskDiskList.stream().filter(diskDTO -> diskDTO.getType() == CbbDiskType.SYSTEM).findFirst();
        if (optionalDisk.isPresent()) {
            CbbDeskDiskDTO diskDTO = optionalDisk.get();
            specDetailDTO.setSystemDiskStoragePoolId(UUID.fromString(diskDTO.getAssignStoragePoolIds()));
            specDetailDTO.setSystemDiskStoragePool(getAssignStoragePool(specDetailDTO.getSystemDiskStoragePoolId(), storagePoolMap));
        }
        optionalDisk = deskDiskList.stream().filter(diskDTO -> diskDTO.getType() == CbbDiskType.PERSONAL).findFirst();
        if (optionalDisk.isPresent()) {
            CbbDeskDiskDTO diskDTO = optionalDisk.get();
            specDetailDTO.setPersonDiskStoragePoolId(UUID.fromString(diskDTO.getAssignStoragePoolIds()));
            specDetailDTO.setPersonDiskStoragePool(getAssignStoragePool(specDetailDTO.getPersonDiskStoragePoolId(), storagePoolMap));
        }

        List<CbbDeskDiskDTO> cbbExtraDiskList = deskDiskList.stream().filter(diskDTO -> diskDTO.getType() == CbbDiskType.USER_EXTRA)
                .sorted(Comparator.comparing(CbbDeskDiskDTO::getCreateTime)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cbbExtraDiskList)) {
            return specDetailDTO;
        }
        List<ExtraDiskDTO> extraDiskDTOList = new ArrayList<>();
        UUID extraDiskStoragePoolId;
        for (int i = 0; i < cbbExtraDiskList.size(); i++) {
            CbbDeskDiskDTO diskDTO = cbbExtraDiskList.get(i);
            ExtraDiskDTO extraDiskDTO = new ExtraDiskDTO();
            extraDiskDTO.setIndex(i);
            extraDiskDTO.setDiskId(diskDTO.getId());
            extraDiskDTO.setExtraSize(diskDTO.getCapacity());
            extraDiskStoragePoolId = UUID.fromString(diskDTO.getAssignStoragePoolIds());
            extraDiskDTO.setAssignedStoragePoolId(extraDiskStoragePoolId);
            extraDiskDTO.setExtraDiskStoragePool(getAssignStoragePool(extraDiskStoragePoolId, storagePoolMap));
            extraDiskDTOList.add(extraDiskDTO);
        }
        specDetailDTO.setExtraDiskArr(extraDiskDTOList.toArray(new ExtraDiskDTO[0]));
        return specDetailDTO;
    }

    @Override
    public void updateDeskStrategyThirdParty(EditDesktopStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "Param [request] must not be null");

        LOGGER.info("执行更新ThirdParty云桌面策略API，云桌面id[{}]，策略id[{}]", request.getId(), request.getStrategyId());
        cbbDeskMgmtAPI.updateStrategyThirdParty(request.getId(), request.getStrategyId());
    }

    @Override
    public void deleteDesktopThirdParty(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "Param desktopId must not be null");
        int count = desktopSessionServiceAPI.countByDeskId(deskId);
        if (count > 0) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EXIST_SESSION_NOT_ALLOW_DELETE);
        }
        cbbDeskMgmtAPI.deleteDesktopThirdParty(deskId);
    }

    @Override
    public String getImageUsageByDeskId(@Nullable UUID deskId) throws BusinessException {
        if (deskId == null) {
            LOGGER.warn("根据deskId={}获取桌面类型异常，deskId为空，默认返回云桌面类型");
            return Constants.CLOUD_DESKTOP;
        }
        CloudDesktopDetailDTO desktopDetailDTO = getDesktopDetailById(deskId);
        String desktopType = Constants.CLOUD_DESKTOP;
        if (desktopDetailDTO.getImageUsage() == ImageUsageTypeEnum.APP || desktopDetailDTO.getImageUsage() == ImageUsageTypeEnum.CLOUD_DOCK) {
            desktopType = Constants.APP_CLOUD_DESKTOP;
        }
        return desktopType;
    }

    @Override
    public void deleteDeskFromDb(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        // 删除桌面关联用户消息
        deleteDesktopRelativeUserMessage(deskId);
        // 删除桌面关联的应用ISO挂载信息
        deleteDesktopAppConfig(deskId);

        cbbVDIDeskMgmtAPI.deleteDeskFromDb(deskId);
    }


    @Override
    public void deleteDesktopAppConfig(UUID desktopId) {
        Assert.notNull(desktopId, "deskId must not be null");
        LOGGER.info("删除桌面[{}]关联的应用ISO挂载信息", desktopId);
        cloudDeskAppConfigDAO.deleteByDeskIdAndAppType(desktopId, AppTypeEnum.CMLAUNCHER);
        cloudDeskAppConfigDAO.deleteByDeskIdAndAppType(desktopId, AppTypeEnum.UWSLAUNCHER);
    }

    private boolean isOsSupport(CbbOsType osType) {
        return osType == CbbOsType.UOS_64 || osType == CbbOsType.KYLIN_64 || osType == CbbOsType.NEOKYLIN_64;
    }

    private IdLabelEntry getAssignStoragePool(UUID storagePoolId, Map<UUID, PlatformStoragePoolDTO> storagePoolMap) {
        IdLabelEntry assignStoragePool = new IdLabelEntry();

        PlatformStoragePoolDTO storagePoolDTO = storagePoolMap.get(storagePoolId);
        if (Objects.nonNull(storagePoolDTO)) {
            assignStoragePool.setId(storagePoolDTO.getId());
            assignStoragePool.setLabel(storagePoolDTO.getName());
        }
        return assignStoragePool;
    }

    @Override
    public List<CloudDesktopDTO> findAllByImageTemplateId(UUID imageTemplateId) {
        Assert.notNull(imageTemplateId, "imageTemplateId can not be null");

        List<ViewUserDesktopEntity> bindDesktopList = viewDesktopDetailDAO.findAllByImageTemplateId(imageTemplateId);
        return getCloudDesktopList(bindDesktopList);
    }

    private List<CloudDesktopDTO> getCloudDesktopList(List<ViewUserDesktopEntity> bindDesktopList) {
        List<CloudDesktopDTO> desktopDTOList = new ArrayList<>(bindDesktopList.size());
        for (ViewUserDesktopEntity desktopEntity : bindDesktopList) {
            CloudDesktopDTO cloudDesktopDTO = ViewUserDesktopEntity.convertEntityToDTO(desktopEntity);
            if (!Objects.equals(cloudDesktopDTO.getDesktopCategory(), CbbCloudDeskType.THIRD.name())) {
                CbbOsType deskImageType =
                        getDeskImageType(desktopEntity.getDeskCreateMode(), desktopEntity.getImageTemplateId(), desktopEntity.getOsType());
                cloudDesktopDTO.setDesktopImageType(deskImageType);
            }
            desktopDTOList.add(cloudDesktopDTO);
        }
        return desktopDTOList;
    }


    /**
     * 获得镜像系统类型
     *
     * @param deskCreateMode 创建模式
     * @param imageTemplateId 镜像id
     * @param osType 系统类型
     * @return 返回值
     * @Date 2021/11/30 19:21
     * @Author zjy
     **/
    private CbbOsType getDeskImageType(String deskCreateMode, UUID imageTemplateId, String osType) {
        if (DeskCreateMode.FULL_CLONE == DeskCreateMode.valueOf(deskCreateMode)) {
            return CbbOsType.valueOf(osType);
        } else {
            CbbImageTemplateDetailDTO imageDto = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
            if (imageDto == null) {
                // 匹配不上返回null
                return null;
            }
            return imageDto.getOsType();
        }
    }


    private void deleteDesktopRelativeUserMessage(UUID desktopId) {
        LOGGER.info("删除桌面[{}]关联用户消息", desktopId);
        List<UserMessageUserEntity> messageUserList = userMessageUserDAO.findByDesktopId(desktopId);
        messageUserList.forEach(messageUser -> userMessageUserDAO.deleteById(messageUser.getId()));
    }

    private void fillPlatformInfo(UUID platformId, CloudDesktopDTO cloudDesktopDTO) {
        try {
            CloudPlatformDTO cloudPlatformDTO = cloudPlatformManageAPI.getInfoById(platformId);
            cloudDesktopDTO.setPlatformId(platformId);
            cloudDesktopDTO.setPlatformName(cloudPlatformDTO.getName());
            cloudDesktopDTO.setPlatformStatus(cloudPlatformDTO.getStatus());
            cloudDesktopDTO.setPlatformType(cloudPlatformDTO.getType());
        } catch (Throwable e) {
            LOGGER.error("获取云平台[{}]信息失败", platformId, e);
        }

    }
}
