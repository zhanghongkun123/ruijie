package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditFileConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditPrinterConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbIpLimitDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbTransparentEncryptDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbUsbBandwidthDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.UsbStorageDeviceAccelerationEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskTopAllowLoginTimeDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.CdRomMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.NetDiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.GetWatermarkConfigResponse;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月15日
 *
 * @author zhfw
 */
public class DeskStrategyVO {

    private UUID id;

    private String strategyName;

    private CbbCloudDeskPattern desktopType;

    private CbbDesktopSessionType sessionType;

    private Boolean enableSharedPrinting;

    private Integer systemDisk;

    private UUID[] usbTypeIdArr;

    private Boolean enableUsbReadOnly;

    private Boolean enableInternet;

    private Boolean enableDoubleScreen;

    private Boolean enableForbidCatchScreen;

    private Integer cloudNumber;

    private Boolean canUsed;

    /**
     * 不能使用提示消息
     */
    private String canUsedMessage;

    private CbbDeskStrategyState deskStrategyState;

    private CbbClipboardMode clipBoardMode;

    private CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr;

    private Boolean enableOpenDesktopRedirect;

    /**
     * idv特有，是否启用本地盘
     */
    private Boolean enableAllowLocalDisk;

    private CbbStrategyType strategyType;

    /**
     * 计算机名称，RCO5.3版本新增
     */
    private String computerName;

    private Boolean hasBindUserOrUserGroup;

    private Boolean enableNested;

    /**
     * 创建者登录账号
     */
    private String  creatorUserName;

    /**
     * 是否开启协议代理
     */
    private Boolean enableAgreementAgency;

    /**
     * 强制使用协议代理
     */
    private Boolean enableForceUseAgreementAgency;

    /**
     * 是否开启网页客户端接入
     */
    private Boolean enableWebClient;

    @ApiModelProperty(value = "云桌面标签")
    private String remark;

    @ApiModelProperty(value = "AD域加入OU路径")
    private String adOu;

    /**
     * 磁盘映射枚举，关闭、可读、读写
     */
    private DiskMappingEnum diskMappingType;

    /**
     * 是否开启局域网自动检测
     */
    private Boolean enableLanAutoDetection;

    private DeskCreateMode deskCreateMode;

    /**
     * 电源计划
     */
    private CbbPowerPlanEnum powerPlan;

    /**
     * 多久没操作后执行电源计划
     */
    private Integer powerPlanTime;

    /**
     * est连接空闲超时时间
     */
    private Integer estIdleOverTime;

    /**
     * 键盘模拟类型
     */
    private CbbKeyboardEmulationType keyboardEmulationType;

    /**
     * 是否开启系统盘自动扩容
     */
    private Boolean enableFullSystemDisk;

    /**
     * 网盘映射枚举，关闭、可读、读写
     */
    private NetDiskMappingEnum netDiskMappingType;

    /**
     * CDROM映射枚举，关闭、可读
     */
    private CdRomMappingEnum cdRomMappingType;

    /**
     * 是否开启桌面登录时间限制
     */
    @NotNull
    private Boolean enableOpenLoginLimit;

    /**
     * 云桌面允许登录时间
     */
    @ApiModelProperty(value = "云桌面允许登录时间")
    private List<DeskTopAllowLoginTimeDTO> desktopAllowLoginTimeList;

    private Boolean enableUserSnapshot;

    /**
     * 盘符预占配置
     */
    @Nullable
    private String[] desktopOccupyDriveArr;

    private CbbIpLimitModeEnum ipLimitMode;

    private List<CbbIpLimitDTO> ipSegmentDTOList;

    /**
     * 是否开启水印
     */
    @ApiModelProperty("是否开启水印")
    @Nullable
    private Boolean enableWatermark;

    /**
     * 水印配置内容
     */
    @ApiModelProperty("水印配置内容")
    @Nullable
    private GetWatermarkConfigResponse watermarkInfo;

    /**
     * 描述
     */
    private String note;

    /**
     * 是否开启文件审计
     */
    private Boolean enableAuditFile;

