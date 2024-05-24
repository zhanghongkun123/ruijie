package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbClipboardMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUsbStorageDeviceMappingMode;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;

import java.util.UUID;

/**
 * <br>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2019/4/3  <br>
 *
 * @author yyz
 */
public class DeskStrategyRecommendDTO {


    private UUID id;

    private String strategyName;

    private String desktopType;

    private Integer systemDisk;

    private boolean enableInternet;

    private boolean enableUsbReadOnly;

    private boolean isShow;

    private UUID[] usbTypeIdArr;

    private CbbClipboardMode clipBoardMode;

    private CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr;

    private CbbDesktopSessionType sessionType;

    private Boolean enableDoubleScreen;

    private Boolean enableAllowLocalDisk;

    private CbbStrategyType strategyType;

    private Boolean enableOpenDesktopRedirect;

    private String cloudDeskType;

    /**
     * 磁盘映射枚举，关闭、可读、读写
     */
    private DiskMappingEnum diskMappingType;

    /**
     * 是否开启局域网自动检测
     */
    private Boolean enableLanAutoDetection;

    private CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode;

    /**
     * 连接协议
     */
    private CbbEstProtocolType estProtocolType;

    /**
     * 协议配置
     */
    private AgreementDTO agreementInfo;

    /**
     * 透明加解密总开关
     */
    private Boolean enableTransparentEncrypt;

    public UUID[] getUsbTypeIdArr() {
        return usbTypeIdArr;
    }

    public void setUsbTypeIdArr(UUID[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
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

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public boolean isEnableInternet() {
        return enableInternet;
    }

    public void setEnableInternet(boolean enableInternet) {
        this.enableInternet = enableInternet;
    }

    public boolean isEnableUsbReadOnly() {
        return enableUsbReadOnly;
    }

    public void setEnableUsbReadOnly(boolean enableUsbReadOnly) {
        this.enableUsbReadOnly = enableUsbReadOnly;
    }

    public boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(boolean show) {
        isShow = show;
    }

    public CbbClipboardMode getClipBoardMode() {
        return clipBoardMode;
    }

    public void setClipBoardMode(CbbClipboardMode clipBoardMode) {
        this.clipBoardMode = clipBoardMode;
    }

    public CbbClipBoardSupportTypeDTO[] getClipBoardSupportTypeArr() {
        return clipBoardSupportTypeArr;
    }

    public void setClipBoardSupportTypeArr(CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr) {
        this.clipBoardSupportTypeArr = clipBoardSupportTypeArr;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Boolean getEnableDoubleScreen() {
        return enableDoubleScreen;
    }

    public void setEnableDoubleScreen(Boolean enableDoubleScreen) {
        this.enableDoubleScreen = enableDoubleScreen;
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
    
    public Boolean getEnableOpenDesktopRedirect() {
        return enableOpenDesktopRedirect;
    }

    public void setEnableOpenDesktopRedirect(Boolean enableOpenDesktopRedirect) {
        this.enableOpenDesktopRedirect = enableOpenDesktopRedirect;
    }

    public String getCloudDeskType() {
        return cloudDeskType;
    }

    public void setStrategyType(CbbStrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public void setCloudDeskType(String cloudDeskType) {
        this.cloudDeskType = cloudDeskType;
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

    public CbbUsbStorageDeviceMappingMode getUsbStorageDeviceMappingMode() {
        return usbStorageDeviceMappingMode;
    }

    public void setUsbStorageDeviceMappingMode(CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode) {
        this.usbStorageDeviceMappingMode = usbStorageDeviceMappingMode;
    }

    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(CbbEstProtocolType estProtocolType) {
        this.estProtocolType = estProtocolType;
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
}
