package com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotUserType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 用户自定义快照创建请求DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author zqj
 */
public class CreateDeskSnapshotTaskDTO {

    @NotNull
    private UUID deskId;

    private String deskSnapshotName;

    private UUID userId;

    private  CbbDeskSnapshotUserType userType;

    /**
     * @param deskId           云桌面UUID（不允许为空）
     * @param userId           用户ID
     * @param userType         用户类型 (管理员、普通用户)
     * @param deskSnapshotName 云桌面快照名
     */
    public CreateDeskSnapshotTaskDTO(@NotNull UUID deskId, String deskSnapshotName, UUID userId, CbbDeskSnapshotUserType userType) {
        this.deskId = deskId;
        this.deskSnapshotName = deskSnapshotName;
        this.userId = userId;
        this.userType = userType;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public String getDeskSnapshotName() {
        return deskSnapshotName;
    }

    public UUID getUserId() {
        return userId;
    }

    public CbbDeskSnapshotUserType getUserType() {
        return userType;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public void setDeskSnapshotName(String deskSnapshotName) {
        this.deskSnapshotName = deskSnapshotName;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUserType(CbbDeskSnapshotUserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "CreateDeskSnapshotTaskDTO{" +
                "deskId=" + deskId +
                ", deskSnapshotName='" + deskSnapshotName + '\'' +
                ", userId=" + userId +
                ", userType=" + userType +
                '}';
    }
}
