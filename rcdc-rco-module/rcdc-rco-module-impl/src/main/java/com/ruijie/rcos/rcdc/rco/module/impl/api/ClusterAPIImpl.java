package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudClusterStateEnums;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.StoragePoolHealthState;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VDIDesktopValidateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.kernel.request.PageQueryConstant;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 集群相关API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/30
 *
 * @author TD
 */
public class ClusterAPIImpl implements ClusterAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterAPIImpl.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;
    
    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterServerMgmtAPI;
    
    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Override
    public List<ClusterInfoDTO> queryAllClusterInfoList() throws BusinessException {
        List<ClusterInfoDTO> clusterInfoList = Lists.newArrayList();
        Integer page = PageQueryConstant.DEFAULT_PAGE;
        while (true) {
            PageQueryRequest request = pageQueryBuilderFactory.newRequestBuilder().setPageLimit(page++, PageQueryConstant.MAX_LIMIT).build();
            PageQueryResponse<PlatformComputerClusterDTO> response = computerClusterServerMgmtAPI.pageQuery(request);
            if (response == null || ArrayUtils.isEmpty(response.getItemArr())) {
                LOGGER.debug("从CCP查询第[{}]页数据为空，无需继续查询", page);
                break;
            }
            clusterInfoList.addAll(Arrays.stream(response.getItemArr()).map(clusterItemDTO -> {
                ClusterInfoDTO clusterInfoDTO = new ClusterInfoDTO();
                BeanUtils.copyProperties(clusterItemDTO, clusterInfoDTO);
                if (clusterItemDTO.getArchitecture() != null) {
                    clusterInfoDTO.setArchSet(Sets.newHashSet(clusterItemDTO.getArchitecture()));
                }
                return clusterInfoDTO;
            }).collect(Collectors.toList()));
        }
        return clusterInfoList;
    }

    @Override
    public Boolean existsCluster(UUID clusterId) {
        Assert.notNull(clusterId, "clusterId is not null");
        try {
            return Objects.nonNull(computerClusterServerMgmtAPI.getComputerClusterInfoById(clusterId));
        } catch (BusinessException e) {
            LOGGER.error("计算集群ID:{}对应的集群不存在，请检查对应集群状态", clusterId, e);
        }
        return false;
    }

    @Override
    public ClusterInfoDTO queryAvailableClusterById(UUID clusterId) {
        Assert.notNull(clusterId, "clusterId is not null");
        ClusterInfoDTO clusterInfo = new ClusterInfoDTO();
        try {
            PlatformComputerClusterDTO computerClusterDTO = computerClusterServerMgmtAPI.getComputerClusterInfoById(clusterId);
            BeanUtils.copyProperties(computerClusterDTO, clusterInfo);
            clusterInfo.setArchSet(Sets.newHashSet(computerClusterDTO.getArchitecture()));
        } catch (BusinessException e) {
            LOGGER.error("查询计算集群[{}]出错", clusterId, e);
        }
        return clusterInfo;
    }

    @Override
    public void validateVDIDesktopConfig(VDIDesktopValidateDTO validateDTO) throws BusinessException {
        Assert.notNull(validateDTO, "validateDTO can be not null");
        // 校验云平台是否可用
        CloudPlatformDTO cloudPlatformDTO = cloudPlatformManageAPI.getInfoById(validateDTO.getPlatformId());
        if (!CloudPlatformStatus.isAvailable(cloudPlatformDTO.getStatus())) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_ASSIGNED_CLOUD_PLATFORM_NOT_AVAILABLE,
                    cloudPlatformDTO.getName());
        }
        UUID clusterId = validateDTO.getClusterId();
        UUID imageId = validateDTO.getImageId();
        // 校验输入计算集群是否存在以及状态是否健康
        PlatformComputerClusterDTO currentCluster = validateComputerClusterExist(clusterId);
        // 获取镜像所属计算集群信息
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        // 判断镜像CPU架构与所选计算集群是否一致
        if (Objects.nonNull(imageTemplateDetail.getClusterInfo())) {
            PlatformComputerClusterDTO computerClusterDTO =
                    computerClusterServerMgmtAPI.getComputerClusterInfoById(imageTemplateDetail.getClusterInfo().getId());
            if (!Objects.equals(currentCluster.getArchitecture(), computerClusterDTO.getArchitecture())) {
                throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_CLUSTER_CPU_FRAMEWORK_NOT_AGREEMENT, imageTemplateDetail.getImageName(),
                        currentCluster.getClusterName());
            }
        }
        UUID networkId = validateDTO.getNetworkId();
        CbbDeskNetworkDetailDTO networkDetailDTO = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
        // 网络策略与运行位置不在同一个计算集群
        if (!Objects.equals(networkDetailDTO.getClusterId(), clusterId)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_NETWORK_NOT_CLUSTER, 
                    networkDetailDTO.getDeskNetworkName(), currentCluster.getClusterName());
        }
        // 判断云平台是否存在，以及计算集群是否属于该云平台
        if (!Objects.equals(cloudPlatformDTO.getId(), currentCluster.getPlatformId())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_CLUSTER_NOT_CLOUD_PLATFORM,
                    currentCluster.getClusterName(), cloudPlatformDTO.getName());
        }

        if (Objects.nonNull(validateDTO.getDeskSpec())) {
            // 批量查询存储信息，所有存储都校验一遍
            CbbDeskSpecDTO deskSpecDTO = validateDTO.getDeskSpec();
            List<UUID> storagePoolIdList = mergeStoragePoolId(deskSpecDTO);
            // 查询计算集群下的存储列表
            List<PlatformStoragePoolDTO> storagePoolList = storagePoolServerMgmtAPI.getStoragePoolByComputeClusterId(clusterId);
            Set<UUID> storagePoolIdSet = storagePoolList.stream().map(PlatformStoragePoolDTO::getId).collect(Collectors.toSet());
            for (UUID storagePoolId : storagePoolIdList) {
                // 存储位置与运行位置不在同一个计算集群
                if (!storagePoolIdSet.contains(storagePoolId)) {
                    // 校验输入存储池是否存在以及状态是否健康
                    PlatformStoragePoolDTO noMatchStoragePool = validateStoragePoolExist(storagePoolId);
                    throw new BusinessException(BusinessKey.RCDC_RCO_STORAGE_NOT_CLUSTER, noMatchStoragePool.getName(),
                            currentCluster.getClusterName());
                }
            }
        }
    }

    @Override
    public Set<UUID> queryClusterToSetByStoragePoolId(UUID storagePoolId) throws BusinessException {
        Assert.notNull(storagePoolId, "storagePoolId can be not null");
        Set<UUID> clusterIdSet = storagePoolServerMgmtAPI.getClusterIdSetByStoragePoolId(storagePoolId);
        return CollectionUtils.isEmpty(clusterIdSet) ? Sets.newHashSet() : clusterIdSet;
    }

    @Override
    public UUID queryClusterIdByName(UUID platformId, String clusterName) {
        Assert.hasText(clusterName, "clusterName is not empty");
        Assert.notNull(platformId, "platformId can be not null");

        PlatformComputerClusterDTO clusterDTO = computerClusterServerMgmtAPI.getComputerClusterByClusterName(platformId, clusterName);
        //找不到该计算集群
        return Objects.isNull(clusterDTO) ? null : clusterDTO.getId();
    }

    @Override
    public void validateVDIDesktopNetwork(UUID clusterId, UUID networkId) throws BusinessException {
        Assert.notNull(clusterId, "validateVDIDesktopNetwork clusterId can be not null");
        Assert.notNull(networkId, "validateVDIDesktopNetwork networkId can be not null");
        CbbDeskNetworkDetailDTO networkDetailDTO = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
        // 网络策略与运行位置不在同一个计算集群
        if (!Objects.equals(networkDetailDTO.getClusterId(), clusterId)) {
            PlatformComputerClusterDTO computerClusterDTO = computerClusterServerMgmtAPI.getComputerClusterInfoById(clusterId);
            throw new BusinessException(BusinessKey.RCDC_RCO_NETWORK_NOT_CLUSTER,
                    networkDetailDTO.getDeskNetworkName(), computerClusterDTO.getClusterName());
        }
    }

    @Override
    public void validateVDIImageTemplateFramework(UUID clusterId, UUID imageId) throws BusinessException {
        Assert.notNull(clusterId, "clusterId can be not null");
        Assert.notNull(imageId, "imageId can be not null");
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        ClusterInfoDTO imageClusterInfo = imageTemplateDetail.getClusterInfo();
        if (Objects.isNull(imageClusterInfo)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_TEMPLATE_CLUSTER_NOT_EXIT, imageTemplateDetail.getImageName());
        }

        PlatformComputerClusterDTO currentCluster = computerClusterServerMgmtAPI.getComputerClusterInfoById(clusterId);
        PlatformComputerClusterDTO imageComputerCluster = computerClusterServerMgmtAPI.getComputerClusterInfoById(imageClusterInfo.getId());
        if (Objects.equals(currentCluster.getArchitecture(), imageComputerCluster.getArchitecture())) {
            return;
        }
        throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_CLUSTER_CPU_FRAMEWORK_NOT_AGREEMENT, imageTemplateDetail.getImageName(),
                currentCluster.getClusterName());
    }

    @Override
    public void validateComputerClusterStoragePool(UUID computerClusterId, UUID storagePoolId, @Nullable UUID platformId) throws BusinessException {
        Assert.notNull(computerClusterId, "computerClusterId can be not null");
        Assert.notNull(storagePoolId, "storagePoolId can be not null");

        // 校验输入计算集群是否存在以及状态是否健康
        PlatformComputerClusterDTO computerCluster = validateComputerClusterExist(computerClusterId);

        // 校验输入存储池是否存在以及状态是否健康
        PlatformStoragePoolDTO storagePoolDTO = validateStoragePoolExist(storagePoolId);
        boolean canMatch = storagePoolServerMgmtAPI.getStoragePoolByComputeClusterId(computerClusterId).stream()
                .anyMatch(storagePool -> Objects.equals(storagePoolId, storagePool.getId()));
        // 存储位置与运行位置不在同一个计算集群
        if (!canMatch) {
            throw new BusinessException(BusinessKey.RCDC_RCO_STORAGE_NOT_CLUSTER, storagePoolDTO.getName(), computerCluster.getClusterName());
        }
        // 判断云平台是否存在，以及计算集群是否属于该云平台
        if (Objects.nonNull(platformId)) {
            CloudPlatformDTO cloudPlatformDTO = cloudPlatformManageAPI.getInfoById(platformId);
            if (!Objects.equals(cloudPlatformDTO.getId(), computerCluster.getPlatformId())) {
                throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_CLUSTER_NOT_CLOUD_PLATFORM,
                        computerCluster.getClusterName(), cloudPlatformDTO.getName());
            }
        }
    }

    @Override
    public void validateClusterStoragePoolList(UUID computerClusterId, List<UUID> storagePoolIdList) throws BusinessException {
        Assert.notNull(computerClusterId, "computerClusterId can be not null");
        Assert.notNull(storagePoolIdList, "storagePoolIdList can be not null");

        // 校验输入计算集群是否存在以及状态是否健康
        PlatformComputerClusterDTO computerCluster = validateComputerClusterExist(computerClusterId);

        if (CollectionUtils.isEmpty(storagePoolIdList)) {
            return;
        }
        // 查询计算集群下的存储列表
        List<PlatformStoragePoolDTO> storagePoolList = storagePoolServerMgmtAPI.getStoragePoolByComputeClusterId(computerClusterId);
        Set<UUID> storagePoolIdSet = storagePoolList.stream().map(PlatformStoragePoolDTO::getId).collect(Collectors.toSet());
        for (UUID storagePoolId : storagePoolIdList) {
            // 存储位置与运行位置不在同一个计算集群
            if (!storagePoolIdSet.contains(storagePoolId)) {
                // 校验输入存储池是否存在以及状态是否健康
                PlatformStoragePoolDTO noMatchStoragePool = validateStoragePoolExist(storagePoolId);
                throw new BusinessException(BusinessKey.RCDC_RCO_STORAGE_NOT_CLUSTER, noMatchStoragePool.getName(),
                        computerCluster.getClusterName());
            }
        }
    }

    @Override
    public PageResponse<ClusterInfoDTO> pageQueryComputerCluster(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can be not null");
        PageQueryResponse<PlatformComputerClusterDTO> response = computerClusterServerMgmtAPI.pageQuery(request);
        PageResponse<ClusterInfoDTO> pageResponse = new PageResponse<>();
        if (response == null || response.getTotal() == 0) {
            return getClusterInfoDTOPageResponse(pageResponse);
        }
        pageResponse.setTotal(response.getTotal());
        // 返回数据为空的情况下
        if (ArrayUtils.isEmpty(response.getItemArr())) {
            return getClusterInfoDTOPageResponse(pageResponse);
        }
        pageResponse.setItems(Arrays.stream(response.getItemArr()).map(clusterItemDTO -> {
            ClusterInfoDTO clusterInfoDTO = new ClusterInfoDTO();
            BeanUtils.copyProperties(clusterItemDTO, clusterInfoDTO);
            if (clusterItemDTO.getArchitecture() != null) {
                clusterInfoDTO.setArchSet(Sets.newHashSet(clusterItemDTO.getArchitecture()));
            }
            return clusterInfoDTO;
        }).toArray(ClusterInfoDTO[]::new));
        return pageResponse;
    }

    private PageResponse<ClusterInfoDTO> getClusterInfoDTOPageResponse(PageResponse<ClusterInfoDTO> pageResponse) {
        pageResponse.setItems(new ClusterInfoDTO[0]);
        return pageResponse;
    }

    @Override
    public void validateComputerClusterFramework(UUID computerClusterId, UUID vmComputerClusterId) throws BusinessException {
        Assert.notNull(computerClusterId, "clusterId can be not null");
        Assert.notNull(vmComputerClusterId, "imageId can be not null");

        // 校验输入计算集群是否存在以及状态是否健康
        PlatformComputerClusterDTO computerCluster = validateComputerClusterExist(computerClusterId);

        // 校验输入计算集群是否存在以及状态是否健康
        PlatformComputerClusterDTO vmComputerCluster = validateComputerClusterExist(vmComputerClusterId);

        if (Objects.equals(computerCluster.getArchitecture(), vmComputerCluster.getArchitecture())) {
            return;
        }
        throw new BusinessException(BusinessKey.RCDC_RCO_CLUSTER_CPU_FRAMEWORK_NOT_AGREEMENT, computerCluster.getClusterName(),
                vmComputerCluster.getClusterName());
    }

    private PlatformStoragePoolDTO validateStoragePoolExist(UUID storagePoolId) throws BusinessException {
        PlatformStoragePoolDTO storagePoolDTO = storagePoolServerMgmtAPI.getStoragePoolDetail(storagePoolId);
        if (storagePoolDTO == null) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_ASSIGNED_STORAGE_POOL_NOT_EXSIT, 
                    storagePoolId.toString());
        }

        /* 检查存储池是否可用 */
        if (storagePoolDTO.getStoragePoolHealthState() != StoragePoolHealthState.HEALTHY
                && storagePoolDTO.getStoragePoolHealthState() != StoragePoolHealthState.WARNING) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_ASSIGNED_STORAGE_POOL_NOT_AVAILABLE,
                    storagePoolDTO.getName());
        }
        return storagePoolDTO;
    }

    private PlatformComputerClusterDTO validateComputerClusterExist(UUID computerClusterId) throws BusinessException {
        PlatformComputerClusterDTO computerClusterDTO = computerClusterServerMgmtAPI.getComputerClusterInfoById(computerClusterId);
        if (computerClusterDTO == null) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_ASSIGNED_COMPUTER_CLUSTER_NOT_EXSIT,
                    computerClusterId.toString());
        }

        /* 检查计算集群是否可用 */
        if (computerClusterDTO.getClusterState() != CloudClusterStateEnums.ENABLED) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_ASSIGNED_COMPUTER_CLUSTER_NOT_AVAILABLE,
                    computerClusterDTO.getClusterName());
        }
        return computerClusterDTO;
    }

    private List<UUID> mergeStoragePoolId(CbbDeskSpecDTO deskSpecDTO) {
        List<UUID> storagePoolIdList = new ArrayList<>();
        storagePoolIdList.add(deskSpecDTO.getSystemDiskStoragePoolId());
        if (Objects.nonNull(deskSpecDTO.getPersonDiskStoragePoolId())) {
            storagePoolIdList.add(deskSpecDTO.getPersonDiskStoragePoolId());
        }
        if (CollectionUtils.isEmpty(deskSpecDTO.getExtraDiskList())) {
            return storagePoolIdList;
        }
        for (CbbAddExtraDiskDTO extraDiskDTO : deskSpecDTO.getExtraDiskList()) {
            if (Objects.nonNull(extraDiskDTO.getAssignedStoragePoolId())) {
                storagePoolIdList.add(extraDiskDTO.getAssignedStoragePoolId());
            }
        }
        return storagePoolIdList;
    }
}
