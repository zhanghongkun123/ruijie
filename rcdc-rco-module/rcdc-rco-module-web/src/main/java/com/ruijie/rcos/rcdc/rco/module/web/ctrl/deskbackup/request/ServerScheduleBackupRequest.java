package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月22日
 *
 * @author zhanghongkun
 */
@ApiModel("服务器定时备份")
public class ServerScheduleBackupRequest implements Serializable {

    @ApiModelProperty(value = "任务周期")
    @NotNull
    private TaskCycleEnum taskCycle;

    @ApiModelProperty(value = "备份日期")
    @Nullable
    private String scheduleDate;

    @ApiModelProperty(value = "备份时间")
    @NotBlank
    private String scheduleTime;

    @Nullable
    private Integer[] dayOfWeekArr;

    @ApiModelProperty(value = "任务状态")
    @Nullable
    private QuartzTaskState quartzTaskState;

    @Nullable
    @Range(min = "1", max = "31")
    private Integer day;

    @Nullable
    private UUID id;

    @NotNull
    private ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> data;


    public TaskCycleEnum getTaskCycle() {
        return taskCycle;
    }

    public void setTaskCycle(TaskCycleEnum taskCycle) {
        this.taskCycle = taskCycle;
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

    @Nullable
    public Integer getDay() {
        return day;
    }

    public void setDay(@Nullable Integer day) {
        this.day = day;
    }

    public ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> getData() {
        return data;
    }

    public void setData(ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> data) {
        this.data = data;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }
}
