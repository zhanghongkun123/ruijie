package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年7月14日
 *
 * @author chenl
 */
public class SoftRelatedSoftStrategyRelatedDTO extends SoftwareStrategyDTO {

    private Boolean related = false;

    public Boolean getRelated() {
        return related;
    }

    public void setRelated(Boolean related) {
        this.related = related;
    }
}
