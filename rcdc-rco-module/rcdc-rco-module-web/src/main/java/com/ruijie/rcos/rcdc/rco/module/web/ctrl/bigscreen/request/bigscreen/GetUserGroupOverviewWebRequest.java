package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import java.util.Date;
import java.util.UUID;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/29 16:07
 *
 * @author zhangyichi
 */
public class GetUserGroupOverviewWebRequest implements WebRequest {

    @Nullable
    private UUID groupId;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
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
