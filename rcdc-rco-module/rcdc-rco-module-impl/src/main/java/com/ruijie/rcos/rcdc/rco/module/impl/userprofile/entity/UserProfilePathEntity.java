package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: 子路径对象的Entity
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21
 *
 * @author zwf
 */
@Entity
@Table(name = "t_rco_user_profile_path_detail")
public class UserProfilePathEntity {
    /**
     * 具体路径ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 路径
     */
    private String path;

    /**
     *  所属路径配置ID
     */
    private UUID userProfilePathId;

    /**
     *  所属子路径配置ID
     */
    private UUID userProfileChildPathId;

    @Version
    private int version;

    private int index;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UUID getUserProfilePathId() {
        return userProfilePathId;
    }

    public void setUserProfilePathId(UUID userProfilePathId) {
        this.userProfilePathId = userProfilePathId;
    }

    public UUID getUserProfileChildPathId() {
        return userProfileChildPathId;
    }

    public void setUserProfileChildPathId(UUID userProfileChildPathId) {
        this.userProfileChildPathId = userProfileChildPathId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
