package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalComponentIndependentUpgradeComponentDTO;

import java.util.UUID;

/**
 * Description: 获取组件独立升级任务组件列表响应视图
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/3
 *
 * @author lyb
 */
public class UpgradeComponentVO {

    private UUID id;

    private UUID upgradeId;

    private String componentName;

    private String componentVersion;

    public UpgradeComponentVO(CbbTerminalComponentIndependentUpgradeComponentDTO dto) {
        this.id = dto.getId();
        this.upgradeId = dto.getUpgradeId();
        this.componentName = dto.getComponentName();
        this.componentVersion = dto.getComponentVersion();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUpgradeId() {
        return upgradeId;
    }

    public void setUpgradeId(UUID upgradeId) {
        this.upgradeId = upgradeId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
        this.componentVersion = componentVersion;
    }
}
