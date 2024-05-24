package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Description: 用户登录结果枚举
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-17 19:24:00
 *
 * @author zjy
 */
public enum UserLoginResultEnum {

    SUCCESS(0),

    USERNAME_OR_PASSWORD_ERROR(1),

    /**
     * 当前为访客账号登录,不允许作为普通用户登录
     **/
    VISITOR_LOGIN(2),

    /**
     * RCDC未授权 不允许登录
     **/
    NOT_LICENSE(3),

    /**
     * AD域服务器异常
     **/
    AD_SERVER_ERROR(4),

    /**
     * 用户被禁用
     **/
    AD_ACCOUNT_DISABLE(5),

    /**
     * 当前时间不允许登录
     **/
    AD_LOGIN_LIMIT(6),

    /**
     * AD账户过期
     **/
    AD_ACCOUNT_EXPIRE(7),

    /**
     * 非访客用户不允许用于访客登录
     **/
    NOT_ALLOW_LOGIN_FOR_NOT_VISITOR(8),

    /**
     * LDAP 服务器异常
     **/
    LDAP_SERVER_ERROR(11),

    /**
     * 用户被锁定
     **/
    USER_LOCKED(12),

    /**
     * 提示剩余密码可错误次数
     **/
    REMIND_ERROR_TIMES(13),

    /**
     * AD/LDAP域连接失败且用户本地认证未开启
     */
    AD_ACCOUNT_LOCAL_AUTH_DISABLE(17),

    /**
     * 用户本地认证密码过期
     */
    AD_ACCOUNT_LOCAL_AUTH_PASSWORD_EXPIRE(18),

    /**
     * 用户末开启账号密码登录
     **/
    USERNAME_OR_PASSWORD_UNOPENED(31),

    /**
     * 用户绑定动态口令失败
     **/
    USER_BIND_OTP_ERROR(92),

    /**
     * 密码解密异常
     **/
    DESCRYPT_PWD_ERROR(93),

    /**
     * 用户未绑定过动态口令
     */
    USER_NO_BIND_OTP(94),

    /**
     * 动态口令不一致
     */
    OTP_INCONSISTENT(95),

    /**
     * 用户未开启动态口令
     */
    UNENABLE_USER_OTP(96),

    /**
     * 未开启全局动态口令
     */
    UNENABLE_GLOBAL_OTP(97),

    /**
     * 用户名或动态口令错误
     **/
    USERNAME_OR_OTP_ERROR(98),

    /**
     * 统一登录失败
     **/
    UNITE_LOGIN_FAIL(99),

    /**
     * 账号失效
     **/
    ACCOUNT_INVALID(33),

    /**
     * 未开启全局第三方认证
     **/
    UNABLE_THIRD_PARTY_AUTH(-998),

    /**
     * 未开启个人第三方认证
     **/
    UNABLE_USER_THIRD_PARTY_AUTH(-999),

    /**
     * 图形验证码错误
     **/
    CAPTCHA_ERROE(66080083),

    /**
     * 图形验证码过期
     **/
    INVALID_CAPTCHA(66080082),

    /**
     * 请输入图形验证码
     */
    NOT_CAPTCHA(66080084),

    /**
     * 用户不存在且开启图形验证码时被锁定
     */
    USER_NOT_EXIST_WITH_CAPTCHA_LOCKED(66080127),

    /**
     * 用户不存在且未开启图形验证码时被锁定
     */
    USER_NOT_EXIST_LOCKED(66061022),

    /**
     * 第三方认证失败
     **/
    USER_THIRD_PARTY_AUTH_ERROR(-1000);


    private Integer code;

    UserLoginResultEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 根据登录信息返回对应枚举
     *
     * @param code code
     * @return 返回值
     * @Date 2022/1/19 14:41
     * @Author zjy
     **/
    public static UserLoginResultEnum getUserLoginResultByCode(Integer code) {
        Assert.notNull(code, "code must not be null");

        return Arrays.asList(UserLoginResultEnum.values()).stream()
                .filter(item -> item.getCode().equals(code))
                .findFirst().orElse(null);
    }
}
