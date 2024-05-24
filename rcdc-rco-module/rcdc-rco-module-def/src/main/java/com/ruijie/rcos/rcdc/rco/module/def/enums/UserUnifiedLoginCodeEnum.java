package com.ruijie.rcos.rcdc.rco.module.def.enums;

import com.ruijie.rcos.rcdc.rco.module.def.LoginLogBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserUnifiedLoginResultCode;
import org.springframework.util.Assert;


/**
 * Description: 登录异常信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/14
 *
 * @author zqj
 */
public enum UserUnifiedLoginCodeEnum {

    /**
     * 登录成功
     */
    LOGIN_SUCCESS(CommonMessageCode.SUCCESS, LoginLogBusinessKey.LOGIN_SUCCESS),

    NAME_OR_PASSWORD_ERROR(UserUnifiedLoginResultCode.NAME_OR_PASSWORD_ERROR, LoginLogBusinessKey.NAME_OR_PASSWORD_ERROR),

    VISITOR_LOGIN_AS_NORMAL_ERROR(UserUnifiedLoginResultCode.VISITOR_LOGIN, LoginLogBusinessKey.VISITOR_LOGIN_AS_NORMAL_ERROR),

    NOT_LICENSE(UserUnifiedLoginResultCode.NOT_LICENSE, LoginLogBusinessKey.NOT_LICENSE),

    AD_SERVER_ERROR(UserUnifiedLoginResultCode.AD_SERVER_ERROR, LoginLogBusinessKey.AD_SERVER_ERROR),

    AD_ACCOUNT_DISABLE(UserUnifiedLoginResultCode.AD_ACCOUNT_DISABLE, LoginLogBusinessKey.AD_ACCOUNT_DISABLE),

    AD_LOGIN_LIMIT(UserUnifiedLoginResultCode.AD_LOGIN_LIMIT, LoginLogBusinessKey.AD_LOGIN_LIMIT),

    AD_ACCOUNT_EXPIRE(UserUnifiedLoginResultCode.AD_ACCOUNT_EXPIRE, LoginLogBusinessKey.AD_ACCOUNT_EXPIRE),

    NOT_ALLOW_LOGIN_FOR_NOT_VISITOR(UserUnifiedLoginResultCode.NOT_ALLOW_LOGIN_FOR_NOT_VISITOR, LoginLogBusinessKey.NOT_ALLOW_LOGIN_FOR_NOT_VISITOR),

    USER_LOCKED(UserUnifiedLoginResultCode.USER_LOCKED, LoginLogBusinessKey.USER_LOCKED),

    CAS_AUTH_UNOPENED(UserUnifiedLoginResultCode.CAS_AUTH_UNOPENED, LoginLogBusinessKey.CAS_AUTH_UNOPENED),

    USERNAME_OR_PASSWORD_UNOPENED(UserUnifiedLoginResultCode.USERNAME_OR_PASSWORD_UNOPENED, LoginLogBusinessKey.USERNAME_OR_PASSWORD_UNOPENED),

    USER_NOT_EXIST(UserUnifiedLoginResultCode.USER_NOT_EXIST, LoginLogBusinessKey.USER_NOT_EXIST),

    LOGIN_FAIL(CommonMessageCode.CODE_ERR_OTHER, LoginLogBusinessKey.LOGIN_FAIL),

    QR_USER_NOT_EXIST(UserUnifiedLoginResultCode.QR_USER_NOT_EXIST, LoginLogBusinessKey.QR_USER_NOT_EXIST),

    CAS_CONFIG_NON_EXISTENT(UserUnifiedLoginResultCode.CAS_CONFIG_NON_EXISTENT, LoginLogBusinessKey.CAS_CONFIG_NON_EXISTENT),

    ACCOUNT_INVALID(UserUnifiedLoginResultCode.ACCOUNT_INVALID, LoginLogBusinessKey.ACCOUNT_INVALID),

    QR_CODE_NOT_SCAN_CODE(UserUnifiedLoginResultCode.QR_CODE_NOT_SCAN_CODE, LoginLogBusinessKey.QR_CODE_NOT_SCAN_CODE),

    QR_CODE_EXPIRE(UserUnifiedLoginResultCode.QR_CODE_EXPIRE, LoginLogBusinessKey.QR_CODE_EXPIRE),

    UNENABLE_GLOBAL_CAS(UserUnifiedLoginResultCode.UNENABLE_GLOBAL_CAS, LoginLogBusinessKey.UNENABLE_GLOBAL_CAS),

    REMIND_ERROR_TIMES(UserUnifiedLoginResultCode.REMIND_ERROR_TIMES, LoginLogBusinessKey.REMIND_ERROR_TIMES),

    API_FAILURE(UserUnifiedLoginResultCode.API_FAILURE, LoginLogBusinessKey.API_FAILURE),

    PARAM_ERROR(UserUnifiedLoginResultCode.PARAM_ERROR, LoginLogBusinessKey.PARAM_ERROR),

    TICKET_NOT_EXISTS(UserUnifiedLoginResultCode.TICKET_NOT_EXISTS, LoginLogBusinessKey.TICKET_NOT_EXISTS),

    ACCOUNT_LOCKED(UserUnifiedLoginResultCode.ACCOUNT_LOCKED, LoginLogBusinessKey.ACCOUNT_LOCKED),

    DEVICE_CHANGED(UserUnifiedLoginResultCode.DEVICE_CHANGED, LoginLogBusinessKey.DEVICE_CHANGED),

    PASSWORD_EXPIRED(UserUnifiedLoginResultCode.PASSWORD_EXPIRED, LoginLogBusinessKey.PASSWORD_EXPIRED),

    SERVER_EXCEPTION(UserUnifiedLoginResultCode.SERVER_EXCEPTION, LoginLogBusinessKey.SERVER_EXCEPTION),

    TICKET_VERIFY_FAIL(UserUnifiedLoginResultCode.TICKET_VERIFY_FAIL, LoginLogBusinessKey.TICKET_VERIFY_FAIL),

    QR_UNKNOWN(UserUnifiedLoginResultCode.QR_UNKNOWN, LoginLogBusinessKey.QR_UNKNOWN),

    DEFAULT_ERROR(CommonMessageCode.DEFAULT_ERROR, LoginLogBusinessKey.DEFAULT_ERROR),

    NO_CLUSTER_CACHE(UserUnifiedLoginResultCode.NO_CLUSTER_CACHE, LoginLogBusinessKey.NO_CLUSTER_CACHE),

    RCDC_CLUSTER_VALIDATE_FAIL(UserUnifiedLoginResultCode.RCDC_CLUSTER_VALIDATE_FAIL, LoginLogBusinessKey.RCDC_CLUSTER_VALIDATE_FAIL),

    REQUEST_RCDC_HTTP_ERROR(UserUnifiedLoginResultCode.REQUEST_RCDC_HTTP_ERROR, LoginLogBusinessKey.REQUEST_RCDC_HTTP_ERROR);

    private int code;

    private String message;

    UserUnifiedLoginCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 获取UserUnifiedLoginCodeEnum
     *
     * @param code code
     * @return 获取UserUnifiedLoginCodeEnum
     */
    public static UserUnifiedLoginCodeEnum getByCode(Integer code) {
        Assert.notNull(code, "code不能为null");
        for (UserUnifiedLoginCodeEnum codeEnum : UserUnifiedLoginCodeEnum.values()) {
            if (codeEnum.getCode() == code) {
                return codeEnum;
            }
        }
        return UserUnifiedLoginCodeEnum.DEFAULT_ERROR;
    }
}
