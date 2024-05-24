package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 获取服务器资源web请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 16:10
 *
 * @author zhangyichi
 */
public class GetServerHistoryWebRequest implements WebRequest {

    @NotNull
    private UUID serverId;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    public UUID getServerId() {
        return serverId;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
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
