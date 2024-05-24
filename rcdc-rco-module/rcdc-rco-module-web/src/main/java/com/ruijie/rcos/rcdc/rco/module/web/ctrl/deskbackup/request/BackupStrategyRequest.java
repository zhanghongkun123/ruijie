package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request;

import com.ruijie.rcos.rcdc.backup.module.def.enums.BackupType;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 创建备份策略WEB请求体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月23日
 *
 * @author qiuzy
 */
@ApiModel("服务器定时备份策略")
public class BackupStrategyRequest {

    @Nullable
    private UUID id;

    /**
     * 策略名称
     */
    @NotBlank
    @TextShort
    @TextName
    private String name;

    /**
     * 备份数据类型
     */
    @ApiModelProperty(value = "备份数据类型")
    @NotNull
    private BackupType backupType;

    /**
     * 备份数据详细内容，json结构存储
     */
    @ApiModelProperty(value = "备份数据详细内容")
    @Nullable
    private String backupContent;

    /**
     * 启用/禁用
     */
    @ApiModelProperty(value = "启用/禁用")
    @NotNull
    private Boolean strategyState;

    @ApiModelProperty(value = "任务周期")
    @NotNull
    private TaskCycleEnum taskCycle;

    @ApiModelProperty(value = "备份时间")
    @NotBlank
    private String scheduleTime;

    @ApiModelProperty(value = "每周的第几天")
    @Nullable
    private Integer[] dayOfWeekArr;

    @ApiModelProperty(value = "任务状态")
    @Nullable
    private QuartzTaskState quartzTaskState;

    @ApiModelProperty(value = "每月的第几天")
    @Nullable
    @Range(min = "1", max = "31")
    private Integer day;

    /**
     * 持续时间，单位是秒，需要以小时为单位设置
     */
    @ApiModelProperty(value = "任务持续时间")
    @NotNull
    private Integer durationSeconds;

    @ApiModelProperty(value = "外置存储")
    @NotNull
    private IdLabelEntry externalStorageIdLabel;

    /**
     * 备份保留数量
     */
    @ApiModelProperty(value = "备份保留数量")
    @NotNull
    @Range(min = "1",max = "30")
    private Integer maxBackup;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BackupType getBackupType() {
        return backupType;
    }

    public void setBackupType(BackupType backupType) {
        this.backupType = backupType;
    }

    @Nullable
    public String getBackupContent() {
        return backupContent;
    }

    public void setBackupContent(@Nullable String backupContent) {
        this.backupContent = backupContent;
    }

    public Boolean getStrategyState() {
        return strategyState;
    }

    public void setStrategyState(Boolean strategyState) {
        this.strategyState = strategyState;
    }

    public TaskCycleEnum getTaskCycle() {
        return taskCycle;
    }

    public void setTaskCycle(TaskCycleEnum taskCycle) {
        this.taskCycle = taskCycle;
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

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public IdLabelEntry getExternalStorageIdLabel() {
        return externalStorageIdLabel;
    }

    public void setExternalStorageIdLabel(IdLabelEntry externalStorageIdLabel) {
        this.externalStorageIdLabel = externalStorageIdLabel;
    }

    public Integer getMaxBackup() {
        return maxBackup;
    }

    public void setMaxBackup(Integer maxBackup) {
        this.maxBackup = maxBackup;
    }
}
