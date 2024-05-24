package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryObjectEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 18:55
 *
 * @author coderLee23
 */
public interface ViewUamDeliveryObjectService {

    /**
     *
     * 根据交付组id查询、或+云桌面名称模糊查找
     * 
     * @param searchDeliveryObjectDTO 交付应用查询条件
     * @param pageable 分页参数
     * @return Page<ViewUamDeliveryObjectEntity>
     */
    Page<ViewUamDeliveryObjectEntity> pageUamDeliveryObject(SearchDeliveryObjectDTO searchDeliveryObjectDTO, Pageable pageable);


    /**
     *
     * 根据id查找
     *
     * @param id 唯一标识
     * @return ViewUamDeliveryObjectEntity
     * @throws BusinessException 业务异常
     */
    ViewUamDeliveryObjectEntity findById(UUID id) throws BusinessException;


    /**
     * 基于桌面ID查询
     * 
     * @param deskId 桌面ID
     * @return List<ViewUamDeliveryObjectEntity>
     */
    List<ViewUamDeliveryObjectEntity> findByDeskId(UUID deskId);

    /**
     * 基于桌面ID查询
     * 
     * @param deskId 桌面ID
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
