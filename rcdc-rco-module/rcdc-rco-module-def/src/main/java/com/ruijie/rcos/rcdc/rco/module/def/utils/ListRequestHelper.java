package com.ruijie.rcos.rcdc.rco.module.def.utils;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/1/12
 *
 * @author chenl
 */
public class ListRequestHelper {
    /**
     * 拆分List参数
     *
     * @param list    list
     * @param maxsize maxsize
     * @param <T>     泛型
     * @return 返回拆分后的数组
     */
    public static <T> List<List<T>> subList(List<T> list, Integer maxsize) {
        Assert.notNull(list, "list is null");
        Assert.notNull(maxsize, "maxsize is null");

        List<List<T>> listArrayList = new ArrayList<List<T>>();
        int size = list.size();
        int round = (size / maxsize) + (size % maxsize != 0 ? 1 : 0);
        for (int i = 0; i < round; i++) {
            int from = i * maxsize;
            int to = (from + maxsize) >= size ? size : from + maxsize;
            listArrayList.add(list.subList(from, to));
        }
        return listArrayList;
    }

    /**
     * 拆分List参数
     *
     * @param array    array
     * @param maxsize maxsize
     * @param <T>     泛型
     * @return 返回拆分后的数组
     */
    public static <T> List<List<T>> subArray(T[] array, Integer maxsize) {
        Assert.notNull(array, "array is null");
        Assert.notNull(maxsize, "maxsize is null");

        List<List<T>> listArrayList = new ArrayList<List<T>>();
        List<T> arrayList = Arrays.asList(array);
        int size = array.length;
        int round = (size / maxsize) + (size % maxsize != 0 ? 1 : 0);
        for (int i = 0; i < round; i++) {
            int from = i * maxsize;
            int to = (from + maxsize) >= size ? size : from + maxsize;
            listArrayList.add(arrayList.subList(from, to));
        }
        return listArrayList;
    }
}
