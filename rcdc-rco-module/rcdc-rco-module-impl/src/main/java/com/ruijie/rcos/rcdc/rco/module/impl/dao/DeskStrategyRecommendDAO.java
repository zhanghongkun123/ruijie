package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DeskStrategyRecommendEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <br>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2019/4/3  <br>
 *
 * @author yyz
 */
public interface DeskStrategyRecommendDAO extends SkyEngineJpaRepository<DeskStrategyRecommendEntity, UUID> {

    /**
     * 更新 isShoe 字段
     *
     * @param isShoe  是否显示
     * @param version version
     * @param id      修改的id
     */
    @Transactional
    @Modifying
    @Query(value = "update  DeskStrategyRecommendEntity set isShow = ?1 , version = version + 1 where id = ?3 and version = ?2")
    void updateIsShowById(Boolean isShoe, int version, UUID id);


}
