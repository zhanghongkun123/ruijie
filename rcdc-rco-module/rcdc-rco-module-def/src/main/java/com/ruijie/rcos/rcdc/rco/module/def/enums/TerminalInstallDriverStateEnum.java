package com.ruijie.rcos.rcdc.rco.module.def.enums;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月07日
 *
 * @author xiejian
 */
public enum TerminalInstallDriverStateEnum {

    /**
     * 可以进行驱动安装
     */
    SUCCESS,

    /**
     * 终端编辑镜像中
     */
    LOCAL_EDIT_IMAGE,

    /**
     * 获取失败
     */
    ERROR,

    /**
     * 远程编辑镜像中
     */
    REMOTE_EDIT_IMAGE,

    /**
     * 云桌面运行中
     */
    DESK_RUNNING,

    /**
     * 云桌面启动中
     */
    DESK_STARTING;

}
