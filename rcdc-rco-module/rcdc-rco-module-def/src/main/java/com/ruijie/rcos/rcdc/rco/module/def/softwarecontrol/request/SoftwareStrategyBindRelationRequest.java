package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareRelationTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 绑定对象类型：DESKTOP_POOL桌面池，其他待增加
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/31
 *
 * @author linke
 */
public class SoftwareStrategyBindRelationRequest {

    @NotNull
    private UUID softwareStrategyId;

    @NotNull
    private SoftwareRelationTypeEnum relationType;

    @NotNull
    private UUID relationId;

    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    public SoftwareRelationTypeEnum getRelationType() {
        return relationType;
    }

    public void setRelationType(SoftwareRelationTypeEnum relationType) {
        this.relationType = relationType;
    }

    public UUID getRelationId() {
        return relationId;
    }

    public void setRelationId(UUID relationId) {
        this.relationId = relationId;
    }
}
