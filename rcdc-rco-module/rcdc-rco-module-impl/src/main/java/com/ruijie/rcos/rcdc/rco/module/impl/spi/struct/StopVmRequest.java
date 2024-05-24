package com.ruijie.rcos.rcdc.rco.module.impl.spi.struct;

import java.util.UUID;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月7日
 * 
 * @author artom
 */
public class StopVmRequest {
    
    private UUID id;

    private Boolean isForce;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public Boolean getForce() {
        return isForce;
    }

    public void setForce(Boolean force) {
        isForce = force;
    }
}
