package com.ruijie.rcos.rcdc.rco.module.def.desktopsession;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月22日
 *
 * @author wangjie9
 */
public class RdsMgrDesktopSessionInfoDTO {

    @NotNull
    private UUID userId;


    /**
     * 1,   # 用户名
     */
    @NotNull
    @JSONField(name = "userName")
    private String userName;

    /**
     * 2,  # 会话ID
     */
    @Nullable
    @JSONField(name = "sessionId")
    private Integer sessionId;

    /**
     * 3,  # 最近会话建立时间
     */
    @Nullable
    @JSONField(name = "lastCreateTime")
    private Long lastCreateTime;

    /**
     * 4,  # 最近会话空闲开始时间
     */
    @Nullable
    @JSONField(name = "lastIdleTime")
    private Long lastIdleTime;

    /**
     * 5,  # 会话注销时间
     */
    @Nullable
    @JSONField(name = "destroyTime")
    private Long destroyTime;

    /**
     * 6,  # 会话注销时间
     */
    @Nullable
    @JSONField(name = "destroyType")
    private Integer destroyType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(@Nullable Integer sessionId) {
        this.sessionId = sessionId;
    }

    @Nullable
    public Long getLastCreateTime() {
        return lastCreateTime;
    }

    public void setLastCreateTime(@Nullable Long lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }

    @Nullable
    public Long getLastIdleTime() {
        return lastIdleTime;
    }

    public void setLastIdleTime(@Nullable Long lastIdleTime) {
        this.lastIdleTime = lastIdleTime;
    }

    @Nullable
    public Long getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(@Nullable Long destroyTime) {
        this.destroyTime = destroyTime;
    }

    @Nullable
    public Integer getDestroyType() {
        return destroyType;
    }

    public void setDestroyType(@Nullable Integer destroyType) {
        this.destroyType = destroyType;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
