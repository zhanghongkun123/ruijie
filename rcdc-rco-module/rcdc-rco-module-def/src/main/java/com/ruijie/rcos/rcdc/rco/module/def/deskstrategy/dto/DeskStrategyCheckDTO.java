package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditFileConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditPrinterConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbTransparentEncryptDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbUsbBandwidthDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbClipboardMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskTopAllowLoginTimeDTO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 云桌面校验DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/19
 *
 * @author TD
 */
public class DeskStrategyCheckDTO {

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
    @TextName
    private String strategyName;

    /**
     * 策略类型
     */
    @Nullable
    private CbbCloudDeskPattern desktopType;

    /**
     * 云桌面策略类型
     */
    @Nullable
    private CbbStrategyType strategyType;

    /**
     * 会话类型
     */
    @Nullable
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
    @Nullable
    private String computerName;

    /**
     * 启用云桌面重定向：默认开启
     */
    @Nullable
    private Boolean enableOpenDesktopRedirect;

    /**
     * AD域加入OU路径
     */
    @Nullable
    private String adOu;

    /**
     * 云桌面允许登录时间
     */
    @Nullable
    private DeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr;

    /**
     * 用户自助快照最大数量
     */
    @Nullable
    private Integer userMaxSnapshotNum;

    /**
     * 是否开启水印
     */
    @Nullable
    private Boolean enableWatermark;

    /**
     * 水印配置内容
     */
    @Nullable
    private CbbWatermarkConfigDTO watermarkInfo;

    /**
     * 启用本地盘
     */
    @Nullable
    private Boolean enableAllowLocalDisk;

    /**
     * 统一管理ID
     */
    @Nullable
    private UUID unifiedManageDataId;

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

    @Nullable
    private CbbEstProtocolType estProtocolType;


    @Nullable
    private AgreementDTO agreementInfo;

    @Nullable
    private Boolean enableTransparentEncrypt;

    @Nullable
    private CbbTransparentEncryptDTO transparentEncryptInfo;

    @Nullable
    private Boolean enableUsbBandwidth;

    @Nullable
    private CbbUsbBandwidthDTO usbBandwidthInfo;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    @Nullable
    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(@Nullable Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    @Nullable
    public Integer getUserMaxSnapshotNum() {
        return userMaxSnapshotNum;
    }

    public void setUserMaxSnapshotNum(@Nullable Integer userMaxSnapshotNum) {
        this.userMaxSnapshotNum = userMaxSnapshotNum;
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

    @Nullable
    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(@Nullable CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    @Nullable
    public CbbStrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(@Nullable CbbStrategyType strategyType) {
        this.strategyType = strategyType;
    }

    @Nullable
    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(@Nullable String computerName) {
        this.computerName = computerName;
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
    public String getAdOu() {
        return adOu;
    }

    public void setAdOu(@Nullable String adOu) {
        this.adOu = adOu;
    }

    @Nullable
    public DeskTopAllowLoginTimeDTO[] getDesktopAllowLoginTimeArr() {
        return desktopAllowLoginTimeArr;
    }

    public void setDesktopAllowLoginTimeArr(@Nullable DeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr) {
        this.desktopAllowLoginTimeArr = desktopAllowLoginTimeArr;
    }

    @Nullable
    public Boolean getEnableAllowLocalDisk() {
        return enableAllowLocalDisk;
    }

    public void setEnableAllowLocalDisk(@Nullable Boolean enableAllowLocalDisk) {
        this.enableAllowLocalDisk = enableAllowLocalDisk;
    }

    @Nullable
    public Boolean getEnableOpenDesktopRedirect() {
        return enableOpenDesktopRedirect;
    }

    public void setEnableOpenDesktopRedirect(@Nullable Boolean enableOpenDesktopRedirect) {
        this.enableOpenDesktopRedirect = enableOpenDesktopRedirect;
    }

    @Nullable
    public UUID getUnifiedManageDataId() {
        return unifiedManageDataId;
    }

    public void setUnifiedManageDataId(@Nullable UUID unifiedManageDataId) {
        this.unifiedManageDataId = unifiedManageDataId;
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
    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(@Nullable CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }
}
