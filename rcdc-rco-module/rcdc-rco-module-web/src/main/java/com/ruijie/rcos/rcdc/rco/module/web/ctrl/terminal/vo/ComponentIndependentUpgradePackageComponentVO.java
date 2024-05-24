package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalComponentIndependentUpgradePackageComponentDTO;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 组件独立升级包包含组件视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/21
 *
 * @author lyb
 */
public class ComponentIndependentUpgradePackageComponentVO {

    private UUID id;

    private UUID packageId;

    private String componentName;

    private String componentVersion;

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

    public ComponentIndependentUpgradePackageComponentVO(CbbTerminalComponentIndependentUpgradePackageComponentDTO componentDTO) {
        Assert.notNull(componentDTO, "componentDTO cannot be null!");
        this.id = componentDTO.getId();
        this.packageId = componentDTO.getPackageId();
        this.componentName = componentDTO.getComponentName();
        this.componentVersion = componentDTO.getComponentVersion();
    }
}
