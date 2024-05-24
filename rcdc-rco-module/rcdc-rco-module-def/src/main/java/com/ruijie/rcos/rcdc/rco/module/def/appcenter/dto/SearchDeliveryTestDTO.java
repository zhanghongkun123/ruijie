package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 23:05
 *
 * @author coderLee23
 */
public class SearchDeliveryTestDTO extends BasePermissionDTO {


    /**
     * 跳转使用
     */
    private UUID id;

    /**
     *  测试任务名称
     */
    private String name;

    /**
     *  应用id
     */
    private UUID appId;


    /**
     * 虚拟机类型
     */
    private List<CbbImageType> appSoftwarePackageTypeList;

    /**
     * 测试状态
     */
    private List<TestTaskStateEnum> stateList;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getAppId() {
        return appId;
    }

    public void setAppId(UUID appId) {
        this.appId = appId;
    }

    public List<CbbImageType> getAppSoftwarePackageTypeList() {
        return appSoftwarePackageTypeList;
    }

    public void setAppSoftwarePackageTypeList(List<CbbImageType> appSoftwarePackageTypeList) {
        this.appSoftwarePackageTypeList = appSoftwarePackageTypeList;
    }

    public List<TestTaskStateEnum> getStateList() {
        return stateList;
    }

    public void setStateList(List<TestTaskStateEnum> stateList) {
        this.stateList = stateList;
    }
}
