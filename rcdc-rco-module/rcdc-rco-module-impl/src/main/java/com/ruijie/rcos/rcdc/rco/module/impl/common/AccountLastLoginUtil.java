package com.ruijie.rcos.rcdc.rco.module.impl.common;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.api.UserDesktopMgmtAPIImpl;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/26 20:42
 *
 * @author liuwang1
 */

public class AccountLastLoginUtil {

    private static final Long TIME_CONVERSION_UNIT = 24 * 60 * 60 * 1000L;

    private static final Long EXPIRE_TIME_ZERO = 0L;

    /**
     * 获取最近登录时间
     *
     * @param userId  用户id
     * @param dtoList 用户云桌面列表
     * @return 最近登录时间
     */
    public static Long offerLastLoginTime(UUID userId, List<CloudDesktopDTO> dtoList) {
        Assert.notNull(userId, "userId is not null");
        Assert.notNull(dtoList, "dtoList is not null");

        if (CollectionUtils.isEmpty(dtoList)) {
            return new Date().getTime();
        }

        Optional<Long> lastLoginTime = dtoList.stream().map(entity -> {
            if (Objects.isNull(entity.getLatestLoginTime())) {
                return entity.getCreateTime().getTime();
            } else {
                return entity.getLatestLoginTime().getTime();
            }
        }).max(Long::compare);
        return lastLoginTime.get();
    }

    /**
     * 设置失效描述
     *
     * @param lastLoginTime 最近登录时间
     * @param invalidTime   失效时间
     * @return 失效描述
     */
    public static String setInvalidDescription(Long lastLoginTime, Integer invalidTime) {
        Assert.notNull(lastLoginTime, "lastLoginTime is not null");
        Assert.notNull(invalidTime, "invalidTime is not null");

        long timeGapValue = new Date().getTime() - lastLoginTime - invalidTime * TIME_CONVERSION_UNIT;
        Long invalidDay;
        if (timeGapValue < EXPIRE_TIME_ZERO) {
            invalidDay = Math.abs(timeGapValue) / TIME_CONVERSION_UNIT;
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_REMAIN_INVALID_DAY, invalidDay.toString());
        } else {
            invalidDay = timeGapValue / TIME_CONVERSION_UNIT;
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ALREADY_INVALID, invalidDay.toString());
        }
    }

}
