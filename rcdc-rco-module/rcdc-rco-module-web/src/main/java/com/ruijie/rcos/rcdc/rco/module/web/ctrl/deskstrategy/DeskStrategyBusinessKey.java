package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月10日
 *
 * @author wjp
 */
public interface DeskStrategyBusinessKey {

    /** 操作成功 */
    String RCO_DESK_STRATEGY_MODULE_OPERATE_SUCCESS = "rco_desk_strategy_module_operate_success";
    /** 操作失败 */
    String RCO_DESK_STRATEGY_MODULE_OPERATE_FAIL = "rco_desk_strategy_module_operate_fail";

    String RCO_DESK_STRATEGY_INVALID_VDI_MEM_VALUE = "23200823";

    String RCO_DESK_STRATEGY_INVALID_VDI_MEM_VALUE_LIMIT = "23200824";

    String RCO_DESK_STRATEGY_INVALID_VDI_PERSON_DISK_VALUE_LIMIT = "23200825";

    String RCO_DESK_STRATEGY_STRATEGY_NOT_LEGAL = "23200826";

    String RCO_DESK_STRATEGY_QUERY_EXCEPTIONS = "rco_desk_strategy_query_exceptions";

    String RCO_DESK_QUERY_EXCEPTIONS = "rco_desk_query_exceptions";

    String RCO_DESK_POOL_QUERY_EXCEPTIONS = "rco_desk_pool_query_exceptions";

    String RCO_DESK_STRATEGY_EDIT_NOT_SUPER_ADMIN = "rco_desk_strategy_edit_not_super_admin";

    String RCO_DESK_STRATEGY_VGPU_INFO_ERROR = "rco_desk_strategy_vgpu_info_error";

    String RCO_DESK_STRATEGY_VGPU_INFO_TYPE_SIZE_ERROR = "rco_desk_strategy_vgpu_info_type_size_error";

    String RCO_DESK_STRATEGY_VGPU_OPTIONS_NOT_EXIST = "23200827";

    String RCO_DESK_STRATEGY_UPDATE_STRATEGY_TYPE_ERROR = "rco_desk_strategy_update_strategy_type_error";

    String RCDC_CLOUDDESKTOP_DESK_STRATEGY_INVALID_CREATE_MODE_VDI = "23200829";
    String RCDC_RCO_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_NOT_FULL_STRATEGY_IDV = "rcdc_rco_full_system_disk_desk_can_not_use_not_full_strategy_idv";
    String RCDC_RCO_NOT_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_FULL_STRATEGY_IDV = "rcdc_rco_not_full_system_disk_desk_can_not_use_full_strategy_idv";
    String RCDC_RCO_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_NOT_FULL_STRATEGY_VOI = "rcdc_rco_full_system_disk_desk_can_not_use_not_full_strategy_voi";
    String RCDC_RCO_NOT_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_FULL_STRATEGY_VOI = "rcdc_rco_not_full_system_disk_desk_can_not_use_full_strategy_voi";

    String RCDC_RCO_STRATEGY_TYPE_NOT_MATCH = "23200828";

    String RCO_DESK_STRATEGY_HAD_DATA_DISK_CANNOT_CLOSE = "23200830";

    String RCO_DESK_STRATEGY_DYNAMIC_POOL_CANNOT_OPEN_DISK = "23200831";

    /** 策略关联的云桌面的镜像模板显卡驱动未安装 */
    String RCDC_DESK_STRATEGY_UPDATE_BIND_IMAGE_GPU_NOT_SUPPORT = "23200832";

    String RCO_DESK_STRATEGY_DESK_REDIRECT_BIND_DESK_MUST_HAS_PERSON_DISK = "23201532";
    String RCO_DESK_STRATEGY_DESK_REDIRECT_BIND_POOL_MUST_HAS_PERSON_DISK = "23201533";
    String RCO_DESK_STRATEGY_DESK_REDIRECT_BIND_USER_GROUP_MUST_HAS_PERSON_DISK = "23201534";
    String RCO_DESK_STRATEGY_SESSION_TYPE_NON_CONFORMANCE = "23201428";
}
