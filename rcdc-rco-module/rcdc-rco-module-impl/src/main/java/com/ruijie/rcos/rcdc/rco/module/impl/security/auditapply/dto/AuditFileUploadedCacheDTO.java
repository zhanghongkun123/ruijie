package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dto;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * Description: 文件上传完成缓存DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/6
 *
 * @author TD
 */
public class AuditFileUploadedCacheDTO {

    /**
     * 申请单ID
     */
    private UUID applyId;

    /**
     * 文件ID
     */
    private UUID fileId;

    /**
     * 执行接受器
     */
    private Future<?> future;

    /**
     * 入队时间
     */
    private Date entryTime = new Date();

    public AuditFileUploadedCacheDTO(UUID applyId, UUID fileId) {
        this.applyId = applyId;
        this.fileId = fileId;
    }

    public AuditFileUploadedCacheDTO(UUID applyId, UUID fileId, Future<?> future) {
        this.applyId = applyId;
        this.fileId = fileId;
        this.future = future;
    }

    public UUID getApplyId() {
        return applyId;
    }

    public void setApplyId(UUID applyId) {
        this.applyId = applyId;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }
}
