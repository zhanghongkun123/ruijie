package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbIpLimitDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbTransparentEncryptDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbUsbBandwidthDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.UsbStorageDeviceAccelerationEnum;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskTopAllowLoginTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.CdRomMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.NetDiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy.EditWatermarkWebRequest;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月20日
 *
 * @author wjp
 */
public class CreateVDIDeskStrategyWebRequest extends AbstractDeskStrategyWebRequest implements WebRequest {

    @ApiModelProperty(value = "云桌面策略名称", required = true)
    @NotBlank
    @TextShort
    private String strategyName;

    @ApiModelProperty(value = "云桌面类型", required = true)
    @NotNull
    private CbbCloudDeskPattern desktopType;

    @ApiModelProperty(value = "会话类型", required = true)
    @NotNull
    private CbbDesktopSessionType sessionType;

    @ApiModelProperty(value = "剪切板", required = true)
    @Nullable
    private CbbClipboardMode clipBoardMode;

    @ApiModelProperty(value = "支持传输的类型")
    @Nullable
    @Size(min = 1, max = 2)
    private CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr;

    @ApiModelProperty(value = "计算机名前缀", required = false)
    @NotBlank
    private String computerName;

    @ApiModelProperty(value = "启用扩展屏", required = true)
    @NotNull
    private Boolean enableDoubleScreen;

    @ApiModelProperty(value = "启用防截屏", required = true)
    @Nullable
    private Boolean enableForbidCatchScreen;

    @ApiModelProperty(value = "是否开启网页客户端接入", required = true)
    @Nullable
    private Boolean enableWebClient;

    @ApiModelProperty(value = "USB存储设备只读", required = true)
    @NotNull
    private Boolean enableUsbReadOnly;

    @ApiModelProperty(value = "启用云桌面重定向", required = true)
    @Nullable
    private Boolean enableOpenDesktopRedirect;

    @ApiModelProperty(value = "外设策略", required = true)
    @NotNull
    private UUID[] usbTypeIdArr;

    @ApiModelProperty(value = "共享打印开关", required = true)
    @Nullable
    private Boolean enableSharedPrinting;

    // 页面上没此信息，默认传true
    @ApiModelProperty(value = "允许访问外网", required = true)
    @NotNull
    private Boolean enableInternet;

    @ApiModelProperty(value = "允许嵌套虚拟化", required = false)
    @Nullable
    private Boolean enableNested;

    @ApiModelProperty(value = "云桌面标签")
    @Nullable
    @TextName
    @TextShort
    private String remark;

    @ApiModelProperty(value = "AD域加入OU路径")
    @Nullable
    private String adOu;

    @ApiModelProperty(value = "协议代理")
    @Nullable
    private Boolean enableAgreementAgency;

    @ApiModelProperty(value = "强制使用协议代理")
    @Nullable
    private Boolean enableForceUseAgreementAgency;

    @ApiModelProperty("磁盘映射枚举，关闭、可读、读写")
    @NotNull
    private DiskMappingEnum diskMappingType;

    @ApiModelProperty("是否开启局域网自动检测")
    @NotNull
    private Boolean enableLanAutoDetection;

    @ApiModelProperty("云桌面创建方式，链接克隆、完整克隆;默认为链接克隆")
    @Nullable
    private DeskCreateMode deskCreateMode;

    /**
     * 电源计划
     */
    @ApiModelProperty("电源计划，关机或者休眠")
    @Nullable
    private CbbPowerPlanEnum powerPlan;

    /**
     * 多久没操作后执行电源计划
     */
    @ApiModelProperty("多久没操作后执行电源计划，单位分钟")
    @Nullable
    @Range(min = "0", max = "259200")
    private Integer powerPlanTime;

    /**
     * est连接空闲多久自动断开
     */
    @ApiModelProperty("闲置VDI桌面超时断开，单位分钟")
    @Nullable
    @Range(min = "0", max = "240")
    private Integer estIdleOverTime;

    /**
     * 键盘模拟类型
     */
    @ApiModelProperty("键盘模拟类型")
    @Nullable
    private CbbKeyboardEmulationType keyboardEmulationType;

    @ApiModelProperty("网盘映射枚举，关闭、可读、读写")
    @Nullable
    private NetDiskMappingEnum netDiskMappingType;

