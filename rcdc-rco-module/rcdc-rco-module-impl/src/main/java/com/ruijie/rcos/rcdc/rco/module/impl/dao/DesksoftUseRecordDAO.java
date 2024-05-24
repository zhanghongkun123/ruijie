package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesksoftUseRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 软件使用记录DAO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月15日
 *
 * @author jarman
 */

public interface DesksoftUseRecordDAO extends SkyEngineJpaRepository<DesksoftUseRecordEntity, UUID> {

    /**
     * 查找同一天相同软件记录数据
     *
     * @param name       软件名
     * @param updateTime 更新时间
     * @return 查询结果
     */
    List<DesksoftUseRecordEntity> findByNameAndUpdateTimeEquals(String name, Long updateTime);

    /**
     * 操作次数+1
     * @param operateTimes 操作次数
     * @param name 软件名
     * @param softVersion softVersion
     * @param version version
     * @param updateTime 更新时间
     * @return 返回更新数
     */
    @Transactional
    @Modifying
    @Query("update DesksoftUseRecordEntity set operateTimes = :operateTimes, version = version + 1 where name = :name " +
            "and softVersion = :softVersion and version = :version and updateTime = :updateTime")
    int increaseOperateTimes(@Param("operateTimes") Integer operateTimes,
                             @Param("name") String name,
                             @Param("softVersion") String softVersion,
                             @Param("version") Integer version,
                             @Param("updateTime") Long updateTime
    );

    /**
     * 查找比更新时间小的记录
     *
     * @param updateTime 更新时间
     * @return 查询结果
     */
    List<DesksoftUseRecordEntity> findByUpdateTimeLessThanEqual(Long updateTime);
}