package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ClusterMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.RccpPrometheusMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.cluster.ComputerClusterBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.prometheus.ObtainServerResourceCurrentUsageRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.prometheus.ObtainServerResourceHistoryUsageRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceCurrentUsageResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.shennong.ObtainServerResourceHistoryUsageResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cluster.ClusterNodeGpuInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.shennong.ObtainServerResourceHistoryUsageDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PrometheusResourceMode;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.UseState;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.GpuResourceUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerGpuUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceCurrentUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request.ServerResourceHistoryUsageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.DashboardStatisticsBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dto.ServerResourceHistoryUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dto.StatisticsServerResourceTimeQueryDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.service.StatisticsServerService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.kernel.request.PageQueryConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PrometheusResourceMode.CLUSTER_CAPACITY_TOTAL;
import static com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PrometheusResourceMode.CLUSTER_CAPACITY_USAGE;
import static com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PrometheusResourceMode.CLUSTER_CAPACITY_USED;
import static com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PrometheusResourceMode.CLUSTER_CPU_FREQUENCY_TOTAL;
import static com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PrometheusResourceMode.CLUSTER_CPU_USAGE;
import static com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PrometheusResourceMode.CLUSTER_MEMORY_TOTAL;
import static com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PrometheusResourceMode.CLUSTER_MEMORY_USAGE;
import static com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PrometheusResourceMode.CLUSTER_MEMORY_USED;

