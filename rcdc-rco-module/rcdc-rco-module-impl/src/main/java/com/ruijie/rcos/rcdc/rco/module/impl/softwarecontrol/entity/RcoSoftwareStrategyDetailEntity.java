package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareStrategyRelatedTypeEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@Entity
@Table(name = "t_rco_software_strategy_detail")
public class RcoSoftwareStrategyDetailEntity extends EqualsHashcodeSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 软件策略ID
     **/
    private UUID strategyId;

    /**
     * 软件策略关联对象ID
     **/
    private UUID relatedId;

    /**
     * 软件策略关联对象类型 （软件/软件分组）
     **/
    @Enumerated(EnumType.STRING)
    private SoftwareStrategyRelatedTypeEnum relatedType;

    @Version
    private int version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public SoftwareStrategyRelatedTypeEnum getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(SoftwareStrategyRelatedTypeEnum relatedType) {
        this.relatedType = relatedType;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
