package com.ruijie.rcos.rcdc.rco.module.web.ctrl.authorization.dto;


import java.util.Date;

/**
 * 桌面授权统计返回信息
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/14
 *
 * @author zjy
 */
public class DesktopLicenseStatValueDTO {

    /**
     * 记录时间
     */
    private Date createTime;

    /**
     * 使用的授权总数
     */
    private Integer usedCount;

    /**
     * 授权总数
     */
    private Integer totalCount;

    /**
     * vdi使用的授权数
     */
    private Integer vdiUsedCount;

    /**
     * idv使用的授权数
     */
    private Integer idvUsedCount;

    /**
     * voi使用的授权数
     */
    private Integer voiUsedCount;

    /**
     * rca使用的授权数
     */
    private Integer rcaUsedCount;

    /**
     * eduVoi使用的授权数
     */
    private Integer eduVoiUsedCount;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
