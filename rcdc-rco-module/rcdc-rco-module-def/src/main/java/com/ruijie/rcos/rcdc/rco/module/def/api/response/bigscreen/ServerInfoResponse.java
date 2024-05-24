package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.ServerUsageDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 获取服务器信息API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23 9:28
 *
 * @author zhangyichi
 */
public class ServerInfoResponse extends DefaultResponse {

    private ServerUsageDTO serverUsageDTO;

    public ServerUsageDTO getServerUsageDTO() {
        return serverUsageDTO;
    }

    public void setServerUsageDTO(ServerUsageDTO serverUsageDTO) {
        this.serverUsageDTO = serverUsageDTO;
    }
}
