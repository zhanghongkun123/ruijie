package com.ruijie.rcos.rcdc.rco.module.def.api.response.rccm;

/**
 * Description: 检查是否可以退出纳管
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/20
 *
 * @author WuShengQiang
 */
public class RccmCheckExitManageResponse {

    /**
     * true:允许退出 false:不允许退出
     */
    private boolean allow;

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }
}
