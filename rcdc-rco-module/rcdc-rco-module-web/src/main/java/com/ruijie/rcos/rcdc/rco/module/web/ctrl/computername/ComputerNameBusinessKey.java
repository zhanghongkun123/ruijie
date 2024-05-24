package com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月10日
 *
 * @author wjp
 */
public interface ComputerNameBusinessKey {

    /** 操作成功 */
    String RCO_COMPUTER_NAME_MODULE_OPERATE_SUCCESS = "rco_computer_name_module_operate_success";
    /** 操作失败 */
    String RCO_COMPUTER_NAME_MODULE_OPERATE_FAIL = "rco_computer_name_module_operate_fail";

    /** 编辑云桌面计算机名称成功 */
    String RCO_EDIT_COMPUTER_NAME_SUCCESS_LOG = "rco_edit_computer_name_success_log";
    /** 编辑云桌面计算机名称失败 */
    String RCO_EDIT_COMPUTER_NAME_FAIL_LOG = "rco_edit_computer_name_fail_log";

    String RCO_COMPUTER_NAME_INVALID_COMPUTER_NAME = "23200810";

    String RCO_COMPUTER_NAME_INVALID_COMPUTER_NAME_PREFIX = "23200811";

    String RCO_COMPUTER_NAME_INVALID_COMPUTER_NAME_NOT_WINDOWS = "rco_computer_name_invalid_computer_name_not_windows";

}
