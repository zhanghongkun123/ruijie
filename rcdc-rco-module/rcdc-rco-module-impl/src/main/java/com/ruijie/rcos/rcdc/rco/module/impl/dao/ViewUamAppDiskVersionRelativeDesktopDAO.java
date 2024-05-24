package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppDiskVersionRelativeDesktopEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppSoftwarePackageVersionState;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年03月31日
 *
 * @author xgx
 */
public interface ViewUamAppDiskVersionRelativeDesktopDAO extends SkyEngineJpaRepository<ViewUamAppDiskVersionRelativeDesktopEntity, UUID>,
        JpaSpecificationExecutor<ViewUamAppDiskVersionRelativeDesktopEntity>, PageQueryDAO<ViewUamAppDiskVersionRelativeDesktopEntity> {
    /**
     * 获取应用磁盘指定状态版本关联桌面列表
     * 
     * @param appId 应用磁盘ID
     * @param appVersionState 应用版本状态
     * @return 应用磁盘版本关联桌面列表
     */
    List<ViewUamAppDiskVersionRelativeDesktopEntity> findByAppIdAndAppVersionState(UUID appId, AppSoftwarePackageVersionState appVersionState);

    /**
     * 是否存在应用磁盘指定状态版本关联桌面
     * 
     * @param appId 应用磁盘ID
     * @param appVersionState 应用版本状态
     * @return 是否存在应用磁盘指定状态版本关联桌面
     */
    boolean existsByAppIdAndAppVersionState(UUID appId, AppSoftwarePackageVersionState appVersionState);

    /**
     * 获取应用磁盘指定状态版本关联桌面列表
     *
     * @param deskId 应用磁盘ID
     * @return 应用磁盘版本关联桌面列表
     */
    List<ViewUamAppDiskVersionRelativeDesktopEntity> findByDeskId(UUID deskId);
}
