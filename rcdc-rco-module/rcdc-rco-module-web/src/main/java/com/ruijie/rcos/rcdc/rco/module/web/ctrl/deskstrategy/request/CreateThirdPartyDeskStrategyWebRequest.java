package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbIpLimitDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbUsbBandwidthDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbClipboardMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbIpLimitModeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbPowerPlanEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbSyncLoginAccountPermissionEnums;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUsbStorageDeviceMappingMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.UsbStorageDeviceAccelerationEnum;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskTopAllowLoginTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.CdRomMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.NetDiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy.EditWatermarkWebRequest;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;


/**
 * Description: 创建第三方云桌面策略请求参数
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月17日
 *
 * @author wangjie9
 */
public class CreateThirdPartyDeskStrategyWebRequest extends AbstractDeskStrategyWebRequest implements WebRequest {

    @ApiModelProperty(value = "云桌面策略名称", required = true)
    @NotBlank
    @TextShort
    private String strategyName;

    @ApiModelProperty(value = "会话类型", required = true)
    @NotNull
    private CbbDesktopSessionType sessionType;

    @ApiModelProperty(value = "云桌面标签")
    @Nullable
    @TextName
    @TextShort
    private String remark;

    @ApiModelProperty(value = "USB存储设备只读", required = true)
    @NotNull
    private Boolean enableUsbReadOnly;

    @ApiModelProperty(value = "外设策略", required = true)
    @NotNull
    private UUID[] usbTypeIdArr;

    @ApiModelProperty(value = "共享打印开关")
    @Nullable
    private Boolean enableSharedPrinting;

    @ApiModelProperty(value = "剪切板", required = true)
    @Nullable
    private CbbClipboardMode clipBoardMode;

    @ApiModelProperty(value = "文件和剪切板配置")
    @Nullable
    @Size(min = 1, max = 2)
    private CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr;

    @ApiModelProperty("磁盘映射枚举，关闭、可读、读写")
    @NotNull
    private DiskMappingEnum diskMappingType;

    @ApiModelProperty("是否开启局域网自动检测")
    @Nullable
    private Boolean enableLanAutoDetection;

    @ApiModelProperty("网盘映射枚举，关闭、可读、读写")
    @Nullable
    private NetDiskMappingEnum netDiskMappingType;

    @ApiModelProperty("CDROM映射枚举，关闭、可读")
    @Nullable
    private CdRomMappingEnum cdRomMappingType;

    @ApiModelProperty("Usb存储设备映射")
    @NotNull
    private CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode;

    @ApiModelProperty(value = "启用防截屏", required = true)
    @Nullable
    private Boolean enableForbidCatchScreen;

    @ApiModelProperty("电源计划，关机或者休眠")
    @Nullable
    private CbbPowerPlanEnum powerPlan;

    @ApiModelProperty("多久没操作后执行电源计划，单位分钟")
    @Nullable
    @Range(min = "0", max = "259200")
    private Integer powerPlanTime;

    @ApiModelProperty("闲置会话超时断开，单位分钟")
    @Nullable
    @Range(min = "0", max = "240")
    private Integer estIdleOverTime;

    @ApiModelProperty(value = "启用扩展屏", required = true)
    @NotNull
    private Boolean enableDoubleScreen;

    @ApiModelProperty("水印配置内容")
    @Nullable
    private EditWatermarkWebRequest watermarkInfo;

    @ApiModelProperty(value = "协议代理")
    @Nullable
    private Boolean enableAgreementAgency;

    @ApiModelProperty(value = "强制使用协议代理")
    @Nullable
    private Boolean enableForceUseAgreementAgency;

    @ApiModelProperty("是否开启桌面登录时间限制")
    @Nullable
    private Boolean enableOpenLoginLimit;

    @ApiModelProperty("云桌面允许登录时间")
    @Nullable
    private DeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr;

    @ApiModelProperty("IP访问规则，不启用、允许、拒绝")
    @Nullable
    private CbbIpLimitModeEnum ipLimitMode;

    @ApiModelProperty("IP网段")
    @Nullable
    @Size(min = 1, max = 50)
    private List<CbbIpLimitDTO> ipSegmentDTOList;

    @ApiModelProperty("是否开启文件审计")
    @Nullable
    private Boolean enableAuditFile = false;

    @ApiModelProperty("文件审计配置内容")
    @Nullable
    private EditAuditFileWebRequest auditFileInfo;

    @ApiModelProperty("协议配置-连接协议")
    @NotNull
    private CbbEstProtocolType estProtocolType;

    @ApiModelProperty("协议配置")
    @Nullable
    private AgreementDTO agreementInfo;

    /**
     * Windows账号权限（多会话策略配置）
     */
    @ApiModelProperty("Windows账号权限")
    @Nullable
    private CbbSyncLoginAccountPermissionEnums windowsAccountPermission;

    /**
     * Usb存储设备加速
     */
    @ApiModelProperty("Usb存储设备加速")
    @Nullable
    private UsbStorageDeviceAccelerationEnum usbStorageDeviceAcceleration = UsbStorageDeviceAccelerationEnum.NO_ACCELERATION;

