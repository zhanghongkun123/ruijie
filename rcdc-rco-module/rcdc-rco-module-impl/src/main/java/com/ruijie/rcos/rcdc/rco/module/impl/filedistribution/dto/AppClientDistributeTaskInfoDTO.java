package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterSeedDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionExecType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionLocationType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 文件分发任务信息（应用客户端）
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/23 14:05
 *
 * @author zhangyichi
 */
public class AppClientDistributeTaskInfoDTO {

    @JSONField(name = "taskId")
    @NotNull
    private UUID taskId;

    @JSONField(name = "rcaClientId")
    @NotNull
    private Integer rcaClientId;

    @JSONField(name = "locationType")
    @NotNull
    private FileDistributionLocationType locationType;

    /**
     * 文件存放路径
     */
    @Nullable
    private String location;

    /**
     * 是否替换同名文件
     */
    @JSONField(name = "isReplace")
    @Nullable
    private Boolean isReplace;

    /**
     * 任务执行方式
     */
    @JSONField(name = "execType")
    @NotNull
    private FileDistributionExecType execType;

    /**
     * 运行命令
     */
    @JSONField(name = "execCmd")
    @Nullable
    private String execCmd;

    /**
     * 失败后重试间隔（分）
     */
    @JSONField(name = "retryInterval")
    @Nullable
    private Integer retryInterval;

    /**
     * 限制资源占用率高时不执行
     */
    @JSONField(name = "busyLimit")
    @Nullable
    private Boolean isBusyLimit;

    /**
     * 系统重启时安装
     */
    @JSONField(name = "installOnReboot")
    @Nullable
    private Boolean isInstallOnReboot;

    /**
     * 运行数据列表
     */
    @JSONField(name = "data")
    @NotNull
    private List<DistributeParameterSeedDataDTO> dataList;

    /**
     * 是否保留文件
     */
    @JSONField(name = "isSave")
    @Nullable
    private Boolean isSave;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public Integer getRcaClientId() {
        return rcaClientId;
    }

    public void setRcaClientId(Integer rcaClientId) {
        this.rcaClientId = rcaClientId;
    }

    public FileDistributionLocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(FileDistributionLocationType locationType) {
        this.locationType = locationType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getIsReplace() {
        return isReplace;
    }

    public void setIsReplace(Boolean isReplace) {
        this.isReplace = isReplace;
    }

    public FileDistributionExecType getExecType() {
        return execType;
    }

    public void setExecType(FileDistributionExecType execType) {
        this.execType = execType;
    }

    public String getExecCmd() {
        return execCmd;
    }

    public void setExecCmd(String execCmd) {
        this.execCmd = execCmd;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }

    public Boolean getIsBusyLimit() {
        return isBusyLimit;
    }

    public void setIsBusyLimit(Boolean isBusyLimit) {
        this.isBusyLimit = isBusyLimit;
    }

    public Boolean getIsInstallOnReboot() {
        return isInstallOnReboot;
    }

    public void setIsInstallOnReboot(Boolean isInstallOnReboot) {
        this.isInstallOnReboot = isInstallOnReboot;
    }

    public List<DistributeParameterSeedDataDTO> getDataList() {
        return dataList;
    }

    public void setDataList(List<DistributeParameterSeedDataDTO> dataList) {
        this.dataList = dataList;
    }

    public Boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(Boolean save) {
        isSave = save;
    }

}
