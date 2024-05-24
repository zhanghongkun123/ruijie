package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.SystemBusinessMappingEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.06
 *
 * @author LinHJ
 */
public interface SystemBusinessMappingDAO
        extends SkyEngineJpaRepository<SystemBusinessMappingEntity, UUID>, PageQueryDAO<SystemBusinessMappingEntity> {

    /**
     * 根据业务标识查询目标唯一
     *
     * @param systemType 外系统类型
     * @param businessType 业务类型
     * @param srcId 源业务标识
     * @return 实体
     */
    SystemBusinessMappingEntity findBySystemTypeAndBusinessTypeAndSrcId(String systemType, String businessType, String srcId);

    /**
     * 根据业务标识查询目标唯一
     *
     * @param systemType 外系统类型
     * @param businessType 业务类型
     * @param destId 目标业务标识
     * @return 实体
     */
    List<SystemBusinessMappingEntity> findBySystemTypeAndBusinessTypeAndDestId(String systemType, String businessType, String destId);

    /**
     * 返回某个业务的所有集合
     *
     * @param systemType 外系统类型
     * @param businessType 业务类型
     * @return 实体
     */
    List<SystemBusinessMappingEntity> findBySystemTypeAndBusinessType(String systemType, String businessType);

    /**
     * 更新业务标识
     *
     * @param systemType 外系统类型
     * @param businessType 业务类型
     * @param srcId 源业务标识
     * @param destId 目标业务标识
     * @param context 自定义内容
     */
    @Transactional
    @Modifying
    @Query(value = "update SystemBusinessMappingEntity set updateDate = current_timestamp, destId = ?4 , context = ?5, "
            + "version = version + 1 where systemType = ?1 and businessType =?2 and srcId = ?3 and version = version")
    void updateDestIdBySrcId(String systemType, String businessType, String srcId, String destId, String context);

    /**
     * * 查询超期的操作日志数据
     *
     * @param updateDate 更新时间
     * @return 数量
     */
    Integer countByUpdateDateLessThan(Date updateDate);

    /**
     * * 删除超期的操作日志数据
     *
     * @param updateDate 更新时间
     */
    @Transactional
    @Modifying
    void deleteByUpdateDateLessThan(Date updateDate);

    /**
     * 根据资源id查询信息
     * 
     * @param srcId 资源id
     * @return 是否存在
     */
    boolean existsBySrcId(String srcId);

    /**
     * 删除4升5终端记录
     *
     * @param srcId 资源id
     */
    @Transactional
    @Modifying
    void deleteBySrcId(String srcId);
}
