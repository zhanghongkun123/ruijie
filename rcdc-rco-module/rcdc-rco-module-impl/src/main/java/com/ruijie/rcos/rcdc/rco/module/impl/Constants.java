package com.ruijie.rcos.rcdc.rco.module.impl;

import java.util.UUID;

/**
 * Description: 业务中的常量
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月29日
 *
 * @author Jarman
 */
public interface Constants {

    int SUCCESS = 0;

    int FAILURE = 99;

    /**
     * PC纳管业务DispatcherKey
     */
    String COMPUTER_BUSINESS = "computer_business";

    /**
     * 字符串常量0
     */
    String STRING_0 = "0";

    /**
     * 排序字段-创建时间
     */
    String ORDER_BY_CREATE_TIME = "createTime";

    /**
     * 告警同步记录key
     */
    String GLOBAL_PARAMETER_KEY_ALARM_RECORD = "alarm_sync_record";

    /**
     * 1小时，单位/毫秒
     */
    long ONE_HOUR_MILLIS = 1000 * 60 * 60L;

    /**
     * 1天，单位/毫秒
     */
    long ONE_DAY_MILLIS = 1000 * 60 * 60 * 24L;

    /**
     * 1分钟，单位/毫秒
     */
    long ONE_MINUTE_MILLIS = 1000 * 60L;

    /**
     * 云桌面CPU使用率上限
     */
    Double DESKTOP_CPU_MAX_USAGE = 100D;

    /**
     * 云桌面CPU使用率下限
     */
    Double DESKTOP_CPU_MIN_USAGE = 0D;

    /**
     * HCI底层请求支持5个服务器监控数据,多了会影响性能
     */
    int MONITOR_REQUEST_LIMIT = 10;

    /**
     * API分页长度限制
     */
    Integer API_PAGE_QUERY_LIMIT = 1000;

    /**
     * 日统计任务补偿时间段，天
     */
    Integer DEFAULT_RETRY_DAYS = 7;

    /**
     * 统计时间，
     */
    Integer STATIC_MINUTE_BEFORE = -5;

    /**
     * 用户设置屏幕水印消息
     */
    String SCREEN_WATERMARK_MESSAGE = "123";

    /**
     * acpi参数终端模式单用户:
     */
    String ACPI_IDV_TERMINAL_MODE_PERSONAL = "0";

    /**
     * VDI终端获取VDI镜像列表Action
     */
    String VDI_EDIT_IMAGE_REFRESH_IMAGES_LIST_ACTION = "refresh_vdi_edit_images_list";

    /**
     * VDI终端获取VDI镜像列表Action
     */
    String VDI_EDIT_IMAGE_GET_IMAGES_DETAIL_ACTION = "get_vdi_edit_images_detail";

    /**
     * VDI终端获取网络策略列表Action
     */
    String VDI_EDIT_IMAGE_GET_RCDC_NETWORK_STRATEGY_ACTION = "get_rcdc_network_strategy";

    /**
     * VDI终端配置编辑镜像的虚机参数Action
     */
    String VDI_EDIT_IMAGE_CONFIG_VM_ACTION = "config_vm_for_edit_image_template";

    /**
     * VDI终端启动镜像模版Action
     */
    String VDI_EDIT_IMAGE_START_VM_ACTION = "start_img_vm";

    /**
     * VDI终端放弃镜像模版Action
     */
    String VDI_EDIT_IMAGE_GIVE_UP_ACTION = "giveup_edit_img_vm";

    /**
     * VDI终端发布镜像模版Action
     */
    String VDI_EDIT_IMAGE_PUBLISH_ACTION = "publish_img";

    /**
     * VDI终端界面上执行关闭编辑Action
     */
    String VDI_EDIT_IMAGE_STOP_ACTION = "stop_edit_img";

    /**
     * VDI终端获取虚机spice信息Action
     */
    String VDI_EDIT_IMAGE_GET_VM_SPICE_ACTION = "get_img_vm_spice";

    /**
     * VDI终端获取虚机USB配置Action
     */
    String VDI_EDIT_IMAGE_GET_VM_USB_INFO_ACTION = "get_img_vm_usb";

