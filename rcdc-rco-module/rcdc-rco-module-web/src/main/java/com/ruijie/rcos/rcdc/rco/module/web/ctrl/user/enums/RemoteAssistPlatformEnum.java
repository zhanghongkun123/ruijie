package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums;

/**
 * Description: 远程协助平台枚举
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/10
 *
 * @author chenjiehui
 */
public enum  RemoteAssistPlatformEnum {
    /**
     * 云桌面
     */
    CLOUD_DESK("cloudDesk"),

    /**
     * PC
     */
    PC("pc");


    private String platform;

    RemoteAssistPlatformEnum(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

}
