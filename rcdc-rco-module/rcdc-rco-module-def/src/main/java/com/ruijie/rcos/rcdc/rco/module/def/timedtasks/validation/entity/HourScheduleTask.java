package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.entity;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月25日
 *
 * @author zjy
 */
public class HourScheduleTask extends EqualsHashcodeSupport {
    @NotBlank
    @Size(min = 1, max = 128)
    private String scheduleTypeCode;

    @NotBlank
    private String scheduleTime;


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
}
