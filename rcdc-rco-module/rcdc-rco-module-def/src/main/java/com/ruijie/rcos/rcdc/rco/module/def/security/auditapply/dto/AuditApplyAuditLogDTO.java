package com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyAuditLogStateEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDTOConfig;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 文件导出审批审批流程日志
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@PageQueryDTOConfig(entityType = "AuditApplyAuditLogEntity")
public class AuditApplyAuditLogDTO extends EqualsHashcodeSupport {

    private UUID id;

    /**
     * 文件导出审批申请单ID
     **/
    private UUID applyId;

    /**
     * 审批人ID
     **/
    private UUID auditorId;

    /**
     * 审批人名
     **/
    private String auditorName;

    /**
     * 审批角色ID
     **/
    private UUID roleId;

    /**
     * 审批角色名
     **/
    private String roleName;

    /**
     * 审批层级
     **/
    private Integer auditorLevel;

    /**
     * 审批状态
     **/
    private AuditApplyAuditLogStateEnum auditorState;

    /**
     * 审批意见
     **/
    private String auditorOpinion;

    /**
     * 是否最后一个审批节点
     **/
    private Boolean isLastAuditor;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 更新时间
     **/
    private Date updateTime;

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

    public UUID getAuditorId() {
        return this.auditorId;
    }

    public void setAuditorId(UUID auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return this.auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public UUID getRoleId() {
        return this.roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getAuditorLevel() {
        return this.auditorLevel;
    }

    public void setAuditorLevel(Integer auditorLevel) {
        this.auditorLevel = auditorLevel;
    }

    public AuditApplyAuditLogStateEnum getAuditorState() {
        return this.auditorState;
    }

    public void setAuditorState(AuditApplyAuditLogStateEnum auditorState) {
        this.auditorState = auditorState;
    }

    public String getAuditorOpinion() {
        return this.auditorOpinion;
    }

    public void setAuditorOpinion(String auditorOpinion) {
        this.auditorOpinion = auditorOpinion;
    }

    public Boolean getIsLastAuditor() {
        return this.isLastAuditor;
    }

    public void setIsLastAuditor(Boolean lastAuditor) {
        this.isLastAuditor = lastAuditor;
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

}
