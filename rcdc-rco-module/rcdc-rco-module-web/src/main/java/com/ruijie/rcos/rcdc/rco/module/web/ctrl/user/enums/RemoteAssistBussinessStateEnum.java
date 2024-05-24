package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/1 11:44
 *
 * @author ketb
 */
public enum RemoteAssistBussinessStateEnum {

    NORMAL,

    /**
     * 驱动安装
     */
    IDV_INSTALL_DRIVE;

    /**
     * 根据传入的驱动安装状态获取状态
     * @param isInstallDriver 驱动安装状态
     * @return 镜像业务状态
     */
    public static RemoteAssistBussinessStateEnum convertRemoteState(boolean isInstallDriver) {
        if (isInstallDriver) {
            return IDV_INSTALL_DRIVE;
        }

        return NORMAL;
    }
}