    /**
     * VDI终端通知RCDC正在编辑镜像Action
     */
    String VDI_EDIT_IMAGE_INFORM_EDITING_ACTION = "send_editing_img_to_rcdc";

    /**
     * VDI终端通知RCDC退出编辑镜像Action
     */
    String VDI_EDIT_IMAGE_INFORM_EXIT_VM_ACTION = "send_Image_vm_est_exit";

    /**
     * 管理员在终端登录请求
     */
    String START_ADMIN_LOGIN = "start_admin_login";

    /**
     * 管理员在终端登出请求
     */
    String SEND_ADMIN_LOGOUT = "send_admin_logout";

    /**
     * 通知终端退出管理员登陆
     */
    String ADMIN_LOGOUT = "admin_logout";

    /**
     * 通知用户退出登录
     */
    String NOTIFY_USER_LOGOUT = "notify_user_logout";

    /**
     * 管理员终端登录会话超时
     */
    String ADMIN_SESSION_TIME_OUT = "admin_session_time_out";

    /**
     * 用户与IDV终端绑定之前的检查处理
     */
    String CHECK_USER_BEFORE_BIND_IDV = "check_user_before_bind_idv";

    /**
     * IDV终端上报配置向导数据
     */
    String IDV_REPORT_CONFIG_WIZARD = "idv_report_config_wizard";

    /**
     * 获取绑定镜像信息
     */
    String GET_BIND_IMAGE_INFO = "get_bind_image_info";

    /**
     * CMS对接：每次分页获取所有管理员数量
     */
    int CMS_DOCKING_GET_ADMIN_PAGE_LIMIT = 1000;

    /**
     * bCrypt 加解密salt
     */
    int BCRYPY_SALT = 10;

    /**
     * 终端默认分组UUID
     */
    UUID DEFAULT_TERMINAL_GROUP_UUID = UUID.fromString("7769c0c6-473c-4d4c-9f47-5a62bdeb30ba");

    /**
     * 终端用户登录
     */
    String LOGIN_NORMAL = "login_normal";

    /**
     * 终端访客用户登录
     */
    String LOGIN_VISITOR = "login_visitor";

    /** ================= PC 纳管业务参数start============= */

    /**
     * 小助手请求报文中的字段名，虚机/桌面类型，0 标识云桌面，1 标识pc桌面
     */
    String VM_TYPE = "vmType";

    /**
     * 小助手请求报文中的字段名，业务类型
     */
    String BUSINESS = "business";

    /**
     * 小助手请求报文中的字段名，故障描述
     */
    String FAULTDESCRIPTION = "faultDescription";

    /**
     * VDI云桌面虚机
     */
    int VM_TYPE_VDI_CLOUDDESKTOP = 0;

    /**
     * IDV云桌面虚机
     */
    int VM_TYPE_IDV_CLOUDDESKTOP = 2;

    /**
     * VOI云桌面虚机
     */
    int VM_TYPE_VOI_CLOUDDESKTOP = 3;

    /**
     * PC桌面
     */
    int VM_TYPE_PC = 1;

    /**
     * 云桌面以及pc报障结果成功
     */
    int RESULT_SUCCESS = 0;

    /**
     * 云桌面以及pc报障结果成功
     */
    int RESULT_FAIL = 1;

    /**
     * 不存在云桌面或pc桌面
     */
    int RESULT_NOTEXIST_DESKTOP = 2;

    /**
     * ftp 头
     */
    String FTP_HEAD = "ftp://";

    /**
     * ftp 端口号
     */
    String FTP_PORT = "2021";

    /**
     * 小助手安装包路径，提供FTP下载相对路径
     */
    String ASSISTANT_APP_COMPONENT_PATH_FOR_FTP = "/terminal_component/assistant_app/component/";

    /**
     * 小助手安装包路径
     */
    String ASSISTANT_APP_COMPONENT_PATH = "/opt/ftp/terminal/terminal_component/assistant_app/component/";

    /**
     * 小助手安装包名称
     */
    String ASSISTANT_APP_PACKAGE_NAME = "RG-CloudManagerAssisant_Upgrade.exe";

    /**
     * HALO安装包路径
     */
    String HALO_APP_COMPONENT_PATH = "/data/ToolKit/Windows/Tool/";

