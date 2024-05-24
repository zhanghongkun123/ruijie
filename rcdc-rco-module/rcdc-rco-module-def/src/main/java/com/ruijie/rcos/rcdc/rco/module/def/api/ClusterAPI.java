package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VDIDesktopValidateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 集群操作
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/31
 *
 * @author TD
 */
public interface ClusterAPI {

    /**
     * 查询全部计算集群列表
     * @return 集群列表
     * @throws BusinessException 业务异常
     */
    List<ClusterInfoDTO> queryAllClusterInfoList() throws BusinessException;

    /**
     * 判断指定集群是否存在
     * @param clusterId 集群ID
     * @return 结果
     */
    Boolean existsCluster(UUID clusterId);

    /**
     * 查询指定id集群
     * @param clusterId 集群ID
     * @return 集群信息
     */
    ClusterInfoDTO queryAvailableClusterById(UUID clusterId);

    /**
     * 校验多计算集群VDI云桌面配置
     * @param validateDTO 配置DTO
     * @throws BusinessException 业务异常
     */
    void validateVDIDesktopConfig(VDIDesktopValidateDTO validateDTO) throws BusinessException;

    /**
     * 根据存储池查询全部集群ID
     *
     * @param storagePoolId 存储池ID
     * @return 集群ID集合
     * @throws BusinessException 业务异常
     */
    Set<UUID> queryClusterToSetByStoragePoolId(UUID storagePoolId) throws BusinessException;

    /**
     * 获取集群id
     *
     * @param platformId 云平台ID
     * @param clusterName 集群名称
     * @return 集群id
     */
    UUID queryClusterIdByName(UUID platformId, String clusterName);

    /**
     * 校验多计算集群网络配置
     * @param clusterId 计算集群id
     * @param networkId 网络id
     * @throws BusinessException 业务异常
     */
    void validateVDIDesktopNetwork(UUID clusterId, UUID networkId) throws BusinessException;

    /**
     * 校验VDI桌面所属的计算集群和镜像模板CPU架构是否一致
     *
     * @param clusterId 桌面计算集群ID
     * @param imageId   镜像ID
     * @throws BusinessException 业务异常
     */
    void validateVDIImageTemplateFramework(UUID clusterId, UUID imageId) throws BusinessException;

    /**
     * 校验计算集群与存储池
     * @param computerClusterId 计算集群id
     * @param storagePoolId 存储池id
     * @param platformId 云平台ID
     * @throws BusinessException 业务异常
     */
    void validateComputerClusterStoragePool(UUID computerClusterId, UUID storagePoolId, UUID platformId) throws BusinessException;

    /**
     * 校验计算集群与存储池列表
     * @param computerClusterId 计算集群id
     * @param storagePoolIdList 存储池id列表
     * @throws BusinessException 业务异常
     */
    void validateClusterStoragePoolList(UUID computerClusterId, List<UUID> storagePoolIdList) throws BusinessException;

    /**
     * 分页查询计算集群
     * @param request 分页请求参数
     * @return 计算集群结果
     * @throws BusinessException 业务异常
     */
    PageResponse<ClusterInfoDTO> pageQueryComputerCluster(PageQueryRequest request) throws BusinessException;

    /**
     * 校验镜像的计算集群和虚拟机计算集群CPU架构是否一致
     * @param computerClusterId 镜像所属计算集群
     * @param vmComputerClusterId 虚拟机所属计算集群
     * @throws BusinessException 业务异常
     */
    void validateComputerClusterFramework(UUID computerClusterId, UUID vmComputerClusterId) throws BusinessException;
}
