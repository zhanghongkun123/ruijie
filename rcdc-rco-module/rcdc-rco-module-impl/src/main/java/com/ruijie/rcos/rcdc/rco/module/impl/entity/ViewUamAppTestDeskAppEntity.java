package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月08日
 *
 * @author zhk
 */
@Table(name = "v_rco_uam_app_test_desk_app_detail")
@Entity
public class ViewUamAppTestDeskAppEntity {

    @Id
    private UUID id;

    /**
     * 应用测试状态
     */
    private String appTestState;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 测试id
     */
    private UUID  testId;

    /**
     * 应用名称
     */
    private String  appName;

    /**
     *应用包
     */
    private String appType;

    /**
     * 桌面id
     */
    private UUID deskId;

    /**
     * 应用id
     */
    private UUID appId;

    /**
     * 失败原因
     */
    private String failReason;

    @Version
    private Integer version;

    public String getAppTestState() {
        return appTestState;
    }

    public void setAppTestState(String appTestState) {
        this.appTestState = appTestState;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAppId() {
        return appId;
    }

    public void setAppId(UUID appId) {
        this.appId = appId;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
