package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户配置路径组
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
@PageQueryDTOConfig(entityType = "ViewRcoUserProfilePathGroupCountEntity")
public class UserProfilePathGroupDTO {

    /**
     * 组 id
     */
    private UUID id;

    /**
     * 组名称
     */
    private String name;

    /**
     * 组描述
     */
    private String description;

    /**
     * 路径个数
     */
    private Long count;

    private Date createTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
