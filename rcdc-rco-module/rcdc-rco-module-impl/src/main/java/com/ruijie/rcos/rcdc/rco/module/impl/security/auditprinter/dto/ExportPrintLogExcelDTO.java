package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dto;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.ViewAuditPrintLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.DateUtils;

/**
 * Description: 申请单导出表格每一行DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/4
 *
 * @author WuShengQiang
 */
public class ExportPrintLogExcelDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPrintLogExcelDTO.class);

    /**
     * 申请单号
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_APPLY_SERIAL_NUMBER)
    private String applySerialNumber;

    /**
     * 用户名
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_USER_NAME)
    private String userName;

    /**
     * 打印机名称
     */
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_PRINTER_NAME)
    private String printerName;

    /**
     * 打印结果
     */
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_PRINT_STATE)
    private String printState;

    /**
     * 打印页数
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_PRINT_PAGE_COUNT)
    private String printPageCount;

    /**
     * 打印时间
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_PRINT_TIME)
    private String printTime;

    /**
     * 申请单状态
     */
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_STATE)
    private String applyState;

    /**
     * 文件总数量
     */
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_TOTAL_FILE_COUNT)
    private String totalFileCount;

    /**
     * 文件总大小
     */
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_TOTAL_FILE_SIZE)
    private String totalFileSize;

    /**
     * 申请理由
     */
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_REASON)
    private String applyReason;

    /**
     * 终端名
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_TERMINAL_NAME)
    private String terminalName;

    /**
     * 终端IP
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_TERMINAL_IP)
    private String terminalIp;

    /**
     * 终端IP
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_TERMINAL_MAC)
    private String terminalMac;

    /**
     * 云桌面名
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_DESKTOP_NAME)
    private String desktopName;

    /**
     * 云桌面IP
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_DESKTOP_IP)
    private String desktopIp;

    /**
     * 云桌面MAC地址
     **/
    @ExcelHead(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_FIELD_DESKTOP_MAC)
    private String desktopMac;



    public ExportPrintLogExcelDTO() {

    }

    public ExportPrintLogExcelDTO(ViewAuditPrintLogDTO viewAuditPrintLogDTO) {
        Assert.notNull(viewAuditPrintLogDTO, "viewAuditPrintLogDTO is not null");
        this.userName = viewAuditPrintLogDTO.getUserName();
        this.printerName = viewAuditPrintLogDTO.getPrinterName();
        this.printTime =
                viewAuditPrintLogDTO.getPrintTime() != null ? DateUtils.format(viewAuditPrintLogDTO.getPrintTime(), DateUtils.NORMAL_DATE_FORMAT)
                        : StringUtils.EMPTY;
        this.printState = viewAuditPrintLogDTO.getPrintState().getMessage();
        this.printPageCount =
                viewAuditPrintLogDTO.getPrintPageCount() != null ? String.valueOf(viewAuditPrintLogDTO.getPrintPageCount()) : StringUtils.EMPTY;
        this.desktopName = viewAuditPrintLogDTO.getDesktopName();
        this.desktopIp = viewAuditPrintLogDTO.getDesktopIp();
        this.desktopMac = viewAuditPrintLogDTO.getDesktopMac();
        this.terminalName = viewAuditPrintLogDTO.getTerminalName();
        this.terminalIp = viewAuditPrintLogDTO.getTerminalIp();
        this.terminalMac = viewAuditPrintLogDTO.getTerminalMac();
        this.applySerialNumber = viewAuditPrintLogDTO.getApplySerialNumber();
        this.applyState = viewAuditPrintLogDTO.getApplyState().getMessage();
        this.totalFileCount = Optional.ofNullable(viewAuditPrintLogDTO.getTotalFileCount()).map(String::valueOf).orElse(StringUtils.EMPTY);
        this.totalFileSize = Optional.ofNullable(viewAuditPrintLogDTO.getTotalFileSize()).map(String::valueOf).orElse(StringUtils.EMPTY);
        this.applyReason = Optional.ofNullable(viewAuditPrintLogDTO.getApplyReason()).orElse(StringUtils.EMPTY);

        try {
            // 给属性为null的赋值为空字符串
            format();
        } catch (IllegalAccessException e) {
            LOGGER.error("格式化ExportPrintLogExcelDTO类失败，失败原因:", e);
        }
    }


    private void format() throws IllegalAccessException {
        Class<? extends ExportPrintLogExcelDTO> exportApplyClazz = this.getClass();
        Field[] fieldArr = exportApplyClazz.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (Objects.isNull(field.get(this))) {
                field.set(this, "");
            }
        }
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrinterName() {
        return this.printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrintState() {
        return this.printState;
    }

    public void setPrintState(String printState) {
        this.printState = printState;
    }

    public String getPrintPageCount() {
        return this.printPageCount;
    }

    public void setPrintPageCount(String printPageCount) {
        this.printPageCount = printPageCount;
    }

    public String getPrintTime() {
        return this.printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
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

    public String getDesktopName() {
        return this.desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getDesktopIp() {
        return this.desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
    }

    public String getDesktopMac() {
        return this.desktopMac;
    }

    public void setDesktopMac(String desktopMac) {
        this.desktopMac = desktopMac;
    }

    public String getApplySerialNumber() {
        return this.applySerialNumber;
    }

    public void setApplySerialNumber(String applySerialNumber) {
        this.applySerialNumber = applySerialNumber;
    }

    public String getApplyState() {
        return applyState;
    }

    public void setApplyState(String applyState) {
        this.applyState = applyState;
    }

    public String getTotalFileCount() {
        return totalFileCount;
    }

    public void setTotalFileCount(String totalFileCount) {
        this.totalFileCount = totalFileCount;
    }

    public String getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(String totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }
}
