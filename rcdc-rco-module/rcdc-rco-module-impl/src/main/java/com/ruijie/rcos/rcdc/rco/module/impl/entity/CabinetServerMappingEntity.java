package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.UUID;

import javax.persistence.*;

/**
 * 机柜关联服务器持久化实
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月9日
 *
 * @author bairuqiang
 */
@Entity
@Table(name = "t_rco_cabinet_server_mapping")
public class CabinetServerMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private Integer version;

    /**
     * 机柜Id
     */
    private UUID cabinetId;

    /**
     * 服务器Id
     */
    private UUID serverId;

    private Integer cabinetLocationBegin;

    private Integer cabinetLocationEnd;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(UUID cabinetId) {
        this.cabinetId = cabinetId;
    }

    public UUID getServerId() {
        return serverId;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }

    public Integer getCabinetLocationBegin() {
        return cabinetLocationBegin;
    }

    public void setCabinetLocationBegin(Integer cabinetLocationBegin) {
        this.cabinetLocationBegin = cabinetLocationBegin;
    }

    public Integer getCabinetLocationEnd() {
        return cabinetLocationEnd;
    }

    public void setCabinetLocationEnd(Integer cabinetLocationEnd) {
        this.cabinetLocationEnd = cabinetLocationEnd;
    }
}
