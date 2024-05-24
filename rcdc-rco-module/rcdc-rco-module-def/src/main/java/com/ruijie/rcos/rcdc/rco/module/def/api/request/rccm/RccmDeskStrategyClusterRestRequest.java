package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
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
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 云桌面策略数据给RCCM
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/17
 *
 * @author TD
 */
public class RccmDeskStrategyClusterRestRequest {


    /**
     * 云桌面策略ID
     */
    @Nullable
    private UUID id;

    /**
     * 云桌面策略名称
     */
    @NotBlank
    @TextShort
    private String strategyName;

    /**
     * 策略类型
     */
    @NotNull
    private CbbStrategyType strategyType;

    /**
     * 云桌面类型
     */
    @Nullable
    private CbbCloudDeskPattern desktopType;

    /**
     * 会话类型
     */
    @NotNull
    private CbbDesktopSessionType sessionType;

    /**
     * 系统盘（GB）
     */
    @Nullable
    @Range(min = "20", max = "2048")
    private Integer systemDisk;

    /**
     * 剪切板
     */
    @Nullable
    private CbbClipboardMode clipBoardMode;

    /**
     * 支持传输的类型
     */
    @Nullable
    @Size(min = 1, max = 2)
    private CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr;

    /**
     * 计算机名前缀
     */
    @NotNull
    private String computerName;

    /**
     * 启用扩展屏
     */
    @NotNull
    private Boolean enableDoubleScreen;

    /**
     * 启用防截屏
     */
    @Nullable
    private Boolean enableForbidCatchScreen;

    /**
     * 是否开启网页客户端接入
     */
    @Nullable
    private Boolean enableWebClient;

    /**
     * USB存储设备只读
     */
    @NotNull
    private Boolean enableUsbReadOnly;

    /**
     * 启用云桌面重定向
     */
    @Nullable
    private Boolean enableOpenDesktopRedirect;

    /**
     * 外设策略
     */
    @NotNull
    private UUID[] usbTypeIdArr;

    /**
     * 外设策略名称集合,同步给从集群时,根据名称进行关联
     */
    @NotNull
    private List<String> usbTypeNameList;

    /**
     * 允许访问外网
     */
    @NotNull
    private Boolean enableInternet = true;

    /**
     * 允许嵌套虚拟化
     */
    @Nullable
    private Boolean enableNested;


    /**
     * 云桌面标签
     */
    @Nullable
    @TextName
    @TextShort
    private String remark;

    /**
     * AD域加入OU路径
     */
    @Nullable
    private String adOu;

    /**
     * 协议代理
     */
    @Nullable
    private Boolean enableAgreementAgency;

    /**
     * 强制使用协议代理
     */
    @Nullable
    private Boolean enableForceUseAgreementAgency;

    /**
     * 磁盘映射枚举，关闭、可读、读写
     */
    @Nullable
    private DiskMappingEnum diskMappingType;

    /**
     * 是否开启局域网自动检测
     */
    @Nullable
    private Boolean enableLanAutoDetection;

    /**
     * 云桌面创建方式，链接克隆、完整克隆;默认为链接克隆
     */
    @Nullable
    private DeskCreateMode deskCreateMode = DeskCreateMode.LINK_CLONE;

    /**
     * 电源计划
     */
    @Nullable
    private CbbPowerPlanEnum powerPlan;

    /**
     * 多久没操作后执行电源计划
     */
    @Nullable
    @Range(min = "0", max = "259200")
    private Integer powerPlanTime;

    /**
     * est连接空闲超时时间
     */
    @Nullable
    @Range(min = "0")
    private Integer estIdleOverTime;

    /**
     * 键盘模拟类型
     */
    @Nullable
    private CbbKeyboardEmulationType keyboardEmulationType;

    /**
     * 网盘映射枚举，关闭、可读、读写
     */
    @Nullable
    private NetDiskMappingEnum netDiskMappingType;

    /**
     * CDROM映射枚举，关闭、可读
     */
    @Nullable
    private CdRomMappingEnum cdRomMappingType;

