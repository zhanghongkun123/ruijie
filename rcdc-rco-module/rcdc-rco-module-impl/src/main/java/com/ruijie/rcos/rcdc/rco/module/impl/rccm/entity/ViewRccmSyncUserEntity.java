package com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity;

import java.util.UUID;

import javax.persistence.*;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:36
 *
 * @author coderLee23
 */
@Table(name = "v_rco_rccm_sync_user")
@Entity
public class ViewRccmSyncUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 用户名称
     **/
    private String userName;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