    /**
     * HALO安装包名称
     */
    String HALO_APP_PACKAGE_NAME = "halo.zip";

    /**
     * sunny客户端安装包路径
     */
    String SUNNY_CLIENT_PACKAGE_PATH = "/data/cm_app/component/";

    /**
     * sunny客户端安装包名称
     */
    String SUNNY_CLIENT_PACKAGE_NAME = "RG-CloudManagerStorage_Upgrade.exe";

    /**
     * uws客户端安装包路径
     */
    String UWS_CLIENT_PACKAGE_PATH = "/data/cm_app/component/";

    /**
     * uws客户端安装包名称
     */
    String UWS_CLIENT_PACKAGE_NAME = "RG-UnifiedWorkSpace_Setup.exe";

    /**
     * 小助手版本信息配置文件
     */
    String ASSISTANT_APP_CONFIG_FILE_NAME = "CMA_Assistant_Version.ini";

    /**
     * 小助手版本号配置文件的属性
     */
    String MAIN_VERSION_PROPERTIES = "ruijie.rco.cma.mainVersion";
    String MINOR_VERSION_PROPERTIES = "ruijie.rco.cma.minorVersion";
    String THREE_VERSION_PROPERTIES = "ruijie.rco.cma.threeVersion";
    String FOUR_VERSION_PROPERTIES = "ruijie.rco.cma.fourVersion";

    /**
     * ftp账号密码
     */
    String FTP_USER = "shine";

    /**
     * rcdc报文转发action（rcdc到小助手）
     */
    String RCDC_TO_ASSISTANT_ACTION = "computer_business";

    /**
     * vnc websocket端口号
     */
    int VNC_WEBSOCKET_PORT = 5700;

    /**
     * guesttool消息portId值
     */
    int ASSISTANT_MESSAGE_PORTID = 137;

    String COMPUTER_VNC_FILE_PATH = "file.busiz.dir.computer.vnc";

    String USER_IDENTITY_CONFIG_SYNC_NOTIFY = "user_identity_config_sync_notify";


    /**
     * 旧的日志周期天数
     */
    String OLD_DAY_KEY = "oldDay";
    /**
     * 新的日志周期天数
     */
    String INTERVAL_DAY_KEY = "intervalDay";

    /**
     * 普通用户最多可创建的桌面个数3
     */
    int USER_DESKTOP_MAX_NUM = 3;

    /**
     * 访客用户最多可创建桌面的个数 10
     */
    int VISITOR_USER_DESKTOP_MAX_NUM = 10;

    /**
     * 终端初始化
     */
    String TERMINAL_INIT = "terminal_init";

    /**
     * RCDC发送更新访客登录消息事件给终端（Shine）
     */
    String UPDATE_VISITOR_SETTING = "update_visitor_setting";


    /**
     * 终端管理员密码全局参数表key
     */
    String RCDC_TERMINAL_ADMIN_PWD_GLOBAL_PARAMETER_KEY = "terminal_pwd";


    /**
     * 通知小助手显示报障的
     */
    String ASSISTANT_APP_SHOW_FAULT = "show_fault";

    /**
     * 请求远程协助
     */
    String REMOTE_ASSIST_REQUEST_STATUS = "remote_assist_request_status";

    /**
     * 通知IDV终端放弃编辑
     */
    String IDV_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE = "abort_local_edit_image_template";

    /**
     * 通知IDV终端放弃编辑
     */
    String CHECK_IDV_EXISTS_LOCAL_EDIT_IMAGE_TEMPLATE = "check_idv_exists_local_edit_image_template";

    /**
     * 关于窗口查询特征码
     */
    String ACTION_ABOUT_WINDOW_VIEW_INIT = "init_about_window";
    String RCDC_RCO_MARK_AS_S2_VERSION = "s2_version";

    /**
     * 5.4 分级分权 终端获取管理员权限
     */
    String GET_ADMIN_PERMISSION = "get_admin_permission";


    /**
     * 5.4 分级分权 终端提取镜像发布完成后续处理
     */
    String LOCAL_POST_PROCESSING_AFTER_PUBLISHING = "local_post_processing_after_publishing";

