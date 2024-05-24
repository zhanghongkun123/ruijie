package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 20:26
 *
 * @author ketb
 */
public class EditComputerWebRequest implements WebRequest {

    @NotNull
    @ApiModelProperty(value = "id", required = true)
    private UUID id;

    @Nullable
    @TextShort
    @ApiModelProperty(value = "别名", required = false)
    private String alias;

    @NotNull
    @ApiModelProperty(value = "终端分组", required = true)
    private IdLabelEntry terminalGroup;


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public IdLabelEntry getTerminalGroup() {
        return terminalGroup;
    }

    public void setTerminalGroup(IdLabelEntry terminalGroup) {
        this.terminalGroup = terminalGroup;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
