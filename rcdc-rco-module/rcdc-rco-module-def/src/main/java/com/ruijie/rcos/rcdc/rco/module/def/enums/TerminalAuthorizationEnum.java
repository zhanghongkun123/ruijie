package com.ruijie.rcos.rcdc.rco.module.def.enums;

import org.springframework.util.Assert;

/**
 * Description: 终端授权状态码枚举
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/15
 *
 * @author gaoxueyuan
 */
public enum TerminalAuthorizationEnum {

    /**
     * 终端已授权
     */
    TERMINAL_AUTHORIZED(0),

    /**
     * 终端未授权
     */
    TERMINAL_NOT_AUTHORIZED(1),

    /**
     * 终端无需授权
     */
    TERMINAL_NOT_NEED_AUTHORIZED(2);

    /**
     * 返回状态码
     */
    private int code;

    TerminalAuthorizationEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * 返回IDV终端授权码
     *
     * @param authed authed
     * @return statusCode
     */
    public static int statusCode(Boolean authed) {

        Assert.notNull(authed, "authed can not be null");

        if (Boolean.TRUE.equals(authed)) {
            return TerminalAuthorizationEnum.TERMINAL_AUTHORIZED.getCode();
        } else {
            return TerminalAuthorizationEnum.TERMINAL_NOT_AUTHORIZED.getCode();
        }
    }
}
