package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月08日
 *
 * @author zhk
 */
public class AppTestDeskAppInfoDTO implements Serializable {

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
    private UUID testId;

    /**
     * 测试id
     */
    private UUID deskId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用包
     */
    private String appType;

    /**
     * 应用id
     */
    private UUID appId;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 主键id
     */
    private UUID id;

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

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
