package com.ruijie.rcos.rcdc.rco.module.impl.desktop.service;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopImageUpdateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 桌面与镜像接口服务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-04-10
 *
 * @author chen zj
 */
public interface DesktopImageService {
    /**
     * 编辑云桌面镜像
     *
     * @param cbbDesktopImageUpdateDTO 桌面ID、镜像id
     * @throws BusinessException 异常
     */
    void updateDesktopImage(CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO) throws BusinessException;
}
