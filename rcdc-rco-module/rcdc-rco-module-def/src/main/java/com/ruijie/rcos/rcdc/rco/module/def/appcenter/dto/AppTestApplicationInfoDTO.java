package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Version;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月08日
 *
 * @author zhk
 */
public class AppTestApplicationInfoDTO implements Serializable {

    /**
     * 主键id
     */
    private UUID id;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
