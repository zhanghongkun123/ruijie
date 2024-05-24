package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 创建磁盘池请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8
 *
 * @author TD
 */
public class CreateDiskPoolWebRequest extends DiskPoolNameWebRequest {

    /**
     * 磁盘数量
     */
    @ApiModelProperty(value = "磁盘数量", required = true)
    @NotNull
    @Range(min = "1", max = "1000")
    private Integer diskNum;

    /**
     * 是否开启自动创建磁盘
     */
    @NotNull
    @ApiModelProperty(value = "自动创建磁盘开关", required = true)
    private Boolean enableCreateDisk;

    /**
     * 磁盘配置大小
     */
    @ApiModelProperty(value = "磁盘配置大小", required = true)
    @NotNull
    @Range(min = "20", max = "2048")
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

    public Integer getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(Integer diskNum) {
        this.diskNum = diskNum;
    }

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
        return "CreateDiskPoolWebRequest{" +
                "diskNum=" + diskNum +
                ", enableCreateDisk=" + enableCreateDisk +
                ", diskSize=" + diskSize +
                ", diskNamePrefix='" + diskNamePrefix + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
