package com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.entity.ViewUserLoginRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import java.util.UUID;

/**
 * @author zjy
 * @version 1.0.0
 * @Description 用户登录记录dao
 * @createTime 2021-10-28 15:00:00
 */
public interface ViewUserLoginRecordDAO extends SkyEngineJpaRepository<ViewUserLoginRecordEntity, UUID> {
}
