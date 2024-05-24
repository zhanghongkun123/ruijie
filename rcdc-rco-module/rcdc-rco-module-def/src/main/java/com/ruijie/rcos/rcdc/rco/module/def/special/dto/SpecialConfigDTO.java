package com.ruijie.rcos.rcdc.rco.module.def.special.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/18
 *
 * @author chenjiehui
 */
public class SpecialConfigDTO {

    /**
     * 主键
     */
    private UUID id;

    /**
     * 文件名
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
     * 配置内容
     */
    private String configContent;

    /**
     * 创建时间
     */
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

    public String getConfigMd5() {
        return configMd5;
    }

    public void setConfigMd5(String configMd5) {
        this.configMd5 = configMd5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
