package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.CompressPackageConfigDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 文件分发参数VO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/25 15:46
 *
 * @author zhangyichi
 */
public class DistributeParameterVO {

    @Nullable
    private UUID id;

    /**
     * FileDistributionLocationType
     */
    @NotNull
    private String locationType;

    /**
     * 文件存放路径
     */
    @Nullable
    private String location;

    /**
     * 是否替换同名文件
     */
    @NotNull
    private Boolean isReplace;

    /**
     * 任务执行方式
     * FileDistributionExecType
     */
    @NotNull
    private String execType;

    /**
     * 运行命令
     */
    @Nullable
    private String execCmd;

    /**
     * 失败后重试间隔（分）
     */
    @Nullable
    private Integer retryInterval;

    /**
     * 限制资源占用率高时不执行
     */
    @Nullable
    private Boolean isBusyLimit;

    /**
     * 系统重启时安装
     */
    @Nullable
    private Boolean isInstallOnReboot;

    /**
     * 运行数据列表
     */
    @Nullable
    @JSONField(name = "data")
    private List<DistributeParameterDataVO> dataList;

    /**
     * 是否保留文件
     */
    @NotNull
    private Boolean isSave;

    /**
     * 压缩包配置
     */
    @Nullable
    private CompressPackageConfigDTO compressPackageConfig;

    @Nullable
    private String osLike;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
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

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
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

    @Nullable
    public List<DistributeParameterDataVO> getDataList() {
        return dataList;
    }

    public void setDataList(@Nullable List<DistributeParameterDataVO> dataList) {
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
    public String getOsLike() {
        return osLike;
    }

    public void setOsLike(@Nullable String osLike) {
        this.osLike = osLike;
    }
}
