package com.ruijie.rcos.rcdc.rco.module.def.constants;


import java.io.File;
import java.util.UUID;

/**
 * Description: 常量类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/18
 *
 * @author zhiweiHong
 */
public interface Constants {

    /**
     * 云桌面策略使用应用分层时系统盘容量默认增加大小(G)
     */
    Integer SYSTEM_DISK_CAPACITY_INCREASE_SIZE = 15;

    /**
     * 导出存放临时文件
     */
    String EXPORT_TMP_DIRECTORY = File.separator + "tmp";

    /**
     * 云桌面规格内存MB单位时步长512
     */
    Integer MEMORY_MB_STEP = 512;

    Integer OPERATE_MAX_SIZE = 50;

    Integer SOFTWARE_NAME_MAX_SIZE = 10;

    String COMMA_SEPARATION_CHARACTER = "，";
    /**
     * 下划线
     */
    String UNDERLINE = "_";

    /**
     * 顿号
     */
    String CAESURA = "、";

    /**
     * 软件白名单开关
     */
    String ENABLE_SOFTWARE_STRATEGY = "ENABLE_SOFTWARE_STRATEGY";

    /**
     * 最大个数限制
     */
    long SOFTWARE_MAX_COUNT_LIMIT = 2000;

    /**
     * 自动扩容全局配置KEY
     */
    String ENABLE_FULL_SYSTEM_DISK_GLOBAL_STRATEGY = "enable_full_system_disk";

    /**
     * 软控策略查询桌面的最大数量
     */
    Integer SOFTWARE_DESK_MAXSIZE = 500;

    /**
     * 软件id字段
     */
    String WEB_REQUEST_SOFTWARE_FIELD_NAME = "softwareId";

    /**
     * 系统盘最大值 (业务统一定义引用)
     */
    Integer SYSTEM_DISK_MAX_SIZE = 1024;

    /**
     * MINI 服务器系统盘最大值(业务统一定义引用)
     */
    Integer MAX_SYSTEM_SIZE_FOR_MINI_SERVER = 200;

    /**
     * 镜像一级差分
     */
    Integer IMAGE_DIFF_ONLY_ONE = 1;

    /**
     * 镜像默认的分区文件大小(为0)
     */
    int DEFAULT_IMAGE_DISK_SIZE = 0;

    /**
     * 桌面会话配置
     */
    String DESK_SESSION_CONFIG = "desk_session_config";


    /**
     * 邮箱全局配置
     */
    String SERVER_MAIL_CONFIG = "server_mail_config";

    /**
     * 用户密码AES秘钥
     */
    String USER_REQ_PARAMETER = "user_password_key";

    /**
     * 用户信息导出密码脱敏
     */
    String SECRET_USER_PASSWORD = "******";

    String COMMA = ",";

    String NEED_CHECK_PORT = "139,445";

    /**
     * 是否提示用户登录终端IP变更
     */
    String NOTIFY_LOGIN_TERMINAL_CHANGE = "enable_notify_login_terminal_change";

    /**
     * gt设置windows账号失败
     */
    String ALARM_PREFIX_GT_UPDATE_WINDOWS_ACCOUNT_OR_PWD_ERROR = "ALARM_PREFIX_GT_UPDATE_WINDOWS_ACCOUNT_OR_PWD_ERROR";

    /**
     * 第三方平台动态口令验证配置
     */
    String THIRD_PARTY_AUTH_CODE_CONFIG = "third_party_auth_code_config";

    String DEFAULT_RADIUS_SERVER_NAME = "默认服务器";

    Integer DEFAULT_RADIUS_SERVER_TIMEOUT = 3;

    Integer DEFAULT_RADIUS_SERVER_RETRIES = 3;

    /**
     * 外置存储挂载路径
     */
    String HOME_EXTERNAL_STORAGE_PATH = "/external_storage/%s";

    /**
     * 查询字段:云平台ID
     */
    String QUERY_PLATFORM_ID = "platformId";

    /**
     * 查询字段:镜像用途
     */
    String QUERY_IMAGE_USAGE = "imageUsage";

    /**
     * 查询字段:磁盘池类型
     */
    String QUERY_DISK_POOL_TYPE = "diskPoolType";

    /**
     * 默认未纳管RCCM的集群Id
     */
    UUID DEFAULT_CLUSTER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /**
     * 云桌面
     */
    String CLOUD_DESKTOP = "云桌面";
    /**
     * 应用主机
     */
    String APP_CLOUD_DESKTOP = "应用主机";

    /**
     * 第三方
     */
    String THIRD = "第三方";

    /**
     * GB2312字符集
     */
    String CHARSET_GB2312 = "GB2312";

    /**
     * 接口一次最多查询数量
     */
    int MAX_QUERY_LIST_SIZE = 1000;

    String DELIMITER = "，";
}
