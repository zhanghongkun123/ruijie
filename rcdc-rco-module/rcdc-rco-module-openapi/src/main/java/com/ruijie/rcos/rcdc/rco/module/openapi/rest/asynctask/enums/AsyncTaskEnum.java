package com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums;

/**
 * Description: 异步任务类型，用于存库
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/26 18:36
 *
 * @author xiejian
 */
public enum  AsyncTaskEnum {

    /** 创建云桌面 */
    CREATE_VDI,

    /** 启动云桌面 */
    START_DESK,

    /** 关闭云桌面 */
    SHUTDOWN_DESK,

    /** 强制关闭云桌面 */
    POWER_OFF_DESK,

    /** 软删除VDI云桌面 */
    SOFT_DELETE_VDI,

    /** 修改云桌面虚拟机配置 */
    MODIFY_CONFIGURATION,

    /** 唤醒终端 */
    WAKE_UP_TERMINAL,

    /** 批量创建云桌面 */
    BATCH_CREATE_VDI,

    /** 批量同步Ad域用户 */
    BATCH_SYNC_AD_USER,

    /** 批量同步Ldap域用户 */
    BATCH_SYNC_LDAP_USER,

    /** 批量软删除VDI云桌面 */
    BATCH_SOFT_DELETE_VDI,

    /** 创建用户 */
    CREATE_USER,

    /** 创建用户 */
    DELETE_USER,

    /** 修改用户 */
    MODIFY_USER,

    /** 还原云桌面 */
    RESTORE_DESK,

    /** 修改云桌面策略 */
    MODIFY_DESK_STRATEGY,

    /** 批量修改云桌面标签 */
    BATCH_MODIFY_DESK_REMARK,

    /** 编辑桌面云桌面策略 */
    EDIT_DESK_STRATEGY,

    /** 批量编辑桌面云桌面策略 */
    BATCH_EDIT_DESK_STRATEGY,

    /** 批量启用用户 */
    BATCH_ENABLE_USER,

    /** 批量禁用用户 */
    BATCH_DISABLE_USER,

    /** 编辑用户 */
    EDIT_USER,

    /**
     * 批量绑定用户mac
     */
    BATCH_BIND_USER_MAC,

    /**
     * 批量删除用户和终端mac绑定关系
     */
    BATCH_DELETE_USER_MAC_BINDING,

    /**
     * 批量更新用户和终端mac绑定关系
     */
    BATCH_UPDATE_USER_MAC_BINDING

}
