package com.ruijie.rcos.rcdc.rco.module.impl.spi.common;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author Jarman
 */
public interface ShineAction {

    /**
     * 终端用户登录
     */
    String LOGIN = "user_login";

    /**
     * 终端用户扫码登录
     */
    String QR_LOGIN = "qr_user_login";

    /**
     * 终端用户扫码登录-查找用户信息
     */
    String QRY_QR_INFO = "qry_qr_info";

    /**
     * 启动虚机
     */
    String START_VM = "start_vm";

    /**
     * 终端访客用户登录
     */
    String VISITOR_LOGIN = "visitor_user_login";

    /**
     * 退出登录
     */
    String LOGIN_OUT = "user_login_out";

    /**
     * 修改终端用户密码
     */
    String CHANGE_TERMINAL_USER_PASSWORD = "change_user_password";

    /**
     * 云桌面在不同终端重复登录
     */
    String REPEAT_START_VM_IN_DIFFERENT_PLACE = "repeat_start_vm_in_different_place";


    /**
     * 外设策略到齐，退出EST窗口
     */
    String TEMP_PERMISSION_EXPIRE_NOTIFY = "temp_permission_expire_notify";

    /**
     * 查询云桌面
     */
    String RCDC_SHINE_CMM_QUERY_DESKTOP = "query_desktop";

    /**
     * 关闭云桌面完成
     */
    String RCDC_SHINE_CMM_SHUTDOWN_VM_FINISH = "shutdown_vm_finish";

    /**
     * 桌面和终端绑定
     */
    String BIND_DESKTOP_TERMINAL = "bind_desktop_terminal";

    /**
     * 上报终端模式设置
     */
    String REPORT_TERMINAL_SETTING = "report_terminal_setting";

    /**
     * IDV终端上报访客登录设置
     */
    String RCDC_SHINE_CMM_VISITOR_SETTING = "report_visitor_setting";

    /**
     * IDV终端上报终端初始化选择
     */
    String RCDC_SHINE_CMM_TERMINAL_INIT_CHOICE = "terminal_init_choice";

    /**
     * IDV终端上报终端初始化结果
     */
    String RCDC_SHINE_CMM_TERMINAL_INIT_RESULT = "terminal_init_result";

    /**
     * IDV终端获取终端组的桌面配置信息
     */
    String RCDC_SHINE_GET_IDV_TERMINAL_GROUP_DESK_CONFIG = "get_terminal_group_info";

    /**
     * 获取用户信息
     */
    String GET_USER_INFO = "get_user_info";

    /**
     * 同步用户信息
     */
    String SYNC_USER_INFO = "sync_user_info";

    /**
     * 获取打印机配置开关状态
     */
    String GET_PRINTER_CONFIG_STATUS = "get_printer_config_status";

    /**
     * 获取请求远程协助状态
     */
    String GET_REMOTE_ASSIST_REQUEST_STATUS = "get_remote_assist_request_status";

    /**
     * 更新wifi白名单指令
     */
    String UPDATE_WIFI_WHITELIST = "update_wifi_whitelist";
    /**
     * 获取wifi列表
     */
    String GET_WIFI_WHITELIST = "get_wifi_whitelist";

    /**
     * 获取网卡工作模式
     */
    String GET_NETWORK_MODEL = "get_network_model";

    /**
     * 设置网卡工作模式
     */
    String SET_NETWORK_MODEL = "set_network_model";

    /**
     * 用户请求进行远程协助
     */
    String EST_RA_USER_REQ = "remote_assist_user_request";

    /**
     * 用户取消进行远程协助
     */
    String EST_RA_USER_CANCEL = "remote_assist_user_cancel";

    /**
     * 通知云桌面更新镜像
     */
    String NOTIFY_IMAGE_CHANGED = "notify_image_changed";

    /**
     * 获取配置策略
     */
    String GET_CERTIFICATION_STRATEGY = "get_certification_strategy";

    /**
     * 解锁终端管理密码
     */
    String UNLOCK_TERMINAL_MANAGER_PWD = "unlock_terminal_manager_pwd";

    /**
     * 终端上报锁定信息
     */
    String SEND_TERMINAL_LOCK_STATE = "send_terminal_lock_state";

    /**
     * 获取用户动态口令配置
     */
    String GET_USER_OTP_CONFIG = "get_user_otp_config";

    /**
     * 用户动态口令校验
     */
    String CHECK_USER_OTP_CODE = "check_user_otp_code";

    /**
     * 用户动态口令登录
     */
    String USER_OTP_CODE_LOGIN = "user_otp_code_login";

    /**
     * 获取全局配置参数
     */
    String GET_GLOBAL_PARAMETER = "get_global_parameter";

    /**
     * 修改动态口令全局配置
     */
    String UPDATE_OTP_CODE = "update_otp_code";

    /**
     * 同步终端、服务器用户锁定信息以及防爆策略
     */
    String SYNC_USER_SECURITY_INFO = "sync_user_security_info";

