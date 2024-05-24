package com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto;

import java.util.UUID;

/**
 * Description: 镜像应用信息DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/7/7
 *
 * @author chenjuan
 */
public class ImageAppInfoDTO {

    private UUID id;

    /**
     * 已安装应用name
     */
    private String name;

    /**
     * 应用的路径
     */
    private String path;

    /**
     * 已安装应用参数
     */
    private String args;

    /**
     * 应用的工作目录
     */
    private String workDir;

    /**
     * 图标
     */
    private String icon;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
