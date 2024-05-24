package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbDesktopTempPermissionState;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 存放临时权限信息
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20 19:53
 *
 * @author linrenjian
 */
public class DesktopTempPermissionDTO  extends EqualsHashcodeSupport {

    /**
     * 桌面临时权限信息
     */
    private CbbDesktopTempPermissionDTO permission;


    /**
     * USB 外设集合
     */
    private List<UUID> usbTypeList;


    /**
     * 权限删除状态
     */
    private CbbDesktopTempPermissionState state;

    public CbbDesktopTempPermissionState getState() {
        return state;
    }

    public void setState(CbbDesktopTempPermissionState state) {
        this.state = state;
    }

    public CbbDesktopTempPermissionDTO getPermission() {
        return permission;
    }

    public void setPermission(CbbDesktopTempPermissionDTO permission) {
        this.permission = permission;
    }

    public List<UUID> getUsbTypeList() {
        return usbTypeList;
    }

    public void setUsbTypeList(List<UUID> usbTypeList) {
        this.usbTypeList = usbTypeList;
    }
}
