package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * Description: 编辑终端信息请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 * 
 * @author nt
 */
public class EditTerminalWebRequest implements WebRequest {

    @NotBlank
    @ApiModelProperty(value = "终端Id", required = true)
    private String id;

    @NotBlank
    @TextShort
    @TextName
    @ApiModelProperty(value = "终端名称", required = true)
    private String terminalName;

    @NotNull
    @ApiModelProperty(value = "终端分组", required = true)
    private IdLabelEntry terminalGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public IdLabelEntry getTerminalGroup() {
        return terminalGroup;
    }

    public void setTerminalGroup(IdLabelEntry terminalGroup) {
        this.terminalGroup = terminalGroup;
    }

    @Override
    public String toString() {
        return "EditTerminalWebRequest{" + "id='" + id + '\'' + ", terminalName='" + terminalName + '\'' + ", terminalGroup=" + terminalGroup + '}';
    }
}
