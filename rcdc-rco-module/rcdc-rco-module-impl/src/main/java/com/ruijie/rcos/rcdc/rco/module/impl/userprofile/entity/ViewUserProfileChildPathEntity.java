package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: 子路径列表展示的Entity
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21
 *
 * @author zwf
 */
@Entity
@Table(name = "v_rco_user_profile_child_path")
public class ViewUserProfileChildPathEntity {
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
     * 配置方式(同步/排除)
     */
    @Enumerated(EnumType.STRING)
    private UserProfilePathModeEnum mode;

    /**
     * 类型(文件夹/文件/注册表)
     */
    @Enumerated(EnumType.STRING)
    private UserProfilePathTypeEnum type;

    @Version
    private int version;

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

    public UserProfilePathModeEnum getMode() {
        return mode;
    }

    public void setMode(UserProfilePathModeEnum mode) {
        this.mode = mode;
    }

    public UserProfilePathTypeEnum getType() {
        return type;
    }

    public void setType(UserProfilePathTypeEnum type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
