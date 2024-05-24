package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

/**
 * 
 * Description: 角色命名唯一性校验
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月14日
 * 
 * @author Ghang
 */
public class RoleCheckDuplicationWebResponse {

    private boolean hasDuplication;

    public boolean isHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
   
}
