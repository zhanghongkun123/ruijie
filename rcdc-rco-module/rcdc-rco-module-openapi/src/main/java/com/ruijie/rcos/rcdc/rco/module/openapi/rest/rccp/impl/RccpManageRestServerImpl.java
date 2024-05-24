package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterGpuInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.PageQueryNullableRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.cluster.RccpClusterResourceResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.storagepool.StoragePoolClustersSummaryResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.SearchStoragePoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiRccpManageAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageSummaryRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.RccpManageRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.dto.RestComputeClustersDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.dto.RestStoragePoolDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.response.ComputeClustersSummaryResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.response.StorageClustersSummaryResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: rccp openapi接口实现
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25 14:57
 *
 * @author lyb
 */
@Service
public class RccpManageRestServerImpl implements RccpManageRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RccpManageRestServerImpl.class);

    private static final String COMPUTE_CLUSTER_ID = "computeClusterId";
    
    private static final String CREATE_TIME = "createTime";

    @Autowired
    OpenApiRccpManageAPI openApiRccpManageAPI;

    @Autowired
    private ClusterAPI clusterAPI;
    
    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private CbbClusterServerMgmtAPI cbbClusterServerMgmtAPI;

    @Override
    public ComputeClustersSummaryResponse computeClustersSummary(PageSummaryRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null.");

        ComputeClustersSummaryResponse response = new ComputeClustersSummaryResponse();
        PageQueryNullableRequest clusterPageQueryRequest = new PageQueryNullableRequest();
        BeanUtils.copyProperties(request, clusterPageQueryRequest);
        PageResponse<RccpClusterResourceResponse> pageResponse = openApiRccpManageAPI.computeClustersSummary(clusterPageQueryRequest);
        LOGGER.info("RccpClusterResourceResponse:{}", JSONObject.toJSONString(pageResponse));
        response.dtoToResponse(pageResponse);
        return response;
    }

    @Override
    public StorageClustersSummaryResponse storageClustersSummary(PageSummaryRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null.");

        StorageClustersSummaryResponse response = new StorageClustersSummaryResponse();
        PageQueryNullableRequest pageQueryNullableRequest = new PageQueryNullableRequest();
        BeanUtils.copyProperties(request, pageQueryNullableRequest);
        PageResponse<StoragePoolClustersSummaryResponse> pageResponse = openApiRccpManageAPI.storageClustersSummary(pageQueryNullableRequest);
        LOGGER.info("storageClustersSummaryResponse:{}", JSONObject.toJSONString(pageResponse));
        response.dtoToResponse(pageResponse);
        return response;
    }

    @Override
    public PageQueryResponse computeClustersPageQuery(PageQueryServerRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        PageResponse<ClusterInfoDTO> pageResponse = clusterAPI.pageQueryComputerCluster(request);
        return convert(pageResponse);
    }

    private PageQueryResponse<RestComputeClustersDTO> convert(PageResponse<ClusterInfoDTO> pageResponse) {
        List<RestComputeClustersDTO> dtoList = new ArrayList<>();
        PageQueryResponse<RestComputeClustersDTO> result = new PageQueryResponse<>();
        if (ArrayUtils.isEmpty(pageResponse.getItems())) {
            result.setTotal(0);
            result.setItemArr(dtoList.toArray(new RestComputeClustersDTO[0]));
            return result;
        }

        Arrays.stream(pageResponse.getItems()).forEach(clusterInfoDTO -> {
            RestComputeClustersDTO dto = new RestComputeClustersDTO();
            BeanUtils.copyProperties(clusterInfoDTO, dto);
            dtoList.add(dto);
        });

        result.setTotal(pageResponse.getTotal());
        result.setItemArr(dtoList.toArray(new RestComputeClustersDTO[dtoList.size()]));
        return result;
    }

    @Override
    public PageQueryResponse storagePoolPageQuery(PageQueryServerRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        DefaultExactMatch[] matchArr = request.getMatchArr();
        SearchStoragePoolDTO storagePoolDTO = new SearchStoragePoolDTO();
        // 设置匹配参数
        if (ArrayUtils.isNotEmpty(matchArr)) {
            for (DefaultExactMatch match : matchArr) {
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(COMPUTE_CLUSTER_ID, match.getFieldName())) {
                    storagePoolDTO.setComputerClusterId(Arrays.stream(match.getValueArr())
                            .map(item -> UUID.fromString(item.toString())).findFirst().orElse(null));
                    continue;
                }
                storagePoolDTO.addMatch(match);
            }
        }
        // 设置排序
        storagePoolDTO.setPageable(getPageRequest(request));

        return convertPageQuery(storagePoolServerMgmtAPI.pageQueryByComputeClusterId(storagePoolDTO));
    }

    @Override
    public List<String> listVGpu(UUID clusterId) throws BusinessException {
        Assert.notNull(clusterId, "clusterId must not be null");
        List<CbbClusterGpuInfoDTO> gpuInfoDTOList = cbbClusterServerMgmtAPI.getClusterGpuInfoByClusterId(clusterId);

        return gpuInfoDTOList.stream().map(CbbClusterGpuInfoDTO::getModel).distinct().collect(Collectors.toList());
    }

    private PageQueryResponse<RestStoragePoolDTO> convertPageQuery(PageQueryResponse<PlatformStoragePoolDTO> queryResponse) {
        PageQueryResponse<RestStoragePoolDTO> result = new PageQueryResponse<>();
        PlatformStoragePoolDTO[] itemArr = queryResponse.getItemArr();
        if (queryResponse.getTotal() == 0 || ArrayUtils.isEmpty(itemArr)) {
            result.setTotal(0);
            return result;
        }

        // 手动处理分页结果
        result.setTotal(queryResponse.getTotal());
        result.setItemArr(Arrays.stream(itemArr).map(storagePoolInfoDTO -> {
            RestStoragePoolDTO dto = new RestStoragePoolDTO();
            BeanUtils.copyProperties(storagePoolInfoDTO, dto);
            return dto;
        }).toArray(RestStoragePoolDTO[]::new));
        return result;
    }

    private Pageable getPageRequest(PageQueryServerRequest request) {
        int page = request.getPage();
        int limit = request.getLimit();
        //未指定排序字段，则以创建时间到序
        if (request.getSortArr() == null) {
            return PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, CREATE_TIME));
        }
        // 有指定排序字段和排方式
        List<Sort.Order> orderList = new ArrayList<>();
        Arrays.stream(request.getSortArr()).forEach(sortVO -> {
            Sort.Direction directionEnum = Sort.Direction.fromString(sortVO.getDirection().name());
            Sort.Order order = new Sort.Order(directionEnum, sortVO.getFieldName());
            orderList.add(order);
        });
        Sort sort = Sort.by(orderList);
        return PageRequest.of(page, limit, sort);
    }
}
