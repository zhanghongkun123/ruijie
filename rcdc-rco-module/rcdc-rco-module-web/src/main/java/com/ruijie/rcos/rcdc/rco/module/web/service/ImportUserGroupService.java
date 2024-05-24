package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.*;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateServerCpuDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateCpuInstallState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ClusterMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.cluster.ComputerClusterBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cluster.ClusterNodesInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils.ImageCheckUtils;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.rco.module.web.util.ImportUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVOIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.StoragePoolAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VDIDesktopValidateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.UserConfigHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.IDVDesktopConfig;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VDIDesktopConfig;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VOIDesktopConfig;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/23
 *
 * @author jarman
 */
@Service
public class ImportUserGroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserGroupService.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private UserConfigHelper userConfigHelper;

    @Autowired
    private StoragePoolAPI storagePoolAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private ClusterMgmtAPI clusterMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI deskStrategyMgmtAPI;

    @Autowired
    private ImageCheckUtils imageCheckUtils;

    /**
     * checkAndGetVdiDesktopConfig
     *
     * @param dto dto
     * @return VDIDesktopConfig
     * @throws BusinessException 异常
     */
    public VDIDesktopConfig checkAndGetVdiDesktopConfig(ImportUserGroupDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto can not be null");

        CbbGetImageTemplateInfoDTO vdiImageTemplate = this.getImageTemplateByNameAndImageType(dto.getVdiImageTemplateName(), CbbImageType.VDI);
        CbbDeskStrategyVDIDTO vdiStrategy = this.getVDIStrategyByName(dto.getVdiStrategyName());
        CbbDeskNetworkDetailDTO vdiDeskNetworkDTO = this.getNetworkIdByName(dto.getVdiNetworkName());
        int vdiImageSystemSize = vdiImageTemplate == null ? 0 : vdiImageTemplate.getImageSystemSize();
        int vdiSystemSize = StringUtils.isBlank(dto.getVdiSystemSize()) ? 0 : Integer.parseInt(dto.getVdiSystemSize());
        // 系统盘大小不能小于镜像模板的系统盘大小
        if (vdiSystemSize < vdiImageSystemSize) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_SPEC_SYSTEM_SIZE_ERROR, dto.getVdiImageTemplateName());
        }
        UUID vdiImageTemplateId = vdiImageTemplate == null ? null : vdiImageTemplate.getId();
        UUID vdiStrategyId = vdiStrategy == null ? null : vdiStrategy.getId();
        UUID vdiNetworkId = vdiDeskNetworkDTO == null ? null : vdiDeskNetworkDTO.getId();

        VDIDesktopConfig vdiDesktopConfig = new VDIDesktopConfig(vdiImageTemplateId, vdiStrategyId, vdiNetworkId);
        // 校验镜像、运行位置、存储位置、云桌面策略、网络策略是否合规
        if (vdiImageTemplateId != null) {
            checkNetworkAndStrategy(dto, vdiStrategyId, vdiNetworkId);
            CloudPlatformDTO platform = getPlatformIdByName(dto.getCloudPlatformName());
            vdiDesktopConfig.setVdiPlatformId(platform.getId());
            vdiDesktopConfig.setVdiClusterId(getClusterIdByName(platform, dto.getVdiClusterName()));
            // 构建规格信息
            CbbDeskSpecDTO deskSpecDTO = buildDeskSpec(dto, platform, vdiDesktopConfig.getVdiClusterId());
            vdiDesktopConfig.setDeskSpecDTO(deskSpecDTO);
            checkDeskConfigAndVGPU(vdiDesktopConfig, vdiStrategy);

            checkIsImageInstallCpuDrive(vdiImageTemplate, vdiDesktopConfig.getVdiClusterId());

            //校验用户组VDI配置镜像与云桌面策略
            deskStrategyMgmtAPI.checkDeskStrategyImageIdMatch(vdiImageTemplateId, vdiStrategyId, vdiSystemSize);

            imageCheckUtils.checkForSingleImageSync(vdiImageTemplateId, deskSpecDTO.getSystemDiskStoragePoolId());
        }

        return vdiDesktopConfig;
    }

    private void checkIsImageInstallCpuDrive(CbbGetImageTemplateInfoDTO templateInfoDTO , UUID clusterId) throws BusinessException {
        // 获取clusterInfoDTOArr中云平台的全部节点
        ClusterInfoDTO clusterInfoDTO = clusterAPI.queryAvailableClusterById(clusterId);
        ComputerClusterBaseRequest request = new ComputerClusterBaseRequest(clusterInfoDTO.getId(), clusterInfoDTO.getPlatformId());
        List<ClusterNodesInfoDTO> nodesInfoList = clusterMgmtAPI.getClusterNodesInfo(request)
                .getClusterNodesInfoDTOList();
        // 获取计算集群全部cpu驱动类型
        List<String> clusterCpuList = nodesInfoList.stream()
                .map(ClusterNodesInfoDTO::getCpuType)
                .collect(Collectors.toList());

        UUID imageTemplateId = templateInfoDTO.getId();
        Set<String> imageInstallDriveSet = cbbImageTemplateMgmtAPI.listImageServerCpuByImageIdAndState(imageTemplateId,
                ImageTemplateCpuInstallState.AVAILABLE).stream().map(CbbImageTemplateServerCpuDTO::getServerCpuType).collect(Collectors.toSet());

        UUID sourcePlatformId = templateInfoDTO.getPlatformId();

        boolean isPlatformNotEqAndNotInstallCpu =
                !imageInstallDriveSet.containsAll(clusterCpuList)
                        && !Objects.equals(sourcePlatformId, clusterInfoDTO.getPlatformId());
        if (isPlatformNotEqAndNotInstallCpu) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_TARGET_PLATFORM_ID_EXIST_MIX_CPU, clusterInfoDTO.getClusterName() , templateInfoDTO.getImageName());
        }
    }

    private void checkDeskConfigAndVGPU(VDIDesktopConfig vdiDesktopConfig, CbbDeskStrategyVDIDTO vdiStrategy) throws BusinessException {
        CbbDeskSpecDTO deskSpecDTO = vdiDesktopConfig.getDeskSpecDTO();
        if (BooleanUtils.isTrue(vdiStrategy.getOpenDesktopRedirect()) && Optional.ofNullable(deskSpecDTO.getPersonSize()).orElse(0) <= 0) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_DESK_REDIRECT_MUST_PERSON_DISK);
        }

        VDIDesktopValidateDTO vdiDesktopValidateDTO = new VDIDesktopValidateDTO();
        vdiDesktopValidateDTO.setClusterId(vdiDesktopConfig.getVdiClusterId());
        vdiDesktopValidateDTO.setNetworkId(vdiDesktopConfig.getVdiNetworkId());
        vdiDesktopValidateDTO.setImageId(vdiDesktopConfig.getVdiImageId());
        vdiDesktopValidateDTO.setStrategyId(vdiDesktopConfig.getVdiStrategyId());
        vdiDesktopValidateDTO.setPlatformId(vdiDesktopConfig.getVdiPlatformId());
        vdiDesktopValidateDTO.setDeskSpec(deskSpecDTO);
        // 校验镜像、运行位置、存储位置、云桌面策略、网络策略是否合规
        userConfigHelper.checkDeskConfigAndVGPU(vdiDesktopValidateDTO);
    }

    private void checkNetworkAndStrategy(ImportUserGroupDTO dto, UUID vdiStrategyId, UUID vdiNetworkId) throws BusinessException {
        if (vdiNetworkId == null) {
            throw new BusinessException(UserBusinessKey.RCDC_BATCH_IMPORT_NETWORK_NAME_NOT_EXIST, dto.getVdiNetworkName());
        }
        if (vdiStrategyId == null) {
            throw new BusinessException(UserBusinessKey.RCDC_BATCH_IMPORT_STRATEGY_NAME_NOT_EXIST, dto.getVdiStrategyName());
        }
    }

    private UUID getClusterIdByName(CloudPlatformDTO platform, String vdiClusterName) throws BusinessException {
        UUID clusterId = clusterAPI.queryClusterIdByName(platform.getId(), vdiClusterName);
        if (clusterId == null) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CLUSTER_NAME_NOT_EXIST, platform.getName(), vdiClusterName);
        } else {
            return clusterId;
        }
    }

    private CloudPlatformDTO getPlatformIdByName(String cloudPlatformName) throws BusinessException {
        CloudPlatformDTO cloudPlatformDTO = cloudPlatformManageAPI.getInfoByName(cloudPlatformName);
        if (cloudPlatformDTO == null) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CLOUD_PLATFORM_NAME_NOT_EXIST, cloudPlatformName);
        }
        return cloudPlatformDTO;
    }

    /**
     * checkAndGetIdvDesktopConfig
     *
     * @param dto dto
     * @return IDVDesktopConfig
     * @throws BusinessException 异常
     */
    public IDVDesktopConfig checkAndGetIdvDesktopConfig(ImportUserGroupDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto can not be null");

        LOGGER.info("导入的idv桌面配置信息，idvImageName:{},idvStrategyName:{}", dto.getIdvImageTemplateName(), dto.getIdvStrategyName());
        CbbGetImageTemplateInfoDTO idvImageTemplate = this.getImageTemplateByNameAndImageType(dto.getIdvImageTemplateName(), CbbImageType.IDV);
        CbbDeskStrategyIDVDTO idvStrategy = this.getIDVStrategyByName(dto.getIdvStrategyName());
        Integer idvImageSystemSize = idvImageTemplate == null ? 0 : idvImageTemplate.getImageSystemSize();
        Integer idvStrategySystemSize = idvStrategy == null ? 0 : idvStrategy.getSystemSize();
        // 云桌面策略的系统盘大小不能小于镜像模板的系统盘大小
        if (idvStrategySystemSize < idvImageSystemSize) {
            throw new BusinessException(UserBusinessKey.RCDC_USER_IMPORT_GROUP_STRATEGY_SYSTEM_SIZE_TOO_SMALL);
        }
        UUID idvImageTemplateId = idvImageTemplate == null ? null : idvImageTemplate.getId();
        UUID idvStrategyId = idvStrategy == null ? null : idvStrategy.getId();
        return new IDVDesktopConfig(idvImageTemplateId, idvStrategyId);
    }

    /**
     * 检查VOI桌面配置
     *
     * @param dto dto
     * @return IDVDesktopConfig
     * @throws BusinessException 异常
     */
    public VOIDesktopConfig checkAndGetVoiDesktopConfig(ImportUserGroupDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto can not be null");

        LOGGER.info("导入的voi桌面配置信息，idvImageName:{},idvStrategyName:{}", dto.getVoiImageTemplateName(), dto.getVoiStrategyName());
        // VOI镜像模板
        CbbGetImageTemplateInfoDTO voiImageTemplate = this.getImageTemplateByNameAndImageType(dto.getVoiImageTemplateName(), CbbImageType.VOI);
        // VOI镜像策略
        CbbDeskStrategyVOIDTO voiStrategy = this.getVOIStrategyByName(dto.getVoiStrategyName());
        // 模板的镜像大小
        Integer voiImageSystemSize = voiImageTemplate == null ? 0 : voiImageTemplate.getImageSystemSize();
        // 镜像策略的大小
        Integer voiStrategySystemSize = voiStrategy == null ? 0 : voiStrategy.getSystemSize();
        // 云桌面策略的系统盘大小不能小于镜像模板的系统盘大小
        if (voiStrategySystemSize < voiImageSystemSize) {
            throw new BusinessException(UserBusinessKey.RCDC_USER_IMPORT_GROUP_STRATEGY_SYSTEM_SIZE_TOO_SMALL);
        }
        UUID voiImageTemplateId = voiImageTemplate == null ? null : voiImageTemplate.getId();
        UUID voiStrategyId = voiStrategy == null ? null : voiStrategy.getId();
        return new VOIDesktopConfig(voiImageTemplateId, voiStrategyId);
    }

    private CbbGetImageTemplateInfoDTO getImageTemplateByNameAndImageType(@Nullable String imageName, CbbImageType cbbImageType)
            throws BusinessException {
        Assert.notNull(cbbImageType, "cbbImageType can not be null");
        if (StringUtils.isBlank(imageName)) {
            // 为空返回 null
            return null;
        }
        CbbGetImageTemplateInfoDTO response = cbbImageTemplateMgmtAPI.getImageTemplateInfoByImageTemplateName(imageName);
        if (Objects.isNull(response)) {
            // 直接返回
            return response;
        }

        // 如果查找到的镜像模板是开启多版本为true，则可以认为是多版本的母镜像，需要找到最新的镜像版本，可通过lastRecoveryPointId
        Boolean enableMultipleVersion = response.getEnableMultipleVersion();
        if (cbbImageType == CbbImageType.VDI && Boolean.TRUE.equals(enableMultipleVersion)) {
            UUID lastRecoveryPointId = response.getLastRecoveryPointId();
            if (Objects.isNull(lastRecoveryPointId)) {
                // 没有版本
                throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_DISABLED_VERSION, imageName);
            }
            // 取出最新的镜像版本
            response = cbbImageTemplateMgmtAPI.getImageTemplateInfoBySourceSnapshotId(lastRecoveryPointId);
            // 防御性非空断言，正常不存在该情况
            Assert.notNull(response, "response must not be null");

            imageName = response.getImageName();
        }

        if (response.getImageState() != ImageTemplateState.AVAILABLE) {
            LOGGER.error("镜像不存在或者不是有效：{}", imageName);
            throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_DISABLED, imageName);
        }
        if (response.getCbbImageType() != cbbImageType) {
            LOGGER.error("镜像类型不是VDI：{}", JSON.toJSONString(response));
            throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TYPE_UNMATCH, imageName, cbbImageType.name());
        }

        return response;
    }

    private CbbDeskNetworkDetailDTO getNetworkIdByName(@Nullable String networkName) throws BusinessException {
        if (StringUtils.isBlank(networkName)) {
            // 为空返回 null
            return null;
        }
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getNetworkByName(networkName);
        if (deskNetworkDTO == null) {
            throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_NOT_FOUND, networkName);
        }

        return deskNetworkDTO;
    }

    private CbbDeskStrategyVDIDTO getVDIStrategyByName(@Nullable String vdiStrategyName) throws BusinessException {
        if (StringUtils.isBlank(vdiStrategyName)) {
            // 为空返回 null
            return null;
        }
        return cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDIByName(vdiStrategyName);
    }

    private CbbDeskStrategyIDVDTO getIDVStrategyByName(@Nullable String idvStrategyName) throws BusinessException {
        if (StringUtils.isBlank(idvStrategyName)) {
            // 为空返回 null
            return null;
        }
        return cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDVByName(idvStrategyName);
    }

    /**
     * 根据VOI策略名称获取VOI策略
     * 
     * @param voiStrategyName
     * @return
     * @throws BusinessException
     */
    private CbbDeskStrategyVOIDTO getVOIStrategyByName(@Nullable String voiStrategyName) throws BusinessException {
        if (StringUtils.isBlank(voiStrategyName)) {
            // 为空返回 null
            return null;
        }
        return cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOIByName(voiStrategyName);
    }

    private CbbDeskSpecDTO buildDeskSpec(ImportUserGroupDTO dto, CloudPlatformDTO platform, UUID clusterId) throws BusinessException {
        CbbDeskSpecDTO deskSpecDTO = new CbbDeskSpecDTO();
        deskSpecDTO.setCpu(Integer.parseInt(dto.getVdiCpu()));
        deskSpecDTO.setMemory(CapacityUnitUtils.gb2Mb(Double.parseDouble(dto.getVdiMemory())));
        deskSpecDTO.setSystemSize(Integer.parseInt(dto.getVdiSystemSize()));
        deskSpecDTO.setSystemDiskStoragePoolId(getStoragePoolIdByName(platform, dto.getVdiStoragePoolName()));
        if (StringUtils.isNotBlank(dto.getVdiPersonSize())) {
            deskSpecDTO.setPersonSize(Integer.parseInt(dto.getVdiPersonSize().trim()));
        }
        if (Objects.nonNull(deskSpecDTO.getPersonSize()) && deskSpecDTO.getPersonSize() > 0) {
            deskSpecDTO.setPersonDiskStoragePoolId(getStoragePoolIdByName(platform, dto.getVdiPersonDiskStoragePoolName()));
        }
        if (StringUtils.isBlank(dto.getVdiVgpuModel())) {
            deskSpecDTO.setVgpuInfoDTO(new VgpuInfoDTO());
        } else {
            String model = ImportUtils.replaceSpace(dto.getVdiVgpuModel().trim());
            deskSpecDTO.setVgpuInfoDTO(deskSpecAPI.getVGpuByModel(clusterId, model));
        }
        return deskSpecDTO;
    }

    private UUID getStoragePoolIdByName(CloudPlatformDTO platform, String vdiStoragePoolName) throws BusinessException {
        UUID storagePoolId = storagePoolAPI.queryStoragePoolIdByName(platform.getId(), vdiStoragePoolName);
        if (storagePoolId == null) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_STORAGE_POOL_NAME_NOT_EXIST, platform.getName(), vdiStoragePoolName);
        } else {
            return storagePoolId;
        }
    }
}
