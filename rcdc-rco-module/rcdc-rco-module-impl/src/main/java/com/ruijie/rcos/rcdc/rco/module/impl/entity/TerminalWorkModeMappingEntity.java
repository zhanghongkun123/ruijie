package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/30 15:08
 *
 * @author ketb
 */
@Entity
@Table(name = "t_rco_terminal_work_mode_mapping")
public class TerminalWorkModeMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String supportMode;


    private String workingMode;

    private String judgeBasis;

    private Boolean enableState;

    private Date createTime;

    private Date updateTime;

    private String matchRule;

    private String subMatchRule;

    @Version
    private Integer version;

    private String platform;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSupportMode() {
        return supportMode;
    }

    public void setSupportMode(String supportMode) {
        this.supportMode = supportMode;
    }

    public String getWorkingMode() {
        return workingMode;
    }

    public void setWorkingMode(String workingMode) {
        this.workingMode = workingMode;
    }

    public String getJudgeBasis() {
        return judgeBasis;
    }

    public void setJudgeBasis(String judgeBasis) {
        this.judgeBasis = judgeBasis;
    }

    public Boolean getEnableState() {
        return enableState;
    }

    public void setEnableState(Boolean enableState) {
        this.enableState = enableState;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    public String getSubMatchRule() {
        return subMatchRule;
    }

    public void setSubMatchRule(String subMatchRule) {
        this.subMatchRule = subMatchRule;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