    /**
     * 文件审计配置内容
     */
    private CbbAuditFileConfigDTO auditFileInfo;

    /**
     * 是否开启打印审计
     */
    private Boolean enableAuditPrinter;

    /**
     * 打印审计配置内容
     */
    private CbbAuditPrinterConfigDTO auditPrinterInfo;

    /**
     * 桌面登录账号同步
     */
    @Nullable
    private Boolean desktopSyncLoginAccount;

    /**
     * 桌面登录密码同步
     */
    @Nullable
    private Boolean desktopSyncLoginPassword;

    /**
     * 桌面登录账号权限
     */
    @Nullable
    private CbbSyncLoginAccountPermissionEnums desktopSyncLoginAccountPermission;

    /**
     * 是否开启高可用
     */
    @Nullable
    private Boolean enableHa;

    /**
     * "配置HA优先级"
     */
    @Nullable
    private Integer haPriority;

    /**
     * Usb存储设备映射
     */
    private CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode;

    @Nullable
    private UsbStorageDeviceAccelerationEnum usbStorageDeviceAcceleration;

    @Nullable
    private CbbEstProtocolType estProtocolType;

    /**
     * 协议配置
     */
    private AgreementDTO agreementInfo;

    /**
     * 启用串口重定向
     */
    private Boolean enableSerialPortRedirect;

    /**
     * 启用并口重定向
     */
    private Boolean enableParallelPortRedirect;

    /**
     * usb压缩加速
     */
    private Boolean enableUsbCompressAcceleration;

    /**
     * usb带宽控制
     */
    private Boolean enableUsbBandwidth;

    /**
     * usb带宽控制配置
     */
    private CbbUsbBandwidthDTO usbBandwidthInfo;

    /**
     * 透明加解密总开关
     */
    private Boolean enableTransparentEncrypt;

    /**
     * 透明加解密配置
     */
    private CbbTransparentEncryptDTO transparentEncryptInfo;

    public String getAdOu() {
        return adOu;
    }

