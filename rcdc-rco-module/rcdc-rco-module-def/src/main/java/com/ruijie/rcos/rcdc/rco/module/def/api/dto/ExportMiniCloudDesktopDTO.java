package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 
 * Description: 导出实体类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/17
 *
 * @author linrenjian
 */
public class ExportMiniCloudDesktopDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportMiniCloudDesktopDTO.class);


    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_NAME)
    private String desktopName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_USER_NAME)
    private String userName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_REAL_NAME)
    private String realName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_USER_TYPE)
    private String userType;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_STATE)
    private String desktopState;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_CATEGORY)
    private String desktopCategory;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CPU)
    private String cpu;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_MEMORY)
    private String memory;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_SYSTEM_DISK)
    private String systemDisk;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_PERSONAL_DISK)
    private String persionalDisk;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_TYPE)
    private String desktopType;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_IMAGE_NAME)
    private String imageName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DOWNLOAD_STATE)
    private String downloadState;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DOWNLOAD_FINISH_TIME)
    private String downloadFinishTime;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_IP)
    private String desktopIp;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_TERMINAL_IP)
    private String terminalIp;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_TERMINAL_NAME)
    private String terminalName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_PHYSICAL_SERVERIP)
    private String physicalServerIp;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_LATEST_LOGIN_TIME)
    private String latestLoginTime;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_COMPUTER_NAME)
    private String computerName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CREATE_TIME)
    private String createTime;


    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(String desktopState) {
        this.desktopState = desktopState;
    }

    public String getDesktopCategory() {
        return desktopCategory;
    }

    public void setDesktopCategory(String desktopCategory) {
        this.desktopCategory = desktopCategory;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(String systemDisk) {
        this.systemDisk = systemDisk;
    }

    public String getPersionalDisk() {
        return persionalDisk;
    }

    public void setPersionalDisk(String persionalDisk) {
        this.persionalDisk = persionalDisk;
    }

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDesktopIp() {
        return desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getPhysicalServerIp() {
        return physicalServerIp;
    }

    public void setPhysicalServerIp(String physicalServerIp) {
        this.physicalServerIp = physicalServerIp;
    }

    public String getLatestLoginTime() {
        return latestLoginTime;
    }

    public void setLatestLoginTime(String latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(String downloadState) {
        this.downloadState = downloadState;
    }

    public String getDownloadFinishTime() {
        return downloadFinishTime;
    }

    public void setDownloadFinishTime(String downloadFinishTime) {
        this.downloadFinishTime = downloadFinishTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
