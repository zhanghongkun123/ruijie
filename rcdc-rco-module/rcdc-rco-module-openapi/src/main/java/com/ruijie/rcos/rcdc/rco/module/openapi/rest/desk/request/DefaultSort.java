package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public class DefaultSort implements Serializable, Sort {

    @NotBlank
    private String fieldName;

    @NotNull
    private Direction direction;

    public DefaultSort(String sortField, Direction direction) {
        Assert.hasText(sortField, "sortField is not empty");
        Assert.notNull(direction, "direction is not null");
        this.fieldName = sortField;
        this.direction = direction;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    static DefaultSort desc(String fieldName) {
        Assert.hasText(fieldName, "fieldName is not null");
        return new DefaultSort(fieldName, Direction.DESC);
    }

    static DefaultSort asc(String fieldName) {
        Assert.hasText(fieldName, "fieldName is not null");
        return new DefaultSort(fieldName, Direction.ASC);
    }

    /**
     * 转换
     * @param newFieldName newFieldName
     * @param direction direction
     * @return sort
     */
    public static DefaultSort convertSortFieldName(String newFieldName, Direction direction) {
        Assert.hasText(newFieldName, "newFieldName is not null");
        Assert.notNull(direction, "direction is not null");
        return new DefaultSort(newFieldName, direction);
    }
}
