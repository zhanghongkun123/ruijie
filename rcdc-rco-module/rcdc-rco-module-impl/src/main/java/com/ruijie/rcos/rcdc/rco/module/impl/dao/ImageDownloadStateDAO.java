package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageDownloadStateEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: 镜像模板下载状态DAO类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/28 14:06
 *
 * @author yxq
 */
public interface ImageDownloadStateDAO extends SkyEngineJpaRepository<ImageDownloadStateEntity, UUID> {

    /**
     * 根据终端ID查找
     *
     * @param terminalId 终端ID
     * @return 实体类
     */
    ImageDownloadStateEntity findByTerminalId(String terminalId);

    /**
     * 根据终端ID删除记录
     *
     * @param terminalId 终端ID
     */
    @Transactional
    @Modifying
    void deleteByTerminalId(String terminalId);

    /**
     * 查询镜像是否正在下载
     *
     * @param imageTemplateId 镜像ID
     * @param start           下载状态
     * @return 镜像列表
     */
    List<ImageDownloadStateEntity> findByImageIdAndDownloadState(UUID imageTemplateId, DownloadStateEnum start);
}
