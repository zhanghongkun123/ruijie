package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbComponentIndependentUpgradeTaskDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalComponentIndependentUpgradePackageComponentDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalComponentIndependentUpgradePackageInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbSystemUpgradeModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbSystemUpgradeTaskStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 组件独立升级包，历史升级任务记录统一视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/21
 *
 * @author lyb
 */
public class ComponentIndependentUpgradePackageTaskDetailVO {
    private UUID packageId;

    private String packageName;

    private String packageVersion;

    /**
     * 刷机包平台类型
     */
    private CbbTerminalTypeEnums packageType;

    private CbbSystemUpgradeTaskStateEnums state;

    private UUID upgradeTaskId;

    /**
     * 升级包的上传时间或升级任务的创建时间
     */
    private Date date;

    private CbbSystemUpgradeModeEnums upgradeMode;

    private Long total = 0L;

    private Integer waitNum = 0;

    private Integer upgradingNum = 0;

    private Integer failNum = 0;

    private ComponentIndependentUpgradePackageComponentVO[] componentArr;

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    public CbbTerminalTypeEnums getPackageType() {
        return packageType;
    }

    public void setPackageType(CbbTerminalTypeEnums packageType) {
        this.packageType = packageType;
    }

    public CbbSystemUpgradeTaskStateEnums getState() {
        return state;
    }

    public void setState(CbbSystemUpgradeTaskStateEnums state) {
        this.state = state;
    }

    public UUID getUpgradeTaskId() {
        return upgradeTaskId;
    }

    public void setUpgradeTaskId(UUID upgradeTaskId) {
        this.upgradeTaskId = upgradeTaskId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CbbSystemUpgradeModeEnums getUpgradeMode() {
        return upgradeMode;
    }

    public void setUpgradeMode(CbbSystemUpgradeModeEnums upgradeMode) {
        this.upgradeMode = upgradeMode;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getWaitNum() {
        return waitNum;
    }

    public void setWaitNum(Integer waitNum) {
        this.waitNum = waitNum;
    }

    public Integer getUpgradingNum() {
        return upgradingNum;
    }

    public void setUpgradingNum(Integer upgradingNum) {
        this.upgradingNum = upgradingNum;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public ComponentIndependentUpgradePackageComponentVO[] getComponentArr() {
        return componentArr;
    }

    public void setComponentArr(ComponentIndependentUpgradePackageComponentVO[] componentArr) {
        this.componentArr = componentArr;
    }

    /**
     * 从升级任务信息中更新字段
     * 
     * @param taskDTO 升级任务信息
     */
    public void updateFields(CbbComponentIndependentUpgradeTaskDTO taskDTO) {
        Assert.notNull(taskDTO, "taskDTO cannot be null!");

        this.upgradeTaskId = taskDTO.getId();
        this.packageId = taskDTO.getUpgradePackageId();
        this.date = taskDTO.getCreateTime();
        this.packageName = taskDTO.getPackageName();
        this.packageType = taskDTO.getPackageType();
        this.state = taskDTO.getUpgradeTaskState();
        this.upgradeMode = taskDTO.getUpgradeMode();
    }

    /**
     * 从升级包信息中更新字段
     * 
     * @param packageInfoDTO 升级包信息
     */
    public void updateFields(CbbTerminalComponentIndependentUpgradePackageInfoDTO packageInfoDTO) {
        Assert.notNull(packageInfoDTO, "packageInfoDTO cannot be null!");

        this.packageId = packageInfoDTO.getId();
        this.date = packageInfoDTO.getUploadTime();
        this.packageName = packageInfoDTO.getName();
        this.packageVersion = packageInfoDTO.getPackageVersion();
        this.packageType = packageInfoDTO.getPackageType();
        this.state = packageInfoDTO.getState();
        this.upgradeMode = packageInfoDTO.getUpgradeMode();
        this.upgradeTaskId = packageInfoDTO.getUpgradeTaskId();
        updateFields(packageInfoDTO.getComponentList());
    }

    private void updateFields(List<CbbTerminalComponentIndependentUpgradePackageComponentDTO> componentDTOList) {
        Assert.notEmpty(componentDTOList, "componentDTOList must not be empty");
        this.componentArr = componentDTOList.stream().map(ComponentIndependentUpgradePackageComponentVO::new)
                .toArray(ComponentIndependentUpgradePackageComponentVO[]::new);
    }
}
