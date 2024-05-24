package com.ruijie.rcos.rcdc.rco.module.openapi.rest;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public interface RestErrorCode {

    /**
     * 云桌面系统异常
     */
    String RCDC_OPEN_API_SUCCESS = "0";

    /**
     * 云桌面系统异常
     */
    String RCDC_OPEN_API_SYSTEM_EXCEPTION = "23010000";

    /**
     * 创建云桌面时，用户不存在
     */
    String RCDC_OPEN_API_REST_CREATE_DESK_USER_NOT_EXIST = "23010001";

    /**
     * 批量创建云桌面，单次操作最大数量1000个
     */
    String RCDC_OPEN_API_REST_BATCH_CREATE_DESK_OVER_NUM = "23010002";

    /**
     * 动态池桌面不支持单独编辑桌面标签
     */
    String RCDC_OPEN_API_REST_DESKTOP_EDIT_REMARK_DYNAMIC_POOL_NOT_SUPPORT = "23010004";

    /**
     * 云桌面已绑定个性化配置策略，无法切换至非还原云桌面策略
     */
    String RCDC_OPEN_API_REST_DESKTOP_EDIT_STRATEGY_CONFLICT_WITH_UPM = "23010005";

    /**
     * 云桌面正在测试任务中测试，不允许操作
     */
    String RCDC_OPEN_API_REST_APPCENTER_TEST_DESKTOP_STATE_ERROR = "23010006";

    /**
     * 桌面为动态池桌面不支持单独修改云桌面策略
     */
    String RCDC_RCO_DESKTOP_EDIT_STRATEGY_DYNAMIC_POOL_NOT_SUPPORT = "23010007";

    /**
     * 桌面为动态池桌面不支持单独修改云桌面策略
     */
    String RCDC_OPEN_API_REST_IMAGE_NOT_SUPPORT_GPU = "23010008";

    /**
     * 桌面为动态池桌面不支持单独修改云桌面策略
     */
    String RCDC_OPEN_API_REST_IMAGE_NOT_SUPPORT_GPU_MODEL = "23010009";

    /**
     * 桌面为动态池桌面不支持单独修改云桌面策略
     */
    String RCDC_OPEN_API_REST_CLUSTER_NOT_EXIST_GPU_RESOURCES = "23010010";

    /**
     * 桌面为动态池桌面不支持单独修改云桌面策略
     */
    String RCDC_OPEN_API_REST_IMAGE_NOT_EXIST_GPU_DRIVER = "23010011";

    /**
     * 批量软删除云桌面，，单次操作最大数量1000个
     */
    String RCDC_OPEN_API_REST_BATCH_SOFT_DELETE_DESK_OVER_NUM = "23010601";

    /**
     * 修改云桌面配置参数非法
     */
    String OPEN_API_DESK_MEMORY_CONFIGURATION_PARAMER_LIMIT = "23010704";

    /**
     * 批量更换云桌面策略单次操作最大数量非法
     */
    String RCDC_OPEN_API_REST_BATCH_EDIT_DESK_STRATEGY_PARAMER_INVALID = "23010702";

    /**
     * 批量编辑云桌面标签单次操作最大数量非法
     */
    String RCDC_OPEN_API_REST_BATCH_EDIT_DESK_REMARK_PARAMER_INVALID = "23010703";

    /**
     * 查询云桌面匹配条件的规则或者类型非法，暂时必须为EXCAT和EQ
     */
    String OPEN_API_DESK_PAGE_QUERY_MATCH_RULE_OR_TYPE_INVALID = "23010801";

    /**
     * 当前版本查询集群云桌面，只支持VDI
     */
    String RCDC_CLOUDDESKTOP_CLUSTER_DESK_MUST_EQUALS_VDI = "23010802";

    /**
     * 当前终端登录已失效
     */
    String RCDC_CLOUDDESKTOP_CLUSTER_DESK_TERMINAL_INVALID = "23010803";

    /**
     * 云桌面[xxx]的当前状态为[xxx]，不能重启
     */
    String OPEN_API_DESK_NOT_RUNNING_STATE_REBOOT_FORBID = "23010901";


    /**
     * 异步任务未找到
     */
    String OPEN_API_TASK_CAN_NOT_FOUND = "23130001";

    /**
     * 有异步任务正在执行此动作
     */
    String OPEN_API_TASK_RUNNING_TASK_EXIST = "23130002";


    /**
     * 用户模块：用户不存在或被禁用
     */
    String OPEN_API_USER_NOT_EXISTS = "23070001";

    /**
     * 用户模块：用户不存在
     */
    String OPEN_API_REST_USER_NOT_EXISTS = "23070002";

    /**
     * 用户模块-登录：账号密码有误
     */
    String OPEN_API_USER_LOGIN_USERNAME_OR_PASSWORD_ERROR = "23070101";

    /**
     * 用户模块-登录：当前为访客账号登录,不允许作为普通用户登录
     */
    String OPEN_API_USER_LOGIN_VISITOR_LOGIN = "23070102";

    /**
     * 用户模块-登录：RCDC未授权,不允许登录
     */
    String OPEN_API_USER_LOGIN_NOT_LICENSE = "23070103";

    /**
     * 用户模块-登录：AD域服务器异常
     */
    String OPEN_API_USER_LOGIN_AD_SERVER_ERROR = "23070104";

    /**
     * 用户模块-登录：用户被禁用
     */
    String OPEN_API_USER_LOGIN_AD_ACCOUNT_DISABLE = "23070105";

    /**
     * 用户模块-登录：当前时间不允许登录
     */
    String OPEN_API_USER_LOGIN_AD_LOGIN_LIMIT = "23070106";

    /**
     * 用户模块-登录：AD账户过期
     */
    String OPEN_API_USER_LOGIN_AD_ACCOUNT_EXPIRE = "23070107";

    /**
     * 用户模块-登录：非访客用户不允许用于访客登录
     */
    String OPEN_API_USER_LOGIN_NOT_ALLOW_LOGIN_FOR_NOT_VISITOR = "23070108";

    /**
     * 用户模块-登录：LDAP服务器异常
     */
    String OPEN_API_USER_LOGIN_LDAP_SERVER_ERROR = "23070109";

    /**
     * 用户模块-登录：用户被锁定
     */
    String OPEN_API_USER_LOGIN_USER_LOCKED = "23070110";

    /**
     * 用户模块-登录：提示剩余密码可错误次数
     */
    String OPEN_API_USER_LOGIN_REMIND_ERROR_TIMES = "23070111";

    /**
     * 用户模块-登录：用户末开启账号密码登录
     */
    String OPEN_API_USER_LOGIN_USERNAME_OR_PASSWORD_UNOPENED = "23070112";

    /**
     * 用户模块-登录：密码解密异常
     */
    String OPEN_API_USER_LOGIN_DESCRYPT_PWD_ERROR = "23070113";

    /**
     * 用户模块-统一登录失败
     */
    String OPEN_API_USER_UNITE_LOGIN_FAIL = "23070114";

    /**
     * 用户模块-网页客户端不支持硬件特征码开启
     */
    String OPEN_API_USER_NO_HARDWARE_CERTIFICATION = "23070115";

    /**
     * 动态口令-查询用户动态口令配置信息失败，请联系管理员
     */
    String OPEN_API_GET_USER_OTP_CONFIG_ERROR = "23070116";

    /**
     * 动态口令-访客不支持动态口令登录
     */
    String OPEN_API_VISITOR_USER_NOT_SUPPORT_OTP_LOGIN = "23070117";

    /**
     * 动态口令-用户绑定动态口令失败
     */
    String OPEN_API_USER_BIND_OTP_ERROR = "23070118";

    /**
     * 动态口令-用户未绑定过动态口令
     */
    String OPEN_API_USER_NO_BIND_OTP = "23070119";

    /**
     * 动态口令-动态口令不一致
     */
    String OPEN_API_LOGIN_OTP_INCONSISTENT = "23070120";

    /**
     * 动态口令-用户未开启动态口令
     */
    String OPEN_API_UNENABLE_USER_OTP = "23070121";

    /**
     * 动态口令-未开启全局动态口令
     */
    String OPEN_API_UNENABLE_GLOBAL_OTP = "23070122";

    /**
     * 动态口令-用户名或动态口令错误
     */
    String OPEN_API_USERNAME_OR_OTP_ERROR = "23070123";

    /**
     * 用户模块-账号失效
     */
    String OPEN_API_USER_ACCOUNT_INVALID = "23070124";

    /**
     * 主要认证策略选择有误
     */
    String RCDC_OPEN_API_USER_PRIMARY_CERTIFICATION_CONFIG_FAIL = "23070125";

    /**
     * 未开启全局第三方认证
     **/
    String UNABLE_THIRD_PARTY_AUTH = "23070126";

    /**
     * 未开启个人第三方认证
     **/
    String UNABLE_USER_THIRD_PARTY_AUTH = "23070127";

    /**
     * 第三方认证失败
     **/
    String USER_THIRD_PARTY_AUTH_ERROR = "23070128";

    /**
     * 用户名不能为空
     */
    String OPEN_API_USER_NAME_NOT_EMPTY = "23070129";

    /**
     * 用户名不合法
     */
    String OPEN_API_USER_NAME_INVALID = "23070130";

    /**
     * 用户名超过32个字符
     */
    String OPEN_API_USER_NAME_TOO_LENGTH = "23070131";

    /**
     * 图形验证码错误
     */
    String CAPTCHA_ERROR = "23070132";

    /**
     * 图形验证码过期
     */
    String INVALID_CAPTCHA = "23070133";

    /**
     * 请输入图形验证码
     */
    String NOT_CAPTCHA = "23070134";

    /**
     * 图形验证码错误且关闭锁定配置
     */
    String CAPTCHA_ERROR_AND_CLOSE_LOCK = "23070135";

    /**
     * AD/LDAP域连接失败，且用户本地认证未开启
     */
    String AD_ACCOUNT_LOCAL_AUTH_DISABLE = "23070136";

    /**
     * 用户本地认证密码过期
     */
    String AD_ACCOUNT_LOCAL_AUTH_PASSWORD_EXPIRE = "23070137";

    /**
     * 用户模块-修改密码：账号密码有误
     */
    String OPEN_API_USER_UPDATE_PWD_OLD_PASSWORD_ERROR = "23070201";

    /**
     * 用户模块-修改密码：AD域用户不允许修改密码
     */
    String OPEN_API_USER_UPDATE_PWD_AD_USER_NOT_ALLOW_CHANGE_PASSWORD = "23070202";

    /**
     * 用户模块-修改密码：LDAP用户不允许修改密码
     */
    String OPEN_API_USER_UPDATE_PWD_LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD = "23070203";

    /**
     * 用户模块-修改密码：修改密码不符合密码策略要求
     */
    String OPEN_API_USER_UPDATE_PWD_CHANGE_PASSWORD_UNABLE_REQUIRE = "23070204";

    /**
     * 用户模块-修改密码：访客不允许修改密码
     */
    String OPEN_API_USER_UPDATE_PWD_VISITOR_USER_NOT_ALLOW_CHANGE_PASSWORD = "23070205";

    /**
     * 用户模块-修改密码：系统处于维护模式状态
     */
    String OPEN_API_USER_UPDATE_PWD_IN_MAINTENANCE = "23070206";

    /**
     * 用户模块-修改密码：用户处于锁定状态
     */
    String OPEN_API_USER_UPDATE_PWD_IN_LOCKED = "23070207";

    /**
     * 用户模块-修改密码：提示剩余密码可错误次数
     */
    String OPEN_API_USER_UPDATE_PWD_REMIND_ERROR_TIMES = "23070208";

    /**
     * 用户模块-修改密码：TOKEN失效或比对不一致
     */
    String OPEN_API_USER_UPDATE_PWD_TOKEN_EXPIRE = "23070209";

    /**
     * 用户模块-禁用用户：关闭云桌面失败
     */
    String OPEN_API_USER_CLOSE_DSEK_FAIL = "23070210";

    String RCDC_OPEN_API_NOT_OPEN_CAS_FAIL_RESULT = "23070211";
    String RCDC_OPEN_API_NOT_OPEN_OTP_FAIL_RESULT = "23070212";
    String RCDC_OPEN_API_NOT_HARDWARE_FAIL_RESULT = "23070213";
    String RCDC_OPEN_API_NOT_OPEN_SMS_AUTH_FAIL_RESULT = "23070214";
    String RCDC_OPEN_API_USER_TYPE_NOT_ALLOW_CHANGE = "23070215";
    String RCDC_OPEN_API_USER_NAME_NOT_ALLOW_CHANGE = "23070216";
    String RCDC_OPEN_API_USER_INVALID_TIME_VALIDATE_ERROR = "23070217";
    String RCDC_OPEN_API_USER_DESCRIPTION_INVALIDATE_ERROR = "23070218";
    String RCDC_OPEN_API_USER_ACCOUNT_EXPIRE_TIME_ERROR = "23070221";
    String RCDC_OPEN_API_NOT_OPEN_RADIUS_FAIL_RESULT = "23070222";
    String RCDC_OPEN_API_NOT_OPEN_OTP_RADIUS_MEANWHILE = "23070223";
    /**
     * 该用户已过期，请联系管理员
     */
    String RCDC_OPEN_API_USER_ACCOUNT_EXPIRE_ERROR = "23070224";
    /**
     * 批量启用用户，单次操作最大数量1000个
     */
    String RCDC_OPEN_API_REST_BATCH_ENABLE_USER_OVER_NUM = "23010019";

    /**
     * 批量禁用用户，单次操作最大数量1000个
     */
    String RCDC_OPEN_API_REST_BATCH_DISABLE_USER_OVER_NUM = "23010020";

    /**
     * 批量绑定用户和终端，单次操作最大数量1000个
     */
    String RCDC_OPEN_API_REST_BATCH_BIND_USER_MAC_OVER_NUM = "23200101";

    /**
     * 批量删除用户和终端绑定关系，单次操作最大数量1000个
     */
    String RCDC_OPEN_API_REST_BATCH_DELETE_USER_MAC_BINDING_OVER_NUM = "23200201";

    /**
     * 批量更新用户和终端绑定关系，单次操作最大数量1000个
     */
    String RCDC_OPEN_API_REST_BATCH_UPDATE_USER_MAC_BINDING_OVER_NUM = "23200301";

    /**
     * 用户模块-修改密码：第三方用户不允许修改密码
     */
    String OPEN_API_USER_UPDATE_PWD_THIRD_PARTY_USER_NOT_ALLOW_CHANGE_PASSWORD = "23070219";

    /**
     * 用户模块-修改密码：第三方用户不允许修改密码
     */
    String OPEN_API_USER_UPDATE_PWD_WEAK_PWD_NOT_ALLOW_CHANGE_PASSWORD = "23070220";
    /**
     * 桌面不存在
     */
    String OPEN_API_DESK_NOT_EXISTS = "23010101";

    /**
     * 桌面运行中
     */
    String OPEN_API_DESK_RUNNING = "23010112";

    /**
     * 终端不支持唤醒
     */
    String OPEN_API_TERMINAL_NOT_SUPPORT_WAKE_UP = "23010113";

    /**
     * 终端不存在
     */
    String OPEN_API_TERMINAL_NOT_EXIST = "23050001";


    /**
     * 云桌面处于不可关闭状态
     */
    String OPEN_API_DESK_NOT_ALLOW_CLOSE_STATE = "23010202";

    /**
     * 云桌面当前状态无法重启
     */
    String OPEN_API_DESK_NOT_ALLOW_RESTART_STATE = "23010901";

    /**
     * 当前云桌面状态不符合预期，请稍后重试
     */
    String RCDC_USER_CLOUDDESKTOP_DESK_STATE_NOT_ALLOW = "23011001";

    /**
     * 当前云桌面状态不符合预期，请稍后重试
     */
    String RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID = "23011101";


    // 扫码登录模块 ====

    /**
     * 扫码登录模块-二维码类型有误
     */
    String OPEN_API_RCDC_USER_UN_SUPPORT_QR_CODE_TYPE = "23140001";

    /**
     * 扫码登录模块-二维码开关已关闭
     */
    String OPEN_API_RCDC_USER_QR_CODE_TYPE_SWITCH_IS_CLOSE = "23140002";

    /**
     * 扫码登录模块-二维码客户端信息不匹配
     */
    String OPEN_API_RCDC_USER_CLIENT_QR_CODE_NOT_MATCH = "23140003";

    /**
     * 扫码登录模块-二维码状态与预期不匹配
     */
    String OPEN_API_RCDC_USER_QR_CODE_STATUS_NOT_EXPECT = "23140004";

    /**
     * 扫码登录模块-二维码不存在或过期
     */
    String OPEN_API_RCDC_USER_QR_CODE_NOT_EXIST_OR_EXPIRE = "23140005";

    // 镜像模板

    // osFile状态不可用
    String RCDC_CODE_OS_FILE_STATE_UNAVAILABLE = "23160001";
    // 参数需要提供或者参数校验错误
    String RCDC_CODE_PARAMETER_ERROR = "23160002";
    // 文件后缀参数错误
    String RCDC_CODE_FILE_SUFFIX_ERROR = "23160003";
    // 文件不存在
    String RCDC_CODE_FILE_NOT_EXISTS = "23160004";
    // 文件描述过长
    String RCDC_CODE_FILE_NOTE_TOO_LONG = "23160005";
    // 文件 MD5 不一致
    String RCDC_CODE_FILE_MD5_ERROR = "23160006";
    // 文件已存在
    String RCDC_CODE_FILENAME_EXISTS = "23160007";
    // 系统繁忙（加锁）
    String RCDC_CODE_SYSTEM_BUSY = "23160008";
    // 系统配置错误
    String RCDC_CODE_SYSTEM_CONFIG_ERROR = "23160009";
    // 镜像模板不存在
    String RCDC_CODE_IMAGE_TEMPLATE_NOT_FOUND = "23160010";
    // 镜像模板状态错误
    String RCDC_CODE_IMAGE_TEMPLATE_STATUS_ERROR = "23160011";

    // 镜像模版名称已存在
    String RCDC_CODE_IMAGE_TEMPLATE_NAME_EXISTS = "23160012";
    // 镜像模版创建中
    String RCDC_CODE_IMAGE_TEMPLATE_STATE_CREATING = "23160013";
    // 镜像模版状态异常
    String RCDC_CODE_IMAGE_STATE_ERROR = "23160014";

    // 磁盘类型与镜像文件类型不匹配
    String RCDC_CODE_IMAGE_DISK_TYPE_NOT_MATCH_OSFILE_TYPE = "23160015";
    // 镜像模版备份中
    String RCDC_CODE_IMAGE_BACK_OR_CREATE = "23160016";
    // 镜像模版不存在
    String RCDC_CODE_IMAGE_NOT_EXIST = "23160017";
    // 镜像模版配置状态校验不通过
    String RCDC_CODE_CONFIG_IMAGE_VALIDATION = "23160018";
    // 系统盘大小必须大于原系统盘大小
    String RCDC_CODE_VM_DISK_SIZE_LARGE_THAN_BEFORE = "23160019";
    // 网络策略不存在
    String RCDC_CODE_NETWORK_NOT_EXIST = "23160020";
    // 网络策略不可用
    String RCDC_CODE_NETWORK_UNAVAILABLE = "23160021";
    // 镜像发布状态校验不通过
    String RCDC_CODE_PUBLISH_IMAGE_VALIDATION = "23160022";
    // 发布镜像模版失败
    String RCDC_CODE_PUBLISH_BUSINESS_VALIDATION = "23160023";
    // gt未就绪
    String RCDC_CODE_GUESTTOOL_STAGE_ERROR = "23160024";
    // 镜像驱动未安装
    String RCDC_CODE_IDV_IMAGE_TEMPLATE_NO_DRIVER = "23160025";
    // 镜像模版已存在
    String RCDC_CODE_IMAGE_TEMPLATE_ALREADY_EXIST = "23160026";
    // 镜像模版大小小于os文件
    String RCDC_CODE_IMAGE_TEMPLATE_SIZE_SMALL_THAN_OS_FILE = "23160027";
    // 网络策略名称已存在
    String RCDC_CODE_NETWORK_NAME_EXIST = "23160028";
    // 存在相同MD5的镜像文件
    String RCDC_CODE_EXIST_SAME_MD5_OS_FILE = "23160029";
    // 文件已存在，非旧平台镜像文件
    String RCDC_CODE_FILMD5_EXISTS = "23160080";
    // Qcow2 文件大小不一致，请旧平台核对文件计算是否有误
    String RCDC_CODE_FILE_SIZE_ERROR = "23160081";
    // MD5 长度错误，请旧平台核对文件计算是否有误
    String RCDC_CODE_FILE_MD5_INPUT_ERROR = "23160082";
    // 非标准模式只允许编辑一个镜像
    String RCDC_CODE_ONLY_ONE_IMAGE_ALLOWED_TO_RUN = "23160083";
    // 通用业务异常码
    String RCDC_CODE_NORMAL_BUSINESS_EXCEPTION = "23160084";

    // sunny安装失败
    String RCDC_CODE_SUNNY_INSTALL_ERROR = "23160030";
    String RCDC_CODE_SUNNY_INSTALL_INFO_NOT_FOUND_ERROR = "23160041";

    // 终端模块
    // 终端组的父组不存在
    String RCDC_CODE_TERMINAL_GROUP_NOT_EXISTS = "23150001";
    // 终端部署模式错误
    String RCDC_CODE_TERMINAL_MODE_ERROR = "23150002";
    // 终端的父组不存在
    String RCDC_CODE_TERMINAL_NOT_EXISTS = "23150003";
    // 终端父组的镜像特性或策略特性不存在
    String RCDC_CODE_TERMINAL_FEATURE_NOT_EXISTS = "23150004";
    // 终端关联的用户不存在
    String RCDC_CODE_TERMINAL_BIND_USER_NOT_EXISTS = "23150005";
    // 个性终端需要绑定用户
    String RCDC_CODE_TERMINAL_BIND_USER_NOT_FOUND = "23150006";
    // 终端重复导入
    String RCDC_CODE_TERMINAL_EXISTS = "23150007";
    // 终端关联用户未找到特性配置
    String RCDC_CODE_TERMINAL_BIND_USER_CONFIG_NOT_EXISTS = "23150008";
    // 终端白名单数量不允许超过三个
    String RCDC_CODE_TERMINAL_SSID_SUM_ERROR = "23150009";
    // 终端白名单无线名称重复
    String RCDC_RCO_WIFI_DUPLICATE = "23150010";
    // 终端分组名称同级下重复
    String RCDC_RCO_TERMINALGROUP_GROUP_NAME_DUPLICATE = "23150011";
    // 参数需要提供或者参数校验错误
    String RCDC_RCO_TERMINALGROUP_PARAMETER_ERROR = "23150012";
    // 云桌面配置填写不完整
    String RCDC_RCO_TERMINAL_PARAMETER_ERROR = "23150014";
    // 镜像系统类型和终端 CPU 类型不匹配
    String RCDC_RCO_TERMINAL_OS_TYPE_ERROR = "23150015";
    // 终端关联用户不存在
    String RCDC_RCO_TERMINAL_USER_NOT_FOUND = "23150016";
    // 终端关联用户被禁用
    String RCDC_RCO_TERMINAL_USER_DISABLE = "23150017";
    // 终端已和用户绑定
    String RCDC_RCO_TERMINAL_USER_BINDED = "23150018";
    // 删除终端的公共桌面失败
    String RCDC_RCO_TERMINAL_DELETE_ERROR = "23150019";
    // 终端绑定用户名称不一致
    String RCDC_RCO_TERMINAL_USER_NAME_ERROR = "23150020";
    // 终端绑定终端组名称不一致
    String RCDC_RCO_TERMINAL_GROUP_NAME_ERROR = "23150021";
    // 终端分组层级超出
    String RCDC_RCO_TERMINALGROUP_GROUP_LEVEL_EXCEED_LIMIT = "23150022";
    // 云桌面策略系统盘小于镜像模板最小限制
    String RCDC_RCO_CLOUDDESKTOP_STRATEGY_SYSTEM_DISK_OUT_RANGE = "23150023";
    // 终端组不允许使用系统保留的组名
    String RCDC_RCO_TERMINAL_USERGROUP_NOT_ALLOW_RESERVE_NAME = "23150024";
    // 终端已导入，如需重新导入需要删除已有终端
    String RCDC_RCO_TERMINAL_EXISTS = "23150025";
    // 创建云桌面的桌面策略不存在
    String RCDC_RCO_STRATEGY_IDV_NOT_FOUND = "23150026";
    // 创建云桌面的镜像策略不存在
    String RCDC_RCO_IMAGE_TEMPLATE_NOT_FOUND = "23150027";
    // 云桌面已存在不能重复创建
    String RCDC_RCO_TERMINAL_DESKINFO_EXIST = "23150028";
    // 终端分组子分组数量超出限制
    String RCDC_OPENAPI_TERMINAL_SUB_GROUP_EXCEED_LIMIT = "23150029";
    // 终端分组数量超出限制
    String RCDC_OPENAPI_TERMINALGROUP_GROUP_NUM_EXCEED_LIMIT = "23150030";
    // 删除终端分组下的子分组在移动分组下存在重名分组
    String RCDC_OPENAPI_DELETE_TERMINAL_GROUP_SUB_GROUP_HAS_DUPLICATION_WITH_MOVE_GROUP = "23150031";
    // 不可选择自己或自己的子节点为父分组
    String RCDC_OPENAPI_TERMINALGROUP_GROUP_PARENT_CAN_NOT_SELECT_ITSELF_OR_SUB = "23150032";
    // 云桌面策略不存在
    String RCDC_RCO_STRATEGY_NOT_FOUND = "23150033";

    /**
     * 批量同步AD域用户，单次操作最大数量1000个
     */
    String RCDC_OPEN_API_REST_BATCH_SYNC_AD_USER_OVER_NUM = "23020000";

    /**
     * AD域未配置或者未开启
     */
    String RCDC_OPEN_API_REST_BATCH_SYNC_AD_CONFIG_NO_OPEN_OR_NO_SET = "23020001";

    /**
     * AD域服务器连接超时
     */
    String RCDC_OPEN_API_REST_BATCH_SYNC_AD_CONNECTION_OVERTIME = "23020002";

    /**
     * 创建用户校验
     */
    String RCDC_OPEN_API_REST_CREATE_USER_VALID_ERROR = "23020003";

    /**
     * 用户变更组校验
     */
    String RCDC_OPEN_API_REST_MODIFY_USER_GROUP_NOT_AD_LDAP_ERROR = "23020004";

    /**
     * LDAP域未配置或者未开启
     */
    String RCDC_OPEN_API_REST_BATCH_SYNC_LDAP_CONFIG_NO_OPEN_OR_NO_SET = "23020008";

    /**
     * 增量同步LDAP域用户校验失败
     */
    String RCDC_OPEN_API_REST_BATCH_SYNC_LDAP_USER_VALID_ERROR = "23020009";

    /**
     * 虚拟交换机不存在
     */
    String RCDC_CODE_VSWITCH_NOT_EXIST = "10007";

    /**
     * 系统存在同名镜像模版
     */
    String RCDC_CODE_IMAGE_TEMPLATE_NAME_REPEATE = "10008";


    String RCDC_OPEN_API_REST_MODIFY_USER_GROUP_VALID_ERROR = "23020004";

    /**
     * 用户组不存在
     */
    String RCDC_OPEN_API_REST_USER_GROUP_NOT_EXIST = "23020007";

    /**
     * PublicBusinessKey.RCDC_USER_USERGROUP_NUM_OVER
     * 校验总组数量，不能超2000
     */
    String RCDC_USER_USERGROUP_NUM_OVER = "23020008";

    /**
     * PublicBusinessKey.RCDC_USER_SUB_USERGROUP_NUM_OVER
     * 校验子组数量，不能超200(创建或更新时校验)
     */
    String RCDC_USER_SUB_USERGROUP_NUM_OVER = "23020009";

    /**
     * BusinessKey.RCDC_USER_USERGROUP_HIERARCHY_OVER
     * 用户组层级不能超过12级
     */
    String RCDC_USER_USERGROUP_HIERARCHY_OVER = "23020010";

    /**
     * PublicBusinessKey.RCDC_USER_USERGROUP_HAS_DUPLICATION_NAME
     * 用户组[{0}]下存在同名组[{1}]
     */
    String RCDC_USER_USERGROUP_HAS_DUPLICATION_NAME = "23020011";


    /**
     * BusinessKey.RCDC_USER_USERGROUP_NOT_ALLOW_RESERVE_NAME
     * 检测组名是否为保留字
     */
    String RCDC_USER_USERGROUP_NOT_ALLOW_RESERVE_NAME = "23020012";

    /**
     * BusinessKey.RCDC_USER_USER_NAME_HAS_EXIST
     * 用户名已存在
     */
    String RCDC_USER_USER_NAME_HAS_EXIST = "23070002";

    /**
     * BusinessKey.RCDC_CLOUDDESKTOP_DESK_STRATEGY_NAME_REPEATED
     * 云桌面策略名称[{0}]重复
     */
    String RCDC_CLOUDDESKTOP_DESK_STRATEGY_NAME_REPEATED = "23070003";

    /**
     * BusinessKey.RCDC_CLOUDDESKTOP_DESK_LOCAL_MODE_STRATEGY_NOT_LEGAL
     * IDV云桌面策略[{0}]参数不合法，本地盘和云桌面重定向功能必须同时开启或关闭
     */
    String RCDC_CLOUDDESKTOP_DESK_LOCAL_MODE_STRATEGY_NOT_LEGAL = "23070004";


    /**
     * 删除用户校验
     */
    String RCDC_OPEN_API_REST_DELETE_USER_VALID_ERROR = "23020005";

    /**
     * AD域用户和LDAP用户不支持移动分组
     */
    String RCDC_OPEN_API_REST_AD_LDAP_MOVE_GROUP_ERROR = "23020006";

    /**
     * 第三方用户不支持移动分组
     */
    String RCDC_OPEN_API_REST_THIRD_PARTY_MOVE_GROUP_ERROR = "23020013";


    /**
     * 集群转发异常
     */
    String RCDC_OPEN_API_REST_FORWARD_RCDC_REQUEST_ERROR = "23170002";

    /**
     * 云桌面未开启安全访问网关策略
     */
    String RCDC_RCO_CLOUDDESKTOP_STRATEGY_NOT_ENABLE_AGREEMENT_AGENCY = "23011201";

    /**
     * 云桌面未开启网页客户端接入策略
     */
    String RCDC_RCO_CLOUDDESKTOP_STRATEGY_NOT_ENABLE_ENABLEWEB_CLIENT = "23011202";

    String RCDC_RCO_USBDEVICE_FIRM_AND_PRODUCT_ALL_UNKNOW_FORBID = "23170001";

    /**
     * samba未配置
     */
    String RCDC_OPEN_API_REST_CHECK_SAMBA_NOT_CONFIG = "23170003";

    /**
     * samba未挂载
     */
    String RCDC_OPEN_API_REST_CHECK_SAMBA_UMOUNT = "23170004";

    /**
     * 维护模式中
     */
    String RCDC_OPEN_API_REST_CHECK_IN_MAINTENANCE_MODE = "23170005";

    /**
     * 服务器备份中
     */
    String RCDC_OPEN_API_REST_CHECK_IN_BACKUP_RUNNING = "23170006";

    /**
     * 创建镜像中
     */
    String RCDC_OPEN_API_REST_CHECK_IN_CREATING_IMAGE = "23170007";

    /**
     * 配置向导未完成
     */
    String RCDC_OPEN_API_REST_CHECK_IN_CONFIG_WIZARD_SHOWING = "23170008";

    /**
     * 桌面池不存在
     */
    String RCDC_OPEN_API_REST_DESKTOP_POOL_NOT_EXIST = "23180001";

    /**
     * 桌面池处于维护模式
     */
    String RCDC_OPEN_API_REST_DESKTOP_POOL_UNDER_MAINTENANCE = "23180101";

    /**
     * 桌面池无云桌面资源
     */
    String RCDC_OPEN_API_REST_DESKTOP_POOL_NO_RESOURCE = "23180102";

    /**
     * 用户未分配桌面池
     */
    String RCDC_OPEN_API_REST_DESKTOP_POOL_USER_NO_AUTH = "23180103";

    /**
     * 分配桌面池失败
     */
    String RCDC_OPEN_API_REST_DESKTOP_POOL_ASSIGN_ERROR = "23180104";

    /**
     * 桌面池状态不可用
     */
    String RCDC_OPEN_API_REST_DESKTOP_POOL_STATUS_ERROR = "23180105";

    /**
     * 桌面池关联的镜像模板状态不可用
     */
    String RCDC_OPEN_API_REST_DESKTOP_POOL_IMAGE_STATUS_ERROR = "23180106";

    /**
     * 桌面所属用户个人盘状态不能挂载
     */
    String RCDC_OPEN_API_REST_DESKTOP_USER_DISK_STATUS_ERROR = "23180107";

    /**
     * 云桌面在该时间范围已被限制登录
     */
    String RCDC_OPEN_API_REST_DESKTOP_LOGIN_TIME_LIMIT = "23011203";

    /**
     * 云桌面在IP访问规则内限制使用
     */
    String RCDC_OPEN_API_REST_DESKTOP_LOGIN_IP_LIMIT = "23011204";

    /**
     * 池桌面在该时间范围已被限制登录
     */
    String RCDC_OPEN_API_REST_DESKTOP_POOL_LOGIN_TIME_LIMIT = "23180108";

    /**
     * 云桌面策略相关
     */
    String RCDC_OPEN_API_REST_DESKTOP_STRATEGY_CREATE_ERROR = "23190001";
    String RCDC_OPEN_API_REST_DESKTOP_STRATEGY_UPDATE_ERROR = "23190002";
    String RCDC_OPEN_API_REST_DESKTOP_STRATEGY_DELETE_ERROR = "23190003";
    String RCDC_OPEN_API_REST_DESKTOP_STRATEGY_NOT_AVAILABLE = "23190004";
    String RCDC_OPEN_API_REST_DESKTOP_REFRESH_STRATEGY_SUC_LOG = "23190005";
    String RCDC_OPEN_API_REST_DESKTOP_STRATEGY_TYPE_NOT_EXIST = "23190006";

    /**
     * 短信认证相关
     */
    String RCDC_OPEN_API_REST_USER_NOT_BIND_PHONE_NUMBER_ERROR = "23070501";
    String RCDC_OPEN_API_REST_SMS_AUTH_NOT_SUPPORT_VISITOR_USER_ERROR = "23070502";
    String RCDC_OPEN_API_REST_USER_NOT_OPEN_SMS_AUTH_ERROR = "23070503";
    String RCDC_OPEN_API_REST_USER_NON_LOCAL_PERSONAL_NOT_SUPPORTED_MODIFY_PWD = "23070504";
    String RCDC_OPEN_API_REST_SMS_SEND_PHONE_NUMBER_NOT_NULL_ERROR = "23070505";
    String RCDC_OPEN_API_REST_SMS_PWD_RECOVER_NOT_OPEN_ERROR = "23070506";
    String RCDC_OPEN_API_REST_GLOBAL_SMS_AUTH_NOT_OPEN_ERROR = "23070507";
    String RCDC_OPEN_API_REST_USER_SMS_VERIFY_CODE_REFRESH_ERROR = "23070508";
    String RCDC_OPEN_API_REST_PHONE_SMS_VERIFY_CODE_DAY_NUMBER_EXCEED_MAX_LIMIT = "23070509";
    String RCDC_OPEN_API_REST_SMS_GATEWAY_NOT_OPEN_ERROR = "23070510";
    String RCDC_OPEN_API_REST_SMS_RESULT_RESOLVING_FAIL = "23070511";
    String RCDC_OPEN_API_REST_SEND_SMS_PHONE_BIND_PHONE_NO_EQUAL_ERROR = "23070512";
    String RCDC_OPEN_API_REST_SMS_VERIFY_CODE_MUST_EQUAL = "23070513";
    String RCDC_OPEN_API_REST_BIND_PHONE_NOT_SUPPORT_VISITOR_USER_ERROR = "23070514";
    String RCDC_OPEN_API_REST_USER_PHONE_NOT_NULL_ERROR = "23070515";
    String RCDC_OPEN_API_REST_SMS_VERIFY_CODE_EXPIRED_ERROR = "23070516";
    String RCDC_OPEN_API_REST_SMS_SEND_FAIL_ERROR = "23070517";
    String RCDC_OPEN_API_REST_SMS_SEND_CONTENT_ENCODING_ERROR = "23070518";
    String RCDC_OPEN_API_REST_SMS_SERVER_CONNECT_ERROR = "23070519";
    String RCDC_OPEN_API_REST_SMS_USER_NOT_EXISTS = "23070520";
    String RCDC_OPEN_API_REST_NEW_OLD_PWD_NOT_EQUAL = "23070521";

    /**
     * 第三方扫码认证校验
     */
    String RCDC_OPEN_API_REST_USER_LOCKED = "23070530";
    String RCDC_OPEN_API_REST_USER_DISABLE = "23070531";
}
