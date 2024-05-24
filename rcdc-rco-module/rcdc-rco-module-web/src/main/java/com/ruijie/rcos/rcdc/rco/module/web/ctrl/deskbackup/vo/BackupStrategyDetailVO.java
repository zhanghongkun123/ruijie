package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo;

import com.ruijie.rcos.rcdc.backup.module.def.enums.BackupType;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月24日
 *
 * @author qiuzy
 */
public class BackupStrategyDetailVO {

    @ApiModelProperty(value = "ID")
    private UUID id;

    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 备份数据类型
     */
    @ApiModelProperty(value = "备份数据类型")
    private BackupType backupType;

    /**
     * 启用/禁用
     */
    @ApiModelProperty(value = "启用/禁用")
    private Boolean strategyState;

    /**
     * 任务周期
     */
    @ApiModelProperty(value = "任务周期")
    private TaskCycleEnum taskCycle;

    /**
     * 备份时间
     */
    @ApiModelProperty(value = "备份时间")
    private String scheduleTime;

    /**
     * 每周的哪几天
     */
    @ApiModelProperty(value = "每周的哪几天")
    private Integer[] dayOfWeekArr;

    /**
     * 每月第几天
     */
    @ApiModelProperty(value = "每月第几天")
    private Integer day;

    /**
     * 持续时间，单位是秒，需要以小时为单位设置
     */
    @ApiModelProperty(value = "持续时间")
    private Integer durationSeconds;

    /**
     * 外部存储
     */
    @ApiModelProperty(value = "外部存储")
    private IdLabelEntry externalStorageIdLabel;

    /**
     * 备份保留数量
     */
    @ApiModelProperty(value = "备份保留数量")
    private Integer maxBackup;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
