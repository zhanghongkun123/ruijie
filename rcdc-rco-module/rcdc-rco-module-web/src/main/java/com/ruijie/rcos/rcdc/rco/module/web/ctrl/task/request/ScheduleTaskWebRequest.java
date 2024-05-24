package com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.request;

import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月12日
 *
 * @author xgx
 */
public class ScheduleTaskWebRequest implements WebRequest {
    @NotBlank
    @Size(min = 1, max = 128)
    private String scheduleTypeCode;

    @NotNull
    private TaskCycleEnum taskCycle;

    @NotBlank
    @TextName
    @TextMedium
    private String taskName;

    @Nullable
    private String scheduleDate;

    @NotBlank
    private String scheduleTime;

    @Nullable
    private Integer[] dayOfWeekArr;

    @Nullable
    private QuartzTaskState quartzTaskState;

    @NotNull
    private ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> data;

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

    public ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> getData() {
        return data;
    }

    public void setData(ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> data) {
        this.data = data;
    }
}
