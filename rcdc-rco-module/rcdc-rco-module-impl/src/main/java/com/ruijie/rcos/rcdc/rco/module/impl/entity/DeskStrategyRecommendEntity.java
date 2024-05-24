package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbClipboardMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUsbStorageDeviceMappingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
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
@Entity
@Table(name = "t_rco_user_desk_strategy_recommend")
public class DeskStrategyRecommendEntity {

    @Id
    private UUID id;

    private Date createTime;

    private String name;

    private String pattern;

    private Integer cpu;

    private Integer memory;

    private Integer systemSize;

    private Integer personalSize;

    private Boolean isAllowInternet;

    private boolean isOpenUsbReadOnly;

    private boolean isShow;

    @Enumerated(EnumType.STRING)
    private CbbClipboardMode clipBoardMode;

    @Enumerated(EnumType.STRING)
    private CbbDesktopSessionType sessionType;

    /**
     * 支持传输的类型
     */
    private String clipBoardSupportType;

    private Boolean isOpenDoubleScreen;

    private Boolean isAllowLocalDisk;

    private Boolean isOpenDesktopRedirect;

    @Enumerated(EnumType.STRING)
    private CbbStrategyType cloudDeskType;

    @Version
    private int version;

    /**
     * 开启磁盘映射
     */
    private Boolean enableDiskMapping = false;

    /**
     * 是否可写
     */
    private Boolean enableDiskMappingWriteable = false;

    /**
     * 是否开启局域网自动检测
     */
    private Boolean enableLanAutoDetection = false;

    /**
     * USB存储设备映射模式
     */
    @Enumerated(EnumType.STRING)
    private CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode;

    /**
     * 连接协议
     */
    @Enumerated(EnumType.STRING)
    private CbbEstProtocolType estProtocolType;

    /**
     * 协议配置
     */
    @Column(name = "agreement_info")
    private String agreementInfo;

    /**
     * 透明加解密总开关
     */
    private Boolean enableTransparentEncrypt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getSystemSize() {
        return systemSize;
    }

    public void setSystemSize(Integer systemSize) {
        this.systemSize = systemSize;
    }

    public Integer getPersonalSize() {
        return personalSize;
    }

    public void setPersonalSize(Integer personalSize) {
        this.personalSize = personalSize;
    }

    public Boolean getIsAllowInternet() {
        return isAllowInternet;
    }

    public void setIsAllowInternet(Boolean allowInternet) {
        isAllowInternet = allowInternet;
    }

    public boolean getIsOpenUsbReadOnly() {
        return isOpenUsbReadOnly;
    }

    public void setIsOpenUsbReadOnly(boolean openUsbReadOnly) {
        isOpenUsbReadOnly = openUsbReadOnly;
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

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public String getClipBoardSupportType() {
        return clipBoardSupportType;
    }

    public void setClipBoardSupportType(String clipBoardSupportType) {
        this.clipBoardSupportType = clipBoardSupportType;
    }

    public Boolean getIsOpenDoubleScreen() {
        return isOpenDoubleScreen;
    }

    public void setIsOpenDoubleScreen(Boolean openDoubleScreen) {
        isOpenDoubleScreen = openDoubleScreen;
    }

    public Boolean getIsAllowLocalDisk() {
        return isAllowLocalDisk;
    }

    public void setIsAllowLocalDisk(Boolean allowLocalDisk) {
        isAllowLocalDisk = allowLocalDisk;
    }

    public CbbStrategyType getCloudDeskType() {
        return cloudDeskType;
    }

    public void setCloudDeskType(CbbStrategyType cloudDeskType) {
        this.cloudDeskType = cloudDeskType;
    }

    public Boolean getIsOpenDesktopRedirect() {
        return isOpenDesktopRedirect;
    }

    public void setIsOpenDesktopRedirect(Boolean openDesktopRedirect) {
        isOpenDesktopRedirect = openDesktopRedirect;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Boolean getEnableDiskMapping() {
        return enableDiskMapping;
    }

    public void setEnableDiskMapping(Boolean enableDiskMapping) {
        this.enableDiskMapping = enableDiskMapping;
    }

    public Boolean getEnableDiskMappingWriteable() {
        return enableDiskMappingWriteable;
    }

    public void setEnableDiskMappingWriteable(Boolean enableDiskMappingWriteable) {
        this.enableDiskMappingWriteable = enableDiskMappingWriteable;
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

    public String getAgreementInfo() {
        return agreementInfo;
    }

    public void setAgreementInfo(String agreementInfo) {
        this.agreementInfo = agreementInfo;
    }

    public Boolean getEnableTransparentEncrypt() {
        return enableTransparentEncrypt;
    }

    public void setEnableTransparentEncrypt(Boolean enableTransparentEncrypt) {
        this.enableTransparentEncrypt = enableTransparentEncrypt;
    }
}