    /**
     * 是否开起桌面登录时间限制
     */
    @Nullable
    private Boolean enableOpenLoginLimit;

    /**
     * 云桌面允许登录时间
     */
    @Nullable
    private DeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr;

    @Nullable
    private Boolean enableUserSnapshot;

    /**
     * IP访问规则，不启用、允许、拒绝
     */
    @Nullable
    private CbbIpLimitModeEnum ipLimitMode;

    /**
     * IP网段
     */
    @Nullable
    @Size(min = 1, max = 50)
    private List<CbbIpLimitDTO> ipSegmentDTOList;

    /**
     * 是否开启水印
     */
    @Nullable
    private Boolean enableWatermark = false;

    /**
     * 水印配置内容
     */
    @Nullable
    private CbbWatermarkConfigDTO watermarkInfo;

    /**
     * 盘符预占配置
     */
    @Nullable
    private String[] desktopOccupyDriveArr;

    /**
     * 启用本地盘
     */
    @Nullable
    private Boolean enableAllowLocalDisk;

    /**
     * 系统盘自动扩容
     */
    @Nullable
    private Boolean enableFullSystemDisk;

    /**
     * 统一管理ID：RCCM场景下同步云桌面策略使用
     */
    @NotNull
    private UUID unifiedManageDataId;

    /**
     * 描述
     */
    @Nullable
    private String note;

    /**
     * 是否开启文件审计
     */
    @Nullable
    private Boolean enableAuditFile;

    /**
     * 文件审计配置内容
     */
    @Nullable
    private CbbAuditFileConfigDTO auditFileInfo;

    /**
     * 是否开启打印审计
     */
    @Nullable
    private Boolean enableAuditPrinter;

    /**
     * 打印审计配置内容
     */
    @Nullable
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
     * Usb存储设备加速
     */
    @Nullable
    private UsbStorageDeviceAccelerationEnum usbStorageDeviceAcceleration;

    /**
     * 是否开启Usb存储设备映射
     */
    @Nullable
    private CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode;

    /**
     * 是否启用串口重定向
     */
    @Nullable
    private Boolean enableSerialPortRedirect;

    /**
     * 是否启用并口重定向
     */
    @Nullable
    private Boolean enableParallelPortRedirect;

    /**
     * usb压缩加速
     */
    @Nullable
    private Boolean enableUsbCompressAcceleration;

    /**
     * usb带宽控制
     */
    @Nullable
    private Boolean enableUsbBandwidth;

    /**
     * usb带宽控制配置
     */
    @Nullable
    private CbbUsbBandwidthDTO usbBandwidthInfo;

    @Nullable
    private CbbEstProtocolType estProtocolType;

    @Nullable
    private AgreementDTO agreementInfo;

    @Nullable
    private Boolean enableTransparentEncrypt;

    @Nullable
    private CbbTransparentEncryptDTO transparentEncryptInfo;

    /**
     * 共享打印开关
     */
    @Nullable
    private Boolean enableSharedPrinting;

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public CbbStrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(CbbStrategyType strategyType) {
        this.strategyType = strategyType;
    }

