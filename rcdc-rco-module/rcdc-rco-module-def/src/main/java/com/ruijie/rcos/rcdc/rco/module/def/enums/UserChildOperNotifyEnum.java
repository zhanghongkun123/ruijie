package com.ruijie.rcos.rcdc.rco.module.def.enums;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年03月01日
 *
 * @author xgx
 */
public enum UserChildOperNotifyEnum {
    UPDATE_USER_PASSWORD("updateUserPassword");

    private String oper;

    public String getOper() {
        return oper;
    }

    UserChildOperNotifyEnum(String oper) {
        this.oper = oper;
    }
}
