package com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Version;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.PrintStateEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;
import org.springframework.lang.Nullable;

/**
 * Description: 文件导出审批申请单
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@PageQueryDTOConfig(entityType = "AuditFilePrintInfoEntity")
public class AuditFilePrintInfoDTO extends EqualsHashcodeSupport {

    private UUID id;

    /**
     * 文件记录ID
     */
    @NotNull
    private UUID fileId;

    /**
     * 打印机记录ID（添加打印机记录ID）
     */
    @Nullable
    private UUID printerId;

    /**
     * 打印机名称
     **/
    @Nullable
    private String printerName;

    /**
     * 打印机品牌
     **/
    @Nullable
    private String printerBrand;

    /**
     * 打印机型号
     **/
    @Nullable
    private String printerModel;

    /**
     * 打印机序列码
     **/
    @Nullable
    private String printerSn;

    /**
     * 打印进程名
     */
    @Nullable
    private String printProcessName;

    /**
     * 打印页数
     **/
    @NotNull
    private Integer printPageCount;

    /**
     * 打印纸张大小
     **/
    @Nullable
    private String printPaperSize;

    /**
     * 打印结果信息
     */
    @Nullable
    private String printResultMsg;

    /**
     * 打印时间
     **/
    @NotNull
    private Date printTime;

    /**
     * 是否打印成功
     **/
    @NotNull
    private PrintStateEnum printState;

    /**
     * 打印机记录创建时间
     **/
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @Version
    private Integer version;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFileId() {
        return this.fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    @Nullable
    public UUID getPrinterId() {
        return this.printerId;
    }

    public void setPrinterId(@Nullable UUID printerId) {
        this.printerId = printerId;
    }

    @Nullable
    public String getPrinterName() {
        return this.printerName;
    }

    public void setPrinterName(@Nullable String printerName) {
        this.printerName = printerName;
    }

    @Nullable
    public String getPrinterBrand() {
        return this.printerBrand;
    }

    public void setPrinterBrand(@Nullable String printerBrand) {
        this.printerBrand = printerBrand;
    }

    @Nullable
    public String getPrinterModel() {
        return this.printerModel;
    }

    public void setPrinterModel(@Nullable String printerModel) {
        this.printerModel = printerModel;
    }

    @Nullable
    public String getPrinterSn() {
        return this.printerSn;
    }

    public void setPrinterSn(@Nullable String printerSn) {
        this.printerSn = printerSn;
    }

    @Nullable
    public String getPrintProcessName() {
        return this.printProcessName;
    }

    public void setPrintProcessName(@Nullable String printProcessName) {
        this.printProcessName = printProcessName;
    }

    public Integer getPrintPageCount() {
        return this.printPageCount;
    }

    public void setPrintPageCount(Integer printPageCount) {
        this.printPageCount = printPageCount;
    }

    @Nullable
    public String getPrintPaperSize() {
        return this.printPaperSize;
    }

    public void setPrintPaperSize(@Nullable String printPaperSize) {
        this.printPaperSize = printPaperSize;
    }

    @Nullable
    public String getPrintResultMsg() {
        return this.printResultMsg;
    }

    public void setPrintResultMsg(@Nullable String printResultMsg) {
        this.printResultMsg = printResultMsg;
    }

    public Date getPrintTime() {
        return this.printTime;
    }

    public void setPrintTime(Date printTime) {
        this.printTime = printTime;
    }

    public PrintStateEnum getPrintState() {
        return this.printState;
    }

    public void setPrintState(PrintStateEnum printState) {
        this.printState = printState;
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
