package com.ruijie.rcos.rcdc.rco.module.def.api.version.response;

/**
 * 
 * Description: 查询当前服务器版本回应类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/1
 *
 * @author zhiweiHong
 */
public class VersionResponse {

    private String version;

    private String time;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