    public void setAdOu(String adOu) {
        this.adOu = adOu;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Boolean getEnableSharedPrinting() {
        return enableSharedPrinting;
    }

    public void setEnableSharedPrinting(Boolean enableSharedPrinting) {
        this.enableSharedPrinting = enableSharedPrinting;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public UUID[] getUsbTypeIdArr() {
        return usbTypeIdArr;
    }

    public void setUsbTypeIdArr(UUID[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
    }

    public Boolean getEnableUsbReadOnly() {
        return enableUsbReadOnly;
    }

    public void setEnableUsbReadOnly(Boolean enableUsbReadOnly) {
        this.enableUsbReadOnly = enableUsbReadOnly;
    }

    public Boolean getEnableInternet() {
        return enableInternet;
    }

    public void setEnableInternet(Boolean enableInternet) {
        this.enableInternet = enableInternet;
    }

    public Boolean getEnableDoubleScreen() {
        return enableDoubleScreen;
    }

    public void setEnableDoubleScreen(Boolean enableDoubleScreen) {
        this.enableDoubleScreen = enableDoubleScreen;
    }

    public Boolean getEnableForbidCatchScreen() {
        return enableForbidCatchScreen;
    }

    public void setEnableForbidCatchScreen(Boolean enableForbidCatchScreen) {
        this.enableForbidCatchScreen = enableForbidCatchScreen;
    }

    public Integer getCloudNumber() {
        return cloudNumber;
    }

    public void setCloudNumber(Integer cloudNumber) {
        this.cloudNumber = cloudNumber;
    }

    public Boolean getCanUsed() {
        return canUsed;
    }

    public void setCanUsed(Boolean canUsed) {
        this.canUsed = canUsed;
    }

    public CbbDeskStrategyState getDeskStrategyState() {
        return deskStrategyState;
    }

    public void setDeskStrategyState(CbbDeskStrategyState deskStrategyState) {
        this.deskStrategyState = deskStrategyState;
    }

    public CbbClipboardMode getClipBoardMode() {
        return clipBoardMode;
    }

    public void setClipBoardMode(CbbClipboardMode clipBoardMode) {
        this.clipBoardMode = clipBoardMode;
    }

    public Boolean getEnableOpenDesktopRedirect() {
        return enableOpenDesktopRedirect;
    }

    public void setEnableOpenDesktopRedirect(Boolean enableOpenDesktopRedirect) {
        this.enableOpenDesktopRedirect = enableOpenDesktopRedirect;
    }

    public Boolean getEnableAllowLocalDisk() {
        return enableAllowLocalDisk;
    }

    public void setEnableAllowLocalDisk(Boolean enableAllowLocalDisk) {
        this.enableAllowLocalDisk = enableAllowLocalDisk;
    }

    public CbbStrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(CbbStrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public String getCanUsedMessage() {
        return canUsedMessage;
    }

    public void setCanUsedMessage(String canUsedMessage) {
        this.canUsedMessage = canUsedMessage;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public Boolean getHasBindUserOrUserGroup() {
        return hasBindUserOrUserGroup;
    }

    public void setHasBindUserOrUserGroup(Boolean hasBindUserOrUserGroup) {
        this.hasBindUserOrUserGroup = hasBindUserOrUserGroup;
    }

    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(Boolean enableNested) {
        this.enableNested = enableNested;
    }

    public Boolean getEnableAgreementAgency() {
        return enableAgreementAgency;
    }

    public void setEnableAgreementAgency(Boolean enableAgreementAgency) {
        this.enableAgreementAgency = enableAgreementAgency;
    }

    public Boolean getEnableWebClient() {
        return enableWebClient;
    }

    public void setEnableWebClient(Boolean enableWebClient) {
        this.enableWebClient = enableWebClient;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public DiskMappingEnum getDiskMappingType() {
        return diskMappingType;
    }

    public void setDiskMappingType(DiskMappingEnum diskMappingType) {
        this.diskMappingType = diskMappingType;
    }

    public Boolean getEnableLanAutoDetection() {
        return enableLanAutoDetection;
    }

    public void setEnableLanAutoDetection(Boolean enableLanAutoDetection) {
        this.enableLanAutoDetection = enableLanAutoDetection;
    }

    public DeskCreateMode getDeskCreateMode() {
        return deskCreateMode;
    }

    public void setDeskCreateMode(DeskCreateMode deskCreateMode) {
        this.deskCreateMode = deskCreateMode;
    }

    public CbbPowerPlanEnum getPowerPlan() {
        return powerPlan;
    }

    public void setPowerPlan(CbbPowerPlanEnum powerPlan) {
        this.powerPlan = powerPlan;
    }

    public Integer getPowerPlanTime() {
        return powerPlanTime;
    }

    public void setPowerPlanTime(Integer powerPlanTime) {
        this.powerPlanTime = powerPlanTime;
    }

    public Integer getEstIdleOverTime() {
        return estIdleOverTime;
    }

    public void setEstIdleOverTime(Integer estIdleOverTime) {
        this.estIdleOverTime = estIdleOverTime;
    }

    public CbbKeyboardEmulationType getKeyboardEmulationType() {
        return keyboardEmulationType;
    }

    public void setKeyboardEmulationType(CbbKeyboardEmulationType keyboardEmulationType) {
        this.keyboardEmulationType = keyboardEmulationType;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public NetDiskMappingEnum getNetDiskMappingType() {
        return netDiskMappingType;
    }

    public void setNetDiskMappingType(NetDiskMappingEnum netDiskMappingType) {
        this.netDiskMappingType = netDiskMappingType;
    }

    public CdRomMappingEnum getCdRomMappingType() {
        return cdRomMappingType;
    }

    public void setCdRomMappingType(CdRomMappingEnum cdRomMappingType) {
        this.cdRomMappingType = cdRomMappingType;
    }

    public Boolean getEnableOpenLoginLimit() {
        return enableOpenLoginLimit;
    }

    public void setEnableOpenLoginLimit(Boolean enableOpenLoginLimit) {
        this.enableOpenLoginLimit = enableOpenLoginLimit;
    }

    public Boolean getEnableUserSnapshot() {
        return enableUserSnapshot;
    }

    public void setEnableUserSnapshot(Boolean enableUserSnapshot) {
        this.enableUserSnapshot = enableUserSnapshot;
    }

    @Nullable
    public List<DeskTopAllowLoginTimeDTO> getDesktopAllowLoginTimeList() {
        return desktopAllowLoginTimeList;
    }

    public void setDesktopAllowLoginTimeList(@Nullable List<DeskTopAllowLoginTimeDTO> desktopAllowLoginTimeList) {
        this.desktopAllowLoginTimeList = desktopAllowLoginTimeList;
    }

    @Nullable
    public String[] getDesktopOccupyDriveArr() {
        return desktopOccupyDriveArr;
    }

    public void setDesktopOccupyDriveArr(@Nullable String[] desktopOccupyDriveArr) {
        this.desktopOccupyDriveArr = desktopOccupyDriveArr;
    }

    public CbbIpLimitModeEnum getIpLimitMode() {
        return ipLimitMode;
    }

    public void setIpLimitMode(CbbIpLimitModeEnum ipLimitMode) {
        this.ipLimitMode = ipLimitMode;
    }

    public List<CbbIpLimitDTO> getIpSegmentDTOList() {
        return ipSegmentDTOList;
    }

    public void setIpSegmentDTOList(List<CbbIpLimitDTO> ipSegmentDTOList) {
        this.ipSegmentDTOList = ipSegmentDTOList;
    }

    public CbbClipBoardSupportTypeDTO[] getClipBoardSupportTypeArr() {
        return clipBoardSupportTypeArr;
    }

    public void setClipBoardSupportTypeArr(CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr) {
        this.clipBoardSupportTypeArr = clipBoardSupportTypeArr;
    }

    @Nullable
    public Boolean getEnableWatermark() {
        return enableWatermark;
    }

    public void setEnableWatermark(@Nullable Boolean enableWatermark) {
        this.enableWatermark = enableWatermark;
    }

    @Nullable
    public GetWatermarkConfigResponse getWatermarkInfo() {
        return watermarkInfo;
    }

    public void setWatermarkInfo(@Nullable GetWatermarkConfigResponse watermarkInfo) {
        this.watermarkInfo = watermarkInfo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getEnableAuditFile() {
        return enableAuditFile;
    }

    public void setEnableAuditFile(Boolean enableAuditFile) {
        this.enableAuditFile = enableAuditFile;
    }

    public CbbAuditFileConfigDTO getAuditFileInfo() {
        return auditFileInfo;
    }

    public void setAuditFileInfo(CbbAuditFileConfigDTO auditFileInfo) {
        this.auditFileInfo = auditFileInfo;
    }

    public Boolean getEnableAuditPrinter() {
        return enableAuditPrinter;
    }

    public void setEnableAuditPrinter(Boolean enableAuditPrinter) {
        this.enableAuditPrinter = enableAuditPrinter;
    }

    public CbbAuditPrinterConfigDTO getAuditPrinterInfo() {
        return auditPrinterInfo;
    }

    public void setAuditPrinterInfo(CbbAuditPrinterConfigDTO auditPrinterInfo) {
        this.auditPrinterInfo = auditPrinterInfo;
    }

    @Nullable
    public Boolean getDesktopSyncLoginAccount() {
        return desktopSyncLoginAccount;
    }

    public void setDesktopSyncLoginAccount(@Nullable Boolean desktopSyncLoginAccount) {
        this.desktopSyncLoginAccount = desktopSyncLoginAccount;
    }

    @Nullable
    public Boolean getDesktopSyncLoginPassword() {
        return desktopSyncLoginPassword;
    }

    public void setDesktopSyncLoginPassword(@Nullable Boolean desktopSyncLoginPassword) {
        this.desktopSyncLoginPassword = desktopSyncLoginPassword;
    }

    @Nullable
    public CbbSyncLoginAccountPermissionEnums getDesktopSyncLoginAccountPermission() {
        return desktopSyncLoginAccountPermission;
    }

    public void setDesktopSyncLoginAccountPermission(@Nullable CbbSyncLoginAccountPermissionEnums desktopSyncLoginAccountPermission) {
        this.desktopSyncLoginAccountPermission = desktopSyncLoginAccountPermission;
    }

    @Nullable
    public Boolean getEnableHa() {
        return enableHa;
    }

    public void setEnableHa(@Nullable Boolean enableHa) {
        this.enableHa = enableHa;
    }

    @Nullable
    public Integer getHaPriority() {
        return haPriority;
    }

    public void setHaPriority(@Nullable Integer haPriority) {
        this.haPriority = haPriority;
    }

    public CbbUsbStorageDeviceMappingMode getUsbStorageDeviceMappingMode() {
        return usbStorageDeviceMappingMode;
    }

    public void setUsbStorageDeviceMappingMode(CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode) {
        this.usbStorageDeviceMappingMode = usbStorageDeviceMappingMode;
    }

    @Nullable
    public UsbStorageDeviceAccelerationEnum getUsbStorageDeviceAcceleration() {
        return usbStorageDeviceAcceleration;
    }

    public void setUsbStorageDeviceAcceleration(@Nullable UsbStorageDeviceAccelerationEnum usbStorageDeviceAcceleration) {
        this.usbStorageDeviceAcceleration = usbStorageDeviceAcceleration;
    }

    @Nullable
    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(@Nullable CbbEstProtocolType estProtocolType) {
        this.estProtocolType = estProtocolType;
    }

    public Boolean getEnableSerialPortRedirect() {
        return enableSerialPortRedirect;
    }

    public void setEnableSerialPortRedirect(Boolean enableSerialPortRedirect) {
        this.enableSerialPortRedirect = enableSerialPortRedirect;
    }

    public Boolean getEnableParallelPortRedirect() {
        return enableParallelPortRedirect;
    }

    public void setEnableParallelPortRedirect(Boolean enableParallelPortRedirect) {
        this.enableParallelPortRedirect = enableParallelPortRedirect;
    }

    public Boolean getEnableUsbCompressAcceleration() {
        return enableUsbCompressAcceleration;
    }

    public void setEnableUsbCompressAcceleration(Boolean enableUsbCompressAcceleration) {
        this.enableUsbCompressAcceleration = enableUsbCompressAcceleration;
    }

    public Boolean getEnableUsbBandwidth() {
        return enableUsbBandwidth;
    }

    public void setEnableUsbBandwidth(Boolean enableUsbBandwidth) {
        this.enableUsbBandwidth = enableUsbBandwidth;
    }

    public CbbUsbBandwidthDTO getUsbBandwidthInfo() {
        return usbBandwidthInfo;
    }

    public void setUsbBandwidthInfo(CbbUsbBandwidthDTO usbBandwidthInfo) {
        this.usbBandwidthInfo = usbBandwidthInfo;
    }

    public AgreementDTO getAgreementInfo() {
        return agreementInfo;
    }

    public void setAgreementInfo(AgreementDTO agreementInfo) {
        this.agreementInfo = agreementInfo;
    }

    public Boolean getEnableTransparentEncrypt() {
        return enableTransparentEncrypt;
    }

    public void setEnableTransparentEncrypt(Boolean enableTransparentEncrypt) {
        this.enableTransparentEncrypt = enableTransparentEncrypt;
    }

    public CbbTransparentEncryptDTO getTransparentEncryptInfo() {
        return transparentEncryptInfo;
    }

    public void setTransparentEncryptInfo(CbbTransparentEncryptDTO transparentEncryptInfo) {
        this.transparentEncryptInfo = transparentEncryptInfo;
    }

    public Boolean getEnableForceUseAgreementAgency() {
        return enableForceUseAgreementAgency;
    }

    public void setEnableForceUseAgreementAgency(Boolean enableForceUseAgreementAgency) {
        this.enableForceUseAgreementAgency = enableForceUseAgreementAgency;
    }
}
