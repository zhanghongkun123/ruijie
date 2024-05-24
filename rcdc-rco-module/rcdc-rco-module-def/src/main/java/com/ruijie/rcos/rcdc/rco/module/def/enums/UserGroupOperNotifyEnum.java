package com.ruijie.rcos.rcdc.rco.module.def.enums;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7
 *
 * @author wjp
 */
public enum UserGroupOperNotifyEnum {
    /**
     * 新增、修改用户组时同步
     */
    SYNC_USER_GROUP("syncUserGroup"),
    /**
     * 删除用户组时同步
     */
    DEL_USER_GROUP("delUserGroup"),
    /**
     * 全量同步用户组
     */
    SYNC_USER_GROUPS("syncUserGroups");

    private String oper;

    public String getOper() {
        return oper;
    }

    UserGroupOperNotifyEnum(String oper) {
        this.oper = oper;
    }
}
