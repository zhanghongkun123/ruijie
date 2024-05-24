package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.impl.dto.global.GlobalParameterDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;

import java.util.List;
import java.util.Set;

/**
 * Description: GlobalParameterService
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-29
 *
 * @author hli
 */
public interface GlobalParameterService {

    /**
     * 更新指定参数
     * 
     * @param paramKey paramKey
     * @param value value
     */
    void updateParameter(String paramKey, @Nullable String value);

    /**
     * 查询指定参数的值
     * 
     * @param paramKey paramKey
     * @return java.lang.String
     */
    String findParameter(String paramKey);

    /**
     * 保存参数
     * 
     * @param entity RcoGlobalParameterEntity
     */
    void saveParameter(RcoGlobalParameterEntity entity);

    /**
     * 根据key获取默认值
     *
     * @param paramKey key
     * @return 默认值
     */
    String findDefaultValue(String paramKey);


    /**
     * 批量获取全局配置参数
     * @param paramKeys 参数名集合
     * @return 返回全局参数列表
     */
    List<GlobalParameterDTO> findParameters(@NotNull Set<String> paramKeys);

    /**
     * 是否存在这个key的记录
     *
     * @param paramKey paramKey
     * @return true已存在，false不存在
     */
    boolean existParamKey(String paramKey);
}
