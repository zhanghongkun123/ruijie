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
@Table(name = "v_rco_soft_related_soft_strategy")
public class ViewSoftRelatedSoftStrategyEntity extends EqualsHashcodeSupport {

    /**
     * 软件策略Id
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    private UUID softId;

    /**
     * 软件策略名
     */
    private String name;


    /**
     * 软件策略描述
     */
    private String description;

    /**
     * 软件策略类型 内置、自定义
     */
    private Boolean isWhitelistMode;


    /**
     * 软件名称
     **/
    private Long count ;

    @Version
    private int version;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSoftId() {
        return softId;
    }

    public void setSoftId(UUID softId) {
        this.softId = softId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

