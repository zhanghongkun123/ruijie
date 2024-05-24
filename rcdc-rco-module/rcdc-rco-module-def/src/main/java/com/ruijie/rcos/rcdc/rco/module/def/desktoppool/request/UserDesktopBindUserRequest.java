package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 *
 * Description: 池中桌面绑定用户信息请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年01月10日
 *
 * @author linke
 */
public class UserDesktopBindUserRequest {

    @NotNull
    private UUID desktopId;

    @NotNull
    private UUID userId;

    @Nullable
    private String desktopName;

    /**
     * 池ID
     */
    @Nullable
    private UUID desktopPoolId;

    /**
     * 会话类型
     **/
    @Nullable
    private CbbDesktopSessionType sessionType;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Nullable
    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(@Nullable String desktopName) {
        this.desktopName = desktopName;
    }

    @Nullable
    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(@Nullable UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }
}
