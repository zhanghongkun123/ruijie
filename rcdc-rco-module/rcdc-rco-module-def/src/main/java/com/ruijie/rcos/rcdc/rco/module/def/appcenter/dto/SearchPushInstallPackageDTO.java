package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DataSourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/17 15:14
 *
 * @author coderLee23
 */
public class SearchPushInstallPackageDTO extends BasePermissionDTO {

    /**
     * 应用id
     */
    private UUID id;

    /**
     * 支持应用名称模糊查询，忽略大小写
     */
    private String appName;

    /**
     * 过滤已被交付组添加的应用
     */
    private UUID filterGroupId;

    /**
     *  数据来源
     */
    private DataSourceTypeEnum dataSourceType;

    /**
     * 应用状态
     */
    private List<AppStatusEnum> appStatusList;

    /**
     * 操作系统类型
     */
    private OsPlatform osPlatform;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public UUID getFilterGroupId() {
        return filterGroupId;
    }

    public void setFilterGroupId(UUID filterGroupId) {
        this.filterGroupId = filterGroupId;
    }

    public DataSourceTypeEnum getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(DataSourceTypeEnum dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public List<AppStatusEnum> getAppStatusList() {
        return appStatusList;
    }

    public void setAppStatusList(List<AppStatusEnum> appStatusList) {
        this.appStatusList = appStatusList;
    }

    public OsPlatform getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(OsPlatform osPlatform) {
        this.osPlatform = osPlatform;
    }
}
