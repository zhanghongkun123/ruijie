package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
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
public class UpdateUserGroupRequest implements Request {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @Nullable
    private UUID parentId;

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

    /**
     * IDV云桌面关联镜像模板id
     */
    @Nullable
    private UUID idvDesktopImageId;

    /**
     * IDV云桌面关联策略模板id
     * @return
     */
    @Nullable
    private UUID idvDesktopStrategyId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(@Nullable UUID parentId) {
        this.parentId = parentId;
    }

    @Nullable
    public UUID getCbbVdiDesktopImageId() {
        return cbbVdiDesktopImageId;
    }

    public void setCbbVdiDesktopImageId(@Nullable UUID cbbVdiDesktopImageId) {
        this.cbbVdiDesktopImageId = cbbVdiDesktopImageId;
    }

    @Nullable
    public UUID getCbbVdiDesktopStrategyId() {
        return cbbVdiDesktopStrategyId;
    }

    public void setCbbVdiDesktopStrategyId(@Nullable UUID cbbVdiDesktopStrategyId) {
        this.cbbVdiDesktopStrategyId = cbbVdiDesktopStrategyId;
    }

    @Nullable
    public UUID getCbbVdiDesktopNetworkId() {
        return cbbVdiDesktopNetworkId;
    }

    public void setCbbVdiDesktopNetworkId(@Nullable UUID cbbVdiDesktopNetworkId) {
        this.cbbVdiDesktopNetworkId = cbbVdiDesktopNetworkId;
    }

    @Nullable
    public UUID getIdvDesktopImageId() {
        return idvDesktopImageId;
    }

    public void setIdvDesktopImageId(@Nullable UUID idvDesktopImageId) {
        this.idvDesktopImageId = idvDesktopImageId;
    }

    @Nullable
    public UUID getIdvDesktopStrategyId() {
        return idvDesktopStrategyId;
    }

    public void setIdvDesktopStrategyId(@Nullable UUID idvDesktopStrategyId) {
        this.idvDesktopStrategyId = idvDesktopStrategyId;
    }
}
