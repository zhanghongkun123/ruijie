package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/16 19:57
 *
 * @author lihengjing
 */
@Entity
@Table(name = "v_rco_software_strategy_count")
public class ViewRcoSoftwareStrategyCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 软件策略名称
     */
    private String name;

    /**
     * 软件策略运行模式
     */
    private Boolean isWhitelistMode;

    /**
     * 软件策略软件数量
     */
    private Long count;

    /**
     * 软件策略描述
     */
    private String description;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsWhitelistMode() {
        return isWhitelistMode;
    }

    public void setIsWhitelistMode(Boolean whitelistMode) {
        isWhitelistMode = whitelistMode;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
