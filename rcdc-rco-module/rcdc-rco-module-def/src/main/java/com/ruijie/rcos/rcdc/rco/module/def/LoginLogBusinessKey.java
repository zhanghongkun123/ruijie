package com.ruijie.rcos.rcdc.rco.module.def;

/**
 * Description: LoginLogBusinessKey
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-09-29
 *
 * @author linke
 */
public interface LoginLogBusinessKey {

    String LOGIN_SUCCESS = "login_success";

    String LOGIN_FAIL = "login_fail";

    String NAME_OR_PASSWORD_ERROR = "name_or_password_error";

    String VISITOR_LOGIN_AS_NORMAL_ERROR = "visitor_login_as_normal_error";

    String NOT_LICENSE = "not_license";

    String AD_SERVER_ERROR = "ad_server_error";

    String AD_ACCOUNT_DISABLE = "ad_account_disable";

    String AD_LOGIN_LIMIT = "ad_login_limit";

    String AD_ACCOUNT_EXPIRE = "ad_account_expire";

    String NOT_ALLOW_LOGIN_FOR_NOT_VISITOR = "not_allow_login_for_not_visitor";

    String USER_LOCKED = "user_locked";

    String CAS_AUTH_UNOPENED = "cas_auth_unopened";

    String USERNAME_OR_PASSWORD_UNOPENED = "username_or_password_unopened";

    String USER_NOT_EXIST = "user_not_exist";

    String DEFAULT_ERROR = "default_error";

    String RCDC_RETURN_ERROR = "rcdc_return_error";

    String NO_CLUSTER_CACHE = "no_cluster_cache";

    String RCDC_CLUSTER_VALIDATE_FAIL = "rcdc_cluster_validate_fail";

    String REQUEST_RCDC_HTTP_ERROR = "request_rcdc_http_error";

    String QR_USER_NOT_EXIST = "qr_user_not_exist";

    String CAS_CONFIG_NON_EXISTENT = "cas_config_non_existent";

    String ACCOUNT_INVALID = "account_invalid";

    String QR_CODE_NOT_SCAN_CODE = "qr_code_not_scan_code";

    String QR_CODE_EXPIRE = "qr_code_expire";

    String API_FAILURE = "api_failure";

    String PARAM_ERROR = "param_error";

    String TICKET_NOT_EXISTS = "ticket_not_exists";

    String ACCOUNT_LOCKED = "account_locked";

    String DEVICE_CHANGED = "device_changed";

    String PASSWORD_EXPIRED = "password_expired";

    String SERVER_EXCEPTION = "server_exception";

    String QR_UNKNOWN = "qr_unknown";

    String TICKET_VERIFY_FAIL = "ticket_verify_fail";

    String REMIND_ERROR_TIMES = "remind_error_times";

    String UNENABLE_GLOBAL_CAS = "unenable_global_cas";

    String USER_LOCKED_PERMANENT = "user_locked_permanent";
}
