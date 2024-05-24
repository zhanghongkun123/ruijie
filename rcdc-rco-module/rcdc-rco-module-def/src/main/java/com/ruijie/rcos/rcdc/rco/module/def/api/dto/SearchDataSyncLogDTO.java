package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/22 12:09
 *
 * @author coderLee23
 */
public class SearchDataSyncLogDTO {

    /**
     * 创建时间 - start
     */
    private Date startCreateTime;

    /**
     * 创建时间 - end
     */
    private Date endCreateTime;

    /**
     * 日志内容
     */
    private String content;

    public Date getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(Date startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public Date getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(Date endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
