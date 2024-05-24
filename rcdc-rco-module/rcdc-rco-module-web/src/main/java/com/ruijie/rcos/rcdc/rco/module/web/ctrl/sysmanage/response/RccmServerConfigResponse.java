package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response;

/**
 * Description: 云桌面集群管理中心,纳管配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/20
 *
 * @author WuShengQiang
 */
public class RccmServerConfigResponse {

    private boolean hasJoin;

    private String serverIp;

    public boolean isHasJoin() {
        return hasJoin;
    }

    public void setHasJoin(boolean hasJoin) {
        this.hasJoin = hasJoin;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
