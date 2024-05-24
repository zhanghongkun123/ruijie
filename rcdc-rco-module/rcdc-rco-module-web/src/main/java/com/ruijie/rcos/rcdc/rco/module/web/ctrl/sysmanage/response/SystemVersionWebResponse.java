package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response;

/**
 * Description: 系统版本响应对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月13日
 *
 * @author nt
 */
public class SystemVersionWebResponse {

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
