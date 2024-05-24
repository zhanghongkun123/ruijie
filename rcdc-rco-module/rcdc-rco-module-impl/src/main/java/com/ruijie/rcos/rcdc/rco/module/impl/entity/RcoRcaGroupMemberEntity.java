package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: 关联关系分页查询使用 ————> com.ruijie.rcos.rcdc.rca.module.impl.newentity.RcaGroupMemberEntity
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/12 14:16
 *
 * @author zhengjingyong
 */

@Entity
@Table(name = "t_cbb_rca_group_member")
public class RcoRcaGroupMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID memberId;

    private UUID groupId;

    @Enumerated(EnumType.STRING)
    private RcaEnum.GroupMemberType memberType;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public RcaEnum.GroupMemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(RcaEnum.GroupMemberType memberType) {
        this.memberType = memberType;
    }
}
