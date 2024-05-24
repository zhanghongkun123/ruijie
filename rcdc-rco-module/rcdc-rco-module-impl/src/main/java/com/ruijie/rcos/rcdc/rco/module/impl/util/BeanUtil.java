package com.ruijie.rcos.rcdc.rco.module.impl.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/24 16:16
 *
 * @author ketb
 */
public class BeanUtil {

    private BeanUtil() {
        throw new IllegalStateException("BeanUtil Utility class");
    }

    /**
     * 复制选项属性，不复制为空的属性
     *
     * @param src    源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        Assert.notNull(src, "src is not be null");
        Assert.notNull(target, "target is not be null");
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pdsArr = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pdsArr) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] resultArr = new String[emptyNames.size()];
        return emptyNames.toArray(resultArr);
    }
}
