package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import org.springframework.lang.Nullable;

/**
 * Description: 云桌面允许登录时间配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/1
 *
 * @author zqj
 */
public class DeskTopAllowLoginTimeDTO {


    @Nullable
    private String startTime;


    @Nullable
    private String endTime;

    @Nullable
    private Integer[] weekArr;

    @Nullable
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(@Nullable String startTime) {
        this.startTime = startTime;
    }

    @Nullable
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable String endTime) {
        this.endTime = endTime;
    }

    @Nullable
    public Integer[] getWeekArr() {
        return weekArr;
    }

    public void setWeekArr(@Nullable Integer[] weekArr) {
        this.weekArr = weekArr;
    }
}
