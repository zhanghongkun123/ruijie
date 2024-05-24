package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryObjectEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 交付对象视图
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/05 12:03
 *
 * @author coderLee23
 */
public interface ViewUamDeliveryObjectDAO
        extends SkyEngineJpaRepository<ViewUamDeliveryObjectEntity, UUID>, JpaSpecificationExecutor<ViewUamDeliveryObjectEntity> {


    /**
     * 基于一个桌面ID查询
     *
     * @param deskId deskId
     * @return List<ViewUamDeliveryObjectEntity>
     */
    List<ViewUamDeliveryObjectEntity> findByCloudDesktopId(UUID deskId);


    /**
     * 基于一个桌面ID查询
     *
     * @param deskId deskId
     * @param appDeliveryType AppDeliveryTypeEnum
     * @return List<ViewUamDeliveryObjectEntity>
     */
    List<ViewUamDeliveryObjectEntity> findByCloudDesktopIdAndAppDeliveryType(UUID deskId, AppDeliveryTypeEnum appDeliveryType);

    /**
     * 根据应用交付类型 判定云桌面列表是否被使用
     *
     * @param appDeliveryType 应用交付类型
     * @param cloudDesktopIdList 判定云桌面列表是否被使用
     * @return true or false
     */
    Boolean existsByAppDeliveryTypeAndCloudDesktopIdIn(AppDeliveryTypeEnum appDeliveryType, List<UUID> cloudDesktopIdList);

}
