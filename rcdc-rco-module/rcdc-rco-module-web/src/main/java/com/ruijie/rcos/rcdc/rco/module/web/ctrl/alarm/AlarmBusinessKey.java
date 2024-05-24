package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月19日
 *
 * @author xgx
 */
public interface AlarmBusinessKey {
    /**编辑告警配置*/
    String BASE_ALARM_EDIT_ALARM_SUCCESS = "base_alarm_edit_alarm_success";

    String BASE_ALARM_EDIT_ALARM_FAIL = "base_alarm_edit_alarm_fail";

    /**解除告警*/
    String BASE_ALARM_RELEASE_ALARM_SUCCESS = "base_alarm_release_alarm_success";

    String BASE_ALARM_RELEASE_ALARM_FAIL = "base_alarm_release_alarm_fail";

    String BASE_ALARM_BATCH_RELEASE_ALARM_TASK_TITLE = "base_alarm_batch_release_alarm_task_title";

    String BASE_ALARM_BATCH_RELEASE_ALARM_TASK_DESC = "base_alarm_batch_release_alarm_task_desc";

    String BASE_ALARM_BATCH_RELEASE_ALARM_TASK_RESULT = "base_alarm_batch_release_alarm_task_result";

    String BASE_ALARM_BATCH_RELEASE_ALARM_TASK_ITEM_NAME = "base_alarm_batch_release_alarm_task_item_name";


    /**邮件配置*/
    String BASE_ALARM_EDIT_EMAIL_CONFIG_SUCCESS = "base_alarm_edit_email_config_success";

    String BASE_ALARM_EDIT_EMAIL_CONFIG_FAIL  = "base_alarm_edit_email_config_fail";

    String BASE_ALARM_ARG_VALUE_ILLEGAL = "23200704";

    String BASE_ALARM_SEND_TEST_EMAIL_SUCCEED = "base_alarm_send_test_email_succeed";

    /**操作提示*/
    String BASE_ALARM_OPERATOR_SUCCEED = "base_alarm_operator_succeed";

    String BASE_ALARM_OPERATOR_FAIL = "base_alarm_operator_fail";
}
