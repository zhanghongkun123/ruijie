package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.enums.AuditFileStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.PrintStateEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;

/**
 * Description: 文件流转审计申请单视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26
 *
 * @author WuShengQiang
 */
@Entity
@Table(name = "v_rco_audit_print_log_static")
public class ViewAuditPrintLogEntity {

    @Id
    private UUID id;

    /**
     * 申请单UUID
     */
    private UUID applyId;

    /**
     * 申请单号
     */
    private String applySerialNumber;

    /**
     * 申请单关联用户ID
     **/
    private UUID userId;

    /**
     * 申请单关联用户名
     **/
    private String userName;

    /**
     * 申请单关联用户组ID
     **/
    private UUID groupId;

    /**
     * 申请单关联用户组名
     **/
    private String groupName;

    /**
     * 申请单关联云桌面ID
     **/
    private UUID desktopId;

    /**
     * 申请单关联云桌面名
     **/
    private String desktopName;

    /**
     * 申请单关联云桌面MAC地址
     **/
    private String desktopMac;

    /**
     * 申请单关联云桌面IP
     **/
    private String desktopIp;

    /**
     * 申请单关联终端ID
     **/
    private String terminalId;

    /**
     * 申请单关联终端名
     **/
    private String terminalName;

    /**
     * 申请单关联终端IP
     **/
    private String terminalIp;

    /**
     * 申请单关联终端类型
     **/
    @Enumerated(EnumType.STRING)
    private CbbTerminalTypeEnums terminalType;

    /**
     * 文件记录ID
     */
    private UUID fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件在服务端中的存放路径
     **/
    private String fileServerStoragePath;

    /**
     * 文件在服务端中的存放路径
     **/
    private AuditFileStateEnum fileState;


    /**
     * 打印机名称
     */
    private String printerName;

    /**
     * 打印进程名称
     **/
    private String printProcessName;

    /**
     * 打印页数
     **/
    private Integer printPageCount;

    /**
     * 打印纸张大小
     **/
    private Integer printPaperSize;

    /**
     * 打印时间
     **/
    private Date printTime;

    /**
     * 打印结果
     **/
    @Enumerated(EnumType.STRING)
    private PrintStateEnum printState;

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

    public String getApplySerialNumber() {
        return this.applySerialNumber;
    }

    public void setApplySerialNumber(String applySerialNumber) {
        this.applySerialNumber = applySerialNumber;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getGroupId() {
        return this.groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public UUID getDesktopId() {
        return this.desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public String getDesktopName() {
        return this.desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getDesktopMac() {
        return this.desktopMac;
    }

    public void setDesktopMac(String desktopMac) {
        this.desktopMac = desktopMac;
    }

    public String getDesktopIp() {
        return this.desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
    }

    public String getTerminalId() {
        return this.terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return this.terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalIp() {
        return this.terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public CbbTerminalTypeEnums getTerminalType() {
        return this.terminalType;
    }

    public void setTerminalType(CbbTerminalTypeEnums terminalType) {
        this.terminalType = terminalType;
    }

    public UUID getFileId() {
        return this.fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileServerStoragePath() {
        return this.fileServerStoragePath;
    }

    public void setFileServerStoragePath(String fileServerStoragePath) {
        this.fileServerStoragePath = fileServerStoragePath;
    }

    public AuditFileStateEnum getFileState() {
        return this.fileState;
    }

    public void setFileState(AuditFileStateEnum fileState) {
        this.fileState = fileState;
    }

    public String getPrinterName() {
        return this.printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
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

    public Integer getPrintPaperSize() {
        return this.printPaperSize;
    }

    public void setPrintPaperSize(Integer printPaperSize) {
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

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