    /**
     * 评测功能策略
     */
    String EVALUATION_STRATEGY = "enable_evaluation_strategy";

    /**
     * 全局表表示S2版本字段
     */
    String RCDC_RCO_S2_VERSION = "s2_version";

    /**
     * IP限制在全局表中的param_key
     */
    String RCDC_RCO_IP_LIMIT_VALUE = "enable_vdi_ip_limit";

    /**
     * 管理员密码正则表达式，大写字母、小写字母、数字、特殊符号三种及以上，长度[8,32]
     */
    String ADMIN_PASSWORD_REGEX = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z`~!@#$%^&*()_\\-+=<>?:\""
            + "{}|,./\\\\;'\\[\\]\\s]+$)(?![a-z0-9]+$)(?![a-z`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]+$)(?![0-9`~!@#$"
            + "%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]+$)[a-zA-Z0-9`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]{8,32}$";

    /**
     * syslog app name
     */
    String RCDC_RCO_SYSLOG_APP_NAME = "CDC";


    /**
     * 是否需要发送syslog字段 (变更储存上一次发送时间)
     */
    String RCDC_RCO_NEED_SEND_SYSLOG = "need_send_syslog";

    /**
     * 发送syslog定时任务cron表达式（废弃）
     */
    String RCDC_RCO_SEND_SYSLOG_SCHEDULE_CRON = "send_syslog_schedule_cron";

    /**
     * 发送syslog定时任务执行时间配置信息 {"intervalMinute":1,"scheduleCron":"0 0 3 * * ?", "sendCycleType":"DAY"}
     */
    String RCDC_RCO_SEND_SYSLOG_SCHEDULE_CONFIG = "send_syslog_schedule_config";

    /**
     * GT 软件上报次数开关
     */
    String RCDC_GT_DESKSOFT_MSG_STATUS = "rcdc_gt_desksoft_msg_status";

    /**
     * 服务器IP全局表字段
     */
    String VIP_PARAM_KEY = "cluster_virtual_ip";

    /**
     * CMS 服务器路径
     */
    String CMS_FTP_PATH = "/opt/ftp/data/cms/";


    /**
     * uws tcp 端口号
     */
    int UWS_TCP_PORT = 9411;

    /**
     * uws tcp 地址
     */
    String UWS_TCP_ADDRESS = "uws";

    /**
     * 是否已经迁移了自动休眠策略全局表KEY
     */
    String HAS_MIGRATE_KEY = "has_migrate_auto_sleep_config";

    String GET_NEW_IMAGE_ID = "get_new_image_id";

    /**
     * 远程密码加密
     */
    String AES_ASSIST_KEY = "ECI584C47E58605D";

    /**
     * 终端ip字段名
     */
    String IP = "ip";

    /**
     * 终端网卡信息字段名
     */
    String NETWORK_INFOS = "networkInfos";


    /**
     * 终端极简部署开关
     */
    String TERMINAL_SIMPLIFY_DEPLOYMENT_CONFIG = "terminal_simplify_deployment_config";

    /**
     * rcdc <=> shine 通知终端用户变更
     */
    String TERMINAL_USER_CHANGE = "terminal_user_change";

    /**
     * 桌面名称
     */
    String DESKTOP_NAME = "desktopName";

    /**
     * 终端mac地址
     */
    String MAC_ADDR = "macAddr";

    /**
     * 终端名称
     */
    String TERMINAL_NAME = "terminalName";

    /**
     * 终端绑定用户名
     */
    String BIND_USER_NAME = "bindUserName";

    /**
     * web客户端标识的终端ID
     */
    String WEB_TERMINAL_ID = "web_terminal_id";

    /**
     * 数值类常量 1
     */
    int INT_1 = 1;

    /**
     * 清除桌面会话配置
     */
    String CLEAR_DESK_SESSION_CONFIG = "clear_desk_session_config";

    /**
     * 桌面会话配置
     */
    String DESK_SESSION_CONFIG = "desk_session_config";

    /**
     * 计算集群key
     */
    String CLUSTER_ID = "clusterId";

    String DESKTOP_TYPE = "desktopType";

    String USER_GROUP_ID = "userGroupId";

    String USER_NAME = "userName";

    String APP_NAME = "appName";

    String TEST_NAME = "testName";

    String LIKE = "%";

    String PERMISSION_DATA_ID = "permissionDataId";

    String ADMIN_ID = "adminId";

    String OS_TYPE = "osType";

    String IMAGE_TYPE = "cbbimageType";

    String STATE = "state";

    String TERMINAL_GROUP_ID = "terminalGroupId";

    String TERMINAL_GROUP_NAME = "terminalGroupName";

    /**
     * 获取应用软件包磁盘列表
     */
    String GET_APP_DISK_LIST = "get_app_disk_list";

    /** GT消息加密KEY**/
    String GT_SSH_KEY = "ZGM1ODNhNTA0OGM0";

    /**
     * 终端报障默认描述
     */
    String TERMINAL_REPORT_FAULT_DES = "terminal_report_fault_des";

    String UAM_VM_MODE = "uam_vm_mode";

    String NORMAL_UAM_MODE = "1";

    String NONE_UAM_MODE = "0";

    String TEST_UAM_MODE = "3";

    String APP_DISK_ID_ARR = "app_disk_id_arr";

    String APPDISK_RESTORE = "appdisk_restore";

    /**
     * FTP目录
     */
    String FTP_DIR = "/opt/ftp/terminal";

    /**
     * 是否需要关联sysadmin新增的菜单权限
     */
    String NEED_ATTACH_SYSADMIN_PERMISSION_KEY = "need_attach_sysadmin_permission";

    /**
     * 虚拟应用功能开关
     */
    String VIRTUAL_APPLICATION_STATE = "virtual_application_state";

    /**
     * 文件审计管理:临时文件存储空间,ftp容器内路径
     */
    String AUDIT_FILE_FTP_TEMP_PATH = "/opt/ftp/data/audit_file";

    /**
     * 文件审计管理：持久化文件存储空间文件夹名称--容器内路径
     */
    String AUDIT_FILE_STORAGE_PATH = "audit_file";

    String DISCONNECT_RCENTER_DURATION = "disconnect_rcenter_duration";

    /**
     * 是否需要补偿云桌面策略的数据权限，ent1.1版本之前的管理员默认有所有桌面管理员的权限
     */
    String NEED_COMPENSATE_STRATEGY_DATA_PERMISSION = "need_compensate_strategy_data_permission";

    /**
     * 检测云桌面端口异常 告警类型
     */
    String CHECK_DESKTOP_PORT_FAIL_CODE = "CHECK_DESKTOP_PORT_FAIL_CODE";

    /**
     * 是否提示用户登录终端IP变更
     */
    String NOTIFY_LOGIN_TERMINAL_CHANGE = "enable_notify_login_terminal_change";

    /** 修改终端云服务器IP */
    String CHANGE_TERMINAL_SERVER_IP = "change_terminal_server_ip";


    String CHANGE_VIP_CONFIG_ONE_CLICK_KEY = "config_one_click_after_vip_changed";

    String AUTO_CONFIG_ONE_CLICK_INSTALL_CODE = "auto_config_one_click_install_code";

    String AUTO_CONFIG_ONE_CLICK_INSTALL_TYPE = "auto_config_one_click_install_type";

    /**
     * GuestTool向RCDC上报UAM冲突应用列表
     */
    String INSTALL_SOFT_LIST = "InstallList";

    String TERMINAL_WAKE_UP_CONFIG_KEY = "terminal_wake_up_config";

    /**
     * 自定义桌面规格升级数据处理标识
     */
    String NEED_UPGRADE_DESK_SPEC_INFO = "need_upgrade_desk_spec_info";

    String ONLINE = "在线";

    String DESTROYING = "注销中";

    /**
     * VDI应用池预启动告警
     */
    String APP_POOL_PRE_START_ERROR_ALARM_CODE = "app_pool_pre_start_error_alarm_code";

    /**
     * 是否需要校验用户明长度校验并进行告警
     */
    String NEED_CHECK_USER_NAME_AND_ALARM = "need_check_user_name_and_alarm";

    /**
     * 清理客户端日志时间间隔
     */
    String CLEAR_CLIENT_OP_LOG_INTERVAL = "clear_client_op_log_interval";
}
