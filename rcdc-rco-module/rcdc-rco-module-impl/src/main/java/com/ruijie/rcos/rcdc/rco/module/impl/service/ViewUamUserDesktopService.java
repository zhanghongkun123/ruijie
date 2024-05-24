package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamUserDesktopEntity;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/01 22:05
 *
 * @author coderLee23
 */
public interface ViewUamUserDesktopService {

    /**
     * 根据云桌面id查找对象
     *
     * @param cloudDesktopId 云桌面id
     * @return entity
     */
    ViewUamUserDesktopEntity findByCloudDesktopId(UUID cloudDesktopId);

}
