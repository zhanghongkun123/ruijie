package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo;

import com.ruijie.rcos.rcdc.backup.module.def.enums.BackupStateEnum;
import com.ruijie.rcos.rcdc.backup.module.def.enums.BackupType;
import com.ruijie.rcos.rcdc.backup.module.def.enums.TaskTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月27日
 *
 * @author zhanghongkun
 */
@ApiModel("备份日志")
public class BackupLogVO implements Serializable {

    @ApiModelProperty(value = "id")
    private UUID id;

    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    /**
     * 任务类型
     */
    @ApiModelProperty(value = "任务类型")
    private TaskTypeEnum taskType;

    /**
     * 备份状态
     **/
    @ApiModelProperty(value = "备份状态")
    private BackupStateEnum backupState;

    /**
     * 任务进度
     */
    private String percentage;

    /**
     * 外置存储名称
     */
    @ApiModelProperty(value = "外置存储名称")
    private String externalStorageName;

    /**
     * 备份开始时间
     **/
    @ApiModelProperty(value = "备份开始时间")
    private Date backupBeginTime;

    /**
     * 备份结束时间
     */
    @ApiModelProperty(value = "备份结束时间")
    private Date backupEndTime;

    /**
     * 备份结果
     */
    @ApiModelProperty(value = "备份结果")
    private String backupResult;

    /**
     * 备份id
     */
    @ApiModelProperty(value = "备份ID")
    private UUID backupId;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

    /**
     * 策略ID
     */
    @ApiModelProperty(value = "策略ID")
    private UUID strategyId;


    /**
     * 备份数据类型
     */
    @ApiModelProperty(value = "备份数据类型")
    private BackupType backupType;

    /**
     * 备份目录名称
     */
    @ApiModelProperty(value = "备份目录名称")
    private String backupCatalog;

    /**
     * 平台ID
     */
    @ApiModelProperty(value = "平台ID")
    private String platformId;

    /**
     * 详情
     */
    @ApiModelProperty(value = "详情")
    private String detail;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public TaskTypeEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypeEnum taskType) {
        this.taskType = taskType;
    }

    public BackupStateEnum getBackupState() {
        return backupState;
    }

    public void setBackupState(BackupStateEnum backupState) {
        this.backupState = backupState;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getExternalStorageName() {
        return externalStorageName;
    }

    public void setExternalStorageName(String externalStorageName) {
        this.externalStorageName = externalStorageName;
    }

    public Date getBackupBeginTime() {
        return backupBeginTime;
    }

    public void setBackupBeginTime(Date backupBeginTime) {
        this.backupBeginTime = backupBeginTime;
    }

    public Date getBackupEndTime() {
        return backupEndTime;
    }

    public void setBackupEndTime(Date backupEndTime) {
        this.backupEndTime = backupEndTime;
    }

    public String getBackupResult() {
        return backupResult;
    }

    public void setBackupResult(String backupResult) {
        this.backupResult = backupResult;
    }

    public UUID getBackupId() {
        return backupId;
    }

    public void setBackupId(UUID backupId) {
        this.backupId = backupId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public BackupType getBackupType() {
        return backupType;
    }

    public void setBackupType(BackupType backupType) {
        this.backupType = backupType;
    }

    public String getBackupCatalog() {
        return backupCatalog;
    }

    public void setBackupCatalog(String backupCatalog) {
        this.backupCatalog = backupCatalog;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