    /**
     * usb压缩加速
     */
    @ApiModelProperty("usb压缩加速")
    @Nullable
    private Boolean enableUsbCompressAcceleration;

    /**
     * usb带宽控制
     */
    @ApiModelProperty("usb带宽控制")
    @Nullable
    private Boolean enableUsbBandwidth;

    /**
     * usb带宽控制配置
     */
    @ApiModelProperty("usb带宽控制配置")
    @Nullable
    private CbbUsbBandwidthDTO usbBandwidthInfo;

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    @Nullable
    public String getRemark() {
        return remark;
    }

    public void setRemark(@Nullable String remark) {
        this.remark = remark;
    }

    public Boolean getEnableUsbReadOnly() {
        return enableUsbReadOnly;
    }

    public void setEnableUsbReadOnly(Boolean enableUsbReadOnly) {
        this.enableUsbReadOnly = enableUsbReadOnly;
    }

    public UUID[] getUsbTypeIdArr() {
        return usbTypeIdArr;
    }

    public void setUsbTypeIdArr(UUID[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
    }

    @Nullable
    public Boolean getEnableSharedPrinting() {
        return enableSharedPrinting;
    }

    public void setEnableSharedPrinting(@Nullable Boolean enableSharedPrinting) {
        this.enableSharedPrinting = enableSharedPrinting;
    }

    @Nullable
    public CbbClipboardMode getClipBoardMode() {
        return clipBoardMode;
    }

    public void setClipBoardMode(@Nullable CbbClipboardMode clipBoardMode) {
        this.clipBoardMode = clipBoardMode;
    }

    @Nullable
    public CbbClipBoardSupportTypeDTO[] getClipBoardSupportTypeArr() {
        return clipBoardSupportTypeArr;
    }

    public void setClipBoardSupportTypeArr(@Nullable CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr) {
        this.clipBoardSupportTypeArr = clipBoardSupportTypeArr;
    }

    public DiskMappingEnum getDiskMappingType() {
        return diskMappingType;
    }

    public void setDiskMappingType(DiskMappingEnum diskMappingType) {
        this.diskMappingType = diskMappingType;
    }

    @Nullable
    public NetDiskMappingEnum getNetDiskMappingType() {
        return netDiskMappingType;
    }

    public void setNetDiskMappingType(@Nullable NetDiskMappingEnum netDiskMappingType) {
        this.netDiskMappingType = netDiskMappingType;
    }

    @Nullable
    public Boolean getEnableLanAutoDetection() {
        return enableLanAutoDetection;
    }

    public void setEnableLanAutoDetection(@Nullable Boolean enableLanAutoDetection) {
        this.enableLanAutoDetection = enableLanAutoDetection;
    }

    @Nullable
    public CdRomMappingEnum getCdRomMappingType() {
        return cdRomMappingType;
    }

    public void setCdRomMappingType(@Nullable CdRomMappingEnum cdRomMappingType) {
        this.cdRomMappingType = cdRomMappingType;
    }

    public CbbUsbStorageDeviceMappingMode getUsbStorageDeviceMappingMode() {
        return usbStorageDeviceMappingMode;
    }

    public void setUsbStorageDeviceMappingMode(CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode) {
        this.usbStorageDeviceMappingMode = usbStorageDeviceMappingMode;
    }

    @Nullable
    public Boolean getEnableForbidCatchScreen() {
        return enableForbidCatchScreen;
    }

    public void setEnableForbidCatchScreen(@Nullable Boolean enableForbidCatchScreen) {
        this.enableForbidCatchScreen = enableForbidCatchScreen;
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

    public Boolean getEnableDoubleScreen() {
        return enableDoubleScreen;
    }

    public void setEnableDoubleScreen(Boolean enableDoubleScreen) {
        this.enableDoubleScreen = enableDoubleScreen;
    }

    @Nullable
    public EditWatermarkWebRequest getWatermarkInfo() {
        return watermarkInfo;
    }

    public void setWatermarkInfo(@Nullable EditWatermarkWebRequest watermarkInfo) {
        this.watermarkInfo = watermarkInfo;
    }

    @Nullable
    public Boolean getEnableAgreementAgency() {
        return enableAgreementAgency;
    }

    public void setEnableAgreementAgency(@Nullable Boolean enableAgreementAgency) {
        this.enableAgreementAgency = enableAgreementAgency;
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

    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(CbbEstProtocolType estProtocolType) {
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
    public CbbSyncLoginAccountPermissionEnums getWindowsAccountPermission() {
        return windowsAccountPermission;
    }

    public void setWindowsAccountPermission(@Nullable CbbSyncLoginAccountPermissionEnums windowsAccountPermission) {
        this.windowsAccountPermission = windowsAccountPermission;
    }

    @Nullable
    public UsbStorageDeviceAccelerationEnum getUsbStorageDeviceAcceleration() {
        return usbStorageDeviceAcceleration;
    }

    public void setUsbStorageDeviceAcceleration(@Nullable UsbStorageDeviceAccelerationEnum usbStorageDeviceAcceleration) {
        this.usbStorageDeviceAcceleration = usbStorageDeviceAcceleration;
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
