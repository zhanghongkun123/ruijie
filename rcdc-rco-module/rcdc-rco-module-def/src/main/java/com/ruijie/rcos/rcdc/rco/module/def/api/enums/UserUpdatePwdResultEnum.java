package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Description: 用户更新密码结果枚举
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-18 11:16:00
 *
 * @author zjy
 */
public enum UserUpdatePwdResultEnum {

    SUCCESS(0),

    /**
     * 旧密码不正确
     **/
    OLD_PASSWORD_ERROR(1),

    /**
     * AD域用户不允许修改密码
     **/
    AD_USER_NOT_ALLOW_CHANGE_PASSWORD(2),

    /**
     * LDAP用户不允许修改密码
     **/
    LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD(3),

    /**
     * 查无此人
     **/
    NOT_THIS_USER(4),

    /**
     * 修改密码不符合密码策略要求
     **/
    CHANGE_PASSWORD_UNABLE_REQUIRE(6),

    /**
     * 访客不允许修改密码
     **/
    VISITOR_USER_NOT_ALLOW_CHANGE_PASSWORD(7),

    /**
     * 系统处于维护模式状态
     **/
    IN_MAINTENANCE(-7),

    /**
     * 用户处于锁定状态
     **/
    IN_LOCKED(-10),

    /**
     * 提示剩余密码可错误次数
     **/
    REMIND_ERROR_TIMES(-11),

    /**
     * token失效或不一致
     */
    TOKEN_ERROR(-12),
    
    /**
     * 短信修改密码用户不存在
     */
    SMS_NOT_THIS_USER(-13),

    /**
     * 新密码不能与旧密码相同
     */
    NEW_OLD_PWD_NOT_EQUAL(-14),

    /**
     * 第三方用户不允许修改密码
     */
    THIRD_PARTY_USER_NOT_ALLOW_CHANGE_PASSWORD(-16),

    /**
     * 弱密码不允许修改密码
     */
    WEAK_PWD_NOT_ALLOW_CHANGE_PASSWORD(-17);

    private Integer code;

    UserUpdatePwdResultEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 根据修改密码信息返回对应枚举
     * @param code code
     * @Date 2022/1/19 14:41
     * @Author zjy
     * @return 返回值
     **/
    public static UserUpdatePwdResultEnum getUserUpdatePwdResultByCode(Integer code) {
        Assert.notNull(code, "code must not be null");

        return Arrays.stream(UserUpdatePwdResultEnum.values())
                .filter(item -> item.getCode().equals(code))
                .findFirst().orElse(null);
    }


}
