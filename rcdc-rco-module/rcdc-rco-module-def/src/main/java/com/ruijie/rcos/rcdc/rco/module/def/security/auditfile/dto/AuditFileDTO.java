package com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.enums.AuditFileStateEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 文件导出审批申请关联文件
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@PageQueryDTOConfig(entityType = "AuditFileEntity")
public class AuditFileDTO extends EqualsHashcodeSupport {

    private UUID id;

    /**
     * 申请导出理由
     **/
    private UUID applyId;

    /**
     * 文件名
     **/
    @NotNull
    private String fileName;

    /**
     * 文件格式
     **/
    @Nullable
    private String fileSuffix;

    /**
     * 文件md5
     **/
    @NotNull
    private String fileMd5;


    /**
     * 文件md5
     **/
    @NotNull
    private Long fileSize;

    /**
     * 文件页数
     **/
    @Nullable
    private Integer filePage;

    /**
     * 状态
     */
    private AuditFileStateEnum fileState;

    /**
     * 文件在客户端存储位置
     **/
    @Nullable
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

    @Nullable
    public String getFileSuffix() {
        return this.fileSuffix;
    }

    public void setFileSuffix(@Nullable String fileSuffix) {
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

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Nullable
    public Integer getFilePage() {
        return this.filePage;
    }

    public void setFilePage(@Nullable Integer filePage) {
        this.filePage = filePage;
    }

    public AuditFileStateEnum getFileState() {
        return this.fileState;
    }

    public void setFileState(AuditFileStateEnum fileState) {
        this.fileState = fileState;
    }

    @Nullable
    public String getFileClientStoragePath() {
        return this.fileClientStoragePath;
    }

    public void setFileClientStoragePath(@Nullable String fileClientStoragePath) {
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
