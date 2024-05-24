package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * Description: 文件导出审批申请单视图
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/19
 *
 * @author jarman
 */
@Entity
@Table(name = "v_rco_audit_apply")
public class ViewAuditApplyEntity extends EqualsHashcodeSupport {

    @Id
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
    @Enumerated(EnumType.STRING)
    private AuditApplyTypeEnum applyType;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
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
    @Enumerated(EnumType.STRING)
    private CbbTerminalTypeEnums terminalType;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 更新时间
     **/
    private Date updateTime;

    /**
     * 文件名,以','分隔
     */
    private String fileName;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 申请单关联用户组ID
     **/
    private UUID groupId;

    /**
     * 申请单关联用户组名
     **/
    private String groupName;

    /**
     * 桌面池ID
     */
    private UUID desktopPoolId;

    /**
     * 桌面类型
     */
    @Enumerated(EnumType.STRING)
    private DesktopPoolType desktopPoolType;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApplySerialNumber() {
        return applySerialNumber;
    }

    public void setApplySerialNumber(String applySerialNumber) {
        this.applySerialNumber = applySerialNumber;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public Long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(Long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public Integer getTotalFileCount() {
        return totalFileCount;
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
        return state;
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
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getDesktopMac() {
        return desktopMac;
    }

    public void setDesktopMac(String desktopMac) {
        this.desktopMac = desktopMac;
    }

    public String getDesktopIp() {
        return desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalIp() {
        return terminalIp;
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
        return terminalType;
    }

    public void setTerminalType(CbbTerminalTypeEnums terminalType) {
        this.terminalType = terminalType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
