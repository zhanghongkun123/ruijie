package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateNetworkDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/28 16:25
 *
 * @author conghaifeng
 */
public class ImageTemplateDetailDTO extends PlatformBaseInfoDTO {

    private UUID id;

    private Integer cpu;

    private Double memory;

    private Integer systemDisk;

    private CbbImageTemplateNetworkDTO network;

    private String imageName;

    private String imageFileName;

    private String note;

    private CbbOsType osType;

    private ImageTemplateState imageState;

    private CbbImageType cbbImageType;

    private UUID tempVmId;

    private Integer imageSystemSize;

    private Integer clouldDeskopNumOfRecoverable;

    private Integer clouldDeskopNumOfPersonal;

    private String editErrorMessage;

    private String editErrorMessageKey;

    private boolean supportGoldenImage;

    private Integer clouldDeskopNumOfAppLayer;

    private boolean canUsed;

    private String canUsedMessage;

    private ExternalImageVmState vmState;

    private ImageTemplateGuestToolState guesttoolState;

    private boolean canEditSystemDiskSize;

    private boolean canEditDataDiskSize;

    private String terminalId;

    private ImageVmHost imageVmHost;

    private Boolean enableGpu;

    private VgpuType vgpuType;

    private String graphicsMemorySize;

    private String vgpuItem;

    private String vgpuModel;

    private Boolean enableNested;

    private ImageTemplatePublishTaskDTO publishTaskDTO;

    private List<VgpuInfoDTO> vgpuInfoDTOHistoryList;

    /**
     * 镜像加域结果
     */
    private Boolean isAd;

    /**
     * 计算机名
     */
    private String computerName;

    private ClusterInfoDTO clusterInfo;
    
    private PlatformStoragePoolDTO storagePool;

    private List<CbbImageDiskInfoDTO> imageDiskList;

    private ClusterInfoDTO vmClusterInfo;

    private PlatformStoragePoolDTO vmStoragePool;
    
    private boolean canModifyVm = false;

    private UUID rootImageId;

    private String rootImageName;

    private UUID sourceSnapshotId;

    private ImageRoleType imageRoleType;

    private Boolean isNewestVersion;

    private Boolean enableMultipleVersion;

    private ImageTerminalLocalEditType terminalLocalEditType;

    private List<ImageRelateDriverInfoDTO> imageDriverList = new ArrayList<>();

    private CbbCpuArchType cpuArch;

    private ImageUsageTypeEnum imageUsage;

    public ImageTemplatePublishTaskDTO getPublishTaskDTO() {
        return publishTaskDTO;
    }

