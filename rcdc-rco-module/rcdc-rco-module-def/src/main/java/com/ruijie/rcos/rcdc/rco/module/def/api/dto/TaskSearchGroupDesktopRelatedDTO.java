package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.BasePermissionDTO;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/09 17:09
 *
 * @author coderLee23
 */
public class TaskSearchGroupDesktopRelatedDTO extends BasePermissionDTO {

    private UUID groupId;

    private List<UUID> groupIdList;

    /**
     * 支持云桌面名称和终端名称模糊查询
     */
    private String searchName;


    /**
     * 桌面状态
     */
    private List<CbbCloudDeskState> deskStateList;

    private List<CloudPlatformStatus> platformStatusList;

    private List<CloudPlatformType> platformTypeList;


    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public List<UUID> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<UUID> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public List<CbbCloudDeskState> getDeskStateList() {
        return deskStateList;
    }

    public void setDeskStateList(List<CbbCloudDeskState> deskStateList) {
        this.deskStateList = deskStateList;
    }

    public List<CloudPlatformStatus> getPlatformStatusList() {
        return platformStatusList;
    }

    public void setPlatformStatusList(List<CloudPlatformStatus> platformStatusList) {
        this.platformStatusList = platformStatusList;
    }

    public List<CloudPlatformType> getPlatformTypeList() {
        return platformTypeList;
    }

    public void setPlatformTypeList(List<CloudPlatformType> platformTypeList) {
        this.platformTypeList = platformTypeList;
    }
}
