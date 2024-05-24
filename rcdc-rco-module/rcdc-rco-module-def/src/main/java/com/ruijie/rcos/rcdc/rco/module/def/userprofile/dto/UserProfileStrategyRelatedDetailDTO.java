package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户配置策略关联信息详情DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/18
 *
 * @author WuShengQiang
 */
public class UserProfileStrategyRelatedDetailDTO {

    /**
     * 路径ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 策略ID
     */
    private UUID strategyId;

    /**
     * 组ID
     */
    private UUID groupId;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 子路径信息
     */
    private String groupName;

    /**
     * 描述
     **/
    private String description;

    /**
     * 创建人
     */
    private String creatorUserName;

    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}