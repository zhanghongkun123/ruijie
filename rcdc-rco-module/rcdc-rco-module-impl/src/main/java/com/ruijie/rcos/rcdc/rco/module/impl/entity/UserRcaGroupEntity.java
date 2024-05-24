package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 应用池绑定关系
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/02/21
 *
 * @author zhengjingyong
 */
@Entity
@Table(name = "v_rco_user_rca_group")
public class UserRcaGroupEntity {

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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Version
    private int version;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
