package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dto;

import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ViewAuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.DateUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Description: 申请单导出表格每一行DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/4
 *
 * @author WuShengQiang
 */
public class ExportAuditApplyExcelDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportAuditApplyExcelDTO.class);

    /**
     * 申请单号
     */
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_SERIAL_NUMBER)
    private String applySerialNumber;

    /**
     * 申请单关联用户名
     **/
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_USER_NAME)
    private String userName;

    /**
     * 创建时间
     **/
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_CREATE_TIME)
    private String createTime;

    /**
     * 更新时间
     **/
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_UPDATE_TIME)
    private String updateTime;

    /**
     * 状态
     */
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_STATE)
    private String state;

    /**
     * 导出文件总数量
     **/
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_TOTAL_FILE_COUNT)
    private String totalFileCount;

    /**
     * 导出文件总大小
     **/
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_TOTAL_FILE_SIZE)
    private String totalFileSize;

    /**
     * 申请导出理由
     **/
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_REASON)
    private String applyReason;

    /**
     * 申请单关联云桌面名
     **/
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_DESKTOP_NAME)
    private String desktopName;

    /**
     * 申请单关联云桌面IP
     **/
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_DESKTOP_IP)
    private String desktopIp;

    /**
     * 申请单关联云桌面MAC地址
     **/
    @ExcelHead(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_DESKTOP_MAC)
    private String desktopMac;

    public ExportAuditApplyExcelDTO() {
    }

    public ExportAuditApplyExcelDTO(ViewAuditApplyDTO viewAuditApplyDTO) {
        Assert.notNull(viewAuditApplyDTO, "auditFileApplyViewDTO is not null");
        this.applySerialNumber = viewAuditApplyDTO.getApplySerialNumber();
        this.userName = viewAuditApplyDTO.getUserName();
        this.createTime = DateUtils.format(viewAuditApplyDTO.getCreateTime(), DateUtils.NORMAL_DATE_FORMAT);
        this.updateTime = DateUtils.format(viewAuditApplyDTO.getUpdateTime(), DateUtils.NORMAL_DATE_FORMAT);
        this.state = viewAuditApplyDTO.getState().getMessage();
        this.totalFileCount = String.valueOf(viewAuditApplyDTO.getTotalFileCount());
        this.totalFileSize = CapacityUnitUtils.dynamicChange(new BigDecimal(viewAuditApplyDTO.getTotalFileSize()));
        this.applyReason = viewAuditApplyDTO.getApplyReason();
        this.desktopName = viewAuditApplyDTO.getDesktopName();
        this.desktopIp = viewAuditApplyDTO.getDesktopIp();
        this.desktopMac = viewAuditApplyDTO.getDesktopMac();

        try {
            // 给属性为null的赋值为空字符串
            format();
        } catch (IllegalAccessException e) {
            LOGGER.error("格式化ExportAuditFileApplyDTO类失败，失败原因:", e);
        }
    }

    public ExportAuditApplyExcelDTO(AuditApplyDTO auditApplyDTO) {
        Assert.notNull(auditApplyDTO, "auditApplyDTO is not null");
        this.applySerialNumber = auditApplyDTO.getApplySerialNumber();
        this.userName = auditApplyDTO.getUserName();
        this.createTime = DateUtils.format(auditApplyDTO.getCreateTime(), DateUtils.NORMAL_DATE_FORMAT);
        this.updateTime = DateUtils.format(auditApplyDTO.getUpdateTime(), DateUtils.NORMAL_DATE_FORMAT);
        this.state = auditApplyDTO.getState().getMessage();
        this.totalFileCount = String.valueOf(auditApplyDTO.getTotalFileCount());
        this.totalFileSize = CapacityUnitUtils.dynamicChange(new BigDecimal(auditApplyDTO.getTotalFileSize()));
        this.applyReason = auditApplyDTO.getApplyReason();
        this.desktopName = auditApplyDTO.getDesktopName();
        this.desktopIp = auditApplyDTO.getDesktopIp();
        this.desktopMac = auditApplyDTO.getDesktopMac();

        try {
            // 给属性为null的赋值为空字符串
            format();
        } catch (IllegalAccessException e) {
            LOGGER.error("格式化ExportAuditFileApplyDTO类失败，失败原因:", e);
        }
    }

    private void format() throws IllegalAccessException {
        Class<? extends ExportAuditApplyExcelDTO> exportApplyClazz = this.getClass();
        Field[] fieldArr = exportApplyClazz.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (Objects.isNull(field.get(this))) {
                field.set(this, "");
            }
        }
    }

    public String getApplySerialNumber() {
        return applySerialNumber;
    }

    public void setApplySerialNumber(String applySerialNumber) {
        this.applySerialNumber = applySerialNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getDesktopIp() {
        return desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
    }

    public String getDesktopMac() {
        return desktopMac;
    }

    public void setDesktopMac(String desktopMac) {
        this.desktopMac = desktopMac;
    }
}
