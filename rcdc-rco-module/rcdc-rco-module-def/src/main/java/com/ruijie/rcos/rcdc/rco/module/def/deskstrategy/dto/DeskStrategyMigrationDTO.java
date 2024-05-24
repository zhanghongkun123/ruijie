
package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/13 20:22
 *
 * @author linke
 */
@PageQueryDTOConfig(entityType = "DeskStrategyEntity")
public class DeskStrategyMigrationDTO extends EqualsHashcodeSupport {

    private UUID id;

    private int version = 0;

    /**
     * 策略名称
     **/
    private String name;

    /**
     * 运行方式，个性：PERSONAL 还原：RECOVERABLE
     **/
    private CbbCloudDeskPattern pattern;

    /**
     * 系统盘大小
     **/
    private int systemSize;

    private CbbStrategyType strategyType;

    /**
     * 是否开启USB只读
     **/
    private boolean isOpenUsbReadOnly;

    /**
     * 创建时间
     **/
    private Date createTime;

    private int refCount;

    /**
     * 云桌面策略状态
     **/
    private CbbDeskStrategyState state;

    /**
     * 是否开启桌面重定向
     **/
    private boolean isOpenDesktopRedirect;

    private Boolean enableNested;

    /**
     * VDI策略独有的属性
     **/
    private int cpu;

    /**
     * 内存大小
     **/
    private int memory;

    /**
     * Windows软终端剪切板模式
     **/
    private CbbClipboardMode clipBoardMode;

    /**
     * 是否开启双屏
     **/
    private boolean isOpenDoubleScreen;

    /**
     * 是否开启防截屏
     **/
    private Boolean forbidCatchScreen;

    /**
     * 个人盘大小
     **/
    private int personSize;

    /**
     * 是否开启上网，默认为true
     **/
    private boolean isOpenInternet;

    /**
     * IDV的属性
     **/
    private Boolean isAllowLocalDisk;

    /**
     * 桌面异常处理策略
     **/
    private String deskErrorStrategy;

    private CbbDeskPersonalConfigStrategyType deskPersonalConfigStrategyType;

    /**
     * 个性数据配置盘大小
     **/
    private Integer personalConfigDiskSize;

    private Boolean enableDiskMapping;

    private Boolean enableDiskMappingWriteable;

    private Boolean enableLanAutoDetection;

    private DeskCreateMode deskCreateMode;

    public DeskCreateMode getDeskCreateMode() {
        return deskCreateMode;
    }

    public void setDeskCreateMode(DeskCreateMode deskCreateMode) {
        this.deskCreateMode = deskCreateMode;
    }

    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(Boolean enableNested) {
        this.enableNested = enableNested;
    }

    /**
     * 桌面是否自适应终端分辨率
     */
    private Boolean enableAdaptiveResolution;

    /**
     * 桌面是否隐藏浮动条
     */
    private Boolean needHideFloatBar;

    private Boolean enableSoftwareDecode;

    /**
     * 虚拟化GPU的显卡类型
     */
    @Enumerated(EnumType.STRING)
    private VgpuType vgpuType = VgpuType.QXL;

    /**
     * vgpu的扩展配置
     */
    private String vgpuExtraInfo = "{}";

    private String remark;

    /**
     * 管理员名称
     */
    @Column(name = "creator_user_name")
    private String creatorUserName;

    /**
     * AD域加入OU路径
     */
    private String adOu;

    /**
     * 自动编辑功能，RCC V6.0 R2 ADD，是否开启自动编辑
     */
    private Boolean autoEdit;

    /**
     * 自动编辑功能，RCC V6.0 R2 ADD，是否适配完驱动后自动重启
     */
    private Boolean forceAutoEdit;


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

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getPersonSize() {
        return personSize;
    }

