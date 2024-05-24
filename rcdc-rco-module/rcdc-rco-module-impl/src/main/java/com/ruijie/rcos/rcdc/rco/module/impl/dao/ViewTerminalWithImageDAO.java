package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalWithImageEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 终端关于镜像信息视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月12日
 *
 * @author ypp
 */
public interface ViewTerminalWithImageDAO extends SkyEngineJpaRepository<ViewTerminalWithImageEntity, UUID> {


    /**
     *  查找镜像终端信息
     *
     * @param imageId      镜像id
     * @param terminalId   终端id
     * @return 镜像终端信息
     */
    ViewTerminalWithImageEntity findByImageIdAndTerminalId(String imageId, String terminalId);
}
