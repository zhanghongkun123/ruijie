package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 管理员数据权限VO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月30日
 *
 * @author lihengjing
 */
public class DataPermissionVO {

    @ApiModelProperty(value = "用户组")
    private GroupIdLabelEntry[] userGroupArr;

    @ApiModelProperty(value = "终端组")
    private GroupIdLabelEntry[] terminalGroupArr;

    @ApiModelProperty(value = "镜像组")
    private GroupIdLabelEntry[] imageArr;

    @ApiModelProperty(value = "桌面池组")
    private GroupIdLabelEntry[] desktopPoolArr;

    @ApiModelProperty(value = "磁盘池组")
    private GroupIdLabelEntry[] diskPoolArr;

    @ApiModelProperty(value = "云桌面策略组")
    private GroupIdLabelEntry[] deskStrategyArr;

    @ApiModelProperty(value = "应用池组")
    private GroupIdLabelEntry[] appPoolArr;

    @ApiModelProperty(value = "云应用策略组")
    private GroupIdLabelEntry[] appMainStrategyArr;

    @ApiModelProperty(value = "云应用外设策略组")
    private GroupIdLabelEntry[] appPeripheralStrategyArr;

    public GroupIdLabelEntry[] getUserGroupArr() {
        return userGroupArr;
    }

    public void setUserGroupArr(GroupIdLabelEntry[] userGroupArr) {
        this.userGroupArr = userGroupArr;
    }

    public GroupIdLabelEntry[] getTerminalGroupArr() {
        return terminalGroupArr;
    }

    public void setTerminalGroupArr(GroupIdLabelEntry[] terminalGroupArr) {
        this.terminalGroupArr = terminalGroupArr;
    }

    public GroupIdLabelEntry[] getImageArr() {
        return imageArr;
    }

    public void setImageArr(GroupIdLabelEntry[] imageArr) {
        this.imageArr = imageArr;
    }

    public GroupIdLabelEntry[] getDesktopPoolArr() {
        return desktopPoolArr;
    }

    public void setDesktopPoolArr(GroupIdLabelEntry[] desktopPoolArr) {
        this.desktopPoolArr = desktopPoolArr;
    }

    public GroupIdLabelEntry[] getDiskPoolArr() {
        return diskPoolArr;
    }

    public void setDiskPoolArr(GroupIdLabelEntry[] diskPoolArr) {
        this.diskPoolArr = diskPoolArr;
    }

    public GroupIdLabelEntry[] getDeskStrategyArr() {
        return deskStrategyArr;
    }

    public void setDeskStrategyArr(GroupIdLabelEntry[] deskStrategyArr) {
        this.deskStrategyArr = deskStrategyArr;
    }

    public GroupIdLabelEntry[] getAppPoolArr() {
        return appPoolArr;
    }

    public void setAppPoolArr(GroupIdLabelEntry[] appPoolArr) {
        this.appPoolArr = appPoolArr;
    }

    public GroupIdLabelEntry[] getAppMainStrategyArr() {
        return appMainStrategyArr;
    }

    public void setAppMainStrategyArr(GroupIdLabelEntry[] appMainStrategyArr) {
        this.appMainStrategyArr = appMainStrategyArr;
    }

    public GroupIdLabelEntry[] getAppPeripheralStrategyArr() {
        return appPeripheralStrategyArr;
    }

    public void setAppPeripheralStrategyArr(GroupIdLabelEntry[] appPeripheralStrategyArr) {
        this.appPeripheralStrategyArr = appPeripheralStrategyArr;
    }
}
