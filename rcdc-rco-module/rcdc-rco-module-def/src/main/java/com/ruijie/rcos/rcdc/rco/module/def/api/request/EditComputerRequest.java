package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 20:42
 *
 * @author ketb
 */
public class EditComputerRequest implements Request {

    @NotNull
    private UUID id;

    @Nullable
    private String alias;

    @NotNull
    private IdLabelEntry terminalGroupName;

    public EditComputerRequest(UUID id, @Nullable String alias, IdLabelEntry terminalGroupName) {
        this.id = id;
        this.alias = alias;
        this.terminalGroupName = terminalGroupName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public IdLabelEntry getTerminalGroupName() {
        return terminalGroupName;
    }

    public void setTerminalGroupName(IdLabelEntry terminalGroupName) {
        this.terminalGroupName = terminalGroupName;
    }
}
