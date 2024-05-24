package com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/2
 *
 * @author Jarman
 */
public interface GuestToolCmdId {
    /** 消息推送 */
    /** GT请求确认或忽略消息 */
    String RCDC_GT_CMD_ID_SMS = "105";

    /** GT请求加域信息 */
    String RCDC_GT_CMD_ID_AD = "140";
    int RCDC_GT_PORT_ID_AD = 135;

    /** 通知guesttool ad域用户权限变更 */
    int RCDC_GT_CMD_ID_AD_USER = 143;
    int RCDC_GT_PORT_ID_AD_USER = 135;

    /** GT 上报打印机配置信息 **/
    int RCDC_GT_PORT_ID_PRINTER_CONFIG = 140;
    /** GT 上报自动加入AD域结果 **/
    String RCDC_GT_CMD_ID_AD_RESULT = "141";
    String RCDC_GT_CMD_ID_UPLOAD_PRINTER_CONFIG = "220";
    /** GT 查询打印机配置信息 **/
    String RCDC_GT_CMD_ID_SEARCH_PRINTER_CONFIG = "221";
    /** GT 下载打印机配置信息 **/
    String RCDC_GT_CMD_ID_DOWNLOAD_PRINTER_CONFIG = "222";
    /** GT 获取特殊配置的hash **/
    String RCDC_GT_CMD_ID_GET_PRINTER_SPECIAL_CONFIG_HASH = "223";
    /** GT 获取特殊配置的详细信息 **/
    String RCDC_GT_CMD_ID_DOWNLOAD_PRINTER_SPECIAL_CONFIG = "224";

    /** GT 获取虚拟托盘的信息 **/
    String RCDC_GT_CMD_ID_GET_TRAY_INFO = "240";
    int RCDC_GT_PORT_ID_TRAY_INFO = 136;

    /* RA Tool 获取远程协助信息 **/
    int RCDC_RA_TOOL_PORT_ID_REMOTE_ASSIST_STATUS = 501;
    String RCDC_RA_TOOL_CMD_ID_REMOTE_ASSIST_STATUS = "5003";

    /** GuestTool远程协助心跳消息 */
    String REMOTE_ASSIST_HEART_BEAT = "112";

    /** GT 上报软件信息 */
    String RCDC_GT_DESKSOFT_MSG = "170";


    /**
     * 软件上报配置
     */
    String RCDC_GT_DESKSOFT_USE_CONFIG = "171";

    /**
     * 通知GT port id 软件上报开关状态
     */
    Integer NOTIFY_GT_DESKSOFT_MSG_STATUS_PORT_ID = 136;

    /**
     * 通知GT cmd id 软件上报开关状态
     */
    Integer NOTIFY_GT_DESKSOFT_MSG_STATUS_CMD_ID = 171;



    String RCDC_GT_CMD_ID_EVENT_NOTICE = "5";


    /**
     * 通知GT用户登录 port id
     */
    Integer NOTIFY_GT_DISK_MAPPING_PORT_ID = 147;

    /**
     * 通知GT用户登录 cmd id
     */
    Integer NOTIFY_GT_DISK_MAPPING_CMD_ID = 322;

    /**
     * 通知GT同步会话信息 port id
     */
    Integer NOTIFY_GT_USER_SESSION_PORT_ID = 135;

    /**
     * 通知GT同步会话信息 cmd id
     */
    Integer NOTIFY_GT_USER_SESSION_CMD_ID = 400;
    /** GuestTool -> RCDC 获取软件策略信息 */
    String RCDC_GT_CMD_ID_SOFTWARE_CONTROL_INFO = "310";
    int RCDC_GT_PORT_ID_SOFTWARE_CONTROL_INFO = 148;

    /**
     * gt获取用户密码 cmdId/ cdc下发密码
     */
    String GT_GET_USER_PWD_CMD_ID = "228";

    /**
     * 用户密码更新 portId
     */
    Integer GT_GET_USER_PWD_PORT_ID = 0;

    /**
     * 主动推送新密码到gt cmdId
     */
    String PUSH_USER_NAME_WITH_PWD_CMD_ID = "229";

    /** sunny -> 上报sunny状态 */
    String RCDC_SUNNY_CMD_ID_STATUS_REPORT_INFO = "340";
    int RCDC_SUNNY_PORT_ID_STATUS_REPORT_INFO = 149;

    /** RCDC -> GuestTool 获取用户信息 */
    String NOTIFY_GT_CMD_ID_USER_INFO = "351";
    int NOTIFY_GT_PORT_ID_USER_INFO = 135;

    /** GuestTool -> RCDC 获取用户信息 */
    String RCDC_GT_CMD_ID_USER_INFO = "352";
    int RCDC_GT_PORT_ID_USER_INFO = 135;

    /**
     * GT => RCDC 获取用户配置策略
     */
    String RCDC_GT_CMD_ID_USER_PROFILE_STRATEGY = "6000";

    Integer RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY = 601;

    /** RCDC -> GuestTool 获取用户个人磁盘信息 */
    String NOTIFY_GT_CMD_ID_DISK_INFO = "411";
    int NOTIFY_GT_PORT_ID = 135;

    Integer NOTIFY_GT_CMD_ID_CLEAN_USER_PROFILE_PATH = 6001;

    String NOTIFY_GT_CMD_ID_FAIL_CLEAN = "412";
    String RCDC_GT_CMD_ID_ACK_FROM_UPM = "413";

    /**
     * GT -> CDC 请求备份磁盘的SN码
     */
    String RCDC_GT_OBTAIN_BACKUP_DISK_SN_CMD_ID = "379";
    int RCDC_GT_OBTAIN_BACKUP_DISK_SN_PORT_ID = 135;

    /** GusetTool启动完成消息 */
    String RCDC_GT_CMD_ID_GT_START_END = "102";

    /**
     * app guard 启动成功消息,文件分发任务可通过该接口重置状态
     */
    String RCDC_GT_CMD_ID_APP_GUARD_START_END = "7003";

    /** RCDC -> GuestTool 修复音频异常 */
    String REPAIR_AUDIO_EXCEPTION_CMD_ID = "420";
    /** RCDC -> GuestTool 修复音频异常 */
    int REPAIR_AUDIO_EXCEPTION_PORT_ID = 0;


    /** 文件导出审批组件 PORT_ID */
    int RCDC_GT_PORT_ID_AUDIT_FILE = 730;
    /** 获取文件导出审批当前策略信息策略 */
    String RCDC_GT_CMD_ID_AUDIT_FILE_GLOBAL_STRATEGY = "7010";
    /** 创建审计申请单 */
    String RCDC_GT_CMD_ID_CREATE_AUDIT_APPLY = "7011";
    /** 审计申请单详情 */
    String RCDC_GT_CMD_ID_AUDIT_APPLY_DETAIL = "7012";
    /** 审计申请单上报变更状态 */
    String RCDC_GT_CMD_ID_AUDIT_APPLY_CLIENT_NOTIFY = "7013";
    /** 获取审计申请单列表 */
    String RCDC_GT_CMD_ID_GET_AUDIT_APPLY_LIST = "7014";

    /** 获取安全打印机当前策略信息策略 */
    String RCDC_GT_CMD_ID_AUDIT_PRINTER_GLOBAL_STRATEGY = "7015";

    /**
     * gt上报windows用户名更新失败事件 cmdId
     */
    String GT_REPORT_UPDATE_WINDOWS_ACCOUNT_OR_PWD_ERROR = "227";

    /** windowsUAM应用冲突上报 */
    String REPORT_SOFT_CONFLICT = "368";
}
