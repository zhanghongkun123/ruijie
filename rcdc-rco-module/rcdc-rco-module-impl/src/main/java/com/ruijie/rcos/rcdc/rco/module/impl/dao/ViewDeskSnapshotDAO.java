package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.common.query.RcdcJpaRepository;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDeskSnapshotEntity;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月26日
 *
 * @author 徐国祥
 */
public interface ViewDeskSnapshotDAO extends RcdcJpaRepository<ViewDeskSnapshotEntity, UUID>, PageQueryDAO<ViewDeskSnapshotEntity> {
}
