package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.vo;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ConnectMode;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月19日
 *
 * @author 徐国祥
 */
public class CloudPlatformVO {

    /**
     * 唯一标识
     */
    private UUID id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 类型
     */
    private CloudPlatformType type;

    /**
     * 扩展配置
     */
    private Object extendConfig;

    /**
     * 状态
     */
    private CloudPlatformStatus status;

    /**
     * 是否默认纳管
     */
    private Boolean shouldDefault;

    /**
     * 连接方式
     */
    private ConnectMode connectMode;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 云平台ID
     */
    private String cloudPlatformId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CloudPlatformType getType() {
        return type;
    }

    public void setType(CloudPlatformType type) {
        this.type = type;
    }

    public Object getExtendConfig() {
        return extendConfig;
    }

    public void setExtendConfig(Object extendConfig) {
        this.extendConfig = extendConfig;
    }

    public CloudPlatformStatus getStatus() {
        return status;
    }

    public void setStatus(CloudPlatformStatus status) {
        this.status = status;
    }

    public Boolean getShouldDefault() {
        return shouldDefault;
    }

    public void setShouldDefault(Boolean shouldDefault) {
        this.shouldDefault = shouldDefault;
    }

    public ConnectMode getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(ConnectMode connectMode) {
        this.connectMode = connectMode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCloudPlatformId() {
        return cloudPlatformId;
    }

    public void setCloudPlatformId(String cloudPlatformId) {
        this.cloudPlatformId = cloudPlatformId;
    }
}
