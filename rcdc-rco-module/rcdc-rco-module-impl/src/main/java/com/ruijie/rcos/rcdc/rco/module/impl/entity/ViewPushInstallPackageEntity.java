package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;


/**
 * Description: 应用主表
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/22 12:43
 *
 * @author coderLee23
 */
@Table(name = "v_rco_uam_push_install_package")
@Entity
public class ViewPushInstallPackageEntity {
    @Id
    private UUID id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 应用类型
     */
    @Enumerated(value = EnumType.STRING)
    private AppTypeEnum appType;

    /**
     * 应用状态
     */
    @Enumerated(value = EnumType.STRING)
    private AppStatusEnum appStatus;

    /**
     * 关联的交付组数量
     */
    private Integer deliveryGroupCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 应用描述
     */
    private String appDesc;

    @Version
    private Integer version;

    /**
     * 操作系统类型【Windows、Linux】
     */
    @Enumerated(value = EnumType.STRING)
    private OsPlatform osPlatform;

    /**
     * 运行命令
     */
    private String manualQuietInstallParam;

    /**
     * 执行文件路径[针对压缩包格式使用]
     */
    private String executeFilePath;


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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public AppTypeEnum getAppType() {
        return appType;
    }

    public void setAppType(AppTypeEnum appType) {
        this.appType = appType;
    }

    public AppStatusEnum getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(AppStatusEnum appStatus) {
        this.appStatus = appStatus;
    }

    public Integer getDeliveryGroupCount() {
        return deliveryGroupCount;
    }

    public void setDeliveryGroupCount(Integer deliveryGroupCount) {
        this.deliveryGroupCount = deliveryGroupCount;
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

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public OsPlatform getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(OsPlatform osPlatform) {
        this.osPlatform = osPlatform;
    }

    public String getManualQuietInstallParam() {
        return manualQuietInstallParam;
    }

    public void setManualQuietInstallParam(String manualQuietInstallParam) {
        this.manualQuietInstallParam = manualQuietInstallParam;
    }

    public String getExecuteFilePath() {
        return executeFilePath;
    }

    public void setExecuteFilePath(String executeFilePath) {
        this.executeFilePath = executeFilePath;
    }
}
