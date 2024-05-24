package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryAppEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Description: 交付应用视图
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/05 12:03
 *
 * @author coderLee23
 */
public interface ViewUamDeliveryAppDAO
        extends SkyEngineJpaRepository<ViewUamDeliveryAppEntity, UUID>, JpaSpecificationExecutor<ViewUamDeliveryAppEntity> {


    /**
     * 基于交付组id查询应用列表
     * @param groupId groupId
     * @return 应用列表
     */
    List<ViewUamDeliveryAppEntity> findByDeliveryGroupIdOrderByAppName(UUID groupId);


    /**
     * 基于交付组id查询应用列表
     * @param groupIdList groupIdList
     * @return 应用名称列表
     */
    @Query("select distinct o.appName from ViewUamDeliveryAppEntity o where o.deliveryGroupId in ?1 order by o.appName desc")
    List<String> findDistinctAppNameList(List<UUID> groupIdList);
}
