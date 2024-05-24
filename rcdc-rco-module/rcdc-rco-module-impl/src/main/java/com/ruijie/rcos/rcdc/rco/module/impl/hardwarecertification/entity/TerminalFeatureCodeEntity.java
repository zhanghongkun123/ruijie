package com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 终端特征码关联表
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/03/29
 *
 * @author linke
 */
@Entity
@Table(name = "t_rco_terminal_feature_code")
public class TerminalFeatureCodeEntity {

    /**
     * 记录ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 终端ID
     */
    private String terminalId;

    /**
     * 特征码
     */
    private String featureCode;

    /**
     * 创建时间
     */
    private Date createTime;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
