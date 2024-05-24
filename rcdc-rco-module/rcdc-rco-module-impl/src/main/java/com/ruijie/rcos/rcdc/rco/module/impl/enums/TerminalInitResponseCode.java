package com.ruijie.rcos.rcdc.rco.module.impl.enums;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/17 19:04
 *
 * @author conghaifeng
 */
public enum TerminalInitResponseCode {

    /** 终端云桌面正在运行，不可初始化 */
    DESKTOP_ON_RUNNING(-1),

    /** 通知shine前端失败，不可初始化 */
    NOTIFY_SHINE_WEB_FAIL(-2),

    /** 终端正在初始化 */
    TERMINAL_ON_INIT(-5),

    /** 终端上正在清空数据盘 */
    TERMINAL_ON_CLEAR_DATA_DISK(-6),

    /** 终端上正在还原云桌面 */
    TERMINAL_ON_RESTORE_DESKTOP(-7),

    /** 终端上正在变更镜像 **/
    TERMINAL_ON_IMAGE_REPLACEMENT(-8),

    /**
     * 终端升级中
     */
    TERMINAL_ON_UPGRADE(-9),

    /**
     * 终端上正在编辑或提取镜像
     */
    TERMINAL_ON_LOCAL_EDIT_IMAGE(-11),


    /**
     *使用4.x迁移过来的镜像的终端上正在还原云桌面
     */
    TERMINAL_ON_RESTORE_DESKTOP_FOR_4X25X(-10),

    /**
     * 终端上正在检查和修复镜像
     */
    TERMINAL_ON_REPAIR_IMAGE(-13);

    /** shine返回状态码 */
    private int code;

    TerminalInitResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
