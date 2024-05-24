package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbBackupFailureImageTemplateDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月27日
 *
 * @author zhanghongkun
 */
@ApiModel("备份详情")
public class BackupDetailVO implements Serializable {

    private static final long serialVersionUID = 4425531752005601359L;

    @ApiModelProperty(value = "备份id")
    private UUID id;

    /**
     * 备份目录
     */
    @ApiModelProperty(value = "备份目录")
    private String backupCatalog;

    /**
     * 备份时间
     **/
    @ApiModelProperty(value = "备份时间")
    private String backupTime;

    /**
     * 备份版本
     */
    @ApiModelProperty(value = "备份版本")
    private String backupVersion;

    /**
     * 外置存储名称
     */
    @ApiModelProperty(value = "外置存储名称")
    private String externalStorageName;

    /**
     * 外置存储id
     */
    @ApiModelProperty(value = "外置存储ID")
    private UUID externalStorageId;

    @ApiModelProperty(value = "备份成功镜像")
    private List<String> backupSuccessImageTemplateList;

    @ApiModelProperty(value = "备份失败镜像")
    private List<CbbBackupFailureImageTemplateDTO> backupFailureImageTemplateList;

    @ApiModelProperty(value = "备份成功应用磁盘")
    private List<String> backupSuccessAppSoftwarePackageList;

    @ApiModelProperty(value = "备份失败应用磁盘")
    private List<CbbBackupFailureImageTemplateDTO> backupFailureAppSoftwarePackageList;

    @ApiModelProperty(value = "备份服务器模式")
    private String serverModel;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBackupCatalog() {
        return backupCatalog;
    }

    public void setBackupCatalog(String backupCatalog) {
        this.backupCatalog = backupCatalog;
    }

    public String getBackupVersion() {
        return backupVersion;
    }

    public void setBackupVersion(String backupVersion) {
        this.backupVersion = backupVersion;
    }

    public String getExternalStorageName() {
        return externalStorageName;
    }

    public void setExternalStorageName(String externalStorageName) {
        this.externalStorageName = externalStorageName;
    }

    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }

    public String getBackupTime() {
        return backupTime;
    }

    public void setBackupTime(String backupTime) {
        this.backupTime = backupTime;
    }

    public List<String> getBackupSuccessImageTemplateList() {
        return backupSuccessImageTemplateList;
    }

    public void setBackupSuccessImageTemplateList(List<String> backupSuccessImageTemplateList) {
        this.backupSuccessImageTemplateList = backupSuccessImageTemplateList;
    }

    public String getServerModel() {
        return serverModel;
    }

    public void setServerModel(String serverModel) {
        this.serverModel = serverModel;
    }

    public List<CbbBackupFailureImageTemplateDTO> getBackupFailureImageTemplateList() {
        return backupFailureImageTemplateList;
    }

    public void setBackupFailureImageTemplateList(List<CbbBackupFailureImageTemplateDTO> backupFailureImageTemplateList) {
        this.backupFailureImageTemplateList = backupFailureImageTemplateList;
    }

    public List<String> getBackupSuccessAppSoftwarePackageList() {
        return backupSuccessAppSoftwarePackageList;
    }

    public void setBackupSuccessAppSoftwarePackageList(List<String> backupSuccessAppSoftwarePackageList) {
        this.backupSuccessAppSoftwarePackageList = backupSuccessAppSoftwarePackageList;
    }

    public List<CbbBackupFailureImageTemplateDTO> getBackupFailureAppSoftwarePackageList() {
        return backupFailureAppSoftwarePackageList;
    }

    public void setBackupFailureAppSoftwarePackageList(List<CbbBackupFailureImageTemplateDTO> backupFailureAppSoftwarePackageList) {
        this.backupFailureAppSoftwarePackageList = backupFailureAppSoftwarePackageList;
    }
}
