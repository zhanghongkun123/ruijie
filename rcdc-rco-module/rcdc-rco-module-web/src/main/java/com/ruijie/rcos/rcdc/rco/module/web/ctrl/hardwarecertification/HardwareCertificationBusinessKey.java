package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification;

/**
 *
 * Description:硬件特征码国际化
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年03月09日
 *
 * @author zhang.zhiwen
 */
public interface HardwareCertificationBusinessKey {

    /** 认证策略配置成功 */
    String RCDC_HARDWARE_CERTIFICATION_CONFIG_SUCCESS = "rcdc_hardware_certification_config_success";

    /** 认证策略配置失败 */
    String RCDC_HARDWARE_CERTIFICATION_CONFIG_FAIL = "rcdc_hardware_certification_config_fail";

    /**
     * 驳回失败
     */
    String RCDC_HARDWARE_CERTIFICATION_REJECT_FAIL = "rcdc_hardware_certification_reject_fail";

    /**
     * 驳回成功
     */
    String RCDC_HARDWARE_CERTIFICATION_REJECT_SUCCESS = "rcdc_hardware_certification_reject_success";

    String RCDC_HARDWARE_CERTIFICATION_REJECT_SUCCESS_LOG = "rcdc_hardware_certification_reject_success_log";

    /**
     * 批准操作失败
     */
    String RCDC_HARDWARE_CERTIFICATION_REJECT_ERROR = "rcdc_hardware_certification_reject_error";

    /**
     * 批准失败
     */
    String RCDC_HARDWARE_CERTIFICATION_APPROVE_FAIL = "rcdc_hardware_certification_approve_fail";

    /**
     * 批准操作失败
     */
    String RCDC_HARDWARE_CERTIFICATION_APPROVE_ERROR = "rcdc_hardware_certification_approve_error";

    /**
     * 批准成功
     */
    String RCDC_HARDWARE_CERTIFICATION_APPROVE_SUCCESS = "rcdc_hardware_certification_approve_success";

    String RCDC_HARDWARE_CERTIFICATION_APPROVE_SUCCESS_LOG = "rcdc_hardware_certification_approve_success_log";

    /**
     * 批准操作失败：数量已超最大值
     */
    String RCDC_HARDWARE_CERTIFICATION_APPROVE_FAIL_MAX = "23200890";

    /**
     * 删除失败
     */
    String RCDC_HARDWARE_CERTIFICATION_DELETE_FAIL = "rcdc_hardware_certification_delete_fail";

    /**
     * 删除成功
     */
    String RCDC_HARDWARE_CERTIFICATION_DELETE_SUCCESS = "rcdc_hardware_certification_delete_success";

    String RCDC_HARDWARE_CERTIFICATION_DELETE_SUCCESS_LOG = "rcdc_hardware_certification_delete_success_log";

    /**
     * 删除发生错误
     */
    String RCDC_HARDWARE_CERTIFICATION_DELETE_ERROR = "23200891";

    /**
     * 删除用户硬件特征码审批记录任务名
     */
    String RCDC_HARDWARE_CERTIFICATION_DELETE_ITEM_NAME = "rcdc_hardware_certification_delete_item_name";

    /**
     * 批量删除用户硬件特征码审批记录任务名
     */
    String RCDC_HARDWARE_CERTIFICATION_BATCH_DELETE_ITEM_NAME = "rcdc_hardware_certification_batch_delete_item_name";

    /**
     * 批量删除用户硬件特征码审批记录任务描述
     */
    String RCDC_HARDWARE_CERTIFICATION_BATCH_DELETE_TASK_DESC = "rcdc_hardware_certification_batch_delete_task_desc";

    /**
     * 批量删除用户硬件特征码审批记录任务执行结果
     */
    String RCDC_HARDWARE_CERTIFICATION_BATCH_DELETE_RESULT = "rcdc_hardware_certification_batch_delete_result";

    String RCDC_HARDWARE_CERTIFICATION_CREATE_BINDING_ITEM_NAME = "rcdc_hardware_certification_create_binding_item_name";
    String RCDC_HARDWARE_CERTIFICATION_IMPORT_BINDING_ITEM_NAME = "rcdc_hardware_certification_import_binding_item_name";
    String RCDC_HARDWARE_CERTIFICATION_CREATE_BINDING_TASK_NAME = "rcdc_hardware_certification_create_binding_task_name";
    String RCDC_HARDWARE_CERTIFICATION_IMPORT_BINDING_TASK_NAME = "rcdc_hardware_certification_import_binding_task_name";

    /**
     * 模板相关
     */
    String RCDC_IMPORT_USER_MAC_BINDING_USER_NAME = "rcdc_import_user_mac_binding_user_name";
    String RCDC_IMPORT_USER_MAC_BINDING_MAC = "rcdc_import_user_mac_binding_mac";
    String RCDC_IMPORT_USER_MAC_NOT_BINDING_DATA = "rcdc_import_user_mac_not_binding_data";
    String RCDC_IMPORT_USER_MAC_OVER_MAC_LIMIT = "rcdc_import_user_mac_over_mac_limit";
    String RCDC_IMPORT_USER_MAC_VALIDATE_ERROR = "rcdc_import_user_mac_validate_error";
    String RCDC_IMPORT_USER_MAC_NOT_ALLOW_EMPTY = "rcdc_import_user_mac_not_allow_empty";
    String RCDC_IMPORT_USER_MAC_TEMPLATE_IS_INVALIDATE = "rcdc_import_user_mac_template_is_invalidate";

    String RCDC_IMPORT_USER_MAC_FAIL = "rcdc_import_user_mac_fail";

}