    @ApiModelProperty("CDROM映射枚举，关闭、可读")
    @Nullable
    private CdRomMappingEnum cdRomMappingType;

    @ApiModelProperty("是否开起桌面登录时间限制")
    @Nullable
    private Boolean enableOpenLoginLimit;

    @ApiModelProperty("云桌面允许登录时间")
    @Nullable
    private DeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr;

    @ApiModelProperty("启用用户自助快照")
    @Nullable
    private Boolean enableUserSnapshot;

    @ApiModelProperty("IP访问规则，不启用、允许、拒绝")
    @Nullable
    private CbbIpLimitModeEnum ipLimitMode;

    @ApiModelProperty("IP网段")
    @Nullable
    @Size(min = 1, max = 50)
    private List<CbbIpLimitDTO> ipSegmentDTOList;

    /**
     * 水印配置内容
     */
    @ApiModelProperty("水印配置内容")
    @Nullable
    private EditWatermarkWebRequest watermarkInfo;

    /**
     * 是否开启文件审计
     */
    @ApiModelProperty("是否开启文件审计")
    @Nullable
    private Boolean enableAuditFile = false;

    /**
     * 文件审计配置内容
     */
    @ApiModelProperty("文件审计配置内容")
    @Nullable
    private EditAuditFileWebRequest auditFileInfo;

    /**
     * 是否开启打印审计
     */
    @ApiModelProperty("是否开启打印审计")
    @Nullable
    private Boolean enableAuditPrinter = false;

    /**
     * 打印审计配置内容
     */
    @ApiModelProperty("打印审计配置内容")
    @Nullable
    private EditAuditPrinterWebRequest auditPrinterInfo;

    /**
     * Windows账号权限（多会话策略配置）
     */
    @ApiModelProperty("Windows账号权限")
    @Nullable
    private CbbSyncLoginAccountPermissionEnums windowsAccountPermission;

    /**
     * 桌面登录账号同步
     */
    @ApiModelProperty("桌面登录账号同步")
    @Nullable
    private Boolean desktopSyncLoginAccount;

    /**
     * 桌面登录密码同步
     */
    @ApiModelProperty("桌面登录密码同步")
    @Nullable
    private Boolean desktopSyncLoginPassword;

    /**
     * 桌面登录账号权限
     */
    @ApiModelProperty("桌面登录账号权限")
    @Nullable
    private CbbSyncLoginAccountPermissionEnums desktopSyncLoginAccountPermission;

    @ApiModelProperty("启用/禁用高可用特性")
    @Nullable
    private Boolean enableHa;

    @ApiModelProperty("配置HA优先级")
    @Nullable
    @Range(min = "0", max = "10")
    private Integer haPriority;

    /**
     * Usb存储设备映射
     */
    @ApiModelProperty("Usb存储设备映射")
    @NotNull
    private CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode;

    @ApiModelProperty("Usb存储设备加速")
    @Nullable
    private UsbStorageDeviceAccelerationEnum usbStorageDeviceAcceleration = UsbStorageDeviceAccelerationEnum.NO_ACCELERATION;

    @ApiModelProperty("est协议选择仅VDI策略可配置")
    @Nullable
    private CbbEstProtocolType estProtocolType;

    @ApiModelProperty("协议配置")
    @Nullable
    private AgreementDTO agreementInfo;

    @ApiModelProperty("启用串口重定向")
    @Nullable
    private Boolean enableSerialPortRedirect;

    @ApiModelProperty("启用并口重定向")
    @Nullable
    private Boolean enableParallelPortRedirect;

    @ApiModelProperty("透明加解密总开关")
    @Nullable
    private Boolean enableTransparentEncrypt;

    @ApiModelProperty("透明加解密配置")
    @Nullable
    private CbbTransparentEncryptDTO transparentEncryptInfo;

    @ApiModelProperty("usb压缩加速")
    @Nullable
    private Boolean enableUsbCompressAcceleration;

    @ApiModelProperty("usb带宽控制")
    @Nullable
    private Boolean enableUsbBandwidth;

    @ApiModelProperty("usb带宽控制配置")
    @Nullable
    private CbbUsbBandwidthDTO usbBandwidthInfo;


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

