package com.ruijie.rcos.rcdc.rco.module.def.mtool.dto;

import java.util.Date;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.12
 *
 * @author linhj
 */
public class SyncUpgradeDTO {

    // 迁移流程状态
    private SyncUpgradeStatus status;

    // 终端处理状态
    private Integer shineCode;

    // 更新时间
    private Date updateDate;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SyncUpgradeStatus getStatus() {
        return status;
    }

    public void setStatus(SyncUpgradeStatus status) {
        this.status = status;
    }

    public Integer getShineCode() {
        return shineCode;
    }

    public void setShineCode(Integer shineCode) {
        this.shineCode = shineCode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Description: 旧平台迁移终端升级状态
     * Copyright: Copyright (c) 2022
     * Company: Ruijie Co., Ltd.
     * Create Time: 2022.04.12
     *
     * @author linhj
     */
    public enum SyncUpgradeStatus {
        STATUS_IMPORT,
        STATUS_INIT,
        STATUS_DEAL
    }
}
