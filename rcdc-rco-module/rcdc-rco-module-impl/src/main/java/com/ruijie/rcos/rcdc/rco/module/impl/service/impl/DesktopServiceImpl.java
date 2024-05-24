package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGlobalVmMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbListVMMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbMonitorDataDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbVMMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.TerminalVmModeTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.StatisticsAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AllDesktopOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.NormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AbnormalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskTypeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopUsageIdArrRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DesktopStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopStartCountDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopNormalDistributionService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.util.MathUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:19
 *
 * @author zhangyichi
 */
@Service
public class DesktopServiceImpl implements DesktopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopServiceImpl.class);

    @Autowired
    private DesktopStartCountDayDAO desktopStartCountDayDAO;

    @Autowired
    private StatisticsAPI statisticsAPI;

    @Autowired
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private DesktopNormalDistributionService desktopNormalDistributionService;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    @Override
    public AllDesktopOverviewDTO getAllDesktopOverview() {
        AllDesktopOverviewDTO dto = new AllDesktopOverviewDTO();

        // 获取平均开机数
        Integer averageStartCount = desktopStartCountDayDAO.getAverageStartCount();
        // 获取最大开机数
        Integer maxStartCount = desktopStartCountDayDAO.getMaxStartCount();

        DeskTypeRequest vdiRequest = new DeskTypeRequest();
        vdiRequest.setDesktopType(CbbCloudDeskType.VDI);
        DesktopStatisticsResponse vdiResponse = statisticsAPI.statisticsDesktop(vdiRequest);
        if (null != vdiResponse && null != vdiResponse.getVdi()) {
            dto.setBootUp(vdiResponse.getVdi().getOnline());
            dto.setTotal(vdiResponse.getVdi().getTotal());
        }

        dto.setBootUpAvg(null == averageStartCount ? 0 : averageStartCount);
        dto.setHistoryBootUpTop(null == maxStartCount ? 0 : maxStartCount);

        return dto;
    }

    @Override
    public List<UUID> getChildGroupId(UUID parentGroupId) throws BusinessException {
        Assert.notNull(parentGroupId, "parentGroupId cannot be null!");

        IacUserGroupDetailDTO[] userGroupDTOArr = cbbUserGroupAPI.getAllUserGroup();
        if (null == userGroupDTOArr) {
            LOGGER.error("获取用户组信息失败");
            throw new BusinessException(BusinessKey.RCDC_RCO_BIGSCREEN_GROUP_OVERVIEW_GROUP_NOT_FOUND);
        }
        Map<UUID, IacUserGroupDetailDTO> userGroupMap =
                Stream.of(userGroupDTOArr).collect(Collectors.toMap(IacUserGroupDetailDTO::getId, Function.identity()));
        if (!userGroupMap.containsKey(parentGroupId)) {
            LOGGER.error("用户组不存在，id:[{}]", parentGroupId);
            throw new BusinessException(BusinessKey.RCDC_RCO_BIGSCREEN_GROUP_OVERVIEW_GROUP_ID_NOT_EXIST, parentGroupId.toString());
        }
        List<UUID> userGroupIdList = Lists.newArrayList();
        // 构造为用户组Id数组
        UUID parentId = userGroupMap.get(parentGroupId).getId();
        userGroupIdList.add(parentId);
        userGroupIdList.addAll(getAllGroupIdByParentId(parentId, userGroupMap));
        return userGroupIdList;
    }

    /**
     * 递归获取用户组下各层子分组id
     *
     * @param parentId 父分组id
     * @param groupMap 所有用户组
     * @return 属于父分组下的分组id
     */
    private List<UUID> getAllGroupIdByParentId(UUID parentId, Map<UUID, IacUserGroupDetailDTO> groupMap) {
        List<UUID> resultList = Lists.newArrayList();
        groupMap.forEach((id, dto) -> {
            if (parentId.equals(dto.getParentId())) {
                resultList.add(dto.getId());
                resultList.addAll(getAllGroupIdByParentId(dto.getId(), groupMap));
            }
        });
        return resultList;
    }

    @Override
    public List<DesktopUsageDTO> listDesktopDayUsage(DesktopUsageIdArrRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Date startTime = request.getStartTime();
        Date endTime = request.getEndTime();
        List<DesktopResourceUsageDayEntity> entityList =
                desktopResourceUsageDayDAO.getByDesktopIdInAndStatisticTimeBetween(request.getIdArr(), startTime, endTime);
        List<DesktopUsageDTO> usageDTOList = new ArrayList<>();
        for (DesktopResourceUsageDayEntity entity : entityList) {
            DesktopUsageDTO dto = new DesktopUsageDTO();
            dto.setDesktopId(entity.getDesktopId());
            dto.setCollectTime(entity.getStatisticTime());
            dto.setCpuUsage(entity.getCpuUsage());
            dto.setMemoryUsage(entity.getMemoryUsage());
            dto.setDiskUsage(entity.getDiskUsage());
            dto.setDesktopName(getDesktopNameById(entity.getDesktopId()));
            usageDTOList.add(dto);
        }
        return usageDTOList;
    }

    @Override
    public List<DesktopUsageDTO> listDesktopUsageById(UUID[] idArr, Date startTime, Date endTime) throws BusinessException {
        Assert.notEmpty(idArr, "idArr cannot be null!");
        Assert.notNull(startTime, "startTime cannot be null!");
        Assert.notNull(endTime, "endTime cannot be null!");

        CbbListVMMonitorInfoDTO apiRequest = new CbbListVMMonitorInfoDTO();
        apiRequest.setStartTime(DateUtil.dateToSecondLong(startTime));
        apiRequest.setEndTime(DateUtil.dateToSecondLong(endTime));

        // 分段请求
        List<DesktopUsageDTO> resultUsageDTOList = Lists.newArrayList();
        int startIndex = 0;
        while (startIndex < idArr.length) {
            List<UUID> tempIdList = Stream.of(idArr).skip(startIndex).limit(Constants.MONITOR_REQUEST_LIMIT)
                .collect(Collectors.toList());
            apiRequest.setVmIdArr(tempIdList.toArray(new UUID[]{}));
            resultUsageDTOList.addAll(getDesktopUsageDtoList(apiRequest));
            startIndex += Constants.MONITOR_REQUEST_LIMIT;
        }
        return resultUsageDTOList;
    }

    private List<DesktopUsageDTO> getDesktopUsageDtoList(CbbListVMMonitorInfoDTO apiRequest) throws BusinessException {
        // 加入基本信息
        Map<UUID, DesktopUsageDTO> usageDTOMap = Maps.newHashMap();
        for (UUID id : apiRequest.getVmIdArr()) {
            DesktopUsageDTO usageDTO = new DesktopUsageDTO();
            usageDTO.setDesktopId(id);
            usageDTO.setDesktopName(getDesktopNameById(id));
            usageDTOMap.put(id, usageDTO);
        }

        List<CbbVMMonitorInfoDTO> vmMonitorInfoDTOList = getListVMMonitorInfo(apiRequest);
        for (CbbVMMonitorInfoDTO infoDTO : vmMonitorInfoDTOList) {
            DesktopUsageDTO usageDTO = usageDTOMap.get(infoDTO.getUuid());
            usageDTO.setCpuUsage(MathUtil.getUsage(processNullList(infoDTO.getCpuUseRateDTOList())));
            usageDTO.setMemoryUsage(MathUtil.getUsage(processNullList(infoDTO.getMemoryUseRateDTOList())));
            usageDTO.setDiskUsage(MathUtil.getUsage(processNullList(infoDTO.getStorageUseRateDTOList())));
        }

        return new ArrayList<>(usageDTOMap.values());
    }

    private List<CbbVMMonitorInfoDTO> getListVMMonitorInfo(CbbListVMMonitorInfoDTO apiRequest) {
        // 查询监控信息
        try {
            return cbbVDIDeskMgmtAPI.listVMMonitorInfo(apiRequest);
        } catch (BusinessException ex) {
            LOGGER.error("调用MonitorMgmtAPI接口异常");
            return Lists.newArrayList();
        }
    }

    private String getDesktopNameById(UUID id) throws BusinessException {

        CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(id);

        if (null == deskDTO) {
            LOGGER.error("获取云桌面信息失败, id={}", id);
            throw new BusinessException(BusinessKey.RCDC_RCO_BIGSCREEN_GET_DESKTOP_INFO_FAIL);
        }
        return deskDTO.getName();
    }

    private List<CbbMonitorDataDTO<Double>> processNullList(List<CbbMonitorDataDTO<Double>> monitorDataList) {
        return null == monitorDataList ? Lists.newArrayList() : monitorDataList;
    }

    @Override
    public List<DesktopResourceUsageDayEntity> listAbnormalDesktop(ResourceTypeEnum resourceType, AbnormalTypeEnum abnormalType)
            throws BusinessException {
        Assert.notNull(resourceType, "resourceType cannot be null!");
        Assert.notNull(abnormalType, "abnormalType cannot be null!");

        List<DesktopResourceUsageDayEntity> resultList = Lists.newArrayList();
        // 获取区间分段点
        NormalDistributionDTO distributionDTO = computeNormalDistributionSegment(resourceType);
        if (null == distributionDTO.getAverageValue()) {
            return resultList;
        }

        // 根据分段点，获取指定区间的异常云桌面
        Double high;
        Double low;
        switch (abnormalType) {
            case ABNORMAL:
                // 异常（高）范围内使用率的云桌面
                high = Constants.DESKTOP_CPU_MAX_USAGE;
                low = distributionDTO.getUltraHighLoadLine();
                resultList.addAll(getDesktopUsageIn(resourceType, low, high));
                // 异常（低）范围内CPU使用率的云桌面
                high = distributionDTO.getUltraLowLoadLine();
                low = Constants.DESKTOP_CPU_MIN_USAGE;
                resultList.addAll(getDesktopUsageIn(resourceType, low, high));
                break;

            case ULTRALOWLOAD:
                high = distributionDTO.getLowLoadLine();
                low = distributionDTO.getUltraLowLoadLine();
                resultList.addAll(getDesktopUsageIn(resourceType, low, high));
                break;

            case ULTRAHIGHLOAD:
                high = distributionDTO.getUltraHighLoadLine();
                low = distributionDTO.getHighLoadLine();
                resultList.addAll(getDesktopUsageIn(resourceType, low, high));
                break;

            default:
                LOGGER.error("获取异常虚机列表失败，原因：不支持列表类型[{}]", abnormalType);
                throw new BusinessException(BusinessKey.RCDC_RCO_BIGSCREEN_ABNORMAL_DESKTOP_LIST_TYPE_ILLEGAL, abnormalType.name());
        }

        // 查询出对应的云桌面名称
        return resultList;
    }

    /**
     * 根据正态分布计算结果，得到异常使用率的区间分段点
     */
    private NormalDistributionDTO computeNormalDistributionSegment(ResourceTypeEnum resourceType) {
        Map<ResourceTypeEnum, DesktopNormalDistributionDTO> desktopNormalDistributionDTOMap =
            desktopNormalDistributionService.normalDistribution();
        DesktopNormalDistributionDTO desktopNormalDistributionDTO = desktopNormalDistributionDTOMap.get(resourceType);
        if (null == desktopNormalDistributionDTO) {
            LOGGER.error("未获取到[{}]使用率正态分布计算结果", resourceType.name());
            return new NormalDistributionDTO();
        }
        return MathUtil.normalDistribution(desktopNormalDistributionDTO.getAverageValue(), desktopNormalDistributionDTO.getStandardDeviation());
    }

    /**
     * 按资源类型获取特定使用率范围内的云桌面
     */
    private List<DesktopResourceUsageDayEntity> getDesktopUsageIn(ResourceTypeEnum resourceType, Double low, Double high) {
        if (ResourceTypeEnum.CPU.equals(resourceType)) {
            return desktopResourceUsageDayDAO.queryCpuUsageInRange(low, high);
        }
        return desktopResourceUsageDayDAO.queryMemoryUsageInRange(low, high);
    }

    @Override
    public void validateImageTemplateDesktopNotRunning(UUID imageId, String imageName) throws BusinessException {
        Assert.notNull(imageId, "imageId cannot be null!");
        Assert.hasText(imageName, "imageName cannot be empty!");

        if (!isAllowEditOrPublishImageTemplateDesktop(imageId)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CLOUDDESKTOP_START_VM_DESKTOP_NOT_CLOSE_ALL_FAIL, imageName);
        }
    }

    private boolean isAllowEditOrPublishImageTemplateDesktop(UUID imageTemplateId) throws BusinessException {
        List<CbbDeskInfoDTO> cbbDeskInfoDTOList = cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate(imageTemplateId);
        boolean isAllow = true;
        for (CbbDeskInfoDTO dto : cbbDeskInfoDTOList) {
            CbbCloudDeskState deskState = dto.getDeskState();
            if (!isDesktopStateAllowEditOrPublish(deskState)) {
                isAllow = false;
                break;
            }
        }

        return isAllow;
    }

    private boolean isDesktopStateAllowEditOrPublish(CbbCloudDeskState deskState) {
        return (CbbCloudDeskState.CLOSE == deskState) || (CbbCloudDeskState.ERROR == deskState) || (CbbCloudDeskState.RECYCLE_BIN == deskState);
    }

    @Override
    public List<UserBindDesktopNumDTO> listUserBindDesktopNum(List<UUID> deskIdList) {
        Assert.notEmpty(deskIdList, "deskIdList is not null");

        return userDesktopDAO.findUserBindDesktopNumByDeskIds(deskIdList);
    }

    @Override
    public boolean existDesktopByTerminalGlobalVmMode(TerminalVmModeTypeEnum terminalVmModeType) {
        Assert.notNull(terminalVmModeType, "terminalVmModeType cannot be null!");

        CbbGlobalVmMode globalVmMode =  cbbGlobalStrategyMgmtAPI.getGlobalVmModeByTerminalVmModeType(terminalVmModeType);

        // 桌面运行策略默认是IDV才有透传、模拟的概念
        long desktopCount = viewDesktopDetailDAO.countByProductTypeInAndCbbImageTypeAndOsTypeIn(
                globalVmMode.getProductTypeList(), globalVmMode.getImageType().toString(), globalVmMode.getOsTypeList());
        if (desktopCount <= 0) {
            LOGGER.info("不存在全局运行策略{}的桌面", terminalVmModeType.toString());
            return false;
        } else {
            LOGGER.info("desktopCount: {}, 存在全局运行策略{}的桌面", desktopCount, terminalVmModeType.toString());
            return true;
        }
    }
}
