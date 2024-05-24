package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 应用客户端详情请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/16 15:38
 *
 * @author zhangyichi
 */
public class AppTerminalDetailRequest {

    @NotNull
    private Integer id;

    public AppTerminalDetailRequest() {
    }

    public AppTerminalDetailRequest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
