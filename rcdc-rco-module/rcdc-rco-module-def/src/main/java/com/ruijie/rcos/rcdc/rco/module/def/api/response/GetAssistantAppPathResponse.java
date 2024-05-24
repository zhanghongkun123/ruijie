package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/9 16:28
 *
 * @author ketb
 */
public class GetAssistantAppPathResponse extends DefaultResponse {

    private String path;

    public GetAssistantAppPathResponse(String path) {
        this.setStatus(Status.SUCCESS);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
