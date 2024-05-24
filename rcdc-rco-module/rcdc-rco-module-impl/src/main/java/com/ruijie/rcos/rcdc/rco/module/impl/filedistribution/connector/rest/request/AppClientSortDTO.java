package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;

/**
 * Description: 云应用列表排序DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/6 15:11
 *
 * @author chenjuan
 */
public class AppClientSortDTO {

    @NotBlank
    @JSONField(name = "fieldName")
    private String fieldName;

    @NotNull
    @JSONField(name = "direction")
    private Sort.Direction direction;

    public AppClientSortDTO() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Sort.Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }
}
