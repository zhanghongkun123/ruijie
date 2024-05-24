package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant;

import java.util.UUID;

/**
 * Description: openapi常量
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/10 10:50
 *
 * @author lyb
 */
public interface Constant {

    /**
     * 云桌面id常量
     */
    String DESK_ID = "desk_id";

    /**
     * 任务id
     */
    String TASK_ID = "task_id";

    /**
     * 用户名
     */
    String USER_NAME = "user_name";

    /**
     * MAC地址
     */
    String TERMINAL_MAC = "terminal_mac";

    /**
     * 用户和终端mac绑定关系id
     */
    String BINDING_ID = "binding_id";

    /**
     * 用户和终端mac绑定关系状态
     */
    String BINDING_STATE = "state";

    /**
     * 是否回滚成功
     */
    String ROLLBACK = "rollback";

    /**
     * 用户id
     */
    String USER_ID = "user_id";

    /**
     * 云桌面名称
     */
    String DESK_NAME = "desk_name";

    /**
     * cbb云桌面id
     */
    String CBB_DESK_ID = "cbbDesktopId";

    /**
     * 前端云桌面类型
     */
    String DESK_TYPE = "deskType";

    /**
     * 数据库云桌面类型
     */
    String PATTERN = "pattern";

    /**
     * 创建云桌面线程
     */
    String CREATE_VDI_DESKTOP_THREAD = "async_create_vdi_desktop_thread";

    /**
     * 批量创建云桌面线程
     */
    String BATCH_CREATE_VDI_DESKTOP_THREAD = "async_batch_create_vdi_desktop_thread";

    /**
     * 批量软删除云桌面线程
     */
    String BATCH_SOFT_DELETE_VDI_DESKTOP_THREAD = "async_batch_soft_delete_vdi_desktop_thread";


    /**
     * 启动云桌面线程
     */
    String START_DESKTOP_THREAD = "async_start_desktop_thread";

    /**
     * 关闭云桌面线程
     */
    String SHUTDOWN_DESKTOP_THREAD = "async_shutdown_desktop_thread";

    /**
     * 强制关闭云桌面线程
     */
    String POWER_OFF_DESKTOP_THREAD = "async_power_off_desktop_thread";

    /**
     * 软删除云桌面线程
     */
    String DELETE_DESKTOP_THREAD = "async_soft_delete_desktop_thread";

    /**
     * 修改云桌面规格
     */
    String MODIFY_DESKTOP_CONFIGURATION_THREAD = "async_modify_desktop_configuration_thread";

    /**
     * 批量编辑云桌面标签线程
     */
    String BATCH_EDIT_VDI_DESK_REMARK_THREAD = "async_batch_edit_vdi_desk_remark_thread";

    /**
     * 变更桌面的云桌面策略线程
     */
    String EDIT_DESKTOP_STRATEGY_THREAD = "async_edit_desktop_strategy_thread";

    /**
     * 批量编辑云桌面策略线程
     */
    String BATCH_EDIT_DESKTOP_STRATEGY_THREAD = "async_batch_edit_desktop_strategy_thread";

    /**
     * 批量应用云桌面策略线程
     */
    String ASYNC_BATCH_APPLY_DESKTOP_STRATEGY_THREAD = "async_batch_apply_desktop_strategy_thread";

    /**
     * 批量同步Ad域用户线程
     */
    String BATCH_SYNC_AD_USER_THREAD = "async_batch_sync_ad_user_thread";

    /**
     * 批量同步Ldap域用户线程
     */
    String BATCH_SYNC_LDAP_USER_THREAD = "async_batch_sync_ldap_user_thread";

    /**
     * 创建用户线程
     */
    String CREATE_USER_THREAD = "async_create_user_thread";

    /**
     * 创建用户线程
     */
    String DELETE_USER_THREAD = "async_delete_user_thread";

    /**
     * 批量启用用户线程
     */
    String BATCH_ENABLE_USER_THREAD = "async_batch_enable_user_thread";

    /**
     * 批量禁用用户线程
     */
    String BATCH_DISABLE_USER_THREAD = "async_batch_disable_user_thread";

    /**
     * 编辑用户线程
     */
    String EDIT_USER_THREAD = "async_edit_user_thread";

    /**
     * 批量绑定用户mac线程
     */
    String BATCH_BIND_USER_MAC_THREAD = "async_batch_bind_user_mac_thread";

    /**
     * 批量删除用户mac绑定线程
     */
    String BATCH_DELETE_USER_MAC_BINDING_THREAD = "async_batch_delete_user_mac_binding_thread";

    /**
     * 批量更新用户mac绑定线程
     */
    String BATCH_UPDATE_USER_MAC_BINDING_THREAD = "async_batch_update_user_mac_binding_thread";

    /**
     * 用户组
     */
    String GROUP_ID = "group_id";

    /**
     * 初始密码
     */
    String INIT_PASSWORD = "123456";

    /**
     * 还原云桌面线程
     */
    String RESTORE_DESKTOP_THREAD = "async_restore_desktop_thread";

    /**
     * 默认存储池Id
     */
    UUID DEFAULT_STORAGE_POOL_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /**
     * 云桌面策略id常量
     */
    String DESK_STRATEGY_ID = "desk_strategy_id";

    /**
     * 修改云桌面策略线程
     */
    String MODIFY_DESK_STRATEGY_THREAD = "modify_desk_strategy_thread";
}
