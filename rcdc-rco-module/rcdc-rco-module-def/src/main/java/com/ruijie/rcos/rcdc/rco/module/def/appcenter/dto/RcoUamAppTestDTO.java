package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月28日
 *
 * @author zhk
 */
public class RcoUamAppTestDTO {

    private UUID id;

    private String name;

    private TestTaskStateEnum state;

    private UUID imageTemplateId;

    /**
     * 镜像模板名称
     */
    private String imageTemplateName;

    /**
     * 源镜像模板id
     */
    private UUID rootImageId;

    /**
     * 源镜像模板名称
     */
    private String rootImageName;

    private CbbImageType appSoftwarePackageType;

    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    private String osVersion;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 修改时间
     **/
    private Date updateTime;

    /**
     * 是否有更新
     **/
    private Boolean hasAppUpdate;

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

    public TestTaskStateEnum getState() {
        return state;
    }

    public void setState(TestTaskStateEnum state) {
        this.state = state;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public CbbImageType getAppSoftwarePackageType() {
        return appSoftwarePackageType;
    }

    public void setAppSoftwarePackageType(CbbImageType appSoftwarePackageType) {
        this.appSoftwarePackageType = appSoftwarePackageType;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getHasAppUpdate() {
        return hasAppUpdate;
    }

    public void setHasAppUpdate(Boolean hasAppUpdate) {
        this.hasAppUpdate = hasAppUpdate;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public UUID getRootImageId() {
        return rootImageId;
    }

    public void setRootImageId(UUID rootImageId) {
        this.rootImageId = rootImageId;
    }

    public String getRootImageName() {
        return rootImageName;
    }

    public void setRootImageName(String rootImageName) {
        this.rootImageName = rootImageName;
    }
}
