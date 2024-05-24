package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dao;

import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.entity.ImageTemplateAppVersionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * 
 * Description: 镜像应用软件版本配置数据接口
 */
public interface ImageTemplateAppVersionDAO extends SkyEngineJpaRepository<ImageTemplateAppVersionEntity, UUID> {

    /**
     * 根据imageId查询镜像应用软件版本关联表
     * 
     * @param imageId 镜像ID
     * @param appType 应用类型
     * @return 结果
     */
    ImageTemplateAppVersionEntity findByImageIdAndAppType(UUID imageId, AppTypeEnum appType);
}
