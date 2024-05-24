package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 分发文件Entity
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/14 17:38
 *
 * @author zhangyichi
 */
@Entity
@Table(name = "t_rco_distribute_file")
public class DistributeFileEntity {

    @Id
    private UUID id;

    /**
     * 文件名称
     **/
    private String fileName;

    /**
     * 文件大小
     **/
    private Long fileSize;

    /**
     * 描述
     **/
    private String description;

    @Version
    private int version;

    /**
     * 上传时间
     **/
    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
