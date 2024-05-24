package com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto;


import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.AuditPrintLogStaticTypeEnum;

import java.util.UUID;

/**
 * Description: 文件导出审批申请单视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26
 *
 * @author WuShengQiang
 */
public class AuditPrintLogStaticDTO {

    private UUID userId;

    private String userName;

    private UUID groupId;

    private String groupName;

    private UUID desktopId;

    private String desktopName;

    private String terminalId;

    private String terminalName;

    private AuditPrintLogStaticTypeEnum staticType;

    private Long printPage;

    private Long printCount;

    private String printCountPercent;

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

    public AuditPrintLogStaticTypeEnum getStaticType() {
        return this.staticType;
    }

    public void setStaticType(AuditPrintLogStaticTypeEnum staticType) {
        this.staticType = staticType;
    }

    public Long getPrintPage() {
        return this.printPage;
    }

    public void setPrintPage(Long printPage) {
        this.printPage = printPage;
    }

    public Long getPrintCount() {
        return this.printCount;
    }

    public void setPrintCount(Long printCount) {
        this.printCount = printCount;
    }

    public String getPrintCountPercent() {
        return this.printCountPercent;
    }

    public void setPrintCountPercent(String printCountPercent) {
        this.printCountPercent = printCountPercent;
    }
}
