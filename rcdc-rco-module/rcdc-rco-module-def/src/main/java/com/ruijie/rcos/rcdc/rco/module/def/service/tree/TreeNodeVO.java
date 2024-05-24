package com.ruijie.rcos.rcdc.rco.module.def.service.tree;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/6/27
 *
 * @author Jarman
 */
public class TreeNodeVO {

    protected String id;

    protected String parentId;

    protected String label;

    @SuppressWarnings("PMD.ArrayOrListPropertyNamingRule")
    private List<? super TreeNodeVO> childrenList = new ArrayList<>();

    /**
     * 添加子节点
     * 
     * @param node 节点
     * @param <T> 具体节点类型
     */
    public <T extends TreeNodeVO> void addChildren(T node) {
        Assert.notNull(node, "node cannot null");
        this.childrenList.add(node);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<? super TreeNodeVO> getChildren() {
        return childrenList;
    }

    public void setChildren(List<? super TreeNodeVO> children) {
        this.childrenList = children;
    }
}
