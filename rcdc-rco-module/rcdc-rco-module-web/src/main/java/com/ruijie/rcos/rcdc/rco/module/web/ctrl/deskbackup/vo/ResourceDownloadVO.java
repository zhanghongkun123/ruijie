package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo;

import com.ruijie.rcos.rcdc.backup.module.def.enums.CbbResourceCompressStateEnums;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: 备份文件下载
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年05月09日
 *
 * @author qiuzy
 */
@ApiModel("资源下载")
public class ResourceDownloadVO implements Serializable {

    /**
     * 资源压缩状态
     */
    @ApiModelProperty(value = "资源压缩状态")
    @NotNull
    private CbbResourceCompressStateEnums state;

    /**
     * 只有状态为已完成时才有值
     */
    @ApiModelProperty(value = "文件名称")
    @Nullable
    private String fileName;

    /**
     * 详情ID
     */
    @ApiModelProperty(value = "详情ID")
    @NotNull
    private UUID id;

    public ResourceDownloadVO() {
    }

    public ResourceDownloadVO(CbbResourceCompressStateEnums state, @Nullable String fileName) {
        this.state = state;
        this.fileName = fileName;
    }

    public CbbResourceCompressStateEnums getState() {
        return state;
    }

    public void setState(CbbResourceCompressStateEnums state) {
        this.state = state;
    }

    @Nullable
    public String getFileName() {
        return fileName;
    }

    public void setFileName(@Nullable String fileName) {
        this.fileName = fileName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
