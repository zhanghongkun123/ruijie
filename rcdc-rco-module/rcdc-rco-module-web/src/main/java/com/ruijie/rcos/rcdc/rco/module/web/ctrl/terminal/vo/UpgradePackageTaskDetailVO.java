package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbSystemUpgradeTaskDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalSystemUpgradePackageInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbSystemUpgradeModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbSystemUpgradeTaskStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbBootTypeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 终端升级包、历史升级任务统一视图结构
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/15 17:28
 *
 * @author zhangyichi
 */
public class UpgradePackageTaskDetailVO {
    private UUID packageId;

    private String packageName;

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

    private long total = 0;

    private int waitNum = 0;

    private int upgradingNum = 0;

    private int failNum = 0;

    private String packageArch;

    private String packageVersion;

    private String cpulist;

    private String boot;

    private String plat;

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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getWaitNum() {
        return waitNum;
    }

    public void setWaitNum(int waitNum) {
        this.waitNum = waitNum;
    }

    public int getUpgradingNum() {
        return upgradingNum;
    }

    public void setUpgradingNum(int upgradingNum) {
        this.upgradingNum = upgradingNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    public String getPackageArch() {
        return packageArch;
    }

    public void setPackageArch(String packageArch) {
        this.packageArch = packageArch;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    public String getCpulist() {
        return cpulist;
    }

    public void setCpulist(String cpulist) {
        this.cpulist = cpulist;
    }

    public String getBoot() {
        return boot;
    }

    public void setBoot(String boot) {
        this.boot = boot;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    /**
     * 从升级任务信息中更新字段
     * @param taskDTO 升级任务信息
     */
    public void updateFields(CbbSystemUpgradeTaskDTO taskDTO) {
        Assert.notNull(taskDTO, "taskDTO cannot be null!");

        this.upgradeTaskId = taskDTO.getId();
        this.date = taskDTO.getCreateTime();
        this.packageName = taskDTO.getPackageName();
        this.packageType = taskDTO.getPackageType();
        this.state = taskDTO.getUpgradeTaskState();
        this.upgradeMode = taskDTO.getUpgradeMode();
    }

    /**
     * 从升级包信息中更新字段
     * @param packageInfoDTO 升级包信息
     */
    public void updateFields(CbbTerminalSystemUpgradePackageInfoDTO packageInfoDTO) {
        Assert.notNull(packageInfoDTO, "packageInfoDTO cannot be null!");

        this.packageId = packageInfoDTO.getId();
        this.date = packageInfoDTO.getUploadTime();
        this.packageName = packageInfoDTO.getName();
        this.packageType = packageInfoDTO.getPackageType();
        this.state = packageInfoDTO.getState();
        this.upgradeMode = packageInfoDTO.getUpgradeMode();
        this.upgradeTaskId = packageInfoDTO.getUpgradeTaskId();
        this.cpulist = packageInfoDTO.getSupportCpu();
        String boot = packageInfoDTO.getBoot();
        this.boot = Objects.isNull(boot) ? null : boot;
        this.packageArch = packageInfoDTO.getCpuArch().getArchName();
        this.packageVersion = packageInfoDTO.getPackageVersion();
        this.plat = packageInfoDTO.getPlat();
    }
}
