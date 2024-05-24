package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/22 11:02
 *
 * @author ketb
 */
public class DeleteImageExportFileWebRequest implements WebRequest {

    @NotEmpty
    @ApiModelProperty(value = "导出文件任务id", required = true)
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
