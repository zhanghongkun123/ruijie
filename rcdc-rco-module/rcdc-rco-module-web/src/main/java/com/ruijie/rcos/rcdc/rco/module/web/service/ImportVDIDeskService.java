package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.StoragePoolAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VDIDesktopValidateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.UserConfigHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportVDIDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.ImportVDIDesktopConfig;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.rco.module.web.util.ImportUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/23
 *
 * @author linrenjian
 */
@Service
public class ImportVDIDeskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportVDIDeskService.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    /**
     * 用户API
     */
    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private StoragePoolAPI storagePoolAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private UserConfigHelper userConfigHelper;
    
    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    /**
     * checkAndGetVdiDesktopConfig 检查VDI配置
     *
     * @param dto dto
     * @return VDIDesktopConfig
     * @throws BusinessException 异常
     */
    public ImportVDIDesktopConfig checkAndGetVdiDesktopConfig(ImportVDIDeskDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto can not be null");
        // 检查用户信息 根据用户名称 获取用户ID
        IacUserDetailDTO cbbUserDetailDTO = this.getUserIdByName(dto.getUserName());
        UUID userId = cbbUserDetailDTO == null ? null : cbbUserDetailDTO.getId();
        // 检查VDI镜像模板 根据模板名称 获取镜像ID
        CbbGetImageTemplateInfoDTO vdiImageTemplate = this.getImageTemplateByNameAndImageType(dto.getVdiImageTemplateName(), CbbImageType.VDI);
        UUID vdiImageTemplateId = vdiImageTemplate == null ? null : vdiImageTemplate.getId();
        // 检查VDI云桌面策略 根据策略名称 获取策略ID
        CbbDeskStrategyVDIDTO vdiStrategy = this.getVDIStrategyByName(dto.getVdiStrategyName());
        UUID vdiStrategyId = vdiStrategy == null ? null : vdiStrategy.getId();
        // 检查网络策略 根据网络名称 获取网络ID
        CbbDeskNetworkDetailDTO vdiDeskNetworkDTO = this.getNetworkIdByName(dto.getVdiNetworkName());
        UUID vdiNetworkId = vdiDeskNetworkDTO == null ? null : vdiDeskNetworkDTO.getId();
        // 系统盘，个人盘比较
        int vdiImageSystemSize = vdiImageTemplate == null ? 0 : vdiImageTemplate.getImageSystemSize();
        int vdiSystemSize = StringUtils.isBlank(dto.getVdiSystemSize()) ? 0 : Integer.parseInt(dto.getVdiSystemSize());
        // 系统盘大小不能小于镜像模板的系统盘大小
        if (vdiSystemSize < vdiImageSystemSize) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_SPEC_SYSTEM_SIZE_ERROR, dto.getVdiImageTemplateName());
        }

        CloudPlatformDTO platform = getPlatformIdByName(dto.getCloudPlatformName());
        // 生成导入VDI云桌面配置对象
        ImportVDIDesktopConfig importVDIDesktopConfig = new ImportVDIDesktopConfig(userId, vdiImageTemplateId, vdiStrategyId, vdiNetworkId);
        importVDIDesktopConfig.setPlatformId(platform.getId());
        importVDIDesktopConfig.setClusterId(getClusterIdByName(platform, dto.getVdiClusterName()));
        importVDIDesktopConfig.setDeskSpecDTO(buildDeskSpec(dto, platform, importVDIDesktopConfig.getClusterId()));

        checkDeskConfigAndVGPU(dto, vdiImageTemplateId, vdiStrategy, vdiNetworkId, importVDIDesktopConfig);
        return importVDIDesktopConfig;
    }

    private void checkDeskConfigAndVGPU(ImportVDIDeskDTO dto, UUID vdiImageTemplateId, CbbDeskStrategyVDIDTO vdiStrategy, UUID vdiNetworkId,
            ImportVDIDesktopConfig importVDIDesktopConfig) throws BusinessException {
        if (vdiImageTemplateId != null) {
            if (vdiNetworkId == null) {
                throw new BusinessException(UserBusinessKey.RCDC_BATCH_IMPORT_NETWORK_NAME_NOT_EXIST, dto.getVdiNetworkName());
            }
            if (Objects.isNull(vdiStrategy) || Objects.isNull(vdiStrategy.getId())) {
                throw new BusinessException(UserBusinessKey.RCDC_BATCH_IMPORT_STRATEGY_NAME_NOT_EXIST, dto.getVdiStrategyName());
            }
            CbbDeskSpecDTO deskSpecDTO = importVDIDesktopConfig.getDeskSpecDTO();
            if (BooleanUtils.isTrue(vdiStrategy.getOpenDesktopRedirect())
                    && (Objects.isNull(deskSpecDTO.getPersonSize()) || deskSpecDTO.getPersonSize() <= 0)) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_DESK_REDIRECT_MUST_PERSON_DISK);
            }

            VDIDesktopValidateDTO vdiDesktopValidateDTO = new VDIDesktopValidateDTO();
            vdiDesktopValidateDTO.setClusterId(importVDIDesktopConfig.getClusterId());
            vdiDesktopValidateDTO.setNetworkId(vdiNetworkId);
            vdiDesktopValidateDTO.setImageId(vdiImageTemplateId);
            vdiDesktopValidateDTO.setStrategyId(vdiStrategy.getId());
            vdiDesktopValidateDTO.setPlatformId(importVDIDesktopConfig.getPlatformId());
            vdiDesktopValidateDTO.setDeskSpec(importVDIDesktopConfig.getDeskSpecDTO());
            // 校验镜像、运行位置、存储位置、云桌面策略、网络策略是否合规
            userConfigHelper.checkDeskConfigAndVGPU(vdiDesktopValidateDTO);
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
     * 检查用户 根据用户名称
     * 
     * @param userName
     * @return
     * @throws BusinessException
     */
    private IacUserDetailDTO getUserIdByName(@Nullable String userName) throws BusinessException {
        if (StringUtils.isBlank(userName)) {
            // 为空返回 null
            return null;
        }
        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(userName);
        if (cbbUserDetailDTO == null) {
            LOGGER.error("用户不存在{}", userName);
            throw new BusinessException(UserBusinessKey.RCDC_USER_NOT_FOUND, userName);
        }

        return cbbUserDetailDTO;
    }

    /**
     * 检查VDI镜像模板 根据模板名称
     * 
     * @param imageName
     * @param cbbImageType
     * @return
     * @throws BusinessException
     */
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
        if (Boolean.TRUE.equals(enableMultipleVersion)) {
            UUID lastRecoveryPointId = response.getLastRecoveryPointId();
            if (Objects.isNull(lastRecoveryPointId)) {
                // 没有版本
                throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_DISABLED_VERSION, imageName);
            }
            // 先判断源镜像是否应用镜像
            checkIsAppImgae(response.getImageUsage(), imageName);

            // 取出最新的镜像版本
            response = cbbImageTemplateMgmtAPI.getImageTemplateInfoBySourceSnapshotId(lastRecoveryPointId);
            // 判断最新版本是否是应用镜像
            if (response.getImageUsage() == ImageUsageTypeEnum.APP) {
                throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_NEWEST_VERSION_USAGE_APP, imageName, response.getImageName());
            }
            // 防御性非空断言，正常不存在该情况
            Assert.notNull(response, "response must not be null");
            imageName = response.getImageName();
        }

        // enableMultipleVersion=false说明用户填写的是镜像版本或单版本镜像名称。需要判断填的是否版本名称，并判断是否源镜像是否应用镜像
        if (Boolean.FALSE.equals(enableMultipleVersion) &&
                response.getImageRoleType() == ImageRoleType.VERSION && Objects.nonNull(response.getRootImageId())) {
            // 用户填的是版本名称，先判断当前版本是否应用
            checkIsAppImgae(response.getImageUsage(), imageName);
            // 输入的版本是桌面版本，接着判断源镜像是否是应用镜像
            CbbImageTemplateDTO rootImage = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(response.getRootImageId());
            if (rootImage.getImageUsage() == ImageUsageTypeEnum.APP) {
                throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_ROOT_USAGE_APP, imageName, rootImage.getImageTemplateName());
            }
        }

        if (response.getImageState() != ImageTemplateState.AVAILABLE) {
            LOGGER.error("镜像不存在或者不是有效：{}", imageName);
            throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_DISABLED, imageName);
        }
        if (response.getCbbImageType() != cbbImageType) {
            LOGGER.error("镜像类型不是VDI：{}", JSON.toJSONString(response));
            throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TYPE_UNMATCH, imageName, cbbImageType.name());
        }
        // 判断判断镜像或版本是否应用镜像
        checkIsAppImgae(response.getImageUsage(), imageName);

        return response;
    }

    private void checkIsAppImgae(ImageUsageTypeEnum imageUsageType, String imageName) throws BusinessException {
        if (imageUsageType == ImageUsageTypeEnum.APP) {
            throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_USAGE_APP, imageName);
        }
    }

    /**
     * 检查网络策略 根据网络名称
     * 
     * @param networkName
     * @return
     * @throws BusinessException
     */
    private CbbDeskNetworkDetailDTO getNetworkIdByName(@Nullable String networkName) throws BusinessException {
        if (StringUtils.isBlank(networkName)) {
            // 为空返回 null
            return null;
        }
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getNetworkByName(networkName);
        if (deskNetworkDTO == null || deskNetworkDTO.getId() == null) {
            LOGGER.error("网络不存在:{}", networkName);
            throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_NOT_FOUND, networkName);
        }
        return deskNetworkDTO;
    }

    /**
     * 检查VDI云桌面策略 根据策略名称
     * 
     * @param vdiStrategyName
     * @return
     * @throws BusinessException
     */
    private CbbDeskStrategyVDIDTO getVDIStrategyByName(@Nullable String vdiStrategyName) throws BusinessException {
        if (StringUtils.isBlank(vdiStrategyName)) {
            // 为空返回 null
            return null;
        }
        CbbDeskStrategyVDIDTO cbbDeskStrategyVDIDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDIByName(vdiStrategyName);
        if (cbbDeskStrategyVDIDTO == null) {
            LOGGER.error("VDI云桌面策略不存在:{}", vdiStrategyName);
            throw new BusinessException(UserBusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_VDI_NOT_FOUND, vdiStrategyName);
        }
        return cbbDeskStrategyVDIDTO;
    }

    private CbbDeskSpecDTO buildDeskSpec(ImportVDIDeskDTO dto, CloudPlatformDTO platform, UUID clusterId) throws BusinessException {
        CbbDeskSpecDTO deskSpecDTO = new CbbDeskSpecDTO();
        deskSpecDTO.setCpu(Integer.parseInt(dto.getVdiCpu()));
        deskSpecDTO.setMemory(CapacityUnitUtils.gb2Mb(Double.parseDouble(dto.getVdiMemory())));
        deskSpecDTO.setSystemSize(Integer.parseInt(dto.getVdiSystemSize()));
        deskSpecDTO.setSystemDiskStoragePoolId(getStoragePoolIdByName(platform, dto.getVdiStoragePoolName()));
        if (StringUtils.isNotBlank(dto.getVdiPersonSize())) {
            deskSpecDTO.setPersonSize(Integer.parseInt(dto.getVdiPersonSize().trim()));
        } else {
            deskSpecDTO.setPersonSize(0);
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
