package com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule;

import java.util.*;


import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月14日
 *
 * @param <T> 数据类型
 * @param <R> 类型
 * @author xgx
 */
public class RcoScheduleTaskDTO<T, R> extends EqualsHashcodeSupport {
    private UUID id;

    @NotBlank
    @Size(min = 1, max = 128)
    private String scheduleTypeCode;

    @NotNull
    private TaskCycleEnum taskCycle;

    @TextMedium
    private String taskName;

    private String scheduleTypeName;

    @Nullable
    private String scheduleDate;

    @NotBlank
    private String scheduleTime;

    @Nullable
    private Integer[] dayOfWeekArr;

    @Nullable
    private QuartzTaskState quartzTaskState;

    @NotNull
    private ScheduleDataDTO<T, R> data;

    private Integer day;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getScheduleTypeCode() {
        return scheduleTypeCode;
    }

    public void setScheduleTypeCode(String scheduleTypeCode) {
        this.scheduleTypeCode = scheduleTypeCode;
    }

    public TaskCycleEnum getTaskCycle() {
        return taskCycle;
    }

    public void setTaskCycle(TaskCycleEnum taskCycle) {
        this.taskCycle = taskCycle;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Nullable
    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(@Nullable String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    @Nullable
    public Integer[] getDayOfWeekArr() {
        return dayOfWeekArr;
    }

    public void setDayOfWeekArr(@Nullable Integer[] dayOfWeekArr) {
        this.dayOfWeekArr = dayOfWeekArr;
    }

    @Nullable
    public QuartzTaskState getQuartzTaskState() {
        return quartzTaskState;
    }

    public void setQuartzTaskState(@Nullable QuartzTaskState quartzTaskState) {
        this.quartzTaskState = quartzTaskState;
    }

    public ScheduleDataDTO<T, R> getData() {
        return data;
    }

    public void setData(ScheduleDataDTO<T, R> data) {
        this.data = data;
    }

    public String getScheduleTypeName() {
        return scheduleTypeName;
    }

    public void setScheduleTypeName(String scheduleTypeName) {
        this.scheduleTypeName = scheduleTypeName;
    }

}
