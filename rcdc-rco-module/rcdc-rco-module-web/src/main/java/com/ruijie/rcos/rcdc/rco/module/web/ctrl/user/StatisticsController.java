package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.SystemInfoAPI;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryCpuInfoResponse;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryCurrentNetworkSpeedResponse;
import com.ruijie.rcos.base.alarm.module.def.api.response.QuerySystemInfoResponse;
import com.ruijie.rcos.base.alarm.module.def.dto.CpuDTO;
import com.ruijie.rcos.base.alarm.module.def.dto.DiskDTO;
import com.ruijie.rcos.base.alarm.module.def.dto.MemoryDTO;
import com.ruijie.rcos.gss.base.iac.module.dto.IacLoginUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOsFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateServerCpuDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateCpuInstallState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ClusterMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cluster.ClusterNodesInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ServerModel;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ClusterServerTrendDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.LicenseTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskTypeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DesktopStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ObtainRcdcLicenseNumResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.DeskActiveStatisticsItemDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.StatisticsResourceUseDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.BandwidthSpeedResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalStatisticsDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 首页统计
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/4
 *
 * @author Jarman
 */
@Controller
@RequestMapping("/rco/user")
public class StatisticsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsAPI statisticsAPI;

    @Autowired
    private SystemInfoAPI systemInfoAPI;

    @Autowired
    private ClusterServerStatisticsAPI clusterServerStatisticsAPI;

    @Autowired
    private LicenseAPI licenseAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private CbbClusterServerMgmtAPI cbbClusterServerMgmtAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;
    
    @Autowired
    private ClusterMgmtAPI clusterMgmtAPI;

    @Autowired
    private CbbOsFileMgmtAPI cbbOsFileMgmtAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    /**
     * 统计桌面数据
     *
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 返回统计结果数据
     *
     * @throws BusinessException 业务异常
     *
     */
    @RequestMapping("statisticsDesktop")
    public DefaultWebResponse statisticsDesktop(DefaultWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        DeskTypeRequest deskTypeRequest = new DeskTypeRequest();
        deskTypeRequest.setDesktopType(CbbCloudDeskType.VDI);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<UUID> userGroupIdList = fetchUserGroupIdList(sessionContext.getUserId());
            List<UUID> terminalGroupIdList = fetchTerminalGroupIdList(sessionContext.getUserId());
            deskTypeRequest.setUserGroupIdArr(userGroupIdList.toArray(new UUID[userGroupIdList.size()]));
            deskTypeRequest.setTerminalGroupIdArr(terminalGroupIdList.toArray(new UUID[terminalGroupIdList.size()]));
        }
        DesktopStatisticsResponse response = statisticsAPI.statisticsDesktop(deskTypeRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 统计终端状态
     *
     * @param request 无参数
     * @param sessionContext session信息
     * @return 返回同级结果
     *
     * @throws BusinessException 业务异常
     *
     */
    @RequestMapping("statisticsTerminal")
    public DefaultWebResponse statisticsTerminal(DefaultWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        IacLoginUserDTO loginUserInfo = baseAdminMgmtAPI.getLoginUserInfo();
        List<UUID> terminalGroupIdList = new ArrayList<>();
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            terminalGroupIdList = fetchTerminalGroupIdList(loginUserInfo.getId());
        }
        UUID[] groupIdArr = terminalGroupIdList.toArray(new UUID[0]);
        CbbTerminalStatisticsDTO response = statisticsAPI.statisticsTerminal(groupIdArr);
        return DefaultWebResponse.Builder.success(response);
    }


    /**
     * 统计服务器资源使用情况
     *
     * @param request 空参数
     * @return 返回结果数据
     * @throws BusinessException 业务异常
     */
    @RequestMapping("statisticsResourceUse")
    public DefaultWebResponse statisticsResourceUse(DefaultWebRequest request) throws BusinessException {
        QuerySystemInfoResponse response = systemInfoAPI.querySystemInfo();
        CpuDTO cpuDTO = response.getCpu();
        MemoryDTO memoryDTO = response.getMemory();
        DiskDTO[] diskArr = response.getDiskArr();
        long diskUsed = 0L;
        long diskTotal = 0L;
        for (DiskDTO disk : diskArr) {
            diskUsed += disk.getUsed();
            diskTotal += disk.getTotal();
        }
        StatisticsResourceUseDTO resourceUseDTO = new StatisticsResourceUseDTO();
        resourceUseDTO.setCpu(cpuDTO);
        resourceUseDTO.setMemory(memoryDTO);
        StatisticsResourceUseDTO.DiskUseDTO disk = new StatisticsResourceUseDTO.DiskUseDTO();
        disk.setTotal(diskTotal);
        disk.setUsed(diskUsed);
        resourceUseDTO.setDisk(disk);
        return DefaultWebResponse.Builder.success(resourceUseDTO);
    }

    /**
     * 获取cpu使用率
     *
     * @param request 空参数
     * @return 返回结果数据
     * @throws BusinessException 业务异常
     */
    @RequestMapping("fetchCpuUseRate")
    public DefaultWebResponse fetchCpuUseRate(DefaultWebRequest request) throws BusinessException {
        QueryCpuInfoResponse response = systemInfoAPI.queryCpuInfo();
        Integer usedRate = response.getUsedRate();
        return DefaultWebResponse.Builder.success(usedRate);
    }

    /**
     * 获取网络带宽速度
     *
     * @param request 请求参数
     * @return 返回结果数据
     * @throws BusinessException 业务异常
     */
    @RequestMapping("fetchBandwidthSpeed")
    public DefaultWebResponse fetchBandwidthSpeed(DefaultWebRequest request) throws BusinessException {
        BigDecimal upSpeed = new BigDecimal(0);
        BigDecimal downSpeed = new BigDecimal(0);
        try {
            QueryCurrentNetworkSpeedResponse response = systemInfoAPI.queryCurrentNetworkSpeed();
            upSpeed = response.getUpSpeed();
            downSpeed = response.getDownSpeed();
        } catch (Exception e) {
            LOGGER.error("获取网卡速率失败", e);
        }
        BandwidthSpeedResponse bandwidthSpeed = new BandwidthSpeedResponse(upSpeed, downSpeed);
        return DefaultWebResponse.Builder.success(bandwidthSpeed);
    }

    /**
     * 统计云桌面激活情况
     *
     * @param request 请求参数
     * @return 返回结果数据
     * @throws BusinessException 业务异常
     */
    @ApiOperation("统计云桌面激活情况")
    @RequestMapping("statisticsDeskActive")
    public CommonWebResponse statisticsDeskActive(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        return queryLicense(LicenseTypeEnum.WINDOWS);
    }

    /**
     * 统计集群服务器信息情况
     *
     * @param request 请求参数
     * @return 返回结果数据
     * @throws BusinessException 业务异常
     */
    @RequestMapping("statisticsClusterServer")
    public DefaultWebResponse statisticsClusterServer(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "DefaultWebRequest can not be null");
        ClusterServerDTO clusterServerDTO = cbbClusterServerMgmtAPI.statisticsClusterServer();
        return DefaultWebResponse.Builder.success(clusterServerDTO);
    }

    /**
     * 集群服务器信息趋势情况
     *
     * @param request 请求参数
     * @return 返回结果数据
     * @throws BusinessException 业务异常
     */
    @RequestMapping("clusterServerTrend")
    public DefaultWebResponse clusterServerTrend(DefaultWebRequest request) throws BusinessException {
        List<ClusterServerTrendDTO> clusterServerTrendDTOList = clusterServerStatisticsAPI.clusterServerTrend();
        return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", clusterServerTrendDTOList));
    }

    /**
     * 获取RCDC云桌面授权信息
     *
     * @param request 请求参数
     * @return 返回结果数据
     * @throws BusinessException 业务异常
     */
    @ServerModel
    @ApiOperation("获取VDI云桌面正式授权信息")
    @RequestMapping(value = "obtainRcdcAuthInfo", method = RequestMethod.POST)
    public CommonWebResponse obtainRcdcAuthInfo(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        return queryLicense(LicenseTypeEnum.DESKTOP);
    }

    /**
     * 获取RCDC云桌面授权信息,不区分服务器类型
     * @param request  请求参数
     * @return  返回结果数据
     * @throws BusinessException  业务异常
     */
    @ApiOperation("获取VDI云桌面正式授权信息,不区分服务器类型")
    @RequestMapping(value = "obtainAllRcdcAuthInfo", method = RequestMethod.POST)
    public CommonWebResponse obtainAllRcdcAuthInfo(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        return queryLicense(LicenseTypeEnum.DESKTOP);
    }

    /**
     * 获取计算集群列表
     *
     * @param request 请求参数
     * @return 返回结果数据
     * @throws BusinessException      业务异常
     * @throws NoSuchFieldException   NoSuchFieldException
     * @throws IllegalAccessException IllegalAccessException
     */
    @ApiOperation("获取计算资源列表")
    @RequestMapping(value = "obtainComputeClusterList", method = RequestMethod.POST)
    public CommonWebResponse<PageResponse<ClusterInfoDTO>> obtainComputeClusterList(PageQueryRequest request) throws BusinessException,
            NoSuchFieldException, IllegalAccessException {
        Assert.notNull(request, "obtainComputeClusterList request can not be null");
        return fetchPageResponseCommonWebResponse(request, false);
    }

    /**
     * 获取计算集群列表
     *
     * @param request 请求参数
     * @return 返回结果数据
     * @throws BusinessException      业务异常
     * @throws NoSuchFieldException   NoSuchFieldException
     * @throws IllegalAccessException IllegalAccessException
     */
    @ApiOperation("获取镜像管理计算资源列表")
    @RequestMapping(value = "image/obtainComputeClusterList", method = RequestMethod.POST)
    public CommonWebResponse<PageResponse<ClusterInfoDTO>> obtainImageComputeClusterList(PageQueryRequest request) throws BusinessException,
            NoSuchFieldException, IllegalAccessException {
        Assert.notNull(request, "request can not be null");
        return fetchPageResponseCommonWebResponse(request, true);
    }

    private CommonWebResponse<PageResponse<ClusterInfoDTO>> fetchPageResponseCommonWebResponse(PageQueryRequest request,
                                                                                               Boolean isFromImage) throws NoSuchFieldException,
            IllegalAccessException, BusinessException {
        Match[] matchArr = request.getMatchArr();
        UUID[] imageTemplateIdArr = null;
        UUID[] storagePoolIdArr = null;
        UUID[] networkIdArr = null;
        UUID[] osFileIdArr = null;
        List<Match> matchList = Lists.newArrayList();
        if (ArrayUtils.isNotEmpty(matchArr)) {
            for (Match match : matchArr) {
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.IMAGE_TEMPLATE_ID, ((ExactMatch) match).getFieldName())) {
                    imageTemplateIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.STORAGE_POOL_ID, ((ExactMatch) match).getFieldName())) {
                    storagePoolIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.NETWORK_ID, ((ExactMatch) match).getFieldName())) {
                    networkIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.OS_FILE_ID, ((ExactMatch) match).getFieldName())) {
                    osFileIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                matchList.add(match);
            }
        }
        Field matchArrField = request.getClass().getDeclaredField("matchArr");
        matchArrField.setAccessible(true);
        matchArrField.set(request, matchList.toArray(new Match[0]));
        PageResponse<ClusterInfoDTO> pageResponse = clusterAPI.pageQueryComputerCluster(request);
        // 筛选过滤计算集群信息
        fetchClusterByIds(pageResponse.getItems(), imageTemplateIdArr, storagePoolIdArr, networkIdArr, osFileIdArr, isFromImage);
        return CommonWebResponse.success(pageResponse);
    }


    @SuppressWarnings({"checkstyle:ParameterNumber"})
    private void fetchClusterByIds(ClusterInfoDTO[] clusterInfoDTOArr, UUID[] imageTemplateIdArr,
                                   UUID[] storagePoolIdArr, UUID[] networkIdArr, UUID[] osFileIdArr,
                                   Boolean isFromImage) throws BusinessException {
        if (ArrayUtils.isEmpty(clusterInfoDTOArr)) {
            return;
        }
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfoDTO -> clusterInfoDTO));
        if (imageTemplateIdArr != null && imageTemplateIdArr.length > 0) {
            fetchClusterByImageTemplateId(clusterInfoDTOArr, imageTemplateIdArr, clusterInfoAllMap);
            // 镜像是否安装对应集群的驱动
            fetchDriveByImageTemplateId(clusterInfoDTOArr, imageTemplateIdArr , isFromImage);
        }

        if (storagePoolIdArr != null && storagePoolIdArr.length > 0) {
            fetchClusterByStoragePoolId(clusterInfoDTOArr, storagePoolIdArr);
        }

        if (networkIdArr != null && networkIdArr.length > 0) {
            fetchClusterByNetworkId(clusterInfoDTOArr, networkIdArr);
        }

        if (osFileIdArr != null && osFileIdArr.length > 0) {
            fetchClusterByOsFileId(clusterInfoDTOArr, osFileIdArr);
        }
    }

    private void fetchClusterByImageTemplateId(ClusterInfoDTO[] clusterInfoDTOArr,
                                               UUID[] imageTemplateIdArr, Map<UUID, ClusterInfoDTO> clusterInfoAllMap) throws BusinessException {
        for (UUID imageTemplateId : imageTemplateIdArr) {
            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
            if (Objects.isNull(imageTemplateDetail.getClusterInfo())) {
                continue; // CCP磁盘与计算集群解耦
            }

            ClusterInfoDTO hciClusterDTO = clusterInfoAllMap.getOrDefault(imageTemplateDetail.getClusterInfo().getId(), new ClusterInfoDTO());
            Set<String> imageArchSet = hciClusterDTO.getArchSet();

            Arrays.stream(clusterInfoDTOArr).forEach(clusterInfoDTO -> {
                Set<String> currentArchSet = clusterInfoDTO.getArchSet();
                if (Objects.isNull(currentArchSet) || Objects.isNull(imageArchSet) || SetUtils.intersection(imageArchSet, currentArchSet).isEmpty()) {
                    clusterInfoDTO.setCanUsed(Boolean.FALSE);
                    clusterInfoDTO.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_IMAGE_CLUSTER_CPU_FRAMEWORK_NOT_AGREEMENT,
                            imageTemplateDetail.getImageName(), clusterInfoDTO.getClusterName()));
                }
            });
        }
    }

    private void fetchClusterByStoragePoolId(ClusterInfoDTO[] clusterInfoDTOArr, UUID[] storagePoolIdArr) throws BusinessException {
        for (UUID storagePoolId : storagePoolIdArr) {
            Set<UUID> clusterIdSet = clusterAPI.queryClusterToSetByStoragePoolId(storagePoolId);
            Arrays.stream(clusterInfoDTOArr).forEach(clusterInfoDTO -> {
                if (!clusterIdSet.contains(clusterInfoDTO.getId())) {
                    clusterInfoDTO.setCanUsed(Boolean.FALSE);
                    clusterInfoDTO.setCanUsedMessage(LocaleI18nResolver.
                            resolve(BusinessKey.RCDC_RCO_STORAGE_POOL_CLUSTER_NOT_AGREEMENT));
                }
            });
        }
    }

    private void fetchClusterByNetworkId(ClusterInfoDTO[] clusterInfoDTOArr, UUID[] networkIdArr) throws BusinessException {
        for (UUID networkId : networkIdArr) {
            CbbDeskNetworkDetailDTO networkDetailDTO = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
            Arrays.stream(clusterInfoDTOArr).forEach(clusterInfoDTO -> {
                if (!Objects.equals(networkDetailDTO.getClusterId(), clusterInfoDTO.getId())) {
                    clusterInfoDTO.setCanUsed(false);
                    clusterInfoDTO.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NETWORK_NOT_CLUSTER,
                            networkDetailDTO.getDeskNetworkName(), clusterInfoDTO.getClusterName()));
                }
            });
        }
    }
    
    private void fetchDriveByImageTemplateId(ClusterInfoDTO[] clusterInfoDTOArr, UUID[] imageTemplateIdArr, Boolean isFromImage) {
        // 获取可用云平台的节点cpu类型信息
        Map<UUID, Set<String>> clusterAllCpuDriveTypeMap = fetchAvailablePlatformCpuTypeMap(clusterInfoDTOArr);
        // 获取镜像平台信息
        Map<UUID, UUID> imageToPlatformId = cbbImageTemplateMgmtAPI.listImageTemplateByIdList(Arrays.asList(imageTemplateIdArr))
                .stream().collect(Collectors.toMap(CbbImageTemplateDetailDTO::getId, PlatformBaseInfoDTO::getPlatformId));
        for (UUID imageTemplateId : imageTemplateIdArr) {
            Set<String> imageInstallDriveSet = cbbImageTemplateMgmtAPI.listImageServerCpuByImageIdAndState(imageTemplateId,
                    ImageTemplateCpuInstallState.AVAILABLE).stream().map(CbbImageTemplateServerCpuDTO::getServerCpuType).collect(Collectors.toSet());
            Arrays.stream(clusterInfoDTOArr)
                    .filter(clusterInfoDTO -> Objects.nonNull(clusterAllCpuDriveTypeMap.get(clusterInfoDTO.getId())))
                    .forEach(clusterInfoDTO -> {
                        boolean isNotNeedInstallDrive = CloudPlatformStatus.isAvailable(clusterInfoDTO.getPlatformStatus()) && 
                                imageInstallDriveSet.containsAll(clusterAllCpuDriveTypeMap.get(clusterInfoDTO.getId()));
                        clusterInfoDTO.setCanInstallDrive(isNotNeedInstallDrive);

                        UUID sourcePlatformId = imageToPlatformId.get(imageTemplateId);

                        boolean isPlatformNotEqAndNotInstallCpu =
                                !isNotNeedInstallDrive && !Objects.equals(sourcePlatformId, clusterInfoDTO.getPlatformId());
                        if (isPlatformNotEqAndNotInstallCpu && Boolean.FALSE.equals(isFromImage)) {
                            // 原平台与目标云平台不相同
                            clusterInfoDTO.setCanUsed(false);
                            clusterInfoDTO.setCanUsedMessage(LocaleI18nResolver.
                                    resolve(BusinessKey.RCDC_RCO_TARGET_PLATFORM_EXIST_UNINSTALL_CPU));
                        }
                    });
        }
    }
    
    private Map<UUID, Set<String>> fetchAvailablePlatformCpuTypeMap(ClusterInfoDTO[] clusterInfoDTOArr) {
        // 获取clusterInfoDTOArr中可用云平台的全部节点
        List<ClusterNodesInfoDTO> nodesInfoList = Lists.newArrayList();
        for (UUID platformId : Arrays.stream(clusterInfoDTOArr)
                .filter(clusterInfoDTO -> CloudPlatformStatus.isAvailable(clusterInfoDTO.getPlatformStatus()))
                .map(ClusterInfoDTO::getPlatformId).collect(Collectors.toSet())) {
            // 单个云平台异常不影响计算集群获取
            try {
                nodesInfoList.addAll(clusterMgmtAPI.getAllClusterNodesInfo(new CloudPlatformBaseRequest(platformId)).getClusterNodesInfoDTOList());
            } catch (BusinessException e) {
                LOGGER.error("获取云平台[{}]全部节点信息出现异常，忽略处理", platformId, e);
            }
        }
        // 获取计算集群全部cpu驱动类型
        return nodesInfoList.stream()
                .collect(Collectors.groupingBy(ClusterNodesInfoDTO::getClusterId,
                        Collectors.mapping(ClusterNodesInfoDTO::getCpuType, Collectors.toSet())));
    }


    private CommonWebResponse queryLicense(LicenseTypeEnum licenseType) {
        ObtainRcdcLicenseNumResponse response = licenseAPI.acquireLicenseNum(licenseType);
        DeskActiveStatisticsItemDTO itemDTO = new DeskActiveStatisticsItemDTO();

        itemDTO.setTotal(response.getLicenseNum());
        itemDTO.setUsed(response.getUsedNum());
        itemDTO.setVdiUsed(response.getVdiCloudDesktopUsedLicenseNum());
        itemDTO.setIdvUsed(response.getIdvTerminalUsedLicenseNum());
        itemDTO.setVoiUsed(response.getTciTerminalUsedLicenseNum());
        itemDTO.setRcaUsed(response.getRcaUsedLicenseNum());
        return CommonWebResponse.success(itemDTO);
    }

    private List<UUID> fetchUserGroupIdList(UUID adminId) throws BusinessException {
        ListUserGroupIdRequest request = new ListUserGroupIdRequest();
        request.setAdminId(adminId);
        ListUserGroupIdResponse response = adminDataPermissionAPI.listUserGroupIdByAdminId(request);
        List<UUID> uuidList = new ArrayList<>();
        for (String groupId : response.getUserGroupIdList()) {
            if (groupId.equals(UserGroupHelper.USER_GROUP_ROOT_ID)) {
                continue;
            }
            uuidList.add(UUID.fromString(groupId));
        }
        return uuidList;
    }

    private List<UUID> fetchTerminalGroupIdList(UUID adminId) throws BusinessException {
        ListTerminalGroupIdRequest request = new ListTerminalGroupIdRequest();
        request.setAdminId(adminId);
        ListTerminalGroupIdResponse response = adminDataPermissionAPI.listTerminalGroupIdByAdminId(request);

        return response.getTerminalGroupIdList().stream().filter(groupId -> !groupId.equals(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID))
                .map(groupId -> UUID.fromString(groupId)).collect(Collectors.toList());
    }

    private void fetchClusterByOsFileId(ClusterInfoDTO[] clusterInfoDTOArr, UUID[] osFileIdArr) throws BusinessException {
        for (UUID osFileId : osFileIdArr) {
            CbbGetOsFileResultDTO osFileDTO = cbbOsFileMgmtAPI.getOsFile(osFileId);
            Arrays.stream(clusterInfoDTOArr).forEach(clusterInfoDTO -> {
                Set<String> archSet = clusterInfoDTO.getArchSet();
                // 不为其他且CPU架构不一致
                if (CollectionUtils.isEmpty(archSet) ||
                        (CbbCpuArchType.OTHER != osFileDTO.getCpuArch() && !archSet.contains(osFileDTO.getCpuArch().getArchName()))) {
                    clusterInfoDTO.setCanUsed(false);
                    clusterInfoDTO.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_OS_FILE_CLUSTER_CPU_ARCH_NOT_MATCH,
                            osFileDTO.getImageFileName(), clusterInfoDTO.getClusterName()));
                }
            });
        }
    }

}
