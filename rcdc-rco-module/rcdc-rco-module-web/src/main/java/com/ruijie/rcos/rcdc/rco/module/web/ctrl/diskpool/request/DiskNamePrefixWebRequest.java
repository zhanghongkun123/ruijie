package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 校验磁盘名称前缀请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8
 *
 * @author lk
 */
public class DiskNamePrefixWebRequest implements WebRequest {

    @ApiModelProperty(value = "磁盘池ID")
    @Nullable
    private UUID id;

    @NotBlank
    @TextShort
    @TextName
    @ApiModelProperty(value = "磁盘名称前缀", required = true)
    private String diskNamePrefix;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public String getDiskNamePrefix() {
        return diskNamePrefix;
    }

    public void setDiskNamePrefix(String diskNamePrefix) {
        this.diskNamePrefix = diskNamePrefix;
    }
}
