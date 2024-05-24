package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * 全局参数的DAO
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年8月29日 <br>
 * 
 * @author hli
 */
public interface RcoGlobalParameterDAO extends SkyEngineJpaRepository<RcoGlobalParameterEntity, UUID> {

    /**
     * 根据KEY查询值
     * 
     * @param paramKey paramKey
     * @return UUID
     */
    @Query("select paramValue from RcoGlobalParameterEntity where paramKey=:paramKey")
    String findValueByParamKey(@Param("paramKey") String paramKey);

    /**
     * 更新指定参数key的value
     * 
     * @param paramKey paramKey
     * @param paramValue paramValue
     */
    @Modifying
    @Transactional
    @Query(value = "update RcoGlobalParameterEntity set paramValue=:paramValue,version=version+1 where paramKey=:paramKey")
    void updateValueByParamKey(@Param("paramKey") String paramKey, @Param("paramValue") String paramValue);

    /**
     * 根据paramKey，RcoGlobalParameterEntity
     *
     * @param   paramKey paramKey
     * @return  RcoGlobalParameterEntity
     */
    RcoGlobalParameterEntity findByParamKey(@Param("paramKey") String paramKey);

    /**
     * 根据key获取默认值
     *
     * @param paramKey key
     * @return 默认值
     */
    @Query("select defaultValue from RcoGlobalParameterEntity where paramKey=:paramKey ")
    String obtainDefaultValueByParamKey(@Param("paramKey") String paramKey);

    /**
     * 批量获取全局配置参数
     * @param paramKeys 参数名集合
     * @return 全局参数配置列表
     */
    List<RcoGlobalParameterEntity> findByParamKeyIn(Iterable<String> paramKeys);

    /**
     * 是否存在这个key的记录
     *
     * @param paramKey paramKey
     * @return true已存在，false不存在
     */
    boolean existsByParamKey(String paramKey);
}
