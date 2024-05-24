
package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbIpLimitDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/13 20:22
 *
 * @author linke
 */
public class DeskStrategyDTO extends EqualsHashcodeSupport {

    private UUID id;

    private String strategyName;

    private CbbCloudDeskPattern desktopType;

    private Integer systemDisk;

    private Boolean enableUsbReadOnly;

    private Boolean enableInternet;

    private Boolean enableDoubleScreen;

    private Integer cloudNumber;

    private Boolean canUsed;

    /** 不能使用提示消息 */
    private String canUsedMessage;

    private CbbDeskStrategyState deskStrategyState;

    private CbbClipboardMode clipBoardMode;

    private CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr;

    private Boolean enableOpenDesktopRedirect;

    /**
     * idv特有，是否启用本地盘
     */
    private Boolean enableAllowLocalDisk;

    private String strategyType;

    /**
     * 计算机名称，RCO5.3版本新增
     */
    private String computerName;

    private Boolean hasBindUserOrUserGroup;

    private Boolean needWarnGpuError;

    /**
     * 协议代理开关
     */
    private Boolean enableAgreementAgency;

    /**
     * 创建者登录账号
     */
    private String  creatorUserName;

    /**
     * 云桌面标签
     */
    private String remark;

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
     * 键盘模拟类型
     */
    private CbbKeyboardEmulationType keyboardEmulationType;

    /**
     * 是否开启系统盘自动扩容
     */
    private Boolean enableFullSystemDisk;

    /**
     * 是否开启水印
     */
    private Boolean enableWatermark;

    /**
     * 水印配置内容
     */
    private CbbWatermarkConfigDTO watermarkInfo;

    /**
     * 是否锁定
     */
    private Boolean canLock;

    /**
     * 统一数据管理ID：多计算集群场景下，该数据才会存在
     */
    private UUID unifiedManageDataId;

    /**
     * 描述
     */
    private String note;

    /**
     * IP访问规则
     */
    private CbbIpLimitModeEnum ipLimitMode;

    /**
     * IP网段
     */
    private List<CbbIpLimitDTO> ipSegmentDTOList;

    /**
     * 桌面登录账号同步
     */
    private Boolean desktopSyncLoginAccount;

    /**
     * 桌面登录密码同步
     */
    private Boolean desktopSyncLoginPassword;

    /**
     * 桌面登录账号权限
     */
    private CbbSyncLoginAccountPermissionEnums desktopSyncLoginAccountPermission;

    /**
     * 是否开启高可用
     */
    private Boolean enableHa;

    /**
     * "配置HA优先级"
     */
    private Integer haPriority;

    /**
     * USB存储设备映射
     */
    private CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode;

    /**
     * 会话类型
     **/
    private CbbDesktopSessionType sessionType;

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

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
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

    public String getCanUsedMessage() {
        return canUsedMessage;
    }

    public void setCanUsedMessage(String canUsedMessage) {
        this.canUsedMessage = canUsedMessage;
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

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
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

    public Boolean getEnableAgreementAgency() {
        return enableAgreementAgency;
    }

    public void setEnableAgreementAgency(Boolean enableAgreementAgency) {
        this.enableAgreementAgency = enableAgreementAgency;
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

    public Boolean getEnableWatermark() {
        return enableWatermark;
    }

    public void setEnableWatermark(Boolean enableWatermark) {
        this.enableWatermark = enableWatermark;
    }

    public CbbWatermarkConfigDTO getWatermarkInfo() {
        return watermarkInfo;
    }

    public void setWatermarkInfo(CbbWatermarkConfigDTO watermarkInfo) {
        this.watermarkInfo = watermarkInfo;
    }

    public Boolean getCanLock() {
        return canLock;
    }

    public void setCanLock(Boolean canLock) {
        this.canLock = canLock;
    }

    public UUID getUnifiedManageDataId() {
        return unifiedManageDataId;
    }

    public void setUnifiedManageDataId(UUID unifiedManageDataId) {
        this.unifiedManageDataId = unifiedManageDataId;
    }

    public CbbClipBoardSupportTypeDTO[] getClipBoardSupportTypeArr() {
        return clipBoardSupportTypeArr;
    }

    public void setClipBoardSupportTypeArr(CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr) {
        this.clipBoardSupportTypeArr = clipBoardSupportTypeArr;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Boolean getDesktopSyncLoginAccount() {
        return desktopSyncLoginAccount;
    }

    public void setDesktopSyncLoginAccount(Boolean desktopSyncLoginAccount) {
        this.desktopSyncLoginAccount = desktopSyncLoginAccount;
    }

    public Boolean getDesktopSyncLoginPassword() {
        return desktopSyncLoginPassword;
    }

    public void setDesktopSyncLoginPassword(Boolean desktopSyncLoginPassword) {
        this.desktopSyncLoginPassword = desktopSyncLoginPassword;
    }

    public CbbSyncLoginAccountPermissionEnums getDesktopSyncLoginAccountPermission() {
        return desktopSyncLoginAccountPermission;
    }

    public void setDesktopSyncLoginAccountPermission(CbbSyncLoginAccountPermissionEnums desktopSyncLoginAccountPermission) {
        this.desktopSyncLoginAccountPermission = desktopSyncLoginAccountPermission;
    }

    public Boolean getEnableHa() {
        return enableHa;
    }

    public void setEnableHa(Boolean enableHa) {
        this.enableHa = enableHa;
    }

    public Integer getHaPriority() {
        return haPriority;
    }

    public void setHaPriority(Integer haPriority) {
        this.haPriority = haPriority;
    }

    public CbbUsbStorageDeviceMappingMode getUsbStorageDeviceMappingMode() {
        return usbStorageDeviceMappingMode;
    }

    public void setUsbStorageDeviceMappingMode(CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode) {
        this.usbStorageDeviceMappingMode = usbStorageDeviceMappingMode;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }
}
