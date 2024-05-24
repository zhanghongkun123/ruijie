package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import java.util.UUID;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月8日
 * 
 * @author nt
 */
public class CreateUserMsgResultVO {

    private UUID id;
    
    public CreateUserMsgResultVO(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
}
