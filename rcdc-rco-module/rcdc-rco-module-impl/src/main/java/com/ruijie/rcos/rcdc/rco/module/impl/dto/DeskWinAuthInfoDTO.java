package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.io.Serializable;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/26
 *
 * @author zqj
 */
public class DeskWinAuthInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 激活状态
     */
    private Boolean hasAction;

    public Boolean getHasAction() {
        return hasAction;
    }

    public void setHasAction(Boolean hasAction) {
        this.hasAction = hasAction;
    }
}
