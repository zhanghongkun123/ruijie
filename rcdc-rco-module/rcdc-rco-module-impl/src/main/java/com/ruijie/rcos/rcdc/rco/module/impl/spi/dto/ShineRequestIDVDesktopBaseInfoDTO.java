package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbKeyboardEmulationType;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskPersonalConfigStrategyType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.PartType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: IDV虚机启动参数ACPI信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/19
 *
 * @author chen zj
 */
public class ShineRequestIDVDesktopBaseInfoDTO {

    /**
     * 云桌面ID
     */
    @NotNull
    @JSONField(name = "desk_id")
    private UUID uuid;

    /**
     * 桌面模式: 个性、还原、应用分层
     */
    @NotNull
    @JSONField(name = "desktop_pattern")
    private String desktopPattern;

    /**
     * 桌面名称
     */
    @NotNull
    @JSONField(name = "desk_name")
    private String deskName;

    /**
     * 镜像名称
     */
    @NotNull
    @JSONField(name = "image_name")
    private String imageName;

    /**
     * 镜像模板id
     */
    @NotNull
    @JSONField(name = "image_id")
    private UUID imageId;

    /**
     * 系统盘大小
     */
    @NotNull
    @JSONField(name = "system_disk_size")
    private Integer systemDiskSize;

    /**
     * 是否开启个人盘
     */
    @NotNull
    @JSONField(name = "data_disk_enable")
    private Boolean dataDiskEnable;


    /**
     * 是否开启重定向
     */
    @NotNull
    @JSONField(name = "desktop_redirect")
    private Boolean desktopRedirect;

    /**
     * 是否驱动安装, 正常用户启动都是：noinstall
     * <p>
     * noinstall:未安装
     * auto:自动
     * manual:手动
     */
    @NotNull
    @JSONField(name = "install_driver")
    private String installDriver;

    /**
     * gt是否需要升级
     */
    @NotNull
    @JSONField(name = "vmtype")
    private String vmType;

    /**
     * 操作系统类型
     */
    @NotNull
    @JSONField(name = "os_type")
    private CbbOsType osType;

    /**
     * 桌面采用模拟还是透传
     */
    @Nullable
    @JSONField(name = "vmmode")
    private String vmModel;

    /**
     * 声卡采用模拟还是透传
     */
    @Nullable
    @JSONField(name = "audio_mode")
    private String audioMode;

    /**
     * 终端第二张网卡采用模拟还是透传
     */
    @Nullable
    @JSONField(name = "nic_mode")
    private String nicMode;

    /**
     * 串口采用模拟还是透传
     */
    @Nullable
    @JSONField(name = "serial_mode")
    private String serialMode;

    /**
     * USB控制器采用模拟还是透传
     */
    @Nullable
    @JSONField(name = "usbctrl_mode")
    private String usbctrlMode;

    /**
     * 并口采用模拟还是透传
     */
    @Nullable
    @JSONField(name = "parallel_mode")
    private String parallelMode;

    /**
     * 虚拟化策略版本
     */
    @Nullable
    @JSONField(name = "vmmode_version")
    private Integer vmmodeVersion;

    /**
     * 虚机运行介质
     * SERVER:运行在服务器上VDI场景
     * TERM:运行在IDV终端山
     */
    @NotNull
    private String host;

    /**
     * 应用分层磁盘数
     */
    @Nullable
    @JSONField(name = "layer_disk_number")
    private int layerDiskNumber;

    /**
     * 是否开启应用分层
     */
    @Nullable
    @JSONField(name = "layer_on_1")
    private Boolean layerOn;

    /**
     * 应用分层是否使用x64位方式
     */
    @Nullable
    @JSONField(name = "layer_x64_1")
    private Boolean layerX64;


    @NotNull
    @JSONField(name = "open_internet")
    private Boolean openInternet;

    @NotNull
    @JSONField(name = "part_type")
    private PartType partType;

    @Nullable
    @JSONField(name = "personal_config_strategy")
    private CbbDeskPersonalConfigStrategyType personalConfigStrategy;

    @NotNull
    private Boolean supportGoldenImage;

    /**
     * 是否强制使用计算机名设置
     */
    @Nullable
    @JSONField(name = "computer_name_set")
    private Boolean computerNameSet;

    /**
     * 计算机名
     */
    @Nullable
    @JSONField(name = "computer_name")
    private String computerName;

    /**
     * 嵌套虚拟化
     */
    @Nullable
    @JSONField(name = "enable_nested")
    private Boolean enableNested = false;

    @NotNull
    @JSONField(name = "enable_full_system_disk")
    private Boolean enableFullSystemDisk;

    /**
     * 用户配置策略启用状态
     */
    @JSONField(name = "upm_policy_enable")
    private Integer upmPolicyEnable = 0;

    /**
     * 键盘模拟类型
     */
    @Nullable
    private Boolean hasUsbKeyboard;

    @Nullable
    @JSONField(name = "using_drives")
    private String usingDrives;

    /**
     * 自动登录密码
     */
    @Nullable
    @JSONField(name = "win_password")
    private String winPassword;

    /**
     * 自动登录用户名
     */
    @Nullable
    @JSONField(name = "win_username")
    private String winUsername;

    /**
     * 自动登录
     */
    @Nullable
    @JSONField(name = "auto_logon")
    private String autoLogon;


    /**
     * vms映射Id
     */
    @Nullable
    private UUID vmsId;

    @Nullable
    @JSONField(name = "image_recovery_point_id")
    private UUID imageRecoveryPointId;

    @Nullable
    private String diskController;

    public int getLayerDiskNumber() {
        return layerDiskNumber;
    }

    public void setLayerDiskNumber(int layerDiskNumber) {
        this.layerDiskNumber = layerDiskNumber;
    }

