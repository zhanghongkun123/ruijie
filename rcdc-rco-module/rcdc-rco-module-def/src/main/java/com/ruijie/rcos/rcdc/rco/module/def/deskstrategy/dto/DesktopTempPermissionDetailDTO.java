package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import java.util.List;

/**
 * Description: 临时权限详情
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-04-26
 *
 * @author linke
 */
public class DesktopTempPermissionDetailDTO extends CbbDesktopTempPermissionDTO {

    /**
     * 磁盘映射枚举，关闭、可读、读写
     */
    private DiskMappingEnum diskMappingType;

    /**
     * 桌面ID-名称列表
     */
    private List<IdLabelEntry> desktopList;

    /**
     * 用户ID-名称列表
     */
    private List<IdLabelEntry> userList;

    /**
     * 外设策略ID-名称列表
     */
    private List<IdLabelEntry> usbTypeList;

    public DiskMappingEnum getDiskMappingType() {
        return diskMappingType;
    }

    public void setDiskMappingType(DiskMappingEnum diskMappingType) {
        this.diskMappingType = diskMappingType;
    }

    public List<IdLabelEntry> getDesktopList() {
        return desktopList;
    }

    public void setDesktopList(List<IdLabelEntry> desktopList) {
        this.desktopList = desktopList;
    }

    public List<IdLabelEntry> getUserList() {
        return userList;
    }

    public void setUserList(List<IdLabelEntry> userList) {
        this.userList = userList;
    }

    public List<IdLabelEntry> getUsbTypeList() {
        return usbTypeList;
    }

    public void setUsbTypeList(List<IdLabelEntry> usbTypeList) {
        this.usbTypeList = usbTypeList;
    }
}
