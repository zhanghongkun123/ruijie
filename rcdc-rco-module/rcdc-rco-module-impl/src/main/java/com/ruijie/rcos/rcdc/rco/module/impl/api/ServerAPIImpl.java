package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbServerStatusType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.RccpComputeMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.CommonPageQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ConfigKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetServerHistoryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ServerForecastRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.*;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ServerForecastCache;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.compute.ComputeHostInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.compute.ComputeHostListResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CommonConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHistoryDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageHourDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CommonConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHistoryEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageHourEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ServerService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.pagekit.kernel.request.PageQueryConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 服务器管理API接口实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月10日
 *
 * @author brq
 */
public class ServerAPIImpl implements ServerAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerAPIImpl.class);

    // 服务器线性回归数据天数区间
    private static final Integer FORECAST_DAYS = 180;
    
    private static final Integer DEFAULT_ESTIMATE = 0;

    @Autowired
    private ServerService serverService;

    @Autowired
    private ServerResourceUsageHourDAO serverResourceUsageHourDAO;

    @Autowired
    private ServerResourceUsageDayDAO serverResourceUsageDayDAO;

    @Autowired
    private ServerResourceUsageHistoryDAO serverResourceUsageHistoryDAO;

    @Autowired
    private ServerForecastCache serverForecastCache;

    @Autowired
    private CommonConfigDAO commonConfigDAO;

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private RccpComputeMgmtAPI computeMgmtAPI;
    
    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Override
    public ServerInfoResponse getServerInfo(IdRequest request) throws BusinessException {
        Assert.notNull(request, "IdRequest cannot be null!");

        ServerInfoResponse response = new ServerInfoResponse();

        // 返回服务器基础信息
        ServerUsageDTO serverInfoDTO = getServerBasicInfo(request.getId());

        if (CbbServerStatusType.RUNNING == serverInfoDTO.getServerStatus()) {
            // 从本地库中获取服务器最新监控信息
            ServerResourceUsageHistoryEntity entity =
                serverResourceUsageHistoryDAO.findFirstByServerIdOrderByCollectTimeDesc(request.getId());
            if (null != entity) {
                BeanUtils.copyProperties(entity, serverInfoDTO);
            }
        }

        // 设置服务器中的桌面信息
        setDesktopInfo(serverInfoDTO);

        response.setServerUsageDTO(serverInfoDTO);
        return response;
    }

    private ServerUsageDTO getServerBasicInfo(UUID id) throws BusinessException {
        ServerUsageDTO serverInfoDTO = new ServerUsageDTO();


        PhysicalServerDTO physicalServerDTO = cbbPhysicalServerMgmtAPI.getPhysicalServer(id);
        if (null == physicalServerDTO) {
            LOGGER.error("服务器信息不存在，id = {}", id);
            throw new BusinessException(BusinessKey.RCDC_RCO_CABINET_SERVER_INFO_NOT_EXIST);
        }

        serverInfoDTO.setIp(physicalServerDTO.getIp());
        serverInfoDTO.setServerId(physicalServerDTO.getId());
        serverInfoDTO.setServerName(physicalServerDTO.getHostName());
        serverInfoDTO.setServerStatus(physicalServerDTO.getCbbServerStatusType());
        return serverInfoDTO;
    }

    private void setDesktopInfo(ServerUsageDTO serverInfoDTO) {
        List<CbbDeskInfoDTO> infoDTOList = getDeskInfoDTOList(serverInfoDTO.getServerId());
        if (!CollectionUtils.isEmpty(infoDTOList)) {
            Long online = infoDTOList.stream().filter(o -> CbbCloudDeskState.RUNNING.equals(o.getDeskState())).count();
            serverInfoDTO.setOnline(online);
            serverInfoDTO.setTotal(infoDTOList.size());
        }
    }

    /**
     * 获取服务器关联的云桌面列表
     * @param serverId 服务器id
     * @return List<CbbDeskInfoDTO> 服务器关联的云桌面列表
     */
    private List<CbbDeskInfoDTO> getDeskInfoDTOList(UUID serverId) {

        List<CbbDeskInfoDTO> deskInfoDTOList = cbbVDIDeskMgmtAPI.listDeskInfoByServer(serverId);

        if (CollectionUtils.isEmpty(deskInfoDTOList)) {
            return Lists.newArrayList();
        }

        return deskInfoDTOList;
    }

    @Override
    public ServerHistoryResponse getServerHistoryForHourStep(GetServerHistoryRequest request) {
        Assert.notNull(request, "request is null!");

        ServerHistoryResponse response = new ServerHistoryResponse();
        List<ServerResourceUsageHourEntity> entitiesList = serverResourceUsageHourDAO
                .listByIdAndStatisticTime(request.getServerId(), request.getStartTime(), request.getEndTime());
        if (CollectionUtils.isEmpty(entitiesList)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("没有Id为[{}]的服务器的数据", request.getServerId());
            }
            return response;
        }
        List<ServerResourceUsageDTO> dtoList = entitiesList.stream().map(entity -> convertEntityToDTO(entity)
        ).collect(Collectors.toList());
        response.setServerResourceUsageList(dtoList);
        return response;
    }

    /**
     * @param entity entity参数
     * @return 返回dto
     */
    private ServerResourceUsageDTO convertEntityToDTO(ServerResourceUsageHourEntity entity) {
        ServerResourceUsageDTO dto = new ServerResourceUsageDTO();
        dto.setCpuUsage(entity.getCpuUsage());
        dto.setDate(entity.getStatisticTime());
        dto.setMemoryUsage(entity.getMemoryUsage());
        dto.setDiskUsage(entity.getDiskUsage());
        return dto;
    }

    @Override
    public ServerHistoryResponse getServerHistoryForDayStep(GetServerHistoryRequest request) {
        Assert.notNull(request, "request is null");

        ServerHistoryResponse response = new ServerHistoryResponse();
        List<ServerResourceUsageDayEntity> entityList = serverResourceUsageDayDAO
                .listByIdAndStatisticTime(request.getServerId(), request.getStartTime(), request.getEndTime());
        if (CollectionUtils.isEmpty(entityList)) {
            return response;
        }

        List<ServerResourceUsageDTO> dtoList = entityList.stream().map(entity -> {
            ServerResourceUsageDTO dto = new ServerResourceUsageDTO();
            dto.setDate(entity.getStatisticTime());
            dto.setCpuUsage(entity.getCpuUsage());
            dto.setMemoryUsage(entity.getMemoryUsage());
            dto.setDiskUsage(entity.getDiskUsage());
            return dto;
        }).collect(Collectors.toList());

        response.setServerResourceUsageList(dtoList);
        return response;
    }

    @Override
    public ServerForecastResponse getServerForecast(ServerForecastRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");

        ServerForecastResponse response = new ServerForecastResponse();
        // 优先从缓存取
        ServerForecastDTO dto = serverForecastCache.getCache(request.getServerId(), request.getResourceType());
        if (dto != null) {
            response.setServerForecast(dto);
            return response;
        }

        // 缓存没有时计算
        dto = generateServerForecast(request);
        response.setServerForecast(dto);
        if (null != dto.getEstimate() && DEFAULT_ESTIMATE != dto.getEstimate()) {
            // 存放缓存
            serverForecastCache.addCache(request.getServerId(), request.getResourceType(), dto);
        }
        return response;
    }

    private ServerForecastDTO generateServerForecast(ServerForecastRequest request) throws BusinessException {
        ResourceTypeEnum resourceType = request.getResourceType();
        Map<ConfigKeyEnum, CommonConfigEntity> configMap = findAllConfig();

        ServerForecastDTO dto = new ServerForecastDTO();
        // 设置告警天数参数值
        dto.setForecastDays(Integer.valueOf(getConfigValue(resourceType.getTimeKey(), configMap)));
        // 设置告警阈值
        dto.setThreshold(Double.valueOf(getConfigValue(resourceType.getLimitKey(), configMap)));

        forecast(request, dto);
        return dto;
    }

    private String getConfigValue(ConfigKeyEnum configKey, Map<ConfigKeyEnum, CommonConfigEntity> configMap) throws BusinessException {
        CommonConfigEntity entity = configMap.get(configKey);
        if (entity == null) {
            LOGGER.error("the config entity[" + configKey + "] is null.");
            throw new BusinessException(BusinessKey.RCDC_RCO_BIGSCREEN_COMMON_CONFIG_NOT_FOUND);
        }

        return entity.getConfigValue();
    }

    /**
     * 获取所有大屏参数配置
     *
     * @return Map<ConfigKeyEnum, CommonConfigEntity>
     */
    private Map<ConfigKeyEnum, CommonConfigEntity> findAllConfig() {
        List<CommonConfigEntity> entityList = commonConfigDAO.findAll();
        return entityList.stream().collect(Collectors.toMap(CommonConfigEntity::getConfigKey, o -> o));
    }

    private void forecast(ServerForecastRequest request, ServerForecastDTO dto) {
        UUID serverId = request.getServerId();
        ResourceTypeEnum resourceType = request.getResourceType();

        List<ServerResourceUsageDayEntity> entityList = getServerResourceUsageDayEntities(serverId);
        if (entityList.size() < 1) {
            LOGGER.info("数据不足，无法预测！");
            return;
        }

        Compute compute = compute(resourceType, entityList);

        // 根据线性回归公式计算拟合的回归线坐标
        dto.setRegressionLineList(generateRegressionLineList(entityList, compute));

        if (compute.getA() <= 0) {
            return;
        }

        dto.setEstimate(computeAlarmDays(dto, entityList, compute));
    }

    private Integer computeAlarmDays(ServerForecastDTO dto, List<ServerResourceUsageDayEntity> entityList, Compute compute) {

        List<Integer> xList = compute.getXList();
        int last = entityList.size() - 1;

        if (dto.getThreshold() <= compute.getB()) {
            return 0;
        }
        int thresholdTime = (int) Math.ceil((dto.getThreshold() - compute.getB()) / compute.getA());
        if (thresholdTime < xList.get(last)) {
            return 0;
        }
        return thresholdTime - xList.get(last);
    }

    private List<RegressionLineDTO> generateRegressionLineList(List<ServerResourceUsageDayEntity> entityList, Compute compute) {
        List<Integer> xList = compute.getXList();
        float a = compute.getA();
        Double b = compute.getB();

        List<RegressionLineDTO> regressionLineList = Lists.newArrayList();
        for (int x = 0; x < entityList.size(); x++) {
            regressionLineList.add(generateRegressionLineDTO(entityList.get(x), (a * xList.get(x) + b)));
        }
        return regressionLineList;
    }

    private RegressionLineDTO generateRegressionLineDTO(ServerResourceUsageDayEntity usageEntity, Double usage) {
        RegressionLineDTO regressionLine = new RegressionLineDTO();
        regressionLine.setDate(usageEntity.getStatisticTime());
        regressionLine.setResourceUsage(usage);
        return regressionLine;
    }

    private List<ServerResourceUsageDayEntity> getServerResourceUsageDayEntities(UUID serverId) {
        Date endTime = DateUtil.localDateToDate(LocalDate.now());
        Date startTime = DateUtil.localDateToDate(LocalDate.now().minusDays(FORECAST_DAYS));
        return serverResourceUsageDayDAO.listByIdAndStatisticTime(serverId, startTime, endTime);
    }

    @Override
    public ServerAverageUsageResponse getServerAverageUsage(GetServerHistoryRequest request) {
        Assert.notNull(request, "request must not be null");

        List<ServerResourceUsageDayEntity> entityList = serverResourceUsageDayDAO
                .listByIdAndStatisticTime(request.getServerId(), request.getStartTime(), request.getEndTime());
        if (CollectionUtils.isEmpty(entityList)) {
            return new ServerAverageUsageResponse();
        }
        Double cpuUsageAverage = entityList.stream().mapToDouble(ServerResourceUsageDayEntity::getCpuUsage)
                .summaryStatistics().getAverage();
        Double memoryUsageAverage = entityList.stream().mapToDouble(ServerResourceUsageDayEntity::getMemoryUsage)
                .summaryStatistics().getAverage();
        Double diskUsageAverage = entityList.stream().mapToDouble(ServerResourceUsageDayEntity::getDiskUsage)
                .summaryStatistics().getAverage();
        return new ServerAverageUsageResponse(cpuUsageAverage, memoryUsageAverage, diskUsageAverage);
    }

    @Override
    public ListPhysicalServerCabinetResponse listAllPhysicalServer(DefaultRequest request) {
        Assert.notNull(request, "request不能为null");

        ListPhysicalServerCabinetResponse listPhysicalServerCabinetResponse = new ListPhysicalServerCabinetResponse();

        List<PhysicalServerInfoDTO> dtoList = serverService.listAllPhysicalServer();
        for (PhysicalServerInfoDTO dto : dtoList) {
            ServerResourceUsageHistoryEntity entity =
                serverResourceUsageHistoryDAO.findFirstByServerIdOrderByCollectTimeDesc(dto.getId());
            
            if (entity == null) {
                continue;
            }
            
            if (CbbServerStatusType.RUNNING == dto.getServerStatus()) {
                dto.setCpuUsage(entity.getCpuUsage());
                dto.setMemoryUsage(entity.getMemoryUsage());
                dto.setDiskUsage(entity.getDiskUsage());
            }
        }

        listPhysicalServerCabinetResponse.setPhysicalServerDTOList(dtoList);
        return listPhysicalServerCabinetResponse;
    }

    private Compute compute(ResourceTypeEnum resourceType, List<ServerResourceUsageDayEntity> entityList) {
        List<Integer> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        Integer xSum = 0;
        Double ySum = (double) 0;
        int size = entityList.size();

        for (int i = 0; i < size; i++) {
            Integer xTemp = i + 1;
            xList.add(xTemp);
            xSum += xTemp;

            Double yTemp = entityList.get(i).getUsage(resourceType);
            yList.add(yTemp);
            ySum += yTemp;
        }

        float xAvg = xSum / (float)size;
        Double yAvg = ySum / size;

        // 分子
        float molecular = 0;
        // 分母
        float denominator = 0;
        for (int i = 0; i < size; i++) {
            molecular += (xList.get(i) - xAvg) * (yList.get(i) - yAvg);
            denominator += (xList.get(i) - xAvg) * (xList.get(i) - xAvg);
        }

        if (denominator == 0) {
            LOGGER.error("Divisor cannot be zero");
            throw new ArithmeticException("Divisor cannot be zero");
        } else {
            // 计算斜率
            float a = molecular / denominator;
            Double b = yAvg - a * xAvg;
            return new Compute(xList, a, b);
        }
    }

    /**
     * Compute类
     */
    private static class Compute {

        private List<Integer> xList;

        private float a;

        private Double b;

        Compute(List<Integer> xList, float a, Double b) {
            this.xList = xList;
            this.a = a;
            this.b = b;
        }

        public List<Integer> getXList() {
            return xList;
        }

        public float getA() {
            return a;
        }

        public Double getB() {
            return b;
        }
    }

    @Override
    public List<String> getServerCPU() {
        List<String> cpuModelNameList = Lists.newArrayList();
        try {
            ComputeHostListResponse computeHostListResponse = 
                    computeMgmtAPI.getHostList(buildRequest(cloudPlatformManageAPI.getDefaultCloudPlatform().getId()));
            LOGGER.info("获取主机列表，数据为：{}", JSON.toJSONString(computeHostListResponse));
            cpuModelNameList =
                    Stream.of(computeHostListResponse.getComputeHostInfoDTOArr())
                            .map(ComputeHostInfoDTO::getCpuModelName).collect(Collectors.toList());
            LOGGER.info("获取到服务器的CPU类型数据，cpuModelNameList=[{}]", JSON.toJSONString(cpuModelNameList));
        } catch (BusinessException e) {
            LOGGER.error("获取到服务器的CPU类型数据发生异常", e);
        }
        return cpuModelNameList;
    }

    @Override
    public ServerHostStatusResponse statisticsServerHostStatus() throws BusinessException {
        ServerHostStatusResponse response = new ServerHostStatusResponse();
        try {
            List<PhysicalServerDTO> physicalServerDTOList = cbbPhysicalServerMgmtAPI.listAllPhysicalServer(true);
            if (CollectionUtils.isEmpty(physicalServerDTOList)) {
                setDefaultServerHost(response);
                return response;
            }
            operateServerHostStatus(response, physicalServerDTOList);
        } catch (Exception e) {
            LOGGER.error("统计云主机各种状态数量发生异常", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_BIGSCREEN_SERVER_HOST_COUNT_ERROR, e);
        }
        return response;
    }
    
    private CommonPageQueryRequest buildRequest(UUID platformId) {
        CommonPageQueryRequest pageQueryRequest = new CommonPageQueryRequest();
        pageQueryRequest.setPage(PageQueryConstant.DEFAULT_PAGE);
        pageQueryRequest.setLimit(PageQueryConstant.MAX_LIMIT);
        pageQueryRequest.setPlatformId(platformId);
        return pageQueryRequest;
    }

    /**
     * 统计服务器状态数量
     * @param response  返回云桌面状态数量
     * @param physicalServerDTOList  云主机实体类DTO
     */
    private void operateServerHostStatus(ServerHostStatusResponse response, List<PhysicalServerDTO> physicalServerDTOList) {
        if (CollectionUtils.isEmpty(physicalServerDTOList)) {
            setDefaultServerHost(response);
            return;
        }
        int normalCount = 0;
        int abnormalCount = 0;
        for (PhysicalServerDTO dto : physicalServerDTOList) {
            if (dto.getCbbServerStatusType() == CbbServerStatusType.ONLINE) {
                normalCount ++;
                continue;
            }
            abnormalCount ++;
        }
        response.setNormalCount(normalCount);
        response.setAbnormalCount(abnormalCount);
    }

    /**
     * 设置云主机默认值
     * @param response
     */
    private void setDefaultServerHost(ServerHostStatusResponse response) {
        response.setNormalCount(ServerHostStatusResponse.NORMAL_COUNT_DEFAULT);
        response.setAbnormalCount(ServerHostStatusResponse.ABNORMAL_COUNT_DEFAULT);
    }
}
