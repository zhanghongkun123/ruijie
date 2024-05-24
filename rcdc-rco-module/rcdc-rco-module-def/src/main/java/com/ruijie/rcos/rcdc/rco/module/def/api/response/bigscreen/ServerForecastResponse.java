package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.ServerForecastDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月25日
 * 
 * @author wanmulin
 */
public class ServerForecastResponse extends DefaultResponse {

    private ServerForecastDTO serverForecast;

    public ServerForecastDTO getServerForecast() {
        return serverForecast;
    }

    public void setServerForecast(ServerForecastDTO serverForecast) {
        this.serverForecast = serverForecast;
    }
}
