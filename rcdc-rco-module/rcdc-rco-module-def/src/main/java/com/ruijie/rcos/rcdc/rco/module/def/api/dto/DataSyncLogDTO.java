package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/22 12:06
 *
 * @author coderLee23
 */
public class DataSyncLogDTO {

    /**
     * 主键ID
     */
    private UUID id;

    /**
     * 日志记录内容
     */
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
