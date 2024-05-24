package com.ruijie.rcos.rcdc.rco.module.impl.special.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户特殊配置实体类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/15
 *
 * @author zwf
 */
@Entity
@Table(name = "t_rco_user_profile_special_config")
public class UserProfileSpecialConfigEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 配置文件md5
     */
    private String configMd5;

    /**
     * 配置版本号
     */
    private Long configVersion;

    /**
     * 特殊配置内容
     */
    private String configContent;

    @Version
    private Integer version;

    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(Long configVersion) {
        this.configVersion = configVersion;
    }

    public String getConfigContent() {
        return configContent;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getConfigMd5() {
        return configMd5;
    }

    public void setConfigMd5(String configMd5) {
        this.configMd5 = configMd5;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
