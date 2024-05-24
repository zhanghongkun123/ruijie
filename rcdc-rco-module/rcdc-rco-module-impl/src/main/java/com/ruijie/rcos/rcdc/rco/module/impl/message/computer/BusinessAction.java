package com.ruijie.rcos.rcdc.rco.module.impl.message.computer;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/30 14:51
 *
 * @author ketb
 */
public interface BusinessAction {
    /**
     * 终端上线
     */
    String ONLINE = "online";

    /**
     * 终端离线
     */
    String OFFLINE = "offline";

    /**
     * 接收PC设备信息
     */
    String REPORT_COMPUTER_INFO = "report_computer_info";

    /**
     *  查询设备故障信息
     */
    String CHECK_FAULT = "check_fault";

    /**
     *  报障
     */
    String REPORT_FAULT = "report_fault";

    /**
     * 解除报障
     */
    String RELIEVE_FAULT = "relieve_fault";

    /**
     *  远程协助请求结果
     */
    String REMOTE_ASSIST_RESULT = "remote_assist_result";

    /**
     * 远程协助状态上报
     */
    String REMOTE_ASSIST_REPORT_STATE = "remote_assist_report_state";

    /**
     * 关闭远程协助
     */
    String CLOSE_REMOTE_ASSIST = "close_remote_assist";

    /**
     * 远程协助心跳
     */
    String REMOTE_HEART = "remote_heart";

    /**
     * 查询版本号
     */
    String QUERY_ASSISTANT_VERSION = "query_assistant_version";

    /**
     * 获取升级包下载路径
     */
    String ASSISTANT_APP_COMPONENT_PATH = "assistant_app_component_path";

    /**
     * 查询ftp账号密码
     */
    String QUERY_FTP_INFO = "query_ftp_info";

    /**
     * 获取USB外设配置
     */
    String QUERY_USB_INFO = "query_usb_info";
}
