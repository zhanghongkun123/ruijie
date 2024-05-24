package com.ruijie.rcos.rcdc.rco.module.impl.computername.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Description: 云桌面计算机名实体类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/10
 *
 * @author wjp
 */
@Entity
@Table(name = "t_rco_cloud_desk_computer_name")
public class CloudDeskComputerNameConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String computerName;

    private UUID deskStrategyId;

    @Version
    private Integer version;

    private Date updateTime;

    public CloudDeskComputerNameConfigEntity() {
        // Do nothing because of X and Y.
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public UUID getDeskStrategyId() {
        return deskStrategyId;
    }

    public void setDeskStrategyId(UUID deskStrategyId) {
        this.deskStrategyId = deskStrategyId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
