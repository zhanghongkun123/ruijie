package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryAppDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryObjectDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryDetailEntity;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 18:55
 *
 * @author coderLee23
 */
public interface ViewUamDeliveryDetailService {

    /**
     * 根据交付组id和应用id查询 交付应用详情
     *
     * @param searchDeliveryAppDetailDTO 查询对象
     * @param pageable 分页参数
     * @return Page<CbbUamDeliveryAppDetailDTO>
     */
    Page<ViewUamDeliveryDetailEntity> pageUamDeliveryAppDetail(SearchDeliveryAppDetailDTO searchDeliveryAppDetailDTO, Pageable pageable);


    /**
     * 根据交付组id和云桌面id查询 交付对象详情
     *
     * @param searchDeliveryObjectDetailDTO 查询对象
     * @param pageable 分页参数
     * @return Page<CbbUamDeliveryAppDetailDTO>
     */
    Page<ViewUamDeliveryDetailEntity> pageUamDeliveryObjectDetail(SearchDeliveryObjectDetailDTO searchDeliveryObjectDetailDTO, Pageable pageable);


    /**
     * 根据id查找
     * 
     * @param id 唯一标识
     * @return ViewUamDeliveryDetailEntity
     * @throws BusinessException 业务异常
     */
    ViewUamDeliveryDetailEntity getById(UUID id) throws BusinessException;
}
