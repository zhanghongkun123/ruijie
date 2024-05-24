package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 创建或编辑用户组请求参数对象
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月17日
 * 
 * @author jarman
 */
public class UserGroupRequest implements Request {

    @Nullable
    private UUID id;

    @NotBlank
    private String groupName;

    @Nullable
    private UUID parentId;

    @Nullable
    private Boolean enableVdiConfigDesktop = Boolean.FALSE;

    /**
     * VDI云桌面关联镜像模板id
     */
    @Nullable
    private UUID cbbVdiDesktopImageId;

    /**
     * VDI云桌面关联策略模板id
     */
    @Nullable
    private UUID cbbVdiDesktopStrategyId;

    /**
     * VDI云桌面关联网络模板id
     */
    @Nullable
    private UUID cbbVdiDesktopNetworkId;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public Boolean getEnableVdiConfigDesktop() {
        return enableVdiConfigDesktop;
    }

    public void setEnableVdiConfigDesktop(Boolean enableVdiConfigDesktop) {
        this.enableVdiConfigDesktop = enableVdiConfigDesktop;
    }

    public UUID getCbbVdiDesktopImageId() {
        return cbbVdiDesktopImageId;
    }

    public void setCbbVdiDesktopImageId(UUID cbbVdiDesktopImageId) {
        this.cbbVdiDesktopImageId = cbbVdiDesktopImageId;
    }

    public UUID getCbbVdiDesktopStrategyId() {
        return cbbVdiDesktopStrategyId;
    }

    public void setCbbVdiDesktopStrategyId(UUID cbbVdiDesktopStrategyId) {
        this.cbbVdiDesktopStrategyId = cbbVdiDesktopStrategyId;
    }

    public UUID getCbbVdiDesktopNetworkId() {
        return cbbVdiDesktopNetworkId;
    }

    public void setCbbVdiDesktopNetworkId(UUID cbbVdiDesktopNetworkId) {
        this.cbbVdiDesktopNetworkId = cbbVdiDesktopNetworkId;
    }

}
