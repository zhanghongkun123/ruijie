package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareGroupTypeEnum;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@PageQueryDTOConfig(entityType = "ViewRcoSoftwareGroupCountEntity")
public class SoftwareGroupDTO {
    /**
     * 软件分组 id
     */
    private UUID id;

    /**
     * 软件分组名称
     */
    private String name;

    /**
     * 软件分组类型 内置、自定义
     */
    private SoftwareGroupTypeEnum groupType;

    /**
     * 软件分组描述
     */
    private String description;

    /**
     * 软件个数
     */
    private Long count;

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

    public SoftwareGroupTypeEnum getGroupType() {
        return groupType;
    }

    public void setGroupType(SoftwareGroupTypeEnum groupType) {
        this.groupType = groupType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
