package com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 按id查询云桌面使用率API请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 17:45
 *
 * @author zhangyichi
 */
public class DesktopUsageIdArrRequest implements Request {

    @NotEmpty
    private UUID[] idArr;

    @NotNull
    private Date startTime;

    @NotNull
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

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

}
