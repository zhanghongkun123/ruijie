package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.HomePageSearchDeliveryGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryGroupEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 18:55
 *
 * @author coderLee23
 */
public interface ViewUamDeliveryGroupService {

    /**
     *
     * 根据交付组名称和云桌面名称查找
     *
     * @param searchDeliveryGroupDTO 查询对象
     * @param pageable 分页参数
     * @return Page<ViewUamDeliveryGroupEntity>
     */
    Page<ViewUamDeliveryGroupEntity> pageUamDeliveryGroup(SearchDeliveryGroupDTO searchDeliveryGroupDTO, Pageable pageable);


    /**
     *
     * 根据交付组名称和云桌面名称查找
     *
     * @param homePageSearchDeliveryGroupDTO 查询对象
     * @param pageable 分页参数
     * @return Page<ViewUamDeliveryGroupEntity>
     */
    Page<ViewUamDeliveryGroupEntity> pageUamDeliveryGroup(HomePageSearchDeliveryGroupDTO homePageSearchDeliveryGroupDTO, Pageable pageable);


    /**
     * 根据交付组id查找
     * 
     * @param id 交付组id
     * @return ViewUamDeliveryGroupEntity 返回值
     * @throws BusinessException 业务异常
     */
    ViewUamDeliveryGroupEntity findById(UUID id) throws BusinessException;

}
