package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;

import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@PageQueryDTOConfig(entityType = "ViewRcoSoftwareStrategyCountEntity")
public class SoftwareStrategyDTO {
    /**
     * 软件策略 id
     */
    private UUID id;

    /**
     * 软件策略名称
     */
    private String name;

    /**
     * 软件分组ID及软件ID
     */
    private SoftwareStrategyDetailDTO[] softwareGroupArr;

    /**
     * 软件策略类型 内置、自定义
     */
    private Boolean isWhitelistMode;

    /**
     * 软件策略描述
     */
    private String description;

    /**
     * 软件个数
     */
    private Long count;

    private Integer version;

    /**
     *
     * @return 策略版本
     */
    public String generateStrategyVersion() {
        return getId() + Constants.UNDERLINE + getVersion();
    }

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

    public SoftwareStrategyDetailDTO[] getSoftwareGroupArr() {
        return softwareGroupArr;
    }

    public void setSoftwareGroupArr(SoftwareStrategyDetailDTO[] softwareGroupArr) {
        this.softwareGroupArr = softwareGroupArr;
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
