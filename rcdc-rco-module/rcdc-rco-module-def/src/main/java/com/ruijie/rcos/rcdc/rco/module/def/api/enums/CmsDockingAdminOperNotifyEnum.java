package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * Description: CMS对接管理员同步类型
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7
 *
 * @author ljm
 */
public enum CmsDockingAdminOperNotifyEnum {

    /**
     *  新增/修改
     */
    SYNC_ADMIN("syncAdmin"),
    /**
     * 全量
     */
    SYNC_ADMINS("syncAdmins"),
    /**
     * 删除
     */
    SYNC_DEL_ADMIN("delAdmin");

    private String oper;

    public String getOper() {
        return oper;
    }

    CmsDockingAdminOperNotifyEnum(String oper) {
        this.oper = oper;
    }
}
