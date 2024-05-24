package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;

/**
 * Description: 镜像应用软件版本实体类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/10
 *
 * @author wjp
 */
@Entity
@Table(name = "t_rco_image_template_app_version")
public class ImageTemplateAppVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String appVersion;

    private String lastAppVersion;

    @Enumerated(EnumType.STRING)
    private AppTypeEnum appType;

    private UUID imageId;

    @Version
    private Integer version;

    private Date updateTime;

    public ImageTemplateAppVersionEntity() {
        // Do nothing because of X and Y.
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public AppTypeEnum getAppType() {
        return appType;
    }

    public String getLastAppVersion() {
        return lastAppVersion;
    }

    public void setLastAppVersion(String lastAppVersion) {
        this.lastAppVersion = lastAppVersion;
    }

    public void setAppType(AppTypeEnum appType) {
        this.appType = appType;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
