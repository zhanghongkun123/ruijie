package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayFieldDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.WatermarkDisplayField;
import com.ruijie.rcos.rcdc.rco.module.impl.service.WatermarkMessageParser;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 解析用户自定义显示桌面信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/3
 *
 * @author jarman
 */
@Service
public class WatermarkMessageParserImpl implements WatermarkMessageParser<WatermarkDisplayContentDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkMessageParserImpl.class);

    @Override
    public WatermarkDisplayContentDTO parse(WatermarkFieldMappingValueDTO mappingValue, String displayContent) {
        Assert.notNull(mappingValue, "mappingValue cannot be null");
        Assert.hasText(displayContent, "displayContent cannot be empty");
        WatermarkDisplayFieldDTO displayFieldDTO = JSON.parseObject(displayContent, WatermarkDisplayFieldDTO.class);
        WatermarkDisplayContentDTO displayContentDTO = new WatermarkDisplayContentDTO();

        // 根据配置的显示字段构建显示内容
        String[] fieldArr = displayFieldDTO.getFieldArr();
        for (String fieldName : fieldArr) {
            if (StringUtils.isBlank(fieldName)) {
                LOGGER.error("自定义显示的水印字段为空，不解析");
                continue;
            }
            WatermarkDisplayField displayField;
            try {
                displayField = WatermarkDisplayField.valueOf(fieldName);
            } catch (IllegalArgumentException e) {
                LOGGER.error("找不到定义的水印显示字段[" + fieldName + "]，请检查前后端定义字段是否一致", e);
                continue;
            }
            displayField.mapping(mappingValue, displayContentDTO);
        }
        return displayContentDTO;
    }
}
