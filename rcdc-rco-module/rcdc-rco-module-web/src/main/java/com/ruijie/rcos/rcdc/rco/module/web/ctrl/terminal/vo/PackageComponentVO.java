package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalComponentIndependentUpgradePackageComponentDTO;

import java.util.UUID;

/**
 * Description: 获取组件独立升级包组件列表响应视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/23
 *
 * @author lyb
 */
public class PackageComponentVO {

    private UUID id;

    private UUID packageId;

    private String componentName;

    private String componentVersion;

    public PackageComponentVO(CbbTerminalComponentIndependentUpgradePackageComponentDTO dto) {
        this.id = dto.getId();
        this.packageId = dto.getPackageId();
        this.componentName = dto.getComponentName();
        this.componentVersion = dto.getComponentVersion();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
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