/**
 * Description: StatisticsServerServiceImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
@Service
public class StatisticsServerServiceImpl implements StatisticsServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsServerServiceImpl.class);

    @Autowired
    private RccpPrometheusMgmtAPI prometheusMgmtAPI;

    private static final Long ONE_MINUTE_TIME = 60L;

    private static final Long FIVE_MINUTES_TIME = 5 * 60L;

    private static final Long TWELVE_HOURS_TIME = 12 * 60 * 60L;

    private static final Long TWENTY_FOUR_HOURS_TIME = 24 * 60 * 60L;

    private static final Long ONE_HOUR = 60 * 60L;

    private static final Long ONE_DAY = 24 * 60 * 60L;

    private static final Long ONE_MONTH = 30 * 24 * 60 * 60L;

    private static final Long ONE_YEAR = 365 * 24 * 60 * 60L;

    private static final Long ONE_HOUR_AGO = ONE_HOUR - ONE_MINUTE_TIME;

    private static final Long TWENTY_FOUR_HOURS_AGO = ONE_DAY - FIVE_MINUTES_TIME;

    private static final Long ONE_MONTH_AGO = ONE_MONTH - TWELVE_HOURS_TIME;

    private static final Long ONE_YEAR_AGO = ONE_YEAR - TWENTY_FOUR_HOURS_TIME;

    private static final String TIME_UNIT = "s";
    
    private static final String PLATFORM_ID = "platformId";

    @Autowired
    private ClusterMgmtAPI clusterMgmtAPI;
    
    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;
    
    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterServerMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Override
    public ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsageByCpu(ServerResourceHistoryUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        StatisticsServerResourceTimeQueryDTO statisticsServerResourceTimeQueryDTO =
                buildStatisticsServerResourceTimeQueryDTO(request.getTimeQueryType());
        ObtainServerResourceHistoryUsageRequest obtainServerResourceHistoryUsageRequest =
                buildObtainServerResourceHistoryUsageRequest(CLUSTER_CPU_USAGE, statisticsServerResourceTimeQueryDTO,
                        request.getPlatformId());
        LOGGER.info("request={}", JSONObject.toJSONString(obtainServerResourceHistoryUsageRequest));
        ObtainServerResourceHistoryUsageResponse response =
                prometheusMgmtAPI.obtainServerResourceHistoryUsage(obtainServerResourceHistoryUsageRequest);
        buildStatisticsServerResourceResult(response, statisticsServerResourceTimeQueryDTO);
        return response;
    }

    @Override
    public ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsageByMem(ServerResourceHistoryUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        StatisticsServerResourceTimeQueryDTO statisticsServerResourceTimeQueryDTO =
                buildStatisticsServerResourceTimeQueryDTO(request.getTimeQueryType());
        ObtainServerResourceHistoryUsageRequest obtainServerResourceHistoryUsageRequest =
                buildObtainServerResourceHistoryUsageRequest(CLUSTER_MEMORY_USAGE, statisticsServerResourceTimeQueryDTO, request.getPlatformId());
        LOGGER.info("request={}", JSONObject.toJSONString(obtainServerResourceHistoryUsageRequest));
        ObtainServerResourceHistoryUsageResponse response =
                prometheusMgmtAPI.obtainServerResourceHistoryUsage(obtainServerResourceHistoryUsageRequest);
        buildStatisticsServerResourceResult(response, statisticsServerResourceTimeQueryDTO);
        return response;
    }

    @Override
    public ObtainServerResourceHistoryUsageResponse statisticsServerResourceHistoryUsageByDisk(ServerResourceHistoryUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        StatisticsServerResourceTimeQueryDTO statisticsServerResourceTimeQueryDTO =
                buildStatisticsServerResourceTimeQueryDTO(request.getTimeQueryType());
        ObtainServerResourceHistoryUsageRequest obtainServerResourceHistoryUsageRequest =
                buildObtainServerResourceHistoryUsageRequest(CLUSTER_CAPACITY_USAGE, statisticsServerResourceTimeQueryDTO, request.getPlatformId());
        LOGGER.info("request={}", JSONObject.toJSONString(obtainServerResourceHistoryUsageRequest));
        ObtainServerResourceHistoryUsageResponse response =
                prometheusMgmtAPI.obtainServerResourceHistoryUsage(obtainServerResourceHistoryUsageRequest);
        buildStatisticsServerResourceResult(response, statisticsServerResourceTimeQueryDTO);
        return response;
    }

    /*
     * 首页采集间隔：
     * <= 1小时 ： 1分钟
     * (1小时 - 24小时]： 5分钟
     * (1天 - 7天]：1小时
     * (7天 - 1个月] ：12小时
     * (1月 - 1年]：24小时
     */
    private StatisticsServerResourceTimeQueryDTO buildStatisticsServerResourceTimeQueryDTO(TimeQueryTypeEnum timeQueryType) throws BusinessException {
        LOGGER.debug("timeQueryType={}", timeQueryType.toString());
        switch (timeQueryType) {
            case HOUR:
                return buildStatisticsServerResourceTimeQueryDTOByHour();
            case DAY:
                return buildStatisticsServerResourceTimeQueryDTOByDay();
            case MONTHLY:
                return buildStatisticsServerResourceTimeQueryDTOByMonth();
            case YEAR:
                return buildStatisticsServerResourceTimeQueryDTOByYear();
            default:
                throw new BusinessException(DashboardStatisticsBusinessKey.RCDC_RCO_TIME_QUERY_TYPE_NOT_EXIT);
        }
    }

    private void buildStatisticsServerResourceResult(ObtainServerResourceHistoryUsageResponse response,
            StatisticsServerResourceTimeQueryDTO statisticsServerResourceTimeQueryDTO) {
        List<ServerResourceHistoryUsageDTO> serverResourceHistoryUsageDTOList;
        ObtainServerResourceHistoryUsageDTO[] obtainServerResourceHistoryUsageDTOArr = response.getResultArr();
        if (obtainServerResourceHistoryUsageDTOArr != null && obtainServerResourceHistoryUsageDTOArr.length > 0) {
            serverResourceHistoryUsageDTOList = Stream.of(obtainServerResourceHistoryUsageDTOArr[0].getValueArr())
                    .map(v -> new ServerResourceHistoryUsageDTO(v[0], v[1])).collect(Collectors.toList());
            Arrays.stream(statisticsServerResourceTimeQueryDTO.getValueArr()).forEach(dto -> {
                if (!serverResourceHistoryUsageDTOList.stream().anyMatch(o -> o.getDateTime().equals(dto.getDateTime()))) {
                    serverResourceHistoryUsageDTOList.add(new ServerResourceHistoryUsageDTO(dto.getDateTime(), StringUtils.EMPTY));
                }
            });
        } else {
            serverResourceHistoryUsageDTOList = Arrays.stream(statisticsServerResourceTimeQueryDTO.getValueArr()).collect(Collectors.toList());
        }
        LOGGER.debug(JSONObject.toJSONString(serverResourceHistoryUsageDTOList));
        List<ObtainServerResourceHistoryUsageDTO> resultList = Lists.newArrayList();
        ObtainServerResourceHistoryUsageDTO obtainServerResourceHistoryUsageDTO = new ObtainServerResourceHistoryUsageDTO();
        obtainServerResourceHistoryUsageDTO.setMetricMap(Collections.emptyMap());
        obtainServerResourceHistoryUsageDTO.setValueArr(serverResourceHistoryUsageDTOList.stream()
                .sorted(Comparator.comparing(ServerResourceHistoryUsageDTO::getDateTime)).map(v -> new String[] {v.getDateTime(), v.getUsage()})
                .collect(Collectors.toList()).toArray(new String[serverResourceHistoryUsageDTOList.size()][]));
        resultList.add(obtainServerResourceHistoryUsageDTO);
        response.setResultArr(resultList.stream().collect(Collectors.toList()).toArray(new ObtainServerResourceHistoryUsageDTO[] {}));
    }

    private StatisticsServerResourceTimeQueryDTO buildStatisticsServerResourceTimeQueryDTOByHour() {
        StatisticsServerResourceTimeQueryDTO statisticsServerResourceTimeQueryDTO = new StatisticsServerResourceTimeQueryDTO();
        Instant instant = Instant.now();
        statisticsServerResourceTimeQueryDTO.setEndTime(instant.getLong(ChronoField.INSTANT_SECONDS));
        statisticsServerResourceTimeQueryDTO.setStartTime(instant.minusSeconds(ONE_HOUR_AGO).getLong(ChronoField.INSTANT_SECONDS));
        statisticsServerResourceTimeQueryDTO.setStep(ONE_MINUTE_TIME + TIME_UNIT);
        statisticsServerResourceTimeQueryDTO.setValueArr(getLastOneHour(instant.minusSeconds(ONE_HOUR_AGO)));
        return statisticsServerResourceTimeQueryDTO;
    }

    private ServerResourceHistoryUsageDTO[] getLastOneHour(Instant instant) {
        List<ServerResourceHistoryUsageDTO> serverResourceHistoryUsageDTOList = Lists.newArrayList();
        for (int i = 0; i < ONE_HOUR / ONE_MINUTE_TIME; i++) {
            Long time = instant.plusSeconds(i * ONE_MINUTE_TIME).getLong(ChronoField.INSTANT_SECONDS);
            serverResourceHistoryUsageDTOList.add(new ServerResourceHistoryUsageDTO(time.toString(), StringUtils.EMPTY));
        }
        return serverResourceHistoryUsageDTOList.stream().collect(Collectors.toList()).toArray(new ServerResourceHistoryUsageDTO[] {});
    }

    private StatisticsServerResourceTimeQueryDTO buildStatisticsServerResourceTimeQueryDTOByDay() {
        StatisticsServerResourceTimeQueryDTO statisticsServerResourceTimeQueryDTO = new StatisticsServerResourceTimeQueryDTO();
        Instant instant = Instant.now();
        statisticsServerResourceTimeQueryDTO.setEndTime(instant.getLong(ChronoField.INSTANT_SECONDS));
        statisticsServerResourceTimeQueryDTO.setStartTime(instant.minusSeconds(TWENTY_FOUR_HOURS_AGO).getLong(ChronoField.INSTANT_SECONDS));
        statisticsServerResourceTimeQueryDTO.setStep(FIVE_MINUTES_TIME + TIME_UNIT);
        statisticsServerResourceTimeQueryDTO.setValueArr(getLastOneDay(instant.minusSeconds(TWENTY_FOUR_HOURS_AGO)));
        return statisticsServerResourceTimeQueryDTO;
    }

    private ServerResourceHistoryUsageDTO[] getLastOneDay(Instant instant) {
        List<ServerResourceHistoryUsageDTO> serverResourceHistoryUsageDTOList = Lists.newArrayList();
        for (int i = 0; i < ONE_DAY / FIVE_MINUTES_TIME; i++) {
            Long time = instant.plusSeconds(i * FIVE_MINUTES_TIME).getLong(ChronoField.INSTANT_SECONDS);
            serverResourceHistoryUsageDTOList.add(new ServerResourceHistoryUsageDTO(time.toString(), StringUtils.EMPTY));
        }
        return serverResourceHistoryUsageDTOList.stream().collect(Collectors.toList()).toArray(new ServerResourceHistoryUsageDTO[] {});
    }

    private StatisticsServerResourceTimeQueryDTO buildStatisticsServerResourceTimeQueryDTOByMonth() {
        StatisticsServerResourceTimeQueryDTO statisticsServerResourceTimeQueryDTO = new StatisticsServerResourceTimeQueryDTO();
        Instant instant = Instant.now();
        statisticsServerResourceTimeQueryDTO.setEndTime(instant.getLong(ChronoField.INSTANT_SECONDS));
        statisticsServerResourceTimeQueryDTO.setStartTime(instant.minusSeconds(ONE_MONTH_AGO).getLong(ChronoField.INSTANT_SECONDS));
        statisticsServerResourceTimeQueryDTO.setStep(TWELVE_HOURS_TIME + TIME_UNIT);
        statisticsServerResourceTimeQueryDTO.setValueArr(getLastOneMonth(instant.minusSeconds(ONE_MONTH_AGO)));
        return statisticsServerResourceTimeQueryDTO;
    }

    private ServerResourceHistoryUsageDTO[] getLastOneMonth(Instant instant) {
        List<ServerResourceHistoryUsageDTO> serverResourceHistoryUsageDTOList = Lists.newArrayList();
        for (int i = 0; i < ONE_MONTH / TWELVE_HOURS_TIME; i++) {
            Long time = instant.plusSeconds(i * TWELVE_HOURS_TIME).getLong(ChronoField.INSTANT_SECONDS);
            serverResourceHistoryUsageDTOList.add(new ServerResourceHistoryUsageDTO(time.toString(), StringUtils.EMPTY));
        }
        return serverResourceHistoryUsageDTOList.stream().collect(Collectors.toList()).toArray(new ServerResourceHistoryUsageDTO[] {});
    }

    private StatisticsServerResourceTimeQueryDTO buildStatisticsServerResourceTimeQueryDTOByYear() {
        StatisticsServerResourceTimeQueryDTO statisticsServerResourceTimeQueryDTO = new StatisticsServerResourceTimeQueryDTO();
        Instant instant = Instant.now();
        statisticsServerResourceTimeQueryDTO.setEndTime(instant.getLong(ChronoField.INSTANT_SECONDS));
        statisticsServerResourceTimeQueryDTO.setStartTime(instant.minusSeconds(ONE_YEAR_AGO).getLong(ChronoField.INSTANT_SECONDS));
        statisticsServerResourceTimeQueryDTO.setStep(TWENTY_FOUR_HOURS_TIME + TIME_UNIT);
        statisticsServerResourceTimeQueryDTO.setValueArr(getLastOneYear(instant.minusSeconds(ONE_YEAR_AGO)));
        return statisticsServerResourceTimeQueryDTO;
    }

    private ServerResourceHistoryUsageDTO[] getLastOneYear(Instant instant) {
        List<ServerResourceHistoryUsageDTO> serverResourceHistoryUsageDTOList = Lists.newArrayList();
        for (int i = 0; i < ONE_YEAR / TWENTY_FOUR_HOURS_TIME; i++) {
            Long time = instant.plusSeconds(i * TWENTY_FOUR_HOURS_TIME).getLong(ChronoField.INSTANT_SECONDS);
            serverResourceHistoryUsageDTOList.add(new ServerResourceHistoryUsageDTO(time.toString(), StringUtils.EMPTY));
        }
        return serverResourceHistoryUsageDTOList.stream().collect(Collectors.toList()).toArray(new ServerResourceHistoryUsageDTO[]{});
    }

    private ObtainServerResourceHistoryUsageRequest buildObtainServerResourceHistoryUsageRequest(
            PrometheusResourceMode resourceMode, StatisticsServerResourceTimeQueryDTO statisticsServerResourceTimeQueryDTO, UUID platformId) {
        LOGGER.debug("startTime={},endTime={},step={}", statisticsServerResourceTimeQueryDTO.getStartTime(),
                statisticsServerResourceTimeQueryDTO.getEndTime(), statisticsServerResourceTimeQueryDTO.getStep());
        ObtainServerResourceHistoryUsageRequest obtainServerResourceHistoryUsageRequest = new ObtainServerResourceHistoryUsageRequest();
        obtainServerResourceHistoryUsageRequest.setArgs(Collections.emptyMap());
        obtainServerResourceHistoryUsageRequest.setResourceMode(resourceMode);
        obtainServerResourceHistoryUsageRequest.setStart(statisticsServerResourceTimeQueryDTO.getStartTime());
        obtainServerResourceHistoryUsageRequest.setEnd(statisticsServerResourceTimeQueryDTO.getEndTime());
        obtainServerResourceHistoryUsageRequest.setStep(statisticsServerResourceTimeQueryDTO.getStep());
        obtainServerResourceHistoryUsageRequest.setPlatformId(platformId);
        return obtainServerResourceHistoryUsageRequest;
    }

    @Override
    public ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentUsageByCpu(ServerResourceCurrentUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        ObtainServerResourceCurrentUsageRequest obtainServerResourceCurrentUsageRequest =
                buildObtainServerResourceCurrentUsageRequest(CLUSTER_CPU_USAGE, request.getPlatformId());
        LOGGER.debug("request={}", JSONObject.toJSONString(obtainServerResourceCurrentUsageRequest));
        return prometheusMgmtAPI.obtainServerResourceCurrentUsage(obtainServerResourceCurrentUsageRequest);
    }

    @Override
    public ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentUsageByMem(ServerResourceCurrentUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        ObtainServerResourceCurrentUsageRequest obtainServerResourceCurrentUsageRequest =
                buildObtainServerResourceCurrentUsageRequest(CLUSTER_MEMORY_USED, request.getPlatformId());
        LOGGER.debug("request={}", JSONObject.toJSONString(obtainServerResourceCurrentUsageRequest));
        return prometheusMgmtAPI.obtainServerResourceCurrentUsage(obtainServerResourceCurrentUsageRequest);
    }

    @Override
    public ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentUsageByDisk(ServerResourceCurrentUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        ObtainServerResourceCurrentUsageRequest obtainServerResourceCurrentUsageRequest =
                buildObtainServerResourceCurrentUsageRequest(CLUSTER_CAPACITY_USED, request.getPlatformId());
        LOGGER.debug("request={}", JSONObject.toJSONString(obtainServerResourceCurrentUsageRequest));
        return prometheusMgmtAPI.obtainServerResourceCurrentUsage(obtainServerResourceCurrentUsageRequest);
    }

    @Override
    public ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentTotalByCpu(ServerResourceCurrentUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        ObtainServerResourceCurrentUsageRequest obtainServerResourceCurrentUsageRequest =
                buildObtainServerResourceCurrentUsageRequest(CLUSTER_CPU_FREQUENCY_TOTAL, request.getPlatformId());
        LOGGER.debug("request={}", JSONObject.toJSONString(obtainServerResourceCurrentUsageRequest));
        return prometheusMgmtAPI.obtainServerResourceCurrentUsage(obtainServerResourceCurrentUsageRequest);
    }

    @Override
    public ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentTotalByMem(ServerResourceCurrentUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        ObtainServerResourceCurrentUsageRequest obtainServerResourceCurrentUsageRequest =
                buildObtainServerResourceCurrentUsageRequest(CLUSTER_MEMORY_TOTAL, request.getPlatformId());
        LOGGER.debug("request={}", JSONObject.toJSONString(obtainServerResourceCurrentUsageRequest));
        return prometheusMgmtAPI.obtainServerResourceCurrentUsage(obtainServerResourceCurrentUsageRequest);
    }

    @Override
    public ObtainServerResourceCurrentUsageResponse statisticsServerResourceCurrentTotalByDisk(ServerResourceCurrentUsageRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        ObtainServerResourceCurrentUsageRequest obtainServerResourceCurrentUsageRequest =
                buildObtainServerResourceCurrentUsageRequest(CLUSTER_CAPACITY_TOTAL, request.getPlatformId());
        LOGGER.debug("request={}", JSONObject.toJSONString(obtainServerResourceCurrentUsageRequest));
        return prometheusMgmtAPI.obtainServerResourceCurrentUsage(obtainServerResourceCurrentUsageRequest);
    }

    @Override
    public GpuResourceUsageDTO getGpuResourceUsage(ServerGpuUsageRequest gpuUsageRequest) throws BusinessException {
        Assert.notNull(gpuUsageRequest, "gpuUsageRequest is not null");
        
        UUID platformId = Objects.isNull(gpuUsageRequest.getPlatformId()) ? 
                cloudPlatformManageAPI.getDefaultCloudPlatform().getId() : gpuUsageRequest.getPlatformId();
        GpuResourceUsageDTO gpuResourceUsageDTO = new GpuResourceUsageDTO();
        Set<UUID> clusterIdSet = getClusterId(platformId);

        if (CollectionUtils.isEmpty(clusterIdSet)) {
            LOGGER.debug("查询到计算集群信息为空");
            return gpuResourceUsageDTO;
        }
        List<ClusterNodeGpuInfo> gpuDataDTOList = Lists.newArrayList();
        for (UUID clusterId : clusterIdSet) {
            ComputerClusterBaseRequest request = new ComputerClusterBaseRequest(clusterId, platformId);
            gpuDataDTOList.addAll(Arrays.asList(clusterMgmtAPI.getClusterGpuInfo(request)));
        }
        
        if (CollectionUtils.isEmpty(gpuDataDTOList)) {
            LOGGER.debug("查询到GPU信息为空");
            return gpuResourceUsageDTO;
        }

        LOGGER.debug("集群GPU信息：", JSON.toJSONString(gpuResourceUsageDTO));
        Long usageGpuMemorySize = 0L;
        Long totalGpuMemorySize = 0L;

        for (ClusterNodeGpuInfo gpuDataDTO : gpuDataDTOList) {
            totalGpuMemorySize += gpuDataDTO.getGraphicsMemorySize();

            for (VgpuDTO vGpuDetailDTO : gpuDataDTO.getVGpuArr()) {
                if (vGpuDetailDTO.getUseState() == UseState.IN_USE) {
                    usageGpuMemorySize += vGpuDetailDTO.getGraphicsMemorySize();
                }
            }
        }

        gpuResourceUsageDTO.setUsageGpuMemorySize(usageGpuMemorySize);
        gpuResourceUsageDTO.setTotalGpuMemorySize(totalGpuMemorySize);
        return gpuResourceUsageDTO;
    }

    private ObtainServerResourceCurrentUsageRequest buildObtainServerResourceCurrentUsageRequest(PrometheusResourceMode resourceMode,
                                                                                                 UUID platformId) {
        ObtainServerResourceCurrentUsageRequest obtainServerResourceCurrentUsageRequest = new ObtainServerResourceCurrentUsageRequest();
        obtainServerResourceCurrentUsageRequest.setArgs(Collections.emptyMap());
        obtainServerResourceCurrentUsageRequest.setResourceMode(resourceMode);
        obtainServerResourceCurrentUsageRequest.setPlatformId(platformId);
        return obtainServerResourceCurrentUsageRequest;
    }

    private Set<UUID> getClusterId(UUID platformId) throws BusinessException {
        Set<UUID> clusterIdSet = Sets.newHashSet();
        Integer page = PageQueryConstant.DEFAULT_PAGE;
        while (true) {
            PageQueryRequest request = pageQueryBuilderFactory.newRequestBuilder()
                    .setPageLimit(page++, PageQueryConstant.MAX_LIMIT).eq(PLATFORM_ID, platformId).build();
            PageQueryResponse<PlatformComputerClusterDTO> response = computerClusterServerMgmtAPI.pageQuery(request);
            if (response == null || ArrayUtils.isEmpty(response.getItemArr())) {
                LOGGER.debug("从HCI查询云平台[{}]计算集群第[{}]页数据为空，无需继续查询", platformId, page);
                return clusterIdSet;
            }
            clusterIdSet.addAll(Arrays.stream(response.getItemArr()).map(PlatformComputerClusterDTO::getId).collect(Collectors.toSet()));
        }
    }
}
