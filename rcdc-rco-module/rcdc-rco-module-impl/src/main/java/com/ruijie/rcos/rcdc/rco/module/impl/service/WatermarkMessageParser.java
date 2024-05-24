package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/3
 *
 * @param <T> 转换的目标对象
 * @author jarman
 */
public interface WatermarkMessageParser<T> {

    /**
     *  屏幕水印自定义显示的数据结构解析
     *
     * @param mappingValue 水印显示的自定义字段映射的值对象
     * @param displayContent 水印显示内容
     * @return 返回结果数据
     */
    T parse(WatermarkFieldMappingValueDTO mappingValue, String displayContent);

}
