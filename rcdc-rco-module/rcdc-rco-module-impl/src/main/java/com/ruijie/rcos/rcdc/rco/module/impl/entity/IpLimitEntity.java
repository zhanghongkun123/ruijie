package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: VDI网段限制实体类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 18:55
 *
 * @author yxq
 */
@Entity
@Table(name = "t_rco_ip_limit")
public class IpLimitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 限制网段-起始IP
     */
    private String ipStart;

    /**
     * 限制网段-终止IP
     */
    private String ipEnd;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIpStart() {
        return ipStart;
    }

    public void setIpStart(String limitIpBegin) {
        this.ipStart = limitIpBegin;
    }

    public String getIpEnd() {
        return ipEnd;
    }

    public void setIpEnd(String limitIpEnd) {
        this.ipEnd = limitIpEnd;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
