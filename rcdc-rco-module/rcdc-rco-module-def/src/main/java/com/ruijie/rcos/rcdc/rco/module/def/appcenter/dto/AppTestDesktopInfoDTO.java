package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.ProgressStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月04日
 *
 * @author zhk
 */
public class AppTestDesktopInfoDTO extends PlatformBaseInfoDTO implements Serializable {

    /**
     * 桌面名称
     */
    private String desktopName;

    /**
     * 桌面ip
     */
    private String deskIp;

    /**
     * 桌面状态
     **/
    @Enumerated(EnumType.STRING)
    private CbbCloudDeskState deskState;

    /**
     * 桌面测试状态
     */
    @Enumerated(value = EnumType.STRING)
    private DesktopTestStateEnum testState;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 运行方式，个性：PERSONAL 还原：RECOVERABLE
     **/
    private CbbCloudDeskPattern pattern;

    /**
     * 测试id
     */
    private UUID testId;

    /**
     * 云桌面类型，VDI\IDV
     **/
    @Enumerated(EnumType.STRING)
    private CbbImageType deskType;

    /**
     * 桌面id
     */
    private UUID deskId;

    /**
     * 终端名称
     */
    private String terminalName;

    private String failReason;

    private String logFileName;

    /**
     * 测试任务状态
     */
    @Enumerated(EnumType.STRING)
    private TestTaskStateEnum testTaskState;

    /**
     * 终端状态
     */
    @Enumerated(EnumType.STRING)
    private CbbTerminalStateEnums terminalState;

    /**
     * 云桌面类型，VDI\IDV
     **/
    @Enumerated(EnumType.STRING)
    private CbbImageType cbbImageType;

    /**
     * 操作系统
     **/
    @Enumerated(EnumType.STRING)
    private CbbOsType osType;

    /**
     * 操作系统版本
     */
    private String osVersion;

    /**
     * 测试任务名称
     */
    private String testName;

    /**
     * 交付状态
     */
    @Enumerated(EnumType.STRING)
    private ProgressStatusEnum deliveryState;

    /**
     * 交付
     */
    private Date deliveryTime;

    /**
     * 创建时间
     */
    private Date createTime;


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

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    public DesktopTestStateEnum getTestState() {
        return testState;
    }

    public void setTestState(DesktopTestStateEnum testState) {
        this.testState = testState;
    }

    public CbbCloudDeskPattern getPattern() {
        return pattern;
    }

    public void setPattern(CbbCloudDeskPattern pattern) {
        this.pattern = pattern;
    }

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public CbbImageType getDeskType() {
        return deskType;
    }

    public void setDeskType(CbbImageType deskType) {
        this.deskType = deskType;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public TestTaskStateEnum getTestTaskState() {
        return testTaskState;
    }

    public void setTestTaskState(TestTaskStateEnum testTaskState) {
        this.testTaskState = testTaskState;
    }

    public CbbTerminalStateEnums getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(CbbTerminalStateEnums terminalState) {
        this.terminalState = terminalState;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public ProgressStatusEnum getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(ProgressStatusEnum deliveryState) {
        this.deliveryState = deliveryState;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
