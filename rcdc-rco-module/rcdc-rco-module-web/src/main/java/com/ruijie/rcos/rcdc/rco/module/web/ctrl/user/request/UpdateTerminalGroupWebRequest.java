package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 更新终端分组web请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/09
 *
 * @author nt
 */
public class UpdateTerminalGroupWebRequest implements WebRequest {

    @ApiModelProperty(value = "终端id", required = true)
    @NotEmpty
    @Size(min = 1)
    private String[] idArr;

    @ApiModelProperty(value = "终端组id", required = true)
    @NotNull
    private UUID terminalGroupId;

    public String[] getIdArr() {
        return idArr;
    }

    public void setIdArr(String[] idArr) {
        this.idArr = idArr;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }
    
}
