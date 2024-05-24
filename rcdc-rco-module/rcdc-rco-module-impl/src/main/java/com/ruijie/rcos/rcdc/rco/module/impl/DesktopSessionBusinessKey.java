package com.ruijie.rcos.rcdc.rco.module.impl;

/**
 * Description: 会话信息常量
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月21日
 *
 * @author wangjie9
 */
public interface DesktopSessionBusinessKey {

    String RCDC_RCO_EXPORT_DESKTOP_NAME = "rcdc_rco_export_desktop_name";
    // 终端ip获取不到，暂不输出到excel
    String RCDC_RCO_EXPORT_TERMINALIP = "rcdc_rco_export_terminalip";
    String RCDC_RCO_EXPORT_DESKTOPPOOLNAME = "rcdc_rco_export_desktoppool_name";
    String RCDC_RCO_EXPORT_USERNAME = "rcdc_rco_export_username";
    String RCDC_RCO_EXPORT_REALNAME = "rcdc_rco_export_realname";
    String RCDC_RCO_EXPORT_GROUPNAME = "rcdc_rco_export_groupname";
    String RCDC_RCO_EXPORT_CREATETIME = "rcdc_rco_export_createtime";
    String RCDC_RCO_EXPORT_LASTCREATETIMESECOND = "rcdc_rco_export_lastcreatetimesecond";
    String RCDC_RCO_EXPORT_LASTIDLETIMESECOND = "rcdc_rco_export_lastidletimesecond";
    String RCDC_RCO_EXPORT_SESSIONSTATUS = "rcdc_rco_export_sessionstatus";

    String RCDC_RCO_DESKTOP_SESSION_DELETE_SUCCESS = "rcdc_rco_desktop_session_delete_success";
    String RCDC_RCO_DESKTOP_SESSION_SAVE_SUCCESS = "rcdc_rco_desktop_session_save_success";

    String RCDC_RCO_DESKTOP_SESSION_NOT_EXIT = "rcdc_rco_desktop_session_not_exit";

}
