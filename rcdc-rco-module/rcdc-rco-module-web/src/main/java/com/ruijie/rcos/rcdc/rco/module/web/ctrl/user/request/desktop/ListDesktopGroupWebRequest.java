package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年1月13日
 *
 * @author liuchaoxue
 */

public class ListDesktopGroupWebRequest implements WebRequest {
    @Nullable
    private Boolean enableFilterDefaultGroup;

    @Nullable
    private UUID filterGroupId;

    @Nullable
    private Boolean noPermission;

    @Nullable
    private Boolean enableQueryUser;

    @Nullable
    private UUID desktopPoolId;

    public Boolean getEnableFilterDefaultGroup() {
        return enableFilterDefaultGroup;
    }

    public void setEnableFilterDefaultGroup(Boolean enableFilterDefaultGroup) {
        this.enableFilterDefaultGroup = enableFilterDefaultGroup;
    }

    @Nullable
    public UUID getFilterGroupId() {
        return filterGroupId;
    }

    public void setFilterGroupId(@Nullable UUID filterGroupId) {
        this.filterGroupId = filterGroupId;
    }

    @Nullable
    public Boolean getNoPermission() {
        return noPermission;
    }

    public void setNoPermission(@Nullable Boolean noPermission) {
        this.noPermission = noPermission;
    }

    @Nullable
    public Boolean getEnableQueryUser() {
        return enableQueryUser;
    }

    public void setEnableQueryUser(@Nullable Boolean enableQueryUser) {
        this.enableQueryUser = enableQueryUser;
    }

    @Nullable
    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(@Nullable UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }
}
