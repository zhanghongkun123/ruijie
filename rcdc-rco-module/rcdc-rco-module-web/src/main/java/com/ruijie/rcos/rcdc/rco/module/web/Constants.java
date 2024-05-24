package com.ruijie.rcos.rcdc.rco.module.web;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/11
 *
 * @author Jarman
 */
public interface Constants extends com.ruijie.rcos.rcdc.rco.module.def.constants.Constants {

    /**
     * 终端检测带宽正常标准值,小于等于20Mb为异常
     */
    double TERMINAL_DETECT_BINDWIDTH_NORM = 20;

    /**
     * 终端检测延时正常标准值，大于等于20ms为异常
     */
    int TERMINAL_DETECT_DELAY_NORM = 20;

    /**
     * 丢包率正常标准值(该数值是实际比率乘以100的数值)，大于等于0.1时为异常
     */
    double TERMINAL_DETECT_PACKET_LOSS_RATE = 0.1;
    
    /** 全局保存的http端口字段名 */
    String HTTP_PORT = "http_port";
    
    /** RCDC授权的featureCode */
    String LICENSE_FEATURE_CODE = "RG-COS-CDC";
    
    /** RCDC授权的featureCode */
    String LICENSE_FEATURE_ID = "RG-COS-CDC";

    /**
     * 默认最大可用策略数
     */
    int MAX_AVAILABLE_STRATEGY_AMOUNT = 100;

    String INIT_USER_PASSWORD = "123456";

    String SECRT_USER_PASSWORD = "******";

    String DESK_TYPE_PC = "PC";

    /**
     * 音频设备UUID
     */
    UUID AUDIO_DEVICE = UUID.fromString("6be5d94e-ed5e-4acc-90d5-8ee1ad75666d");

    /**
     * 云桌面策略使用应用分层时系统盘容量默认增加大小(G)
     */
    int SYSTEM_DISK_CAPACITY_INCREASE_SIZE = 15;

    /**
     * 终端管理密码正则表达式，大写字母、小写字母、数字、特殊字符中三种及以上，长度[8,16]
     */
    String TERMINAL_MNG_PWD_REGEX = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;"
            + "'\\[\\]\\s]+$)(?![a-z0-9]+$)(?![a-z`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]+$)(?![0-9`~!@#$%^&*()_\\"
            + "-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]+$)[a-zA-Z0-9`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]{8,16}$";

    /**
     * 管理员密码正则表达式，大写字母、小写字母、数字、特殊符号三种及以上，长度[8,32]
     */
    String ADMIN_PASSWORD_REGEX = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z`~!@#$%^&*()_\\-+=<>?:\""
            + "{}|,./\\\\;'\\[\\]\\s]+$)(?![a-z0-9]+$)(?![a-z`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]+$)(?![0-9`~!@#$"
            + "%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]+$)[a-zA-Z0-9`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]{8,32}$";

    /**
     * 用户密码正则表达式，大写字母、小写字母、数字、特殊符号三种及以上，长度[8,32]
     */
    String USER_PASSWORD_REGEX = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z`~!@#$%^&*()_\\-+=<>?:\""
            + "{}|,./\\\\;'\\[\\]\\s]+$)(?![a-z0-9]+$)(?![a-z`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]+$)(?![0-9`~!@#$"
            + "%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]+$)[a-zA-Z0-9`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]{6,32}$";


    /**
     * VDI登录IP限制允许设置的最大IP段数量
     */
    int IP_LIMIT_MAX_SIZE = 50;

    /**
     * 一键安装
     */
    String ONE_CLICK_INSTALL = "one_click_install";

    /**
     * 云办公客户端
     */
    String CLOUD_OFFICE_CLIENT = "cloud_office_client";

    /**
     * 镜像模板key
     */
    String IMAGE_TEMPLATE_ID = "imageTemplateId";

    /**
     * 存储池key
     */
    String STORAGE_POOL_ID = "storagePoolId";

    /**
     * 存储池key
     */
    String OS_FILE_ID = "osFileId";

    /**
     * 计算集群key
     */
    String CLUSTER_ID = "clusterId";

    /**
     * 网络策略key
     */
    String NETWORK_ID = "networkId";

    String SAMBA_APP_PATH = "/opt/samba/soft/";

    String SOFT_NAME = "softName";



    /**
     * 端口最小值
     */
    int PORT_MINIMUM = 0;

    /**
     * 端口最大值
     */
    int PORT_MAXIMUM = 65535;

    /**
     * ip地址合法性正则表达式
     */
    String IP_REGEX = "(2[01]\\d|22[0-3]|1\\d\\d|[1-9]\\d|[1-9])"
            + "(\\.(2[0-4]\\d|25[0-5]|1\\d\\d|[1-9]\\d|\\d)){3}";

    /**
     * 域名地址合法性正则表达式
     */
    String DOMAIN_REGEX = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";

    /**
     * 展示远程唤醒提示语
     */
    String SHOW_WAKE_UP_TI_CODE = "WAKE_UP_FAILED";

    String LIMIT = "limit";

    Integer MIN_LIMIT = 1;

    Integer MAX_LIMIT = 1000;

    String COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_ID_FIELD_NAME = "packageId";

    String COMPONENT_INDEPENDENT_UPGRADE_TASK_STATE_FIELD_NAME = "upgradeTaskState";

    String COMPONENT_INDEPENDENT_UPGRADE_TASK_ID_FIELD_NAME = "upgradeTaskId";

    String COMPONENT_INDEPENDENT_UPGRADE_TASK_PACKAGE_TYPE_FIELD_NAME = "packageType";

    String COMPONENT_INDEPENDENT_UPGRADE_TERMINAL_UPGRADE_STATE_FIELD_NAME = "terminalUpgradeState";

    String ERROR_MSG_SPERATOR = "，";

    String TERMINAL_TYPE = "terminalType";

    String TERMINAL_OS_TYPE = "terminalOsType";

    String TERMINAL_ID_ARR = "terminalIdArr";

    Integer MAX_TERMINAL_SUPPORT = 10000;
}
