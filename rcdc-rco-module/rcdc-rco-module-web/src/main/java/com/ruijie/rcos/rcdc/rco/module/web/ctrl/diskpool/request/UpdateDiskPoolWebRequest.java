package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request;

import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 修改磁盘池配置请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/11
 *
 * @author TD
 */
public class UpdateDiskPoolWebRequest extends DiskPoolNameWebRequest {

    /**
     * 是否开启自动创建磁盘
     */
    @NotNull
    @ApiModelProperty(value = "自动创建磁盘开关", required = true)
    private Boolean enableCreateDisk;

    /**
     * 磁盘配置大小
     */
    @NotNull
    @Range(min = "1", max = "2048")
    @ApiModelProperty(value = "磁盘配置大小", required = true)
    private Integer diskSize;

    /**
     * 磁盘池名称前缀,为null时采用磁盘池名称
     */
    @ApiModelProperty(value = "磁盘池名称前缀,为null时采用磁盘池名称")
    @Nullable
    @TextShort
    @TextName
    private String diskNamePrefix;

    /**
     * 备注
     */
    @Nullable
    @ApiModelProperty(value = "备注")
    private String description;

    public Boolean getEnableCreateDisk() {
        return enableCreateDisk;
    }

    public void setEnableCreateDisk(Boolean enableCreateDisk) {
        this.enableCreateDisk = enableCreateDisk;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    @Nullable
    public String getDiskNamePrefix() {
        return diskNamePrefix;
    }

    public void setDiskNamePrefix(@Nullable String diskNamePrefix) {
        this.diskNamePrefix = diskNamePrefix;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UpdateDiskPoolWebRequest{" +
                "enableCreateDisk=" + enableCreateDisk +
                ", diskSize=" + diskSize +
                ", diskNamePrefix='" + diskNamePrefix + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
