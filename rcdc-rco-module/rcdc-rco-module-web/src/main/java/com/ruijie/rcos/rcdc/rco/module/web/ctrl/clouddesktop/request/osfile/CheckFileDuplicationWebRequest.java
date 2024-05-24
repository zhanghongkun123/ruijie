package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.osfile;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: CheckSoftNameDuplicationWebRequest Description
 * Copyright: Copyright (c) 2017
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/04/28
 *
 * @author chixin
 */
public class CheckFileDuplicationWebRequest implements WebRequest {

    @NotNull
    private String imageFileName;

    @NotNull
    @Range(min = "1")
    private Long fileSize;

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
