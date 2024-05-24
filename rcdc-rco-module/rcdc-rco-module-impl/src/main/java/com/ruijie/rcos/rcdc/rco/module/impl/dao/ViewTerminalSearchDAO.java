package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalSearchEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 *
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月8日
 *
 * @author jarman
 */
public interface ViewTerminalSearchDAO extends SkyEngineJpaRepository<ViewTerminalSearchEntity, UUID> {

    /**
     * 根据groupIdList查询终端信息视图
     * 
     * @param groupIdList 终端组ID列表
     * @return 结果
     */
    @Query(value = "select * from v_cbb_terminal_search s where s.group_id in (?1)", nativeQuery = true)
    List<ViewTerminalSearchEntity> findByGroupIdIn(List<UUID> groupIdList);
}
