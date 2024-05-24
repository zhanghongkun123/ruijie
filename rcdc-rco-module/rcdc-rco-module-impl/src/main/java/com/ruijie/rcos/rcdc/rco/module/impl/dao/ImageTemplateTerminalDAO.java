package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageTemplateTerminalEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time:  2019/12/3
 *
 * @author songxiang
 */
public interface ImageTemplateTerminalDAO extends SkyEngineJpaRepository<ImageTemplateTerminalEntity, UUID> {

    /**
     * 获取镜像编辑过的终端列表
     * @param imageId 镜像iD
     * @return 镜像关联终端的列表
     */
    List<ImageTemplateTerminalEntity> findByImageId(UUID imageId);
}
