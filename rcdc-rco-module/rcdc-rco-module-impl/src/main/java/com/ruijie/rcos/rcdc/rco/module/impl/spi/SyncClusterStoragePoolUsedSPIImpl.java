package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbUpdateImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.DiskMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ImageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.VMMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.disk.QueryDiskRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.image.QueryImageRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.vm.VmIdRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.disk.DiskDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.image.ImageDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.vm.VmDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ImageFileMode;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ImageFormat;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserGroupDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.ViewUserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.ViewUserDiskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserGroupDesktopConfigEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Description: 7.0R1支持多计算集群，需要同步计算集群-存储池应用信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/9
 *
 * @author TD
 */
@DispatcherImplemetion("SyncClusterStoragePoolUsedSPIImpl")
public class SyncClusterStoragePoolUsedSPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncClusterStoragePoolUsedSPIImpl.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ImageMgmtAPI imageMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private VMMgmtAPI vmMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private ViewUserDiskDAO userDiskDAO;

    @Autowired
    private DiskMgmtAPI diskMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterServerMgmtAPI;

    @Autowired
    private UserGroupDesktopConfigDAO userGroupDesktopConfigDAO;

    /** 默认存储池Id */
    private static final UUID DEFAULT_STORAGE_POOL_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /**
     * 休眠最大时长
     */
    private static final int SLEEP_MAX_DURATION = 3;

    @Override
    public Boolean beforeEnteringMaintenance(String dispatchKey, BaseUpgradeDTO upgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterEnteringMaintenance(String dispatchKey, BaseUpgradeDTO upgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterUnderMaintenance(String dispatchKey, BaseUpgradeDTO upgradeDTO) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterMaintenanceEnd(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        Assert.hasText(dispatchKey, "dispatchKey must not be null or empty");
        Assert.notNull(baseUpgradeDTO, "baseUpgradeDTO must not be null");
        if (baseUpgradeDTO.getType() == BaseUpgradeDTO.UpgradeType.ONLINE) {
            LOGGER.info("当前为热补丁升级，无需执行");
            return Boolean.TRUE;
        }

        while (true) {
            try {
                LOGGER.info("========开始执行计算集群和存储池信息同步=========");
                HashSet<Boolean> hashSet = Sets.newHashSet(syncImageTemplate(), syncDesktop(), syncDesktopPool(), syncDisk(), syncDiskPool());
                // 同步用户组VDI桌面配置
                syncUserGroupVDIConfig();
                if (!hashSet.contains(Boolean.FALSE)) {
                    break;
                }
                waitForSecond();
            } catch (Exception e) {
                LOGGER.error("同步数据操作出现系统级别异常", e);
                waitForSecond();
            }
        }
        return Boolean.TRUE;
    }

    private boolean syncImageTemplate() {
        List<CbbImageTemplateDetailDTO> syncImageTemplateList = cbbImageTemplateMgmtAPI.listAllImageTemplate().stream()
                .filter(imageTemplateDetailDTO -> imageTemplateDetailDTO.getCbbImageType() == CbbImageType.VDI)
                .filter(imageTemplateDetailDTO -> Objects.isNull(imageTemplateDetailDTO.getClusterInfo())
                        || Objects.isNull(imageTemplateDetailDTO.getStoragePool()) || Objects.isNull(imageTemplateDetailDTO.getVmClusterId())
                        || Objects.isNull(imageTemplateDetailDTO.getVmStoragePoolId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(syncImageTemplateList)) {
            LOGGER.debug("需要进行同步镜像模板列表为空，结束同步镜像操作");
            return true;
        }
        int syncImageTemplateNumber = syncImageTemplateList.size();
        LOGGER.info("需要进行同步镜像模板数量：{}", syncImageTemplateNumber);
        AtomicInteger processSuccessCount = new AtomicInteger();
        syncImageTemplateList.forEach(templateDetailDTO -> {
            try {
                ImageDTO imageDTO = getImageDTO(templateDetailDTO);
                if (Objects.isNull(imageDTO)) {
                    LOGGER.warn("镜像[{}]查询CCP返回的信息镜像为空，无法进行信息补充，默认信息补充为成功", templateDetailDTO.getId());
                    processSuccessCount.incrementAndGet();
                    return;
                }
                CbbUpdateImageTemplateDTO updateImageTemplateDTO = new CbbUpdateImageTemplateDTO();
                updateImageTemplateDTO.setImageTemplateId(templateDetailDTO.getId());
                updateImageTemplateDTO.setImageTemplateName(templateDetailDTO.getImageName());
                updateImageTemplateDTO.setCbbOsType(templateDetailDTO.getOsType());
                updateImageTemplateDTO.setNote(templateDetailDTO.getNote());
                updateImageTemplateDTO.setPartType(templateDetailDTO.getPartType());
                updateImageTemplateDTO.setClusterId(UUID.fromString(imageDTO.getClusterId()));
                updateImageTemplateDTO.setStoragePoolId(UUID.fromString(imageDTO.getStoragePoolId()));
                updateImageTemplateDTO.setVmClusterId(UUID.fromString(imageDTO.getClusterId()));
                updateImageTemplateDTO.setVmStoragePoolId(UUID.fromString(imageDTO.getStoragePoolId()));
                cbbImageTemplateMgmtAPI.updateImageTemplate(updateImageTemplateDTO);
                processSuccessCount.incrementAndGet();
            } catch (BusinessException e) {
                LOGGER.error("升级同步镜像模板[{}]数据发生异常", templateDetailDTO.getImageName(), e);
            }
        });
        LOGGER.info("升级需要进行同步镜像模板总数量：[{}]，成功同步镜像模板信息数量：[{}]", syncImageTemplateNumber, processSuccessCount.get());
        return processSuccessCount.get() == syncImageTemplateList.size();
    }

    private boolean syncDesktop() {
        List<UUID> deskIdList = cbbDeskMgmtAPI.queryVDIByClusterIsNullList();
        if (CollectionUtils.isEmpty(deskIdList)) {
            LOGGER.debug("需要进行桌面数据同步列表为空，结束同步操作");
            return true;
        }
        LOGGER.info("待同步的云桌面列表数量:{}", deskIdList.size());
        AtomicInteger processSuccessCount = new AtomicInteger();
        deskIdList.forEach(desktopId -> {
            try {
                CbbDeskInfoDTO deskInfoDTO = cbbDeskMgmtAPI.getByDeskId(desktopId);
                if (deskInfoDTO.getDeskType() == CbbCloudDeskType.THIRD) {
                    return;
                }
                VmDTO vmDTO = vmMgmtAPI.queryById(new VmIdRequest(deskInfoDTO.getPlatformId(), desktopId)).getDto();
                if (Objects.isNull(vmDTO)) {
                    LOGGER.warn("桌面[{}]查询CCP返回的信息桌面为空，无法进行信息补充，默认信息补充为成功", desktopId);
                    processSuccessCount.incrementAndGet();
                    return;
                }
                cbbDeskMgmtAPI.updateVDIDesktopClusterInfo(desktopId, UUID.fromString(vmDTO.getClusterId()));
                processSuccessCount.incrementAndGet();
            } catch (BusinessException e) {
                LOGGER.error("同步桌面[{}]数据发生异常", desktopId, e);
            }
        });
        LOGGER.info("升级需要进行同步桌面总数量：[{}]，成功同步桌面信息数量：[{}]", deskIdList.size(), processSuccessCount.get());
        return processSuccessCount.get() == deskIdList.size();
    }

    private boolean syncDesktopPool() {
        List<CbbDesktopPoolDTO> syncPoolDTOList = cbbDesktopPoolMgmtAPI.listAllDesktopPool().stream()
                .filter(cbbDesktopPoolDTO -> enableSyncInfo(cbbDesktopPoolDTO.getClusterId(), cbbDesktopPoolDTO.getStoragePoolId()))
                .filter(cbbDesktopPoolDTO -> cbbDesktopPoolDTO.getImageTemplateId() != null)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(syncPoolDTOList)) {
            LOGGER.debug("需要进行桌面池数据同步列表为空，结束同步操作");
            return true;
        }
        LOGGER.info("待同步的桌面池列表数量:{}", syncPoolDTOList.size());
        AtomicInteger processSuccessCount = new AtomicInteger();
        syncPoolDTOList.forEach(desktopPoolDTO -> {
            try {

                CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(desktopPoolDTO.getImageTemplateId());
                if (Objects.nonNull(imageTemplateDetail.getClusterInfo()) && Objects.nonNull(imageTemplateDetail.getStoragePool().getId())) {
                    desktopPoolDTO.setClusterId(imageTemplateDetail.getClusterInfo().getId());
                    desktopPoolDTO.setStoragePoolId(imageTemplateDetail.getStoragePool().getId());
                    cbbDesktopPoolMgmtAPI.updateDesktopPool(desktopPoolDTO);
                } else {
                    LOGGER.warn("桌面池[{}]配置的镜像模板[{}]计算集群或者存储池为空，" + "无法进行信息补充，默认信息补充为成功", 
                            desktopPoolDTO.getId(), imageTemplateDetail.getId());
                }
                processSuccessCount.incrementAndGet();
            } catch (BusinessException e) {
                LOGGER.error("同步桌面池[{}]数据发生异常", desktopPoolDTO.getName(), e);
            }
        });
        LOGGER.info("升级需要进行同步桌面池总数量：[{}]，成功同步桌面池信息数量：[{}]", syncPoolDTOList.size(), processSuccessCount.get());
        return processSuccessCount.get() == syncPoolDTOList.size();
    }

    private boolean syncDisk() {
        List<ViewUserDiskEntity> syncUserDiskEntityList = userDiskDAO.findByAssignStoragePoolIdsIsNull().stream()
                .filter(diskEntity -> Objects.isNull(diskEntity.getDeskType()) || diskEntity.getDeskType() == CbbCloudDeskType.VDI)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(syncUserDiskEntityList)) {
            LOGGER.debug("需要进行磁盘同步列表为空，结束磁盘同步操作");
            return true;
        }
        LOGGER.info("待同步的磁盘池列表数量:{}", syncUserDiskEntityList.size());
        AtomicInteger processSuccessCount = new AtomicInteger();
        syncUserDiskEntityList.forEach(userDiskEntity -> {
            try {
                UUID diskId = userDiskEntity.getDiskId();
                QueryDiskRequest queryDiskRequest = new QueryDiskRequest(diskId, ImageFileMode.VDI);
                queryDiskRequest.setPlatformId(userDiskEntity.getPlatformId());
                DiskDTO diskDTO = diskMgmtAPI.queryById(queryDiskRequest).getDto();
                if (Objects.isNull(diskDTO)) {
                    LOGGER.warn("磁盘[{}]查询CCP返回的信息为空，无法进行磁盘信息补充", diskId);
                    processSuccessCount.incrementAndGet();
                    return;
                }
                cbbVDIDeskDiskAPI.updateDiskStoragePool(diskId, diskDTO.getStoragePoolId());
                processSuccessCount.incrementAndGet();
            } catch (BusinessException e) {
                LOGGER.error("同步磁盘[{}]数据发生异常", userDiskEntity.getDiskName(), e);
            }
        });
        LOGGER.info("升级需要进行同步磁盘总数量：[{}]，成功同步磁盘信息数量：[{}]", syncUserDiskEntityList.size(), processSuccessCount.get());
        return processSuccessCount.get() == syncUserDiskEntityList.size();
    }

    private boolean syncDiskPool() throws BusinessException {
        List<CbbDiskPoolDTO> syncDiskPoolList = cbbDiskPoolMgmtAPI.listAllDiskPool().stream()
                .filter(cbbDiskPoolDTO -> enableSyncInfo(cbbDiskPoolDTO.getClusterId(), cbbDiskPoolDTO.getStoragePoolId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(syncDiskPoolList)) {
            LOGGER.debug("需要进行磁盘池同步列表为空，结束磁盘池同步操作");
            return true;
        }
        LOGGER.info("待同步的磁盘池列表数量:{}", syncDiskPoolList.size());
        AtomicInteger processSuccessCount = new AtomicInteger();
        PlatformComputerClusterDTO computeCluster = computerClusterServerMgmtAPI.getDefaultComputeCluster();
        UUID computerClusterId = computeCluster.getId();
        syncDiskPoolList.forEach(diskPoolDTO -> {
            try {
                diskPoolDTO.setClusterId(computerClusterId);
                diskPoolDTO.setStoragePoolId(DEFAULT_STORAGE_POOL_ID);
                cbbDiskPoolMgmtAPI.syncDiskPoolClusterAndStoragePool(diskPoolDTO);
                processSuccessCount.incrementAndGet();
            } catch (BusinessException e) {
                LOGGER.error("同步磁盘池[{}]数据发生异常", diskPoolDTO.getName(), e);
            }
        });
        LOGGER.info("升级需要进行同步磁盘池总数量：[{}]，成功同步磁盘池信息数量：[{}]", syncDiskPoolList.size(), processSuccessCount.get());
        return processSuccessCount.get() == syncDiskPoolList.size();
    }

    private void syncUserGroupVDIConfig() {
        List<UserGroupDesktopConfigEntity> syncDesktopConfigList = userGroupDesktopConfigDAO.findByDeskType(UserCloudDeskTypeEnum.VDI).stream()
                .filter(desktopConfigEntity -> enableSyncInfo(desktopConfigEntity.getClusterId(), desktopConfigEntity.getStoragePoolId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(syncDesktopConfigList)) {
            LOGGER.debug("需要进行用户组桌面配置数据同步列表为空，结束同步操作");
            return;
        }
        LOGGER.info("待同步的用户组桌面配置列表数量:{}", syncDesktopConfigList.size());
        List<UserGroupDesktopConfigEntity> successUserDesktopList = Lists.newArrayList();
        for (UserGroupDesktopConfigEntity desktopConfigEntity : syncDesktopConfigList) {
            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(desktopConfigEntity.getImageTemplateId());
            if (Objects.nonNull(imageTemplateDetail.getClusterInfo()) && Objects.nonNull(imageTemplateDetail.getStoragePool().getId())) {
                desktopConfigEntity.setClusterId(imageTemplateDetail.getClusterInfo().getId());
                desktopConfigEntity.setStoragePoolId(imageTemplateDetail.getStoragePool().getId());
                successUserDesktopList.add(desktopConfigEntity);
            } else {
                LOGGER.warn("用户组[{}]配置的镜像模板[{}]计算集群或者存储池为空，" + "无法进行信息补充，默认信息补充为成功", 
                        desktopConfigEntity.getGroupId(), imageTemplateDetail.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(successUserDesktopList)) {
            userGroupDesktopConfigDAO.saveAll(successUserDesktopList);
        }
        LOGGER.info("升级需要进行同步用户组VDI桌面配置总数量：[{}]，" + "成功同步VDI桌面配置信息数量：[{}]", 
                syncDesktopConfigList.size(), successUserDesktopList.size());
    }

    private ImageDTO getImageDTO(CbbImageTemplateDetailDTO imageDetail) throws BusinessException {
        QueryImageRequest queryImageRequest = new QueryImageRequest();
        queryImageRequest.setImageId(imageDetail.getId());
        queryImageRequest.setImageFormat(ImageFormat.QCOW2);
        queryImageRequest.setImageFileMode(ImageFileMode.VDI);
        queryImageRequest.setPlatformId(imageDetail.getPlatformId());
        return imageMgmtAPI.queryById(queryImageRequest).getDto();
    }

    private boolean enableSyncInfo(UUID clusterId, UUID storagePoolId) {
        return Objects.isNull(clusterId) || Objects.isNull(storagePoolId);
    }

    private void waitForSecond() {
        try {
            TimeUnit.SECONDS.sleep(SLEEP_MAX_DURATION);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            LOGGER.error("sleep异常", ex);
        }
    }
}
