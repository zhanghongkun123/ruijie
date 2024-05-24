package com.ruijie.rcos.rcdc.rco.module.impl.dao;


import com.ruijie.rcos.rcdc.rco.module.impl.entity.DeskFaultInfoEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/8 17:20
 *
 * @author ketb
 */
public interface DeskFaultInfoDAO extends SkyEngineJpaRepository<DeskFaultInfoEntity, UUID> {

    /**
     * 根据mac查询报障信息
     *
     * @param mac 桌面mac
     * @return 结果
     */
    DeskFaultInfoEntity findByMac(String mac);

    /**
     * 根据id查询报障信息
     *
     * @param deskId 桌面id
     * @return 结果
     */
    DeskFaultInfoEntity findByDeskId(UUID deskId);

    /**
     * 根据指定的桌面id，查询报障信息
     *
     * @param uuidList 要查询的桌面id list
     * @return 结果
     */
    @Query("from DeskFaultInfoEntity where deskId in (:uuidList)")
    List<DeskFaultInfoEntity> queryDesignatedDesk(@Param("uuidList") List<UUID> uuidList);

    /**
     * 根据云桌面ID删除
     *
     * @param deskId 云桌面ID
     * @return 删除条数
     */
    @Modifying
    @Transactional
    int deleteByDeskId(UUID deskId);

    /**
     * 修改云桌面MAC地址
     *
     * @param deskId 云桌面ID
     * @param mac MAC地址
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update DeskFaultInfoEntity entity set entity.mac = :mac, entity.version = entity.version + 1 where entity.deskId = :deskId")
    void updateMac(@Param("deskId") UUID deskId, @Param("mac") String mac);
}
