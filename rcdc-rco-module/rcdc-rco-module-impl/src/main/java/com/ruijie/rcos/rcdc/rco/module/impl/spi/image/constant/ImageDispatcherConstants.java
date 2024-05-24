package com.ruijie.rcos.rcdc.rco.module.impl.spi.image.constant;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Date: 2021.04.25
 *
 * @author linhj
 */
public interface ImageDispatcherConstants {

    /**
     * 终端获取镜像列表
     */
    String PERMISSION_IMAGE_LIST = "permission_image_list";

    /**
     * 终端获取镜像列表
     */
    String TERMINAL_EDIT_IMAGE_LIST = "terminal_edit_image_list";

    /**
     * 终端获取镜像信息
     */
    String PERMISSION_IMAGE_INFO = "permission_image_info";

    /**
     * 对镜像设置下载锁定
     */
    String IMAGE_DOWNLOAD_LOCK = "image_download_lock";

    /**
     * 对镜像设置取消下载锁定
     */
    String IMAGE_DOWNLOAD_UNLOCK = "image_download_unlock";

    /**
     * 终端部署时获取镜像列表
     */
    String TERMINAL_DEPLOY_QUERY_IMAGE_LIST = "terminal_deploy_query_image_list";

    /**
     * 终端部署校验镜像状态
     */
    String TERMINAL_DEPLOY_VALIDATE_IMAGE_STATE = "terminal_deploy_validate_image_state";

    /**
     * 获取终端虚拟运行策略
     */
    String GET_TERMINAL_VIRTUALIZATION_STRATEGY = "get_terminal_virtualization_strategy";
}
