package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetServerMappingEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description: 机柜关联服务器持久化接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public interface CabinetServerMappingDAO extends SkyEngineJpaRepository<CabinetServerMappingEntity, UUID> {

    /**
     * 基于机柜Id删除指定机柜
     *
     * @param cabinetId 机柜Id
     */
    @Modifying
    @Transactional
    void deleteByCabinetId(UUID cabinetId);

    /**
     * 获得与指定机柜Id关联的服务器信息
     *
     * @param cabinetId 机柜Id
     * @return 响应
     */
    @Query("from CabinetServerMappingEntity where cabinetId = ?1 order by cabinetLocationBegin")
    @Transactional
    List<CabinetServerMappingEntity> findAllByCabinetId(UUID cabinetId);

    /**
     * 根据服务器id列表删除机柜和服务器对应关系数据
     * @param idList 服务器id列表
     */
    @Transactional
    void deleteByServerIdIn(List<UUID> idList);

    /**
     * 根据服务器id获取关联关系
     * @param serverId 服务器id
     * @return CabinetServerMappingEntity 关联关系对象
     */
    CabinetServerMappingEntity getByServerId(UUID serverId);

    /**
     * 根据服务器id批量获取关联关系
     * @param idList 服务器id列表
     * @return 多个关联关系
     */
    List<CabinetServerMappingEntity> getByServerIdIn(List<UUID> idList);
}