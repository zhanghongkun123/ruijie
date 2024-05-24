package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年11月01日
 *
 * @author luojianmo
 */
public class MonthScheduleTask extends EqualsHashcodeSupport {

    @NotBlank
    @Size(min = 1, max = 128)
    private String scheduleTypeCode;

    @NotBlank
    private String scheduleTime;

    @Nullable
    @Range(min = "1", max = "31")
    private Integer day;



    public String getScheduleTypeCode() {
        return scheduleTypeCode;
    }

    public void setScheduleTypeCode(String scheduleTypeCode) {
        this.scheduleTypeCode = scheduleTypeCode;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getDay() {
        return day;
    }

}
