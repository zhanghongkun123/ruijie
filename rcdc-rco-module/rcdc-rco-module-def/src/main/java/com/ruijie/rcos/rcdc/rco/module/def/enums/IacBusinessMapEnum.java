package com.ruijie.rcos.rcdc.rco.module.def.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月28日
 *
 * @author zdc
 */
public enum IacBusinessMapEnum {

    /**
     * 账号密码错误
     */
    RCDC_RCO_ADMIN_OR_PWD_ERROR_EXCEPTION(BusinessKey.RCDC_IAC_LOGIN_ADMIN_NOT_EXIST_OR_PWD_ERROR,
            BusinessKey.RCDC_RCO_IAC_LOGIN_ADMIN_NOT_EXIST_OR_PWD_ERROR),

    /**
     * 验证管理员密码，提示剩余次数
     */
    RCDC_RCO_VALID_ADMIN_REMAIN_ERROR_TIMES(BusinessKey.RCDC_IAC_ADMIN_REMAIN_ERROR_TIMES, BusinessKey.RCDC_RCO_IAC_ADMIN_REMAIN_ERROR_TIMES),

    /**
     * 验证管理员密码，提示非永久锁定
     */
    RCDC_RCO_VALID_ADMIN_LIMITED_LOGIN(BusinessKey.RCDC_IAC_ADMIN_LIMITED_LOGIN, BusinessKey.RCDC_RCO_IAC_ADMIN_LIMITED_LOGIN),

    /**
     * 验证管理员密码，提示永久锁定
     */
    RCDC_RCO_VALID_ADMIN_LIMITED_LOGIN_FOREVER(BusinessKey.RCDC_IAC_ADMIN_LIMITED_LOGIN_FOREVER,
            BusinessKey.RCDC_RCO_IAC_ADMIN_LIMITED_LOGIN_FOREVER);


    private String iacKey;

    private String rcdcKey;

    IacBusinessMapEnum(String iacKey, String rcdcKey) {
        this.iacKey = iacKey;
        this.rcdcKey = rcdcKey;
    }

    public String getIacKey() {
        return iacKey;
    }

    public String getRcdcKey() {
        return rcdcKey;
    }

    /**
     * 根据业务异常提取提示语中的数字
     * @param adminName 管理员名称
     * @param e 业务异常
     * @return 提示语中的数字列表
     */
    public static String mapIacErrorMsgToRcdcMsg(String adminName, BusinessException e) {
        Assert.hasText(adminName, "adminName must not be null");
        Assert.notNull(e, "businessException must not be null");
        String errorMsg = e.getAttachment(UserTipContainer.Constants.USER_TIP, e.getI18nMessage());
        List<String> stringList = new ArrayList<>();
        for (IacBusinessMapEnum iacBusinessMapEnum : IacBusinessMapEnum.values()) {
            if (e.getKey().equals(iacBusinessMapEnum.getIacKey())) {
                stringList.add(adminName);
                String regex = "\\d+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(errorMsg);
                while (matcher.find()) {
                    stringList.add(matcher.group());
                }
                return LocaleI18nResolver.resolve(iacBusinessMapEnum.getRcdcKey(), stringList.stream().toArray(String[]::new));
            }
        }
        return errorMsg;
    }
}
