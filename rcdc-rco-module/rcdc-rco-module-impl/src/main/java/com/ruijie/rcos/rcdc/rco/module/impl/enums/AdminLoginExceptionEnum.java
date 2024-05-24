package com.ruijie.rcos.rcdc.rco.module.impl.enums;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_IAC_ADMIN_IS_DISABLED;
import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_IAC_ADMIN_LIMITED_LOGIN;
import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_IAC_ADMIN_REMAIN_ERROR_TIMES;
import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_IAC_LOGIN_ADMIN_NOT_EXIST_OR_PWD_ERROR;

/**
 * Description: 终端管理员登录异常code枚举类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/26 7:00 下午
 *
 * @author zhouhuan
 */
public enum AdminLoginExceptionEnum {

    /**
     * 管理员账号或者密码有误
     */
    ADMIN_OR_PWD_ERROR_EXCEPTION(RCDC_IAC_LOGIN_ADMIN_NOT_EXIST_OR_PWD_ERROR, -3),

    /**
     *管理员被禁用
     */
    ADMIN_IS_DISABLED_EXCEPTION(RCDC_IAC_ADMIN_IS_DISABLED, -4),

    /**
     *管理员被禁用
     */
    ADMIN_IS_DISABLE(RCDC_IAC_ADMIN_IS_DISABLED, -14),

    /**
     * 管理员不是系统管理员
     */
    RCDC_RCO_NOT_SYS_ADMIN(BusinessKey.RCDC_RCO_NOT_SYS_ADMIN, -5),

    /**
     * 管理员没有终端所在终端组的权限
     */
    RCDC_RCO_ADMIN_NOT_HAS_TERMINAL_GROUP_PERMISSION(BusinessKey.RCDC_RCO_ADMIN_NOT_HAS_TERMINAL_GROUP_PERMISSION, -6),

    /**
     * 管理员没有镜像模板菜单权限
     */
    RCDC_RCO_ADMIN_NOT_HAS_IMAGE_TEMPLATE_MENU_PERMISSION(BusinessKey.RCDC_RCO_ADMIN_NOT_HAS_IMAGE_TEMPLATE_MENU_PERMISSION, -7),

    /**
     * 密码错误，提醒剩余次数
     */
    RCDC_RCO_ADMIN_REMAIN_ERROR_TIMES(RCDC_IAC_ADMIN_REMAIN_ERROR_TIMES, -8),

    /**
     * 管理员已被锁定
     */
    RCDC_RCO_ADMIN_LIMITED_LOGIN(RCDC_IAC_ADMIN_LIMITED_LOGIN, -9),

    /**
     * AD域用户当前时间不允许登录
     */
    RCDC_RCO_AAA_ADMIN_NOT_ALLOW_LOGIN_THIS_TIME(BusinessKey.RCDC_RCO_AAA_ADMIN_NOT_ALLOW_LOGIN_THIS_TIME, -10),

    /**
     * AD域用户已经过期
     */
    RCDC_RCO_AAA_ADMIN_EXPIRED(BusinessKey.RCDC_RCO_AAA_ADMIN_EXPIRED, -11);

    private String key;

    private Integer code;

    private static final Integer FAILURE = -1;

    private static final Integer IAC_DEFAULT_FAIL_CODE = -120;

    AdminLoginExceptionEnum(String key, Integer code) {
        this.key = key;
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 获取异常key值对应的响应给shine的code
     * @param key 异常key值
     * @return 异常key值对应的响应给shine的code值
     */
    public static Integer getCorrespondingCode(String key) {
        Assert.hasText(key, "key can not be blank");
        for (AdminLoginExceptionEnum adminLoginExceptionEnum : AdminLoginExceptionEnum.values()) {
            if (key.equals(adminLoginExceptionEnum.getKey())) {
                return adminLoginExceptionEnum.getCode();
            }
        }
        // IAC业务异常key为纯数字。防止未记录的IAC业务异常返回错误码-1，导致shine直接抛出异常，不展示IAC提示语。
        if (StringUtils.isNumeric(key)) {
            return IAC_DEFAULT_FAIL_CODE;
        }
        // 预期之外的异常，返回-1
        return FAILURE;
    }

    /**
     * 根据业务异常提取提示语中的数字
     * @param e 业务异常
     * @return 提示语中的数字列表
     */
    public static List<Integer> getErrorMsgIntegerInfo(BusinessException e) {
        Assert.notNull(e, "businessException must not be null");
        String errorMsg = e.getAttachment(UserTipContainer.Constants.USER_TIP, e.getI18nMessage());
        List<Integer> integerList = new ArrayList<>();
        for (AdminLoginExceptionEnum adminLoginExceptionEnum : AdminLoginExceptionEnum.values()) {
            if (e.getKey().equals(adminLoginExceptionEnum.getKey())) {
                String regex = "\\d+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(errorMsg);
                while (matcher.find()) {
                    integerList.add(Integer.parseInt(matcher.group()));
                }
            }
        }
        return integerList;
    }
}
