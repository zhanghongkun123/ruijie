package com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 文件导出审批申请单
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public class AuditApplyDTO extends EqualsHashcodeSupport {

    private UUID id;

    /**
     * 申请单号
     */
    private String applySerialNumber;

    /**
     * 申请导出理由
     **/
    private String applyReason;

    /**
     * 导出文件总大小
     **/
    private Long totalFileSize;

    /**
     * 导出文件总数量
     **/
    private Integer totalFileCount;

    /**
     * 导出文件总页数
     **/
    private Integer totalFilePage;

    /**
     * 审计文件申请类型（导出、打印）
     */
    private AuditApplyTypeEnum applyType;

    /**
     * 状态
     */
    private AuditApplyStateEnum state;

    /**
     * 申请单关联的告警记录ID
     **/
    private String alarmIds;

    /**
     * 申请单关联用户ID
     **/
    private UUID userId;

    /**
     * 申请单关联用户名
     **/
    private String userName;

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
     * 失败原因
     */
    private String failReason;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 更新时间
     **/
    private Date updateTime;

    private Integer version;

    /**
     * 文件名列表
     */
    private String fileName;

    /**
     * 桌面池ID
     */
    private UUID desktopPoolId;

    /**
     * 桌面类型
     */
    private DesktopPoolType desktopPoolType;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApplySerialNumber() {
        return this.applySerialNumber;
    }

    public void setApplySerialNumber(String applySerialNumber) {
        this.applySerialNumber = applySerialNumber;
    }

    public String getApplyReason() {
        return this.applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public Long getTotalFileSize() {
        return this.totalFileSize;
    }

    public void setTotalFileSize(Long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public Integer getTotalFileCount() {
        return this.totalFileCount;
    }

    public void setTotalFileCount(Integer totalFileCount) {
        this.totalFileCount = totalFileCount;
    }

    public Integer getTotalFilePage() {
        return this.totalFilePage;
    }

    public void setTotalFilePage(Integer totalFilePage) {
        this.totalFilePage = totalFilePage;
    }

    public AuditApplyTypeEnum getApplyType() {
        return this.applyType;
    }

    public void setApplyType(AuditApplyTypeEnum applyType) {
        this.applyType = applyType;
    }

    public AuditApplyStateEnum getState() {
        return this.state;
    }

    public void setState(AuditApplyStateEnum state) {
        this.state = state;
    }

    public String getAlarmIds() {
        return alarmIds;
    }

    public void setAlarmIds(String alarmIds) {
        this.alarmIds = alarmIds;
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

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }
}
