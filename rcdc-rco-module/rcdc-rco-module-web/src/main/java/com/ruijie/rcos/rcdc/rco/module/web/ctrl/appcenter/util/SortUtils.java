package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.util;

import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/27 23:53
 *
 * @author coderLee23
 */
public class SortUtils {

    private SortUtils() {
        throw new UnsupportedOperationException("Illegal operation！");
    }

    /**
     * 提供将sk的sort 转换成spring-data-jpa sort
     * 
     * @param sort sk sort
     * @return spring-data-jpa sort
     */
    public static Sort transforSort(com.ruijie.rcos.sk.webmvc.api.vo.Sort sort) {
        Assert.notNull(sort, "sort must not be null");
        String fieldName = sort.getSortField();
        com.ruijie.rcos.sk.webmvc.api.vo.Sort.Direction direction = sort.getDirection();
        return Sort.by(Sort.Direction.valueOf(direction.name()), fieldName);
    }

}