    public Boolean getLayerOn() {
        return layerOn;
    }

    public void setLayerOn(Boolean layerOn) {
        this.layerOn = layerOn;
    }

    public Boolean getLayerX64() {
        return layerX64;
    }

    public void setLayerX64(Boolean layerX64) {
        this.layerX64 = layerX64;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDesktopPattern() {
        return desktopPattern;
    }

    public void setDesktopPattern(String desktopPattern) {
        this.desktopPattern = desktopPattern;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public Boolean getDataDiskEnable() {
        return dataDiskEnable;
    }

    public void setDataDiskEnable(Boolean dataDiskEnable) {
        this.dataDiskEnable = dataDiskEnable;
    }

    public Boolean getDesktopRedirect() {
        return desktopRedirect;
    }

    public void setDesktopRedirect(Boolean desktopRedirect) {
        this.desktopRedirect = desktopRedirect;
    }

    public String getInstallDriver() {
        return installDriver;
    }

    public void setInstallDriver(String installDriver) {
        this.installDriver = installDriver;
    }

    public String getVmType() {
        return vmType;
    }

    public void setVmType(String vmType) {
        this.vmType = vmType;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public String getVmModel() {
        return vmModel;
    }

    public void setVmModel(String vmModel) {
        this.vmModel = vmModel;
    }

    public String getAudioMode() {
        return audioMode;
    }

    public void setAudioMode(String audioMode) {
        this.audioMode = audioMode;
    }

    public String getNicMode() {
        return nicMode;
    }

    public void setNicMode(String nicMode) {
        this.nicMode = nicMode;
    }

    public String getSerialMode() {
        return serialMode;
    }

    public void setSerialMode(String serialMode) {
        this.serialMode = serialMode;
    }

    public String getUsbctrlMode() {
        return usbctrlMode;
    }

    public void setUsbctrlMode(String usbctrlMode) {
        this.usbctrlMode = usbctrlMode;
    }

    public String getParallelMode() {
        return parallelMode;
    }

    public void setParallelMode(String parallelMode) {
        this.parallelMode = parallelMode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Boolean getOpenInternet() {
        return openInternet;
    }

    public void setOpenInternet(Boolean openInternet) {
        this.openInternet = openInternet;
    }

    public PartType getPartType() {
        return partType;
    }

    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    @Nullable
    public CbbDeskPersonalConfigStrategyType getPersonalConfigStrategy() {
        return personalConfigStrategy;
    }

    public void setPersonalConfigStrategy(@Nullable CbbDeskPersonalConfigStrategyType personalConfigStrategy) {
        this.personalConfigStrategy = personalConfigStrategy;
    }

    public Boolean getSupportGoldenImage() {
        return supportGoldenImage;
    }

    public void setSupportGoldenImage(Boolean supportGoldenImage) {
        this.supportGoldenImage = supportGoldenImage;
    }

    public Boolean getComputerNameSet() {
        return computerNameSet;
    }

    public void setComputerNameSet(Boolean computerNameSet) {
        this.computerNameSet = computerNameSet;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    @Nullable
    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(@Nullable Boolean enableNested) {
        this.enableNested = enableNested;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public Integer getUpmPolicyEnable() {
        return upmPolicyEnable;
    }

    public void setUpmPolicyEnable(Integer upmPolicyEnable) {
        this.upmPolicyEnable = upmPolicyEnable;
    }

    @Nullable
    public Boolean getHasUsbKeyboard() {
        return hasUsbKeyboard;
    }

    public void setHasUsbKeyboard(@Nullable Boolean hasUsbKeyboard) {
        this.hasUsbKeyboard = hasUsbKeyboard;
    }

    /**
     * 设置是否有USB键盘设置
     *
     * @param keyboardEmulationType 键盘类型
     */
    public void setHasUsbKeyboard(@Nullable CbbKeyboardEmulationType keyboardEmulationType) {
        if (CbbKeyboardEmulationType.USB == keyboardEmulationType) {
            this.hasUsbKeyboard = Boolean.TRUE;
        } else {
            this.hasUsbKeyboard = Boolean.FALSE;
        }
    }

    @Nullable
    public String getUsingDrives() {
        return usingDrives;
    }

    public void setUsingDrives(@Nullable String usingDrives) {
        this.usingDrives = usingDrives;
    }

    @Nullable
    public String getWinPassword() {
        return winPassword;
    }

    public void setWinPassword(@Nullable String winPassword) {
        this.winPassword = winPassword;
    }

    @Nullable
    public String getWinUsername() {
        return winUsername;
    }

    public void setWinUsername(@Nullable String winUsername) {
        this.winUsername = winUsername;
    }

    @Nullable
    public String getAutoLogon() {
        return autoLogon;
    }

    public void setAutoLogon(@Nullable String autoLogon) {
        this.autoLogon = autoLogon;
    }

    @Nullable
    public UUID getVmsId() {
        return vmsId;
    }

    public void setVmsId(@Nullable UUID vmsId) {
        this.vmsId = vmsId;
    }

    @Nullable
    public UUID getImageRecoveryPointId() {
        return imageRecoveryPointId;
    }

    public void setImageRecoveryPointId(@Nullable UUID imageRecoveryPointId) {
        this.imageRecoveryPointId = imageRecoveryPointId;
    }

    @Nullable
    public String getDiskController() {
        return diskController;
    }

    public void setDiskController(@Nullable String diskController) {
        this.diskController = diskController;
    }

    @Nullable
    public Integer getVmmodeVersion() {
        return vmmodeVersion;
    }

    public void setVmmodeVersion(@Nullable Integer vmmodeVersion) {
        this.vmmodeVersion = vmmodeVersion;
    }
}
