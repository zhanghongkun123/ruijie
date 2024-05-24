package com.ruijie.rcos.rcdc.rco.module.impl.datasync.dao;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.impl.datasync.entity.DataSyncLogEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/25 16:18
 *
 * @author coderLee23
 */
public interface DataSyncLogDAO extends SkyEngineJpaRepository<DataSyncLogEntity, UUID>, JpaSpecificationExecutor<DataSyncLogEntity> {


    /**
     ** 删除过期的用户和用户组日志
     * 
     * @param expireTime 过期时间
     */
    @Transactional
    @Modifying
    void deleteByCreateTimeLessThan(Date expireTime);

    /**
     * 根据偏移量
     *
     * @param offset 偏移量
     * @return DataSyncLogEntity
     */
    @Query(value = "select * from t_rco_data_sync_log order by create_time desc limit 1 offset ?1", nativeQuery = true)
    DataSyncLogEntity findOneByOffset(Long offset);

}
