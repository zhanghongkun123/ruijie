package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalLicenseTypeEnums;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * 云桌面授权使用情况统计DTO
 * <p>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2023/6/14  <br>
 *
 * @author zjy
 */
public class DesktopLicenseStatDTO {

    /**
     * 使用的授权总数
     */
    @JSONField(name = "used_count")
    private Integer usedCount;

    /**
     * 授权总数
     */
    @JSONField(name = "total_count")
    private Integer totalCount;

    /**
     * vdi使用的授权数
     */
    @JSONField(name = "vdi_used_count")
    private Integer vdiUsedCount;

    /**
     * idv使用的授权数
     */
    @JSONField(name = "idv_used_count")
    private Integer idvUsedCount;

    /**
     * voi使用的授权数
     */
    @JSONField(name = "voi_used_count")
    private Integer voiUsedCount;

    /**
     * rca使用的授权数
     */
    @JSONField(name = "rca_used_count")
    private Integer rcaUsedCount;

    /**
     * eduVoi使用的授权数
     */
    @JSONField(name = "edu_voi_used_count")
    private Integer eduVoiUsedCount;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private Date createTime;


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
