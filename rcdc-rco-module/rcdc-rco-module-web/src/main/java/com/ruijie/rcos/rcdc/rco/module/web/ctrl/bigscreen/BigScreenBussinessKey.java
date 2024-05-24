package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24 18:37
 *
 * @author zhangyichi
 */
public interface BigScreenBussinessKey {

    /**
     * 配置管理
     */
    String RCDC_RCO_BIGSCREEN_COMMON_CONFIG_UPDATE_SUCCESS = "rcdc_rco_bigscreen_common_config_update_success";
    String RCDC_RCO_BIGSCREEN_COMMON_CONFIG_UPDATE_FAIL = "rcdc_rco_bigscreen_common_config_update_fail";
    String RCDC_RCO_BIGSCREEN_COMMON_CONFIG_WARN_BIGGER_THAN_ALARM = "23200984";
    String RCDC_RCO_BIGSCREEN_COMMON_CONFIG_VALUE_MUST_BE_INTEGER = "23200985";

    /*
     * 机柜管理
     */
    String RCDC_RCO_CABINET_EDIT_SUCCESS = "rcdc_rco_cabinet_edit_success";
    String RCDC_RCO_CABINET_EDIT_FAIL = "rcdc_rco_cabinet_edit_fail";
    String RCDC_RCO_CABINET_CREATE_SUCCESS = "rcdc_rco_cabinet_create_success";
    String RCDC_RCO_CABINET_CREATE_FAIL = "rcdc_rco_cabinet_create_fail";
    String RCDC_RCO_CABINET_SERVER_INFO_NOT_EXIST = "23200448";
    String RCDC_RCO_CABINET_CONFIG_SERVER_SUCCESS = "rcdc_rco_cabinet_config_server_success";
    String RCDC_RCO_CABINET_CONFIG_SERVER_FAIL = "rcdc_rco_cabinet_config_server_fail";
    String RCDC_RCO_CABINET_DELETE_SERVER_SUCCESS = "rcdc_rco_cabinet_delete_server_success";
    String RCDC_RCO_CABINET_DELETE_SERVER_FAIL = "rcdc_rco_cabinet_delete_server_fail";
    String RCDC_RCO_CABINET_DELETE_SUCCESS = "rcdc_rco_cabinet_delete_success";
    String RCDC_RCO_CABINET_DELETE_FAIL = "rcdc_rco_cabinet_delete_fail";

    /**
     * 大屏信息收集
     */
    String RCDC_RCO_BIGSCREEN_ABNORMAL_DESKTOP_LIST_EMPTY = "rcdc_rco_bigscreen_abnormal_desktop_list_empty";
    String RCDC_RCO_BIGSCREEN_START_TIME_LATER_THAN_END_TIME = "23200986";
}
