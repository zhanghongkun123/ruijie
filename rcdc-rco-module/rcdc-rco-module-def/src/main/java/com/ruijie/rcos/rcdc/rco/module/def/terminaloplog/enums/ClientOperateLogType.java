package com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.enums;

/**
 * Description: rca-client操作日志类型
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/3/17 10:24 上午
 *
 * @author zhouhuan
 */
public enum ClientOperateLogType {

    ONE_CLIENT_LOG_LOGIN_SUCCESS("rcdc_rco_one_client_log_login_success"),

    ONE_CLIENT_LOG_LOGOUT_SUCCESS("rcdc_rco_one_client_log_logout_success");

    private String type;

    ClientOperateLogType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