    /**
     * 获取全局配置
     */
    String GET_RCO_GLOBAL_PARAMETER = "get_rco_global_parameter";

    /**
     * 获取全局windows密码配置开关
     */
    String GET_WINDOWS_PASSWORD_CONFIG_STATUS = "get_windows_password_config_status";

    /**
     * 获取安全认证配置
     */
    String GET_CERTIFIED_SECURITY_CONFIG = "get_certified_security_config";

    /**
     * 同步评测功能策略
     */
    String SYNC_EVALUATION_STRATEGY = "sync_evaluation_strategy";

    /**
     * 获取CAS扫码认证信息
     */
    String CAS_QUERY_QR_CONFIG = "cas_query_qr_config";

    /**
     * 获取二维码
     */
    String CAS_QUERY_QR_CONTENT = "cas_query_qr_content";

    /**
     * 获取扫码结果
     */
    String CAS_QUERY_QR_RESULT = "cas_query_qr_result";

    /**
     * CAS扫码登录
     */
    String CAS_ACTION_LOGIN = "cas_start_login";

    /**
     * CAS查询用户信息服务
     */
    String CAS_QUERY_USER_INFO = "cas_query_user_info";

    /**
     * 用户在GT登录防爆
     */
    String USER_LOGIN_IN_GT = "user_login_in_gt";

    /**
     * 统一登录 绑定用户终端会话
     */
    String UNIFIED_LOGIN_BIND_USER_TERMINAL = "unified_login_bind_user_terminal";

    /**
     * 获取终端开机模式
     */
    String NOTIFY_TERMINAL_START_MODE = "notify_terminal_start_mode";


    /**
     * 获取全局CMC 上报配置开关
     */
    String GET_CMC_CONFIG_STATUS = "get_cmc_config_status";

    /**
     * 同步EST配置给shine
     */
    String SYNC_EST_CONFIG = "sync_est_config";


    /**
     * 获取UWS扫码登录配置
     */
    String UWS_QR_CONFIG = "uws_qr_config";

    /**
     * UWS扫码登录配置更新
     */
    String UWS_QR_CONFIG_UPDATE = "uws_qr_config_update";

    /**
     * 获取UWS登录二维码
     */
    String QUERY_UWS_QR = "query_uws_qr";

    /**
     * 获取UWS登录二维码状态
     */
    String QUERY_UWS_QR_STATE = "query_uws_qr_state";

    /**
     * UWS二维码登录
     */
    String UWS_QR_START_LOGIN = "uws_qr_start_login";

    /**
     * 远程启动IDV虚拟机
     */
    String SHINE_ACTION_REMOTE_START_VM = "remote_start_vm";

    /**
     * 检查是否自动进入虚机
     */
    String CHECK_START_VM = "check_start_vm";

    /**
     * 镜像下载状态
     */
    String NOTIFY_IMAGE_DOWNLOAD_STATUS = "notify_image_download_status";

    /**
     * 同网段终端唤醒
     */
    String WAKE_UP_TERMINAL = "wake_up_terminal";


    /**
     * 切换成对应云桌面策略
     */
    String CHANGE_DESK_VM_TYPE = "change_desk_vm_type";

    /**
     * 云桌面休眠完成
     */
    String RCDC_SHINE_SLEEP_VM_FINISH = "sleep_vm_finish";

    /**
     * 配置系统盘自动扩容
     */
    String SET_FULL_SYSTEM_DISK_CONFIG = "set_full_system_disk";

    /**
     * SHINE同步桌面是否开启系统盘自动扩容
     */
    String GET_FULL_SYSTEM_DISK_CONFIG = "notify_full_system_disk";

    /**
     * SHINE同步终端极简部署模式配置
     */
    String SYS_TERMINAL_SIMPLIFY_DEPLOYMENT_CONFIG = "sys_terminal_simplify_deployment_config";

    /**
     * 获取池桌面资源
     */
    String GET_DESKTOP_POOL_RESOURCE = "get_desktop_pool_resource";

    /**
     * 应用客户端登录
     */
    String RCA_CLIENT_LOGIN = "rca_client_login";

    /**
     * rca client用户动态口令登录
     */
    String RCA_CLIENT_USER_OTP_CODE_LOGIN = "rca_client_user_otp_code_login";

    /**
     * IDV、TCI上报故障
     */
    String TERMINAL_REPORT_FAULT = "terminal_report_fault";

    /**
     * IDV、TCI获取报障处理结果
     */
    String REQUEST_REPORT_FAULT_RESULT = "request_report_fault_result";

    /**
     * EST跟RCDC透传接口
     */
    String RCDC_TRANSPARENT_EST_COMMON_ACTION = "common_action";

    /**
     * 网页版客户端登录
     */
    String WEB_CLIENT_LOGIN = "web_client_login";

    /**
     * 向TCI公共终端桌面发送重新获取启动参数的命令
     */
    String SEND_FETCH_START_PARAM_COMMAND =  "send_fetch_start_param_command";

