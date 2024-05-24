package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbAppTestTargetDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVmGraphicsDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.TerminalVmModeTypeEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.vm.VmIdRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.vm.VmVirtGraphicInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskFaultInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopStateNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AllDesktopOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.BigScreenCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.GroupAvgUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.CheckDesktopPortResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ListDesktopNumByUserGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.AbnormalPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopUsageIdArrRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetUserGroupOverviewRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListDesktopByUserGroupPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CbbDeskFaultInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AbnormalDesktopListResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DeskTopStatusResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DesktopOnlineSituationStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DesktopUsageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.ListDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.UserGroupOverviewResponse;
import com.ruijie.rcos.rcdc.rco.module.def.desktopsession.DesktopSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.DashboardStatisticsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service.StatisticsDeskTopService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopNormalDistributionService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.QueryUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:07
 *
 * @author zhangyichi
 */
public class DesktopAPIImpl implements DesktopAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopAPIImpl.class);

    /**
     * 获取云桌面信息的分页长度
     */
    private static final Integer DESKTOP_PAGE_QUERY_LIMIT = 50;

    @Autowired
    private DesktopService desktopService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Autowired
    private DesktopNormalDistributionService desktopNormalDistributionService;

    @Autowired
    private DeskFaultInfoAPI deskFaultInfoAPI;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private StatisticsDeskTopService statisticsDeskTopService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbUamAppTestTargetAPI testTargetAPI;

    @Autowired
    private DesktopSessionServiceAPI desktopSessionServiceAPI;

    @Autowired
    private HostUserService hostUserService;

    private static final Map<UUID, LocalDateTime> DESK_SHUTDOWN_DATE = Maps.newConcurrentMap();

    private static final int CHECK_IP_PORT_TIMEOUT = 15000;

    private static final int LOCK_TIME = 2;

    @Override
    public AllDesktopOverviewDTO getAllDesktopOverview() throws BusinessException {
        return desktopService.getAllDesktopOverview();
    }

    @Override
    public UserGroupOverviewResponse getUserGroupOverview(GetUserGroupOverviewRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        PageSearchRequest pageSearchRequest = new PageSearchRequest();
        pageSearchRequest.setLimit(DESKTOP_PAGE_QUERY_LIMIT);
        List<UUID> userGroupIdList = Lists.newArrayList();
        if (null != request.getGroupId()) {
            // 查询所有子组的userGroupId
            userGroupIdList = desktopService.getChildGroupId(request.getGroupId());
            // 分页获取用户组下所有云桌面信息
            MatchEqual matchEqual = new MatchEqual("userGroupId", userGroupIdList.toArray());
            pageSearchRequest.setMatchEqualArr(new MatchEqual[]{matchEqual});
        }

        QueryUtil<CloudDesktopDTO, PageSearchRequest> desktopQuery = new QueryUtil<>();
        List<CloudDesktopDTO> cloudDesktopDTOList = desktopQuery.findAllByPageQuery(pageSearchRequest, userDesktopMgmtAPI::pageQuery, dto -> true,
            dtoList -> dtoList.size() == DESKTOP_PAGE_QUERY_LIMIT);

        UserGroupOverviewResponse response = new UserGroupOverviewResponse();
        setAvgUsageList(response, cloudDesktopDTOList, request.getStartTime(), request.getEndTime());
        setDesktopNum(response, userGroupIdList);
        return response;
    }

    private void setAvgUsageList(UserGroupOverviewResponse response, List<CloudDesktopDTO> desktopDTOList, Date startTime, Date endTime) {

        List<GroupAvgUsageDTO> usageDTOList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(desktopDTOList)) {
            response.setResourceAvgUsageList(usageDTOList);
            return;
        }

        List<UUID> deskIdList = desktopDTOList.stream().map(CloudDesktopDTO::getId).collect(Collectors.toList());
        List<DesktopResourceUsageDayEntity> entityList =
                desktopResourceUsageDayDAO.getAverageUsageGroupByStatisticTime(deskIdList, startTime, endTime);

        for (DesktopResourceUsageDayEntity entity : entityList) {
            GroupAvgUsageDTO groupAvgUsageDTO = new GroupAvgUsageDTO();
            BeanUtils.copyProperties(entity, groupAvgUsageDTO);
            usageDTOList.add(groupAvgUsageDTO);
        }
        response.setResourceAvgUsageList(usageDTOList);
    }

    private void setDesktopNum(UserGroupOverviewResponse response, List<UUID> userGroupIdList) throws BusinessException {

        ListDesktopNumByUserGroupRequest request = new ListDesktopNumByUserGroupRequest();
        request.setUuidArr(userGroupIdList.toArray(new UUID[]{}));

        List<DesktopStateNumDTO> dtoList = userDesktopMgmtAPI.listDesktopNumByUserGroup(userGroupIdList.toArray(new UUID[]{}));
        if (null == dtoList) {
            LOGGER.error("获取用户组云桌面数统计失败");
            return;
        }

        Long total = 0L;
        Long online = 0L;
        for (DesktopStateNumDTO dto : dtoList) {
            total += dto.getNum();
            if (CbbCloudDeskState.RUNNING.equals(dto.getDeskState())) {
                online += dto.getNum();
            }
        }
        response.setOnline(online);
        response.setTotal(total);
    }

    @Override
    public ListDesktopResponse listDesktopByUserGroup(ListDesktopByUserGroupPageRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        PageSearchRequest pageSearchRequest = new PageSearchRequest();
        pageSearchRequest.setPage(request.getPage());
        pageSearchRequest.setLimit(request.getLimit());
        pageSearchRequest.setMatchEqualArr(buildMatchEqualArr(request));

        DefaultPageResponse<CloudDesktopDTO> desktopResponse = userDesktopMgmtAPI.pageQuery(pageSearchRequest);
        if (null == desktopResponse || null == desktopResponse.getItemArr()) {
            LOGGER.error("获取云桌面信息失败");
            throw new BusinessException(BusinessKey.RCDC_RCO_BIGSCREEN_GROUP_OVERVIEW_DESKTOP_NOT_FOUND);
        }

        List<BigScreenCloudDesktopDTO> desktopDTOList =
                Stream.of(desktopResponse.getItemArr()).map(BigScreenCloudDesktopDTO::new).collect(Collectors.toList());
        ListDesktopResponse response = new ListDesktopResponse();
        response.setDesktopList(desktopDTOList);
        response.setTotal(desktopResponse.getTotal());
        return response;
    }

    private MatchEqual[] buildMatchEqualArr(ListDesktopByUserGroupPageRequest request) throws BusinessException {
        MatchEqual isDeleteMatchEqual = new MatchEqual("isDelete", new Object[]{false});
        MatchEqual deskStateMatchEqual = new MatchEqual("deskState", new Object[]{CbbCloudDeskState.RUNNING.name()});

        if (null == request.getGroupId()) {
            return new MatchEqual[]{isDeleteMatchEqual, deskStateMatchEqual};
        }

        // 查询所有子组的userGroupId
        List<UUID> userGroupIdList = desktopService.getChildGroupId(request.getGroupId());
        // 构造用户云桌面请求
        MatchEqual userGroupIdMatchEqual = new MatchEqual("userGroupId", userGroupIdList.toArray());
        return new MatchEqual[]{isDeleteMatchEqual, deskStateMatchEqual, userGroupIdMatchEqual};
    }

    @Override
    public DesktopUsageResponse getDesktopUsage(DesktopUsageIdArrRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        DesktopUsageResponse response = new DesktopUsageResponse();
        response.setResourceUsageList(desktopService.listDesktopUsageById(request.getIdArr(), request.getStartTime(), request.getEndTime()));
        return response;
    }

    @Override
    public DesktopNormalDistributionDTO getDesktopNormalDistribution(ResourceTypeEnum resourceType) {
        Assert.notNull(resourceType, "resourceType can not be null");

        // 正太分布计算
        Map<ResourceTypeEnum, DesktopNormalDistributionDTO> distributionDTOMap = desktopNormalDistributionService.normalDistribution();

        if (CollectionUtils.isEmpty(distributionDTOMap) || !distributionDTOMap.containsKey(resourceType)) {
            return new DesktopNormalDistributionDTO();
        }
        return distributionDTOMap.get(resourceType);
    }

    @Override
    public AbnormalDesktopListResponse listAbnormalDesktop(AbnormalPageRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        // 按资源类型和异常类型获取云桌面名称
        List<DesktopResourceUsageDayEntity> entityList = desktopService.listAbnormalDesktop(request.getResourceType(), request.getAbnormalType());

        AbnormalDesktopListResponse response = new AbnormalDesktopListResponse();
        int offset = request.getPage() * request.getLimit();
        response.setIdList(entityList.stream().skip(offset).limit(request.getLimit()).map(DesktopResourceUsageDayEntity::getDesktopId)
                .collect(Collectors.toList()));
        response.setTotal(entityList.size());
        return response;
    }

    @Override
    public void validateImageTemplateDesktopNotRunning(UUID imageId, String imageName) throws BusinessException {
        Assert.notNull(imageId, "imageId cannot be null!");
        Assert.hasText(imageName, "imageName cannot be empty!");

        desktopService.validateImageTemplateDesktopNotRunning(imageId, imageName);
    }

    @Override
    public boolean isRemoteAssistAutoAgree(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId cannot be null!");

        List<CbbAppTestTargetDTO> testTargetDTOList =
                testTargetAPI.findByResourceIdAndInState(deskId, Collections.singletonList(DesktopTestStateEnum.TESTING));
        if (!CollectionUtils.isEmpty(testTargetDTOList)) {
            LOGGER.info("桌面[id:{}]处于测试模式，无需用户确认", deskId);
            return true;
        }


        CloudDesktopDetailDTO cloudDesktopDTO = queryCloudDesktopService.queryDeskDetail(deskId);
        if (StringUtils.equals(cloudDesktopDTO.getDesktopType(), CbbCloudDeskType.IDV.name())) {
            UserTerminalEntity userTerminalEntity = userTerminalDAO.findByBindDeskId(deskId);
            if (userTerminalEntity == null) {
                LOGGER.error("未找对与云桌面[id:{}]对应的终端绑定关系", deskId);
                throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, String.valueOf(deskId));
            }
            return IdvTerminalModeEnums.PUBLIC == userTerminalEntity.getTerminalMode();
        } else if (StringUtils.equals(cloudDesktopDTO.getDesktopType(), CbbCloudDeskType.VDI.name())) {

            UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(deskId);
            UUID userId = userDesktopEntity.getUserId();
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
            LOGGER.info("远程协助目标用户信息：{}", JSONObject.toJSONString(userDetail));
            return IacUserTypeEnum.VISITOR == userDetail.getUserType();
        }

        return false;
    }

    @Override
    public boolean isRequestRemoteAssist(UUID deskId) {
        Assert.notNull(deskId, "deskId cannot be null!");

        CbbDeskFaultInfoResponse deskFaultInfo = deskFaultInfoAPI.findFaultInfoByDeskId(deskId);

        return deskFaultInfo != null && deskFaultInfo.getCbbDeskFaultInfoDTO() != null
                && BooleanUtils.toBoolean(deskFaultInfo.getCbbDeskFaultInfoDTO().getFaultState());
    }

    @Override
    public boolean isAnyConnectedChannel(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId cannot be null!");

        CbbDeskDTO desktopDetailDTO = cbbDeskMgmtAPI.getDeskById(desktopId);
        if (desktopDetailDTO.getSessionType() == CbbDesktopSessionType.SINGLE) {
            // 处理HEST的场景,先查询本地是否存在会话记录
            boolean isExistSession = false;
            if (CbbEstProtocolType.HEST == desktopDetailDTO.getEstProtocolType()) {
                isExistSession = isAnyConnectedChannelByMultipleSession(desktopId);
            }

            // 非运行中,或者第三方,无需向CCP查询会话信息
            if (CbbCloudDeskState.RUNNING != desktopDetailDTO.getDeskState() || CbbCloudDeskType.THIRD == desktopDetailDTO.getDeskType()) {
                return isExistSession;
            }
            // 虽然策略可能是HEST,但是有的客户端使用EST连接,所以要查询EST会话数
            return isExistSession || isAnyConnectedChannelBySingleSession(desktopDetailDTO);
        } else if (desktopDetailDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            return isAnyConnectedChannelByMultipleSession(desktopId);
        }

        LOGGER.warn("桌面ID[{}]会话类型异常，当前会话类型为：[{}]，查询桌面是否存在连接信息异常默认返回存在", desktopId, desktopDetailDTO.getSessionType());
        // 目前主机只有单会话、多会话，会话类型为空时默认返回存在连接信息
        return true;

    }

    private boolean isAnyConnectedChannelByMultipleSession(UUID desktopId) {
        List<DesktopSessionDTO> desktopSessionDTOList = desktopSessionServiceAPI.findByDeskId(desktopId);
        return !CollectionUtils.isEmpty(desktopSessionDTOList);
    }

    private boolean isAnyConnectedChannelBySingleSession(CbbDeskDTO desktopDetail) throws BusinessException {
        UUID deskId = desktopDetail.getDeskId();
        try {
            // 该方法只支持查询运行中的桌面EST的会话数
            CbbVmGraphicsDTO vmGraphicsDTO = cbbVDIDeskMgmtAPI.querySpiceById(new VmIdRequest(desktopDetail.getPlatformId(), deskId));
            if (Objects.isNull(vmGraphicsDTO)) {
                LOGGER.warn("桌面[{}]查询qemu，信息不完整[{}]", deskId, JSON.toJSONString(vmGraphicsDTO));
                throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_QEMU_INFO_INCOMPLETE, deskId.toString(),
                        JSON.toJSONString(vmGraphicsDTO));
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("桌面[{}]查询到的qemu信息:{}", deskId, JSON.toJSONString(vmGraphicsDTO));
            }
            List<VmVirtGraphicInfoDTO> virtGraphicInfoList = vmGraphicsDTO.getVirtGraphicInfoList();
            if (CollectionUtils.isEmpty(virtGraphicInfoList)) {
                return true;
            }
            return virtGraphicInfoList.stream().anyMatch(vmVirtGraphicInfoDTO ->
                    org.apache.commons.collections.CollectionUtils.isNotEmpty(vmVirtGraphicInfoDTO.getChannelDTOList()));
        } catch (BusinessException e) {
            LOGGER.error("桌面[{}]查询qemu信息异常", deskId, e);
            throw e;
        }
    }

    @Override
    public void recordDeskShutdownDate(UUID deskId, Integer shutOvertime) {
        Assert.notNull(deskId, "deskId cannot be null!");
        Assert.notNull(shutOvertime, "shutOvertime cannot be null!");
        LocalDateTime shutdownDate = LocalDateTime.now().plusMinutes(shutOvertime);
        DESK_SHUTDOWN_DATE.put(deskId, shutdownDate);
        LOGGER.debug("记录桌面会话配置 deskId:{}，shutOvertime:{}", deskId, shutOvertime);
    }

    @Override
    public List<UUID> getDeskShutdownIdList() {
        LocalDateTime now = LocalDateTime.now();
        List<UUID> deskIdList = new ArrayList<>();
        for (Map.Entry<UUID, LocalDateTime> desk : DESK_SHUTDOWN_DATE.entrySet()) {
            try {
                LockableExecutor.executeWithTryLock(desk.getKey().toString(), () -> {
                    if (DESK_SHUTDOWN_DATE.get(desk.getKey()) != null) {
                        if (desk.getValue().isBefore(now)) {
                            deskIdList.add(desk.getKey());
                        }
                    }
                }, LOCK_TIME);
            } catch (BusinessException e) {
                LOGGER.error("获取关机列表失败", e);
            }
        }
        return deskIdList;
    }

    @Override
    public void clearDeskShutdownDate(UUID deskId) {
        Assert.notNull(deskId, "deskId cannot be null!");
        try {
            LockableExecutor.executeWithTryLock(deskId.toString(), () -> {
                DESK_SHUTDOWN_DATE.remove(deskId);
            }, LOCK_TIME);
        } catch (BusinessException e) {
            LOGGER.error("清除桌面会话配置 deskId:[{}]", deskId, e);
        }
        LOGGER.debug("清除桌面会话配置 deskId:[{}]", deskId);
    }

    @Override
    public void resetDeskShutdownDate(Integer shutOvertime) {
        Assert.notNull(shutOvertime, "shutOvertime cannot be null!");
        //等于0 不限制，清除关机时间
        if (shutOvertime == 0) {
            DESK_SHUTDOWN_DATE.clear();
            return;
        }
        for (Map.Entry<UUID, LocalDateTime> desk : DESK_SHUTDOWN_DATE.entrySet()) {
            try {
                LockableExecutor.executeWithTryLock(desk.getKey().toString(), () -> {
                    if (DESK_SHUTDOWN_DATE.get(desk.getKey()) != null) {
                        recordDeskShutdownDate(desk.getKey(), shutOvertime);
                    }
                }, LOCK_TIME);
            } catch (BusinessException e) {
                LOGGER.error("重新设置关机时间异常 deskId:[{}]", desk.getKey(), e);
            }
        }
    }

    // 空接口
    @Override
    public void removeDesktopByGroupId(UUID deskId, UUID groupId) throws BusinessException {
    }

    @Override
    public DeskTopStatusResponse statisticsDeskTopStatus() {
        DeskTopStatusResponse response = new DeskTopStatusResponse();
        Long runningCount = userDesktopMgmtAPI.findCountByDeskState(CbbCloudDeskState.RUNNING);
        Long closeCount = userDesktopMgmtAPI.findCountByDeskState(CbbCloudDeskState.CLOSE);
        Long sleepCount = userDesktopMgmtAPI.findCountByDeskState(CbbCloudDeskState.SLEEP);
        Long offLineCount = userDesktopMgmtAPI.findCountByDeskState(CbbCloudDeskState.OFF_LINE);
        Long errorCount = userDesktopMgmtAPI.findCountByDeskState(CbbCloudDeskState.ERROR);
        Long totalCount = userDesktopMgmtAPI.findCount();
        Long otherCount = totalCount - runningCount - closeCount - sleepCount - offLineCount - errorCount;

        response.setRunningCount(runningCount);
        response.setCloseCount(closeCount);
        response.setSleepCount(sleepCount);
        response.setOffLineCount(offLineCount);
        response.setErrorCount(errorCount);
        response.setOtherCount(otherCount);
        return response;
    }

    @Override
    public DesktopOnlineSituationStatisticsResponse statisticsDesktopHistoryOnlineSituation(DesktopOnlineSituationStatisticsRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        switch (request.getTimeQueryType()) {
            case HOUR:
                throw new BusinessException(DashboardStatisticsBusinessKey.RCDC_RCO_TIME_QUERY_TYPE_HOUR_NOT_SUPPORTED);
            case DAY:
                return statisticsDeskTopService.statisticsDesktopHistoryOnlineSituationByDay(request);
            case MONTHLY:
                return statisticsDeskTopService.statisticsDesktopHistoryOnlineSituationByMonth(request);
            case YEAR:
                return statisticsDeskTopService.statisticsDesktopHistoryOnlineSituationByYear(request);
            default:
                throw new BusinessException(DashboardStatisticsBusinessKey.RCDC_RCO_TIME_QUERY_TYPE_NOT_EXIT);
        }
    }

    @Override
    public CheckDesktopPortResultDTO checkDesktopPort(String desktopIp, List<Integer> needCheckPortList) {
        Assert.notNull(desktopIp, "desktopIp is not null");
        Assert.notNull(needCheckPortList, "needCheckPortList is not null");

        CheckDesktopPortResultDTO checkDesktopPortResultDTO = new CheckDesktopPortResultDTO();

        List<Integer> successPortList = Lists.newArrayList();
        List<Integer> errorPortList = Lists.newArrayList();

        for (Integer port : needCheckPortList) {
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(desktopIp, port), CHECK_IP_PORT_TIMEOUT);
                LOGGER.debug("桌面ip[{}]端口[{}]可用", desktopIp, port);
                successPortList.add(port);
            } catch (Exception e) {
                LOGGER.error("桌面ip[{}]端口[{}]不可用", desktopIp, port);
                errorPortList.add(port);
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                    LOGGER.error("socket close error, desktop ip:[{}], port:[{}]", desktopIp, port);
                }
            }

        }
        checkDesktopPortResultDTO.setErrorPortList(errorPortList);
        checkDesktopPortResultDTO.setSuccessPortList(successPortList);
        return checkDesktopPortResultDTO;
    }

    @Override
    public boolean existUserBindMoreDesktop(List<UUID> deskIdList) {
        Assert.notNull(deskIdList, "deskIdList is not null");
        if (CollectionUtils.isEmpty(deskIdList)) {
            return false;
        }
        List<UserBindDesktopNumDTO> userDeskNumList = desktopService.listUserBindDesktopNum(deskIdList);
        if (CollectionUtils.isEmpty(userDeskNumList)) {
            return false;
        }
        return userDeskNumList.stream().anyMatch(item -> item.getBindNum() > 1);
    }

    @Override
    public boolean existDesktopByTerminalGlobalVmMode(TerminalVmModeTypeEnum terminalVmModeType) {
        Assert.notNull(terminalVmModeType, "terminalVmModeType can not be null");
        return desktopService.existDesktopByTerminalGlobalVmMode(terminalVmModeType);
    }

    @Override
    public void validateComputerName(UUID deskId, String computerName, CbbOsType osType) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        Assert.hasText(computerName, "computerName can not be empty");
        Assert.notNull(osType, "osType can not be null");
        cbbDeskMgmtAPI.validateComputerName(deskId, computerName, osType);
    }

    @Override
    public void throwExceptionWhenNotExist(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        queryCloudDesktopService.checkDesktopExistInDeskViewById(deskId);
    }

    @Override
    public List<UUID> findUserIdByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");

        List<HostUserEntity> userEntityList = hostUserService.findByDeskId(deskId);
        if (userEntityList == null) {
            return Collections.emptyList();
        }
        return userEntityList.stream().map(HostUserEntity::getUserId).collect(Collectors.toList());
    }
}
