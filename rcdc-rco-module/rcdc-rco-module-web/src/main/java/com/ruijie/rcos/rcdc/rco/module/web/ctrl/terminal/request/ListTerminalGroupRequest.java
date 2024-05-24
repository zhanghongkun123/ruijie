package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 *
 * Description: 获取终端分组列表请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 *
 * @author nt
 */
public class ListTerminalGroupRequest implements WebRequest {

    @Nullable
    private Boolean enableFilterDefaultGroup;

    @Nullable
    private UUID filterGroupId;

    @Nullable
    private Boolean noPermission;

    @Nullable
    private UUID desktopPoolId;

    @Nullable
    private Boolean enableFilterPoolGroup;

    public Boolean getEnableFilterDefaultGroup() {
        return enableFilterDefaultGroup;
    }

    public void setEnableFilterDefaultGroup(Boolean enableFilterDefaultGroup) {
        this.enableFilterDefaultGroup = enableFilterDefaultGroup;
    }

    public UUID getFilterGroupId() {
        return filterGroupId;
    }

    public void setFilterGroupId(UUID filterGroupId) {
        this.filterGroupId = filterGroupId;
    }

    public Boolean getNoPermission() {
        return noPermission;
    }

    public void setNoPermission(Boolean noPermission) {
        this.noPermission = noPermission;
    }

    @Nullable
    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(@Nullable UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    @Nullable
    public Boolean getEnableFilterPoolGroup() {
        return enableFilterPoolGroup;
    }

    public void setEnableFilterPoolGroup(@Nullable Boolean enableFilterPoolGroup) {
        this.enableFilterPoolGroup = enableFilterPoolGroup;
    }
}
