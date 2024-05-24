package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ControlStateEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageRelateDriverInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageSnapshotRestoreTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTemplatePublishTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ProductDriverDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;

import java.util.List;
import java.util.UUID;

/**
 * Description: ImageTemplateInfoDTO Description
 * Copyright: Copyright (c) 2017
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/05/06
 *
 * @author chixin
 */
public class ImageTemplateInfoDTO extends PlatformBaseInfoDTO {

    private UUID id;

    private int cpu;

    private Double memory;

    private int imageSystemSize;

    private ImageTemplateState imageState;

    private String imageName;

    private String imageFileName;

    private String network;

    private String guestTool;

    private String ip;

    private String describe;

    private long createTime;

    private long lastEditTime;

    private String imageSystemType;

    private CbbImageType cbbImageType;

    private ProductDriverDTO[] driverInfoArr;

    private Boolean enableGpu;

    private VgpuType vgpuType;

    private String graphicsMemorySize;

    private String vgpuItem;

    private String vgpuModel;

    private Boolean enableNested;

    private ImageTemplatePublishTaskDTO publishTaskDTO;

    private ControlStateEnum controlState;

    private FtpUploadState ftpUploadState;

    private ImageVmHost imageVmHost;

    private ClusterInfoDTO clusterInfoDTO;

    private List<VgpuInfoDTO> vgpuInfoDTOHistoryList;

    private ImageSnapshotRestoreTaskDTO restoreTaskDTO;

    private String mac;

    /**
     * 镜像加域结果
     */
    private Boolean isAd;

    /**
     * 计算机名
     */
    private String computerName;

    private Integer systemDiskFileSize;

    private Integer dataDiskFileSize;

    private String storagePoolName;

    private List<CbbImageDiskInfoDTO> imageDiskList;

    private String osVersion;

    private Boolean enableMultipleVersion;

    private UUID rootImageId;

    private String rootImageName;

    private UUID sourceSnapshotId;

    private ImageRoleType imageRoleType;

    private Boolean isNewestVersion;

    private List<ImageRelateDriverInfoDTO> imageDriverList;

    private ImageTerminalLocalEditType terminalLocalEditType;

    private Boolean hasImportDriverPackage;

    private CbbRemoteTerminalImageEditEnum remoteTerminalImageEditState;

    private CbbCpuArchType cpuArch;

    private Boolean enableGlobalImage;

    private Boolean isGlobalImage;

    public Integer getDataDiskFileSize() {
        return dataDiskFileSize;
    }

    public void setDataDiskFileSize(Integer dataDiskFileSize) {
        this.dataDiskFileSize = dataDiskFileSize;
    }

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

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public int getImageSystemSize() {
        return imageSystemSize;
    }

    public void setImageSystemSize(int imageSystemSize) {
        this.imageSystemSize = imageSystemSize;
    }

    public ImageTemplateState getImageState() {
        return imageState;
    }

