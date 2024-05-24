package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums;

/**
 * Description: 远程协助窗口标题枚举
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/10
 *
 * @author chenjiehui
 */
public enum RemoteAssistWindowTitleEnums {
    /**
     * 远程协助
     */
    REMOTE_ASSIST("远程协助"),

    /**
     * 编辑镜像模板
     */
    EDIT_IMAGE("编辑镜像"),

    /**
     * 编辑软件模板
     */
    EDIT_APP("制作应用磁盘");

    private String title;

    RemoteAssistWindowTitleEnums(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
