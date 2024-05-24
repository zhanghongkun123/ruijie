package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;

/**
 * Description: 映射水印字段值
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/3
 *
 * @author jarman
 */
public interface WaterDisplayFieldMapper {
    /**
     * 把映射的值设置到对应的显示对象dto字段中
     * 
     * @param mappingValueDTO 映射的值对象
     * @param displayContentDTO 需要设置的自定义显示对象
     */
    void mapping(WatermarkFieldMappingValueDTO mappingValueDTO, WatermarkDisplayContentDTO displayContentDTO);
}
