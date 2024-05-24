package com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto;

import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年11月01日
 *
 * @author luojianmo
 */
public class CronConvertDTO {

    private TaskCycleEnum taskCycle;

    private String scheduleDate;

    private String scheduleTime;

    private Integer[] dayOfWeekArr;

    private Integer day;

    public TaskCycleEnum getTaskCycle() {
        return taskCycle;
    }

    public void setTaskCycle(TaskCycleEnum taskCycle) {
        this.taskCycle = taskCycle;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Integer[] getDayOfWeekArr() {
        return dayOfWeekArr;
    }

    public void setDayOfWeekArr(Integer[] dayOfWeekArr) {
        this.dayOfWeekArr = dayOfWeekArr;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