    public CbbClipboardMode getClipBoardMode() {
        return clipBoardMode;
    }

    public void setClipBoardMode(CbbClipboardMode clipBoardMode) {
        this.clipBoardMode = clipBoardMode;
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

    public Boolean getEnableForbidCatchScreen() {
        return enableForbidCatchScreen;
    }

    public void setEnableForbidCatchScreen(Boolean enableForbidCatchScreen) {
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

    @Nullable
    public Boolean getEnableSharedPrinting() {
        return enableSharedPrinting;
    }

    public void setEnableSharedPrinting(@Nullable Boolean enableSharedPrinting) {
        this.enableSharedPrinting = enableSharedPrinting;
    }

    public void setUsbTypeIdArr(UUID[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
    }

    public Boolean getEnableInternet() {
        return enableInternet;
    }

    public void setEnableInternet(Boolean enableInternet) {
        this.enableInternet = enableInternet;
    }

    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(Boolean enableNested) {
        this.enableNested = enableNested;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
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

    public Boolean getEnableOpenLoginLimit() {
        return enableOpenLoginLimit;
    }

    public void setEnableOpenLoginLimit(Boolean enableOpenLoginLimit) {
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
    public CbbClipBoardSupportTypeDTO[] getClipBoardSupportTypeArr() {
        return clipBoardSupportTypeArr;
    }

    public void setClipBoardSupportTypeArr(@Nullable CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr) {
        this.clipBoardSupportTypeArr = clipBoardSupportTypeArr;
    }

    @Nullable
    public EditWatermarkWebRequest getWatermarkInfo() {
        return watermarkInfo;
    }

    public void setWatermarkInfo(@Nullable EditWatermarkWebRequest watermarkInfo) {
        this.watermarkInfo = watermarkInfo;
    }

    @Nullable
    public Boolean getEnableAuditFile() {
        return enableAuditFile;
    }

    public void setEnableAuditFile(@Nullable Boolean enableAuditFile) {
        this.enableAuditFile = enableAuditFile;
    }

    @Nullable
    public EditAuditFileWebRequest getAuditFileInfo() {
        return auditFileInfo;
    }

    public void setAuditFileInfo(@Nullable EditAuditFileWebRequest auditFileInfo) {
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
    public EditAuditPrinterWebRequest getAuditPrinterInfo() {
        return auditPrinterInfo;
    }

    public void setAuditPrinterInfo(@Nullable EditAuditPrinterWebRequest auditPrinterInfo) {
        this.auditPrinterInfo = auditPrinterInfo;
    }

    @Nullable
    public CbbSyncLoginAccountPermissionEnums getWindowsAccountPermission() {
        return windowsAccountPermission;
    }

    public void setWindowsAccountPermission(@Nullable CbbSyncLoginAccountPermissionEnums windowsAccountPermission) {
        this.windowsAccountPermission = windowsAccountPermission;
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
    public Boolean getEnableUserSnapshot() {
        return enableUserSnapshot;
    }

    public void setEnableUserSnapshot(@Nullable Boolean enableUserSnapshot) {
        this.enableUserSnapshot = enableUserSnapshot;
    }

    @Nullable
    public Integer getEstIdleOverTime() {
        return estIdleOverTime;
    }

    public void setEstIdleOverTime(@Nullable Integer estIdleOverTime) {
        this.estIdleOverTime = estIdleOverTime;
    }

    public UsbStorageDeviceAccelerationEnum getUsbStorageDeviceAcceleration() {
        return usbStorageDeviceAcceleration;
    }

    public void setUsbStorageDeviceAcceleration(UsbStorageDeviceAccelerationEnum usbStorageDeviceAcceleration) {
        this.usbStorageDeviceAcceleration = usbStorageDeviceAcceleration;
    }

    @Nullable
    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(@Nullable CbbEstProtocolType estProtocolType) {
        this.estProtocolType = estProtocolType;
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
    public Boolean getEnableForceUseAgreementAgency() {
        return enableForceUseAgreementAgency;
    }

    public void setEnableForceUseAgreementAgency(@Nullable Boolean enableForceUseAgreementAgency) {
        this.enableForceUseAgreementAgency = enableForceUseAgreementAgency;
    }
}
