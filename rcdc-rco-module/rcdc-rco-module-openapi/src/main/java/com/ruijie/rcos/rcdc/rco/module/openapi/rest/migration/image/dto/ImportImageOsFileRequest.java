package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.common.ServerBusinessName;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
@ServerBusinessName("镜像文件导入")
public class ImportImageOsFileRequest {

    @NotBlank
    @Size(max = 64)
    private String fileName;

    @NotBlank
    @Size(max = 64)
    private String fileMD5;

    @NotNull
    private Long fileSize;

    @NotNull
    private CbbOsType osType;

    @Nullable
    @TextMedium
    private String description;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }
}
