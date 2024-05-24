package com.ruijie.rcos.rcdc.rco.module.def.enums;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7
 *
 * @author ljm
 */
public enum UserOperNotifyEnum {
    /**
     * 添加
     */
    ADD_USER("addUser"),
    /**
     * 修改
     */
    UPDATE_USER("updateUser"),
    /**
     * 删除
     */
    DEL_USER("delUser"),
    /**
     * 全量
     */
    SYNC_USERS("syncUsers"),

    /**
     * 全量写入用户CMS通过FTP下载
     */
    SYNC_USERS_FOR_FTP("syncUsersForFtp");

    private String oper;

    public String getOper() {
        return oper;
    }

    UserOperNotifyEnum(String oper) {
        this.oper = oper;
    }
}
