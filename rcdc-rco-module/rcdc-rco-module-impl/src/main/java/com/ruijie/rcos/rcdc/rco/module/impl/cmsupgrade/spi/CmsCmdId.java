package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.spi;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public interface CmsCmdId {

    /** 镜像安装或升级完CMS，发送版本号事件给RCDC */
    String RCO_CMD_ID_GET_CM_LAUNCHER_VERSION = "6001";
    
    /** CMS独立升级后，发送版本更新事件给RCDC */
    String RCO_CMD_ID_NOTIFY_CLOUD_DESK_REMOUNT = "21";
}
