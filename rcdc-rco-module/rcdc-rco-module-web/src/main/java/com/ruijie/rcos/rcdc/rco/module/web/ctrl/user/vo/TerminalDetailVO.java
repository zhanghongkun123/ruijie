package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: 终端编辑详情信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 *
 * @author nt
 */
public class TerminalDetailVO {

    /**
     * 终端id
     */
    private String id;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端分组
     */
    private IdLabelEntry terminalGroup;

    /**
     * idv终端模式
     */
    private IdvTerminalModeEnums idvTerminalMode;

    /**
     * 是否开启访客登录
     */
    private Boolean enableVisitorLogin;

    /**
     * 是否开启自动登录云桌面
     */
    private Boolean enableAutoLogin;

    /**
     * 绑定用户
     */
    private IdLabelEntry bindUser;

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

    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }

    public Boolean getEnableVisitorLogin() {
        return enableVisitorLogin;
    }

    public void setEnableVisitorLogin(Boolean enableVisitorLogin) {
        this.enableVisitorLogin = enableVisitorLogin;
    }

    public Boolean getEnableAutoLogin() {
        return enableAutoLogin;
    }

    public void setEnableAutoLogin(Boolean enableAutoLogin) {
        this.enableAutoLogin = enableAutoLogin;
    }

    public IdLabelEntry getBindUser() {
        return bindUser;
    }

    public void setBindUser(IdLabelEntry bindUser) {
        this.bindUser = bindUser;
    }
}
