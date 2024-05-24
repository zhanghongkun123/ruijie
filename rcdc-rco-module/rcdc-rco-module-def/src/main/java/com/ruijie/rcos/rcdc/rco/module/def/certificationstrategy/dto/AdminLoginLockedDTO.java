package com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto;


/**
 * Description: 管理员被锁定时返回剩余锁定时间
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-07-13
 *
 * @author ryx
 */
public class AdminLoginLockedDTO {

    private Long remainLockTime;

    public AdminLoginLockedDTO(Long remainLockTime) {
        this.remainLockTime = remainLockTime;
    }

    public Long getRemainLockTime() {
        return remainLockTime;
    }

    public void setRemainLockTime(Long remainLockTime) {
        this.remainLockTime = remainLockTime;
    }
}
