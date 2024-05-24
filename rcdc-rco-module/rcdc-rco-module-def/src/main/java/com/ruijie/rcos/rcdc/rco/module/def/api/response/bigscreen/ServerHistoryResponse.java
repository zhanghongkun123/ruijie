package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.ServerResourceUsageDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;

/**
 * Description: 获取服务器资源使用率（小时）API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23 9:31
 *
 * @author zhangyichi
 */
public class ServerHistoryResponse extends DefaultResponse {

    private List<ServerResourceUsageDTO> serverResourceUsageList;

    public List<ServerResourceUsageDTO> getServerResourceUsageList() {
        return serverResourceUsageList;
    }

    public void setServerResourceUsageList(List<ServerResourceUsageDTO> serverResourceUsageList) {
        this.serverResourceUsageList = serverResourceUsageList;
    }
}
