package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.28
 *
 * @author linhj
 */
public interface VOIDownloadLockCode {

    /** 镜像模板不存在 **/
    int RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_NOT_EXIST_CODE = 1;

    /** 镜像模板当前状态无法锁定 **/
    int RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_CAN_NOT_LOCK_CODE = 2;

    /** 镜像非终端本地编辑中 **/
    int RCDC_CLOUDDESKTOP_IMAGE_LOCAL_EDIT_STATUS_ERROR_CODE = 3;

    /** 镜像不存在 **/
    int RCDC_CLOUDDESKTOP_IMAGE_NOT_EXISTS_CODE = 4;
}
