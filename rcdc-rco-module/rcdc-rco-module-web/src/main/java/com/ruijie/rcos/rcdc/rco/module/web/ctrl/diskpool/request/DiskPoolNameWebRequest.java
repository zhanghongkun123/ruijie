package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 校验磁盘池名称请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8
 *
 * @author TD
 */
public class DiskPoolNameWebRequest implements WebRequest {

    @ApiModelProperty(value = "磁盘池ID")
    @Nullable
    private UUID id;

    /**
     * 盘符
     */
    @ApiModelProperty(value = "盘符[D-Z]", required = true)
    @Nullable
    private String diskLetter;

    @NotBlank
    @TextShort
    @TextName
    @ApiModelProperty(value = "磁盘池名称", required = true)
    private String name;

    /**
     * 计算集群位置
     */
    @Nullable
    @ApiModelProperty(value = "计算集群位置", required = true)
    private UUID clusterId;

    /**
     * 存储池位置
     */
    @Nullable
    @ApiModelProperty(value = "存储池位置", required = true)
    private UUID storagePoolId;

    /**
     * 云平台
     */
    @Nullable
    @ApiModelProperty(value = "云平台", required = true)
    private UUID platformId;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDiskLetter() {
        return diskLetter;
    }

    public void setDiskLetter(@Nullable String diskLetter) {
        this.diskLetter = diskLetter;
    }

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    @Nullable
    public UUID getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(@Nullable UUID storagePoolId) {
        this.storagePoolId = storagePoolId;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
