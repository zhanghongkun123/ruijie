package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity;

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
@Table(name = "t_rco_software_strategy")
public class RcoSoftwareStrategyEntity extends EqualsHashcodeSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    /**
     * 软件策略名称
     **/
    private String name;

    /**
     * 是否白名单运行
     **/
    private Boolean isWhitelistMode;

    /**
     * 软件策略描述
     **/
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
