package com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.enums.AuditFileStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.PrintStateEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.modulekit.api.ds.DataSource;
import com.ruijie.rcos.sk.modulekit.api.ds.DataSourceNames;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


/**
 * Description: 文件导出审批申请单视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26
 *
 * @author WuShengQiang
 */
@DataSource(DataSourceNames.DEFAULT_DATASOURCE_BEAN_NAME)
//@PageQueryDTOConfig(dmql = "v_rco_audit_print_log.dmql")
@PageQueryDTOConfig(entityType = "ViewAuditApplyPrintLogEntity")
public class ViewAuditPrintLogDTO {

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
     * 申请单状态
     */
    @Enumerated(EnumType.STRING)
    private AuditApplyStateEnum applyState;

    /**
     * 文件总数量
     */
    private Integer totalFileCount;

    /**
     * 文件总大小
     */
    private Long totalFileSize;

    /**
     * 申请理由
     */
    private String applyReason;

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
     * 申请单关联终端MAC
     **/
    private String terminalMac;

    /**
     * 申请单关联终端类型
     **/
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
    private String printPaperSize;

    /**
     * 打印时间
     **/
    private Date printTime;

    /**
     * 打印结果
     **/
    private PrintStateEnum printState;

    /**
     * 打印结果信息
     */
    private String printResultMsg;

    /**
     * 失败原因
     */
    private String failReason;


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

    public AuditApplyStateEnum getApplyState() {
        return applyState;
    }

    public void setApplyState(AuditApplyStateEnum applyState) {
        this.applyState = applyState;
    }

    public Integer getTotalFileCount() {
        return totalFileCount;
    }

    public void setTotalFileCount(Integer totalFileCount) {
        this.totalFileCount = totalFileCount;
    }

    public Long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(Long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
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

    public String getTerminalMac() {
        return this.terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
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

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
