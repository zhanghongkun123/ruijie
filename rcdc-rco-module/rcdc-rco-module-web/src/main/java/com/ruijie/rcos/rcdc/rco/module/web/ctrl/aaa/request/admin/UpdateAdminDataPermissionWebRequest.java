package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月21日
 *
 * @author zhuangchenwu
 */
public class UpdateAdminDataPermissionWebRequest implements WebRequest {

    @ApiModelProperty(value = "管理员ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "用户组权限", required = false)
    @NotNull
    private GroupIdLabelWebEntry[] userGroupArr;

    @ApiModelProperty(value = "终端组权限", required = false)
    @NotNull
    private GroupIdLabelWebEntry[] terminalGroupArr;

    /**
     * 镜像组
     */
    @Nullable
    private IdLabelEntry[] imageArr;

    @ApiModelProperty(value = "桌面池权限", required = false)
    @Nullable
    private IdLabelEntry[] desktopPoolArr;

    @ApiModelProperty(value = "磁盘池权限", required = false)
    @Nullable
    private IdLabelEntry[] diskPoolArr;

    @ApiModelProperty(value = "云桌面策略权限组", required = false)
    @Nullable
    private IdLabelEntry[] deskStrategyArr;

    @ApiModelProperty(value = "应用池权限组")
    @Nullable
    private IdLabelEntry[] appPoolArr;

    @ApiModelProperty(value = "云应用策略权限组")
    @Nullable
    private IdLabelEntry[] appMainStrategyArr;

    @ApiModelProperty(value = "云应用外设策略权限组")
    @Nullable
    private IdLabelEntry[] appPeripheralStrategyArr;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public GroupIdLabelWebEntry[] getUserGroupArr() {
        return userGroupArr;
    }

    public void setUserGroupArr(@Nullable GroupIdLabelWebEntry[] userGroupArr) {
        this.userGroupArr = userGroupArr;
    }

    @Nullable
    public GroupIdLabelWebEntry[] getTerminalGroupArr() {
        return terminalGroupArr;
    }

    public void setTerminalGroupArr(@Nullable GroupIdLabelWebEntry[] terminalGroupArr) {
        this.terminalGroupArr = terminalGroupArr;
    }

    @Nullable
    public IdLabelEntry[] getImageArr() {
        return imageArr;
    }

    public void setImageArr(@Nullable IdLabelEntry[] imageArr) {
        this.imageArr = imageArr;
    }

    @Nullable
    public IdLabelEntry[] getDesktopPoolArr() {
        return desktopPoolArr;
    }

    public void setDesktopPoolArr(@Nullable IdLabelEntry[] desktopPoolArr) {
        this.desktopPoolArr = desktopPoolArr;
    }

    @Nullable
    public IdLabelEntry[] getDiskPoolArr() {
        return diskPoolArr;
    }

    public void setDiskPoolArr(@Nullable IdLabelEntry[] diskPoolArr) {
        this.diskPoolArr = diskPoolArr;
    }

    @Nullable
    public IdLabelEntry[] getDeskStrategyArr() {
        return deskStrategyArr;
    }

    public void setDeskStrategyArr(@Nullable IdLabelEntry[] deskStrategyArr) {
        this.deskStrategyArr = deskStrategyArr;
    }

    @Nullable
    public IdLabelEntry[] getAppPoolArr() {
        return appPoolArr;
    }

    public void setAppPoolArr(@Nullable IdLabelEntry[] appPoolArr) {
        this.appPoolArr = appPoolArr;
    }

    @Nullable
    public IdLabelEntry[] getAppMainStrategyArr() {
        return appMainStrategyArr;
    }

    public void setAppMainStrategyArr(@Nullable IdLabelEntry[] appMainStrategyArr) {
        this.appMainStrategyArr = appMainStrategyArr;
    }

    @Nullable
    public IdLabelEntry[] getAppPeripheralStrategyArr() {
        return appPeripheralStrategyArr;
    }

    public void setAppPeripheralStrategyArr(@Nullable IdLabelEntry[] appPeripheralStrategyArr) {
        this.appPeripheralStrategyArr = appPeripheralStrategyArr;
    }
}
