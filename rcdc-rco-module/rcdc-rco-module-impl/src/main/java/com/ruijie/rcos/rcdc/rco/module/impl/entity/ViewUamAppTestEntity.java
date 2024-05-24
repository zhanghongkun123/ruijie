package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

/**
 * Description: 应用测试记录表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月26日
 *
 * @author zhk
 */
@Entity
@Table(name = "v_rco_uam_app_test")
public class ViewUamAppTestEntity {

    /**
     * 唯一标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 测试任务名称
     **/
    private String name;

    /**
     * 测试状态
     **/
    @Enumerated(value = EnumType.STRING)
    private TestTaskStateEnum state;

    /**
     * 镜像模板id
     */
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

    /**
     * 应用软件库类型IDV/VDI/TCI
     */
    @Enumerated(value = EnumType.STRING)
    private CbbImageType appSoftwarePackageType;

    /**
     * 操作系统类型
     **/
    @Enumerated(value = EnumType.STRING)
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

    /**
     * 版本号
     */
    @Version
    private int version;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
