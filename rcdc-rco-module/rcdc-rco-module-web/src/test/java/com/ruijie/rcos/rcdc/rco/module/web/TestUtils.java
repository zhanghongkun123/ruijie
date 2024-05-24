package com.ruijie.rcos.rcdc.rco.module.web;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/27
 *
 * @author chen zj
 */
public class TestUtils {

    /**
     * 单元测试工具赋值类
     * 
     * @param object 要赋值的对象
     * @throws IllegalAccessException 异常
     */
    public static void generateDataByReflect(Object object) throws IllegalAccessException {
        Assert.notNull(object, "Param [object] must not be null");
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Random random = new Random();
        Field [] fieldArr = new Field[fieldList.size()];
        fieldList.toArray(fieldArr);
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (field.getType().isEnum()) {
                Object [] objArr = field.getType().getEnumConstants();
                field.set(object, objArr[0]);
                continue;
            }
            if (field.getType().getName().equals(UUID.class.getName())) {
                field.set(object, UUID.randomUUID());
                continue;
            }
            if (field.getType().getName().equals(Integer.class.getName())) {
                field.set(object, new Integer(10));
                continue;
            }
            if (field.getType().getName().equals("int")) {
                field.setInt(object, 10);
                continue;
            }
            if (field.getType().getName().equals("long")) {
                field.setLong(object, 123L);
                continue;
            }
            if (field.getType().getName().equals(String.class.getName())) {

                field.set(object, "abc123" + String.valueOf(random.nextInt(1024)));
                continue;
            }
            if (field.getType().getName().equals(Boolean.class.getName())) {
                field.set(object, true);
                continue;
            }
            if (field.getType().getName().equals("boolean")) {
                field.set(object, true);
                continue;
            }
        }
    }
}
