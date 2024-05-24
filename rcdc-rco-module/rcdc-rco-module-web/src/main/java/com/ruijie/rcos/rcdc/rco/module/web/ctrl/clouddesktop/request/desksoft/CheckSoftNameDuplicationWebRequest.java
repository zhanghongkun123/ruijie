package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desksoft;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 检查命名唯一性
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月11日
 * 
 * @author Ghang
 */
public class CheckSoftNameDuplicationWebRequest implements WebRequest {

    @NotBlank
    private String name;

    @NotNull
    @Range(min = "1")
    private Long fileSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
