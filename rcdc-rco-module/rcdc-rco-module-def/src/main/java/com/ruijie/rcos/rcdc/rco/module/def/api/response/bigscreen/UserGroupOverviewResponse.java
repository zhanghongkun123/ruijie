package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.GroupAvgUsageDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;

/**
 * Description: 获取用户组使用率概览API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23 9:34
 *
 * @author zhangyichi
 */
public class UserGroupOverviewResponse extends DefaultResponse {

    private Long online = 0L;

    private Long total = 0L;

    private List<GroupAvgUsageDTO> resourceAvgUsageList;

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<GroupAvgUsageDTO> getResourceAvgUsageList() {
        return resourceAvgUsageList;
    }

    public void setResourceAvgUsageList(List<GroupAvgUsageDTO> resourceAvgUsageList) {
        this.resourceAvgUsageList = resourceAvgUsageList;
    }
}