    /**
     * 向TCI公共终端发送编辑的计算机名。
     */
    String  SEND_EDIT_COMPUTER_NAME = "edit_computer_name";

    /**
     * 检查CT3XXX系列的终端授权
     */
    String CHECK_AUTH = "check_auth";

    /**
     * 获取用户短信认证配置
     */
    String GET_USER_SMS_AUTH_CONFIG  = "get_user_sms_auth_config";    
    
    /**
     * 发送短信
     */
    String SEND_SMS  = "send_sms";    
    
    /**
     * 用户绑定手机号
     */
    String USER_BIND_PHONE  = "user_bind_phone";

    /**
     * 校验短信码
     */
    String VERIFY_SMS_CODE  = "verify_sms_code";    
    
    /**
     * 校验短信码
     */
    String SMS_REST_USER_PASSWORD  = "sms_reset_user_password";

    /**
     * 获取用户短信密码找回配置
     */
    String GET_USER_PWD_RECOVER_CONFIG  = "get_user_pwd_recover_config";

    /**
     * 上报桌面信息
     */
    String REPORT_TERMINAL_DESK_INFO = "report_terminal_desk_info";

    String CHECK_THIRD_PARTY_CERTIFICATION_CODE = "check_third_party_certification_code";

    String LOGIN_FINISH = "login_finish";
    String CONNECT_VM_FAIL = "connect_vm_fail";

    /** 检测终端型号 */
    String CHECK_PRODUCT_TYPE = "check_product_type";

    /**
     * 登陆信息变化CODE
     */
    String LOGIN_INFO_CHANGE_CODE = "login_info_change_code";


    String OBTAIN_CHECK_USER_OTP_CODE = "obtain_check_user_otp_code";

    String OBTAIN_USER_OTP_CODE_LOGIN = "obtain_user_otp_code_login";

    String OBTAIN_GET_USER_OTP_CONFIG = "obtain_get_user_otp_config";

    /**
     * 统一登录状态变更
     */
    String UNIFIED_LOGIN_UPDATE = "unified_login_update";

    /**
     * 终端部署成vdi模式时上报
     */
    String SETTING_WORK_MODE = "setting_work_mode";

    /**
     * 7800终端离线登录场景补充授权
     */
    String SUPPLEMENT_AUTH = "supplement_auth";

    /**
     * 获取是否可进行终端编辑镜像
     */
    String IS_LIMIT_EDIT_IMAGE = "is_limit_edit_image";

    String NOTIFY_CHANGE_START_MODE = "notify_change_start_mode";

    /**
     * 终端上报当前会话连接列表信息（用户并发授权）
     */
    String CURRENT_SESSION_INFO = "current_session_info";

    /**
     * 第三方应用授权码认证
     */
    String AUTHORIZATION_CODE_AUTH = "authorization_code_auth";

    /**
     * 获取图形验证码
     */
    String GET_CAPTCHA = "get_captcha";

    /**
     * 获取客户端登录认证方式
     */
    String GET_LOGIN_PAGE_INFO = "get_login_page_info";

    /**
     * 客户端绑定加域桌面密码
     */
    String BIND_AD_PASSWORD = "bind_ad_password";

    /**
     * 通知客户端登录认证方式变更
     */
    String REFRESH_LOGIN_PAGE_INFO = "refresh_login_page_info";

    /**
     * 查询域桌面sso配置
     */
    String GET_DOMAIN_SSO_CONFIG = "get_domain_sso_config";

    /**
     * 获取第三方认证方式配置
     */
    String GET_THIRD_PARTY_AUTH_CONFIG = "get_third_party_auth_config";

    /**
     * 获取移动客户端扫码登录配置
     */
    String MOBILE_CLIENT_QR_CONFIG = "mobile_client_qr_config";

    /**
     * 获取移动客户端扫码登录二维码
     */
    String QUERY_MOBILE_CLIENT_QR = "query_mobile_client_qr";

    /**
     * 获取移动客户端扫码登录二维码状态
     */
    String QUERY_MOBILE_CLIENT_QR_STATE = "query_mobile_client_qr_state";

    /**
     * 移动客户端扫码登录
     */
    String MOBILE_CLIENT_QR_START_LOGIN = "mobile_client_qr_start_login";

    /**
     * 移动客户端用户扫码
     */
    String MOBILE_CLIENT_USER_SCAN_QR_CODE = "mobile_client_user_scan_qr_code";

    /**
     * 移动客户端扫码用户确认登录
     */
    String MOBILE_CLIENT_USER_CONFIRM_LOGIN = "mobile_client_user_confirm_login";

    /**
     * 移动客户端扫码用户取消
     */
    String MOBILE_CLIENT_USER_CANCEL_QR_CODE = "mobile_client_user_cancel_qr_code";

    /**
     * 移动客户端扫码登录配置更新
     */
    String MOBILE_CLIENT_QR_CONFIG_UPDATE = "mobile_client_qr_config_update";

}
