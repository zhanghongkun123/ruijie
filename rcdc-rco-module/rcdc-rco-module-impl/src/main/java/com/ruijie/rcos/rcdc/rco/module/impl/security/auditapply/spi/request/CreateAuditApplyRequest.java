package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi.request;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 文件流转审计申请请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public class CreateAuditApplyRequest {

    /**
     *
     */
    @NotNull
    private UUID requestId;

    /**
     * 文件列表
     */
    @NotNull
    private List<AuditFileDTO> fileList;

    /**
     * 导出文件总大小
     **/
    @Nullable
    private Long totalFileSize;

    /**
     * 导出文件总数量
     **/
    @Nullable
    private Integer totalFileCount;

    /**
     * 导出文件总页数
     **/
    @Nullable
    private Integer totalFilePage;

    @Nullable
    private String applyReason;

    @Nullable
    private AuditApplyTypeEnum applyType;

    public UUID getRequestId() {
        return this.requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public List<AuditFileDTO> getFileList() {
        return this.fileList;
    }

    public void setFileList(List<AuditFileDTO> fileList) {
        this.fileList = fileList;
    }

    @Nullable
    public Long getTotalFileSize() {
        return this.totalFileSize;
    }

    public void setTotalFileSize(@Nullable Long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Nullable
    public Integer getTotalFileCount() {
        return this.totalFileCount;
    }

    public void setTotalFileCount(@Nullable Integer totalFileCount) {
        this.totalFileCount = totalFileCount;
    }

    @Nullable
    public Integer getTotalFilePage() {
        return this.totalFilePage;
    }

    public void setTotalFilePage(@Nullable Integer totalFilePage) {
        this.totalFilePage = totalFilePage;
    }

    @Nullable
    public AuditApplyTypeEnum getApplyType() {
        return this.applyType;
    }

    public void setApplyType(@Nullable AuditApplyTypeEnum applyType) {
        this.applyType = applyType;
    }

    @Nullable
    public String getApplyReason() {
        return this.applyReason;
    }

    public void setApplyReason(@Nullable String applyReason) {
        this.applyReason = applyReason;
    }
}