    public void setImageState(ImageTemplateState imageState) {
        this.imageState = imageState;
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

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getGuestTool() {
        return guestTool;
    }

    public void setGuestTool(String guestTool) {
        this.guestTool = guestTool;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(long lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getImageSystemType() {
        return imageSystemType;
    }

    public void setImageSystemType(String imageSystemType) {
        this.imageSystemType = imageSystemType;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public ProductDriverDTO[] getDriverInfoArr() {
        return driverInfoArr;
    }

    public void setDriverInfoArr(ProductDriverDTO[] driverInfoArr) {
        this.driverInfoArr = driverInfoArr;
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

    public String getVgpuModel() {
        return vgpuModel;
    }

    public void setVgpuModel(String vgpuModel) {
        this.vgpuModel = vgpuModel;
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

    public ControlStateEnum getControlState() {
        return controlState;
    }

    public void setControlState(ControlStateEnum controlState) {
        this.controlState = controlState;
    }

    public FtpUploadState getFtpUploadState() {
        return ftpUploadState;
    }

    public void setFtpUploadState(FtpUploadState ftpUploadState) {
        this.ftpUploadState = ftpUploadState;
    }

    public ImageVmHost getImageVmHost() {
        return imageVmHost;
    }

    public void setImageVmHost(ImageVmHost imageVmHost) {
        this.imageVmHost = imageVmHost;
    }

    public List<VgpuInfoDTO> getVgpuInfoDTOHistoryList() {
        return vgpuInfoDTOHistoryList;
    }

    public void setVgpuInfoDTOHistoryList(List<VgpuInfoDTO> vgpuInfoDTOHistoryList) {
        this.vgpuInfoDTOHistoryList = vgpuInfoDTOHistoryList;
    }

    public ImageSnapshotRestoreTaskDTO getRestoreTaskDTO() {
        return restoreTaskDTO;
    }

    public void setRestoreTaskDTO(ImageSnapshotRestoreTaskDTO restoreTaskDTO) {
        this.restoreTaskDTO = restoreTaskDTO;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public Integer getSystemDiskFileSize() {
        return systemDiskFileSize;
    }

    public void setSystemDiskFileSize(Integer systemDiskFileSize) {
        this.systemDiskFileSize = systemDiskFileSize;
    }

    public ClusterInfoDTO getClusterInfoDTO() {
        return clusterInfoDTO;
    }

    public void setClusterInfoDTO(ClusterInfoDTO clusterInfoDTO) {
        this.clusterInfoDTO = clusterInfoDTO;
    }

    public String getStoragePoolName() {
        return storagePoolName;
    }

    public void setStoragePoolName(String storagePoolName) {
        this.storagePoolName = storagePoolName;
    }

    public List<CbbImageDiskInfoDTO> getImageDiskList() {
        return imageDiskList;
    }

    public void setImageDiskList(List<CbbImageDiskInfoDTO> imageDiskList) {
        this.imageDiskList = imageDiskList;
    }


    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }


    public Boolean getEnableMultipleVersion() {
        return enableMultipleVersion;
    }

    public void setEnableMultipleVersion(Boolean enableMultipleVersion) {
        this.enableMultipleVersion = enableMultipleVersion;
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

    public List<ImageRelateDriverInfoDTO> getImageDriverList() {
        return imageDriverList;
    }

    public void setImageDriverList(List<ImageRelateDriverInfoDTO> imageDriverList) {
        this.imageDriverList = imageDriverList;
    }

    public ImageTerminalLocalEditType getTerminalLocalEditType() {
        return terminalLocalEditType;
    }

    public void setTerminalLocalEditType(ImageTerminalLocalEditType terminalLocalEditType) {
        this.terminalLocalEditType = terminalLocalEditType;
    }

    public Boolean getHasImportDriverPackage() {
        return hasImportDriverPackage;
    }

    public void setHasImportDriverPackage(Boolean hasImportDriverPackage) {
        this.hasImportDriverPackage = hasImportDriverPackage;
    }

    public CbbRemoteTerminalImageEditEnum getRemoteTerminalImageEditState() {
        return remoteTerminalImageEditState;
    }

    public void setRemoteTerminalImageEditState(CbbRemoteTerminalImageEditEnum remoteTerminalImageEditState) {
        this.remoteTerminalImageEditState = remoteTerminalImageEditState;
    }

    public CbbCpuArchType getCpuArch() {
        return cpuArch;
    }

    public void setCpuArch(CbbCpuArchType cpuArch) {
        this.cpuArch = cpuArch;
    }


    public String getGraphicsMemorySize() {
        return graphicsMemorySize;
    }

    public void setGraphicsMemorySize(String graphicsMemorySize) {
        this.graphicsMemorySize = graphicsMemorySize;
    }

    public Boolean getEnableGlobalImage() {
        return enableGlobalImage;
    }

    public void setEnableGlobalImage(Boolean enableGlobalImage) {
        this.enableGlobalImage = enableGlobalImage;
    }

    public Boolean getIsGlobalImage() {
        return isGlobalImage;
    }

    public void setIsGlobalImage(Boolean globalImage) {
        isGlobalImage = globalImage;
    }

}
