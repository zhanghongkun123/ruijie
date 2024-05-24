package com.ruijie.rcos.rcdc.rco.module.impl.datasync;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/25 16:34
 *
 * @author coderLee23
 */
public interface DataSyncBusinessKey {


    /**
     * 定时清理用户和用户组数据同步日志
     */
    String RCDC_QUARTZ_CLEAR_DATA_SYNC_LOG = "rcdc_quartz_clear_data_sync_log";

    /**
     * 用户组同步失败，原因：[{0}]域用户组[{1}]不存在
     */
    String RCDC_RCO_DATA_SYNC_AD_OR_LDAP_USER_GROUP_NOT_EXISTS = "23201902";


    /**
     * "同步用户[{0}]数据失败，失败原因：{1}"
     */
    String RCDC_RCO_SYNC_USER_DATA_FAIL = "rcdc_rco_sync_user_data_fail";

    /**
     * "同步用户组[{0}]数据失败，失败原因：{1}"
     */
    String RCDC_RCO_SYNC_USER_GROUP_DATA_FAIL = "rcdc_rco_sync_user_group_data_fail";


    /**
     * "AD域未启用，不支持同步"
     */
    String RCDC_RCO_SYNC_DATA_AD_NOT_ENABLED = "23200278";

    /**
     * "LDAP域未启用，不支持同步"
     */
    String RCDC_RCO_SYNC_DATA_LDAP_NOT_ENABLED = "23200279";

    /**
     * "第三方认证未启用，不支持同步"
     */
    String RCDC_RCO_SYNC_DATA_THIRD_PARTY_NOT_ENABLED = "23200280";

    /**
     * "{0}域服务器已存在多个相同账号[{1}],不支持同步"
     */
    String RCDC_RCO_SYNC_DATA_AD_OR_LDAP_USER_EXISTS = "23200281";

}
