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
public class RebootVmRequest {

    private UUID deskId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
