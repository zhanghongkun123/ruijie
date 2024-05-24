package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public class SoftRelatedSoftStrategyDTO {


    /**
     * 软件策略Id
     **/
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
