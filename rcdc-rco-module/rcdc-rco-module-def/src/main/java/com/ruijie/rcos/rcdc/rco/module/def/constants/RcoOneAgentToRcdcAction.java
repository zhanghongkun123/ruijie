package com.ruijie.rcos.rcdc.rco.module.def.constants;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/21
 *
 * @author zqj
 */
public interface RcoOneAgentToRcdcAction {

    /**
     * pc终端首次连接
     */
    String COMPUTER_CONNECT = "computer_connect";


    /**
     * 上报系统信息
     */
    String REPORT_SYSTEM_INFO = "report_system_info";

    /**
     * 获取PC终端信息
     */
    String COMPUTER_INFO = "oa_get_cdc_computer_info";

    /**
     * 上报网卡信息
     */
    String REPORT_NETWORK_INFO = "oa_send_cdc_computer_info";

    /**
     * PC终端绑定用户
     */
    String COMPUTER_BIND_USER = "oa_notify_cdc_desk_bind_user";


    /**
     * 主机资源信息
     */
    String OA_SEND_CDC_HOST_INFO = "oa_send_cdc_host_info";

    /**
     * 上报windowsserver授权是否到期
     */
    String OA_SEND_CDC_WIN_SERVER_AUTHORITY = "oa_send_cdc_win_server_authority";

    /**
     * oa请求会话断开时长
     */
    String OA_GET_CDC_SESSION_IDLE_TIME = "oa_get_cdc_session_idle_time";


    /**
     * oa变更PC终端工作模式通知
     */
    String OA_NOTIFY_CDC_COMPUTER_WORK_MODEL = "oa_notify_cdc_computer_work_model";


    String OBTAIN_USER_LOGIN = "oa_user_login";

    String OA_SEND_CDC_SESSION_INFO = "oa_send_cdc_session_info";

    String OA_SEND_CDC_NEW_HOST_IP = "oa_send_cdc_new_host_ip";

    String OA_GET_CDC_USER_PROFILE_STRATEGY = "oa_get_cdc_user_profile_strategy";

    String OA_GET_CDC_SESSION_CONFIG = "oa_get_cdc_session_config";
}
