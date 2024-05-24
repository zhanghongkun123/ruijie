package com.ruijie.rcos.rcdc.rco.module.def.service.tree;

import java.util.*;

import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeNodeVO;
import org.springframework.util.Assert;

/**
 *
 * Description: 构建树
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 * 
 * @param <T> 具体节点类型
 * @author Jarman
 */
public class TreeBuilder<T extends TreeNodeVO> {

    private Map<String, T> dataMap;

    public TreeBuilder(List<T> dataList) {
        Assert.notNull(dataList, "dataList cannot null");
        dataMap = new LinkedHashMap<>(dataList.size());
        // 转为map，key -> id ; value -> 节点对象
        dataList.forEach(node -> dataMap.put(node.getId(), node));
    }

    /**
     * 构建树结构
     * 
     * @return 返回树结构
     */
    public final List<T> build() {
        List<T> resultList = new ArrayList<>();
        dataMap.forEach((id, node) -> {
            // 父节点为null，表示根节点
            if (node.getParentId() == null) {
                resultList.add(node);
            } else {
                // 找到所属父节点，挂在父节点下
                if (dataMap.get(node.getParentId()) != null) {
                    dataMap.get(node.getParentId()).addChildren(node);
                }
            }
        });
        return resultList;
    }
}
