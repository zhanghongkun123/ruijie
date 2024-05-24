package com.ruijie.rcos.rcdc.rco.module.def.enums;

import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/20 9:28
 *
 * @author conghaifeng
 */
public enum OfflineAutoLockedEnum {

    /**
     * 无限制
     */
    UNLIMITED(-1),

    /**
     * 一天
     */
    ONE_DAY(1),


    /**
     * 半个月
     */
    HALF_MONTH(15),

    /**
     * 一个月
     */
    ONE_MONTH(30),

    /**
     * 三个月
     */
    THREE_MONTH(90),

    /**
     * 半年
     */
    HALF_YEAR(180),

    /**
     * 一年
     */
    ONE_YEAR(360);

    private Integer days;

    OfflineAutoLockedEnum(Integer days) {
        this.days = days;
    }

    public Integer getDays() {
        return days;
    }

    /**
     * 根据天数获取枚举类型
     * @param days days
     * @return OfflineAutoLockedEnum
     */
    public static OfflineAutoLockedEnum getByDays(Integer days) {
        Assert.notNull(days, "days can not be null");
        for (OfflineAutoLockedEnum temp : OfflineAutoLockedEnum.values()) {
            if (days.equals(temp.getDays())) {
                return temp;
            }
        }
        //未匹配返回null
        return null;
    }
}
