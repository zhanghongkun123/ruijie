package com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月12日
 * 
 * @param <T> 定时任务数据
 * @param <R> 类型
 * @author xgx
 */
public class ScheduleDataDTO<T, R> extends EqualsHashcodeSupport {
    @Nullable
    private T[] userGroupArr;

    @Nullable
    private T[] userArr;

    @Nullable
    private T[] deskArr;

    @Nullable
    private T[] desktopPoolArr;

    @Nullable
    private T[] terminalGroupArr;

    @Nullable
    private R[] terminalArr;
    
    @Nullable
    private UUID platformId;

    @Nullable
    private String platformName;

    @Nullable
    private UUID extStorageId;

    @Nullable
    private UUID externalStorageId;

    @Nullable
    private String externalStorageName;

    @Nullable
    private Integer backupDuration;

    /**
     * 最大备份数
     */
    @Nullable
    private Integer maxBackUp;

    /**
     * 备份持续时长
     */
    @Nullable
    private Integer maxBackupDuration;

    /**
     * 是否允许取消
     */
    @Nullable
    private Boolean allowCancel;

    /**
     * 配置的数量
     */
    @Nullable
    private Integer amount;

    /**
     * 磁盘池
     */
    @Nullable
    private T[] diskPoolArr;

    /**
     * 磁盘id
     */
    @Nullable
    private T[] diskArr;

    public T[] getUserGroupArr() {
        return userGroupArr;
    }

    public void setUserGroupArr(T[] userGroupArr) {
        this.userGroupArr = userGroupArr;
    }

    public T[] getUserArr() {
        return userArr;
    }

    public void setUserArr(T[] userArr) {
        this.userArr = userArr;
    }

    public T[] getDeskArr() {
        return deskArr;
    }

    public void setDeskArr(T[] deskArr) {
        this.deskArr = deskArr;
    }

    public T[] getDesktopPoolArr() {
        return desktopPoolArr;
    }

    public void setDesktopPoolArr(T[] desktopPoolArr) {
        this.desktopPoolArr = desktopPoolArr;
    }

    public T[] getTerminalGroupArr() {
        return terminalGroupArr;
    }

    public void setTerminalGroupArr(T[] terminalGroupArr) {
        this.terminalGroupArr = terminalGroupArr;
    }

    @Nullable
    public R[] getTerminalArr() {
        return terminalArr;
    }

    public void setTerminalArr(@Nullable R[] terminalArr) {
        this.terminalArr = terminalArr;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }

    @Nullable
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(@Nullable String platformName) {
        this.platformName = platformName;
    }

    @Nullable
    public UUID getExtStorageId() {
        return extStorageId;
    }

    public void setExtStorageId(@Nullable UUID extStorageId) {
        this.extStorageId = extStorageId;
    }

    @Nullable
    public String getExternalStorageName() {
        return externalStorageName;
    }

    public void setExternalStorageName(@Nullable String externalStorageName) {
        this.externalStorageName = externalStorageName;
    }

    @Nullable
    public Integer getBackupDuration() {
        return backupDuration;
    }

    public void setBackupDuration(@Nullable Integer backupDuration) {
        this.backupDuration = backupDuration;
    }

    @Nullable
    public Integer getMaxBackUp() {
        return maxBackUp;
    }

    public void setMaxBackUp(@Nullable Integer maxBackUp) {
        this.maxBackUp = maxBackUp;
    }

    @Nullable
    public Integer getMaxBackupDuration() {
        return maxBackupDuration;
    }

    public void setMaxBackupDuration(@Nullable Integer maxBackupDuration) {
        this.maxBackupDuration = maxBackupDuration;
    }

    @Nullable
    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(@Nullable UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }

    @Nullable
    public Boolean getAllowCancel() {
        return allowCancel;
    }

    public void setAllowCancel(@Nullable Boolean allowCancel) {
        this.allowCancel = allowCancel;
    }

    @Nullable
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(@Nullable Integer amount) {
        this.amount = amount;
    }

    @Nullable
    public T[] getDiskPoolArr() {
        return diskPoolArr;
    }

    public void setDiskPoolArr(@Nullable T[] diskPoolArr) {
        this.diskPoolArr = diskPoolArr;
    }

    @Nullable
    public T[] getDiskArr() {
        return diskArr;
    }

    public void setDiskArr(@Nullable T[] diskArr) {
        this.diskArr = diskArr;
    }
}
