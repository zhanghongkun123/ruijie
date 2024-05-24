package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.PrintStateEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * Description: 打印文件详细信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Entity
@Table(name = "t_rco_audit_file_print_info")
public class AuditFilePrintInfoEntity extends EqualsHashcodeSupport {

    @Id
    private UUID id;

    /**
     * 文件记录ID
     */
    private UUID fileId;

    /**
     * 打印机名称
     **/
    private String printerName;

    /**
     * 打印机品牌
     **/
    private String printerBrand;

    /**
     * 打印机型号
     **/
    private String printerModel;

    /**
     * 打印机序列码
     **/
    private String printerSn;

    /**
     * 打印进程名
     */
    private String printProcessName;

    /**
     * 打印页数
     **/
    private Integer printPageCount;

    /**
     * 打印纸张大小
     **/
    private String printPaperSize;

    /**
     * 打印时间
     **/
    private Date printTime;

    /**
     * 是否打印成功
     **/
    @Enumerated(EnumType.STRING)
    private PrintStateEnum printState;

    /**
     * 打印结果信息
     */
    private String printResultMsg;

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

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterBrand() {
        return printerBrand;
    }

    public void setPrinterBrand(String printerBrand) {
        this.printerBrand = printerBrand;
    }

    public String getPrinterModel() {
        return printerModel;
    }

    public void setPrinterModel(String printerModel) {
        this.printerModel = printerModel;
    }

    public String getPrinterSn() {
        return printerSn;
    }

    public void setPrinterSn(String printerSn) {
        this.printerSn = printerSn;
    }

    public String getPrintProcessName() {
        return this.printProcessName;
    }

    public void setPrintProcessName(String printProcessName) {
        this.printProcessName = printProcessName;
    }

    public Integer getPrintPageCount() {
        return this.printPageCount;
    }

    public void setPrintPageCount(Integer printPageCount) {
        this.printPageCount = printPageCount;
    }

    public String getPrintPaperSize() {
        return this.printPaperSize;
    }

    public void setPrintPaperSize(String printPaperSize) {
        this.printPaperSize = printPaperSize;
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

    public String getPrintResultMsg() {
        return this.printResultMsg;
    }

    public void setPrintResultMsg(String printResultMsg) {
        this.printResultMsg = printResultMsg;
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
