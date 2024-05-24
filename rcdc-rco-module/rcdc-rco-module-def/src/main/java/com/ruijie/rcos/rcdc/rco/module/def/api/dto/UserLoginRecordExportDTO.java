package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;

/**
 * Description: 导出模型
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/01
 *
 * @author zjy
 */
public class UserLoginRecordExportDTO implements Comparable<UserLoginRecordExportDTO> {

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_USER_NAME)
    private String userName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_GROUP_NAME)
    private String userGroupName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_TERMINAL_NAME)
    private String terminalName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_TERMINAL_IP)
    private String terminalIp;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_TERMINAL_MAC)
    private String terminalMac;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_USER_RECORD_COMPUTER_NAME)
    private String computerName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_NAME)
    private String deskName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_TYPE)
    private String deskType;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_IP)
    private String deskIp;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_MAC)
    private String deskMac;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_USER_RECORD_IMAGE_NAME)
    private String deskImage;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_STRATEGY)
    private String deskStrategy;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESKTOP_SYSTEM)
    private String deskSystem;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_SESSION_STATE)
    private String sessionState;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_LOGIN_TIME)
    private String loginTime;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_AUTH_DURATION)
    private String authDuration;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CONNECT_TIME)
    private String connectTime;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CONNECT_DURATION)
    private String connectDuration;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_LOGOUT_TIME)
    private String logoutTime;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_USE_DURATION)
    private String useDuration;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getAuthDuration() {
        return authDuration;
    }

    public void setAuthDuration(String authDuration) {
        this.authDuration = authDuration;
    }

    public String getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(String connectTime) {
        this.connectTime = connectTime;
    }

    public String getConnectDuration() {
        return connectDuration;
    }

    public void setConnectDuration(String connectDuration) {
        this.connectDuration = connectDuration;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getUseDuration() {
        return useDuration;
    }

    public void setUseDuration(String useDuration) {
        this.useDuration = useDuration;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public String getDeskMac() {
        return deskMac;
    }

    public void setDeskMac(String deskMac) {
        this.deskMac = deskMac;
    }

    public String getDeskSystem() {
        return deskSystem;
    }

    public void setDeskSystem(String deskSystem) {
        this.deskSystem = deskSystem;
    }

    public String getDeskImage() {
        return deskImage;
    }

    public void setDeskImage(String deskImage) {
        this.deskImage = deskImage;
    }

    public String getDeskStrategy() {
        return deskStrategy;
    }

    public void setDeskStrategy(String deskStrategy) {
        this.deskStrategy = deskStrategy;
    }

    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    @Override
    public int compareTo(UserLoginRecordExportDTO o) {
        int compareLoginTime = o.getLoginTime().compareTo(this.loginTime);
        if (compareLoginTime != 0) {
            return compareLoginTime;
        }
        return o.getConnectTime().compareTo(this.connectTime);
    }
}
