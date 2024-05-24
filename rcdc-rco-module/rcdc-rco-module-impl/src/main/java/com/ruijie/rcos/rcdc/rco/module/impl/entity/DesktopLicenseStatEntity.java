package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalLicenseTypeEnums;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * 云桌面授权使用情况统计表
 * <p>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2023/6/14  <br>
 *
 * @author zjy
 */
@Entity
@Table(name = "t_rco_desktop_license_stat")
public class DesktopLicenseStatEntity {

    @Id
    private UUID id;

    /**
     * 授权类型
     */
    @Enumerated(EnumType.STRING)
    private CbbTerminalLicenseTypeEnums licenseType;

    /**
     * 使用的授权总数
     */
    private Integer usedCount = 0;

    /**
     * 授权总数
     */
    private Integer totalCount = 0;

    /**
     * vdi使用的授权数
     */
    private Integer vdiUsedCount = 0;

    /**
     * idv使用的授权数
     */
    private Integer idvUsedCount = 0;

    /**
     * voi使用的授权数
     */
    private Integer voiUsedCount = 0;

    /**
     * rca使用的授权数
     */
    private Integer rcaUsedCount = 0;

    /**
     * eduVoi使用的授权数
     */
    private Integer eduVoiUsedCount = 0;

    /**
     * 创建时间
     */
    private Date createTime;

    @Version
    private int version;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CbbTerminalLicenseTypeEnums getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(CbbTerminalLicenseTypeEnums licenseType) {
        this.licenseType = licenseType;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Integer getVdiUsedCount() {
        return vdiUsedCount;
    }

    public void setVdiUsedCount(Integer vdiUsedCount) {
        this.vdiUsedCount = vdiUsedCount;
    }

    public Integer getIdvUsedCount() {
        return idvUsedCount;
    }

    public void setIdvUsedCount(Integer idvUsedCount) {
        this.idvUsedCount = idvUsedCount;
    }

    public Integer getVoiUsedCount() {
        return voiUsedCount;
    }

    public void setVoiUsedCount(Integer voiUsedCount) {
        this.voiUsedCount = voiUsedCount;
    }

    public Integer getRcaUsedCount() {
        return rcaUsedCount;
    }

    public void setRcaUsedCount(Integer rcaUsedCount) {
        this.rcaUsedCount = rcaUsedCount;
    }

    public Integer getEduVoiUsedCount() {
        return eduVoiUsedCount;
    }

    public void setEduVoiUsedCount(Integer eduVoiUsedCount) {
        this.eduVoiUsedCount = eduVoiUsedCount;
    }
}
