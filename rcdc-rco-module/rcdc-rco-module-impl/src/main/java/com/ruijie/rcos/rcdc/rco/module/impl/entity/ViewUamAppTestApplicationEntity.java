package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
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
@Table(name = "v_rco_uam_app_test_application_detail")
@Entity
public class ViewUamAppTestApplicationEntity {

    @Id
    private UUID id;


    private UUID appId;

    /**
     *应用包类型
     */
    private String appType;

    /**
     * 测试id
     */
    private UUID  testId;

    /**
     * 应用名称
     */
    private String  appName;

    /**
     * 修改时间
     */
    private Date createTime;

    @Version
    private Integer version;


    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public UUID getAppId() {
        return appId;
    }

    public void setAppId(UUID appId) {
        this.appId = appId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
