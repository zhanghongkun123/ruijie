package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: CheckUploadRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/21
 *
 * @author chenl
 */
public class CheckUploadRequest extends DefaultWebRequest {

    @ApiModelProperty(value = "文件的大小", required = true)
    @NotNull
    @Range(min = "1")
    private Long fileSize;


    @NotNull
    private String fileName;


    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
