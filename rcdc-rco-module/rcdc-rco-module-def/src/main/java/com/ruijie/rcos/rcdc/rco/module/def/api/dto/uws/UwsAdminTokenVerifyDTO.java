package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import java.util.UUID;

/**
 * Description: 管理员token认证
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-17 15:29:00
 *
 * @author zjy
 */
public class UwsAdminTokenVerifyDTO extends UwsBaseDTO {

    private UUID id;

    private String userName;

    private String displayName;

    private String password;

    private Long createTime;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
