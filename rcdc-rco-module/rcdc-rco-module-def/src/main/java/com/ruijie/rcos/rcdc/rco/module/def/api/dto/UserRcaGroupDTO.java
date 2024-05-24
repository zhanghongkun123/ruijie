package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.UUID;

/**
 *
 * Description: 用户应用分组信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月21日
 *
 * @author zhengjingyong
 */
public class UserRcaGroupDTO {

    /**
     * 应用池名称
     */
    private String poolName;

    /**
     * 应用池绑定对象
     */
    private UUID memberId;

    /**
     * 应用池绑定对象类型
     */
    @Enumerated(EnumType.STRING)
    private RcaEnum.GroupMemberType memberType;

    /**
     * 应用组id
     */
    private UUID id;

    /**
     * 应用组名称
     */
    private String name;

    /**
     * 归属应用池id
     */
    private UUID poolId;

    /**
     * 应用组描述
     */
    private String description;

    /**
     * 是否默认组
     */
    private Boolean defaultGroup;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public RcaEnum.GroupMemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(RcaEnum.GroupMemberType memberType) {
        this.memberType = memberType;
    }

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

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(Boolean defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
