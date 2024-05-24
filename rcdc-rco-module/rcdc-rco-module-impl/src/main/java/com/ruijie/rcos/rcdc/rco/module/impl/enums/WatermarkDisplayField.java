package com.ruijie.rcos.rcdc.rco.module.impl.enums;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.WaterDisplayFieldMapper;

/**
 * Description: 与前端定义的水印显示字段
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/3
 *
 * @author jarman
 */
public enum WatermarkDisplayField implements WaterDisplayFieldMapper {

    /** 用户名 */
    USER_NAME {
        @Override
        public void mapping(WatermarkFieldMappingValueDTO mappingValueDTO, WatermarkDisplayContentDTO displayContentDTO) {
            Assert.notNull(mappingValueDTO, "mappingValueDTO cannot be null");
            Assert.notNull(displayContentDTO, "displayContentDTO cannot be null");
            displayContentDTO.setUserName(mappingValueDTO.getUserName());
        }
    },

    /** 桌面名 */
    DESK_NAME {
        @Override
        public void mapping(WatermarkFieldMappingValueDTO mappingValueDTO, WatermarkDisplayContentDTO displayContentDTO) {
            Assert.notNull(mappingValueDTO, "mappingValueDTO cannot be null");
            Assert.notNull(displayContentDTO, "displayContentDTO cannot be null");
            displayContentDTO.setDeskName(mappingValueDTO.getDeskName());
        }
    },

    /** 桌面mac */
    DESK_MAC {
        @Override
        public void mapping(WatermarkFieldMappingValueDTO mappingValueDTO, WatermarkDisplayContentDTO displayContentDTO) {
            Assert.notNull(mappingValueDTO, "mappingValueDTO cannot be null");
            Assert.notNull(displayContentDTO, "displayContentDTO cannot be null");
            displayContentDTO.setDeskMac(mappingValueDTO.getDeskMac());
        }
    },

    /** 桌面IP */
    DESK_IP {
        @Override
        public void mapping(WatermarkFieldMappingValueDTO mappingValueDTO, WatermarkDisplayContentDTO displayContentDTO) {
            Assert.notNull(mappingValueDTO, "mappingValueDTO cannot be null");
            Assert.notNull(displayContentDTO, "displayContentDTO cannot be null");
            displayContentDTO.setDeskIp(mappingValueDTO.getDeskIp());
        }
    },

    /** 自定义内容 */
    CUSTOMIZE {
        @Override
        public void mapping(WatermarkFieldMappingValueDTO mappingValueDTO, WatermarkDisplayContentDTO displayContentDTO) {
            Assert.notNull(mappingValueDTO, "mappingValueDTO cannot be null");
            Assert.notNull(displayContentDTO, "displayContentDTO cannot be null");
            displayContentDTO.setCustomContent(mappingValueDTO.getCustomContent());
        }
    }
}
