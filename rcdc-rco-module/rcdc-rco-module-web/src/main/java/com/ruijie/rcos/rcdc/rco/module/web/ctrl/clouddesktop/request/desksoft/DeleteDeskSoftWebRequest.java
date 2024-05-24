package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desksoft;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 删除安装包web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月14日
 * 
 * @author Ghang
 */
public class DeleteDeskSoftWebRequest implements WebRequest {

    @ApiModelProperty(value = "安装包ID数组")
    @NotEmpty
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

}