    public void setPersonSize(int personSize) {
        this.personSize = personSize;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public CbbStrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(CbbStrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public boolean isOpenInternet() {
        return isOpenInternet;
    }

    public void setOpenInternet(boolean isOpenInternet) {
        this.isOpenInternet = isOpenInternet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CbbCloudDeskPattern getPattern() {
        return pattern;
    }

    public void setPattern(CbbCloudDeskPattern pattern) {
        this.pattern = pattern;
    }

    public int getSystemSize() {
        return systemSize;
    }

    public void setSystemSize(int systemSize) {
        this.systemSize = systemSize;
    }

    public boolean isOpenUsbReadOnly() {
        return isOpenUsbReadOnly;
    }

    public void setOpenUsbReadOnly(boolean isOpenUsbReadOnly) {
        this.isOpenUsbReadOnly = isOpenUsbReadOnly;
    }

    public boolean isOpenDoubleScreen() {
        return isOpenDoubleScreen;
    }

    public void setOpenDoubleScreen(boolean openDoubleScreen) {
        isOpenDoubleScreen = openDoubleScreen;
    }

    public boolean getForbidCatchScreen() {
        return forbidCatchScreen;
    }

    public void setForbidCatchScreen(boolean forbidCatchScreen) {
        this.forbidCatchScreen = forbidCatchScreen;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getRefCount() {
        return refCount;
    }

    public void setRefCount(int refCount) {
        this.refCount = refCount;
    }

    public CbbDeskStrategyState getState() {
        return state;
    }

    public void setState(CbbDeskStrategyState state) {
        this.state = state;
    }

    public CbbClipboardMode getClipBoardMode() {
        return clipBoardMode;
    }

    public void setClipBoardMode(CbbClipboardMode clipBoardMode) {
        this.clipBoardMode = clipBoardMode;
    }

    public boolean getOpenDesktopRedirect() {
        return isOpenDesktopRedirect;
    }

    public void setOpenDesktopRedirect(boolean isOpenDesktopRedirect) {
        this.isOpenDesktopRedirect = isOpenDesktopRedirect;
    }

    public boolean getIsOpenInternet() {
        return isOpenInternet;
    }

    public void setIsOpenInternet(boolean isOpenInternet) {
        this.isOpenInternet = isOpenInternet;
    }

    public boolean getIsOpenUsbReadOnly() {
        return isOpenUsbReadOnly;
    }

    public void setIsOpenUsbReadOnly(boolean isOpenUsbReadOnly) {
        this.isOpenUsbReadOnly = isOpenUsbReadOnly;
    }

    public Boolean getAllowLocalDisk() {
        return isAllowLocalDisk;
    }

    public void setAllowLocalDisk(Boolean allowLocalDisk) {
        isAllowLocalDisk = allowLocalDisk;
    }

    public boolean isOpenDesktopRedirect() {
        return isOpenDesktopRedirect;
    }

    public String getDeskErrorStrategy() {
        return deskErrorStrategy;
    }

    public void setDeskErrorStrategy(String deskErrorStrategy) {
        this.deskErrorStrategy = deskErrorStrategy;
    }

    public CbbDeskPersonalConfigStrategyType getDeskPersonalConfigStrategyType() {
        return deskPersonalConfigStrategyType;
    }

    public void setDeskPersonalConfigStrategyType(CbbDeskPersonalConfigStrategyType deskPersonalConfigStrategyType) {
        this.deskPersonalConfigStrategyType = deskPersonalConfigStrategyType;
    }

    public Integer getPersonalConfigDiskSize() {
        return personalConfigDiskSize;
    }

    public void setPersonalConfigDiskSize(Integer personalConfigDiskSize) {
        this.personalConfigDiskSize = personalConfigDiskSize;
    }

    public Boolean getEnableAdaptiveResolution() {
        return enableAdaptiveResolution;
    }

    public void setEnableAdaptiveResolution(Boolean enableAdaptiveResolution) {
        this.enableAdaptiveResolution = enableAdaptiveResolution;
    }

    public Boolean getNeedHideFloatBar() {
        return needHideFloatBar;
    }

    public void setNeedHideFloatBar(Boolean needHideFloatBar) {
        this.needHideFloatBar = needHideFloatBar;
    }

    public Boolean getEnableSoftwareDecode() {
        return enableSoftwareDecode;
    }

    public void setEnableSoftwareDecode(Boolean enableSoftwareDecode) {
        this.enableSoftwareDecode = enableSoftwareDecode;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public String getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(String vgpuInfo) {
        this.vgpuExtraInfo = vgpuInfo;
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

    public String getAdOu() {
        return adOu;
    }

    public void setAdOu(String adOu) {
        this.adOu = adOu;
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

    public Boolean getAutoEdit() {
        return autoEdit;
    }

    public void setAutoEdit(Boolean autoEdit) {
        this.autoEdit = autoEdit;
    }

    public Boolean getForceAutoEdit() {
        return forceAutoEdit;
    }

    public void setForceAutoEdit(Boolean forceAutoEdit) {
        this.forceAutoEdit = forceAutoEdit;
    }


}
