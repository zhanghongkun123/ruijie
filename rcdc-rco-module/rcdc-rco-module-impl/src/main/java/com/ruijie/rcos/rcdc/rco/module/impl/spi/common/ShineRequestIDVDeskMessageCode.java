package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/23
 *
 * @author chen zj
 */
public interface ShineRequestIDVDeskMessageCode extends CommonMessageCode {

    /** 桌面关联的镜像不存在 */
    int DESK_RELEASE_IMAGE_NOT_FOUND = 1;

    /** 桌面关联的终端不存在 */
    int DESK_RELEASE_TERMINAL_NOT_FOUND = 2;

    /**
     * 镜像驱动未安装
     */
    int DESK_RELEASE_IMAGE_DRIVER_NOT_INSTALL = 3;

    /**
     * 云桌面不存在
     */
    int DESK_NOT_EXISTS = 4;

    /**
     * 镜像种子文件不存在
     */
    int DESK_RELEASE_IMAGE_TORRENT_NOT_FOUND = 5;

    /**
     * 镜像未就绪（非AVAILABLE状态）
     */
    int DESK_RELEASE_IMAGE_NOT_AVAILABLE = 6;

    /**
     * 当前终端CPU型号不支持当前操作系统
     */
    int DESK_RELEASE_IMAGE_UN_SUPPORT_WITH_TERMINAL_CPU = 7;

    /**
     * 镜像种子文件不存在
     */
    int DESK_RELEASE_IMAGE_NOT_FIND_BACKING_FILE = 9;

    /**
     * 终端不支持该类型镜像
     */
    int TERMINAL_NOT_SUPPORT_OS_TYPE = 10;


}
