package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 创建用户配置策略请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/14
 *
 * @author WuShengQiang
 */
public class CreateUserProfileStrategyRequest implements WebRequest {

    @ApiModelProperty(value = "策略ID")
    @Nullable
    private UUID id;

    @ApiModelProperty(value = "策略名称，通用名称格式", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String name;

    @ApiModelProperty(value = "存储位置(本地/UNC路径)", required = true)
    @NotNull
    private UserProfileStrategyStorageTypeEnum storageType;

    @ApiModelProperty(value = "磁盘路径")
    @Nullable
    @Size(max = 10000)
    private String diskPath;

    @ApiModelProperty(value = "磁盘容量 单位:GB", required = true)
    @Nullable
    @Range(min = "1", max = "2048")
    private Integer diskSize;

    @ApiModelProperty(value = "策略关联的路径对象列表", required = true)
    @NotNull
    private UserProfileStrategyRelatedDTO[] pathArr;

    @ApiModelProperty(value = "策略描述")
    @Nullable
    @TextMedium
    private String description;

    @ApiModelProperty("文件服务器ID")
    @Nullable
    private UUID externalStorageId;

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

    public UserProfileStrategyStorageTypeEnum getStorageType() {
        return storageType;
    }

    public void setStorageType(UserProfileStrategyStorageTypeEnum storageType) {
        this.storageType = storageType;
    }

    @Nullable
    public String getDiskPath() {
        return diskPath;
    }

    public void setDiskPath(@Nullable String diskPath) {
        this.diskPath = diskPath;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public UserProfileStrategyRelatedDTO[] getPathArr() {
        return pathArr;
    }

    public void setPathArr(UserProfileStrategyRelatedDTO[] pathArr) {
        this.pathArr = pathArr;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(@Nullable UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }
}