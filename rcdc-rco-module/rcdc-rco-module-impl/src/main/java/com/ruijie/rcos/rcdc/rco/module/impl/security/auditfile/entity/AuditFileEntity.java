package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.enums.AuditFileStateEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * Description: 文件流转审计关联文件
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Entity
@Table(name = "t_rco_audit_file")
public class AuditFileEntity extends EqualsHashcodeSupport {

    @Id
    private UUID id;

    /**
     * 申请导出理由
     **/
    private UUID applyId;

    /**
     * 文件名
     **/
    private String fileName;

    /**
     * 文件格式
     **/
    private String fileSuffix;

    /**
     * 文件md5
     **/
    private String fileMd5;


    /**
     * 文件大小
     **/
    private Long fileSize;

    /**
     * 文件页数
     **/
    private Integer filePage;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private AuditFileStateEnum fileState;

    /**
     * 文件在客户端存储位置
     **/
    private String fileClientStoragePath;


    /**
     * 文件在服务端临时路径
     **/
    private String fileServerTempPath;


    /**
     * 文件在服务端中的存放路径
     **/
    private String fileServerStoragePath;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 更新时间
     **/
    private Date updateTime;

    @Version
    private Integer version;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getApplyId() {
        return this.applyId;
    }

    public void setApplyId(UUID applyId) {
        this.applyId = applyId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSuffix() {
        return this.fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getFileMd5() {
        return this.fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public Integer getFilePage() {
        return this.filePage;
    }

    public void setFilePage(Integer filePage) {
        this.filePage = filePage;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public AuditFileStateEnum getFileState() {
        return this.fileState;
    }

    public void setFileState(AuditFileStateEnum fileState) {
        this.fileState = fileState;
    }

    public String getFileClientStoragePath() {
        return this.fileClientStoragePath;
    }

    public void setFileClientStoragePath(String fileClientStoragePath) {
        this.fileClientStoragePath = fileClientStoragePath;
    }

    public String getFileServerTempPath() {
        return this.fileServerTempPath;
    }

    public void setFileServerTempPath(String fileServerTempPath) {
        this.fileServerTempPath = fileServerTempPath;
    }

    public String getFileServerStoragePath() {
        return this.fileServerStoragePath;
    }

    public void setFileServerStoragePath(String fileServerStoragePath) {
        this.fileServerStoragePath = fileServerStoragePath;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
