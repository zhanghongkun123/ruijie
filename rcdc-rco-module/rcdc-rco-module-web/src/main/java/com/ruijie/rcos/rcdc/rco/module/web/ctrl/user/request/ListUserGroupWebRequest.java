package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/22
 *
 * @author Jarman
 */
public class ListUserGroupWebRequest implements WebRequest {

    @Nullable
    private Boolean enableFilterDefaultGroup;

    @Nullable
    private Boolean enableFilterAdGroup;

    @Nullable
    private Boolean enableFilterLdapGroup;

    @Nullable
    private Boolean enableFilterThirdPartyGroup;

    @Nullable
    private UUID filterGroupId;

    @Nullable
    private Boolean noPermission;

    @Nullable
    private UUID desktopPoolId;

    @Nullable
    private UUID diskPoolId;

    @Nullable
    private UUID appGroupId;

    @Nullable
    private Boolean enableFilterPoolUserGroup;

    @Nullable
    private Boolean enableCountGroupUser;

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

    public Boolean getEnableFilterAdGroup() {
        return enableFilterAdGroup;
    }

    public void setEnableFilterAdGroup(Boolean enableFilterAdGroup) {
        this.enableFilterAdGroup = enableFilterAdGroup;
    }

    @Nullable
    public Boolean getNoPermission() {
        return noPermission;
    }

    public void setNoPermission(@Nullable Boolean noPermission) {
        this.noPermission = noPermission;
    }

    @Nullable
    public Boolean getEnableFilterLdapGroup() {
        return enableFilterLdapGroup;
    }

    public void setEnableFilterLdapGroup(@Nullable Boolean enableFilterLdapGroup) {
        this.enableFilterLdapGroup = enableFilterLdapGroup;
    }

    @Nullable
    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(@Nullable UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    @Nullable
    public UUID getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(@Nullable UUID diskPoolId) {
        this.diskPoolId = diskPoolId;
    }

    @Nullable
    public Boolean getEnableFilterPoolUserGroup() {
        return enableFilterPoolUserGroup;
    }

    public void setEnableFilterPoolUserGroup(@Nullable Boolean enableFilterPoolUserGroup) {
        this.enableFilterPoolUserGroup = enableFilterPoolUserGroup;
    }

    @Nullable
    public Boolean getEnableFilterThirdPartyGroup() {
        return enableFilterThirdPartyGroup;
    }

    public void setEnableFilterThirdPartyGroup(@Nullable Boolean enableFilterThirdPartyGroup) {
        this.enableFilterThirdPartyGroup = enableFilterThirdPartyGroup;
    }

    @Nullable
    public UUID getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(@Nullable UUID appGroupId) {
        this.appGroupId = appGroupId;
    }

    @Nullable
    public Boolean getEnableCountGroupUser() {
        return enableCountGroupUser;
    }

    public void setEnableCountGroupUser(@Nullable Boolean enableCountGroupUser) {
        this.enableCountGroupUser = enableCountGroupUser;
    }
}
