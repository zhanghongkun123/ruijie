package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminFunctionListCustomEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.UUID;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年2月28日
 *
 * @author brq
 */
public interface AdminFunctionListRelationDAO extends SkyEngineJpaRepository<AdminFunctionListCustomEntity, UUID> {

    /**
     * 根据管理员id和功能类型获取列表自定义数据
     * @param adminId 管理员id
     * @param functionType 功能类型
     * @return 返回数据
     */
    AdminFunctionListCustomEntity findByAdminIdAndFunctionType(UUID adminId, String functionType);

}
