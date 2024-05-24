package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 桌面池与PC终端关系表
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
@Entity
@Table(name = "t_rco_desktop_pool_computer")
public class DesktopPoolComputerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID desktopPoolId;

    private UUID relatedId;

    @Enumerated(EnumType.STRING)
    private ComputerRelatedType relatedType;

    private Date createTime;

    @Version
    private Integer version;

    public DesktopPoolComputerEntity() {
    }

    public DesktopPoolComputerEntity(UUID relatedId, ComputerRelatedType relatedType) {
        this.relatedId = relatedId;
        this.relatedType = relatedType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public ComputerRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(ComputerRelatedType relatedType) {
        this.relatedType = relatedType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
