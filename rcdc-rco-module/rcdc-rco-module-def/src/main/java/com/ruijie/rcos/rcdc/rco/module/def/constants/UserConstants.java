package com.ruijie.rcos.rcdc.rco.module.def.constants;

/**
 * Description: Function Description 用户组静态常量
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年06月30日
 *
 * @author linrenjian
 */

public interface UserConstants {

    String USER_GROUP_ROOT_ID = "root";

    String USER_GROUP_ROOT_NAME = "总览";

    /**
     * 全局表中保存第三方配置信息
     */
    String THIRD_PARTY_AUTH_PLATFORM_CONFIG = "third_party_auth_platform_config";

    /**
     * 第三方用户同步定时任务
     */
    String THIRD_PARTY_USER_SYNC_CODE = "third_party_user_sync_code";

    String IAC_USER_API_USER_GROUP_HAS_DUPLICATION_NAME = "66065011";
    String IAC_USER_API_DELETE_USER_GROUP_SUB_GROUP_NAME_DUPLICATION_WITH_MOVE_SUB_GROUP = "66065012";
    String IAC_USER_API_USER_GROUP_NUM_OVER = "66065015";
    String IAC_USER_API_SUB_USER_GROUP_NUM_OVER = "66065016";
}