    public void setPublishTaskDTO(ImageTemplatePublishTaskDTO publishTaskDTO) {
        this.publishTaskDTO = publishTaskDTO;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public CbbImageTemplateNetworkDTO getNetwork() {
        return network;
    }

    public void setNetwork(CbbImageTemplateNetworkDTO network) {
        this.network = network;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public ImageTemplateState getImageState() {
        return imageState;
    }

    public void setImageState(ImageTemplateState imageState) {
        this.imageState = imageState;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public UUID getTempVmId() {
        return tempVmId;
    }

    public void setTempVmId(UUID tempVmId) {
        this.tempVmId = tempVmId;
    }

    public Integer getImageSystemSize() {
        return imageSystemSize;
    }

    public void setImageSystemSize(Integer imageSystemSize) {
        this.imageSystemSize = imageSystemSize;
    }

    public Integer getClouldDeskopNumOfRecoverable() {
        return clouldDeskopNumOfRecoverable;
    }

    public void setClouldDeskopNumOfRecoverable(Integer clouldDeskopNumOfRecoverable) {
        this.clouldDeskopNumOfRecoverable = clouldDeskopNumOfRecoverable;
    }

    public Integer getClouldDeskopNumOfPersonal() {
        return clouldDeskopNumOfPersonal;
    }

    public void setClouldDeskopNumOfPersonal(Integer clouldDeskopNumOfPersonal) {
        this.clouldDeskopNumOfPersonal = clouldDeskopNumOfPersonal;
    }

    public String getEditErrorMessage() {
        return editErrorMessage;
    }

    public void setEditErrorMessage(String editErrorMessage) {
        this.editErrorMessage = editErrorMessage;
    }

    public String getEditErrorMessageKey() {
        return editErrorMessageKey;
    }

    public void setEditErrorMessageKey(String editErrorMessageKey) {
        this.editErrorMessageKey = editErrorMessageKey;
    }

    public boolean isSupportGoldenImage() {
        return supportGoldenImage;
    }

    public void setSupportGoldenImage(boolean supportGoldenImage) {
        this.supportGoldenImage = supportGoldenImage;
    }

    public Integer getClouldDeskopNumOfAppLayer() {
        return clouldDeskopNumOfAppLayer;
    }

    public void setClouldDeskopNumOfAppLayer(Integer clouldDeskopNumOfAppLayer) {
        this.clouldDeskopNumOfAppLayer = clouldDeskopNumOfAppLayer;
    }

    public boolean isCanUsed() {
        return canUsed;
    }

    public void setCanUsed(boolean canUsed) {
        this.canUsed = canUsed;
    }

    public ExternalImageVmState getVmState() {
        return vmState;
    }

    public void setVmState(ExternalImageVmState vmState) {
        this.vmState = vmState;
    }

    public ImageTemplateGuestToolState getGuesttoolState() {
        return guesttoolState;
    }

    public void setGuesttoolState(ImageTemplateGuestToolState guesttoolState) {
        this.guesttoolState = guesttoolState;
    }

    public boolean isCanEditSystemDiskSize() {
        return canEditSystemDiskSize;
    }

    public void setCanEditSystemDiskSize(boolean canEditSystemDiskSize) {
        this.canEditSystemDiskSize = canEditSystemDiskSize;
    }

    public boolean isCanEditDataDiskSize() {
        return canEditDataDiskSize;
    }

    public void setCanEditDataDiskSize(boolean canEditDataDiskSize) {
        this.canEditDataDiskSize = canEditDataDiskSize;
    }

    public String getCanUsedMessage() {
        return canUsedMessage;
    }

    public void setCanUsedMessage(String canUsedMessage) {
        this.canUsedMessage = canUsedMessage;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public ImageVmHost getImageVmHost() {
        return imageVmHost;
    }

    public void setImageVmHost(ImageVmHost imageVmHost) {
        this.imageVmHost = imageVmHost;
    }

    public Boolean getEnableGpu() {
        return enableGpu;
    }

    public void setEnableGpu(Boolean enableGpu) {
        this.enableGpu = enableGpu;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public String getGraphicsMemorySize() {
        return graphicsMemorySize;
    }

    public void setGraphicsMemorySize(String graphicsMemorySize) {
        this.graphicsMemorySize = graphicsMemorySize;
    }

    public String getVgpuItem() {
        return vgpuItem;
    }

    public void setVgpuItem(String vgpuItem) {
        this.vgpuItem = vgpuItem;
    }

    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(Boolean enableNested) {
        this.enableNested = enableNested;
    }

    public String getVgpuModel() {
        return vgpuModel;
    }

    public void setVgpuModel(String vgpuModel) {
        this.vgpuModel = vgpuModel;
    }

    public List<VgpuInfoDTO> getVgpuInfoDTOHistoryList() {
        return vgpuInfoDTOHistoryList;
    }

    public void setVgpuInfoDTOHistoryList(List<VgpuInfoDTO> vgpuInfoDTOHistoryList) {
        this.vgpuInfoDTOHistoryList = vgpuInfoDTOHistoryList;
    }

    public Boolean getAd() {
        return isAd;
    }

    public void setAd(Boolean ad) {
        isAd = ad;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public ClusterInfoDTO getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(ClusterInfoDTO clusterInfo) {
        this.clusterInfo = clusterInfo;
    }

    public PlatformStoragePoolDTO getStoragePool() {
        return storagePool;
    }

    public void setStoragePool(PlatformStoragePoolDTO storagePool) {
        this.storagePool = storagePool;
    }

    public List<CbbImageDiskInfoDTO> getImageDiskList() {
        return imageDiskList;
    }

    public void setImageDiskList(List<CbbImageDiskInfoDTO> imageDiskList) {
        this.imageDiskList = imageDiskList;
    }

    public ClusterInfoDTO getVmClusterInfo() {
        return vmClusterInfo;
    }

    public void setVmClusterInfo(ClusterInfoDTO vmClusterInfo) {
        this.vmClusterInfo = vmClusterInfo;
    }

    public PlatformStoragePoolDTO getVmStoragePool() {
        return vmStoragePool;
    }

    public void setVmStoragePool(PlatformStoragePoolDTO vmStoragePool) {
        this.vmStoragePool = vmStoragePool;
    }

    public boolean isCanModifyVm() {
        return canModifyVm;
    }

    public void setCanModifyVm(boolean canModifyVm) {
        this.canModifyVm = canModifyVm;
    }

    public UUID getRootImageId() {
        return rootImageId;
    }

    public void setRootImageId(UUID rootImageId) {
        this.rootImageId = rootImageId;
    }

    public String getRootImageName() {
        return rootImageName;
    }

    public void setRootImageName(String rootImageName) {
        this.rootImageName = rootImageName;
    }

    public UUID getSourceSnapshotId() {
        return sourceSnapshotId;
    }

    public void setSourceSnapshotId(UUID sourceSnapshotId) {
        this.sourceSnapshotId = sourceSnapshotId;
    }

    public ImageRoleType getImageRoleType() {
        return imageRoleType;
    }

    public void setImageRoleType(ImageRoleType imageRoleType) {
        this.imageRoleType = imageRoleType;
    }

    public Boolean getNewestVersion() {
        return isNewestVersion;
    }

    public void setNewestVersion(Boolean newestVersion) {
        isNewestVersion = newestVersion;
    }

    public Boolean getEnableMultipleVersion() {
        return enableMultipleVersion;
    }

    public void setEnableMultipleVersion(Boolean enableMultipleVersion) {
        this.enableMultipleVersion = enableMultipleVersion;
    }

    public ImageTerminalLocalEditType getTerminalLocalEditType() {
        return terminalLocalEditType;
    }

    public void setTerminalLocalEditType(ImageTerminalLocalEditType terminalLocalEditType) {
        this.terminalLocalEditType = terminalLocalEditType;
    }

    public List<ImageRelateDriverInfoDTO> getImageDriverList() {
        return imageDriverList;
    }

    public void setImageDriverList(List<ImageRelateDriverInfoDTO> imageDriverList) {
        this.imageDriverList = imageDriverList;
    }

    public CbbCpuArchType getCpuArch() {
        return cpuArch;
    }

    public void setCpuArch(CbbCpuArchType cpuArch) {
        this.cpuArch = cpuArch;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }
}
