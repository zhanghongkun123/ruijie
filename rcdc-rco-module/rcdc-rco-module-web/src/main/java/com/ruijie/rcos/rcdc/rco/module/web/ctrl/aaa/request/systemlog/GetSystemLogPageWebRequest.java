package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.systemlog;

import java.util.Date;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月27日
 * 
 * @author zhuangchenwu
 */
public class GetSystemLogPageWebRequest extends PageWebRequest {

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
