package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums;

/**
 * Description: 远程协助业务枚举
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/10
 *
 * @author chenjiehui
 */
public enum  RemoteAssistComponentEnum {

    /**
     * 远程协助
     */
    REMOTE_ASSIST("remoteAssist"),

    /**
     * 编辑镜像
     */
    EDIT_IMAGE("editImage");


    private String component;

    RemoteAssistComponentEnum(String component) {
        this.component = component;
    }

    public String getComponent() {
        return component;
    }

}