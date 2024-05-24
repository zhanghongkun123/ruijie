package com.ruijie.rcos.rcdc.rco.module.def.sms.enums;

import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Description: 短信认证返回的状态码
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/25
 *
 * @author TD
 */
public enum SmsAuthCodeEnum {

    /**
     * 成功
     */
    SUCCESS(0, ""),

    /**
     * 用户未绑定手机号
     */
    USER_NOT_BIND_PHONE(1, "66100005"),
    
    /**
     * 短信认证不支持访客
     */
    SMS_NOT_SUPPORT_VISITOR(2, "66100010"),

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(3, "66061001"),

    /**
     * 用户未开启短信认证
     */
    USER_NOT_SMS_AUTH(4, "66100006"),
    
    /**
     * 非本地个性用户不支持短信密码找回
     */
    NON_LOCAL_USER_NOT_SUPPORTED_MODIFY_PWD(5, "66100008"),

    /**
     * 发送短信手机号不能为空
     */
    SEND_SMS_PHONE_NOT_NULL(6, "66100007"),

    /**
     * 短信密码找回功能未开启
     */
    SMS_PWD_RECOVER_NOT_OPEN(7, "66100004"),

    /**
     * 全局未开启短信认证
     */
    SMS_AUTH_GLOBAL_NOT_OPEN(8, "66100003"),

    /**
     * 短信验证码未到刷新时间
     */
    SMS_VERIFY_CODE_NOT_REFRESH(9, "66100002"),
    
    /**
     * 手机短信验证码超出当天可发送最大条数限制
     */
    PHONE_SMS_VERIFY_CODE_MAX(10, "66100009"),
    
    /**
     * 短信服务器未开启
     */
    SMS_SERVER_NOT_OPEN(11, "23200366"),    
    
    /**
     * 短信返回内容解析失败
     */
    SMS_RESULT_RESOLVING_FAIL(12, "23201900"),
    
    /**
     * 发送短信验证码的手机与用户要绑定手机号不一致
     */
    BIND_PHONE_NO_EQUAL(13, "66100011"),
    
    /**
     * 短信验证码不一致
     */
    SMS_VERIFY_NO_EQUAL(14, "23200369"),
    
    /**
     * 访客不支持绑定手机号
     */
    BIND_PHONE_NOT_SUPPORT_VISITOR(15, "66100012"),
    
    /**
     * 用户手机号不为空，无需进行手机号绑定操作
     */
    USER_PHONE_NOT_NULL(16, "66100013"),
    
    /**
     * 短信验证码已失效
     */
    SMS_VERIFY_CODE_EXPIRED(17, "66100001"),

    /**
     * 短信发送失败
     */
    SMS_SEND_FAIL(18, "66100015"),

    /**
     * 短信发送内容编码失败
     */
    SMS_SEND_CONTENT_ENCODING_ERROR(19, "23200383"),

    /**
     * 短信服务器连接失败
     */
    SMS_SERVER_CONNECT_FAIL(20, "23200382"),
    
    /**
     * 系统异常
     */
    SYSTEM_ERROR(99, "66100016");

    private final Integer code;

    private final String key;

    SmsAuthCodeEnum(Integer code, String key) {
        this.code = code;
        this.key = key;
    }

    public Integer getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    /**
     * 根据错误key获取枚举
     * @param key 参数
     * @return 状态码
     */
    public static SmsAuthCodeEnum getSmsAuthCodeEnum(String key) {
        Assert.hasText(key, "key must not be null");

        return Arrays.stream(SmsAuthCodeEnum.values())
                .filter(item -> item.getKey().equals(key))
                .findFirst().orElse(SYSTEM_ERROR);
    }
}
