package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 20:19
 *
 * @author ketb
 */
public class ComputerDetailVO {
    private String id;

    private String name;

    private String alias;

    private String deskName;

    private String deskPoolName;

    /**
     * 终端分组
     */
    private IdLabelEntry terminalGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskPoolName() {
        return deskPoolName;
    }

    public void setDeskPoolName(String deskPoolName) {
        this.deskPoolName = deskPoolName;
    }
}
