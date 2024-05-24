package com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution;


import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionExecType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionLocationType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionOsType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.CompressPackageConfigDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 分发任务参数DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 09:54
 *
 * @author zhangyichi
 */
public class DistributeParameterDTO {

    private UUID id;

    @NotNull
    @JSONField(name = "location_type", alternateNames = "locationType")
    private FileDistributionLocationType locationType;

    /**
     * 文件存放路径
     */
    @Nullable
    private String location;

    /**
     * 是否替换同名文件
     */
    @NotNull
    @JSONField(name = "is_replace", alternateNames = "isReplace")
    private Boolean isReplace;

    /**
     * 任务执行方式
     */
    @NotNull
    @JSONField(name = "exec_type", alternateNames = "execType")
    private FileDistributionExecType execType;

    /**
     * 运行命令
     */
    @Nullable
    @JSONField(name = "exec_cmd", alternateNames = "execCmd")
    private String execCmd;

    /**
     * 失败后重试间隔（分）
     */
    @NotNull
    @JSONField(name = "retry_interval", alternateNames = "retryInterval")
    private Integer retryInterval;

    /**
     * 限制资源占用率高时不执行
     */
    @JSONField(name = "busy_limit", alternateNames = "busyLimit")
    private Boolean isBusyLimit;

    /**
     * 系统重启时安装
     */
    @JSONField(name = "install_on_reboot", alternateNames = "installOnReboot")
    private Boolean isInstallOnReboot;

    /**
     * 运行数据列表
     */
    @NotNull
    @JSONField(name = "data")
    private List<DistributeParameterDataDTO> dataList;

    /**
     * 是否保留文件
     */
    @NotNull
    @JSONField(name = "is_save", alternateNames = "isSave")
    private Boolean isSave;

    /**
     * 压缩包配置
     */
    @Nullable
    private CompressPackageConfigDTO compressPackageConfig;


    @Nullable
    private FileDistributionOsType osLike = FileDistributionOsType.WINDOWS;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<DistributeParameterDataDTO> getDataList() {
        return dataList;
    }

    public void setDataList(List<DistributeParameterDataDTO> dataList) {
        this.dataList = dataList;
    }

    public Boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(Boolean save) {
        isSave = save;
    }

    @Nullable
    public CompressPackageConfigDTO getCompressPackageConfig() {
        return compressPackageConfig;
    }

    public void setCompressPackageConfig(@Nullable CompressPackageConfigDTO compressPackageConfig) {
        this.compressPackageConfig = compressPackageConfig;
    }

    @Nullable
    public FileDistributionOsType getOsLike() {
        return osLike;
    }

    public void setOsLike(@Nullable FileDistributionOsType osLike) {
        this.osLike = osLike;
    }
}

