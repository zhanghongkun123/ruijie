package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.entity.CloudDeskAppConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * 
 * Description: 云桌面应用ISO挂载信息数据接口
 */
public interface CloudDeskAppConfigDAO extends SkyEngineJpaRepository<CloudDeskAppConfigEntity, UUID> {

    /**
     * 根据deskId查询云桌面应用ISO挂载信息关联表
     * 
     * @param deskId 云桌面ID
     * @param appType 应用类型
     * @return 结果
     */
    CloudDeskAppConfigEntity findByDeskIdAndAppType(UUID deskId, AppTypeEnum appType);

    /**
     * 根据deskId删除云桌面应用ISO挂载信息关联表
     * 
     * @param deskId 云桌面ID
     * @param appType 应用类型
     */
    @Transactional
    void deleteByDeskIdAndAppType(UUID deskId, AppTypeEnum appType);

    /**
     * 根据桌面id和应用类型更新iso version
     * 
     * @param deskId 云桌面id
     * @param appType 应用类型
     * @param isoVersion iso版本号
     */
    @Modifying
    @Transactional
    @Query(value = "update CloudDeskAppConfigEntity set isoVersion=?3,version=version+1 where deskId=?1 and appType=?2")
    void updateByDeskId(UUID deskId, AppTypeEnum appType, String isoVersion);
}
