package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

/**
 * Description: 磁盘与用户关联表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/11
 *
 * @author TD
 */
@Entity
@Table(name = "t_rco_user_disk")
public class UserDiskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID diskId;

    private UUID userId;

    @Version
    private Integer version;

    private Date createTime;

    private Date latestUseTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLatestUseTime() {
        return latestUseTime;
    }

    public void setLatestUseTime(Date latestUseTime) {
        this.latestUseTime = latestUseTime;
    }
}