    @Nullable
    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(@Nullable CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    @Nullable
    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(@Nullable Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public CbbClipboardMode getClipBoardMode() {
        return clipBoardMode;
    }

    public void setClipBoardMode(CbbClipboardMode clipBoardMode) {
        this.clipBoardMode = clipBoardMode;
    }

    @Nullable
    public CbbClipBoardSupportTypeDTO[] getClipBoardSupportTypeArr() {
        return clipBoardSupportTypeArr;
    }

    public void setClipBoardSupportTypeArr(@Nullable CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr) {
        this.clipBoardSupportTypeArr = clipBoardSupportTypeArr;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public Boolean getEnableDoubleScreen() {
        return enableDoubleScreen;
    }

    public void setEnableDoubleScreen(Boolean enableDoubleScreen) {
        this.enableDoubleScreen = enableDoubleScreen;
    }

    @Nullable
    public Boolean getEnableForbidCatchScreen() {
        return enableForbidCatchScreen;
    }

    public void setEnableForbidCatchScreen(@Nullable Boolean enableForbidCatchScreen) {
        this.enableForbidCatchScreen = enableForbidCatchScreen;
    }

    @Nullable
    public Boolean getEnableWebClient() {
        return enableWebClient;
    }

    public void setEnableWebClient(@Nullable Boolean enableWebClient) {
        this.enableWebClient = enableWebClient;
    }

    public Boolean getEnableUsbReadOnly() {
        return enableUsbReadOnly;
    }

    public void setEnableUsbReadOnly(Boolean enableUsbReadOnly) {
        this.enableUsbReadOnly = enableUsbReadOnly;
    }

    @Nullable
    public Boolean getEnableOpenDesktopRedirect() {
        return enableOpenDesktopRedirect;
    }

    public void setEnableOpenDesktopRedirect(@Nullable Boolean enableOpenDesktopRedirect) {
        this.enableOpenDesktopRedirect = enableOpenDesktopRedirect;
    }

    public UUID[] getUsbTypeIdArr() {
        return usbTypeIdArr;
    }

    public void setUsbTypeIdArr(UUID[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
    }

    public List<String> getUsbTypeNameList() {
        return usbTypeNameList;
    }

    public void setUsbTypeNameList(List<String> usbTypeNameList) {
        this.usbTypeNameList = usbTypeNameList;
    }

    public Boolean getEnableInternet() {
        return enableInternet;
    }

    public void setEnableInternet(Boolean enableInternet) {
        this.enableInternet = enableInternet;
    }

    @Nullable
    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(@Nullable Boolean enableNested) {
        this.enableNested = enableNested;
    }

    @Nullable
    public String getRemark() {
        return remark;
    }

    public void setRemark(@Nullable String remark) {
        this.remark = remark;
    }

    @Nullable
    public String getAdOu() {
        return adOu;
    }

    public void setAdOu(@Nullable String adOu) {
        this.adOu = adOu;
    }

    @Nullable
    public Boolean getEnableAgreementAgency() {
        return enableAgreementAgency;
    }

    public void setEnableAgreementAgency(@Nullable Boolean enableAgreementAgency) {
        this.enableAgreementAgency = enableAgreementAgency;
    }

    @Nullable
    public DiskMappingEnum getDiskMappingType() {
        return diskMappingType;
    }

    public void setDiskMappingType(@Nullable DiskMappingEnum diskMappingType) {
        this.diskMappingType = diskMappingType;
    }

    @Nullable
    public Boolean getEnableLanAutoDetection() {
        return enableLanAutoDetection;
    }

    public void setEnableLanAutoDetection(@Nullable Boolean enableLanAutoDetection) {
        this.enableLanAutoDetection = enableLanAutoDetection;
    }

    @Nullable
    public DeskCreateMode getDeskCreateMode() {
        return deskCreateMode;
    }

    public void setDeskCreateMode(@Nullable DeskCreateMode deskCreateMode) {
        this.deskCreateMode = deskCreateMode;
    }

    @Nullable
    public CbbPowerPlanEnum getPowerPlan() {
        return powerPlan;
    }

    public void setPowerPlan(@Nullable CbbPowerPlanEnum powerPlan) {
        this.powerPlan = powerPlan;
    }

    @Nullable
    public Integer getPowerPlanTime() {
        return powerPlanTime;
    }

    public void setPowerPlanTime(@Nullable Integer powerPlanTime) {
        this.powerPlanTime = powerPlanTime;
    }

    @Nullable
    public Integer getEstIdleOverTime() {
        return estIdleOverTime;
    }

    public void setEstIdleOverTime(@Nullable Integer estIdleOverTime) {
        this.estIdleOverTime = estIdleOverTime;
    }

    @Nullable
    public CbbKeyboardEmulationType getKeyboardEmulationType() {
        return keyboardEmulationType;
    }

    public void setKeyboardEmulationType(@Nullable CbbKeyboardEmulationType keyboardEmulationType) {
        this.keyboardEmulationType = keyboardEmulationType;
    }

    @Nullable
    public NetDiskMappingEnum getNetDiskMappingType() {
        return netDiskMappingType;
    }

    public void setNetDiskMappingType(@Nullable NetDiskMappingEnum netDiskMappingType) {
        this.netDiskMappingType = netDiskMappingType;
    }

    @Nullable
    public CdRomMappingEnum getCdRomMappingType() {
        return cdRomMappingType;
    }

    public void setCdRomMappingType(@Nullable CdRomMappingEnum cdRomMappingType) {
        this.cdRomMappingType = cdRomMappingType;
    }

    @Nullable
    public Boolean getEnableOpenLoginLimit() {
        return enableOpenLoginLimit;
    }

    public void setEnableOpenLoginLimit(@Nullable Boolean enableOpenLoginLimit) {
        this.enableOpenLoginLimit = enableOpenLoginLimit;
    }

    @Nullable
    public DeskTopAllowLoginTimeDTO[] getDesktopAllowLoginTimeArr() {
        return desktopAllowLoginTimeArr;
    }

    public void setDesktopAllowLoginTimeArr(@Nullable DeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr) {
        this.desktopAllowLoginTimeArr = desktopAllowLoginTimeArr;
    }

    @Nullable
    public Boolean getEnableUserSnapshot() {
        return enableUserSnapshot;
    }

    public void setEnableUserSnapshot(@Nullable Boolean enableUserSnapshot) {
        this.enableUserSnapshot = enableUserSnapshot;
    }

    @Nullable
    public CbbIpLimitModeEnum getIpLimitMode() {
        return ipLimitMode;
    }

    public void setIpLimitMode(@Nullable CbbIpLimitModeEnum ipLimitMode) {
        this.ipLimitMode = ipLimitMode;
    }

    @Nullable
    public List<CbbIpLimitDTO> getIpSegmentDTOList() {
        return ipSegmentDTOList;
    }

    public void setIpSegmentDTOList(@Nullable List<CbbIpLimitDTO> ipSegmentDTOList) {
        this.ipSegmentDTOList = ipSegmentDTOList;
    }

    @Nullable
    public Boolean getEnableWatermark() {
        return enableWatermark;
    }

    public void setEnableWatermark(@Nullable Boolean enableWatermark) {
        this.enableWatermark = enableWatermark;
    }

    @Nullable
    public CbbWatermarkConfigDTO getWatermarkInfo() {
        return watermarkInfo;
    }

    public void setWatermarkInfo(@Nullable CbbWatermarkConfigDTO watermarkInfo) {
        this.watermarkInfo = watermarkInfo;
    }

    @Nullable
    public String[] getDesktopOccupyDriveArr() {
        return desktopOccupyDriveArr;
    }

    public void setDesktopOccupyDriveArr(@Nullable String[] desktopOccupyDriveArr) {
        this.desktopOccupyDriveArr = desktopOccupyDriveArr;
    }

    @Nullable
    public Boolean getEnableAllowLocalDisk() {
        return enableAllowLocalDisk;
    }

    public void setEnableAllowLocalDisk(@Nullable Boolean enableAllowLocalDisk) {
        this.enableAllowLocalDisk = enableAllowLocalDisk;
    }

    @Nullable
    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(@Nullable Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public UUID getUnifiedManageDataId() {
        return unifiedManageDataId;
    }

    public void setUnifiedManageDataId(UUID unifiedManageDataId) {
        this.unifiedManageDataId = unifiedManageDataId;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }

    @Nullable
    public Boolean getEnableAuditFile() {
        return enableAuditFile;
    }

    public void setEnableAuditFile(@Nullable Boolean enableAuditFile) {
        this.enableAuditFile = enableAuditFile;
    }

    @Nullable
    public CbbAuditFileConfigDTO getAuditFileInfo() {
        return auditFileInfo;
    }

    public void setAuditFileInfo(@Nullable CbbAuditFileConfigDTO auditFileInfo) {
        this.auditFileInfo = auditFileInfo;
    }

    @Nullable
    public Boolean getEnableAuditPrinter() {
        return enableAuditPrinter;
    }

    public void setEnableAuditPrinter(@Nullable Boolean enableAuditPrinter) {
        this.enableAuditPrinter = enableAuditPrinter;
    }

    @Nullable
    public CbbAuditPrinterConfigDTO getAuditPrinterInfo() {
        return auditPrinterInfo;
    }

    public void setAuditPrinterInfo(@Nullable CbbAuditPrinterConfigDTO auditPrinterInfo) {
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

    @Nullable
    public CbbUsbStorageDeviceMappingMode getUsbStorageDeviceMappingMode() {
        return usbStorageDeviceMappingMode;
    }

    public void setUsbStorageDeviceMappingMode(@Nullable CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode) {
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
    public Boolean getEnableSerialPortRedirect() {
        return enableSerialPortRedirect;
    }

    public void setEnableSerialPortRedirect(@Nullable Boolean enableSerialPortRedirect) {
        this.enableSerialPortRedirect = enableSerialPortRedirect;
    }

    @Nullable
    public Boolean getEnableParallelPortRedirect() {
        return enableParallelPortRedirect;
    }

    public void setEnableParallelPortRedirect(@Nullable Boolean enableParallelPortRedirect) {
        this.enableParallelPortRedirect = enableParallelPortRedirect;
    }

    @Nullable
    public Boolean getEnableUsbCompressAcceleration() {
        return enableUsbCompressAcceleration;
    }

    public void setEnableUsbCompressAcceleration(@Nullable Boolean enableUsbCompressAcceleration) {
        this.enableUsbCompressAcceleration = enableUsbCompressAcceleration;
    }

    @Nullable
    public Boolean getEnableUsbBandwidth() {
        return enableUsbBandwidth;
    }

    public void setEnableUsbBandwidth(@Nullable Boolean enableUsbBandwidth) {
        this.enableUsbBandwidth = enableUsbBandwidth;
    }

    @Nullable
    public CbbUsbBandwidthDTO getUsbBandwidthInfo() {
        return usbBandwidthInfo;
    }

    public void setUsbBandwidthInfo(@Nullable CbbUsbBandwidthDTO usbBandwidthInfo) {
        this.usbBandwidthInfo = usbBandwidthInfo;
    }

    @Nullable
    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(@Nullable CbbEstProtocolType estProtocolType) {
        this.estProtocolType = estProtocolType;
    }

    @Nullable
    public AgreementDTO getAgreementInfo() {
        return agreementInfo;
    }

    public void setAgreementInfo(@Nullable AgreementDTO agreementInfo) {
        this.agreementInfo = agreementInfo;
    }

    @Nullable
    public Boolean getEnableTransparentEncrypt() {
        return enableTransparentEncrypt;
    }

    public void setEnableTransparentEncrypt(@Nullable Boolean enableTransparentEncrypt) {
        this.enableTransparentEncrypt = enableTransparentEncrypt;
    }

    @Nullable
    public CbbTransparentEncryptDTO getTransparentEncryptInfo() {
        return transparentEncryptInfo;
    }

    public void setTransparentEncryptInfo(@Nullable CbbTransparentEncryptDTO transparentEncryptInfo) {
        this.transparentEncryptInfo = transparentEncryptInfo;
    }

    public Boolean getEnableForceUseAgreementAgency() {
        return enableForceUseAgreementAgency;
    }

    public void setEnableForceUseAgreementAgency(Boolean enableForceUseAgreementAgency) {
        this.enableForceUseAgreementAgency = enableForceUseAgreementAgency;
    }

    @Nullable
    public Boolean getEnableSharedPrinting() {
        return enableSharedPrinting;
    }

    public void setEnableSharedPrinting(@Nullable Boolean enableSharedPrinting) {
        this.enableSharedPrinting = enableSharedPrinting;
    }
}
