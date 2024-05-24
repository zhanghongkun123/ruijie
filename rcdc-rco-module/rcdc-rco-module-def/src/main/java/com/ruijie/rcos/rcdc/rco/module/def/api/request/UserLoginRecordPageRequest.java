package com.ruijie.rcos.rcdc.rco.module.def.api.request;


import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import java.util.Date;

/**
 * Description: 用户登录记录分页条件
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/2 17:10
 *
 * @author zjy
 */
public class UserLoginRecordPageRequest extends PageSearchRequest {

    private Date startTime;

    private Date endTime;

    public UserLoginRecordPageRequest() {
    }

    public UserLoginRecordPageRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

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