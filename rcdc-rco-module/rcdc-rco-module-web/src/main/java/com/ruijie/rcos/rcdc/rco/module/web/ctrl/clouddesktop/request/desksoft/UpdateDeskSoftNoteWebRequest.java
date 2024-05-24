package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desksoft;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 更新安装包描述
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月14日
 * 
 * @author Ghang
 */
public class UpdateDeskSoftNoteWebRequest implements WebRequest {

    @ApiModelProperty(value = "ID")
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "描述")
    @TextMedium
    private String note;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
