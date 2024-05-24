package com.ruijie.rcos.rcdc.rco.module.impl.desktop.service;

import java.util.UUID;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-01-25
 *
 * @author linke
 */
public interface DesktopUpdateService {


    /**
     * 更新云桌面信息
     *
     * @param desktopId 云桌面ID
     * @throws BusinessException ex
     */
    void updateNotRecoverableVDIConfigAsync(UUID desktopId);

    /**
     * 异步操作：所有桌面。
     * 普通桌面比对云桌面策略与当前虚机的配置，更新云桌面信息；
     * 池桌面需要比对相应的网络策略，软件策略，云桌面策略，镜像模板。
     *
     * @param desktopId 云桌面ID
     */
    void updateVDIConfigAsync(UUID desktopId);

    /**
     * 同步操作：所有桌面。
     * 普通桌面比对云桌面策略与当前虚机的配置，更新云桌面信息；
     * 池桌面需要比对相应的网络策略，软件策略，云桌面策略，镜像模板。
     *
     * @param desktopId 云桌面ID
     */
    void updateVDIConfigSync(UUID desktopId);
}
