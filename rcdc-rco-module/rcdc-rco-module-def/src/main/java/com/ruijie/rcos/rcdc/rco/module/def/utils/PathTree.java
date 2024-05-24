package com.ruijie.rcos.rcdc.rco.module.def.utils;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDetailDTO;
import org.springframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Description: 路径树
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/24
 *
 * @author WuShengQiang
 */
public class PathTree {

    /**
     * 树结构
     */
    private Set<PathTree> children = new LinkedHashSet<PathTree>();

    /**
     * 路径DTO对象
     */
    private UserProfilePathDetailDTO data;

    /**
     * 路径
     */
    private String path;

    public PathTree(UserProfilePathDetailDTO data, String path) {
        this.data = data;
        this.path = path;
    }

    public PathTree(String path) {
        this.path = path;
    }

    public PathTree() {
    }

    /**
     * 处理节点
     *
     * @param data 路径对象
     * @param path 路径
     * @return 响应节点对象
     */
    public PathTree child(UserProfilePathDetailDTO data, String path) {
        Assert.notNull(data, "data cannot null");
        Assert.hasText(path, "path cannot null");
        for (PathTree child : children) {
            if (child.path.toLowerCase().equals(path.toLowerCase())) {
                return child;
            }
        }
        return child(new PathTree(data, path));
    }

    /**
     * 处理节点
     *
     * @param path 路径
     * @return 响应节点对象
     */
    public PathTree child(String path) {
        Assert.hasText(path, "path cannot null");
        for (PathTree child : children) {
            if (child.path.toLowerCase().equals(path.toLowerCase())) {
                return child;
            }
        }
        return child(new PathTree(path));
    }

    PathTree child(PathTree child) {
        children.add(child);
        return child;
    }

    public Set<PathTree> getChildren() {
        return children;
    }

    public void setChildren(Set<PathTree> children) {
        this.children = children;
    }

    public UserProfilePathDetailDTO getData() {
        return data;
    }

    public void setData(UserProfilePathDetailDTO data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}