package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopGuestToolEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

/**
 * 云桌面GT信息
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年8月30日
 *
 * @author linrenjian
 */
public interface ViewDesktopGuestToolDAO
        extends SkyEngineJpaRepository<ViewUserDesktopGuestToolEntity, UUID>, PageQueryDAO<ViewUserDesktopGuestToolEntity> {

    /**
     * 根据桌面状态 桌面类型 ， 查询桌面列表
     *
     * @param deskState 桌面状态
     * @param desktopType 桌面类型
     * @return 返回桌面列表
     */
    List<ViewUserDesktopGuestToolEntity> findAllByDeskStateAndCbbImageType(String deskState, String desktopType);
}
