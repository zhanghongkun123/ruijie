package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo;

import com.ruijie.rcos.rcdc.backup.module.def.enums.BackupType;
import com.ruijie.rcos.rcdc.backup.module.def.enums.MetaType;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月30日
 *
 * @author qiuzy
 */
@PageQueryDTOConfig(dmql = "v_backup_detail.dmql")
public class ViewBackupDetailVO {

    private UUID id;

    /**
     * 任务ID
     */
    private UUID backupId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源ID
     */
    private String resourceId;

    /**
     * 备份对象类型
     */
    @Enumerated(value = EnumType.STRING)
    private BackupType backupType;

    /**
     * 资源类型
     */
    @Enumerated(value = EnumType.STRING)
    private MetaType metaType;

    /**
     * 备份数量
     */
    private Integer backupNum;

    /**
     * 最后一次备份时间
     */
    private Date lastBackupTime;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 最后一次备份目录
     */
    private String externalStorageName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBackupId() {
        return backupId;
    }

    public void setBackupId(UUID backupId) {
        this.backupId = backupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public BackupType getBackupType() {
        return backupType;
    }

    public void setBackupType(BackupType backupType) {
        this.backupType = backupType;
    }

    public MetaType getMetaType() {
        return metaType;
    }

    public void setMetaType(MetaType metaType) {
        this.metaType = metaType;
    }

    public Integer getBackupNum() {
        return backupNum;
    }

    public void setBackupNum(Integer backupNum) {
        this.backupNum = backupNum;
    }

    public Date getLastBackupTime() {
        return lastBackupTime;
    }

    public void setLastBackupTime(Date lastBackupTime) {
        this.lastBackupTime = lastBackupTime;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getExternalStorageName() {
        return externalStorageName;
    }

    public void setExternalStorageName(String externalStorageName) {
        this.externalStorageName = externalStorageName;
    }
}
