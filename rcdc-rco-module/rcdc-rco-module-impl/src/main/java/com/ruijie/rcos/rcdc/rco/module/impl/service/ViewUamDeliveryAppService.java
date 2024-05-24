package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryAppDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryAppEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 18:55
 *
 * @author coderLee23
 */
public interface ViewUamDeliveryAppService {

    /**
     *
     * 根据交付组id查询、或+云桌面名称模糊查找
     * 
     * @param searchDeliveryAppDTO 交付应用查询条件
     * @param pageable 分页参数
     * @return Page<ViewUamDeliveryAppEntity>
     */
    Page<ViewUamDeliveryAppEntity> pageUamDeliveryApp(SearchDeliveryAppDTO searchDeliveryAppDTO, Pageable pageable);


    /**
     * 
     * 交付组-根据id查找应用
     * 
     * @param id 唯一标识
     * @return ViewUamDeliveryAppEntity
     * @throws BusinessException 业务异常
     */
    ViewUamDeliveryAppEntity findById(UUID id) throws BusinessException;


    /**
     * 基于交付组ID查询应用列表
     * @param groupId groupId
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    List<ViewUamDeliveryAppEntity> findAppListByGroupId(UUID groupId) throws BusinessException;


    /**
     * 基于交付组ID查询应用名称列表
     * @param groupIdList groupIdList
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    List<String> findAppNameListByGroupId(List<UUID> groupIdList) throws BusinessException;

}
