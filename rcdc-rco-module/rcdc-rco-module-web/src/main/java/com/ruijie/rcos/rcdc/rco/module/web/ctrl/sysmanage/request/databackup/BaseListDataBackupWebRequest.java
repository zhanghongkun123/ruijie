package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.databackup;

import java.util.Date;

import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.lang.Nullable;

/**
 * Description: 数据库备份列表web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月06日
 *
 * @author fyq
 */
public class BaseListDataBackupWebRequest extends PageWebRequest {

    @Nullable
    private Date startTime;

    @Nullable
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}