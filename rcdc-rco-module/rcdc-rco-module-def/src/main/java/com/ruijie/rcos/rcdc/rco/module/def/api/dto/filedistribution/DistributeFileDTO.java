package com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution;

import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 分发文件DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/11 15:09
 *
 * @author zhangyichi
 */
@PageQueryDTOConfig(entityType = "DistributeFileEntity")
public class DistributeFileDTO {

    private UUID id;

    private String fileName;

    private Long fileSize;

    private String description;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
