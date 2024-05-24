package com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa;

import java.io.Serializable;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月07日
 *
 * @author xiejian
 */
public class UpdateAdminDataPermissionRequest implements Serializable {

    @NotNull
    private UUID id;


    @Nullable
    private String[] userGroupIdArr;

    @Nullable
    private String[] terminalGroupIdArr;

    /**
     * 镜像组
     */
    @Nullable
    private UUID[] imageIdArr;

    /**
     * 桌面池组
     */
    @Nullable
    private UUID[] desktopPoolArr;

    /**
     * 磁盘池组
     */
    @Nullable
    private UUID[] diskPoolArr;

    /**
     * 云桌面策略权限组
     */
    @Nullable
    private UUID[] deskStrategyArr;

    /**
     * 能否需要初始化默认权限。
     * 如果为false，则初始化默认终端组、用户组权限；如果为true，则以请求的权限为准
     */
    @Nullable
    private Boolean needInitDefaultPermission;

    /**
     * 应用池权限组
     */
    @Nullable
    private UUID[] appPoolArr;

    /**
     * 云应用策略权限组
     */
    @Nullable
    private UUID[] appMainStrategyArr;

    /**
     * 云应用外设策略权限组
     */
    @Nullable
    private UUID[] appPeripheralStrategyArr;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String[] getUserGroupIdArr() {
        return userGroupIdArr;
    }

    public void setUserGroupIdArr(String[] userGroupIdArr) {
        this.userGroupIdArr = userGroupIdArr;
    }

    public String[] getTerminalGroupIdArr() {
        return terminalGroupIdArr;
    }

    public void setTerminalGroupIdArr(String[] terminalGroupIdArr) {
        this.terminalGroupIdArr = terminalGroupIdArr;
    }


    @Nullable
    public UUID[] getImageIdArr() {
        return imageIdArr;
    }

    public void setImageIdArr(@Nullable UUID[] imageIdArr) {
        this.imageIdArr = imageIdArr;
    }

    @Nullable
    public UUID[] getDesktopPoolArr() {
        return desktopPoolArr;
    }

    public void setDesktopPoolArr(@Nullable UUID[] desktopPoolArr) {
        this.desktopPoolArr = desktopPoolArr;
    }

    @Nullable
    public UUID[] getDiskPoolArr() {
        return diskPoolArr;
    }

    public void setDiskPoolArr(@Nullable UUID[] diskPoolArr) {
        this.diskPoolArr = diskPoolArr;
    }

    @Nullable
    public UUID[] getDeskStrategyArr() {
        return deskStrategyArr;
    }

    public void setDeskStrategyArr(@Nullable UUID[] deskStrategyArr) {
        this.deskStrategyArr = deskStrategyArr;
    }

    public Boolean getNeedInitDefaultPermission() {
        return needInitDefaultPermission;
    }

    public void setNeedInitDefaultPermission(Boolean needInitDefaultPermission) {
        this.needInitDefaultPermission = needInitDefaultPermission;
    }

    @Nullable
    public UUID[] getAppPoolArr() {
        return appPoolArr;
    }

    public void setAppPoolArr(@Nullable UUID[] appPoolArr) {
        this.appPoolArr = appPoolArr;
    }

    @Nullable
    public UUID[] getAppMainStrategyArr() {
        return appMainStrategyArr;
    }

    public void setAppMainStrategyArr(@Nullable UUID[] appMainStrategyArr) {
        this.appMainStrategyArr = appMainStrategyArr;
    }

    @Nullable
    public UUID[] getAppPeripheralStrategyArr() {
        return appPeripheralStrategyArr;
    }

    public void setAppPeripheralStrategyArr(@Nullable UUID[] appPeripheralStrategyArr) {
        this.appPeripheralStrategyArr = appPeripheralStrategyArr;
    }
}
