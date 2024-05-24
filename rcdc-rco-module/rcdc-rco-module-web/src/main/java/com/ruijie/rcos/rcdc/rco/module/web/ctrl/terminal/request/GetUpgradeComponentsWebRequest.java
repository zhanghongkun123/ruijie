package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 获取组件独立升级包组件列表请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/17
 *
 * @author lyb
 */
public class GetUpgradeComponentsWebRequest {

    @NotNull
    private UUID upgradeId;

    public UUID getUpgradeId() {
        return upgradeId;
    }

    public void setUpgradeId(UUID upgradeId) {
        this.upgradeId = upgradeId;
    }
}
